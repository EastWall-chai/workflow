package com.sinocarbon.workflow.utils;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;


public class DeleteRunningTaskCmd implements Command<java.lang.Void>{
	private TaskEntity currentTaskEntity;

	public DeleteRunningTaskCmd(TaskEntity currentTaskEntity){
		this.currentTaskEntity = currentTaskEntity;
	}

	public Void execute(CommandContext commandContext){
		//删除当前的任务
		//不能删除当前正在执行的任务，所以要先清除掉关联
		if (currentTaskEntity != null){
			Context.getCommandContext().getTaskEntityManager()
					.deleteTask(currentTaskEntity, TaskEntity.DELETE_REASON_DELETED, false);
		}

		return null;
	}
}
