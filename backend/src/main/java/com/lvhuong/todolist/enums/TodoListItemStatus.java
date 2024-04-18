package com.lvhuong.todolist.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TodoListItemStatus {
    @JsonProperty("planned")
    PLANNED,
    @JsonProperty("finished")
    FINISHED,
    @JsonProperty("failed")
    FAILED,
}
