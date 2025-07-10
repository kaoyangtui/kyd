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
    /**
     * 接口基础地址
     */
    private String baseUrl;

    /**
     * sf1-v1 接口
     */
    private String sf1V1Url;

    /**
     * pi11 接口
     */
    private String pi11Url;

    /**
     * pi12 接口
     */
    private String pi12Url;

    /**
     * pi16 接口
     */
    private String pi16Url;
}
