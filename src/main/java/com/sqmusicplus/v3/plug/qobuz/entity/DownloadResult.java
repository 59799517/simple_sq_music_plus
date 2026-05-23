package com.sqmusicplus.v3.plug.qobuz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname DownloadResult
 * @Description Qobuz下载链接结果
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@NoArgsConstructor
@Data
public class DownloadResult {
    @JsonProperty("url")
    private String url;
    @JsonProperty("format_id")
    private String formatId;
    @JsonProperty("mime_type")
    private String mimeType;
    @JsonProperty("sampling_rate")
    private Double samplingRate;
    @JsonProperty("bit_depth")
    private Integer bitDepth;
}
