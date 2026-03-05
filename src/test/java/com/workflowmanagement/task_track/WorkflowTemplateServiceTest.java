package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.dto.workflow.template.*;
import com.workflowmanagement.task_track.enums.workspace.UpdateWorkflowStepOperation;
import com.workflowmanagement.task_track.exceptions.WorkflowUpdateException;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplate;
import com.workflowmanagement.task_track.model.workflow.template.WorkflowTemplateStep;
import com.workflowmanagement.task_track.repository.WorkflowTemplateRepository;
import com.workflowmanagement.task_track.service.implementation.WorkflowTemplateService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowTemplateServiceTest {

    @Mock
    private WorkflowTemplateRepository workflowTemplateRepository;

    @InjectMocks
    private WorkflowTemplateService workflowTemplateService;

    private WorkflowTemplate template;
    private WorkflowTemplateStep step;

    @BeforeEach
    void setUp() {
        // 1. Initialize Template
        template = new WorkflowTemplate();
        template.setWorkflowTemplateId(1);
        template.setWorkflowTemplateName("General Template");
        template.setStatus(true);

        // 2. Initialize Step
        step = new WorkflowTemplateStep();
        step.setWorkflowTemplateStepId(10);
        step.setWorkflowTemplateStepName("Old Step Name");

        // CRITICAL: Link Step to Template (fixes the NPE in Objects.equals)
        step.setWorkflowTemplate(template);

        // 3. Add Step to Template (use ArrayList to allow modification in NEW case)
        template.setWorkflowSteps(new ArrayList<>(List.of(step)));
    }

    @Test
    void updateWorkFlowTemplate_UpdateName_Success() {
        // Arrange
        UpdateTemplateDto updateDto = new UpdateTemplateDto();
        updateDto.setName("New Template Name");

        HelperUpdateTemplateDto helper = new HelperUpdateTemplateDto();
        helper.setOperation(UpdateWorkflowStepOperation.NAME);
        helper.setName("New Step Name");
        updateDto.setStepUpdate(List.of(helper));

        when(workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(1))
                .thenReturn(Optional.of(template));

        // Act
        workflowTemplateService.updateWorkFlowTemplate(1, 10, updateDto);

        // Assert
        assertEquals("New Template Name", template.getWorkflowTemplateName());
        assertEquals("New Step Name", step.getWorkflowTemplateStepName());
        verify(workflowTemplateRepository).save(template);
    }

    @Test
    void updateWorkFlowTemplate_AddNewStep_Success() {
        // Arrange
        UpdateTemplateDto updateDto = new UpdateTemplateDto();

        HelperUpdateTemplateDto helper = new HelperUpdateTemplateDto();
        helper.setOperation(UpdateWorkflowStepOperation.NEW);

        // CHECK: Replace 'WorkflowTemplateStepDto' with the actual class name
        // found in your HelperUpdateTemplateDto.getNewStep() return type
        HelperEmbeddedStepsDTO newStepDto = new HelperEmbeddedStepsDTO();
        newStepDto.setWorkflowStepName("Brand New Step");
        // add any other required fields for a new step here

        helper.setNewStep(newStepDto);
        updateDto.setStepUpdate(List.of(helper));

        when(workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(1))
                .thenReturn(Optional.of(template));

        // Act
        workflowTemplateService.updateWorkFlowTemplate(1, 1, updateDto);

        // Assert
        assertEquals(2, template.getWorkflowSteps().size());
        verify(workflowTemplateRepository).save(template);
    }

    @Test
    void updateWorkFlowTemplate_InvalidStepId_ThrowsException() {
        // Arrange
        UpdateTemplateDto updateDto = new UpdateTemplateDto();
        HelperUpdateTemplateDto helper = new HelperUpdateTemplateDto();
        helper.setOperation(UpdateWorkflowStepOperation.NAME);
        updateDto.setStepUpdate(List.of(helper));

        when(workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(1))
                .thenReturn(Optional.of(template));

        // Act & Assert (Testing the 'if(templateStepId < 1)' check)
        assertThrows(WorkflowUpdateException.class, () ->
                workflowTemplateService.updateWorkFlowTemplate(1, 0, updateDto)
        );
    }

    @Test
    void getWorkflowTemplate_NotFound_ThrowsException() {
        // Arrange
        when(workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(99))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                workflowTemplateService.getWorkflowTemplate(99)
        );
    }

    @Test
    void deleteWorkFlowTemplate_SetsStatusFalse() {
        // Arrange
        when(workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(1))
                .thenReturn(Optional.of(template));

        // Act
        workflowTemplateService.deleteWorkFlowTemplate(1);

        // Assert
        assertFalse(template.isStatus());
    }
}