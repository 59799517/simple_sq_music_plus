package com.sqmusicplus.v3.plug.entity;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 16:51
 * Description: 搜索结果返回对象
 */
@Data
@Accessors(chain = true)
public class PlugSearchMusicResult {
    /**
     * id
     */
    private String id;
    /**
     * 歌曲名称
     */
    private String name;
    /**
     * 歌手名称
     */
    private List<String> artistName;
    /**
     * 歌手id
     */
    private List<String> artistids;
    /**
     * 歌曲图片（必须是完整的url地址）
     */
    private String pic;
    /**
     * 专辑名称
     */
    private String albumName;
    /**
     * 专辑id
     */
    private String albumid;
    /**
     * 歌词
     */
    private String lyric;
    /**
     * 歌词id
     */
    private String lyricId;

    /**
     * 搜索类型
     */
    private String  plugName;
    /**
     * 歌曲时长(毫秒)
     */

    private String duration;



    /**
     * br 码
     */
    private List<PlugBrType> brTypes;

    /**
     * 其余信息
     */
    private JSONObject dataInfo;




}
