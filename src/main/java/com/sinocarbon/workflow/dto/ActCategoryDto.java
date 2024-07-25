package com.sinocarbon.workflow.dto;

import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;

import lombok.Data;

@Data
public class ActCategoryDto {
	
	/** 标签ID */
	private Integer id;
	/** 标签名称 */
	private String name;
	/** 父标签ID */
	private Integer pid;
	/** 标签记录状态 */
	private Integer status;
	/** 租户ID */
	private String tenantId;
	/** 流程定义ID */
	private String proDefId;
	/** 下一标签等级列表 */
	private List<ActCategoryDto> actCategoryList;
	/** 标签关联的流程信息 */
	private List<ProcessDetailslDto> processList;
	
}
