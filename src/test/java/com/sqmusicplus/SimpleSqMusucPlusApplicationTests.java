package com.sqmusicplus;
import com.alibaba.fastjson.JSONObject;


import com.sqmusicplus.v3.plug.qq.entity.QQMusicQr;
import com.sqmusicplus.v3.plug.qq.entity.QQMusicQrEventResult;
import com.sqmusicplus.v3.plug.qq.enums.LoginType;
import com.sqmusicplus.v3.plug.qq.enums.QRCodeLoginEvents;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.v3.task.ScanQQVIPLikeMusicTask;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;
import java.io.IOException;

import java.util.*;

@SpringBootTest
@Slf4j
class SimpleSqMusucPlusApplicationTests {
//    public static final CookieManager manager = new CookieManager();
//    public static final   HttpClient client = HttpClient.newBuilder()
//            .followRedirects(HttpClient.Redirect.NEVER)
//            .cookieHandler(manager)
//            .build();

    public  static String downloadRequestParam(String qq,String musicKey,String loginType ,String filename,String songmid,String songtype) {
//        "QIMEI36": "%s",
        String msg = """
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "11",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8",
                      "uid": "3931641530",
                      "qq": "%s",
                      "authst": "%s",
                      "tmeLoginType": "%s"
                    },
                    "music.vkey.GetVkey.UrlGetVkey": {
                      "module": "music.vkey.GetVkey",
                      "method": "UrlGetVkey",
                      "param": {
                        "filename": [
                          %s
                        ],
                        "guid": "%s",
                        "songmid": [
                          "%s"
                        ],
                        "songtype": [
                          %s
                        ]
                      }
                    }
                  }
               """;
        String format = String.format(msg,
                qq,
                musicKey,
                loginType,
                filename,
                QQMusicUtil.getGuid(),
                songmid,
                songtype
        );
        return format;
    }

    public static void main(String[] args) {



//        String s = String.valueOf(sigHash("MFzb5OFrs0WQflcecG6ILLASd0*UgmQbNAHMTLdWvO4_", 5381));
//        System.out.println(s);
//        System.out.println("1980093307".equals(s));
//
//
//        try {
//            // 1. 获取登录二维码
//            QQMusicQr qr = getQQLoginQr();
//            System.out.println("二维码获取成功，Base64长度: " + getQQMusicQrBase64(qr).length());
//            //转BASE64
//            String qqMusicQrBase64 = getQQMusicQrBase64(qr);
//            System.out.println(qqMusicQrBase64);
//
//            // 2. 轮询检查二维码状态
//            QQMusicQrEventResult status = checkQQQr(qr);
//            while (status.getQrCodeLoginEvents() != QRCodeLoginEvents.DONE) {
//                System.out.println("当前状态: " + status.getQrCodeLoginEvents());
//                Thread.sleep(2000); // 2秒轮询一次
//                status = checkQQQr(qr);
//            }
//
//            // 3. 获取授权码
//            QQMusicQrEventResult authResult = getAuthorizeByQQMusicQrEventResult(status);
//            if (authResult.getQrCodeLoginEvents() == QRCodeLoginEvents.SUCCESS) {
//                System.out.println("登录成功! 授权码: " + authResult.getCode());
//            } else {
//                System.out.println("登录失败: " + authResult.getQrCodeLoginEvents());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


@Autowired
private QQHander qqHander;
    @Autowired
    private ScanQQVIPLikeMusicTask scanQQVIPLikeMusicTask;
    @Test
    public void test()  {
        scanQQVIPLikeMusicTask.excute();

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



    /**
     * 获取登录二维码
     * @return
     */
    public static QQMusicQr  getQQLoginQr(){
        //生成随机小数不能大于1
        double random = new Random().nextDouble();

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

        QQSession session = QQSession.getCurrentSession();
        Map<String, String> printcookies = session.getCookies();
       //打印
        System.out.println("获取二维码请求-COOKIES:"+JSONObject.toJSONString(printcookies));


        HttpRequest request  = session.buildRequest(url.toString()).GET().build();

        String qrsig = null;
        HttpResponse<byte[]> response=null;

        try {
            response = session.getClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
            System.out.println("获取二维码返回-HEADERS:"+JSONObject.toJSONString(response.headers().map()));
            session.updateCookies(response);
            HttpHeaders headers = response.headers();
            if (headers.map().containsKey("Set-Cookie")) {
                List<String> cookies = headers.map().get("Set-Cookie");
                for (String cookie : cookies) {
                    qrsig = cookie.split(";")[0].split("=")[1];
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


//
//        HttpRequest get = HttpUtil.createGet(url.toString(), false);
//        get.setFollowRedirects(false);
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Referer","https://xui.ptlogin2.qq.com/");
//        get.addHeaders(headers);
//        HttpResponse execute = get.execute();
//        Map<String, List<String>> headers1 = execute.headers();
//        String qrsig = null;
//
//        if (headers1.containsKey("Set-Cookie")) {
//            List<String> cookies = headers1.get("Set-Cookie");
//            for (String cookie : cookies) {
//                qrsig = cookie.split(";")[0].split("=")[1];
//            }
//        }
//
//        if (qrsig == null) {
//            return null;
//        }
//        byte[] qrData = null;
//        qrData = execute.bodyBytes();
//
//        return new QQMusicQr(qrData, LoginType.QQ, "image/png", qrsig,0);




//
//
//        OkHttpUtils request  = OkHttpUtils.builder().url(url.toString()).addHeader("Referer", "https://xui.ptlogin2.qq.com/");
//        Response response = request.get().syncReturnResponse();
//        if(response!=null){
//            if (!response.isSuccessful()) {
//                return null;
//            }
//            String qrsig = null;
//            for (String cookie : response.headers("Set-Cookie")) {
//                if (cookie.startsWith("qrsig=")) {
//                    qrsig = cookie.split(";")[0].split("=")[1];
//                    break;
//                }
//            }
            if (qrsig == null) {
                return null;
            }
            byte[] qrData = response.body();

            return new QQMusicQr(qrData, LoginType.QQ, "image/png", qrsig,0);
        }


    //检测QQ二维码状态
    public static QQMusicQrEventResult checkQQQr(QQMusicQr qqMusicQr){
        QQMusicQrEventResult qqMusicQrEventResult = new QQMusicQrEventResult();
        qqMusicQrEventResult.setQqMusicQr(qqMusicQr);
        Integer retryCount = qqMusicQr.getRetryCount();
        //超过100次就停止监听
        if (retryCount > 100) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
            return qqMusicQrEventResult;
        }
        String qrsig = qqMusicQr.getIdentifier();

        if (qrsig == null || qrsig.isEmpty()) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.STOP);
            return qqMusicQrEventResult;
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
//
//        HttpRequest get = HttpUtil.createGet(url.toString(), false);
//        get.setFollowRedirects(false);
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Referer","https://xui.ptlogin2.qq.com/");
//        headers.put("Cookie","qrsig=" + qrsig);
//
//        get.addHeaders(headers);
//        HttpResponse response = get.execute();
////        Map<String, List<String>> headers1 = response.headers();
//
//        if (!response.isOk()) {
//            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
//            return qqMusicQrEventResult;
//        }
//        String responseBody = null;
//
//            responseBody = response.body().toString();
//        Matcher matcher = Pattern.compile("ptuiCB\\((.*?)\\)").matcher(responseBody);
//        if (!matcher.find()) {
//            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
//            return qqMusicQrEventResult;
//
//        }
//        Integer code = null;
//        try {
//            String[] data = matcher.group(1).replace("'", "").split(",");
//            code = Integer.parseInt(data[0]);
//            qqMusicQrEventResult.setUrl(data[2]);
//
//        } catch (NumberFormatException e) {
//            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
//            return qqMusicQrEventResult;
//        }
//        QRCodeLoginEvents byValue = QRCodeLoginEvents.getByValue(code);
//        String sigx = extractValue(responseBody, "&ptsigx=(.+?)&s_url");
//        String uin = extractValue(responseBody, "&uin=(.+?)&service");
//        qqMusicQrEventResult.setQrCodeLoginEvents(byValue);
//        qqMusicQrEventResult.setSigx(sigx);
//        qqMusicQrEventResult.setUin(uin);
//        return qqMusicQrEventResult;


        QQSession session = QQSession.getCurrentSession();
        Map<String, String> printcookies = session.getCookies();
        //打印
        System.out.println("检测二维码请求-COOKIES:"+JSONObject.toJSONString(printcookies));
        HttpRequest request = session.buildRequest(url.toString()).GET().build();

        HttpResponse<String> response=null;

        try {
            response = session.getClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("检测二维码返回-HEADERS:"+JSONObject.toJSONString(response.headers().map()));
            session.updateCookies(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }








//
//
//

//        OkHttpUtils request  = OkHttpUtils.builder()
//                .url(url.toString());
//
//        request = request.url(url.toString());
//        request = request.addHeader("Referer", "https://xui.ptlogin2.qq.com/");
//        request = request.addHeader("Cookie", "qrsig=" + qrsig);
//
//        Response response = request.get().syncReturnResponse();
//        if (!response.isSuccessful()) {
//            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
//            return qqMusicQrEventResult;
//        }
//
//        String responseBody = null;
//        try {
//            responseBody = response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
//            return qqMusicQrEventResult;
//
//        }
        String responseBody = response.body();
        Matcher matcher = Pattern.compile("ptuiCB\\((.*?)\\)").matcher(responseBody);

        if (!matcher.find()) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
            return qqMusicQrEventResult;

        }
        Integer code = null;
        try {
            String[] data = matcher.group(1).replace("'", "").split(",");
            code = Integer.parseInt(data[0]);
            qqMusicQrEventResult.setUrl(data[2]);

        } catch (NumberFormatException e) {
            qqMusicQrEventResult.setQrCodeLoginEvents(QRCodeLoginEvents.OTHER);
            return qqMusicQrEventResult;
        }
        QRCodeLoginEvents byValue = QRCodeLoginEvents.getByValue(code);
        String sigx = extractValue(responseBody, "&ptsigx=(.+?)&s_url");
        String uin = extractValue(responseBody, "&uin=(.+?)&service");
        qqMusicQrEventResult.setQrCodeLoginEvents(byValue);
        qqMusicQrEventResult.setSigx(sigx);
        qqMusicQrEventResult.setUin(uin);
        return qqMusicQrEventResult;
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

        // Step 2: Build the QR Code URL
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

//    将QQMusicQr转为base64图片并带上mimeType

    public static  String  getQQMusicQrBase64(QQMusicQr qqMusicQr){
        if(qqMusicQr==null){
            return null;
        }
        return  Base64Utils.encodeToString(qqMusicQr.getData());

    }



//@Test
//    void testkg() throws IOException {
//        String keyword = "陈奕迅";
//        int page = 1;
//        int pageSize = 20;
//        String timeStemp = System.currentTimeMillis() + "";
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("bitrate=0");
//        strings.add("clienttime=" + timeStemp);
//        strings.add("clientver=2000");
//        strings.add("dfid=-");
//        strings.add("inputtype=0");
//        strings.add("iscorrection=1");
//        strings.add("isfuzzy=0");
//        strings.add("keyword=" + keyword);
//        strings.add("mid=" + timeStemp);
//        strings.add("page=" + page);
//        strings.add("pagesize=" + pageSize);
//        strings.add("platform=WebFilter");
//        strings.add("privilege_filter=0");
//        strings.add("srcappid=2919");
//        strings.add("tag=em");
//        strings.add("userid=-1");
//        strings.add("uuid=" + timeStemp);
//        strings.sort(String::compareTo);
//        StringBuilder md5Builder = new StringBuilder();
//        md5Builder.append("NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt");
//        for (String s : strings) {
//            md5Builder.append(s);
//        }
//        md5Builder.append("NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt");
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String s : strings) {
//            stringBuilder.append(s).append("&");
//        }
//
//        String s = DigestUtil.md5Hex(md5Builder.toString());
//        stringBuilder.append("signature=" + s);
//
//        String s1 = DownloadUtils.getHttp().sync("https://complexsearch.kugou.com/v2/search/song?" + stringBuilder.toString()).get().getBody().toByteString().utf8();
//
//        System.out.println(s1);
//    }


//    /**
//     * AES 加密
//     * @param data 需要加密的数据
//     * @param opt 包含key和iv的选项
//     * @return 加密后的字符串或包含加密字符串和临时密钥的Map
//     * @throws Exception 加密过程中可能抛出的异常
//     */
//    public static Map<String, String> cryptoAesEncrypt(String data, Map<String, String> opt) throws Exception {
//        byte[] buffer;
//        if (data == null) {
//            throw new IllegalArgumentException("Data cannot be null");
//        }
//        buffer = data.getBytes(StandardCharsets.UTF_8);
//
//        String key;
//        byte[] iv;
//        String tempKey = "";
//
//        if (opt != null && opt.containsKey("key") && opt.containsKey("iv")) {
//            key = opt.get("key");
//            iv = opt.get("iv").getBytes(StandardCharsets.UTF_8);
//        } else {
//            tempKey = opt != null && opt.containsKey("key") ? opt.get("key") : generateRandomString(16).toLowerCase();
//            key = DigestUtil.md5Hex(tempKey).substring(0, 32);
//            iv = key.substring(key.length() - 16).getBytes(StandardCharsets.UTF_8);
//        }
//
//        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
//
//        byte[] encrypted = cipher.doFinal(buffer);
//        String encryptedHex = bytesToHex(encrypted);
//        Map<String, String> result = new HashMap<>();
//
//        if (opt != null && opt.containsKey("key")) {
//            result.put("str", encryptedHex);
//        } else {
//            result.put("str", encryptedHex);
//            result.put("key", tempKey);
//        }
//        return result;
//
//    }
//    private static String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) hexString.append('0');
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//    @Test
//    void trstkglogin() throws Exception {
//
//        String key = "";
//        String timeStemp = System.currentTimeMillis() + "";
//        String userName = "123";
//        String password = "123";
//
//        HashMap<String, String> dataMap  = new HashMap<>();
//        dataMap.put("plat", "1");
//        dataMap.put("support_multi", "1");
//        dataMap.put("clienttime_ms", timeStemp);
//        dataMap.put("t1", "0");
//        dataMap.put("t2", "0");
//        dataMap.put("t3", "MCwwLDAsMCwwLDAsMCwwLDA");
//        dataMap.put("username", userName);
//
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("pwd", password);
//        map.put("code", "");
//        map.put("clienttime_ms", timeStemp);
//        String data = JSONObject.toJSONString(map);
//
//        Map<String, String> o = CryptoUtil.cryptoAesEncrypt(data, null);
//        String encryptedData = o.get("str");
//        dataMap.put("params", encryptedData);
//        HashMap<String, String> cryptoRSAEncryptMap = new HashMap<>();
//        cryptoRSAEncryptMap.put("clienttime_ms", timeStemp);
//        cryptoRSAEncryptMap.put("key", o.get("key"));
//
//        String s = CryptoUtil.cryptoRSAEncrypt(cryptoRSAEncryptMap, null);
//        dataMap.put("params", encryptedData);
//        dataMap.put("pk", s);
//
//        System.out.println(s);
//
//        String baseyrl = "http://login.user.kugou.com/v9/login_by_pwd";
//        String s1 = DownloadUtils.getHttp().sync(baseyrl).bodyType(OkHttps.JSON)
//                .addHeader("x-router", "login.user.kugou.com")
//                .setBodyPara(dataMap)
//                .post().getBody().toByteString().utf8();
//        System.out.println(s1);
//
//
////        byte[] buffer = data.getBytes(StandardCharsets.UTF_8);
////        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, key.getBytes(), iv);
////        String encryptedData = aes.decryptStr(buffer);
////        System.out.println(encryptedData);
//
//
//    }

//    private static String generateRandomString(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            sb.append(characters.charAt(index));
//        }
//        return sb.toString();
//    }

//    @Autowired
//    private QQConfig qqConfig;

//    @Test
//    void contextLoads()  {
//
//        String artistInfoUrl = qqConfig.getArtistInfoUrl();
//        String artistInfoReferer = qqConfig.getArtistInfoReferer();
//        String s = artistInfoUrl.replaceAll("#\\{mid}", "0025NhlN2yWrP4");
//        Mapper mapper1 = DownloadUtils.getHttp().sync(s).bodyType(OkHttps.XML).addHeader("Referer", artistInfoReferer).get().getBody().toMapper();
//        System.out.println(mapper1);
//
//    }
//    @Test
//    public void downloadUrl(){
//        String downloadurl = "http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=";
//        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
//        try {
//            s = s.replaceAll("#\\{musicId}","184274130").replaceAll("#\\{brvalue}","2000");
//            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
//            char[] encode = Base64Coder.encode(bytes);
//            String out =  new String(encode);
//            downloadurl =  downloadurl+out;
//        } catch (UnsupportedEncodingException e) {
////            log.error("获取下载链接失败：{}",e.getMessage());
//            return ;
//        }
//        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
//        System.out.println(s1);
////        downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];
//    }



    @Test
    public void contextLoads() throws IOException, ScriptException, NoSuchMethodException {

//
//
//        String url = "https://m.lanzouj.com/i6h9c1lbimdc";
//        String downloadUrl = getDownloadUrl(url);
//
//
//        File file = new File("D:\\temp\\sq\\download\\ccc.flac");
////        boolean download = DownloadUtils.download(url, file);
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//
////        java.util.function.Consumer<Process> onProcess,Consumer<File> onSuccess,Consumer< Download.Failure> onFailure,Consumer<Download.Status> onComplete
//
//        DownloadUtils.download(downloadUrl, file, stringStringHashMap,onProcess->{
//            long doneBytes = onProcess.getDoneBytes();   // 已下载字节数
//            long totalBytes = onProcess.getTotalBytes(); // 总共的字节数
//            double rate = onProcess.getRate();           // 已下载的比例
//            boolean isDone = onProcess.isDone();         // 是否下载完成
//            System.out.println("下载中");
//            System.out.println("已下载字节数："+doneBytes);
//            System.out.println("总共的字节数"+totalBytes);
//            System.out.println("已下载的比例"+rate);
//            System.out.println("是否下载完成"+isDone);
//
//        },onSuccess->{
//            System.out.println("下载成功");
//
//        },onFailure -> {
//            System.out.println("下载失败:");
//            onFailure.getException().printStackTrace();
//
//        },onComplete->{
//            System.out.println("下载失败XXX:");
//            System.out.println(onComplete);
//        });

//        System.out.println(download);
//        String word1 = "不枉此生(电视剧《雪山飞狐》主题曲)";
//        String word2 = "不枉此生";
//        double cilinSimilarityResult = Similarity.cilinSimilarity(word1, word2);
//        double pinyinSimilarityResult = Similarity.pinyinSimilarity(word1, word2);
//        double conceptSimilarityResult = Similarity.conceptSimilarity(word1, word2);
//        double charBasedSimilarityResult = Similarity.charBasedSimilarity(word1, word2);
//
//        System.out.println(word1 + " vs " + word2 + " 词林相似度值：" + cilinSimilarityResult);
//        System.out.println(word1 + " vs " + word2 + " 拼音相似度值：" + pinyinSimilarityResult);
//        System.out.println(word1 + " vs " + word2 + " 概念相似度值：" + conceptSimilarityResult);
//        System.out.println(word1 + " vs " + word2 + " 字面相似度值：" + charBasedSimilarityResult);


//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("JavaScript");
//
//        Invocable inv = (Invocable) engine;
//
//        String javascriptPath = "C:\\Users\\Administrator\\Desktop\\MyFree.js";
//        engine.eval(new FileReader(javascriptPath));
//        System.out.println(inv.invokeFunction("search", "周杰伦"));



//
//        String downloadUrl = "https://lxmusic.ikunshare.com:9763/url/tx/003KtYhg4frNXC/flac";
//        HTTP http = DownloadUtils.getHttp();
//
//        HttpResult post = http.sync(downloadUrl).addHeader("X-Request-Key","ikunsource")
//                .addHeader("Accept","*/*")
//                .addHeader("Accept-Encoding","gzip, deflate, br")
//                .get();
//        System.out.println(post.getBody().toByteString().utf8());


//        MusicEnum.setBASE_URL_163Music("http://cloud-music.pl-fe.cn");
//        NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();
//       neteaseCloudMusicInfo.setCookieString("MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/feedback;;MUSIC_SNS=; Max-Age=0; Expires=Wed, 21 Feb 2024 09:08:14 GMT; Path=/;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/clientlog;;NMTID=00OdwRJrfs-IpN9mkV2q8y5pzJqou8AAAGNyuw2TA; Max-Age=315360000; Expires=Sat, 18 Feb 2034 09:08:14 GMT; Path=/;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/openapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/feedback;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/feedback;;MUSIC_A=bf8bfeabb1aa84f9c8c3906c04a04fb864322804c83f5d607e91a04eae463c9436bd1a17ec353cf715bc45df4b3a42a3273c7f3b958a6c67993166e004087dd38107ed2866cbf0ed9de062968295a442f4bf066e6fe094b68be4803e9b31b77d807e650dd04abd3fb8130b7ae43fcc5b; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/weapi/feedback;;__csrf=0e5ba47686a0f3816c74eb6ed2af3c06; Max-Age=1296010; Expires=Thu, 07 Mar 2024 09:08:24 GMT; Path=/;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/feedback;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/openapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/neapi/clientlog;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/wapi/feedback;;MUSIC_A_T=1699405917736; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/eapi/clientlog;;MUSIC_R_T=0; Max-Age=2147483647; Expires=Mon, 10 Mar 2092 12:22:21 GMT; Path=/api/feedback;");
//
//
//        final JSONObject parameter = new JSONObject();// 请求参数
//        parameter.put("keywords", "陶喆");
//        parameter.put("limit", "10");
//        parameter.put("type", "10");
//        JSONObject search = neteaseCloudMusicInfo.cloudsearch(parameter);
//        System.out.println(search);

//        parameter.put("ids","109125");
//        JSONObject jsonObject = neteaseCloudMusicInfo.songDetail(parameter);

//        parameter.put("id","109125");
//        JSONObject jsonObject = neteaseCloudMusicInfo.lyric(parameter);

//        parameter.put("id", "3689");
//        JSONObject jsonObject = neteaseCloudMusicInfo.artistDetail(parameter);


//        parameter.put("id", "10820");
//        JSONObject jsonObject = neteaseCloudMusicInfo.album(parameter);


//        parameter.put("id", 3689);
//        parameter.put("limit", 50);
//        parameter.put("offset", 0);
//        JSONObject jsonObject = neteaseCloudMusicInfo.artistAlbum(parameter);

//        parameter.put("id", 3689);
//
//        JSONObject jsonObject = neteaseCloudMusicInfo.artists(parameter);
//
//                System.out.println(jsonObject);

    }
//    @Test
//    public void contextLoads() throws IOException {
//        String t1_MusicID = "0039wALP1ImfSQ";
//        String platform = "qq";
//        String t2 = "SQ";
//        String device = "MI 14 Pro Max";
//        String osVersion = "13" ;
//         String time = DateUtils.getNowDate().getTime()/1000+"";
//        String  lowerCase = DigestUtil.md5Hex("6d849adb2f3e00d413fe48efbb18d9bb" + time + "6562653262383463363633646364306534333668");
//        String   s6 = "{\\\"method\\\":\\\"GetMusicUrl\\\",\\\"platform\\\":\\\"" + platform + "\\\",\\\"t1\\\":\\\"" + t1_MusicID + "\\\",\\\"t2\\\":\\\"" + t2 + "\\\"}";
//        String s7 = "{\\\"uid\\\":\\\"\\\",\\\"token\\\":\\\"\\\",\\\"deviceid\\\":\\\"84ac82836212e869dbeea73f09ebe52b\\\",\\\"appVersion\\\":\\\"4.1.2\\\",\\\"vercode\\\":\\\"4120\\\",\\\"device\\\":\\\"" + device + "\\\",\\\"osVersion\\\":\\\"" + osVersion + "\\\"}";
//        String  s8 = "{\n\t\"text_1\":\t\"" + s6 + "\",\n\t\"text_2\":\t\"" + s7 + "\",\n\t\"sign_1\":\t\"" + lowerCase + "\",\n\t\"time\":\t\"" + time + "\",\n\t\"sign_2\":\t\"" + DigestUtil.md5Hex(
//                s6.replace("\\", "") + s7.replace("\\", "") + lowerCase + time + "NDRjZGIzNzliNzEe") + "\"\n}" ;
//        byte[] utf8Bytes = s8.getBytes(StandardCharsets.UTF_8);
//        String hexString = ByteArrayUtil.toHexString(utf8Bytes);
//        String upperHexString = hexString.toUpperCase();
//        byte[] encodedBytes = upperHexString.getBytes(StandardCharsets.UTF_8);
//        byte[] compress = ZLibUtils.compress(encodedBytes);
//        HTTP http = DownloadUtils.getHttp();
//        SHttpTask sync = http.sync("http://gcsp.kzti.top:1030/client/cgi-bin/api.fcg");
//        sync.setBodyPara(compress);
//        HttpResult post = sync.post();
//        byte[] decompress = ZLibUtils.decompress(post.getBody().toBytes());
//        String s = new String(decompress);
//        System.out.println(s);
//    }

    @Test
    public void downloadLyric(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.liumingye.cn/m/api/lyric/id/m4eedd78464c21ce789dea6928415b323-fa4273678781b988a016b813970ee9d7-f89edca7c1ee56f620ab70817d12f739/name/Thinking About You - Jay Sean,Hardwell")
                .get()
                .addHeader("accept", "*/*")
                .addHeader("accept-language", "zh-CN,zh;q=0.9")
                .addHeader("origin", "https://tool.liumingye.cn")
                .addHeader("priority", "u=1, i")
                .addHeader("sec-ch-ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-site")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());

        } catch (IOException e) {

        }

    }

    String getDownloadUrl(String musicurl){
        //判断是否是蓝奏地址还是其他地址
        if (musicurl.contains("lanzou")){
            //转直连再下载

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://lz.qaiu.top/json/parser?url="+musicurl)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String string = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(string);
                Integer code = jsonObject.getInteger("code");
                if (code==200){
                    String url = jsonObject.getString("data");
                    return url;
                }else{
                    return null;
                }
            } catch (IOException e) {
                return null;
            }


        }else{
            //musicId
            return musicurl;
        }

    }


}
