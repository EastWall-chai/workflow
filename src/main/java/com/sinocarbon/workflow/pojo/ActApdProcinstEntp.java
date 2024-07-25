package com.sinocarbon.workflow.pojo;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 流程实例与企业/个人的绑定关系表
 * </p>
 *
 * @author sxc123
 * @since 2022-09-01
 */
@TableName("ACT_APD_PROCINST_ENTP")
public class ActApdProcinstEntp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID_", type = IdType.AUTO)
    private Long id;
    /**
     * 企业标识
     */
    @TableField("ENTP_MARKS_")
    private String entpMarks;
    /**
     * 流程实例ID
     */
    @TableField("PROC_INST_ID_")
    private String procInstId;
    /**
     * 个人标识（保留字段）
     */
    @TableField("PERSON_MARKS_")
    private String personMarks;
    /**
     * 流转企业标识
     */
    @TableField("TO_ENTP_MARKS_")
    private String toEntpMarks;
    /**
     * 租户ID
     */
    @TableField("TENANT_ID_")
    private String tenantId;

    public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getToEntpMarks() {
		return toEntpMarks;
	}

	public void setToEntpMarks(String toEntpMarks) {
		this.toEntpMarks = toEntpMarks;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntpMarks() {
        return entpMarks;
    }

    public void setEntpMarks(String entpMarks) {
        this.entpMarks = entpMarks;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getPersonMarks() {
        return personMarks;
    }

    public void setPersonMarks(String personMarks) {
        this.personMarks = personMarks;
    }

    @Override
    public String toString() {
        return "ActApdProcinstEntp{" +
        ", id=" + id +
        ", entpMarks=" + entpMarks +
        ", procInstId=" + procInstId +
        ", personMarks=" + personMarks +
         ", toEntpMarks=" + toEntpMarks +
          ", tenantId=" + tenantId +
        "}";
    }
}
