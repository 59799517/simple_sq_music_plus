package com.sqmusicplus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Classname KeyPositionDetectionV2
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/11/24 17:38
 * @Created by SQ
 */

public class KeyPositionDetectionV2 {
    // 配置参数
    private static final int DATA_GROUP_SIZE = 60;         // 每60个数据作为一组
    private static final double MIN_RSSI = -100.0;        // RSSI最小值
    private static final double MAX_RSSI = -30.0;         // RSSI最大值
    private static final int SMOOTHING_WINDOW = 3;        // 平滑窗口大小
    private static final double KEY_IN_CAR_THRESHOLD = -60.0;  // 钥匙在车内的阈值

    // 数据存储
    private final Queue<Double> rawDataQueue = new LinkedList<>();  // 原始数据队列
    private int totalDataCount = 0;                                 // 总数据计数

    // 输入数据方法
//    public void inputData() {
//        try (Scanner scanner = new Scanner(System.in)) {
//            System.out.println("=== 钥匙位置检测系统 ===");
//            System.out.println("输入RSSI数据（输入 'exit' 结束输入，输入 'clear' 清空数据）：");
//            System.out.println("数据范围：" + MIN_RSSI + " 到 " + MAX_RSSI);
//            System.out.println("检测逻辑：每60个数据为一组，每组数据处理后立即输出位置判断");
//            System.out.println("阈值：" + KEY_IN_CAR_THRESHOLD + " (大于为车内，小于等于为车外)");
//
//            while (true) {
//                String input = scanner.nextLine();
//
//                if (input.equalsIgnoreCase("exit")) {
//                    System.out.println("程序结束");
//                    break;
//                } else if (input.equalsIgnoreCase("clear")) {
//                    rawDataQueue.clear();
//                    totalDataCount = 0;
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
//                        totalDataCount++;
//                        System.out.println("已接收数据: " + rssiValue +
//                                " | 当前队列长度: " + rawDataQueue.size() +
//                                " | 总数据数: " + totalDataCount);
//
//                        // 第二步：当数据达到60个时进行一组处理
//                        if (rawDataQueue.size() >= DATA_GROUP_SIZE) {
//                            processDataGroup();
//                        }
//
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



    public Integer keyPositionDetection_V2(double newValue) {
        if (newValue >= MIN_RSSI && newValue <= MAX_RSSI) {
            rawDataQueue.add(newValue);
            totalDataCount++;
//            System.out.println("已接收数据: " + newValue +
//                    " | 当前队列长度: " + rawDataQueue.size() +
//                    " | 总数据数: " + totalDataCount);

            // 第二步：当数据达到60个时进行一组处理
            if (rawDataQueue.size() >= DATA_GROUP_SIZE) {
                return  processDataGroup();
            }else {
                return null;
            }

        } else {
//            System.out.println("数据超出范围 (" + MIN_RSSI + " 到 " + MAX_RSSI + ")，已跳过");
            return null;
        }
    }


    // 处理60个数据一组
    private Integer processDataGroup() {
//        System.out.println("\n=== 开始处理数据组 ===");

        // 转换为列表便于处理
        List<Double> dataList = new ArrayList<>(rawDataQueue);

        // 平滑处理
        List<Double> smoothedData = smoothData(dataList);
//        System.out.println("平滑处理前数据量: " + dataList.size());
//        System.out.println("平滑处理后数据量: " + smoothedData.size());

        // 计算处理后数据的平均值
        double averageRssi = calculateAverageRssi(smoothedData);
//        System.out.println("平均RSSI值: " + String.format("%.2f", averageRssi));

        // 判断钥匙位置并输出结果
        int positionResult = determineKeyPosition(averageRssi);
//        System.out.println("=== 钥匙位置判断结果 ===");
//        System.out.println("判断阈值: " + KEY_IN_CAR_THRESHOLD);
//        System.out.println("平均RSSI: " + String.format("%.2f", averageRssi));
//        System.out.println("位置输出: " + positionResult);
//        System.out.println("位置状态: " + (positionResult == 1 ? "车内" : "车外"));
        // 为下一次处理准备：清空队列，等待下一组数据
        rawDataQueue.clear();
//        System.out.println("=== 数据组处理完成 ===");
//        System.out.println("已处理组数: " + (totalDataCount / DATA_GROUP_SIZE) + "\n");
        return positionResult;

    }

    // 计算钥匙位置（在processDataGroup中直接使用）

    // 数据平滑处理（保留原算法的移动平均）
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



    // 计算平均RSSI值
    private double calculateAverageRssi(List<Double> data) {
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.size();
    }

    // 判断钥匙位置
    private int determineKeyPosition(double averageRssi) {
        // 大于-60为车内(1)，小于等于-60为车外(0)
        return averageRssi > KEY_IN_CAR_THRESHOLD ? 1 : 0;
    }



    public boolean clear() {
        try {
            rawDataQueue.clear();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
