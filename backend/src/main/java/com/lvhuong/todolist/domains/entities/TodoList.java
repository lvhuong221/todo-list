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
            return ((TodoList) obj).getId() == null;
        } else return getId().equals(((TodoList) obj).getId());
    }
}
