package com.sqmusicplus;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QqMusicLoginDemo {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    return cookieStore.getOrDefault(url.host(), new ArrayList<>());
                }
            })
            .build();

    // 二维码登录主流程
    public static void main(String[] args) throws Exception {
        QrLoginData qrData = getLoginQrCode();
        System.out.println("二维码Base64：\ndata:image/png;base64," + Base64.encode(qrData.qrImage));

        LoginStatus status = checkLoginStatus(qrData.qrsig);
        while (status.status == QrStatus.WAITING) {
            System.out.println("等待扫码...");
            Thread.sleep(3000);
            status = checkLoginStatus(qrData.qrsig);
        }

        if (status.status == QrStatus.CONFIRMED) {
            String authCode = getAuthorizationCode(status.redirectUrl);
            System.out.println("登录成功！授权码：" + authCode);
        } else {
            System.out.println("登录失败，原因：" + status.status.description);
        }
    }

    // 获取登录二维码
    private static QrLoginData getLoginQrCode() throws IOException {
        double random = new Random().nextDouble();

        String url = new HttpUrl.Builder()
                .scheme("https")
                .host("ssl.ptlogin2.qq.com")
                .addPathSegment("ptqrshow")
                .addQueryParameter("appid", "716027609")
                .addQueryParameter("e", "2")
                .addQueryParameter("l", "M")
                .addQueryParameter("s", "3")
                .addQueryParameter("d", "72")
                .addQueryParameter("v", "4")
                .addQueryParameter("t", random+"")
                .addQueryParameter("daid", "383")
                .addQueryParameter("pt_3rd_aid", "100497308")
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Referer", "https://xui.ptlogin2.qq.com/")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String qrsig = parseCookie(response, "qrsig");
            byte[] qrImage = response.body().bytes();

            System.out.println("获取二维码成功，qrsig=" + qrsig);
            return new QrLoginData(qrImage, qrsig);
        }
    }

    // 检查登录状态
    private static LoginStatus checkLoginStatus(String qrsig) throws IOException {
        String ptqrtoken = String.valueOf(calculatePtqrtoken(qrsig));

        String url = new HttpUrl.Builder()
                .scheme("https")
                .host("ssl.ptlogin2.qq.com")
                .addPathSegments("ptqrlogin")
                .addQueryParameter("ptqrtoken", ptqrtoken)
                .addQueryParameter("u1", "https://y.qq.com/")
                .build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Cookie", "qrsig=" + qrsig)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            return parseLoginStatus(body);
        }
    }

    // 获取授权码
    private static String getAuthorizationCode(String redirectUrl) throws Exception {
        // 自动处理cookie重定向
        HttpResponse response = HttpRequest.get(redirectUrl)
                .execute();

        // 获取关键cookie
        String pSkey = response.getCookieValue("p_skey");
        System.out.println("获取到p_skey: " + pSkey);

        // 构造授权请求
        Map<String, String> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", "100497308");
        params.put("redirect_uri", URLEncoder.encode("https://y.qq.com/portal/wx_redirect.html", "UTF-8"));
        params.put("g_tk", String.valueOf(calculateGtk(pSkey)));

        String authUrl = "https://graph.qq.com/oauth2.0/authorize";
        String formBody = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String result = HttpRequest.post(authUrl)
                .header("Referer", "https://graph.qq.com/oauth2.0/show")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(formBody)
                .execute()
                .body();
        // 解析授权码
        Matcher matcher = Pattern.compile("code=([^&]+)").matcher(result);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("授权码获取失败");
    }

    // 核心算法计算
    private static int calculatePtqrtoken(String qrsig) {
        int hash = 0;
        for (char c : qrsig.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & 0x7FFFFFFF;
    }

    private static int calculateGtk(String skey) {
        int hash = 5381;
        for (char c : skey.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & 0x7FFFFFFF;
    }

    // 状态解析方法
    private static LoginStatus parseLoginStatus(String responseBody) {
        Matcher matcher = Pattern.compile("ptuiCB\\('(\\d+)','0','(.*?)','',''(.*?)\\)").matcher(responseBody);
        if (matcher.find()) {
            int code = Integer.parseInt(matcher.group(1));
            String url = matcher.group(2);
            return new LoginStatus(QrStatus.fromCode(code), url);
        }
        return new LoginStatus(QrStatus.UNKNOWN, "");
    }

    private static String parseCookie(Response response, String name) {
        for (String header : response.headers("Set-Cookie")) {
            if (header.startsWith(name + "=")) {
                return header.split(";")[0].split("=")[1];
            }
        }
        return null;
    }

    // 状态枚举
    enum QrStatus {
        WAITING(65, "等待扫码"),
        SCANNED(66, "已扫码待确认"),
        CONFIRMED(0, "已确认"),
        EXPIRED(67, "二维码过期"),
        UNKNOWN(-1, "未知状态");

        final int code;
        final String description;

        QrStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

        static QrStatus fromCode(int code) {
            for (QrStatus status : values()) {
                if (status.code == code) {
                    return status;
                }
            }
            return UNKNOWN;
        }
    }

    // 数据结构
    record QrLoginData(byte[] qrImage, String qrsig) {}
    record LoginStatus(QrStatus status, String redirectUrl) {}
}
