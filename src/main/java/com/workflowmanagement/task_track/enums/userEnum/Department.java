package com.workflowmanagement.task_track.enums.userEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Department {
    IT, HR, SALES, MARKETING, OPERATIONS;

    @JsonCreator
    public static Department fromValue(String value)  throws IllegalArgumentException{
        for (Department dept : Department.values()) {
            if (dept.name().equalsIgnoreCase(value)) {
                return dept;
            }
        }
        // This message will be caught by your GlobalExceptionHandler
        throw new IllegalArgumentException("Invalid Department: [" + value +
                "]. ");
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}