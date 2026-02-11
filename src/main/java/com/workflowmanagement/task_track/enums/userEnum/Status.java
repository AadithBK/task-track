    package com.workflowmanagement.task_track.enums.userEnum;

    import com.fasterxml.jackson.annotation.JsonCreator;

    public enum Status {
        ACTIVE, INACTIVE, PENDING, SUSPENDED;

        @JsonCreator
        public static Status fromValue(String value) throws IllegalArgumentException {
            for (Status status : Status.values()) {
                if (status.name().equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid Status: [" + value + "].");
        }
    }
