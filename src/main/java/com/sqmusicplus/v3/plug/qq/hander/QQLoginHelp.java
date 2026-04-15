package com.sqmusicplus.v3.plug.qq.hander;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicCookie;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicCookieInfo;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicQr;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicQrEventResult;
import com.sqmusicplus.v3.plug.qq.enums.LoginType;
import com.sqmusicplus.v3.plug.qq.enums.QRCodeLoginEvents;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import okhttp3.*;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Classname QQLoginHelp
 * @Description QQ二维码登录
 * @Version 1.0.0
 * @Date 2025/4/28 14:13
 * @Created by SQ
 */
@Service
public class QQLoginHelp {
    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    // 新增全局 Cookie 管理器
    private static  TrustManager[] trustManagers = buildTrustManagers();

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .hostnameVerifier((hostName, session) -> true)
            .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
            .cookieJar(new CookieJar() {
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

    /**
     *
     *  QQ扫码专区
     *
     */

    /**
     * 获取QQ音乐QQ扫码的二维码
     * @return
     */
    public static QQMusicQr getQQLoginQr() {
        double random = new Random().nextDouble();
        // 构建请求 URL
        HttpUrl url = new HttpUrl.Builder()
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
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Referer", "https://xui.ptlogin2.qq.com/")
                .get()
                .build();
//        logRequest("getQQLoginQr", request);
        try (Response response = okHttpClient.newCall(request).execute()) {
//            logResponse("getQQLoginQr", response);
            if (!response.isSuccessful()) return null;

            // 提取 qrsig
            String qrsig = response.headers("Set-Cookie").stream()
                    .filter(c -> c.startsWith("qrsig="))
                    .findFirst()
                    .map(c -> c.split(";")[0].split("=")[1])
                    .orElse(null);

            if (qrsig == null) return null;

            return new QQMusicQr(
                    response.body().bytes(),
                    LoginType.QQ,
                    "image/png",
                    qrsig,
                    0
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get QR code", e);
        }
    }


    /**
     * 检测生成的QQ扫描二维码是否已被扫描查扫描结果
     * @param qqMusicQr
     * @return
     */
    public static QQMusicQrEventResult checkQQQr(QQMusicQr qqMusicQr) {
        String qrsig = qqMusicQr.getIdentifier();
        if (qrsig == null || qrsig.isEmpty()) {
            return new QQMusicQrEventResult().setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
        }

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("ssl.ptlogin2.qq.com")
                .addPathSegment("ptqrlogin")
                .addQueryParameter("u1", "https://graph.qq.com/oauth2.0/login_jump")
                .addQueryParameter("ptqrtoken", String.valueOf(sigHash(qrsig)))
                .addQueryParameter("ptredirect", "0")
                .addQueryParameter("h", "1")
                .addQueryParameter("t", "1")
                .addQueryParameter("g", "1")
                .addQueryParameter("from_ui", "1")
                .addQueryParameter("ptlang", "2052")
                .addQueryParameter("action", "0-0-" + System.currentTimeMillis())
                .addQueryParameter("js_ver", "20102616")
                .addQueryParameter("js_type", "1")
                .addQueryParameter("pt_uistyle", "40")
                .addQueryParameter("aid", "716027609")
                .addQueryParameter("daid", "383")
                .addQueryParameter("pt_3rd_aid", "100497308")
                .addQueryParameter("has_onekey", "1")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Referer", "https://xui.ptlogin2.qq.com/")
                .header("Cookie", "qrsig=" + qrsig) // 手动携带 Cookie
                .get()
                .build();
//        logRequest("checkQQQr", request);
        try (Response response = okHttpClient.newCall(request).execute()) {
//            logResponse("checkQQQr", response);
            String responseBody = response.body().string();

            // 保持原有解析逻辑
            Matcher matcher = Pattern.compile("ptuiCB\\((.*?)\\)").matcher(responseBody);
            if (!matcher.find()) {
                return new QQMusicQrEventResult().setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
            }
            // 示例代码片段：
            String[] data = matcher.group(1).replace("'", "").split(",");
            int code = Integer.parseInt(data[0]);
            String redirectUrl = data[2].replace("\"", "");

            return new QQMusicQrEventResult()
                    .setQqMusicQr(qqMusicQr)
                    .setQrCodeLoginEvents(QRCodeLoginEvents.getByKey(code))
                    .setUrl(redirectUrl)
                    .setSigx(extractValue(responseBody, "&ptsigx=(.+?)&s_url"))
                    .setUin(extractValue(responseBody, "&uin=(.+?)&service"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to check QR status", e);
        }
    }


    /**
     * 获取授权的code
     * @param eventResult
     * @return
     */
    public static QQMusicQrEventResult getAuthorizeByQQMusicQrEventResult(QQMusicQrEventResult eventResult) {
        try {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("ssl.ptlogin2.graph.qq.com")
                    .addPathSegment("check_sig")
                    .addQueryParameter("uin", eventResult.getUin())
                    .addQueryParameter("pttype", "1")
                    .addQueryParameter("service", "ptqrlogin")
                    .addQueryParameter("nodirect", "0")
                    .addQueryParameter("ptsigx", eventResult.getSigx())
                    .addQueryParameter("s_url", "https://graph.qq.com/oauth2.0/login_jump")
                    .addQueryParameter("ptlang", "2052")
                    .addQueryParameter("ptredirect", "100")
                    .addQueryParameter("aid", "716027609")
                    .addQueryParameter("daid", "383")
                    .addQueryParameter("j_later", "0")
                    .addQueryParameter("low_login_hour", "0")
                    .addQueryParameter("regmaster", "0")
                    .addQueryParameter("pt_login_type", "3")
                    .addQueryParameter("pt_aid", "0")
                    .addQueryParameter("pt_aaid", "16")
                    .addQueryParameter("pt_light", "0")
                    .addQueryParameter("pt_3rd_aid", "100497308")
                    .build();

            // 第一步：获取 p_skey
            Request request = new Request.Builder()
                    .url(url.toString())
                    .header("Referer", "https://xui.ptlogin2.qq.com/")
                    .get()
                    .build();
//            logRequest("getAuthorize-Step1", request);
            try (Response response = okHttpClient.newCall(request).execute()) {
//                logResponse("getAuthorize-Step1", response);
                // 提取 p_skey
                String pSkey = response.headers("Set-Cookie").stream()
                        .filter(c -> c.contains("p_skey="))
                        .findFirst()
                        .map(c -> c.split("p_skey=")[1].split(";")[0])
                        .orElse(null);

                if (pSkey == null) {
                    return eventResult.setQrCodeLoginEvents(QRCodeLoginEvents.NOTFOUND);
                }
                ArrayList<Cookie> cookies = new ArrayList<>();
                cookieStore.forEach((k, v) -> {
                    cookies.addAll(v);

                });
                String cookieHeader = cookies.stream()
                        .map(c -> c.name() + "=" + c.value())
                        .collect(Collectors.joining("; "));


                // 第二步：构造授权请求
                String gtk = String.valueOf(sigHash(pSkey, 5381));
                FormBody formBody = new FormBody.Builder()
                        .add("response_type", "code")
                        .add("client_id", "100497308")
                        .add("redirect_uri", "https://y.qq.com/portal/wx_redirect.html?login_type=1&surl=https://y.qq.com")
                        .add("state", "state")
                        .add("switch", "")
                        .add("from_ptlogin", "1")
                        .add("src", "1")
                        .add("update_auth", "1")
                        .add("openapi", "1010_1030")
                        .add("g_tk", gtk)
                        .add("auth_time", String.valueOf(System.currentTimeMillis()))
                        .add("ui", UUID.randomUUID().toString())
                        .build();

                Request authRequest = new Request.Builder()
                        .url("https://graph.qq.com/oauth2.0/authorize")

                        .header("Cookie", cookieHeader) // 显式添加Cookie
                        .header("Content-Type","application/x-www-form-urlencoded")
                        .header("Referer","https://graph.qq.com/oauth2.0/show?which=Login&display=pc&response_type=code&client_id=100497308&redirect_uri=https://y.qq.com/portal/wx_redirect.html?login_type=1&surl=https://y.qq.com/portal/profile.html#stat=y_new.top.user_pic&stat=y_new.top.pop.logout&use_customer_cb=0&state=state&display=pc")

//                        .header("Referer", "https://graph.qq.com/oauth2.0/show?...")
                        .post(formBody)
                        .build();
//                logRequest("getAuthorize-Step2", authRequest); // 新增第二步请求日志
                try (Response authResponse = okHttpClient.newCall(authRequest).execute()) {
//                    logResponse("getAuthorize-Step2", authResponse); // 新增第二步响应日志
                    // 从 Location 头提取 code
                    String location = authResponse.header("Location");
                    if (location == null) return eventResult;

                    Matcher matcher = Pattern.compile("code=([^&]+)").matcher(location);
                    if (matcher.find()) {
                        return eventResult
                                .setQrCodeLoginEvents(QRCodeLoginEvents.CODE_SUCCESS)
                                .setCode(matcher.group(1));
                    }
                    return eventResult;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Authorization failed", e);
        }
    }


/**
 * 微信专区
 */
    /**
     * 获取（生成）微信扫码的二维码
     * @return
     */
    public static  QQMusicQr  getWechatLoginQr(){
    HttpUrl uuidUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("open.weixin.qq.com")
            .addPathSegment("connect")
            .addPathSegment("qrconnect")
            .addQueryParameter("appid", "wx48db31d50e334801")
            .addQueryParameter("redirect_uri", "https://y.qq.com/portal/wx_redirect.html?login_type=2&surl=https://y.qq.com/")
            .addQueryParameter("response_type", "code")
            .addQueryParameter("scope", "snsapi_login")
            .addQueryParameter("state", "STATE")
            .addQueryParameter("href", "https://y.qq.com/mediastyle/music_v17/src/css/popup_wechat.css#wechat_redirect")
            .build();

    OkHttpUtils request  = OkHttpUtils.builder().url(uuidUrl.toString());
    Response uuidResponse = request.get().syncReturnResponse();
    if (!uuidResponse.isSuccessful()) {
    }
    String responseBody = null;
    try {
        responseBody = uuidResponse.body().string();
    } catch (IOException e) {
        e.printStackTrace();
    }
    Pattern uuidPattern = Pattern.compile("uuid=(.+?)\"");
    Matcher uuidMatcher = uuidPattern.matcher(responseBody);
    if (!uuidMatcher.find()) {
    }
    String uuid = uuidMatcher.group(1);
    HttpUrl qrCodeUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("open.weixin.qq.com")
            .addPathSegment("connect")
            .addPathSegment("qrcode")
            .addPathSegment(uuid)
            .build();
    OkHttpUtils qrCodeRequest = OkHttpUtils.builder().url(qrCodeUrl.toString()).addHeader("Referer", "https://open.weixin.qq.com/connect/qrconnect");
    Response qrCodeResponse = qrCodeRequest.get().syncReturnResponse();
    if (!qrCodeResponse.isSuccessful()) {
    }
    byte[] qrData = null;
    try {
        qrData = qrCodeResponse.body().bytes();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return new QQMusicQr(qrData, LoginType.WECHAT, "image/jpeg", uuid,0);
}

    /**
     * 检测微信扫码状态
     * @param qqMusicQr
     * @throws Exception
     */
    public static QQMusicQrEventResult checkWechatQR(QQMusicQr qqMusicQr) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        QQMusicQrEventResult qqMusicQrEventResult = new QQMusicQrEventResult();
        qqMusicQrEventResult.setQqMusicQr(qqMusicQr);
        Integer retryCount = qqMusicQr.getRetryCount();
        //超过100次就停止监听1
        if (retryCount > 100) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
            return qqMusicQrEventResult;
        }
        String qrsig = qqMusicQr.getIdentifier();

        if (qrsig == null || qrsig.isEmpty()) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
            return qqMusicQrEventResult;
        }

        String uuid = qqMusicQr.getIdentifier();
        int unixTimeStamp = (int) (System.currentTimeMillis() / 1000);
        String url = "https://lp.open.weixin.qq.com/connect/l/qrconnect?uuid=" + uuid + "&_=" + unixTimeStamp;
        HttpGet request = new HttpGet(url);request.setHeader("Referer", "https://open.weixin.qq.com/");


        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseText = EntityUtils.toString(response.getEntity());
            Pattern pattern = Pattern.compile("window\\.wx_errcode=(\\d+);window\\.wx_code='([^']*)'");
            Matcher matcher = pattern.matcher(responseText);

            if (!matcher.find()) {
                throw new Exception("获取二维码状态失败");
            }

            String wxErrcode = matcher.group(1);
            if (!wxErrcode.matches("\\d+")) {
                qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.NOTFOUND);
                return qqMusicQrEventResult;
            }

            QRCodeLoginEvents event = QRCodeLoginEvents.getByValue(Integer.parseInt(wxErrcode));
            qqMusicQrEventResult.setQrCodeLoginEvents(event);
            if (event == QRCodeLoginEvents.DONE) {
                String wxCode = matcher.group(2);
                if (wxCode == null || wxCode.isEmpty()) {
                    throw new Exception("获取code失败");
                }
                qqMusicQrEventResult.setCode(wxCode);
//                authorizeWXQR(wxCode);
            }
            return qqMusicQrEventResult;
        }
    }

    /**
     * 根据微信code获取cookie
     * 使用  QQSearchEntity的getQQWechatLoginParam生产请求参数
     */

    public static QQMusicCookieInfo authorizeWechatQR(String qqWechatLoginParam) throws Exception {
        HttpPost request = new HttpPost("https://u.y.qq.com/cgi-bin/musicu.fcg");
        request.setHeader("Content-Type", "application/json");
        Response referer = OkHttpUtils.builder()
                .url("https://u.y.qq.com/cgi-bin/musicu.fcg")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Referer", "https://y.qq.com/")
                .addHeader("User-Agent", "QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true, qqWechatLoginParam)
                .syncReturnResponse();
        String data = referer.body().string();
        QQMusicCookie qqMusicCookie = JSONObject.parseObject(data, QQMusicCookie.class);
        if (qqMusicCookie.getCode()==0){
            QQMusicCookie.ReqDTO req = qqMusicCookie.getReq();
            if (req.getCode()==0){
                return req.getData();
            }
        }
        return null;
    }




    //获取这个SSLSocketFactory

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }





    //    计算qq的hash值
    private static long sigHash(String qrsig) {
        long hash = 0;
        for (char c : qrsig.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & 0x7FFFFFFF;
    }
    private static long sigHash(String input, long seed) {
        long hash = seed;
        for (char c : input.toCharArray()) {
            hash = (hash << 5) + hash + c;
        }
        return hash & 0x7FFFFFFF;
    }

    // 提取特定值的正则匹配方法
    private static String extractValue(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    public static  String  getQQMusicQrBase64(QQMusicQr qqMusicQr){
        if(qqMusicQr==null){
            return null;
        }
        return  Base64.encode(qqMusicQr.getData());

    }
    // 在类开头添加日志记录工具方法
    private static void logRequest(String methodName, Request request) {
        System.out.println("\n--- "+methodName+" Request ---");
        System.out.println("URL: " + request.url());

        // 请求头
        System.out.println("Headers:");
        request.headers().forEach(header ->
                System.out.println("  " + header.getFirst() + ": " + header.getSecond()));

        // 当前Cookie
        List<Cookie> cookies = cookieStore.get(request.url().host());
        System.out.println("Cookies: " + (cookies != null ? cookies : "[]"));
    }

    private static void logResponse(String methodName, Response response) throws IOException {
        System.out.println("\n--- "+methodName+" Response ---");
        System.out.println("Status: " + response.code() + " " + response.message());

        // 响应头
        System.out.println("Headers:");
        response.headers().forEach(header ->
                System.out.println("  " + header.getFirst() + ": " + header.getSecond()));

        // 响应体（注意body只能读取一次）
        String responseBody = response.peekBody(Long.MAX_VALUE).string();
        System.out.println("Body: " + responseBody.substring(0, Math.min(200, responseBody.length())) + "...");
    }

    // 辅助方法：格式化FormBody
    private static String formBodyToString(FormBody body) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < body.size(); i++) {
            sb.append(body.name(i))
                    .append("=")
                    .append(body.value(i))
                    .append("&");
        }
        return sb.toString();
    }

}
