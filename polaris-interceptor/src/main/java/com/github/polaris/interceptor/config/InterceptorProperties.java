package com.github.polaris.interceptor.config;

import com.github.polaris.constants.PropertiesConstants;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = PropertiesConstants.INTERCEPTOR_PROPERTIES_PREFIX)
@ConditionalOnProperty(prefix = PropertiesConstants.INTERCEPTOR_PROPERTIES_PREFIX,
        name = "enabled", havingValue = "true", matchIfMissing = true)
public class InterceptorProperties {

    /**
     * 是否开启通用请求拦截器
     */
    private Boolean enabled = true;

    /**
     * 是否忽略默认的url
     */
    private Boolean ignoreDefaultUrlPatterns = true;

    /**
     * 需要忽略的url集合
     */
    private List<String> ignoreUrlPatterns = new ArrayList<>();

    @PostConstruct
    public void afterConstruct() {
        log.info("开启默认请求拦截器");
    }
}
