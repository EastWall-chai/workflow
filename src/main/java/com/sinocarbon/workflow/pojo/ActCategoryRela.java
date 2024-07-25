package com.sinocarbon.workflow.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
@Data
@TableName("ACT_APD_CATE_RELA")
public class ActCategoryRela implements Serializable {

	private static final long serialVersionUID = 699054834803384518L;

    @TableId(value = "ID_", type = IdType.AUTO)
    @TableField("ID_")
	private Integer id;
	@TableField("CATEGORY_ID_")
	private Integer categoryId;
	@TableField("PRODEF_ID_")
	private String proDefId;
}
