package com.sinocarbon.workflow.dto;

import java.util.List;

import com.sinocarbon.workflow.pojo.ActApdStatusConf;

import lombok.Data;

@Data
public class StatusConfigDto extends ActApdStatusConf{

	private String key;
	
	private List<String> taskKeys;
	
	private List<String> taskNames;
}
