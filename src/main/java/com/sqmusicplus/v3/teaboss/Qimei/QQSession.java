package com.sqmusicplus.v3.teaboss.Qimei;

import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname QQSession
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/4/25 11:38
 * @Created by Administrator
 */

public class QQSession {
    private static final ThreadLocal<QQSession> sessionContext = ThreadLocal.withInitial(QQSession::new);
    private final HttpClient client;
    public static final CookieManager manager = new CookieManager();
    private final Map<String, String> cookies = new ConcurrentHashMap<>();
    private String userAgent = "Mozilla/5.0 (Windows NT 11.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.54";

    private static final Map<String, Object> API_CONFIG = Map.of(
            "version", "13.2.5.8",
            "version_code", 13020508,
            "endpoint", "https://u.y.qq.com/cgi-bin/musicu.fcg",
            "enc_endpoint", "https://u.y.qq.com/cgi-bin/musics.fcg"
    );


    // 初始化时创建独立 Cookie 存储
    private QQSession() {
       client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .cookieHandler(manager)
                .build();
    }
    // 响应后自动更新 Cookie 存储
    public void updateCookies(HttpResponse<?> response) {
        response.headers().allValues("Set-Cookie").forEach(cookie -> {
            String[] parts = cookie.split(";")[0].split("=");
            if (parts.length == 2) {
                cookies.put(parts[0].trim(), parts[1].trim());
            }
        });
    }

    public HttpRequest.Builder buildRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", userAgent) // 固定 UA
                .header("Referer", "y.qq.com") // 固定 Referer
                .header("Cookie", buildCookieHeader()); // 自动注入 Cookie
    }
    public static QQSession getCurrentSession() {
        QQSession session =  sessionContext.get();
        if (session == null) {
            session = new QQSession();
            sessionContext.set(session);
        }
        return session;
    }

    // 请求时自动携带 Cookie
    private String buildCookieHeader() {
        StringBuilder sb = new StringBuilder();
        cookies.forEach((k, v) -> sb.append(k).append("=").append(v).append("; "));
        return sb.toString();
    }

    public HttpClient getClient() {
        return this.client;
    }
}
