package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname DownloadResult
 * @Description 下载链接返回值
 * @Version 1.0.0
 * @Date 2025/2/12 14:12
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class DownloadResult {


    @JsonProperty("data")
    private List<DataDTO> data;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("error_code")
    private Long errorCode;
    @JsonProperty("message")
    private String message;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("_errno")
        private Long errno;
        @JsonProperty("_msg")
        private String msg;
        @JsonProperty("album_audio_id")
        private Long albumAudioId;
        @JsonProperty("fail_process")
        private Long failProcess;
        @JsonProperty("hash")
        private String hash;
        @JsonProperty("info")
        private InfoDTO info;
        @JsonProperty("quality")
        private String quality;
        @JsonProperty("privilege")
        private Long privilege;
        @JsonProperty("publish")
        private Long publish;
        @JsonProperty("status")
        private Long status;
        @JsonProperty("relate_goods")
        private List<RelateGoodsDTO> relateGoods;
        @JsonProperty("type")
        private String type;
        @JsonProperty("trans_param")
        private TransParamDTO transParam;
        @JsonProperty("album_id")
        private String albumId;
        @JsonProperty("albumname")
        private String albumname;
        @JsonProperty("buy_count")
        private Long buyCount;
        @JsonProperty("buy_count_kubi")
        private Long buyCountKubi;
        @JsonProperty("buy_count_audios")
        private Long buyCountAudios;
        @JsonProperty("buy_count_vip")
        private Long buyCountVip;
        @JsonProperty("cid")
        private Long cid;
        @JsonProperty("discount")
        private List<?> discount;
        @JsonProperty("end_time")
        private String endTime;
        @JsonProperty("expire")
        private Long expire;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("is_search_top")
        private Long isSearchTop;
        @JsonProperty("level")
        private Long level;
        @JsonProperty("name")
        private String name;
        @JsonProperty("old_cpy")
        private Long oldCpy;
        @JsonProperty("old_hide")
        private Long oldHide;
        @JsonProperty("pay_block_text")
        private String payBlockText;
        @JsonProperty("is_separate")
        private Long isSeparate;
        @JsonProperty("pay_type")
        private Long payType;
        @JsonProperty("pkg_price")
        private Long pkgPrice;
        @JsonProperty("price")
        private Long price;
        @JsonProperty("cd_url")
        private String cdUrl;
        @JsonProperty("rebuy_pay_type")
        private Long rebuyPayType;
        @JsonProperty("recommend_album_id")
        private String recommendAlbumId;
        @JsonProperty("singername")
        private String singername;
        @JsonProperty("start_time")
        private String startTime;
        @JsonProperty("topic_url")
        private String topicUrl;
        @JsonProperty("topic_remark")
        private String topicRemark;
        @JsonProperty("is_publish")
        private Long isPublish;
        @JsonProperty("popup")
        private PopupDTO popup;

        @NoArgsConstructor
        @Data
        public static class InfoDTO {
            @JsonProperty("imgsize")
            private List<Long> imgsize;
            @JsonProperty("flag")
            private Long flag;
            @JsonProperty("image")
            private String image;
            @JsonProperty("intro")
            private String intro;
            @JsonProperty("filesize")
            private Long filesize;
            @JsonProperty("extname")
            private String extname;
            @JsonProperty("bitrate")
            private Long bitrate;
            @JsonProperty("duration")
            private Long duration;
            @JsonProperty("hasseed")
            private Long hasseed;
            @JsonProperty("en_hasseed")
            private Long enHasseed;
            @JsonProperty("tracker_url")
            private List<String> trackerUrl;
            @JsonProperty("volume")
            private String volume;
            @JsonProperty("volume_peak")
            private String volumePeak;
            @JsonProperty("volume_gain")
            private String volumeGain;
            @JsonProperty("tracker_status")
            private Long trackerStatus;
            @JsonProperty("tracker_type")
            private String trackerType;
            @JsonProperty("climax_info")
            private ClimaxInfoDTO climaxInfo;

            @NoArgsConstructor
            @Data
            public static class ClimaxInfoDTO {
                @JsonProperty("url")
                private List<String> url;
                @JsonProperty("hash_offset")
                private HashOffsetDTO hashOffset;

                @NoArgsConstructor
                @Data
                public static class HashOffsetDTO {
                    @JsonProperty("start_byte")
                    private Long startByte;
                    @JsonProperty("end_byte")
                    private Long endByte;
                    @JsonProperty("start_ms")
                    private Long startMs;
                    @JsonProperty("end_ms")
                    private Long endMs;
                    @JsonProperty("offset_hash")
                    private String offsetHash;
                    @JsonProperty("file_type")
                    private Long fileType;
                }
            }
        }

        @NoArgsConstructor
        @Data
        public static class TransParamDTO {
            @JsonProperty("hash_offset")
            private HashOffsetDTO hashOffset;
            @JsonProperty("musicpack_advance")
            private Long musicpackAdvance;
            @JsonProperty("pay_block_tpl")
            private Long payBlockTpl;
            @JsonProperty("display")
            private Long display;
            @JsonProperty("display_rate")
            private Long displayRate;
            @JsonProperty("appid_block")
            private String appidBlock;
            @JsonProperty("cpy_grade")
            private Long cpyGrade;
            @JsonProperty("cpy_level")
            private Long cpyLevel;
            @JsonProperty("cid")
            private Long cid;
            @JsonProperty("cpy_attr0")
            private Long cpyAttr0;
            @JsonProperty("classmap")
            private ClassmapDTO classmap;
            @JsonProperty("hash_multitrack")
            private String hashMultitrack;
            @JsonProperty("qualitymap")
            private QualitymapDTO qualitymap;
            @JsonProperty("language")
            private String language;
            @JsonProperty("ipmap")
            private IpmapDTO ipmap;
            @JsonProperty("ogg_128_hash")
            private String ogg128Hash;
            @JsonProperty("ogg_128_filesize")
            private Long ogg128Filesize;
            @JsonProperty("ogg_320_hash")
            private String ogg320Hash;
            @JsonProperty("ogg_320_filesize")
            private Long ogg320Filesize;
            @JsonProperty("union_cover")
            private String unionCover;

            @NoArgsConstructor
            @Data
            public static class HashOffsetDTO {
                @JsonProperty("start_byte")
                private Long startByte;
                @JsonProperty("end_byte")
                private Long endByte;
                @JsonProperty("start_ms")
                private Long startMs;
                @JsonProperty("end_ms")
                private Long endMs;
                @JsonProperty("offset_hash")
                private String offsetHash;
                @JsonProperty("file_type")
                private Long fileType;
                @JsonProperty("clip_hash")
                private String clipHash;
            }

            @NoArgsConstructor
            @Data
            public static class ClassmapDTO {
                @JsonProperty("attr0")
                private Long attr0;
            }

            @NoArgsConstructor
            @Data
            public static class QualitymapDTO {
                @JsonProperty("attr0")
                private Long attr0;
                @JsonProperty("attr1")
                private Long attr1;
                @JsonProperty("bits")
                private String bits;
            }

            @NoArgsConstructor
            @Data
            public static class IpmapDTO {
                @JsonProperty("attr0")
                private Long attr0;
            }
        }

        @NoArgsConstructor
        @Data
        public static class PopupDTO {
            @JsonProperty("title")
            private String title;
            @JsonProperty("content")
            private String content;
            @JsonProperty("btn_name")
            private String btnName;
            @JsonProperty("btn_url_ios")
            private String btnUrlIos;
            @JsonProperty("btn_url_android")
            private String btnUrlAndroid;
            @JsonProperty("redirect_url_h5")
            private String redirectUrlH5;
        }

        @NoArgsConstructor
        @Data
        public static class RelateGoodsDTO {
            @JsonProperty("_errno")
            private Long errno;
            @JsonProperty("_msg")
            private String msg;
            @JsonProperty("album_audio_id")
            private Long albumAudioId;
            @JsonProperty("fail_process")
            private Long failProcess;
            @JsonProperty("hash")
            private String hash;
            @JsonProperty("info")
            private InfoDTO info;
            @JsonProperty("quality")
            private String quality;
            @JsonProperty("privilege")
            private Long privilege;
            @JsonProperty("publish")
            private Long publish;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("relate_goods")
            private List<?> relateGoods;
            @JsonProperty("type")
            private String type;
            @JsonProperty("trans_param")
            private TransParamDTO transParam;
            @JsonProperty("album_id")
            private String albumId;
            @JsonProperty("albumname")
            private String albumname;
            @JsonProperty("buy_count")
            private Long buyCount;
            @JsonProperty("buy_count_kubi")
            private Long buyCountKubi;
            @JsonProperty("buy_count_audios")
            private Long buyCountAudios;
            @JsonProperty("buy_count_vip")
            private Long buyCountVip;
            @JsonProperty("cid")
            private Long cid;
            @JsonProperty("discount")
            private List<?> discount;
            @JsonProperty("end_time")
            private String endTime;
            @JsonProperty("expire")
            private Long expire;
            @JsonProperty("id")
            private Long id;
            @JsonProperty("is_search_top")
            private Long isSearchTop;
            @JsonProperty("level")
            private Long level;
            @JsonProperty("name")
            private String name;
            @JsonProperty("old_cpy")
            private Long oldCpy;
            @JsonProperty("old_hide")
            private Long oldHide;
            @JsonProperty("pay_block_text")
            private String payBlockText;
            @JsonProperty("is_separate")
            private Long isSeparate;
            @JsonProperty("pay_type")
            private Long payType;
            @JsonProperty("pkg_price")
            private Long pkgPrice;
            @JsonProperty("price")
            private Long price;
            @JsonProperty("cd_url")
            private String cdUrl;
            @JsonProperty("rebuy_pay_type")
            private Long rebuyPayType;
            @JsonProperty("recommend_album_id")
            private String recommendAlbumId;
            @JsonProperty("singername")
            private String singername;
            @JsonProperty("start_time")
            private String startTime;
            @JsonProperty("topic_url")
            private String topicUrl;
            @JsonProperty("topic_remark")
            private String topicRemark;
            @JsonProperty("is_publish")
            private Long isPublish;
            @JsonProperty("popup")
            private PopupDTO popup;

            @NoArgsConstructor
            @Data
            public static class InfoDTO {
                @JsonProperty("imgsize")
                private List<Long> imgsize;
                @JsonProperty("flag")
                private Long flag;
                @JsonProperty("image")
                private String image;
                @JsonProperty("intro")
                private String intro;
                @JsonProperty("filesize")
                private Long filesize;
                @JsonProperty("extname")
                private String extname;
                @JsonProperty("bitrate")
                private Long bitrate;
                @JsonProperty("duration")
                private Long duration;
                @JsonProperty("hasseed")
                private Long hasseed;
                @JsonProperty("en_hasseed")
                private Long enHasseed;
                @JsonProperty("tracker_url")
                private List<String> trackerUrl;
                @JsonProperty("volume")
                private String volume;
                @JsonProperty("volume_peak")
                private String volumePeak;
                @JsonProperty("volume_gain")
                private String volumeGain;
                @JsonProperty("tracker_status")
                private Long trackerStatus;
                @JsonProperty("tracker_type")
                private String trackerType;
                @JsonProperty("climax_info")
                private ClimaxInfoDTO climaxInfo;

                @NoArgsConstructor
                @Data
                public static class ClimaxInfoDTO {
                    @JsonProperty("url")
                    private List<String> url;
                    @JsonProperty("hash_offset")
                    private HashOffsetDTO hashOffset;

                    @NoArgsConstructor
                    @Data
                    public static class HashOffsetDTO {
                        @JsonProperty("start_byte")
                        private Long startByte;
                        @JsonProperty("end_byte")
                        private Long endByte;
                        @JsonProperty("start_ms")
                        private Long startMs;
                        @JsonProperty("end_ms")
                        private Long endMs;
                        @JsonProperty("offset_hash")
                        private String offsetHash;
                        @JsonProperty("file_type")
                        private Long fileType;
                    }
                }
            }

            @NoArgsConstructor
            @Data
            public static class TransParamDTO {
                @JsonProperty("hash_offset")
                private HashOffsetDTO hashOffset;
                @JsonProperty("musicpack_advance")
                private Long musicpackAdvance;
                @JsonProperty("pay_block_tpl")
                private Long payBlockTpl;
                @JsonProperty("display")
                private Long display;
                @JsonProperty("display_rate")
                private Long displayRate;
                @JsonProperty("appid_block")
                private String appidBlock;
                @JsonProperty("cpy_grade")
                private Long cpyGrade;
                @JsonProperty("cpy_level")
                private Long cpyLevel;
                @JsonProperty("cid")
                private Long cid;
                @JsonProperty("cpy_attr0")
                private Long cpyAttr0;
                @JsonProperty("classmap")
                private ClassmapDTO classmap;
                @JsonProperty("hash_multitrack")
                private String hashMultitrack;
                @JsonProperty("qualitymap")
                private QualitymapDTO qualitymap;
                @JsonProperty("language")
                private String language;
                @JsonProperty("ipmap")
                private IpmapDTO ipmap;
                @JsonProperty("ogg_128_hash")
                private String ogg128Hash;
                @JsonProperty("ogg_128_filesize")
                private Long ogg128Filesize;
                @JsonProperty("ogg_320_hash")
                private String ogg320Hash;
                @JsonProperty("ogg_320_filesize")
                private Long ogg320Filesize;
                @JsonProperty("union_cover")
                private String unionCover;

                @NoArgsConstructor
                @Data
                public static class HashOffsetDTO {
                    @JsonProperty("start_byte")
                    private Long startByte;
                    @JsonProperty("end_byte")
                    private Long endByte;
                    @JsonProperty("start_ms")
                    private Long startMs;
                    @JsonProperty("end_ms")
                    private Long endMs;
                    @JsonProperty("offset_hash")
                    private String offsetHash;
                    @JsonProperty("file_type")
                    private Long fileType;
                    @JsonProperty("clip_hash")
                    private String clipHash;
                }

                @NoArgsConstructor
                @Data
                public static class ClassmapDTO {
                    @JsonProperty("attr0")
                    private Long attr0;
                }

                @NoArgsConstructor
                @Data
                public static class QualitymapDTO {
                    @JsonProperty("attr0")
                    private Long attr0;
                    @JsonProperty("attr1")
                    private Long attr1;
                    @JsonProperty("bits")
                    private String bits;
                }

                @NoArgsConstructor
                @Data
                public static class IpmapDTO {
                    @JsonProperty("attr0")
                    private Long attr0;
                }
            }

            @NoArgsConstructor
            @Data
            public static class PopupDTO {
                @JsonProperty("title")
                private String title;
                @JsonProperty("content")
                private String content;
                @JsonProperty("btn_name")
                private String btnName;
                @JsonProperty("btn_url_ios")
                private String btnUrlIos;
                @JsonProperty("btn_url_android")
                private String btnUrlAndroid;
                @JsonProperty("redirect_url_h5")
                private String redirectUrlH5;
            }
        }
    }
}
