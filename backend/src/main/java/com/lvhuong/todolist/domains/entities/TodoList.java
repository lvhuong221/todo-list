package com.lvhuong.todolist.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "todo_list")
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(mappedBy = "parentList")
    private ArrayList<TodoListItem> listItems;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName ="id")
    private UserEntity user;

    protected boolean isDeleted = false;
    protected LocalDateTime createDate;
    protected LocalDateTime lastUpdate;
}
