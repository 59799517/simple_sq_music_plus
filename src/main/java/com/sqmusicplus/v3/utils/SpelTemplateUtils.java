package com.sqmusicplus.v3.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模板字符串工具类 — 使用纯文本替换处理 ${...} 占位符
 */
public class SpelTemplateUtils {
    
    /**
     * 使用纯文本替换处理模板字符串（${...}格式），
     * 替代基于SpEL的旧实现以消除表达式注入风险
     * @param template 模板字符串，如 "${artists}/${album}/${musicName} - ${artists}"
     * @param params 参数映射
     * @return 替换后的字符串
     */
    public static String formatTemplateWithDollar(String template, Map<String, Object> params) {
        if (template == null || template.isEmpty() || params == null || params.isEmpty()) {
            return template;
        }
        
        String result = template;
        // 按key长度降序排列，避免短key（如"artist"）错误替换长key（如"artists"）中的子串
        List<Map.Entry<String, Object>> entries = new ArrayList<>(params.entrySet());
        entries.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));
        
        for (Map.Entry<String, Object> entry : entries) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? String.valueOf(entry.getValue()) : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
}