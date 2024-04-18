package com.lvhuong.todolist.services;

import com.lvhuong.todolist.domains.dto.TodoListDto;
import com.lvhuong.todolist.domains.dto.TodoListItemDto;
import com.lvhuong.todolist.domains.dto.UserDto;
import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.TodoListItem;
import com.lvhuong.todolist.domains.entities.UserEntity;
import com.lvhuong.todolist.mappers.impl.TodoListItemMapper;
import com.lvhuong.todolist.mappers.impl.TodoListMapper;
import com.lvhuong.todolist.repositories.TodoListItemsRepository;
import com.lvhuong.todolist.repositories.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final TodoListItemsRepository todoListItemsRepository;
    private final AuthenticationManager authenticationManager;
    private final TodoListMapper todoListMapper;
    private final TodoListItemMapper todoListItemMapper;
    private final UserService userService;

    public Page<TodoList> findPaginated(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) auth.getPrincipal();
        if(userDto.getId() == null){
            return null;
        }
        return todoListRepository.findByUserId(pageable, userDto.getId());
    }

    public TodoListDto add(TodoListDto todoListDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) auth.getPrincipal();
        UserEntity userEntity = UserEntity.builder()
                .id(userDto.getId())
                .build();

        ArrayList<TodoListItemDto> listItemsDto = todoListDto.getListItems();
        ArrayList<TodoListItem> listItems = new ArrayList<>();

        // Convert to entity
        for(TodoListItemDto item : todoListDto.getListItems()){
            TodoListItem newItem = TodoListItem.builder()
                    .description(item.getDescription())
                    .orderInList(item.getOrderInList())
                    .createDate(LocalDateTime.now())
                    .status(item.getStatus())
                    .build();
            listItems.add(newItem);
        }

        // save list to get Id
        TodoList newTodoList = TodoList.builder()
                .user(userEntity)
                .title(todoListDto.getTitle())
                .createDate(LocalDateTime.now())
                .isDeleted(false)
                .build();

        // Use Id to save items in list
        TodoList savedList = todoListRepository.save(newTodoList);
        for(TodoListItem item: listItems){
            item.setParentList(savedList);
            todoListItemsRepository.save(item);
        }

        // Return list with items
        savedList.setListItems(listItems);
        return todoListMapper.mapTo(savedList);
    }

    public TodoListDto delete(TodoListDto todoListDto){
        return null;
    }

    public TodoListDto update(TodoListDto todoListDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) auth.getPrincipal();

        if(todoListDto.getId() == null){
            return null;
        }

        TodoList existingList = todoListRepository.findByIdAndUserId(todoListDto.getId(), userDto.getId());
        if(existingList == null)
            return null;

        existingList.setDeleted(true);
        todoListRepository.save(existingList);

        return null;
    }
}
