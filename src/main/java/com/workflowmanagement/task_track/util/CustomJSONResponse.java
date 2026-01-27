package com.workflowmanagement.task_track.util;

import com.workflowmanagement.task_track.enums.CustomStatus;
import jakarta.annotation.Nullable;
import lombok.Getter;



@Getter
public class CustomJSONResponse<T> {
    private final @Nullable String message;
    private final @Nullable String error;
    private final CustomStatus status;
    private final @Nullable Long timestamp;
    private final @Nullable T data;

    public CustomJSONResponse(@Nullable String message, @Nullable String error, CustomStatus status, @Nullable T data) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }


    public static <T> CustomJSONResponse<T> ok(String message, T data) {
        return new CustomJSONResponse<>(message, null, CustomStatus.OK, data);
    }

    // Static method for Errors
    public static <T> CustomJSONResponse<T> err(String error) {
        return new CustomJSONResponse<>(null, error, CustomStatus.ERR, null);
    }

}
