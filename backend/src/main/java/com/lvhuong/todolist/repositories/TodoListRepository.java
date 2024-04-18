package com.lvhuong.todolist.repositories;

import com.lvhuong.todolist.domains.entities.TodoList;
import com.lvhuong.todolist.domains.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

    Page<TodoList> findByUserId(Pageable pageable, long userId);

    TodoList findByIdAndUserId(Long id, Long userId);

    boolean existsByUserId(long userId);


}
