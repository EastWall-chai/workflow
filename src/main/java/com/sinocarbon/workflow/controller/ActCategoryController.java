package com.sinocarbon.workflow.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.polaris.commons.utils.Constant;
import com.sinocarbon.polaris.commons.utils.ErrorCode;
import com.sinocarbon.workflow.dto.ActCategoryDto;
import com.sinocarbon.workflow.pojo.ActCategory;
import com.sinocarbon.workflow.pojo.ActCategoryRela;
import com.sinocarbon.workflow.service.ActCategoryService;

@RestController
@RequestMapping
public class ActCategoryController {
	@Autowired
	private ActCategoryService actCategoryService;
	
	/**
	 * 新增/修改分类信息
	 * @param name 标签名称
	 * @param pId 父标签ID
	 * @param status 状态
	 * @param tenanId 租户ID
	 * @return
	 */
	@PostMapping(value = "/category")
	public BaseResponse inserCategory(@RequestBody ActCategory actCategory) {
		if (StringUtils.isEmpty(actCategory.getTenantId()) && StringUtils.isEmpty(actCategory.getId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少租户参数");
		}else {
			return BaseResponse.successed(actCategoryService.save(actCategory));
		}
	}

	/**
	 * 分类信息分层展示
	 * @param name 标签名称
	 * @param pId 父标签ID
	 * @param status 状态
	 * @param tenanId 租户ID
	 * @return
	 */
	@GetMapping(value = "/category/levels")
	public BaseResponse getCategoryList(@RequestBody(required=false) ActCategoryDto actCategoryDto) {
      	List<ActCategoryDto> getActCategoryList=actCategoryService.getActCategoryList(actCategoryDto); 
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, getActCategoryList);
	}
	
	/**
	 * 根据不同条件查询标签
	 * @param name 标签名称
	 * @param pId 父标签ID
	 * @param status 状态
	 * @param tenanId 租户ID
	 * @return
	 */
	@GetMapping(value = "/category")
	public BaseResponse getCategory(Integer id, Integer pid, String tenantId, String proDefId ) {
		if(StringUtils.isEmpty(tenantId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, "缺少租户参数");
		}
		ActCategoryDto actCategoryDto = new ActCategoryDto();
		actCategoryDto.setId(id);
		actCategoryDto.setPid(pid);
		actCategoryDto.setTenantId(tenantId);
		actCategoryDto.setProDefId(proDefId);
      	List<ActCategoryDto> getActCategoryList=actCategoryService.getAllActCategory(actCategoryDto); 
		return BaseResponse.successed(Constant.SUCCESS_MESSAGE, getActCategoryList);
	}
	
	/**
	 * 删除标签
	 * @param name 标签名称
	 * @param pId 父标签ID
	 * @param status 状态
	 * @param tenanId 租户ID
	 * @return
	 */
	@DeleteMapping(value = "/category")
	public BaseResponse deleteCategory(@RequestBody(required=false) ActCategory actCategory) {
		if(StringUtils.isEmpty(actCategory.getId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010,ErrorCode.CODE_200010_MSG);
		}
		actCategoryService.deleteCategory(actCategory); 
		return BaseResponse.successed(Constant.SUCCESS);
	}
	
	
	/**
	 * 增加标签关系
	 * @param categoryId 标签ID
	 * @param proDefId 流程定义ID
	 * @return
	 */
	@PostMapping(value = "/category/rela")
	public BaseResponse addCategoryRela(@RequestBody ActCategoryRela actCategoryRela) {
		if(StringUtils.isEmpty(actCategoryRela.getCategoryId()) || StringUtils.isEmpty(actCategoryRela.getProDefId())) {
			return BaseResponse.failed(ErrorCode.CODE_200010,ErrorCode.CODE_200010_MSG);
		}
		return actCategoryService.addCategoryRela(actCategoryRela);
	}
	
	/**
	 * 删除标签关系
	 * @param name 标签名称
	 * @param categoryId 标签ID
	 * @param categoryId 标签ID
	 * @return
	 */
	@DeleteMapping(value = "/category/rela")
	public BaseResponse deleteCategoryRela(@RequestBody(required=false) ActCategoryRela actCategoryRela) {
		if (actCategoryRela==null) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}else {
			actCategoryService.deleteCategoryRela(actCategoryRela);
		}
		return BaseResponse.successed(Constant.SUCCESS);
	}
	
	/**
	 * 根据分类查询流程定义
	 * @param categoryId 流程分类ID
	 * @return
	 */
	@GetMapping(value = "/category/process")
	public BaseResponse getProcessByCategory(String categoryId) {
		if(StringUtils.isEmpty(categoryId)) {
			return BaseResponse.failed(ErrorCode.CODE_200010, ErrorCode.CODE_200010_MSG);
		}
		return BaseResponse.successed( actCategoryService.getProcessByCategory(categoryId));
	}
}
