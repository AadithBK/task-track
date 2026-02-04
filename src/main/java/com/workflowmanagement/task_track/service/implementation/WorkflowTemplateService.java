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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WorkflowTemplateService implements IWorkFlowTemplateService {
    @PersistenceContext // Inject the JPA EntityManager
    private EntityManager entityManager;
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
    workflowTemplate1.setWorkflowTemplateSteps(createTemplateDto.getWorkflowSteps().size());
    workflowTemplate1.setStatus(true);
//   workflowTemplate1.setWorkflowSteps(workflowStepDtoToEntity);


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
        WorkflowTemplate template = workflowTemplateRepository.findByWorkflowTemplateIdAndStatusTrue(templateId).orElseThrow(() -> new EntityNotFoundException("Workflow Template with ID " + templateId + " not found"));
System.out.println("Iran 1");
        if(updateTemplateDto.getName() != null) {
            template.setWorkflowTemplateName(updateTemplateDto.getName());
        }
        if(updateTemplateDto.getStepUpdate() != null ) {
            for(HelperUpdateTemplateDto h: updateTemplateDto.getStepUpdate()) {
                if(templateStepId < 1 ) throw new WorkflowUpdateException("Workflow Step ID not valid");
                System.out.println(h.getOperation());
                switch (h.getOperation()) {
//                    case UpdateWorkflowStepOperation.CHANGEPOSITION -> {

//                        if(template.getWorkflowTemplateId() ) throw  new WorkflowUpdateException("Template Step with ID " +  templateStepId+" not linked to Workflow template " + template.getWorkflowTemplateId());
//                        if(( h.getPosition() < 0 || h.getPosition() > template.getWorkflowTemplateSteps())  ) throw new WorkflowUpdateException("Position is SAME/Less than 0/ More than number of steps");
//                      WorkflowTemplateStep workflowStepToChangeFrom =   template.getWorkflowSteps().stream().filter(s -> s.getWorkflowTemplateStepId() == templateStepId).findFirst().orElseThrow(() -> new EntityNotFoundException("Workflow Template Step with ID " + templateStepId + " not found on this template"));
//                        WorkflowTemplateStep workflowStepToChangeTo =   template.getWorkflowSteps().stream().filter(s -> s.getWorkflowTemplateStepPosition() == h.getPosition()).findFirst().orElseThrow(() -> new EntityNotFoundException("Workflow Template Step Position " + h.getPosition() + " not found on this template"));
//                      workflowTemplateRepository.swapPositions(template.getWorkflowTemplateId(), workflowStepToChangeFrom.getWorkflowTemplateStepId(), h.getPosition());
//                    entityManager.refresh(template);

//                       Integer temp = workflowStepToChangeFrom.getWorkflowTemplateStepPosition();
//                       Integer temp2 = workflowStepToChangeTo.getWorkflowTemplateStepPosition();
//                       if(!Objects.equals(workflowStepToChangeTo.getWorkflowTemplate().getWorkflowTemplateId(), template.getWorkflowTemplateId()) && !Objects.equals(workflowStepToChangeFrom.getWorkflowTemplate().getWorkflowTemplateId(), template.getWorkflowTemplateId())) throw  new WorkflowUpdateException("Template Step with ID " +  templateStepId+" not linked to Workflow template " + template.getWorkflowTemplateId());
//                        WorkflowTemplateStep ws1 = workflowStepToChangeTo;
//                        WorkflowTemplateStep ws2 = workflowStepToChangeFrom;
//
//                        workflowStepToChangeTo.setWorkflowTemplateStepPosition(90000);
//                        workflowStepToChangeFrom.setWorkflowTemplateStepPosition(90001);
//                        workflowTemplateRepository.flush();
//                       workflowStepToChangeFrom.setWorkflowTemplateStepPosition(temp2);
//                       workflowStepToChangeTo.setWorkflowTemplateStepPosition(temp);
//
//                        workflowTemplateRepository.flush();
//                    }
                    case UpdateWorkflowStepOperation.NAME -> {
                        if(h.getName() == null) throw  new WorkflowUpdateException("Workflow Name is null");

                        WorkflowTemplateStep workflowStep =   template.getWorkflowSteps().stream().filter(s -> {
                            return s.getWorkflowTemplateStepId() == templateStepId;
                        }).findFirst().orElseThrow(() -> new EntityNotFoundException("Workflow Template Step with ID " + templateStepId + " not found"));
                        System.out.println(workflowStep.getWorkflowTemplate().getWorkflowTemplateId() +" "+template.getWorkflowTemplateId());
                        if(!Objects.equals(workflowStep.getWorkflowTemplate().getWorkflowTemplateId(), template.getWorkflowTemplateId())) throw  new WorkflowUpdateException("Template Step with ID " +  templateStepId+" not linked to Workflow template " + template.getWorkflowTemplateId());
                        workflowStep.setWorkflowTemplateStepName(h.getName());
                    }

                    case UpdateWorkflowStepOperation.NEW -> {
                        if(h.getNewStep() == null) throw  new WorkflowUpdateException("Workflow Name is null");
                       WorkflowTemplateStep newStep = mapper.toStepEntity(h.getNewStep());
                       newStep.setWorkflowTemplate(template);
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
