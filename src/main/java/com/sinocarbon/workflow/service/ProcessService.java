package com.sinocarbon.workflow.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.TableStringConverter;

import org.activiti.engine.repository.ProcessDefinition;

import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.workflow.dto.ButtonRelaDto;
import com.sinocarbon.workflow.dto.FormPropertyDto;
import com.sinocarbon.workflow.dto.ProcessDetailslDto;
import com.sinocarbon.workflow.dto.ProcessInstanceDto;
import com.sinocarbon.workflow.dto.StatusConfigDto;
import com.sinocarbon.workflow.dto.TaskDetailsDto;
import com.sinocarbon.workflow.dto.TaskNextDefinitionDto;
import com.sinocarbon.workflow.pojo.ActApdBtnRela;
import com.sinocarbon.workflow.pojo.ActApdStatusConf;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sxc
 * @since 2019-07-31
 */
public interface ProcessService{
	
	BaseResponse deployProcess(String deploymentName, String bpmnResourceUrl, String picResourceUrl, String tenantId);
	
	BaseResponse startProcesses(String processesKey, String processInstanceName, String startUser, Map<String,Object> map, String tenantId, List<String> entpMarks);
	
	BaseResponse startProcessesByDefId(String processesId, String processInstanceName, String startUser, Map<String,Object> map);

	BaseResponse findPersonTaskByAssignee(String assignee, String tenantId);
	
	BaseResponse completeTaskV2(Map<String,Object> map);
	
	BaseResponse completeTask(Map<String,Object> map,Integer type);
	
	BaseResponse findGroupTaskByAssignee(String group, String tenantId, String processInstanceIds);
	
	BaseResponse findTask(String taskId);
	
	BaseResponse addGroupUser(TaskDetailsDto taskDetailsDto);
	
	BaseResponse removeGroupUser(TaskDetailsDto taskDetailsDto);
	
	BaseResponse findProcessDefinition(String processkey,String tenantId);
	
	BaseResponse deleteProcessDefinition(String processKsy,String processDefinitionId, String tenantId );
	
	BaseResponse findPersonHistoryTask(String assignee, String tenantId);
	
	BaseResponse findGroupHistoryTask(String assignee, String tenantId, String processInstanceIds);
	
	BaseResponse findCurrTaskByProcessInstanceId(String processInstanceId, String tenantId);
	
	BaseResponse getProcessActivity(String processDefinitionId, String processInstanceId, String processKey, String tenantId);
	
	InputStream currentProcessInstanceImage(String processInstanceId);
	
	BaseResponse unfinishedProcess(Set<String> processInstanceIds);
	
	BaseResponse finishedProcess(Set<String> processInstanceIds);
	
	BaseResponse isfinishedProcess(String processInstanceId);
	
	BaseResponse deleteProcessInstance(String processInstanceId);

	InputStream getFlowImgByInstantId(String processInstanceId);

	BaseResponse createProcess(ProcessDetailslDto processDetailslDto);

	BaseResponse queryLatestProcessDefinition(ProcessDetailslDto processDetailslDto);

	byte[] getProcessDefinitionImage(String processDefinitionId) throws IOException;

	boolean modifyOldProcDefToNew(String processDefinitionId);

	BaseResponse taskKeyIsOld(String taskId);

	boolean taskChangeLastestDefinitionId(TaskDetailsDto taskDetailsDto);
	
	TaskNextDefinitionDto getNextTaskDefinition(String taskId);

	BaseResponse getHistoricProcessInstanceList(String userId,String tenantId);

	String getProcessStartUser(String processInstanceId);
	
	void suspendProcessInstance(String processInstanceId);
	
	void activateProcessInstance(String processInstanceId);
	
	void suspendProcessDefinitionByKey(ProcessDetailslDto processDetailslDto);

	void activateProcessDefinitionByKey(ProcessDetailslDto processDetailslDto);
	
	List<FormPropertyDto> getTaskFormProperty(String taskId);

	void saveFormData(String taskId, Map<String, String> properties);

	BaseResponse processFreeJump(TaskDetailsDto taskDetailsDto);
	
	BaseResponse backTask(TaskDetailsDto taskDetailsDto);
	
	ProcessDetailslDto getProDetailByProDefId(String proDefId);
	
	BaseResponse  codyProcess(ProcessDetailslDto processDetailslDto);

	Map<String, List<ProcessInstanceDto>> getProcessInstanceByKey(String processkey,String processInstanceName, String tenantId);
	
	Map<String, List<ProcessInstanceDto>> getProcessInstanceById(String proDefId,String processInstanceName);

	BaseResponse findHisTaskByProcessInstanceId(String processInstanceId, String tenantId);

	BaseResponse getProcessInstanceByInstId(String processInstanceId);

	BaseResponse getProcessInstanceByTaskId(String taskId);

	List<FormPropertyDto> getHisFormProperty(String taskId);

	BaseResponse deleteHisFormValue(String taskId);
	
	ProcessDefinition getProcessDefinitionById(String processDefinitionId);

	BaseResponse queryInstallkeys();

	BaseResponse addBtnRela(List<ButtonRelaDto> buttonRelaDtoList);

	BaseResponse updBtnRela(List<ButtonRelaDto> buttonRelaDtoList);

	BaseResponse queryBtnRela(ButtonRelaDto buttonRelaDto);

	BaseResponse queryBtnRelaByProcInstId(String processInstanceId, String btnName);

	BaseResponse updateRuTaskKeyBatch(ProcessDetailslDto processDetailslDto);

	BaseResponse queryDefByTenantId(String tenantId);

	BaseResponse completeTaskOld(Map<String, Object> params);

	List<TaskDetailsDto> getHisFormPropertyByProcessId(String processInstanceId);

	BaseResponse completeTasks(Map<String, Object> map, Integer type);

	BaseResponse processFreeJumpBatch(TaskDetailsDto taskDetailsDto);

	List<ActApdBtnRela> queryBtnByTask(TaskDetailsDto taskDetailsDto);

	List<String> findProcInstIdsByEntp(String entpMarks);
	
	List<Map<String, String>> getTaskSortByProDefId(String processDefinitionId, String processInstanceId, String processKey);

	BaseResponse findGroupHistoryTaskByProInstId(String processInstanceId);

	BaseResponse queryBtnByAuth(List<TaskDetailsDto> taskDetailsDtos);

	List<ActApdStatusConf> queryProcessStatusConfig(String processDefinitionId, String processDefinitionKey, String tenantId);

	BaseResponse addProcessStatusConfig(List<StatusConfigDto> statusConfigDtos);

	List<StatusConfigDto> queryProcessStatusConfigGroup(String processDefinitionId, String processDefinitionKey, String tenantId);
}
