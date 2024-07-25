package com.sinocarbon.workflow.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProcessInstanceDto {

	/**流程定义ID*/
	private String processDefinitionId;
	
	/**流程实例ID*/
	private String processInstanceId;
	
	/**流程定义key*/
	private String processDefinitionKey;
	
	/**流程定义名称*/
	private String processDefinitionName;
	
	/**开始时间*/
	private String startTime;
	
	/**结束时间*/
	private String endTime;
	
	/**流程实例名称*/
	private String processInstanceName;
	
	/**实例启动人ID*/
	private String startUserId;
	
	/**企业标识*/
	private String entpMark;
	 
	/**当前任务集*/
	private List<TaskDetailsDto> currentTaskList;
}
