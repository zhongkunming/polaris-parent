package com.github.polaris.exception;

import com.github.polaris.common.JsonResponse;
import com.github.polaris.config.ExceptionProperties;
import com.github.polaris.error.ServiceErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author zhongkunming
 * @since 2025-11-07
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(ExceptionProperties.class)
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public JsonResponse<Void> handleServiceException(ServiceException e) {
        log.error("[应用异常] {}", e.getMessage(), e);
        return JsonResponse.error(e.getErrorCode(), e.getParams());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = String.format("不支持 %s 请求方法", e.getMethod());
        log.error("[请求方法不支持] {}", message);
        return JsonResponse.error(ServiceErrorCode.REQUEST_METHOD_NOT_SUPPORTED, message);
    }

    /**
     * 处理媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JsonResponse<Void> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        String message = String.format("不支持的媒体类型：%s", e.getContentType());
        log.error("[媒体类型不支持] {}", message);
        return JsonResponse.error(ServiceErrorCode.REQUEST_MEDIA_NOT_SUPPORTED, message);
    }

    /**
     * 处理参数校验异常（@Valid注解）
     */
    @ExceptionHandler(BindException.class)
    public JsonResponse<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("，"));
        log.error("[参数校验不通过] {}", message);
        return JsonResponse.error(ServiceErrorCode.PARAMS_VALID_ERROR, message);
    }

    /**
     * 处理参数约束违反异常（@Validated注解）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public JsonResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("，"));
        log.error("[参数校验不通过] {}", message);
        return JsonResponse.error(ServiceErrorCode.PARAMS_VALID_ERROR, message);
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public JsonResponse<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = String.format("请求参数缺失: %s", e.getParameterName());
        log.error("[请求参数缺失] {}", message);
        return JsonResponse.error(ServiceErrorCode.REQUEST_PARAMS_MISS, message);
    }

    /**
     * 处理请求路径变量缺失异常
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public JsonResponse<Void> handleMissingPathVariableException(MissingPathVariableException e) {
        String message = String.format("请求路径参数缺失: %s", e.getVariableName());
        log.error("[请求路径参数缺失] {}", message);
        return JsonResponse.error(ServiceErrorCode.PATH_VARIABLE_MISS, message);
    }

    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonResponse<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        String message = String.format("上传文件大小超过限制: %d bytes", e.getMaxUploadSize());
        log.error("[上传文件大小超过限制] {}", message);
        return JsonResponse.error(ServiceErrorCode.REQUEST_FILE_TOO_LARGE, message);
    }

    /**
     * 处理数据类型转换失败
     */
    @ExceptionHandler({TypeMismatchException.class, HttpMessageNotReadableException.class})
    public JsonResponse<Void> handleTypeMismatchException(Exception e) {
        log.error("[数据类型转换失败] {}", e.getMessage(), e);
        return JsonResponse.error(ServiceErrorCode.MEDIA_TYPE_MISS_ERROR);
    }

    /**
     * 数据访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    public JsonResponse<Void> handleSQLException(DataAccessException e) {
        Throwable cause = e.getRootCause();
        if (Objects.isNull(cause) || !(cause instanceof SQLException sqlException)) {
            return handleException(e);
        }
        int errorCode = sqlException.getErrorCode();
        String sqlState = sqlException.getSQLState();
        String errorMessage = sqlException.getMessage();
        log.error("[数据异常] [底层数据库异常] errorCode: {}, sqlState: {}, errorMessage: {}", errorCode, sqlState, errorMessage, e);
        return JsonResponse.error(ServiceErrorCode.DATABASE_ACCESS_ERROR, errorMessage);
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public JsonResponse<Void> handleException(Exception e) {
        log.error("[系统异常] {}", e.getMessage(), e);
        return JsonResponse.error(ServiceErrorCode.ERROR);
    }
}
