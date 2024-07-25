package com.sinocarbon.workflow.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 流程状态管理
 * </p>
 *
 * @author sxc123
 * @since 2022-11-15
 */
@TableName("ACT_APD_STATUS_CONF")
public class ActApdStatusConf implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_", type = IdType.AUTO)
    private Integer id;
    /**
     * 流程定义key
     */
    @TableField("PROC_DEF_KEY_")
    private String procDefKey;
    /**
     * 流程定义ID
     */
    @TableField("PROC_DEF_ID_")
    private String procDefId;
    /**
     * 租户ID
     */
    @TableField("TENANT_ID_")
    private String tenantId;
    /**
     * 大状态名称
     */
    @TableField("BIG_STATUS_NAME_")
    private String bigStatusName;
    /**
     * 小状态key
     */
    @TableField("TASK_KEY_")
    private String taskKey;
    /**
     * 小状态名称
     */
    @TableField("TASK_NAME_")
    private String taskName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
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

    public String getBigStatusName() {
        return bigStatusName;
    }

    public void setBigStatusName(String bigStatusName) {
        this.bigStatusName = bigStatusName;
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

    @Override
    public String toString() {
        return "ActApdStatusConf{" +
        ", id=" + id +
        ", procDefKey=" + procDefKey +
        ", procDefId=" + procDefId +
        ", tenantId=" + tenantId +
        ", bigStatusName=" + bigStatusName +
        ", taskKey=" + taskKey +
        ", taskName=" + taskName +
        "}";
    }
}
