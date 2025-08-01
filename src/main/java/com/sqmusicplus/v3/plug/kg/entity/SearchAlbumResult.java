package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchAlbumResult
 * @Description 搜索专辑结果
 * @Version 1.0.0
 * @Date 2025/2/6 17:06
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SearchAlbumResult {


    @JsonProperty("status")
    private Long status;
    @JsonProperty("error_code")
    private Long errorCode;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("pagesize")
        private Long pagesize;
        @JsonProperty("page")
        private Long page;
        @JsonProperty("from")
        private Long from;
        @JsonProperty("size")
        private Long size;
        @JsonProperty("total")
        private Long total;
        @JsonProperty("correctiontype")
        private Long correctiontype;
        @JsonProperty("correctionforce")
        private Long correctionforce;
        @JsonProperty("correctiontip")
        private String correctiontip;
        @JsonProperty("lists")
        private List<ListsDTO> lists;

        @NoArgsConstructor
        @Data
        public static class ListsDTO {
            @JsonProperty("albumid")
            private Long albumid;
            @JsonProperty("albumname")
            private String albumname;
            @JsonProperty("singer")
            private String singer;
            @JsonProperty("singerid")
            private String singerid;
            @JsonProperty("grade")
            private Long grade;
            @JsonProperty("grade_int")
            private String gradeInt;
            @JsonProperty("img")
            private String img;
            @JsonProperty("intro")
            private String intro;
            @JsonProperty("grade_float")
            private String gradeFloat;
            @JsonProperty("company")
            private String company;
            @JsonProperty("quality")
            private Long quality;
            @JsonProperty("title")
            private String title;
            @JsonProperty("collect_count")
            private Long collectCount;
            @JsonProperty("publish_time")
            private String publishTime;
            @JsonProperty("language")
            private String language;
            @JsonProperty("privilege")
            private Long privilege;
            @JsonProperty("oldhide")
            private Long oldhide;
            @JsonProperty("buyercount")
            private Long buyercount;
            @JsonProperty("songcount")
            private Long songcount;
            @JsonProperty("newquality")
            private Long newquality;
            @JsonProperty("cd_url")
            private String cdUrl;
            @JsonProperty("isfirst")
            private Long isfirst;
            @JsonProperty("category")
            private Long category;
            @JsonProperty("short_intro")
            private String shortIntro;
            @JsonProperty("ostremark")
            private String ostremark;
            @JsonProperty("auxiliary")
            private String auxiliary;
            @JsonProperty("play_times")
            private Long playTimes;
            @JsonProperty("program_inner")
            private Long programInner;
            @JsonProperty("alg_path")
            private String algPath;
            @JsonProperty("program_def_songs")
            private List<?> programDefSongs;
            @JsonProperty("tag_str")
            private String tagStr;
            @JsonProperty("album_aux")
            private String albumAux;
            @JsonProperty("play_count")
            private Long playCount;
            @JsonProperty("isouter")
            private Long isouter;
            @JsonProperty("outerdata")
            private OuterdataDTO outerdata;
            @JsonProperty("trans_param")
            private TransParamDTO transParam;
            @JsonProperty("singerids")
            private List<Long> singerids;
            @JsonProperty("singers")
            private List<SingersDTO> singers;

            @NoArgsConstructor
            @Data
            public static class OuterdataDTO {
            }

            @NoArgsConstructor
            @Data
            public static class TransParamDTO {
                @JsonProperty("special_tag")
                private String specialTag;
            }

            @NoArgsConstructor
            @Data
            public static class SingersDTO {
                @JsonProperty("name")
                private String name;
                @JsonProperty("id")
                private Long id;
            }
        }
    }
}
