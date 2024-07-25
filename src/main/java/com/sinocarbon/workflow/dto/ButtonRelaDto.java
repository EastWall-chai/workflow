package com.sinocarbon.workflow.dto;

import lombok.Data;

@Data
public class ButtonRelaDto {

	private Integer id;
	
	private String fromTaskKey;
	
	private String targetTaskKey;
	
	private String fromTaskName;
	
	private String targetTaskName;
	
	private String btnName;
	
	private String procDefId;
	
	private String key;
	
	private String roles;
	
	private String recipient;
	
	private String status;
	
	private String time;
	
	private String procInstId;
	
	private Integer step;
	
	
}
