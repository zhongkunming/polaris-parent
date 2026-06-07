package com.github.polaris.filter;

import cn.hutool.core.util.IdUtil;
import com.github.polaris.config.FilterProperties;
import com.github.polaris.constants.CommonConstants;
import com.github.polaris.context.TraceContextHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 应用层统一过滤器
 *
 * @author zhongkunming
 * @since 2025-11-07
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/**")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(FilterProperties.class)
public class GlobalWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String traceId = request.getHeader(CommonConstants.HEADER_X_TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = IdUtil.getSnowflakeNextIdStr();
        }
        TraceContextHelper.setTrace(traceId);
        long inboundTs = System.currentTimeMillis();
        request.setAttribute(CommonConstants.ATTR_INBOUND_TIMESTAMP, inboundTs);
        String url = request.getRequestURI();
        log.info("请求开始, url: {}", url);
        chain.doFilter(request, response);
        long outboundTs = System.currentTimeMillis();
        log.info("请求结束, url: {}, 耗时: {} ms", url, outboundTs - inboundTs);
        TraceContextHelper.clearTrace();
    }
}
