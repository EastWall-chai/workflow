package com.sinocarbon.workflow.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sinocarbon.workflow.dto.ActCategoryDto;
import com.sinocarbon.workflow.pojo.ActCategory;

public interface ActCategoryMapper  extends BaseMapper<ActCategory>{

	public List<ActCategoryDto>  queryCategory(ActCategoryDto actCategoryDto);

	

}
