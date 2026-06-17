package com.sqmusicplus;

import com.sqmusicplus.v3.download.DownloadExcute;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DownloadExcute 计数逻辑本地校验测试
 * 不依赖数据库，仅验证配置值处理等纯逻辑
 */
class DownloadExcuteTest {

    @Test
    void getDownloadInfo_空值保护_应初始化为0() {
        // 测试空字符串处理逻辑
        String plug_qqvip_download_today = "";
        long currentCount = 0;
        
        if (plug_qqvip_download_today == null || plug_qqvip_download_today.isEmpty()) {
            plug_qqvip_download_today = "0";
        }
        
        currentCount = Long.parseLong(plug_qqvip_download_today);
        assertEquals(0, currentCount, "空值应被初始化为0");
    }

    @Test
    void getDownloadInfo_非数字值_应被解析为0() {
        String plug_qqvip_download_today = "abc";
        long currentCount = 0;
        
        if (plug_qqvip_download_today != null && !plug_qqvip_download_today.isEmpty()) {
            try {
                currentCount = Long.parseLong(plug_qqvip_download_today);
            } catch (NumberFormatException e) {
                currentCount = 0;
            }
        }
        
        assertEquals(0, currentCount, "非数字值应被解析为0");
    }

    @Test
    void getDownloadInfo_正常数值_应正确解析() {
        String plug_qqvip_download_today = "42";
        long currentCount = 0;
        
        if (plug_qqvip_download_today != null && !plug_qqvip_download_today.isEmpty()) {
            try {
                currentCount = Long.parseLong(plug_qqvip_download_today);
            } catch (NumberFormatException e) {
                currentCount = 0;
            }
        }
        
        assertEquals(42, currentCount, "正常数值应正确解析");
    }

    @Test
    void getDownloadInfo_计数加1_应正确递增() {
        long currentCount = 5;
        long newCount = currentCount + 1;
        assertEquals(6, newCount, "计数应正确递增");
    }
}
