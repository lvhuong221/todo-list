package com.lvhuong.todolist.domains.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    protected boolean isDeleted = false;
    protected LocalDateTime createDate = LocalDateTime.now();
    protected LocalDateTime lastUpdate;
}