package com.sqmusicplus.v3.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicAudioInfoResult
 * @Description Administrator
 * @Version 1.0.0
 * @Date 2025/8/15 09:17
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class MusicAudioInfoResult {


    @JsonProperty("Reason")
    private String reason;
    @JsonProperty("errorcode")
    private Long errorcode;
    @JsonProperty("errormsg")
    private String errormsg;
    @JsonProperty("result")
    private String result;
    @JsonProperty("songs")
    private List<SongsDTO> songs;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("user")
    private List<?> user;

    @NoArgsConstructor
    @Data
    public static class SongsDTO {
        @JsonProperty("MINFO")
        private String minfo;
        @JsonProperty("N_MINFO")
        private String nMinfo;
        @JsonProperty("ad_subtype")
        private String adSubtype;
        @JsonProperty("ad_type")
        private String adType;
        @JsonProperty("album")
        private String album;
        @JsonProperty("albumid")
        private Long albumid;
        @JsonProperty("allfree")
        private Long allfree;
        @JsonProperty("artist")
        private String artist;
        @JsonProperty("artistid")
        private Long artistid;
        @JsonProperty("audio")
        private List<AudioDTO> audio;
        @JsonProperty("barrage")
        private String barrage;
        @JsonProperty("cache_status")
        private String cacheStatus;
        @JsonProperty("canAudition")
        private Long canAudition;
        @JsonProperty("content_type")
        private Long contentType;
        @JsonProperty("copywriting")
        private String copywriting;
        @JsonProperty("copywriting_id")
        private Long copywritingId;
        @JsonProperty("copywriting_songid")
        private Long copywritingSongid;
        @JsonProperty("dc_rtimestamp")
        private String dcRtimestamp;
        @JsonProperty("duration")
        private Long duration;
        @JsonProperty("formats")
        private String formats;
        @JsonProperty("fpay")
        private Long fpay;
        @JsonProperty("hasecho")
        private Long hasecho;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("isRedSong")
        private Long isRedSong;
        @JsonProperty("is_point")
        private Long isPoint;
        @JsonProperty("isshow")
        private Long isshow;
        @JsonProperty("isshowtype")
        private Long isshowtype;
        @JsonProperty("isshowtype_car")
        private Long isshowtypeCar;
        @JsonProperty("isstar")
        private Long isstar;
        @JsonProperty("mkvnsig1")
        private Long mkvnsig1;
        @JsonProperty("mkvnsig2")
        private Long mkvnsig2;
        @JsonProperty("mkvrid")
        private Long mkvrid;
        @JsonProperty("mp3nsig1")
        private Long mp3nsig1;
        @JsonProperty("mp3nsig2")
        private Long mp3nsig2;
        @JsonProperty("mp3rid")
        private Long mp3rid;
        @JsonProperty("muti_ver")
        private Long mutiVer;
        @JsonProperty("mvflag")
        private Long mvflag;
        @JsonProperty("mvquality")
        private String mvquality;
        @JsonProperty("name")
        private String name;
        @JsonProperty("nsig1")
        private Long nsig1;
        @JsonProperty("nsig2")
        private Long nsig2;
        @JsonProperty("overseas_copyright")
        private String overseasCopyright;
        @JsonProperty("overseas_lib")
        private Long overseasLib;
        @JsonProperty("overseas_pay")
        private Long overseasPay;
        @JsonProperty("pay")
        private Long pay;
        @JsonProperty("payInfo")
        private PayInfoDTO payInfo;
        @JsonProperty("paytype")
        private Long paytype;
        @JsonProperty("pc_formats")
        private String pcFormats;
        @JsonProperty("phone_formats")
        private String phoneFormats;
        @JsonProperty("policy")
        private List<PolicyDTO> policy;
        @JsonProperty("spPrivilege")
        private String spPrivilege;
        @JsonProperty("subsStrategy")
        private String subsStrategy;
        @JsonProperty("subsText")
        private String subsText;
        @JsonProperty("terminalOnline")
        private String terminalOnline;
        @JsonProperty("timingonline")
        private Long timingonline;
        @JsonProperty("tme_musician_adtype")
        private String tmeMusicianAdtype;
        @JsonProperty("token")
        private TokenDTO token;
        @JsonProperty("tpay")
        private Long tpay;
        @JsonProperty("video")
        private List<VideoDTO> video;

        @NoArgsConstructor
        @Data
        public static class PayInfoDTO {
            @JsonProperty("cannotDownload")
            private Long cannotDownload;
            @JsonProperty("cannotOnlinePlay")
            private Long cannotOnlinePlay;
            @JsonProperty("download")
            private String download;
            @JsonProperty("feeType")
            private FeeTypeDTO feeType;
            @JsonProperty("limitfree")
            private String limitfree;
            @JsonProperty("listen_fragment")
            private String listenFragment;
            @JsonProperty("local_encrypt")
            private String localEncrypt;
            @JsonProperty("ndown")
            private String ndown;
            @JsonProperty("nplay")
            private String nplay;
            @JsonProperty("overseas_ndown")
            private String overseasNdown;
            @JsonProperty("overseas_nplay")
            private String overseasNplay;
            @JsonProperty("paytagindex")
            private PaytagindexDTO paytagindex;
            @JsonProperty("paytype")
            private Long paytype;
            @JsonProperty("play")
            private String play;
            @JsonProperty("refrain_end")
            private String refrainEnd;
            @JsonProperty("refrain_start")
            private String refrainStart;
            @JsonProperty("tips_intercept")
            private String tipsIntercept;

            @NoArgsConstructor
            @Data
            public static class FeeTypeDTO {
                @JsonProperty("album")
                private String album;
                @JsonProperty("bookvip")
                private String bookvip;
                @JsonProperty("song")
                private String song;
                @JsonProperty("vip")
                private String vip;
            }

            @NoArgsConstructor
            @Data
            public static class PaytagindexDTO {
                @JsonProperty("AR501")
                private Long ar501;
                @JsonProperty("DB")
                private Long db;
                @JsonProperty("F")
                private Long f;
                @JsonProperty("H")
                private Long h;
                @JsonProperty("HR")
                private Long hr;
                @JsonProperty("L")
                private Long l;
                @JsonProperty("S")
                private Long s;
                @JsonProperty("ZP")
                private Long zp;
                @JsonProperty("ZPGA201")
                private Long zpga201;
                @JsonProperty("ZPGA501")
                private Long zpga501;
                @JsonProperty("ZPLY")
                private Long zply;
            }
        }

        @NoArgsConstructor
        @Data
        public static class TokenDTO {
            @JsonProperty("AR501")
            private String ar501;
            @JsonProperty("BCMS")
            private String bcms;
            @JsonProperty("DB")
            private String db;
            @JsonProperty("F")
            private String f;
            @JsonProperty("H")
            private String h;
            @JsonProperty("HR")
            private String hr;
            @JsonProperty("L")
            private String l;
            @JsonProperty("S")
            private String s;
            @JsonProperty("ZP")
            private String zp;
            @JsonProperty("ZPGA201")
            private String zpga201;
            @JsonProperty("ZPGA501")
            private String zpga501;
            @JsonProperty("ZPLY")
            private String zply;
        }

        @NoArgsConstructor
        @Data
        public static class AudioDTO {
            @JsonProperty("PackType")
            private Long packType;
            @JsonProperty("albumid")
            private Long albumid;
            @JsonProperty("audioproductid")
            private Long audioproductid;
            @JsonProperty("avaliable")
            private Long avaliable;
            @JsonProperty("br")
            private Long br;
            @JsonProperty("fmt")
            private String fmt;
            @JsonProperty("p2p_audiosourceid")
            private String p2pAudiosourceid;
            @JsonProperty("pid")
            private String pid;
            @JsonProperty("policy")
            private String policy;
            @JsonProperty("price")
            private Long price;
            @JsonProperty("quality")
            private String quality;
            @JsonProperty("raw_policy")
            private String rawPolicy;
            @JsonProperty("row_fmt")
            private String rowFmt;
            @JsonProperty("st")
            private Long st;
            @JsonProperty("webavaliable")
            private Long webavaliable;
            @JsonProperty("opid")
            private String opid;
            @JsonProperty("oprice")
            private Long oprice;
        }

        @NoArgsConstructor
        @Data
        public static class PolicyDTO {
            @JsonProperty("audioDownBR")
            private Long audioDownBR;
            @JsonProperty("audioPlayBR")
            private Long audioPlayBR;
            @JsonProperty("id")
            private Long id;
            @JsonProperty("policy")
            private String policy;
            @JsonProperty("price")
            private Long price;
            @JsonProperty("rid")
            private Long rid;
            @JsonProperty("rtimestamp")
            private String rtimestamp;
            @JsonProperty("type")
            private Long type;
            @JsonProperty("videoDownBR")
            private Long videoDownBR;
            @JsonProperty("videoPlayBR")
            private Long videoPlayBR;
        }

        @NoArgsConstructor
        @Data
        public static class VideoDTO {
            @JsonProperty("albumid")
            private Long albumid;
            @JsonProperty("avaliable")
            private Long avaliable;
            @JsonProperty("br")
            private Long br;
            @JsonProperty("fmt")
            private String fmt;
            @JsonProperty("quality")
            private String quality;
            @JsonProperty("row_fmt")
            private String rowFmt;
            @JsonProperty("webavaliable")
            private Long webavaliable;
        }
    }
}
