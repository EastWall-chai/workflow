package com.sinocarbon.workflow.dto;

import java.util.Date;

import lombok.Data;

@Data
public class LimitTaskDto {

	 /**
     * 主键
     */
    private Long id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 任务key
     */
    private String taskKey;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 实例ID
     */
    private String procInstId;
    /**
     * 定义ID
     */
    private String procDefId;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 任务开始时间
     */
    private Date timeStart;
    /**
     * 任务预期结束时间
     */
    private Date timeEnd;
    /**
     * 任务预期持续时间
     */
    private String timeDuration;
    /**
     * 任务状态，1:未逾期，2:已逾期
     */
    private String taskState;
    /**
     * 任务标识：1:未通知，2:已通知
     */
    private String taskMark;
    
    private String taskOpreator;
	
}
