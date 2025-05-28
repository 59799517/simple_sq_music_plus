//package com.sqmusicplus;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import org.apache.http.Header;
//import org.springframework.util.Base64Utils;
//
//import javax.imageio.ImageIO;
//import java.net.URI;
//
//public class QQMusicApi {
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
//
//    public enum QRCodeLoginEvents {
//        DONE(0, 405),
//        SCAN(66, 408),
//        CONF(67, 404),
//        TIMEOUT(65, null),
//        REFUSE(68, 403),
//        OTHER(null, null);
//
//        private final Integer value1;
//        private final Integer value2;
//
//        QRCodeLoginEvents(Integer value1, Integer value2) {
//            this.value1 = value1;
//            this.value2 = value2;
//        }
//
//        public static QRCodeLoginEvents getByValue(int value) {
//            for (QRCodeLoginEvents event : values()) {
//                if ((event.value1 != null && event.value1 == value) ||
//                        (event.value2 != null && event.value2 == value)) {
//                    return event;
//                }
//            }
//            return OTHER;
//        }
//    }
//
//    public enum PhoneLoginEvents {
//        SEND(0),
//        CAPTCHA(20276),
//        FREQUENCY(100001),
//        OTHER(null);
//
//        private final Integer value;
//
//        PhoneLoginEvents(Integer value) {
//            this.value = value;
//        }
//
//        public static PhoneLoginEvents getByValue(int value) {
//            for (PhoneLoginEvents event : values()) {
//                if (event.value != null && event.value == value) {
//                    return event;
//                }
//            }
//            return OTHER;
//        }
//    }
//
//    public enum QRLoginType {
//        QQ("qq"),
//        WX("wx");
//
//        private final String value;
//
//        QRLoginType(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//    }
//
//    public static class QR {
//        private final byte[] data;
//        private final QRLoginType qrType;
//        private final String mimeType;
//        private final String identifier;
//
//        public QR(byte[] data, QRLoginType qrType, String mimeType, String identifier) {
//            this.data = data;
//            this.qrType = qrType;
//            this.mimeType = mimeType;
//            this.identifier = identifier;
//        }
//
//        public byte[] getData() {
//            return data;
//        }
//
//        public QRLoginType getQrType() {
//            return qrType;
//        }
//
//        public String getMimeType() {
//            return mimeType;
//        }
//
//        public String getIdentifier() {
//            return identifier;
//        }
//    }
//
//    public static class Credential {
//        private String musicid;
//        private String musickey;
//        private String openid;
//        private String unionid;
//        private String encryptUin;
//        private int loginType;
//        private String refreshKey;
//        private String refreshToken;
//
//        // Getters and Setters
//        public String getMusicid() { return musicid; }
//        public void setMusicid(String musicid) { this.musicid = musicid; }
//        public String getMusickey() { return musickey; }
//        public void setMusickey(String musickey) { this.musickey = musickey; }
//        public String getOpenid() { return openid; }
//        public void setOpenid(String openid) { this.openid = openid; }
//        public String getUnionid() { return unionid; }
//        public void setUnionid(String unionid) { this.unionid = unionid; }
//        public String getEncryptUin() { return encryptUin; }
//        public void setEncryptUin(String encryptUin) { this.encryptUin = encryptUin; }
//        public int getLoginType() { return loginType; }
//        public void setLoginType(int loginType) { this.loginType = loginType; }
//        public String getRefreshKey() { return refreshKey; }
//        public void setRefreshKey(String refreshKey) { this.refreshKey = refreshKey; }
//        public String getRefreshToken() { return refreshToken; }
//        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
//
//        public static Credential fromCookies(Map<String, Object> data) {
//            Credential credential = new Credential();
//            if (data != null) {
//                credential.setMusicid(getStringValue(data, "musicid"));
//                credential.setMusickey(getStringValue(data, "musickey"));
//                credential.setOpenid(getStringValue(data, "openid"));
//                credential.setUnionid(getStringValue(data, "unionid"));
//                credential.setEncryptUin(getStringValue(data, "encrypt_uin"));
//                credential.setLoginType(getIntValue(data, "login_type"));
//                credential.setRefreshKey(getStringValue(data, "refresh_key"));
//                credential.setRefreshToken(getStringValue(data, "refresh_token"));
//            }
//            return credential;
//        }
//
//        private static String getStringValue(Map<String, Object> data, String key) {
//            Object value = data.get(key);
//            return value != null ? value.toString() : null;
//        }
//
//        private static int getIntValue(Map<String, Object> data, String key) {
//            Object value = data.get(key);
//            if (value == null) {
//                return 0;
//            }
//            if (value instanceof Number) {
//                return ((Number) value).intValue();
//            }
//            try {
//                return Integer.parseInt(value.toString());
//            } catch (NumberFormatException e) {
//                return 0;
//            }
//        }
//    }
//
//    public static QR getQRCode(QRLoginType loginType) throws Exception {
//        if (loginType == QRLoginType.WX) {
//            return getWXQR();
//        }
//        return getQQQR();
//    }
//
//    private static QR getQQQR() throws Exception {
//        HttpGet request = new HttpGet("https://ssl.ptlogin2.qq.com/ptqrshow");
//
//        // 添加必要的请求头
//        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//        request.setHeader("Accept", "image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
//        request.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
//        request.setHeader("Referer", "https://xui.ptlogin2.qq.com/");
//        request.setHeader("Connection", "keep-alive");
//        request.setHeader("Sec-Fetch-Dest", "image");
//        request.setHeader("Sec-Fetch-Mode", "no-cors");
//        request.setHeader("Sec-Fetch-Site", "same-site");
//
//        // 构建完整的URL参数
//        String url = "https://ssl.ptlogin2.qq.com/ptqrshow?appid=716027609" +
//                "&e=2" +
//                "&l=M" +
//                "&s=3" +
//                "&d=72" +
//                "&v=4" +
//                "&t=" + Math.random() +
//                "&daid=383" +
//                "&pt_3rd_aid=100497308";
//        request.setURI(new URI(url));
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            if (response.getStatusLine().getStatusCode() != 200) {
//                throw new Exception("获取二维码失败，状态码: " + response.getStatusLine().getStatusCode());
//            }
//
//            String qrsig = null;
//            Header[] headers = response.getHeaders("Set-Cookie");
//            for (Header header : headers) {
//                String cookie = header.getValue();
//                if (cookie.contains("qrsig=")) {
//                    qrsig = cookie.split("qrsig=")[1].split(";")[0];
//                    break;
//                }
//            }
//
//            if (qrsig == null) {
//                throw new Exception("获取二维码失败：未找到qrsig");
//            }
//            return new QR(EntityUtils.toByteArray(response.getEntity()), QRLoginType.QQ, "image/png", qrsig);
//        }
//    }
//
//    private static QR getWXQR() throws Exception {
//        HttpGet request = new HttpGet("https://open.weixin.qq.com/connect/qrconnect");
//        request.setHeader("Referer", "https://open.weixin.qq.com/");
//
//        Map<String, String> params = new HashMap<>();
//        params.put("appid", "wx48db31d50e334801");
//        params.put("redirect_uri", "https://y.qq.com/portal/wx_redirect.html?login_type=2&surl=https://y.qq.com/");
//        params.put("response_type", "code");
//        params.put("scope", "snsapi_login");
//        params.put("state", "STATE");
//        params.put("href", "https://y.qq.com/mediastyle/music_v17/src/css/popup_wechat.css#wechat_redirect");
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Pattern pattern = Pattern.compile("uuid=(.+?)\"");
//            Matcher matcher = pattern.matcher(responseText);
//            if (!matcher.find()) {
//                throw new Exception("获取uuid失败");
//            }
//            String uuid = matcher.group(1);
//
//            HttpGet qrRequest = new HttpGet("https://open.weixin.qq.com/connect/qrcode/" + uuid);
//            qrRequest.setHeader("Referer", "https://open.weixin.qq.com/connect/qrconnect");
//
//            try (CloseableHttpResponse qrResponse = httpClient.execute(qrRequest)) {
//                return new QR(EntityUtils.toByteArray(qrResponse.getEntity()), QRLoginType.WX, "image/jpeg", uuid);
//            }
//        }
//    }
//
//    public static Map.Entry<QRCodeLoginEvents, Credential> checkQRCode(QR qr) throws Exception {
//        if (qr.getQrType() == QRLoginType.WX) {
//            return checkWXQR(qr);
//        }
//        return checkQQQR(qr);
//    }
//
//    private static Map.Entry<QRCodeLoginEvents, Credential> checkQQQR(QR qr) throws Exception {
//        String qrsig = qr.getIdentifier();
//        HttpGet request = new HttpGet("https://ssl.ptlogin2.qq.com/ptqrlogin");
//        request.setHeader("Referer", "https://xui.ptlogin2.qq.com/");
//        request.setHeader("Cookie", "qrsig=" + qrsig);
//
//        Map<String, String> params = new HashMap<>();
//        params.put("u1", "https://graph.qq.com/oauth2.0/login_jump");
//        params.put("ptqrtoken", String.valueOf(hash33(qrsig)));
//        params.put("ptredirect", "0");
//        params.put("h", "1");
//        params.put("t", "1");
//        params.put("g", "1");
//        params.put("from_ui", "1");
//        params.put("ptlang", "2052");
//        params.put("action", "0-0-" + System.currentTimeMillis());
//        params.put("js_ver", "20102616");
//        params.put("js_type", "1");
//        params.put("pt_uistyle", "40");
//        params.put("aid", "716027609");
//        params.put("daid", "383");
//        params.put("pt_3rd_aid", "100497308");
//        params.put("has_onekey", "1");
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Pattern pattern = Pattern.compile("ptuiCB\\((.*?)\\)");
//            Matcher matcher = pattern.matcher(responseText);
//            if (!matcher.find()) {
//                throw new Exception("获取二维码状态失败");
//            }
//
//            String[] data = matcher.group(1).split(",");
//            String codeStr = data[0].trim().replace("'", "");
//            if (codeStr.matches("\\d+")) {
//                QRCodeLoginEvents event = QRCodeLoginEvents.getByValue(Integer.parseInt(codeStr));
//                if (event == QRCodeLoginEvents.DONE) {
//                    Pattern sigxPattern = Pattern.compile("&ptsigx=(.+?)&s_url");
//                    Pattern uinPattern = Pattern.compile("&uin=(.+?)&service");
//                    Matcher sigxMatcher = sigxPattern.matcher(data[2]);
//                    Matcher uinMatcher = uinPattern.matcher(data[2]);
//
//                    if (sigxMatcher.find() && uinMatcher.find()) {
//                        String sigx = sigxMatcher.group(1);
//                        String uin = uinMatcher.group(1);
//                        return new AbstractMap.SimpleEntry<>(event, authorizeQQQR(uin, sigx));
//                    }
//                }
//                return new AbstractMap.SimpleEntry<>(event, null);
//            }
//            return new AbstractMap.SimpleEntry<>(QRCodeLoginEvents.OTHER, null);
//        }
//    }
//
//    public static int hash33(String str) {
//        int hash = 0;
//        for (int i = 0; i < str.length(); i++) {
//            hash = (hash << 5) + hash + str.charAt(i);
//        }
//        return hash;
//    }
//
//    private static Credential authorizeQQQR(String uin, String sigx) throws Exception {
//        HttpGet request = new HttpGet("https://ssl.ptlogin2.graph.qq.com/check_sig");
//        request.setHeader("Referer", "https://xui.ptlogin2.qq.com/");
//
//        Map<String, String> params = new HashMap<>();
//        params.put("uin", uin);
//        params.put("pttype", "1");
//        params.put("service", "ptqrlogin");
//        params.put("nodirect", "0");
//        params.put("ptsigx", sigx);
//        params.put("s_url", "https://graph.qq.com/oauth2.0/login_jump");
//        params.put("ptlang", "2052");
//        params.put("ptredirect", "100");
//        params.put("aid", "716027609");
//        params.put("daid", "383");
//        params.put("j_later", "0");
//        params.put("low_login_hour", "0");
//        params.put("regmaster", "0");
//        params.put("pt_login_type", "3");
//        params.put("pt_aid", "0");
//        params.put("pt_aaid", "16");
//        params.put("pt_light", "0");
//        params.put("pt_3rd_aid", "100497308");
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String pskey = null;
//            Header[] headers = response.getHeaders("Set-Cookie");
//            for (Header header : headers) {
//                String cookie = header.getValue();
//                if (cookie.contains("p_skey=")) {
//                    pskey = cookie.split("p_skey=")[1].split(";")[0];
//                    break;
//                }
//            }
//
//            if (pskey == null) {
//                throw new Exception("获取p_skey失败：未找到p_skey");
//            }
//
//            HttpPost postRequest = new HttpPost("https://graph.qq.com/oauth2.0/authorize");
//            Map<String, String> postParams = new HashMap<>();
//            postParams.put("response_type", "code");
//            postParams.put("client_id", "100497308");
//            postParams.put("redirect_uri", "https://y.qq.com/portal/wx_redirect.html?login_type=1&surl=https%3A%252F%252Fy.qq.com%252F");
//            postParams.put("scope", "get_user_info,get_app_friends");
//            postParams.put("state", "state");
//            postParams.put("switch", "");
//            postParams.put("from_ptlogin", "1");
//            postParams.put("src", "1");
//            postParams.put("update_auth", "1");
//            postParams.put("openapi", "1010_1030");
//            postParams.put("g_tk", String.valueOf(hash33(pskey)));
//            postParams.put("auth_time", String.valueOf(System.currentTimeMillis()));
//            postParams.put("ui", UUID.randomUUID().toString());
//
//            try (CloseableHttpResponse postResponse = httpClient.execute(postRequest)) {
//                Header locationHeader = postResponse.getFirstHeader("Location");
//                if (locationHeader == null) {
//                    throw new Exception("获取code失败：未找到Location头");
//                }
//
//                String location = locationHeader.getValue();
//                Pattern pattern = Pattern.compile("code=([^&]+)");
//                Matcher matcher = pattern.matcher(location);
//                if (!matcher.find()) {
//                    throw new Exception("获取code失败：未找到code参数");
//                }
//                String code = matcher.group(1);
//
//                // 调用登录API获取凭证
//                return loginWithCode(code, "2");
//            }
//        }
//    }
//
//    private static Credential loginWithCode(String code, String loginType) throws Exception {
//        HttpPost request = new HttpPost("https://u.y.qq.com/cgi-bin/musicu.fcg");
//        request.setHeader("Content-Type", "application/json");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("comm", Map.of("tmeLoginType", loginType));
//        requestBody.put("music.login.LoginServer.Login", Map.of("code", code));
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Map<String, Object> responseMap = objectMapper.readValue(responseText, Map.class);
//            Map<String, Object> data = (Map<String, Object>) responseMap.get("music.login.LoginServer.Login");
//
//            if (data == null) {
//                throw new Exception("登录失败");
//            }
//
//            return Credential.fromCookies((Map<String, Object>) data);
//        }
//    }
//
//    public static Map.Entry<PhoneLoginEvents, String> sendAuthCode(int phone, int countryCode) throws Exception {
//        HttpPost request = new HttpPost("https://u.y.qq.com/cgi-bin/musicu.fcg");
//        request.setHeader("Content-Type", "application/json");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("comm", Map.of("tmeLoginMethod", "3"));
//        requestBody.put("music.login.LoginServer.SendPhoneAuthCode", Map.of(
//                "tmeAppid", "qqmusic",
//                "phoneNo", String.valueOf(phone),
//                "areaCode", String.valueOf(countryCode)
//        ));
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Map<String, Object> responseMap = objectMapper.readValue(responseText, Map.class);
//            Map<String, Object> data = (Map<String, Object>) responseMap.get("music.login.LoginServer.SendPhoneAuthCode");
//
//            if (data == null) {
//                return new AbstractMap.SimpleEntry<>(PhoneLoginEvents.OTHER, "未知错误");
//            }
//
//            int code = (int) data.get("code");
//            switch (code) {
//                case 20276:
//                    return new AbstractMap.SimpleEntry<>(PhoneLoginEvents.CAPTCHA, (String) data.get("securityURL"));
//                case 100001:
//                    return new AbstractMap.SimpleEntry<>(PhoneLoginEvents.FREQUENCY, null);
//                case 0:
//                    return new AbstractMap.SimpleEntry<>(PhoneLoginEvents.SEND, null);
//                default:
//                    return new AbstractMap.SimpleEntry<>(PhoneLoginEvents.OTHER, (String) data.get("errMsg"));
//            }
//        }
//    }
//
//    public static Credential phoneAuthorize(int phone, int authCode, int countryCode) throws Exception {
//        HttpPost request = new HttpPost("https://u.y.qq.com/cgi-bin/musicu.fcg");
//        request.setHeader("Content-Type", "application/json");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("comm", Map.of(
//                "tmeLoginMethod", "3",
//                "tmeLoginType", "0"
//        ));
//        requestBody.put("music.login.LoginServer.Login", Map.of(
//                "code", String.valueOf(authCode),
//                "phoneNo", String.valueOf(phone),
//                "areaCode", String.valueOf(countryCode),
//                "loginMode", 1
//        ));
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Map<String, Object> responseMap = objectMapper.readValue(responseText, Map.class);
//            Map<String, Object> data = (Map<String, Object>) responseMap.get("music.login.LoginServer.Login");
//
//            if (data == null) {
//                throw new Exception("登录失败");
//            }
//
//            int code = (int) data.get("code");
//            if (code == 20271) {
//                throw new Exception("验证码错误或已鉴权");
//            } else if (code == 0) {
//                return Credential.fromCookies((Map<String, Object>) data);
//            }
//            throw new Exception("未知原因导致鉴权失败");
//        }
//    }
//
//    private static Map.Entry<QRCodeLoginEvents, Credential> checkWXQR(QR qr) throws Exception {
//        String uuid = qr.getIdentifier();
//        HttpGet request = new HttpGet("https://lp.open.weixin.qq.com/connect/l/qrconnect");
//        request.setHeader("Referer", "https://open.weixin.qq.com/");
//
//        Map<String, String> params = new HashMap<>();
//        params.put("uuid", uuid);
//        params.put("_", String.valueOf(System.currentTimeMillis()));
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Pattern pattern = Pattern.compile("window\\.wx_errcode=(\\d+);window\\.wx_code='([^']*)'");
//            Matcher matcher = pattern.matcher(responseText);
//
//            if (!matcher.find()) {
//                throw new Exception("获取二维码状态失败");
//            }
//
//            String wxErrcode = matcher.group(1);
//            if (!wxErrcode.matches("\\d+")) {
//                return new AbstractMap.SimpleEntry<>(QRCodeLoginEvents.OTHER, null);
//            }
//
//            QRCodeLoginEvents event = QRCodeLoginEvents.getByValue(Integer.parseInt(wxErrcode));
//            if (event == QRCodeLoginEvents.DONE) {
//                String wxCode = matcher.group(2);
//                if (wxCode == null || wxCode.isEmpty()) {
//                    throw new Exception("获取code失败");
//                }
//                return new AbstractMap.SimpleEntry<>(event, authorizeWXQR(wxCode));
//            }
//            return new AbstractMap.SimpleEntry<>(event, null);
//        }
//    }
//
//    private static Credential authorizeWXQR(String code) throws Exception {
//        HttpPost request = new HttpPost("https://u.y.qq.com/cgi-bin/musicu.fcg");
//        request.setHeader("Content-Type", "application/json");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("comm", Map.of("tmeLoginType", "1"));
//        requestBody.put("music.login.LoginServer.Login", Map.of(
//                "code", code,
//                "strAppid", "wx48db31d50e334801"
//        ));
//
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            String responseText = EntityUtils.toString(response.getEntity());
//            Map<String, Object> responseMap = objectMapper.readValue(responseText, Map.class);
//            Map<String, Object> data = (Map<String, Object>) responseMap.get("music.login.LoginServer.Login");
//
//            if (data == null) {
//                throw new Exception("登录失败");
//            }
//
//            return Credential.fromCookies((Map<String, Object>) data);
//        }
//    }
//
//
//
//    public static void printQRCode(byte[] imageData) throws Exception {
//        // 使用 Java 内置的 ImageIO 读取图片
//        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
//        BufferedImage image = ImageIO.read(bais);
//
//        if (image == null) {
//            throw new Exception("无法读取图片数据，请检查图片格式是否正确");
//        }
//
//        // 调整图片大小以适应控制台
//        int newWidth = 50;
//        int newHeight = (int) (image.getHeight() * (newWidth / (double) image.getWidth()));
//        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics2D gc = resizedImage.createGraphics();
//        gc.drawImage(image, 0, 0, newWidth, newHeight, null);
//        gc.dispose();
//
//        // ASCII 字符集，从暗到亮
//        String asciiChars = "@%#*+=-:. ";
//
//        // 打印二维码
//        System.out.println("\n二维码预览：");
//        for (int y = 0; y < newHeight; y++) {
//            for (int x = 0; x < newWidth; x++) {
//                int rgb = resizedImage.getRGB(x, y);
//                int r = (rgb >> 16) & 0xFF;
//                int g = (rgb >> 8) & 0xFF;
//                int b = rgb & 0xFF;
//
//                // 计算灰度值
//                int gray = (r + g + b) / 3;
//
//                // 将灰度值映射到 ASCII 字符
//                int index = gray * (asciiChars.length() - 1) / 255;
//                System.out.print(asciiChars.charAt(index));
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
//        public static  String  getQQMusicQrBase64(byte[] imageData){
//        return  Base64Utils.encodeToString(imageData);
//
//    }
//
//}
