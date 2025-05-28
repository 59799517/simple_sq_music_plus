//package com.sqmusicplus.utils;
//
///**
// * @Author 薄荷你玩
// * @Date 2023/04/16
// * @Website www.bhshare.cn
// */
//
//public class ConsoleQRcode {
//    /**
//     * 打印二维码 -> console
//     * @param content 二维码内容
//     */
//    public static void printQRcode(String content) {
//        int width = 1; // 二维码宽度
//        int height = 1; // 二维码高度
//
//        // 定义二维码的参数
//        HashMap<EncodeHintType, java.io.Serializable> hints = new HashMap<EncodeHintType, java.io.Serializable>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码方式
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//纠错等级
//
//        // 打印二维码
//        try {
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            for (int j = 0; j < bitMatrix.getHeight(); j++) {
//                for (int i = 0; i < bitMatrix.getWidth(); i++) {
//                    if (bitMatrix.get(i, j)) {
//                        System.out.print("■");
//                    } else {
//                        System.out.print("  ");
//                    }
//
//                }
//                System.out.println();
//            }
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String args[]) {
//        String str = "http://www.bhshare.cn";
//        printQRcode(str);
//    }
//}
