package com.sqmusicplus.v3.plug.netease.hander;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.utils.DownloadUtils;



/**
 * @Classname SQNeteaseCloudMusicInfo
 * @Description NeteaseCloudMusicInfo扩展
 * @Version 1.0.0
 * @Date 2025/7/11 09:06
 * @Created by SQ
 */

public class SQNeteaseCloudMusicInfo {
    private String baseUrl="";
    private String cookie="";


    public void init(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public void init(String baseUrl,String cookie){
        this.baseUrl = baseUrl;
        this.cookie = cookie;
    }


    public JSONObject songMusicDetail(JSONObject parameter) {
        String url = "/song/music/detail";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }


    public JSONObject songDownloadUrl(JSONObject parameter) {
        String url = "/song/download/url";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);

    }


    public JSONObject searchSuggest(JSONObject parameter) {
        String url = "/search/suggest";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }



    public JSONObject cloudsearch(JSONObject parameter) {
        String url = "/cloudsearch";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }



    public JSONObject songDetail(JSONObject parameter) {
        String url = "/song/detail";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }

    public JSONObject artistDetail(JSONObject parameter) {
        String url = "/artist/detail";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }


    public JSONObject album(JSONObject parameter) {
        String url = "/album";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }



    public JSONObject lyric(JSONObject parameter) {
        String url = "/lyric";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }


    public JSONObject artistAlbum(JSONObject parameter) {
        String url = "/artist/album";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }

    public JSONObject playlistDetail(JSONObject parameter) {
        String url = "/playlist/detail";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }

    public JSONObject playlistTrackAll(JSONObject parameter) {
        String url = "/playlist/track/all";
        return DownloadUtils.postCookieToJsonObject(baseUrl + url, parameter, cookie);
    }



    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
