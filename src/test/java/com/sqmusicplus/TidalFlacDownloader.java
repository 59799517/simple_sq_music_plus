package com.sqmusicplus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Base64;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Classname TidalFlacDownloader
 * @Description TODO
 * @Date 2026/4/30 09:37
 * @Created by SQ
 */
public class TidalFlacDownloader {

    // ====================== 代理配置（和你的浏览器代理一致） ======================
    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 7897;
    private static final boolean ENABLE_PROXY = true; // 开启/关闭代理

    // ====================== 业务配置 ======================
    private static final long TEST_TRACK_ID = 64750834;
    private static final String QUALITY = "LOSSLESS";
    private static final String TIDAL_DECRYPT_KEY = "UIlTTEMmmLfGowo/UC60xw==";

    private static final List<String> ALL_AVAILABLE_APIS = List.of(
            "https://eu-central.monochrome.tf",
            "https://us-west.monochrome.tf",
            "https://api.monochrome.tf",
            "https://triton.squid.wtf",
            "https://tidal.kinoplus.online"
    );

    // ====================== 带代理的OkHttpClient ======================
    private static final OkHttpClient HTTP_CLIENT;
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS);

        // 开启代理
        if (ENABLE_PROXY) {
            System.out.println("✅ 已启用本地代理：" + PROXY_HOST + ":" + PROXY_PORT);
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)));
            // 【可选】代理需要账号密码时开启
            // builder.proxyAuthenticator((route, response) -> {
            //     String credential = Credentials.basic("用户名", "密码");
            //     return response.request().newBuilder().header("Proxy-Authorization", credential).build();
            // });
        }

        HTTP_CLIENT = builder.build();
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Tidal 代理测试工具 (JDK21 虚拟线程)");
        System.out.println("========================================\n");

        // 先验证代理是否生效
        checkProxyValid();

        // 测试Tidal接口
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String api : ALL_AVAILABLE_APIS) {
                executor.submit(() -> testSingleApi(api));
            }
        }
    }

    // ====================== 验证代理是否生效 ======================
    private static void checkProxyValid() {
        System.out.println("🔍 正在验证代理是否生效...");
        try {
            Request request = new Request.Builder().url("https://httpbin.org/ip").get().build();
            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    System.out.println("✅ 代理验证成功，当前出口IP：" + JSON.parseObject(body).getString("origin"));
                } else {
                    System.out.println("❌ 代理验证失败，HTTP状态码：" + response.code());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ 代理连接失败：" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------\n");
    }

    // ====================== 测试单个API ======================
    private static void testSingleApi(String apiUrl) {
        String fullUrl = apiUrl + "/track/?id=" + TEST_TRACK_ID + "&quality=" + QUALITY;
        System.out.println("🔗 测试API：" + apiUrl);
        System.out.println("   请求地址：" + fullUrl);

        try {
            Request request = new Request.Builder().url(fullUrl).get().build();
            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
                String body = response.body().string();
                System.out.println("   响应状态：HTTP " + response.code());
                System.out.println("   响应内容：" + (body.length() > 200 ? body.substring(0, 200) + "..." : body));

                if (response.isSuccessful() && body.contains("manifest") || body.contains("OriginalTrackUrl")) {
                    System.out.println("   🎉 该API可用！");
                }
            }
        } catch (Exception e) {
            System.out.println("   ❌ 请求失败：" + e.getMessage());
        }
        System.out.println();
    }

    // ====================== Manifest解密方法 ======================
    private static String decryptManifest(String manifestB64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(TIDAL_DECRYPT_KEY);
        byte[] encryptedData = Base64.getDecoder().decode(manifestB64);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        JSONObject manifestObj = JSON.parseObject(new String(decryptedBytes));
        return manifestObj.getJSONArray("urls").getString(0);
    }
}
