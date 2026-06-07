package com.github.polaris.constants;

/**
 * 全局常量集合
 *
 * @author zhongkunming
 */
public final class CommonConstants {

    /**
     * SQL limit 1
     */
    public static final String SQL_LAST_LIMIT_1 = "limit 1";


    /**
     * 请求属性 开始时间键
     */
    public static final String ATTR_INBOUND_TIMESTAMP = "#inboundTs";

    /**
     * 请求属性 sso token
     */
    public static final String ATTR_TOKEN = "#token";


    /**
     * 请求头 traceId
     */
    public static final String HEADER_X_TRACE_ID = "X-Trace-Id";

    /**
     * 请求头 用户信息
     */
    public static final String HEADER_X_USER_INFO = "X-User-Info";

    /**
     * 请求头 Token
     */
    public static final String AUTHORIZATION = "Authorization";


    public static final String TRACE_ID = "traceId";

    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static final Integer DEFAULT_PAGE_NUM = 1;
}
