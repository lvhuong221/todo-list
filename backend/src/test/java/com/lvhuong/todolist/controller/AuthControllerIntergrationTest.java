package com.lvhuong.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvhuong.todolist.TestDataUtil;
import com.lvhuong.todolist.domains.dto.CredentialsDto;
import com.lvhuong.todolist.domains.dto.SignUpDto;
import com.lvhuong.todolist.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerIntergrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthControllerIntergrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatRegisterSuccessfully() throws Exception {
        SignUpDto signUpDto = TestDataUtil.createSignUpData();
        String signUpJson = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signUpJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.username").value("johndoe")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName").value("John")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.lastName").value("Doe")
                );
    }

    @Test
    public void testResgisterAndLoginSuccessfully() throws Exception {
        // Must have signUpDto and credentialDto match in values
        SignUpDto signUpDto = TestDataUtil.createSignUpData();
        String signUpJson = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signUpJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.username").value("johndoe")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName").value("John")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.lastName").value("Doe")
                );

        CredentialsDto credentialDto = TestDataUtil.createLoginCredential();
        String loginJson = objectMapper.writeValueAsString(credentialDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("johndoe")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value("John")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value("Doe")
        );
    }

    @Test
    public void testLoginWithWrongCredential() throws Exception {
        // Login with non-existent account
        CredentialsDto credentialDto = TestDataUtil.createLoginCredential();
        String loginJson = objectMapper.writeValueAsString(credentialDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}
