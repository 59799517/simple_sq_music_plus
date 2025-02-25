package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname AlubmSongResult
 * @Description 专辑歌曲
 * @Version 1.0.0
 * @Date 2025/2/11 18:05
 * @Created by SQ
 */

public class AlubmSongResult {

    @JSONField(name = "total")
    private Long total;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "data")
    private DataDTO data;
    @JSONField(name = "extra")
    private ExtraDTO extra;
    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "errmsg")
    private String errmsg;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public ExtraDTO getExtra() {
        return extra;
    }

    public void setExtra(ExtraDTO extra) {
        this.extra = extra;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class DataDTO {
        @JSONField(name = "total")
        private Long total;
        @JSONField(name = "songs")
        private List<SongsDTO> songs;

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public List<SongsDTO> getSongs() {
            return songs;
        }

        public void setSongs(List<SongsDTO> songs) {
            this.songs = songs;
        }

        public static class SongsDTO {
            @JSONField(name = "mvdata")
            private List<MvdataDTO> mvdata;
            @JSONField(name = "extend")
            private ExtendDTO extend;
            @JSONField(name = "trans_param")
            private TransParamDTO transParam;
            @JSONField(name = "copyright")
            private CopyrightDTO copyright;
            @JSONField(name = "audio_info")
            private AudioInfoDTO audioInfo;
            @JSONField(name = "base")
            private BaseDTO base;
            @JSONField(name = "album_info")
            private AlbumInfoDTO albumInfo;
            @JSONField(name = "extra")
            private ExtraDTO extra;
            @JSONField(name = "deprecated")
            private DeprecatedDTO deprecated;
            @JSONField(name = "authors")
            private List<AuthorsDTO> authors;

            public List<MvdataDTO> getMvdata() {
                return mvdata;
            }

            public void setMvdata(List<MvdataDTO> mvdata) {
                this.mvdata = mvdata;
            }

            public ExtendDTO getExtend() {
                return extend;
            }

            public void setExtend(ExtendDTO extend) {
                this.extend = extend;
            }

            public TransParamDTO getTransParam() {
                return transParam;
            }

            public void setTransParam(TransParamDTO transParam) {
                this.transParam = transParam;
            }

            public CopyrightDTO getCopyright() {
                return copyright;
            }

            public void setCopyright(CopyrightDTO copyright) {
                this.copyright = copyright;
            }

            public AudioInfoDTO getAudioInfo() {
                return audioInfo;
            }

            public void setAudioInfo(AudioInfoDTO audioInfo) {
                this.audioInfo = audioInfo;
            }

            public BaseDTO getBase() {
                return base;
            }

            public void setBase(BaseDTO base) {
                this.base = base;
            }

            public AlbumInfoDTO getAlbumInfo() {
                return albumInfo;
            }

            public void setAlbumInfo(AlbumInfoDTO albumInfo) {
                this.albumInfo = albumInfo;
            }

            public ExtraDTO getExtra() {
                return extra;
            }

            public void setExtra(ExtraDTO extra) {
                this.extra = extra;
            }

            public DeprecatedDTO getDeprecated() {
                return deprecated;
            }

            public void setDeprecated(DeprecatedDTO deprecated) {
                this.deprecated = deprecated;
            }

            public List<AuthorsDTO> getAuthors() {
                return authors;
            }

            public void setAuthors(List<AuthorsDTO> authors) {
                this.authors = authors;
            }

            public static class ExtendDTO {
                @JSONField(name = "has_obbligato")
                private Long hasObbligato;
                @JSONField(name = "cd_name")
                private String cdName;
                @JSONField(name = "disc")
                private Long disc;
                @JSONField(name = "sort")
                private Long sort;

                public Long getHasObbligato() {
                    return hasObbligato;
                }

                public void setHasObbligato(Long hasObbligato) {
                    this.hasObbligato = hasObbligato;
                }

                public String getCdName() {
                    return cdName;
                }

                public void setCdName(String cdName) {
                    this.cdName = cdName;
                }

                public Long getDisc() {
                    return disc;
                }

                public void setDisc(Long disc) {
                    this.disc = disc;
                }

                public Long getSort() {
                    return sort;
                }

                public void setSort(Long sort) {
                    this.sort = sort;
                }
            }

            public static class TransParamDTO {
                @JSONField(name = "ogg_128_hash")
                private String ogg128Hash;
                @JSONField(name = "classmap")
                private ClassmapDTO classmap;
                @JSONField(name = "language")
                private String language;
                @JSONField(name = "cpy_attr0")
                private Long cpyAttr0;
                @JSONField(name = "musicpack_advance")
                private Long musicpackAdvance;
                @JSONField(name = "display")
                private Long display;
                @JSONField(name = "display_rate")
                private Long displayRate;
                @JSONField(name = "union_cover")
                private String unionCover;
                @JSONField(name = "ogg_320_filesize")
                private Long ogg320Filesize;
                @JSONField(name = "qualitymap")
                private QualitymapDTO qualitymap;
                @JSONField(name = "ogg_320_hash")
                private String ogg320Hash;
                @JSONField(name = "ogg_128_filesize")
                private Long ogg128Filesize;
                @JSONField(name = "cid")
                private Long cid;
                @JSONField(name = "cpy_grade")
                private Long cpyGrade;
                @JSONField(name = "appid_block")
                private String appidBlock;
                @JSONField(name = "ipmap")
                private IpmapDTO ipmap;
                @JSONField(name = "hash_offset")
                private HashOffsetDTO hashOffset;
                @JSONField(name = "hash_multitrack")
                private String hashMultitrack;
                @JSONField(name = "pay_block_tpl")
                private Long payBlockTpl;
                @JSONField(name = "cpy_level")
                private Long cpyLevel;

                public String getOgg128Hash() {
                    return ogg128Hash;
                }

                public void setOgg128Hash(String ogg128Hash) {
                    this.ogg128Hash = ogg128Hash;
                }

                public ClassmapDTO getClassmap() {
                    return classmap;
                }

                public void setClassmap(ClassmapDTO classmap) {
                    this.classmap = classmap;
                }

                public String getLanguage() {
                    return language;
                }

                public void setLanguage(String language) {
                    this.language = language;
                }

                public Long getCpyAttr0() {
                    return cpyAttr0;
                }

                public void setCpyAttr0(Long cpyAttr0) {
                    this.cpyAttr0 = cpyAttr0;
                }

                public Long getMusicpackAdvance() {
                    return musicpackAdvance;
                }

                public void setMusicpackAdvance(Long musicpackAdvance) {
                    this.musicpackAdvance = musicpackAdvance;
                }

                public Long getDisplay() {
                    return display;
                }

                public void setDisplay(Long display) {
                    this.display = display;
                }

                public Long getDisplayRate() {
                    return displayRate;
                }

                public void setDisplayRate(Long displayRate) {
                    this.displayRate = displayRate;
                }

                public String getUnionCover() {
                    return unionCover;
                }

                public void setUnionCover(String unionCover) {
                    this.unionCover = unionCover;
                }

                public Long getOgg320Filesize() {
                    return ogg320Filesize;
                }

                public void setOgg320Filesize(Long ogg320Filesize) {
                    this.ogg320Filesize = ogg320Filesize;
                }

                public QualitymapDTO getQualitymap() {
                    return qualitymap;
                }

                public void setQualitymap(QualitymapDTO qualitymap) {
                    this.qualitymap = qualitymap;
                }

                public String getOgg320Hash() {
                    return ogg320Hash;
                }

                public void setOgg320Hash(String ogg320Hash) {
                    this.ogg320Hash = ogg320Hash;
                }

                public Long getOgg128Filesize() {
                    return ogg128Filesize;
                }

                public void setOgg128Filesize(Long ogg128Filesize) {
                    this.ogg128Filesize = ogg128Filesize;
                }

                public Long getCid() {
                    return cid;
                }

                public void setCid(Long cid) {
                    this.cid = cid;
                }

                public Long getCpyGrade() {
                    return cpyGrade;
                }

                public void setCpyGrade(Long cpyGrade) {
                    this.cpyGrade = cpyGrade;
                }

                public String getAppidBlock() {
                    return appidBlock;
                }

                public void setAppidBlock(String appidBlock) {
                    this.appidBlock = appidBlock;
                }

                public IpmapDTO getIpmap() {
                    return ipmap;
                }

                public void setIpmap(IpmapDTO ipmap) {
                    this.ipmap = ipmap;
                }

                public HashOffsetDTO getHashOffset() {
                    return hashOffset;
                }

                public void setHashOffset(HashOffsetDTO hashOffset) {
                    this.hashOffset = hashOffset;
                }

                public String getHashMultitrack() {
                    return hashMultitrack;
                }

                public void setHashMultitrack(String hashMultitrack) {
                    this.hashMultitrack = hashMultitrack;
                }

                public Long getPayBlockTpl() {
                    return payBlockTpl;
                }

                public void setPayBlockTpl(Long payBlockTpl) {
                    this.payBlockTpl = payBlockTpl;
                }

                public Long getCpyLevel() {
                    return cpyLevel;
                }

                public void setCpyLevel(Long cpyLevel) {
                    this.cpyLevel = cpyLevel;
                }

                public static class ClassmapDTO {
                    @JSONField(name = "attr0")
                    private Long attr0;

                    public Long getAttr0() {
                        return attr0;
                    }

                    public void setAttr0(Long attr0) {
                        this.attr0 = attr0;
                    }
                }

                public static class QualitymapDTO {
                    @JSONField(name = "attr0")
                    private Long attr0;
                    @JSONField(name = "attr1")
                    private Long attr1;

                    public Long getAttr0() {
                        return attr0;
                    }

                    public void setAttr0(Long attr0) {
                        this.attr0 = attr0;
                    }

                    public Long getAttr1() {
                        return attr1;
                    }

                    public void setAttr1(Long attr1) {
                        this.attr1 = attr1;
                    }
                }

                public static class IpmapDTO {
                    @JSONField(name = "attr0")
                    private Long attr0;

                    public Long getAttr0() {
                        return attr0;
                    }

                    public void setAttr0(Long attr0) {
                        this.attr0 = attr0;
                    }
                }

                public static class HashOffsetDTO {
                    @JSONField(name = "clip_hash")
                    private String clipHash;
                    @JSONField(name = "start_byte")
                    private Long startByte;
                    @JSONField(name = "end_ms")
                    private Long endMs;
                    @JSONField(name = "end_byte")
                    private Long endByte;
                    @JSONField(name = "file_type")
                    private Long fileType;
                    @JSONField(name = "start_ms")
                    private Long startMs;
                    @JSONField(name = "offset_hash")
                    private String offsetHash;

                    public String getClipHash() {
                        return clipHash;
                    }

                    public void setClipHash(String clipHash) {
                        this.clipHash = clipHash;
                    }

                    public Long getStartByte() {
                        return startByte;
                    }

                    public void setStartByte(Long startByte) {
                        this.startByte = startByte;
                    }

                    public Long getEndMs() {
                        return endMs;
                    }

                    public void setEndMs(Long endMs) {
                        this.endMs = endMs;
                    }

                    public Long getEndByte() {
                        return endByte;
                    }

                    public void setEndByte(Long endByte) {
                        this.endByte = endByte;
                    }

                    public Long getFileType() {
                        return fileType;
                    }

                    public void setFileType(Long fileType) {
                        this.fileType = fileType;
                    }

                    public Long getStartMs() {
                        return startMs;
                    }

                    public void setStartMs(Long startMs) {
                        this.startMs = startMs;
                    }

                    public String getOffsetHash() {
                        return offsetHash;
                    }

                    public void setOffsetHash(String offsetHash) {
                        this.offsetHash = offsetHash;
                    }
                }
            }

            public static class CopyrightDTO {
                @JSONField(name = "audition")
                private AuditionDTO audition;
                @JSONField(name = "privilege_flac")
                private Long privilegeFlac;
                @JSONField(name = "sale_mode_128_download")
                private Long saleMode128Download;
                @JSONField(name = "viponly_tag")
                private Long viponlyTag;
                @JSONField(name = "privilege_128")
                private Long privilege128;
                @JSONField(name = "album_sale_url")
                private String albumSaleUrl;
                @JSONField(name = "privilege")
                private Long privilege;
                @JSONField(name = "sale_mode_flac_download")
                private Long saleModeFlacDownload;
                @JSONField(name = "privilege_320")
                private Long privilege320;
                @JSONField(name = "sale_mode_320_download")
                private Long saleMode320Download;
                @JSONField(name = "sale_mode_download")
                private Long saleModeDownload;

                public AuditionDTO getAudition() {
                    return audition;
                }

                public void setAudition(AuditionDTO audition) {
                    this.audition = audition;
                }

                public Long getPrivilegeFlac() {
                    return privilegeFlac;
                }

                public void setPrivilegeFlac(Long privilegeFlac) {
                    this.privilegeFlac = privilegeFlac;
                }

                public Long getSaleMode128Download() {
                    return saleMode128Download;
                }

                public void setSaleMode128Download(Long saleMode128Download) {
                    this.saleMode128Download = saleMode128Download;
                }

                public Long getViponlyTag() {
                    return viponlyTag;
                }

                public void setViponlyTag(Long viponlyTag) {
                    this.viponlyTag = viponlyTag;
                }

                public Long getPrivilege128() {
                    return privilege128;
                }

                public void setPrivilege128(Long privilege128) {
                    this.privilege128 = privilege128;
                }

                public String getAlbumSaleUrl() {
                    return albumSaleUrl;
                }

                public void setAlbumSaleUrl(String albumSaleUrl) {
                    this.albumSaleUrl = albumSaleUrl;
                }

                public Long getPrivilege() {
                    return privilege;
                }

                public void setPrivilege(Long privilege) {
                    this.privilege = privilege;
                }

                public Long getSaleModeFlacDownload() {
                    return saleModeFlacDownload;
                }

                public void setSaleModeFlacDownload(Long saleModeFlacDownload) {
                    this.saleModeFlacDownload = saleModeFlacDownload;
                }

                public Long getPrivilege320() {
                    return privilege320;
                }

                public void setPrivilege320(Long privilege320) {
                    this.privilege320 = privilege320;
                }

                public Long getSaleMode320Download() {
                    return saleMode320Download;
                }

                public void setSaleMode320Download(Long saleMode320Download) {
                    this.saleMode320Download = saleMode320Download;
                }

                public Long getSaleModeDownload() {
                    return saleModeDownload;
                }

                public void setSaleModeDownload(Long saleModeDownload) {
                    this.saleModeDownload = saleModeDownload;
                }

                public static class AuditionDTO {
                    @JSONField(name = "clip_hash")
                    private String clipHash;
                    @JSONField(name = "start_byte")
                    private Long startByte;
                    @JSONField(name = "end_ms")
                    private Long endMs;
                    @JSONField(name = "end_byte")
                    private Long endByte;
                    @JSONField(name = "file_type")
                    private Long fileType;
                    @JSONField(name = "start_ms")
                    private Long startMs;
                    @JSONField(name = "offset_hash")
                    private String offsetHash;

                    public String getClipHash() {
                        return clipHash;
                    }

                    public void setClipHash(String clipHash) {
                        this.clipHash = clipHash;
                    }

                    public Long getStartByte() {
                        return startByte;
                    }

                    public void setStartByte(Long startByte) {
                        this.startByte = startByte;
                    }

                    public Long getEndMs() {
                        return endMs;
                    }

                    public void setEndMs(Long endMs) {
                        this.endMs = endMs;
                    }

                    public Long getEndByte() {
                        return endByte;
                    }

                    public void setEndByte(Long endByte) {
                        this.endByte = endByte;
                    }

                    public Long getFileType() {
                        return fileType;
                    }

                    public void setFileType(Long fileType) {
                        this.fileType = fileType;
                    }

                    public Long getStartMs() {
                        return startMs;
                    }

                    public void setStartMs(Long startMs) {
                        this.startMs = startMs;
                    }

                    public String getOffsetHash() {
                        return offsetHash;
                    }

                    public void setOffsetHash(String offsetHash) {
                        this.offsetHash = offsetHash;
                    }
                }
            }

            public static class AudioInfoDTO {
                @JSONField(name = "hash")
                private String hash;
                @JSONField(name = "bitrate")
                private Long bitrate;
                @JSONField(name = "duration_flac")
                private Long durationFlac;
                @JSONField(name = "filesize_320")
                private Long filesize320;
                @JSONField(name = "filesize_flac")
                private Long filesizeFlac;
                @JSONField(name = "duration_320")
                private Long duration320;
                @JSONField(name = "filesize_high")
                private Long filesizeHigh;
                @JSONField(name = "hash_320")
                private String hash320;
                @JSONField(name = "duration")
                private Long duration;
                @JSONField(name = "duration_high")
                private Long durationHigh;
                @JSONField(name = "hash_high")
                private String hashHigh;
                @JSONField(name = "hash_super")
                private String hashSuper;
                @JSONField(name = "filesize")
                private Long filesize;
                @JSONField(name = "hash_128")
                private String hash128;
                @JSONField(name = "bitrate_high")
                private Long bitrateHigh;
                @JSONField(name = "hash_flac")
                private String hashFlac;
                @JSONField(name = "filesize_128")
                private Long filesize128;
                @JSONField(name = "duration_super")
                private Long durationSuper;
                @JSONField(name = "bitrate_super")
                private Long bitrateSuper;
                @JSONField(name = "filesize_super")
                private Long filesizeSuper;
                @JSONField(name = "extname")
                private String extname;
                @JSONField(name = "duration_128")
                private Long duration128;
                @JSONField(name = "extname_super")
                private String extnameSuper;

                public String getHash() {
                    return hash;
                }

                public void setHash(String hash) {
                    this.hash = hash;
                }

                public Long getBitrate() {
                    return bitrate;
                }

                public void setBitrate(Long bitrate) {
                    this.bitrate = bitrate;
                }

                public Long getDurationFlac() {
                    return durationFlac;
                }

                public void setDurationFlac(Long durationFlac) {
                    this.durationFlac = durationFlac;
                }

                public Long getFilesize320() {
                    return filesize320;
                }

                public void setFilesize320(Long filesize320) {
                    this.filesize320 = filesize320;
                }

                public Long getFilesizeFlac() {
                    return filesizeFlac;
                }

                public void setFilesizeFlac(Long filesizeFlac) {
                    this.filesizeFlac = filesizeFlac;
                }

                public Long getDuration320() {
                    return duration320;
                }

                public void setDuration320(Long duration320) {
                    this.duration320 = duration320;
                }

                public Long getFilesizeHigh() {
                    return filesizeHigh;
                }

                public void setFilesizeHigh(Long filesizeHigh) {
                    this.filesizeHigh = filesizeHigh;
                }

                public String getHash320() {
                    return hash320;
                }

                public void setHash320(String hash320) {
                    this.hash320 = hash320;
                }

                public Long getDuration() {
                    return duration;
                }

                public void setDuration(Long duration) {
                    this.duration = duration;
                }

                public Long getDurationHigh() {
                    return durationHigh;
                }

                public void setDurationHigh(Long durationHigh) {
                    this.durationHigh = durationHigh;
                }

                public String getHashHigh() {
                    return hashHigh;
                }

                public void setHashHigh(String hashHigh) {
                    this.hashHigh = hashHigh;
                }

                public String getHashSuper() {
                    return hashSuper;
                }

                public void setHashSuper(String hashSuper) {
                    this.hashSuper = hashSuper;
                }

                public Long getFilesize() {
                    return filesize;
                }

                public void setFilesize(Long filesize) {
                    this.filesize = filesize;
                }

                public String getHash128() {
                    return hash128;
                }

                public void setHash128(String hash128) {
                    this.hash128 = hash128;
                }

                public Long getBitrateHigh() {
                    return bitrateHigh;
                }

                public void setBitrateHigh(Long bitrateHigh) {
                    this.bitrateHigh = bitrateHigh;
                }

                public String getHashFlac() {
                    return hashFlac;
                }

                public void setHashFlac(String hashFlac) {
                    this.hashFlac = hashFlac;
                }

                public Long getFilesize128() {
                    return filesize128;
                }

                public void setFilesize128(Long filesize128) {
                    this.filesize128 = filesize128;
                }

                public Long getDurationSuper() {
                    return durationSuper;
                }

                public void setDurationSuper(Long durationSuper) {
                    this.durationSuper = durationSuper;
                }

                public Long getBitrateSuper() {
                    return bitrateSuper;
                }

                public void setBitrateSuper(Long bitrateSuper) {
                    this.bitrateSuper = bitrateSuper;
                }

                public Long getFilesizeSuper() {
                    return filesizeSuper;
                }

                public void setFilesizeSuper(Long filesizeSuper) {
                    this.filesizeSuper = filesizeSuper;
                }

                public String getExtname() {
                    return extname;
                }

                public void setExtname(String extname) {
                    this.extname = extname;
                }

                public Long getDuration128() {
                    return duration128;
                }

                public void setDuration128(Long duration128) {
                    this.duration128 = duration128;
                }

                public String getExtnameSuper() {
                    return extnameSuper;
                }

                public void setExtnameSuper(String extnameSuper) {
                    this.extnameSuper = extnameSuper;
                }
            }

            public static class BaseDTO {
                @JSONField(name = "is_publish")
                private Long isPublish;
                @JSONField(name = "album_id")
                private Long albumId;
                @JSONField(name = "author_name")
                private String authorName;
                @JSONField(name = "audio_id")
                private Long audioId;
                @JSONField(name = "audio_name")
                private String audioName;
                @JSONField(name = "album_audio_id")
                private Long albumAudioId;

                public Long getIsPublish() {
                    return isPublish;
                }

                public void setIsPublish(Long isPublish) {
                    this.isPublish = isPublish;
                }

                public Long getAlbumId() {
                    return albumId;
                }

                public void setAlbumId(Long albumId) {
                    this.albumId = albumId;
                }

                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }

                public Long getAudioId() {
                    return audioId;
                }

                public void setAudioId(Long audioId) {
                    this.audioId = audioId;
                }

                public String getAudioName() {
                    return audioName;
                }

                public void setAudioName(String audioName) {
                    this.audioName = audioName;
                }

                public Long getAlbumAudioId() {
                    return albumAudioId;
                }

                public void setAlbumAudioId(Long albumAudioId) {
                    this.albumAudioId = albumAudioId;
                }
            }

            public static class AlbumInfoDTO {
                @JSONField(name = "album_name")
                private String albumName;
                @JSONField(name = "cover")
                private String cover;

                public String getAlbumName() {
                    return albumName;
                }

                public void setAlbumName(String albumName) {
                    this.albumName = albumName;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }
            }

            public static class ExtraDTO {
                @JSONField(name = "remark")
                private String remark;

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }
            }

            public static class DeprecatedDTO {
                @JSONField(name = "cpy_grade")
                private Long cpyGrade;
                @JSONField(name = "video_hash")
                private String videoHash;
                @JSONField(name = "old_cpy")
                private Long oldCpy;
                @JSONField(name = "pkg_price")
                private Long pkgPrice;
                @JSONField(name = "cid")
                private Long cid;
                @JSONField(name = "price")
                private Long price;
                @JSONField(name = "display_rate")
                private Long displayRate;
                @JSONField(name = "hash_multitrack")
                private String hashMultitrack;
                @JSONField(name = "pay_type")
                private Long payType;
                @JSONField(name = "pay_block_tpl")
                private Long payBlockTpl;
                @JSONField(name = "display")
                private Long display;
                @JSONField(name = "type")
                private String type;

                public Long getCpyGrade() {
                    return cpyGrade;
                }

                public void setCpyGrade(Long cpyGrade) {
                    this.cpyGrade = cpyGrade;
                }

                public String getVideoHash() {
                    return videoHash;
                }

                public void setVideoHash(String videoHash) {
                    this.videoHash = videoHash;
                }

                public Long getOldCpy() {
                    return oldCpy;
                }

                public void setOldCpy(Long oldCpy) {
                    this.oldCpy = oldCpy;
                }

                public Long getPkgPrice() {
                    return pkgPrice;
                }

                public void setPkgPrice(Long pkgPrice) {
                    this.pkgPrice = pkgPrice;
                }

                public Long getCid() {
                    return cid;
                }

                public void setCid(Long cid) {
                    this.cid = cid;
                }

                public Long getPrice() {
                    return price;
                }

                public void setPrice(Long price) {
                    this.price = price;
                }

                public Long getDisplayRate() {
                    return displayRate;
                }

                public void setDisplayRate(Long displayRate) {
                    this.displayRate = displayRate;
                }

                public String getHashMultitrack() {
                    return hashMultitrack;
                }

                public void setHashMultitrack(String hashMultitrack) {
                    this.hashMultitrack = hashMultitrack;
                }

                public Long getPayType() {
                    return payType;
                }

                public void setPayType(Long payType) {
                    this.payType = payType;
                }

                public Long getPayBlockTpl() {
                    return payBlockTpl;
                }

                public void setPayBlockTpl(Long payBlockTpl) {
                    this.payBlockTpl = payBlockTpl;
                }

                public Long getDisplay() {
                    return display;
                }

                public void setDisplay(Long display) {
                    this.display = display;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class MvdataDTO {
                @JSONField(name = "typ")
                private Long typ;
                @JSONField(name = "trk")
                private String trk;
                @JSONField(name = "hash")
                private String hash;
                @JSONField(name = "id")
                private String id;

                public Long getTyp() {
                    return typ;
                }

                public void setTyp(Long typ) {
                    this.typ = typ;
                }

                public String getTrk() {
                    return trk;
                }

                public void setTrk(String trk) {
                    this.trk = trk;
                }

                public String getHash() {
                    return hash;
                }

                public void setHash(String hash) {
                    this.hash = hash;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }

            public static class AuthorsDTO {
                @JSONField(name = "author_name")
                private String authorName;
                @JSONField(name = "author_id")
                private Long authorId;

                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }

                public Long getAuthorId() {
                    return authorId;
                }

                public void setAuthorId(Long authorId) {
                    this.authorId = authorId;
                }
            }
        }
    }

    public static class ExtraDTO {
        @JSONField(name = "disc_cnt")
        private Long discCnt;

        public Long getDiscCnt() {
            return discCnt;
        }

        public void setDiscCnt(Long discCnt) {
            this.discCnt = discCnt;
        }
    }
}
