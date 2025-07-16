package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname SearchAlbumResult
 * @Description 搜索专辑结果
 * @Version 1.0.0
 * @Date 2025/2/6 17:06
 * @Created by SQ
 */

public class SearchAlbumResult {


    @JSONField(name = "status")
    private Long status;
    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "error_msg")
    private String errorMsg;
    @JSONField(name = "data")
    private DataDTO data;

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

    public static class DataDTO {
        @JSONField(name = "pagesize")
        private Long pagesize;
        @JSONField(name = "page")
        private Long page;
        @JSONField(name = "from")
        private Long from;
        @JSONField(name = "size")
        private Long size;
        @JSONField(name = "total")
        private Long total;
        @JSONField(name = "correctiontype")
        private Long correctiontype;
        @JSONField(name = "correctionforce")
        private Long correctionforce;
        @JSONField(name = "correctiontip")
        private String correctiontip;
        @JSONField(name = "lists")
        private List<ListsDTO> lists;

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

        public Long getFrom() {
            return from;
        }

        public void setFrom(Long from) {
            this.from = from;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getCorrectiontype() {
            return correctiontype;
        }

        public void setCorrectiontype(Long correctiontype) {
            this.correctiontype = correctiontype;
        }

        public Long getCorrectionforce() {
            return correctionforce;
        }

        public void setCorrectionforce(Long correctionforce) {
            this.correctionforce = correctionforce;
        }

        public String getCorrectiontip() {
            return correctiontip;
        }

        public void setCorrectiontip(String correctiontip) {
            this.correctiontip = correctiontip;
        }

        public List<ListsDTO> getLists() {
            return lists;
        }

        public void setLists(List<ListsDTO> lists) {
            this.lists = lists;
        }

        public static class ListsDTO {
            @JSONField(name = "albumid")
            private Long albumid;
            @JSONField(name = "albumname")
            private String albumname;
            @JSONField(name = "singer")
            private String singer;
            @JSONField(name = "singerid")
            private String singerid;
            @JSONField(name = "grade")
            private Long grade;
            @JSONField(name = "grade_int")
            private String gradeInt;
            @JSONField(name = "img")
            private String img;
            @JSONField(name = "intro")
            private String intro;
            @JSONField(name = "grade_float")
            private String gradeFloat;
            @JSONField(name = "company")
            private String company;
            @JSONField(name = "quality")
            private Long quality;
            @JSONField(name = "title")
            private String title;
            @JSONField(name = "collect_count")
            private Long collectCount;
            @JSONField(name = "publish_time")
            private String publishTime;
            @JSONField(name = "language")
            private String language;
            @JSONField(name = "privilege")
            private Long privilege;
            @JSONField(name = "oldhide")
            private Long oldhide;
            @JSONField(name = "buyercount")
            private Long buyercount;
            @JSONField(name = "songcount")
            private Long songcount;
            @JSONField(name = "newquality")
            private Long newquality;
            @JSONField(name = "cd_url")
            private String cdUrl;
            @JSONField(name = "isfirst")
            private Long isfirst;
            @JSONField(name = "category")
            private Long category;
            @JSONField(name = "short_intro")
            private String shortIntro;
            @JSONField(name = "ostremark")
            private String ostremark;
            @JSONField(name = "auxiliary")
            private String auxiliary;
            @JSONField(name = "play_times")
            private Long playTimes;
            @JSONField(name = "program_inner")
            private Long programInner;
            @JSONField(name = "alg_path")
            private String algPath;
            @JSONField(name = "program_def_songs")
            private List<?> programDefSongs;
            @JSONField(name = "tag_str")
            private String tagStr;
            @JSONField(name = "album_aux")
            private String albumAux;
            @JSONField(name = "play_count")
            private Long playCount;
            @JSONField(name = "isouter")
            private Long isouter;
            @JSONField(name = "outerdata")
            private OuterdataDTO outerdata;
            @JSONField(name = "trans_param")
            private TransParamDTO transParam;
            @JSONField(name = "singerids")
            private List<Long> singerids;
            @JSONField(name = "singers")
            private List<SingersDTO> singers;

            public Long getAlbumid() {
                return albumid;
            }

            public void setAlbumid(Long albumid) {
                this.albumid = albumid;
            }

            public String getAlbumname() {
                return albumname;
            }

            public void setAlbumname(String albumname) {
                this.albumname = albumname;
            }

            public String getSinger() {
                return singer;
            }

            public void setSinger(String singer) {
                this.singer = singer;
            }

            public String getSingerid() {
                return singerid;
            }

            public void setSingerid(String singerid) {
                this.singerid = singerid;
            }

            public Long getGrade() {
                return grade;
            }

            public void setGrade(Long grade) {
                this.grade = grade;
            }

            public String getGradeInt() {
                return gradeInt;
            }

            public void setGradeInt(String gradeInt) {
                this.gradeInt = gradeInt;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getGradeFloat() {
                return gradeFloat;
            }

            public void setGradeFloat(String gradeFloat) {
                this.gradeFloat = gradeFloat;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public Long getQuality() {
                return quality;
            }

            public void setQuality(Long quality) {
                this.quality = quality;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Long getCollectCount() {
                return collectCount;
            }

            public void setCollectCount(Long collectCount) {
                this.collectCount = collectCount;
            }

            public String getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(String publishTime) {
                this.publishTime = publishTime;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public Long getPrivilege() {
                return privilege;
            }

            public void setPrivilege(Long privilege) {
                this.privilege = privilege;
            }

            public Long getOldhide() {
                return oldhide;
            }

            public void setOldhide(Long oldhide) {
                this.oldhide = oldhide;
            }

            public Long getBuyercount() {
                return buyercount;
            }

            public void setBuyercount(Long buyercount) {
                this.buyercount = buyercount;
            }

            public Long getSongcount() {
                return songcount;
            }

            public void setSongcount(Long songcount) {
                this.songcount = songcount;
            }

            public Long getNewquality() {
                return newquality;
            }

            public void setNewquality(Long newquality) {
                this.newquality = newquality;
            }

            public String getCdUrl() {
                return cdUrl;
            }

            public void setCdUrl(String cdUrl) {
                this.cdUrl = cdUrl;
            }

            public Long getIsfirst() {
                return isfirst;
            }

            public void setIsfirst(Long isfirst) {
                this.isfirst = isfirst;
            }

            public Long getCategory() {
                return category;
            }

            public void setCategory(Long category) {
                this.category = category;
            }

            public String getShortIntro() {
                return shortIntro;
            }

            public void setShortIntro(String shortIntro) {
                this.shortIntro = shortIntro;
            }

            public String getOstremark() {
                return ostremark;
            }

            public void setOstremark(String ostremark) {
                this.ostremark = ostremark;
            }

            public String getAuxiliary() {
                return auxiliary;
            }

            public void setAuxiliary(String auxiliary) {
                this.auxiliary = auxiliary;
            }

            public Long getPlayTimes() {
                return playTimes;
            }

            public void setPlayTimes(Long playTimes) {
                this.playTimes = playTimes;
            }

            public Long getProgramInner() {
                return programInner;
            }

            public void setProgramInner(Long programInner) {
                this.programInner = programInner;
            }

            public String getAlgPath() {
                return algPath;
            }

            public void setAlgPath(String algPath) {
                this.algPath = algPath;
            }

            public List<?> getProgramDefSongs() {
                return programDefSongs;
            }

            public void setProgramDefSongs(List<?> programDefSongs) {
                this.programDefSongs = programDefSongs;
            }

            public String getTagStr() {
                return tagStr;
            }

            public void setTagStr(String tagStr) {
                this.tagStr = tagStr;
            }

            public String getAlbumAux() {
                return albumAux;
            }

            public void setAlbumAux(String albumAux) {
                this.albumAux = albumAux;
            }

            public Long getPlayCount() {
                return playCount;
            }

            public void setPlayCount(Long playCount) {
                this.playCount = playCount;
            }

            public Long getIsouter() {
                return isouter;
            }

            public void setIsouter(Long isouter) {
                this.isouter = isouter;
            }

            public OuterdataDTO getOuterdata() {
                return outerdata;
            }

            public void setOuterdata(OuterdataDTO outerdata) {
                this.outerdata = outerdata;
            }

            public TransParamDTO getTransParam() {
                return transParam;
            }

            public void setTransParam(TransParamDTO transParam) {
                this.transParam = transParam;
            }

            public List<Long> getSingerids() {
                return singerids;
            }

            public void setSingerids(List<Long> singerids) {
                this.singerids = singerids;
            }

            public List<SingersDTO> getSingers() {
                return singers;
            }

            public void setSingers(List<SingersDTO> singers) {
                this.singers = singers;
            }

            public static class OuterdataDTO {
            }

            public static class TransParamDTO {
                @JSONField(name = "special_tag")
                private String specialTag;

                public String getSpecialTag() {
                    return specialTag;
                }

                public void setSpecialTag(String specialTag) {
                    this.specialTag = specialTag;
                }
            }

            public static class SingersDTO {
                @JSONField(name = "name")
                private String name;
                @JSONField(name = "id")
                private Long id;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }
            }
        }
    }
}
