package com.lvhuong.todolist.repositories;

import com.lvhuong.todolist.domains.entities.TodoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

    Page<TodoList> findPageByUserId(Pageable pageable, Long userId);

    Optional<TodoList> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);


}
