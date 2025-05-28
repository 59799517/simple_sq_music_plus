package com.sqmusicplus.plug.qq.entity.getfollowsingerlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname GetFollowSingerList
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/27 11:36
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class GetFollowSingerList {

    @JsonProperty("code")
    public Integer code;
    @JsonProperty("data")
    public DataVDTO data;
}
