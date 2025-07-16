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

    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("error_code")
    private Integer errorCode;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("list")
        private List<ListDTO> list;
        @JsonProperty("sign_list")
        private List<?> signList;
        @JsonProperty("month")
        private String month;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @JsonProperty("day")
            private String day;
            @JsonProperty("receive_vip")
            private Integer receiveVip;
        }
    }
}
