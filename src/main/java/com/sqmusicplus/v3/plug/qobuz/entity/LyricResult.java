package com.sqmusicplus.v3.plug.qobuz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname LyricResult
 * @Description Qobuz歌词结果
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@NoArgsConstructor
@Data
public class LyricResult {
    @JsonProperty("track_id")
    private String trackId;
    @JsonProperty("lyrics")
    private String lyrics;
}
