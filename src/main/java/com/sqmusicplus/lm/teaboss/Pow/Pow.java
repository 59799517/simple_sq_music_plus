//package com.sqmusicplus.lm.teaboss.Pow;
//
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.util.Arrays;
//
//public class Pow {
//    public Pow() {
//    }
//
//    public static byte[] calcPow(byte[] _546) {
//        try {
//            BufferHelper r = new BufferHelper(_546);
//            byte a = r.getByte();
//            byte typ = r.getByte();
//            byte c = r.getByte();
//            boolean ok = r.getByte() != 0;
//            int e = r.getShort();
//            int f = r.getShort();
//            byte[] src = r.getArray(r.getShort());
//            byte[] tgt = r.getArray(r.getShort());
//            byte[] cpy = r.getArray(r.getShort());
//            byte[] dst = new byte[0];
//            long elp = 0L;
//            long cnt = 0L;
//            if (typ == 2 && tgt.length == 32) {
//                long start = System.currentTimeMillis();
//                BigInteger tmp = new BigInteger(src);
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//                messageDigest.update(tmp.toByteArray());
//                byte[] hash = messageDigest.digest();
//                BigInteger one = new BigInteger("1", 10);
//
//                while(!Arrays.equals(hash, tgt)) {
//                    tmp = tmp.add(one);
//                    messageDigest.reset();
//                    messageDigest.update(tmp.toByteArray());
//                    hash = messageDigest.digest();
//                    ++cnt;
//                    if (cnt >= 1000000L) {
//                        break;
//                    }
//                }
//
//                ok = true;
//                dst = tmp.toByteArray();
//                elp = System.currentTimeMillis() - start;
//            }
//
//            Pack w = new Pack();
//            w.setByte(a);
//            w.setByte(typ);
//            w.setByte(c);
//            w.setBoolean(ok);
//            w.setShort(e);
//            w.setShort(f);
//            w.setBinWithLen(src);
//            w.setBinWithLen(tgt);
//            w.setBinWithLen(cpy);
//            if (ok) {
//                w.setBinWithLen(dst);
//                w.setInt(elp);
//                w.setInt(cnt);
//            }
//
//            return w.getAll();
//        } catch (Exception var22) {
//            var22.printStackTrace();
//            return _546;
//        }
//    }
//
//    public static void main(String[] args) {
//        byte[] _546 = Converts.hexStringToByte("0102010200010000008013D45BE3CC245CA4C78DE26381435856C5C3A4772EA8AFBCD846779D54218AC362D63E2D87AAF871454C7A943E65771CC84D2DC192936521FFD44EB055978DE76329ACA2F68519A542A4575E9A7DF7306105BA0440746E4FD71D67603CE829789797259B13BA69AD59223DBF86E01C6283084F88091AA031F09EF6E34BF829AE0020E4BA2DF17E2266A53537D6784443AFA5C98D131C248219C678122E18B94188A800AC0102010200010000008013D45BE3CC245CA4C78DE26381435856C5C3A4772EA8AFBCD846779D54218AC362D63E2D87AAF871454C7A943E65771CC84D2DC192936521FFD44EB055978DE76329ACA2F68519A542A4575E9A7DF7306105BA0440746E4FD71D67603CE829789797259B13BA69AD59223DBF86E01C6283084F88091AA031F09EF6E34BF829AE0020E4BA2DF17E2266A53537D6784443AFA5C98D131C248219C678122E18B94188A8");
//        byte[] _547 = calcPow(_546);
//        System.out.println(_547.length);
//        System.out.println(Converts.printBytesToHexString(_547));
//    }
//}
