package com.sqmusicplus;

import cn.hutool.crypto.digest.DigestUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MGTest
 * @Description 咪咕音乐搜索接口测试 - 基于 C++ Qt 实现
 * @Date 2026/4/23 10:06
 * @Created by SQ
 */
public class MGTest {
    
    private static final String SIGNATURE_MD5 = "6cdc72a439cef99a3418d2a78aa28c73";
    private static final String FIXED_STR = "yyapp2d16148780a1dcc7408e06336b98cfd50";
    private static final String UI_VERSION = "A_music_3.6.1";
    private static final String CHANNEL = "0146921";
    private static final String DEVICE_ID = "963B7AA0D21511ED807EE5846EC87D20";
    
    public static void main(String[] args) throws IOException {
        String keyword = "周杰伦";
        int page = 1;
        int pageSize = 10;
        
        Map<String, String> signParams = generateSignParams(keyword);
        
        printDebugInfo(keyword, signParams);
        
        String result = searchMusic(keyword, page, pageSize, signParams);
        
        System.out.println("\n===== 搜索结果 =====");
        System.out.println(result);
    }
    
    /**
     * 生成签名参数（与 C++ createSignature 完全一致）
     */
    private static Map<String, String> generateSignParams(String keyword) {
        long timestamp = System.currentTimeMillis();
        
        // 拼接签名原始字符串：keyword + signatureMd5 + fixedStr + deviceId + timestamp
        String text = keyword + SIGNATURE_MD5 + FIXED_STR + DEVICE_ID + timestamp;
        
        // MD5 加密（小写十六进制）
        String sign = DigestUtil.md5Hex(text).toLowerCase();
        
        Map<String, String> params = new HashMap<>();
        params.put("uiVersion", UI_VERSION);
        params.put("deviceId", DEVICE_ID);
        params.put("timestamp", String.valueOf(timestamp));
        params.put("sign", sign);
        params.put("channel", CHANNEL);
        
        return params;
    }
    
    /**
     * 打印调试信息
     */
    private static void printDebugInfo(String keyword, Map<String, String> signParams) {
        System.out.println("===== 签名调试信息 =====");
        System.out.println("keyword:   " + keyword);
        System.out.println("uiVersion: " + signParams.get("uiVersion"));
        System.out.println("deviceId:  " + signParams.get("deviceId"));
        System.out.println("timestamp: " + signParams.get("timestamp"));
        System.out.println("sign:      " + signParams.get("sign"));
        System.out.println("channel:   " + signParams.get("channel"));
        System.out.println("========================\n");
    }
    
    /**
     * 搜索音乐（与 C++ Qt 实现完全一致）
     */
    private static String searchMusic(String keyword, int page, int pageSize, 
                                     Map<String, String> signParams) throws IOException {
        // 构建 URL 参数（与 C++ 代码完全一致）
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://jadeite.migu.cn/music_search/v3/search/searchAll")
            .newBuilder()
            .addQueryParameter("isCorrect", "0")           // C++ 中是 0
            .addQueryParameter("isCopyright", "1")
            .addQueryParameter("searchSwitch", "{\"song\":1,\"album\":0,\"singer\":0,\"tagSong\":1,\"mvSong\":0,\"bestShow\":1,\"songlist\":0,\"lyricSong\":0}")
            .addQueryParameter("pageSize", String.valueOf(pageSize))
            .addQueryParameter("text", keyword)             // OkHttp 会自动编码
            .addQueryParameter("pageNo", String.valueOf(page))
            .addQueryParameter("sort", "0")
            .addQueryParameter("sid", "USS");               // 关键参数！
        
        String url = urlBuilder.build().toString();
        System.out.println("请求 URL: " + url);
        
        // 构建请求头（与 C++ 代码完全一致）
        Headers headers = new Headers.Builder()
            .add("Host", "jadeite.migu.cn")
            .add("uiVersion", signParams.get("uiVersion"))
            .add("deviceId", signParams.get("deviceId"))
            .add("timestamp", signParams.get("timestamp"))
            .add("sign", signParams.get("sign"))
            .add("channel", signParams.get("channel"))
            .add("User-Agent", "Mozilla/5.0 (Linux; U; Android 11.0.0; zh-cn; MI 11 Build/OPR1.170623.032) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
            .build();
        
        System.out.println("\n===== 请求头 =====");
        for (String name : headers.names()) {
            System.out.println(name + ": " + headers.get(name));
        }
        System.out.println("==================\n");
        
        // 创建请求
        Request request = new Request.Builder()
            .url(url)
            .headers(headers)
            .get()
            .build();
        
        // 执行请求
        OkHttpClient client = new OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            System.out.println("响应码: " + response.code());
            System.out.println("响应消息: " + response.message());
            
            if (!response.isSuccessful()) {
                ResponseBody errorBody = response.body();
                String errorMsg = errorBody != null ? errorBody.string() : "无错误信息";
                System.out.println("错误响应: " + errorMsg);
                throw new IOException("请求失败: " + response.code());
            }
            
            ResponseBody body = response.body();
            String result = body != null ? body.string() : null;
            
            // 如果返回的是压缩数据，OkHttp 会自动解压
            return result;
        }
    }
}
