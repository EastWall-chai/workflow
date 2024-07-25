package com.sinocarbon.workflow.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.sinocarbon.workflow.dto.LimitTaskDto;
import com.sinocarbon.workflow.pojo.ActApdTaskLimit;
import com.sinocarbon.workflow.pojo.ActApdTaskLimitDef;

/**
 * <p>
 * 任务期限表 服务类
 * </p>
 *
 * @author sxc123
 * @since 2022-08-18
 */
public interface ActApdTaskLimitService extends IService<ActApdTaskLimit> {

	List<ActApdTaskLimit> getOverdueTask();

	ActApdTaskLimit saveActApdTaskLimitInst(ActApdTaskLimit actApdTaskLimit);
	
	ActApdTaskLimitDef saveActApdTaskLimitDef(ActApdTaskLimitDef actApdTaskLimitDef);

}
