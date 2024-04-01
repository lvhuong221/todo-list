package com.lvhuong.todolist.controller;

import com.lvhuong.todolist.config.UserAuthenticationProvider;
import com.lvhuong.todolist.domains.dto.CredentialsDto;
import com.lvhuong.todolist.domains.dto.SignUpDto;
import com.lvhuong.todolist.domains.dto.UserDto;
import com.lvhuong.todolist.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto){
        UserDto userDto = userService.login(credentialsDto);

        userDto.setToken(userAuthProvider.createToken(userDto.getUsername()));
        userDto.setPassword("");

        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        UserDto userDto = userService.register(signUpDto);
        userDto.setToken(userAuthProvider.createToken(userDto.getUsername()));

        return ResponseEntity.created(URI.create("/users/"+userDto.getId())).body(userDto);
    }
}
