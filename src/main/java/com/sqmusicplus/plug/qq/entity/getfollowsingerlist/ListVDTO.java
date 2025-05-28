package com.sqmusicplus.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname ListVDTO
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class ListVDTO {
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
    public OtherInfoVDTO otherInfo;
    @JsonProperty("ExtraInfo")
    public ExtraInfoVDTO extraInfo;
    @JsonProperty("extra_info")
    public ExtraInfoVDTO extraInfo_x;
    @JsonProperty("NewIconInfo")
    public NewIconInfoVDTOX newIconInfo;
    @JsonProperty("BeFollowed")
    public Boolean beFollowed;
    @JsonProperty("Time")
    public Integer time;
    @JsonProperty("Medal")
    public MedalVDTOXXX medal;
}
