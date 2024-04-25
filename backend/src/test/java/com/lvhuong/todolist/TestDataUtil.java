package com.lvhuong.todolist;

import com.lvhuong.todolist.domains.dto.CredentialsDto;
import com.lvhuong.todolist.domains.dto.SignUpDto;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.UserEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class TestDataUtil {

    public static UserEntity createUserA(){
        return UserEntity.builder()
                .id(1L)
                .username("userA")
                .password("password")
                .firstName("user")
                .lastName("Ay")
                .isDeleted(false)
                .build();
    }
    public static UserEntity createUserB(){
        return UserEntity.builder()
                .id(2L)
                .username("userB")
                .password("password")
                .firstName("user")
                .lastName("Bee")
                .isDeleted(false)
                .build();
    }
    public static UserEntity createUserC(){
        return UserEntity.builder()
                .id(3L)
                .username("userC")
                .password("password")
                .firstName("user")
                .lastName("See")
                .isDeleted(false)
                .build();
    }


    public static TodoList createTodoListA(UserEntity user) {
        return TodoList.builder()
                .id(1L)
                .user(user)
                .title("Todo list A")
                .build();
    }

    public static TodoList createTodoListB(UserEntity user) {
        return TodoList.builder()
                .id(2L)
                .user(user)
                .title("Todo list B")
                .build();
    }

    public static TodoList createTodoListC(UserEntity user) {
        return TodoList.builder()
                .id(3L)
                .user(user)
                .title("Todo list C")
                .build();
    }

    public static SignUpDto createSignUpData(){
        return SignUpDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .password("password".toCharArray())
                .build();
    }

    public static CredentialsDto createLoginCredential() {
        return CredentialsDto.builder()
                .username("johndoe")
                .password("password".toCharArray())
                .build();
    }
}
