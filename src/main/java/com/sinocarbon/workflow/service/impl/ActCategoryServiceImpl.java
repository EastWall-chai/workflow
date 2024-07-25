package com.sinocarbon.workflow.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sinocarbon.polaris.commons.utils.BaseResponse;
import com.sinocarbon.workflow.dao.ActCategoryMapper;
import com.sinocarbon.workflow.dao.ActCategoryRelaMapper;
import com.sinocarbon.workflow.dto.ActCategoryDto;
import com.sinocarbon.workflow.pojo.ActCategory;
import com.sinocarbon.workflow.pojo.ActCategoryRela;
import com.sinocarbon.workflow.service.ActCategoryService;
import com.sinocarbon.workflow.service.ProcessService;
import com.sinocarbon.workflow.utils.Constant;
import com.sinocarbon.workflow.utils.ErrorCode;

@Service
public class ActCategoryServiceImpl  extends ServiceImpl<ActCategoryMapper, ActCategory> implements ActCategoryService{

	@Autowired 
	private ActCategoryRelaMapper actCategoryRelaMapper;
	@Autowired
	private ProcessService processService;
	
	@Transactional
	@Override
	public ActCategory save(ActCategory category) {
		if (category.getId() !=null) {
			category.setPid(baseMapper.selectById(category).getPid());
			baseMapper.updateById(category);
			return baseMapper.selectById(category);
		}else {
			category.setStatus(1);
			baseMapper.insert(category);
			return category;
		}
		
	}

	@Override
	public List<ActCategoryDto> getActCategoryList(ActCategoryDto category) {
      		return getActCategory(category);
	}
	
	private List<ActCategoryDto> getActCategory(ActCategoryDto category) {
		ActCategoryDto actCategoryDto=new ActCategoryDto();
		if (category !=null) {
			actCategoryDto.setPid(category.getId());
		}
		List<ActCategoryDto> list=baseMapper.queryCategory(actCategoryDto);
		return getNextActCategory(list);
		
	}
	
	//递归获取标签各级内容
	private List<ActCategoryDto> getNextActCategory(List<ActCategoryDto> list){
		for(ActCategoryDto actCategoryDto: list) {
			ActCategoryDto actCategoryDtoNew=new ActCategoryDto();
			actCategoryDtoNew.setPid(actCategoryDto.getId());
			 List<ActCategoryDto> listNew=  baseMapper.queryCategory(actCategoryDtoNew);
				   if (!listNew.isEmpty()) {
					   actCategoryDto.setActCategoryList(listNew);	
					   getNextActCategory(listNew);
				   }else {
					   return list;
				   }	   
		}
		return list;
	}

	@Override
	public List<ActCategoryDto> getAllActCategory(ActCategoryDto category) {
		return baseMapper.queryCategory(category);
	}

	@Override
	public void deleteCategory(ActCategory category) {
		EntityWrapper<ActCategory> ew = new EntityWrapper<>();
		ew.eq("PID_", category.getId()).and().eq("STATUS_", 1);
		//查出当前标签的子标签
		List<ActCategory> childList=baseMapper.selectList(ew);
		//更新它的子标签的父标签为当前删除标签的父标签
		for(ActCategory actCategoryChild : childList) {
			ActCategory actCategory = new ActCategory();
			actCategory.setId(actCategoryChild.getId());
			actCategory.setPid(category.getPid());
			baseMapper.updateById(actCategory);
		}
		
		category.setStatus(Constant.DELETE_STATUS);
		baseMapper.updateById(category);
	}

	@Override
	public BaseResponse addCategoryRela(ActCategoryRela actCategoryRela) {
		ProcessDefinition processDefinition = null;
		try {
			processDefinition = processService.getProcessDefinitionById(actCategoryRela.getProDefId());
		} catch (Exception e) {
			return BaseResponse.failed(ErrorCode.CODE_400060, ErrorCode.CODE_400060_MSG);
		}
		if(processDefinition == null) {
			return BaseResponse.failed(ErrorCode.CODE_400060, ErrorCode.CODE_400060_MSG);
		}
		actCategoryRelaMapper.insert(actCategoryRela);
		return BaseResponse.successed(actCategoryRela);
		
	}

	@Override
	public void deleteCategoryRela(ActCategoryRela actCategoryRela) {
		actCategoryRelaMapper.deleteActCategoryRela(actCategoryRela);
	}

	@Override
	public List<Map<String, Object>> getProcessByCategory(String categoryId) {
		ActCategoryRela actCategoryRela = new ActCategoryRela();
		actCategoryRela.setCategoryId(Integer.valueOf(categoryId));
		return actCategoryRelaMapper.queryCategoryRela(actCategoryRela);
	}

}
