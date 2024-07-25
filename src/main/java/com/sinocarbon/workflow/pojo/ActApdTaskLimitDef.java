package com.sinocarbon.workflow.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 任务节点逾期时间定义表
 * </p>
 *
 * @author sxc123
 * @since 2022-08-19
 */
@TableName("ACT_APD_TASK_LIMIT_DEF_")
public class ActApdTaskLimitDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_", type = IdType.AUTO)
    private Long id;
    /**
     * 任务KEY
     */
    @TableField("TASK_KEY_")
    private String taskKey;
    /**
     * 任务名称
     */
    @TableField("TASK_NAME_")
    private String taskName;
    /**
     * 任务预期持续时间(天)
     */
    @TableField("TIME_DURATION_")
    private String timeDuration;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
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

    @Override
    public String toString() {
        return "ActApdTaskLimitDef{" +
        ", id=" + id +
        ", taskKey=" + taskKey +
        ", taskName=" + taskName +
        ", timeDuration=" + timeDuration +
        ", procDefId=" + procDefId +
        ", tenantId=" + tenantId +
        "}";
    }
}
