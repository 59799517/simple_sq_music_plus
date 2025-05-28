package com.sqmusicplus.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname SegmentVDTOX
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class SegmentVDTOX {
    @JsonProperty("Width")
    public Integer width;
    @JsonProperty("Height")
    public Integer height;
    @JsonProperty("DarkIconURL")
    public String darkIconURL;
    @JsonProperty("LightIconURL")
    public String lightIconURL;
    @JsonProperty("StretchLeft")
    public Integer stretchLeft;
    @JsonProperty("StretchRight")
    public Integer stretchRight;
    @JsonProperty("Contents")
    public Object contents;
    @JsonProperty("MaxDisplayLen")
    public Integer maxDisplayLen;
    @JsonProperty("PaddingLeft")
    public Integer paddingLeft;
    @JsonProperty("PaddingRight")
    public Integer paddingRight;
    @JsonProperty("JumpURL")
    public String jumpURL;
}
