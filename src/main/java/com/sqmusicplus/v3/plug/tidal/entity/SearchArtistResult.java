package com.sqmusicplus.v3.plug.tidal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchArtistResult
 * @Description Tidal搜索艺术家结果
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@NoArgsConstructor
@Data
public class SearchArtistResult {

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
        private Integer id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("artistTypes")
        private List<String> artistTypes;
        @JsonProperty("url")
        private String url;
        @JsonProperty("picture")
        private String picture;
        @JsonProperty("selectedAlbumCoverFallback")
        private String selectedAlbumCoverFallback;
        @JsonProperty("popularity")
        private Integer popularity;
        @JsonProperty("artistRoles")
        private List<ArtistRolesDTO> artistRoles;
        @JsonProperty("mixes")
        private MixesDTO mixes;
        @JsonProperty("handle")
        private Object handle;
        @JsonProperty("userId")
        private Object userId;
        @JsonProperty("spotlighted")
        private Boolean spotlighted;

        @NoArgsConstructor
        @Data
        public static class MixesDTO {
        }

        @NoArgsConstructor
        @Data
        public static class ArtistRolesDTO {
            @JsonProperty("categoryId")
            private Integer categoryId;
            @JsonProperty("category")
            private String category;
        }
    }
}
