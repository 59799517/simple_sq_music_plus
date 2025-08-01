package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchResult
 * @Description 搜索返回值
 * @Version 1.0.0
 * @Date 2025/2/6 10:32
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SearchMusicResult {

    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("error_code")
    private Long errorCode;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("correctiontip")
        private String correctiontip;
        @JsonProperty("pagesize")
        private Long pagesize;
        @JsonProperty("page")
        private Long page;
        @JsonProperty("correctiontype")
        private Long correctiontype;
        @JsonProperty("correctionrelate")
        private String correctionrelate;
        @JsonProperty("total")
        private Long total;
        @JsonProperty("lists")
        private List<ListsDTO> lists;
        @JsonProperty("size")
        private Long size;
        @JsonProperty("allowerr")
        private Long allowerr;
        @JsonProperty("AlgPath")
        private String algPath;
        @JsonProperty("sec_aggre_v2")
        private List<?> secAggreV2;
        @JsonProperty("correctionforce")
        private Long correctionforce;
        @JsonProperty("istag")
        private Long istag;
        @JsonProperty("from")
        private Long from;
        @JsonProperty("istagresult")
        private Long istagresult;
        @JsonProperty("isshareresult")
        private Long isshareresult;

        @NoArgsConstructor
        @Data
        public static class ListsDTO {
            @JsonProperty("PublishTime")
            private String publishTime;
            @JsonProperty("Audioid")
            private Long audioid;
            @JsonProperty("OldCpy")
            private Long oldCpy;
            @JsonProperty("PublishAge")
            private Long publishAge;
            @JsonProperty("bitflag")
            private Long bitflag;
            @JsonProperty("PayType")
            private Long payType;
            @JsonProperty("TagContent")
            private String tagContent;
            @JsonProperty("Accompany")
            private Long accompany;
            @JsonProperty("SingerName")
            private String singerName;
            @JsonProperty("ShowingFlag")
            private Long showingFlag;
            @JsonProperty("Source")
            private String source;
            @JsonProperty("AlbumAux")
            private String albumAux;
            @JsonProperty("Image")
            private String image;
            @JsonProperty("SQ")
            private SQDTO sq;
            @JsonProperty("HQ")
            private HQDTO hq;
            @JsonProperty("M4aSize")
            private Long m4aSize;
            @JsonProperty("HeatLevel")
            private Long heatLevel;
            @JsonProperty("trans_param")
            private TransParamDTO transParam;
            @JsonProperty("UploaderContent")
            private String uploaderContent;
            @JsonProperty("FileSize")
            private Long fileSize;
            @JsonProperty("IsOriginal")
            private Long isOriginal;
            @JsonProperty("FileHash")
            private String fileHash;
            @JsonProperty("FoldType")
            private Long foldType;
            @JsonProperty("Grp")
            private List<GrpDTO> grp;
            @JsonProperty("Type")
            private String type;
            @JsonProperty("Bitrate")
            private Long bitrate;
            @JsonProperty("isPrepublish")
            private Long isPrepublish;
            @JsonProperty("ExtName")
            private String extName;
            @JsonProperty("mvdata")
            private List<MvdataDTO> mvdata;
            @JsonProperty("AlbumPrivilege")
            private Long albumPrivilege;
            @JsonProperty("AlbumID")
            private String albumID;
            @JsonProperty("AlbumName")
            private String albumName;
            @JsonProperty("OtherName")
            private String otherName;
            @JsonProperty("SourceID")
            private Long sourceID;
            @JsonProperty("MixSongID")
            private String mixSongID;
            @JsonProperty("Singers")
            private List<SingersDTO> singers;
            @JsonProperty("Suffix")
            private String suffix;
            @JsonProperty("MatchFlag")
            private Long matchFlag;
            @JsonProperty("Scid")
            private Long scid;
            @JsonProperty("OriSongName")
            private String oriSongName;
            @JsonProperty("FailProcess")
            private Long failProcess;
            @JsonProperty("RankId")
            private Long rankId;
            @JsonProperty("PublishDate")
            private String publishDate;
            @JsonProperty("Auxiliary")
            private String auxiliary;
            @JsonProperty("TagDetails")
            private List<TagDetailsDTO> tagDetails;
            @JsonProperty("PrepublishInfo")
            private PrepublishInfoDTO prepublishInfo;
            @JsonProperty("OwnerCount")
            private Long ownerCount;
            @JsonProperty("Uploader")
            private String uploader;
            @JsonProperty("Duration")
            private Long duration;
            @JsonProperty("TopID")
            private Long topID;
            @JsonProperty("FileName")
            private String fileName;
            @JsonProperty("recommend_type")
            private Long recommendType;
            @JsonProperty("Res")
            private ResDTO res;

            @NoArgsConstructor
            @Data
            public static class SQDTO {
                @JsonProperty("FileSize")
                private Long fileSize;
                @JsonProperty("Hash")
                private String hash;
                @JsonProperty("Privilege")
                private Long privilege;
            }

            @NoArgsConstructor
            @Data
            public static class HQDTO {
                @JsonProperty("FileSize")
                private Long fileSize;
                @JsonProperty("Hash")
                private String hash;
                @JsonProperty("Privilege")
                private Long privilege;
            }

            @NoArgsConstructor
            @Data
            public static class TransParamDTO {
                @JsonProperty("identity_block")
                private Long identityBlock;
                @JsonProperty("ogg_128_hash")
                private String ogg128Hash;
                @JsonProperty("classmap")
                private ClassmapDTO classmap;
                @JsonProperty("language")
                private String language;
                @JsonProperty("cpy_attr0")
                private Long cpyAttr0;
                @JsonProperty("musicpack_advance")
                private Long musicpackAdvance;
                @JsonProperty("display")
                private Long display;
                @JsonProperty("display_rate")
                private Long displayRate;
                @JsonProperty("union_cover")
                private String unionCover;
                @JsonProperty("qualitymap")
                private QualitymapDTO qualitymap;
                @JsonProperty("ogg_320_filesize")
                private Long ogg320Filesize;
                @JsonProperty("ogg_320_hash")
                private String ogg320Hash;
                @JsonProperty("cid")
                private Long cid;
                @JsonProperty("ogg_128_filesize")
                private Long ogg128Filesize;
                @JsonProperty("cpy_grade")
                private Long cpyGrade;
                @JsonProperty("ipmap")
                private IpmapDTO ipmap;
                @JsonProperty("appid_block")
                private String appidBlock;
                @JsonProperty("hash_multitrack")
                private String hashMultitrack;
                @JsonProperty("pay_block_tpl")
                private Long payBlockTpl;
                @JsonProperty("cpy_level")
                private Long cpyLevel;

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
            public static class PrepublishInfoDTO {
                @JsonProperty("ReserveCount")
                private Long reserveCount;
                @JsonProperty("DisplayTime")
                private String displayTime;
                @JsonProperty("Id")
                private Long id;
                @JsonProperty("PublishTime")
                private String publishTime;
            }

            @NoArgsConstructor
            @Data
            public static class ResDTO {
                @JsonProperty("FileSize")
                private Long fileSize;
                @JsonProperty("Privilege")
                private Long privilege;
                @JsonProperty("Hash")
                private String hash;
                @JsonProperty("BitRate")
                private Long bitRate;
                @JsonProperty("TimeLength")
                private Long timeLength;
            }

            @NoArgsConstructor
            @Data
            public static class GrpDTO {
                @JsonProperty("PublishTime")
                private String publishTime;
                @JsonProperty("Audioid")
                private Long audioid;
                @JsonProperty("OldCpy")
                private Long oldCpy;
                @JsonProperty("PublishAge")
                private Long publishAge;
                @JsonProperty("bitflag")
                private Long bitflag;
                @JsonProperty("PayType")
                private Long payType;
                @JsonProperty("TagContent")
                private String tagContent;
                @JsonProperty("Accompany")
                private Long accompany;
                @JsonProperty("SingerName")
                private String singerName;
                @JsonProperty("ShowingFlag")
                private Long showingFlag;
                @JsonProperty("Source")
                private String source;
                @JsonProperty("AlbumAux")
                private String albumAux;
                @JsonProperty("Image")
                private String image;
                @JsonProperty("SQ")
                private SQDTO sq;
                @JsonProperty("M4aSize")
                private Long m4aSize;
                @JsonProperty("HQ")
                private HQDTO hq;
                @JsonProperty("HeatLevel")
                private Long heatLevel;
                @JsonProperty("UploaderContent")
                private String uploaderContent;
                @JsonProperty("FileSize")
                private Long fileSize;
                @JsonProperty("IsOriginal")
                private Long isOriginal;
                @JsonProperty("FileHash")
                private String fileHash;
                @JsonProperty("trans_param")
                private TransParamDTO transParam;
                @JsonProperty("isPrepublish")
                private Long isPrepublish;
                @JsonProperty("Type")
                private String type;
                @JsonProperty("Bitrate")
                private Long bitrate;
                @JsonProperty("ExtName")
                private String extName;
                @JsonProperty("AlbumPrivilege")
                private Long albumPrivilege;
                @JsonProperty("AlbumID")
                private String albumID;
                @JsonProperty("AlbumName")
                private String albumName;
                @JsonProperty("mvdata")
                private List<MvdataDTO> mvdata;
                @JsonProperty("OtherName")
                private String otherName;
                @JsonProperty("Res")
                private ResDTO res;
                @JsonProperty("SourceID")
                private Long sourceID;
                @JsonProperty("MixSongID")
                private String mixSongID;
                @JsonProperty("Singers")
                private List<SingersDTO> singers;
                @JsonProperty("Suffix")
                private String suffix;
                @JsonProperty("MatchFlag")
                private Long matchFlag;
                @JsonProperty("Scid")
                private Long scid;
                @JsonProperty("OriSongName")
                private String oriSongName;
                @JsonProperty("FailProcess")
                private Long failProcess;
                @JsonProperty("RankId")
                private Long rankId;
                @JsonProperty("PublishDate")
                private String publishDate;
                @JsonProperty("Auxiliary")
                private String auxiliary;
                @JsonProperty("TagDetails")
                private List<?> tagDetails;
                @JsonProperty("PrepublishInfo")
                private PrepublishInfoDTO prepublishInfo;
                @JsonProperty("OwnerCount")
                private Long ownerCount;
                @JsonProperty("Uploader")
                private String uploader;
                @JsonProperty("Duration")
                private Long duration;
                @JsonProperty("TopID")
                private Long topID;
                @JsonProperty("FileName")
                private String fileName;
                @JsonProperty("recommend_type")
                private Long recommendType;

                @NoArgsConstructor
                @Data
                public static class SQDTO {
                    @JsonProperty("FileSize")
                    private Long fileSize;
                    @JsonProperty("Hash")
                    private String hash;
                    @JsonProperty("Privilege")
                    private Long privilege;
                }

                @NoArgsConstructor
                @Data
                public static class HQDTO {
                    @JsonProperty("FileSize")
                    private Long fileSize;
                    @JsonProperty("Hash")
                    private String hash;
                    @JsonProperty("Privilege")
                    private Long privilege;
                }

                @NoArgsConstructor
                @Data
                public static class TransParamDTO {
                    @JsonProperty("union_cover")
                    private String unionCover;
                    @JsonProperty("identity_block")
                    private Long identityBlock;
                    @JsonProperty("ipmap")
                    private IpmapDTO ipmap;
                    @JsonProperty("language")
                    private String language;
                    @JsonProperty("cpy_grade")
                    private Long cpyGrade;
                    @JsonProperty("classmap")
                    private ClassmapDTO classmap;
                    @JsonProperty("qualitymap")
                    private QualitymapDTO qualitymap;
                    @JsonProperty("pay_block_tpl")
                    private Long payBlockTpl;
                    @JsonProperty("cid")
                    private Long cid;
                    @JsonProperty("display_rate")
                    private Long displayRate;
                    @JsonProperty("cpy_attr0")
                    private Long cpyAttr0;
                    @JsonProperty("hash_multitrack")
                    private String hashMultitrack;
                    @JsonProperty("appid_block")
                    private String appidBlock;
                    @JsonProperty("musicpack_advance")
                    private Long musicpackAdvance;
                    @JsonProperty("display")
                    private Long display;
                    @JsonProperty("cpy_level")
                    private Long cpyLevel;

                    @NoArgsConstructor
                    @Data
                    public static class IpmapDTO {
                        @JsonProperty("attr0")
                        private Long attr0;
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
                }

                @NoArgsConstructor
                @Data
                public static class ResDTO {
                    @JsonProperty("FileSize")
                    private Long fileSize;
                    @JsonProperty("Privilege")
                    private Long privilege;
                    @JsonProperty("Hash")
                    private String hash;
                    @JsonProperty("BitRate")
                    private Long bitRate;
                    @JsonProperty("TimeLength")
                    private Long timeLength;
                }

                @NoArgsConstructor
                @Data
                public static class PrepublishInfoDTO {
                    @JsonProperty("ReserveCount")
                    private Long reserveCount;
                    @JsonProperty("DisplayTime")
                    private String displayTime;
                    @JsonProperty("Id")
                    private Long id;
                    @JsonProperty("PublishTime")
                    private String publishTime;
                }

                @NoArgsConstructor
                @Data
                public static class MvdataDTO {
                    @JsonProperty("typ")
                    private Long typ;
                    @JsonProperty("trk")
                    private String trk;
                    @JsonProperty("hash")
                    private String hash;
                    @JsonProperty("id")
                    private String id;
                }

                @NoArgsConstructor
                @Data
                public static class SingersDTO {
                    @JsonProperty("name")
                    private String name;
                    @JsonProperty("ip_id")
                    private Long ipId;
                    @JsonProperty("id")
                    private Long id;
                }
            }

            @NoArgsConstructor
            @Data
            public static class MvdataDTO {
                @JsonProperty("id")
                private String id;
                @JsonProperty("trk")
                private String trk;
                @JsonProperty("hash")
                private String hash;
                @JsonProperty("typ")
                private Long typ;
            }

            @NoArgsConstructor
            @Data
            public static class SingersDTO {
                @JsonProperty("name")
                private String name;
                @JsonProperty("ip_id")
                private Long ipId;
                @JsonProperty("id")
                private Long id;
            }

            @NoArgsConstructor
            @Data
            public static class TagDetailsDTO {
                @JsonProperty("content")
                private String content;
                @JsonProperty("version")
                private Long version;
                @JsonProperty("type")
                private Long type;
            }
        }
    }
}
