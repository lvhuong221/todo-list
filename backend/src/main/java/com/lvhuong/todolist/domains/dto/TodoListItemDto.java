package com.lvhuong.todolist.domains.dto;

import com.lvhuong.todolist.domains.entities.TodoListItem;
import com.lvhuong.todolist.enums.TodoListItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TodoListItemDto {

    private Long id;
    private Integer orderInList;
    private String description;
    private TodoListItemStatus status;
    private boolean isDeleted;

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
            return ((TodoListItemDto) obj).getId() == null;
        } else return getId().equals(((TodoListItemDto) obj).getId());
    }
}
