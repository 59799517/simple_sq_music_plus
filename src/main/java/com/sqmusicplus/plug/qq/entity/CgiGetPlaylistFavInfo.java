package com.sqmusicplus.plug.qq.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Classname CgiGetPlaylistFavInfo
 * @Description CgiGetPlaylistFavInfo
 * @Version 1.0.0
 * @Date 2025/5/26 12:00
 * @Created by SQ
 */
@Data
public class CgiGetPlaylistFavInfo {


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
//        @JsonProperty("v_failTids")
//        private List<?> vFailtids;
//        @JsonProperty("v_delTids")
//        private List<?> vDeltids;
        @JsonProperty("total")
        private Long total;
        @JsonProperty("hide")
        private Boolean hide;

        @Data
        public static class VListDTO {
            @JsonProperty("tid")
            private Long tid;
            @JsonProperty("dirId")
            private Long dirId;
            @JsonProperty("name")
            private String name;
            @JsonProperty("songnum")
            private Long songnum;
            @JsonProperty("logo")
            private String logo;
            @JsonProperty("dirShow")
            private Long dirShow;
            @JsonProperty("uin")
            private String uin;
            @JsonProperty("nickname")
            private String nickname;
            @JsonProperty("createtime")
            private Long createtime;
            @JsonProperty("updateTime")
            private Long updateTime;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("edgeMark")
            private String edgeMark;
            @JsonProperty("sortWeight")
            private Long sortWeight;
            @JsonProperty("opType")
            private Long opType;
            @JsonProperty("orderTime")
            private Long orderTime;
            @JsonProperty("albumPicUrl")
            private String albumPicUrl;
            @JsonProperty("readtime")
            private Long readtime;
            @JsonProperty("layerUrl")
            private String layerUrl;
            @JsonProperty("ext1")
            private String ext1;
            @JsonProperty("ext2")
            private String ext2;
            @JsonProperty("dirType")
            private Long dirType;

        }
    }
}
