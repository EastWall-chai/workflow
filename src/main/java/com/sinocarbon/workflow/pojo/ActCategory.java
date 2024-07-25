package com.sinocarbon.workflow.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@TableName("ACT_APD_CATEGORY")
@Data
public class ActCategory implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId(value = "ID_", type = IdType.AUTO)
    @TableField("ID_")
    private Integer id;
    @TableField("NAME_")
    private String name;
    @TableField(value="PID_")
    private Integer pid;
    @TableField("STATUS_")
    private Integer status;
    @TableField("TENANT_ID_")
    private String tenantId;



}
