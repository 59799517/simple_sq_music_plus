package com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname NewIconInfoVDTOX
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class NewIconInfoVDTOX {
    @JsonProperty("nickname")
    public NicknameVDTO nickname;
    @JsonProperty("iconlist")
    public List<?> iconlist;
}
