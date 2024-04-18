package com.lvhuong.todolist.repositories;

import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.TodoListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListItemsRepository extends JpaRepository<TodoListItem, Long> {

}

