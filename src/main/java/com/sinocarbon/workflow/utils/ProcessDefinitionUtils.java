package com.sinocarbon.workflow.utils;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;


public interface ProcessDefinitionUtils {
	
	public static ActivityImpl getActivity(ProcessEngine processEngine, String processDefId, String activityId){
		ProcessDefinitionEntity pde = getProcessDefinition(processEngine, processDefId);
		return  pde.findActivity(activityId);
	}

	public static ProcessDefinitionEntity getProcessDefinition(ProcessEngine processEngine, String processDefId){
		return (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService())
				.getDeployedProcessDefinition(processDefId);
	}


}
