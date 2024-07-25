package com.sinocarbon.workflow.pojo;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 任务期限表
 * </p>
 *
 * @author sxc123
 * @since 2022-08-18
 */
@TableName("ACT_APD_TASK_LIMIT_INST_")
public class ActApdTaskLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID_", type = IdType.AUTO)
    private Long id;
    /**
     * 任务ID
     */
    @TableField("TASK_ID_")
    private String taskId;
    /**
     * 任务key
     */
    @TableField("TASK_KEY_")
    private String taskKey;
    /**
     * 任务名称
     */
    @TableField("TASK_NAME_")
    private String taskName;
    /**
     * 实例ID
     */
    @TableField("PROC_INST_ID_")
    private String procInstId;
    /**
     * 定义ID
     */
    @TableField("PROC_DEF_ID_")
    private String procDefId;
    /**
     * 租户ID
     */
    @TableField("TENANT_ID_")
    private String tenantId;
    /**
     * 任务开始时间
     */
    @TableField("TIME_START_")
    private Date timeStart;
    /**
     * 任务预期结束时间
     */
    @TableField("TIME_END_")
    private Date timeEnd;
    /**
     * 任务预期持续时间
     */
    @TableField("TIME_DURATION_")
    private String timeDuration;
    /**
     * 任务责任人
     */
    @TableField("TASK_OPERATORS_")
    private String taskOperators;
    /**
     * 任务状态，1:未逾期，2:已逾期
     */
    @TableField("TASK_STATE_")
    private String taskState;
    /**
     * 任务标识：1:未通知，2:已通知
     */
    @TableField("TASK_MARK_")
    private String taskMark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskMark() {
        return taskMark;
    }

    public void setTaskMark(String taskMark) {
        this.taskMark = taskMark;
    }

    public String getTaskOperators() {
		return taskOperators;
	}

	public void setTaskOperators(String taskOperators) {
		this.taskOperators = taskOperators;
	}

	@Override
    public String toString() {
        return "ActApdTaskLimit{" +
        ", id=" + id +
        ", taskId=" + taskId +
        ", taskKey=" + taskKey +
        ", taskName=" + taskName +
        ", procInstId=" + procInstId +
        ", procDefId=" + procDefId +
        ", tenantId=" + tenantId +
        ", timeStart=" + timeStart +
        ", timeEnd=" + timeEnd +
        ", timeDuration=" + timeDuration +
        ", taskState=" + taskState +
        ", taskMark=" + taskMark +
        ", taskOperators=" + taskOperators +
        "}";
    }
}
