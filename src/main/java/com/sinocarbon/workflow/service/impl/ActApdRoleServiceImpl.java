package com.sinocarbon.workflow.service.impl;

import com.sinocarbon.workflow.pojo.ActApdRole;
import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.polaris.commons.utils.ErrorCode;
import com.sinocarbon.workflow.dao.ActApdRoleMapper;
import com.sinocarbon.workflow.service.ActApdRoleService;
import com.sinocarbon.workflow.utils.Constant;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 工作流默认角色表 服务实现类
 * </p>
 *
 * @author sxc123
 * @since 2022-09-09
 */
@Service
public class ActApdRoleServiceImpl extends ServiceImpl<ActApdRoleMapper, ActApdRole> implements ActApdRoleService {

	@Override
	public List<ActApdRole> getActRole(ActApdRole actApdRole) {
		EntityWrapper<ActApdRole> ew = new EntityWrapper<ActApdRole>();
		ew.eq("TENANT_ID_", actApdRole.getTenantId());
		if(actApdRole.getId() != null) {
			ew.eq("ID_", actApdRole.getId());
		}
		if(!StringUtils.isEmpty(actApdRole.getRoleCode())) {
			ew.eq("ROLE_CODE_", actApdRole.getRoleCode());
		}
		if(!StringUtils.isEmpty(actApdRole.getRoleName())) {
			ew.eq("ROLE_NAME_", actApdRole.getRoleName());
		}
		return baseMapper.selectList(ew);
	}

	@Override
	public BaseResponse saveActRole(ActApdRole actApdRole) {
		// 判断roleCode是否重复
		EntityWrapper<ActApdRole> ew = new EntityWrapper<ActApdRole>();
		ew.eq("TENANT_ID_", actApdRole.getTenantId())
			.eq("ROLE_CODE_", actApdRole.getRoleCode());
			
		List<ActApdRole> list = baseMapper.selectList(ew);
		if(CollectionUtils.isNotEmpty(list)) {
			return BaseResponse.failed(ErrorCode.CODE_200030, ErrorCode.CODE_200030_MSG);
		}
		baseMapper.insert(actApdRole);
		return BaseResponse.successed(actApdRole);
	}

	@Override
	public BaseResponse modifyActRole(ActApdRole actApdRole) {
		// 判断roleCode是否重复
		EntityWrapper<ActApdRole> ew = new EntityWrapper<ActApdRole>();
		ew.eq("TENANT_ID_", actApdRole.getTenantId())
			.eq("ROLE_CODE_", actApdRole.getRoleCode())
			.ne("ID_", actApdRole.getId());
		List<ActApdRole> list = baseMapper.selectList(ew);
		if(CollectionUtils.isNotEmpty(list)) {
			return BaseResponse.failed(ErrorCode.CODE_200030, ErrorCode.CODE_200030_MSG);
		}
		baseMapper.updateById(actApdRole);
		return BaseResponse.successed(actApdRole);
	}

	@Override
	public BaseResponse deleteActRole(ActApdRole actApdRole) {
		Integer result = baseMapper.deleteById(actApdRole.getId());
		if(result != 1) {
			return BaseResponse.failed(ErrorCode.CODE_200020, ErrorCode.CODE_200020_MSG);
		}
		return BaseResponse.successed(Constant.DELETE_MESSAGE);
	}

}
