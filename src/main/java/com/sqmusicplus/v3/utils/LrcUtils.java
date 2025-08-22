package com.sqmusicplus.v3.utils;


import cn.hutool.core.util.ReUtil;
import com.sqmusicplus.v3.plug.kw.entity.MusicInfoResult;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname LrcUtils
 * @Description 歌词工具类
 * @Version 1.0.0
 * @Date 2022/5/31 9:42
 * @Created by SQ
 */

public class LrcUtils {


    public static final String PAIR_FMT = "<%s,%s>";



    /**
     * 酷我歌词转为lrc歌词
     * @param krcList 酷我歌词
     * @param album 专辑名称
     * @param artist 歌手名称
     * @param songName 歌曲名称
     * @return lrc歌词
     */
   public static String krcTolrc (List<MusicInfoResult.DataDTO.LrclistDTO> krcList, String album, String artist, String songName){
//        String lineLyric = lrclistDTO.getLineLyric();
//        String time = lrclistDTO.getTime();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[ti:"+songName+"]\n" +
            "[ar:"+artist+"]\n" +
            "[al:"+album+"]\n" +
            "[by: SqMusic-kw]\n" +
            "[offset:0]\n");
        krcList.forEach(e->{
            String time = e.getTime();
            String s = KugoulyricTimeToLrcTimeStr(time);
            stringBuffer.append("["+s+"]"+e.getLineLyric()+"\n");
        });
      return stringBuffer.toString();
    }

    /**
     * 酷我歌词时间转换
     * @param time 酷我歌词的时间戳
     * @return lrc 时间戳
     */
    public static String KugoulyricTimeToLrcTimeStr(String time){
        String[] split = time.split("\\.");
        int seconds =0 ;
        try {
            seconds=Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            seconds=0;
        }
        int microseconds =0 ;
        try {
            microseconds=Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            microseconds=0;
        }
        //分
        int minute =0;
        try {
            minute=(seconds/60);
        } catch (Exception e) {
            minute=0;
        }
        //秒
        seconds=seconds%60;
        //微秒
        int  milliseconds =0;
        //毫秒
        microseconds=Integer.parseInt(split[1]);
        milliseconds = (microseconds/1000);
//        microseconds=microseconds%1000;
        return String.format("%02d:%02d.%03d",minute,seconds,milliseconds);

    }

    public static  String mgLrcTolrc(String lrc){
        //歌曲信息
        String[] split = lrc.split("\r\n");
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("[ti:"+split[1]+"]\n" +
                    "[by: SqMusic-mg]\n" +
                    "[offset:0]\n");
        } catch (Exception e) {
        }
        for (int i = 4; i < split.length; i++) {
            stringBuffer.append(split[i]);
        }
        return stringBuffer.toString();


    }


    /**
     * 解析酷我的偏移值
     * @param lrc
     * @return
     */
    public static String parseKuwoLyricOffset(String lrc){
        // 解析酷我的偏移值
        int offset = 1, offset2 = 1;
        String kuwoValStr = ReUtil.getGroup1("\\[kuwo:(\\d+)\\]", lrc);
        if (StringUtils.isNotBlank(kuwoValStr)) {
            int kuwoVal = Integer.parseInt(kuwoValStr, 8);
            offset = kuwoVal / 10;
            offset2 = kuwoVal % 10;
        }
        // 解析逐字歌词
        String lineTimeExp = "\\[\\d+:\\d+(?:[.:]\\d+)?\\]";
        String[] lsp = lrc.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String l : lsp) {
            List<String> s1List = ReUtil.findAllGroup1("<(\\d+),-?\\d+>", l);
            if (s1List.isEmpty()) sb.append(l);
            else {
                List<String> s2List = ReUtil.findAllGroup1("<\\d+,(-?\\d+)>", l);
                // 行时间
                String lineTimeStr = ReUtil.getGroup0(lineTimeExp, l);
                sb.append(lineTimeStr);
                String[] sp = removeFirstEmpty(l.replaceFirst(lineTimeExp, "").split("<\\d+,-?\\d+>", -1));
                for (int k = 0, s = s1List.size(); k < s; k++) {
                    int n1 = Integer.parseInt(s1List.get(k));
                    int n2 = Integer.parseInt(s2List.get(k));
                    int wordStartTime = Math.abs((n1 + n2) / (offset * 2));
                    int wordDuration = Math.abs((n1 - n2) / (offset2 * 2));
                    sb.append(String.format(PAIR_FMT, wordStartTime, wordDuration));
                    sb.append(sp[k]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String  splitLyrics(String lrc){
        String lineTimeExp = "\\[\\d+:\\d+(?:[.:]\\d+)?\\]";
        // 分离歌词和翻译
        String[] sp = lrc.split("\n");
        StringBuilder sb = new StringBuilder();
        boolean hasTrans = false;
        int s = sp.length;
        for (int j = 0; j < s; j++) {
            String sentence = sp[j], nextSentence = j + 1 < s ? sp[j + 1] : null;
            // 歌词中带有翻译时，最后一句是翻译直接跳过
            if (hasTrans && StringUtils.isEmpty(nextSentence)) break;
            String time = ReUtil.getGroup0(lineTimeExp, sentence);
            if (StringUtils.isEmpty(time)) {
                sb.append(sentence).append("\n");
                continue;
            }
            String nextTime = null;
            if (StringUtils.isNotEmpty(nextSentence)) nextTime = ReUtil.getGroup0(lineTimeExp, nextSentence);
            // 歌词中带有翻译，有多个 time 相同的歌词时取不重复的第二个
            if (!time.equals(nextTime)) sb.append(sentence).append("\n");
            else hasTrans = true;
        }
//    return sb.toString();
        return   sb.toString().replaceAll("<\\d+,-?\\d+>", "");
    }

    public static String  splitTranslation(String lrc){
        StringBuilder sb = new StringBuilder();
        boolean  hasTrans = false;

        String lineTimeExp = "\\[\\d+:\\d+(?:[.:]\\d+)?\\]";
        // 分离歌词和翻译
        String[] sp = lrc.split("\n");
        int s = sp.length;

        String lastTime = null;
        for (int i = 0; i < s; i++) {
            String sentence = sp[i], nextSentence = i + 1 < s ? sp[i + 1] : null;
            String time = ReUtil.getGroup0(lineTimeExp, sentence);
            if (StringUtils.isEmpty(time)) continue;
            String nextTime = null;
            if (StringUtils.isNotEmpty(nextSentence)) nextTime = ReUtil.getGroup0(lineTimeExp, nextSentence);
            // 歌词中带有翻译，有多个 time 相同的歌词时取重复的第一个；最后一句也是翻译
            if (hasTrans && nextTime == null || time.equals(nextTime)) {
                sb.append(lastTime);
                sb.append(sentence.replaceFirst(lineTimeExp, ""));
                sb.append("\n");
                hasTrans = true;
            }
            lastTime = time;
        }
        return   sb.toString().replaceAll("<\\d+,-?\\d+>", "");
    }





    /**
     * 返回子数组在原数组中的位置
     *
     * @param array
     * @param subArray
     * @return
     */
    public static int indexOf(byte[] array, byte[] subArray) {
        int[] next = calculateNext(subArray);
        int i = 0, j = 0, len = array.length, sLen = subArray.length;
        while (i < len && j < sLen) {
            if (array[i] == subArray[j]) {
                i++;
                j++;
            } else {
                if (j > 0) j = next[j - 1];
                else i++;
            }
        }
        if (j == sLen) return i - j;
        return -1;
    }
    private static int[] calculateNext(byte[] array) {
        int i = 1, j = 0, len = array.length;
        int[] next = new int[len];
        while (i < len) {
            if (array[i] == array[j]) {
                next[i] = j + 1;
                i++;
                j++;
            } else if (j > 0) {
                j = next[j - 1];
            } else {
                next[i] = 0;
                i++;
            }
        }
        return next;
    }

    /**
     * 删除字符串数组中第一个位置上的空值
     *
     * @param array
     * @return
     */
    public static String[] removeFirstEmpty(String[] array) {
        int len = array.length;
        if (len == 0 || StringUtils.isNotBlank(array[0])) return array;
        return Arrays.copyOfRange(array, 1, len);
    }


}
