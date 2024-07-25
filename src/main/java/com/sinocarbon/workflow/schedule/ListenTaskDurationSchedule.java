package com.sinocarbon.workflow.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.sinocarbon.workflow.pojo.ActApdTaskLimit;
import com.sinocarbon.workflow.service.ActApdTaskLimitService;

@Component
public class ListenTaskDurationSchedule {

	@Autowired
	private ActApdTaskLimitService actApdTaskLimitService;
	/**
	 * 每天查询任务逾期状况
	 */
	@Scheduled(cron = "0 0 1 * * ?")   // 每天凌晨1点执行
    private void checkLimitTask() {
		List<ActApdTaskLimit> actApdTaskLimitList = actApdTaskLimitService.getOverdueTask();
		if(CollectionUtils.isNotEmpty(actApdTaskLimitList)) {
			//TODO 遍历逾期任务，调用消息中心接口发送消息
			
		}
    }
}
