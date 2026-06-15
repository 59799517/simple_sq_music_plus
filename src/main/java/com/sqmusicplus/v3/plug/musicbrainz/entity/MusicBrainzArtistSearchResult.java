package com.sqmusicplus.v3.plug.musicbrainz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicBrainzArtistSearchResult
 * @Description MusicBrainz 艺术家搜索结果实体
 * @Version 1.0.0
 * @Date 2026/5/23 11:00
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class MusicBrainzArtistSearchResult {
    
    @JsonProperty("created")
    private String created;
    
    @JsonProperty("count")
    private Integer count;
    
    @JsonProperty("offset")
    private Integer offset;
    
    @JsonProperty("artists")
    private List<Artist> artists;
    
    @NoArgsConstructor
    @Data
    public static class Artist {
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
        
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("gender")
        private String gender;
        
        @JsonProperty("disambiguation")
        private String disambiguation;
        
        @JsonProperty("life-span")
        private LifeSpan lifeSpan;
        
        @JsonProperty("aliases")
        private List<Alias> aliases;
        
        @JsonProperty("tags")
        private List<Tag> tags;
        
        @JsonProperty("score")
        private Integer score;
        
        @NoArgsConstructor
        @Data
        public static class Area {
            @JsonProperty("id")
            private String id;
            
            @JsonProperty("name")
            private String name;
            
            @JsonProperty("sort-name")
            private String sortName;
            
            @JsonProperty("iso-3166-1-codes")
            private List<String> iso3166_1Codes;
        }
        
        @NoArgsConstructor
        @Data
        public static class LifeSpan {
            @JsonProperty("begin")
            private String begin;
            
            @JsonProperty("end")
            private String end;
            
            @JsonProperty("ended")
            private Boolean ended;
        }
        
        @NoArgsConstructor
        @Data
        public static class Alias {
            @JsonProperty("sort-name")
            private String sortName;
            
            @JsonProperty("name")
            private String name;
            
            @JsonProperty("locale")
            private String locale;
            
            @JsonProperty("type")
            private String type;
            
            @JsonProperty("primary")
            private Boolean primary;
            
            @JsonProperty("begin-date")
            private String beginDate;
            
            @JsonProperty("end-date")
            private String endDate;
        }
        
        @NoArgsConstructor
        @Data
        public static class Tag {
            @JsonProperty("name")
            private String name;
            
            @JsonProperty("count")
            private Integer count;
        }
    }
}
