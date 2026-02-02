package com.workflowmanagement.task_track.enums.taskenum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {

    OPEN,
    IN_PROGRESS,
    COMPLETED,
    BLOCKED;

    @JsonCreator
    public static TaskStatus fromValue(String value){
        for (TaskStatus t : TaskStatus.values()){
            if (t.name().equalsIgnoreCase(value)){
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid Status: ["+value+"].");
    }

    @JsonValue
    public String toValue(){
        return name();
    }

}
