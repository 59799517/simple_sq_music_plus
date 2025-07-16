package com.sqmusicplus.v3.plug.kg.enums;
/**
 * @Classname KgSearchType
 * @Description 酷狗搜索类型
 * @Version 1.0.0
 * @Date 2025/2/6 10:23
 * @Created by SQ
 */

public enum KgSearchType
{
//    搜索类型；默认为单曲，special：歌单，lyric：歌词，song：单曲，album：专辑，author：歌手，mv：mv

    MUSIC("song"),
    ALBUM("album"),
    ARTIST("author"),
    SPECIAL("special"),
    LYRIC("lyric"),
    MV("mv");

    private String value;

    KgSearchType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
