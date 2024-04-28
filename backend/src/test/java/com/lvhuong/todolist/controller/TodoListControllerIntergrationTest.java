package com.lvhuong.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.lvhuong.todolist.TestDataUtil;
import com.lvhuong.todolist.config.UserAuthenticationProvider;
import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.entities.RoleEntity;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.UserEntity;
import com.lvhuong.todolist.repositories.RoleRepository;
import com.lvhuong.todolist.repositories.TodoListRepository;
import com.lvhuong.todolist.repositories.UserRepository;
import com.lvhuong.todolist.services.TodoListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TodoListControllerIntergrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TodoListRepository testListRepo;
    private final UserAuthenticationProvider userAuthentication;
    private final RoleRepository roleTest;
    private final UserRepository userRepo;


    @Autowired
    public TodoListControllerIntergrationTest(MockMvc mockMvc, ObjectMapper objectMapper, TodoListRepository testListRepo, UserAuthenticationProvider userAuthentication, RoleRepository roleTest, UserRepository userRepo) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.testListRepo = testListRepo;
        this.userAuthentication = userAuthentication;
        this.roleTest = roleTest;
        this.userRepo = userRepo;
    }

    @Test
    public void testAddNewTodoListFailWithoutBearerToken() throws Exception {
        TodoListDto listDto = TestDataUtil.CreateTodoListDto();
        String listJson = objectMapper.writeValueAsString(listDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(listJson)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testAddNewTodoListFailWithExpiredBearerToken() throws Exception {
        TodoListDto listDto = TestDataUtil.CreateTodoListDto();
        String listJson = objectMapper.writeValueAsString(listDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ1c2VyVGVzdCIsImV4cCI6MTcxNDMyMzc3MCwiaWF0IjoxNzE0MjM3MzcwfQ.MqvQ5mgzjhdwzp0i53j4Z_GLsoERhsjRwAr9Y_DCud0")
                        .content(listJson)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testAddNewTodoListSuccessfully() throws Exception {
        TodoListDto listDto = TestDataUtil.CreateTodoListDto();
        String listJson = objectMapper.writeValueAsString(listDto);
        String token = GetAuthorization();

        assertThat(token).isNotEmpty();
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/todo-list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(listJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(listDto.getTitle())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.listItems.length()").value(2)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.listItems[0].description").value("Planned action A")
                );
    }

    @Test
    public void testUpdateTodoListSuccessfully() throws Exception {
        TodoListDto listDto = TestDataUtil.CreateTodoListDto();
        String listJson = objectMapper.writeValueAsString(listDto);
        String token = GetAuthorization();

        assertThat(token).isNotEmpty();
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(listJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        listDto.setId(Long.parseLong(JsonPath.parse(response).read("$.id").toString()));
        listDto.setTitle("New list title");
        listJson = objectMapper.writeValueAsString(listDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(listJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(listDto.getTitle())
        );
    }

    // We're not inserting todoList here so expect an empty response
    @Test
    public void testFindAllTodoListsEmpty() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 5);
        String pageRequestJson = objectMapper.writeValueAsString(pageRequest);
        String token = GetAuthorization();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(pageRequestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content").isEmpty()
        );
    }

    @Test
    public void testFindAllTodoList() throws Exception {
        UserEntity userA = userRepo.findByUsername("userTest").get();

        TodoList todoListA = TestDataUtil.createTodoListA(userA);
        testListRepo.save(todoListA);

        TodoList todoListB = TestDataUtil.createTodoListB(userA);
        testListRepo.save(todoListB);

        TodoList todoListC = TestDataUtil.createTodoListC(userA);
        testListRepo.save(todoListC);

        TodoList todoListD = TestDataUtil.createTodoListC(userA);
        testListRepo.save(todoListA);

        // Create 4 lists and expect 3
        PageRequest pageRequest = PageRequest.of(0, 3);
        String pageRequestJson = objectMapper.writeValueAsString(pageRequest);
        String token = GetAuthorization();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(pageRequestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content.length()").value(3)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value(todoListA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[1].title").value(todoListB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[2].title").value(todoListC.getTitle())
        );
    }

    @Test
    public void testFindOneTodoListsEmpty() throws Exception {
        String token = GetAuthorization();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/todo-list/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testFindOneTodoList() throws Exception {
        UserEntity userA = userRepo.findByUsername("userTest").get();

        TodoList todoListA = TestDataUtil.createTodoListA(userA);
        TodoList savedListA = testListRepo.save(todoListA);

        TodoList todoListB = TestDataUtil.createTodoListB(userA);
        testListRepo.save(todoListB);

        String token = GetAuthorization();


        mockMvc.perform(
                MockMvcRequestBuilders.get("/todo-list/" +  savedListA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(todoListA.getTitle())
        );
    }

    @Test
    public void testSearchForTodoListNotDeleted() throws Exception {
        UserEntity userA = userRepo.findByUsername("userTest").get();

        TodoList todoListA = TestDataUtil.createTodoListA(userA);
        TodoList savedListA = testListRepo.save(todoListA);

        TodoList todoListB = TestDataUtil.createTodoListB(userA);
        TodoList savedListB = testListRepo.save(todoListB);

        TodoList todoListC = TestDataUtil.createTodoListC(userA);
        TodoList savedListC = testListRepo.save(todoListC);

        TodoList todoListD = TestDataUtil.createTodoListC(userA);
        todoListD.setDeleted(true);
        TodoList savedListD = testListRepo.save(todoListA);

        // 1 list is deleted so expect 3
        PageRequest pageRequest = PageRequest.of(0, 5);
        String pageRequestJson = objectMapper.writeValueAsString(pageRequest);
        String token = GetAuthorization();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(pageRequestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content.length()").value(3)
        );
    }

    @Test
    public void testDeleteTodoList() throws Exception {
        TodoListDto listDto = TestDataUtil.CreateTodoListDto();
        String listJson = objectMapper.writeValueAsString(listDto);
        String token = GetAuthorization();

        assertThat(token).isNotEmpty();
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("/todo-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(listJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        listDto.setId(Long.parseLong(JsonPath.parse(response).read("$.id").toString()));
        listDto.setTitle("New list title");
        listJson = objectMapper.writeValueAsString(listDto);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/todo-list/" + listDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testDeleteNonexistentTodoList() throws Exception {
        String token = GetAuthorization();
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/todo-list/598174981")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    private String GetAuthorization() {
        // inserted this user in data.sql
        return userAuthentication.createToken("userTest");
    }
}
