package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname SongInfoResult
 * @Description
 * @Version 1.0.0
 * @Date 2025/2/11 11:33
 * @Created by SQ
 */

public class SongInfoResult {


    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "appid_group")
    private Long appidGroup;
    @JSONField(name = "should_cache")
    private Long shouldCache;
    @JSONField(name = "data")
    private List<DataDTO> data;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getAppidGroup() {
        return appidGroup;
    }

    public void setAppidGroup(Long appidGroup) {
        this.appidGroup = appidGroup;
    }

    public Long getShouldCache() {
        return shouldCache;
    }

    public void setShouldCache(Long shouldCache) {
        this.shouldCache = shouldCache;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        @JSONField(name = "type")
        private String type;
        @JSONField(name = "id")
        private Long id;
        @JSONField(name = "album_id")
        private String albumId;
        @JSONField(name = "recommend_album_id")
        private String recommendAlbumId;
        @JSONField(name = "album_audio_id")
        private Long albumAudioId;
        @JSONField(name = "hash")
        private String hash;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "singername")
        private String singername;
        @JSONField(name = "albumname")
        private String albumname;
        @JSONField(name = "level")
        private Long level;
        @JSONField(name = "quality")
        private String quality;
        @JSONField(name = "expire")
        private Long expire;
        @JSONField(name = "publish")
        private Long publish;
        @JSONField(name = "is_publish")
        private Long isPublish;
        @JSONField(name = "old_hide")
        private Long oldHide;
        @JSONField(name = "privilege")
        private Long privilege;
        @JSONField(name = "status")
        private Long status;
        @JSONField(name = "fail_process")
        private Long failProcess;
        @JSONField(name = "pay_type")
        private Long payType;
        @JSONField(name = "price")
        private Long price;
        @JSONField(name = "pkg_price")
        private Long pkgPrice;
        @JSONField(name = "topic_url")
        private String topicUrl;
        @JSONField(name = "topic_remark")
        private String topicRemark;
        @JSONField(name = "info")
        private InfoDTO info;
        @JSONField(name = "discount")
        private List<?> discount;
        @JSONField(name = "start_time")
        private String startTime;
        @JSONField(name = "end_time")
        private String endTime;
        @JSONField(name = "cd_url")
        private String cdUrl;
        @JSONField(name = "cid")
        private Long cid;
        @JSONField(name = "old_cpy")
        private Long oldCpy;
        @JSONField(name = "rebuy_pay_type")
        private Long rebuyPayType;
        @JSONField(name = "is_search_top")
        private Long isSearchTop;
        @JSONField(name = "pay_block_text")
        private String payBlockText;
        @JSONField(name = "is_separate")
        private Long isSeparate;
        @JSONField(name = "buy_count")
        private Long buyCount;
        @JSONField(name = "buy_count_vip")
        private Long buyCountVip;
        @JSONField(name = "buy_count_kubi")
        private Long buyCountKubi;
        @JSONField(name = "buy_count_audios")
        private Long buyCountAudios;
        @JSONField(name = "trans_param")
        private TransParamDTO transParam;
        @JSONField(name = "_msg")
        private String msg;
        @JSONField(name = "_errno")
        private Long errno;
        @JSONField(name = "relate_goods")
        private List<RelateGoodsDTO> relateGoods;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getRecommendAlbumId() {
            return recommendAlbumId;
        }

        public void setRecommendAlbumId(String recommendAlbumId) {
            this.recommendAlbumId = recommendAlbumId;
        }

        public Long getAlbumAudioId() {
            return albumAudioId;
        }

        public void setAlbumAudioId(Long albumAudioId) {
            this.albumAudioId = albumAudioId;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }

        public String getAlbumname() {
            return albumname;
        }

        public void setAlbumname(String albumname) {
            this.albumname = albumname;
        }

        public Long getLevel() {
            return level;
        }

        public void setLevel(Long level) {
            this.level = level;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public Long getExpire() {
            return expire;
        }

        public void setExpire(Long expire) {
            this.expire = expire;
        }

        public Long getPublish() {
            return publish;
        }

        public void setPublish(Long publish) {
            this.publish = publish;
        }

        public Long getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(Long isPublish) {
            this.isPublish = isPublish;
        }

        public Long getOldHide() {
            return oldHide;
        }

        public void setOldHide(Long oldHide) {
            this.oldHide = oldHide;
        }

        public Long getPrivilege() {
            return privilege;
        }

        public void setPrivilege(Long privilege) {
            this.privilege = privilege;
        }

        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public Long getFailProcess() {
            return failProcess;
        }

        public void setFailProcess(Long failProcess) {
            this.failProcess = failProcess;
        }

        public Long getPayType() {
            return payType;
        }

        public void setPayType(Long payType) {
            this.payType = payType;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }

        public Long getPkgPrice() {
            return pkgPrice;
        }

        public void setPkgPrice(Long pkgPrice) {
            this.pkgPrice = pkgPrice;
        }

        public String getTopicUrl() {
            return topicUrl;
        }

        public void setTopicUrl(String topicUrl) {
            this.topicUrl = topicUrl;
        }

        public String getTopicRemark() {
            return topicRemark;
        }

        public void setTopicRemark(String topicRemark) {
            this.topicRemark = topicRemark;
        }

        public InfoDTO getInfo() {
            return info;
        }

        public void setInfo(InfoDTO info) {
            this.info = info;
        }

        public List<?> getDiscount() {
            return discount;
        }

        public void setDiscount(List<?> discount) {
            this.discount = discount;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCdUrl() {
            return cdUrl;
        }

        public void setCdUrl(String cdUrl) {
            this.cdUrl = cdUrl;
        }

        public Long getCid() {
            return cid;
        }

        public void setCid(Long cid) {
            this.cid = cid;
        }

        public Long getOldCpy() {
            return oldCpy;
        }

        public void setOldCpy(Long oldCpy) {
            this.oldCpy = oldCpy;
        }

        public Long getRebuyPayType() {
            return rebuyPayType;
        }

        public void setRebuyPayType(Long rebuyPayType) {
            this.rebuyPayType = rebuyPayType;
        }

        public Long getIsSearchTop() {
            return isSearchTop;
        }

        public void setIsSearchTop(Long isSearchTop) {
            this.isSearchTop = isSearchTop;
        }

        public String getPayBlockText() {
            return payBlockText;
        }

        public void setPayBlockText(String payBlockText) {
            this.payBlockText = payBlockText;
        }

        public Long getIsSeparate() {
            return isSeparate;
        }

        public void setIsSeparate(Long isSeparate) {
            this.isSeparate = isSeparate;
        }

        public Long getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(Long buyCount) {
            this.buyCount = buyCount;
        }

        public Long getBuyCountVip() {
            return buyCountVip;
        }

        public void setBuyCountVip(Long buyCountVip) {
            this.buyCountVip = buyCountVip;
        }

        public Long getBuyCountKubi() {
            return buyCountKubi;
        }

        public void setBuyCountKubi(Long buyCountKubi) {
            this.buyCountKubi = buyCountKubi;
        }

        public Long getBuyCountAudios() {
            return buyCountAudios;
        }

        public void setBuyCountAudios(Long buyCountAudios) {
            this.buyCountAudios = buyCountAudios;
        }

        public TransParamDTO getTransParam() {
            return transParam;
        }

        public void setTransParam(TransParamDTO transParam) {
            this.transParam = transParam;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Long getErrno() {
            return errno;
        }

        public void setErrno(Long errno) {
            this.errno = errno;
        }

        public List<RelateGoodsDTO> getRelateGoods() {
            return relateGoods;
        }

        public void setRelateGoods(List<RelateGoodsDTO> relateGoods) {
            this.relateGoods = relateGoods;
        }

        public static class InfoDTO {
            @JSONField(name = "duration")
            private Long duration;
            @JSONField(name = "filesize")
            private Long filesize;
            @JSONField(name = "bitrate")
            private Long bitrate;
            @JSONField(name = "extname")
            private String extname;
            @JSONField(name = "flag")
            private Long flag;
            @JSONField(name = "image")
            private String image;
            @JSONField(name = "imgsize")
            private List<Long> imgsize;
            @JSONField(name = "intro")
            private String intro;

            public Long getDuration() {
                return duration;
            }

            public void setDuration(Long duration) {
                this.duration = duration;
            }

            public Long getFilesize() {
                return filesize;
            }

            public void setFilesize(Long filesize) {
                this.filesize = filesize;
            }

            public Long getBitrate() {
                return bitrate;
            }

            public void setBitrate(Long bitrate) {
                this.bitrate = bitrate;
            }

            public String getExtname() {
                return extname;
            }

            public void setExtname(String extname) {
                this.extname = extname;
            }

            public Long getFlag() {
                return flag;
            }

            public void setFlag(Long flag) {
                this.flag = flag;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public List<Long> getImgsize() {
                return imgsize;
            }

            public void setImgsize(List<Long> imgsize) {
                this.imgsize = imgsize;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }
        }

        public static class TransParamDTO {
            @JSONField(name = "hash_offset")
            private HashOffsetDTO hashOffset;
            @JSONField(name = "musicpack_advance")
            private Long musicpackAdvance;
            @JSONField(name = "pay_block_tpl")
            private Long payBlockTpl;
            @JSONField(name = "display")
            private Long display;
            @JSONField(name = "display_rate")
            private Long displayRate;
            @JSONField(name = "appid_block")
            private String appidBlock;
            @JSONField(name = "cpy_grade")
            private Long cpyGrade;
            @JSONField(name = "cpy_level")
            private Long cpyLevel;
            @JSONField(name = "cid")
            private Long cid;
            @JSONField(name = "cpy_attr0")
            private Long cpyAttr0;
            @JSONField(name = "classmap")
            private ClassmapDTO classmap;
            @JSONField(name = "hash_multitrack")
            private String hashMultitrack;
            @JSONField(name = "qualitymap")
            private QualitymapDTO qualitymap;
            @JSONField(name = "language")
            private String language;
            @JSONField(name = "ipmap")
            private IpmapDTO ipmap;
            @JSONField(name = "ogg_128_hash")
            private String ogg128Hash;
            @JSONField(name = "ogg_128_filesize")
            private Long ogg128Filesize;
            @JSONField(name = "ogg_320_hash")
            private String ogg320Hash;
            @JSONField(name = "ogg_320_filesize")
            private Long ogg320Filesize;
            @JSONField(name = "union_cover")
            private String unionCover;

            public HashOffsetDTO getHashOffset() {
                return hashOffset;
            }

            public void setHashOffset(HashOffsetDTO hashOffset) {
                this.hashOffset = hashOffset;
            }

            public Long getMusicpackAdvance() {
                return musicpackAdvance;
            }

            public void setMusicpackAdvance(Long musicpackAdvance) {
                this.musicpackAdvance = musicpackAdvance;
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

            public Long getDisplayRate() {
                return displayRate;
            }

            public void setDisplayRate(Long displayRate) {
                this.displayRate = displayRate;
            }

            public String getAppidBlock() {
                return appidBlock;
            }

            public void setAppidBlock(String appidBlock) {
                this.appidBlock = appidBlock;
            }

            public Long getCpyGrade() {
                return cpyGrade;
            }

            public void setCpyGrade(Long cpyGrade) {
                this.cpyGrade = cpyGrade;
            }

            public Long getCpyLevel() {
                return cpyLevel;
            }

            public void setCpyLevel(Long cpyLevel) {
                this.cpyLevel = cpyLevel;
            }

            public Long getCid() {
                return cid;
            }

            public void setCid(Long cid) {
                this.cid = cid;
            }

            public Long getCpyAttr0() {
                return cpyAttr0;
            }

            public void setCpyAttr0(Long cpyAttr0) {
                this.cpyAttr0 = cpyAttr0;
            }

            public ClassmapDTO getClassmap() {
                return classmap;
            }

            public void setClassmap(ClassmapDTO classmap) {
                this.classmap = classmap;
            }

            public String getHashMultitrack() {
                return hashMultitrack;
            }

            public void setHashMultitrack(String hashMultitrack) {
                this.hashMultitrack = hashMultitrack;
            }

            public QualitymapDTO getQualitymap() {
                return qualitymap;
            }

            public void setQualitymap(QualitymapDTO qualitymap) {
                this.qualitymap = qualitymap;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public IpmapDTO getIpmap() {
                return ipmap;
            }

            public void setIpmap(IpmapDTO ipmap) {
                this.ipmap = ipmap;
            }

            public String getOgg128Hash() {
                return ogg128Hash;
            }

            public void setOgg128Hash(String ogg128Hash) {
                this.ogg128Hash = ogg128Hash;
            }

            public Long getOgg128Filesize() {
                return ogg128Filesize;
            }

            public void setOgg128Filesize(Long ogg128Filesize) {
                this.ogg128Filesize = ogg128Filesize;
            }

            public String getOgg320Hash() {
                return ogg320Hash;
            }

            public void setOgg320Hash(String ogg320Hash) {
                this.ogg320Hash = ogg320Hash;
            }

            public Long getOgg320Filesize() {
                return ogg320Filesize;
            }

            public void setOgg320Filesize(Long ogg320Filesize) {
                this.ogg320Filesize = ogg320Filesize;
            }

            public String getUnionCover() {
                return unionCover;
            }

            public void setUnionCover(String unionCover) {
                this.unionCover = unionCover;
            }

            public static class HashOffsetDTO {
                @JSONField(name = "start_byte")
                private Long startByte;
                @JSONField(name = "end_byte")
                private Long endByte;
                @JSONField(name = "start_ms")
                private Long startMs;
                @JSONField(name = "end_ms")
                private Long endMs;
                @JSONField(name = "offset_hash")
                private String offsetHash;
                @JSONField(name = "file_type")
                private Long fileType;
                @JSONField(name = "clip_hash")
                private String clipHash;

                public Long getStartByte() {
                    return startByte;
                }

                public void setStartByte(Long startByte) {
                    this.startByte = startByte;
                }

                public Long getEndByte() {
                    return endByte;
                }

                public void setEndByte(Long endByte) {
                    this.endByte = endByte;
                }

                public Long getStartMs() {
                    return startMs;
                }

                public void setStartMs(Long startMs) {
                    this.startMs = startMs;
                }

                public Long getEndMs() {
                    return endMs;
                }

                public void setEndMs(Long endMs) {
                    this.endMs = endMs;
                }

                public String getOffsetHash() {
                    return offsetHash;
                }

                public void setOffsetHash(String offsetHash) {
                    this.offsetHash = offsetHash;
                }

                public Long getFileType() {
                    return fileType;
                }

                public void setFileType(Long fileType) {
                    this.fileType = fileType;
                }

                public String getClipHash() {
                    return clipHash;
                }

                public void setClipHash(String clipHash) {
                    this.clipHash = clipHash;
                }
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
        }

        public static class RelateGoodsDTO {
            @JSONField(name = "type")
            private String type;
            @JSONField(name = "id")
            private Long id;
            @JSONField(name = "album_id")
            private String albumId;
            @JSONField(name = "recommend_album_id")
            private String recommendAlbumId;
            @JSONField(name = "album_audio_id")
            private Long albumAudioId;
            @JSONField(name = "hash")
            private String hash;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "singername")
            private String singername;
            @JSONField(name = "albumname")
            private String albumname;
            @JSONField(name = "level")
            private Long level;
            @JSONField(name = "quality")
            private String quality;
            @JSONField(name = "expire")
            private Long expire;
            @JSONField(name = "publish")
            private Long publish;
            @JSONField(name = "is_publish")
            private Long isPublish;
            @JSONField(name = "old_hide")
            private Long oldHide;
            @JSONField(name = "privilege")
            private Long privilege;
            @JSONField(name = "status")
            private Long status;
            @JSONField(name = "fail_process")
            private Long failProcess;
            @JSONField(name = "pay_type")
            private Long payType;
            @JSONField(name = "price")
            private Long price;
            @JSONField(name = "pkg_price")
            private Long pkgPrice;
            @JSONField(name = "topic_url")
            private String topicUrl;
            @JSONField(name = "topic_remark")
            private String topicRemark;
            @JSONField(name = "info")
            private InfoDTO info;
            @JSONField(name = "discount")
            private List<?> discount;
            @JSONField(name = "start_time")
            private String startTime;
            @JSONField(name = "end_time")
            private String endTime;
            @JSONField(name = "cd_url")
            private String cdUrl;
            @JSONField(name = "cid")
            private Long cid;
            @JSONField(name = "old_cpy")
            private Long oldCpy;
            @JSONField(name = "rebuy_pay_type")
            private Long rebuyPayType;
            @JSONField(name = "is_search_top")
            private Long isSearchTop;
            @JSONField(name = "pay_block_text")
            private String payBlockText;
            @JSONField(name = "is_separate")
            private Long isSeparate;
            @JSONField(name = "buy_count")
            private Long buyCount;
            @JSONField(name = "buy_count_vip")
            private Long buyCountVip;
            @JSONField(name = "buy_count_kubi")
            private Long buyCountKubi;
            @JSONField(name = "buy_count_audios")
            private Long buyCountAudios;
            @JSONField(name = "trans_param")
            private TransParamDTO transParam;
            @JSONField(name = "_msg")
            private String msg;
            @JSONField(name = "_errno")
            private Long errno;
            @JSONField(name = "relate_goods")
            private List<?> relateGoods;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getAlbumId() {
                return albumId;
            }

            public void setAlbumId(String albumId) {
                this.albumId = albumId;
            }

            public String getRecommendAlbumId() {
                return recommendAlbumId;
            }

            public void setRecommendAlbumId(String recommendAlbumId) {
                this.recommendAlbumId = recommendAlbumId;
            }

            public Long getAlbumAudioId() {
                return albumAudioId;
            }

            public void setAlbumAudioId(Long albumAudioId) {
                this.albumAudioId = albumAudioId;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSingername() {
                return singername;
            }

            public void setSingername(String singername) {
                this.singername = singername;
            }

            public String getAlbumname() {
                return albumname;
            }

            public void setAlbumname(String albumname) {
                this.albumname = albumname;
            }

            public Long getLevel() {
                return level;
            }

            public void setLevel(Long level) {
                this.level = level;
            }

            public String getQuality() {
                return quality;
            }

            public void setQuality(String quality) {
                this.quality = quality;
            }

            public Long getExpire() {
                return expire;
            }

            public void setExpire(Long expire) {
                this.expire = expire;
            }

            public Long getPublish() {
                return publish;
            }

            public void setPublish(Long publish) {
                this.publish = publish;
            }

            public Long getIsPublish() {
                return isPublish;
            }

            public void setIsPublish(Long isPublish) {
                this.isPublish = isPublish;
            }

            public Long getOldHide() {
                return oldHide;
            }

            public void setOldHide(Long oldHide) {
                this.oldHide = oldHide;
            }

            public Long getPrivilege() {
                return privilege;
            }

            public void setPrivilege(Long privilege) {
                this.privilege = privilege;
            }

            public Long getStatus() {
                return status;
            }

            public void setStatus(Long status) {
                this.status = status;
            }

            public Long getFailProcess() {
                return failProcess;
            }

            public void setFailProcess(Long failProcess) {
                this.failProcess = failProcess;
            }

            public Long getPayType() {
                return payType;
            }

            public void setPayType(Long payType) {
                this.payType = payType;
            }

            public Long getPrice() {
                return price;
            }

            public void setPrice(Long price) {
                this.price = price;
            }

            public Long getPkgPrice() {
                return pkgPrice;
            }

            public void setPkgPrice(Long pkgPrice) {
                this.pkgPrice = pkgPrice;
            }

            public String getTopicUrl() {
                return topicUrl;
            }

            public void setTopicUrl(String topicUrl) {
                this.topicUrl = topicUrl;
            }

            public String getTopicRemark() {
                return topicRemark;
            }

            public void setTopicRemark(String topicRemark) {
                this.topicRemark = topicRemark;
            }

            public InfoDTO getInfo() {
                return info;
            }

            public void setInfo(InfoDTO info) {
                this.info = info;
            }

            public List<?> getDiscount() {
                return discount;
            }

            public void setDiscount(List<?> discount) {
                this.discount = discount;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getCdUrl() {
                return cdUrl;
            }

            public void setCdUrl(String cdUrl) {
                this.cdUrl = cdUrl;
            }

            public Long getCid() {
                return cid;
            }

            public void setCid(Long cid) {
                this.cid = cid;
            }

            public Long getOldCpy() {
                return oldCpy;
            }

            public void setOldCpy(Long oldCpy) {
                this.oldCpy = oldCpy;
            }

            public Long getRebuyPayType() {
                return rebuyPayType;
            }

            public void setRebuyPayType(Long rebuyPayType) {
                this.rebuyPayType = rebuyPayType;
            }

            public Long getIsSearchTop() {
                return isSearchTop;
            }

            public void setIsSearchTop(Long isSearchTop) {
                this.isSearchTop = isSearchTop;
            }

            public String getPayBlockText() {
                return payBlockText;
            }

            public void setPayBlockText(String payBlockText) {
                this.payBlockText = payBlockText;
            }

            public Long getIsSeparate() {
                return isSeparate;
            }

            public void setIsSeparate(Long isSeparate) {
                this.isSeparate = isSeparate;
            }

            public Long getBuyCount() {
                return buyCount;
            }

            public void setBuyCount(Long buyCount) {
                this.buyCount = buyCount;
            }

            public Long getBuyCountVip() {
                return buyCountVip;
            }

            public void setBuyCountVip(Long buyCountVip) {
                this.buyCountVip = buyCountVip;
            }

            public Long getBuyCountKubi() {
                return buyCountKubi;
            }

            public void setBuyCountKubi(Long buyCountKubi) {
                this.buyCountKubi = buyCountKubi;
            }

            public Long getBuyCountAudios() {
                return buyCountAudios;
            }

            public void setBuyCountAudios(Long buyCountAudios) {
                this.buyCountAudios = buyCountAudios;
            }

            public TransParamDTO getTransParam() {
                return transParam;
            }

            public void setTransParam(TransParamDTO transParam) {
                this.transParam = transParam;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public Long getErrno() {
                return errno;
            }

            public void setErrno(Long errno) {
                this.errno = errno;
            }

            public List<?> getRelateGoods() {
                return relateGoods;
            }

            public void setRelateGoods(List<?> relateGoods) {
                this.relateGoods = relateGoods;
            }

            public static class InfoDTO {
                @JSONField(name = "duration")
                private Long duration;
                @JSONField(name = "filesize")
                private Long filesize;
                @JSONField(name = "bitrate")
                private Long bitrate;
                @JSONField(name = "extname")
                private String extname;
                @JSONField(name = "flag")
                private Long flag;
                @JSONField(name = "image")
                private String image;
                @JSONField(name = "imgsize")
                private List<Long> imgsize;
                @JSONField(name = "intro")
                private String intro;

                public Long getDuration() {
                    return duration;
                }

                public void setDuration(Long duration) {
                    this.duration = duration;
                }

                public Long getFilesize() {
                    return filesize;
                }

                public void setFilesize(Long filesize) {
                    this.filesize = filesize;
                }

                public Long getBitrate() {
                    return bitrate;
                }

                public void setBitrate(Long bitrate) {
                    this.bitrate = bitrate;
                }

                public String getExtname() {
                    return extname;
                }

                public void setExtname(String extname) {
                    this.extname = extname;
                }

                public Long getFlag() {
                    return flag;
                }

                public void setFlag(Long flag) {
                    this.flag = flag;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public List<Long> getImgsize() {
                    return imgsize;
                }

                public void setImgsize(List<Long> imgsize) {
                    this.imgsize = imgsize;
                }

                public String getIntro() {
                    return intro;
                }

                public void setIntro(String intro) {
                    this.intro = intro;
                }
            }

            public static class TransParamDTO {
                @JSONField(name = "hash_offset")
                private HashOffsetDTO hashOffset;
                @JSONField(name = "musicpack_advance")
                private Long musicpackAdvance;
                @JSONField(name = "pay_block_tpl")
                private Long payBlockTpl;
                @JSONField(name = "display")
                private Long display;
                @JSONField(name = "display_rate")
                private Long displayRate;
                @JSONField(name = "appid_block")
                private String appidBlock;
                @JSONField(name = "cpy_grade")
                private Long cpyGrade;
                @JSONField(name = "cpy_level")
                private Long cpyLevel;
                @JSONField(name = "cid")
                private Long cid;
                @JSONField(name = "cpy_attr0")
                private Long cpyAttr0;
                @JSONField(name = "classmap")
                private ClassmapDTO classmap;
                @JSONField(name = "hash_multitrack")
                private String hashMultitrack;
                @JSONField(name = "qualitymap")
                private QualitymapDTO qualitymap;
                @JSONField(name = "language")
                private String language;
                @JSONField(name = "ipmap")
                private IpmapDTO ipmap;
                @JSONField(name = "ogg_128_hash")
                private String ogg128Hash;
                @JSONField(name = "ogg_128_filesize")
                private Long ogg128Filesize;
                @JSONField(name = "ogg_320_hash")
                private String ogg320Hash;
                @JSONField(name = "ogg_320_filesize")
                private Long ogg320Filesize;
                @JSONField(name = "union_cover")
                private String unionCover;

                public HashOffsetDTO getHashOffset() {
                    return hashOffset;
                }

                public void setHashOffset(HashOffsetDTO hashOffset) {
                    this.hashOffset = hashOffset;
                }

                public Long getMusicpackAdvance() {
                    return musicpackAdvance;
                }

                public void setMusicpackAdvance(Long musicpackAdvance) {
                    this.musicpackAdvance = musicpackAdvance;
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

                public Long getDisplayRate() {
                    return displayRate;
                }

                public void setDisplayRate(Long displayRate) {
                    this.displayRate = displayRate;
                }

                public String getAppidBlock() {
                    return appidBlock;
                }

                public void setAppidBlock(String appidBlock) {
                    this.appidBlock = appidBlock;
                }

                public Long getCpyGrade() {
                    return cpyGrade;
                }

                public void setCpyGrade(Long cpyGrade) {
                    this.cpyGrade = cpyGrade;
                }

                public Long getCpyLevel() {
                    return cpyLevel;
                }

                public void setCpyLevel(Long cpyLevel) {
                    this.cpyLevel = cpyLevel;
                }

                public Long getCid() {
                    return cid;
                }

                public void setCid(Long cid) {
                    this.cid = cid;
                }

                public Long getCpyAttr0() {
                    return cpyAttr0;
                }

                public void setCpyAttr0(Long cpyAttr0) {
                    this.cpyAttr0 = cpyAttr0;
                }

                public ClassmapDTO getClassmap() {
                    return classmap;
                }

                public void setClassmap(ClassmapDTO classmap) {
                    this.classmap = classmap;
                }

                public String getHashMultitrack() {
                    return hashMultitrack;
                }

                public void setHashMultitrack(String hashMultitrack) {
                    this.hashMultitrack = hashMultitrack;
                }

                public QualitymapDTO getQualitymap() {
                    return qualitymap;
                }

                public void setQualitymap(QualitymapDTO qualitymap) {
                    this.qualitymap = qualitymap;
                }

                public String getLanguage() {
                    return language;
                }

                public void setLanguage(String language) {
                    this.language = language;
                }

                public IpmapDTO getIpmap() {
                    return ipmap;
                }

                public void setIpmap(IpmapDTO ipmap) {
                    this.ipmap = ipmap;
                }

                public String getOgg128Hash() {
                    return ogg128Hash;
                }

                public void setOgg128Hash(String ogg128Hash) {
                    this.ogg128Hash = ogg128Hash;
                }

                public Long getOgg128Filesize() {
                    return ogg128Filesize;
                }

                public void setOgg128Filesize(Long ogg128Filesize) {
                    this.ogg128Filesize = ogg128Filesize;
                }

                public String getOgg320Hash() {
                    return ogg320Hash;
                }

                public void setOgg320Hash(String ogg320Hash) {
                    this.ogg320Hash = ogg320Hash;
                }

                public Long getOgg320Filesize() {
                    return ogg320Filesize;
                }

                public void setOgg320Filesize(Long ogg320Filesize) {
                    this.ogg320Filesize = ogg320Filesize;
                }

                public String getUnionCover() {
                    return unionCover;
                }

                public void setUnionCover(String unionCover) {
                    this.unionCover = unionCover;
                }

                public static class HashOffsetDTO {
                    @JSONField(name = "start_byte")
                    private Long startByte;
                    @JSONField(name = "end_byte")
                    private Long endByte;
                    @JSONField(name = "start_ms")
                    private Long startMs;
                    @JSONField(name = "end_ms")
                    private Long endMs;
                    @JSONField(name = "offset_hash")
                    private String offsetHash;
                    @JSONField(name = "file_type")
                    private Long fileType;
                    @JSONField(name = "clip_hash")
                    private String clipHash;

                    public Long getStartByte() {
                        return startByte;
                    }

                    public void setStartByte(Long startByte) {
                        this.startByte = startByte;
                    }

                    public Long getEndByte() {
                        return endByte;
                    }

                    public void setEndByte(Long endByte) {
                        this.endByte = endByte;
                    }

                    public Long getStartMs() {
                        return startMs;
                    }

                    public void setStartMs(Long startMs) {
                        this.startMs = startMs;
                    }

                    public Long getEndMs() {
                        return endMs;
                    }

                    public void setEndMs(Long endMs) {
                        this.endMs = endMs;
                    }

                    public String getOffsetHash() {
                        return offsetHash;
                    }

                    public void setOffsetHash(String offsetHash) {
                        this.offsetHash = offsetHash;
                    }

                    public Long getFileType() {
                        return fileType;
                    }

                    public void setFileType(Long fileType) {
                        this.fileType = fileType;
                    }

                    public String getClipHash() {
                        return clipHash;
                    }

                    public void setClipHash(String clipHash) {
                        this.clipHash = clipHash;
                    }
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
            }
        }
    }
}
