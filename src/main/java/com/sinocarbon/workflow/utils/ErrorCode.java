package com.sinocarbon.workflow.utils;

public class ErrorCode {

	/**
	 * 缺少必要参数！
	 */
	public static final Integer CODE_200010 = 200010;
	public static final String CODE_200010_MSG = "缺少必要参数！";
	/**
	 * 无效的操作！请检查参数信息是否正确！
	 */
	public static final Integer CODE_200020 = 200020;
	public static final String CODE_200020_MSG = "无效的操作！请检查参数信息是否正确！";
	
	/**
	 * 添加或修改的对象已存在！
	 */
	public static final Integer CODE_200030 = 200030;
	public static final String CODE_200030_MSG = "添加或修改的对象已存在！";
	/**
	 * 添加或修改失败！请重新尝试或联系管理员！
	 */
	public static final Integer CODE_200040 = 200040;
	public static final String CODE_200040_MSG = "添加或修改失败！请重新尝试或联系管理员！";
	/**
	 * 导出失败！请重新尝试或联系管理员！
	 */
	public static final Integer CODE_200050 = 200050;
	public static final String CODE_200050_MSG = "导出失败！请重新尝试或联系管理员！";
	
	/**
	 * 用户名不存在
	 */
	public static final Integer CODE_300010 = 300010;
	public static final String CODE_300010_MSG = "用户名不存在！";
	/**
	 * 用户名或密码错误！
	 */
    public static final Integer CODE_300020 = 300020;
    public static final String CODE_300020_MSG = "用户名或密码错误！";
    /**
	 * 该用户被冻结或关闭，无法登陆！请联系管理员！
	 */
    public static final Integer CODE_300030 = 300030;
    public static final String CODE_300030_MSG = "该用户被冻结或关闭，无法登陆！请联系管理员！";
    /**
	 * 同一账号不可重复登录！
	 */
    public static final Integer CODE_300040 = 300040;
    public static final String CODE_300040_MSG = "同一账号不可重复登录！";
    /**
	 * 获取当前用户信息失败！缓存失效或用户已退出登录！
	 */
    public static final Integer CODE_300050 = 300050;
    public static final String CODE_300050_MSG = "获取当前用户信息失败！缓存失效或用户已退出登录！";
    /**
	 * 当前系统没有账号登录，退出异常！
	 */
    public static final Integer CODE_300060 = 300060;
    public static final String CODE_300060_MSG = "当前系统没有账号登录，退出异常！";
    
    /**
	 * 第三方登录授权失败，请重新授权！
	 */
    public static final Integer CODE_300070 = 300070;
    public static final String CODE_300070_MSG = "第三方登录授权失败，请重新授权！";
    
    /**
     * 输入的原密码错误，请重新输入！
     */
    public static final Integer CODE_300080 = 300080;
    public static final String CODE_300080_MSG = "输入的原密码错误，请重新输入！";
    
    /**流程部署失败,请核对资源路径是否正确！*/
    public static final Integer CODE_400010 = 400010;
    public static final String CODE_400010_MSG = "流程部署失败,请核对资源路径是否正确！";
    
    /**流程实例无当前任务！*/
    public static final Integer CODE_400020 = 400020;
    public static final String CODE_400020_MSG = "流程实例无当前任务，请核对该流程实例是否正确！";
    
    /**流程实例当前有多个任务！*/
    public static final Integer CODE_400030 = 400030;
    public static final String CODE_400030_MSG = "流程实例当前有多个任务，请核对该流程实例是否正确！";
    
    /**当前责任人并不是该任务的责任人！*/
    public static final Integer CODE_400040 = 400040;
    public static final String CODE_400040_MSG = "当前责任人并不是该任务的责任人！";
    
    /**该任务无此候选人！*/
    public static final Integer CODE_400041 = 400041;
    public static final String CODE_400041_MSG = "该任务无此候选人！";
    
    /**该任务无此候选组！*/
    public static final Integer CODE_400042 = 400042;
    public static final String CODE_400042_MSG = "该任务无此候选组！";
    
    /**当前用户无流转权限！*/
    public static final Integer CODE_400050 = 400050;
    public static final String CODE_400050_MSG = "当前用户无流转权限！";
    
    /**未找到相应的流程定义对象！*/
    public static final Integer CODE_400060 = 400060;
    public static final String CODE_400060_MSG = "未找到相应的流程定义对象！";
    
    /**流程启动失败，请稍后重试！*/
    public static final Integer CODE_400070 = 400070;
    public static final String CODE_400070_MSG = "流程启动失败，请稍后重试！";
    
    /**当前流程实例是无效的或该实例被挂起！*/
    public static final Integer CODE_400080 = 400080;
    public static final String CODE_400080_MSG = "当前流程实例是无效的或该实例被挂起！";
    
    /**该任务ID无效或该任务不存在！*/
    public static final Integer CODE_400090 = 400090;
    public static final String CODE_400090_MSG = "该任务ID无效或该任务不存在！";
    
    /**任务流转失败，请稍后重试！*/
    public static final Integer CODE_400100 = 400100;
    public static final String CODE_400100_MSG = "任务流转失败，请稍后重试！";
    
}
