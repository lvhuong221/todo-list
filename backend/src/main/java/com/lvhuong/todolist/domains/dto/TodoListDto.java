package com.lvhuong.todolist.domains.dto;

import com.lvhuong.todolist.domains.entities.TodoListItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TodoListDto {

    private Long id;

    private String title;

    private ArrayList<TodoListItemDto> listItems;
}
