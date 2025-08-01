package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SongInfoResult
 * @Description
 * @Version 1.0.0
 * @Date 2025/2/11 11:33
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SongInfoResult {


    @JsonProperty("status")
    private Long status;
    @JsonProperty("error_code")
    private Long errorCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("appid_group")
    private Long appidGroup;
    @JsonProperty("should_cache")
    private Long shouldCache;
    @JsonProperty("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("type")
        private String type;
        @JsonProperty("id")
        private Long id;
        @JsonProperty("album_id")
        private String albumId;
        @JsonProperty("recommend_album_id")
        private String recommendAlbumId;
        @JsonProperty("album_audio_id")
        private Long albumAudioId;
        @JsonProperty("hash")
        private String hash;
        @JsonProperty("name")
        private String name;
        @JsonProperty("singername")
        private String singername;
        @JsonProperty("albumname")
        private String albumname;
        @JsonProperty("level")
        private Long level;
        @JsonProperty("quality")
        private String quality;
        @JsonProperty("expire")
        private Long expire;
        @JsonProperty("publish")
        private Long publish;
        @JsonProperty("is_publish")
        private Long isPublish;
        @JsonProperty("old_hide")
        private Long oldHide;
        @JsonProperty("privilege")
        private Long privilege;
        @JsonProperty("status")
        private Long status;
        @JsonProperty("fail_process")
        private Long failProcess;
        @JsonProperty("pay_type")
        private Long payType;
        @JsonProperty("price")
        private Long price;
        @JsonProperty("pkg_price")
        private Long pkgPrice;
        @JsonProperty("topic_url")
        private String topicUrl;
        @JsonProperty("topic_remark")
        private String topicRemark;
        @JsonProperty("info")
        private InfoDTO info;
        @JsonProperty("discount")
        private List<?> discount;
        @JsonProperty("start_time")
        private String startTime;
        @JsonProperty("end_time")
        private String endTime;
        @JsonProperty("cd_url")
        private String cdUrl;
        @JsonProperty("cid")
        private Long cid;
        @JsonProperty("old_cpy")
        private Long oldCpy;
        @JsonProperty("rebuy_pay_type")
        private Long rebuyPayType;
        @JsonProperty("is_search_top")
        private Long isSearchTop;
        @JsonProperty("pay_block_text")
        private String payBlockText;
        @JsonProperty("is_separate")
        private Long isSeparate;
        @JsonProperty("buy_count")
        private Long buyCount;
        @JsonProperty("buy_count_vip")
        private Long buyCountVip;
        @JsonProperty("buy_count_kubi")
        private Long buyCountKubi;
        @JsonProperty("buy_count_audios")
        private Long buyCountAudios;
        @JsonProperty("trans_param")
        private TransParamDTO transParam;
        @JsonProperty("_msg")
        private String msg;
        @JsonProperty("_errno")
        private Long errno;
        @JsonProperty("relate_goods")
        private List<RelateGoodsDTO> relateGoods;

        @NoArgsConstructor
        @Data
        public static class InfoDTO {
            @JsonProperty("duration")
            private Long duration;
            @JsonProperty("filesize")
            private Long filesize;
            @JsonProperty("bitrate")
            private Long bitrate;
            @JsonProperty("extname")
            private String extname;
            @JsonProperty("flag")
            private Long flag;
            @JsonProperty("image")
            private String image;
            @JsonProperty("imgsize")
            private List<Long> imgsize;
            @JsonProperty("intro")
            private String intro;
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
        public static class RelateGoodsDTO {
            @JsonProperty("type")
            private String type;
            @JsonProperty("id")
            private Long id;
            @JsonProperty("album_id")
            private String albumId;
            @JsonProperty("recommend_album_id")
            private String recommendAlbumId;
            @JsonProperty("album_audio_id")
            private Long albumAudioId;
            @JsonProperty("hash")
            private String hash;
            @JsonProperty("name")
            private String name;
            @JsonProperty("singername")
            private String singername;
            @JsonProperty("albumname")
            private String albumname;
            @JsonProperty("level")
            private Long level;
            @JsonProperty("quality")
            private String quality;
            @JsonProperty("expire")
            private Long expire;
            @JsonProperty("publish")
            private Long publish;
            @JsonProperty("is_publish")
            private Long isPublish;
            @JsonProperty("old_hide")
            private Long oldHide;
            @JsonProperty("privilege")
            private Long privilege;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("fail_process")
            private Long failProcess;
            @JsonProperty("pay_type")
            private Long payType;
            @JsonProperty("price")
            private Long price;
            @JsonProperty("pkg_price")
            private Long pkgPrice;
            @JsonProperty("topic_url")
            private String topicUrl;
            @JsonProperty("topic_remark")
            private String topicRemark;
            @JsonProperty("info")
            private InfoDTO info;
            @JsonProperty("discount")
            private List<?> discount;
            @JsonProperty("start_time")
            private String startTime;
            @JsonProperty("end_time")
            private String endTime;
            @JsonProperty("cd_url")
            private String cdUrl;
            @JsonProperty("cid")
            private Long cid;
            @JsonProperty("old_cpy")
            private Long oldCpy;
            @JsonProperty("rebuy_pay_type")
            private Long rebuyPayType;
            @JsonProperty("is_search_top")
            private Long isSearchTop;
            @JsonProperty("pay_block_text")
            private String payBlockText;
            @JsonProperty("is_separate")
            private Long isSeparate;
            @JsonProperty("buy_count")
            private Long buyCount;
            @JsonProperty("buy_count_vip")
            private Long buyCountVip;
            @JsonProperty("buy_count_kubi")
            private Long buyCountKubi;
            @JsonProperty("buy_count_audios")
            private Long buyCountAudios;
            @JsonProperty("trans_param")
            private TransParamDTO transParam;
            @JsonProperty("_msg")
            private String msg;
            @JsonProperty("_errno")
            private Long errno;
            @JsonProperty("relate_goods")
            private List<?> relateGoods;

            @NoArgsConstructor
            @Data
            public static class InfoDTO {
                @JsonProperty("duration")
                private Long duration;
                @JsonProperty("filesize")
                private Long filesize;
                @JsonProperty("bitrate")
                private Long bitrate;
                @JsonProperty("extname")
                private String extname;
                @JsonProperty("flag")
                private Long flag;
                @JsonProperty("image")
                private String image;
                @JsonProperty("imgsize")
                private List<Long> imgsize;
                @JsonProperty("intro")
                private String intro;
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
                }

                @NoArgsConstructor
                @Data
                public static class IpmapDTO {
                    @JsonProperty("attr0")
                    private Long attr0;
                }
            }
        }
    }
}
