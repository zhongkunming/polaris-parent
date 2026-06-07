package com.github.polaris.config;

import com.github.polaris.constants.PropertiesConstants;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConfigurationProperties(prefix = PropertiesConstants.FILTER_PROPERTIES_PREFIX)
@ConditionalOnProperty(prefix = PropertiesConstants.FILTER_PROPERTIES_PREFIX,
        name = "enabled", havingValue = "true", matchIfMissing = true)
public class FilterProperties {

    /**
     * 是否开启全局过滤器
     */
    private Boolean enabled = true;

    @PostConstruct
    public void afterConstruct() {
        log.info("开启默认过滤器");
    }
}
