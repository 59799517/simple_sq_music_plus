package com.sqmusicplus.v3.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Classname OkHttpUtils
 * @Description 支持默认流量监控的HTTP工具类
 * @Version 1.1.0 (默认启用流量监控)
 * @Date 2023/8/25 9:56
 * @Created by Administrator
 */

public class OkHttpUtils {
    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private Request.Builder request;
    private static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    
    // 默认启用流量监控的拦截器
    private static final Interceptor trafficInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            
            // 记录请求体大小（上传流量）
            long requestBodySize = 0;
            if (request.body() != null) {
                requestBodySize = request.body().contentLength();
                if (requestBodySize > 0) {
                    SystemUtils.recordAppUpload(requestBodySize);
                }
            }
            
            // 执行请求
            Response response = chain.proceed(request);
            
            // 记录响应体大小（下载流量）
            long responseBodySize = response.body().contentLength();
            if (responseBodySize > 0) {
                SystemUtils.recordAppDownload(responseBodySize);
            }
            
            return response;
        }
    };

    /**
     * 初始化okHttpClient，并且允许https访问
     * 默认启用流量监控拦截器
     */
    private OkHttpUtils(boolean followRedirects) {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    File cacheDirectory = new File(System.getProperty("java.io.tmpdir"), "OkHttpCache");
                    Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024); // 10MB 缓存

                    TrustManager[] trustManagers = buildTrustManagers();
                    
                    // 默认添加流量监控拦截器
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(0, TimeUnit.MILLISECONDS)
                            .writeTimeout(0, TimeUnit.MILLISECONDS)
                            .readTimeout(0, TimeUnit.MILLISECONDS)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier((hostName, session) -> true)
                            .retryOnConnectionFailure(true)
                            .followRedirects(followRedirects)
                            .followSslRedirects(followRedirects)
                            .cache(cache)
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
                            .addInterceptor(trafficInterceptor) // 默认启用流量监控
                            .build();
                    
                    addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                }
            }
        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (OkHttpUtils.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     * @return
     */
    public static OkHttpUtils builder() {
        return new OkHttpUtils(true);
    }
    /**
     * 创建OkHttpUtils禁止重定向
     */
    public static OkHttpUtils builder(boolean followRedirects) {
        return new OkHttpUtils(followRedirects);
    }

    public static Call newCall(Request request) {
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient.newCall(request);

    }

    /**
     * 添加 cookie
     *
     */
    public OkHttpUtils addCookie(String cookie) {
        addHeader("Cookie", cookie);
        return this;
    }


    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttpUtils url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtils addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * 添加参数
     *
     * @param param   参数名
     * @return
     */
    public OkHttpUtils addParam(Map<String, String> param) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.putAll(param);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtils addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * 初始化get方法
     *
     * @return
     */
    public OkHttpUtils get() {
        request = new Request.Builder().get()
                .cacheControl(new CacheControl.Builder()
                        .maxAge(10, TimeUnit.MINUTES) // 新增：缓存有效期10分钟
                        .build());
        StringBuilder urlBuilder = new StringBuilder(this.url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * 初始化post方法
     *
     * @param isJsonPost true等于json的方式提交数据，类似postman里post方法的raw
     *                   false等于普通的表单提交
     * @return
     */
    public OkHttpUtils post(boolean isJsonPost) {
        RequestBody requestBody;
        if (isJsonPost) {
            String json = "";
            if (paramMap != null) {
                json = JSON.toJSONString(paramMap);
            }
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder formBody = new FormBody.Builder();
            if (paramMap != null) {
                paramMap.forEach(formBody::add);
            }
            requestBody = formBody.build();
        }
        request = new Request.Builder().post(requestBody)
                .url(url)  .cacheControl(new CacheControl.Builder()
                        .maxAge(10, TimeUnit.MINUTES) // 新增：缓存有效期10分钟
                        .build());
        return this;
    }
    public OkHttpUtils post(RequestBody requestBody) {
        request = new Request.Builder().post(requestBody).url(url).cacheControl(new CacheControl.Builder()
                .maxAge(10, TimeUnit.MINUTES) // 新增：缓存有效期10分钟
                .build());
        return this;
    }


    /**
     * 初始化post方法
     *
     * @param isJsonPost true等于json的方式提交数据，类似postman里post方法的raw
     *                   false等于普通的表单提交
     * @return
     */
    public OkHttpUtils post(boolean isJsonPost,String bodyStr) {
        RequestBody requestBody;
        if (isJsonPost) {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyStr);
        } else {

            requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), bodyStr);

        }
        request = new Request.Builder().post(requestBody).url(url).cacheControl(new CacheControl.Builder()
                .maxAge(10, TimeUnit.MINUTES) // 新增：缓存有效期10分钟
                .build());
        return this;
    }

    public OkHttpUtils post(boolean isJsonPost, JSONObject bodyStr) {
        return post(isJsonPost, bodyStr.toJSONString());
    }
    /**
     * 同步请求
     *
     * @return
     */
    public String sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
           return "请求失败：" + e.getMessage();
        }
    }

    /**
     * 同步请求
     *
     * @return
     */
    public Response syncReturnResponse() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
//            System.out.println("Response code: " + response.code());
//            System.out.println("Response URL: " + response.request().url());
//            if (response.isRedirect()) {
//                System.out.println("Redirected to: " + response.header("Location"));
//            }
            // 打印所有 Cookie
//            for (String cookie : response.headers("Set-Cookie")) {
//                System.out.println("Set-Cookie: " + cookie);
//            }
            assert response.body() != null;
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
//    /**
//     * 同步禁止跳转302
//     */
//    public Response syncReturnResponseNoRedirect() {
//        //转为原子类
//        AtomicReference<Response> resresponse = new AtomicReference<>();
//        okHttpClient.followUpRequest
//             okHttpClient.newCall(request.build()).enqueue(new Callback() {
//
//                 @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                     resresponse.set(response);
//                }
//
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                }
//            });
//             //等待返回值
//             while (resresponse.get() == null) {
//                 try {
//                     Thread.sleep(100);
//                 } catch (InterruptedException e) {
//                     e.printStackTrace();
//                 }
//             }
//
//            return resresponse.get();
//
//    }


    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                buffer.append(response.body().string());
                getSemaphoreInstance().release();
            }
        });
        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, response.body().string());
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


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



    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);

    }

    /**
     * 获取重定向地址
     * @param path 原地址
     * @return
     * @throws Exception
     */
    public static String getRedirectUrl(String path) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(path)
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

        Response response = client.newCall(request).execute();
        return  response.request().url().toString();

    }
//    获取所有缓存的Cookie

    public static  HashMap<String, List<Cookie>> getAllCookies() {
       return cookieStore;
    }
    //根据url获取Cookie
    public static List<Cookie> getCookies(String url) {
        return cookieStore.get(url);
    }
    //清空所有缓存
    public static void clearCookies() {
        cookieStore.clear();
    }


    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpUtils.okHttpClient = okHttpClient;
    }
    
    /**
     * 获取应用程序流量统计
     * @deprecated 由于SystemUtils现在只缓存最近数据，建议使用getApplicationSpeed()获取实时速度
     */
    @Deprecated
    public static Map<String, Long> getApplicationTraffic() {
        // 返回空的统计结果，因为不再维护累计数据
        Map<String, Long> result = new HashMap<>();
        result.put("upload", 0L);
        result.put("download", 0L);
        return result;
    }

}
