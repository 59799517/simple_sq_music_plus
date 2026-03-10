package com.sqmusicplus.v3.plug.qq.util;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.teaboss.Pow.CharacterUtils;
import com.sqmusicplus.v3.teaboss.Qimei.QQDevice;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Classname QQMusicUtil
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/4/30 10:02
 * @Created by SQ
 */
@Slf4j
public class QQMusicUtil {


    private static final char[] ALLOWED_CHARS = "abcdef1234567890".toCharArray();
    private static final Random RANDOM = new Random();
    private static final String DEFAULT_QIMEI_APPID_KEY = "qimei_qq_androidpzAuCmaFAaFaHrdakPjLIEqKrGnSOOvH";

    private static final String VERSION = "13.2.5.8";
    private static  String  Q36="";

    public static void main(String[] args) throws Exception {
        String qq = "59799517";
        String musicKey = "Q_H_L_63k3NcDPrQrsuD0_09pHm4Ne1fJdP9A7e2qG7XldYLEew_T7YtgIphCETu41_Hs3NfJ4YBZ7jd5DL3O1ZFRZcgHdzXFI1lJ_BkbDOIvw9zb7uD-bmoIfXOC5e-AS-B6x760wJsW7n-PVN_d1Fqf9Y7dg";
        String loginType = "2";
        String songmid = "0039MnYb0qxYhV";
        String fileName = "F000"+songmid+songmid+".flac";

        String data =downloadRequestParam(qq,musicKey,loginType,fileName,songmid);
        System.out.println(data);
        String sign = sign(data);
        System.out.println(sign);


    }


    /**
     * 获取下载链接
     */
    public  static  String downloadRequestParam(String qq,String musicKey,String loginType ,String filename,String songmid) {
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
                          "%s"
                        ],
                        "guid": "%s",
                        "songmid": [
                          "%s"
                        ],
                        "songtype": [
                          0
                        ],
                        "uin": "%s",
                        "loginflag": 1,
                        "platform": "20"
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
                qq
        );
        return format;
    }



    public static String getQ36(){
        if (com.sqmusicplus.v3.utils.StringUtils.isEmpty(Q36)){
            Map<String, String> q36AndQ16 = getQ36AndQ16();
            Q36= q36AndQ16.get("q16");
            return Q36;
        }
        return Q36;

    }


    /**
     * 获取请求参数的q36
     * @return {\"q16\":\"fde9508748b00283b2723a9210001b617301\",\"q36\":\"8a3a4a1580693c8b7e59b97510001ad1730c\"}
     */
    public static Map getQ36AndQ16() {
        try {

            Map<String, String> payload = randomPayloadByDevice(getQQdevice(), VERSION);
//            System.out.println(JSONObject.toJSONString(payload));
            String crypt_key = generateRandom(16);
            String nonce = generateRandom(16);
//            System.out.println("crypt_key:" + crypt_key);
//            System.out.println("nonce:" + nonce);
            //获得一节秒的时间戳不是日期
            long ts = System.currentTimeMillis() / 1000;
            String key = rsaEncrypt(crypt_key);
            String params = aesEncrypt(crypt_key, JSONObject.toJSONString(payload));
//            System.out.println("key:" + key);
//            System.out.println("ts:" + ts);
//            System.out.println("params:" + params);
            String extra = "{\"appKey\":\"" + APP_KEY + "\"}";
            extra = String.format(extra, APP_KEY);
//            System.out.println("extra:" + extra);
            String sign = calcMd5(key, params, (ts + 1000) + "", nonce, SECRET, extra);
//            System.out.println("sign:" + sign);

            OkHttpUtils builder = OkHttpUtils.builder();
            String url = "https://api.tencentmusic.com/tme/trpc/proxy";
            builder.url(url)
                    .addHeader("Host", "api.tencentmusic.com")
                    .addHeader("method", "GetQimei")
                    .addHeader("service", "trpc.tme_datasvr.qimeiproxy.QimeiProxy")
                    .addHeader("appid", "qimei_qq_android")
                    .addHeader("sign", calcMd5(DEFAULT_QIMEI_APPID_KEY, ts + ""))
                    .addHeader("user-agent", "QQMusic")
                    .addHeader("timestamp", ts + "");
            LinkedHashMap<String, Object> jsondata = new LinkedHashMap<>();
            jsondata.put("app", 0);
            jsondata.put("os", 1);
            LinkedHashMap<String, String> qimeiParamsdata = new LinkedHashMap<>();
            qimeiParamsdata.put("key", key);
            qimeiParamsdata.put("params", params);
            qimeiParamsdata.put("time", ts + "");
            qimeiParamsdata.put("nonce", nonce);
            qimeiParamsdata.put("sign", sign);
            qimeiParamsdata.put("extra", extra);
            jsondata.put("qimeiParams", qimeiParamsdata);
            builder.post(false, JSONObject.toJSONString(jsondata));
            String result = builder.sync();
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("code") == 0) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject data1 = data.getJSONObject("data");
                //将data1转为map
                Map<String, Object> map = data1.toJavaObject(Map.class);
                return map;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    //生成gui的方法
    public static String getGuid() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int index = RANDOM.nextInt(ALLOWED_CHARS.length);
            sb.append(ALLOWED_CHARS[index]);
        }
        return sb.toString();
    }


    /**
     * 生成QQ音乐请求签名
     * @param jsondata 请求参数Map
     * @return 处理后的签名字符串（小写且移除特殊字符）
     * @throws Exception 可能抛出的异常包括：
     *                   - NoSuchAlgorithmException (MD5算法不存在)
     *                   - JSON序列化异常
     * @implNote 签名流程：
     * 1. JSON序列化请求参数
     * 2. 计算MD5并转为大写
     * 3. 提取头/尾/中间特征数据
     * 4. Base64编码中间数据
     * 5. 拼接最终签名并清理特殊字符
     */
    public static String sign(String jsondata) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(jsondata.getBytes(StandardCharsets.UTF_8));
        String md5Str = bytesToHex(md5Bytes).toUpperCase();

        List<Integer> h = head(md5Str.getBytes());
        List<Integer> e = tail(md5Str.getBytes());
        List<Integer> ls = middle(md5Str.getBytes());

        byte[] mBytes = new byte[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            mBytes[i] = ls.get(i).byteValue();
        }
        String m = Base64.getEncoder().encodeToString(mBytes);

        StringBuilder res = new StringBuilder("zzb");
        h.forEach(c -> res.append((char) c.intValue()));
        res.append(m);
        e.forEach(c -> res.append((char) c.intValue()));

        return res.toString().toLowerCase()
                .replace("/", "")
                .replace("+", "")
                .replace("=", "");
    }





    // MD5 计算方法（sing签名计算方法）
    public static String calcMd5(String... parts) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part);
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    /**
     * 从MD5字节数组中提取头部特征字节
     * @param b MD5字节数组（长度应>=31）
     * @return 包含索引[21,4,9,26,16,20,27,30]处字节的列表（处理过无符号值）
     */
    private static List<Integer> head(byte[] b) {
        int[] p = {21, 4, 9, 26, 16, 20, 27, 30};
        return extractBytes(b, p);
    }

    /**
     * 从MD5字节数组中提取尾部特征字节
     * @param b MD5字节数组（长度应>=25）
     * @return 包含索引[18,11,3,2,1,7,6,25]处字节的列表（处理过无符号值）
     */
    private static List<Integer> tail(byte[] b) {
        int[] p = {18, 11, 3, 2, 1, 7, 6, 25};
        return extractBytes(b, p);
    }

    /**
     * 处理MD5中间部分生成签名核心数据
     * @param b MD5字符串的字节数组（应为32字符的大写MD5）
     * @return 经过转换和异或处理后的字节列表
     * @implNote 处理逻辑：
     * 1. 每两个字符转换成一个字节（十六进制解析）
     * 2. 与预定义的ol数组进行异或运算
     */
    private static List<Integer> middle(byte[] b) {
        Map<Character, Integer> zd = new HashMap<>();
        String keys = "0123456789ABCDEF";
        for (int i = 0; i < keys.length(); i++) {
            zd.put(keys.charAt(i), i);
        }

        int[] ol = {212, 45, 80, 68, 195, 163, 163, 203, 157, 220, 254, 91, 204, 79, 104, 6};
        List<Integer> res = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < b.length; i += 2) {
            char c1 = (char) (b[i] & 0xFF);
            char c2 = (char) (b[i+1] & 0xFF);

            int one = zd.get(c1);
            int two = zd.get(c2);
            int r = (one * 16) ^ two;
            res.add(r ^ ol[j]);
            j++;
        }
        return res;
    }


    /**
     * 从字节数组中提取指定位置的字节
     * @param b 原始字节数组
     * @param positions 需要提取的位置索引数组
     * @return 提取后的字节列表（处理过无符号值）
     */
    private static List<Integer> extractBytes(byte[] b, int[] positions) {
        List<Integer> result = new ArrayList<>();
        for (int pos : positions) {
            result.add((int) b[pos] & 0xFF);
        }
        return result;
    }

    /**
     * 字节转字符串
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }




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
    public static String generateRandom(int length) {
        String chars = "adbcdef1234567890";
        StringBuilder nonce = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(chars.length());
            nonce.append(chars.charAt(index));
        }
        return nonce.toString();
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

}
