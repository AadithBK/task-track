package com.workflowmanagement.task_track.enums.userEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN, MANAGER, EMPLOYEE;

    @JsonCreator
    public static Role fromValue(String value)  throws IllegalArgumentException {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        // Instead of a generic crash, we throw a specific message
        throw new IllegalArgumentException("Invalid Role: [" + value + "].");
    }
}