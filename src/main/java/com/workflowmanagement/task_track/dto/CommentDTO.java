package com.workflowmanagement.task_track.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDTO {

    // Getters and setters
    @NotNull
    private Long taskId;

    @NotBlank
    private String text;

}
