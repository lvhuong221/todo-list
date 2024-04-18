package com.lvhuong.todolist.mappers.impl;

import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.dto.TodoListItemDto;
import com.lvhuong.todolist.domains.entities.TodoListItem;
import com.lvhuong.todolist.mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TodoListItemMapper implements Mapper<TodoListItem, TodoListItemDto> {

    private final ModelMapper modelMapper;

    @Override
    public TodoListItemDto mapTo(TodoListItem todoListItem) {
        return modelMapper.map(todoListItem, TodoListItemDto.class);
    }

    @Override
    public TodoListItem mapFrom(TodoListItemDto todoListItemDto) {
        return modelMapper.map(todoListItemDto, TodoListItem.class);
    }
}
