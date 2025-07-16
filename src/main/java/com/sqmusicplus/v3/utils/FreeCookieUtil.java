package com.sqmusicplus.v3.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @Classname FreeCookieUtil
 * @Description FreeMp3 cookie 工具
 * @Version 1.0.0
 * @Date 2024/7/31 10:13
 * @Created by SQ
 */
@Slf4j
public class FreeCookieUtil {

private static   ArrayList<String> Cookies = new ArrayList<String>();
    public static ArrayList<String> getCookie() {
        return Cookies;
    }

    /**
     *
     * @return 获取cookie字符串
     */
    public static String getCookieStr() {
        StringBuilder stringBuilder = new StringBuilder();
        Cookies.forEach(e->{
            stringBuilder.append(e);
            stringBuilder.append(";");
        });
        return stringBuilder.toString();
    }

    /**
     *
     * @param qq qq号
     * @param baseUrl urlip
     * @return cookie信息
     */
    public static ArrayList<String> refreshCookies(String qq, String baseUrl){
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(baseUrl + "/user/getCookie?id=" + qq).get();
        Mapper mapper = httpResult.getBody().toMapper();
        mapper.forEach((s, dataSet) -> {
            log.info("qqapi返回值：{}->{}", s,dataSet.toString());
        });
        ArrayList<String> cookies =  new ArrayList<String>();

        ListMap<String> stringListMap = httpResult.allHeaders();
        stringListMap.forEach((s, s2) -> {
            if (s.equalsIgnoreCase("Set-Cookie")) {
                cookies.add(s2);
            }
        });
        Cookies = cookies;
        return cookies;
    }


}
