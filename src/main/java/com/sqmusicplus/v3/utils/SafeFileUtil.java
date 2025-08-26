package com.sqmusicplus.v3.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.concurrent.TimeUnit;

/**
 * 安全文件操作工具类
 * 提供带重试机制的文件删除功能，解决Windows平台下文件正在被占用导致的删除失败问题
 */
@Slf4j
public class SafeFileUtil {
    
    /**
     * 安全删除文件，包含重试机制
     * @param file 要删除的文件
     * @return 是否删除成功
     */
    public static boolean safeDelete(File file) {
        return safeDelete(file, 3, 1000);
    }
    
    /**
     * 安全删除文件，包含重试机制
     * @param file 要删除的文件
     * @param retryAttempts 重试次数
     * @param retryDelayMillis 重试间隔（毫秒）
     * @return 是否删除成功
     */
    public static boolean safeDelete(File file, int retryAttempts, long retryDelayMillis) {
        if (file == null || !file.exists()) {
            return true;
        }
        
        for (int i = 0; i <= retryAttempts; i++) {
            try {
                FileUtil.del(file);
                log.debug("文件删除成功: {}", file.getAbsolutePath());
                return true;
            } catch (IORuntimeException e) {
                if (e.getCause() instanceof FileSystemException) {
                    log.warn("文件删除失败，文件可能正在被占用: {}，尝试次数: {}/{}", 
                            file.getAbsolutePath(), i + 1, retryAttempts + 1);
                    
                    // 如果不是最后一次尝试，则等待后重试
                    if (i < retryAttempts) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(retryDelayMillis);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            log.warn("删除文件时线程被中断: {}", file.getAbsolutePath());
                            return false;
                        }
                    } else {
                        log.error("文件删除最终失败，已达到最大重试次数: {}", file.getAbsolutePath(), e);
                    }
                } else {
                    log.error("文件删除发生其他IO异常: {}", file.getAbsolutePath(), e);
                    return false;
                }
            } catch (Exception e) {
                log.error("文件删除发生未预期异常: {}", file.getAbsolutePath(), e);
                return false;
            }
        }
        return false;
    }
    
    /**
     * 安全删除文件或目录
     * @param path 文件或目录路径
     * @return 是否删除成功
     */
    public static boolean safeDelete(String path) {
        if (path == null || path.isEmpty()) {
            return true;
        }
        return safeDelete(new File(path));
    }
    
    /**
     * 安全删除文件或目录
     * @param path 文件或目录路径
     * @param retryAttempts 重试次数
     * @param retryDelayMillis 重试间隔（毫秒）
     * @return 是否删除成功
     */
    public static boolean safeDelete(String path, int retryAttempts, long retryDelayMillis) {
        if (path == null || path.isEmpty()) {
            return true;
        }
        return safeDelete(new File(path), retryAttempts, retryDelayMillis);
    }
}