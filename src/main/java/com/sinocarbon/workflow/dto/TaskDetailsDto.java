package com.sinocarbon.workflow.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class TaskDetailsDto {
	
	/** 任务ID */
	private String taskId;
	
	/** 任务key */
	private String taskKey;
	
	/** 任务名 */
	private String taskName;
	
	/** 执行ID */
	private String executionId;
	
	/** 实例流程Id */
	private String processInstanceId;
	
	/** 实例流程Id集合 */
	private List<String> processInstanceIds;
	
	/** 流程变量信息 */
	private Map<String, Object> variables;
	
	/** 流程变量的key */
	private List<String> variableKeys;
	
	/** 任务单独候选人 */
	private String assignee;
	
	/** 任务候选人集合 */
	private  List<String> candidateUsers;
	
	/** 任务候选组集合 */
	private List<String> candidateGroups;
	
	/** 任务表单值,“表单变量 : 表单值” */
	private Map<String, String> properties;
	
	/** 租户 */
	private String tenantId;
	
	/** 任务表单列表 */
	private List<FormPropertyDto> formProperties;
	
	/** 任务创建时间 */
	private String createDate;
	
	/** 任务完成时间 */
	private String endDate;
	
	/**任务完成人*/
	private String operater;
	
	/**是否被挂起*/
	private boolean isSuspended;
	
	/**实例名称*/
	private String processInstanceName;
	
	/**实例发起人*/
	private String startUser;
	
	/**流程定义ID*/
	private String processDefinitionId;
	
	/**任务逾期设置*/
	private String timeDuration;
	
	/**历史任务完成的按钮名称*/
	private String btnName;
	
	/**流程分类*/
	private String category;
	
	/**角色code*/
	private String roleCode;
	
	/**流程变量*/
	private Map<String, Object> varMap;
}
