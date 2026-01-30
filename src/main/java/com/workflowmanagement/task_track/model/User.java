package com.workflowmanagement.task_track.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.Role;
import com.workflowmanagement.task_track.enums.Status;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.jdbc.Work;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "assignedToUser" , cascade = CascadeType.ALL)
    @Column(nullable = false)
    @JsonIgnore
    private List<Task> assignedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "createdByUser" , cascade = CascadeType.ALL)
    @Column(nullable = false)
    @JsonIgnore
    private List<Task> createdTasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // Changed: Role starts as NULL
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Department is required")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.INACTIVE; // Default set here



}