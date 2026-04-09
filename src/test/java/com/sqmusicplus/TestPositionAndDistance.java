package com.sqmusicplus;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.utils.AliyunDriveUtils;

public class TestPositionAndDistance {

    
    private static void testPositionAndDistance(KeyPositionDetectionV2 keyPositionDetection, 
                                               RSSIAnalyzerV2 rssiAnalyzer, 
                                               double rssiValue, 
                                               int index) {
        Integer position = keyPositionDetection.keyPositionDetection_V2(rssiValue);
        String distance = rssiAnalyzer.getData(String.valueOf(rssiValue));
        
        System.out.printf("第%3d次调用 | RSSI值: %6.2f | 位置: %6s | 距离: %6s%n", 
                         index, rssiValue, 
                         (position != null ? (position == 1 ? "车内" : "车外") : "等待数据"), 
                         (distance != null ? getDistanceText(distance) : "等待数据"));
        
        try {
            Thread.sleep(10); // 稍微延时以便观察
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static String getDistanceText(String distanceCode) {
        switch (distanceCode) {
            case "1": return "靠近";
            case "-1": return "远离";
            case "0": return "无变化";
            default: return distanceCode; // 直接显示返回值
        }
    }
}