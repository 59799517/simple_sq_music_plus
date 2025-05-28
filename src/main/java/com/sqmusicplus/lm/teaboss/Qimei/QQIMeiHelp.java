package com.sqmusicplus.lm.teaboss.Qimei;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.lm.teaboss.Pow.CharacterUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Classname QQIMeiHelp
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/4/27 13:56
 * @Created by SQ
 */

public class QQIMeiHelp {
public static final  String APP_KEY = "0AND0HD6FE4HY80F";
public static final String DEFAULT_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEIxgwoutfwoJxcGQeedgP7FG9qaIuS0qzfR8gWkrkTZKM2iWHn2ajQpBRZjMSoSf6+KJGvar2ORhBfpDXyVtZCKpqLQ+FLkpncClKVIrBwv6PHyUvuCb0rIarmgDnzkfQAqVufEtR64iazGDKatvJ9y6B9NMbHddGSAUmRTCrHQIDAQAB";
public static final String SECRET = "ZdJqM15EeO2zWc08";


    public static QQDevice getQQdevice() {
        //从数据库获取没有就返回默认的
        QQDevice qqDevice = new QQDevice();
        return qqDevice;
    }


    public static Map<String, String> randomPayloadByDevice(QQDevice device, String version) {
        // 生成 reserved 数据
        int fixedRand = ThreadLocalRandom.current().nextInt(0, 14401);
        Map<String, String> reserved = new HashMap<>();
        reserved.put("harmony", "0");
        reserved.put("clone", "0");
        reserved.put("containe", "");
        reserved.put("oz", "UhYmelwouA+V2nPWbOvLTgN2/m8jwGB+yUB5v9tysQg=");
        reserved.put("oo", "Xecjt+9S1+f8Pz2VLSxgpw==");
        reserved.put("kelong", "0");
        reserved.put("uptimes", LocalDateTime.now()
                .minusSeconds(fixedRand)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        reserved.put("multiUser", "0");
        reserved.put("bod", device.getBrand());
        reserved.put("dv", device.getDevice());
        reserved.put("firstLevel", "");
        reserved.put("manufact", device.getBrand());
        reserved.put("name", device.getModel());
        reserved.put("host", "se.infra");
        reserved.put("kernel", device.getProcVersion());

        // 构建主 payload
        Map<String, String> payload = new HashMap<>();
        payload.put("androidId", device.getAndroidId());
        payload.put("platformId", "1");
        payload.put("appKey", APP_KEY);
        payload.put("appVersion", version);
        payload.put("beaconIdSrc", randomBeaconId());
        payload.put("brand", device.getBrand());
        payload.put("channelId", "10003505");
        payload.put("cid", "");
        payload.put("imei", device.getImei());
        payload.put("imsi", "");
        payload.put("mac", "");
        payload.put("model", device.getModel());
        payload.put("networkType", "unknown");
        payload.put("oaid", "");
        payload.put("osVersion", String.format("Android %s,level %d",
                device.getVersion().getRelease(), device.getVersion().getSdk()));
        payload.put("qimei", "");
        payload.put("qimei36", "");
        payload.put("sdkVersion", "1.2.13.6");
        payload.put("targetSdkVersion", "33");
        payload.put("audit", "");
        payload.put("userId", "{}");
        payload.put("packageId", "com.tencent.qqmusic");
        payload.put("deviceType", "Phone");
        payload.put("sdkName", "");
        payload.put("reserved", JSONObject.toJSONString(reserved));

        return payload;
    }

    private static String randomBeaconId() {
        String timeMonth = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-")) + "01";
        int rand1 = ThreadLocalRandom.current().nextInt(100000, 1000000);
        int rand2 = ThreadLocalRandom.current().nextInt(100000000, 1000000000);

    String beaconId ="";
        for(int i = 1; i <= 40; ++i) {
            switch (i) {
                case 1:
                case 2:
                case 13:
                case 14:
                case 17:
                case 18:
                case 21:
                case 22:
                case 25:
                case 26:
                case 29:
                case 30:
                case 33:
                case 34:
                case 37:
                case 38:
                    beaconId = beaconId + "k" + i + ":" + timeMonth + rand1 + "." + rand2;
                    break;
                case 3:
                    beaconId = beaconId + "k3:0000000000000000";
                    break;
                case 4:
                    beaconId = beaconId + "k4:" + CharacterUtils.RandomStringRange(16, "123456789abcdef");
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 15:
                case 16:
                case 19:
                case 20:
                case 23:
                case 24:
                case 27:
                case 28:
                case 31:
                case 32:
                case 35:
                case 36:
                default:
                    beaconId = beaconId + "k" + i + ":" + ThreadLocalRandom.current().nextInt(0, 10000);
            }

            beaconId = beaconId + ";";
        }
        return beaconId;
    }

    // RSA 加密方法
    public static String rsaEncrypt(String data) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(DEFAULT_PUB_KEY));
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    // AES 加密方法
    public static String aesEncrypt(String key, String data) throws Exception {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = cipher.doFinal(dataBytes);
       return Base64.getEncoder().encodeToString(bytes);
    }
    // MD5 计算方法
    public static String calcMd5(String... parts) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part);
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public static JSONObject sendRequest(String key, String params, String ts,
                                   String nonce, String sign, String extra) throws Exception {


        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("app", 0);
        requestBody.put("os", 1);

        Map<String, Object> qimeiParams = new LinkedHashMap<>();
        qimeiParams.put("key", key);
        qimeiParams.put("params", params);
        qimeiParams.put("time", ts);
        qimeiParams.put("nonce", nonce);
        qimeiParams.put("sign", sign);
        qimeiParams.put("extra", extra);
        requestBody.put("qimeiParams", qimeiParams);


        // 1. 创建 HTTP 客户端
        QQSession session = QQSession.getCurrentSession();
        HttpRequest request = session.buildRequest("https://api.tencentmusic.com/tme/trpc/proxy")
                .header("method", "GetQimei")
                .header("service", "trpc.tme_datasvr.qimeiproxy.QimeiProxy")
                .header("appid", "qimei_qq_android")
                .header("sign", sign)
                .header("user-agent", "QQusic")
                .header("timestamp", ts)
                .POST(HttpRequest.BodyPublishers.ofString(JSONObject.toJSONString(requestBody))).build();


        HttpResponse<String> response = session.getClient().send(
                request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        return JSONObject.parseObject(body);


    }

}
