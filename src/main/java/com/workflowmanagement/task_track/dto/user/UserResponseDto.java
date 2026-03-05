package com.workflowmanagement.task_track.dto.user;

import com.workflowmanagement.task_track.enums.Department;
import com.workflowmanagement.task_track.enums.Role;
import com.workflowmanagement.task_track.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Integer userId;
    private String userName;
    private String email;
    private Role role;
    private Department department;
    private Status status;

    public UserResponseDto() {

    }
}