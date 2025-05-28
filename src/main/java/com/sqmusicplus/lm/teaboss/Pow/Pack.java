//package com.sqmusicplus.lm.teaboss.Pow;
//
//public class Pack {
//    private byte[] bytes;
//
//    public Pack() {
//    }
//
//    public byte[] getAll() {
//        return this.bytes;
//    }
//
//    public byte[] toByteArray() {
//        return this.bytes;
//    }
//
//    public String getHexString() {
//        return lm.teaboss.Pow.Converts.bytesToHexString(this.bytes);
//    }
//
//    public void empty() {
//        this.bytes = null;
//    }
//
//    public void clear() {
//        this.bytes = null;
//    }
//
//    public void setBin(byte[] bytes) {
//        if (bytes != null) {
//            if (this.bytes != null) {
//                int thisLength = this.bytes.length;
//                int length = bytes.length;
//                byte[] newByte = new byte[thisLength + length];
//                System.arraycopy(this.bytes, 0, newByte, 0, thisLength);
//                System.arraycopy(bytes, 0, newByte, thisLength, length);
//                this.bytes = newByte;
//            } else {
//                this.bytes = bytes;
//            }
//
//        }
//    }
//
//    public void write(byte[] bytes) {
//        this.setBin(bytes);
//    }
//
//    public void setHex(String input) {
//        this.setBin(lm.teaboss.Pow.Converts.hexStringToByte(input));
//    }
//
//    public void setHexWithLen(String input) {
//        this.setBinWithLen(lm.teaboss.Pow.Converts.hexStringToByte(input));
//    }
//
//    public void setBoolean(boolean b) {
//        if (b) {
//            this.setByte((int)1);
//        } else {
//            this.setByte((int)0);
//        }
//
//    }
//
//    public void setByte(byte b) {
//        this.setBin(new byte[]{b});
//    }
//
//    public void setByte(int num) {
//        this.setBin(new byte[]{(byte)num});
//    }
//
//    public void setShort(short num) {
//        this.setBin(lm.teaboss.Pow.Converts.shortToBytes(num));
//    }
//
//    public void setShort(int num) {
//        this.setBin(lm.teaboss.Pow.Converts.numTo2Bytes(num));
//    }
//
//    public void setInt(int num) {
//        this.setBin(lm.teaboss.Pow.Converts.intToBytes(num));
//    }
//
//    public void setInt(long num) {
//        this.setBin(lm.teaboss.Pow.Converts.longTo4Bytes(num));
//    }
//
//    public void setLong(long num) {
//        this.setBin(Converts.longToBytes(num));
//    }
//
//    public void setStr(String input) {
//        try {
//            this.setBin(input.getBytes("UTF8"));
//        } catch (Exception var3) {
//            var3.printStackTrace();
//        }
//
//    }
//
//    public void setBinWithLen(byte[] bin) {
//        try {
//            this.setShort(bin.length);
//            this.setBin(bin);
//        } catch (Exception var3) {
//            var3.printStackTrace();
//        }
//
//    }
//
//    public void setStrWithLen(String input) {
//        try {
//            byte[] bin = input.getBytes("UTF8");
//            this.setShort(bin.length);
//            this.setBin(bin);
//        } catch (Exception var3) {
//            var3.printStackTrace();
//        }
//
//    }
//}
//
