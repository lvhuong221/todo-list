package com.lvhuong.todolist.services;

import com.lvhuong.todolist.domains.entities.TodoListItem;
import com.lvhuong.todolist.repositories.TodoListItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class TodoListItemsService {

    private final TodoListItemsRepository todoListItemsRepository;

    List<TodoListItem> saveListItems(List<TodoListItem> listItems){
        return todoListItemsRepository.saveAll(listItems);
    }

    public List<TodoListItem> deleteListItems(List<TodoListItem> listItems){
        for(TodoListItem item: listItems){
            item.setDeleted(true);
        }
        return todoListItemsRepository.saveAll(listItems);
    }

    public List<TodoListItem> findAllByParent(Long parentId){
        return StreamSupport.stream(todoListItemsRepository
                        .findAllByParentId(parentId)
                        .spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteAllByParent(Long parentId){
        List<TodoListItem> listResult = findAllByParent(parentId);
        for(TodoListItem item: listResult){
            item.setDeleted(true);
        }
        todoListItemsRepository.saveAll(listResult);
        return;
    }
}
