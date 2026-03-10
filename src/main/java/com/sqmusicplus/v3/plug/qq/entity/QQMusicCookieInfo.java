package com.sqmusicplus.v3.plug.qq.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Classname QQMusicCookieInfo
 * @Description qq音乐cookie信息
 * @Version 1.0.0
 * @Date 2025/4/28 17:52
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
public class QQMusicCookieInfo {
    @JSONField(name = "openid")
    private String openid;
    @JSONField(name = "refresh_token")
    private String refreshToken;
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "expired_at")
    private Long expiredAt;
    @JSONField(name = "musicid")
    private String musicid;
    @JSONField(name = "musickey")
    private String musickey;
    @JSONField(name = "musickeyCreateTime")
    private Long musickeyCreateTime;
    @JSONField(name = "first_login")
    private Long firstLogin;
    @JSONField(name = "errMsg")
    private String errMsg;
    @JSONField(name = "sessionKey")
    private String sessionKey;
    @JSONField(name = "unionid")
    private String unionid;
    @JSONField(name = "str_musicid")
    private String strMusicid;
    @JSONField(name = "errtip")
    private String errtip;
    @JSONField(name = "nick")
    private String nick;
    @JSONField(name = "logo")
    private String logo;
    @JSONField(name = "feedbackURL")
    private String feedbackURL;
    @JSONField(name = "encryptUin")
    private String encryptUin;
    @JSONField(name = "userip")
    private String userip;
    @JSONField(name = "lastLoginTime")
    private Long lastLoginTime;
    @JSONField(name = "keyExpiresIn")
    private Long keyExpiresIn;
    @JSONField(name = "refresh_key")
    private String refreshKey;
    @JSONField(name = "loginType")
    private Long loginType;
    @JSONField(name = "prompt2bind")
    private Long prompt2bind;
    @JSONField(name = "logoffStatus")
    private Long logoffStatus;
    @JSONField(name = "otherAccounts")
    private List<?> otherAccounts;
    @JSONField(name = "otherPhoneNo")
    private String otherPhoneNo;
    @JSONField(name = "token")
    private String token;
    @JSONField(name = "isPrized")
    private Long isPrized;
    @JSONField(name = "isShowDevManage")
    private Long isShowDevManage;
    @JSONField(name = "errTip2")
    private String errTip2;
    @JSONField(name = "tip3")
    private String tip3;
    @JSONField(name = "encryptedPhoneNo")
    private String encryptedPhoneNo;
    @JSONField(name = "phoneNo")
    private String phoneNo;
    @JSONField(name = "bindAccountType")
    private Long bindAccountType;
    @JSONField(name = "needRefreshKeyIn")
    private Long needRefreshKeyIn;

    private String  toCookieStr(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("openid=").append(this.openid).append(";");
        stringBuilder.append("refresh_token=").append(this.refreshToken).append(";");
        stringBuilder.append("access_token=").append(this.accessToken).append(";");
        stringBuilder.append("expired_at=").append(this.expiredAt).append(";");
        stringBuilder.append("musicid=").append(this.musicid).append(";");
        stringBuilder.append("musickey=").append(this.musickey).append(";");
        stringBuilder.append("musickeyCreateTime=").append(this.musickeyCreateTime).append(";");
        stringBuilder.append("first_login=").append(this.firstLogin).append(";");
        stringBuilder.append("errMsg=").append(this.errMsg).append(";");
        stringBuilder.append("sessionKey=").append(this.sessionKey).append(";");
        stringBuilder.append("unionid=").append(this.unionid).append(";");
        stringBuilder.append("str_musicid=").append(this.strMusicid).append(";");
        stringBuilder.append("errtip=").append(this.errtip).append(";");
        stringBuilder.append("nick=").append(this.nick).append(";");
        stringBuilder.append("logo=").append(this.logo).append(";");
        stringBuilder.append("feedbackURL=").append(this.feedbackURL).append(";");
        stringBuilder.append("encryptUin=").append(this.encryptUin).append(";");
        stringBuilder.append("userip=").append(this.userip).append(";");
        stringBuilder.append("lastLoginTime=").append(this.lastLoginTime).append(";");
        stringBuilder.append("keyExpiresIn=").append(this.keyExpiresIn).append(";");
        stringBuilder.append("refresh_key=").append(this.refreshKey).append(";");
        stringBuilder.append("loginType=").append(this.loginType).append(";");
        stringBuilder.append("prompt2bind=").append(this.prompt2bind).append(";");
        stringBuilder.append("logoffStatus=").append(this.logoffStatus).append(";");
        stringBuilder.append("otherAccounts=").append(this.otherAccounts).append(";");
        stringBuilder.append("otherPhoneNo=").append(this.otherPhoneNo).append(";");
        stringBuilder.append("token=").append(this.token).append(";");
        stringBuilder.append("isPrized=").append(this.isPrized).append(";");
        stringBuilder.append("isShowDevManage=").append(this.isShowDevManage).append(";");
        stringBuilder.append("errTip2=").append(this.errTip2).append(";");
        stringBuilder.append("tip3=").append(this.tip3).append(";");
        stringBuilder.append("encryptedPhoneNo=").append(this.encryptedPhoneNo).append(";");
        stringBuilder.append("phoneNo=").append(this.phoneNo).append(";");
        stringBuilder.append("bindAccountType=").append(this.bindAccountType).append(";");
        stringBuilder.append("needRefreshKeyIn=").append(this.needRefreshKeyIn).append(";");
        return stringBuilder.toString();

    }
}
