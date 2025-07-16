package com.sqmusicplus.v3.plug.qq.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Classname QQMuserUserInfo
 * @Description qq用户详情
 * @Version 1.0.0
 * @Date 2025/4/29 11:04
 * @Created by SQ
 */

public class QQMuserUserInfo {

    @JSONField(name = "code")
    private Long code;
    @JSONField(name = "ts")
    private Long ts;
    @JSONField(name = "start_ts")
    private Long startTs;
    @JSONField(name = "traceid")
    private String traceid;
    @JSONField(name = "req")
    private ReqDTO req;

    @JSONField(name = "cacheable")
    private CacheableDTO cacheable;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public String getTraceid() {
        return traceid;
    }

    public void setTraceid(String traceid) {
        this.traceid = traceid;
    }

    public ReqDTO getReq() {
        return req;
    }

    public void setReq(ReqDTO req) {
        this.req = req;
    }

    public CacheableDTO getCacheable() {
        return cacheable;
    }

    public void setCacheable(CacheableDTO cacheable) {
        this.cacheable = cacheable;
    }

    public static class ReqDTO {
        @JSONField(name = "code")
        private Long code;
        @JSONField(name = "data")
        private DataDTO data;

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public DataDTO getData() {
            return data;
        }

        public void setData(DataDTO data) {
            this.data = data;
        }

        public static class DataDTO {
            @JSONField(name = "errMsg")
            private String errMsg;
            @JSONField(name = "identify")
            private Long identify;
            @JSONField(name = "identifyHintURL")
            private String identifyHintURL;
            @JSONField(name = "identifyHintMsg")
            private String identifyHintMsg;
            @JSONField(name = "info")
            private InfoDTO info;
            @JSONField(name = "celebrityInfo")
            private CelebrityInfoDTO celebrityInfo;
            @JSONField(name = "pendantInfo")
            private PendantInfoDTO pendantInfo;
            @JSONField(name = "aiLogoPortal")
            private AiLogoPortalDTO aiLogoPortal;
            @JSONField(name = "banner")
            private BannerDTO banner;

            public String getErrMsg() {
                return errMsg;
            }

            public void setErrMsg(String errMsg) {
                this.errMsg = errMsg;
            }

            public Long getIdentify() {
                return identify;
            }

            public void setIdentify(Long identify) {
                this.identify = identify;
            }

            public String getIdentifyHintURL() {
                return identifyHintURL;
            }

            public void setIdentifyHintURL(String identifyHintURL) {
                this.identifyHintURL = identifyHintURL;
            }

            public String getIdentifyHintMsg() {
                return identifyHintMsg;
            }

            public void setIdentifyHintMsg(String identifyHintMsg) {
                this.identifyHintMsg = identifyHintMsg;
            }

            public InfoDTO getInfo() {
                return info;
            }

            public void setInfo(InfoDTO info) {
                this.info = info;
            }

            public CelebrityInfoDTO getCelebrityInfo() {
                return celebrityInfo;
            }

            public void setCelebrityInfo(CelebrityInfoDTO celebrityInfo) {
                this.celebrityInfo = celebrityInfo;
            }

            public PendantInfoDTO getPendantInfo() {
                return pendantInfo;
            }

            public void setPendantInfo(PendantInfoDTO pendantInfo) {
                this.pendantInfo = pendantInfo;
            }

            public AiLogoPortalDTO getAiLogoPortal() {
                return aiLogoPortal;
            }

            public void setAiLogoPortal(AiLogoPortalDTO aiLogoPortal) {
                this.aiLogoPortal = aiLogoPortal;
            }

            public BannerDTO getBanner() {
                return banner;
            }

            public void setBanner(BannerDTO banner) {
                this.banner = banner;
            }

            public static class InfoDTO {
                @JSONField(name = "nick")
                private String nick;
                @JSONField(name = "logo")
                private String logo;
                @JSONField(name = "hasUnuditLogo")
                private Long hasUnuditLogo;
                @JSONField(name = "gender")
                private Long gender;
                @JSONField(name = "birthday")
                private Long birthday;
                @JSONField(name = "country")
                private String country;
                @JSONField(name = "province")
                private String province;
                @JSONField(name = "city")
                private String city;
                @JSONField(name = "area")
                private String area;
                @JSONField(name = "detailLoc")
                private String detailLoc;
                @JSONField(name = "phone")
                private String phone;
                @JSONField(name = "email")
                private String email;
                @JSONField(name = "school")
                private String school;
                @JSONField(name = "intro")
                private String intro;
                @JSONField(name = "alterTs")
                private Long alterTs;
                @JSONField(name = "registerIP")
                private String registerIP;
                @JSONField(name = "registerDate")
                private Long registerDate;
                @JSONField(name = "metaData")
                private String metaData;
                @JSONField(name = "status")
                private String status;
                @JSONField(name = "retCode")
                private Long retCode;
                @JSONField(name = "singerID")
                private Long singerID;
                @JSONField(name = "identify")
                private Long identify;
                @JSONField(name = "types")
                private String types;
                @JSONField(name = "ifpicurl")
                private String ifpicurl;
                @JSONField(name = "extra")
                private String extra;
                @JSONField(name = "relSingerIDs")
                private String relSingerIDs;
                @JSONField(name = "logos")
                private String logos;
                @JSONField(name = "birthdayEx")
                private BirthdayExDTO birthdayEx;
                @JSONField(name = "isAiLogo")
                private Boolean isAiLogo;

                public String getNick() {
                    return nick;
                }

                public void setNick(String nick) {
                    this.nick = nick;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }

                public Long getHasUnuditLogo() {
                    return hasUnuditLogo;
                }

                public void setHasUnuditLogo(Long hasUnuditLogo) {
                    this.hasUnuditLogo = hasUnuditLogo;
                }

                public Long getGender() {
                    return gender;
                }

                public void setGender(Long gender) {
                    this.gender = gender;
                }

                public Long getBirthday() {
                    return birthday;
                }

                public void setBirthday(Long birthday) {
                    this.birthday = birthday;
                }

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getProvince() {
                    return province;
                }

                public void setProvince(String province) {
                    this.province = province;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getArea() {
                    return area;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public String getDetailLoc() {
                    return detailLoc;
                }

                public void setDetailLoc(String detailLoc) {
                    this.detailLoc = detailLoc;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getSchool() {
                    return school;
                }

                public void setSchool(String school) {
                    this.school = school;
                }

                public String getIntro() {
                    return intro;
                }

                public void setIntro(String intro) {
                    this.intro = intro;
                }

                public Long getAlterTs() {
                    return alterTs;
                }

                public void setAlterTs(Long alterTs) {
                    this.alterTs = alterTs;
                }

                public String getRegisterIP() {
                    return registerIP;
                }

                public void setRegisterIP(String registerIP) {
                    this.registerIP = registerIP;
                }

                public Long getRegisterDate() {
                    return registerDate;
                }

                public void setRegisterDate(Long registerDate) {
                    this.registerDate = registerDate;
                }

                public String getMetaData() {
                    return metaData;
                }

                public void setMetaData(String metaData) {
                    this.metaData = metaData;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Long getRetCode() {
                    return retCode;
                }

                public void setRetCode(Long retCode) {
                    this.retCode = retCode;
                }

                public Long getSingerID() {
                    return singerID;
                }

                public void setSingerID(Long singerID) {
                    this.singerID = singerID;
                }

                public Long getIdentify() {
                    return identify;
                }

                public void setIdentify(Long identify) {
                    this.identify = identify;
                }

                public String getTypes() {
                    return types;
                }

                public void setTypes(String types) {
                    this.types = types;
                }

                public String getIfpicurl() {
                    return ifpicurl;
                }

                public void setIfpicurl(String ifpicurl) {
                    this.ifpicurl = ifpicurl;
                }

                public String getExtra() {
                    return extra;
                }

                public void setExtra(String extra) {
                    this.extra = extra;
                }

                public String getRelSingerIDs() {
                    return relSingerIDs;
                }

                public void setRelSingerIDs(String relSingerIDs) {
                    this.relSingerIDs = relSingerIDs;
                }

                public String getLogos() {
                    return logos;
                }

                public void setLogos(String logos) {
                    this.logos = logos;
                }

                public BirthdayExDTO getBirthdayEx() {
                    return birthdayEx;
                }

                public void setBirthdayEx(BirthdayExDTO birthdayEx) {
                    this.birthdayEx = birthdayEx;
                }

                public Boolean getIsAiLogo() {
                    return isAiLogo;
                }

                public void setIsAiLogo(Boolean isAiLogo) {
                    this.isAiLogo = isAiLogo;
                }

                public static class BirthdayExDTO {
                    @JSONField(name = "date")
                    private Long date;
                    @JSONField(name = "from")
                    private Long from;
                    @JSONField(name = "update")
                    private Long update;
                    @JSONField(name = "count")
                    private Long count;
                    @JSONField(name = "intervalSec")
                    private Long intervalSec;
                    @JSONField(name = "hint")
                    private String hint;
                    @JSONField(name = "canAlter")
                    private Long canAlter;

                    public Long getDate() {
                        return date;
                    }

                    public void setDate(Long date) {
                        this.date = date;
                    }

                    public Long getFrom() {
                        return from;
                    }

                    public void setFrom(Long from) {
                        this.from = from;
                    }

                    public Long getUpdate() {
                        return update;
                    }

                    public void setUpdate(Long update) {
                        this.update = update;
                    }

                    public Long getCount() {
                        return count;
                    }

                    public void setCount(Long count) {
                        this.count = count;
                    }

                    public Long getIntervalSec() {
                        return intervalSec;
                    }

                    public void setIntervalSec(Long intervalSec) {
                        this.intervalSec = intervalSec;
                    }

                    public String getHint() {
                        return hint;
                    }

                    public void setHint(String hint) {
                        this.hint = hint;
                    }

                    public Long getCanAlter() {
                        return canAlter;
                    }

                    public void setCanAlter(Long canAlter) {
                        this.canAlter = canAlter;
                    }
                }
            }

            public static class CelebrityInfoDTO {
                @JSONField(name = "uin")
                private Long uin;
                @JSONField(name = "name")
                private String name;
                @JSONField(name = "pic")
                private String pic;
                @JSONField(name = "vec_identify")
                private String vecIdentify;
                @JSONField(name = "vec_type")
                private String vecType;
                @JSONField(name = "desc")
                private String desc;
                @JSONField(name = "phonenum")
                private String phonenum;
                @JSONField(name = "extra")
                private String extra;
                @JSONField(name = "status")
                private Long status;
                @JSONField(name = "singerid")
                private Long singerid;
                @JSONField(name = "ifpicurl")
                private String ifpicurl;
                @JSONField(name = "write_article")
                private String writeArticle;
                @JSONField(name = "create_radio")
                private String createRadio;
                @JSONField(name = "radio_broadcast")
                private String radioBroadcast;
                @JSONField(name = "video_broadcast")
                private String videoBroadcast;
                @JSONField(name = "top_comment")
                private String topComment;
                @JSONField(name = "video_check_later")
                private String videoCheckLater;
                @JSONField(name = "vec_rel_singer_id")
                private String vecRelSingerId;
                @JSONField(name = "retCode")
                private Long retCode;
                @JSONField(name = "pendantInfo")
                private PendantInfoDTO pendantInfo;
                @JSONField(name = "gender")
                private Long gender;
                @JSONField(name = "logos")
                private String logos;
                @JSONField(name = "birthday")
                private BirthdayDTO birthday;

                public Long getUin() {
                    return uin;
                }

                public void setUin(Long uin) {
                    this.uin = uin;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getVecIdentify() {
                    return vecIdentify;
                }

                public void setVecIdentify(String vecIdentify) {
                    this.vecIdentify = vecIdentify;
                }

                public String getVecType() {
                    return vecType;
                }

                public void setVecType(String vecType) {
                    this.vecType = vecType;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public String getPhonenum() {
                    return phonenum;
                }

                public void setPhonenum(String phonenum) {
                    this.phonenum = phonenum;
                }

                public String getExtra() {
                    return extra;
                }

                public void setExtra(String extra) {
                    this.extra = extra;
                }

                public Long getStatus() {
                    return status;
                }

                public void setStatus(Long status) {
                    this.status = status;
                }

                public Long getSingerid() {
                    return singerid;
                }

                public void setSingerid(Long singerid) {
                    this.singerid = singerid;
                }

                public String getIfpicurl() {
                    return ifpicurl;
                }

                public void setIfpicurl(String ifpicurl) {
                    this.ifpicurl = ifpicurl;
                }

                public String getWriteArticle() {
                    return writeArticle;
                }

                public void setWriteArticle(String writeArticle) {
                    this.writeArticle = writeArticle;
                }

                public String getCreateRadio() {
                    return createRadio;
                }

                public void setCreateRadio(String createRadio) {
                    this.createRadio = createRadio;
                }

                public String getRadioBroadcast() {
                    return radioBroadcast;
                }

                public void setRadioBroadcast(String radioBroadcast) {
                    this.radioBroadcast = radioBroadcast;
                }

                public String getVideoBroadcast() {
                    return videoBroadcast;
                }

                public void setVideoBroadcast(String videoBroadcast) {
                    this.videoBroadcast = videoBroadcast;
                }

                public String getTopComment() {
                    return topComment;
                }

                public void setTopComment(String topComment) {
                    this.topComment = topComment;
                }

                public String getVideoCheckLater() {
                    return videoCheckLater;
                }

                public void setVideoCheckLater(String videoCheckLater) {
                    this.videoCheckLater = videoCheckLater;
                }

                public String getVecRelSingerId() {
                    return vecRelSingerId;
                }

                public void setVecRelSingerId(String vecRelSingerId) {
                    this.vecRelSingerId = vecRelSingerId;
                }

                public Long getRetCode() {
                    return retCode;
                }

                public void setRetCode(Long retCode) {
                    this.retCode = retCode;
                }

                public PendantInfoDTO getPendantInfo() {
                    return pendantInfo;
                }

                public void setPendantInfo(PendantInfoDTO pendantInfo) {
                    this.pendantInfo = pendantInfo;
                }

                public Long getGender() {
                    return gender;
                }

                public void setGender(Long gender) {
                    this.gender = gender;
                }

                public String getLogos() {
                    return logos;
                }

                public void setLogos(String logos) {
                    this.logos = logos;
                }

                public BirthdayDTO getBirthday() {
                    return birthday;
                }

                public void setBirthday(BirthdayDTO birthday) {
                    this.birthday = birthday;
                }

                public static class PendantInfoDTO {
                    @JSONField(name = "staticImg")
                    private String staticImg;
                    @JSONField(name = "dynamicImg")
                    private String dynamicImg;
                    @JSONField(name = "status")
                    private Long status;
                    @JSONField(name = "id")
                    private Long id;

                    public String getStaticImg() {
                        return staticImg;
                    }

                    public void setStaticImg(String staticImg) {
                        this.staticImg = staticImg;
                    }

                    public String getDynamicImg() {
                        return dynamicImg;
                    }

                    public void setDynamicImg(String dynamicImg) {
                        this.dynamicImg = dynamicImg;
                    }

                    public Long getStatus() {
                        return status;
                    }

                    public void setStatus(Long status) {
                        this.status = status;
                    }

                    public Long getId() {
                        return id;
                    }

                    public void setId(Long id) {
                        this.id = id;
                    }
                }

                public static class BirthdayDTO {
                    @JSONField(name = "date")
                    private Long date;
                    @JSONField(name = "from")
                    private Long from;
                    @JSONField(name = "update")
                    private Long update;
                    @JSONField(name = "count")
                    private Long count;
                    @JSONField(name = "intervalSec")
                    private Long intervalSec;
                    @JSONField(name = "hint")
                    private String hint;
                    @JSONField(name = "canAlter")
                    private Long canAlter;

                    public Long getDate() {
                        return date;
                    }

                    public void setDate(Long date) {
                        this.date = date;
                    }

                    public Long getFrom() {
                        return from;
                    }

                    public void setFrom(Long from) {
                        this.from = from;
                    }

                    public Long getUpdate() {
                        return update;
                    }

                    public void setUpdate(Long update) {
                        this.update = update;
                    }

                    public Long getCount() {
                        return count;
                    }

                    public void setCount(Long count) {
                        this.count = count;
                    }

                    public Long getIntervalSec() {
                        return intervalSec;
                    }

                    public void setIntervalSec(Long intervalSec) {
                        this.intervalSec = intervalSec;
                    }

                    public String getHint() {
                        return hint;
                    }

                    public void setHint(String hint) {
                        this.hint = hint;
                    }

                    public Long getCanAlter() {
                        return canAlter;
                    }

                    public void setCanAlter(Long canAlter) {
                        this.canAlter = canAlter;
                    }
                }
            }

            public static class PendantInfoDTO {
                @JSONField(name = "staticImg")
                private String staticImg;
                @JSONField(name = "dynamicImg")
                private String dynamicImg;
                @JSONField(name = "status")
                private Long status;
                @JSONField(name = "id")
                private Long id;

                public String getStaticImg() {
                    return staticImg;
                }

                public void setStaticImg(String staticImg) {
                    this.staticImg = staticImg;
                }

                public String getDynamicImg() {
                    return dynamicImg;
                }

                public void setDynamicImg(String dynamicImg) {
                    this.dynamicImg = dynamicImg;
                }

                public Long getStatus() {
                    return status;
                }

                public void setStatus(Long status) {
                    this.status = status;
                }

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }
            }

            public static class AiLogoPortalDTO {
                @JSONField(name = "isShow")
                private Boolean isShow;
                @JSONField(name = "icon")
                private String icon;
                @JSONField(name = "text")
                private String text;
                @JSONField(name = "link")
                private String link;

                public Boolean getIsShow() {
                    return isShow;
                }

                public void setIsShow(Boolean isShow) {
                    this.isShow = isShow;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }

            public static class BannerDTO {
                @JSONField(name = "isShow")
                private Boolean isShow;
                @JSONField(name = "picOfBanner")
                private String picOfBanner;
                @JSONField(name = "link")
                private String link;

                public Boolean getIsShow() {
                    return isShow;
                }

                public void setIsShow(Boolean isShow) {
                    this.isShow = isShow;
                }

                public String getPicOfBanner() {
                    return picOfBanner;
                }

                public void setPicOfBanner(String picOfBanner) {
                    this.picOfBanner = picOfBanner;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }
        }
    }

    public static class CacheableDTO {
        @JSONField(name = "code")
        private Long code;
        @JSONField(name = "subcode")
        private Long subcode;

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public Long getSubcode() {
            return subcode;
        }

        public void setSubcode(Long subcode) {
            this.subcode = subcode;
        }
    }
}
