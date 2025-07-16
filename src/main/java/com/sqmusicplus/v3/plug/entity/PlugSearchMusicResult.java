package com.sqmusicplus.v3.plug.entity;

import com.alibaba.fastjson.JSONObject;
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
    String id;
    /**
     * 歌曲名称
     */
    String name;
    /**
     * 歌手名称
     */
    List<String> artistName;
    /**
     * 歌手id
     */
    List<String> artistids;
    /**
     * 歌曲图片（必须是完整的url地址）
     */
    String pic;
    /**
     * 专辑名称
     */
    String albumName;
    /**
     * 专辑id
     */
    String albumid;
    /**
     * 歌词
     */
    String lyric;
    /**
     * 歌词id（一次性获取不到歌词时接下来使用---尽量避免使用一次性就获取好）
     */
    String lyricId;

    /**
     * 搜索类型
     */
    String  searchType;
    /**
     * 歌曲时长
     */

    String duration;

    /**
     * 其余信息（
     */
    JSONObject dataInfo;


}
