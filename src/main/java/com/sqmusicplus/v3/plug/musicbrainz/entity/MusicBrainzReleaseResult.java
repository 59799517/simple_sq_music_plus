package com.sqmusicplus.v3.plug.musicbrainz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicBrainzReleaseResult
 * @Description MusicBrainz专辑结果实体
 * @Version 1.0.0
 * @Date 2026/5/23 10:15
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class MusicBrainzReleaseResult {
    
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
    
    @JsonProperty("release-events")
    private List<ReleaseEvent> releaseEvents;
    
    @JsonProperty("artist-credit")
    private List<ArtistCredit> artistCredit;
    
    @JsonProperty("label-info")
    private List<LabelInfo> labelInfo;
    
    @JsonProperty("media")
    private List<Media> media;
    
    @JsonProperty("text-representation")
    private TextRepresentation textRepresentation;
    
    @NoArgsConstructor
    @Data
    public static class ReleaseEvent {
        @JsonProperty("area")
        private Area area;
        
        @JsonProperty("date")
        private String date;
        
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
    }
    
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
    public static class LabelInfo {
        @JsonProperty("catalog-number")
        private String catalogNumber;
        
        @JsonProperty("label")
        private Label label;
        
        @NoArgsConstructor
        @Data
        public static class Label {
            @JsonProperty("id")
            private String id;
            
            @JsonProperty("name")
            private String name;
        }
    }
    
    @NoArgsConstructor
    @Data
    public static class Media {
        @JsonProperty("format")
        private String format;
        
        @JsonProperty("track-count")
        private Integer trackCount;
        
        @JsonProperty("tracks")
        private List<Track> tracks;
        
        @NoArgsConstructor
        @Data
        public static class Track {
            @JsonProperty("id")
            private String id;
            
            @JsonProperty("number")
            private String number;
            
            @JsonProperty("title")
            private String title;
            
            @JsonProperty("length")
            private Integer length;
            
            @JsonProperty("artist-credit")
            private List<ArtistCredit> artistCredit;
            
            @JsonProperty("recording")
            private Recording recording;
            
            @NoArgsConstructor
            @Data
            public static class Recording {
                @JsonProperty("id")
                private String id;
                
                @JsonProperty("title")
                private String title;
                
                @JsonProperty("length")
                private Integer length;
                
                @JsonProperty("isrcs")
                private List<String> isrcs;
            }
        }
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
