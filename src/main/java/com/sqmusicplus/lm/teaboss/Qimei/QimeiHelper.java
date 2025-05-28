//package com.sqmusicplus.lm.teaboss.Qimei;
//
//import com.google.gson.Gson;
//import okhttp3.*;
//import javax.crypto.*;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.*;
//import java.security.spec.X509EncodedKeySpec;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.Base64;
//import java.util.concurrent.ThreadLocalRandom;
///**
// * @Classname QimeiHelper
// * @Description TODO
// * @Version 1.0.0
// * @Date 2025/4/27 13:53
// * @Created by Administrator
// */
//
//public class QimeiHelper {
//    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEIxgwoutfwoJxcGQeedgP7FG9qaIuS0qzfR8gWkrkTZKM2iWHn2ajQpBRZjMSoSf6+KJGvar2ORhBfpDXyVtZCKpqLQ+FLkpncClKVIrBwv6PHyUvuCb0rIarmgDnzkfQAqVufEtR64iazGDKatvJ9y6B9NMbHddGSAUmRTCrHQIDAQAB";
//    private static final String SECRET = "ZdJqM15EeO2zWc08";
//    private static final String APP_KEY = "0AND0HD6FE4HY80F";
//    private static final OkHttpClient client = new OkHttpClient();
//
//    public static class QimeiResult {
//        public String q16;
//        public String q36;
//    }
//
//    public static byte[] rsaEncrypt(byte[] content) throws Exception {
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY));
//        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
//
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        return cipher.doFinal(content);
//    }
//
//    public static byte[] aesEncrypt(byte[] keyBytes, byte[] content) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
//        IvParameterSpec iv = new IvParameterSpec(keyBytes);
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
//        return cipher.doFinal(content);
//    }
//
//    private static String randomBeaconId() {
//        // 实现同Python版本，省略部分代码...
//    }
//
//    private static Map<String, Object> randomPayloadByDevice(Device device, String version) {
//        Map<String, Object> reserved = new HashMap<>();
//        reserved.put("harmony", "0");
//        reserved.put("uptimes", LocalDateTime.now().minusSeconds(randomInt(0,14400))
//                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        // 其他字段初始化...
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("androidId", device.androidId);
//        payload.put("brand", device.brand);
//        payload.put("reserved", gson.toJson(reserved));
//        // 其他字段初始化...
//
//        return payload;
//    }
//
//    public static QimeiResult getQimei(String version) throws Exception {
//        Device device = QQDevice.getCachedDevice();
//        try {
//            Map<String, Object> payload = randomPayloadByDevice(device, version);
//            String cryptKey = randomString(16, "abcdef1234567890");
//            String nonce = randomString(16, "abcdef1234567890");
//            long ts = System.currentTimeMillis() / 1000;
//
//            // 加密处理
//            String keyBase64 = Base64.getEncoder().encodeToString(rsaEncrypt(cryptKey.getBytes()));
//            byte[] encryptedParams = aesEncrypt(cryptKey.getBytes(), gson.toJson(payload).getBytes());
//            String paramsBase64 = Base64.getEncoder().encodeToString(encryptedParams);
//
//            // 构建请求
//            Request request = new Request.Builder()
//                    .url("https://api.tencentmusic.com/tme/trpc/proxy")
//                    .addHeader("timestamp", String.valueOf(ts))
//                    .post(RequestBody.create(gson.toJson(buildRequestBody(keyBase64, paramsBase64, nonce, ts)),
//                            MediaType.parse("application/json")))
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            // 解析响应...
//
//            device.qimei = result.q36;
//            DeviceHelper.saveDevice(device);
//            return result;
//        } catch (IOException e) {
//            if (device.qimei != null) {
//                QimeiResult result = new QimeiResult();
//                result.q36 = device.qimei;
//                return result;
//            }
//            // 日志处理...
//            throw e;
//        }
//    }
//
//    // 辅助方法
//    private static int randomInt(int min, int max) {
//        return ThreadLocalRandom.current().nextInt(min, max + 1);
//    }
//
//    private static String randomString(int length, String chars) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            sb.append(chars.charAt(randomInt(0, chars.length()-1)));
//        }
//        return sb.toString();
//    }
//}
