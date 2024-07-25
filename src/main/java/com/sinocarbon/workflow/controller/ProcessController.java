package com.sinocarbon.workflow.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.MapUtils;
import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.polaris.commons.utils.Constant;
import com.sinocarbon.polaris.commons.utils.ErrorCode;
import com.sinocarbon.polaris.commons.utils.ParamMapUtils;
import com.sinocarbon.workflow.dto.ButtonRelaDto;
import com.sinocarbon.workflow.dto.ProcessDetailslDto;
import com.sinocarbon.workflow.dto.StatusConfigDto;
import com.sinocarbon.workflow.dto.TaskDetailsDto;
import com.sinocarbon.workflow.pojo.ActApdStatusConf;
import com.sinocarbon.workflow.service.ProcessService;
import com.sinocarbon.workflow.utils.CommonUtils;


/**
 * <p>
 *  工作流流程控制器
 * </p>
 *
 * @author sxc
 * @since 2019-07-31
 */
@RestController
@RequestMapping
public class ProcessController {
	
	@Autowired
	private ProcessService processService;
	
	/**
	 * 部署流程
	 * @param deploymentName 流程部署名称
	 * @param bpmnResourceUrl 流程bpmn文件路径
	 * @param picResourceUrl 流程png文件路径
	 * @param tenantId 租户ID
	 * @return 部署详情
	 */
	@PostMapping(value = "/process/deploy")
	public BaseResponse deployProcess(@RequestBody Map<String,Object> params) {
		String deploymentName = ParamMapUtils.getString(params, "deploymentName");
		String bpmnResourceUrl = ParamMapUtils.getString(params, "bpmnResourceUrl");
		String picResourceUrl = ParamMapUtils.getString(params, "picResourceUrl");
		String tenantId = ParamMapUtils.getString(params, "tenantId");
		return processService.deployProcess(deploymentName, bpmnResourceUrl, picResourceUrl, tenantId);
	}
	
	/**
	 * 启动流程
	 * @param processesKey 流程文件的key
	 * @param startUser 启动人
	 * @param tenantId 租户ID
	 * @param map 流程变量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/process/start")
	public BaseResponse startProcesses(@RequestBody Map<String,Object> params){
		String processesKey = ParamMapUtils.getString(params, "processDefinitionKey");
		String startUser = ParamMapUtils.getString(params, "startUser");
		String tenantId = ParamMapUtils.getString(params, "tenantId");
		String processesId = ParamMapUtils.getString(params, "processDefinitionId");
//		String entpMarks = ParamMapUtils.getString(params, "entpMarks");// 企业标识
		List<String> entpMarks = (List<String>)params.get("entpMarks");// 企业标识集合
		String processInstanceName = ParamMapUtils.getString(params, "processInstanceName");
		Map<String,Object> map = (Map<String,Object>) params.get("map");
		if (!StringUtils.isEmpty(processesId)) {
			return processService.startProcessesByDefId(processesId, processInstanceName, startUser, map);
		}else {
			if(StringUtils.isEmpty(processesKey) || StringUtils.isEmpty(tenantId)) {
				return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
			}
			return processService.startProcesses(processesKey, processInstanceName, startUser, map, tenantId, entpMarks);
		}
	}
	
	/**
	 * 查询个人当前任务
	 * @param assignee 办理人
	 * @param tenantId 租户ID
	 * @return
	 */
	@GetMapping(value = "/task/person")
	public BaseResponse findPersonTaskByAssignee(String assignee, String tenantId){
		if(StringUtils.isEmpty(assignee) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findPersonTaskByAssignee(assignee,tenantId);
	}
	
	/**
	 * 查询组当前任务
	 * @param group 组名
	 * @param tenantId 租户ID
	 * @return
	 */
	@GetMapping(value = "/task/group")
	public BaseResponse findGroupTaskByAssignee(String group, String tenantId, String processInstanceIds){
		
		if(StringUtils.isEmpty(group) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findGroupTaskByAssignee(group, tenantId, processInstanceIds);
	}
	
	/**
	 * 查询组当前任务-数据量比较大的流程
	 * @param group 组名
	 * @param tenantId 租户ID
	 * @return
	 */
	@PostMapping(value = "/task/group")
	public BaseResponse findGroupTaskByAssigneeLarge(@RequestBody Map<String,Object> params){
		String group = ParamMapUtils.getString(params, "group"); 
		String tenantId = ParamMapUtils.getString(params, "tenantId");  
		String processInstanceIds = ParamMapUtils.getString(params, "processInstanceIds");  
		if(StringUtils.isEmpty(group) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findGroupTaskByAssignee(group, tenantId, processInstanceIds);
	}
	
	/**
	 * 根据角色+企业标识，查询当前任务，即待办
	 * @param role 角色标识
	 * @param tenantId 租户ID
	 * @param entpMarks 企业标识
	 * @return
	 */
	@GetMapping(value = "/task/role/entp")
	public BaseResponse findTodoTaskByRoleAndEntp(String role, String tenantId, String entpMarks){
		
		if(StringUtils.isEmpty(role) || StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(entpMarks) ) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		// 根据企业标识查询初所有实例ID
		List<String> procInstIds = processService.findProcInstIdsByEntp(entpMarks);
		String processInstanceIds = CommonUtils.praseListToString(procInstIds);
		return processService.findGroupTaskByAssignee(role, tenantId, processInstanceIds);
	}
	
	/**
	 * 查询个人历史任务
	 * @param assignee 办理人
	 * @return
	 */
	@GetMapping(value = "/task/history/person")
	public BaseResponse findPersonHistoryTask(String assignee, String tenantId){
		if(StringUtils.isEmpty(assignee) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findPersonHistoryTask(assignee, tenantId);
	}
	
	/**
	 * 查询组历史任务
	 * @param assignee 办理人
	 * @return
	 */
	@GetMapping(value = "/task/history/group")
	public BaseResponse findGroupHistoryTask(String group, String tenantId, String processInstanceIds){
		if(StringUtils.isEmpty(group) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findGroupHistoryTask(group, tenantId, processInstanceIds);
	}
	
	/**
	 * 查询单条实例的历史任务
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@GetMapping(value = "/task/history/inst")
	public BaseResponse findGroupHistoryTaskByProInstId(String processInstanceId){
		if(StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findGroupHistoryTaskByProInstId(processInstanceId);
	}
	
	/**
	 * 查询组历史任务-数据量比较大的流程
	 * @param group 组名
	 * @param tenantId 租户ID
	 * @return
	 */
	@PostMapping(value = "/task/history/group")
	public BaseResponse findGroupHistoryTaskLarge(@RequestBody Map<String,Object> params){
		String group = ParamMapUtils.getString(params, "group"); 
		String tenantId = ParamMapUtils.getString(params, "tenantId");  
		String processInstanceIds = ParamMapUtils.getString(params, "processInstanceIds");  
		if(StringUtils.isEmpty(group) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findGroupHistoryTask(group, tenantId, processInstanceIds);
	}
	
	
	/**
	 * 查询任务详情
	 * @param taskId 任务ID
	 * @return
	 */
	@GetMapping(value = "/task")
	public BaseResponse findTask(String taskId){
		return processService.findTask(taskId);
	}
	
	/**
	 * 向任务添加候选人组
	 * @param taskId: 任务ID
	 * @param assignee: 任务指定人
	 * @param candidateUsers: 任务候选人
	 * @param candidateGroups: 任务候选组
	 * @return
	 */
	@PostMapping(value = "/task/assignee")
	public BaseResponse addGroupUser(@RequestBody TaskDetailsDto taskDetailsDto){
		if(StringUtils.isEmpty(taskDetailsDto.getTaskId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		if(StringUtils.isEmpty(taskDetailsDto.getAssignee()) && 
				StringUtils.isEmpty(taskDetailsDto.getCandidateGroups()) &&
				StringUtils.isEmpty(taskDetailsDto.getCandidateUsers()) ) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.addGroupUser(taskDetailsDto);
	}
	
	/**
	 * 向任务删除办理人
	 * @param taskId： 任务ID
	 * @param candidateUsers: 任务候选人
	 * @param candidateGroups: 任务候选组
	 * @return
	 */
	@DeleteMapping(value = "/task/assignee")
	public BaseResponse deleteGroupUser(@RequestBody TaskDetailsDto taskDetailsDto){
		if(StringUtils.isEmpty(taskDetailsDto.getTaskId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		if(StringUtils.isEmpty(taskDetailsDto.getAssignee()) && 
				StringUtils.isEmpty(taskDetailsDto.getCandidateGroups()) &&
				StringUtils.isEmpty(taskDetailsDto.getCandidateUsers()) ) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.removeGroupUser(taskDetailsDto);
	}
	
	/**
	 * 审批任务/完成任务(当前任务)
	 * 针对权限在任务候选人组上
	 * @param params processInstanceId：流程实例ID
	 * @param params map：审批任务条件/流程变量
	 * @return
	 */
	@PostMapping(value = "/task/complete")
	public BaseResponse completeTask(@RequestBody Map<String,Object> params){
		if(MapUtils.isEmpty(params)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.completeTask(params,1);
	}
	
	/**
	 * 审批任务/完成任务(当前任务)
	 * 针对权限添加在流程线上
	 * @param params processInstanceId：流程实例ID
	 * @param params map：审批任务条件/流程变量
	 * @return
	 */
	@PostMapping(value = "/task/complete/auth")
	public BaseResponse completeTaskWithAuth(@RequestBody Map<String,Object> params){
		if(MapUtils.isEmpty(params)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.completeTask(params,2);
	}
	
	/**
	 * 审批任务/完成任务(当前任务)
	 * 针对权限添加在流程线上
	 * @param params processInstanceId：流程实例ID
	 * @param params map：审批任务条件/流程变量
	 * @return
	 */
	@PatchMapping(value = "/task/completes/auth/patch")
	public BaseResponse completeTasksWithAuth(@RequestBody Map<String,Object> params){
		if(MapUtils.isEmpty(params)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.completeTasks(params,2);
	}
	
	/**
	 * 原版的完成任务
	 * 直接通过线上权限流转
	 * @param params processInstanceId：流程实例ID
	 * @param params map：审批任务条件/流程变量
	 * @return
	 */
	@PostMapping(value = "/task/complete/old")
	public BaseResponse completeTaskOld(@RequestBody Map<String,Object> params){
		System.out.println("======进入完成任务的方法");
		if(MapUtils.isEmpty(params)) {
			System.out.println("======入参body为空");
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.completeTaskOld(params);
	}
	
	/**
	 * 通过流程的key来查询所有流程定义
	 * @param processkey 流程key
	 * @return 流程定义
	 */
	@GetMapping(value = "/process/definition/key")
	public BaseResponse findProcessDefinitionByKey(String processDefinitionKey,String tenantId ){
		if(StringUtils.isEmpty(processDefinitionKey) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findProcessDefinition(processDefinitionKey,tenantId);
	}
	
	/**
	 * 根据流程key/id删除流程定义(一次部署对应一个流程定义)
	 * @param processKsy 流程key
	 * @return
	 */
	@DeleteMapping(value = "/process/definition/key")
	public BaseResponse deleteProcessDefinition(@RequestBody ProcessDetailslDto processDetailslDto){
		String processDefinitionKey=processDetailslDto.getProcessDefinitionKey();
		String processDefinitionId = processDetailslDto.getProcessDefinitionId();
		String tenantId=processDetailslDto.getTenantId();
		return processService.deleteProcessDefinition(processDefinitionKey,processDefinitionId,tenantId);
	}
	
	/**
	 * 根据流程实例ID查询该流程的当前任务
	 * @param processInstanceId
	 * @return 当前任务集合
	 */
	@GetMapping(value = "/task/current-process")
	public BaseResponse findCurrTaskByProcessInstanceId(String processInstanceId, String tenantId){
		if(StringUtils.isEmpty(processInstanceId) && StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findCurrTaskByProcessInstanceId(processInstanceId, tenantId);
	}
	
	/**
	 * 根据流程实例ID查询该流程的历史
	 * @param processInstanceId
	 * @return 当前任务集合
	 */
	@GetMapping(value = "/task/history-process")
	public BaseResponse findHisTaskByProcessInstanceId(String processInstanceId, String tenantId){
		if(StringUtils.isEmpty(processInstanceId) && StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.findHisTaskByProcessInstanceId(processInstanceId, tenantId);
	}
	
	/**
	 * 根据流程定义ID获取流程节点信息
	 * @param processDefinitionId 流程定义ID
	 * @return
	 */
	@GetMapping(value = "/process/activities")
	public BaseResponse getProcessActivity(String processDefinitionId, String processInstanceId,String processKey, String tenantId){
		if(StringUtils.isEmpty(processDefinitionId) && StringUtils.isEmpty(processInstanceId) 
				&& ( StringUtils.isEmpty(processKey) || StringUtils.isEmpty(tenantId)) ) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.getProcessActivity(processDefinitionId, processInstanceId, processKey, tenantId);
	}
	
	
	/**
	 * 查询已结束的流程
	 * @param processInstanceIds 流程实例ID集合
	 * @return
	 */
	@PostMapping(value = "/process/finished")
	public BaseResponse finishedProcess(@RequestBody Set<String> processInstanceIds){
		return processService.finishedProcess(processInstanceIds);
	}
	
	/**
	 * 查询未结束的流程
	 * @param processInstanceIds 流程实例ID集合
	 * @return
	 */
	@PostMapping(value = "/process/unfinished")
	public BaseResponse unfinishedProcess(@RequestBody Set<String> processInstanceIds){
		return processService.unfinishedProcess(processInstanceIds);
	}
	
	/**
	 * 查询流程是否结束（未）
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@GetMapping(value = "/process/isfinished")
	public BaseResponse isfinishedProcess(String processInstanceId){
		return processService.isfinishedProcess(processInstanceId);
	}
	
	/**
	 * 删除流程实例
	 * @param processInstanceId 流程实例ID
	 * @return
	 */
	@DeleteMapping(value = "/process/instance")
	public BaseResponse deleteProcessInstance(String processInstanceId){
		if(StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.deleteProcessInstance(processInstanceId);
	}
	
	/**
	 * 查看任务流程图片，并标记当前流程节点
	 * @param processInstanceId 流程实例ID
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/process/image")
    public String currentProcessInstanceImage(String processInstanceId, HttpServletResponse response) throws IOException {
		if(StringUtils.isEmpty(processInstanceId)) {
			return "";
		}
//        InputStream inputStream = processService.getFlowImgByInstantId(processInstanceId);
		InputStream inputStream = processService.currentProcessInstanceImage(processInstanceId);
		if(inputStream == null) {
			return "";
		}
		byte[] data = IOUtils.toByteArray(inputStream);
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(data);
    }
	
	
	/**
	 * 页面创建流程信息并部署
	 * @param processBpmnModelDto 流程信息
	 * @return 流程部署ID
	 * @throws IOException
	 */
	@PostMapping(value = "/process/create/deploy")
	public BaseResponse createProcess(@RequestBody ProcessDetailslDto processDetailslDto) throws IOException {
		if(StringUtils.isEmpty(processDetailslDto.getProcessDefinitionKey())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’流程key‘");
		}
		if(StringUtils.isEmpty(processDetailslDto.getProcessName())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’流程名称‘");
		}
		if(StringUtils.isEmpty(processDetailslDto.getStartNode())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’流程开始节点‘");
		}
//		if(CollectionUtils.isEmpty(processDetailslDto.getEndNodes())) {
//			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’流程结束节点‘");
//		}
		if(CollectionUtils.isEmpty(processDetailslDto.getNodeList())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’流程节点信息‘");
		}
		if(CollectionUtils.isEmpty(processDetailslDto.getSequenceList())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’流程流向信息‘");
		}
		return processService.createProcess(processDetailslDto);
	}
	
	/**
	 * 查询最新版本的流程定义
	 * @return
	 */
	@GetMapping("/process/definition")
	public BaseResponse queryLatestProcessDefinition(ProcessDetailslDto processDetailslDto) {
		if(StringUtils.isEmpty(processDetailslDto.getTenantId())) {
			processDetailslDto.setTenantId(com.sinocarbon.workflow.utils.Constant.DEF_TENANT_ID);
		}
		
		return processService.queryLatestProcessDefinition(processDetailslDto);
	}

	/**
	 * 下载查看流程定义的流程图
	 * @param processDefinitionId
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/process/definition/image")
	public String getProcessDefinitionImage(String processDefinitionId) throws IOException {
		if(StringUtils.isEmpty(processDefinitionId)) {
			return "";
		}
		byte[] data = processService.getProcessDefinitionImage(processDefinitionId);
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(data);
	}
	
	/**
	 * 覆盖流程后，将旧版本的流程定义改成指定版本的流程定义ID，并将旧版本的正在执行的任务key修改成新流程上的key
	 * @param processDefinitionKey 流程定义的key
	 * @return 
	 */
	@PostMapping("/process/definition/old2new")
	public BaseResponse modifyOldProcDefToNew(@RequestBody ProcessDetailslDto processDetailslDto) {
		String processDefinitionId = processDetailslDto.getProcessDefinitionId();
		if(StringUtils.isEmpty(processDefinitionId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		// 修改流程定义ID
		boolean result = processService.modifyOldProcDefToNew(processDefinitionId);
		
		if(result) {
			if(CollectionUtils.isNotEmpty(processDetailslDto.getOld2newTaskKeys())) {
				// 修改任务key
				return processService.updateRuTaskKeyBatch(processDetailslDto);
			}
			return BaseResponse.successed(Constant.SUCCESS);
		}else {
			return BaseResponse.failed(ErrorCode.CODE_200040, ErrorCode.CODE_200040_MSG);
		}
	}
	
	/**
	 * 判断某任务的key是否在最新流程定义中存在。
	 * 基于流程修改版本升级后，旧流程实例上的当前任务是否在新流程定义节点中存在。
	 * @param taskId 任务ID
	 * @return true/false
	 */
	@GetMapping("/task/key/latest")
	public BaseResponse taskKeyIsLastest(String taskId) {
		return processService.taskKeyIsOld(taskId);
	}
	
	/**
	 * 根据任务ID修改任务的key
	 * @param taskDetailsDto
	 * @param taskId 需要修改的任务ID
	 * @param taskKey 需要给成的任务key
	 * @return true/false
	 */
	@PostMapping("/task/key")
	public BaseResponse taskChangeLastestDefinitionId(@RequestBody TaskDetailsDto taskDetailsDto) {
		if(StringUtils.isEmpty(taskDetailsDto.getTaskId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’taskId‘");
		}
		if(StringUtils.isEmpty(taskDetailsDto.getTaskKey())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少必要参数’taskKey‘");
		}
		boolean result = processService.taskChangeLastestDefinitionId(taskDetailsDto);
		return BaseResponse.successed(result);
	}
	
	

	/**
	 * 查询当前任务节点的下个任务节点对象
	 * @param taskId
	 * @return
	 */
	@GetMapping("/task/next")
	public BaseResponse getNextTaskDefinition(String taskId){
		return BaseResponse.successed(processService.getNextTaskDefinition(taskId));
	}
	
	
	/**
	 * 查询某人发起的流程
	 * @param userId（用户Id）
	 * @param tenantId（租户Id）
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/process/startuser/process")
	public BaseResponse getHistoricProcessInstanceList(String startUser,String tenantId){
		if(StringUtils.isEmpty(startUser) || StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.getHistoricProcessInstanceList(startUser,tenantId);
	}
	
	/**
	 * 获取流程的发起人
	 * @param processInstanceId
	 * @return String 流程发起人
	 */
	@GetMapping("/process/startuser")
	public BaseResponse getProcessStartUser(String processInstanceId) {
		return BaseResponse.successed(Constant.SUCCESS,processService.getProcessStartUser(processInstanceId));
	}
	

	/**
	 * 挂起一个流程实例
	 * @param processInstanceId
	 * @return
	 */
	@PutMapping("/process/instance/suspend")
	public BaseResponse suspendProcessInstance(String processInstanceId) {
		if(StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 processService.suspendProcessInstance(processInstanceId);
		 return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}
	
	/**
	 * 激活一个流程实例
	 * @param processInstanceId
	 * @return
	 */
	@PutMapping("/process/instance/activate")
	public BaseResponse activateProcessInstance(String processInstanceId) {
		if(StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 processService.activateProcessInstance(processInstanceId);
		 return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}
	
	/**
	 * 挂起一个流程(设置是否挂起流程下的所有运行的流程实例)
	 * @param processDefinitionKey(流程定义的key)
	 * @param allSuspend (是否级联挂起该流程定义下的流程实例)
	 * @return
	 */
	@PutMapping("/process/definition/suspend")

	public BaseResponse suspendProcessDefinitionByKey(@RequestBody ProcessDetailslDto processDetailslDto ) {
		if(StringUtils.isEmpty(processDetailslDto.getProcessDefinitionKey())
				|| StringUtils.isEmpty(processDetailslDto.getAllSuspend())
				|| StringUtils.isEmpty(processDetailslDto.getTenantId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 processService.suspendProcessDefinitionByKey(processDetailslDto);
		 return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}
	
	/**
	 * 激活一个流程(设置是否挂起流程下的所有运行的流程实例)
	 * @param processDefinitionKey(流程定义的key)
	 * @param allSuspend (是否级联挂起该流程定义下的流程实例)
	 * @return
	 */
	@PutMapping("/process/definition/activate")
	public BaseResponse activateProcessDefinitionByKey(@RequestBody ProcessDetailslDto processDetailslDto ) {
		if(StringUtils.isEmpty(processDetailslDto.getProcessDefinitionKey())
				|| StringUtils.isEmpty(processDetailslDto.getTenantId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 processService.activateProcessDefinitionByKey(processDetailslDto);
		 return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}
	
	/**
	 * 获取用户任务的表单属性
	 * @param taskId
	 * @return
	 */
	@GetMapping("/task/form")
	public BaseResponse getTaskFormProperty(String taskId) {
		if(StringUtils.isEmpty(taskId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.getTaskFormProperty(taskId));
	}
	
	/**
	 * 获取用户历史任务的表单属性
	 * @param taskId
	 * @return
	 */
	@GetMapping("/task/form/his")
	public BaseResponse getHisFormProperty(String taskId) {
		if(StringUtils.isEmpty(taskId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.getHisFormProperty(taskId));
	}
	
	/**
	 * 获取流程的所有历史任务表单
	 * @param taskId
	 * @return
	 */
	@GetMapping("/process/form/his")
	public BaseResponse getHisFormPropertyByProcessId(String processInstanceId) {
		if(StringUtils.isEmpty(processInstanceId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.getHisFormPropertyByProcessId(processInstanceId));
	}
	
	/**
	 * 给用户任务表单赋值
	 * @param taskDetailsDto
	 * @param taskId 任务ID
	 * @param properties 表单值
	 * @return
	 */
	@PostMapping("/task/form")
	public BaseResponse saveFormData(@RequestBody TaskDetailsDto taskDetailsDto) {
		if(StringUtils.isEmpty(taskDetailsDto.getTaskId()) || 
				MapUtils.isEmpty(taskDetailsDto.getProperties())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		processService.saveFormData(taskDetailsDto.getTaskId(),taskDetailsDto.getProperties());
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE);
	}
	
	/**
	 * 流程实例自由跳转任意节点
	 * @return
	 */
	@PostMapping("/task/move")
	public BaseResponse freeJump( @RequestBody TaskDetailsDto taskDetailsDto) {
		if(StringUtils.isEmpty(taskDetailsDto.getProcessInstanceId()) ||
				StringUtils.isEmpty(taskDetailsDto.getTaskKey())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 return processService.processFreeJump(taskDetailsDto);
	}
	
	/**
	 * 流程实例自由跳转任意节点(批量)
	 * @return
	 */
	@PostMapping("/task/move/batch")
	public BaseResponse freeJumpBatch( @RequestBody TaskDetailsDto taskDetailsDto) {
		if(CollectionUtils.isEmpty(taskDetailsDto.getProcessInstanceIds()) ||
				StringUtils.isEmpty(taskDetailsDto.getTaskKey())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 return processService.processFreeJumpBatch(taskDetailsDto);
	}
	
	/**
	 * 流程实例向前跳转
	 * @param taskId(任务Id)
	 * @return
	 */
	@PutMapping("/task/back")
	public BaseResponse backTask( @RequestBody TaskDetailsDto taskDetailsDto) {
		if(StringUtils.isEmpty(taskDetailsDto.getProcessInstanceId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		 return processService.backTask(taskDetailsDto);
	}
	
	
	/**
	 * 拷贝流程信息
	 * @param List 流程定义ID
	 * @return
	 * @throws IOException 
	 */
	@PostMapping("/process/copy")
	public BaseResponse codyProcess(@RequestBody ProcessDetailslDto processDetailslDto){
		if(CollectionUtils.isEmpty(processDetailslDto.getProcessDefinitionIds())
				|| StringUtils.isEmpty(processDetailslDto.getTenantId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.codyProcess(processDetailslDto);
	}
	
	/**
	 * 通过流程定义ID或流程Key获取流程实例列表
	 * @param processDetailslDto
	 * @return
	 */
	@GetMapping("/process/instance")
	public BaseResponse getProcessInstance(ProcessDetailslDto processDetailslDto)  {
		String processDefinitionId = processDetailslDto.getProcessDefinitionId();
		String processDefinitionKey = processDetailslDto.getProcessDefinitionKey();
		String tenantId = processDetailslDto.getTenantId();
		String processInstanceName=processDetailslDto.getProcessInstanceName();
		if (!StringUtils.isEmpty(processDefinitionId)) {
			return BaseResponse.successed(processService.getProcessInstanceById(processDefinitionId,processInstanceName));
		}else if(!StringUtils.isEmpty(processDefinitionKey) && !StringUtils.isEmpty(tenantId)){
			return BaseResponse.successed(processService.getProcessInstanceByKey(processDefinitionKey,processInstanceName,tenantId));
		}else {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
	}
	
	/**
	 * 通过实例ID或任务ID获取流程实例信息
	 * @param taskDetailsDto
	 * @return
	 */
	@GetMapping("/process/instance/task")
	public BaseResponse getProcessInstance(TaskDetailsDto taskDetailsDto) {
		String taskId = taskDetailsDto.getTaskId();
		String processInstanceId = taskDetailsDto.getProcessInstanceId();
		if(!StringUtils.isEmpty(processInstanceId)) {
			return processService.getProcessInstanceByInstId(processInstanceId);
		}else if(!StringUtils.isEmpty(taskId)) {
			return processService.getProcessInstanceByTaskId(taskId);
		}else {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
	}
	
	/**
	 * 通过任务ID删除ACT_HI_DETAIL表里的表单信息
	 * 出现情况：给任务表单赋值之后，完成任务失败，此时需要删除赋值表单的数据
	 * @param taskId
	 * @return
	 */
	@DeleteMapping(value = "/task/form")
	public BaseResponse deleteHisFormValue(String taskId) {
		if(StringUtils.isEmpty(taskId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.deleteHisFormValue(taskId);
	}
	
	/**
	 * 获取系统内置节点key
	 * @return
	 */
	@GetMapping(value = "/taskkey")
	public BaseResponse queryInstallkeys() {
		return processService.queryInstallkeys();
	}
	
	/**
	 * 添加状态流转与页面按钮关系
	 * @param buttonRelaDtoList
	 * @return
	 */
	@PostMapping(value = "/btn")
	public BaseResponse addBtnRela(@RequestBody List<ButtonRelaDto> buttonRelaDtoList) {
		if(CollectionUtils.isEmpty(buttonRelaDtoList)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.addBtnRela(buttonRelaDtoList);
	}
	
	/**
	 * 修改状态流转与页面按钮关系
	 * @param buttonRelaDtoList
	 * @return
	 */
	@PutMapping(value = "/btn")
	public BaseResponse updBtnRela(@RequestBody List<ButtonRelaDto> buttonRelaDtoList) {
		if(CollectionUtils.isEmpty(buttonRelaDtoList)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.updBtnRela(buttonRelaDtoList);
	}
	
	/**
	 * 查询流转状态与页面按钮关系
	 * @param buttonRelaDto
	 * @return
	 */
	@GetMapping(value = "/btn")
	public BaseResponse queryBtnRela(ButtonRelaDto buttonRelaDto) {
		// 流程定义ID必传
		if(buttonRelaDto == null || StringUtils.isEmpty(buttonRelaDto.getProcDefId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.queryBtnRela(buttonRelaDto);
	}
	
	/**
	 * 根据流程实例和按钮名，获取下一步对应的节点key
	 * @param processInstanceId
	 * @param btnName
	 * @return
	 */
	@GetMapping(value = "/btn/instance")
	public BaseResponse queryBtnRelaByProcInstId(String processInstanceId, String btnName) {
		if(StringUtils.isEmpty(processInstanceId) || StringUtils.isEmpty(btnName)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.queryBtnRelaByProcInstId(processInstanceId, btnName);
	}
	
	/**
	 * 审批任务/完成任务(当前任务)
	 * 根据流程实例和按钮名，获取下⼀步对应的节点key和审批任务/完成任务(当前任务)两个接⼝整合
	 * @param params processInstanceId：流程实例ID
	 * @param params btnName：按钮名称
	 * @param params auth：权限
	 * @param params operater：完成人
	 * @return
	 */
	@PostMapping(value = "/task/complete/v2")
	public BaseResponse completeTaskWithAuthV2(@RequestBody Map<String,Object> params){
		if(MapUtils.isEmpty(params)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.completeTaskV2(params);
	}
	
	/**
	 * 根据租户ID，查看该租户的所有流程
	 * @param processInstanceId
	 * @param btnName
	 * @return
	 */
	@GetMapping(value = "/process/tenant")
	public BaseResponse queryDefByTenantId(String tenantId) {
		if(StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.queryDefByTenantId(tenantId);
	}
	
	/**
	 * 根据流程实例和节点名称，获取流转按钮信息
	 * @param processInstanceId
	 * @param taskKey
	 * @param taskName
	 * @return
	 */
	@GetMapping(value = "/task/btn")
	public BaseResponse queryBtnByTask(TaskDetailsDto taskDetailsDto) {
		if((StringUtils.isEmpty(taskDetailsDto.getProcessInstanceId()) &&  StringUtils.isEmpty(taskDetailsDto.getProcessDefinitionId()))|| 
				(StringUtils.isEmpty(taskDetailsDto.getTaskKey()) && StringUtils.isEmpty(taskDetailsDto.getTaskName()))) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.queryBtnByTask(taskDetailsDto));
	}
	
	/**
	 * 根据流程定义ID获取流程节点顺序
	 * 仅支持仅有一个开始节点、一个结束节点且流程有序的流程（经典碳资产流程）
	 * @param processDefinitionId
	 * @return
	 */
	@GetMapping(value = "/task/sort")
	public BaseResponse getTaskSortByProDefId(String processDefinitionId, String processInstanceId, String processKey) {
		if(StringUtils.isEmpty(processDefinitionId) && StringUtils.isEmpty(processInstanceId) && StringUtils.isEmpty(processKey)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.getTaskSortByProDefId(processDefinitionId, processInstanceId, processKey));
	}
	
	/**
	 * 根据流程实例+节点名称+角色，获取流转按钮信息
	 * @param processInstanceId
	 * @param taskKey
	 * @param taskName
	 * @return
	 */
	@PostMapping(value = "/task/btns")
	public BaseResponse queryBtnByAuth(@RequestBody List<TaskDetailsDto> taskDetailsDtos) {
		if(CollectionUtils.isEmpty(taskDetailsDtos)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.queryBtnByAuth(taskDetailsDtos);
	}
	
	/**
	 * 查询流程的状态配置列表
	 * @param processDefinitionId
	 * @param processDefinitionKey
	 * @param tenantId
	 * @return
	 */
	@GetMapping(value = "/process/status/config")
	public BaseResponse queryProcessStatusConfig(String processDefinitionId, String processDefinitionKey, String tenantId ) {
		if(StringUtils.isEmpty(processDefinitionId)  && (StringUtils.isEmpty(processDefinitionKey) || StringUtils.isEmpty(tenantId))) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.queryProcessStatusConfig(processDefinitionId, processDefinitionKey, tenantId));
	}
	
	/**
	 * 查询流程的状态配置-组装
	 * @param processDefinitionId
	 * @param processDefinitionKey
	 * @param tenantId
	 * @return
	 */
	@GetMapping(value = "/process/status/config/group")
	public BaseResponse queryProcessStatusConfigGroup(String processDefinitionId, String processDefinitionKey, String tenantId ) {
		if(StringUtils.isEmpty(processDefinitionId)  && (StringUtils.isEmpty(processDefinitionKey) || StringUtils.isEmpty(tenantId))) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed(processService.queryProcessStatusConfigGroup(processDefinitionId, processDefinitionKey, tenantId));
	}
	
	/**
	 * 新增流程的状态配置
	 * @return
	 */
	@PostMapping(value = "/process/status/config")
	public BaseResponse addProcessStatusConfig(@RequestBody List<StatusConfigDto> statusConfigDtos) {
		if(CollectionUtils.isEmpty(statusConfigDtos)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return processService.addProcessStatusConfig(statusConfigDtos);
	}
	
}

