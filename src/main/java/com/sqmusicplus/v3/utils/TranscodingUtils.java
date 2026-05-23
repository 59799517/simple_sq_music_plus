package com.sqmusicplus.v3.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname TranscodingUtils
 * @Description 转码工具类
 * @Version 1.0.0
 * @Date 2022/6/13 14:39
 * @Created by SQ
 */
@Slf4j
public class TranscodingUtils {




    public static boolean  transcoding(File source,File target,String targetType,Integer bit ,Integer channels,Integer samplingRate)  {
        // 只支持 flac 和 mp3 格式
        if (!targetType.equals("flac") && !targetType.equals("mp3")) {
            log.error("不支持的目标格式: {}", targetType);
            return false;
        }
        
        // 检查源文件是否存在
        if (!source.isFile() || !source.exists()) {
            log.error("源文件不存在: {}", source.getAbsolutePath());
            return false;
        }
        
        log.info("========== 开始转码 ==========");
        log.info("源文件: {}", source.getAbsolutePath());
        log.info("目标文件: {}", target.getAbsolutePath());
        log.info("目标格式: {}", targetType);
        log.info("采样率: {} Hz", samplingRate);
        log.info("声道数: {}", channels);
        if (bit != null && bit > 0) {
            log.info("比特率: {} bps", bit);
        }
        
        AudioAttributes audio = new AudioAttributes();
        if (targetType.equals("flac")) {
            audio.setCodec("flac");
        } else if (targetType.equals("mp3")) {
            audio.setCodec("libmp3lame");
            audio.setBitRate(bit);
        }
        audio.setChannels(channels);
        audio.setSamplingRate(samplingRate);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat(targetType);
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
            log.info("✓ 转码成功");
            log.info("文件大小: {} bytes", target.length());
            log.info("========== 转码完成 ==========\n");
            return true;
        } catch (EncoderException e) {
            log.error("✗ 转码失败: {}", e.getMessage(), e);
            log.error("提示: 请检查以下几点:");
            log.error("  1. FFmpeg 是否正确安装并可用");
            log.error("  2. 源文件格式是否有效");
            log.error("  3. 目标路径是否有写入权限");
            log.error("  4. 磁盘空间是否充足");
            return false;
        }
    }
    public static boolean transcoding(File source,File target,String targetType,Integer bit ){
      return  transcoding(source,target,targetType,bit,2,44100);
    }
    public static List<File> AutoTranscoding(File source){
        ArrayList<File> files = new ArrayList<>();

        File parentFile = source.getParentFile();
        File flac2000 = new File(parentFile, IdUtil.fastSimpleUUID()+".flac");
        File mp3320 = new File(parentFile, IdUtil.fastSimpleUUID()+" - 320"+".mp3");
        File mp3192 = new File(parentFile, IdUtil.fastSimpleUUID()+" - 192"+".mp3");
        File mp3128 = new File(parentFile, IdUtil.fastSimpleUUID()+" - 128"+".mp3");

        String suffix = FileUtil.getSuffix(source);
        if (suffix.equals("flac")) {
            if (transcodingFlacToMp3(source,mp3320)){
                files.add(mp3320);
            }
            if (transcodingMp3ToMp3(source,mp3192,192000)){
                files.add(mp3192);
            }
            if (transcodingMp3ToMp3(source,mp3128,128000)){
                files.add(mp3128);
            }
        } else if (suffix.equals("ape")) {
            if (transcodingApeToFlac(source,flac2000)){
                files.add(flac2000);
            }
            if(transcodingApeToMp3(source,mp3320)){
                files.add(mp3320);
            }
            if (transcodingMp3ToMp3(source,mp3192,192000)){
                files.add(mp3192);
            }
            if (transcodingMp3ToMp3(source,mp3128,128000)){
                files.add(mp3128);
            }
        }else{
//            if (transcodingMp3ToMp3(source,mp3320,320000)){
//                files.add(mp3320);
//            }
            if (transcodingMp3ToMp3(source,mp3192,192000)){
                files.add(mp3192);
            }
            if (transcodingMp3ToMp3(source,mp3128,128000)){
                files.add(mp3128);
            }
        }
        return files;
    }

    public static boolean transcodingApeToFlac(File source,File target){
        return  transcoding(source,target,"flac",-1,2,44100);
    }
    public static boolean transcodingFlacToMp3(File source,File target){
        return  transcoding(source,target,"mp3",320000,2,44100);
    }
    public static boolean transcodingApeToMp3(File source,File target){
        return  transcodingFlacToMp3(source,target);
    }
    public static boolean transcodingMp3ToMp3(File source,File target,Integer bit){
        return  transcoding(source,target,"mp3",bit,2,44100);
    }

    /**
     * M4A 转 FLAC
     * @param source 源文件（M4A）
     * @param target 目标文件（FLAC）
     * @return 是否成功
     */
    public static boolean transcodingM4aToFlac(File source, File target) {
        return transcoding(source, target, "flac", -1, 2, 44100);
    }

    /**
     * M4A 转 FLAC（自定义采样率）
     * @param source 源文件（M4A）
     * @param target 目标文件（FLAC）
     * @param samplingRate 采样率（如 44100, 48000, 96000）
     * @return 是否成功
     */
    public static boolean transcodingM4aToFlac(File source, File target, Integer samplingRate) {
        return transcoding(source, target, "flac", -1, 2, samplingRate);
    }

    /**
     * M4A 转 MP3
     * @param source 源文件（M4A）
     * @param target 目标文件（MP3）
     * @param bitRate 比特率（如 320000, 192000, 128000）
     * @return 是否成功
     */
    public static boolean transcodingM4aToMp3(File source, File target, Integer bitRate) {
        return transcoding(source, target, "mp3", bitRate, 2, 44100);
    }

    /**
     * 使用系统 FFmpeg 进行转码（更可靠）
     * @param source 源文件
     * @param target 目标文件
     * @param targetType 目标格式（flac 或 mp3）
     * @param bitRate 比特率（仅 MP3 需要）
     * @param samplingRate 采样率
     * @param channels 声道数
     * @return 是否成功
     */
    public static boolean transcodingWithSystemFfmpeg(File source, File target, String targetType, 
                                                      Integer bitRate, Integer samplingRate, Integer channels) {
        return transcodingWithSystemFfmpegWithPath(source, target, targetType, bitRate, samplingRate, channels, "ffmpeg");
    }

    /**
     * 使用指定路径的 FFmpeg 进行转码
     * @param source 源文件
     * @param target 目标文件
     * @param targetType 目标格式（flac 或 mp3）
     * @param bitRate 比特率（仅 MP3 需要）
     * @param samplingRate 采样率
     * @param channels 声道数
     * @param ffmpegPath FFmpeg 可执行文件路径（如 "C:\\ffmpeg\\bin\\ffmpeg.exe" 或 "ffmpeg"）
     * @return 是否成功
     */
    public static boolean transcodingWithSystemFfmpegWithPath(File source, File target, String targetType,
                                                               Integer bitRate, Integer samplingRate, Integer channels,
                                                               String ffmpegPath) {
        if (!targetType.equals("flac") && !targetType.equals("mp3")) {
            log.error("不支持的目标格式: {}", targetType);
            return false;
        }
        
        if (!source.isFile() || !source.exists()) {
            log.error("源文件不存在: {}", source.getAbsolutePath());
            return false;
        }
        
        log.info("========== 使用系统 FFmpeg 转码 ==========");
        log.info("FFmpeg 路径: {}", ffmpegPath);
        log.info("源文件: {}", source.getAbsolutePath());
        log.info("目标文件: {}", target.getAbsolutePath());
        
        try {
            // 构建 FFmpeg 命令
            List<String> command = new ArrayList<>();
            command.add(ffmpegPath);
            command.add("-i");
            command.add(source.getAbsolutePath());
            command.add("-y"); // 覆盖输出文件
            
            if (targetType.equals("flac")) {
                command.add("-acodec");
                command.add("flac");
            } else if (targetType.equals("mp3")) {
                command.add("-acodec");
                command.add("libmp3lame");
                if (bitRate != null && bitRate > 0) {
                    command.add("-b:a");
                    command.add(String.valueOf(bitRate));
                }
            }
            
            if (samplingRate != null) {
                command.add("-ar");
                command.add(String.valueOf(samplingRate));
            }
            
            if (channels != null) {
                command.add("-ac");
                command.add(String.valueOf(channels));
            }
            
            command.add(target.getAbsolutePath());
            
            // 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // 合并错误流和标准输出
            
            Process process = processBuilder.start();
            
            // 读取输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("FFmpeg: {}", line);
            }
            reader.close();
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0 && target.exists() && target.length() > 0) {
                log.info("✓ 转码成功");
                log.info("文件大小: {} bytes", target.length());
                log.info("========== 转码完成 ==========\n");
                return true;
            } else {
                log.error("✗ 转码失败，退出代码: {}", exitCode);
                return false;
            }
            
        } catch (Exception e) {
            log.error("✗ 转码异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * M4A 转 FLAC（使用系统 FFmpeg）
     */
    public static boolean transcodingM4aToFlacWithSystemFfmpeg(File source, File target) {
        return transcodingWithSystemFfmpeg(source, target, "flac", null, 44100, 2);
    }

    /**
     * M4A 转 FLAC（使用 JAVE 内置 FFmpeg）
     * 这个方法会自动使用 JAVE 库自带的 FFmpeg，无需安装系统 FFmpeg
     */
    public static boolean transcodingM4aToFlacWithJaveFfmpeg(File source, File target) {
        try {
            // 获取 JAVE 内置的 FFmpeg 路径
            Class<?> locatorClass = Class.forName("ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator");
            Object locator = locatorClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method getFFMPEGPathMethod = locatorClass.getMethod("getFFMPEGPath");
            String ffmpegPath = (String) getFFMPEGPathMethod.invoke(locator);
            
            if (ffmpegPath != null && !ffmpegPath.isEmpty()) {
                log.info("使用 JAVE 内置 FFmpeg: {}", ffmpegPath);
                return transcodingWithSystemFfmpegWithPath(source, target, "flac", null, 44100, 2, ffmpegPath);
            } else {
                log.error("无法获取 JAVE 内置 FFmpeg 路径");
                return false;
            }
        } catch (Exception e) {
            log.error("获取 JAVE FFmpeg 失败: {}", e.getMessage(), e);
            return false;
        }
    }

}
