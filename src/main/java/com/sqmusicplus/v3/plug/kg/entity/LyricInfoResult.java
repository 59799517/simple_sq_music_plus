package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname LyricInfoResult
 * @Description 歌词主要信息（非歌词内容）
 * @Version 1.0.0
 * @Date 2025/2/11 15:59
 * @Created by SQ
 */

public class LyricInfoResult {

    @JSONField(name = "status")
    private Long status;
    private String info;
    @JSONField(name = "errcode")
    private Long errcode;
    @JSONField(name = "errmsg")
    private String errmsg;
    @JSONField(name = "keyword")
    private String keyword;
    @JSONField(name = "proposal")
    private String proposal;
    @JSONField(name = "has_complete_right")
    private Long hasCompleteRight;
    @JSONField(name = "companys")
    private String companys;
    @JSONField(name = "ugc")
    private Long ugc;
    @JSONField(name = "ugccount")
    private Long ugccount;
    @JSONField(name = "expire")
    private Long expire;
    @JSONField(name = "candidates")
    private List<CandidatesDTO> candidates;
    @JSONField(name = "ugccandidates")
    private List<?> ugccandidates;
    @JSONField(name = "artists")
    private List<ArtistsDTO> artists;
    @JSONField(name = "ai_candidates")
    private List<?> aiCandidates;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public Long getHasCompleteRight() {
        return hasCompleteRight;
    }

    public void setHasCompleteRight(Long hasCompleteRight) {
        this.hasCompleteRight = hasCompleteRight;
    }

    public String getCompanys() {
        return companys;
    }

    public void setCompanys(String companys) {
        this.companys = companys;
    }

    public Long getUgc() {
        return ugc;
    }

    public void setUgc(Long ugc) {
        this.ugc = ugc;
    }

    public Long getUgccount() {
        return ugccount;
    }

    public void setUgccount(Long ugccount) {
        this.ugccount = ugccount;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public List<CandidatesDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidatesDTO> candidates) {
        this.candidates = candidates;
    }

    public List<?> getUgccandidates() {
        return ugccandidates;
    }

    public void setUgccandidates(List<?> ugccandidates) {
        this.ugccandidates = ugccandidates;
    }

    public List<ArtistsDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistsDTO> artists) {
        this.artists = artists;
    }

    public List<?> getAiCandidates() {
        return aiCandidates;
    }

    public void setAiCandidates(List<?> aiCandidates) {
        this.aiCandidates = aiCandidates;
    }

    public static class CandidatesDTO {
        @JSONField(name = "id")
        private String id;
        @JSONField(name = "product_from")
        private String productFrom;
        @JSONField(name = "accesskey")
        private String accesskey;
        @JSONField(name = "can_score")
        private Boolean canScore;
        @JSONField(name = "singer")
        private String singer;
        @JSONField(name = "song")
        private String song;
        @JSONField(name = "duration")
        private Long duration;
        @JSONField(name = "uid")
        private String uid;
        @JSONField(name = "nickname")
        private String nickname;
        @JSONField(name = "origiuid")
        private String origiuid;
        @JSONField(name = "transuid")
        private String transuid;
        @JSONField(name = "sounduid")
        private String sounduid;
        @JSONField(name = "originame")
        private String originame;
        @JSONField(name = "transname")
        private String transname;
        @JSONField(name = "soundname")
        private String soundname;
        @JSONField(name = "parinfo")
        private List<?> parinfo;
        @JSONField(name = "parinfoExt")
        private List<?> parinfoExt;
        @JSONField(name = "language")
        private String language;
        @JSONField(name = "krctype")
        private Long krctype;
        @JSONField(name = "hitlayer")
        private Long hitlayer;
        @JSONField(name = "hitcasemask")
        private Long hitcasemask;
        @JSONField(name = "adjust")
        private Long adjust;
        @JSONField(name = "score")
        private Long score;
        @JSONField(name = "contenttype")
        private Long contenttype;
        @JSONField(name = "content_format")
        private Long contentFormat;
        @JSONField(name = "download_id")
        private String downloadId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductFrom() {
            return productFrom;
        }

        public void setProductFrom(String productFrom) {
            this.productFrom = productFrom;
        }

        public String getAccesskey() {
            return accesskey;
        }

        public void setAccesskey(String accesskey) {
            this.accesskey = accesskey;
        }

        public Boolean getCanScore() {
            return canScore;
        }

        public void setCanScore(Boolean canScore) {
            this.canScore = canScore;
        }

        public String getSinger() {
            return singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }

        public Long getDuration() {
            return duration;
        }

        public void setDuration(Long duration) {
            this.duration = duration;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOrigiuid() {
            return origiuid;
        }

        public void setOrigiuid(String origiuid) {
            this.origiuid = origiuid;
        }

        public String getTransuid() {
            return transuid;
        }

        public void setTransuid(String transuid) {
            this.transuid = transuid;
        }

        public String getSounduid() {
            return sounduid;
        }

        public void setSounduid(String sounduid) {
            this.sounduid = sounduid;
        }

        public String getOriginame() {
            return originame;
        }

        public void setOriginame(String originame) {
            this.originame = originame;
        }

        public String getTransname() {
            return transname;
        }

        public void setTransname(String transname) {
            this.transname = transname;
        }

        public String getSoundname() {
            return soundname;
        }

        public void setSoundname(String soundname) {
            this.soundname = soundname;
        }

        public List<?> getParinfo() {
            return parinfo;
        }

        public void setParinfo(List<?> parinfo) {
            this.parinfo = parinfo;
        }

        public List<?> getParinfoExt() {
            return parinfoExt;
        }

        public void setParinfoExt(List<?> parinfoExt) {
            this.parinfoExt = parinfoExt;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Long getKrctype() {
            return krctype;
        }

        public void setKrctype(Long krctype) {
            this.krctype = krctype;
        }

        public Long getHitlayer() {
            return hitlayer;
        }

        public void setHitlayer(Long hitlayer) {
            this.hitlayer = hitlayer;
        }

        public Long getHitcasemask() {
            return hitcasemask;
        }

        public void setHitcasemask(Long hitcasemask) {
            this.hitcasemask = hitcasemask;
        }

        public Long getAdjust() {
            return adjust;
        }

        public void setAdjust(Long adjust) {
            this.adjust = adjust;
        }

        public Long getScore() {
            return score;
        }

        public void setScore(Long score) {
            this.score = score;
        }

        public Long getContenttype() {
            return contenttype;
        }

        public void setContenttype(Long contenttype) {
            this.contenttype = contenttype;
        }

        public Long getContentFormat() {
            return contentFormat;
        }

        public void setContentFormat(Long contentFormat) {
            this.contentFormat = contentFormat;
        }

        public String getDownloadId() {
            return downloadId;
        }

        public void setDownloadId(String downloadId) {
            this.downloadId = downloadId;
        }
    }

    public static class ArtistsDTO {
        @JSONField(name = "identity")
        private Long identity;
        @JSONField(name = "base")
        private BaseDTO base;

        public Long getIdentity() {
            return identity;
        }

        public void setIdentity(Long identity) {
            this.identity = identity;
        }

        public BaseDTO getBase() {
            return base;
        }

        public void setBase(BaseDTO base) {
            this.base = base;
        }

        public static class BaseDTO {
            @JSONField(name = "author_id")
            private Long authorId;
            @JSONField(name = "author_name")
            private String authorName;
            @JSONField(name = "is_publish")
            private Long isPublish;
            @JSONField(name = "avatar")
            private String avatar;
            @JSONField(name = "identity")
            private Long identity;
            @JSONField(name = "type")
            private Long type;
            @JSONField(name = "country")
            private String country;
            @JSONField(name = "birthday")
            private String birthday;
            @JSONField(name = "language")
            private String language;

            public Long getAuthorId() {
                return authorId;
            }

            public void setAuthorId(Long authorId) {
                this.authorId = authorId;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public Long getIsPublish() {
                return isPublish;
            }

            public void setIsPublish(Long isPublish) {
                this.isPublish = isPublish;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public Long getIdentity() {
                return identity;
            }

            public void setIdentity(Long identity) {
                this.identity = identity;
            }

            public Long getType() {
                return type;
            }

            public void setType(Long type) {
                this.type = type;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }
        }
    }
}
