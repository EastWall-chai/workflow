package com.sinocarbon.workflow.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.sinocarbon.workflow.pojo.ActCategoryRela;

public interface ActCategoryRelaMapper extends BaseMapper<ActCategoryRela>{
      public  Integer deleteActCategoryRela(ActCategoryRela actCategoryRela);
      public  List<Map<String, Object>>  queryCategoryRela(ActCategoryRela actCategoryRela);
}
