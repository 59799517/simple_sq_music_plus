package com.sqmusicplus.v3.utils;

import org.hyperic.sigar.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 系统工具类 - 实时网络速度监控
 * 
 * ⚠️ 重要说明：
 * 1. Sigar 无法直接获取特定进程的网络流量
 * 2. 本类提供系统级网卡流量监控
 * 3. 程序级流量监控通过应用层统计实现
 * 4. 只缓存最近一次的流量数据，不存储历史记录
 */
public class SystemUtils {
    
    private static final Logger log = LoggerFactory.getLogger(SystemUtils.class);
    private static final DecimalFormat DF = new DecimalFormat("#.00");
    private static Sigar sigar = new Sigar();
    // 只存储最后一次的流量数据，不保存历史
    private static Map<String, Long> lastRxBytes = new ConcurrentHashMap<>();
    private static Map<String, Long> lastTxBytes = new ConcurrentHashMap<>();
    private static long lastSampleTime = System.currentTimeMillis();
    
    // 应用层流量统计（只保留最近数据）
    private static final AtomicLong currentAppUploadBytes = new AtomicLong(0);
    private static final AtomicLong currentAppDownloadBytes = new AtomicLong(0);
    private static long currentSampleTime = System.currentTimeMillis();

    /**
     * 获取指定网卡的实时网络速度（系统级别）
     *
     * @param networkInterface 网卡名称
     * @return 包含上传和下载速度的 Map（单位：bytes/second）
     * @throws SigarException 如果获取失败则抛出异常
     */
    public static Map<String, Double> getNetworkSpeed(String networkInterface) throws SigarException {
        try {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastSampleTime;
            
            if (timeElapsed <= 0) {
                timeElapsed = 1000; // 至少1秒
            }
            
            NetInterfaceStat netStat = sigar.getNetInterfaceStat(networkInterface);
            
            long currentRxBytes = netStat.getRxBytes(); // 接收字节数
            long currentTxBytes = netStat.getTxBytes(); // 发送字节数

            long lastRx = lastRxBytes.getOrDefault(networkInterface, currentRxBytes);
            long lastTx = lastTxBytes.getOrDefault(networkInterface, currentTxBytes);

            // 计算速度（bytes/second）
            double downloadSpeed = (double)(currentRxBytes - lastRx) * 1000 / timeElapsed;
            double uploadSpeed = (double)(currentTxBytes - lastTx) * 1000 / timeElapsed;

            // 只更新最后一次的数据，不保存历史
            lastRxBytes.put(networkInterface, currentRxBytes);
            lastTxBytes.put(networkInterface, currentTxBytes);
            lastSampleTime = currentTime;

            Map<String, Double> result = new HashMap<>();
            result.put("uploadSpeed", Math.max(0, uploadSpeed));
            result.put("downloadSpeed", Math.max(0, downloadSpeed));

            log.debug("网卡 {} 速度统计 - 上传: {} bytes/s, 下载: {} bytes/s", 
                     networkInterface, uploadSpeed, downloadSpeed);

            return result;
        } catch (SigarException e) {
            log.error("获取网卡 {} 速度信息失败", networkInterface, e);
            throw e;
        }
    }

    /**
     * 获取所有可用网卡的实时网络速度（系统级别）
     *
     * @return 所有网卡的速度信息
     */
    public static Map<String, Map<String, Double>> getAllNetworkSpeed() {
        Map<String, Map<String, Double>> allSpeed = new HashMap<>();
        
        try {
            String[] netInterfaces = sigar.getNetInterfaceList();
            
            for (String netInterface : netInterfaces) {
                try {
                    // 跳过回环接口和无效接口
                    NetInterfaceConfig config = sigar.getNetInterfaceConfig(netInterface);
                    if (config.getAddress() == null || 
                        config.getAddress().startsWith("127.") ||
                        "0.0.0.0".equals(config.getAddress())) {
                        continue;
                    }
                    
                    allSpeed.put(netInterface, getNetworkSpeed(netInterface));
                } catch (SigarException e) {
                    log.warn("无法获取网卡 {} 的速度信息: {}", netInterface, e.getMessage());
                }
            }
        } catch (SigarException e) {
            log.error("获取网络接口列表失败", e);
        }
        
        return allSpeed;
    }

    /**
     * 记录应用程序上传数据（应用层监控）
     * @param bytes 上传字节数
     */
    public static void recordAppUpload(long bytes) {
        currentAppUploadBytes.addAndGet(bytes);
        log.debug("记录应用上传: {} bytes", bytes);
    }

    /**
     * 记录应用程序下载数据（应用层监控）
     * @param bytes 下载字节数
     */
    public static void recordAppDownload(long bytes) {
        currentAppDownloadBytes.addAndGet(bytes);
        log.debug("记录应用下载: {} bytes", bytes);
    }

    /**
     * 获取应用程序实时网络速度
     * @return 应用程序的上传和下载速度（bytes/second）
     */
    public static Map<String, Double> getApplicationSpeed() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - currentSampleTime;
        
        if (timeElapsed <= 0) {
            timeElapsed = 1000; // 至少1秒
        }
        
        long currentUpload = currentAppUploadBytes.get();
        long currentDownload = currentAppDownloadBytes.get();
        
        // 计算速度
        double uploadSpeed = (double)currentUpload * 1000 / timeElapsed;
        double downloadSpeed = (double)currentDownload * 1000 / timeElapsed;
        
        // 重置计数器，只保留最近数据
        currentAppUploadBytes.set(0);
        currentAppDownloadBytes.set(0);
        currentSampleTime = currentTime;
        
        Map<String, Double> result = new HashMap<>();
        result.put("uploadSpeed", Math.max(0, uploadSpeed));
        result.put("downloadSpeed", Math.max(0, downloadSpeed));
        
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

    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== 实时网络速度监控工具 ===");
        System.out.println("📊 显示最近1秒的上传和下载速度");
        System.out.println("💾 只缓存最近数据，不保存历史记录");
        System.out.println();
        
        try {
            // 显示系统网卡信息
            String[] interfaces = sigar.getNetInterfaceList();
            System.out.println("可用网卡列表:");
            for (String iface : interfaces) {
                try {
                    NetInterfaceConfig config = sigar.getNetInterfaceConfig(iface);
                    if (config.getAddress() != null) {
                        System.out.printf("- %s (%s)%n", iface, config.getAddress());
                    }
                } catch (Exception e) {
                    System.out.printf("- %s (无法获取信息)%n", iface);
                }
            }
            System.out.println();
            
            System.out.println("开始实时监控（按Ctrl+C停止）:");
            System.out.println("=====================================");
            
            // 循环显示实时速度
            while (true) {
                try {
                    Thread.sleep(1000); // 每秒更新
                    
                    // 显示应用程序速度
                    System.out.println(getFormattedSpeedReport());
                    System.out.println("-------------------------------------");
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}