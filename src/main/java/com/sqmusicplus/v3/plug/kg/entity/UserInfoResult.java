package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Classname UserInfoResult
 * @Description 用户信息返回值
 * @Version 1.0.0
 * @Date 2025/2/5 16:03
 * @Created by SQ
 */
@Data
public class UserInfoResult {


    @JSONField(name = "is_vip")
    private Long isVip;
    @JSONField(name = "servertime")
    private String servertime;
    @JSONField(name = "roam_type")
    private Long roamType;
    @JSONField(name = "t1")
    private String t1;
    @JSONField(name = "reg_time")
    private String regTime;
    @JSONField(name = "vip_type")
    private Long vipType;
    @JSONField(name = "vip_begin_time")
    private String vipBeginTime;
    @JSONField(name = "userid")
    private Long userid;
    @JSONField(name = "su_vip_end_time")
    private String suVipEndTime;
    @JSONField(name = "sex")
    private Long sex;
    @JSONField(name = "user_type")
    private Long userType;
    @JSONField(name = "username")
    private String username;
    @JSONField(name = "qq")
    private Long qq;
    @JSONField(name = "exp")
    private Long exp;
    @JSONField(name = "m_end_time")
    private String mEndTime;
    @JSONField(name = "score")
    private Long score;
    @JSONField(name = "m_is_old")
    private Long mIsOld;
    @JSONField(name = "birthday")
    private String birthday;
    @JSONField(name = "arttoy_avatar")
    private String arttoyAvatar;
    @JSONField(name = "totp_server_timestamp")
    private Long totpServerTimestamp;
    @JSONField(name = "roam_end_time")
    private String roamEndTime;
    @JSONField(name = "su_vip_begin_time")
    private String suVipBeginTime;
    @JSONField(name = "roam_begin_time")
    private String roamBeginTime;
    @JSONField(name = "vip_end_time")
    private String vipEndTime;
    @JSONField(name = "secu_params")
    private String secuParams;
    @JSONField(name = "nickname")
    private String nickname;
    @JSONField(name = "mobile")
    private Long mobile;
    @JSONField(name = "user_y_type")
    private Long userYType;
    @JSONField(name = "vip_token")
    private String vipToken;
    @JSONField(name = "bc_code")
    private String bcCode;
    @JSONField(name = "m_type")
    private Long mType;
    @JSONField(name = "roam_list")
    private String roamList;
    @JSONField(name = "m_begin_time")
    private String mBeginTime;
    @JSONField(name = "pic")
    private String pic;
    @JSONField(name = "su_vip_clearday")
    private String suVipClearday;
    @JSONField(name = "t_expire_time")
    private Long tExpireTime;
    @JSONField(name = "su_vip_y_endtime")
    private String suVipYEndtime;
    @JSONField(name = "birthday_mmdd")
    private String birthdayMmdd;
    @JSONField(name = "y_type")
    private Long yType;
    @JSONField(name = "wechat")
    private Long wechat;
    @JSONField(name = "token")
    private String token;



}
