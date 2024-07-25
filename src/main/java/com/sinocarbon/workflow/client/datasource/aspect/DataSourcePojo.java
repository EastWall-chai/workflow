package com.sinocarbon.workflow.client.datasource.aspect;

public class DataSourcePojo {
	private String dataBase;
	private String url;
	private String dataUserName;
	private String dataPassword;
	private String driverClassName;
	private String projectName;
	public String getDataBase() {
		return dataBase;
	}
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDataUserName() {
		return dataUserName;
	}
	public void setDataUserName(String dataUserName) {
		this.dataUserName = dataUserName;
	}
	public String getDataPassword() {
		return dataPassword;
	}
	public void setDataPassword(String dataPassword) {
		this.dataPassword = dataPassword;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public DataSourcePojo(String dataBase, String url, String dataUserName, String dataPassword, String driverClassName,
			String projectName) {
		super();
		this.dataBase = dataBase;
		this.url = url;
		this.dataUserName = dataUserName;
		this.dataPassword = dataPassword;
		this.driverClassName = driverClassName;
		this.projectName = projectName;
	}


	
}
