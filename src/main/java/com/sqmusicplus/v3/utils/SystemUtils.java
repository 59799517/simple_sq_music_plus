package com.sqmusicplus.v3.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SystemUtils {
    
    private static final Logger log = LoggerFactory.getLogger(SystemUtils.class);
    private static final DecimalFormat DF = new DecimalFormat("0.00");
    // 应用层流量统计（只保留最近数据）
    private static final AtomicLong currentAppUploadBytes = new AtomicLong(0);
    private static final AtomicLong currentAppDownloadBytes = new AtomicLong(0);
    private static long currentSampleTime = System.currentTimeMillis();
    
    // 新增：最小采样间隔，避免频繁重置导致数据不准确
    private static final long MIN_SAMPLE_INTERVAL_MS = 500; // 至少 0.5 秒采样一次
    
    // 新增：滑动平均窗口参数（用于平滑速度显示）
    private static final double SMOOTHING_FACTOR = 0.7; // 平滑因子（0.3=新数据占 30%，历史数据占 70%）
    private static volatile double lastUploadSpeed = 0.0;  // 上次计算的上传速度
    private static volatile double lastDownloadSpeed = 0.0; // 上次计算的下载速度



    /**
     * 记录应用程序上传数据（应用层监控）
     * @param bytes 上传字节数
     */
    public static void recordAppUpload(long bytes) {
        currentAppUploadBytes.addAndGet(bytes);
    }

    /**
     * 记录应用程序下载数据（应用层监控）
     * @param bytes 下载字节数
     */
    public static void recordAppDownload(long bytes) {
        currentAppDownloadBytes.addAndGet(bytes);
    }

    /**
     * 获取应用程序实时网络速度
     * @return 应用程序的上传和下载速度（bytes/second）
     */
    public static Map<String, Double> getApplicationSpeed() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - currentSampleTime;
            
        // 如果距离上次采样时间太短，直接返回上次的平滑速度，避免数据不准确
        if (timeElapsed < MIN_SAMPLE_INTERVAL_MS) {
            Map<String, Double> result = new HashMap<>();
            result.put("uploadSpeed", lastUploadSpeed);
            result.put("downloadSpeed", lastDownloadSpeed);
            return result;
        }
            
        long currentUpload = currentAppUploadBytes.get();
        long currentDownload = currentAppDownloadBytes.get();
            
        // 计算当前瞬时速度
        double currentUploadSpeed = 0.0;
        double currentDownloadSpeed = 0.0;
        
        if (currentUpload > 0 || currentDownload > 0) {
            currentUploadSpeed = (double)currentUpload * 1000 / timeElapsed;
            currentDownloadSpeed = (double)currentDownload * 1000 / timeElapsed;
        }
            
        // 使用指数加权移动平均（EWMA）平滑速度
        lastUploadSpeed = SMOOTHING_FACTOR * currentUploadSpeed + (1 - SMOOTHING_FACTOR) * lastUploadSpeed;
        lastDownloadSpeed = SMOOTHING_FACTOR * currentDownloadSpeed + (1 - SMOOTHING_FACTOR) * lastDownloadSpeed;
            
        // 重置计数器，只保留最近数据
        currentAppUploadBytes.set(0);
        currentAppDownloadBytes.set(0);
        currentSampleTime = currentTime;
            
        Map<String, Double> result = new HashMap<>();
        result.put("uploadSpeed", Math.max(0, lastUploadSpeed));
        result.put("downloadSpeed", Math.max(0, lastDownloadSpeed));
            
        return result;
    }

    /**
     * 获取应用程序实时网络速度报告实体
     * @return NetworkSpeedReport 对象包含详细的网络速度信息
     */
    public static NetworkSpeedReport getNetworkSpeedReport() {
        Map<String, Double> appSpeed = getApplicationSpeed();
        double uploadSpeed = appSpeed.get("uploadSpeed");
        double downloadSpeed = appSpeed.get("downloadSpeed");
        
        NetworkSpeedReport report = new NetworkSpeedReport();
        report.setUploadSpeed(uploadSpeed);
        report.setDownloadSpeed(downloadSpeed);
        report.setTotalSpeed(uploadSpeed + downloadSpeed);
        report.setUploadSpeedFormatted(formatSpeed(uploadSpeed));
        report.setDownloadSpeedFormatted(formatSpeed(downloadSpeed));
        report.setTotalSpeedFormatted(formatSpeed(uploadSpeed + downloadSpeed));
        
        return report;
    }
    
    /**
     * 获取格式化的速度报告（字符串形式，保持向后兼容）
     */
    public static String getFormattedSpeedReport() {
        NetworkSpeedReport report = getNetworkSpeedReport();
        return String.format(
            "应用程序实时速度:%n" +
            "  上传速度: %s%n" +
            "  下载速度: %s%n" +
            "  总速度: %s",
            report.getUploadSpeedFormatted(),
            report.getDownloadSpeedFormatted(),
            report.getTotalSpeedFormatted()
        );
    }
    
    /**
     * 格式化速度显示
     */
    private static String formatSpeed(double speed) {
        if (speed < 1024) {
            return DF.format(speed) + " B/s";
        } else if (speed < 1024 * 1024) {
            return DF.format(speed / 1024) + " KB/s";
        } else {
            return DF.format(speed / (1024 * 1024)) + " MB/s";
        }
    }

}