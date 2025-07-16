package com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname MedalVDTOXX
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class MedalVDTOXX {
    @JsonProperty("Title")
    public String title;
    @JsonProperty("Icon")
    public String icon;
    @JsonProperty("Color")
    public String color;
    @JsonProperty("Scheme")
    public String scheme;
    @JsonProperty("Segment")
    public SegmentVDTOX segment;
    @JsonProperty("Style")
    public Integer style;
}
