package com.lvhuong.todolist.repositories;

import com.lvhuong.todolist.domains.entities.TodoListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoListItemsRepository extends JpaRepository<TodoListItem, Long> {
    public Optional<TodoListItem> getItemsByParentId(Long userId);

    Iterable<TodoListItem> findAllByParentId(Long userId);
}

