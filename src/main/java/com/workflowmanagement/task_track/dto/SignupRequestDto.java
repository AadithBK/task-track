package com.workflowmanagement.task_track.dto;

import com.workflowmanagement.task_track.enums.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDto {
    @NotBlank(message = "userName is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username can only contain letters")
    private String userName;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Enter a valid EmailId")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Department is required")
    private Department department;
}