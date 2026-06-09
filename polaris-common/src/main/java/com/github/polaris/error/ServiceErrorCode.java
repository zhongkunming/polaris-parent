package com.github.polaris.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认错误码枚举
 *
 * @author zhongkunming
 */
@Getter
@AllArgsConstructor
public enum ServiceErrorCode implements ErrorCode {

    SUCCESS("200", "请求成功"),

    TOKEN_INVALID("90001", "登录已失效，请重新登录"),

    REQUEST_METHOD_NOT_SUPPORTED("90101", "请求方法不支持，{}"),
    REQUEST_MEDIA_NOT_SUPPORTED("90102", "请求参数类型不支持"),
    PARAMS_VALID_ERROR("90103", "参数校验不通过，{}"),
    REQUEST_PARAMS_MISS("90104", "请求参数缺失"),
    PATH_VARIABLE_MISS("90105", "请求路径参数缺失"),
    REQUEST_FILE_TOO_LARGE("90106", "上传文件大小超过限制"),
    MEDIA_TYPE_MISS_ERROR("90107", "数据类型转换失败"),

    DATABASE_ACCESS_ERROR("90200", "[数据库执行异常] 原因：{}"),

    ERROR_CUSTOM("99990", "{}"),
    ERROR("99999", "系统异常，请联系系统管理员");

    private final String code;

    private final String msg;
}
