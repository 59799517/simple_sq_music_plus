package com.sqmusicplus.v3.config;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Classname GlobalStatic
 * @Description 全局静态变量
 * @Version 1.0.0
 * @Date 2022/10/25 11:18
 * @Created by SQ
 */

public class GlobalStatic {


    /**
     * 最大同步次数 超过就算失败放弃同步
     */
    public static Integer SUBSONIC_SYNC_MAXIMUM_STATISTICS = 3;

//    /**
//     * QQ登录cookie缓存key
//     */
//    public static String QQ_LOGIN_COOKIE_KEY = "plug.qqlogin.cookie";
//    /**
//     * QQ登录QR码
//     */
//    public static String QQ_LOGIN_QR_KEY = "plug.qqlogin.qrcode";



}
