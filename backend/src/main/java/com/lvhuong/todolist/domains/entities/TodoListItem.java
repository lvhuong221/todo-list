package com.lvhuong.todolist.domains.entities;

import com.lvhuong.todolist.domains.dto.TodoListItemDto;
import com.lvhuong.todolist.enums.TodoListItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "todo_list_item")
public class TodoListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer orderInList;

    private String description;

    private TodoListItemStatus status;

    @ManyToOne
    @JoinColumn(name="todo_list_id", referencedColumnName="id")
    private TodoList parentList;

    protected boolean isDeleted = false;
    protected LocalDateTime createDate;
    protected LocalDateTime lastUpdate;


    @Override
    public int hashCode(){
        final int prime = 37;
        return prime
                + ((id == null) ? 0 : id.hashCode());
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
            return ((TodoListItem) obj).getId() == null;
        } else return getId().equals(((TodoListItem) obj).getId());
    }
}
