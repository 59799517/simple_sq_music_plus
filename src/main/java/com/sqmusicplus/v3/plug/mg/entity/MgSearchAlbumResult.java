package com.sqmusicplus.v3.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgSearchAlbumResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 15:36
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgSearchAlbumResult {

    @JsonProperty("code")
    private String code;
    @JsonProperty("resultNum")
    private Integer resultNum;
    @JsonProperty("dynamicEffect")
    private Integer dynamicEffect;
    @JsonProperty("mod")
    private Integer mod;
    @JsonProperty("end")
    private Boolean end;
    @JsonProperty("albumResultData")
    private AlbumResultDataDTO albumResultData;
    @JsonProperty("concertResultData")
    private ConcertResultDataDTO concertResultData;
    @JsonProperty("userSortTypes")
    private List<UserSortTypesDTO> userSortTypes;

    @NoArgsConstructor
    @Data
    public static class AlbumResultDataDTO {
        @JsonProperty("totalCount")
        private String totalCount;
        @JsonProperty("correct")
        private List<List<String>> correct;
        @JsonProperty("result")
        private List<ResultDTO> result;

        @NoArgsConstructor
        @Data
        public static class ResultDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("resourceType")
            private String resourceType;
            @JsonProperty("name")
            private String name;
            @JsonProperty("type")
            private String type;
            @JsonProperty("singer")
            private String singer;
            @JsonProperty("highlightStr")
            private List<String> highlightStr;
            @JsonProperty("publishDate")
            private String publishDate;
            @JsonProperty("desc")
            private String desc;
            @JsonProperty("imgItems")
            private List<ImgItemsDTO> imgItems;
            @JsonProperty("isPay")
            private Integer isPay;

            @NoArgsConstructor
            @Data
            public static class ImgItemsDTO {
                @JsonProperty("img")
                private String img;
                @JsonProperty("imgSizeType")
                private String imgSizeType;
                @JsonProperty("fileId")
                private String fileId;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class ConcertResultDataDTO {
        @JsonProperty("result")
        private List<?> result;
        @JsonProperty("totalCount")
        private String totalCount;
    }

    @NoArgsConstructor
    @Data
    public static class UserSortTypesDTO {
        @JsonProperty("name")
        private String name;
        @JsonProperty("value")
        private String value;
    }
}
