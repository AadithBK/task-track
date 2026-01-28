package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.HelperEmbeddedStepsDTO;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.mapper.WorkflowTemplateMapper;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import com.workflowmanagement.task_track.repository.WorkflowTemplateRepository;
import com.workflowmanagement.task_track.service.IWorkFlowTemplateService;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class WorkflowService implements IWorkFlowTemplateService {

    @Autowired
    private WorkflowTemplateRepository workflowTemplateRepository;

    @Override
    @Transactional
    public ResponseWorkflowDto createTemplate(CreateTemplateDto createTemplateDto) {

//        Get mapper for converting DTO to entities
        WorkflowTemplateMapper mapper = Mappers.getMapper(WorkflowTemplateMapper.class);

    WorkflowTemplate workflowTemplate1 = mapper.toEntity(createTemplateDto);
    workflowTemplate1.setWorkflowTemplateSteps(createTemplateDto.getWorkflowSteps().size());
//   workflowTemplate1.setWorkflowSteps(workflowStepDtoToEntity);


    return  mapper.toCustomResponseDto(workflowTemplateRepository.save(workflowTemplate1));
    }
}
