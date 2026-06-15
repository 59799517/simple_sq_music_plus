package com.sqmusicplus.v3.plug.musicbrainz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicBrainzArtistResult
 * @Description MusicBrainz艺术家结果实体
 * @Version 1.0.0
 * @Date 2026/5/23 10:10
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class MusicBrainzArtistResult {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("sort-name")
    private String sortName;
    
    @JsonProperty("country")
    private String country;
    
    @JsonProperty("area")
    private Area area;
    
    @JsonProperty("begin-area")
    private Area beginArea;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("disambiguation")
    private String disambiguation;
    
    @JsonProperty("life-span")
    private LifeSpan lifeSpan;
    
    @NoArgsConstructor
    @Data
    public static class Area {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("sort-name")
        private String sortName;
    }
    
    @NoArgsConstructor
    @Data
    public static class LifeSpan {
        @JsonProperty("ended")
        private Boolean ended;
        
        @JsonProperty("begin")
        private String begin;
        
        @JsonProperty("end")
        private String end;
    }
}
