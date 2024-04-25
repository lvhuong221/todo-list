package com.lvhuong.todolist.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@SQLRestriction("is_deleted <> true")
@Table(name = "app_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(schema = "", name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles;

    @OneToMany(mappedBy = "user", cascade=CascadeType.PERSIST)
    private Set<TodoList> todoLists;

    protected boolean isDeleted = false;
    protected LocalDateTime createDate = LocalDateTime.now();
    protected LocalDateTime lastUpdate;

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if (getId() == null) {
            return ((UserEntity) obj).getId() == null;
        } else return getId().equals(((UserEntity) obj).getId());
    }
}