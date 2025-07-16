package com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname SelfInfoVDTO
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class SelfInfoVDTO {
    @JsonProperty("MID")
    public String mid;
    @JsonProperty("EncUin")
    public String encUin;
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Desc")
    public String desc;
    @JsonProperty("AvatarUrl")
    public String avatarUrl;
    @JsonProperty("VipIconUrl")
    public String vipIconUrl;
    @JsonProperty("MarkUrl")
    public String markUrl;
    @JsonProperty("FanNum")
    public Integer fanNum;
    @JsonProperty("IsFollow")
    public Boolean isFollow;
    @JsonProperty("OtherInfo")
    public Object otherInfo;
    @JsonProperty("ExtraInfo")
    public Object extraInfo;
    @JsonProperty("extra_info")
    public Object extraInfoX;
    @JsonProperty("NewIconInfo")
    public NewIconInfoVDTO newIconInfo;
    @JsonProperty("BeFollowed")
    public Boolean beFollowed;
    @JsonProperty("Time")
    public Integer time;
    @JsonProperty("Medal")
    public MedalVDTOX medal;
}
