//package com.sinocarbon.workflow.client.datasource.aspect;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.sinocarbon.workflow.utils.Constant;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Aspect
//@Order(1)
//@Configuration
//@Slf4j
//public class DataSourceAspect {
//
//	/**
//	 * 切入点,放在controller的每个方法上进行切入,更新数据源
//	 */
//	@Pointcut("execution(* com.sinocarbon.workflow.service.impl.ProcessServiceImpl.*(..))")
//	private void anyMethod() {
//	}// 定义一个切入点
//
//	@Before("anyMethod()")
//	public void dataSourceChange() {
//		// 请求头head中获取对应数据库编号
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//				.getRequest();
//		String no = request.getHeader(Constant.DS_NO);
//		if (StringUtils.isEmpty(no)) {
//			log.warn("DN_NO is not found Headers");
//			Object name = request.getAttribute(Constant.DS_NO);
//			if (name == null) {
//				log.warn("DN_NO is not found request");
//			}else {
//				no = name.toString();
//			}
//		}
//		log.info("当前数据源为{}", no);
//		DataSourceHolder.setDataSource(no);
//		/* 这里数据库项目编号来更改对应的数据源 */
//	}
//}
