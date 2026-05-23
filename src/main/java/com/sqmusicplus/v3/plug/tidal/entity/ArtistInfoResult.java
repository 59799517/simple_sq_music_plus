package com.sqmusicplus.v3.plug.tidal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname ArtistInfoResult
 * @Description Tidal艺术家详情
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class ArtistInfoResult {
    
    @JsonProperty("id")
    private Object id;  // Tidal API 可能返回 Integer 或 String
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("picture")
    private String picture;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("popularity")
    private Integer popularity;
    
    @JsonProperty("biography")
    private String biography;
    
    /**
     * 获取艺术家ID（转换为String）
     */
    public String getId() {
        if (id == null) {
            return null;
        }
        return id.toString();
    }
}
