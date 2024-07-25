package com.sinocarbon.workflow.dto;

import java.util.List;

import lombok.Data;

@Data
public class TaskNextDefinitionDto {
	
	private List<SequenceFlowDto> sequenceFlowDtos;
	
	private List<TaskDetailsDto> taskDetailsDtos;
	
	private EventDefinitionDto eventDefinitionDto;

}
