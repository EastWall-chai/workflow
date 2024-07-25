package com.sinocarbon.workflow.pojo;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 流转按钮关系表
 * </p>
 *
 * @author sxc123
 * @since 2022-08-15
 */
@TableName("ACT_APD_BTN_RELA")
public class ActApdBtnRela implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_", type = IdType.AUTO)
    private Integer id;
    /**
     * 来源节点key
     */
    @TableField("FROM_TASK_KEY_")
    private String fromTaskKey;
    /**
     * 目标节点key
     */
    @TableField("TARGET_TASK_KEY_")
    private String targetTaskKey;
    /**
     * 来源节点名
     */
    @TableField("FROM_TASK_NAME_")
    private String fromTaskName;
    /**
     * 目标节点名
     */
    @TableField("TARGET_TASK_NAME_")
    private String targetTaskName;
    /**
     * 按钮名
     */
    @TableField("BTN_NAME_")
    private String btnName;
    /**
     * 流程定义ID
     */
    @TableField("PROC_DEF_ID_")
    private String procDefId;
    /**
     * 流转权限
     */
    @TableField("ROLES")
    private String roles;
    /**
     * 接收人
     */
    @TableField("RECIPIENT")
    private String recipient;
    /**
     * 业务状态
     */
    @TableField("STATUS_")
    private String status;
    /**
     * 跨级流转
     */
    @TableField("STEP_")
    private Integer step;

    public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromTaskKey() {
        return fromTaskKey;
    }

    public void setFromTaskKey(String fromTaskKey) {
        this.fromTaskKey = fromTaskKey;
    }

    public String getTargetTaskKey() {
        return targetTaskKey;
    }

    public void setTargetTaskKey(String targetTaskKey) {
        this.targetTaskKey = targetTaskKey;
    }

    public String getFromTaskName() {
        return fromTaskName;
    }

    public void setFromTaskName(String fromTaskName) {
        this.fromTaskName = fromTaskName;
    }

    public String getTargetTaskName() {
        return targetTaskName;
    }

    public void setTargetTaskName(String targetTaskName) {
        this.targetTaskName = targetTaskName;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ActApdBtnRela{" +
        ", id=" + id +
        ", fromTaskKey=" + fromTaskKey +
        ", targetTaskKey=" + targetTaskKey +
        ", fromTaskName=" + fromTaskName +
        ", targetTaskName=" + targetTaskName +
        ", btnName=" + btnName +
        ", procDefId=" + procDefId +
        ", roles=" + roles +
        ", recipient=" + recipient +
        ", status=" + status +
         ", step=" + step +
        "}";
    }
}
