package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.dto.workflow.template.CreateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.HelperUpdateTemplateDto;
import com.workflowmanagement.task_track.dto.workflow.template.ResponseWorkflowDto;
import com.workflowmanagement.task_track.dto.workflow.template.UpdateTemplateDto;
import com.workflowmanagement.task_track.enums.workspace.UpdateWorkflowStepOperation;
import com.workflowmanagement.task_track.exceptions.WorkflowUpdateException;
import com.workflowmanagement.task_track.mapper.WorkflowTemplateMapper;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import com.workflowmanagement.task_track.repository.WorkflowTemplateRepository;
import com.workflowmanagement.task_track.service.IWorkFlowTemplateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.jdbc.Work;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkflowTemplateService implements IWorkFlowTemplateService {

    @Autowired
    private WorkflowTemplateRepository workflowTemplateRepository;

   final  private WorkflowTemplateMapper mapper;
    public WorkflowTemplateService() {
            this.mapper = Mappers.getMapper(WorkflowTemplateMapper.class);
    }

    @Override
    @Transactional
    public ResponseWorkflowDto createTemplate(CreateTemplateDto createTemplateDto) {

//        Get mapper for converting DTO to entities
    WorkflowTemplate workflowTemplate1 = mapper.toEntity(createTemplateDto);
    workflowTemplate1.setStatus(true);
    return  mapper.toCustomResponseDto(workflowTemplateRepository.save(workflowTemplate1));
    }

    @Override
    public ResponseWorkflowDto getWorkflowTemplate(Integer templateId) {

        return  mapper.toCustomResponseDto( workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue((templateId)).orElseThrow(() -> new EntityNotFoundException("Workflow Template with ID " + templateId + " not found")));
    }

    @Override
    public List<ResponseWorkflowDto> getAllWorkFlowTemplates() {
        return mapper.toListCustomResponseDto(workflowTemplateRepository.findAllByStatusTrue());
    }

    @Override

    public void updateWorkFlowTemplate(Integer templateId, Integer templateStepId, UpdateTemplateDto updateTemplateDto) {
        WorkflowTemplate template = workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(templateId)
                .orElseThrow(() -> new EntityNotFoundException("Workflow Template with ID " + templateId + " not found"));

        if (updateTemplateDto.getName() != null) {
            template.setWorkflowTemplateName(updateTemplateDto.getName());
        }

        if (updateTemplateDto.getStepUpdate() != null) {
            for (HelperUpdateTemplateDto h : updateTemplateDto.getStepUpdate()) {
                switch (h.getOperation()) {
                    case CHANGEPOSITION -> {
                        // 1. Find the step to move
                        WorkflowTemplateStep mover = template.getWorkflowSteps().stream()
                                .filter(e -> e.getWorkflowTemplateStepId().equals(templateStepId))
                                .findFirst()
                                .orElseThrow(() -> new WorkflowUpdateException("Step not linked to this template"));

                        // 2. Prepare the ordered list
                        List<WorkflowTemplateStep> others = template.getWorkflowSteps().stream()
                                .filter(e -> !e.getWorkflowTemplateStepId().equals(templateStepId))
                                .sorted(Comparator.comparingInt(WorkflowTemplateStep::getWorkflowTemplateStepPosition))
                                .toList();

                        List<WorkflowTemplateStep> finalStepsArray = new ArrayList<>();
                        int targetIdx = Math.max(0, h.getPosition() - 1);
                        int insertAt = Math.min(targetIdx, others.size());

                        finalStepsArray.addAll(others.subList(0, insertAt));
                        finalStepsArray.add(mover);
                        finalStepsArray.addAll(others.subList(insertAt, others.size()));

                        // 3. RE-INDEXING STRATEGY (Avoids Validation & Duplicate Key Errors)
                        // Move to "Parking Lot" (e.g., 1001, 1002...) to free up low numbers
                        for (int i = 0; i < finalStepsArray.size(); i++) {
                            finalStepsArray.get(i).setWorkflowTemplateStepPosition(1000 + i + 1);
                        }
                        workflowTemplateRepository.saveAndFlush(template);

                        // Move back to final positions (1, 2, 3...)
                        for (int i = 0; i < finalStepsArray.size(); i++) {
                            finalStepsArray.get(i).setWorkflowTemplateStepPosition(i + 1);
                        }

                        // We don't need clear()/addAll() if we modified objects already in the managed list
                        workflowTemplateRepository.saveAndFlush(template);
                    }

                    case NAME -> {
                        WorkflowTemplateStep step = template.getWorkflowSteps().stream()
                                .filter(s -> s.getWorkflowTemplateStepId().equals(templateStepId))
                                .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException("Step not found"));
                        step.setWorkflowTemplateStepName(h.getName());
                    }

                    case NEW -> {
                        WorkflowTemplateStep newStep = mapper.toStepEntity(h.getNewStep());
                        newStep.setWorkflowTemplate(template);
                        // Set position to the end of the list
                        newStep.setWorkflowTemplateStepPosition(template.getWorkflowSteps().size() + 1);
                        template.getWorkflowSteps().add(newStep);
                    }
                }
            }
        }
        workflowTemplateRepository.save(template);
    }

     @Transactional
    @Override
    public void deleteWorkFlowTemplate(Integer templateId) {
        WorkflowTemplate workflowTemplate = workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(templateId).orElseThrow(() -> new EntityNotFoundException("Workflow template with ID" + templateId + " not found"));

        workflowTemplate.setStatus(false);

    }
}
