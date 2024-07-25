package com.sinocarbon.workflow.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author sxc123
 * @since 2020-04-07
 */
@TableName("ACT_RE_PROCDEF")
public class ActReProcdef implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;
    @TableField("REV_")
    private Integer rev;
    @TableField("CATEGORY_")
    private String category;
    @TableField("NAME_")
    private String name;
    @TableField("KEY_")
    private String key;
    @TableField("VERSION_")
    private Integer version;
    @TableField("DEPLOYMENT_ID_")
    private String deploymentId;
    @TableField("RESOURCE_NAME_")
    private String resourceName;
    @TableField("DGRM_RESOURCE_NAME_")
    private String dgrmResourceName;
    @TableField("DESCRIPTION_")
    private String description;
    @TableField("HAS_START_FORM_KEY_")
    private Integer hasStartFormKey;
    @TableField("HAS_GRAPHICAL_NOTATION_")
    private Integer hasGraphicalNotation;
    @TableField("SUSPENSION_STATE_")
    private Integer suspensionState;
    @TableField("TENANT_ID_")
    private String tenantId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDgrmResourceName() {
        return dgrmResourceName;
    }

    public void setDgrmResourceName(String dgrmResourceName) {
        this.dgrmResourceName = dgrmResourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHasStartFormKey() {
        return hasStartFormKey;
    }

    public void setHasStartFormKey(Integer hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public Integer getHasGraphicalNotation() {
        return hasGraphicalNotation;
    }

    public void setHasGraphicalNotation(Integer hasGraphicalNotation) {
        this.hasGraphicalNotation = hasGraphicalNotation;
    }

    public Integer getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(Integer suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "ActReProcdef{" +
        ", id=" + id +
        ", rev=" + rev +
        ", category=" + category +
        ", name=" + name +
        ", key=" + key +
        ", version=" + version +
        ", deploymentId=" + deploymentId +
        ", resourceName=" + resourceName +
        ", dgrmResourceName=" + dgrmResourceName +
        ", description=" + description +
        ", hasStartFormKey=" + hasStartFormKey +
        ", hasGraphicalNotation=" + hasGraphicalNotation +
        ", suspensionState=" + suspensionState +
        ", tenantId=" + tenantId +
        "}";
    }
}
