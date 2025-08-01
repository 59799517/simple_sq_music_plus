package com.sqmusicplus.v3.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
@Getter
@Setter
@TableName("download_info")
@Accessors(chain = true)
public class DownloadInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
//    全局id
    @TableField("download_gid")
    private String downloadGid;

//    下载插入时间
    @TableField("download_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date downloadTime;
//    下载文件地址
    @TableField("download_file")
    private String downloadFile;
//    下载歌曲id
    @TableField("download_music_id")
    private String downloadMusicId;
//    插件名称
    @TableField("download_plug_name")
    private String downloadPlugName;
//    下载音质类型
    @TableField("download_br_type")
    private String downloadBrType;
//  歌曲名称
    @TableField("download_musicname")
    private String downloadMusicname;
//    歌手名称
    @TableField("download_artistname")
    private String downloadArtistname;
//    专辑名称
    @TableField("download_albumname")
    private String downloadAlbumname;
//  下载信息错误信息
    @TableField("download_msg")
    private String downloadMsg;

//下载歌曲详细信息
    @TableField("download_music_info")
    private String downloadMusicInfo;
//  下载状态
    @TableField("download_status")
    private String downloadStatus;

//    下载使用的spring处理器
    @TableField("spring_name")
    private String springName;

//    是否是数据音频 1是0 否
    @TableField("audio_book")
    private Integer audioBook;
//最后更新时间
    @TableField("download_update_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date downloadUpdateTime;

    //乐观锁
    @Version
    private Integer revision;

    //    是否是需要重写mp3tag 1 是 0 否
    @TableField("rewrite_mp3tag")
    private Integer rewriteMp3tag;
//    支持的bit 多个,分割
    @TableField("download_bits")
    private String downloadBits;
//支持的类型列表多个,分割
    @TableField("download_br_types")
    private String downloadBrTypes;








}
