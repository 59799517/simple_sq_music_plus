package com.sqmusicplus.Spotify;


import com.sqmusicplus.v3.utils.OkHttpUtils;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * @Classname Test
 * @Description
 * @Version 1.0.0
 * @Date 2026/3/11
 * @Created by SQ
 */
public class Test {
//    https://r.jina.ai/http:// (主要用于内容提取)
//    https://api.allorigins.win/raw?url=https://open.spotify.com
    public static void main(String[] args) throws IOException {
        OkHttpUtils okHttpUtils = OkHttpUtils.builder()
                .url("https://api.cors.lol/?url=https://open.spotify.com")
//                .url("https://corsproxy.io/https://open.spotify.com")
//                .url("https://api.codetabs.com/v1/proxy/?quest=https://open.spotify.com")


                .get();
        Response response = okHttpUtils.syncReturnResponse();
        Headers headers = response.headers();
        //打印全部的 头
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            System.out.println(headers.name(i) + ":" + headers.value(i));
        }
        //打印body
        ResponseBody body = response.body();
        System.out.println(body.string());

    }
}
