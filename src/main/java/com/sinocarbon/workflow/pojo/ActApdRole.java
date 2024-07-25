package com.sinocarbon.workflow.pojo;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 工作流默认角色表
 * </p>
 *
 * @author sxc123
 * @since 2022-09-09
 */
@TableName("ACT_APD_ROLE")
public class ActApdRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_", type = IdType.AUTO)
    private Long id;
    /**
     * 角色code
     */
    @TableField("ROLE_CODE_")
    private String roleCode;
    /**
     * 角色名称
     */
    @TableField("ROLE_NAME_")
    private String roleName;
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "ActApdRole{" +
        ", id=" + id +
        ", roleCode=" + roleCode +
        ", roleName=" + roleName +
        ", tenantId=" + tenantId +
        "}";
    }
}
