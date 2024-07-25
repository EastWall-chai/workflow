//package com.sinocarbon.workflow.client.mybatis.generation;
//
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.rules.DbType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//public class CodeGeneration {
////	
//	public static void main(String[] args) {
//		CodeGeneration codeGeneration = new CodeGeneration();
//		codeGeneration.execute();
//	}
//	
//	/**
//	 * 
//	 * @ClassName: CodeGeneration
//	 * @Description: 代码生成器
//	 * @author yuan
//	 * @date 2018年6月25日
//	 */
//	public void execute() {
//		AutoGenerator mpg = new AutoGenerator();
//		// 全局配置
//		GlobalConfig gc = new GlobalConfig();
//		// gc.setOutputDir("E:\\RECORD\\GitLab_Workspace\\bts-srv\\src\\main\\java");
//		gc.setOutputDir("/Users/shenxiaocheng/RECORD/workflow_engine/src/main/java");
//		gc.setFileOverride(true);
//		gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
//		gc.setEnableCache(false);// XML 二级缓存
//		gc.setBaseResultMap(true);// XML ResultMap
//		gc.setBaseColumnList(false);// XML columList
//		gc.setAuthor("sxc");// 作者
//
//		// 自定义文件命名，注意 %s 会自动填充表实体属性！
//		gc.setControllerName("%sController");
//		gc.setServiceName("%sService");
//		gc.setServiceImplName("%sServiceImpl");
//		gc.setMapperName("%sMapper");
//		gc.setXmlName("%sMapper");
//		mpg.setGlobalConfig(gc);
//
//		// 数据源配置
//		DataSourceConfig dsc = new DataSourceConfig();
//		dsc.setDbType(DbType.MYSQL);
//		dsc.setDriverName("com.mysql.jdbc.Driver");
//		dsc.setUsername("root");
//		dsc.setPassword("SCIIscii123");
//		dsc.setUrl(
//				"jdbc:mysql://39.99.148.90:30870/database_fill_task?useSSL=false&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=GMT%2B8");
//		mpg.setDataSource(dsc);
//
//		// 策略配置
//		StrategyConfig strategy = new StrategyConfig();
//		strategy.setTablePrefix(new String[] { "" });// 此处可以修改为您的表前缀
//		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
//		// 需要生成的表
//		strategy.setInclude(new String[] { "ACT_APD_ROLE" });
//		// strategy.setInclude(new String[] {"appendix_info", "personnel_qualification",
//		// "personnel_qualification_appendix"});
//
//		strategy.setSuperServiceClass(null);
//		strategy.setSuperServiceImplClass(null);
//		strategy.setSuperMapperClass(null);
//
//		mpg.setStrategy(strategy);
//
//		// 包配置
//		PackageConfig pc = new PackageConfig();
//		pc.setParent("com.sinocarbon.workflow");
//		pc.setController("controller");
//		pc.setService("service");
//		 pc.setServiceImpl("service.impl");
//		pc.setMapper("dao");
//		pc.setEntity("pojo");
//		pc.setXml("mapper");
//		mpg.setPackageInfo(pc);
//
//		// 执行生成
//		mpg.execute();
//	}
//}
