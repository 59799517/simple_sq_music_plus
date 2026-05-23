  package com.sqmusicplus.v3.plug.qobuz.config;

import com.sqmusicplus.v3.plug.base.config.PlugConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname QobuzConfig
 * @Description Qobuz配置文件
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qobuz")
public class QobuzConfig extends PlugConfig {
    private String appId;
    private String baseUrl;
    private String searchTrackUrl;
    private String searchAlbumUrl;
    private String trackInfoUrl;
    private String albumInfoUrl;
    private String artistInfoUrl;
    private String downloadUrl;
    private String lyricUrl;
    private String imageUrl;
}
