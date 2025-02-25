package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname SearchResult
 * @Description 搜索返回值
 * @Version 1.0.0
 * @Date 2025/2/6 10:32
 * @Created by SQ
 */

public class SearchMusicResult {


    @JSONField(name = "error_msg")
    private String errorMsg;
    @JSONField(name = "data")
    private DataDTO data;
    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "error_code")
    private Long errorCode;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

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

    public static class DataDTO {
        @JSONField(name = "correctiontip")
        private String correctiontip;
        @JSONField(name = "pagesize")
        private Long pagesize;
        @JSONField(name = "page")
        private Long page;
        @JSONField(name = "correctiontype")
        private Long correctiontype;
        @JSONField(name = "correctionrelate")
        private String correctionrelate;
        @JSONField(name = "total")
        private Long total;
        @JSONField(name = "lists")
        private List<ListsDTO> lists;
        @JSONField(name = "sec_aggre")
        private SecAggreDTO secAggre;
        @JSONField(name = "aggregation")
        private AggregationDTO aggregation;
        @JSONField(name = "size")
        private Long size;
        @JSONField(name = "chinesecount")
        private Long chinesecount;
        @JSONField(name = "searchfull")
        private Long searchfull;
        @JSONField(name = "allowerr")
        private Long allowerr;
        @JSONField(name = "correctionsubject")
        private String correctionsubject;
        @JSONField(name = "AlgPath")
        private String algPath;
        @JSONField(name = "sec_aggre_v2")
        private List<?> secAggreV2;
        @JSONField(name = "correctionforce")
        private Long correctionforce;
        @JSONField(name = "istag")
        private Long istag;
        @JSONField(name = "from")
        private Long from;
        @JSONField(name = "istagresult")
        private Long istagresult;
        @JSONField(name = "subjecttype")
        private Long subjecttype;
        @JSONField(name = "sectag_info")
        private SectagInfoDTO sectagInfo;
        @JSONField(name = "isshareresult")
        private Long isshareresult;

        public String getCorrectiontip() {
            return correctiontip;
        }

        public void setCorrectiontip(String correctiontip) {
            this.correctiontip = correctiontip;
        }

        public Long getPagesize() {
            return pagesize;
        }

        public void setPagesize(Long pagesize) {
            this.pagesize = pagesize;
        }

        public Long getPage() {
            return page;
        }

        public void setPage(Long page) {
            this.page = page;
        }

        public Long getCorrectiontype() {
            return correctiontype;
        }

        public void setCorrectiontype(Long correctiontype) {
            this.correctiontype = correctiontype;
        }

        public String getCorrectionrelate() {
            return correctionrelate;
        }

        public void setCorrectionrelate(String correctionrelate) {
            this.correctionrelate = correctionrelate;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public List<ListsDTO> getLists() {
            return lists;
        }

        public void setLists(List<ListsDTO> lists) {
            this.lists = lists;
        }

        public SecAggreDTO getSecAggre() {
            return secAggre;
        }

        public void setSecAggre(SecAggreDTO secAggre) {
            this.secAggre = secAggre;
        }

        public AggregationDTO getAggregation() {
            return aggregation;
        }

        public void setAggregation(AggregationDTO aggregation) {
            this.aggregation = aggregation;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Long getChinesecount() {
            return chinesecount;
        }

        public void setChinesecount(Long chinesecount) {
            this.chinesecount = chinesecount;
        }

        public Long getSearchfull() {
            return searchfull;
        }

        public void setSearchfull(Long searchfull) {
            this.searchfull = searchfull;
        }

        public Long getAllowerr() {
            return allowerr;
        }

        public void setAllowerr(Long allowerr) {
            this.allowerr = allowerr;
        }

        public String getCorrectionsubject() {
            return correctionsubject;
        }

        public void setCorrectionsubject(String correctionsubject) {
            this.correctionsubject = correctionsubject;
        }

        public String getAlgPath() {
            return algPath;
        }

        public void setAlgPath(String algPath) {
            this.algPath = algPath;
        }

        public List<?> getSecAggreV2() {
            return secAggreV2;
        }

        public void setSecAggreV2(List<?> secAggreV2) {
            this.secAggreV2 = secAggreV2;
        }

        public Long getCorrectionforce() {
            return correctionforce;
        }

        public void setCorrectionforce(Long correctionforce) {
            this.correctionforce = correctionforce;
        }

        public Long getIstag() {
            return istag;
        }

        public void setIstag(Long istag) {
            this.istag = istag;
        }

        public Long getFrom() {
            return from;
        }

        public void setFrom(Long from) {
            this.from = from;
        }

        public Long getIstagresult() {
            return istagresult;
        }

        public void setIstagresult(Long istagresult) {
            this.istagresult = istagresult;
        }

        public Long getSubjecttype() {
            return subjecttype;
        }

        public void setSubjecttype(Long subjecttype) {
            this.subjecttype = subjecttype;
        }

        public SectagInfoDTO getSectagInfo() {
            return sectagInfo;
        }

        public void setSectagInfo(SectagInfoDTO sectagInfo) {
            this.sectagInfo = sectagInfo;
        }

        public Long getIsshareresult() {
            return isshareresult;
        }

        public void setIsshareresult(Long isshareresult) {
            this.isshareresult = isshareresult;
        }

        public static class SecAggreDTO {
        }

        public static class AggregationDTO {
        }

        public static class SectagInfoDTO {
            @JSONField(name = "is_sectag")
            private Long isSectag;

            public Long getIsSectag() {
                return isSectag;
            }

            public void setIsSectag(Long isSectag) {
                this.isSectag = isSectag;
            }
        }

        public static class ListsDTO {
            @JSONField(name = "SQFileHash")
            private String sQFileHash;
            @JSONField(name = "PublishTime")
            private String publishTime;
            @JSONField(name = "Audioid")
            private Long audioid;
            @JSONField(name = "SuperDuration")
            private Long superDuration;
            @JSONField(name = "OldCpy")
            private Long oldCpy;
            @JSONField(name = "PublishAge")
            private Long publishAge;
            @JSONField(name = "HQBitrate")
            private Long hQBitrate;
            @JSONField(name = "PayType")
            private Long payType;
            @JSONField(name = "TagContent")
            private String tagContent;
            @JSONField(name = "Accompany")
            private Long accompany;
            @JSONField(name = "SingerName")
            private String singerName;
            @JSONField(name = "HQPrivilege")
            private Long hQPrivilege;
            @JSONField(name = "TopicRemark")
            private String topicRemark;
            @JSONField(name = "OriOtherName")
            private String oriOtherName;
            @JSONField(name = "ShowingFlag")
            private Long showingFlag;
            @JSONField(name = "Source")
            private String source;
            @JSONField(name = "SQFileSize")
            private Long sQFileSize;
            @JSONField(name = "AlbumAux")
            private String albumAux;
            @JSONField(name = "HQDuration")
            private Long hQDuration;
            @JSONField(name = "Image")
            private String image;
            @JSONField(name = "HQPayType")
            private Long hQPayType;
            @JSONField(name = "mvdata")
            private List<MvdataDTO> mvdata;
            @JSONField(name = "M4aSize")
            private Long m4aSize;
            @JSONField(name = "HeatLevel")
            private Long heatLevel;
            @JSONField(name = "SQPkgPrice")
            private Long sQPkgPrice;
            @JSONField(name = "trans_param")
            private TransParamDTO transParam;
            @JSONField(name = "UploaderContent")
            private String uploaderContent;
            @JSONField(name = "FileSize")
            private Long fileSize;
            @JSONField(name = "IsOriginal")
            private Long isOriginal;
            @JSONField(name = "FileHash")
            private String fileHash;
            @JSONField(name = "FoldType")
            private Long foldType;
            @JSONField(name = "Grp")
            private List<GrpDTO> grp;
            @JSONField(name = "ID")
            private String id;
            @JSONField(name = "MvTrac")
            private Long mvTrac;
            @JSONField(name = "isPrepublish")
            private Long isPrepublish;
            @JSONField(name = "Type")
            private String type;
            @JSONField(name = "Bitrate")
            private Long bitrate;
            @JSONField(name = "SQPrice")
            private Long sQPrice;
            @JSONField(name = "Auxiliary")
            private String auxiliary;
            @JSONField(name = "ExtName")
            private String extName;
            @JSONField(name = "ASQPrivilege")
            private Long aSQPrivilege;
            @JSONField(name = "PkgPrice")
            private Long pkgPrice;
            @JSONField(name = "AlbumPrivilege")
            private Long albumPrivilege;
            @JSONField(name = "AlbumID")
            private String albumID;
            @JSONField(name = "Category")
            private Long category;
            @JSONField(name = "SuperExtName")
            private String superExtName;
            @JSONField(name = "AlbumName")
            private String albumName;
            @JSONField(name = "OtherName")
            private String otherName;
            @JSONField(name = "SongName")
            private String songName;
            @JSONField(name = "Res")
            private ResDTO res;
            @JSONField(name = "AudioCdn")
            private Long audioCdn;
            @JSONField(name = "SourceID")
            private Long sourceID;
            @JSONField(name = "SQDuration")
            private Long sQDuration;
            @JSONField(name = "HQFileSize")
            private Long hQFileSize;
            @JSONField(name = "vvid")
            private String vvid;
            @JSONField(name = "MixSongID")
            private String mixSongID;
            @JSONField(name = "SQPayType")
            private Long sQPayType;
            @JSONField(name = "ResBitrate")
            private Long resBitrate;
            @JSONField(name = "SuperBitrate")
            private Long superBitrate;
            @JSONField(name = "HQPrice")
            private Long hQPrice;
            @JSONField(name = "Suffix")
            private String suffix;
            @JSONField(name = "HQFailProcess")
            private Long hQFailProcess;
            @JSONField(name = "mvTotal")
            private Long mvTotal;
            @JSONField(name = "SongLabel")
            private String songLabel;
            @JSONField(name = "ResDuration")
            private Long resDuration;
            @JSONField(name = "HiFiQuality")
            private Long hiFiQuality;
            @JSONField(name = "Singers")
            private List<SingersDTO> singers;
            @JSONField(name = "SingerId")
            private List<Long> singerId;
            @JSONField(name = "HQExtName")
            private String hQExtName;
            @JSONField(name = "ResFileHash")
            private String resFileHash;
            @JSONField(name = "MatchFlag")
            private Long matchFlag;
            @JSONField(name = "Scid")
            private Long scid;
            @JSONField(name = "SuperFileHash")
            private String superFileHash;
            @JSONField(name = "QualityLevel")
            private Long qualityLevel;
            @JSONField(name = "OriSongName")
            private String oriSongName;
            @JSONField(name = "HasAlbum")
            private Long hasAlbum;
            @JSONField(name = "MvType")
            private Long mvType;
            @JSONField(name = "SuperFileSize")
            private Long superFileSize;
            @JSONField(name = "MvHash")
            private String mvHash;
            @JSONField(name = "FailProcess")
            private Long failProcess;
            @JSONField(name = "SQBitrate")
            private Long sQBitrate;
            @JSONField(name = "SQExtName")
            private String sQExtName;
            @JSONField(name = "PublishDate")
            private String publishDate;
            @JSONField(name = "HQFileHash")
            private String hQFileHash;
            @JSONField(name = "TopicUrl")
            private String topicUrl;
            @JSONField(name = "RankId")
            private Long rankId;
            @JSONField(name = "TagDetails")
            private List<TagDetailsDTO> tagDetails;
            @JSONField(name = "Privilege")
            private Long privilege;
            @JSONField(name = "PrepublishInfo")
            private PrepublishInfoDTO prepublishInfo;
            @JSONField(name = "HQPkgPrice")
            private Long hQPkgPrice;
            @JSONField(name = "OwnerCount")
            private Long ownerCount;
            @JSONField(name = "Uploader")
            private String uploader;
            @JSONField(name = "Duration")
            private Long duration;
            @JSONField(name = "SQFailProcess")
            private Long sQFailProcess;
            @JSONField(name = "TopID")
            private Long topID;
            @JSONField(name = "A320Privilege")
            private Long a320Privilege;
            @JSONField(name = "FileName")
            private String fileName;
            @JSONField(name = "ResFileSize")
            private Long resFileSize;
            @JSONField(name = "SQPrivilege")
            private Long sQPrivilege;
            @JSONField(name = "Price")
            private Long price;
            @JSONField(name = "recommend_type")
            private Long recommendType;
            @JSONField(name = "Publish")
            private Long publish;
            @JSONField(name = "bitflag")
            private Long bitflag;

            public String getSQFileHash() {
                return sQFileHash;
            }

            public void setSQFileHash(String sQFileHash) {
                this.sQFileHash = sQFileHash;
            }

            public String getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(String publishTime) {
                this.publishTime = publishTime;
            }

            public Long getAudioid() {
                return audioid;
            }

            public void setAudioid(Long audioid) {
                this.audioid = audioid;
            }

            public Long getSuperDuration() {
                return superDuration;
            }

            public void setSuperDuration(Long superDuration) {
                this.superDuration = superDuration;
            }

            public Long getOldCpy() {
                return oldCpy;
            }

            public void setOldCpy(Long oldCpy) {
                this.oldCpy = oldCpy;
            }

            public Long getPublishAge() {
                return publishAge;
            }

            public void setPublishAge(Long publishAge) {
                this.publishAge = publishAge;
            }

            public Long getHQBitrate() {
                return hQBitrate;
            }

            public void setHQBitrate(Long hQBitrate) {
                this.hQBitrate = hQBitrate;
            }

            public Long getPayType() {
                return payType;
            }

            public void setPayType(Long payType) {
                this.payType = payType;
            }

            public String getTagContent() {
                return tagContent;
            }

            public void setTagContent(String tagContent) {
                this.tagContent = tagContent;
            }

            public Long getAccompany() {
                return accompany;
            }

            public void setAccompany(Long accompany) {
                this.accompany = accompany;
            }

            public String getSingerName() {
                return singerName;
            }

            public void setSingerName(String singerName) {
                this.singerName = singerName;
            }

            public Long getHQPrivilege() {
                return hQPrivilege;
            }

            public void setHQPrivilege(Long hQPrivilege) {
                this.hQPrivilege = hQPrivilege;
            }

            public String getTopicRemark() {
                return topicRemark;
            }

            public void setTopicRemark(String topicRemark) {
                this.topicRemark = topicRemark;
            }

            public String getOriOtherName() {
                return oriOtherName;
            }

            public void setOriOtherName(String oriOtherName) {
                this.oriOtherName = oriOtherName;
            }

            public Long getShowingFlag() {
                return showingFlag;
            }

            public void setShowingFlag(Long showingFlag) {
                this.showingFlag = showingFlag;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public Long getSQFileSize() {
                return sQFileSize;
            }

            public void setSQFileSize(Long sQFileSize) {
                this.sQFileSize = sQFileSize;
            }

            public String getAlbumAux() {
                return albumAux;
            }

            public void setAlbumAux(String albumAux) {
                this.albumAux = albumAux;
            }

            public Long getHQDuration() {
                return hQDuration;
            }

            public void setHQDuration(Long hQDuration) {
                this.hQDuration = hQDuration;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public Long getHQPayType() {
                return hQPayType;
            }

            public void setHQPayType(Long hQPayType) {
                this.hQPayType = hQPayType;
            }

            public List<MvdataDTO> getMvdata() {
                return mvdata;
            }

            public void setMvdata(List<MvdataDTO> mvdata) {
                this.mvdata = mvdata;
            }

            public Long getM4aSize() {
                return m4aSize;
            }

            public void setM4aSize(Long m4aSize) {
                this.m4aSize = m4aSize;
            }

            public Long getHeatLevel() {
                return heatLevel;
            }

            public void setHeatLevel(Long heatLevel) {
                this.heatLevel = heatLevel;
            }

            public Long getSQPkgPrice() {
                return sQPkgPrice;
            }

            public void setSQPkgPrice(Long sQPkgPrice) {
                this.sQPkgPrice = sQPkgPrice;
            }

            public TransParamDTO getTransParam() {
                return transParam;
            }

            public void setTransParam(TransParamDTO transParam) {
                this.transParam = transParam;
            }

            public String getUploaderContent() {
                return uploaderContent;
            }

            public void setUploaderContent(String uploaderContent) {
                this.uploaderContent = uploaderContent;
            }

            public Long getFileSize() {
                return fileSize;
            }

            public void setFileSize(Long fileSize) {
                this.fileSize = fileSize;
            }

            public Long getIsOriginal() {
                return isOriginal;
            }

            public void setIsOriginal(Long isOriginal) {
                this.isOriginal = isOriginal;
            }

            public String getFileHash() {
                return fileHash;
            }

            public void setFileHash(String fileHash) {
                this.fileHash = fileHash;
            }

            public Long getFoldType() {
                return foldType;
            }

            public void setFoldType(Long foldType) {
                this.foldType = foldType;
            }

            public List<GrpDTO> getGrp() {
                return grp;
            }

            public void setGrp(List<GrpDTO> grp) {
                this.grp = grp;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Long getMvTrac() {
                return mvTrac;
            }

            public void setMvTrac(Long mvTrac) {
                this.mvTrac = mvTrac;
            }

            public Long getIsPrepublish() {
                return isPrepublish;
            }

            public void setIsPrepublish(Long isPrepublish) {
                this.isPrepublish = isPrepublish;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Long getBitrate() {
                return bitrate;
            }

            public void setBitrate(Long bitrate) {
                this.bitrate = bitrate;
            }

            public Long getSQPrice() {
                return sQPrice;
            }

            public void setSQPrice(Long sQPrice) {
                this.sQPrice = sQPrice;
            }

            public String getAuxiliary() {
                return auxiliary;
            }

            public void setAuxiliary(String auxiliary) {
                this.auxiliary = auxiliary;
            }

            public String getExtName() {
                return extName;
            }

            public void setExtName(String extName) {
                this.extName = extName;
            }

            public Long getASQPrivilege() {
                return aSQPrivilege;
            }

            public void setASQPrivilege(Long aSQPrivilege) {
                this.aSQPrivilege = aSQPrivilege;
            }

            public Long getPkgPrice() {
                return pkgPrice;
            }

            public void setPkgPrice(Long pkgPrice) {
                this.pkgPrice = pkgPrice;
            }

            public Long getAlbumPrivilege() {
                return albumPrivilege;
            }

            public void setAlbumPrivilege(Long albumPrivilege) {
                this.albumPrivilege = albumPrivilege;
            }

            public String getAlbumID() {
                return albumID;
            }

            public void setAlbumID(String albumID) {
                this.albumID = albumID;
            }

            public Long getCategory() {
                return category;
            }

            public void setCategory(Long category) {
                this.category = category;
            }

            public String getSuperExtName() {
                return superExtName;
            }

            public void setSuperExtName(String superExtName) {
                this.superExtName = superExtName;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
            }

            public String getOtherName() {
                return otherName;
            }

            public void setOtherName(String otherName) {
                this.otherName = otherName;
            }

            public String getSongName() {
                return songName;
            }

            public void setSongName(String songName) {
                this.songName = songName;
            }

            public ResDTO getRes() {
                return res;
            }

            public void setRes(ResDTO res) {
                this.res = res;
            }

            public Long getAudioCdn() {
                return audioCdn;
            }

            public void setAudioCdn(Long audioCdn) {
                this.audioCdn = audioCdn;
            }

            public Long getSourceID() {
                return sourceID;
            }

            public void setSourceID(Long sourceID) {
                this.sourceID = sourceID;
            }

            public Long getSQDuration() {
                return sQDuration;
            }

            public void setSQDuration(Long sQDuration) {
                this.sQDuration = sQDuration;
            }

            public Long getHQFileSize() {
                return hQFileSize;
            }

            public void setHQFileSize(Long hQFileSize) {
                this.hQFileSize = hQFileSize;
            }

            public String getVvid() {
                return vvid;
            }

            public void setVvid(String vvid) {
                this.vvid = vvid;
            }

            public String getMixSongID() {
                return mixSongID;
            }

            public void setMixSongID(String mixSongID) {
                this.mixSongID = mixSongID;
            }

            public Long getSQPayType() {
                return sQPayType;
            }

            public void setSQPayType(Long sQPayType) {
                this.sQPayType = sQPayType;
            }

            public Long getResBitrate() {
                return resBitrate;
            }

            public void setResBitrate(Long resBitrate) {
                this.resBitrate = resBitrate;
            }

            public Long getSuperBitrate() {
                return superBitrate;
            }

            public void setSuperBitrate(Long superBitrate) {
                this.superBitrate = superBitrate;
            }

            public Long getHQPrice() {
                return hQPrice;
            }

            public void setHQPrice(Long hQPrice) {
                this.hQPrice = hQPrice;
            }

            public String getSuffix() {
                return suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            public Long getHQFailProcess() {
                return hQFailProcess;
            }

            public void setHQFailProcess(Long hQFailProcess) {
                this.hQFailProcess = hQFailProcess;
            }

            public Long getMvTotal() {
                return mvTotal;
            }

            public void setMvTotal(Long mvTotal) {
                this.mvTotal = mvTotal;
            }

            public String getSongLabel() {
                return songLabel;
            }

            public void setSongLabel(String songLabel) {
                this.songLabel = songLabel;
            }

            public Long getResDuration() {
                return resDuration;
            }

            public void setResDuration(Long resDuration) {
                this.resDuration = resDuration;
            }

            public Long getHiFiQuality() {
                return hiFiQuality;
            }

            public void setHiFiQuality(Long hiFiQuality) {
                this.hiFiQuality = hiFiQuality;
            }

            public List<SingersDTO> getSingers() {
                return singers;
            }

            public void setSingers(List<SingersDTO> singers) {
                this.singers = singers;
            }

            public List<Long> getSingerId() {
                return singerId;
            }

            public void setSingerId(List<Long> singerId) {
                this.singerId = singerId;
            }

            public String getHQExtName() {
                return hQExtName;
            }

            public void setHQExtName(String hQExtName) {
                this.hQExtName = hQExtName;
            }

            public String getResFileHash() {
                return resFileHash;
            }

            public void setResFileHash(String resFileHash) {
                this.resFileHash = resFileHash;
            }

            public Long getMatchFlag() {
                return matchFlag;
            }

            public void setMatchFlag(Long matchFlag) {
                this.matchFlag = matchFlag;
            }

            public Long getScid() {
                return scid;
            }

            public void setScid(Long scid) {
                this.scid = scid;
            }

            public String getSuperFileHash() {
                return superFileHash;
            }

            public void setSuperFileHash(String superFileHash) {
                this.superFileHash = superFileHash;
            }

            public Long getQualityLevel() {
                return qualityLevel;
            }

            public void setQualityLevel(Long qualityLevel) {
                this.qualityLevel = qualityLevel;
            }

            public String getOriSongName() {
                return oriSongName;
            }

            public void setOriSongName(String oriSongName) {
                this.oriSongName = oriSongName;
            }

            public Long getHasAlbum() {
                return hasAlbum;
            }

            public void setHasAlbum(Long hasAlbum) {
                this.hasAlbum = hasAlbum;
            }

            public Long getMvType() {
                return mvType;
            }

            public void setMvType(Long mvType) {
                this.mvType = mvType;
            }

            public Long getSuperFileSize() {
                return superFileSize;
            }

            public void setSuperFileSize(Long superFileSize) {
                this.superFileSize = superFileSize;
            }

            public String getMvHash() {
                return mvHash;
            }

            public void setMvHash(String mvHash) {
                this.mvHash = mvHash;
            }

            public Long getFailProcess() {
                return failProcess;
            }

            public void setFailProcess(Long failProcess) {
                this.failProcess = failProcess;
            }

            public Long getSQBitrate() {
                return sQBitrate;
            }

            public void setSQBitrate(Long sQBitrate) {
                this.sQBitrate = sQBitrate;
            }

            public String getSQExtName() {
                return sQExtName;
            }

            public void setSQExtName(String sQExtName) {
                this.sQExtName = sQExtName;
            }

            public String getPublishDate() {
                return publishDate;
            }

            public void setPublishDate(String publishDate) {
                this.publishDate = publishDate;
            }

            public String getHQFileHash() {
                return hQFileHash;
            }

            public void setHQFileHash(String hQFileHash) {
                this.hQFileHash = hQFileHash;
            }

            public String getTopicUrl() {
                return topicUrl;
            }

            public void setTopicUrl(String topicUrl) {
                this.topicUrl = topicUrl;
            }

            public Long getRankId() {
                return rankId;
            }

            public void setRankId(Long rankId) {
                this.rankId = rankId;
            }

            public List<TagDetailsDTO> getTagDetails() {
                return tagDetails;
            }

            public void setTagDetails(List<TagDetailsDTO> tagDetails) {
                this.tagDetails = tagDetails;
            }

            public Long getPrivilege() {
                return privilege;
            }

            public void setPrivilege(Long privilege) {
                this.privilege = privilege;
            }

            public PrepublishInfoDTO getPrepublishInfo() {
                return prepublishInfo;
            }

            public void setPrepublishInfo(PrepublishInfoDTO prepublishInfo) {
                this.prepublishInfo = prepublishInfo;
            }

            public Long getHQPkgPrice() {
                return hQPkgPrice;
            }

            public void setHQPkgPrice(Long hQPkgPrice) {
                this.hQPkgPrice = hQPkgPrice;
            }

            public Long getOwnerCount() {
                return ownerCount;
            }

            public void setOwnerCount(Long ownerCount) {
                this.ownerCount = ownerCount;
            }

            public String getUploader() {
                return uploader;
            }

            public void setUploader(String uploader) {
                this.uploader = uploader;
            }

            public Long getDuration() {
                return duration;
            }

            public void setDuration(Long duration) {
                this.duration = duration;
            }

            public Long getSQFailProcess() {
                return sQFailProcess;
            }

            public void setSQFailProcess(Long sQFailProcess) {
                this.sQFailProcess = sQFailProcess;
            }

            public Long getTopID() {
                return topID;
            }

            public void setTopID(Long topID) {
                this.topID = topID;
            }

            public Long getA320Privilege() {
                return a320Privilege;
            }

            public void setA320Privilege(Long a320Privilege) {
                this.a320Privilege = a320Privilege;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public Long getResFileSize() {
                return resFileSize;
            }

            public void setResFileSize(Long resFileSize) {
                this.resFileSize = resFileSize;
            }

            public Long getSQPrivilege() {
                return sQPrivilege;
            }

            public void setSQPrivilege(Long sQPrivilege) {
                this.sQPrivilege = sQPrivilege;
            }

            public Long getPrice() {
                return price;
            }

            public void setPrice(Long price) {
                this.price = price;
            }

            public Long getRecommendType() {
                return recommendType;
            }

            public void setRecommendType(Long recommendType) {
                this.recommendType = recommendType;
            }

            public Long getPublish() {
                return publish;
            }

            public void setPublish(Long publish) {
                this.publish = publish;
            }

            public Long getBitflag() {
                return bitflag;
            }

            public void setBitflag(Long bitflag) {
                this.bitflag = bitflag;
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
                @JSONField(name = "ogg_128_filesize")
                private Long ogg128Filesize;
                @JSONField(name = "display_rate")
                private Long displayRate;
                @JSONField(name = "union_cover")
                private String unionCover;
                @JSONField(name = "qualitymap")
                private QualitymapDTO qualitymap;
                @JSONField(name = "ogg_320_filesize")
                private Long ogg320Filesize;
                @JSONField(name = "ogg_320_hash")
                private String ogg320Hash;
                @JSONField(name = "cid")
                private Long cid;
                @JSONField(name = "cpy_grade")
                private Long cpyGrade;
                @JSONField(name = "display")
                private Long display;
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

                public Long getOgg128Filesize() {
                    return ogg128Filesize;
                }

                public void setOgg128Filesize(Long ogg128Filesize) {
                    this.ogg128Filesize = ogg128Filesize;
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

                public QualitymapDTO getQualitymap() {
                    return qualitymap;
                }

                public void setQualitymap(QualitymapDTO qualitymap) {
                    this.qualitymap = qualitymap;
                }

                public Long getOgg320Filesize() {
                    return ogg320Filesize;
                }

                public void setOgg320Filesize(Long ogg320Filesize) {
                    this.ogg320Filesize = ogg320Filesize;
                }

                public String getOgg320Hash() {
                    return ogg320Hash;
                }

                public void setOgg320Hash(String ogg320Hash) {
                    this.ogg320Hash = ogg320Hash;
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

                public Long getDisplay() {
                    return display;
                }

                public void setDisplay(Long display) {
                    this.display = display;
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

            public static class ResDTO {
                @JSONField(name = "PkgPrice")
                private Long pkgPrice;
                @JSONField(name = "Privilege")
                private Long privilege;
                @JSONField(name = "PayType")
                private Long payType;
                @JSONField(name = "Price")
                private Long price;
                @JSONField(name = "FailProcess")
                private Long failProcess;

                public Long getPkgPrice() {
                    return pkgPrice;
                }

                public void setPkgPrice(Long pkgPrice) {
                    this.pkgPrice = pkgPrice;
                }

                public Long getPrivilege() {
                    return privilege;
                }

                public void setPrivilege(Long privilege) {
                    this.privilege = privilege;
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

                public Long getFailProcess() {
                    return failProcess;
                }

                public void setFailProcess(Long failProcess) {
                    this.failProcess = failProcess;
                }
            }

            public static class PrepublishInfoDTO {
                @JSONField(name = "ReserveCount")
                private Long reserveCount;
                @JSONField(name = "DisplayTime")
                private String displayTime;
                @JSONField(name = "Id")
                private Long id;
                @JSONField(name = "PublishTime")
                private String publishTime;

                public Long getReserveCount() {
                    return reserveCount;
                }

                public void setReserveCount(Long reserveCount) {
                    this.reserveCount = reserveCount;
                }

                public String getDisplayTime() {
                    return displayTime;
                }

                public void setDisplayTime(String displayTime) {
                    this.displayTime = displayTime;
                }

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getPublishTime() {
                    return publishTime;
                }

                public void setPublishTime(String publishTime) {
                    this.publishTime = publishTime;
                }
            }

            public static class MvdataDTO {
                @JSONField(name = "id")
                private String id;
                @JSONField(name = "trk")
                private String trk;
                @JSONField(name = "hash")
                private String hash;
                @JSONField(name = "typ")
                private Long typ;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public Long getTyp() {
                    return typ;
                }

                public void setTyp(Long typ) {
                    this.typ = typ;
                }
            }

            public static class GrpDTO {
                @JSONField(name = "SQFileHash")
                private String sQFileHash;
                @JSONField(name = "PublishTime")
                private String publishTime;
                @JSONField(name = "Audioid")
                private Long audioid;
                @JSONField(name = "SuperDuration")
                private Long superDuration;
                @JSONField(name = "OldCpy")
                private Long oldCpy;
                @JSONField(name = "PublishAge")
                private Long publishAge;
                @JSONField(name = "HQBitrate")
                private Long hQBitrate;
                @JSONField(name = "PayType")
                private Long payType;
                @JSONField(name = "TagContent")
                private String tagContent;
                @JSONField(name = "Accompany")
                private Long accompany;
                @JSONField(name = "SingerName")
                private String singerName;
                @JSONField(name = "HQPrivilege")
                private Long hQPrivilege;
                @JSONField(name = "TopicRemark")
                private String topicRemark;
                @JSONField(name = "OriOtherName")
                private String oriOtherName;
                @JSONField(name = "ShowingFlag")
                private Long showingFlag;
                @JSONField(name = "Source")
                private String source;
                @JSONField(name = "SQFileSize")
                private Long sQFileSize;
                @JSONField(name = "AlbumAux")
                private String albumAux;
                @JSONField(name = "HQDuration")
                private Long hQDuration;
                @JSONField(name = "Image")
                private String image;
                @JSONField(name = "HQPayType")
                private Long hQPayType;
                @JSONField(name = "M4aSize")
                private Long m4aSize;
                @JSONField(name = "mvdata")
                private List<MvdataDTO> mvdata;
                @JSONField(name = "SQPkgPrice")
                private Long sQPkgPrice;
                @JSONField(name = "HeatLevel")
                private Long heatLevel;
                @JSONField(name = "UploaderContent")
                private String uploaderContent;
                @JSONField(name = "FileSize")
                private Long fileSize;
                @JSONField(name = "IsOriginal")
                private Long isOriginal;
                @JSONField(name = "FileHash")
                private String fileHash;
                @JSONField(name = "trans_param")
                private TransParamDTO transParam;
                @JSONField(name = "MvTrac")
                private Long mvTrac;
                @JSONField(name = "ID")
                private String id;
                @JSONField(name = "Type")
                private String type;
                @JSONField(name = "Bitrate")
                private Long bitrate;
                @JSONField(name = "SQPrice")
                private Long sQPrice;
                @JSONField(name = "isPrepublish")
                private Long isPrepublish;
                @JSONField(name = "ExtName")
                private String extName;
                @JSONField(name = "Auxiliary")
                private String auxiliary;
                @JSONField(name = "AlbumName")
                private String albumName;
                @JSONField(name = "PkgPrice")
                private Long pkgPrice;
                @JSONField(name = "AlbumPrivilege")
                private Long albumPrivilege;
                @JSONField(name = "Category")
                private Long category;
                @JSONField(name = "SuperExtName")
                private String superExtName;
                @JSONField(name = "AlbumID")
                private String albumID;
                @JSONField(name = "OtherName")
                private String otherName;
                @JSONField(name = "SongName")
                private String songName;
                @JSONField(name = "ASQPrivilege")
                private Long aSQPrivilege;
                @JSONField(name = "AudioCdn")
                private Long audioCdn;
                @JSONField(name = "SourceID")
                private Long sourceID;
                @JSONField(name = "SQDuration")
                private Long sQDuration;
                @JSONField(name = "HQFileSize")
                private Long hQFileSize;
                @JSONField(name = "vvid")
                private String vvid;
                @JSONField(name = "MixSongID")
                private String mixSongID;
                @JSONField(name = "SQPayType")
                private Long sQPayType;
                @JSONField(name = "ResBitrate")
                private Long resBitrate;
                @JSONField(name = "SuperBitrate")
                private Long superBitrate;
                @JSONField(name = "HQPrice")
                private Long hQPrice;
                @JSONField(name = "Suffix")
                private String suffix;
                @JSONField(name = "HQFailProcess")
                private Long hQFailProcess;
                @JSONField(name = "mvTotal")
                private Long mvTotal;
                @JSONField(name = "SongLabel")
                private String songLabel;
                @JSONField(name = "ResDuration")
                private Long resDuration;
                @JSONField(name = "HiFiQuality")
                private Long hiFiQuality;
                @JSONField(name = "Singers")
                private List<SingersDTO> singers;
                @JSONField(name = "SingerId")
                private List<Long> singerId;
                @JSONField(name = "HQExtName")
                private String hQExtName;
                @JSONField(name = "ResFileHash")
                private String resFileHash;
                @JSONField(name = "MatchFlag")
                private Long matchFlag;
                @JSONField(name = "Scid")
                private Long scid;
                @JSONField(name = "SuperFileHash")
                private String superFileHash;
                @JSONField(name = "QualityLevel")
                private Long qualityLevel;
                @JSONField(name = "OriSongName")
                private String oriSongName;
                @JSONField(name = "HasAlbum")
                private Long hasAlbum;
                @JSONField(name = "MvType")
                private Long mvType;
                @JSONField(name = "SuperFileSize")
                private Long superFileSize;
                @JSONField(name = "MvHash")
                private String mvHash;
                @JSONField(name = "FailProcess")
                private Long failProcess;
                @JSONField(name = "SQBitrate")
                private Long sQBitrate;
                @JSONField(name = "SQExtName")
                private String sQExtName;
                @JSONField(name = "PublishDate")
                private String publishDate;
                @JSONField(name = "HQFileHash")
                private String hQFileHash;
                @JSONField(name = "TopicUrl")
                private String topicUrl;
                @JSONField(name = "RankId")
                private Long rankId;
                @JSONField(name = "TagDetails")
                private List<?> tagDetails;
                @JSONField(name = "Privilege")
                private Long privilege;
                @JSONField(name = "PrepublishInfo")
                private PrepublishInfoDTO prepublishInfo;
                @JSONField(name = "HQPkgPrice")
                private Long hQPkgPrice;
                @JSONField(name = "OwnerCount")
                private Long ownerCount;
                @JSONField(name = "Uploader")
                private String uploader;
                @JSONField(name = "Duration")
                private Long duration;
                @JSONField(name = "SQFailProcess")
                private Long sQFailProcess;
                @JSONField(name = "TopID")
                private Long topID;
                @JSONField(name = "A320Privilege")
                private Long a320Privilege;
                @JSONField(name = "FileName")
                private String fileName;
                @JSONField(name = "ResFileSize")
                private Long resFileSize;
                @JSONField(name = "SQPrivilege")
                private Long sQPrivilege;
                @JSONField(name = "Price")
                private Long price;
                @JSONField(name = "recommend_type")
                private Long recommendType;
                @JSONField(name = "Publish")
                private Long publish;

                public String getSQFileHash() {
                    return sQFileHash;
                }

                public void setSQFileHash(String sQFileHash) {
                    this.sQFileHash = sQFileHash;
                }

                public String getPublishTime() {
                    return publishTime;
                }

                public void setPublishTime(String publishTime) {
                    this.publishTime = publishTime;
                }

                public Long getAudioid() {
                    return audioid;
                }

                public void setAudioid(Long audioid) {
                    this.audioid = audioid;
                }

                public Long getSuperDuration() {
                    return superDuration;
                }

                public void setSuperDuration(Long superDuration) {
                    this.superDuration = superDuration;
                }

                public Long getOldCpy() {
                    return oldCpy;
                }

                public void setOldCpy(Long oldCpy) {
                    this.oldCpy = oldCpy;
                }

                public Long getPublishAge() {
                    return publishAge;
                }

                public void setPublishAge(Long publishAge) {
                    this.publishAge = publishAge;
                }

                public Long getHQBitrate() {
                    return hQBitrate;
                }

                public void setHQBitrate(Long hQBitrate) {
                    this.hQBitrate = hQBitrate;
                }

                public Long getPayType() {
                    return payType;
                }

                public void setPayType(Long payType) {
                    this.payType = payType;
                }

                public String getTagContent() {
                    return tagContent;
                }

                public void setTagContent(String tagContent) {
                    this.tagContent = tagContent;
                }

                public Long getAccompany() {
                    return accompany;
                }

                public void setAccompany(Long accompany) {
                    this.accompany = accompany;
                }

                public String getSingerName() {
                    return singerName;
                }

                public void setSingerName(String singerName) {
                    this.singerName = singerName;
                }

                public Long getHQPrivilege() {
                    return hQPrivilege;
                }

                public void setHQPrivilege(Long hQPrivilege) {
                    this.hQPrivilege = hQPrivilege;
                }

                public String getTopicRemark() {
                    return topicRemark;
                }

                public void setTopicRemark(String topicRemark) {
                    this.topicRemark = topicRemark;
                }

                public String getOriOtherName() {
                    return oriOtherName;
                }

                public void setOriOtherName(String oriOtherName) {
                    this.oriOtherName = oriOtherName;
                }

                public Long getShowingFlag() {
                    return showingFlag;
                }

                public void setShowingFlag(Long showingFlag) {
                    this.showingFlag = showingFlag;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public Long getSQFileSize() {
                    return sQFileSize;
                }

                public void setSQFileSize(Long sQFileSize) {
                    this.sQFileSize = sQFileSize;
                }

                public String getAlbumAux() {
                    return albumAux;
                }

                public void setAlbumAux(String albumAux) {
                    this.albumAux = albumAux;
                }

                public Long getHQDuration() {
                    return hQDuration;
                }

                public void setHQDuration(Long hQDuration) {
                    this.hQDuration = hQDuration;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public Long getHQPayType() {
                    return hQPayType;
                }

                public void setHQPayType(Long hQPayType) {
                    this.hQPayType = hQPayType;
                }

                public Long getM4aSize() {
                    return m4aSize;
                }

                public void setM4aSize(Long m4aSize) {
                    this.m4aSize = m4aSize;
                }

                public List<MvdataDTO> getMvdata() {
                    return mvdata;
                }

                public void setMvdata(List<MvdataDTO> mvdata) {
                    this.mvdata = mvdata;
                }

                public Long getSQPkgPrice() {
                    return sQPkgPrice;
                }

                public void setSQPkgPrice(Long sQPkgPrice) {
                    this.sQPkgPrice = sQPkgPrice;
                }

                public Long getHeatLevel() {
                    return heatLevel;
                }

                public void setHeatLevel(Long heatLevel) {
                    this.heatLevel = heatLevel;
                }

                public String getUploaderContent() {
                    return uploaderContent;
                }

                public void setUploaderContent(String uploaderContent) {
                    this.uploaderContent = uploaderContent;
                }

                public Long getFileSize() {
                    return fileSize;
                }

                public void setFileSize(Long fileSize) {
                    this.fileSize = fileSize;
                }

                public Long getIsOriginal() {
                    return isOriginal;
                }

                public void setIsOriginal(Long isOriginal) {
                    this.isOriginal = isOriginal;
                }

                public String getFileHash() {
                    return fileHash;
                }

                public void setFileHash(String fileHash) {
                    this.fileHash = fileHash;
                }

                public TransParamDTO getTransParam() {
                    return transParam;
                }

                public void setTransParam(TransParamDTO transParam) {
                    this.transParam = transParam;
                }

                public Long getMvTrac() {
                    return mvTrac;
                }

                public void setMvTrac(Long mvTrac) {
                    this.mvTrac = mvTrac;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public Long getBitrate() {
                    return bitrate;
                }

                public void setBitrate(Long bitrate) {
                    this.bitrate = bitrate;
                }

                public Long getSQPrice() {
                    return sQPrice;
                }

                public void setSQPrice(Long sQPrice) {
                    this.sQPrice = sQPrice;
                }

                public Long getIsPrepublish() {
                    return isPrepublish;
                }

                public void setIsPrepublish(Long isPrepublish) {
                    this.isPrepublish = isPrepublish;
                }

                public String getExtName() {
                    return extName;
                }

                public void setExtName(String extName) {
                    this.extName = extName;
                }

                public String getAuxiliary() {
                    return auxiliary;
                }

                public void setAuxiliary(String auxiliary) {
                    this.auxiliary = auxiliary;
                }

                public String getAlbumName() {
                    return albumName;
                }

                public void setAlbumName(String albumName) {
                    this.albumName = albumName;
                }

                public Long getPkgPrice() {
                    return pkgPrice;
                }

                public void setPkgPrice(Long pkgPrice) {
                    this.pkgPrice = pkgPrice;
                }

                public Long getAlbumPrivilege() {
                    return albumPrivilege;
                }

                public void setAlbumPrivilege(Long albumPrivilege) {
                    this.albumPrivilege = albumPrivilege;
                }

                public Long getCategory() {
                    return category;
                }

                public void setCategory(Long category) {
                    this.category = category;
                }

                public String getSuperExtName() {
                    return superExtName;
                }

                public void setSuperExtName(String superExtName) {
                    this.superExtName = superExtName;
                }

                public String getAlbumID() {
                    return albumID;
                }

                public void setAlbumID(String albumID) {
                    this.albumID = albumID;
                }

                public String getOtherName() {
                    return otherName;
                }

                public void setOtherName(String otherName) {
                    this.otherName = otherName;
                }

                public String getSongName() {
                    return songName;
                }

                public void setSongName(String songName) {
                    this.songName = songName;
                }

                public Long getASQPrivilege() {
                    return aSQPrivilege;
                }

                public void setASQPrivilege(Long aSQPrivilege) {
                    this.aSQPrivilege = aSQPrivilege;
                }

                public Long getAudioCdn() {
                    return audioCdn;
                }

                public void setAudioCdn(Long audioCdn) {
                    this.audioCdn = audioCdn;
                }

                public Long getSourceID() {
                    return sourceID;
                }

                public void setSourceID(Long sourceID) {
                    this.sourceID = sourceID;
                }

                public Long getSQDuration() {
                    return sQDuration;
                }

                public void setSQDuration(Long sQDuration) {
                    this.sQDuration = sQDuration;
                }

                public Long getHQFileSize() {
                    return hQFileSize;
                }

                public void setHQFileSize(Long hQFileSize) {
                    this.hQFileSize = hQFileSize;
                }

                public String getVvid() {
                    return vvid;
                }

                public void setVvid(String vvid) {
                    this.vvid = vvid;
                }

                public String getMixSongID() {
                    return mixSongID;
                }

                public void setMixSongID(String mixSongID) {
                    this.mixSongID = mixSongID;
                }

                public Long getSQPayType() {
                    return sQPayType;
                }

                public void setSQPayType(Long sQPayType) {
                    this.sQPayType = sQPayType;
                }

                public Long getResBitrate() {
                    return resBitrate;
                }

                public void setResBitrate(Long resBitrate) {
                    this.resBitrate = resBitrate;
                }

                public Long getSuperBitrate() {
                    return superBitrate;
                }

                public void setSuperBitrate(Long superBitrate) {
                    this.superBitrate = superBitrate;
                }

                public Long getHQPrice() {
                    return hQPrice;
                }

                public void setHQPrice(Long hQPrice) {
                    this.hQPrice = hQPrice;
                }

                public String getSuffix() {
                    return suffix;
                }

                public void setSuffix(String suffix) {
                    this.suffix = suffix;
                }

                public Long getHQFailProcess() {
                    return hQFailProcess;
                }

                public void setHQFailProcess(Long hQFailProcess) {
                    this.hQFailProcess = hQFailProcess;
                }

                public Long getMvTotal() {
                    return mvTotal;
                }

                public void setMvTotal(Long mvTotal) {
                    this.mvTotal = mvTotal;
                }

                public String getSongLabel() {
                    return songLabel;
                }

                public void setSongLabel(String songLabel) {
                    this.songLabel = songLabel;
                }

                public Long getResDuration() {
                    return resDuration;
                }

                public void setResDuration(Long resDuration) {
                    this.resDuration = resDuration;
                }

                public Long getHiFiQuality() {
                    return hiFiQuality;
                }

                public void setHiFiQuality(Long hiFiQuality) {
                    this.hiFiQuality = hiFiQuality;
                }

                public List<SingersDTO> getSingers() {
                    return singers;
                }

                public void setSingers(List<SingersDTO> singers) {
                    this.singers = singers;
                }

                public List<Long> getSingerId() {
                    return singerId;
                }

                public void setSingerId(List<Long> singerId) {
                    this.singerId = singerId;
                }

                public String getHQExtName() {
                    return hQExtName;
                }

                public void setHQExtName(String hQExtName) {
                    this.hQExtName = hQExtName;
                }

                public String getResFileHash() {
                    return resFileHash;
                }

                public void setResFileHash(String resFileHash) {
                    this.resFileHash = resFileHash;
                }

                public Long getMatchFlag() {
                    return matchFlag;
                }

                public void setMatchFlag(Long matchFlag) {
                    this.matchFlag = matchFlag;
                }

                public Long getScid() {
                    return scid;
                }

                public void setScid(Long scid) {
                    this.scid = scid;
                }

                public String getSuperFileHash() {
                    return superFileHash;
                }

                public void setSuperFileHash(String superFileHash) {
                    this.superFileHash = superFileHash;
                }

                public Long getQualityLevel() {
                    return qualityLevel;
                }

                public void setQualityLevel(Long qualityLevel) {
                    this.qualityLevel = qualityLevel;
                }

                public String getOriSongName() {
                    return oriSongName;
                }

                public void setOriSongName(String oriSongName) {
                    this.oriSongName = oriSongName;
                }

                public Long getHasAlbum() {
                    return hasAlbum;
                }

                public void setHasAlbum(Long hasAlbum) {
                    this.hasAlbum = hasAlbum;
                }

                public Long getMvType() {
                    return mvType;
                }

                public void setMvType(Long mvType) {
                    this.mvType = mvType;
                }

                public Long getSuperFileSize() {
                    return superFileSize;
                }

                public void setSuperFileSize(Long superFileSize) {
                    this.superFileSize = superFileSize;
                }

                public String getMvHash() {
                    return mvHash;
                }

                public void setMvHash(String mvHash) {
                    this.mvHash = mvHash;
                }

                public Long getFailProcess() {
                    return failProcess;
                }

                public void setFailProcess(Long failProcess) {
                    this.failProcess = failProcess;
                }

                public Long getSQBitrate() {
                    return sQBitrate;
                }

                public void setSQBitrate(Long sQBitrate) {
                    this.sQBitrate = sQBitrate;
                }

                public String getSQExtName() {
                    return sQExtName;
                }

                public void setSQExtName(String sQExtName) {
                    this.sQExtName = sQExtName;
                }

                public String getPublishDate() {
                    return publishDate;
                }

                public void setPublishDate(String publishDate) {
                    this.publishDate = publishDate;
                }

                public String getHQFileHash() {
                    return hQFileHash;
                }

                public void setHQFileHash(String hQFileHash) {
                    this.hQFileHash = hQFileHash;
                }

                public String getTopicUrl() {
                    return topicUrl;
                }

                public void setTopicUrl(String topicUrl) {
                    this.topicUrl = topicUrl;
                }

                public Long getRankId() {
                    return rankId;
                }

                public void setRankId(Long rankId) {
                    this.rankId = rankId;
                }

                public List<?> getTagDetails() {
                    return tagDetails;
                }

                public void setTagDetails(List<?> tagDetails) {
                    this.tagDetails = tagDetails;
                }

                public Long getPrivilege() {
                    return privilege;
                }

                public void setPrivilege(Long privilege) {
                    this.privilege = privilege;
                }

                public PrepublishInfoDTO getPrepublishInfo() {
                    return prepublishInfo;
                }

                public void setPrepublishInfo(PrepublishInfoDTO prepublishInfo) {
                    this.prepublishInfo = prepublishInfo;
                }

                public Long getHQPkgPrice() {
                    return hQPkgPrice;
                }

                public void setHQPkgPrice(Long hQPkgPrice) {
                    this.hQPkgPrice = hQPkgPrice;
                }

                public Long getOwnerCount() {
                    return ownerCount;
                }

                public void setOwnerCount(Long ownerCount) {
                    this.ownerCount = ownerCount;
                }

                public String getUploader() {
                    return uploader;
                }

                public void setUploader(String uploader) {
                    this.uploader = uploader;
                }

                public Long getDuration() {
                    return duration;
                }

                public void setDuration(Long duration) {
                    this.duration = duration;
                }

                public Long getSQFailProcess() {
                    return sQFailProcess;
                }

                public void setSQFailProcess(Long sQFailProcess) {
                    this.sQFailProcess = sQFailProcess;
                }

                public Long getTopID() {
                    return topID;
                }

                public void setTopID(Long topID) {
                    this.topID = topID;
                }

                public Long getA320Privilege() {
                    return a320Privilege;
                }

                public void setA320Privilege(Long a320Privilege) {
                    this.a320Privilege = a320Privilege;
                }

                public String getFileName() {
                    return fileName;
                }

                public void setFileName(String fileName) {
                    this.fileName = fileName;
                }

                public Long getResFileSize() {
                    return resFileSize;
                }

                public void setResFileSize(Long resFileSize) {
                    this.resFileSize = resFileSize;
                }

                public Long getSQPrivilege() {
                    return sQPrivilege;
                }

                public void setSQPrivilege(Long sQPrivilege) {
                    this.sQPrivilege = sQPrivilege;
                }

                public Long getPrice() {
                    return price;
                }

                public void setPrice(Long price) {
                    this.price = price;
                }

                public Long getRecommendType() {
                    return recommendType;
                }

                public void setRecommendType(Long recommendType) {
                    this.recommendType = recommendType;
                }

                public Long getPublish() {
                    return publish;
                }

                public void setPublish(Long publish) {
                    this.publish = publish;
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
                    @JSONField(name = "ogg_128_filesize")
                    private Long ogg128Filesize;
                    @JSONField(name = "display_rate")
                    private Long displayRate;
                    @JSONField(name = "union_cover")
                    private String unionCover;
                    @JSONField(name = "qualitymap")
                    private QualitymapDTO qualitymap;
                    @JSONField(name = "ogg_320_filesize")
                    private Long ogg320Filesize;
                    @JSONField(name = "ogg_320_hash")
                    private String ogg320Hash;
                    @JSONField(name = "cid")
                    private Long cid;
                    @JSONField(name = "cpy_grade")
                    private Long cpyGrade;
                    @JSONField(name = "display")
                    private Long display;
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

                    public Long getOgg128Filesize() {
                        return ogg128Filesize;
                    }

                    public void setOgg128Filesize(Long ogg128Filesize) {
                        this.ogg128Filesize = ogg128Filesize;
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

                    public QualitymapDTO getQualitymap() {
                        return qualitymap;
                    }

                    public void setQualitymap(QualitymapDTO qualitymap) {
                        this.qualitymap = qualitymap;
                    }

                    public Long getOgg320Filesize() {
                        return ogg320Filesize;
                    }

                    public void setOgg320Filesize(Long ogg320Filesize) {
                        this.ogg320Filesize = ogg320Filesize;
                    }

                    public String getOgg320Hash() {
                        return ogg320Hash;
                    }

                    public void setOgg320Hash(String ogg320Hash) {
                        this.ogg320Hash = ogg320Hash;
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

                    public Long getDisplay() {
                        return display;
                    }

                    public void setDisplay(Long display) {
                        this.display = display;
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

                public static class PrepublishInfoDTO {
                    @JSONField(name = "ReserveCount")
                    private Long reserveCount;
                    @JSONField(name = "DisplayTime")
                    private String displayTime;
                    @JSONField(name = "Id")
                    private Long id;
                    @JSONField(name = "PublishTime")
                    private String publishTime;

                    public Long getReserveCount() {
                        return reserveCount;
                    }

                    public void setReserveCount(Long reserveCount) {
                        this.reserveCount = reserveCount;
                    }

                    public String getDisplayTime() {
                        return displayTime;
                    }

                    public void setDisplayTime(String displayTime) {
                        this.displayTime = displayTime;
                    }

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
                        this.id = id;
                    }

                    public String getPublishTime() {
                        return publishTime;
                    }

                    public void setPublishTime(String publishTime) {
                        this.publishTime = publishTime;
                    }
                }

                public static class MvdataDTO {
                    @JSONField(name = "id")
                    private String id;
                    @JSONField(name = "trk")
                    private String trk;
                    @JSONField(name = "hash")
                    private String hash;
                    @JSONField(name = "typ")
                    private Long typ;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                    public Long getTyp() {
                        return typ;
                    }

                    public void setTyp(Long typ) {
                        this.typ = typ;
                    }
                }

                public static class SingersDTO {
                    @JSONField(name = "name")
                    private String name;
                    @JSONField(name = "ip_id")
                    private Long ipId;
                    @JSONField(name = "id")
                    private Long id;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public Long getIpId() {
                        return ipId;
                    }

                    public void setIpId(Long ipId) {
                        this.ipId = ipId;
                    }

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
                        this.id = id;
                    }
                }
            }

            public static class SingersDTO {
                @JSONField(name = "name")
                private String name;
                @JSONField(name = "ip_id")
                private Long ipId;
                @JSONField(name = "id")
                private Long id;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Long getIpId() {
                    return ipId;
                }

                public void setIpId(Long ipId) {
                    this.ipId = ipId;
                }

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }
            }

            public static class TagDetailsDTO {
                @JSONField(name = "content")
                private String content;
                @JSONField(name = "rankid")
                private Long rankid;
                @JSONField(name = "version")
                private Long version;
                @JSONField(name = "type")
                private Long type;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public Long getRankid() {
                    return rankid;
                }

                public void setRankid(Long rankid) {
                    this.rankid = rankid;
                }

                public Long getVersion() {
                    return version;
                }

                public void setVersion(Long version) {
                    this.version = version;
                }

                public Long getType() {
                    return type;
                }

                public void setType(Long type) {
                    this.type = type;
                }
            }
        }
    }
}
