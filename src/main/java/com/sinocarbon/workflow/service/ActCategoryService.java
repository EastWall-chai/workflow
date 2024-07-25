package com.sinocarbon.workflow.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.workflow.dto.ActCategoryDto;
import com.sinocarbon.workflow.pojo.ActCategory;
import com.sinocarbon.workflow.pojo.ActCategoryRela;

public interface ActCategoryService extends IService<ActCategory>{
	
	public ActCategory save(ActCategory category);
	
	public List<ActCategoryDto> getActCategoryList(ActCategoryDto categoryDto);
	
	
	public List<ActCategoryDto> getAllActCategory(ActCategoryDto categoryDto);
	
	public void deleteCategory(ActCategory category);
	
	public BaseResponse addCategoryRela(ActCategoryRela actCategoryRela);
	
	public void deleteCategoryRela(ActCategoryRela actCategoryRela);
	
	public List<Map<String, Object>> getProcessByCategory(String categoryId);
}
