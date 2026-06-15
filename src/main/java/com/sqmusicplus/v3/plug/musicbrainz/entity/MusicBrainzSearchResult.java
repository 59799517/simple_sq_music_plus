package com.sqmusicplus.v3.plug.musicbrainz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicBrainzSearchResult
 * @Description MusicBrainz搜索结果实体
 * @Version 1.0.0
 * @Date 2026/5/23 10:05
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class MusicBrainzSearchResult {
    
    @JsonProperty("created")
    private String created;
    
    @JsonProperty("count")
    private Integer count;
    
    @JsonProperty("offset")
    private Integer offset;
    
    @JsonProperty("recordings")
    private List<Recording> recordings;
    
    @NoArgsConstructor
    @Data
    public static class Recording {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("title")
        private String title;
        
        @JsonProperty("length")
        private Integer length; // 时长（毫秒）
        
        @JsonProperty("artist-credit")
        private List<ArtistCredit> artistCredit;
        
        @JsonProperty("releases")
        private List<Release> releases;
        
        @JsonProperty("isrcs")
        private List<String> isrcs;
        
        @NoArgsConstructor
        @Data
        public static class ArtistCredit {
            @JsonProperty("artist")
            private Artist artist;
            
            @JsonProperty("name")
            private String name;
            
            @NoArgsConstructor
            @Data
            public static class Artist {
                @JsonProperty("id")
                private String id;
                
                @JsonProperty("name")
                private String name;
                
                @JsonProperty("sort-name")
                private String sortName;
            }
        }
        
        @NoArgsConstructor
        @Data
        public static class Release {
            @JsonProperty("id")
            private String id;
            
            @JsonProperty("title")
            private String title;
            
            @JsonProperty("status")
            private String status;
            
            @JsonProperty("date")
            private String date;
            
            @JsonProperty("media")
            private List<Media> media;
            
            @NoArgsConstructor
            @Data
            public static class Media {
                @JsonProperty("format")
                private String format;
                
                @JsonProperty("track-count")
                private Integer trackCount;
            }
        }
    }
}
