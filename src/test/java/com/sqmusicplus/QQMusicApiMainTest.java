//package com.sqmusicplus;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Map;
//import java.util.Scanner;
//
//public class QQMusicApiMainTest {
//    public static void main(String[] args) {
//        String number = "99999999999999";
//        int i = hash33(number);
//        System.out.println(i);
//        int i1 = sigHash(number);
//        System.out.println(i1);
//
//
////        try {
////            System.out.println("=== QQ音乐API测试 ===");
////            System.out.println("1. 测试获取QQ二维码");
////            System.out.println("2. 测试获取微信二维码");
////            System.out.println("3. 测试检查二维码状态");
////            System.out.println("4. 测试发送验证码");
////            System.out.println("5. 测试手机号登录");
////            System.out.println("6. 测试hash33算法");
////            System.out.println("7. 测试Credential类");
////            System.out.println("0. 退出");
////            System.out.print("请选择测试项目: ");
////
////            Scanner scanner = new Scanner(System.in);
////            int choice = scanner.nextInt();
////
////            switch (choice) {
////                case 1:
////                    testGetQQQRCode();
////                    break;
////                case 2:
////                    testGetWXQRCode();
////                    break;
////                case 3:
////                    testCheckQRCode();
////                    break;
////                case 4:
////                    testSendAuthCode();
////                    break;
////                case 5:
////                    testPhoneAuthorize();
////                    break;
////                case 6:
////                    testHash33();
////                    break;
////                case 7:
////                    testCredential();
////                    break;
////                case 0:
////                    System.out.println("退出测试");
////                    return;
////                default:
////                    System.out.println("无效的选择");
////            }
////        } catch (Exception e) {
////            System.out.println("测试过程中发生错误: " + e.getMessage());
////            e.printStackTrace();
////        }
//    }
//
//    private static void testGetQQQRCode() throws Exception {
//        System.out.println("\n=== 测试获取QQ登录二维码 ===");
//        QQMusicApi.QR qr = QQMusicApi.getQRCode(QQMusicApi.QRLoginType.QQ);
//        System.out.println("二维码类型: " + qr.getQrType());
//        System.out.println("MIME类型: " + qr.getMimeType());
//        System.out.println("标识符: " + qr.getIdentifier());
//        QQMusicApi.getQQMusicQrBase64(qr.getData());
//        // 保存二维码图片
////        QQMusicApi.printQRCode(qr.getData());
//    }
//
//    private static void testGetWXQRCode() throws Exception {
//        System.out.println("\n=== 测试获取微信登录二维码 ===");
//        QQMusicApi.QR qr = QQMusicApi.getQRCode(QQMusicApi.QRLoginType.WX);
//        System.out.println("二维码类型: " + qr.getQrType());
//        System.out.println("MIME类型: " + qr.getMimeType());
//        System.out.println("标识符: " + qr.getIdentifier());
//
//        // 保存二维码图片
//        Files.write(Paths.get("test_wx_qr.png"), qr.getData());
//        System.out.println("二维码已保存到 test_wx_qr.png");
//    }
//
//    private static void testCheckQRCode() throws Exception {
//        System.out.println("\n=== 测试检查二维码状态 ===");
//        QQMusicApi.QR qr = QQMusicApi.getQRCode(QQMusicApi.QRLoginType.QQ);
//        Map.Entry<QQMusicApi.QRCodeLoginEvents, QQMusicApi.Credential> result =
//                QQMusicApi.checkQRCode(qr);
//
//        System.out.println("二维码状态: " + result.getKey());
//        if (result.getValue() != null) {
//            System.out.println("登录凭证: " + result.getValue().getMusicid());
//        } else {
//            System.out.println("未获取到登录凭证");
//        }
//    }
//
//    private static void testSendAuthCode() throws Exception {
//        System.out.println("\n=== 测试发送验证码 ===");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("请输入手机号: ");
//        int phone = scanner.nextInt();
//        System.out.print("请输入国家码(默认86): ");
//        int countryCode = scanner.nextInt();
//
//        Map.Entry<QQMusicApi.PhoneLoginEvents, String> result =
//                QQMusicApi.sendAuthCode(phone, countryCode);
//
//        System.out.println("发送状态: " + result.getKey());
//        if (result.getValue() != null) {
//            System.out.println("附加信息: " + result.getValue());
//        }
//    }
//
//    private static void testPhoneAuthorize() throws Exception {
//        System.out.println("\n=== 测试手机号登录 ===");
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("请输入手机号: ");
//        int phone = scanner.nextInt();
//        System.out.print("请输入验证码: ");
//        int authCode = scanner.nextInt();
//        System.out.print("请输入国家码(默认86): ");
//        int countryCode = scanner.nextInt();
//
//        try {
//            QQMusicApi.Credential credential = QQMusicApi.phoneAuthorize(phone, authCode, countryCode);
//            System.out.println("登录成功！");
//            System.out.println("MusicID: " + credential.getMusicid());
//            System.out.println("Musickey: " + credential.getMusickey());
//            System.out.println("OpenID: " + credential.getOpenid());
//        } catch (Exception e) {
//            System.out.println("登录失败: " + e.getMessage());
//        }
//    }
//
//    private static void testHash33() {
//        System.out.println("\n=== 测试hash33算法 ===");
//        String testString = "test_string";
//        int hash = QQMusicApi.hash33(testString);
//        System.out.println("测试字符串: " + testString);
//        System.out.println("Hash值: " + hash);
//
//        // 测试相同字符串
//        int sameHash = QQMusicApi.hash33(testString);
//        System.out.println("相同字符串Hash值: " + sameHash);
//        System.out.println("是否相同: " + (hash == sameHash));
//
//        // 测试不同字符串
//        int diffHash = QQMusicApi.hash33("different_string");
//        System.out.println("不同字符串Hash值: " + diffHash);
//        System.out.println("是否不同: " + (hash != diffHash));
//    }
//
//    private static void testCredential() {
//        System.out.println("\n=== 测试Credential类 ===");
//        QQMusicApi.Credential credential = new QQMusicApi.Credential();
//
//        // 测试setter和getter
//        credential.setMusicid("test_musicid");
//        credential.setMusickey("test_musickey");
//        credential.setOpenid("test_openid");
//        credential.setUnionid("test_unionid");
//        credential.setEncryptUin("test_encrypt_uin");
//        credential.setLoginType(1);
//        credential.setRefreshKey("test_refresh_key");
//        credential.setRefreshToken("test_refresh_token");
//
//        System.out.println("设置的值:");
//        System.out.println("MusicID: " + credential.getMusicid());
//        System.out.println("Musickey: " + credential.getMusickey());
//        System.out.println("OpenID: " + credential.getOpenid());
//        System.out.println("UnionID: " + credential.getUnionid());
//        System.out.println("EncryptUin: " + credential.getEncryptUin());
//        System.out.println("LoginType: " + credential.getLoginType());
//        System.out.println("RefreshKey: " + credential.getRefreshKey());
//        System.out.println("RefreshToken: " + credential.getRefreshToken());
//
//        // 测试fromCookies方法
//        Map<String, Object> cookies = Map.of(
//                "musicid", "cookie_musicid",
//                "musickey", "cookie_musickey",
//                "openid", "cookie_openid",
//                "unionid", "cookie_unionid",
//                "encrypt_uin", "cookie_encrypt_uin",
//                "login_type", "2"
//        );
//
//        QQMusicApi.Credential cookieCredential = QQMusicApi.Credential.fromCookies(cookies);
//        System.out.println("\n从Cookies创建的值:");
//        System.out.println("MusicID: " + cookieCredential.getMusicid());
//        System.out.println("Musickey: " + cookieCredential.getMusickey());
//        System.out.println("OpenID: " + cookieCredential.getOpenid());
//        System.out.println("UnionID: " + cookieCredential.getUnionid());
//        System.out.println("EncryptUin: " + cookieCredential.getEncryptUin());
//        System.out.println("LoginType: " + cookieCredential.getLoginType());
//    }
//    public static int hash33(String str) {
//        int hash = 0;
//        for (int i = 0; i < str.length(); i++) {
//            hash = (hash << 5) + hash + str.charAt(i);
//        }
//        return hash;
//    }
//
//    private static int sigHash(String qrsig) {
//        int hash = 0;
//        for (char c : qrsig.toCharArray()) {
//            hash = (hash << 5) + hash + c;
//        }
//        return hash & 0x7FFFFFFF;
//    }
//}
