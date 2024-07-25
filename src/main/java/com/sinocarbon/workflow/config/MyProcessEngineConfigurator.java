package com.sinocarbon.workflow.config;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sinocarbon.workflow.service.impl.UUIDGenerator;

@Configuration
public class MyProcessEngineConfigurator {

	@Autowired
    private UUIDGenerator uuidGenerator;

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfigurationImpl(ProcessEngineConfigurationImpl processEngineConfigurationImpl){
       //设置ProcessEngineConfigurationImpl里的uuidGenerator
       processEngineConfigurationImpl.setIdGenerator(uuidGenerator);
       //设置DbSqlSessionFactory的uuidGenerator，否则流程id，任务id，实例id依然是用DbIdGenerator生成
       processEngineConfigurationImpl.getDbSqlSessionFactory().setIdGenerator(uuidGenerator);
       return processEngineConfigurationImpl;
    }
}
