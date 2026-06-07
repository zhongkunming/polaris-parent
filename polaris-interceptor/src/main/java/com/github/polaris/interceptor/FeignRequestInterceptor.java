package com.github.polaris.interceptor;

import com.github.polaris.common.UserContext;
import com.github.polaris.constants.CommonConstants;
import com.github.polaris.context.TraceContextHelper;
import com.github.polaris.context.UserContextHelper;
import com.github.polaris.interceptor.config.InterceptorProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnBean(InterceptorProperties.class)
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Map<String, Collection<String>> headers = template.headers();
        if (headers.containsKey(CommonConstants.AUTHORIZATION)) {
            return;
        }

        String token = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes)) {
            HttpServletRequest request = attributes.getRequest();
            token = (String) request.getAttribute(CommonConstants.ATTR_TOKEN);
            if (StringUtils.isBlank(token)) {
                token = request.getHeader(CommonConstants.AUTHORIZATION);
            }
        }

        if (StringUtils.isBlank(token)) {
            UserContext userContext = UserContextHelper.currentUser();
            if (Objects.nonNull(userContext)) {
                token = userContext.getToken();
            }
        }
        template.header(CommonConstants.AUTHORIZATION, token);
        template.header(CommonConstants.HEADER_X_TRACE_ID, TraceContextHelper.currentTrace());
    }
}
