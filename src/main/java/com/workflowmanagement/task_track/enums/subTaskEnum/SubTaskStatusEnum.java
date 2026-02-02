package com.workflowmanagement.task_track.enums.subTaskEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubTaskStatusEnum {
    OPEN, INPROGRESS, COMPLETED, ONHOLD;
    @JsonValue
    public String toValue(){
        return name();
    }
    @JsonCreator
    public static SubTaskStatusEnum fromValue(String value){
        for (SubTaskStatusEnum t : SubTaskStatusEnum.values()){
            if (t.name().equalsIgnoreCase(value)){
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid Priority: ["+value+"].");
    }
}
