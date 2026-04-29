package com.sqmusicplus.v3.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgDownloadResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/28 15:21
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgDownloadResult {

    @JsonProperty("code")
    private String code;
    @JsonProperty("info")
    private String info;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("version")
        private String version;
        @JsonProperty("url")
        private String url;
        @JsonProperty("audioFormatType")
        private String audioFormatType;
        @JsonProperty("lrcUrl")
        private String lrcUrl;
        @JsonProperty("mrcUrl")
        private String mrcUrl;
        @JsonProperty("trcUrl")
        private String trcUrl;
        @JsonProperty("auditionsLength")
        private Integer auditionsLength;
        @JsonProperty("auditionsStartTime")
        private Integer auditionsStartTime;
        @JsonProperty("song")
        private SongDTO song;
        @JsonProperty("auditionsTooltips")
        private String auditionsTooltips;
        @JsonProperty("dialogInfo")
        private DialogInfoDTO dialogInfo;
        @JsonProperty("freeListenType")
        private String freeListenType;
        @JsonProperty("haveVisualMv")
        private Boolean haveVisualMv;
        @JsonProperty("processBarActionUrl")
        private String processBarActionUrl;

        @NoArgsConstructor
        @Data
        public static class SongDTO {
            @JsonProperty("resourceType")
            private String resourceType;
            @JsonProperty("contentId")
            private String contentId;
            @JsonProperty("songId")
            private String songId;
            @JsonProperty("songName")
            private String songName;
            @JsonProperty("mvCopyrightType")
            private Integer mvCopyrightType;
            @JsonProperty("ringToneId")
            private String ringToneId;
            @JsonProperty("ringCopyrightId")
            private String ringCopyrightId;
            @JsonProperty("haveShockRing")
            private Integer haveShockRing;
            @JsonProperty("showTags")
            private List<String> showTags;
            @JsonProperty("songPinyin")
            private String songPinyin;
            @JsonProperty("duration")
            private Integer duration;
            @JsonProperty("copyrightId")
            private String copyrightId;
            @JsonProperty("copyrightType")
            private Integer copyrightType;
            @JsonProperty("restrictType")
            private Integer restrictType;
            @JsonProperty("albumId")
            private String albumId;
            @JsonProperty("album")
            private String album;
            @JsonProperty("albumPinyin")
            private String albumPinyin;
            @JsonProperty("img1")
            private String img1;
            @JsonProperty("img2")
            private String img2;
            @JsonProperty("img3")
            private String img3;
            @JsonProperty("downloadTags")
            private List<String> downloadTags;
            @JsonProperty("singerList")
            private List<SingerListDTO> singerList;
            @JsonProperty("ext")
            private ExtDTO ext;
            @JsonProperty("foreverListenFlag")
            private String foreverListenFlag;
            @JsonProperty("foreverListen")
            private Boolean foreverListen;
            @JsonProperty("shockRingId")
            private String shockRingId;
            @JsonProperty("hasAssociatedRing")
            private Boolean hasAssociatedRing;
            @JsonProperty("chorusStartTime")
            private String chorusStartTime;

            @NoArgsConstructor
            @Data
            public static class ExtDTO {
                @JsonProperty("disc")
                private String disc;
            }

            @NoArgsConstructor
            @Data
            public static class SingerListDTO {
                @JsonProperty("id")
                private String id;
                @JsonProperty("name")
                private String name;
                @JsonProperty("img")
                private String img;
                @JsonProperty("nameSpelling")
                private String nameSpelling;
            }
        }

        @NoArgsConstructor
        @Data
        public static class DialogInfoDTO {
            @JsonProperty("showType")
            private Integer showType;
            @JsonProperty("text")
            private String text;
            @JsonProperty("textStyle")
            private List<?> textStyle;
            @JsonProperty("buttonList")
            private List<ButtonListDTO> buttonList;
            @JsonProperty("payCompleteText")
            private String payCompleteText;

            @NoArgsConstructor
            @Data
            public static class ButtonListDTO {
                @JsonProperty("buttonText")
                private String buttonText;
                @JsonProperty("actionUrl")
                private String actionUrl;
                @JsonProperty("type")
                private Integer type;
            }
        }
    }
}
