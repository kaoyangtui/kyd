package com.pig4cloud.pigx.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaoliang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yt")
public class YtConfig {
    private String url;
}
