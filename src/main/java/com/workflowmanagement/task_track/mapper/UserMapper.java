package com.workflowmanagement.task_track.mapper;

import com.workflowmanagement.task_track.dto.TaskDto;
import com.workflowmanagement.task_track.dto.user.UserResponseDto;
import com.workflowmanagement.task_track.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(source = )
    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> toDtoList(List<User> users);
}
