package com.sqmusicplus.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname NicknameVDTO
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:51
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class NicknameVDTO {
    @JsonProperty("lightColor")
    public String lightColor;
    @JsonProperty("darkColor")
    public String darkColor;
}
