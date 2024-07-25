package com.sinocarbon.workflow.utils;

public class Constant {
	
    /**
     * word 后缀
     */
    public static final String DOC_SUFFIX = ".doc";
    public static final String DOCX_SUFFIX = ".docx";
    /**
     * excel 后缀
     */
    public static final String EXCEL_SUFFIX = ".xls";
    public static final String EXCELX_SUFFIX = ".xlsx";
    /**
     * ppt 后缀
     */
    public static final String PPT_SUFFIX = ".ppt";
    public static final String PPTX_SUFFIX = ".pptx";

    public static final String BAT_SUFFIX = ".bat";
    public static final String SH_SUFFIX = ".sh";
    public static final String ENTER_WIN = "\r\n";
    public static final String ENTER_LIN = "\n";
    
    /**
     * 账户状态
     */
    public static final Integer ACCOUNT_ON = 1;//启用
    public static final Integer ACCOUNT_FREEZE = 2;//冻结
    public static final Integer ACCOUNT_OFF = 3;//关闭
    
    /**
     * 文件上传路径
     */
    public static final String PERSON_IMAGE_PATH = "imgs/person";
    public static final String PROJECT_IMAGE_PATH = "imgs/project";
    public static final String COMPANY_IMAGE_PATH = "imgs/company";
    public static final String OTHER_IMAGE_PATH = "imgs/other";
    /**
     * md 后缀
     */
    public static final String MD_SUFFIX = ".md";
    /**
     * mapper扫描包
     */
    public static final String MAPPER_SCAN_PACKAGE = "com.sinocarbon.workflow.dao";

    public static final Integer PAGE_SIZE = 10;
    public static final Integer PAGE_NUMBER_INIT = 1;

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String FAIL = "fail";
    public static final String USER_DISABLED = "该用户被冻结或关闭，无法登陆！请联系管理员！";
    public static final String USER_DOESNOT_EXIST = "用户名不存在！";
    public static final String USER_HAVE_LANDED = "该用户已登陆！";
    public static final String CAPTCHA_ERROR = "验证码错误！";
    public static final String LOGIN_SUCCESS = "登陆成功！";
    public static final String CACHE_FAILURE = "缓存失效或用户已退出登录！";
    public static final String SUCCESS_MESSAGE = "操作成功！";
    public static final String ADD_MESSAGE = "添加成功！";
    public static final String MODFILY_MESSAGE = "修改成功！";
    public static final String DELETE_MESSAGE = "删除成功！";
    public static final String REPEAT_MESSAGE = "数据不重复可正常操作！";

    public static final String FAIL_MESSAGE = "操作失败！请重试！";

    public static final Integer FILE_SUCCESS_FLAG = 0;
    public static final Integer FILE_FAIL_FLAG = 1;
    public static final Integer FAIL_FLAG = 0;
    public static final Integer SUCCESS_FLAG = 1;
    public static final Integer DELETE_FLAG = 2;
    public static final Integer ADD_FLAG = 1;
    public static final Integer EDIT_FLAG = 2;
    public static final Integer OWN_ADD_FLAG = 1;

    public static final String SLASH = "/";
    public static final String SPOT = ".";
    public static final String UNDERLINE = "_";
    public static final String SMALL = "small";
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String GREATER = ">";
    
    public static final Integer APPLY_MODIFY_STATE = 0;//不可申请修改，不可审核申请
    public static final Integer CHECK_APPLY_MODIFIED_STATE = 1;//只可以申请修改
    public static final Integer CHECK_APPLY_MODIFY_STATE = 2;//只可以审核申请
    
    public static final String DS_NO = "dsno";// 数据库编号 从header中取
    
    public static final Integer DELETE_STATUS = 0;//删除状态
    
    /**
     * 默认的租户ID
     */
    public static final String DEF_TENANT_ID = "activitiTenantId";

}
