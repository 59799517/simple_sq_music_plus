package com.sqmusicplus.lm.teaboss.Qimei;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.lm.teaboss.Pow.CharacterUtils;
import com.sqmusicplus.lm.teaboss.Pow.Const;
import com.sqmusicplus.lm.teaboss.Pow.Converts;
import com.sqmusicplus.utils.RandomGenerator;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Method {
    public static final String DEFAULT_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEIxgwoutfwoJxcGQeedgP7FG9qaIuS0qzfR8gWkrkTZKM2iWHn2ajQpBRZjMSoSf6+KJGvar2ORhBfpDXyVtZCKpqLQ+FLkpncClKVIrBwv6PHyUvuCb0rIarmgDnzkfQAqVufEtR64iazGDKatvJ9y6B9NMbHddGSAUmRTCrHQIDAQAB";
    public static final String KeyChar = "123456789abcdef";

    public Method() {
    }

//    public static void main(String[] args) {
//        JSONObject mobile = new JSONObject();
//        mobile.put("brand", "M570H");
//        mobile.put("model", "Meizu PRO 6 Glob");
//        mobile.put("releasekeys", "Meizu/Meizu PRO 6 Glob/20160909.175207:user/release-keys");
//        mobile.put("androidver", "7.1.2");
//        String qimei = getQimei("763560700229518", "921f363daa159992", "71 5D 0C 3B 46 5D 52 0F C2 73 80 55 CE B7 1A 1A", "8.9.53", (String)null, mobile.toJSONString());
//        System.out.println(qimei);
//    }

    public static  HashMap<String,String>  getQimei(String version) throws Exception {
        //获取一个谁被信息
        QQDevice qQdevice = QQIMeiHelp.getQQdevice();
        //获取一个随机的payload
        Map<String, String> payload = QQIMeiHelp.randomPayloadByDevice(qQdevice, version);
        //2. 获取随机的key和nonce
        String cryptKey = RandomGenerator.generateRandomString(16);
        String nonce = RandomGenerator.generateRandomString(16);
        // 获取时间戳（秒）
        long ts = System.currentTimeMillis() / 1000;
        // 4. RSA 加密 + Base64
        String key = QQIMeiHelp.rsaEncrypt(cryptKey);
        String jsonString = JSONObject.toJSONString(payload);
        String params = QQIMeiHelp.aesEncrypt(cryptKey, jsonString);
        String extra = "{\"appKey\":\"" + QQIMeiHelp.DEFAULT_PUB_KEY + "\"}";
        String sign = QQIMeiHelp.calcMd5(
                key,
                params,
                String.valueOf(ts * 1000),
                nonce,
                QQIMeiHelp.SECRET,
                extra
        );
        JSONObject jsonObject = QQIMeiHelp.sendRequest(key, params, String.valueOf(ts * 1000), nonce, sign, extra);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data").getJSONObject("data");
        String qimei = jsonObject1.getString("q36");
        String q16 = jsonObject1.getString("q16");
        qQdevice.setQimei(qimei);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("q36", qimei);
        stringStringHashMap.put("q16", q16);
        return  stringStringHashMap;
    }


//    public static String getQimei(String imei, String androidid, String guid, String version) {
//        return getQimei(imei, androidid, guid, version, (String)null, (String)null);
//    }
//
//    public static String getQimei(String imei, String androidid, String guid, String version, String proxyString) {
//        return getQimei(imei, androidid, guid, version, proxyString, (String)null);
//    }

//    public static String getQimei(String imei, String androidid, String guid, String version, String proxyString, String mobilejson) {
//        try {
//            Qqmobile qqmobile = new Qqmobile();
//            qqmobile.setGuid(guid.replace(" ", "").toUpperCase());
//            qqmobile.setAndroidid(androidid);
//            qqmobile.setImei(imei);
//            new JSONObject();
//            if (mobilejson != null) {
//                JSONObject mobile = JSONObject.parseObject(mobilejson);
//                qqmobile.setBrand(mobile.getString("brand"));
//                qqmobile.setModel(mobile.getString("model"));
//                qqmobile.setReleasekeys(mobile.getString("releasekeys"));
//                qqmobile.setAndroidver(mobile.getString("androidver"));
//            } else {
//                qqmobile.setBrand("Galaxy");
//                qqmobile.setModel("Galaxy S6 edge");
//                qqmobile.setReleasekeys("Galaxy/Galaxy S6 edge/20160909.175207:user/release-keys");
//                qqmobile.setAndroidver("10");
//            }
//
//            LinkedHashMap<String, Object> map = genRandomPayloadByDevice(qqmobile, version);
//            JSONObject json = new JSONObject(map);
//            byte[] payload = json.toString().getBytes();
//            String cryptKey = CharacterUtils.RandomStringRange(16, "123456789abcdef");
//            long ts = System.currentTimeMillis() / 1000L * 1000L;
//            String nonce = CharacterUtils.RandomStringRange(16, "123456789abcdef");
//            byte[] encryptedAesKey = RSAEncrypt.encryptByByte(cryptKey, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEIxgwoutfwoJxcGQeedgP7FG9qaIuS0qzfR8gWkrkTZKM2iWHn2ajQpBRZjMSoSf6+KJGvar2ORhBfpDXyVtZCKpqLQ+FLkpncClKVIrBwv6PHyUvuCb0rIarmgDnzkfQAqVufEtR64iazGDKatvJ9y6B9NMbHddGSAUmRTCrHQIDAQAB");
//            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, cryptKey.getBytes(), cryptKey.getBytes());
//            byte[] encryptedPayload = aes.encrypt(payload);
//            String key = Base64.encode(encryptedAesKey);
//            String params = Base64.encode(encryptedPayload);
//            JSONObject param = new JSONObject();
//            param.put("key", key);
//            param.put("params", params);
//            param.put("time", ts);
//            param.put("nonce", nonce);
//            param.put("sign", sign(key, params, ts, nonce));
//            param.put("extra", "");
//            String url = "https://snowflake.qq.com/ola/android";
//            boolean useproxy = true;
//            boolean useauth = false;
//            String proxy_ip = "";
//            int proxy_port = 0;
//            String username = "";
//            String password = "";
//            if (proxyString != null && proxyString.trim().length() != 0) {
//                String[] proxy = proxyString.split(":");
//                if (proxy.length == 4) {
//                    useauth = true;
//                    proxy_ip = proxy[0];
//                    proxy_port = Integer.parseInt(proxy[1]);
//                    username = proxy[2];
//                    password = proxy[3];
//                } else {
//                    useauth = false;
//                    proxy_ip = proxy[0];
//                    proxy_port = Integer.parseInt(proxy[1]);
//                }
//            } else {
//                useproxy = false;
//            }
//
//            HttpClientContext context;
//            CloseableHttpClient client;
//
//            client = HttpClients.createDefault();
//            context = HttpClientContext.create();
//
//            new JSONObject();
//            HttpPost httpPost = new HttpPost(url);
//            StringEntity entity = new StringEntity(param.toJSONString(), "UTF-8");
//            entity.setContentEncoding("UTF-8");
//            entity.setContentType("application/json");
//            httpPost.setEntity(entity);
//            HttpResponse response = client.execute(httpPost, context);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//                JSONObject result = JSON.parseObject(responseString);
//                if (result.getInteger("code") == 0 && result.getString("data").length() > 0) {
//                    byte[] encryptedResponse = Base64.decode(result.getString("data"));
//                    byte[] decryptedResponse = aes.decrypt(encryptedResponse);
//                    JSONObject qimei = JSON.parseObject(new String(decryptedResponse));
//                    return qimei.toJSONString();
//                }
//            }
//        } catch (IOException var38) {
//            var38.printStackTrace();
//        }
//
//        return "";
//    }

    public static String sign(String key, String params, long ts, String nonce) {
        return Converts.MD5EncodeToHex(key + params + ts + nonce).toLowerCase();
    }

    public static LinkedHashMap<String, Object> genRandomPayloadByDevice(Qqmobile qqmobile, String version) {
        long now = System.currentTimeMillis() / 1000L;
        long seed = 1780L;
        if (qqmobile.getGuid() != null) {
            byte[] guid = qqmobile.getGuidBin();

            for(int i = 0; i < guid.length; ++i) {
                byte b = guid[i];
                seed += (long)(b & 255);
            }
        }

        Random fixedRand = new Random(seed);
        Date date = new Date((now - (long)fixedRand.nextInt(14400)) * 1000L);
        String uptimes = Const.TC.getStrDatetime(date);
        LinkedHashMap<String, Object> reserved = new LinkedHashMap();
        reserved.put("harmony", "0");
        reserved.put("clone", "0");
        reserved.put("containe", "");
        reserved.put("oz", "UhYmelwouA+V2nPWbOvLTgN2/m8jwGB+yUB5v9tysQg=");
        reserved.put("oo", "Xecjt+9S1+f8Pz2VLSxgpw==");
        reserved.put("kelong", "0");
        reserved.put("uptimes", uptimes);
        reserved.put("multiUser", "0");
        reserved.put("bod", qqmobile.getModel());
        reserved.put("brd", qqmobile.getBrand());
        reserved.put("dv", qqmobile.getModel());
        reserved.put("firstLevel", "");
        reserved.put("manufact", qqmobile.getBrand());
        reserved.put("name", qqmobile.getModel());
        reserved.put("host", "se.infra");
        reserved.put("kernel", qqmobile.getReleasekeys());
        JSONObject json = new JSONObject(reserved);
        String deviceType = "Phone";
        String beaconId = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-01");
        String timeMonth = df.format(new Date());
        int rand1 = fixedRand.nextInt(899999) + 100000;
        int rand2 = fixedRand.nextInt(899999999) + 100000000;

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
                    beaconId = beaconId + "k" + i + ":" + fixedRand.nextInt(10000);
            }

            beaconId = beaconId + ";";
        }

        Device device = new Device(version);
        LinkedHashMap<String, Object> map = new LinkedHashMap();
        map.put("androidId", qqmobile.getAndroidid());
        map.put("platformId", "1");
        map.put("appKey", "0S200MNJT807V3GE");
        map.put("appVersion", device.getQq_completename());
        map.put("beaconIdSrc", beaconId);
        map.put("brand", qqmobile.getBrand());
        map.put("channelId", "2017");
        map.put("cid", "");
        map.put("imei", qqmobile.getImei());
        map.put("imsi", "");
        map.put("mac", "");
        map.put("model", qqmobile.getModel());
        map.put("networkType", "unknown");
        map.put("oaid", "");
        map.put("osVersion", "Android " + qqmobile.getAndroidver() + ",level " + qqmobile.getApiLevel(qqmobile.getAndroidver()));
        map.put("qimei", "");
        map.put("qimei36", "");
        map.put("sdkVersion", "1.2.13.6");
        map.put("targetSdkVersion", "26");
        map.put("audit", "");
        map.put("userId", "{}");
        map.put("packageId", "com.tencent.mobileqq");
        map.put("deviceType", deviceType);
        map.put("sdkName", "");
        map.put("reserved", json.toString());
        return map;
    }

//    static class FakeDnsResolver implements DnsResolver {
//        FakeDnsResolver() {
//        }
//
//        public InetAddress[] resolve(String host) throws UnknownHostException {
//            return new InetAddress[]{InetAddress.getByAddress(new byte[]{1, 1, 1, 1})};
//        }
//    }

//    static class MyAuthenticator extends Authenticator {
//        private String username = "";
//        private String password = "";
//
//        public MyAuthenticator(String username, String password) {
//            this.username = username;
//            this.password = password;
//        }
//
//        protected PasswordAuthentication getPasswordAuthentication() {
//            return new PasswordAuthentication(this.username, this.password.toCharArray());
//        }
//    }
//
//    static class MyConnectionSocketFactory extends PlainConnectionSocketFactory {
//        MyConnectionSocketFactory() {
//        }
//
//        public Socket createSocket(HttpContext context) throws IOException {
//            InetSocketAddress socksaddr = (InetSocketAddress)context.getAttribute("socks.address");
//            Proxy proxy = new Proxy(Type.SOCKS, socksaddr);
//            return new Socket(proxy);
//        }
//
//        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
//            InetSocketAddress unresolvedRemote = InetSocketAddress.createUnresolved(host.getHostName(), remoteAddress.getPort());
//            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
//        }
//    }
//
//    static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {
//        public MySSLConnectionSocketFactory(SSLContext sslContext) {
//            super(sslContext);
//        }
//
//        public Socket createSocket(HttpContext context) throws IOException {
//            InetSocketAddress socksaddr = (InetSocketAddress)context.getAttribute("socks.address");
//            Proxy proxy = new Proxy(Type.SOCKS, socksaddr);
//            return new Socket(proxy);
//        }
//
//        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
//            InetSocketAddress unresolvedRemote = InetSocketAddress.createUnresolved(host.getHostName(), remoteAddress.getPort());
//            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
//        }
//    }
}
