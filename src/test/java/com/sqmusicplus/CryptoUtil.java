package com.sqmusicplus;

import com.alibaba.fastjson2.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CryptoUtil {

    private static final String PUBLIC_RAS_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIAG7QOELSYoIJvTFJhMpe1s/gbjDJX51HBNnEl5HXqTW6lQ7LC8jr9fWZTwusknp+sVGzwd40MwP6U5yDE27M/X1+UR4tvOGOqp94TJtQ1EPnWGWXngpeIW5GxoQGao1rmYWAu6oi1z9XkChrsUdC6DJE5E221wf/4WLFxwAtRQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    private static final String PUBLIC_LITE_RAS_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDECi0Np2UR87scwrvTr72L6oO01rBbbBPriSDFPxr3Z5syug0O24QyQO8bg27+0+4kBzTBTBOZ/WWU0WryL1JSXRTXLgFVxtzIY41Pe7lPOgsfTCn5kZcvKhYKJesKnnJDNr5/abvTGf+rHG3YRwsCHcQ08/q6ifSioBszvb3QiwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    /**
     * MD5 加密
     * @param data 需要加密的数据
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException 如果算法不存在
     */
    public static String cryptoMd5(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(messageDigest);
    }

    /**
     * Sha1 加密
     * @param data 需要加密的数据
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException 如果算法不存在
     */
    public static String cryptoSha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(messageDigest);
    }

    /**
     * AES 加密
     * @param data 需要加密的数据
     * @param opt 包含key和iv的选项
     * @return 加密后的字符串或包含加密字符串和临时密钥的Map
     * @throws Exception 加密过程中可能抛出的异常
     */
    public static Map<String,String> cryptoAesEncrypt(String data, Map<String, String> opt) throws Exception {
        byte[] buffer = data.getBytes(StandardCharsets.UTF_8);

        String key;
        byte[] iv;
        String tempKey = "";

        if (opt != null && opt.containsKey("key") && opt.containsKey("iv")) {
            key = opt.get("key");
            iv = opt.get("iv").getBytes(StandardCharsets.UTF_8);
        } else {
            tempKey = opt != null && opt.containsKey("key") ? opt.get("key") : generateRandomString(16).toLowerCase();
            key = cryptoMd5(tempKey).substring(0, 32);
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
            result.put("key", opt.get("key"));
        } else {
            result.put("str", encryptedHex);
            result.put("key", tempKey);
        }
        return result;

    }

    /**
     * AES 解密
     * @param data 加密后的字符串
     * @param key 密钥
     * @param iv 初始化向量
     * @return 解密后的字符串或对象
     * @throws Exception 解密过程中可能抛出的异常
     */
    public static String cryptoAesDecrypt(String data, String key, String iv) throws Exception {
        if (iv == null) {
            key = cryptoMd5(key).substring(0, 32);
        }
        iv = iv != null ? iv : key.substring(key.length() - 16, key.length());

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decrypted = cipher.doFinal(hexToBytes(data));
        String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);
        return decryptedStr;

//        try {
//            return new JSONObject(decryptedStr);
//        } catch (Exception e) {
//            return decryptedStr;
//        }
    }

    /**
     * RSA加密
     * @param data 需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的字符串
     * @throws Exception 加密过程中可能抛出的异常
     */
    public static String cryptoRSAEncrypt(Map<String, String> data, String publicKey) throws Exception {
        boolean isLite = "lite".equals(System.getenv("platform"));
        if (publicKey == null) {
            publicKey = isLite ? PUBLIC_LITE_RAS_KEY : PUBLIC_RAS_KEY;
        }
        String jsonString = JSONObject.toJSONString(data);

        byte[] buffer = jsonString.getBytes(StandardCharsets.UTF_8);
        byte[] paddedBuffer = new byte[128];
        System.arraycopy(buffer, 0, paddedBuffer, 0, buffer.length);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKey.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "")));
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] encrypted = cipher.doFinal(paddedBuffer);
        return bytesToHex(encrypted);
    }

    /**
     * RSA加密（使用PKCS1填充）
     * @param data 需要加密的数据
     * @return 加密后的字符串
     * @throws Exception 加密过程中可能抛出的异常
     */
    public static String rsaEncrypt2(String data) throws Exception {
        boolean isLite = "lite".equals(System.getenv("platform"));
        String publicKey = isLite ? PUBLIC_LITE_RAS_KEY : PUBLIC_RAS_KEY;

        byte[] buffer = data.getBytes(StandardCharsets.UTF_8);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKey.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "")));
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] encrypted = cipher.doFinal(buffer);
        return bytesToHex(encrypted);
    }

    /**
     * Playlist AES加密
     * @param data 需要加密的数据
     * @return 加密后的字符串和密钥
     * @throws Exception 加密过程中可能抛出的异常
     */
    public static Map<String, String> playlistAesEncrypt(String data) throws Exception {
        String key = generateRandomString(6);
        String encryptKey = cryptoMd5(key).substring(0, 16);
        String iv = cryptoMd5(key).substring(16, 32);

        SecretKeySpec secretKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("str", encryptedBase64);
        return result;
    }

    /**
     * Playlist AES解密
     * @param data 加密后的数据
     * @return 解密后的字符串或对象
     * @throws Exception 解密过程中可能抛出的异常
     */
    public static Object playlistAesDecrypt(Map<String, String> data) throws Exception {
        String key = data.get("key");
        String encryptKey = cryptoMd5(key).substring(0, 16);
        String iv = cryptoMd5(key).substring(16, 32);

        SecretKeySpec secretKey = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data.get("str")));
        String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);
        return decryptedStr;
//        try {
//            return new JSONObject(decryptedStr);
//        } catch (Exception e) {
//            return decryptedStr;
//        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

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

//    public static void main(String[] args) {
//        try {
//            // 测试cryptoMd5
//            String md5Result = cryptoMd5("Hello, World!");
//            System.out.println("MD5: " + md5Result);
//
//            // 测试cryptoSha1
//            String sha1Result = cryptoSha1("Hello, World!");
//            System.out.println("SHA1: " + sha1Result);
//
//            // 测试cryptoAesEncrypt
//            Map<String, String> opt = new HashMap<>();
//            opt.put("key", "your_key_here");
//            opt.put("iv", "your_iv_here");
//            Object aesEncryptResult = cryptoAesEncrypt("Hello, World!", opt);
//            System.out.println("AES Encrypt: " + aesEncryptResult);
//
//            // 测试cryptoAesDecrypt
//            String aesDecryptResult = (String) cryptoAesDecrypt((String) aesEncryptResult, "your_key_here", "your_iv_here");
//            System.out.println("AES Decrypt: " + aesDecryptResult);
//
//            // 测试cryptoRSAEncrypt
//            String rsaEncryptResult = cryptoRSAEncrypt("Hello, World!", null);
//            System.out.println("RSA Encrypt: " + rsaEncryptResult);
//
//            // 测试rsaEncrypt2
//            String rsaEncrypt2Result = rsaEncrypt2("Hello, World!");
//            System.out.println("RSA Encrypt2: " + rsaEncrypt2Result);
//
//            // 测试playlistAesEncrypt
//            Map<String, String> playlistEncryptResult = playlistAesEncrypt("Hello, World!");
//            System.out.println("Playlist AES Encrypt: " + playlistEncryptResult);
//
//            // 测试playlistAesDecrypt
//            Object playlistDecryptResult = playlistAesDecrypt(playlistEncryptResult);
//            System.out.println("Playlist AES Decrypt: " + playlistDecryptResult);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
