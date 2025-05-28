package com.sqmusicplus.lm.teaboss.Qimei;

import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @Classname QQDevice
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/4/27 11:47
 * @Created by SQ
 */


@Data
public class QQDevice {
    // OSVersion 内部类
    @Data
    public static class OSVersion {
        public String incremental = "5891938";
        public String release = "10";
        public String codename = "REL";
        public int sdk = 29;

        public OSVersion() {}
    }

    // 字段定义
    public String display;
    public String product = "iarim";
    public String device = "sagit";
    public String board = "eomam";
    public String model = "MI 6";
    public String fingerprint;
    public String bootId = UUID.randomUUID().toString();
    public String procVersion;
    public String imei;
    public String brand = "Xiaomi";
    public String bootloader = "U-boot";
    public String baseBand = "";
    public OSVersion version = new OSVersion();
    public String simInfo = "T-Mobile";
    public String osType = "android";
    public String macAddress = "00:50:56:C0:00:08";
    public static List<Integer> ipAddress = Arrays.asList(10, 0, 1, 3);
    public String wifiBssid = "00:50:56:C0:00:08";
    public String wifiSsid = "<unknown ssid>";
    public List<Integer> imsiMd5;
    public String androidId;
    public String apn = "wifi";
    public String vendorName = "MIUI";
    public String vendorOsName = "qmapi";
    public String qimei;

    public QQDevice() {
        // 初始化需要复杂逻辑的字段
        this.display = "QMAPI." + randomInt(100000, 999999) + ".001";
        this.fingerprint = "xiaomi/iarim/sagit:10/eomam.200122.001/"
                + randomInt(1000000, 9999999) + ":user/release-keys";
        this.procVersion = "Linux 5.4.0-54-generic-" + randomString(8)
                + " (android-build@google.com)";
        this.imei = randomImei();
        this.imsiMd5 = generateMd5Bytes();
        this.androidId = generateAndroidId();
    }

    // 辅助方法
    private static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(randomInt(0, chars.length()-1)));
        }
        return sb.toString();
    }

    public static String randomImei() {
        int[] imei = new int[15];
        int sum = 0;
        for (int i = 0; i < 14; i++) {
            int num = randomInt(0, 9);
            if ((i + 2) % 2 == 0) {
                num *= 2;
                if (num >= 10) num = (num % 10) + 1;
            }
            sum += num;
            imei[i] = num;
        }
        imei[14] = (sum * 9) % 10;
        return Arrays.stream(imei).mapToObj(Integer::toString).collect(Collectors.joining());
    }

    private List<Integer> generateMd5Bytes() {
        byte[] bytes = new byte[16];
        new Random().nextBytes(bytes);
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(bytes);
            List<Integer> result = new ArrayList<>();
            for (byte b : digest) {
                result.add((int) b);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateAndroidId() {
        byte[] bytes = new byte[8];
        new Random().nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
