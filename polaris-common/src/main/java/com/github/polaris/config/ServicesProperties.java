package com.github.polaris.config;

import com.github.polaris.constants.PropertiesConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = PropertiesConstants.SERVICES_PROPERTIES_PREFIX)
public class ServicesProperties {

    /**
     * 系统编码
     */
    private String sysCode = "1001";

    /**
     * token 检查间隔
     */
    private Duration tokenCheckInterval = Duration.ofMinutes(10L);
}
