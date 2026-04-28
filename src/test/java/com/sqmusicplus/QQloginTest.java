//package com.sqmusicplus;
//
//import com.sqmusicplus.v3.plug.qq.entity.QQMusicQr;
//import com.sqmusicplus.v3.plug.qq.entity.QQMusicQrEventResult;
//import com.sqmusicplus.v3.plug.qq.enums.LoginType;
//import com.sqmusicplus.v3.plug.qq.enums.QRCodeLoginEvents;
//import okhttp3.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.util.Base64Utils;
//
//import javax.net.ssl.*;
//import javax.script.ScriptException;
//import java.io.IOException;
//import java.security.KeyStore;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
///**
// * @Classname QQloginTest
// * @Description QQ二维码登录
// * @Version 1.0.0
// * @Date 2025/4/28 14:13
// * @Created by SQ
// */
//
//public class QQloginTest {
//    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//    // 新增全局 Cookie 管理器
//    private static  TrustManager[] trustManagers = buildTrustManagers();
//
//    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(15, TimeUnit.SECONDS)
//            .writeTimeout(20, TimeUnit.SECONDS)
//            .readTimeout(20, TimeUnit.SECONDS)
//            .hostnameVerifier((hostName, session) -> true)
//            .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
//            .retryOnConnectionFailure(true)
//            .followRedirects(false)
//            .followSslRedirects(false)
//            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
//            .cookieJar(new CookieJar() {
//
//                @Override
//                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                    cookieStore.put(url.host(), cookies);
//                }
//
//                @Override
//                public List<Cookie> loadForRequest(HttpUrl url) {
//                    return cookieStore.getOrDefault(url.host(), new ArrayList<>());
//                }
//            })
//            .build();
//
//
//
//
//    @Test
//    void testKgSongUrl() throws IOException, ScriptException, InterruptedException {
////        OkHttpUtils.clearCookies();
////
//        QQMusicQr qqLoginQr = getQQLoginQr();
//        String qqMusicQrBase64 = getQQMusicQrBase64(qqLoginQr);
//        System.out.println(qqMusicQrBase64);
//        QQMusicQrEventResult qqMusicQrEventResult = checkQQQr(qqLoginQr);
//        while (qqMusicQrEventResult.getQrCodeLoginEvents() != QRCodeLoginEvents.DONE) {
//            qqMusicQrEventResult = checkQQQr(qqLoginQr);
//        }
//        //huoqucode
//        QQMusicQrEventResult authorizeByQQMusicQrEventResult = getAuthorizeByQQMusicQrEventResult(qqMusicQrEventResult);
//        if (authorizeByQQMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.CODE_SUCCESS) {
//            System.out.println("登录成功");
//        } else if (authorizeByQQMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.REFUSE) {
//            System.out.println("登录失败");
//        } else if (authorizeByQQMusicQrEventResult.getQrCodeLoginEvents()== QRCodeLoginEvents.TIMEOUT) {
//            System.out.println("登录超时");
//        }
//        System.out.println("完成");
//
//
//    }
//
//
//    public static QQMusicQr getQQLoginQr() {
//        double random = new Random().nextDouble();
//
//        // 构建请求 URL
//        HttpUrl url = new HttpUrl.Builder()
//                .scheme("https")
//                .host("ssl.ptlogin2.qq.com")
//                .addPathSegment("ptqrshow")
//                .addQueryParameter("appid", "716027609")
//                .addQueryParameter("e", "2")
//                .addQueryParameter("l", "M")
//                .addQueryParameter("s", "3")
//                .addQueryParameter("d", "72")
//                .addQueryParameter("v", "4")
//                .addQueryParameter("t", random+"")
//                .addQueryParameter("daid", "383")
//                .addQueryParameter("pt_3rd_aid", "100497308")
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .header("Referer", "https://xui.ptlogin2.qq.com/")
//                .get()
//                .build();
//        logRequest("getQQLoginQr", request);
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            logResponse("getQQLoginQr", response);
//            if (!response.isSuccessful()) return null;
//
//            // 提取 qrsig
//            String qrsig = response.headers("Set-Cookie").stream()
//                    .filter(c -> c.startsWith("qrsig="))
//                    .findFirst()
//                    .map(c -> c.split(";")[0].split("=")[1])
//                    .orElse(null);
//
//            if (qrsig == null) return null;
//
//            return new QQMusicQr(
//                    response.body().bytes(),
//                    LoginType.QQ,
//                    "image/png",
//                    qrsig,
//                    0
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to get QR code", e);
//        }
//    }
//
//
//
//    public static QQMusicQrEventResult checkQQQr(QQMusicQr qqMusicQr) {
//        String qrsig = qqMusicQr.getIdentifier();
//        if (qrsig == null || qrsig.isEmpty()) {
//            return new QQMusicQrEventResult().setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
//        }
//
//        HttpUrl url = new HttpUrl.Builder()
//                .scheme("https")
//                .host("ssl.ptlogin2.qq.com")
//                .addPathSegment("ptqrlogin")
//                .addQueryParameter("u1", "https://graph.qq.com/oauth2.0/login_jump")
//                .addQueryParameter("ptqrtoken", String.valueOf(sigHash(qrsig)))
//                .addQueryParameter("ptredirect", "0")
//                .addQueryParameter("h", "1")
//                .addQueryParameter("t", "1")
//                .addQueryParameter("g", "1")
//                .addQueryParameter("from_ui", "1")
//                .addQueryParameter("ptlang", "2052")
//                .addQueryParameter("action", "0-0-" + System.currentTimeMillis())
//                .addQueryParameter("js_ver", "20102616")
//                .addQueryParameter("js_type", "1")
//                .addQueryParameter("pt_uistyle", "40")
//                .addQueryParameter("aid", "716027609")
//                .addQueryParameter("daid", "383")
//                .addQueryParameter("pt_3rd_aid", "100497308")
//                .addQueryParameter("has_onekey", "1")
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .header("Referer", "https://xui.ptlogin2.qq.com/")
//                .header("Cookie", "qrsig=" + qrsig) // 手动携带 Cookie
//                .get()
//                .build();
//        logRequest("checkQQQr", request);
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            logResponse("checkQQQr", response);
//            String responseBody = response.body().string();
//
//            // 保持原有解析逻辑
//            Matcher matcher = Pattern.compile("ptuiCB\\((.*?)\\)").matcher(responseBody);
//            if (!matcher.find()) {
//                return new QQMusicQrEventResult().setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
//            }
//            // 示例代码片段：
//            String[] data = matcher.group(1).replace("'", "").split(",");
//            int code = Integer.parseInt(data[0]);
//            String redirectUrl = data[2].replace("\"", "");
//
//            return new QQMusicQrEventResult()
//                    .setQrCodeLoginEvents(QRCodeLoginEvents.getByValue(code))
//                    .setUrl(redirectUrl)
//                    .setSigx(extractValue(responseBody, "&ptsigx=(.+?)&s_url"))
//                    .setUin(extractValue(responseBody, "&uin=(.+?)&service"));
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to check QR status", e);
//        }
//    }
//
//
//
//
//
//    public static QQMusicQrEventResult getAuthorizeByQQMusicQrEventResult(QQMusicQrEventResult eventResult) {
//        try {
//            HttpUrl url = new HttpUrl.Builder()
//                    .scheme("https")
//                    .host("ssl.ptlogin2.graph.qq.com")
//                    .addPathSegment("check_sig")
//                    .addQueryParameter("uin", eventResult.getUin())
//                    .addQueryParameter("pttype", "1")
//                    .addQueryParameter("service", "ptqrlogin")
//                    .addQueryParameter("nodirect", "0")
//                    .addQueryParameter("ptsigx", eventResult.getSigx())
//                    .addQueryParameter("s_url", "https://graph.qq.com/oauth2.0/login_jump")
//                    .addQueryParameter("ptlang", "2052")
//                    .addQueryParameter("ptredirect", "100")
//                    .addQueryParameter("aid", "716027609")
//                    .addQueryParameter("daid", "383")
//                    .addQueryParameter("j_later", "0")
//                    .addQueryParameter("low_login_hour", "0")
//                    .addQueryParameter("regmaster", "0")
//                    .addQueryParameter("pt_login_type", "3")
//                    .addQueryParameter("pt_aid", "0")
//                    .addQueryParameter("pt_aaid", "16")
//                    .addQueryParameter("pt_light", "0")
//                    .addQueryParameter("pt_3rd_aid", "100497308")
//                    .build();
//
//            // 第一步：获取 p_skey
//            Request request = new Request.Builder()
//                    .url(url.toString())
//                    .header("Referer", "https://xui.ptlogin2.qq.com/")
//                    .get()
//                    .build();
//            logRequest("getAuthorize-Step1", request);
//            try (Response response = okHttpClient.newCall(request).execute()) {
//                logResponse("getAuthorize-Step1", response);
//                // 提取 p_skey
//                String pSkey = response.headers("Set-Cookie").stream()
//                        .filter(c -> c.contains("p_skey="))
//                        .findFirst()
//                        .map(c -> c.split("p_skey=")[1].split(";")[0])
//                        .orElse(null);
//
//                if (pSkey == null) {
//                    return eventResult.setQrCodeLoginEvents(QRCodeLoginEvents.NOTFOUND);
//                }
//                ArrayList<Cookie> cookies = new ArrayList<>();
//                cookieStore.forEach((k, v) -> {
//                    cookies.addAll(v);
//
//                });
//                String cookieHeader = cookies.stream()
//                        .map(c -> c.name() + "=" + c.value())
//                        .collect(Collectors.joining("; "));
//
//
//                // 第二步：构造授权请求
//                String gtk = String.valueOf(sigHash(pSkey, 5381));
//                FormBody formBody = new FormBody.Builder()
//                        .add("response_type", "code")
//                        .add("client_id", "100497308")
//                        .add("redirect_uri", "https://y.qq.com/portal/wx_redirect.html?login_type=1&surl=https://y.qq.com")
//                        .add("state", "state")
//                        .add("switch", "")
//                        .add("from_ptlogin", "1")
//                        .add("src", "1")
//                        .add("update_auth", "1")
//                        .add("openapi", "1010_1030")
//                        .add("g_tk", gtk)
//                        .add("auth_time", String.valueOf(System.currentTimeMillis()))
//                        .add("ui", UUID.randomUUID().toString())
//                        .build();
//
//                Request authRequest = new Request.Builder()
//                        .url("https://graph.qq.com/oauth2.0/authorize")
//
//                        .header("Cookie", cookieHeader) // 显式添加Cookie
//                        .header("Content-Type","application/x-www-form-urlencoded")
//                        .header("Referer","https://graph.qq.com/oauth2.0/show?which=Login&display=pc&response_type=code&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1&surl=https://y.qq.com/portal/profile.html#stat=y_new.top.user_pic&stat=y_new.top.pop.logout&use_customer_cb=0&state=state&display=pc")
//
////                        .header("Referer", "https://graph.qq.com/oauth2.0/show?...")
//                        .post(formBody)
//                        .build();
//                logRequest("getAuthorize-Step2", authRequest); // 新增第二步请求日志
//                try (Response authResponse = okHttpClient.newCall(authRequest).execute()) {
//                    logResponse("getAuthorize-Step2", authResponse); // 新增第二步响应日志
//                    // 从 Location 头提取 code
//                    String location = authResponse.header("Location");
//                    if (location == null) return eventResult;
//
//                    Matcher matcher = Pattern.compile("code=([^&]+)").matcher(location);
//                    if (matcher.find()) {
//                        return eventResult
//                                .setQrCodeLoginEvents(QRCodeLoginEvents.CODE_SUCCESS)
//                                .setCode(matcher.group(1));
//                    }
//                    return eventResult;
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Authorization failed", e);
//        }
//    }
//
//
////    public static QQMusicQrEventResult getQQMusicLoginCookie(QQMusicQrEventResult){
////
////
////    }
//
//
//
////  "music.login.LoginServer",
////          "Login",
////    common={"tmeLoginType": "2"},
////    params={"code": code},
////    cacheable=False,
//
//
//    //获取这个SSLSocketFactory
//
//    /**
//     * 生成安全套接字工厂，用于https请求的证书跳过
//     *
//     * @return
//     */
//    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
//        SSLSocketFactory ssfFactory = null;
//        try {
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new SecureRandom());
//            ssfFactory = sc.getSocketFactory();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ssfFactory;
//    }
//
//    private static TrustManager[] buildTrustManagers() {
//        return new TrustManager[]{
//                new X509TrustManager() {
//                    @Override
//                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
//                    }
//
//                    @Override
//                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
//                    }
//
//                    @Override
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return new X509Certificate[]{};
//                    }
//                }
//        };
//    }
//
//
//
//
//
//
//
//    //    计算qq的hash值
//    private static long sigHash(String qrsig) {
//        long hash = 0;
//        for (char c : qrsig.toCharArray()) {
//            hash = (hash << 5) + hash + c;
//        }
//        return hash & 0x7FFFFFFF;
//    }
//    private static long sigHash(String input, long seed) {
//        long hash = seed;
//        for (char c : input.toCharArray()) {
//            hash = (hash << 5) + hash + c;
//        }
//        return hash & 0x7FFFFFFF;
//    }
//
//    // 提取特定值的正则匹配方法
//    private static String extractValue(String text, String regex) {
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(text);
//        if (matcher.find()) {
//            return matcher.group(1);
//        }
//        return null;
//    }
//    public static  String  getQQMusicQrBase64(QQMusicQr qqMusicQr){
//        if(qqMusicQr==null){
//            return null;
//        }
//        return  Base64Utils.encodeToString(qqMusicQr.getData());
//
//    }
//    // 在类开头添加日志记录工具方法
//    private static void logRequest(String methodName, Request request) {
//        System.out.println("\n--- "+methodName+" Request ---");
//        System.out.println("URL: " + request.url());
//
//        // 请求头
//        System.out.println("Headers:");
//        request.headers().forEach(header ->
//                System.out.println("  " + header.getFirst() + ": " + header.getSecond()));
//
//        // 当前Cookie
//        List<Cookie> cookies = cookieStore.get(request.url().host());
//        System.out.println("Cookies: " + (cookies != null ? cookies : "[]"));
//    }
//
//    private static void logResponse(String methodName, Response response) throws IOException {
//        System.out.println("\n--- "+methodName+" Response ---");
//        System.out.println("Status: " + response.code() + " " + response.message());
//
//        // 响应头
//        System.out.println("Headers:");
//        response.headers().forEach(header ->
//                System.out.println("  " + header.getFirst() + ": " + header.getSecond()));
//
//        // 响应体（注意body只能读取一次）
//        String responseBody = response.peekBody(Long.MAX_VALUE).string();
//        System.out.println("Body: " + responseBody.substring(0, Math.min(200, responseBody.length())) + "...");
//    }
//
//    // 辅助方法：格式化FormBody
//    private static String formBodyToString(FormBody body) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < body.size(); i++) {
//            sb.append(body.name(i))
//                    .append("=")
//                    .append(body.value(i))
//                    .append("&");
//        }
//        return sb.toString();
//    }
//
//}
