package com.sinocarbon.workflow.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.polaris.commons.utils.ErrorCode;
import com.sinocarbon.workflow.pojo.ActApdRole;
import com.sinocarbon.workflow.service.ActApdRoleService;
import com.sinocarbon.workflow.utils.Constant;

/**
 * <p>
 * 工作流默认角色表 前端控制器
 * </p>
 *
 * @author sxc123
 * @since 2022-09-09
 */
@RestController
@RequestMapping
public class ActApdRoleController {
	
	@Autowired
	private ActApdRoleService actApdRoleService;
	
	@GetMapping(value = "/actrole")
	public BaseResponse getActRole(ActApdRole actApdRole) {
		if(StringUtils.isEmpty(actApdRole.getTenantId())) {
			actApdRole.setTenantId(Constant.DEF_TENANT_ID);
		}
		return BaseResponse.successed(actApdRoleService.getActRole(actApdRole));
	}
	
	@PostMapping(value = "/actrole")
	public BaseResponse saveActRole(ActApdRole actApdRole) {
		if(StringUtils.isEmpty(actApdRole.getTenantId())) {
			actApdRole.setTenantId(Constant.DEF_TENANT_ID);
		}
		if(StringUtils.isEmpty(actApdRole.getRoleCode()) || StringUtils.isEmpty(actApdRole.getRoleName())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return actApdRoleService.saveActRole(actApdRole);
	}
	
	@PutMapping(value = "/actrole")
	public BaseResponse modifyActRole(ActApdRole actApdRole) {
		if(StringUtils.isEmpty(actApdRole.getId()) || 
				StringUtils.isEmpty(actApdRole.getRoleCode()) || 
				StringUtils.isEmpty(actApdRole.getRoleName()) ||
				StringUtils.isEmpty(actApdRole.getTenantId()) ) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return actApdRoleService.modifyActRole(actApdRole);
	}
	
	@DeleteMapping(value = "/actrole")
	public BaseResponse deleteActRole(ActApdRole actApdRole) {
		if(StringUtils.isEmpty(actApdRole.getId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return actApdRoleService.deleteActRole(actApdRole);
	}
}

