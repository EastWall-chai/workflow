//package com.sinocarbon.workflow.client.cors.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import com.sinocarbon.workflow.client.oauth2.service.RedisClientDetailsService;
//import com.sinocarbon.workflow.service.ProcessService;
//
//import lombok.extern.java.Log;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class ApplicationRunnerImpl implements ApplicationRunner{
//	@Autowired
//	private ProcessService processService;
//	
//	/**
//	 * 项目启动后，自动部署流程资源
//	 */
//	@Override
//    public void run(ApplicationArguments args) throws Exception {
//        log.info("=================自动部署`月度数据审核流程`开始");
//        processService.deployProcess("monthDataCheckProcess", "processes/monthDataCheckProcess.bpmn", "processes/monthDataCheckProcess.png");
//        log.info("=================`月度数据审核流程`自动部署成功");
//        
//        log.info("=================自动部署`年度数据审核流程`开始");
//        processService.deployProcess("yearDataCheckProcess", "processes/yearDataCheckProcess.bpmn", "processes/yearDataCheckProcess.png");
//        log.info("=================`年度数据审核流程`自动部署成功");
//        
//        log.info("=================自动部署`核查数据审核流程`开始");
//        processService.deployProcess("examineDataCheckProcess", "processes/examineDataCheckProcess.bpmn", "processes/examineDataCheckProcess.png");
//        log.info("=================`核查数据审核流程`自动部署成功");
//        
//        log.info("=================自动部署`监测计划审核流程`");
//        processService.deployProcess("monitoringPlanCheckProcess", "processes/monitoringPlanCheckProcess.bpmn", "processes/monitoringPlanCheckProcess.png");
//        log.info("=================`监测计划审核流程`自动部署成功");
//        
//        log.info("=================自动部署`报送资料审核流程`");
//        processService.deployProcess("reportMaterialCheckProcess", "processes/reportMaterialCheckProcess.bpmn", "processes/reportMaterialCheckProcess.png");
//        log.info("=================`报送资料审核流程`自动部署成功");
//    }
//}
