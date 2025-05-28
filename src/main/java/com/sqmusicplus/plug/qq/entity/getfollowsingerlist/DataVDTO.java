package com.sqmusicplus.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname DataVDTO
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class DataVDTO {
    @JsonProperty("Msg")
    public String msg;
    @JsonProperty("Total")
    public Integer total;
    @JsonProperty("FromLimit")
    public Integer fromLimit;
    @JsonProperty("List")
    public List<ListVDTO> list;
    @JsonProperty("HasMore")
    public Boolean hasMore;
    @JsonProperty("LastPos")
    public String lastPos;
    @JsonProperty("LockFlag")
    public Integer lockFlag;
    @JsonProperty("LockMsg")
    public String lockMsg;
    @JsonProperty("SelfInfo")
    public SelfInfoVDTO selfInfo;
}
