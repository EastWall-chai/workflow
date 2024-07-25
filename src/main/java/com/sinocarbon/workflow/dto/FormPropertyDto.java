package com.sinocarbon.workflow.dto;

import java.util.Map;

import lombok.Data;

@Data
public class FormPropertyDto {

	/** 表单id */
	private String id;
	
	/** 表单名 */
	private String name;
	
	/** 表单类型：string,long,enum,date,boolean,double*/
	private String type;
	
	/** 表单值 */
	private String value;
	
	/** enum类型时，供选择的id-value */
	private Map<String, String> informationFormValues;
	
	/** date类型时，时间格式，如“YY_MM_DD” */
	private String datePattern;
	
	/** 是否必填 */
	private Boolean isRequired ;
	
	/** 是否可读 */
	private Boolean isReadable ;
	
	/** 是否可写 */
	private Boolean isWritable ;
}
