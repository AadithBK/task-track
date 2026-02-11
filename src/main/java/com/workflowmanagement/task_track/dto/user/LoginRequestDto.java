package com.workflowmanagement.task_track.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid EmailId")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}



