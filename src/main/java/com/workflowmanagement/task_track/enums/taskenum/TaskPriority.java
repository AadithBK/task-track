package com.workflowmanagement.task_track.enums.taskenum;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskPriority {

    LOW,MEDIUM,HIGH;

    @JsonCreator
    public static TaskPriority fromValue(String value){
        for (TaskPriority t : TaskPriority.values()){
            if (t.name().equalsIgnoreCase(value)){
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid Priority: ["+value+"].");
    }

    @JsonValue
    public String toValue(){
        return name();
    }

}
