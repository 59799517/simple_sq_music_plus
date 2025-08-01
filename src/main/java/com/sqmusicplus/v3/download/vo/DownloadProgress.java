package com.sqmusicplus.v3.download.vo;

/**
 * @Classname DownloadProgress
 * @Description 下载状态
 * @Version 1.0.0
 * @Date 2025/7/21 16:34
 * @Created by SQ
 */

public class DownloadProgress {
    private long bytesRead;
    private long totalBytes;

    public DownloadProgress(long bytesRead, long totalBytes) {
        this.bytesRead = bytesRead;
        this.totalBytes = totalBytes;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public int getProgress() {
        return totalBytes == 0 ? 0 : (int) ((bytesRead * 100) / totalBytes);
    }
}
