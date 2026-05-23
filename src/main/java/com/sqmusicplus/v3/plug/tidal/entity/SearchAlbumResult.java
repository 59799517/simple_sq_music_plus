package com.sqmusicplus.v3.plug.tidal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchAlbumResult
 * @Description Tidal搜索专辑结果
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class SearchAlbumResult {

    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("totalNumberOfItems")
    private Integer totalNumberOfItems;
    @JsonProperty("items")
    private List<ItemsDTO> items;

    @NoArgsConstructor
    @Data
    public static class ItemsDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("duration")
        private Integer duration;
        @JsonProperty("streamReady")
        private Boolean streamReady;
        @JsonProperty("payToStream")
        private Boolean payToStream;
        @JsonProperty("adSupportedStreamReady")
        private Boolean adSupportedStreamReady;
        @JsonProperty("djReady")
        private Boolean djReady;
        @JsonProperty("stemReady")
        private Boolean stemReady;
        @JsonProperty("streamStartDate")
        private String streamStartDate;
        @JsonProperty("allowStreaming")
        private Boolean allowStreaming;
        @JsonProperty("premiumStreamingOnly")
        private Boolean premiumStreamingOnly;
        @JsonProperty("numberOfTracks")
        private Integer numberOfTracks;
        @JsonProperty("numberOfVideos")
        private Integer numberOfVideos;
        @JsonProperty("numberOfVolumes")
        private Integer numberOfVolumes;
        @JsonProperty("releaseDate")
        private String releaseDate;
        @JsonProperty("copyright")
        private String copyright;
        @JsonProperty("type")
        private String type;
        @JsonProperty("version")
        private Object version;
        @JsonProperty("url")
        private String url;
        @JsonProperty("cover")
        private String cover;
        @JsonProperty("vibrantColor")
        private String vibrantColor;
        @JsonProperty("videoCover")
        private Object videoCover;
        @JsonProperty("explicit")
        private Boolean explicit;
        @JsonProperty("upc")
        private String upc;
        @JsonProperty("popularity")
        private Integer popularity;
        @JsonProperty("audioQuality")
        private String audioQuality;
        @JsonProperty("audioModes")
        private List<String> audioModes;
        @JsonProperty("mediaMetadata")
        private MediaMetadataDTO mediaMetadata;
        @JsonProperty("upload")
        private Boolean upload;
        @JsonProperty("artist")
        private ArtistDTO artist;
        @JsonProperty("artists")
        private List<ArtistsDTO> artists;

        @NoArgsConstructor
        @Data
        public static class MediaMetadataDTO {
            @JsonProperty("tags")
            private List<String> tags;
        }

        @NoArgsConstructor
        @Data
        public static class ArtistDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("handle")
            private Object handle;
            @JsonProperty("type")
            private String type;
            @JsonProperty("picture")
            private Object picture;
        }

        @NoArgsConstructor
        @Data
        public static class ArtistsDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("handle")
            private Object handle;
            @JsonProperty("type")
            private String type;
            @JsonProperty("picture")
            private Object picture;
        }
    }
}
