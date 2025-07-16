package com.sqmusicplus.v3.utils;

/**
 * @Classname PakoUtil
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/7/24 14:09
 * @Created by SQ
 */

public class PakoUtil {

    /**
     * 从 Pako.js 中接受的数据
     * @param arrStr 形如： 123,2,09,
     * 字符串是由无符号整数构成，逗号分隔
     * @return
     */
    public static byte[] receive(String arrInt){

        /**
         * 将数字字符串 ->  byte[]
         */
        String[] a = arrInt.split(",");
        byte[] clientBytes = new byte[a.length];
        int i = 0;
        for (String e : a) {
            clientBytes[i] = Integer.valueOf(e).byteValue();
            i++;
        }
        return clientBytes;
    }

    /**
     * 发送给 Pako 的数据格式
     * @param bytes 服务端生成的字节数组
     * @return String 发送给 pako.js 的数据格式
     */
    public static String send(byte[] bytes) {
        String[] ints = new String[bytes.length];
        int j=0;
        for(byte e:bytes) {
            int t = e;
            if(t<0) {
                t = 256+t;
            }
            ints[j++] = String.valueOf(t);

        }

        return String.join(",", ints);
    }


}
