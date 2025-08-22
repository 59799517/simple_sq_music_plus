package com.sqmusicplus.v3.plug.kg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SignResultInfo
 * @Description 签到详情
 * @Version 1.0.0
 * @Date 2025/7/10 15:01
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SignResultInfo {

    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error_code")
    private Integer errorCode;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("is_vip")
        private Integer isVip;
        @JsonProperty("roam_type")
        private Integer roamType;
        @JsonProperty("m_reset_time")
        private String mResetTime;
        @JsonProperty("m_y_endtime")
        private String mYEndtime;
        @JsonProperty("vip_clearday")
        private String vipClearday;
        @JsonProperty("vip_type")
        private Integer vipType;
        @JsonProperty("vip_begin_time")
        private String vipBeginTime;
        @JsonProperty("roam_begin_time")
        private String roamBeginTime;
        @JsonProperty("vip_end_time")
        private String vipEndTime;
        @JsonProperty("userid")
        private Integer userid;
        @JsonProperty("vip_y_endtime")
        private String vipYEndtime;
        @JsonProperty("m_clearday")
        private String mClearday;
        @JsonProperty("svip_level")
        private Integer svipLevel;
        @JsonProperty("svip_score")
        private Integer svipScore;
        @JsonProperty("su_vip_clearday")
        private String suVipClearday;
        @JsonProperty("su_vip_end_time")
        private String suVipEndTime;
        @JsonProperty("su_vip_y_endtime")
        private String suVipYEndtime;
        @JsonProperty("su_vip_begin_time")
        private String suVipBeginTime;
        @JsonProperty("busi_vip")
        private List<BusiVipDTO> busiVip;
        @JsonProperty("m_begin_time")
        private String mBeginTime;
        @JsonProperty("user_y_type")
        private Integer userYType;
        @JsonProperty("user_type")
        private Integer userType;
        @JsonProperty("y_type")
        private Integer yType;
        @JsonProperty("m_end_time")
        private String mEndTime;
        @JsonProperty("roam_end_time")
        private String roamEndTime;
        @JsonProperty("m_is_old")
        private Integer mIsOld;
        @JsonProperty("m_type")
        private Integer mType;

        @NoArgsConstructor
        @Data
        public static class BusiVipDTO {
            @JsonProperty("is_vip")
            private Integer isVip;
            @JsonProperty("purchased_ios_type")
            private Integer purchasedIosType;
            @JsonProperty("purchased_type")
            private Integer purchasedType;
            @JsonProperty("is_paid_vip")
            private Integer isPaidVip;
            @JsonProperty("vip_clearday")
            private String vipClearday;
            @JsonProperty("latest_product_id")
            private String latestProductId;
            @JsonProperty("product_type")
            private String productType;
            @JsonProperty("vip_begin_time")
            private String vipBeginTime;
            @JsonProperty("y_type")
            private Integer yType;
            @JsonProperty("vip_end_time")
            private String vipEndTime;
            @JsonProperty("userid")
            private Integer userid;
            @JsonProperty("vip_limit_quota")
            private VipLimitQuotaDTO vipLimitQuota;
            @JsonProperty("paid_vip_expire_time")
            private String paidVipExpireTime;
            @JsonProperty("busi_type")
            private String busiType;

            @NoArgsConstructor
            @Data
            public static class VipLimitQuotaDTO {
            }
        }
    }
}
