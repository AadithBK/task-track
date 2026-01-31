package com.workflowmanagement.task_track.mapper;

import com.workflowmanagement.task_track.dto.workflow.ResponseCreateNewWorkflowDTO;
import com.workflowmanagement.task_track.dto.workflow.ResponseGetWorkflowByIDDTO;
import com.workflowmanagement.task_track.dto.workflow.helper.*;
import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.HelperEmbeddedStepsDTO;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.model.SubTask;
import com.workflowmanagement.task_track.model.Task;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.workflow.Workflow;
import com.workflowmanagement.task_track.model.workflow.WorkflowStep;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface WorkflowTemplateMapper {

    CreateTemplateDto toDto(WorkflowTemplate workflowTemplate);

    WorkflowTemplate toEntity(CreateTemplateDto createTemplateDto);

    @Mapping(target = "workflowTemplateStepName", source = "workflowStepName")
    @Mapping(target = "workflowTemplateStepPosition", source = "workflowStepPosition")
    // Ignore the ID and Parent Template during initial DTO conversion
    @Mapping(target = "workflowTemplateStepId", ignore = true)
    @Mapping(target = "workflowTemplate", ignore = true)
    WorkflowTemplateStep toStepEntity(HelperEmbeddedStepsDTO dto);

    @Mapping(target = "workflowStepName", source = "workflowTemplateStepName")
    @Mapping(target = "workflowStepPosition", source = "workflowTemplateStepPosition")
    HelperEmbeddedStepsDTO toStepDto(WorkflowTemplateStep workflowTemplateStep);

    List<WorkflowTemplateStep> toDtoFromWorkflowStepEntity(Stream<HelperEmbeddedStepsDTO> helperEmbeddedStepsDTOStream);

    @AfterMapping
    default void linkSteps(@MappingTarget WorkflowTemplate entity) {
        if (entity.getWorkflowSteps() != null) {
            entity.getWorkflowSteps().forEach(step -> step.setWorkflowTemplate(entity));
        }
    }

    ResponseWorkflowDto toCustomResponseDto(WorkflowTemplate workflowTemplate);

    List<ResponseWorkflowDto> toListCustomResponseDto(List<WorkflowTemplate> workflowTemplate);

    @Mapping(target = "workflowSteps", source = "workflowSteps")
    ResponseCreateNewWorkflowDTO toCustomWorkflowResponse(Workflow workflow);

    HelperResponseCreateNewWorkflowDTO mapStepToStepDto(WorkflowStep workflowStep);

    ResponseGetWorkflowByIDDTO toGetByIdDto(Workflow workflow);
    // 2. The Step Level (MapStruct uses this for the List<WorkflowStep>)
    HelperWorkflowStepModel toStepModel(WorkflowStep step);

    // 3. The Task Level (MapStruct uses this for the List<Task>)
   HelperTaskModel toTaskModel(Task task);

    // 4. The User Level
   HelperUserModel toUserModel(User user);

   HelperSubTaskModel toSubTasksModel(SubTask subTask);
}
