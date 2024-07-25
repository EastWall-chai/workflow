package com.sinocarbon.workflow.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.junit.Assert;
import org.junit.Test;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;

public class MavenDemoApplicationTests {
	/**工作流程引擎对象*/
	 private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	 
	@Test
	public static void main(String[] args) throws IOException {

	  // 1. Build up the model from scratch 从头开始构建模型
	  BpmnModel model = new BpmnModel();
	  Process process = new Process();

	  model.addProcess(process);
	  process.setId("my-process");
	  process.addFlowElement(createStartEvent());
	  process.addFlowElement(createUserTask("task1", "First task", "fred"));
	  process.addFlowElement(createUserTask("task2", "Second task", "john"));
	  process.addFlowElement(createEndEvent());
	  process.addFlowElement(createSequenceFlow("start", "task1"));
	  process.addFlowElement(createSequenceFlow("task1", "task2"));
	  process.addFlowElement(createSequenceFlow("task2", "end"));

	  // 2. Generate graphical information 自动创建流程图

	  new BpmnAutoLayout(model).execute();

	  // 3. Deploy the process to the engine 将流程部署到引擎

	  Deployment deployment = processEngine.getRepositoryService().createDeployment()
			  					.addBpmnModel("dynamic-model.bpmn", model).name("Dynamic process deployment")
			  					.deploy();

	  // 4. Start a process instance 启动一个流程实例

	  ProcessInstance processInstance = processEngine.getRuntimeService()
			  								.startProcessInstanceByKey("my-process");

	  // 5. Check if task is available 检查任务是否可用

	  List<Task> tasks = processEngine.getTaskService().createTaskQuery()
			  						.processInstanceId(processInstance.getId()).list();
	  Assert.assertEquals(1, tasks.size());
	  Assert.assertEquals("First task", tasks.get(0).getName());
	  Assert.assertEquals("fred", tasks.get(0).getAssignee());

	  // 6. Save process diagram to a file 将流程图保存到文件中

	  InputStream processDiagram = processEngine.getRepositoryService().getProcessDiagram(processInstance.getProcessDefinitionId());
	  FileUtils.copyInputStreamToFile(processDiagram, new File("/Users/shenxiaocheng/Desktop/diagram.png"));

	  // 7. Save resulting BPMN xml to a file 将BPMN xml保存到文件中

	  InputStream processBpmn = processEngine.getRepositoryService().getResourceAsStream(deployment.getId(), "dynamic-model.bpmn");
	  FileUtils.copyInputStreamToFile(processBpmn, new File("/Users/shenxiaocheng/Desktop/process.bpmn20.xml"));

	}

	protected static UserTask createUserTask(String id, String name, String assignee) {
		UserTask userTask = new UserTask();
		userTask.setName(name);
		userTask.setId(id);
		userTask.setAssignee(assignee);	
		return userTask;
	}

	 

	protected static SequenceFlow createSequenceFlow(String from, String to) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		return flow;
	}

	 

	protected static StartEvent createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId("start");
		return startEvent;
	}

	 

	protected static EndEvent createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("end");
		return endEvent;
	}

}
