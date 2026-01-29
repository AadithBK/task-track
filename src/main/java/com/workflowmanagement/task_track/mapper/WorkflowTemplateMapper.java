package com.workflowmanagement.task_track.mapper;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.HelperEmbeddedStepsDTO;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

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



}
