package com.sinocarbon.workflow.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.polaris.commons.utils.ErrorCode;
import com.sinocarbon.workflow.dto.LimitTaskDto;
import com.sinocarbon.workflow.pojo.ActApdTaskLimit;
import com.sinocarbon.workflow.pojo.ActApdTaskLimitDef;
import com.sinocarbon.workflow.service.ActApdTaskLimitService;

/**
 * <p>
 * 任务期限表 前端控制器
 * </p>
 *
 * @author sxc123
 * @since 2022-08-18
 */
@RestController
@RequestMapping
public class ActApdTaskLimitController {

	@Autowired
	private ActApdTaskLimitService actApdTaskLimitService;
	
	/**
	 * 查询租户下已逾期的任务集合
	 * @param limitTaskDao
	 * @return
	 */
	@GetMapping(value = "/task/overdue")
	public BaseResponse  getOverdueTask() {
		List<ActApdTaskLimit> resultList = actApdTaskLimitService.getOverdueTask();
		if(resultList == null) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}else {
			return BaseResponse.successed(resultList);
		}
	}
	/**
	 * 新增期限任务实例
	 * @param actApdTaskLimit
	 * @return
	 */
	@PostMapping(value = "/task/limit/inst")
	public BaseResponse  saveActApdTaskLimitInst(ActApdTaskLimit actApdTaskLimit) {
		actApdTaskLimit = actApdTaskLimitService.saveActApdTaskLimitInst(actApdTaskLimit);
		if(actApdTaskLimit == null || actApdTaskLimit.getId() == null) {
			return BaseResponse.failed(ErrorCode.CODE_200040, ErrorCode.CODE_200040_MSG);
		}else {
			return BaseResponse.successed(actApdTaskLimit);
		}
	}
	/**
	 * 新增期限任务定义
	 * @param actApdTaskLimitDef
	 * @return
	 */
	@PostMapping(value = "/task/limit/def")
	public BaseResponse  saveActApdTaskLimitDef(ActApdTaskLimitDef actApdTaskLimitDef) {
		actApdTaskLimitDef = actApdTaskLimitService.saveActApdTaskLimitDef(actApdTaskLimitDef);
		if(actApdTaskLimitDef == null || actApdTaskLimitDef.getId() == null) {
			return BaseResponse.failed(ErrorCode.CODE_200040, ErrorCode.CODE_200040_MSG);
		}else {
			return BaseResponse.successed(actApdTaskLimitDef);
		}
	}
	
}

