//package com.sinocarbon.workflow.client.datasource.aspect;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.SQLException;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ConcurrentHashMap;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//
//import com.alibaba.druid.pool.DruidDataSource;
//
//@Configuration
//public class DynamicDataSource extends AbstractRoutingDataSource {
//	
//	/**
//	 * 每次请求动态请求哪一个数据源
//	 * 
//	 * @return
//	 */
//	@Override
//	protected Object determineCurrentLookupKey() {
//		return DataSourceHolder.getDataSource();
//	}
//
//	public DynamicDataSource(@Value("${dataSourceUrl}") String dataSourceUrl) throws IOException {
//		Map<Object, Object> dataSources = new ConcurrentHashMap<>();
//		Properties prop = new Properties();
////		File file = new File(dataSourceUrl);
////		InputStream inStream = new FileInputStream(file);
//		InputStream inStream = getClass().getClassLoader().getResourceAsStream(dataSourceUrl);
//		prop.load(inStream);  
//		String[] sourcesList = prop.getProperty("datasource").split(","); //读取所有数据源key 例：[zn,zl]
//		int len= sourcesList.length;
//		String indexSource;
//		for(int i=0;i<len;i++) {//遍历数据源Key封装成DataSourcePojo
//			indexSource= sourcesList[i];
//			DataSource dataSource = druidDataSource(new DataSourcePojo(prop.getProperty(indexSource+".dataBase"),prop.getProperty(indexSource+".url"),prop.getProperty(indexSource+".dataUserName"),prop.getProperty(indexSource+".dataPassword"),prop.getProperty(indexSource+".driverClassName"),indexSource));
//			dataSources.put(indexSource, dataSource);
//			if (i == 0) {
//				super.setDefaultTargetDataSource(dataSource);//第一条数据源为默认数据源
//			}
//		}
//		super.setTargetDataSources(dataSources);
//	}
//
//	/**
//	 * 此处数据库信配置,可以来源于redis等,然后再初始化所有数据源 重点说明:一个DruidDataSource数据源,它里面本身就是线程池了,
//	 * 所以我们不需要考虑线程池的问题
//	 * 
//	 * @param no
//	 * @return
//	 */
//	public DataSource druidDataSource(DataSourcePojo source) {
//		DruidDataSource datasource = new DruidDataSource();
//		datasource.setUrl(source.getUrl()+ source.getDataBase()
//				+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=GMT%2B8");
//		datasource.setUsername(source.getDataUserName());
//		datasource.setPassword(source.getDataPassword());
//		datasource.setDriverClassName(source.getDriverClassName());
//
//		datasource.setInitialSize(5);
//		datasource.setMinIdle(2);
//		datasource.setMaxActive(5);
//		datasource.setDbType("com.alibaba.druid.pool.DruidDataSource");
//		datasource.setMaxWait(60000);
//		datasource.setTimeBetweenEvictionRunsMillis(20000);
//		datasource.setMinEvictableIdleTimeMillis(40000);
//		datasource.setTestWhileIdle(true);
//		datasource.setTestOnBorrow(false);
//		datasource.setTestOnReturn(false);
//		try {
//			datasource.setFilters("stat");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return datasource;
//	}
//}
