package com.sinocarbon.workflow.dto;


import lombok.Data;

@Data
public class EventDefinitionDto {
  //事件Id
  private String id;
  //节点的名称
  private String name;
  //事件节点类型
  private String type;


}
