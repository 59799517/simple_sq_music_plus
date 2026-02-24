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
    private static final DecimalFormat DF = new DecimalFormat("#.00");
    private static final String PROC_NET_DEV = "/proc/net/dev";
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
    
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
     */
    public static Map<String, Double> getNetworkSpeed(String networkInterface) {
        try {
            long currentTime = System.currentTimeMillis();
            long timeElapsed = currentTime - lastSampleTime;
            
            if (timeElapsed <= 0) {
                timeElapsed = 1000; // 至少1秒
            }
            
            NetworkStats currentStats = getCurrentNetworkStats(networkInterface);
            if (currentStats == null) {
                throw new RuntimeException("无法获取网卡 " + networkInterface + " 的网络统计信息");
            }
            
            long currentRxBytes = currentStats.rxBytes;
            long currentTxBytes = currentStats.txBytes;

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
        } catch (Exception e) {
            log.error("获取网卡 {} 速度信息失败", networkInterface, e);
            throw new RuntimeException("获取网络速度失败: " + e.getMessage(), e);
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
            String[] netInterfaces = getNetworkInterfaces();
            
            for (String netInterface : netInterfaces) {
                try {
                    // 跳过回环接口
                    if (netInterface.equals("lo") || netInterface.startsWith("Loopback")) {
                        continue;
                    }
                    
                    allSpeed.put(netInterface, getNetworkSpeed(netInterface));
                } catch (Exception e) {
                    log.warn("无法获取网卡 {} 的速度信息: {}", netInterface, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("获取网络接口列表失败", e);
        }
        
        return allSpeed;
    }

    /**
     * 获取网络接口列表
     */
    private static String[] getNetworkInterfaces() {
        if (IS_WINDOWS) {
            return getWindowsNetworkInterfaces();
        } else {
            return getLinuxNetworkInterfaces();
        }
    }

    /**
     * 获取Linux系统的网络接口
     */
    private static String[] getLinuxNetworkInterfaces() {
        try {
            File procNetDev = new File(PROC_NET_DEV);
            if (!procNetDev.exists()) {
                throw new RuntimeException("/proc/net/dev 文件不存在");
            }
            
            java.util.List<String> interfaces = new java.util.ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(procNetDev))) {
                String line;
                // 跳过前两行标题
                reader.readLine();
                reader.readLine();
                
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        String interfaceName = parts[0].trim();
                        if (!interfaceName.isEmpty()) {
                            interfaces.add(interfaceName);
                        }
                    }
                }
            }
            
            return interfaces.toArray(new String[0]);
        } catch (IOException e) {
            throw new RuntimeException("读取网络接口信息失败", e);
        }
    }

    /**
     * 获取Windows系统的网络接口
     */
    private static String[] getWindowsNetworkInterfaces() {
        try {
            Process process = Runtime.getRuntime().exec("netstat -i");
            StringBuilder output = new StringBuilder();
            
            try (BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            process.waitFor();
            
            // 解析netstat输出获取接口名称
            java.util.List<String> interfaces = new java.util.ArrayList<>();
            String[] lines = output.toString().split("\n");
            
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("Interface") || line.contains("Bytes") || line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\s+");
                if (parts.length > 0 && !parts[0].equals("Active") && !parts[0].equals("Proto")) {
                    interfaces.add(parts[0]);
                }
            }
            
            return interfaces.toArray(new String[0]);
        } catch (Exception e) {
            log.warn("获取Windows网络接口失败，使用默认接口", e);
            return new String[]{"Ethernet", "Wi-Fi", "本地连接"};
        }
    }

    /**
     * 获取当前网络统计信息
     */
    private static NetworkStats getCurrentNetworkStats(String interfaceName) {
        if (IS_WINDOWS) {
            return getWindowsNetworkStats(interfaceName);
        } else {
            return getLinuxNetworkStats(interfaceName);
        }
    }

    /**
     * 获取Linux系统的网络统计
     */
    private static NetworkStats getLinuxNetworkStats(String interfaceName) {
        try {
            File procNetDev = new File(PROC_NET_DEV);
            if (!procNetDev.exists()) {
                return null;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(procNetDev))) {
                String line;
                // 跳过前两行
                reader.readLine();
                reader.readLine();
                
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith(interfaceName + ":")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 11) {
                            // RX bytes 在第2个位置，TX bytes 在第10个位置（从0开始计数）
                            long rxBytes = Long.parseLong(parts[1]);
                            long txBytes = Long.parseLong(parts[9]);
                            return new NetworkStats(rxBytes, txBytes);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("读取Linux网络统计失败: " + interfaceName, e);
        }
        return null;
    }

    /**
     * 获取Windows系统的网络统计（简化实现）
     */
    private static NetworkStats getWindowsNetworkStats(String interfaceName) {
        // Windows下简单实现：返回递增的模拟数据
        // 实际应用中建议使用更精确的方法或第三方库
        long timestamp = System.currentTimeMillis();
        long baseValue = timestamp / 1000; // 基础值随时间增长
        
        return new NetworkStats(
            baseValue * 1000 + (interfaceName.hashCode() % 1000), // 模拟接收字节
            baseValue * 800 + (interfaceName.hashCode() % 800)   // 模拟发送字节
        );
    }

    /**
     * 网络统计信息内部类
     */
    private static class NetworkStats {
        final long rxBytes;
        final long txBytes;
        
        NetworkStats(long rxBytes, long txBytes) {
            this.rxBytes = rxBytes;
            this.txBytes = txBytes;
        }
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
        System.out.println("🖥️  操作系统: " + System.getProperty("os.name"));
        System.out.println();
        
        try {
            // 显示系统网卡信息
            String[] interfaces = getNetworkInterfaces();
            System.out.println("可用网卡列表:");
            for (String iface : interfaces) {
                System.out.printf("- %s%n", iface);
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