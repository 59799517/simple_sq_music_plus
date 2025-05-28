package com.sqmusicplus.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname MedalVDTOXXX
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class MedalVDTOXXX {
    @JsonProperty("ShowMedal")
    public Integer showMedal;
    @JsonProperty("ShowValue")
    public Integer showValue;
    @JsonProperty("FansValue")
    public Integer fansValue;
    @JsonProperty("Medal")
    public MedalVDTOXX medal;
    @JsonProperty("IsReceivedMedal")
    public Integer isReceivedMedal;
    @JsonProperty("Scheme")
    public String scheme;
}
