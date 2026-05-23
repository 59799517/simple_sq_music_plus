package com.sqmusicplus.v3.plug.tidal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname TrackManifestResult
 * @Description Tidal歌曲播放清单（下载链接）
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class TrackManifestResult {
    
    @JsonProperty("trackId")
    private String trackId;
    
    @JsonProperty("manifest")
    private String manifest;
    
    @JsonProperty("manifestMimeType")
    private String manifestMimeType;
    
    @JsonProperty("albumReplayGain")
    private Double albumReplayGain;
    
    @JsonProperty("trackReplayGain")
    private Double trackReplayGain;
}
