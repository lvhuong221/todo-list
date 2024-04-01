package com.lvhuong.todolist.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignUpDto {
    private String firstName;
    private String lastName;
    private String username;
    private char[] password;
}
