package com.lvhuong.todolist.domains.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SQLRestriction("is_deleted <> true")
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