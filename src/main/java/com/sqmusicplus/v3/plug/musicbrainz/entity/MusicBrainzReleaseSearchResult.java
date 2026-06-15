package com.sqmusicplus.v3.plug.musicbrainz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicBrainzReleaseSearchResult
 * @Description MusicBrainz 专辑搜索结果实体
 * @Version 1.0.0
 * @Date 2026/5/23 11:05
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class MusicBrainzReleaseSearchResult {
    
    @JsonProperty("created")
    private String created;
    
    @JsonProperty("count")
    private Integer count;
    
    @JsonProperty("offset")
    private Integer offset;
    
    @JsonProperty("releases")
    private List<Release> releases;
    
    @NoArgsConstructor
    @Data
    public static class Release {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("title")
        private String title;
        
        @JsonProperty("status")
        private String status;
        
        @JsonProperty("quality")
        private String quality;
        
        @JsonProperty("date")
        private String date;
        
        @JsonProperty("country")
        private String country;
        
        @JsonProperty("barcode")
        private String barcode;
        
        @JsonProperty("disambiguation")
        private String disambiguation;
        
        @JsonProperty("packaging")
        private String packaging;
        
        @JsonProperty("artist-credit")
        private List<ArtistCredit> artistCredit;
        
        @JsonProperty("release-group")
        private ReleaseGroup releaseGroup;
        
        @JsonProperty("media")
        private List<Media> media;
        
        @JsonProperty("text-representation")
        private TextRepresentation textRepresentation;
        
        @JsonProperty("score")
        private Integer score;
        
        @NoArgsConstructor
        @Data
        public static class ArtistCredit {
            @JsonProperty("name")
            private String name;
            
            @JsonProperty("joinphrase")
            private String joinPhrase;
            
            @JsonProperty("artist")
            private Artist artist;
            
            @NoArgsConstructor
            @Data
            public static class Artist {
                @JsonProperty("id")
                private String id;
                
                @JsonProperty("name")
                private String name;
                
                @JsonProperty("sort-name")
                private String sortName;
                
                @JsonProperty("disambiguation")
                private String disambiguation;
            }
        }
        
        @NoArgsConstructor
        @Data
        public static class ReleaseGroup {
            @JsonProperty("id")
            private String id;
            
            @JsonProperty("title")
            private String title;
            
            @JsonProperty("primary-type")
            private String primaryType;
            
            @JsonProperty("secondary-types")
            private List<String> secondaryTypes;
            
            @JsonProperty("first-release-date")
            private String firstReleaseDate;
        }
        
        @NoArgsConstructor
        @Data
        public static class Media {
            @JsonProperty("position")
            private Integer position;
            
            @JsonProperty("format")
            private String format;
            
            @JsonProperty("track-count")
            private Integer trackCount;
            
            @JsonProperty("disc-count")
            private Integer discCount;
        }
        
        @NoArgsConstructor
        @Data
        public static class TextRepresentation {
            @JsonProperty("language")
            private String language;
            
            @JsonProperty("script")
            private String script;
        }
    }
}
