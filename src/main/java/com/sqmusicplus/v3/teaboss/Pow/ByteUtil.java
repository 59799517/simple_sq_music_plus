package com.sqmusicplus.v3.teaboss.Pow;

public class ByteUtil
{
    public static byte[] pack(byte[] src) {
        int index = -1; int i;
        for (i = 0; i < src.length; i++) {
            if (src[i] != 0) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            byte[] rs = new byte[src.length - index];
            System.arraycopy(src, index, rs, 0, rs.length);
            src = rs;
        } else {
            return new byte[0];
        }
        index = -1;
        for (i = src.length; i > 0; i--) {
            if (src[i - 1] != 0) {
                index = i - 1;
                break;
            }
        }
        if (index > -1) {
            byte[] rs = new byte[index + 1];
            System.arraycopy(src, 0, rs, 0, rs.length);
            src = rs;
        } else {
            return new byte[0];
        }
        return src;
    }

    public static void main(String[] args) {
        System.out.println(Converts.printBytesToHexString(pack(Converts.hexStringToByte("01 00 00"))));
    }
    public static byte[] merger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

}
