package com.sinocarbon.workflow.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sinocarbon.workflow.dao.ActApdTaskLimitDefMapper;
import com.sinocarbon.workflow.dao.ActApdTaskLimitMapper;
import com.sinocarbon.workflow.dto.LimitTaskDto;
import com.sinocarbon.workflow.pojo.ActApdTaskLimit;
import com.sinocarbon.workflow.pojo.ActApdTaskLimitDef;
import com.sinocarbon.workflow.service.ActApdTaskLimitService;

/**
 * <p>
 * 任务期限表 服务实现类
 * </p>
 *
 * @author sxc123
 * @since 2022-08-18
 */
@Service
public class ActApdTaskLimitServiceImpl extends ServiceImpl<ActApdTaskLimitMapper, ActApdTaskLimit> implements ActApdTaskLimitService {

	@Autowired
	private ActApdTaskLimitDefMapper actApdTaskLimitDefMapper;
	
	@Override
	public List<ActApdTaskLimit> getOverdueTask() {
		EntityWrapper<ActApdTaskLimit> ew = new EntityWrapper<>();
//		if(StringUtils.isEmpty(limitTaskDao.getTenantId())) {
//			return null;
//		}
		String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		ew.eq("TASK_MARK_", 1)// 未通知
			.lt("TIME_END_", now);// 任务结束时间 < 当前时间，表示已逾期
		return baseMapper.selectList(ew);
	}

	@Override
	public ActApdTaskLimit saveActApdTaskLimitInst(ActApdTaskLimit actApdTaskLimit) {
		if(StringUtils.isEmpty(actApdTaskLimit.getTaskId()) || StringUtils.isEmpty(actApdTaskLimit.getTaskKey())
				|| StringUtils.isEmpty(actApdTaskLimit.getTaskName()) || StringUtils.isEmpty(actApdTaskLimit.getProcDefId())
				|| StringUtils.isEmpty(actApdTaskLimit.getProcInstId()) || StringUtils.isEmpty(actApdTaskLimit.getTenantId())
				|| StringUtils.isEmpty(actApdTaskLimit.getTaskOperators()) || StringUtils.isEmpty(actApdTaskLimit.getTimeStart())
				|| StringUtils.isEmpty(actApdTaskLimit.getTimeEnd()) || StringUtils.isEmpty(actApdTaskLimit.getTimeDuration())
			) {
			return actApdTaskLimit;
		}
		if(StringUtils.isEmpty(actApdTaskLimit.getTaskMark())) {
			actApdTaskLimit.setTaskMark("1");// 创建任务时，默认未通知
		}
		baseMapper.insert(actApdTaskLimit);
		return actApdTaskLimit;
	}
	
	@Override
	public ActApdTaskLimitDef saveActApdTaskLimitDef(ActApdTaskLimitDef actApdTaskLimitDef) {
		if(StringUtils.isEmpty(actApdTaskLimitDef.getTaskKey()) 
				|| StringUtils.isEmpty(actApdTaskLimitDef.getTaskName()) 
				|| StringUtils.isEmpty(actApdTaskLimitDef.getProcDefId())
				|| StringUtils.isEmpty(actApdTaskLimitDef.getTenantId())
				|| StringUtils.isEmpty(actApdTaskLimitDef.getTimeDuration())
			) {
			return actApdTaskLimitDef;
		}
		actApdTaskLimitDefMapper.insert(actApdTaskLimitDef);
		return actApdTaskLimitDef;
	}

}
