package com.sqmusicplus.v3.plug.netease.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname PlaylistTrackAllResult
 * @Description 歌单歌曲闲情
 * @Version 1.0.0
 * @Date 2025/7/24 11:42
 * @Created by sq
 */

@NoArgsConstructor
@Data
public class PlaylistTrackAllResult {


    @JsonProperty("songs")
    private List<SongsDTO> songs;
    @JsonProperty("privileges")
    private List<PrivilegesDTO> privileges;
    @JsonProperty("code")
    private Long code;

    @NoArgsConstructor
    @Data
    public static class SongsDTO {
        @JsonProperty("name")
        private String name;
        @JsonProperty("mainTitle")
        private String mainTitle;
        @JsonProperty("additionalTitle")
        private String additionalTitle;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("pst")
        private Long pst;
        @JsonProperty("t")
        private Long t;
        @JsonProperty("ar")
        private List<ArDTO> ar;
        @JsonProperty("alia")
        private List<String> alia;
        @JsonProperty("pop")
        private Long pop;
        @JsonProperty("st")
        private Long st;
        @JsonProperty("rt")
        private String rt;
        @JsonProperty("fee")
        private Long fee;
        @JsonProperty("v")
        private Long v;
        @JsonProperty("crbt")
        private String crbt;
        @JsonProperty("cf")
        private String cf;
        @JsonProperty("al")
        private AlDTO al;
        @JsonProperty("dt")
        private Long dt;
        @JsonProperty("h")
        private HDTO h;
        @JsonProperty("m")
        private MDTO m;
        @JsonProperty("l")
        private LDTO l;
        @JsonProperty("sq")
        private SqDTO sq;
        @JsonProperty("hr")
        private SqDTO hr;
        @JsonProperty("a")
        private String a;
        @JsonProperty("cd")
        private String cd;
        @JsonProperty("no")
        private Long no;
        @JsonProperty("rtUrl")
        private String rtUrl;
        @JsonProperty("ftype")
        private Long ftype;
        @JsonProperty("rtUrls")
        private List<String> rtUrls;
        @JsonProperty("djId")
        private Long djId;
        @JsonProperty("copyright")
        private Long copyright;
        @JsonProperty("s_id")
        private Long sId;
        @JsonProperty("mark")
        private Long mark;
        @JsonProperty("originCoverType")
        private Long originCoverType;
        @JsonProperty("originSongSimpleData")
        private String originSongSimpleData;
        @JsonProperty("tagPicList")
        private String tagPicList;
        @JsonProperty("resourceState")
        private Boolean resourceState;
        @JsonProperty("version")
        private Long version;
        @JsonProperty("songJumpInfo")
        private String songJumpInfo;
        @JsonProperty("entertainmentTags")
        private String entertainmentTags;
        @JsonProperty("awardTags")
        private String awardTags;
        @JsonProperty("displayTags")
        private String displayTags;
        @JsonProperty("single")
        private Long single;
        @JsonProperty("noCopyrightRcmd")
        private String noCopyrightRcmd;
        @JsonProperty("mv")
        private Long mv;
        @JsonProperty("rtype")
        private Long rtype;
        @JsonProperty("rurl")
        private String rurl;
        @JsonProperty("mst")
        private Long mst;
        @JsonProperty("cp")
        private Long cp;
        @JsonProperty("publishTime")
        private Long publishTime;
        @JsonProperty("tns")
        private List<String> tns;

        @NoArgsConstructor
        @Data
        public static class AlDTO {
            @JsonProperty("id")
            private Long id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("picUrl")
            private String picUrl;
            @JsonProperty("tns")
            private List<String> tns;
            @JsonProperty("pic_str")
            private String picStr;
            @JsonProperty("pic")
            private Long pic;
        }

        @NoArgsConstructor
        @Data
        public static class HDTO {
            @JsonProperty("br")
            private Long br;
            @JsonProperty("fid")
            private Long fid;
            @JsonProperty("size")
            private Long size;
            @JsonProperty("vd")
            private Long vd;
            @JsonProperty("sr")
            private Long sr;
        }

        @NoArgsConstructor
        @Data
        public static class MDTO {
            @JsonProperty("br")
            private Long br;
            @JsonProperty("fid")
            private Long fid;
            @JsonProperty("size")
            private Long size;
            @JsonProperty("vd")
            private Long vd;
            @JsonProperty("sr")
            private Long sr;
        }

        @NoArgsConstructor
        @Data
        public static class LDTO {
            @JsonProperty("br")
            private Long br;
            @JsonProperty("fid")
            private Long fid;
            @JsonProperty("size")
            private Long size;
            @JsonProperty("vd")
            private Long vd;
            @JsonProperty("sr")
            private Long sr;
        }

        @NoArgsConstructor
        @Data
        public static class SqDTO {
            @JsonProperty("br")
            private Long br;
            @JsonProperty("fid")
            private Long fid;
            @JsonProperty("size")
            private Long size;
            @JsonProperty("vd")
            private Long vd;
            @JsonProperty("sr")
            private Long sr;
        }

        @NoArgsConstructor
        @Data
        public static class ArDTO {
            @JsonProperty("id")
            private Long id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("tns")
            private List<String> tns;
            @JsonProperty("alias")
            private List<String> alias;
        }
    }

    @NoArgsConstructor
    @Data
    public static class PrivilegesDTO {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("fee")
        private Long fee;
        @JsonProperty("payed")
        private Long payed;
        @JsonProperty("st")
        private Long st;
        @JsonProperty("pl")
        private Long pl;
        @JsonProperty("dl")
        private Long dl;
        @JsonProperty("sp")
        private Long sp;
        @JsonProperty("cp")
        private Long cp;
        @JsonProperty("subp")
        private Long subp;
        @JsonProperty("cs")
        private Boolean cs;
        @JsonProperty("maxbr")
        private Long maxbr;
        @JsonProperty("fl")
        private Long fl;
        @JsonProperty("toast")
        private Boolean toast;
        @JsonProperty("flag")
        private Long flag;
        @JsonProperty("preSell")
        private Boolean preSell;
        @JsonProperty("playMaxbr")
        private Long playMaxbr;
        @JsonProperty("downloadMaxbr")
        private Long downloadMaxbr;
        @JsonProperty("maxBrLevel")
        private String maxBrLevel;
        @JsonProperty("playMaxBrLevel")
        private String playMaxBrLevel;
        @JsonProperty("downloadMaxBrLevel")
        private String downloadMaxBrLevel;
        @JsonProperty("plLevel")
        private String plLevel;
        @JsonProperty("dlLevel")
        private String dlLevel;
        @JsonProperty("flLevel")
        private String flLevel;
        @JsonProperty("rscl")
        private String rscl;
        @JsonProperty("freeTrialPrivilege")
        private FreeTrialPrivilegeDTO freeTrialPrivilege;
        @JsonProperty("rightSource")
        private Long rightSource;
        @JsonProperty("chargeInfoList")
        private List<ChargeInfoListDTO> chargeInfoList;
        @JsonProperty("code")
        private Long code;
        @JsonProperty("message")
        private String message;
        @JsonProperty("plLevels")
        private String plLevels;
        @JsonProperty("dlLevels")
        private String dlLevels;
        @JsonProperty("ignoreCache")
        private String ignoreCache;

        @NoArgsConstructor
        @Data
        public static class FreeTrialPrivilegeDTO {
            @JsonProperty("resConsumable")
            private Boolean resConsumable;
            @JsonProperty("userConsumable")
            private Boolean userConsumable;
            @JsonProperty("listenType")
            private Long listenType;
            @JsonProperty("cannotListenReason")
            private Long cannotListenReason;
            @JsonProperty("playReason")
            private String playReason;
            @JsonProperty("freeLimitTagType")
            private String freeLimitTagType;
        }

        @NoArgsConstructor
        @Data
        public static class ChargeInfoListDTO {
            @JsonProperty("rate")
            private Long rate;
            @JsonProperty("chargeUrl")
            private String chargeUrl;
            @JsonProperty("chargeMessage")
            private String chargeMessage;
            @JsonProperty("chargeType")
            private Long chargeType;
        }
    }
}
