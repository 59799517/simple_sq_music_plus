package com.sqmusicplus.plug.kg.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Classname SignResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/2/10 10:46
 * @Created by Administrator
 */

public class SignResult
{

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
        @JSONField(name = "remain_vip_hour")
        private Long remainVipHour;
        @JSONField(name = "total")
        private Long total;
        @JSONField(name = "done")
        private Long done;
        @JSONField(name = "remain")
        private Long remain;
        @JSONField(name = "award_vip_hour")
        private Long awardVipHour;

        public Long getRemainVipHour() {
            return remainVipHour;
        }

        public void setRemainVipHour(Long remainVipHour) {
            this.remainVipHour = remainVipHour;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getDone() {
            return done;
        }

        public void setDone(Long done) {
            this.done = done;
        }

        public Long getRemain() {
            return remain;
        }

        public void setRemain(Long remain) {
            this.remain = remain;
        }

        public Long getAwardVipHour() {
            return awardVipHour;
        }

        public void setAwardVipHour(Long awardVipHour) {
            this.awardVipHour = awardVipHour;
        }
    }
}
