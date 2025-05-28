package com.sqmusicplus.plug.qq.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname QQMusicDownloadUrlInfo
 * @Description QQ音乐下载url返回值
 * @Version 1.0.0
 * @Date 2025/4/30 16:12
 * @Created by SQ
 */

public class QQMusicDownloadUrlInfo {

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
        @JSONField(name = "uin")
        private String uin;
        @JSONField(name = "retcode")
        private Long retcode;
        @JSONField(name = "verify_type")
        private Long verifyType;
        @JSONField(name = "login_key")
        private String loginKey;
        @JSONField(name = "msg")
        private String msg;
        @JSONField(name = "sip")
        private List<String> sip;
        @JSONField(name = "thirdip")
        private List<String> thirdip;
        @JSONField(name = "testfile2g")
        private String testfile2g;
        @JSONField(name = "testfilewifi")
        private String testfilewifi;
        @JSONField(name = "midurlinfo")
        private List<MidurlinfoDTO> midurlinfo;
        @JSONField(name = "servercheck")
        private String servercheck;
        @JSONField(name = "expiration")
        private Long expiration;
        @JSONField(name = "deviceResult")
        private String deviceResult;

        public String getUin() {
            return uin;
        }

        public void setUin(String uin) {
            this.uin = uin;
        }

        public Long getRetcode() {
            return retcode;
        }

        public void setRetcode(Long retcode) {
            this.retcode = retcode;
        }

        public Long getVerifyType() {
            return verifyType;
        }

        public void setVerifyType(Long verifyType) {
            this.verifyType = verifyType;
        }

        public String getLoginKey() {
            return loginKey;
        }

        public void setLoginKey(String loginKey) {
            this.loginKey = loginKey;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<String> getSip() {
            return sip;
        }

        public void setSip(List<String> sip) {
            this.sip = sip;
        }

        public List<String> getThirdip() {
            return thirdip;
        }

        public void setThirdip(List<String> thirdip) {
            this.thirdip = thirdip;
        }

        public String getTestfile2g() {
            return testfile2g;
        }

        public void setTestfile2g(String testfile2g) {
            this.testfile2g = testfile2g;
        }

        public String getTestfilewifi() {
            return testfilewifi;
        }

        public void setTestfilewifi(String testfilewifi) {
            this.testfilewifi = testfilewifi;
        }

        public List<MidurlinfoDTO> getMidurlinfo() {
            return midurlinfo;
        }

        public void setMidurlinfo(List<MidurlinfoDTO> midurlinfo) {
            this.midurlinfo = midurlinfo;
        }

        public String getServercheck() {
            return servercheck;
        }

        public void setServercheck(String servercheck) {
            this.servercheck = servercheck;
        }

        public Long getExpiration() {
            return expiration;
        }

        public void setExpiration(Long expiration) {
            this.expiration = expiration;
        }

        public String getDeviceResult() {
            return deviceResult;
        }

        public void setDeviceResult(String deviceResult) {
            this.deviceResult = deviceResult;
        }

        public static class MidurlinfoDTO {
            @JSONField(name = "songmid")
            private String songmid;
            @JSONField(name = "filename")
            private String filename;
            @JSONField(name = "purl")
            private String purl;
            @JSONField(name = "errtype")
            private String errtype;
            @JSONField(name = "p2pfromtag")
            private Long p2pfromtag;
            @JSONField(name = "qmdlfromtag")
            private Long qmdlfromtag;
            @JSONField(name = "common_downfromtag")
            private Long commonDownfromtag;
            @JSONField(name = "vip_downfromtag")
            private Long vipDownfromtag;
            @JSONField(name = "pdl")
            private Long pdl;
            @JSONField(name = "premain")
            private Long premain;
            @JSONField(name = "hisdown")
            private Long hisdown;
            @JSONField(name = "hisbuy")
            private Long hisbuy;
            @JSONField(name = "uiAlert")
            private Long uiAlert;
            @JSONField(name = "isbuy")
            private Long isbuy;
            @JSONField(name = "pneedbuy")
            private Long pneedbuy;
            @JSONField(name = "pneed")
            private Long pneed;
            @JSONField(name = "isonly")
            private Long isonly;
            @JSONField(name = "onecan")
            private Long onecan;
            @JSONField(name = "result")
            private Long result;
            @JSONField(name = "tips")
            private String tips;
            @JSONField(name = "opi48kurl")
            private String opi48kurl;
            @JSONField(name = "opi96kurl")
            private String opi96kurl;
            @JSONField(name = "opi192kurl")
            private String opi192kurl;
            @JSONField(name = "opiflackurl")
            private String opiflackurl;
            @JSONField(name = "opi128kurl")
            private String opi128kurl;
            @JSONField(name = "opi192koggurl")
            private String opi192koggurl;
            @JSONField(name = "wififromtag")
            private String wififromtag;
            @JSONField(name = "flowfromtag")
            private String flowfromtag;
            @JSONField(name = "wifiurl")
            private String wifiurl;
            @JSONField(name = "flowurl")
            private String flowurl;
            @JSONField(name = "vkey")
            private String vkey;
            @JSONField(name = "opi30surl")
            private String opi30surl;
            @JSONField(name = "ekey")
            private String ekey;
            @JSONField(name = "auth_switch")
            private Long authSwitch;
            @JSONField(name = "subcode")
            private Long subcode;
            @JSONField(name = "opi96koggurl")
            private String opi96koggurl;
            @JSONField(name = "auth_switch2")
            private Long authSwitch2;
            @JSONField(name = "xcdnurl")
            private String xcdnurl;

            public String getSongmid() {
                return songmid;
            }

            public void setSongmid(String songmid) {
                this.songmid = songmid;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getPurl() {
                return purl;
            }

            public void setPurl(String purl) {
                this.purl = purl;
            }

            public String getErrtype() {
                return errtype;
            }

            public void setErrtype(String errtype) {
                this.errtype = errtype;
            }

            public Long getP2pfromtag() {
                return p2pfromtag;
            }

            public void setP2pfromtag(Long p2pfromtag) {
                this.p2pfromtag = p2pfromtag;
            }

            public Long getQmdlfromtag() {
                return qmdlfromtag;
            }

            public void setQmdlfromtag(Long qmdlfromtag) {
                this.qmdlfromtag = qmdlfromtag;
            }

            public Long getCommonDownfromtag() {
                return commonDownfromtag;
            }

            public void setCommonDownfromtag(Long commonDownfromtag) {
                this.commonDownfromtag = commonDownfromtag;
            }

            public Long getVipDownfromtag() {
                return vipDownfromtag;
            }

            public void setVipDownfromtag(Long vipDownfromtag) {
                this.vipDownfromtag = vipDownfromtag;
            }

            public Long getPdl() {
                return pdl;
            }

            public void setPdl(Long pdl) {
                this.pdl = pdl;
            }

            public Long getPremain() {
                return premain;
            }

            public void setPremain(Long premain) {
                this.premain = premain;
            }

            public Long getHisdown() {
                return hisdown;
            }

            public void setHisdown(Long hisdown) {
                this.hisdown = hisdown;
            }

            public Long getHisbuy() {
                return hisbuy;
            }

            public void setHisbuy(Long hisbuy) {
                this.hisbuy = hisbuy;
            }

            public Long getUiAlert() {
                return uiAlert;
            }

            public void setUiAlert(Long uiAlert) {
                this.uiAlert = uiAlert;
            }

            public Long getIsbuy() {
                return isbuy;
            }

            public void setIsbuy(Long isbuy) {
                this.isbuy = isbuy;
            }

            public Long getPneedbuy() {
                return pneedbuy;
            }

            public void setPneedbuy(Long pneedbuy) {
                this.pneedbuy = pneedbuy;
            }

            public Long getPneed() {
                return pneed;
            }

            public void setPneed(Long pneed) {
                this.pneed = pneed;
            }

            public Long getIsonly() {
                return isonly;
            }

            public void setIsonly(Long isonly) {
                this.isonly = isonly;
            }

            public Long getOnecan() {
                return onecan;
            }

            public void setOnecan(Long onecan) {
                this.onecan = onecan;
            }

            public Long getResult() {
                return result;
            }

            public void setResult(Long result) {
                this.result = result;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }

            public String getOpi48kurl() {
                return opi48kurl;
            }

            public void setOpi48kurl(String opi48kurl) {
                this.opi48kurl = opi48kurl;
            }

            public String getOpi96kurl() {
                return opi96kurl;
            }

            public void setOpi96kurl(String opi96kurl) {
                this.opi96kurl = opi96kurl;
            }

            public String getOpi192kurl() {
                return opi192kurl;
            }

            public void setOpi192kurl(String opi192kurl) {
                this.opi192kurl = opi192kurl;
            }

            public String getOpiflackurl() {
                return opiflackurl;
            }

            public void setOpiflackurl(String opiflackurl) {
                this.opiflackurl = opiflackurl;
            }

            public String getOpi128kurl() {
                return opi128kurl;
            }

            public void setOpi128kurl(String opi128kurl) {
                this.opi128kurl = opi128kurl;
            }

            public String getOpi192koggurl() {
                return opi192koggurl;
            }

            public void setOpi192koggurl(String opi192koggurl) {
                this.opi192koggurl = opi192koggurl;
            }

            public String getWififromtag() {
                return wififromtag;
            }

            public void setWififromtag(String wififromtag) {
                this.wififromtag = wififromtag;
            }

            public String getFlowfromtag() {
                return flowfromtag;
            }

            public void setFlowfromtag(String flowfromtag) {
                this.flowfromtag = flowfromtag;
            }

            public String getWifiurl() {
                return wifiurl;
            }

            public void setWifiurl(String wifiurl) {
                this.wifiurl = wifiurl;
            }

            public String getFlowurl() {
                return flowurl;
            }

            public void setFlowurl(String flowurl) {
                this.flowurl = flowurl;
            }

            public String getVkey() {
                return vkey;
            }

            public void setVkey(String vkey) {
                this.vkey = vkey;
            }

            public String getOpi30surl() {
                return opi30surl;
            }

            public void setOpi30surl(String opi30surl) {
                this.opi30surl = opi30surl;
            }

            public String getEkey() {
                return ekey;
            }

            public void setEkey(String ekey) {
                this.ekey = ekey;
            }

            public Long getAuthSwitch() {
                return authSwitch;
            }

            public void setAuthSwitch(Long authSwitch) {
                this.authSwitch = authSwitch;
            }

            public Long getSubcode() {
                return subcode;
            }

            public void setSubcode(Long subcode) {
                this.subcode = subcode;
            }

            public String getOpi96koggurl() {
                return opi96koggurl;
            }

            public void setOpi96koggurl(String opi96koggurl) {
                this.opi96koggurl = opi96koggurl;
            }

            public Long getAuthSwitch2() {
                return authSwitch2;
            }

            public void setAuthSwitch2(Long authSwitch2) {
                this.authSwitch2 = authSwitch2;
            }

            public String getXcdnurl() {
                return xcdnurl;
            }

            public void setXcdnurl(String xcdnurl) {
                this.xcdnurl = xcdnurl;
            }
        }
    }
}
