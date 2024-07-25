package com.sinocarbon.workflow.dto;

import java.util.List;
import java.util.Map;

import com.sinocarbon.workflow.pojo.ActApdTaskLimitDef;

import lombok.Data;

@Data
public class ProcessDetailslDto {
	
	/** 部署ID */
	private String deploymentId;
	
	/** 流程定义ID （流程key+版本号）*/
	private String processDefinitionId;
	
	/**流程定义key 同下processId*/
	private String processDefinitionKey;
	
	/** 流程定义key*/
	private String processId; 
	
	/** 流程定义name*/
	private String processName;
	
	/** 流程的描述 */
	private String processDescription;
	
	/** 流程的第一个任务节点 */
	private String startNode;
	
	/** 流程的最后一个任务节点 */
	private List<String> endNodes;
	
	/** 流程任务节点集合 */
	private List<TaskDetailsDto> nodeList;
	
	/** 流程线集合 */
	private List<Map<String, List<String>>> sequenceList;
	
	/** 流程部署时间 */
	private String deployTime;
	
	/** 流程分类 */
	private String category;
	
	/** 流程定义ID集合*/
	private List<String> processDefinitionIds;
	
	/** 流程定义全部挂起flag*/
	private Boolean allSuspend;
	
	/**租户*/
	private String tenantId;
	
	/**版本*/
	private Integer version;
	
	/**挂起状态*/
	private Integer state;
	
	/**新旧版本的任务key，oldKey/newKey*/
	private List<Map<String, String>> old2newTaskKeys;
	/**流程实例名称*/
	private String  processInstanceName;
	
	/** 任务节点逾期时间定义 */
	private List<ActApdTaskLimitDef> taskLimitList;
	
}
