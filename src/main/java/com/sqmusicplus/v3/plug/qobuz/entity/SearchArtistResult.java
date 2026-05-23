package com.sqmusicplus.v3.plug.qobuz.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchArtistResult
 * @Description Qobuz搜索艺术家结果
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by Lingma
 */
@NoArgsConstructor
@Data
public class SearchArtistResult {
    @JsonProperty("artists")
    private Artists artists;

    @NoArgsConstructor
    @Data
    public static class Artists {
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
            @JsonProperty("name")
            private String name;
            @JsonProperty("image")
            private String image;
            @JsonProperty("albums_count")
            private Integer albumsCount;
        }
    }
}
