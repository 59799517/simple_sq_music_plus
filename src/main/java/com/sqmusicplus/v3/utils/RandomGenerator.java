package com.sqmusicplus.v3.utils;

import java.security.SecureRandom;

/**
 * @Classname RandomGenerator
 * @Description 随机数生成
 * @Version 1.0.0
 * @Date 2025/4/27 14:34
 * @Created by SQ
 */

public class RandomGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String CHAR_POOL = "adbcdef1234567890";
    private static final char[] CHARS = CHAR_POOL.toCharArray();



    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARS.length);
            sb.append(CHARS[index]);
        }
        return sb.toString();
    }
}
