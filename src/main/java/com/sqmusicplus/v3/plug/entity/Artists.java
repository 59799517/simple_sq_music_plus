package com.sqmusicplus.v3.plug.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 艺术家
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Data
@Accessors(chain = true)
public class Artists  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 艺术家名称
     */
    private String musicArtistsName;

    /**
     * 性别
     */
    private String musicArtistsSex;

    /**
     * 图像
     */
    private String musicArtistsPhoto;

    /**
     * 简介
     */
    private String musicArtistsDescribe;

    /**
     * 别名多个,分割
     */
    private String musicArtistsAlias;

    /**
     * 搜索歌手时携带的信息
     */
    private JSONObject dataInfo;
    /**
     * 专辑信息搜索可以为空
     */
    private List<Album> albums;


}
