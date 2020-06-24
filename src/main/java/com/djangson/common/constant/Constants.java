package com.djangson.common.constant;

public class Constants {

    public static final Integer YES = 1;
    public static final Integer NO = 0;

    // 系统是否字符标志
    public static final String YES_STRING = "Y";
    public static final String NO_STRING = "N";

    // JPUSH 分钟流量限制数量
    public static final int JPUSH_TOKEN_LIMIT = 600;

    public static final String AUTH_TOKEN_HEADER = "Authorization"; // 用户Token请求头

    // 系统默认响应格式
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    // 系统（惠展）客户编码
    public static final String HZ_CUSTOMER_CODE = "CUS000000000001";

    // 默认文件路径分隔符
    public static final String PATH_SEPARATOR = "/";

    // 系统内置角色编码主键
    public static final Long ROLE_SAAS_ADMIN_ID = 2L;
    public static final Long ROLE_SAAS_USER_ID = 3L;

    // 事务有效时间（持续300秒）
    public static final int TX_METHOD_TIMEOUT = 300;

    public static final String BLANK_STRING = "";

    public static final String FEIGN_TOKEN_HEADER = "Feign-Token"; // FeignToken端请求头
    public static final String FEIGN_TOKEN_VALUE = "ViCHAIN@startup2018#vichian.com"; // FeignToken端请求头

    // 分页默认值
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_MAX_PAGE_SIZE = 500;

    // SQL 语句转义字符
    public static final Character SQL_ESCAPE_CHARACTER = '/'; // SQL语句默认转义字符
    public static final Character SQL_MULTI_LIKE_CHARACTER = '%'; // SQL语句默认模糊匹配字符
}
