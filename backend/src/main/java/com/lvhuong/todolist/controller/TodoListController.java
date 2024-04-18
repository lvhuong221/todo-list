package com.lvhuong.todolist.controller;

import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.mappers.impl.TodoListMapper;
import com.lvhuong.todolist.services.TodoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodoListController {

    private final TodoListService todoListService;
    private final TodoListMapper todoListMapper;


    @GetMapping("/todo-list")
    public Page<TodoListDto> findTodoList(Pageable pageable){
        Page<TodoList> resultList = todoListService.findPaginated(pageable);
        return resultList.map(todoListMapper::mapTo);
    }

    @PostMapping("/todo-list")
    public ResponseEntity<TodoListDto> addTodoList(@RequestBody TodoListDto todoListDto){
        TodoListDto todoList = todoListService.add(todoListDto);

        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PutMapping("/todo-list")
    public ResponseEntity<TodoListDto> updateList(@RequestBody TodoListDto todoListDto){
        TodoListDto todoList = todoListService.update(todoListDto);

        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @DeleteMapping("/todo-list")
    public ResponseEntity<TodoListDto> deleteList(@RequestBody TodoListDto todoListDto){
        TodoListDto todoList = todoListService.delete(todoListDto);

        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }
}
