package com.sqmusicplus.plug.qq.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Classname DissInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/26 16:35
 * @Created by SQ
 */

public class DissInfo {

    @JsonProperty("code")
    private Long code;
    @JsonProperty("data")
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
        @JsonProperty("code")
        private Long code;
        @JsonProperty("subcode")
        private Long subcode;
        @JsonProperty("msg")
        private String msg;
        @JsonProperty("from_gedan_plaza")
        private Long fromGedanPlaza;
        @JsonProperty("accessed_plaza_cache")
        private Long accessedPlazaCache;
        @JsonProperty("accessed_byfav")
        private Long accessedByfav;
        @JsonProperty("optype")
        private Long optype;
        @JsonProperty("filter_song_num")
        private Long filterSongNum;
        @JsonProperty("sac_forbid")
        private List<String> sacForbid;
        @JsonProperty("dirinfo")
        private DirinfoDTO dirinfo;
        @JsonProperty("songlist_size")
        private Long songlistSize;
        @JsonProperty("songlist")
        private List<SonglistDTO> songlist;
        @JsonProperty("songtag")
        private List<String> songtag;
        @JsonProperty("toplist_song")
        private List<String> toplistSong;
        @JsonProperty("toplist_nolimit")
        private Boolean toplistNolimit;
        @JsonProperty("login_uin")
        private Long loginUin;
        @JsonProperty("invalid_song")
        private List<String> invalidSong;
        @JsonProperty("filtered_song")
        private List<String> filteredSong;
        @JsonProperty("ad_list")
        private List<String> adList;
        @JsonProperty("total_song_num")
        private Long totalSongNum;
        @JsonProperty("encrypt_login")
        private String encryptLogin;
        @JsonProperty("ct")
        private Long ct;
        @JsonProperty("cv")
        private Long cv;
        @JsonProperty("ip")
        private String ip;
        @JsonProperty("orderlist")
        private List<String> orderlist;
        @JsonProperty("birthday")
        private List<String> birthday;
        @JsonProperty("aiExt")
        private AiExtDTO aiExt;
        @JsonProperty("quickListenVid")
        private List<String> quickListenVid;
        @JsonProperty("bitflag")
        private Long bitflag;
        @JsonProperty("hasmore")
        private Long hasmore;
        @JsonProperty("cmtURL_bykey")
        private CmtURLBykeyDTO cmturlBykey;
        @JsonProperty("srf_ip")
        private String srfIp;
        @JsonProperty("referer")
        private String referer;
        @JsonProperty("namedflag")
        private Long namedflag;
        @JsonProperty("isAd")
        private Long isAd;
        @JsonProperty("adTitle")
        private String adTitle;
        @JsonProperty("adUrl")
        private String adUrl;
        @JsonProperty("recomUgcValid")
        private Long recomUgcValid;

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

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Long getFromGedanPlaza() {
            return fromGedanPlaza;
        }

        public void setFromGedanPlaza(Long fromGedanPlaza) {
            this.fromGedanPlaza = fromGedanPlaza;
        }

        public Long getAccessedPlazaCache() {
            return accessedPlazaCache;
        }

        public void setAccessedPlazaCache(Long accessedPlazaCache) {
            this.accessedPlazaCache = accessedPlazaCache;
        }

        public Long getAccessedByfav() {
            return accessedByfav;
        }

        public void setAccessedByfav(Long accessedByfav) {
            this.accessedByfav = accessedByfav;
        }

        public Long getOptype() {
            return optype;
        }

        public void setOptype(Long optype) {
            this.optype = optype;
        }

        public Long getFilterSongNum() {
            return filterSongNum;
        }

        public void setFilterSongNum(Long filterSongNum) {
            this.filterSongNum = filterSongNum;
        }

        public List<String> getSacForbid() {
            return sacForbid;
        }

        public void setSacForbid(List<String> sacForbid) {
            this.sacForbid = sacForbid;
        }

        public DirinfoDTO getDirinfo() {
            return dirinfo;
        }

        public void setDirinfo(DirinfoDTO dirinfo) {
            this.dirinfo = dirinfo;
        }

        public Long getSonglistSize() {
            return songlistSize;
        }

        public void setSonglistSize(Long songlistSize) {
            this.songlistSize = songlistSize;
        }

        public List<SonglistDTO> getSonglist() {
            return songlist;
        }

        public void setSonglist(List<SonglistDTO> songlist) {
            this.songlist = songlist;
        }

        public List<String> getSongtag() {
            return songtag;
        }

        public void setSongtag(List<String> songtag) {
            this.songtag = songtag;
        }

        public List<String> getToplistSong() {
            return toplistSong;
        }

        public void setToplistSong(List<String> toplistSong) {
            this.toplistSong = toplistSong;
        }

        public Boolean getToplistNolimit() {
            return toplistNolimit;
        }

        public void setToplistNolimit(Boolean toplistNolimit) {
            this.toplistNolimit = toplistNolimit;
        }

        public Long getLoginUin() {
            return loginUin;
        }

        public void setLoginUin(Long loginUin) {
            this.loginUin = loginUin;
        }

        public List<String> getInvalidSong() {
            return invalidSong;
        }

        public void setInvalidSong(List<String> invalidSong) {
            this.invalidSong = invalidSong;
        }

        public List<String> getFilteredSong() {
            return filteredSong;
        }

        public void setFilteredSong(List<String> filteredSong) {
            this.filteredSong = filteredSong;
        }

        public List<String> getAdList() {
            return adList;
        }

        public void setAdList(List<String> adList) {
            this.adList = adList;
        }

        public Long getTotalSongNum() {
            return totalSongNum;
        }

        public void setTotalSongNum(Long totalSongNum) {
            this.totalSongNum = totalSongNum;
        }

        public String getEncryptLogin() {
            return encryptLogin;
        }

        public void setEncryptLogin(String encryptLogin) {
            this.encryptLogin = encryptLogin;
        }

        public Long getCt() {
            return ct;
        }

        public void setCt(Long ct) {
            this.ct = ct;
        }

        public Long getCv() {
            return cv;
        }

        public void setCv(Long cv) {
            this.cv = cv;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public List<String> getOrderlist() {
            return orderlist;
        }

        public void setOrderlist(List<String> orderlist) {
            this.orderlist = orderlist;
        }

        public List<String> getBirthday() {
            return birthday;
        }

        public void setBirthday(List<String> birthday) {
            this.birthday = birthday;
        }

        public AiExtDTO getAiExt() {
            return aiExt;
        }

        public void setAiExt(AiExtDTO aiExt) {
            this.aiExt = aiExt;
        }

        public List<String> getQuickListenVid() {
            return quickListenVid;
        }

        public void setQuickListenVid(List<String> quickListenVid) {
            this.quickListenVid = quickListenVid;
        }

        public Long getBitflag() {
            return bitflag;
        }

        public void setBitflag(Long bitflag) {
            this.bitflag = bitflag;
        }

        public Long getHasmore() {
            return hasmore;
        }

        public void setHasmore(Long hasmore) {
            this.hasmore = hasmore;
        }

        public CmtURLBykeyDTO getCmturlBykey() {
            return cmturlBykey;
        }

        public void setCmturlBykey(CmtURLBykeyDTO cmturlBykey) {
            this.cmturlBykey = cmturlBykey;
        }

        public String getSrfIp() {
            return srfIp;
        }

        public void setSrfIp(String srfIp) {
            this.srfIp = srfIp;
        }

        public String getReferer() {
            return referer;
        }

        public void setReferer(String referer) {
            this.referer = referer;
        }

        public Long getNamedflag() {
            return namedflag;
        }

        public void setNamedflag(Long namedflag) {
            this.namedflag = namedflag;
        }

        public Long getIsAd() {
            return isAd;
        }

        public void setIsAd(Long isAd) {
            this.isAd = isAd;
        }

        public String getAdTitle() {
            return adTitle;
        }

        public void setAdTitle(String adTitle) {
            this.adTitle = adTitle;
        }

        public String getAdUrl() {
            return adUrl;
        }

        public void setAdUrl(String adUrl) {
            this.adUrl = adUrl;
        }

        public Long getRecomUgcValid() {
            return recomUgcValid;
        }

        public void setRecomUgcValid(Long recomUgcValid) {
            this.recomUgcValid = recomUgcValid;
        }

        public static class DirinfoDTO {
            @JsonProperty("id")
            private Long id;
            @JsonProperty("host_uin")
            private Long hostUin;
            @JsonProperty("dirid")
            private Long dirid;
            @JsonProperty("title")
            private String title;
            @JsonProperty("picurl")
            private String picurl;
            @JsonProperty("picid")
            private Long picid;
            @JsonProperty("desc")
            private String desc;
            @JsonProperty("vec_tagid")
            private List<String> vecTagid;
            @JsonProperty("vec_tagname")
            private List<String> vecTagname;
            @JsonProperty("ctime")
            private Long ctime;
            @JsonProperty("mtime")
            private Long mtime;
            @JsonProperty("listennum")
            private Long listennum;
            @JsonProperty("ordernum")
            private Long ordernum;
            @JsonProperty("picmid")
            private String picmid;
            @JsonProperty("dirtype")
            private Long dirtype;
            @JsonProperty("host_nick")
            private String hostNick;
            @JsonProperty("songnum")
            private Long songnum;
            @JsonProperty("ordertime")
            private Long ordertime;
            @JsonProperty("show")
            private Long show;
            @JsonProperty("picurl2")
            private String picurl2;
            @JsonProperty("song_update_time")
            private Long songUpdateTime;
            @JsonProperty("song_update_num")
            private Long songUpdateNum;
            @JsonProperty("disstype")
            private Long disstype;
            @JsonProperty("ai_uin")
            private Long aiUin;
            @JsonProperty("dv2")
            private Long dv2;
            @JsonProperty("dir_show")
            private Long dirShow;
            @JsonProperty("encrypt_uin")
            private String encryptUin;
            @JsonProperty("encrypt_ai_uin")
            private String encryptAiUin;
            @JsonProperty("owndir")
            private Long owndir;
            @JsonProperty("headurl")
            private String headurl;
            @JsonProperty("tag")
            private List<String> tag;
            @JsonProperty("creator")
            private CreatorDTO creator;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("edge_mark")
            private String edgeMark;
            @JsonProperty("layer_url")
            private String layerUrl;
            @JsonProperty("ext1")
            private String ext1;
            @JsonProperty("ext2")
            private String ext2;
            @JsonProperty("origin_title")
            private String originTitle;
            @JsonProperty("ad_tag")
            private Boolean adTag;
            @JsonProperty("aiToast")
            private String aiToast;
            @JsonProperty("role")
            private Long role;
            @JsonProperty("rl2")
            private Long rl2;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public Long getHostUin() {
                return hostUin;
            }

            public void setHostUin(Long hostUin) {
                this.hostUin = hostUin;
            }

            public Long getDirid() {
                return dirid;
            }

            public void setDirid(Long dirid) {
                this.dirid = dirid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPicurl() {
                return picurl;
            }

            public void setPicurl(String picurl) {
                this.picurl = picurl;
            }

            public Long getPicid() {
                return picid;
            }

            public void setPicid(Long picid) {
                this.picid = picid;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public List<String> getVecTagid() {
                return vecTagid;
            }

            public void setVecTagid(List<String> vecTagid) {
                this.vecTagid = vecTagid;
            }

            public List<String> getVecTagname() {
                return vecTagname;
            }

            public void setVecTagname(List<String> vecTagname) {
                this.vecTagname = vecTagname;
            }

            public Long getCtime() {
                return ctime;
            }

            public void setCtime(Long ctime) {
                this.ctime = ctime;
            }

            public Long getMtime() {
                return mtime;
            }

            public void setMtime(Long mtime) {
                this.mtime = mtime;
            }

            public Long getListennum() {
                return listennum;
            }

            public void setListennum(Long listennum) {
                this.listennum = listennum;
            }

            public Long getOrdernum() {
                return ordernum;
            }

            public void setOrdernum(Long ordernum) {
                this.ordernum = ordernum;
            }

            public String getPicmid() {
                return picmid;
            }

            public void setPicmid(String picmid) {
                this.picmid = picmid;
            }

            public Long getDirtype() {
                return dirtype;
            }

            public void setDirtype(Long dirtype) {
                this.dirtype = dirtype;
            }

            public String getHostNick() {
                return hostNick;
            }

            public void setHostNick(String hostNick) {
                this.hostNick = hostNick;
            }

            public Long getSongnum() {
                return songnum;
            }

            public void setSongnum(Long songnum) {
                this.songnum = songnum;
            }

            public Long getOrdertime() {
                return ordertime;
            }

            public void setOrdertime(Long ordertime) {
                this.ordertime = ordertime;
            }

            public Long getShow() {
                return show;
            }

            public void setShow(Long show) {
                this.show = show;
            }

            public String getPicurl2() {
                return picurl2;
            }

            public void setPicurl2(String picurl2) {
                this.picurl2 = picurl2;
            }

            public Long getSongUpdateTime() {
                return songUpdateTime;
            }

            public void setSongUpdateTime(Long songUpdateTime) {
                this.songUpdateTime = songUpdateTime;
            }

            public Long getSongUpdateNum() {
                return songUpdateNum;
            }

            public void setSongUpdateNum(Long songUpdateNum) {
                this.songUpdateNum = songUpdateNum;
            }

            public Long getDisstype() {
                return disstype;
            }

            public void setDisstype(Long disstype) {
                this.disstype = disstype;
            }

            public Long getAiUin() {
                return aiUin;
            }

            public void setAiUin(Long aiUin) {
                this.aiUin = aiUin;
            }

            public Long getDv2() {
                return dv2;
            }

            public void setDv2(Long dv2) {
                this.dv2 = dv2;
            }

            public Long getDirShow() {
                return dirShow;
            }

            public void setDirShow(Long dirShow) {
                this.dirShow = dirShow;
            }

            public String getEncryptUin() {
                return encryptUin;
            }

            public void setEncryptUin(String encryptUin) {
                this.encryptUin = encryptUin;
            }

            public String getEncryptAiUin() {
                return encryptAiUin;
            }

            public void setEncryptAiUin(String encryptAiUin) {
                this.encryptAiUin = encryptAiUin;
            }

            public Long getOwndir() {
                return owndir;
            }

            public void setOwndir(Long owndir) {
                this.owndir = owndir;
            }

            public String getHeadurl() {
                return headurl;
            }

            public void setHeadurl(String headurl) {
                this.headurl = headurl;
            }

            public List<String> getTag() {
                return tag;
            }

            public void setTag(List<String> tag) {
                this.tag = tag;
            }

            public CreatorDTO getCreator() {
                return creator;
            }

            public void setCreator(CreatorDTO creator) {
                this.creator = creator;
            }

            public Long getStatus() {
                return status;
            }

            public void setStatus(Long status) {
                this.status = status;
            }

            public String getEdgeMark() {
                return edgeMark;
            }

            public void setEdgeMark(String edgeMark) {
                this.edgeMark = edgeMark;
            }

            public String getLayerUrl() {
                return layerUrl;
            }

            public void setLayerUrl(String layerUrl) {
                this.layerUrl = layerUrl;
            }

            public String getExt1() {
                return ext1;
            }

            public void setExt1(String ext1) {
                this.ext1 = ext1;
            }

            public String getExt2() {
                return ext2;
            }

            public void setExt2(String ext2) {
                this.ext2 = ext2;
            }

            public String getOriginTitle() {
                return originTitle;
            }

            public void setOriginTitle(String originTitle) {
                this.originTitle = originTitle;
            }

            public Boolean getAdTag() {
                return adTag;
            }

            public void setAdTag(Boolean adTag) {
                this.adTag = adTag;
            }

            public String getAiToast() {
                return aiToast;
            }

            public void setAiToast(String aiToast) {
                this.aiToast = aiToast;
            }

            public Long getRole() {
                return role;
            }

            public void setRole(Long role) {
                this.role = role;
            }

            public Long getRl2() {
                return rl2;
            }

            public void setRl2(Long rl2) {
                this.rl2 = rl2;
            }

            public static class CreatorDTO {
                @JsonProperty("musicid")
                private Long musicid;
                @JsonProperty("type")
                private Long type;
                @JsonProperty("singerid")
                private Long singerid;
                @JsonProperty("nick")
                private String nick;
                @JsonProperty("headurl")
                private String headurl;
                @JsonProperty("ifpicurl")
                private String ifpicurl;
                @JsonProperty("encrypt_uin")
                private String encryptUin;
                @JsonProperty("isVip")
                private Long isVip;
                @JsonProperty("ai_uin")
                private Long aiUin;
                @JsonProperty("encrypt_ai_uin")
                private String encryptAiUin;

                public Long getMusicid() {
                    return musicid;
                }

                public void setMusicid(Long musicid) {
                    this.musicid = musicid;
                }

                public Long getType() {
                    return type;
                }

                public void setType(Long type) {
                    this.type = type;
                }

                public Long getSingerid() {
                    return singerid;
                }

                public void setSingerid(Long singerid) {
                    this.singerid = singerid;
                }

                public String getNick() {
                    return nick;
                }

                public void setNick(String nick) {
                    this.nick = nick;
                }

                public String getHeadurl() {
                    return headurl;
                }

                public void setHeadurl(String headurl) {
                    this.headurl = headurl;
                }

                public String getIfpicurl() {
                    return ifpicurl;
                }

                public void setIfpicurl(String ifpicurl) {
                    this.ifpicurl = ifpicurl;
                }

                public String getEncryptUin() {
                    return encryptUin;
                }

                public void setEncryptUin(String encryptUin) {
                    this.encryptUin = encryptUin;
                }

                public Long getIsVip() {
                    return isVip;
                }

                public void setIsVip(Long isVip) {
                    this.isVip = isVip;
                }

                public Long getAiUin() {
                    return aiUin;
                }

                public void setAiUin(Long aiUin) {
                    this.aiUin = aiUin;
                }

                public String getEncryptAiUin() {
                    return encryptAiUin;
                }

                public void setEncryptAiUin(String encryptAiUin) {
                    this.encryptAiUin = encryptAiUin;
                }
            }
        }

        public static class AiExtDTO {
            @JsonProperty("CountdownTime")
            private Long countdownTime;
            @JsonProperty("ISJoinExp")
            private Boolean iSJoinExp;
            @JsonProperty("blkCntDnlist")
            private List<String> blkCntDnlist;
            @JsonProperty("limitVipSongNum")
            private Long limitVipSongNum;
            @JsonProperty("aiHelperCard")
            private AiHelperCardDTO aiHelperCard;

            public Long getCountdownTime() {
                return countdownTime;
            }

            public void setCountdownTime(Long countdownTime) {
                this.countdownTime = countdownTime;
            }

            public Boolean getISJoinExp() {
                return iSJoinExp;
            }

            public void setISJoinExp(Boolean iSJoinExp) {
                this.iSJoinExp = iSJoinExp;
            }

            public List<String> getBlkCntDnlist() {
                return blkCntDnlist;
            }

            public void setBlkCntDnlist(List<String> blkCntDnlist) {
                this.blkCntDnlist = blkCntDnlist;
            }

            public Long getLimitVipSongNum() {
                return limitVipSongNum;
            }

            public void setLimitVipSongNum(Long limitVipSongNum) {
                this.limitVipSongNum = limitVipSongNum;
            }

            public AiHelperCardDTO getAiHelperCard() {
                return aiHelperCard;
            }

            public void setAiHelperCard(AiHelperCardDTO aiHelperCard) {
                this.aiHelperCard = aiHelperCard;
            }

            public static class AiHelperCardDTO {
                @JsonProperty("title")
                private String title;
                @JsonProperty("desc_icon")
                private String descIcon;
                @JsonProperty("desc_text")
                private String descText;
                @JsonProperty("bubble")
                private String bubble;
                @JsonProperty("scheme")
                private String scheme;
                @JsonProperty("aiTag")
                private List<String> aiTag;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDescIcon() {
                    return descIcon;
                }

                public void setDescIcon(String descIcon) {
                    this.descIcon = descIcon;
                }

                public String getDescText() {
                    return descText;
                }

                public void setDescText(String descText) {
                    this.descText = descText;
                }

                public String getBubble() {
                    return bubble;
                }

                public void setBubble(String bubble) {
                    this.bubble = bubble;
                }

                public String getScheme() {
                    return scheme;
                }

                public void setScheme(String scheme) {
                    this.scheme = scheme;
                }

                public List<String> getAiTag() {
                    return aiTag;
                }

                public void setAiTag(List<String> aiTag) {
                    this.aiTag = aiTag;
                }
            }
        }

        public static class CmtURLBykeyDTO {
            @JsonProperty("url_key")
            private String urlKey;
            @JsonProperty("url_params")
            private String urlParams;

            public String getUrlKey() {
                return urlKey;
            }

            public void setUrlKey(String urlKey) {
                this.urlKey = urlKey;
            }

            public String getUrlParams() {
                return urlParams;
            }

            public void setUrlParams(String urlParams) {
                this.urlParams = urlParams;
            }
        }

        public static class SonglistDTO {
            @JsonProperty("id")
            private Long id;
            @JsonProperty("type")
            private Long type;
            @JsonProperty("songtype")
            private Long songtype;
            @JsonProperty("version")
            private Long version;
            @JsonProperty("trace")
            private String trace;
            @JsonProperty("mid")
            private String mid;
            @JsonProperty("name")
            private String name;
            @JsonProperty("label")
            private String label;
            @JsonProperty("title")
            private String title;
            @JsonProperty("subtitle")
            private String subtitle;
            @JsonProperty("interval")
            private Long interval;
            @JsonProperty("isonly")
            private Long isonly;
            @JsonProperty("language")
            private Long language;
            @JsonProperty("genre")
            private Long genre;
            @JsonProperty("index_cd")
            private Long indexCd;
            @JsonProperty("index_album")
            private Long indexAlbum;
            @JsonProperty("status")
            private Long status;
            @JsonProperty("fnote")
            private Long fnote;
            @JsonProperty("url")
            private String url;
            @JsonProperty("time_public")
            private String timePublic;
            @JsonProperty("singer")
            private List<SingerDTO> singer;
            @JsonProperty("album")
            private AlbumDTO album;
            @JsonProperty("mv")
            private MvDTO mv;
            @JsonProperty("ksong")
            private KsongDTO ksong;
            @JsonProperty("file")
            private FileDTO file;
            @JsonProperty("volume")
            private VolumeDTO volume;
            @JsonProperty("pay")
            private PayDTO pay;
            @JsonProperty("action")
            private ActionDTO action;
            @JsonProperty("new_icon")
            private Long newIcon;
            @JsonProperty("tid")
            private Long tid;
            @JsonProperty("ov")
            private Long ov;
            @JsonProperty("sa")
            private Long sa;
            @JsonProperty("es")
            private String es;
            @JsonProperty("vs")
            private List<String> vs;
            @JsonProperty("vf")
            private List<Double> vf;
            @JsonProperty("data_type")
            private Long dataType;
            @JsonProperty("pingpong")
            private String pingpong;
            @JsonProperty("bpm")
            private Long bpm;
            @JsonProperty("ktag")
            private String ktag;
            @JsonProperty("team")
            private String team;
            @JsonProperty("bf")
            private Long bf;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public Long getType() {
                return type;
            }

            public void setType(Long type) {
                this.type = type;
            }

            public Long getSongtype() {
                return songtype;
            }

            public void setSongtype(Long songtype) {
                this.songtype = songtype;
            }

            public Long getVersion() {
                return version;
            }

            public void setVersion(Long version) {
                this.version = version;
            }

            public String getTrace() {
                return trace;
            }

            public void setTrace(String trace) {
                this.trace = trace;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public Long getInterval() {
                return interval;
            }

            public void setInterval(Long interval) {
                this.interval = interval;
            }

            public Long getIsonly() {
                return isonly;
            }

            public void setIsonly(Long isonly) {
                this.isonly = isonly;
            }

            public Long getLanguage() {
                return language;
            }

            public void setLanguage(Long language) {
                this.language = language;
            }

            public Long getGenre() {
                return genre;
            }

            public void setGenre(Long genre) {
                this.genre = genre;
            }

            public Long getIndexCd() {
                return indexCd;
            }

            public void setIndexCd(Long indexCd) {
                this.indexCd = indexCd;
            }

            public Long getIndexAlbum() {
                return indexAlbum;
            }

            public void setIndexAlbum(Long indexAlbum) {
                this.indexAlbum = indexAlbum;
            }

            public Long getStatus() {
                return status;
            }

            public void setStatus(Long status) {
                this.status = status;
            }

            public Long getFnote() {
                return fnote;
            }

            public void setFnote(Long fnote) {
                this.fnote = fnote;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTimePublic() {
                return timePublic;
            }

            public void setTimePublic(String timePublic) {
                this.timePublic = timePublic;
            }

            public List<SingerDTO> getSinger() {
                return singer;
            }

            public void setSinger(List<SingerDTO> singer) {
                this.singer = singer;
            }

            public AlbumDTO getAlbum() {
                return album;
            }

            public void setAlbum(AlbumDTO album) {
                this.album = album;
            }

            public MvDTO getMv() {
                return mv;
            }

            public void setMv(MvDTO mv) {
                this.mv = mv;
            }

            public KsongDTO getKsong() {
                return ksong;
            }

            public void setKsong(KsongDTO ksong) {
                this.ksong = ksong;
            }

            public FileDTO getFile() {
                return file;
            }

            public void setFile(FileDTO file) {
                this.file = file;
            }

            public VolumeDTO getVolume() {
                return volume;
            }

            public void setVolume(VolumeDTO volume) {
                this.volume = volume;
            }

            public PayDTO getPay() {
                return pay;
            }

            public void setPay(PayDTO pay) {
                this.pay = pay;
            }

            public ActionDTO getAction() {
                return action;
            }

            public void setAction(ActionDTO action) {
                this.action = action;
            }

            public Long getNewIcon() {
                return newIcon;
            }

            public void setNewIcon(Long newIcon) {
                this.newIcon = newIcon;
            }

            public Long getTid() {
                return tid;
            }

            public void setTid(Long tid) {
                this.tid = tid;
            }

            public Long getOv() {
                return ov;
            }

            public void setOv(Long ov) {
                this.ov = ov;
            }

            public Long getSa() {
                return sa;
            }

            public void setSa(Long sa) {
                this.sa = sa;
            }

            public String getEs() {
                return es;
            }

            public void setEs(String es) {
                this.es = es;
            }

            public List<String> getVs() {
                return vs;
            }

            public void setVs(List<String> vs) {
                this.vs = vs;
            }

            public List<Double> getVf() {
                return vf;
            }

            public void setVf(List<Double> vf) {
                this.vf = vf;
            }

            public Long getDataType() {
                return dataType;
            }

            public void setDataType(Long dataType) {
                this.dataType = dataType;
            }

            public String getPingpong() {
                return pingpong;
            }

            public void setPingpong(String pingpong) {
                this.pingpong = pingpong;
            }

            public Long getBpm() {
                return bpm;
            }

            public void setBpm(Long bpm) {
                this.bpm = bpm;
            }

            public String getKtag() {
                return ktag;
            }

            public void setKtag(String ktag) {
                this.ktag = ktag;
            }

            public String getTeam() {
                return team;
            }

            public void setTeam(String team) {
                this.team = team;
            }

            public Long getBf() {
                return bf;
            }

            public void setBf(Long bf) {
                this.bf = bf;
            }

            public static class AlbumDTO {
                @JsonProperty("id")
                private Long id;
                @JsonProperty("mid")
                private String mid;
                @JsonProperty("name")
                private String name;
                @JsonProperty("title")
                private String title;
                @JsonProperty("pmid")
                private String pmid;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getMid() {
                    return mid;
                }

                public void setMid(String mid) {
                    this.mid = mid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPmid() {
                    return pmid;
                }

                public void setPmid(String pmid) {
                    this.pmid = pmid;
                }
            }

            public static class MvDTO {
                @JsonProperty("id")
                private Long id;
                @JsonProperty("vid")
                private String vid;
                @JsonProperty("vt")
                private Long vt;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getVid() {
                    return vid;
                }

                public void setVid(String vid) {
                    this.vid = vid;
                }

                public Long getVt() {
                    return vt;
                }

                public void setVt(Long vt) {
                    this.vt = vt;
                }
            }

            public static class KsongDTO {
                @JsonProperty("id")
                private Long id;
                @JsonProperty("mid")
                private String mid;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getMid() {
                    return mid;
                }

                public void setMid(String mid) {
                    this.mid = mid;
                }
            }

            public static class FileDTO {
                @JsonProperty("media_mid")
                private String mediaMid;
                @JsonProperty("size_try")
                private Long sizeTry;
                @JsonProperty("try_begin")
                private Long tryBegin;
                @JsonProperty("try_end")
                private Long tryEnd;
                @JsonProperty("size_24aac")
                private Long size24aac;
                @JsonProperty("size_48aac")
                private Long size48aac;
                @JsonProperty("size_96aac")
                private Long size96aac;
                @JsonProperty("size_128mp3")
                private Long size128mp3;
                @JsonProperty("size_192ogg")
                private Long size192ogg;
                @JsonProperty("size_192aac")
                private Long size192aac;
                @JsonProperty("size_320mp3")
                private Long size320mp3;
                @JsonProperty("size_flac")
                private Long sizeFlac;
                @JsonProperty("size_ape")
                private Long sizeApe;
                @JsonProperty("size_dts")
                private Long sizeDts;
                @JsonProperty("size_hires")
                private Long sizeHires;
                @JsonProperty("hires_sample")
                private Long hiresSample;
                @JsonProperty("hires_bitdepth")
                private Long hiresBitdepth;
                @JsonProperty("b_30s")
                private Long b30s;
                @JsonProperty("e_30s")
                private Long e30s;
                @JsonProperty("size_96ogg")
                private Long size96ogg;
                @JsonProperty("size_360ra")
                private List<String> size360ra;
                @JsonProperty("size_dolby")
                private Long sizeDolby;
                @JsonProperty("size_new")
                private List<Long> sizeNew;

                public String getMediaMid() {
                    return mediaMid;
                }

                public void setMediaMid(String mediaMid) {
                    this.mediaMid = mediaMid;
                }

                public Long getSizeTry() {
                    return sizeTry;
                }

                public void setSizeTry(Long sizeTry) {
                    this.sizeTry = sizeTry;
                }

                public Long getTryBegin() {
                    return tryBegin;
                }

                public void setTryBegin(Long tryBegin) {
                    this.tryBegin = tryBegin;
                }

                public Long getTryEnd() {
                    return tryEnd;
                }

                public void setTryEnd(Long tryEnd) {
                    this.tryEnd = tryEnd;
                }

                public Long getSize24aac() {
                    return size24aac;
                }

                public void setSize24aac(Long size24aac) {
                    this.size24aac = size24aac;
                }

                public Long getSize48aac() {
                    return size48aac;
                }

                public void setSize48aac(Long size48aac) {
                    this.size48aac = size48aac;
                }

                public Long getSize96aac() {
                    return size96aac;
                }

                public void setSize96aac(Long size96aac) {
                    this.size96aac = size96aac;
                }

                public Long getSize128mp3() {
                    return size128mp3;
                }

                public void setSize128mp3(Long size128mp3) {
                    this.size128mp3 = size128mp3;
                }

                public Long getSize192ogg() {
                    return size192ogg;
                }

                public void setSize192ogg(Long size192ogg) {
                    this.size192ogg = size192ogg;
                }

                public Long getSize192aac() {
                    return size192aac;
                }

                public void setSize192aac(Long size192aac) {
                    this.size192aac = size192aac;
                }

                public Long getSize320mp3() {
                    return size320mp3;
                }

                public void setSize320mp3(Long size320mp3) {
                    this.size320mp3 = size320mp3;
                }

                public Long getSizeFlac() {
                    return sizeFlac;
                }

                public void setSizeFlac(Long sizeFlac) {
                    this.sizeFlac = sizeFlac;
                }

                public Long getSizeApe() {
                    return sizeApe;
                }

                public void setSizeApe(Long sizeApe) {
                    this.sizeApe = sizeApe;
                }

                public Long getSizeDts() {
                    return sizeDts;
                }

                public void setSizeDts(Long sizeDts) {
                    this.sizeDts = sizeDts;
                }

                public Long getSizeHires() {
                    return sizeHires;
                }

                public void setSizeHires(Long sizeHires) {
                    this.sizeHires = sizeHires;
                }

                public Long getHiresSample() {
                    return hiresSample;
                }

                public void setHiresSample(Long hiresSample) {
                    this.hiresSample = hiresSample;
                }

                public Long getHiresBitdepth() {
                    return hiresBitdepth;
                }

                public void setHiresBitdepth(Long hiresBitdepth) {
                    this.hiresBitdepth = hiresBitdepth;
                }

                public Long getB30s() {
                    return b30s;
                }

                public void setB30s(Long b30s) {
                    this.b30s = b30s;
                }

                public Long getE30s() {
                    return e30s;
                }

                public void setE30s(Long e30s) {
                    this.e30s = e30s;
                }

                public Long getSize96ogg() {
                    return size96ogg;
                }

                public void setSize96ogg(Long size96ogg) {
                    this.size96ogg = size96ogg;
                }

                public List<String> getSize360ra() {
                    return size360ra;
                }

                public void setSize360ra(List<String> size360ra) {
                    this.size360ra = size360ra;
                }

                public Long getSizeDolby() {
                    return sizeDolby;
                }

                public void setSizeDolby(Long sizeDolby) {
                    this.sizeDolby = sizeDolby;
                }

                public List<Long> getSizeNew() {
                    return sizeNew;
                }

                public void setSizeNew(List<Long> sizeNew) {
                    this.sizeNew = sizeNew;
                }
            }

            public static class VolumeDTO {
                @JsonProperty("gain")
                private Double gain;
                @JsonProperty("peak")
                private Double peak;
                @JsonProperty("lra")
                private Double lra;

                public Double getGain() {
                    return gain;
                }

                public void setGain(Double gain) {
                    this.gain = gain;
                }

                public Double getPeak() {
                    return peak;
                }

                public void setPeak(Double peak) {
                    this.peak = peak;
                }

                public Double getLra() {
                    return lra;
                }

                public void setLra(Double lra) {
                    this.lra = lra;
                }
            }

            public static class PayDTO {
                @JsonProperty("pay_month")
                private Long payMonth;
                @JsonProperty("price_track")
                private Long priceTrack;
                @JsonProperty("price_album")
                private Long priceAlbum;
                @JsonProperty("pay_play")
                private Long payPlay;
                @JsonProperty("pay_down")
                private Long payDown;
                @JsonProperty("pay_status")
                private Long payStatus;
                @JsonProperty("time_free")
                private Long timeFree;

                public Long getPayMonth() {
                    return payMonth;
                }

                public void setPayMonth(Long payMonth) {
                    this.payMonth = payMonth;
                }

                public Long getPriceTrack() {
                    return priceTrack;
                }

                public void setPriceTrack(Long priceTrack) {
                    this.priceTrack = priceTrack;
                }

                public Long getPriceAlbum() {
                    return priceAlbum;
                }

                public void setPriceAlbum(Long priceAlbum) {
                    this.priceAlbum = priceAlbum;
                }

                public Long getPayPlay() {
                    return payPlay;
                }

                public void setPayPlay(Long payPlay) {
                    this.payPlay = payPlay;
                }

                public Long getPayDown() {
                    return payDown;
                }

                public void setPayDown(Long payDown) {
                    this.payDown = payDown;
                }

                public Long getPayStatus() {
                    return payStatus;
                }

                public void setPayStatus(Long payStatus) {
                    this.payStatus = payStatus;
                }

                public Long getTimeFree() {
                    return timeFree;
                }

                public void setTimeFree(Long timeFree) {
                    this.timeFree = timeFree;
                }
            }

            public static class ActionDTO {
                @JsonProperty("switch")
                private Long switchX;
                @JsonProperty("switches")
                private Long switches;
                @JsonProperty("msgid")
                private Long msgid;
                @JsonProperty("alert")
                private Long alert;
                @JsonProperty("msgshare")
                private Long msgshare;
                @JsonProperty("msgfav")
                private Long msgfav;
                @JsonProperty("msgdown")
                private Long msgdown;
                @JsonProperty("msgpay")
                private Long msgpay;
                @JsonProperty("icons")
                private Long icons;
                @JsonProperty("switch2")
                private Long switch2;
                @JsonProperty("icon2")
                private Long icon2;

                public Long getSwitchX() {
                    return switchX;
                }

                public void setSwitchX(Long switchX) {
                    this.switchX = switchX;
                }

                public Long getSwitches() {
                    return switches;
                }

                public void setSwitches(Long switches) {
                    this.switches = switches;
                }

                public Long getMsgid() {
                    return msgid;
                }

                public void setMsgid(Long msgid) {
                    this.msgid = msgid;
                }

                public Long getAlert() {
                    return alert;
                }

                public void setAlert(Long alert) {
                    this.alert = alert;
                }

                public Long getMsgshare() {
                    return msgshare;
                }

                public void setMsgshare(Long msgshare) {
                    this.msgshare = msgshare;
                }

                public Long getMsgfav() {
                    return msgfav;
                }

                public void setMsgfav(Long msgfav) {
                    this.msgfav = msgfav;
                }

                public Long getMsgdown() {
                    return msgdown;
                }

                public void setMsgdown(Long msgdown) {
                    this.msgdown = msgdown;
                }

                public Long getMsgpay() {
                    return msgpay;
                }

                public void setMsgpay(Long msgpay) {
                    this.msgpay = msgpay;
                }

                public Long getIcons() {
                    return icons;
                }

                public void setIcons(Long icons) {
                    this.icons = icons;
                }

                public Long getSwitch2() {
                    return switch2;
                }

                public void setSwitch2(Long switch2) {
                    this.switch2 = switch2;
                }

                public Long getIcon2() {
                    return icon2;
                }

                public void setIcon2(Long icon2) {
                    this.icon2 = icon2;
                }
            }

            public static class SingerDTO {
                @JsonProperty("id")
                private Long id;
                @JsonProperty("mid")
                private String mid;
                @JsonProperty("name")
                private String name;
                @JsonProperty("title")
                private String title;

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getMid() {
                    return mid;
                }

                public void setMid(String mid) {
                    this.mid = mid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }
        }
    }
}
