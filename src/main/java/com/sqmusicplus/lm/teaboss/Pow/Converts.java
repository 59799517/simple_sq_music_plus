package com.sqmusicplus.lm.teaboss.Pow;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public final class Converts {
    public static final char[] BToA = "0123456789abcdef".toCharArray();

    private Converts() {
    }

    public static byte[] hexStringToByte(String hex) {
        hex = hex.replace(" ", "");
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();

        for(int i = 0; i < len; ++i) {
            int pos = i * 2;
            result[i] = (byte)(toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }

        return result;
    }

    public static byte[] numTo1Bytes(Integer num) {
        String uinHex;
        for(uinHex = Integer.toHexString(num); uinHex.length() < 2; uinHex = "0" + uinHex) {
        }

        return hexStringToByte(uinHex.toUpperCase());
    }

    public static byte[] numTo2Bytes(Integer num) {
        String uinHex;
        for(uinHex = Integer.toHexString(num); uinHex.length() < 4; uinHex = "0" + uinHex) {
        }

        return hexStringToByte(uinHex.toUpperCase());
    }

    public static byte[] shortToBytes(short s) {
        byte[] targets = new byte[2];

        for(int i = 0; i < 2; ++i) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte)(s >>> offset & 255);
        }

        return targets;
    }

    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[]{(byte)(num >>> 24), (byte)(num >>> 16), (byte)(num >>> 8), (byte)num};
        return bytes;
    }

    public static byte[] longTo4Bytes(Long num) {
        String uinHex;
        for(uinHex = Long.toHexString(num); uinHex.length() < 8; uinHex = "0" + uinHex) {
        }

        return hexStringToByte(uinHex.toUpperCase());
    }

    public static byte[] longToBytes(long values) {
        byte[] buffer = new byte[8];

        for(int i = 0; i < 8; ++i) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte)((int)(values >> offset & 255L));
        }

        return buffer;
    }


    public static long bytesToNum(byte[] b) {
        long temp = 0L;
        long res = 0L;

        for(int i = 0; i < b.length; ++i) {
            res <<= 8;
            temp = (long)(b[i] & 255);
            res |= temp;
        }

        return res;
    }
    private static byte toByte(char c) {
        byte b = (byte)"0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        for(int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }

        return sb.toString();
    }

    public static final String printBytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return " null bArray";
        } else {
            String byteStr = bytesToHexString(bArray);
            StringBuffer temp = new StringBuffer();

            for(int i = 0; i < byteStr.length(); ++i) {
                if (i != 0 && i % 2 == 0) {
                    temp.append(" " + byteStr.charAt(i));
                } else {
                    temp.append(byteStr.charAt(i));
                }
            }

            return temp.toString();
        }
    }


    public static String MD5EncodeToHex(String origin) {
        return bytesToHexString(MD5Encode(origin));
    }

    public static byte[] MD5Encode(String origin) {
        return MD5Encode(origin.getBytes());
    }

    public static byte[] MD5Encode(byte[] bytes) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }

    public static void main(String[] args) {
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(hexStringToByte("02 36 13 03 19 20 EB B9 D1 4B 03 04 01 00 00 01 01 01 00 00 67 77"));
        System.out.println(printBytesToHexString(longTo4Bytes(crc.getValue())));
        System.out.println(printBytesToHexString(longTo4Bytes(System.currentTimeMillis())));
    }
}
