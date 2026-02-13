package com.sqmusicplus;

public class TestPositionAndDistance {
    public static void main(String[] args) {
        KeyPositionDetectionV2 keyPositionDetection = new KeyPositionDetectionV2();
        RSSIAnalyzerV2 rssiAnalyzer = new RSSIAnalyzerV2();
        
        System.out.println("开始测试位置和距离检测功能...");
        
        // 模拟钥匙从车外到车内的场景
        // 前100个数据点模拟钥匙在车外（较弱的信号）
        System.out.println("\n=== 模拟钥匙在车外 ===");
        for (int i = 0; i < 100; i++) {
            double rssiValue = -75.0 - Math.random() * 10; // -75到-85之间
            testPositionAndDistance(keyPositionDetection, rssiAnalyzer, rssiValue, i+1);
        }
        
        // 接下来100个数据点模拟钥匙正在靠近车内（逐渐增强的信号）
        System.out.println("\n=== 模拟钥匙正在靠近车内 ===");
        for (int i = 100; i < 200; i++) {
            double rssiValue = -75.0 + (i-99) * 0.4; // 从-75逐渐增加到-35
            testPositionAndDistance(keyPositionDetection, rssiAnalyzer, rssiValue, i+1);
        }
        
        // 再接下来100个数据点模拟钥匙在车内（强信号）
        System.out.println("\n=== 模拟钥匙在车内 ===");
        for (int i = 200; i < 300; i++) {
            double rssiValue = -45.0 + Math.random() * 5; // -45到-40之间
            testPositionAndDistance(keyPositionDetection, rssiAnalyzer, rssiValue, i+1);
        }
        
        // 接下来100个数据点模拟钥匙正在远离车内
        System.out.println("\n=== 模拟钥匙正在远离车内 ===");
        for (int i = 300; i < 400; i++) {
            double rssiValue = -45.0 - (i-299) * 0.4; // 从-45逐渐减少到-85
            testPositionAndDistance(keyPositionDetection, rssiAnalyzer, rssiValue, i+1);
        }
        
        // 最后100个数据点模拟钥匙回到车外
        System.out.println("\n=== 模拟钥匙在车外 ===");
        for (int i = 400; i < 500; i++) {
            double rssiValue = -75.0 - Math.random() * 10; // -75到-85之间
            testPositionAndDistance(keyPositionDetection, rssiAnalyzer, rssiValue, i+1);
        }
    }
    
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