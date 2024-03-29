package com.lvhuong.TodoList.domains.entities;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

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
}