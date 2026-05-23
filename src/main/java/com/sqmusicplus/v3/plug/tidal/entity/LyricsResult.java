package com.sqmusicplus.v3.plug.tidal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname LyricsResult
 * @Description Tidal歌词结果
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class LyricsResult {
    
    @JsonProperty("trackId")
    private String trackId;
    
    @JsonProperty("lyrics")
    private String lyrics;
    
    @JsonProperty("subtitles")
    private String subtitles;
}
