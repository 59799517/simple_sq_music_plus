package com.sqmusicplus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Classname RSSIAnalyzerV2
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/11/24 17:47
 * @Created by SQ
 */

public class RSSIAnalyzerV2 {
    // 配置参数
    private static final int DATA_WINDOW_SIZE = 20;      // 每20个数据计算一次斜率
    private static final int SLOPE_WINDOW_SIZE = 3;      // 每3个斜率计算一次平均-
    private static final double MIN_RSSI = -100.0;       // RSSI最小值
    private static final double MAX_RSSI = -30.0;        // RSSI最大值
    private static final int SMOOTHING_WINDOW = 3;       // 平滑窗口大小
    private static final double DOOR_OPEN_THRESHOLD = 0.5;  // 开门阈值（正斜率表示靠近）
    private static final double DOOR_CLOSE_THRESHOLD = -0.5; // 关门阈值（负斜率表示远离）

    // 数据存储
    private final Queue<Double> rawDataQueue = new LinkedList<>();  // 原始数据队列
    private final Queue<Double> slopeQueue = new LinkedList<>();    // 斜率队列
    private final List<Double> allSlopes = new ArrayList<>();       // 所有斜率记录

    // 输入数据方法
//    public void inputData() {
//        try (Scanner scanner = new Scanner(System.in)) {
//            System.out.println("=== 蓝牙车门控制系统 ===");
//            System.out.println("输入RSSI数据（输入 'exit' 结束输入，输入 'clear' 清空数据）：");
//            System.out.println("数据范围：" + MIN_RSSI + " 到 " + MAX_RSSI);
//
//            while (true) {
//                String input = scanner.nextLine();
//
//                if (input.equalsIgnoreCase("exit")) {
//                    System.out.println("程序结束");
//                    break;
//                } else if (input.equalsIgnoreCase("clear")) {
//                    rawDataQueue.clear();
//                    slopeQueue.clear();
//                    allSlopes.clear();
//                    System.out.println("所有数据已清空，等待新的数据输入...");
//                    continue;
//                }
//
//                try {
//                    double rssiValue = Double.parseDouble(input);
//
//                    // 第一步：数据范围筛查 (-30到-100)
//                    if (rssiValue >= MIN_RSSI && rssiValue <= MAX_RSSI) {
//                        rawDataQueue.add(rssiValue);
//                        System.out.println("已接收数据: " + rssiValue + " | 当前队列长度: " + rawDataQueue.size());
//
//                        // 第二步：当数据达到20个时进行处理
//                        if (rawDataQueue.size() >= DATA_WINDOW_SIZE) {
//                            processDataWindow();
//                        }
//                    } else {
//                        System.out.println("数据超出范围 (" + MIN_RSSI + " 到 " + MAX_RSSI + ")，已跳过");
//                    }
//
//                } catch (NumberFormatException e) {
//                    System.out.println("无效输入，请输入有效的数字");
//                }
//            }
//        }
//    }

    // 处理30个数据窗口
    private String processDataWindow() {
//        System.out.println("\n=== 开始处理数据窗口 ===");

        // 转换为列表便于处理
        List<Double> dataList = new ArrayList<>(rawDataQueue);

        // 第三步：平滑处理
        List<Double> smoothedData = smoothData(dataList);
//        System.out.println("平滑处理后的数据量: " + smoothedData.size());

        // 第四步：计算斜率
        double slope = calculateSlope(smoothedData);
//        System.out.println("当前窗口斜率: " + String.format("%.4f", slope));

        // 添加斜率到队列
        slopeQueue.add(slope);
        allSlopes.add(slope);

        // 第五步：滑动窗口计算平均斜率
        if (slopeQueue.size() >= SLOPE_WINDOW_SIZE) {
            // 保持斜率队列大小为3（滑动窗口）
            if (slopeQueue.size() > SLOPE_WINDOW_SIZE) {
                slopeQueue.poll(); // 淘汰最前面的斜率
            }

            // 计算平均斜率
            double averageSlope = calculateAverageSlope();
//            System.out.println("最近" + SLOPE_WINDOW_SIZE + "个斜率平均值: " + String.format("%.4f", averageSlope));

            // 第六步：判断开关车门
           return determineDoorAction(averageSlope);
        }

        // 为下一次处理准备：保留最近的60个数据作为重叠
        // 这样可以实现滑动窗口的连续性
        for (int i = 0; i < DATA_WINDOW_SIZE / 0.25; i++) {
            rawDataQueue.poll();
        }

//        System.out.println("=== 数据窗口处理完成 ===");
//        System.out.println("剩余数据量: " + rawDataQueue.size() + "\n");
        return null;
    }

    // 数据平滑处理（移动平均）
    private List<Double> smoothData(List<Double> data) {
        List<Double> smoothedData = new ArrayList<>();

        for (int i = 0; i < data.size() - SMOOTHING_WINDOW + 1; i++) {
            double sum = 0;
            for (int j = i; j < i + SMOOTHING_WINDOW; j++) {
                sum += data.get(j);
            }
            smoothedData.add(sum / SMOOTHING_WINDOW);
        }

        return smoothedData;
    }

    // 计算斜率（最小二乘法）
    private double calculateSlope(List<Double> data) {
        int n = data.size();
        if (n < 2) {
            return 0.0;
        }

        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;

        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += data.get(i);
            sumXY += i * data.get(i);
            sumXX += i * i;
        }

        // 计算斜率: m = (nΣxy - ΣxΣy) / (nΣx² - (Σx)²)
        double denominator = n * sumXX - sumX * sumX;
        if (denominator == 0) {
            return 0.0;
        }

        return (n * sumXY - sumX * sumY) / denominator;
    }

    // 计算平均斜率
    private double calculateAverageSlope() {
        double sum = 0;
        for (double slope : slopeQueue) {
            sum += slope;
        }
        return sum / slopeQueue.size();
    }

    // 判断开关车门动作
    private String determineDoorAction(double averageSlope) {
//        System.out.println("=== 车门控制判断 ===");
//        System.out.println("平均斜率: " + String.format("%.4f", averageSlope));
//        System.out.println("开门阈值: " + DOOR_OPEN_THRESHOLD);
//        System.out.println("关门阈值: " + DOOR_CLOSE_THRESHOLD);

        String action;
        if (averageSlope > DOOR_OPEN_THRESHOLD) {
            action = "开门";
//            System.out.println("命令输出: 1"); // 1表示开门
//            System.out.println("命令执行: " + action);
            return "1";
        } else if (averageSlope < DOOR_CLOSE_THRESHOLD) {
            action = "关门";
//            System.out.println("命令输出: -1"); // -1表示关门
//            System.out.println("命令执行: " + action);
            return "-1";
        } else {
            action = "保持当前状态";
//            System.out.println("命令输出: 0"); // 0表示无动作
//            System.out.println("命令执行: " + action);
            return "0";
        }


    }

    // 主程序入口


    public String getData(String keyPosition) {

        try {
            double rssiValue = Double.parseDouble(keyPosition);

            // 第一步：数据范围筛查 (-30到-100)
            if (rssiValue >= MIN_RSSI && rssiValue <= MAX_RSSI) {
                rawDataQueue.add(rssiValue);
//                System.out.println("已接收数据: " + rssiValue + " | 当前队列长度: " + rawDataQueue.size());

                // 第二步：当数据达到20个时进行处理
                if (rawDataQueue.size() >= DATA_WINDOW_SIZE) {
                  return  processDataWindow();
                }
            } else {
//                System.out.println("数据超出范围 (" + MIN_RSSI + " 到 " + MAX_RSSI + ")，已跳过");
            }

        } catch (NumberFormatException e) {
            System.out.println("无效输入，请输入有效的数字");
        }
        return null;
    }

    public boolean clear(){
        try {
            rawDataQueue.clear();
            slopeQueue.clear();
            allSlopes.clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
