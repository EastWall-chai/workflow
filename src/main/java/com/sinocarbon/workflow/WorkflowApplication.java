package com.sinocarbon.workflow;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.sinocarbon.workflow.utils.Constant;

@MapperScan(Constant.MAPPER_SCAN_PACKAGE)
@EnableScheduling  //扫描定时器
@SpringBootApplication(exclude={
		  org.activiti.spring.boot.SecurityAutoConfiguration.class,
		  SecurityAutoConfiguration.class
		})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WorkflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}

}
