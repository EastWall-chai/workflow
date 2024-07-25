package com.sinocarbon.workflow.service;

import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.workflow.pojo.ActApdRole;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 工作流默认角色表 服务类
 * </p>
 *
 * @author sxc123
 * @since 2022-09-09
 */
public interface ActApdRoleService extends IService<ActApdRole> {

	List<ActApdRole> getActRole(ActApdRole actApdRole);

	BaseResponse saveActRole(ActApdRole actApdRole);

	BaseResponse modifyActRole(ActApdRole actApdRole);

	BaseResponse deleteActRole(ActApdRole actApdRole);

}
