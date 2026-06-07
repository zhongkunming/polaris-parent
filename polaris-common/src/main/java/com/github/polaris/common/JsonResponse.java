package com.github.polaris.common;

import com.github.polaris.error.ErrorCode;
import com.github.polaris.error.ServiceErrorCode;
import com.github.polaris.context.TraceContextHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一响应结果
 *
 * @author zhongkunming
 */
@Data
@Schema(description = "响应结果")
public class JsonResponse<T> {

    @Schema(description = "是否成功")
    private Boolean isSuccess;

    @Schema(description = "响应码")
    private String errorCode;

    @Schema(description = "交易ID")
    private String jnNo = TraceContextHelper.currentTrace();

    @Schema(description = "响应消息")
    private String expInfo;

    @Schema(description = "响应数据")
    private T returnObject;

    public static <T> JsonResponse<T> ok() {
        JsonResponse<T> result = new JsonResponse<>();
        result.setIsSuccess(true);
        result.setErrorCode(ServiceErrorCode.SUCCESS.getCode());
        result.setExpInfo(ServiceErrorCode.SUCCESS.getMsg());
        return result;
    }

    public static <T> JsonResponse<T> ok(T data) {
        JsonResponse<T> result = ok();
        result.setReturnObject(data);
        return result;
    }

    public static <T> JsonResponse<T> error(ErrorCode errorCode, Object... params) {
        JsonResponse<T> result = new JsonResponse<>();
        result.setIsSuccess(false);
        result.setErrorCode(errorCode.getCode());
        result.setExpInfo(errorCode.getErrorMessage(params));
        return result;
    }

    public static <T> JsonResponse<T> error(Object... params) {
        JsonResponse<T> result = new JsonResponse<>();
        result.setIsSuccess(false);
        result.setErrorCode(ServiceErrorCode.ERROR_CUSTOM.getCode());
        result.setExpInfo(ServiceErrorCode.ERROR_CUSTOM.getErrorMessage(params));
        return result;
    }
}
