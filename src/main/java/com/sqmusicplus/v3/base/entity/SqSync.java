package com.sqmusicplus.v3.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 * @Classname SqSync
 * @Description 同步功能记录表
 * @Version 1.0.0
 * @Date 2025/7/15 11:18
 * @Created by SQ
 */
@TableName(value = "sq_sync")
public class SqSync {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 插件名称
     */
    @TableField(value = "plug_name")
    private String plugName;

    /**
     * 歌曲ID
     */
    @TableField(value = "music_id")
    private String musicId;

    /**
     * 歌曲信息（禁止搜索）
     */
    @TableField(value = "music_info")
    private String musicInfo;

    /**
     * 下载id
     */
    @TableField(value = "download_id")
    private Integer downloadId;

    /**
     * 歌单名称最大50个字
     */
    @TableField(value = "play_list_name")
    private String playListName;

    /**
     * 歌单名称sha1避免歌单名称过长
     */
    @TableField(value = "play_list_sha1")
    private String playListSha1;
    /**
     * 歌单id
     */
    @TableField(value = "play_list_id")
    private String playListId;

    /**
     * 歌手ID
     */
    @TableField(value = "artist_id")
    private String artistId;
    /**
     * 歌手名称
     */
    @TableField(value = "artist_name")
    private String artistName;
    /**
     * 专辑ID
     */
    @TableField(value = "album_id")
    private String albumId;
    /**
     * 专辑名称
     */
    @TableField(value = "album_name")
    private String albumName;





    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取插件名称
     *
     * @return plug_name - 插件名称
     */
    public String getPlugName() {
        return plugName;
    }

    /**
     * 设置插件名称
     *
     * @param plugName 插件名称
     */
    public void setPlugName(String plugName) {
        this.plugName = plugName == null ? null : plugName.trim();
    }

    /**
     * 获取歌曲ID
     *
     * @return music_id - 歌曲ID
     */
    public String getMusicId() {
        return musicId;
    }

    /**
     * 设置歌曲ID
     *
     * @param musicId 歌曲ID
     */
    public void setMusicId(String musicId) {
        this.musicId = musicId == null ? null : musicId.trim();
    }

    /**
     * 获取歌曲信息（禁止搜索）
     *
     * @return music_info - 歌曲信息（禁止搜索）
     */
    public String getMusicInfo() {
        return musicInfo;
    }

    /**
     * 设置歌曲信息（禁止搜索）
     *
     * @param musicInfo 歌曲信息（禁止搜索）
     */
    public void setMusicInfo(String musicInfo) {
        this.musicInfo = musicInfo == null ? null : musicInfo.trim();
    }

    /**
     * 获取下载id
     *
     * @return download_id - 下载id
     */
    public Integer getDownloadId() {
        return downloadId;
    }

    /**
     * 设置下载id
     *
     * @param downloadId 下载id
     */
    public void setDownloadId(Integer downloadId) {
        this.downloadId = downloadId;
    }

    /**
     * 获取歌单名称最大50个字
     *
     * @return play_list_name - 歌单名称最大50个字
     */
    public String getPlayListName() {
        return playListName;
    }

    /**
     * 设置歌单名称最大50个字
     *
     * @param playListName 歌单名称最大50个字
     */
    public void setPlayListName(String playListName) {
        this.playListName = playListName == null ? null : playListName.trim();
    }

    /**
     * 获取歌单名称sha1避免歌单名称过长
     *
     * @return play_list_sha1 - 歌单名称sha1避免歌单名称过长
     */
    public String getPlayListSha1() {
        return playListSha1;
    }

    /**
     * 设置歌单名称sha1避免歌单名称过长
     *
     * @param playListSha1 歌单名称sha1避免歌单名称过长
     */
    public void setPlayListSha1(String playListSha1) {
        this.playListSha1 = playListSha1 == null ? null : playListSha1.trim();
    }
    public String getPlayListId() {
        return playListId;
    }
    public void setPlayListId(String playListId) {
        this.playListId = playListId == null ? null : playListId.trim();
    }
    public String getArtistId() {
        return artistId;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId == null ? null : artistId.trim();
    }
    public String getArtistName() {
        return artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName == null ? null : artistName.trim();
    }
    public String getAlbumId() {
        return albumId;
    }
    public void setAlbumId(String albumId) {
        this.albumId = albumId == null ? null : albumId.trim();
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName == null ? null : albumName.trim();
    }
}