package com.github.polaris.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.github.polaris.anotation.Anonymous;
import com.github.polaris.common.UserContext;
import com.github.polaris.config.ServicesProperties;
import com.github.polaris.constants.CommonConstants;
import com.github.polaris.context.UserContextHelper;
import com.github.polaris.error.ServiceErrorCode;
import com.github.polaris.exception.ServiceException;
import com.github.polaris.interceptor.config.InterceptorProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Getter
@Component
@ConditionalOnBean(InterceptorProperties.class)
public class ContextHandlerInterceptor implements HandlerInterceptor {

    private final List<ContextProcessor> contextProcessors;

    private final ServicesProperties servicesProperties;

    public ContextHandlerInterceptor(@Autowired(required = false) List<ContextProcessor> contextProcessors,
                                     ServicesProperties servicesProperties) {
        if (CollectionUtil.isEmpty(contextProcessors)) {
            contextProcessors = new ArrayList<>();
        }
        contextProcessors.sort(Comparator.comparingInt(Ordered::getOrder));
        this.contextProcessors = contextProcessors;
        this.servicesProperties = servicesProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (this.checkIgnoreRequest(request, handler)) {
            return true;
        }
        UserContext context = this.currentUser(request);
        for (ContextProcessor contextProcessor : this.getContextProcessors()) {
            context = contextProcessor.beforeHandle(request, context);
        }
        UserContextHelper.setUser(context);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        for (ContextProcessor contextProcessor : this.getContextProcessors()) {
            contextProcessor.afterHandler(request);
        }
        UserContextHelper.clearUser();
    }

    public boolean checkIgnoreRequest(HttpServletRequest request, Object handler) {
        return this.ignoreByAnnotation(handler);
    }

    public boolean ignoreByAnnotation(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            return handlerMethod.hasMethodAnnotation(Anonymous.class);
        }
        return false;
    }

    private UserContext currentUser(HttpServletRequest request) {
        String token = request.getHeader(CommonConstants.AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            log.error("获取当前用户信息失败，请求头中的凭证为空");
            throw new ServiceException(ServiceErrorCode.TOKEN_INVALID);
        }

        // todo
        return new UserContext();
    }
}
