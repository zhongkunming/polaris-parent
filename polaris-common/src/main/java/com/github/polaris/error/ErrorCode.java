package com.github.polaris.error;


import cn.hutool.core.util.StrUtil;

/**
 * 错误码接口
 *
 * @author zhongkunming
 */
public interface ErrorCode {

    String getCode();

    String getMsg();

    default String getErrorMessage(Object... params) {
        return StrUtil.format(getMsg(), params);
    }
}
