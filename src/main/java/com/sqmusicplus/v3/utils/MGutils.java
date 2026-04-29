package com.sqmusicplus.v3.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MGutils
 * @Description 咪咕加墨参数
 * @Date 2026/4/23 11:01
 * @Created by SQ
 */
public class MGutils {
    public static final String signatureMd5 = "6cdc72a439cef99a3418d2a78aa28c73";
    public static final String fixedStr = "yyapp2d16148780a1dcc7408e06336b98cfd50";
    public static final String uiVersion = "A_music_3.12.1";


    public static Map<String, String> getSign(String keyword){
        String deviceId = IdUtil.simpleUUID();
//        String deviceId = "963B7AA0D21511ED807EE5846EC87D20";

        long timestamp = System.currentTimeMillis();
        String text = keyword + signatureMd5 + fixedStr + deviceId + timestamp;
//        String text ="jay6cdc72a439cef99a3418d2a78aa28c73yyapp2d16148780a1dcc7408e06336b98cfd50963B7AA0D21511ED807EE5846EC87D201777010272314";
//        3b8b18ab6219c00780a17f5d5ad3fa38

        String sign = DigestUtil.md5Hex(text);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("uiVersion", uiVersion);
        stringStringHashMap.put("deviceId", deviceId);
        stringStringHashMap.put("timestamp", timestamp+"");
        stringStringHashMap.put("sign", sign);
        stringStringHashMap.put("channel","0146921");
        return stringStringHashMap;

    }
}
