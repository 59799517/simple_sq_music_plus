package com.sqmusicplus;

import cn.hutool.crypto.digest.DigestUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Classname KGCrypto
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/2/5 13:40
 * @Created by SQ
 */

public class KGCrypto {


    /**
     * AES 加密
     * @param data 需要加密的数据
     * @param opt 包含key和iv的选项
     * @return 加密后的字符串或包含加密字符串和临时密钥的Map
     * @throws Exception 加密过程中可能抛出的异常
     */
    public static Map<String, String> cryptoAesEncrypt(String data, Map<String, String> opt) throws Exception {
        byte[] buffer;
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        buffer = data.getBytes(StandardCharsets.UTF_8);

        String key;
        byte[] iv;
        String tempKey = "";

        if (opt != null && opt.containsKey("key") && opt.containsKey("iv")) {
            key = opt.get("key");
            iv = opt.get("iv").getBytes(StandardCharsets.UTF_8);
        } else {
            tempKey = opt != null && opt.containsKey("key") ? opt.get("key") : generateRandomString(16).toLowerCase();
            key = DigestUtil.md5Hex(tempKey).substring(0, 32);
            iv = key.substring(key.length() - 16).getBytes(StandardCharsets.UTF_8);
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encrypted = cipher.doFinal(buffer);
        String encryptedHex = bytesToHex(encrypted);
        Map<String, String> result = new HashMap<>();

        if (opt != null && opt.containsKey("key")) {
            result.put("str", encryptedHex);
        } else {
            result.put("str", encryptedHex);
            result.put("key", tempKey);
        }
        return result;

    }
    /**
     * 字符串转十六进制字符串
     * @param bytes 字符串
     * @return 字符串转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 随机生成字符串
     * @param length
     * @return 生产的字符串
     */
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
