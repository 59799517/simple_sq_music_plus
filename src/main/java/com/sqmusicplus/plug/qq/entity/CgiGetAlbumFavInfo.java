package com.sqmusicplus.plug.qq.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Classname CgiGetAlbumFavInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/26 15:18
 * @Created by SQ
 */
@Data
public class CgiGetAlbumFavInfo {

    @JsonProperty("code")
    private Long code;
    @JsonProperty("data")
    private DataDTO data;

    @Data
    public static class DataDTO {
        @JsonProperty("number")
        private Long number;
        @JsonProperty("hasmore")
        private Long hasmore;
        @JsonProperty("v_list")
        private List<VListDTO> vList;
        //        @JsonProperty("v_failAlbumId")
//        private List<?> vFailalbumid;
        @JsonProperty("total")
        private Long total;
        @JsonProperty("hide")
        private Boolean hide;

        @Data
        public static class VListDTO {
            @JsonProperty("id")
            private Long id;
            @JsonProperty("mid")
            private String mid;
            @JsonProperty("name")
            private String name;
            @JsonProperty("v_singer")
            private List<VSingerDTO> vSinger;
            @JsonProperty("logo")
            private String logo;
            @JsonProperty("songnum")
            private Long songnum;
            @JsonProperty("pubtime")
            private Long pubtime;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("ordertime")
            private Long ordertime;
            @JsonProperty("sortWeight")
            private Long sortWeight;
            @JsonProperty("loc")
            private Long loc;

            @Data
            public static class VSingerDTO {
                @JsonProperty("id")
                private Long id;
                @JsonProperty("mid")
                private String mid;
                @JsonProperty("name")
                private String name;

            }
        }
    }
}
