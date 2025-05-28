package com.sqmusicplus.lm.teaboss.Pow;

import java.util.Random;


public class CharacterUtils
{
  public static final String[] Base64String = new String[] {

      "YmJiYmJiYmJiYmJiYmJiYmJiYmJiYmJiYmJiYmJiYmI=",

      "aGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGhoaGg=",

      "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVU=",

      "WFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFg=",

      "Z2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2c=",

      "UlJSUlJSUlJSUlJSUlJSUlJSUlJSUlJSUlJSUlJSZWU=",

      "YWFhTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU0=",

      "ZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWU=",

      "UFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFA=",

      "WVlZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWVlZWVk=",

      "V1dXV1dXV1dXV1dXV1dXV1dXV1dXV1dXV1dXV1dXV1c="
    };
  public static int Index = 0;

  public static void main(String[] args) {
    for (int i = 0; i < 20; i++) {
      System.out.println(get00A8Base64String());
    }
  }


  public static String get00A8Base64String() {
    Index++;
    return Base64String[Index % Base64String.length];
  }



  public static String RandomStringRange(int length, String value) {
    Random random = new Random();
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < length; i++) {

      int number = random.nextInt(value.length());

      sb.append(value.charAt(number));
    }

    return sb.toString();
  }


  public static String getRandstr() {
    Random random = new Random();
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < 3; i++) {

      int number = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".length());

      sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".charAt(number));
    }

    return "@" + sb.toString();
  }


  public static String getTicket() {
    Random random = new Random();
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < 118; i++) {

      int number = random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-_".length());

      sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-_".charAt(number));
    }

    return "t02" + sb.toString() + "**";
  }

  public static String getCode() {
    return String.valueOf(getRandstr()) + "----" + getTicket();
  }
}

