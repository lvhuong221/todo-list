package com.lvhuong.todolist.controller;

import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.dto.TodoListItemDto;
import com.lvhuong.todolist.domains.dto.UserDto;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.TodoListItem;
import com.lvhuong.todolist.domains.entities.UserEntity;
import com.lvhuong.todolist.mappers.impl.TodoListItemMapper;
import com.lvhuong.todolist.mappers.impl.TodoListMapper;
import com.lvhuong.todolist.services.TodoListItemsService;
import com.lvhuong.todolist.services.TodoListService;
import com.lvhuong.todolist.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class TodoListController {

    private final TodoListService todoListService;
    private final TodoListItemsService todoListItemsService;
    private final UserService userService;
    private final TodoListMapper todoListMapper;
    private final TodoListItemMapper todoListItemMapper;


    @GetMapping("/todo-list")
    public Page<TodoListDto> findAllTodoList(Pageable pageable) {
        UserDto userDto = userService.getCurrentUser();
        Page<TodoList> listResult = todoListService.findPaginated(userDto, pageable, false);
        return listResult.map(todoListMapper::mapTo);
    }

    @GetMapping("/todo-list/{id}")
    public ResponseEntity<TodoListDto> findTodoList(@PathVariable Long id){
        UserDto userDto = userService.getCurrentUser();
        Optional<TodoList> result = todoListService.findOne(userDto.getId(), id);

        return result.map(todoList ->
                        new ResponseEntity<>(todoListMapper.mapTo(todoList), HttpStatus.OK))
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/todo-list")
    public ResponseEntity<TodoListDto> createUpdateTodoList(@RequestBody TodoListDto todoListDto) {
        UserDto userDto = userService.getCurrentUser();
        TodoList todoList;

        // This API will be used by both create and update calls
        // Need to check to know which case is going on
        boolean listExist = todoListDto.getId() != null
                && todoListService.existByIdAndUserId(todoListDto.getId(), userDto.getId());


        if (!listExist) {
            // Need to set some properties like createDate for new entities
            todoList = TodoList.builder()
                    .user(UserEntity.builder().id(userDto.getId()).build())
                    .title(todoListDto.getTitle())
                    .createDate(LocalDateTime.now())
                    .isDeleted(false)
                    .build();

            List<TodoListItem> listItems = new ArrayList<>();
            listItems = todoListDto.getListItems().stream().map(item -> {
                TodoListItem newItem = todoListItemMapper.mapFrom(item);
                newItem.setId(null);
                newItem.setCreateDate(LocalDateTime.now());
                newItem.setParent(todoList);
                return newItem;
            }).collect(Collectors.toList());

            todoList.setListItems(listItems);
        } else {
            todoList = todoListMapper.mapFrom(todoListDto);
            todoList.setUser(null);
        }
        TodoList savedList = todoListService.createUpdateTodoList(todoList);

        if (listExist) {
            return new ResponseEntity<>(todoListMapper.mapTo(savedList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(todoListMapper.mapTo(savedList), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/todo-list/{todoListId}")
    public ResponseEntity deleteTodoList(@PathVariable Long todoListId) {
        UserDto userDto = userService.getCurrentUser();

        // Check if todoList belongs to the current user
        if (!todoListService.existByIdAndUserId(userDto.getId(), todoListId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean deleteResult = todoListService.delete(todoListId);
        if(deleteResult)
            todoListItemsService.deleteAllByParent(todoListId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
