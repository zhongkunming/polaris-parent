package com.github.polaris.interceptor.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.github.polaris.interceptor.ContextHandlerInterceptor;
import jakarta.annotation.Resource;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongkunming
 */
@Component
@ConditionalOnBean(InterceptorProperties.class)
public class InterceptorWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private ContextHandlerInterceptor contextInterceptor;

    @Resource
    private InterceptorProperties interceptorProperties;

    private final List<String> DEFAULT_PATTERNS = CollUtil.newArrayList(
            "/error",
            "/favicon.ico",
            "/doc.html",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/v3/api-docs/swagger-config",
            "/swagger-ui.html",
            "/swagger-ui*/**",
            "/swagger-ui*/*swagger-initializer.js",
            "/webjars/**"
    );

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        List<String> ignoreUrlPatterns = new ArrayList<>();
        if (interceptorProperties.getIgnoreDefaultUrlPatterns()) {
            ignoreUrlPatterns = ListUtil.toList(DEFAULT_PATTERNS);
        }
        ignoreUrlPatterns.addAll(interceptorProperties.getIgnoreUrlPatterns());
        registry.addInterceptor(contextInterceptor)
                .excludePathPatterns(ignoreUrlPatterns);
    }
}
