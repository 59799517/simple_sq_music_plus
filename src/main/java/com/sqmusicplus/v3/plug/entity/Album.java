package com.sqmusicplus.v3.plug.entity;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 专辑
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Data
@Accessors(chain = true)
public class Album  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String albumId;
    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 专辑时间
     */
    private String albumTime;
    /**
     * 专辑简介
     */
    private String albumDescribe;

    /**
     * 专辑歌手
     */
    private String albumArtist;
    /**
     * 专辑歌手id
     */
    private String albumArtistId;

    /**
     * 专辑图片
     */
    private String albumImg;

    /**
     * 搜索歌曲时携带的专辑信息
     */
    private JSONObject dataInfo;
    /**
     * 专辑音乐
     */
    private List<Music> musics;


}
