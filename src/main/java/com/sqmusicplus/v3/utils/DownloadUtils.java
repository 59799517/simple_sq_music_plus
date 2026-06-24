package com.sqmusicplus.v3.utils;

import com.alibaba.fastjson2.JSONObject;
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
import java.util.Map;
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
    public static void download(String url, File  file, Consumer<DownloadProgress> onProcess, Consumer<File> onSuccess,Consumer<Exception> onFailure,Consumer<File> onComplete) {
        download(url, file,null, onProcess, onSuccess, onFailure,onComplete);
    }
    public static void download(String url, String  file, Consumer<DownloadProgress> onProcess, Consumer<File> onSuccess,Consumer<Exception> onFailure,Consumer<File> onComplete) {
        File file1 = new File(file);
        download(url, file1,null, onProcess, onSuccess, onFailure,onComplete);
    }

    public static void download(String url ,File file,Consumer<File> onSuccess,Consumer<Exception> onFailure){
        download(url,file,null,null,onSuccess,onFailure,null);
    }
    public static void download(String url ,String path,Consumer<File> onSuccess,Consumer<Exception> onFailure){
        File file = new File(path);
        download(url,file,null,null,onSuccess,onFailure,null);
    }
    public static void download(String url ,File file,HashMap<String,String> headers,Consumer<File> onSuccess,Consumer<Exception> onFailure){
        download(url,file,headers,null,onSuccess,onFailure,null);
    }
    public static void download(String url ,File file,Consumer<File> onSuccess,Consumer<Exception> onFailure,Consumer<File> onComplete){
        download(url,file,null,null,onSuccess,onFailure,onComplete);
    }
    public static void download(String url , File file, HashMap<String,String> headers, Consumer<File> onSuccess, Consumer<Exception> onFailure, Consumer<File> onComplete){
        download(url,file,headers,null,onSuccess,onFailure,onComplete);
    }


    /**
     * @param url 下载连接
     * @param target 储存地址（无文件信息回自动创建）
     * @param headers url头文件信息
     * @param onProcess 进度回调
     * @param onSuccess 下载成功回调
     * @param onFailure 下载失败回调
     * @param onComplete 下载完成回调
     */
    public static void download( String url,
                          File target,
                          HashMap<String, String> headers,
                          Consumer<DownloadProgress> onProcess,
                          Consumer<File> onSuccess,
                          Consumer<Exception> onFailure,
                          Consumer<File> onComplete) {
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
    if (okHttpClient== null){
        okHttpClient = getOkHttpClient();
    }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                try {
                    if (onFailure != null) {
                        onFailure.accept(e);
                    }
                } catch (Exception callbackEx) {
                    log.error("onFailure回调执行失败: {}", callbackEx.getMessage(), callbackEx);
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                if (body == null) {
                    throw new IOException("Empty response body");
                }

                // ✅ 检查HTTP响应状态码，非2xx直接走onFailure
                if (!response.isSuccessful()) {
                    String errorMsg = "HTTP响应异常: " + response.code() + " " + response.message();
                    log.error("下载失败: {} - {}", url, errorMsg);
                    try {
                        if (onFailure != null) {
                            onFailure.accept(new IOException(errorMsg));
                        }
                    } catch (Exception callbackEx) {
                        log.error("onFailure回调执行失败: {}", callbackEx.getMessage(), callbackEx);
                    }
                    return;
                }

                // ✅ 如果是文件夹，则生成目标文件（提前处理路径）
                File file;
                if (target.isDirectory()) {
                    String fileName = getHeaderFileName(response); // 从 URL 提取文件名
                    target.mkdirs();
                    file = new File(target, fileName);
                } else {
                    file = target;
                }
                
                // ✅ 确保父目录存在，处理特殊字符路径
                try {
                    file.getParentFile().mkdirs();
                } catch (Exception e) {
                    // 如果创建目录失败，尝试过滤路径中的特殊字符
                    String originalPath = file.getAbsolutePath();
                    String filteredPath = filterPathCharacters(originalPath);
                    
                    if (!originalPath.equals(filteredPath)) {
                        log.warn("原路径包含特殊字符，已过滤：{} -> {}", originalPath, filteredPath);
                        file = new File(filteredPath);
                        file.getParentFile().mkdirs();
                    } else {
                        try {
                            if (onFailure != null) {
                                onFailure.accept(e);
                            }
                        } catch (Exception callbackEx) {
                            log.error("onFailure回调执行失败: {}", callbackEx.getMessage(), callbackEx);
                        }
                        return;
                    }
                }

                // ✅ 使用 try-with-resources 自动管理资源
                long total = response.body().contentLength();
                boolean downloadSuccess = false;
                
                try (InputStream is = response.body().byteStream();
                     FileOutputStream fos = new FileOutputStream(file)) {
                    
                    byte[] buf = new byte[8192]; // 优化：从 2KB 提升到 8KB，提高 I/O 效率
                    int len = 0;
                    long sum = 0;
                    
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        onProcess.accept(new DownloadProgress(sum, total, progress));
                    }
                    fos.flush();
                    // 注意：不调用 fos.getFD().sync()，该操作在 Windows 上会导致磁盘 I/O 异常
                    // 且对于音乐下载场景，flush 已足够保证数据完整写入
                    downloadSuccess = true;
                } catch (Exception e) {
                    // ✅ onFailure 回调不应抛出异常，否则会继续传播到 OkHttp
                    try {
                        if (onFailure != null) {
                            onFailure.accept(e);
                        }
                    } catch (Exception callbackEx) {
                        log.error("onFailure回调执行失败: {}", callbackEx.getMessage(), callbackEx);
                    }
                    // 下载失败，不执行 onSuccess 和 onComplete
                    // onComplete 可能执行文件操作（如标签写入），不应在失败文件上执行
                    return;
                }
                
                // ✅ 文件写入成功，但检查文件是否为空（0字节）
                if (file.length() == 0) {
                    log.error("下载文件为空: {}", file.getAbsolutePath());
                    try {
                        if (onFailure != null) {
                            onFailure.accept(new IOException("下载文件为空: " + file.getAbsolutePath()));
                        }
                    } catch (Exception callbackEx) {
                        log.error("onFailure回调执行失败: {}", callbackEx.getMessage(), callbackEx);
                    }
                    return;
                }
                
                // ✅ 文件写入成功，执行后续回调
                try {
                    if (onSuccess != null) {
                        onSuccess.accept(file);
                    }
                } catch (Exception e) {
                    log.error("onSuccess回调执行失败: {}", e.getMessage(), e);
                }
                
                // ✅ onComplete 仅在下载成功时执行，确保文件完整可用
                try {
                    if (onComplete != null) {
                        onComplete.accept(file);
                    }
                } catch (Exception e) {
                    log.error("onComplete回调执行失败: {}", e.getMessage(), e);
                }
            }
        });
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
            
            // ✅ 使用缓冲区流式写入，避免大文件占用大量内存
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192]; // 8KB 缓冲区
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();
                // 注意：不调用 getFD().sync()，避免不必要的磁盘 I/O 压力
            }
            
            result = true;
        }catch (IOException e)
        {
            e.printStackTrace();
            result = false;
        } finally {
            // 确保 InputStream 被关闭
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static  OkHttpClient getOkHttpClient() {
       return getOkHttpClient(true);
    }
    
    /**
     * 重置 OkHttpClient（用于重新创建带拦截器的客户端）
     */
    public static void resetOkHttpClient() {
        okHttpClient = null;
    }
    
    public static  OkHttpClient getOkHttpClient(boolean followRedirects ) {
        // 每次都创建新的带拦截器的客户端（确保流量监控生效）
        // 注意：这会略微增加资源消耗，但能保证流量统计准确
        SSLContext sslCtx = null;
        try {
            sslCtx = SSLContext.getInstance("TLS");
            sslCtx.init(null, new TrustManager[] { myTrustManager }, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        SSLSocketFactory mySSLSocketFactory = sslCtx.getSocketFactory();
        
        // 新增：添加流量监控拦截器
        Interceptor trafficInterceptor = chain -> {
            Request request = chain.request();
            
            // 记录请求体大小（上传流量）
            long requestBodySize = 0;
            if (request.body() != null) {
                requestBodySize = request.body().contentLength();
                if (requestBodySize > 0) {
//                    System.out.println("[DEBUG] 拦截器 - 上传：" + requestBodySize + " bytes");
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
        };
        
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
                .addInterceptor(trafficInterceptor) // 添加流量监控拦截器
                .build();

        return okHttpClient;
    }
    public  static <T> T get(String url,HashMap<String,String> params,Map<String,String> headers,Class<T> clazz){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);

        if (params!=null){
            builder.addParam(params);
        }
        if (headers!=null){
            for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
                builder.addHeader(stringStringEntry.getKey(),stringStringEntry.getValue());
            }
        }
        String sync =builder
                .get().sync();
        return (T)JSONObject.parseObject(sync,clazz);

    }
    public  static <T> T get(String url,HashMap<String,String> params,Class<T> clazz){
        return get(url,params,null,clazz);


    }
    public static JSONObject getToJsonObject(String url, HashMap<String,String> params){
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
    public static <T> T get(String url ,Class<T> clazz,Map<String,String> headers){
        return get(url,null,headers,clazz);
    }

    public static JSONObject getToJsonObject(String url){
        return getToJsonObject(url,null);
    }


    public static JSONObject postToJsonObject(String url,String body,Map<String,String>  headers){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);
        String sync =builder
                .post(true,body)
                .addHeader(headers)
                .sync();
                return JSONObject.parseObject(sync);

    }
    public static JSONObject postToJsonObject(String url,JSONObject body,Map<String,String>  headers){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);
        String sync =builder
                .post(true,body)
                .addHeader(headers)
                .sync();
        return JSONObject.parseObject(sync);
    }
    public static JSONObject postCookieToJsonObject(String url,JSONObject body,String  cookie){
        if (StringUtils.isBlank(cookie)){
            return  postToJsonObject(url, body, null);
        }
        OkHttpUtils builder = OkHttpUtils.builder().url(url).addCookie(cookie);
        String sync =builder
                .post(true,body)
                .sync();
        return JSONObject.parseObject(sync);
    }
    public static JSONObject postCookieToJsonObject(String url,JSONObject body,String  cookie,Map<String,String>  headers){
        if (StringUtils.isBlank(cookie)){
            return  postToJsonObject(url, body,headers);
        }
        OkHttpUtils builder = OkHttpUtils.builder().url(url).addCookie(cookie);
        String sync =builder
                .post(true,body)
                .addHeader( headers)
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
            // 处理 filename*=utf-8''encoded_filename 格式
            if (dispositionHeader.contains("filename*=")) {
                String[] parts = dispositionHeader.split("filename\\*=");
                if (parts.length > 1) {
                    String encodedFilename = parts[1];
                    // 移除可能的引号
                    encodedFilename = encodedFilename.replace("\"", "");
                    // 处理 UTF-8'' 格式
                    if (encodedFilename.contains("UTF-8''")) {
                        String[] utf8Parts = encodedFilename.split("UTF-8''");
                        if (utf8Parts.length > 1) {
                            try {
                                return java.net.URLDecoder.decode(utf8Parts[1], "UTF-8");
                            } catch (Exception e) {
                                return utf8Parts[1];
                            }
                        }
                    }
                    return encodedFilename;
                }
            }
            // 处理普通的 filename= 格式
            else if (dispositionHeader.contains("filename=")) {
                String[] parts = dispositionHeader.split("filename=");
                if (parts.length > 1) {
                    String filename = parts[1];
                    // 移除可能的引号
                    filename = filename.replace("\"", "");
                    return filename;
                }
            }
            // 原有逻辑作为备选方案
            dispositionHeader = dispositionHeader.replace("attachment;filename=", "");
            dispositionHeader = dispositionHeader.replace("filename*=utf-8", "");
            String[] strings = dispositionHeader.split("; ");
            if (strings.length > 1) {
                dispositionHeader = strings[1].replace("filename=", "");
                dispositionHeader = dispositionHeader.replace("\"", "");
                return dispositionHeader;
            }
            return "";
        }
        
        // 如果没有Content-Disposition头，则根据Content-Type生成随机文件名
        String contentType = response.header("Content-Type");
        return generateRandomFileName(contentType);
    }

    /**
     * 根据Content-Type生成随机文件名
     * @param contentType Content-Type头
     * @return 随机文件名
     */
    private static String generateRandomFileName(String contentType) {
        String extension = getFileExtensionFromContentType(contentType);
        String fileName = "file_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
        if (!extension.isEmpty()) {
            fileName += "." + extension;
        }
        return fileName;
    }

    /**
     * 根据Content-Type获取文件扩展名
     * @param contentType Content-Type头
     * @return 文件扩展名
     */
    private static String getFileExtensionFromContentType(String contentType) {
        if (TextUtils.isEmpty(contentType)) {
            return "";
        }
        
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            case "image/webp":
                return "webp";
            case "audio/mpeg":
                return "mp3";
            case "audio/flac":
                return "flac";
            case "audio/wav":
                return "wav";
            case "audio/mp4":
                return "m4a";
            case "video/mp4":
                return "mp4";
            case "text/plain":
                return "txt";
            case "text/html":
                return "html";
            case "application/json":
                return "json";
            case "application/pdf":
                return "pdf";
            case "application/zip":
                return "zip";
            default:
                // 尝试从content-type中提取扩展名
                if (contentType.contains("/")) {
                    String[] parts = contentType.split("/");
                    return parts[1].split(";")[0]; // 处理如 "text/html; charset=utf-8" 的情况
                }
                return "";
        }
    }

    /**
     * 获取body的字符串不转化
     * @param url
     * @param params
     * @return
     */
    public  static String getBodyStr(String url,HashMap<String,String> params){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);

        if (params!=null){
            builder.addParam(params);
        }
        return  builder
                .get().sync();
    }

    /**
     * 获取body的字符串不转化
     * @param url
     * @return
     */
    public static String getBodyStr(String url,HashMap<String,String> params,Map<String,String> haders){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);

        if (params!=null){
            builder.addParam(params);
        }
        if (haders!=null){
            for (Map.Entry<String, String> stringStringEntry : haders.entrySet()) {
                builder.addHeader(stringStringEntry.getKey(),stringStringEntry.getValue());
            }

        }
        return  builder
                .get().sync();
    }
    public  static String getBodyStr(String url){
        OkHttpUtils builder = OkHttpUtils.builder().url(url);
        return getBodyStr(url,null);
    }

    /**
     * 过滤错误信息，只保留括号、&、.、中文、英文和数字
     * @param message 原始错误信息
     * @return 过滤后的错误信息
     */
    private static String filterErrorMessage(String message) {
        if (message == null) {
            return "";
        }
        StringBuilder filtered = new StringBuilder();
        for (char c : message.toCharArray()) {
            // 保留括号、&、.
            if (c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}' || c == '&' || c == '.') {
                filtered.append(c);
            }
            // 保留中文
            else if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
                filtered.append(c);
            }
            // 保留英文和数字
            else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                filtered.append(c);
            }
            // 其他字符全部过滤掉
        }
        return filtered.toString();
    }

    /**
     * 过滤文件路径中的特殊字符，只保留括号、点号、下划线、连字符、&、.、中文、英文和数字
     * 同时保留路径分隔符
     * @param path 原始路径
     * @return 过滤后的路径
     */
    private static String filterPathCharacters(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        StringBuilder filtered = new StringBuilder();
        for (char c : path.toCharArray()) {
            // 保留路径分隔符（Windows 和 Unix）
            if (c == File.separatorChar || c == '/' || c == '\\') {
                filtered.append(c);
            }
            // 保留括号、点号、下划线、连字符、&
            else if (c == '(' || c == ')' || c == '.' || c == '_' || c == '-' || c == '&') {
                filtered.append(c);
            }
            // 保留中文
            else if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
                filtered.append(c);
            }
            // 保留英文和数字
            else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                filtered.append(c);
            }
            // 其他字符全部过滤掉
        }
        return filtered.toString();
    }

}