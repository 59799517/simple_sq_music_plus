package com.sqmusicplus.v3.plug.entity;

import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotEmpty;

/**
 * @Classname PlugFreeMp3DownloadEntity
 * @Description freeMp3下载请求实体
 * @Version 1.0.0
 * @Date 2024/7/19 17:30
 * @Created by SQ
 */
@Data
@ToString
public class PlugFreeMp3DownloadEntity {
    @NotEmpty(message = "下载地址不能为空")
    private String downloadurl;
    @NotEmpty(message = "音乐名称不能为空")
    private String musicname;
    //专辑信息
    private String musicalbumName;
    //歌手信息
    private String musicartistName;
    @NotEmpty(message = "音乐图片不能为空")
    private String musicimage;
    @NotEmpty(message = "音乐作者不能为空")
    private String musiclyrc;
}
