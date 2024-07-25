package com.sinocarbon.workflow.pojo;

import java.io.Serializable;
import java.sql.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@Data
@TableName("ACT_APD_PROCINST_STATUS")
public class ActProcinstStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(value = "ID_", type = IdType.AUTO)
	@TableField("ID_")
	private Integer id;

	@TableField("PROC_INST_ID_")
	private String procInstId;

	@TableField("STATUS_")
	private String status;

	@TableField("UPD_DATE_")
	private Date updDate;

}
