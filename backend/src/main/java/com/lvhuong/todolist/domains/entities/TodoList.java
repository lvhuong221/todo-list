package com.lvhuong.todolist.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLRestriction("is_deleted <> true")
@Table(name = "todo_list")
public class TodoList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "parent", cascade=CascadeType.PERSIST)
    private List<TodoListItem> listItems;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName ="id")
    private UserEntity user;

    protected boolean isDeleted = false;
    protected LocalDateTime createDate;
    protected LocalDateTime lastUpdate;
}
