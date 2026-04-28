package com.sqmusicplus.v3.plug.netease.utils;

import java.util.Map;
import java.util.UUID;

/**
 * @Classname NeteaseAnonCookieUtil
 * @Description TODO
 * @Date 2026/4/27 19:42
 * @Created by SQ
 */
public class NeteaseAnonCookieUtil {

    private static String random32Str() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getAnonCookie() {
        String musicA = random32Str();
        String csrf = random32Str();
        return "MUSIC_A=" + musicA + "; __csrf=" + csrf + "; os=pc; appver=8.10.0";
    }

}
