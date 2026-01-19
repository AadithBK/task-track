package com.workflowmanagement.task_track.model;

import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.Role;
import com.workflowmanagement.task_track.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // Data includes Getter, Setter, and RequiredArgsConstructor
@Entity
@Table(name = "users")

@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    @NotBlank(message = "userName is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username can only contain letters, numbers, and underscores")
    private String userName;

//    @Email(message = "Please provide a valid email address")
      @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "Enter the valid EmailId"
      )
      @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is required")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Department is required")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Status is required")
    private Status status;
}