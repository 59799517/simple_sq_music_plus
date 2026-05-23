package com.sqmusicplus.v3.plug.qobuz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchTrackResult
 * @Description Qobuz搜索歌曲结果
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@NoArgsConstructor
@Data
public class SearchTrackResult {
    @JsonProperty("tracks")
    private Tracks tracks;

    @NoArgsConstructor
    @Data
    public static class Tracks {
        @JsonProperty("items")
        private List<ItemDTO> items;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("limit")
        private Integer limit;
        @JsonProperty("offset")
        private Integer offset;

        @NoArgsConstructor
        @Data
        public static class ItemDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("title")
            private String title;
            @JsonProperty("version")
            private String version;
            @JsonProperty("duration")
            private Integer duration;
            @JsonProperty("track_number")
            private Integer trackNumber;
            @JsonProperty("album")
            private Album album;
            @JsonProperty("artist")
            private Artist artist;
            @JsonProperty("performers")
            private String performers;
            @JsonProperty("isrc")
            private String isrc;
            @JsonProperty("maximum_bit_depth")
            private Integer maximumBitDepth;
            @JsonProperty("maximum_sampling_rate")
            private Double maximumSamplingRate;
            @JsonProperty("hires")
            private Boolean hires;
            @JsonProperty("hires_streamable")
            private Boolean hiresStreamable;
            @JsonProperty("streamable")
            private Boolean streamable;
            @JsonProperty("downloadable")
            private Boolean downloadable;
            @JsonProperty("parental_warning")
            private Boolean parentalWarning;

            @NoArgsConstructor
            @Data
            public static class Album {
                @JsonProperty("id")
                private String id;
                @JsonProperty("title")
                private String title;
                @JsonProperty("image")
                private Image image;
                @JsonProperty("release_date_original")
                private String releaseDateOriginal;
                @JsonProperty("upc")
                private String upc;

                @NoArgsConstructor
                @Data
                public static class Image {
                    @JsonProperty("small")
                    private String small;
                    @JsonProperty("thumbnail")
                    private String thumbnail;
                    @JsonProperty("large")
                    private String large;
                    @JsonProperty("back")
                    private String back;
                }
            }

            @NoArgsConstructor
            @Data
            public static class Artist {
                @JsonProperty("id")
                private String id;
                @JsonProperty("name")
                private String name;
                @JsonProperty("image")
                private String image;
            }
        }
    }
}
