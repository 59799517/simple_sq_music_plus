package com.sqmusicplus.v3.plug.qobuz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchAlbumResult
 * @Description Qobuz搜索专辑结果
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@NoArgsConstructor
@Data
public class SearchAlbumResult {
    @JsonProperty("albums")
    private Albums albums;

    @NoArgsConstructor
    @Data
    public static class Albums {
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
            @JsonProperty("image")
            private Image image;
            @JsonProperty("artist")
            private Artist artist;
            @JsonProperty("release_date_original")
            private String releaseDateOriginal;
            @JsonProperty("tracks_count")
            private Integer tracksCount;
            @JsonProperty("upc")
            private String upc;
            @JsonProperty("hires")
            private Boolean hires;
            @JsonProperty("hires_streamable")
            private Boolean hiresStreamable;

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

            @NoArgsConstructor
            @Data
            public static class Artist {
                @JsonProperty("id")
                private String id;
                @JsonProperty("name")
                private String name;
            }
        }
    }
}
