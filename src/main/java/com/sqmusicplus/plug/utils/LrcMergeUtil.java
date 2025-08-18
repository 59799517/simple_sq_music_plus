package com.sqmusicplus.plug.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LRC歌词合并工具类
 * 支持Merge、Intersect和Union三种合并类型
 */
public class LrcMergeUtil {

    public enum MergeType {
        MERGE,      // 合并同一时间戳的原文和翻译到一行
        INTERSECT,  // 交错显示原文和翻译（时间戳连续）
        UNION       // 保持原文和翻译各自独立的时间轴
    }

    /**
     * 合并LRC歌词
     * @param originalLyrics 原文歌词
     * @param translatedLyrics 翻译歌词
     * @param mergeType 合并类型
     * @return 合并后的歌词
     */
    public static String mergeLyrics(String originalLyrics, String translatedLyrics, MergeType mergeType) {
        if (originalLyrics == null && translatedLyrics == null) {
            return "";
        }
        if (originalLyrics == null) {
            return translatedLyrics;
        }
        if (translatedLyrics == null) {
            return originalLyrics;
        }

        switch (mergeType) {
            case MERGE:
                return mergeLyricsInOneLine(originalLyrics, translatedLyrics);
            case INTERSECT:
                return mergeLyricsIntersect(originalLyrics, translatedLyrics);
            case UNION:
                return mergeLyricsUnion(originalLyrics, translatedLyrics);
            default:
                return mergeLyricsInOneLine(originalLyrics, translatedLyrics);
        }
    }

    /**
     * Merge类型合并：将原文和翻译合并到同一行，翻译用[]包裹
     * 格式：[00:00.00]This is the original text. [这是中文翻译].
     */
    private static String mergeLyricsInOneLine(String originalLyrics, String translatedLyrics) {
        // 解析原文和翻译歌词
        Map<String, String> originalMap = parseLyrics(originalLyrics);
        Map<String, String> translatedMap = parseLyrics(translatedLyrics);

        // 合并歌词
        StringBuilder mergedLyrics = new StringBuilder();

        // 添加无时间戳的元信息行
        addMetadataLines(originalLyrics, mergedLyrics);
        addMetadataLines(translatedLyrics, mergedLyrics);

        // 获取所有时间戳并排序
        Set<String> allTimes = new TreeSet<>(new TimeComparator());
        allTimes.addAll(originalMap.keySet());
        allTimes.addAll(translatedMap.keySet());

        // 合并相同时间戳的歌词
        for (String time : allTimes) {
            String originalLine = originalMap.get(time);
            String translatedLine = translatedMap.get(time);

            mergedLyrics.append("[").append(time).append("]");
            if (originalLine != null && !originalLine.trim().isEmpty()) {
                mergedLyrics.append(originalLine);
                // 只有当翻译内容存在且不为空时才添加方括号包裹
                if (translatedLine != null && !translatedLine.trim().isEmpty()) {
                    mergedLyrics.append(" [").append(translatedLine).append("]");
                }
            } else if (translatedLine != null && !translatedLine.trim().isEmpty()) {
                // 只有当只有翻译内容时，直接输出翻译内容，不加方括号
                mergedLyrics.append(translatedLine);
            }
            mergedLyrics.append("\n");
        }

        return mergedLyrics.toString();
    }

    /**
     * Intersect类型合并：交错显示原文和翻译
     * 格式：原文后紧跟翻译，翻译时间稍晚于原文
     */
    private static String mergeLyricsIntersect(String originalLyrics, String translatedLyrics) {
        // 解析原文和翻译歌词
        Map<String, String> originalMap = parseLyrics(originalLyrics);
        Map<String, String> translatedMap = parseLyrics(translatedLyrics);

        // 合并歌词
        StringBuilder mergedLyrics = new StringBuilder();

        // 添加无时间戳的元信息行
        addMetadataLines(originalLyrics, mergedLyrics);
        addMetadataLines(translatedLyrics, mergedLyrics);

        // 获取所有时间戳并排序
        List<TimeText> timeTextList = new ArrayList<>();

        // 添加原文
        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            timeTextList.add(new TimeText(entry.getKey(), entry.getValue(), true));
        }

        // 添加翻译（时间稍晚于对应的原文）
        for (Map.Entry<String, String> entry : translatedMap.entrySet()) {
            timeTextList.add(new TimeText(entry.getKey(), entry.getValue(), false));
        }

        // 按时间排序
        timeTextList.sort(Comparator.comparing(TimeText::getTime, new TimeComparator()));

        // 输出合并后的歌词
        for (TimeText timeText : timeTextList) {
            mergedLyrics.append("[").append(timeText.getTime()).append("]")
                        .append(timeText.getText()).append("\n");
        }

        return mergedLyrics.toString();
    }

    /**
     * Union类型合并：保持原文和翻译各自独立的时间轴
     * 格式：原文和翻译分别显示在各自的时间点上
     */
    private static String mergeLyricsUnion(String originalLyrics, String translatedLyrics) {
        // 解析原文和翻译歌词
        Map<String, String> originalMap = parseLyrics(originalLyrics);
        Map<String, String> translatedMap = parseLyrics(translatedLyrics);

        // 合并歌词
        StringBuilder mergedLyrics = new StringBuilder();

        // 添加无时间戳的元信息行
        addMetadataLines(originalLyrics, mergedLyrics);
        addMetadataLines(translatedLyrics, mergedLyrics);

        // 获取所有时间戳并排序
        Set<String> allTimes = new TreeSet<>(new TimeComparator());
        allTimes.addAll(originalMap.keySet());
        allTimes.addAll(translatedMap.keySet());

        // 按时间顺序输出歌词
        for (String time : allTimes) {
            String originalLine = originalMap.get(time);
            String translatedLine = translatedMap.get(time);

            if (originalLine != null) {
                mergedLyrics.append("[").append(time).append("]").append(originalLine).append("\n");
            }
            if (translatedLine != null) {
                mergedLyrics.append("[").append(time).append("]").append(translatedLine).append("\n");
            }
        }

        return mergedLyrics.toString();
    }

    /**
     * 解析歌词，提取时间戳和内容
     * @param lyrics 歌词文本
     * @return 时间戳到歌词内容的映射
     */
    private static Map<String, String> parseLyrics(String lyrics) {
        Map<String, String> lyricsMap = new LinkedHashMap<>();
        String[] lines = lyrics.split("\n");
        Pattern timePattern = Pattern.compile("\\[(\\d{1,2}:\\d{1,2}(?:\\.\\d{1,3})?)\\]");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            Matcher matcher = timePattern.matcher(line);
            if (matcher.find()) {
                String timeStr = matcher.group(1);
                String content = line.substring(matcher.end()).trim();
                lyricsMap.put(timeStr, content);
            }
        }

        return lyricsMap;
    }

    /**
     * 添加无时间戳的元信息行到结果中
     * @param lyrics 歌词文本
     * @param result 结果StringBuilder
     */
    private static void addMetadataLines(String lyrics, StringBuilder result) {
        String[] lines = lyrics.split("\n");
        Pattern timePattern = Pattern.compile("\\[(\\d{1,2}:\\d{1,2}(?:\\.\\d{1,3})?)\\]");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            // 如果不包含时间戳格式，则认为是元信息行
            if (!timePattern.matcher(line).find() && line.startsWith("[")) {
                result.append(line).append("\n");
            }
        }
    }

    /**
     * 时间戳比较器
     */
    private static class TimeComparator implements Comparator<String> {
        @Override
        public int compare(String time1, String time2) {
            // 将时间格式 mm:ss.SSS 转换为毫秒进行比较
            String[] parts1 = time1.split(":");
            String[] parts2 = time2.split(":");

            int minutes1 = Integer.parseInt(parts1[0]);
            int seconds1 = 0;
            int millis1 = 0;
            if (parts1.length > 1) {
                String[] secParts = parts1[1].split("\\.");
                seconds1 = Integer.parseInt(secParts[0]);
                if (secParts.length > 1) {
                    millis1 = Integer.parseInt(secParts[1]);
                }
            }

            int minutes2 = Integer.parseInt(parts2[0]);
            int seconds2 = 0;
            int millis2 = 0;
            if (parts2.length > 1) {
                String[] secParts = parts2[1].split("\\.");
                seconds2 = Integer.parseInt(secParts[0]);
                if (secParts.length > 1) {
                    millis2 = Integer.parseInt(secParts[1]);
                }
            }

            int totalMillis1 = minutes1 * 60000 + seconds1 * 1000 + millis1;
            int totalMillis2 = minutes2 * 60000 + seconds2 * 1000 + millis2;

            return Integer.compare(totalMillis1, totalMillis2);
        }
    }

    /**
     * 时间文本类，用于Intersect类型合并
     */
    private static class TimeText {
        private final String time;
        private final String text;
        private final boolean isOriginal;

        public TimeText(String time, String text, boolean isOriginal) {
            this.time = time;
            this.text = text;
            this.isOriginal = isOriginal;
        }

        public String getTime() {
            return time;
        }

        public String getText() {
            return text;
        }

        public boolean isOriginal() {
            return isOriginal;
        }
    }
}
