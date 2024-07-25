//package com.sinocarbon.workflow.listener;
//
//import java.util.Date;
//
//import org.activiti.engine.ProcessEngine;
//import org.activiti.engine.ProcessEngines;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.delegate.DelegateTask;
//import org.activiti.engine.delegate.TaskListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import com.sinocarbon.workflow.pojo.MonthYearReport;
//import com.sinocarbon.workflow.service.MonthYearReportService;
//
///**
// * 任务完成监听器，流程状态更新
// * @author sxc
// *
// */
//@Component("MonthDataStateListener")
//public class MonthDataStateListener implements TaskListener{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	@Autowired
//	private MonthYearReportService monthYearReportService;
//	
//	private static ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//	@Override
//	public void notify(DelegateTask delegateTask) {
//		TaskService taskService = processEngine.getTaskService();
//		/**获取流程变量:状态*/
//		Object status = taskService.getVariable(delegateTask.getId(), "status");
//		if(status == null){
//			return;
//		}
//		String processInstanceId = delegateTask.getProcessInstanceId();
//		/**根据流程实例ID获取唯一填报数据*/
//		EntityWrapper<MonthYearReport> ew = new EntityWrapper<>();
//		ew.eq("process_id", processInstanceId);
//		MonthYearReport monthYearReport = monthYearReportService.selectOne(ew);
//		/**更新状态*/
//		if(monthYearReport == null){
//			return;
//		}
//		monthYearReport.setStatus(Integer.valueOf(status.toString()));
//		/**如果是提交数据，更新提交时间*/
//		if(monthYearReport.getGmtSubmit() == null){
//			monthYearReport.setGmtSubmit(new Date());
//		}
//		monthYearReportService.updateById(monthYearReport);
//	}
//	
//}
