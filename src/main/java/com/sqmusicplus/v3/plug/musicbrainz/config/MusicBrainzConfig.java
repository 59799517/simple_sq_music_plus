package com.sqmusicplus.v3.plug.musicbrainz.config;

import com.sqmusicplus.v3.plug.base.config.PlugConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MusicBrainzConfig
 * @Description MusicBrainz配置文件
 * @Version 1.0.0
 * @Date 2026/5/23 10:00
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "musicbrainz")
public class MusicBrainzConfig extends PlugConfig {
    private String baseUrl;
    private String searchUrl;
    private String artistSearchUrl;
    private String albumSearchUrl;
    private String recordingUrl;
    private String artistUrl;
    private String releaseUrl;
    private String isrcSearchUrl;
}
