package com.workflowmanagement.task_track.mapper;

import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "assignedToUser", source = "assignedToUser", qualifiedByName = "userToId")
    TaskDto toDto(Task task);
    List<TaskDto> toDtoList(List<Task> taskList);

    @Named("userToId")
    static Integer userToId(User user) {
        return (user == null) ? null : user.getUserId();
    }

}
