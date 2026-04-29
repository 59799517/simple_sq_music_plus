package com.sqmusicplus.v3.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgSearchArtistResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 10:12
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgSearchArtistResult {

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
    @JsonProperty("singerResultData")
    private SingerResultDataDTO singerResultData;
    @JsonProperty("concertResultData")
    private ConcertResultDataDTO concertResultData;

    @NoArgsConstructor
    @Data
    public static class SingerResultDataDTO {
        @JsonProperty("totalCount")
        private String totalCount;
        @JsonProperty("correct")
        private List<?> correct;
        @JsonProperty("result")
        private List<ResultDTO> result;

        @NoArgsConstructor
        @Data
        public static class ResultDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("highlightStr")
            private List<String> highlightStr;
            @JsonProperty("albumCount")
            private Integer albumCount;
            @JsonProperty("songCount")
            private Integer songCount;
            @JsonProperty("mvCount")
            private Integer mvCount;
            @JsonProperty("vrbtCount")
            private Integer vrbtCount;
            @JsonProperty("singerPicUrl")
            private List<SingerPicUrlDTO> singerPicUrl;
            @JsonProperty("relationDesc")
            private String relationDesc;

            @NoArgsConstructor
            @Data
            public static class SingerPicUrlDTO {
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
}
