package com.github.polaris.exception;

import cn.hutool.core.util.ArrayUtil;
import com.github.polaris.error.ErrorCode;
import com.github.polaris.error.ServiceErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 *
 * @author zhongkunming
 * @since 2025-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private ErrorCode errorCode;

    /**
     * 错误参数，用于格式化错误信息
     */
    private Object[] params;

    /**
     * 构造函数
     */
    public ServiceException(Exception e) {
        super(e);
        init(null);
    }

    /**
     * 构造函数
     */
    public ServiceException(Object... params) {
        init(null, params);
    }

    /**
     * 构造函数
     */
    public ServiceException(Exception e, Object... params) {
        super(e);
        init(null, params);
    }

    /**
     * 构造函数
     */
    public ServiceException(ErrorCode errorCode, Object... params) {
        init(errorCode, params);
    }

    /**
     * 构造函数
     */
    public ServiceException(Exception e, ErrorCode errorCode, Object... params) {
        super(e);
        init(errorCode, params);
    }

    /**
     * 获取格式化后的错误信息
     */
    @Override
    public String getMessage() {
        return errorCode.getErrorMessage(params);
    }

    /**
     * 初始化异常信息
     */
    private void init(ErrorCode errorCode, Object... params) {
        if (Objects.isNull(errorCode)) {
            errorCode = ArrayUtil.isEmpty(params) ? ServiceErrorCode.ERROR : ServiceErrorCode.ERROR_CUSTOM;
        }
        this.errorCode = errorCode;
        this.params = params;
    }
}
