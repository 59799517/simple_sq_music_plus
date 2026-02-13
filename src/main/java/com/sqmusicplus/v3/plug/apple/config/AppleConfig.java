package com.sqmusicplus.v3.plug.apple.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname AppleConfig
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/10/13 11:41
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "apple")
public class AppleConfig {
    private String BaseUrl;

}
