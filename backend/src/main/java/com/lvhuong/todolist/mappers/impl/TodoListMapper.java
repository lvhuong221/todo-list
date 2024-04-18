package com.lvhuong.todolist.mappers.impl;

import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TodoListMapper implements Mapper<TodoList, TodoListDto> {
    private final ModelMapper modelMapper;

    @Override
    public TodoListDto mapTo(TodoList todoList) {
        return modelMapper.map(todoList, TodoListDto.class);
    }

    @Override
    public TodoList mapFrom(TodoListDto todoListDto) {
        return modelMapper.map(todoListDto, TodoList.class);
    }
}
