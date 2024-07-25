package com.sinocarbon.workflow.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sinocarbon.workflow.dto.ButtonRelaDto;
import com.sinocarbon.workflow.dto.ProcessDetailslDto;
import com.sinocarbon.workflow.dto.StatusConfigDto;
import com.sinocarbon.workflow.dto.TaskDetailsDto;
import com.sinocarbon.workflow.pojo.ActApdStatusConf;
import com.sinocarbon.workflow.pojo.ActApdTaskLimitDef;
import com.sinocarbon.workflow.pojo.ActCategoryRela;
import com.sinocarbon.workflow.pojo.ActReProcdef;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sxc123
 * @since 2020-04-07
 */
public interface ActReProcdefMapper extends BaseMapper<ActReProcdef> {

	List<ProcessDetailslDto> queryProcessDefinition(ProcessDetailslDto processDetailslDto);
	
	void updateRuTaskDefId(ProcessDetailslDto processDetailslDto);
	void updateHiTaskDefId(ProcessDetailslDto processDetailslDto);
	void updateHiProcinstDefId(ProcessDetailslDto processDetailslDto);
	void updateHiActDefId(ProcessDetailslDto processDetailslDto);
	void updateRuExecDefId(ProcessDetailslDto processDetailslDto);
	
	void updateRuTaskKey(TaskDetailsDto taskDetailsDto);
	void updateHiActinstKey(TaskDetailsDto taskDetailsDto);
	void updateHiTaskKey(TaskDetailsDto taskDetailsDto);
	void updateRuExecKey(TaskDetailsDto taskDetailsDto);
	
	void deleteHisFormValue(String taskId);
	
	List<Map<String, Object>> queryInstallkeys();
	List<Map<String, Object>> queryCategoryRelaByDefId(String processDefinitionId);
	void addBtnRela(List<ButtonRelaDto> buttonRelaDtoList);
	void updBtnRela(List<ButtonRelaDto> buttonRelaDtoList);
	List<ButtonRelaDto> queryBtnRela(ButtonRelaDto buttonRelaDto);
	
	List<TaskDetailsDto> queryRunTaskByProDefId(@Param("maps") List<Map<String , Object>> maps, @Param("processDefinitionId") String processDefinitionId);
	
	void updateRuTaskKeyBatch(ProcessDetailslDto processDetailslDto);
	void updateHiActinstKeyBatch(ProcessDetailslDto processDetailslDto);
	void updateHiTaskKeyBatch(ProcessDetailslDto processDetailslDto);
	void updateRuExecKeyBatch(ProcessDetailslDto processDetailslDto);
	
	void updProcinstStatus(ButtonRelaDto buttonRelaDto);
	void insertProcinstStatus(ButtonRelaDto buttonRelaDto);
	
	void addTaskLimitList(List<ActApdTaskLimitDef> taskLimitDefs);

	void addActCategoryRela(List<ActCategoryRela> oldActCategoryRelaList);
	
	void addStatusConfig(List<ActApdStatusConf> actApdStatusConfList);
	List<StatusConfigDto> queryProcessStatusConfigGroup(StatusConfigDto dto);
}
