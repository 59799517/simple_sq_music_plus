package com.sqmusicplus.v3.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgArtisInfoResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 16:37
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgArtisInfoResult {


    @JsonProperty("code")
    private String code;
    @JsonProperty("info")
    private String info;
    @JsonProperty("resource")
    private List<ResourceDTO> resource;

    @NoArgsConstructor
    @Data
    public static class ResourceDTO {
        @JsonProperty("resourceType")
        private String resourceType;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("singerId")
        private String singerId;
        @JsonProperty("singer")
        private String singer;
        @JsonProperty("type")
        private String type;
        @JsonProperty("imgs")
        private List<ImgsDTO> imgs;
        @JsonProperty("followNums")
        private Integer followNums;
        @JsonProperty("detail")
        private String detail;
        @JsonProperty("columnId")
        private String columnId;
        @JsonProperty("isFuns")
        private String isFuns;
        @JsonProperty("artistNamePinyin")
        private String artistNamePinyin;
        @JsonProperty("artistNameFirstLetter")
        private String artistNameFirstLetter;
        @JsonProperty("miguSingerPicItems")
        private List<?> miguSingerPicItems;
        @JsonProperty("artis5gImg")
        private String artis5gImg;
        @JsonProperty("latestContentType")
        private String latestContentType;
        @JsonProperty("latestReleaseTime")
        private String latestReleaseTime;
        @JsonProperty("latestContentId")
        private String latestContentId;

        @NoArgsConstructor
        @Data
        public static class ImgsDTO {
            @JsonProperty("imgSizeType")
            private String imgSizeType;
            @JsonProperty("img")
            private String img;
            @JsonProperty("imgOri")
            private String imgOri;
            @JsonProperty("fileId")
            private String fileId;
            @JsonProperty("webpImg")
            private String webpImg;
        }
    }
}
