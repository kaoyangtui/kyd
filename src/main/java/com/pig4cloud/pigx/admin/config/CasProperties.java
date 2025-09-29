package com.pig4cloud.pigx.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 对应 application.yml 中的 cas: host & service
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cas")
public class CasProperties {
    private String host;
    private String service;
}
