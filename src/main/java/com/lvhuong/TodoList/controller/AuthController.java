package com.lvhuong.TodoList.controller;

import com.lvhuong.TodoList.domains.dto.LoginDto;
import com.lvhuong.TodoList.domains.dto.SignUpDto;
import com.lvhuong.TodoList.domains.entities.RoleEntity;
import com.lvhuong.TodoList.domains.entities.UserEntity;
import com.lvhuong.TodoList.repositories.RoleRepository;
import com.lvhuong.TodoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/login")
    public ResponseEntity<String> authenticateUser(
            @RequestBody LoginDto loginDto
    ){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(), passwordEncoder.encode(loginDto.getPassword())));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @RequestBody SignUpDto signUpDto
    ){
        if(userRepository.existsByEmail(signUpDto.getUsername())){
            return new ResponseEntity<>("Username already exist", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Emailed already exist", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                .name(signUpDto.getName())
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        RoleEntity roles = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User register successfully", HttpStatus.OK);
    }
}
