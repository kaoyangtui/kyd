package com.pig4cloud.pigx.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaoliang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cnipr")
public class CnirpConfig {
    private String userAccount;
    private String userPassword;
    private String clientId;
    private String clientSecret;
}
