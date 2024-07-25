package com.sinocarbon.workflow.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class TestServiceTask implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String acvitivyNameString = execution.getCurrentActivityName();
		System.out.println("活动节点名称："+acvitivyNameString);
	}

}
