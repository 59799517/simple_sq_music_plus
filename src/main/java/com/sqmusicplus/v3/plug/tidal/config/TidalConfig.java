package com.sqmusicplus.v3.plug.tidal.config;

import com.sqmusicplus.v3.plug.base.config.PlugConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname TidalConfig
 * @Description Tidal配置文件
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tidal")
public class TidalConfig extends PlugConfig {
    private String clientId;
    private String clientSecret;
    private String tokenUrl;
    private String baseUrl;
    private String searchUrl;
    private String trackUrl;
    private String albumUrl;
    private String artistUrl;
    private String trackManifestUrl;
    private String lyricsUrl;
    private String artistAlbumsUrl;
    private String artistTracksUrl;
    private String playlistUrl;
}
