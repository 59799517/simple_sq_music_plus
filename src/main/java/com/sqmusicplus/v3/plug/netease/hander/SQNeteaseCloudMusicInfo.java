package com.sqmusicplus.v3.plug.netease.hander;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.v3.plug.netease.utils.SqYumboAnnotationUtils;
import top.yumbo.util.music.annotation.MusicService;
import top.yumbo.util.music.annotation.YumboAnnotationUtils;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

/**
 * @Classname SQNeteaseCloudMusicInfo
 * @Description NeteaseCloudMusicInfo扩展
 * @Version 1.0.0
 * @Date 2025/7/11 09:06
 * @Created by SQ
 */

public class SQNeteaseCloudMusicInfo extends NeteaseCloudMusicInfo {


    @MusicService(
            url = "song/music/detail"
    )
    public JSONObject songMusicDetail(JSONObject parameter) {
        this.setCurrentRunningMethod("songMusicDetail");
        this.setParameter(parameter);
        return this.getResult();
    }


    @MusicService(
            url = "/song/download/url"
    )
    public JSONObject songDownloadUrl(JSONObject parameter) {
        this.setCurrentRunningMethod("songDownloadUrl");
        this.setParameter(parameter);
        return this.getResult();
    }

    @Override
    public JSONObject getResult() {
       return SqYumboAnnotationUtils.sendRequestAutowiredJson(this); // 调用反射发送请求注入数据通过下面的return返回
    }
}
