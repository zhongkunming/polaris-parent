package com.github.polaris.interceptor;

import com.github.polaris.common.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;

/**
 * 可自定义拦截器增强，多个实现注意顺序
 */
public interface ContextProcessor extends Ordered {

    /**
     * 如果要拿到对应的handler <br/>
     * request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE) <br/>
     */
    UserContext beforeHandle(HttpServletRequest request, UserContext userContext);

    /**
     * 如果要获取异常信息 <br/>
     * Exception exception = (Exception) request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE) <br/>
     */
    void afterHandler(HttpServletRequest request);
}
