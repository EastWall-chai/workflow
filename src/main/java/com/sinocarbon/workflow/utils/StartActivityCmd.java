package com.sinocarbon.workflow.utils;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;

public class StartActivityCmd implements Command<java.lang.Void> {
	
	private ActivityImpl activity;

	private String executionId;

	public StartActivityCmd(String executionId, ActivityImpl activity){
		this.activity = activity;
		this.executionId = executionId;
	}

	@Override
	public Void execute(CommandContext commandContext){
		//创建新任务
		ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(executionId);
		execution.setActivity(activity);
		execution.performOperation(AtomicOperation.ACTIVITY_START);
		return null;
	}

}
