package com.sqmusicplus.v3.alidrive.entity;

import lombok.Data;

/**
 * SQ
 * 阿里云盘分享信息
 * 2026年3月28日
 */
@Data
public class ShareInfo {

    String sha1;
    String size;
    String name;

    String musicName;
    String musicArtist;
    String musicAlbum;
    String suffix;

}
