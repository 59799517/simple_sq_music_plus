package com.sqmusicplus.plug.qq.util;

import java.util.Random;

/**
 * @Classname GuidGenerator
 * @Description guid生成
 * @Version 1.0.0
 * @Date 2025/4/30 10:01
 * @Created by SQ
 */

public class GuidGenerator {

    private static final char[] ALLOWED_CHARS = "abcdef1234567890".toCharArray();
    private static final Random RANDOM = new Random();

    public static String getGuid() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            int index = RANDOM.nextInt(ALLOWED_CHARS.length);
            sb.append(ALLOWED_CHARS[index]);
        }
        return sb.toString();
    }
}
