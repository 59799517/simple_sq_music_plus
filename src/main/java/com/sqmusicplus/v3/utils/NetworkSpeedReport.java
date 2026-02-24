package com.sqmusicplus.v3.utils;

/**
 * 网络速度报告实体类
 * 用于封装应用程序的实时网络速度信息
 */
public class NetworkSpeedReport {
    private double uploadSpeed;     // 上传速度 (bytes/second)
    private double downloadSpeed;   // 下载速度 (bytes/second)
    private double totalSpeed;      // 总速度 (bytes/second)
    private String uploadSpeedFormatted;   // 格式化的上传速度
    private String downloadSpeedFormatted; // 格式化的下载速度
    private String totalSpeedFormatted;    // 格式化的总速度
    private long timestamp;         // 时间戳

    public NetworkSpeedReport() {
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public double getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(double uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    public double getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(double downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public double getTotalSpeed() {
        return totalSpeed;
    }

    public void setTotalSpeed(double totalSpeed) {
        this.totalSpeed = totalSpeed;
    }

    public String getUploadSpeedFormatted() {
        return uploadSpeedFormatted;
    }

    public void setUploadSpeedFormatted(String uploadSpeedFormatted) {
        this.uploadSpeedFormatted = uploadSpeedFormatted;
    }

    public String getDownloadSpeedFormatted() {
        return downloadSpeedFormatted;
    }

    public void setDownloadSpeedFormatted(String downloadSpeedFormatted) {
        this.downloadSpeedFormatted = downloadSpeedFormatted;
    }

    public String getTotalSpeedFormatted() {
        return totalSpeedFormatted;
    }

    public void setTotalSpeedFormatted(String totalSpeedFormatted) {
        this.totalSpeedFormatted = totalSpeedFormatted;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "NetworkSpeedReport{" +
                "uploadSpeed=" + uploadSpeed +
                ", downloadSpeed=" + downloadSpeed +
                ", totalSpeed=" + totalSpeed +
                ", uploadSpeedFormatted='" + uploadSpeedFormatted + '\'' +
                ", downloadSpeedFormatted='" + downloadSpeedFormatted + '\'' +
                ", totalSpeedFormatted='" + totalSpeedFormatted + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}