package com.lvhuong.todolist.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TodoListDto {

    private Long id;

    private String title;

    private List<TodoListItemDto> listItems;

    private boolean isDeleted;

}
