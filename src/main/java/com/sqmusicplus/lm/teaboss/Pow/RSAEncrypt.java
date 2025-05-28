//package com.sqmusicplus.lm.teaboss.Pow;
//
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.HashMap;
//import java.util.Map;
//import javax.crypto.Cipher;
//
//
//public class RSAEncrypt
//{
//  public static void main(String[] args) throws Exception {
////    Map<Integer, String> keyMap = genKeyPair();
////
////
////
////    System.out.println("随机生成的公钥为:" + ((String)keyMap.get(Integer.valueOf(0))).length() + " " + (String)keyMap.get(Integer.valueOf(0)));
////    System.out.println("随机生成的私钥为:" + ((String)keyMap.get(Integer.valueOf(1))).length() + " " + (String)keyMap.get(Integer.valueOf(1)));
////    System.out.println("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDb49jFnNqMDLdl87UtY5jOMqqdMuvQg65Zuva3Qm1tORQGBuM04u7fqygA64XbOx9e/KPNkDNDmqS8SlsAPL1fV2lqM/phgV0NY62TJqSR+PLngwJd2rhYR8wQ1N0JE+R59a5c08EGsd6axStjHsVu2+evCf/SWU9Y/oQpEtOjGwIDAQAB");
////
////
////
////
////
////    String message = encrypt("34337qazwsxedc12", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCveW5nuJ2DHgvwQKpdd8EMw5TXQipeBHhxvgZxefhea80ovqIgq+kVCCOMBXWBqKYkKOisM0Qb0cZiirw4Ao4csKIEpVyvaG5ZGqlx726DzoipdVD3494/bxSyD7nHYJBfbEi0A+C55/StJ+lz9j3JXW4uZmMynsIKrTtb3WjFHQIDAQAB");
////    System.out.println(message);
////    System.out.println(message.length());
////    String a = "yoJSE7cDqc8nA1Ll6IJ+tF2YChldO3RilR7962EZpJ4djzHPGUtGqCXhyFHe9mj3C7dWqetMLNjisk+Qg/UBCxLF2zlMRB1cnWP/cIncgDZV8AoQlN3OKt9CPvGxDmwO9ls0IvCAa6ZkiSoBno72V1mkNBhhx/8HkOxGs4hBqzk=";
////    System.out.println(a.length());
////
////    byte[] b = Base64.decodeBase64(a);
////    System.out.println(Converts.printBytesToHexString(b));
//  }
//
//
//
//
//
//
//
//  public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
//    Map<Integer, String> keyMap = new HashMap<>();
//
//    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//
//    keyPairGen.initialize(1024, new SecureRandom());
//
//    KeyPair keyPair = keyPairGen.generateKeyPair();
//    RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
//    RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
//
//    System.out.println((publicKey.getEncoded()).length);
//    System.out.println(Converts.bytesToHexString(publicKey.getEncoded()));
//
//    String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
//
//    String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
//
//
//
//
//    keyMap.put(Integer.valueOf(0), publicKeyString);
//    keyMap.put(Integer.valueOf(1), privateKeyString);
//    return keyMap;
//  }
//
//
//
//
//
//
//
//
//
//
//  public static String encrypt(String str, String publicKey) throws Exception {
//    byte[] decoded = Base64.decodeBase64(publicKey);
//    RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
//
//    Cipher cipher = Cipher.getInstance("RSA");
//    cipher.init(1, pubKey);
//    String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
//    return outStr;
//  }
//
//
//  public static byte[] encryptByByte(String str, String publicKey) {
//    try {
//      byte[] decoded = Base64.decodeBase64(publicKey);
//      RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
//
//      Cipher cipher = Cipher.getInstance("RSA");
//      cipher.init(1, pubKey);
//      return cipher.doFinal(str.getBytes("UTF-8"));
//    } catch (Exception e) {
//      e.printStackTrace();
//
//      return new byte[0];
//    }
//  }
//
//
//
//
//
//
//
//
//
//  public static String decrypt(String str, String privateKey) throws Exception {
//    byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
//
//    byte[] decoded = Base64.decodeBase64(privateKey);
//    RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
//
//    Cipher cipher = Cipher.getInstance("RSA");
//    cipher.init(2, priKey);
//    String outStr = new String(cipher.doFinal(inputByte));
//    return outStr;
//  }
//
//
//
//
//
//
//
//
//
//
//  public static String decryptByByte(byte[] input, String privateKey) throws Exception {
//    byte[] inputByte = Base64.decodeBase64(input);
//
//    byte[] decoded = Base64.decodeBase64(privateKey);
//    RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
//
//    Cipher cipher = Cipher.getInstance("RSA");
//    cipher.init(2, priKey);
//    String outStr = new String(cipher.doFinal(inputByte));
//    return outStr;
//  }
//}
