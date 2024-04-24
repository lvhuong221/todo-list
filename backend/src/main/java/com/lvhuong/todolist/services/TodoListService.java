package com.lvhuong.todolist.services;

import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.dto.UserDto;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.UserEntity;
import com.lvhuong.todolist.mappers.impl.TodoListItemMapper;
import com.lvhuong.todolist.mappers.impl.TodoListMapper;
import com.lvhuong.todolist.mappers.impl.UserMapper;
import com.lvhuong.todolist.repositories.TodoListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserMapper userMapper;
    private final TodoListMapper todoListMapper;
    private final TodoListItemMapper todoListItemMapper;


    public Page<TodoList> findPaginated(UserDto user, Pageable pageable, boolean isDeleted) {
        return todoListRepository.findPageByUserId(pageable, user.getId());
    }

    public Optional<TodoList> findOne(Long userId, Long id) {
        return todoListRepository.findByIdAndUserId(userId, id);
    }

    public Optional<TodoList> findOne(Long id){return todoListRepository.findById(id);}

    public TodoList createUpdateTodoList(TodoList todoList) {
        return todoListRepository.save(todoList);
    }

    public boolean existByIdAndUserId(Long id, Long userId) {
        return todoListRepository.existsByIdAndUserId(id, userId);
    }

    public boolean delete(Long todoListId) {
        Optional<TodoList> listToDelete = findOne(todoListId);

        return listToDelete.map(todoList -> {
            todoList.setDeleted(true);
            todoListRepository.save(todoList);
            return true;
        }).orElse( false);
    }
}
