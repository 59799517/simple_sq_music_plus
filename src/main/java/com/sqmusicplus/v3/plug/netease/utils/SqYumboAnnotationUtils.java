//package com.sqmusicplus.v3.plug.netease.utils;
//
//import com.alibaba.fastjson2.JSONObject;
//import com.sqmusicplus.v3.utils.OkHttpUtils;
//import okhttp3.Response;
//import top.yumbo.util.music.musicAbstract.AbstractMusic;
//
//
//import java.lang.reflect.Method;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @Classname SqYumboAnnotationUtils
// * @Description 使用 OkHttpUtils 替代 RestTemplate 发送 HTTP 请求
// * @Version 1.0.0
// * @Date 2025/9/17 17:10
// * @Created by SQ
// */
//
//public class SqYumboAnnotationUtils {
//
//
//
//
//    /**
//     * 发送请求并且注入json对象
//     *
//     * @param obj 内部是包含了注解的服务
//     */
//    public static JSONObject sendRequestAutowiredJson(Object obj) {
//        if (obj == null) {
//            throw new NullPointerException("封装对象不能为null");
//        }
//        /**
//         * 反射操作,获取url,注入json对象
//         */
//        final Class<?> clazz = obj.getClass(); // 获取到反射的类
//
//        /**
//         * 因为网易云音乐和QQ音乐提供的接口地址不一样这里采用if-else将其分开
//         */
//        if (obj instanceof AbstractMusic) {
//            AbstractMusic abstractMusic = (AbstractMusic) obj; // 强转
//            final String currentRunningMethod = abstractMusic.getCurrentRunningMethod();// 获取当前调用的是那个方法
//            Method method = null;
//
//            final Method[] methods = clazz.getMethods();
//            for (Method m : methods) {
//                if (m.getName().equals(currentRunningMethod)) {
//                    method = m;// 找到对应的方法
//                }
//            }
//            if (method == null) {
//                try {
//                    throw new NoSuchMethodException();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
//            }
//            final MusicService annotation = method.getAnnotation(MusicService.class);// 获取方法上的注解信息
//            if (annotation != null) { // 注解不为null则继续获取注解上的url信息
//                final String url = annotation.url(); // 得到注解上的url
//                final MusicEnum musicEnum = abstractMusic.getMusicEnum();
//
//                String fullPathURL;
//                if (musicEnum == MusicEnum.OtherMusic) {
//                    final String serverAddress = annotation.serverAddress();
//                    fullPathURL = MusicEnum.OtherMusic.getFullPathURL(serverAddress, url);
//                } else {
//                    fullPathURL = musicEnum.getFullPathURL(null, url);// 获取完整的路径
//                }
//
//                if (abstractMusic.getParameter() == null) {// 处理没有参数的方法
//                    abstractMusic.setParameter(new JSONObject());
//                }
//                // 设置请求头
//                String cookie = abstractMusic.getCookieString();
//                if (cookie == null) {
//                    cookie = "";
//                }
////                System.out.println("当前执行:" + clazz.toString() + "." + method.getName() + "()\n请求的相对路径:" + url);
//                // 发送请求得到返回来的数据
//                try {
////                    System.out.println("绝对路径:" + fullPathURL);
//                    final long time = Timestamp.valueOf(LocalDateTime.now()).getTime();
//                    fullPathURL += "?timestamp=" + time; // 带上时间戳
//
//                    // 使用 OkHttpUtils 替代 RestTemplate
//                    OkHttpUtils okHttpUtils = OkHttpUtils.builder()
//                            .url(fullPathURL)
//                            .addHeader("Content-Type", "application/json")
//                            .addHeader("Cookie", cookie)
//                            .post(true, abstractMusic.getParameter().toJSONString());
//
//                    Response response = okHttpUtils.syncReturnResponse();
//                    if (response != null && response.body() != null) {
//                        String responseBody = response.body().string();
//                        abstractMusic.setResult(JSONObject.parseObject(responseBody)); // 直接替换
//
//                        // 处理 set-cookie 头
//                        List<String> setCookieHeaders = response.headers("set-cookie");
//                        if (setCookieHeaders != null && !setCookieHeaders.isEmpty()) {
//                            final String cookieString = parseSetCookie(setCookieHeaders.toString());
//                            abstractMusic.setCookieString(cookieString); // 将 cookie 保存
//                            abstractMusic.setParameter(null); // 清除参数
//                        }
//                        return JSONObject.parseObject(responseBody);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * 发送请求 返回Json对象
//     *
//     * @param url 资源路径
//     */
//    private static JSONObject getJsonObjectFromApi(String url) {
//        // 使用 OkHttpUtils 替代 RestTemplate
//        final String jsonString = OkHttpUtils.builder().url(url).get().sync();
//        final JSONObject jsonObject = JSONObject.parseObject(jsonString); // 转jsonObject
//        return jsonObject;
//    }
//
//    /**
//     * 替换source的内容为target
//     */
//    private static void replaceJsonObject(JSONObject source, JSON target) {
//        if (source == null) {
//            source = new JSONObject();
//        }
//        source.clear();// 清空原先的内容
//        final Map map = JSON.parseObject(target.toString(), Map.class);// 解析成map对象
//        source.putAll(map);// 添加到原先的对象中
//    }
//
//    /**
//     * 解析set-Cookie,去除过期时间等无关信息
//     */
//    private static String parseSetCookie(String setCookie) {
//        if (setCookie == null) {
//            return "";
//        }
////        System.out.println("解析前cookie是" + setCookie);
//        final String[] split = setCookie.split(";, ");
//        final Optional<String> cookieStringOptional = Arrays.stream(split).map(x -> {
//            return x.split(";")[0] + "; ";
//        }).reduce((x, y) -> x + y);
//        String cookieString = cookieStringOptional.get() + "]";
////        System.out.println("解析后cookie是:" + cookieString);
//        return cookieString;
//    }
//
//
//}
