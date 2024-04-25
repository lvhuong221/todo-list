package com.lvhuong.todolist.services;

import com.lvhuong.todolist.domains.dto.CredentialsDto;
import com.lvhuong.todolist.domains.dto.SignUpDto;
import com.lvhuong.todolist.domains.dto.UserDto;
import com.lvhuong.todolist.domains.entities.RoleEntity;
import com.lvhuong.todolist.domains.entities.UserEntity;
import com.lvhuong.todolist.exceptions.AppException;
import com.lvhuong.todolist.mappers.Mapper;
import com.lvhuong.todolist.repositories.RoleRepository;
import com.lvhuong.todolist.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDto findByLogin(String login){
        UserEntity user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unkown user", HttpStatus.NOT_FOUND));
        return userMapper.mapTo(user);
    }

    public UserDto login(CredentialsDto credentialsDto){
        UserEntity user = userRepository.findByUsername(credentialsDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())){
            return userMapper.mapTo(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto){
        Optional<UserEntity> foundUser = userRepository.findByUsername(userDto.getUsername());
        if(foundUser.isPresent()){
            throw new AppException("Username is taken", HttpStatus.BAD_REQUEST);
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())))

                .build();

        RoleEntity roles = roleRepository.findByName("ROLE_USER").get();
        newUser.setRoles(Collections.singleton(roles));

        UserEntity savedUser = userRepository.save(newUser);

        return userMapper.mapTo(savedUser);
    }

    public UserDto getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDto) auth.getPrincipal();
    }

}

