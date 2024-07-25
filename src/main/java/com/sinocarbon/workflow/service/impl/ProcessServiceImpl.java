package com.sinocarbon.workflow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Event;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FormValue;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.polaris.commons.utils.DateUtils;
import com.sinocarbon.polaris.commons.utils.ParamMapUtils;
import com.sinocarbon.workflow.client.rest.config.OrgaizationSerivce;
import com.sinocarbon.workflow.dao.ActApdBtnRelaMapper;
import com.sinocarbon.workflow.dao.ActApdProcinstEntpMapper;
import com.sinocarbon.workflow.dao.ActApdStatusConfMapper;
import com.sinocarbon.workflow.dao.ActApdTaskLimitDefMapper;
import com.sinocarbon.workflow.dao.ActCategoryRelaMapper;
import com.sinocarbon.workflow.dao.ActReProcdefMapper;
import com.sinocarbon.workflow.dto.ButtonRelaDto;
import com.sinocarbon.workflow.dto.EventDefinitionDto;
import com.sinocarbon.workflow.dto.FormPropertyDto;
import com.sinocarbon.workflow.dto.ProcessDetailslDto;
import com.sinocarbon.workflow.dto.ProcessInstanceDto;
import com.sinocarbon.workflow.dto.SequenceFlowDto;
import com.sinocarbon.workflow.dto.StatusConfigDto;
import com.sinocarbon.workflow.dto.TaskDetailsDto;
import com.sinocarbon.workflow.dto.TaskNextDefinitionDto;
import com.sinocarbon.workflow.pojo.ActApdBtnRela;
import com.sinocarbon.workflow.pojo.ActApdProcinstEntp;
import com.sinocarbon.workflow.pojo.ActApdStatusConf;
import com.sinocarbon.workflow.pojo.ActApdTaskLimit;
import com.sinocarbon.workflow.pojo.ActApdTaskLimitDef;
import com.sinocarbon.workflow.pojo.ActCategoryRela;
import com.sinocarbon.workflow.service.ActApdTaskLimitService;
import com.sinocarbon.workflow.service.ProcessService;
import com.sinocarbon.workflow.utils.CommonUtils;
import com.sinocarbon.workflow.utils.Constant;
import com.sinocarbon.workflow.utils.DeleteRunningTaskCmd;
import com.sinocarbon.workflow.utils.ErrorCode;
import com.sinocarbon.workflow.utils.ProcessDefinitionUtils;
import com.sinocarbon.workflow.utils.StartActivityCmd;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sxc
 * @since 2019-06-13
 */
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired

	private RepositoryService repositoryService;

	@Autowired
	private HistoryService historyService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private FormService formService;
	@Autowired
	private ActReProcdefMapper actReProcdefMapper;
	@Autowired
	private ActApdBtnRelaMapper actApdBtnRelaMapper;
	@Autowired
	private ActApdProcinstEntpMapper actApdProcinstEntpMapper;
	@Autowired
	private ActCategoryRelaMapper actCategoryRelaMapper;
	@Autowired
	private ActApdTaskLimitService actApdTaskLimitService;
	@Autowired
	private ActApdTaskLimitDefMapper actApdTaskLimitDefMapper;
	@Autowired
	private OrgaizationSerivce orgaizationSerivce;
	@Autowired
	private ActApdStatusConfMapper actApdStatusConfMapper;

	/** 工作流程引擎对象 */
	private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	/** 开始节点ID */
	private static final String START = "start";
	private static final String OPERATER = "operater";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse deployProcess(String deploymentName, String bpmnResourceUrl, String picResourceUrl,
			String tenantId) {
		// 创建一个部署对象DeploymentBuilder，用来定义流程部署的相关参数
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		// 添加部署的名称
		deploymentBuilder.name(deploymentName);
		// 添加 .bpmn和 .png
		deploymentBuilder.addClasspathResource(bpmnResourceUrl);
		deploymentBuilder.addClasspathResource(picResourceUrl);
		// 部署流程定义
		Deployment deployment = deploymentBuilder.tenantId(tenantId).deploy();

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("deploymentId", deployment.getId());
		resultMap.put("deploymentName", deployment.getName());
		resultMap.put("deploymentTime", DateUtils.formatDateTime(deployment.getDeploymentTime())); // 任务创建时间
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, resultMap);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse startProcesses(String processesKey, String processInstanceName, String startUser,
			Map<String, Object> map, String tenantId, List<String> entpMarks) {
		//现根据key查询默认租户的最新版本流程
		ProcessDefinition defLastPro = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processesKey)
				.processDefinitionTenantId(Constant.DEF_TENANT_ID).latestVersion().singleResult();
		int version = defLastPro.getVersion();// 最新版本号
		// 查询该租户下是否有流程定义，若无，则拷贝
		List<ProcessDefinition> pds = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processesKey)
				.processDefinitionTenantId(tenantId).latestVersion().list();
		// 若无，则拷贝 && 若当前租户的最新版本<默认租户的，则拷贝
		if(CollectionUtils.isEmpty(pds) || pds.get(0).getVersion() < version) {
			ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processesKey)
					.processDefinitionTenantId(Constant.DEF_TENANT_ID).latestVersion().singleResult();
			ProcessDetailslDto processDetailslDto = new ProcessDetailslDto();
			List<String> processDefinitionIds = new ArrayList<String>();
			processDefinitionIds.add(pd.getId());
			processDetailslDto.setTenantId(tenantId);
			processDetailslDto.setProcessDefinitionIds(processDefinitionIds);
			codyProcess(processDetailslDto);
		}
		if (StringUtils.isEmpty(startUser)) {
			identityService.setAuthenticatedUserId("SYSTEM");
		} else {
			identityService.setAuthenticatedUserId(startUser);
		}
		List<ProcessInstanceDto> processInstanceDtos = new ArrayList<ProcessInstanceDto>();
		// 多家企业启动任务，遍历企业
		if(CollectionUtils.isNotEmpty(entpMarks)) {
			for(String entpMark: entpMarks) {
				ProcessInstance processInstance = null;
				if (StringUtils.isEmpty(tenantId)) {
					processInstance = runtimeService.startProcessInstanceByKey(processesKey, map);
				} else {
					processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(processesKey, map, tenantId);
				}
				// 往流程实例与企业关系表内绑定数据
				String processInstanceId = processInstance.getId();
				ActApdProcinstEntp actApdProcinstEntp = new ActApdProcinstEntp();
				actApdProcinstEntp.setEntpMarks(entpMark);
				actApdProcinstEntp.setToEntpMarks(entpMark);
				actApdProcinstEntp.setProcInstId(processInstanceId);
				actApdProcinstEntp.setTenantId(tenantId);
				actApdProcinstEntpMapper.insert(actApdProcinstEntp);
				runtimeService.setProcessInstanceName(processInstance.getId(), processInstanceName);
				// 启动流程后，查看流程第一个任务
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskCreateTime().desc().list();
				// 若该任务设置了逾期条件，则新增任务逾期实例
				setActApdTaskLimitInst(taskList.get(0));
				
				List<TaskDetailsDto> resultTaskList = new ArrayList<>();
				resultTaskList.add(packageTaskDtoByTask(taskList.get(0)));
				
				//封装流程实例信息返回
				ProcessInstanceDto processInstanceDto = new ProcessInstanceDto();
				processInstanceDto.setProcessDefinitionId(processInstance.getProcessDefinitionId());
				processInstanceDto.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
				processInstanceDto.setProcessDefinitionName(processInstance.getProcessDefinitionName());
				processInstanceDto.setProcessInstanceId(processInstance.getId());
				processInstanceDto.setProcessInstanceName(processInstanceName);
				processInstanceDto.setStartTime(DateUtils.formatDateTime(new Date()));
				processInstanceDto.setStartUserId(startUser);
				processInstanceDto.setEntpMark(entpMark);
				processInstanceDto.setCurrentTaskList(resultTaskList);
				processInstanceDtos.add(processInstanceDto);
			}
			
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, processInstanceDtos);
	}

	@Override
	public BaseResponse startProcessesByDefId(String processesId, String processInstanceName, String startUser,
			Map<String, Object> map) {

		if (StringUtils.isEmpty(startUser)) {
			identityService.setAuthenticatedUserId("SYSTEM");
		} else {
			identityService.setAuthenticatedUserId(startUser);
		}
		String processDefKey = repositoryService.getProcessDefinition(processesId).getKey();
		ProcessInstance processInstance = null;
		try {
			processInstance = runtimeService.startProcessInstanceById(processesId);
//			processInstance = runtimeService.startProcessInstanceByKey(processDefKey, map);
			runtimeService.setProcessInstanceName(processInstance.getId(), processInstanceName);
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400070, ErrorCode.CODE_400070_MSG);
		}

		ProcessInstanceDto processInstanceDto = new ProcessInstanceDto();
		processInstanceDto.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		processInstanceDto.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
		processInstanceDto.setProcessDefinitionName(processInstance.getProcessDefinitionName());
		processInstanceDto.setProcessInstanceId(processInstance.getId());
		processInstanceDto.setProcessInstanceName(processInstanceName);
		processInstanceDto.setStartTime(DateUtils.formatDateTime(new Date()));
		processInstanceDto.setStartUserId(startUser);
		
		// 获取该实例的当前任务
		List<Task> taskList = null;
		try {
			taskList = taskService.createTaskQuery().processInstanceId(processInstanceDto.getProcessInstanceId()).orderByTaskId().desc().list();
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400080, ErrorCode.CODE_400080_MSG);
		}
		if (CollectionUtils.isEmpty(taskList)) {// 无当前任务
			return BaseResponse.failed(ErrorCode.CODE_400020, ErrorCode.CODE_400020_MSG);
		}
		if (taskList.size() > 1) {// 多个当前任务
			return BaseResponse.failed(ErrorCode.CODE_400030, ErrorCode.CODE_400030_MSG);
		}
		Task task = taskList.get(0);
		ButtonRelaDto buttonRelaDto = new ButtonRelaDto();
		buttonRelaDto.setFromTaskKey(task.getTaskDefinitionKey());
		buttonRelaDto.setProcDefId(task.getProcessDefinitionId());
		buttonRelaDto.setTargetTaskName(task.getName());
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(new Date());
		buttonRelaDto.setTime(format);
		buttonRelaDto.setProcInstId(task.getProcessInstanceId());
		actReProcdefMapper.insertProcinstStatus(buttonRelaDto);
		
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, processInstanceDto);

	}

	@Override
	public BaseResponse findPersonTaskByAssignee(String assignee, String tenantId) {
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).taskTenantId(tenantId).list();
		List<Task> list2 = taskService.createTaskQuery().taskCandidateUser(assignee).taskTenantId(tenantId).list();
		List<TaskDetailsDto> taskList = new ArrayList<>();
		List<String> taskIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Task task : list) {
				taskIds.add(task.getId());
				taskList.add(packageTaskDtoByTask(task));
			}
		}
		if (CollectionUtils.isNotEmpty(list2)) {
			for (Task task : list2) {
				if (taskIds.contains(task.getId())) {
					continue;
				}
				taskIds.add(task.getId());
				taskList.add(packageTaskDtoByTask(task));
			}
		}
		return BaseResponse.successed(taskList);
	}

	@Override
	public BaseResponse findGroupTaskByAssignee(String group, String tenantId,String processInstanceIds) {
		List<Task> list = new ArrayList<>();
		if(StringUtils.isEmpty(processInstanceIds)) {
			list = taskService.createTaskQuery().taskCandidateGroup(group).taskTenantId(tenantId).list();
		}else {
			list = taskService.createTaskQuery().taskCandidateGroup(group)
					.taskTenantId(tenantId)
					.processInstanceIdIn(Arrays.asList(processInstanceIds.split(",")))
					.list();
		}
		List<TaskDetailsDto> taskList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Task task : list) {
				taskList.add(packageTaskDtoByTask(task));
			}
		}
		return BaseResponse.successed(taskList);
	}

	/** 将Task封装成DTO */
	public TaskDetailsDto packageTaskDtoByTask(Task task) {
		TaskDetailsDto taskDetailsDto = new TaskDetailsDto();
		taskDetailsDto.setTaskId(task.getId());
		taskDetailsDto.setTaskKey(task.getTaskDefinitionKey());
		taskDetailsDto.setTaskName(task.getName());
		taskDetailsDto.setAssignee(task.getAssignee());
		taskDetailsDto.setCreateDate(DateUtils.formatDateTime(task.getCreateTime()));
		taskDetailsDto.setProcessInstanceId(task.getProcessInstanceId());
		taskDetailsDto.setTenantId(task.getTenantId());
		taskDetailsDto.setSuspended(task.isSuspended());
		taskDetailsDto.setProcessDefinitionId(task.getProcessDefinitionId());
		Object operater = taskService.getVariable(task.getId(), OPERATER);
		if (!StringUtils.isEmpty(operater)) {
			taskDetailsDto.setOperater(operater.toString());
		}
		/** 根据任务ID查询组任务的办理人（组任务） */
		List<IdentityLink> identityLinklist = taskService.getIdentityLinksForTask(task.getId());
		List<String> userList = new ArrayList<>();
		List<String> groupList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(identityLinklist)) {
			for (IdentityLink identityLink : identityLinklist) {
				if (identityLink.getUserId() != null) {
					userList.add(identityLink.getUserId());
				} else {
					groupList.add(identityLink.getGroupId());
				}
			}
		}
		List<Map<String, Object>> cateNameList = actReProcdefMapper.queryCategoryRelaByDefId(task.getProcessDefinitionId());
		if(CollectionUtils.isNotEmpty(cateNameList)) {
			taskDetailsDto.setCategory(cateNameList.get(0).get("name")==null?null:cateNameList.get(0).get("name").toString());
		}
		/** 根据定义ID查询所属分类*/
		taskDetailsDto.setCandidateUsers(userList);
		taskDetailsDto.setCandidateGroups(groupList);
		return taskDetailsDto;
	}

	/**
	 * type : 1.权限在候选人组上 2.权限在流程线上
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse completeTask(Map<String, Object> map, Integer type) {

		String processInstanceId = ParamMapUtils.getString(map, "processInstanceId");
		if (StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		// 获取该实例的当前任务
		List<Task> taskList = null;
		try {
			taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskId().desc().list();
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400080, ErrorCode.CODE_400080_MSG);
		}
		if (CollectionUtils.isEmpty(taskList)) {// 无当前任务
			return BaseResponse.failed(ErrorCode.CODE_400020, ErrorCode.CODE_400020_MSG);
		}
		if (taskList.size() > 1) {// 多个当前任务
			return BaseResponse.failed(ErrorCode.CODE_400030, ErrorCode.CODE_400030_MSG);
		}

		// 流向的目的节点
		String targetNode = ParamMapUtils.getString(map, "targetNode");
		if (StringUtils.isEmpty(targetNode)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		Task task = taskList.get(0);
		if (type == 1) {// 判断任务完成权限
			String assignee = ParamMapUtils.getString(map, "assignee");
			String user = ParamMapUtils.getString(map, "user");
			String group = ParamMapUtils.getString(map, "group");
			boolean flag = targetNodeIsUseful(targetNode, task.getId())
					&& taskAuthIsUseful(assignee, user, group, task);
			if (!flag) {
				return BaseResponse.failed(ErrorCode.CODE_400050, ErrorCode.CODE_400050_MSG);
			}
		} else {// 判断任务线流转权限
				// 操作人权限
			String auth = ParamMapUtils.getString(map, "auth");
			boolean flag = targetNodeIsUseful(targetNode, task.getId())
					&& flowAuthIsUseful(targetNode, auth, task.getId());
			if (!flag) {
				return BaseResponse.failed(ErrorCode.CODE_400050, ErrorCode.CODE_400050_MSG);
			}
		}
		// 任务完成人
		String operater = ParamMapUtils.getString(map, OPERATER);
		if (!StringUtils.isEmpty(operater)) {
			taskService.setVariableLocal(task.getId(), OPERATER, operater);
		}
		try {
			taskService.complete(task.getId(), map);
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400100, ErrorCode.CODE_400100_MSG);
		}
		
		//2022-818 返回最新任务节点信息-sxc
		taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskId().desc().list();
		// 设置任务逾期实例，若报错则捕捉，不去影响任务的正常完成
		try {
			if (CollectionUtils.isNotEmpty(taskList)) {// 有且仅有一个当前任务
				// 查询当前任务有无设置逾期时间
				task = taskList.get(0);
				setActApdTaskLimitInst(task);
			}
		} catch (Exception e) {
			log.error("=============插入任务逾期实例失败");
			log.error("=============当前任务列表：" + taskList);
		}
		return BaseResponse.successed(taskList);
	}
	
	// 若该任务设置了逾期条件，则新增任务逾期实例
	public void setActApdTaskLimitInst(Task task) {
		EntityWrapper<ActApdTaskLimitDef> ew = new EntityWrapper<>();
		ew.eq("TENANT_ID_", task.getTenantId()).eq("PROC_DEF_ID_", task.getProcessDefinitionId())
			.eq("TASK_KEY_", task.getTaskDefinitionKey());
		List<ActApdTaskLimitDef> actApdTaskLimitDefs = actApdTaskLimitDefMapper.selectList(ew);
		// 当前任务设置了逾期时间
		if(CollectionUtils.isNotEmpty(actApdTaskLimitDefs)) {
			// 添加任务逾期实例
			ActApdTaskLimit actApdTaskLimit = new ActApdTaskLimit();
			actApdTaskLimit.setTaskId(task.getId());
			actApdTaskLimit.setTaskKey(task.getTaskDefinitionKey());
			actApdTaskLimit.setTaskName(task.getName());
			actApdTaskLimit.setProcInstId(task.getProcessInstanceId());
			actApdTaskLimit.setProcDefId(task.getProcessDefinitionId());
			actApdTaskLimit.setTenantId(task.getTenantId());
			// 获取任务待办人
			TaskDetailsDto taskDetailsDto = packageTaskDtoByTask(task);
			actApdTaskLimit.setTaskOperators(CommonUtils.praseListToString(taskDetailsDto.getCandidateGroups()));
			// 任务开始时间
			actApdTaskLimit.setTimeStart(task.getCreateTime());
			// 任务设置的过程天数
			Integer timeDuration = 0;
			try {
				timeDuration = Integer.valueOf(actApdTaskLimitDefs.get(0).getTimeDuration());
			} catch (Exception e) {
				timeDuration = null;
			}
			if(timeDuration == null) { // 天数转化异常，则不进行逾期实例添加
				return ;
			}
			actApdTaskLimit.setTimeDuration(timeDuration.toString());
			// 计算任务预计结束时间
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(task.getCreateTime());
			calendar.add(calendar.DATE, timeDuration);
			actApdTaskLimit.setTimeEnd(calendar.getTime());
			// 设置任务通知状态为 未通知
			actApdTaskLimit.setTaskMark("1");
			// 添加任务逾期实例
			actApdTaskLimitService.saveActApdTaskLimitInst(actApdTaskLimit);
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse completeTasks(Map<String, Object> map, Integer type) {
		List<String> processInstances = (List<String>)map.get("processInstances");
		List<String> completedProcessInstances = new ArrayList<String>();
		if(CollectionUtils.isEmpty(processInstances)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		for(String processInstanceId : processInstances) {
			// 获取该实例的当前任务
			List<Task> taskList = null;
			try {
				taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskId().desc().list();
			} catch (Exception e) {
				continue;
			}
			if (CollectionUtils.isEmpty(taskList)) {// 无当前任务
				continue;
			}
			if (taskList.size() > 1) {// 多个当前任务
				continue;
			}

			// 流向的目的节点
			String targetNode = ParamMapUtils.getString(map, "targetNode");
			if (StringUtils.isEmpty(targetNode)) {
				return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
			}
			Task task = taskList.get(0);
			if (type == 1) {// 判断任务完成权限
				String assignee = ParamMapUtils.getString(map, "assignee");
				String user = ParamMapUtils.getString(map, "user");
				String group = ParamMapUtils.getString(map, "group");
				boolean flag = targetNodeIsUseful(targetNode, task.getId())
						&& taskAuthIsUseful(assignee, user, group, task);
				if (!flag) {
					continue;
				}
			} else {// 判断任务线流转权限
					// 操作人权限
				String auth = ParamMapUtils.getString(map, "auth");
				boolean flag = targetNodeIsUseful(targetNode, task.getId())
						&& flowAuthIsUseful(targetNode, auth, task.getId());
				if (!flag) {
					continue;
				}
			}
			// 任务完成人
			String operater = ParamMapUtils.getString(map, OPERATER);
			if (!StringUtils.isEmpty(operater)) {
				taskService.setVariableLocal(task.getId(), OPERATER, operater);
			}
			try {
				taskService.complete(task.getId(), map);
			} catch (Exception e) {
				continue;
			}
			completedProcessInstances.add(processInstanceId);
		}
		return BaseResponse.successed(completedProcessInstances);
	}

	public boolean targetNodeIsUseful(String targetNode, String taskId) {
		// 获取当前任务的后面节点信息
		TaskNextDefinitionDto taskNextDefinitionDto = getNextTaskDefinition(taskId);
		if (taskNextDefinitionDto == null) {
			return false;
		}
		// 可流转的节点结合
		List<TaskDetailsDto> taskDtos = taskNextDefinitionDto.getTaskDetailsDtos();
		// 当前节点流出的线
		List<SequenceFlowDto> sequenceFlowDtos = taskNextDefinitionDto.getSequenceFlowDtos();
		// 可流转的结束事件
		EventDefinitionDto eventDto = taskNextDefinitionDto.getEventDefinitionDto();
		boolean targetNodeIsUseful = false;// 流向节点是否正确
		for (TaskDetailsDto task : taskDtos) {
			if (task.getTaskKey().equals(targetNode)) {
				targetNodeIsUseful = true;
				break;
			}
		}
		if (eventDto != null && targetNode.equals(eventDto.getId())) {
			targetNodeIsUseful = true;
		}
		// 经过的线
		String condition = null;
		for (SequenceFlowDto sequenceFlowDto : sequenceFlowDtos) {
			if (sequenceFlowDto.getTargetRef().equals(targetNode)) {
				condition = sequenceFlowDto.getCondition();
			}
		}
		if (condition == null) {
			return false;
		}
		return targetNodeIsUseful;
	}

	public boolean flowAuthIsUseful(String targetNode, String auth, String taskId) {
		// 获取当前任务的后面节点信息
		TaskNextDefinitionDto taskNextDefinitionDto = getNextTaskDefinition(taskId);
		if (taskNextDefinitionDto == null) {
			return false;
		}
		// 当前节点流出的线
		List<SequenceFlowDto> sequenceFlowDtos = taskNextDefinitionDto.getSequenceFlowDtos();
		boolean authIsUseful = false;// 流向权限是否正确
		// 经过的线
		String condition = null;
		for (SequenceFlowDto sequenceFlowDto : sequenceFlowDtos) {
			if (sequenceFlowDto.getTargetRef().equals(targetNode)) {
				condition = sequenceFlowDto.getCondition();
			}
		}
		if (condition == null) {
			return false;
		}
		// 解析线上的权限
		String[] cond1 = condition.split("&&");
		if (cond1.length == 1) {// 没有设置权限
			return true;
		} else {
			if (StringUtils.isEmpty(auth)) {
				return false;
			}
		}
		if (cond1[1].indexOf('(') == -1) {// 仅一个权限
			String[] cond2 = cond1[1].split("==");
			String flowAuth = cond2[1];
			flowAuth = flowAuth.substring(1, flowAuth.length() - 2);
			if (flowAuth.equals(auth)) {
				authIsUseful = true;
			}
		}
		if (cond1[1].indexOf('(') > -1) {// 多个权限可以通过
			String cond2 = cond1[1].substring(1, cond1[1].length() - 2);
			String[] cond3 = cond2.split("\\|\\|");
			for (String str : cond3) {
				str = str.split("==")[1];
				str = str.substring(1, str.length() - 1);
				if (str.equals(auth)) {
					authIsUseful = true;
					break;
				}
			}
		}
		return authIsUseful;
	}

	public boolean taskAuthIsUseful(String assignee, String user, String group, Task task) {
		TaskDetailsDto taskDetailsDto = packageTaskDtoByTask(task);
		String taskAssignee = taskDetailsDto.getAssignee();
		List<String> candidateUsers = taskDetailsDto.getCandidateUsers();
		List<String> candidateGroups = taskDetailsDto.getCandidateGroups();
		// 无权限，任何人都可以执行
		if (StringUtils.isEmpty(taskAssignee) && CollectionUtils.isEmpty(candidateUsers)
				&& CollectionUtils.isEmpty(candidateGroups)) {
			return true;
		}
		// 操作人没有任何权限
		if (StringUtils.isEmpty(assignee) && StringUtils.isEmpty(user) && StringUtils.isEmpty(group)) {
			return false;
		}
		// 任务指定人正确
		if (!StringUtils.isEmpty(taskAssignee) && !StringUtils.isEmpty(assignee) && taskAssignee.equals(assignee)) {
			return true;
		}
		// 任务候选人正确
		if (!CollectionUtils.isEmpty(candidateUsers) && !StringUtils.isEmpty(user) && candidateUsers.contains(user)) {
			return true;
		}
		// 任务候选组正确
		if (!CollectionUtils.isEmpty(candidateGroups) && !StringUtils.isEmpty(group)
				&& candidateGroups.contains(group)) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse completeTaskOld(Map<String, Object> map) {
		
		String processInstanceId = ParamMapUtils.getString(map, "processInstanceId");
		if(StringUtils.isEmpty(processInstanceId)) {
			System.out.println("======processInstanceId为空");
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		//获取该实例的当前任务
		List<Task> taskList = null;
		try {
			taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskId().desc().list();
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400080, ErrorCode.CODE_400080_MSG);
		}
		if(CollectionUtils.isEmpty(taskList)) {//无当前任务
			return BaseResponse.failed(ErrorCode.CODE_400020, ErrorCode.CODE_400020_MSG);
		}
		if(taskList.size()>1) {//多个当前任务
			return BaseResponse.failed(ErrorCode.CODE_400030, ErrorCode.CODE_400030_MSG);
		}
		
		Task task = taskList.get(0);
		//任务完成人
		System.out.println("======task:"+task.getName());
		String operater = ParamMapUtils.getString(map, OPERATER);
		if(!StringUtils.isEmpty(operater)) {
			taskService.setVariableLocal(task.getId(), OPERATER, operater);
		}
		try {
			taskService.complete(task.getId(), map);
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400100, ErrorCode.CODE_400100_MSG);
		}
		System.out.println("======已经完成任务");
		taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskId().desc().list();
		if(taskList.size()>0) {
			return BaseResponse.successed(packageTaskDtoByTask(taskList.get(0)));
		}else {
			return BaseResponse.successed(new ArrayList<TaskDetailsDto>());
		}
		
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse addGroupUser(TaskDetailsDto taskDetailsDto) {
		if (!StringUtils.isEmpty(taskDetailsDto.getAssignee())) {
			taskService.setAssignee(taskDetailsDto.getTaskId(), taskDetailsDto.getAssignee());
		}
		if (CollectionUtils.isNotEmpty(taskDetailsDto.getCandidateUsers())) {
			for (String user : taskDetailsDto.getCandidateUsers()) {
				taskService.addCandidateUser(taskDetailsDto.getTaskId(), user);
			}
		}
		if (CollectionUtils.isNotEmpty(taskDetailsDto.getCandidateGroups())) {
			for (String group : taskDetailsDto.getCandidateGroups()) {
				taskService.addCandidateGroup(taskDetailsDto.getTaskId(), group);
			}
		}
		return BaseResponse.successed(Constant.ADD_MESSAGE);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse removeGroupUser(TaskDetailsDto taskDetailsDto) {
		if (!StringUtils.isEmpty(taskDetailsDto.getAssignee())) {
			HistoricTaskInstance hti = null;
			try {
				hti = historyService.createHistoricTaskInstanceQuery().taskId(taskDetailsDto.getTaskId())
						.singleResult();
			} catch (Exception e) {
				return BaseResponse.failed(ErrorCode.CODE_400090, ErrorCode.CODE_400090_MSG);
			}

			if (hti.getAssignee() != null && hti.getAssignee().equals(taskDetailsDto.getAssignee())) {
				taskService.setAssignee(taskDetailsDto.getTaskId(), null);
			} else {
				return BaseResponse.failed(ErrorCode.CODE_400040, ErrorCode.CODE_400040_MSG);
			}
		}
		/** 根据任务ID查询组任务的办理人（组任务） */
		List<IdentityLink> identityLinklist = taskService.getIdentityLinksForTask(taskDetailsDto.getTaskId());
		List<String> userList = new ArrayList<>();
		List<String> groupList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(identityLinklist)) {
			for (IdentityLink identityLink : identityLinklist) {
				if (identityLink.getUserId() != null) {
					userList.add(identityLink.getUserId());
				} else {
					groupList.add(identityLink.getGroupId());
				}
			}
		}
		if (CollectionUtils.isNotEmpty(taskDetailsDto.getCandidateUsers())) {

			for (String user : taskDetailsDto.getCandidateUsers()) {
				if (!userList.contains(user)) {
					return BaseResponse.failed(ErrorCode.CODE_400041, ErrorCode.CODE_400041_MSG);
				}
				taskService.deleteCandidateUser(taskDetailsDto.getTaskId(), user);
			}
		}
		if (CollectionUtils.isNotEmpty(taskDetailsDto.getCandidateGroups())) {
			for (String group : taskDetailsDto.getCandidateGroups()) {
				if (!groupList.contains(group)) {
					return BaseResponse.failed(ErrorCode.CODE_400042, ErrorCode.CODE_400042_MSG);
				}
				taskService.deleteCandidateGroup(taskDetailsDto.getTaskId(), group);
			}
		}
		return BaseResponse.successed(Constant.DELETE_MESSAGE);
	}

	@Override
	public BaseResponse findProcessDefinition(String processkey, String tenantId) {
		List<ProcessDefinition> list;
		if (!StringUtils.isEmpty(tenantId)) {
			list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processkey)
					.processDefinitionTenantId(tenantId).orderByProcessDefinitionVersion().desc() // 按照版本的倒序排列
					.list();
		} else {
			tenantId = Constant.DEF_TENANT_ID;
			list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processkey)
					.orderByProcessDefinitionVersion().desc() // 按照版本的倒序排列
					.list();
		}

		List<ProcessDetailslDto> resultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (ProcessDefinition pd : list) {
				ProcessDetailslDto processDetailslDto = new ProcessDetailslDto();
				processDetailslDto.setProcessDefinitionId(pd.getId());
				processDetailslDto.setProcessDefinitionKey(pd.getKey());
				processDetailslDto.setProcessName(pd.getName());
				processDetailslDto.setVersion(pd.getVersion());
				processDetailslDto.setProcessDescription(pd.getDescription());
				resultList.add(processDetailslDto);
			}
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, resultList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse deleteProcessDefinition(String processkey, String processDefinitionId, String tenantId) {
		List<ProcessDefinition> list;
		if (!StringUtils.isEmpty(processDefinitionId)) {
			list = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).list();
		} else {
			if (StringUtils.isEmpty(processkey) || StringUtils.isEmpty(tenantId)) {
				return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
			}
			list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processkey)
					.processDefinitionTenantId(tenantId).list();
		}
		if (CollectionUtils.isNotEmpty(list)) {
			for (ProcessDefinition pd : list) {
				/**
				 * 根据部署ID删除流程 级联删除 不管流程有没有启动，都删除
				 */
				repositoryService.deleteDeployment(pd.getDeploymentId(), true);
			}
			return BaseResponse.successed(Constant.DELETE_MESSAGE);
		} else {
			return BaseResponse.failed(ErrorCode.CODE_400060, ErrorCode.CODE_400060_MSG);
		}

	}

	@Override
	public BaseResponse findPersonHistoryTask(String assignee, String tenantId) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询对象
				.taskAssignee(assignee).taskTenantId(tenantId).finished().list();
		List<HistoricTaskInstance> list2 = historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询对象
				.taskCandidateUser(assignee).taskTenantId(tenantId).finished().list();
		List<TaskDetailsDto> resultList = new ArrayList<>();
		List<String> taskIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (HistoricTaskInstance hit : list) {
				taskIds.add(hit.getId());
				resultList.add(packageTaskDtoByHiTask(hit));
			}
		}
		if (CollectionUtils.isNotEmpty(list2)) {
			for (HistoricTaskInstance hit : list2) {
				if (taskIds.contains(hit.getId())) {
					continue;
				}
				taskIds.add(hit.getId());
				resultList.add(packageTaskDtoByHiTask(hit));
			}
		}
		return BaseResponse.successed(resultList);
	}

	@Override
	public BaseResponse findGroupHistoryTask(String group, String tenantId, String processInstanceIds) {
		List<HistoricTaskInstance> list = new ArrayList<>();
		if(StringUtils.isEmpty(processInstanceIds)) {
			list =  historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询对象
					.taskCandidateGroup(group)
					.taskTenantId(tenantId)
					.finished()
					.orderByHistoricTaskInstanceEndTime()
					.desc()
					.list();
		}else {
			list =  historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询对象
					.taskCandidateGroup(group)
					.taskTenantId(tenantId)
					.processInstanceIdIn(Arrays.asList(processInstanceIds.split(",")))
					.finished()
					.orderByHistoricTaskInstanceEndTime()
					.desc()
					.list();
		}
		List<TaskDetailsDto> resultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (HistoricTaskInstance hit : list) {
				resultList.add(packageTaskDtoByHiTask(hit));
			}
		}
		return BaseResponse.successed(resultList);
	}

	/** 将历史任务封装成DTO */
	public TaskDetailsDto packageTaskDtoByHiTask(HistoricTaskInstance hiTask) {
		TaskDetailsDto taskDetailsDto = new TaskDetailsDto();
		taskDetailsDto.setTaskId(hiTask.getId());
		taskDetailsDto.setTaskName(hiTask.getName());
		taskDetailsDto.setTaskKey(hiTask.getTaskDefinitionKey());
		taskDetailsDto.setAssignee(hiTask.getAssignee());
		taskDetailsDto.setCreateDate(DateUtils.formatDateTime(hiTask.getCreateTime()));
		taskDetailsDto.setEndDate(DateUtils.formatDateTime(hiTask.getEndTime()));
		taskDetailsDto.setProcessInstanceId(hiTask.getProcessInstanceId());
		List<HistoricVariableInstance> hviList = historyService.createHistoricVariableInstanceQuery().taskId(hiTask.getId()).list();
		
		Map<String, Object> varMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(hviList)) {
			hviList.forEach(var -> varMap.put(var.getVariableName(), var.getValue()));
			taskDetailsDto.setVarMap(varMap);
		}
//		HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery().taskId(hiTask.getId())
//				.variableName(OPERATER).singleResult();
//		if (hvi != null && !StringUtils.isEmpty(hvi.getValue())) {
//			taskDetailsDto.setOperater(hvi.getValue().toString());
//		}
		/** 根据任务ID查询组任务的办理人（组任务） */
		List<HistoricIdentityLink> hiIdentityLinklist = historyService.getHistoricIdentityLinksForTask(hiTask.getId());
		List<String> userList = new ArrayList<>();
		List<String> groupList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(hiIdentityLinklist)) {
			for (HistoricIdentityLink hiIdentityLink : hiIdentityLinklist) {
				if (hiIdentityLink.getUserId() != null) {
					userList.add(hiIdentityLink.getUserId());
				} else {
					groupList.add(hiIdentityLink.getGroupId());
				}
			}
		}
		/**根据任务ID查询任务表单*/
		List<FormPropertyDto> formProperties = getHisFormProperty(hiTask.getId());
		taskDetailsDto.setFormProperties(formProperties);
		taskDetailsDto.setCandidateUsers(userList);
		taskDetailsDto.setCandidateGroups(groupList);
		return taskDetailsDto;
	}
	
	@Override
	public BaseResponse findGroupHistoryTaskByProInstId(String processInstanceId) {
		List<TaskDetailsDto> resultList = new ArrayList<>();
		List<HistoricTaskInstance> list = 
				historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询对象
				.finished()
				.orderByHistoricTaskInstanceEndTime()
				.desc()
				.list();
		// 查询该流程是否已结束
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		if (CollectionUtils.isNotEmpty(list)) {
			String lastTaskKey = "";// 最近一个任务完成后的节点key
			if(pi == null) {// 流程已结束
				lastTaskKey = "end";
			}else { // 流程未结束
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId)
							.orderByTaskCreateTime().desc().list();
				lastTaskKey = taskList.get(0).getTaskDefinitionKey();
			}
			for (int i = 0;i < list.size(); i++) {
				String sourceNode = "";
				String targetNode = "";
				String btnName = "";
				EntityWrapper<ActApdBtnRela> ew = new EntityWrapper<ActApdBtnRela>();
				if(i == 0) {
					sourceNode = list.get(i).getTaskDefinitionKey();
					targetNode = lastTaskKey;
				}else {
					sourceNode = list.get(i).getTaskDefinitionKey();
					targetNode = list.get(i-1).getTaskDefinitionKey();
				}
				// 查询按钮绑定关系
				ew.eq("FROM_TASK_KEY_", sourceNode)
					.eq("TARGET_TASK_KEY_", targetNode)
					.eq("PROC_DEF_ID_", list.get(i).getProcessDefinitionId());
				List<ActApdBtnRela> actApdBtnRela = actApdBtnRelaMapper.selectList(ew);
				if(CollectionUtils.isEmpty(actApdBtnRela)) {
					actApdBtnRela.get(0).getBtnName();
				}
				resultList.add(packageTaskDtoByHiTask(list.get(i)));
				// 将绑定的按钮名称加入历史任务对象中
				resultList.get(resultList.size()-1).setBtnName(btnName);
			}
		}
		return BaseResponse.successed(resultList);
	}

	@Override
	public BaseResponse findCurrTaskByProcessInstanceId(String processInstanceId, String tenantId) {
		List<Task> taskList;
		if (StringUtils.isEmpty(processInstanceId)) {// 查某租户的所有当前任务
			taskList = taskService.createTaskQuery().taskTenantId(tenantId).orderByTaskCreateTime().desc().list();
		} else {// 查某实例的当前任务
			taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc()
					.list();
		}
		List<TaskDetailsDto> resultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(taskList)) {
			for (Task task : taskList) {
				resultList.add(packageTaskDtoByTask(task));
			}
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, resultList);
	}

	@Override
	public BaseResponse findHisTaskByProcessInstanceId(String processInstanceId, String tenantId) {
		List<HistoricTaskInstance> hisTaskList;
		if (StringUtils.isEmpty(processInstanceId)) {// 查询某租户的所有当前任务
			hisTaskList = historyService.createHistoricTaskInstanceQuery().taskTenantId(tenantId)
					.orderByTaskCreateTime().desc().finished().list();
		} else {
			hisTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
					.orderByTaskCreateTime().asc().finished().list();
		}
		List<TaskDetailsDto> resultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(hisTaskList)) {
			for (HistoricTaskInstance hti : hisTaskList) {
				resultList.add(packageTaskDtoByHiTask(hti));
			}
		}
		return BaseResponse.successed(resultList);
	}

	@Override
	public BaseResponse findTask(String taskId) {
		HistoricTaskInstance hti = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		if (hti == null) {
			return BaseResponse.successed(null);
		} else {
			return BaseResponse.successed(packageTaskDtoByHiTask(hti));
		}
	}

	@Override
	public BaseResponse getProcessActivity(String processDefinitionId, String processInstanceId,String processKey, String tenantId) {
		if(StringUtils.isEmpty(processDefinitionId)) {
			if(StringUtils.isEmpty(processInstanceId)) {
				List<ProcessDefinition> defLastPros = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey)
						.processDefinitionTenantId(tenantId).latestVersion().list();
				if(CollectionUtils.isEmpty(defLastPros)) {
					ProcessDefinition defLastPro = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey)
							.processDefinitionTenantId(Constant.DEF_TENANT_ID).latestVersion().singleResult();
					processDefinitionId = defLastPro.getId();
				}else {
					processDefinitionId = defLastPros.get(0).getId();
				}
			}else {
				ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
				processDefinitionId = pi.getProcessDefinitionId();
			}
		}
		return BaseResponse.successed(getProcessByProDefId(processDefinitionId));
	}

	@Override
	public InputStream currentProcessInstanceImage(String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		if (task == null) {
			return null;
		}
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
		// ID 为 流程定义Key
		Process process = bpmnModel.getProcessById(processDefinition.getKey());
		// 流程节点ID
		FlowElement flowElement = process.getFlowElement(task.getTaskDefinitionKey());
		DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
		List<String> highLightedActivities = new ArrayList<>();
		highLightedActivities.add(flowElement.getId());
		// 生成图片
		return generator.generateDiagram(bpmnModel, "png", highLightedActivities, Collections.emptyList(), "宋体", "宋体",
				"宋体", null, 1);
	}

	@Override
	public BaseResponse unfinishedProcess(Set<String> processInstanceIds) {
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds)
				.orderByProcessInstanceId().desc().list();
		List<String> resultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (ProcessInstance pi : list) {
				resultList.add(pi.getId());
			}
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, resultList);
	}

	@Override
	public BaseResponse finishedProcess(Set<String> processInstanceIds) {
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
				.processInstanceIds(processInstanceIds).finished().orderByProcessInstanceId().desc().list();
		List<String> resultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (HistoricProcessInstance hpi : list) {
				resultList.add(hpi.getId());
			}
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, resultList);
	}

	@Override
	public BaseResponse isfinishedProcess(String processInstanceId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, pi == null);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse deleteProcessInstance(String processInstanceId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)// 使用流程实例ID查询
				.singleResult();
		try {
			if (pi == null) {
				// 该流程实例已经完成了
				historyService.deleteHistoricProcessInstance(processInstanceId);
			} else {
				// 该流程实例未结束的
				runtimeService.deleteProcessInstance(processInstanceId, "");
				historyService.deleteHistoricProcessInstance(processInstanceId);// (顺序不能换)
			}
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400080, ErrorCode.CODE_400080_MSG);
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}

	/**
	 * 根据流程实例Id,获取实时流程图片
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@Override
	public InputStream getFlowImgByInstantId(String processInstanceId) {
		// 获取流程图输入流
		InputStream inputStream = null;
		// 查询历史
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (historicProcessInstance.getEndTime() != null) { // 该流程已经结束
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
			inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
					processDefinition.getDiagramResourceName());
			return inputStream;
		}
		// 查询当前的流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
				.singleResult();
		List<String> highLightedFlows = new ArrayList<>();
		List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
		List<String> historicActivityInstanceList = new ArrayList<>();
		for (HistoricActivityInstance hai : historicActivityInstances) {
			historicActivityInstanceList.add(hai.getActivityId());
		}
		List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
		historicActivityInstanceList.addAll(highLightedActivities);
		for (ActivityImpl activity : processDefinitionEntity.getActivities()) {
			int index = historicActivityInstanceList.indexOf(activity.getId());
			if (index < 0 || index + 1 >= historicActivityInstanceList.size()) {
				continue;
			}
			List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
			for (PvmTransition pvmTransition : pvmTransitionList) {
				String destinationFlowId = pvmTransition.getDestination().getId();
				if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
					highLightedFlows.add(pvmTransition.getId());
				}
			}
		}
		ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration()
				.getProcessDiagramGenerator();
		List<String> activeActivityIds = new ArrayList<>();
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for (org.activiti.engine.task.Task task : tasks) {
			activeActivityIds.add(task.getTaskDefinitionKey());
		}
		inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows, "宋体",
				"宋体", "宋体", null, 1);
		return inputStream;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse createProcess(ProcessDetailslDto processDetailslDto) {
		// 1. Build up the model from scratch 开始构建模型
		BpmnModel model = new BpmnModel();
		Process process = new Process();
		model.addProcess(process);
		process.setName(processDetailslDto.getProcessName());// 设置流程名
		process.setId(processDetailslDto.getProcessDefinitionKey());// 设置流程key
		process.setDocumentation(processDetailslDto.getProcessDescription());// 设置流程描述
		process.addFlowElement(createStartEvent());// 创建开始事件
		process.addFlowElement(createEndEvent());// 创建结束事件
		List<TaskDetailsDto> nodeList = processDetailslDto.getNodeList();// 流程节点信息集合
		for (TaskDetailsDto taskDetails : nodeList) {
			process.addFlowElement(createUserTask(taskDetails));// 创建流程节点对象
		}
		process.addFlowElement(createSequenceFlow(START, processDetailslDto.getStartNode()));// 开始节点的连线
		for (Map<String, List<String>> sequenceInfo : processDetailslDto.getSequenceList()) {
			for (Map.Entry<String, List<String>> entry : sequenceInfo.entrySet()) {
				List<String> sequenceValue = entry.getValue();
				process.addFlowElement(createSequenceFlow(sequenceValue));// 创建流程节点的所有连线
			}
		}

		// 2. Generate graphical information 自动创建流程图
		new BpmnAutoLayout(model).execute();

		// 3. Deploy the process to the engine 将流程部署到引擎
		String tenantId = Constant.DEF_TENANT_ID;
		if(!StringUtils.isEmpty(processDetailslDto.getTenantId())) {
			tenantId = processDetailslDto.getTenantId();
		}
		Deployment deployment = repositoryService.createDeployment()
				.addBpmnModel(processDetailslDto.getProcessDefinitionKey() + ".bpmn", model)
				.name(processDetailslDto.getProcessDefinitionKey())
//				 .category(processDetailslDto.getCategory())
				.tenantId(tenantId).deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()).singleResult();
		//添加流程分类关系
		if(!StringUtils.isEmpty(processDetailslDto.getCategory())) {
			ActCategoryRela actCategoryRela = new ActCategoryRela();
			actCategoryRela.setCategoryId(Integer.valueOf(processDetailslDto.getCategory()));
			actCategoryRela.setProDefId(processDefinition.getId());
			actCategoryRelaMapper.insert(actCategoryRela);
		}
		// 创建流程时，是否任务期限设置
		if(CollectionUtils.isNotEmpty(processDetailslDto.getTaskLimitList())) {
			for(ActApdTaskLimitDef taskLimit: processDetailslDto.getTaskLimitList()) {
				taskLimit.setId(null);
				taskLimit.setProcDefId(processDefinition.getId());
				taskLimit.setTenantId(Constant.DEF_TENANT_ID);
			}
			actReProcdefMapper.addTaskLimitList(processDetailslDto.getTaskLimitList());
		}
		return BaseResponse.successed(Constant.ADD_MESSAGE, processDefinition.getId());
	}

	/** 创建用户任务 */
	protected static UserTask createUserTask(TaskDetailsDto taskDetails) {
		String taskKey = taskDetails.getTaskKey();
		String taskName = taskDetails.getTaskName();
		String assignee = taskDetails.getAssignee();
		List<String> candidateUsers = taskDetails.getCandidateUsers();
		List<String> candidateGroups = taskDetails.getCandidateGroups();
		List<FormPropertyDto> formPropertyDtos = taskDetails.getFormProperties();

		UserTask userTask = new UserTask();
		userTask.setName(taskName);
		userTask.setId(taskKey);
		if (!StringUtils.isEmpty(assignee)) {
			userTask.setAssignee(assignee);
		}
		if (CollectionUtils.isNotEmpty(candidateUsers)) {
			userTask.setCandidateUsers(candidateUsers);
		}
		if (CollectionUtils.isNotEmpty(candidateGroups)) {
			userTask.setCandidateGroups(candidateGroups);
		}
		if (CollectionUtils.isNotEmpty(formPropertyDtos)) {
			List<org.activiti.bpmn.model.FormProperty> formProperties = new ArrayList<>();
			for (FormPropertyDto formPropertyDto : formPropertyDtos) {
				org.activiti.bpmn.model.FormProperty fromProperty = new org.activiti.bpmn.model.FormProperty();
				fromProperty.setId(formPropertyDto.getId());// 表单ID
				fromProperty.setName(formPropertyDto.getName());// 表单名
				fromProperty.setType(formPropertyDto.getType());// 表单类型
				fromProperty.setVariable("#{" + formPropertyDto.getId() + "}");// 表单变量/表达式
				fromProperty.setReadable(formPropertyDto.getIsReadable());// 是否可读
				fromProperty.setWriteable(formPropertyDto.getIsWritable());// 是否可写
				fromProperty.setRequired(formPropertyDto.getIsRequired());// 是否必填
				if ("date".equals(formPropertyDto.getType())) {// 日期类型
					fromProperty.setDatePattern(formPropertyDto.getDatePattern());// 设置日期格式
				} else if ("enum".equals(formPropertyDto.getType())) {// 枚举类型
					Map<String, String> formValusMap = formPropertyDto.getInformationFormValues();// 下拉框值
					List<FormValue> formValues = new ArrayList<>();
					for (Map.Entry<String, String> entry : formValusMap.entrySet()) {
						FormValue formValue = new FormValue();
						formValue.setId(entry.getKey());
						formValue.setName(entry.getValue());
						formValues.add(formValue);
					}
					fromProperty.setFormValues(formValues);
				}
				formProperties.add(fromProperty);
			}
			userTask.setFormProperties(formProperties);
		}
		return userTask;
	}

	/** 创建流程线 */
	protected static SequenceFlow createSequenceFlow(List<String> sequenceValue) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(sequenceValue.get(0));
		flow.setTargetRef(sequenceValue.get(1));
		if (!START.equals(sequenceValue.get(0))) {
			String constantStr = "${targetNode=='";
			if (sequenceValue.size() == 2) {// 没有角色权限
				flow.setConditionExpression(constantStr + sequenceValue.get(1) + "'}");
			}
			if (sequenceValue.size() == 3) {// 仅有一个角色权限
				StringBuilder conditionExpression = new StringBuilder();
				conditionExpression.append(constantStr).append(sequenceValue.get(1)).append("'").append("&&auth=='")
						.append(sequenceValue.get(2)).append("'}");
				flow.setConditionExpression(conditionExpression.toString());
			}
			if (sequenceValue.size() > 3) {// 有多个角色权限
				StringBuilder conditionExpression = new StringBuilder();
				conditionExpression.append(constantStr).append(sequenceValue.get(1)).append("'").append("&&(auth=='")
						.append(sequenceValue.get(2)).append("'");
				for (int i = 3; i < sequenceValue.size(); i++) {
					conditionExpression.append("||auth=='").append(sequenceValue.get(i)).append("'");
				}
				conditionExpression.append(")}");
				flow.setConditionExpression(conditionExpression.toString());
			}

		}
		if (sequenceValue.size() > 2) {
			StringBuilder sb = new StringBuilder();
			for (int i = 2; i < sequenceValue.size(); i++) {
				sb.append(sequenceValue.get(i));
				sb.append(",");
			}
			flow.setDocumentation(sb.toString());
		}
		return flow;
	}

	/** 创建流程线 */
	protected static SequenceFlow createSequenceFlow(String from, String to) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		if (!START.equals(from)) {
			flow.setConditionExpression("${targetNode=='" + to + "'}");
		}
		return flow;
	}

	/** 创建开始节点 */
	protected static StartEvent createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(START);
		startEvent.setName("Start");
		return startEvent;
	}

	/** 创建结束节点 */
	protected static EndEvent createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("end");
		endEvent.setName("End");
		return endEvent;
	}

	@Override
	public BaseResponse queryLatestProcessDefinition(ProcessDetailslDto processDetailslDto) {
		/** api查询太慢，直接关联查表更快 */
		return BaseResponse.successed(actReProcdefMapper.queryProcessDefinition(processDetailslDto));
	}

	@Override
	@Transactional
	public byte[] getProcessDefinitionImage(String processDefinitionId) throws IOException {
		// 该方法最简单，但会乱码，原因不知
		// InputStream processDiagram =
		// repositoryService.getProcessDiagram(processDefinitionId);
		// 启动流程
		// ProcessInstance processInstance =
		// runtimeService.startProcessInstanceById(processDefinitionId);
		// Task task =
		// taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
		// 生成图片
		InputStream processDiagram = generator.generateDiagram(bpmnModel, "png", Collections.emptyList(),
				Collections.emptyList(), "宋体", "宋体", "宋体", null, 1);
		// 删除流程实例
		// deleteProcessInstance(processInstance.getId());
		return IOUtils.toByteArray(processDiagram);
	}

	@Override
	public boolean modifyOldProcDefToNew(String processDefinitionId) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId)
				.singleResult();
		String processDefinitionKey = pd.getKey();
		String tenantId = pd.getTenantId();

		List<String> oldProcDefId = new ArrayList<>();
		// 查处该流程定义的所有版本
		List<ProcessDefinition> queryProcessDefinitionList = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).processDefinitionTenantId(tenantId)
				.orderByProcessDefinitionVersion().desc().list();
		if (CollectionUtils.isEmpty(queryProcessDefinitionList)) {
			return false;
		}
		for (int i = 1; i < queryProcessDefinitionList.size(); i++) {
			oldProcDefId.add(queryProcessDefinitionList.get(i).getId());
		}
		ProcessDetailslDto processDetailslDto = new ProcessDetailslDto();
		processDetailslDto.setProcessDefinitionIds(oldProcDefId);
		processDetailslDto.setProcessDefinitionId(processDefinitionId);
		actReProcdefMapper.updateRuTaskDefId(processDetailslDto);
		actReProcdefMapper.updateHiTaskDefId(processDetailslDto);
		actReProcdefMapper.updateHiProcinstDefId(processDetailslDto);
		actReProcdefMapper.updateHiActDefId(processDetailslDto);
		actReProcdefMapper.updateRuExecDefId(processDetailslDto);
		return true;

	}

	@Override
	public BaseResponse taskKeyIsOld(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			return BaseResponse.failed(ErrorCode.CODE_200020, ErrorCode.CODE_200020_MSG);
		}
		// 获取任务的定义key
		String taskKey = task.getTaskDefinitionKey();
		// 获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		// 通过流程定义ID获取流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		// 获取流程定义的key
		String processDefinitionKey = processDefinition.getKey();
		// 获取最新版本的流程定义对象
		ProcessDefinition lastestPd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
		// 根据流程定义ID获取最新的流程节点集合
		BpmnModel model = repositoryService.getBpmnModel(lastestPd.getId());
		if (model != null) {
			Collection<FlowElement> flowElementList = model.getMainProcess().getFlowElements();
			for (FlowElement fe : flowElementList) {
				if (taskKey.equals(fe.getId())) {
					return BaseResponse.successed(true);
				}
			}
		}
		return BaseResponse.successed(false);
	}

	@Override
	public boolean taskChangeLastestDefinitionId(TaskDetailsDto taskDetailsDto) {
		String executionId = taskService.createTaskQuery().taskId(taskDetailsDto.getTaskId()).singleResult()
				.getExecutionId();
		taskDetailsDto.setExecutionId(executionId);
		actReProcdefMapper.updateRuTaskKey(taskDetailsDto);
		actReProcdefMapper.updateHiActinstKey(taskDetailsDto);
		actReProcdefMapper.updateHiTaskKey(taskDetailsDto);
		actReProcdefMapper.updateRuExecKey(taskDetailsDto);
		return true;
	}

	/**
	 * @author yujuan.xia
	 * @time 2020年4月9日
	 * @version 1.0
	 */
	@Override
	public TaskNextDefinitionDto getNextTaskDefinition(String taskId) {
		// 节点+流程线结果集
		TaskNextDefinitionDto taskNextDefinitionDto = new TaskNextDefinitionDto();
		List<SequenceFlowDto> sqDtos = new ArrayList<>();
		List<TaskDetailsDto> taskDetailsDtos = new ArrayList<>();
		Task task = null;
		try {
			task = taskService.createTaskQuery().taskId(taskId).singleResult();
		} catch (Exception e) {
			return taskNextDefinitionDto;
		}
		// 获取活动对象
		Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
		if (execution == null) {
			return taskNextDefinitionDto;
		}
		// 获取流程定义对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(task.getProcessDefinitionId());
		// 获取活动对象
		ActivityImpl activityImpls = processDefinitionEntity.findActivity(execution.getActivityId());
		// 获取当前的节点信息
		List<PvmTransition> aList = activityImpls.getOutgoingTransitions();
		for (PvmTransition pvmTransition : aList) {
			// 获取下一个节点对象
			PvmActivity pTransition = pvmTransition.getDestination();
			// 获取下一个节点类型
			String type = pTransition.getProperty("type").toString();
			if ("exclusiveGateway".equals(type)) {// 网关
				activityImpls = processDefinitionEntity.findActivity(pTransition.getId());
				List<PvmTransition> ps = activityImpls.getOutgoingTransitions();
				for (PvmTransition pvmTransition2 : ps) {
					// 获取路线
					sqDtos.add(packSequenceFlowDto(pvmTransition2));
					// 获取活动节点内容
					taskDetailsDtos.add(packTaskDetailsDto(pvmTransition2));
				}

			} else if ("userTask".equals(type)) {// 用户任务
				// 获取路线
				sqDtos.add(packSequenceFlowDto(pvmTransition));
				// 获取活动节点内容
				taskDetailsDtos.add(packTaskDetailsDto(pvmTransition));
			} else if ("endEvent".equals(type)) {
				sqDtos.add(packSequenceFlowDto(pvmTransition));
				taskNextDefinitionDto.setEventDefinitionDto(packEventDefinitionDto(pTransition));
			}
			taskNextDefinitionDto.setSequenceFlowDtos(sqDtos);
			taskNextDefinitionDto.setTaskDetailsDtos(taskDetailsDtos);
		}
		return taskNextDefinitionDto;
	}

	// 封装事件类型
	public EventDefinitionDto packEventDefinitionDto(PvmActivity pTransition) {
		EventDefinitionDto eventDefinitionDto = new EventDefinitionDto();
		eventDefinitionDto.setId(pTransition.getId());
		eventDefinitionDto
				.setName(pTransition.getProperty("name") != null ? pTransition.getProperty("name").toString() : null);
		eventDefinitionDto
				.setType(pTransition.getProperty("type") != null ? pTransition.getProperty("type").toString() : null);
		return eventDefinitionDto;
	}

	// 封装流程线对象
	public SequenceFlowDto packSequenceFlowDto(PvmTransition pvmTransition) {
		SequenceFlowDto sequenceFlowDto = new SequenceFlowDto();
		sequenceFlowDto.setId(pvmTransition.getId());
		sequenceFlowDto.setName(
				pvmTransition.getProperty("name") == null ? null : pvmTransition.getProperty("name").toString());
		sequenceFlowDto.setCondition(pvmTransition.getProperty("conditionText") == null ? null
				: pvmTransition.getProperty("conditionText").toString());
		sequenceFlowDto.setSourceRef(pvmTransition.getSource().getId());
		sequenceFlowDto.setTargetRef(pvmTransition.getDestination().getId());
		return sequenceFlowDto;
	}

	// 封装任务节点对象
	public TaskDetailsDto packTaskDetailsDto(PvmTransition pvmTransition) {
		TaskDefinition taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) pvmTransition.getDestination())
				.getActivityBehavior()).getTaskDefinition();
		TaskDetailsDto taskDetailsDto = new TaskDetailsDto();
		taskDetailsDto.setTaskKey(taskDefinition.getKey());
		taskDetailsDto.setTaskName((taskDefinition.getNameExpression()).getExpressionText());
		Expression expression = taskDefinition.getAssigneeExpression();
		if (expression != null) {
			taskDetailsDto.setAssignee(expression.getExpressionText());
		}
		List<String> candidateUsers = new ArrayList<>();
		List<String> candidateGroups = new ArrayList<>();
		// 获取候选人集合
		Set<Expression> expressions = taskDefinition.getCandidateUserIdExpressions();
		Iterator<Expression> iterator = expressions.iterator();
		while (iterator.hasNext()) {
			candidateUsers.add(iterator.next().toString());
		}
		// 获取候选组集合
		expressions = taskDefinition.getCandidateGroupIdExpressions();
		iterator = expressions.iterator();
		while (iterator.hasNext()) {
			candidateGroups.add(iterator.next().toString());
		}

		taskDetailsDto.setCandidateUsers(candidateUsers);
		taskDetailsDto.setCandidateGroups(candidateGroups);
		return taskDetailsDto;
	}

	@Override
	public BaseResponse getHistoricProcessInstanceList(String userId, String tenantId) {
		List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
				.processInstanceTenantId(tenantId).startedBy(userId).list();
		List<ProcessInstanceDto> processInstanceDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(historicProcessInstanceList)) {
			for (HistoricProcessInstance hpi : historicProcessInstanceList) {
				processInstanceDtos.add(packProcessInstanceDto(hpi));
			}
		}
		return BaseResponse.successed(processInstanceDtos);

	}

	@Override
	public String getProcessStartUser(String processInstanceId) {
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		return processInstance.getStartUserId();

	}

	@Override
	public void suspendProcessInstance(String processInstanceId) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
	}

	@Override
	public void activateProcessInstance(String processInstanceId) {
		runtimeService.activateProcessInstanceById(processInstanceId);

	}

	@Override
	public void suspendProcessDefinitionByKey(ProcessDetailslDto processDetailslDto) {
		repositoryService.suspendProcessDefinitionByKey(processDetailslDto.getProcessDefinitionKey(),
				processDetailslDto.getAllSuspend(), null, processDetailslDto.getTenantId());
	}

	@Override
	public void activateProcessDefinitionByKey(ProcessDetailslDto processDetailslDto) {
		repositoryService.activateProcessDefinitionByKey(processDetailslDto.getProcessDefinitionKey(), true, null,
				processDetailslDto.getTenantId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormPropertyDto> getTaskFormProperty(String taskId) {
		List<FormPropertyDto> result = new ArrayList<>();
		TaskFormData taskFormData = formService.getTaskFormData(taskId);
		if (taskFormData == null) {
			return result;
		}
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			FormPropertyDto formPropertyDto = new FormPropertyDto();
			formPropertyDto.setId(formProperty.getId());
			formPropertyDto.setName(formProperty.getName());
			formPropertyDto.setType(formProperty.getType().getName());
			formPropertyDto.setValue(formProperty.getValue());
			formPropertyDto.setIsReadable(formProperty.isReadable());
			formPropertyDto.setIsRequired(formProperty.isRequired());
			formPropertyDto.setIsWritable(formProperty.isWritable());
			String formType = formProperty.getType().getName();
			if ("enum".equals(formType)) {
				Object informationFormValues = formProperty.getType().getInformation("values");
				if (informationFormValues != null) {
					formPropertyDto.setInformationFormValues((Map<String, String>) informationFormValues);
				}
			} else if ("date".equals(formType)) {
				Object datePattern = formProperty.getType().getInformation("datePattern");
				if (datePattern != null) {
					formPropertyDto.setDatePattern(datePattern.toString());
				}
			}
			result.add(formPropertyDto);
		}
		return result;
	}

	@Override
	public List<FormPropertyDto> getHisFormProperty(String taskId) {
		List<FormPropertyDto> result = new ArrayList<>();
		List<HistoricDetail> list = historyService.createHistoricDetailQuery().taskId(taskId).formProperties().list();
		if (CollectionUtils.isEmpty(list)) {
			return result;
		}
		for (HistoricDetail hd : list) {
			HistoricFormProperty hfp = (HistoricFormProperty) hd;
			FormPropertyDto formPropertyDto = new FormPropertyDto();
			formPropertyDto.setId(hfp.getPropertyId());
			formPropertyDto.setName(hfp.getPropertyId());
			formPropertyDto.setValue(hfp.getPropertyValue());
			result.add(formPropertyDto);
		}
		return result;
	}

	@Override
	public void saveFormData(String taskId, Map<String, String> properties) {
		formService.saveFormData(taskId, properties);
	}

	@Override
	public BaseResponse processFreeJump(TaskDetailsDto taskDetailsDto) {
		String processInstanceId = taskDetailsDto.getProcessInstanceId();
		String taskDefinitionKey = taskDetailsDto.getTaskKey();
		try {
			TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().processInstanceId(processInstanceId)
					.active().singleResult();
			ActivityImpl pde = ProcessDefinitionUtils.getActivity(processEngine, taskEntity.getProcessDefinitionId(),
					taskDefinitionKey);
			executeCommand(new StartActivityCmd(taskEntity.getExecutionId(), pde));
			executeCommand(new DeleteRunningTaskCmd(taskEntity));
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_200020, ErrorCode.CODE_200020_MSG);
		}
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}
	
	@Override
	public BaseResponse processFreeJumpBatch(TaskDetailsDto taskDetailsDto) {
		List<String> processInstanceIds = taskDetailsDto.getProcessInstanceIds();
		List<String> errorProcessInstanceIds = new ArrayList<>();
		for(String processInstanceId : processInstanceIds) {
			String taskDefinitionKey = taskDetailsDto.getTaskKey();
			try {
				TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().processInstanceId(processInstanceId)
						.active().singleResult();
				ActivityImpl pde = ProcessDefinitionUtils.getActivity(processEngine, taskEntity.getProcessDefinitionId(),
						taskDefinitionKey);
				executeCommand(new StartActivityCmd(taskEntity.getExecutionId(), pde));
				executeCommand(new DeleteRunningTaskCmd(taskEntity));
			} catch (Exception e) {
				errorProcessInstanceIds.add(processInstanceId);
			}
		}
		if(CollectionUtils.isNotEmpty(errorProcessInstanceIds)) {
			return BaseResponse.failed(ErrorCode.CODE_200020, ErrorCode.CODE_200020_MSG, errorProcessInstanceIds);
		}else {
			return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
		}
	}

	@Override
	public BaseResponse backTask(TaskDetailsDto taskDetailsDto) {
		String processInstanceId = taskDetailsDto.getProcessInstanceId();
		List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().desc().list();
		if (CollectionUtils.isNotEmpty(historicTaskInstanceList)) {
			try {
				TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().processInstanceId(processInstanceId)
						.active().singleResult();
				HistoricTaskInstance historicTaskInstance = historicTaskInstanceList.get(0);
				ActivityImpl pde = ProcessDefinitionUtils.getActivity(processEngine,
						taskEntity.getProcessDefinitionId(), historicTaskInstance.getTaskDefinitionKey());
				executeCommand(new StartActivityCmd(taskEntity.getExecutionId(), pde));
				executeCommand(new DeleteRunningTaskCmd(taskEntity));
			} catch (Exception e) {
				return BaseResponse.failed(ErrorCode.CODE_200020, ErrorCode.CODE_200020_MSG);
			}
			return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
		} else {
			return BaseResponse.failed(ErrorCode.CODE_200020, ErrorCode.CODE_200020_MSG);
		}

	}

	private void executeCommand(Command<java.lang.Void> command) {
		((ServiceImpl) runtimeService).getCommandExecutor().execute(command);
	}

	@Override
	public ProcessDetailslDto getProDetailByProDefId(String proDefId) {
		ProcessDetailslDto processDetailslDto = new ProcessDetailslDto();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(proDefId).singleResult();
		processDetailslDto.setProcessDefinitionKey(processDefinition.getKey());
		processDetailslDto.setCategory(processDefinition.getCategory());
		processDetailslDto.setProcessDefinitionId(processDefinition.getId());
		processDetailslDto.setProcessName(processDefinition.getName());
		processDetailslDto.setProcessDescription(processDefinition.getDescription());
		processDetailslDto.setTenantId(processDefinition.getTenantId());
		processDetailslDto.setVersion(processDefinition.getVersion());
		return processDetailslDto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse codyProcess(ProcessDetailslDto processDetailslDto){
    	List<String> processDefinitionIds = processDetailslDto.getProcessDefinitionIds();
    	List<String> newProcessDefinitionIds = new ArrayList<>();
    	for(String processDefinitionId : processDefinitionIds){
    		ProcessDetailslDto processDetailDto = getProcessByProDefId(processDefinitionId); 
    		processDetailDto.setTenantId(processDetailslDto.getTenantId());
    		// 新的流程定义ID
    		String newProcessDefId = createProcess(processDetailDto).getData().toString();
    		newProcessDefinitionIds.add(newProcessDefId);
    		
    		// 将原流程定义的按钮绑定关系 拷贝到 新的流程定义按钮绑定中,start
    		ButtonRelaDto buttonRelaDto = new ButtonRelaDto();
    		buttonRelaDto.setProcDefId(processDefinitionId);
    		List<ButtonRelaDto> oldBtnList = actReProcdefMapper.queryBtnRela(buttonRelaDto);
    		if(CollectionUtils.isNotEmpty(oldBtnList)) {
    			List<ButtonRelaDto> newBtnList = new ArrayList<>();
    			for(ButtonRelaDto oldBtn : oldBtnList) {
    				oldBtn.setProcDefId(newProcessDefId);
    				newBtnList.add(oldBtn);
    			}
    			actReProcdefMapper.addBtnRela(newBtnList);
    		}
    		// end
    		
    		// 将原流程的分类信息绑定到新流程上
    		EntityWrapper<ActCategoryRela> ew1 = new EntityWrapper<>();
    		ew1.eq("PRODEF_ID_", processDefinitionId);
    		List<ActCategoryRela> oldActCategoryRelaList = actCategoryRelaMapper.selectList(ew1);
    		if(CollectionUtils.isNotEmpty(oldActCategoryRelaList)) {
    			for(ActCategoryRela actCategoryRela: oldActCategoryRelaList) {
    				actCategoryRela.setId(null);
    				actCategoryRela.setProDefId(newProcessDefId);
    			}
    			actReProcdefMapper.addActCategoryRela(oldActCategoryRelaList);
    		}
    		//end
    		
    		// 将原流程的“任务节点逾期时间定义” 拷贝,start
    		EntityWrapper<ActApdTaskLimitDef> ew = new EntityWrapper<>();
    		ew.eq("TENANT_ID_", Constant.DEF_TENANT_ID)
    			.eq("PROC_DEF_ID_", processDefinitionId);
    		List<ActApdTaskLimitDef> taskLimitDefs = actApdTaskLimitDefMapper.selectList(ew);
    		if(CollectionUtils.isNotEmpty(taskLimitDefs)) {
    			for(ActApdTaskLimitDef actApdTaskLimitDef: taskLimitDefs) {
    				actApdTaskLimitDef.setId(null);
    				actApdTaskLimitDef.setProcDefId(newProcessDefId);
    				actApdTaskLimitDef.setTenantId(processDetailslDto.getTenantId());
    			}
    			actReProcdefMapper.addTaskLimitList(taskLimitDefs);
    		}
    		//end
    		
    		// 将原流程的“状态配置信息”拷贝 start
    		List<StatusConfigDto>  actApdStatusConfList= queryProcessStatusConfigGroup(processDefinitionId, null, null);
    		if(CollectionUtils.isNotEmpty(actApdStatusConfList)) {
    			for(StatusConfigDto dto: actApdStatusConfList) {
    				dto.setProcDefId(newProcessDefId);
    				dto.setTenantId(processDetailslDto.getTenantId());
    				dto.setProcDefKey(processDetailDto.getProcessDefinitionKey());
    			}
    			addProcessStatusConfig(actApdStatusConfList);
    		}
    		//end
        }
    	return BaseResponse.successed(newProcessDefinitionIds);
		
	}

	private ProcessDetailslDto getProcessByProDefId(String processDefinitionId) {
		ProcessDetailslDto processDetailslDto = new ProcessDetailslDto();
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
		List<TaskDetailsDto> taskDetails = new ArrayList<>();
		List<Map<String, List<String>>> sequenceList = new ArrayList<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<String> endNodes = new ArrayList<>();
		if (model != null) {
			ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
			processDetailslDto.setProcessDefinitionKey(processDefinition.getKey());
			processDetailslDto.setProcessDefinitionId(processDefinitionId);
			processDetailslDto.setProcessName(processDefinition.getName());
			processDetailslDto.setProcessDescription(processDefinition.getDescription());
			processDetailslDto.setTenantId(processDefinition.getTenantId());
			Collection<FlowElement> flowElementList = model.getMainProcess().getFlowElements();
			String startNode = "";
			String endNode = "";
			for (FlowElement fe : flowElementList) {
				if (fe instanceof Event) {// 开始结束事件
					if ("Start".equals(fe.getName())) {
						startNode = fe.getId();
					}
					if ("End".equals(fe.getName())) {
						endNode = fe.getId();
					}
				}
			}
			for (FlowElement fe : flowElementList) {
				Map<String, Object> map = new HashMap<>();
				if (fe instanceof UserTask) {// 任务节点
					TaskDetailsDto taskDetailsDto = new TaskDetailsDto();
					List<FormPropertyDto> formPropertiesList = new ArrayList<>();
					taskDetailsDto.setTaskName(fe.getName());
					taskDetailsDto.setTaskKey(fe.getId());
					taskDetailsDto.setAssignee(((UserTask) fe).getAssignee());
					taskDetailsDto.setCandidateUsers(((UserTask) fe).getCandidateUsers());
					taskDetailsDto.setCandidateGroups(((UserTask) fe).getCandidateGroups());
					
					List<org.activiti.bpmn.model.FormProperty> formProperties = ((UserTask) fe).getFormProperties();
					for (org.activiti.bpmn.model.FormProperty fromProperty : formProperties) {
						FormPropertyDto formPropertyDto = new FormPropertyDto();
						Map<String, String> valueMap = new HashMap<>();
						formPropertyDto.setId(fromProperty.getId());
						formPropertyDto.setName(fromProperty.getName());
						List<FormValue> valueList = fromProperty.getFormValues();
						for (FormValue formValue : valueList) {
							valueMap.put(formValue.getId(), formValue.getName());
						}
						formPropertyDto.setInformationFormValues(valueMap);
						formPropertyDto.setDatePattern(fromProperty.getDatePattern());
						formPropertyDto.setType(fromProperty.getType());
						formPropertyDto.setIsReadable(fromProperty.isReadable());
						formPropertyDto.setIsRequired(fromProperty.isRequired());
						formPropertyDto.setIsWritable(fromProperty.isWriteable());
						formPropertiesList.add(formPropertyDto);
					}
					taskDetailsDto.setFormProperties(formPropertiesList);
					taskDetails.add(taskDetailsDto);
				} else if (fe instanceof SequenceFlow) {// 流程线
					Map<String, List<String>> sequenceMap = new HashMap<>();
					List<String> flowList = new ArrayList<>();
					String sourceRef = ((SequenceFlow) fe).getSourceRef();
					String targetRef = ((SequenceFlow) fe).getTargetRef();
					String documentation = ((SequenceFlow) fe).getDocumentation();
					if (sourceRef.contains(startNode)) {
						processDetailslDto.setStartNode(targetRef);
					}
					if (targetRef.contains(endNode)) {
						endNodes.add(sourceRef);
					}
					if (!sourceRef.contains(startNode)) {
						flowList.add(sourceRef);
						flowList.add(targetRef);
						if (!StringUtils.isEmpty(documentation)) {
							List<String> roles = Arrays.asList(documentation.split(","));
							flowList.addAll(roles);
						}
						String key = sourceRef + targetRef;
						sequenceMap.put(key, flowList);
						sequenceList.add(sequenceMap);
					}
				}
				// 根据定义ID获取逾期设置
				EntityWrapper<ActApdTaskLimitDef> ew = new EntityWrapper<ActApdTaskLimitDef>();
				ew.eq("PROC_DEF_ID_", processDefinitionId);
				List<ActApdTaskLimitDef> taskLimitList = actApdTaskLimitDefMapper.selectList(ew);
				if(CollectionUtils.isNotEmpty(taskLimitList)) {
					processDetailslDto.setTaskLimitList(taskLimitList);
				}
				processDetailslDto.setEndNodes(endNodes);
				processDetailslDto.setNodeList(taskDetails);
				processDetailslDto.setSequenceList(sequenceList);
				resultList.add(map);
			}
		}

		return processDetailslDto;
	}
	
	@Override
	public List<Map<String, String>> getTaskSortByProDefId(String processDefinitionId, String processInstanceId, String processKey) {
		if(StringUtils.isEmpty(processDefinitionId)) {
			if(StringUtils.isEmpty(processInstanceId)) {
				List<ProcessDefinition> pds = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey)
						.processDefinitionTenantId(Constant.DEF_TENANT_ID).latestVersion().list();
				processDefinitionId = pds.get(0).getId();
			}else {
				HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
						.processInstanceId(processInstanceId).singleResult();
				processDefinitionId = hpi.getProcessDefinitionId();
			}
		}
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
		List<String> nodeTaskSortList = new ArrayList<String>();
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		if (model != null) {
			Collection<FlowElement> flowElementList = model.getMainProcess().getFlowElements();
			String startNode = "";
			for (FlowElement fe : flowElementList) {
				if (fe instanceof Event) {// 开始结束事件
					if ("Start".equals(fe.getName())) {
						startNode = fe.getId();
					}
				}
			}
			
			// 记录所有节点的key-name
			Map<String, String> nodeKeyName = new HashMap<>();
			// 记录来源-目标节点的map
			Map<String, List<String>> stMap = new HashMap<>();
			String startNodeKey = "";
			// 遍历节点-流程线
			for (FlowElement fe : flowElementList) {
				if (fe instanceof UserTask) {// 任务节点
					nodeKeyName.put(fe.getId(), fe.getName());
				} else if (fe instanceof SequenceFlow) {// 流程线
					String sourceRef = ((SequenceFlow) fe).getSourceRef();
					String targetRef = ((SequenceFlow) fe).getTargetRef();
					if (sourceRef.contains(startNode)) {
						startNodeKey = targetRef;
					}
					if (!sourceRef.contains(startNode)) {
						List<String> targetNode = stMap.get(sourceRef);
						if(CollectionUtils.isEmpty(targetNode)) {
							targetNode = new ArrayList<String>();
						}
						targetNode.add(targetRef);
						stMap.put(sourceRef, targetNode);
					}
				}
			}
			// 递归-整理task顺序
			nodeTaskSortList.add(startNodeKey);
			nodeTaskSortList = getSortList(startNodeKey, stMap, nodeTaskSortList);
			// 将taskName封装进去
			for(String nodeTask: nodeTaskSortList) {
				Map<String, String> keyNameMap = new HashMap<String, String>();
				keyNameMap.put("taskKey", nodeTask);
				keyNameMap.put("taskName", nodeKeyName.get(nodeTask));
				resultList.add(keyNameMap);
			}
		}

		return resultList;
	}
	
	public List<String> getSortList(String sourceNode, Map<String, List<String>> stMap, List<String> resultList) {
		List<String> targetNodes = stMap.get(sourceNode);
		for(String targetNode: targetNodes) {
			if("end".equals(targetNode)) {// 到了流程终点
				return resultList;
			}
			if(!resultList.contains(targetNode)) {
				resultList.add(targetNode);
				getSortList(targetNode, stMap, resultList);
			}
		}
		return resultList;
	}
	

	@Override
	public Map<String, List<ProcessInstanceDto>> getProcessInstanceByKey(String processkey, String processInstanceName,
			String tenantId) {
		Map<String, List<ProcessInstanceDto>> map = new HashMap<>();

		List<HistoricProcessInstance> historicProInstanceList = new ArrayList<>();
		if (processInstanceName != null) {
			historicProInstanceList = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey(processkey).processInstanceTenantId(tenantId)
					.processInstanceName("%" + processInstanceName + "%").finished().list();
		} else {
			historicProInstanceList = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey(processkey).processInstanceTenantId(tenantId).finished().list();
		}
		List<ProcessInstanceDto> processInstanceList = new ArrayList<>();
		for (HistoricProcessInstance historicProcessInstance : historicProInstanceList) {
			processInstanceList.add(packProcessInstanceDto(historicProcessInstance));
		}
		List<HistoricProcessInstance> unHistoricProInstanceList = new ArrayList<>();
		if (processInstanceName != null) {
			unHistoricProInstanceList = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey(processkey).processInstanceTenantId(tenantId)
					.processInstanceName("%" + processInstanceName + "%").unfinished().list();
		} else {
			unHistoricProInstanceList = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey(processkey).processInstanceTenantId(tenantId).unfinished().list();
		}
		List<ProcessInstanceDto> unProcessInstanceList = new ArrayList<>();
		for (HistoricProcessInstance historicProcessInstance : unHistoricProInstanceList) {
			unProcessInstanceList.add(packProcessInstanceDto(historicProcessInstance));
		}
		map.put("unfinish", unProcessInstanceList);
		map.put("finish", processInstanceList);
		return map;
	}

	@Override
	public Map<String, List<ProcessInstanceDto>> getProcessInstanceById(String proDefId, String processInstanceName) {
		Map<String, List<ProcessInstanceDto>> map = new HashMap<>();
		List<HistoricProcessInstance> historicProInstanceList = new ArrayList<>();
		if (processInstanceName != null) {
			historicProInstanceList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(proDefId)
					.processInstanceNameLike("%" + processInstanceName + "%").finished().list();
		} else {
			historicProInstanceList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(proDefId)
					.finished().list();
		}
		List<ProcessInstanceDto> processInstanceList = new ArrayList<>();
		for (HistoricProcessInstance historicProcessInstance : historicProInstanceList) {
			processInstanceList.add(packProcessInstanceDto(historicProcessInstance));
		}
		List<HistoricProcessInstance> unHistoricProInstanceList = new ArrayList<>();
		if (processInstanceName != null) {
			unHistoricProInstanceList = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionId(proDefId).processInstanceNameLike("%" + processInstanceName + "%").unfinished()
					.list();
		} else {
			unHistoricProInstanceList = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionId(proDefId).unfinished().list();
		}
		List<ProcessInstanceDto> unProcessInstanceList = new ArrayList<>();
		for (HistoricProcessInstance historicProcessInstance : unHistoricProInstanceList) {
			unProcessInstanceList.add(packProcessInstanceDto(historicProcessInstance));
		}
		map.put("unfinish", unProcessInstanceList);
		map.put("finish", processInstanceList);
		return map;

	}

	public ProcessInstanceDto packProcessInstanceDto(HistoricProcessInstance historicProcessInstance) {
		ProcessInstanceDto processInstanceDto = new ProcessInstanceDto();
		processInstanceDto.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
		processInstanceDto.setProcessInstanceId(historicProcessInstance.getId());
		processInstanceDto.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
		processInstanceDto.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
		processInstanceDto.setStartTime(DateUtils.formatDateTime(historicProcessInstance.getStartTime()));
		processInstanceDto.setEndTime(DateUtils.formatDateTime(historicProcessInstance.getEndTime()));
		processInstanceDto.setProcessInstanceName(historicProcessInstance.getName());
		processInstanceDto.setStartUserId(historicProcessInstance.getStartUserId());
		return processInstanceDto;
	}

	@Override
	public BaseResponse getProcessInstanceByInstId(String processInstanceId) {
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		ProcessInstanceDto processInstanceDto = packProcessInstanceDto(hpi);
		return BaseResponse.successed(processInstanceDto);
	}

	@Override
	public BaseResponse getProcessInstanceByTaskId(String taskId) {
		HistoricTaskInstance hti = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		String processInstanceId = hti.getProcessInstanceId();
		return getProcessInstanceByInstId(processInstanceId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse deleteHisFormValue(String taskId) {
		actReProcdefMapper.deleteHisFormValue(taskId);
		return BaseResponse.successed(Constant.SUCCESS);
	}

	@Override
	public ProcessDefinition getProcessDefinitionById(String processDefinitionId) {
		return repositoryService.getProcessDefinition(processDefinitionId);
	}

	@Override
	public BaseResponse queryInstallkeys() {
		return BaseResponse.successed(actReProcdefMapper.queryInstallkeys());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse addBtnRela(List<ButtonRelaDto> buttonRelaDtoList) {
		actReProcdefMapper.addBtnRela(buttonRelaDtoList);
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse updBtnRela(List<ButtonRelaDto> buttonRelaDtoList) {
		actReProcdefMapper.updBtnRela(buttonRelaDtoList);
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}

	@Override
	public BaseResponse queryBtnRela(ButtonRelaDto buttonRelaDto) {
		return BaseResponse.successed(actReProcdefMapper.queryBtnRela(buttonRelaDto));
	}

	@Override
	public BaseResponse queryBtnRelaByProcInstId(String processInstanceId, String btnName) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskCreateTime()
				.desc().list();
		if (CollectionUtils.isNotEmpty(taskList)) {
			Task task = taskList.get(0);
			ButtonRelaDto buttonRelaDto = new ButtonRelaDto();
			buttonRelaDto.setBtnName(btnName);
			buttonRelaDto.setFromTaskKey(task.getTaskDefinitionKey());
			buttonRelaDto.setProcDefId(task.getProcessDefinitionId());
			return BaseResponse.successed(actReProcdefMapper.queryBtnRela(buttonRelaDto));
		} else {
			return BaseResponse.failed(ErrorCode.CODE_400030, ErrorCode.CODE_400030_MSG);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResponse updateRuTaskKeyBatch(ProcessDetailslDto processDetailslDto) {
		actReProcdefMapper.updateRuTaskKeyBatch(processDetailslDto);
		actReProcdefMapper.updateHiActinstKeyBatch(processDetailslDto);
		actReProcdefMapper.updateHiTaskKeyBatch(processDetailslDto);
		actReProcdefMapper.updateRuExecKeyBatch(processDetailslDto);
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}

	@Override
	public BaseResponse completeTaskV2(Map<String, Object> map) {
		String processInstanceId = ParamMapUtils.getString(map, "processInstanceId");
		if (StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		String btnName = ParamMapUtils.getString(map, "btnName");
		if (StringUtils.isEmpty(btnName)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		// 获取该实例的当前任务
		List<Task> taskList = null;
		try {
			taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).orderByTaskId().desc().list();
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400080, ErrorCode.CODE_400080_MSG);
		}
		if (CollectionUtils.isEmpty(taskList)) {// 无当前任务
			return BaseResponse.failed(ErrorCode.CODE_400020, ErrorCode.CODE_400020_MSG);
		}
		if (taskList.size() > 1) {// 多个当前任务
			return BaseResponse.failed(ErrorCode.CODE_400030, ErrorCode.CODE_400030_MSG);
		}
		Task task = taskList.get(0);
		
		// 获取流转路线（即按钮绑定关系）
		ButtonRelaDto buttonRelaDto = new ButtonRelaDto();
		buttonRelaDto.setBtnName(btnName);
		buttonRelaDto.setFromTaskKey(task.getTaskDefinitionKey());
		buttonRelaDto.setProcDefId(task.getProcessDefinitionId());
		List<ButtonRelaDto> queryBtnRela = actReProcdefMapper.queryBtnRela(buttonRelaDto);
		ButtonRelaDto buttonRelaDto1 = null;
		if (CollectionUtils.isNotEmpty(queryBtnRela)) {
			buttonRelaDto1 = queryBtnRela.get(0);
		}
		if (StringUtils.isEmpty(buttonRelaDto1)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		// 流程流转目标节点
		String targetTaskKey = buttonRelaDto1.getTargetTaskKey();
		if (StringUtils.isEmpty(targetTaskKey)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		String auth = ParamMapUtils.getString(map, "auth");
		if (StringUtils.isEmpty(auth)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		// 检测目标节点的合理性
		boolean flag = targetNodeIsUseful(targetTaskKey, task.getId())
				&& flowAuthIsUseful(targetTaskKey, auth, task.getId());
		if (!flag) {
			return BaseResponse.failed(ErrorCode.CODE_400050, ErrorCode.CODE_400050_MSG);
		}
		
		// 任务完成人，记录进变量
		String operater = ParamMapUtils.getString(map, OPERATER);
		if (!StringUtils.isEmpty(operater)) {
			taskService.setVariableLocal(task.getId(), OPERATER, operater);
		}
		// 任务完成人的企业名称，记录进变量
		String entpName = ParamMapUtils.getString(map, "entpName");
		if (!StringUtils.isEmpty(entpName)) {
			taskService.setVariableLocal(task.getId(), "entpName", entpName);
		}
		// 操作按钮，记录进变量
		taskService.setVariableLocal(task.getId(), "btnName", btnName);
		map.put("targetNode", buttonRelaDto1.getTargetTaskKey());
		// 完成任务
		try {
			taskService.complete(task.getId(), map);
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400100, ErrorCode.CODE_400100_MSG);
		}
		// 更新流程状态表（新版本未用）
		buttonRelaDto1.setProcInstId(processInstanceId);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(new Date());
		buttonRelaDto1.setTime(format);
		if (!StringUtils.isEmpty(buttonRelaDto1.getStatus())) {
			actReProcdefMapper.updProcinstStatus(buttonRelaDto1);
		}else {
			buttonRelaDto1.setStatus(buttonRelaDto1.getTargetTaskName());
			actReProcdefMapper.updProcinstStatus(buttonRelaDto1);
		}
		
		// 更新当前实例当前任务的流转企业
		if(!"end".equals(targetTaskKey)) {// 流程没结束
			// 根据上一个taskkey和下一个taskkey获取跨级级别
			Integer step  = getActApdBtnRela(task.getProcessDefinitionId(), task.getTaskDefinitionKey(), targetTaskKey).getStep();
			if(step != null) {
				// 根据跨级级别+entpCode 调组织机构查询下一个节点的entpCode
				String toEntpCode = null;
				ActApdProcinstEntp apdProcinstEntp = getActApdProcinstEntp(processInstanceId);
				if(step == -1) {// 降级流转
					toEntpCode = apdProcinstEntp.getEntpMarks();
				}else if(step == 0) {// 平级流转
					toEntpCode = apdProcinstEntp.getToEntpMarks();
				}else if(step == 1 || step == 2) {// 跨级流转
					String orgaVersion = ParamMapUtils.getString(map, "orgaVersion");// 集团版组织机构版本ID
					
					toEntpCode = orgaizationSerivce.getToEntpCode(apdProcinstEntp.getTenantId(), apdProcinstEntp.getToEntpMarks(), step, orgaVersion);
				}
				// 将下一个节点的entpCode更新到ACT_APD_PROCINST_ENTP表
				if(toEntpCode != null) {
					apdProcinstEntp.setToEntpMarks(toEntpCode);
					actApdProcinstEntpMapper.updateById(apdProcinstEntp);
				}
			}
		}
		
		return findCurrTaskByProcessInstanceId(processInstanceId, null);
	}
	
	public ActApdBtnRela getActApdBtnRela(String processDefId, String fromKey, String targetKey) {
		EntityWrapper<ActApdBtnRela> ew = new EntityWrapper<>();
		ew.eq("FROM_TASK_KEY_", fromKey).eq("TARGET_TASK_KEY_", targetKey).eq("PROC_DEF_ID_", processDefId);
		List<ActApdBtnRela> actApdBtnRela = actApdBtnRelaMapper.selectList(ew);
		if(CollectionUtils.isEmpty(actApdBtnRela)) {
			return new ActApdBtnRela();
		}else {
			return actApdBtnRela.get(0);
		}
	}
	
	@Override
	public BaseResponse queryDefByTenantId(String tenantId) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionTenantId(tenantId)
				.orderByProcessDefinitionVersion().asc() //按照版本的倒序排列
				.list();
		List<String> defIDs = new ArrayList<String>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(ProcessDefinition def: list) {
				defIDs.add(def.getId());
			}
		}
		return BaseResponse.successed(defIDs);
	}


	@Override
	public List<TaskDetailsDto> getHisFormPropertyByProcessId(String processInstanceId) {
		List<HistoricDetail> historicDetailList = null;
		List<TaskDetailsDto> resultList = new ArrayList<>();
		List<HistoricTaskInstance> list =  historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询对象
				.processInstanceId(processInstanceId).finished().list();
		
		if(CollectionUtils.isNotEmpty(list)){
			for(HistoricTaskInstance hit : list){
				TaskDetailsDto taskDetailsDto = packageTaskDtoByHiTask(hit);
				String taskId = hit.getId();
				historicDetailList = historyService.createHistoricDetailQuery().taskId(taskId).formProperties().list();
				if(CollectionUtils.isEmpty(historicDetailList)) {
					continue;
				}
				List<FormPropertyDto> formList = new ArrayList<>();
				for(HistoricDetail hd : historicDetailList) {
					HistoricFormProperty hfp = (HistoricFormProperty) hd;
					FormPropertyDto formPropertyDto = new FormPropertyDto();
					formPropertyDto.setId(hfp.getPropertyId());
					formPropertyDto.setName(hfp.getPropertyId());
			    	formPropertyDto.setValue(hfp.getPropertyValue());
			    	formList.add(formPropertyDto);
				}
				taskDetailsDto.setFormProperties(formList);
				resultList.add(taskDetailsDto);
			}
		}
		return resultList;
	}

	@Override
	public List<ActApdBtnRela> queryBtnByTask(TaskDetailsDto taskDetailsDto) {
		// 根据实例ID查询流程定义
		String procDefId = taskDetailsDto.getProcessDefinitionId();
		if(StringUtils.isEmpty(procDefId)) {
			HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskDetailsDto.getProcessInstanceId())
					.singleResult();
			procDefId = pi.getProcessDefinitionId();
		}
				
		EntityWrapper<ActApdBtnRela> ew = new EntityWrapper<>();
		ew.eq("PROC_DEF_ID_",  procDefId);
		if(!StringUtils.isEmpty(taskDetailsDto.getTaskKey())) {
			ew.eq("FROM_TASK_KEY_", taskDetailsDto.getTaskKey());
		}else {
			ew.eq("FROM_TASK_NAME_", taskDetailsDto.getTaskName());
		}
		if(!StringUtils.isEmpty(taskDetailsDto.getRoleCode())) {
			ew.like("ROLES", taskDetailsDto.getRoleCode());
		}
		return actApdBtnRelaMapper.selectList(ew);
	}

	@Override
	public List<String> findProcInstIdsByEntp(String entpMarks) {
		EntityWrapper<ActApdProcinstEntp> ew = new EntityWrapper<>();
		ew.eq("TO_ENTP_MARKS_", entpMarks);
		List<ActApdProcinstEntp> actApdProcinstEntps = actApdProcinstEntpMapper.selectList(ew);
		if(CollectionUtils.isEmpty(actApdProcinstEntps)) {
			return new ArrayList<String>();
		}else {
			List<String> procInstIds = new ArrayList<String>();
			for(ActApdProcinstEntp obj: actApdProcinstEntps) {
				procInstIds.add(obj.getProcInstId());
			}
			return procInstIds;
		}
	}
	
	public ActApdProcinstEntp getActApdProcinstEntp(String processInstanceId) {
		EntityWrapper<ActApdProcinstEntp> ew = new EntityWrapper<>();
		ew.eq("PROC_INST_ID_", processInstanceId);
		List<ActApdProcinstEntp> actApdProcinstEntps = actApdProcinstEntpMapper.selectList(ew);
		if(CollectionUtils.isEmpty(actApdProcinstEntps)) {
			return new ActApdProcinstEntp();
		}else {
			return actApdProcinstEntps.get(0);
		}
	}

	@Override
	public BaseResponse queryBtnByAuth(List<TaskDetailsDto> taskDetailsDtos) {
		List<List<ActApdBtnRela>> resultList = new ArrayList<>();
		for(TaskDetailsDto dto: taskDetailsDtos ) {
			if(	(StringUtils.isEmpty(dto.getProcessInstanceId()) && StringUtils.isEmpty(dto.getProcessDefinitionId()))
					|| (StringUtils.isEmpty(dto.getTaskKey()) && StringUtils.isEmpty(dto.getTaskName())) 
//					||StringUtils.isEmpty(dto.getRoleCode())
					) {
				resultList.add(new ArrayList<ActApdBtnRela>());
				continue;
			}
			resultList.add(this.queryBtnByTask(dto));
		}
		return BaseResponse.successed(resultList);
	}

	@Override
	public List<ActApdStatusConf> queryProcessStatusConfig(String processDefinitionId, String processDefinitionKey,
			String tenantId) {
		EntityWrapper<ActApdStatusConf> ew = new EntityWrapper<>();
		if(!StringUtils.isEmpty(processDefinitionId)) {
			ew.eq("PROC_DEF_ID_", processDefinitionId);
		}
		if(!StringUtils.isEmpty(processDefinitionKey)) {
			ew.eq("PROC_DEF_KEY_", processDefinitionKey);
		}
		if(!StringUtils.isEmpty(tenantId)) {
			ew.eq("TENANT_ID_", tenantId);
		}
		return actApdStatusConfMapper.selectList(ew);
	}

	@Override
	@Transactional
	public BaseResponse addProcessStatusConfig(List<StatusConfigDto> statusConfigDtos) {
		// 查询数据库是否已存在该流程定义的状态配置，若有则删除
		String processDefinitionId = statusConfigDtos.get(0).getProcDefId();
		String processDefinitionKey = statusConfigDtos.get(0).getProcDefKey();
		String tenantId = statusConfigDtos.get(0).getTenantId();
		List<ActApdStatusConf> actApdStatusConfList = queryProcessStatusConfig(processDefinitionId, processDefinitionKey, tenantId);
		if(CollectionUtils.isNotEmpty(actApdStatusConfList)) {
			List<Integer> ids = new ArrayList<Integer>();
			for(ActApdStatusConf actApdStatusConf : actApdStatusConfList) {
				ids.add(actApdStatusConf.getId());
			}
			actApdStatusConfMapper.deleteBatchIds(ids);
		}
		//新增
		List<ActApdStatusConf> insertList = new ArrayList<ActApdStatusConf>();
		for(StatusConfigDto dto: statusConfigDtos) {
			List<String> taskKeys = dto.getTaskKeys();
			for (int i = 0; i < taskKeys.size(); i++) {
				ActApdStatusConf actApdStatusConf = new ActApdStatusConf();
				actApdStatusConf.setBigStatusName(dto.getBigStatusName());
				actApdStatusConf.setProcDefId(dto.getProcDefId());
				actApdStatusConf.setProcDefKey(dto.getProcDefKey());
				actApdStatusConf.setTaskKey(taskKeys.get(i));
				actApdStatusConf.setTaskName(dto.getTaskNames().get(i));
				actApdStatusConf.setTenantId(dto.getTenantId());
				insertList.add(actApdStatusConf);
			}
		}
		actReProcdefMapper.addStatusConfig(insertList);
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}

	@Override
	public List<StatusConfigDto> queryProcessStatusConfigGroup(String processDefinitionId, String processDefinitionKey,
			String tenantId) {
		StatusConfigDto dto = new StatusConfigDto();
		dto.setProcDefId(processDefinitionId);
		dto.setProcDefKey(processDefinitionKey);
		dto.setTenantId(tenantId);
		List<StatusConfigDto> result = actReProcdefMapper.queryProcessStatusConfigGroup(dto);
		if(CollectionUtils.isNotEmpty(result)) {
			for(StatusConfigDto statusConfigDto: result) {
				if(!StringUtils.isEmpty(statusConfigDto.getTaskKey())) {
					statusConfigDto.setTaskKeys(Arrays.asList(statusConfigDto.getTaskKey().split(",")));
					statusConfigDto.setTaskNames(Arrays.asList(statusConfigDto.getTaskName().split(",")));
				}
			}
		}
		return result;
	}

	


	




}
