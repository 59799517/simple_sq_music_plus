package com.sqmusicplus.v3.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname MusicInfoNeteaseResult
 * @Description 单曲详情
 * @Version 1.0.0
 * @Date 2024/2/22 16:25
 * @Created by SQ
 */

public class MusicInfoNeteaseResult {


    @JSONField(name = "privileges")
    private List<PrivilegesDTO> privileges;
    @JSONField(name = "code")
    private Long code;
    @JSONField(name = "songs")
    private List<SongsDTO> songs;

    public List<PrivilegesDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegesDTO> privileges) {
        this.privileges = privileges;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public List<SongsDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongsDTO> songs) {
        this.songs = songs;
    }

    public static class PrivilegesDTO {
        @JSONField(name = "flag")
        private Long flag;
        @JSONField(name = "dlLevel")
        private String dlLevel;
        @JSONField(name = "subp")
        private Long subp;
        @JSONField(name = "fl")
        private Long fl;
        @JSONField(name = "fee")
        private Long fee;
        @JSONField(name = "dl")
        private Long dl;
        @JSONField(name = "plLevel")
        private String plLevel;
        @JSONField(name = "maxBrLevel")
        private String maxBrLevel;
        @JSONField(name = "rightSource")
        private Long rightSource;
        @JSONField(name = "maxbr")
        private Long maxbr;
        @JSONField(name = "id")
        private Long id;
        @JSONField(name = "sp")
        private Long sp;
        @JSONField(name = "payed")
        private Long payed;
        @JSONField(name = "st")
        private Long st;
        @JSONField(name = "chargeInfoList")
        private List<ChargeInfoListDTO> chargeInfoList;
        @JSONField(name = "freeTrialPrivilege")
        private FreeTrialPrivilegeDTO freeTrialPrivilege;
        @JSONField(name = "downloadMaxbr")
        private Long downloadMaxbr;
        @JSONField(name = "downloadMaxBrLevel")
        private String downloadMaxBrLevel;
        @JSONField(name = "cp")
        private Long cp;
        @JSONField(name = "preSell")
        private Boolean preSell;
        @JSONField(name = "playMaxBrLevel")
        private String playMaxBrLevel;
        @JSONField(name = "cs")
        private Boolean cs;
        @JSONField(name = "toast")
        private Boolean toast;
        @JSONField(name = "playMaxbr")
        private Long playMaxbr;
        @JSONField(name = "flLevel")
        private String flLevel;
        @JSONField(name = "pl")
        private Long pl;

        public Long getFlag() {
            return flag;
        }

        public void setFlag(Long flag) {
            this.flag = flag;
        }

        public String getDlLevel() {
            return dlLevel;
        }

        public void setDlLevel(String dlLevel) {
            this.dlLevel = dlLevel;
        }

        public Long getSubp() {
            return subp;
        }

        public void setSubp(Long subp) {
            this.subp = subp;
        }

        public Long getFl() {
            return fl;
        }

        public void setFl(Long fl) {
            this.fl = fl;
        }

        public Long getFee() {
            return fee;
        }

        public void setFee(Long fee) {
            this.fee = fee;
        }

        public Long getDl() {
            return dl;
        }

        public void setDl(Long dl) {
            this.dl = dl;
        }

        public String getPlLevel() {
            return plLevel;
        }

        public void setPlLevel(String plLevel) {
            this.plLevel = plLevel;
        }

        public String getMaxBrLevel() {
            return maxBrLevel;
        }

        public void setMaxBrLevel(String maxBrLevel) {
            this.maxBrLevel = maxBrLevel;
        }

        public Long getRightSource() {
            return rightSource;
        }

        public void setRightSource(Long rightSource) {
            this.rightSource = rightSource;
        }

        public Long getMaxbr() {
            return maxbr;
        }

        public void setMaxbr(Long maxbr) {
            this.maxbr = maxbr;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getSp() {
            return sp;
        }

        public void setSp(Long sp) {
            this.sp = sp;
        }

        public Long getPayed() {
            return payed;
        }

        public void setPayed(Long payed) {
            this.payed = payed;
        }

        public Long getSt() {
            return st;
        }

        public void setSt(Long st) {
            this.st = st;
        }

        public List<ChargeInfoListDTO> getChargeInfoList() {
            return chargeInfoList;
        }

        public void setChargeInfoList(List<ChargeInfoListDTO> chargeInfoList) {
            this.chargeInfoList = chargeInfoList;
        }

        public FreeTrialPrivilegeDTO getFreeTrialPrivilege() {
            return freeTrialPrivilege;
        }

        public void setFreeTrialPrivilege(FreeTrialPrivilegeDTO freeTrialPrivilege) {
            this.freeTrialPrivilege = freeTrialPrivilege;
        }

        public Long getDownloadMaxbr() {
            return downloadMaxbr;
        }

        public void setDownloadMaxbr(Long downloadMaxbr) {
            this.downloadMaxbr = downloadMaxbr;
        }

        public String getDownloadMaxBrLevel() {
            return downloadMaxBrLevel;
        }

        public void setDownloadMaxBrLevel(String downloadMaxBrLevel) {
            this.downloadMaxBrLevel = downloadMaxBrLevel;
        }

        public Long getCp() {
            return cp;
        }

        public void setCp(Long cp) {
            this.cp = cp;
        }

        public Boolean getPreSell() {
            return preSell;
        }

        public void setPreSell(Boolean preSell) {
            this.preSell = preSell;
        }

        public String getPlayMaxBrLevel() {
            return playMaxBrLevel;
        }

        public void setPlayMaxBrLevel(String playMaxBrLevel) {
            this.playMaxBrLevel = playMaxBrLevel;
        }

        public Boolean getCs() {
            return cs;
        }

        public void setCs(Boolean cs) {
            this.cs = cs;
        }

        public Boolean getToast() {
            return toast;
        }

        public void setToast(Boolean toast) {
            this.toast = toast;
        }

        public Long getPlayMaxbr() {
            return playMaxbr;
        }

        public void setPlayMaxbr(Long playMaxbr) {
            this.playMaxbr = playMaxbr;
        }

        public String getFlLevel() {
            return flLevel;
        }

        public void setFlLevel(String flLevel) {
            this.flLevel = flLevel;
        }

        public Long getPl() {
            return pl;
        }

        public void setPl(Long pl) {
            this.pl = pl;
        }

        public static class FreeTrialPrivilegeDTO {
            @JSONField(name = "userConsumable")
            private Boolean userConsumable;
            @JSONField(name = "resConsumable")
            private Boolean resConsumable;

            public Boolean getUserConsumable() {
                return userConsumable;
            }

            public void setUserConsumable(Boolean userConsumable) {
                this.userConsumable = userConsumable;
            }

            public Boolean getResConsumable() {
                return resConsumable;
            }

            public void setResConsumable(Boolean resConsumable) {
                this.resConsumable = resConsumable;
            }
        }

        public static class ChargeInfoListDTO {
            @JSONField(name = "rate")
            private Long rate;
            @JSONField(name = "chargeType")
            private Long chargeType;

            public Long getRate() {
                return rate;
            }

            public void setRate(Long rate) {
                this.rate = rate;
            }

            public Long getChargeType() {
                return chargeType;
            }

            public void setChargeType(Long chargeType) {
                this.chargeType = chargeType;
            }
        }
    }

    public static class SongsDTO {
        @JSONField(name = "no")
        private Long no;
        @JSONField(name = "rt")
        private String rt;
        @JSONField(name = "copyright")
        private Long copyright;
        @JSONField(name = "fee")
        private Long fee;
        @JSONField(name = "mst")
        private Long mst;
        @JSONField(name = "pst")
        private Long pst;
        @JSONField(name = "pop")
        private Long pop;
        @JSONField(name = "dt")
        private Long dt;
        @JSONField(name = "rtype")
        private Long rtype;
        @JSONField(name = "s_id")
        private Long sId;

        @JSONField(name = "resourceState")
        private Boolean resourceState;
        @JSONField(name = "id")
        private Long id;
        @JSONField(name = "sq")
        private SqDTO sq;
        @JSONField(name = "st")
        private Long st;
        @JSONField(name = "cd")
        private String cd;
        @JSONField(name = "publishTime")
        private Long publishTime;
        @JSONField(name = "cf")
        private String cf;
        @JSONField(name = "originCoverType")
        private Long originCoverType;
        @JSONField(name = "h")
        private HDTO h;
        @JSONField(name = "hr")
        private HDTO hr;
        @JSONField(name = "mv")
        private Long mv;
        @JSONField(name = "al")
        private AlDTO al;
        @JSONField(name = "l")
        private LDTO l;
        @JSONField(name = "m")
        private MDTO m;
        @JSONField(name = "version")
        private Long version;
        @JSONField(name = "cp")
        private Long cp;



        @JSONField(name = "djId")
        private Long djId;
        @JSONField(name = "single")
        private Long single;
        @JSONField(name = "ar")
        private List<ArDTO> ar;
        @JSONField(name = "ftype")
        private Long ftype;
        @JSONField(name = "t")
        private Long t;
        @JSONField(name = "v")
        private Long v;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "mark")
        private Long mark;

        public Long getNo() {
            return no;
        }

        public void setNo(Long no) {
            this.no = no;
        }

        public String getRt() {
            return rt;
        }

        public void setRt(String rt) {
            this.rt = rt;
        }

        public Long getCopyright() {
            return copyright;
        }

        public void setCopyright(Long copyright) {
            this.copyright = copyright;
        }

        public Long getFee() {
            return fee;
        }

        public void setFee(Long fee) {
            this.fee = fee;
        }

        public Long getMst() {
            return mst;
        }

        public void setMst(Long mst) {
            this.mst = mst;
        }

        public Long getPst() {
            return pst;
        }

        public void setPst(Long pst) {
            this.pst = pst;
        }

        public Long getPop() {
            return pop;
        }

        public void setPop(Long pop) {
            this.pop = pop;
        }

        public Long getDt() {
            return dt;
        }

        public void setDt(Long dt) {
            this.dt = dt;
        }

        public Long getRtype() {
            return rtype;
        }

        public void setRtype(Long rtype) {
            this.rtype = rtype;
        }

        public Long getSId() {
            return sId;
        }

        public void setSId(Long sId) {
            this.sId = sId;
        }

        public Long getsId() {
            return sId;
        }

        public void setsId(Long sId) {
            this.sId = sId;
        }

        public HDTO getHr() {
            return hr;
        }

        public void setHr(HDTO hr) {
            this.hr = hr;
        }

        public Boolean getResourceState() {
            return resourceState;
        }

        public void setResourceState(Boolean resourceState) {
            this.resourceState = resourceState;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public SqDTO getSq() {
            return sq;
        }

        public void setSq(SqDTO sq) {
            this.sq = sq;
        }

        public Long getSt() {
            return st;
        }

        public void setSt(Long st) {
            this.st = st;
        }

        public String getCd() {
            return cd;
        }

        public void setCd(String cd) {
            this.cd = cd;
        }

        public Long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(Long publishTime) {
            this.publishTime = publishTime;
        }

        public String getCf() {
            return cf;
        }

        public void setCf(String cf) {
            this.cf = cf;
        }

        public Long getOriginCoverType() {
            return originCoverType;
        }

        public void setOriginCoverType(Long originCoverType) {
            this.originCoverType = originCoverType;
        }

        public HDTO getH() {
            return h;
        }

        public void setH(HDTO h) {
            this.h = h;
        }

        public Long getMv() {
            return mv;
        }

        public void setMv(Long mv) {
            this.mv = mv;
        }

        public AlDTO getAl() {
            return al;
        }

        public void setAl(AlDTO al) {
            this.al = al;
        }

        public LDTO getL() {
            return l;
        }

        public void setL(LDTO l) {
            this.l = l;
        }

        public MDTO getM() {
            return m;
        }

        public void setM(MDTO m) {
            this.m = m;
        }

        public Long getVersion() {
            return version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        public Long getCp() {
            return cp;
        }

        public void setCp(Long cp) {
            this.cp = cp;
        }





        public Long getDjId() {
            return djId;
        }

        public void setDjId(Long djId) {
            this.djId = djId;
        }

        public Long getSingle() {
            return single;
        }

        public void setSingle(Long single) {
            this.single = single;
        }

        public List<ArDTO> getAr() {
            return ar;
        }

        public void setAr(List<ArDTO> ar) {
            this.ar = ar;
        }

        public Long getFtype() {
            return ftype;
        }

        public void setFtype(Long ftype) {
            this.ftype = ftype;
        }

        public Long getT() {
            return t;
        }

        public void setT(Long t) {
            this.t = t;
        }

        public Long getV() {
            return v;
        }

        public void setV(Long v) {
            this.v = v;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getMark() {
            return mark;
        }

        public void setMark(Long mark) {
            this.mark = mark;
        }

        public static class SqDTO {
            @JSONField(name = "br")
            private Long br;
            @JSONField(name = "fid")
            private Long fid;
            @JSONField(name = "size")
            private Long size;
            @JSONField(name = "vd")
            private Long vd;
            @JSONField(name = "sr")
            private Long sr;

            public Long getBr() {
                return br;
            }

            public void setBr(Long br) {
                this.br = br;
            }

            public Long getFid() {
                return fid;
            }

            public void setFid(Long fid) {
                this.fid = fid;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }

            public Long getVd() {
                return vd;
            }

            public void setVd(Long vd) {
                this.vd = vd;
            }

            public Long getSr() {
                return sr;
            }

            public void setSr(Long sr) {
                this.sr = sr;
            }
        }

        public static class HDTO {
            @JSONField(name = "br")
            private Long br;
            @JSONField(name = "fid")
            private Long fid;
            @JSONField(name = "size")
            private Long size;
            @JSONField(name = "vd")
            private Long vd;
            @JSONField(name = "sr")
            private Long sr;

            public Long getBr() {
                return br;
            }

            public void setBr(Long br) {
                this.br = br;
            }

            public Long getFid() {
                return fid;
            }

            public void setFid(Long fid) {
                this.fid = fid;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }

            public Long getVd() {
                return vd;
            }

            public void setVd(Long vd) {
                this.vd = vd;
            }

            public Long getSr() {
                return sr;
            }

            public void setSr(Long sr) {
                this.sr = sr;
            }
        }

        public static class AlDTO {
            @JSONField(name = "picUrl")
            private String picUrl;
            @JSONField(name = "name")
            private String name;



            @JSONField(name = "pic_str")
            private String picStr;
            @JSONField(name = "id")
            private Long id;
            @JSONField(name = "pic")
            private Long pic;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }





            public String getPicStr() {
                return picStr;
            }

            public void setPicStr(String picStr) {
                this.picStr = picStr;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public Long getPic() {
                return pic;
            }

            public void setPic(Long pic) {
                this.pic = pic;
            }
        }

        public static class LDTO {
            @JSONField(name = "br")
            private Long br;
            @JSONField(name = "fid")
            private Long fid;
            @JSONField(name = "size")
            private Long size;
            @JSONField(name = "vd")
            private Long vd;
            @JSONField(name = "sr")
            private Long sr;

            public Long getBr() {
                return br;
            }

            public void setBr(Long br) {
                this.br = br;
            }

            public Long getFid() {
                return fid;
            }

            public void setFid(Long fid) {
                this.fid = fid;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }

            public Long getVd() {
                return vd;
            }

            public void setVd(Long vd) {
                this.vd = vd;
            }

            public Long getSr() {
                return sr;
            }

            public void setSr(Long sr) {
                this.sr = sr;
            }
        }

        public static class MDTO {
            @JSONField(name = "br")
            private Long br;
            @JSONField(name = "fid")
            private Long fid;
            @JSONField(name = "size")
            private Long size;
            @JSONField(name = "vd")
            private Long vd;
            @JSONField(name = "sr")
            private Long sr;

            public Long getBr() {
                return br;
            }

            public void setBr(Long br) {
                this.br = br;
            }

            public Long getFid() {
                return fid;
            }

            public void setFid(Long fid) {
                this.fid = fid;
            }

            public Long getSize() {
                return size;
            }

            public void setSize(Long size) {
                this.size = size;
            }

            public Long getVd() {
                return vd;
            }

            public void setVd(Long vd) {
                this.vd = vd;
            }

            public Long getSr() {
                return sr;
            }

            public void setSr(Long sr) {
                this.sr = sr;
            }
        }

        public static class ArDTO {
            @JSONField(name = "name")
            private String name;



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
