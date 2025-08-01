package com.sqmusicplus.v3.utils;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.v3.download.vo.DownloadProgress;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.hc.core5.util.TextUtils;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Classname DownloadUtils
 * @Description 下载工具类
 * @Version 1.0.0
 * @Date 2022/5/31 15:14
 * @Created by SQ
 */
@Slf4j
public class DownloadUtils {

    private static OkHttpClient okHttpClient;


    //下载其他的
   public static void download(String url, String path, String fileName, Consumer<DownloadProgress> onProcess, Consumer<File> onSuccess) {

       String s = "";
       if(fileName != null){
            s = path + fileName;
       }else{
           s = path;
       }
       File file = new File(s);
       download(url, file,null, onProcess, onSuccess, null,null);
    }

    public static void download(String url ,File file,Consumer<File> onSuccess,Consumer<Exception> onFailure){
        download(url,file,null,null,onSuccess,onFailure,null);
    }
    public static void download(String url ,File file,HashMap<String,String> headers,Consumer<File> onSuccess,Consumer<Exception> onFailure){
        download(url,file,headers,null,onSuccess,onFailure,null);
    }
    public static void download(String url ,File file,Consumer<File> onSuccess,Consumer<Exception> onFailure,Consumer<String> onComplete){
        download(url,file,null,null,onSuccess,onFailure,onComplete);
    }
    public static void download(String url , File file, HashMap<String,String> headers, Consumer<File> onSuccess, Consumer<Exception> onFailure, Consumer<String> onComplete){
        download(url,file,headers,null,onSuccess,onFailure,onComplete);
    }

    /**
     * 下载文件
     *
     * @param url       下载地址
     * @param target      保存的文件 或者文件夹
     * @param headers   请求头（可为 null）
     * @param onProcess 下载进度回调
     * @param onSuccess 下载成功回调
     * @param onFailure 下载失败回调
     * @param onComplete 下载完成回调（返回执行信息，如 "completed"）
     */
    public static void download(
            String url,
            File target,
            HashMap<String, String> headers,
            Consumer<DownloadProgress> onProcess,
            Consumer<File> onSuccess,
            Consumer<Exception> onFailure,
            Consumer<String> onComplete) {

        if(okHttpClient==null){
            okHttpClient = getOkHttpClient();
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        if (headers != null) {
            headers.put("Accept", "application/xml;version=1");
            headers.forEach(builder::addHeader);
        } else {
            builder.addHeader("Accept", "application/xml;version=1");
        }

        Request request = builder.build();

        new Thread(() -> {
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                ResponseBody body = response.body();
                if (body == null) {
                    throw new IOException("Empty response body");
                }

                // 如果是文件夹，则生成目标文件
                File file;
                if (target.isDirectory()) {
                    String fileName = getHeaderFileName(response); // 从 URL 提取文件名
                    file = new File(target, fileName);
                } else {
                    file = target;
                }


                long totalBytesRead = 0;
                long contentLength = body.contentLength();

                try (InputStream is = body.byteStream();
                     OutputStream os = new FileOutputStream(file)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        if (onProcess != null) {
                            DownloadProgress progress = new DownloadProgress(totalBytesRead, contentLength);
                            onProcess.accept(progress);
                        }
                    }

                    os.flush();

                    if (onSuccess != null) {
                        onSuccess.accept(file);
                    }

                } catch (IOException e) {
                    if (onFailure != null) {
                        onFailure.accept(e);
                    }
                }

            } catch (Exception e) {
                if (onFailure != null) {
                    onFailure.accept(e);
                }
            } finally {
                if (onComplete != null) {
                    onComplete.accept("Download completed");
                }
            }
        }).start();
    }

    public static void download(String url, String path, Consumer<File> onSuccess) {
        download(url,path,null,null,onSuccess);
    }


    private static X509TrustManager myTrustManager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    private static HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };




    /**
     * @param url          下载连接
     * @param file  下载的文件
     */

    public static boolean download(final String url, final File file)  {

    if(okHttpClient==null){
        okHttpClient = getOkHttpClient();
    }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get()
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
        }
        InputStream inputStream = response.body().byteStream();
        return WriteFile4InputStream(file, inputStream);

    }



    public static boolean WriteFile4InputStream(File file, InputStream inputStream)
    {
        //默认为flase 即失败
        boolean result = false;
        try {
            if (!file.exists()){
                file.getParentFile().mkdirs();
            }
            OutputStream os = new FileOutputStream(file);
            os.write(inputStream.readAllBytes());
            os.close();
            result = true;
        }catch (IOException e)
        {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static  OkHttpClient getOkHttpClient() {
       return getOkHttpClient(true);
    }
    public static  OkHttpClient getOkHttpClient(boolean followRedirects ) {
        if(okHttpClient==null){
            SSLContext sslCtx = null;
            try {
                sslCtx = SSLContext.getInstance("TLS");
                sslCtx.init(null, new TrustManager[] { myTrustManager }, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (KeyManagementException e) {
                throw new RuntimeException(e);
            }
//            builder.sslSocketFactory(mySSLSocketFactory, myTrustManager);
//            builder.hostnameVerifier(myHostnameVerifier);
            SSLSocketFactory mySSLSocketFactory = sslCtx.getSocketFactory();
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .hostnameVerifier((hostName, session) -> true)
                    .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                    .retryOnConnectionFailure(true)
                    .followRedirects(followRedirects)
                    .followSslRedirects(followRedirects)
                    .sslSocketFactory(mySSLSocketFactory, myTrustManager)
                    .hostnameVerifier(myHostnameVerifier)
                    .build();

        }
        return okHttpClient;
    }
    public  static <T> T get(String url,HashMap<String,String> params,Class<T> clazz){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);

        if (params!=null){
            builder.addParam(params);
        }
        String sync =builder
                .get().sync();
        return (T)JSONObject.parseObject(sync,clazz);

    }
    public static JSONObject getToJsonObject(String url,HashMap<String,String> params){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);

        if (params!=null){
            builder.addParam(params);
        }
        String sync =builder
                .get().sync();
        return JSONObject.parseObject(sync);

    }
    public static <T> T get(String url ,Class<T> clazz){
        return get(url,null,clazz);
    }

    public static JSONObject getToJsonObject(String url){
        return getToJsonObject(url,null);
    }


    public static JSONObject postToJsonObject(String url,String body){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);
        String sync =builder
                .post(true,body)
                .sync();
                return JSONObject.parseObject(sync);

    }


    /**
     * 解析文件头
     * Content-Disposition:attachment;filename=FileName.txt
     * Content-Disposition: attachment; filename*="UTF-8''%E6%9B%BF%E6%8D%A2%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A.pdf"
     */
    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header("Content-Disposition");
        if (!TextUtils.isEmpty(dispositionHeader)) {
            dispositionHeader.replace("attachment;filename=", "");
            dispositionHeader.replace("filename*=utf-8", "");
            String[] strings = dispositionHeader.split("; ");
            if (strings.length > 1) {
                dispositionHeader = strings[1].replace("filename=", "");
                dispositionHeader = dispositionHeader.replace("\"", "");
                return dispositionHeader;
            }
            return "";
        }
        return "";
    }

}
