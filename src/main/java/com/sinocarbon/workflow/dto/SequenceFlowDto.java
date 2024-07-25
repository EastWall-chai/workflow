package com.sinocarbon.workflow.dto;

import lombok.Data;

@Data
public class SequenceFlowDto {
	//线路Id
	private String id;
	//线路名称
	private String name;
	//线路前节点
	private String sourceRef;
	//线路后节点
	private String targetRef;
	//线路条件
	private String condition;

}
