package com.sqmusicplus.v3.plug.tidal.enums;

/**
 * @Classname TidalSearchType
 * @Description Tidal搜索类型
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
public enum TidalSearchType {
    TRACKS("tracks"), ARTISTS("artists"), ALBUMS("albums"), PLAYLISTS("playlists");
    
    String value;
    
    TidalSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
