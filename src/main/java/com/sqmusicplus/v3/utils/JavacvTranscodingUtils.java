package com.sqmusicplus.v3.utils;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.File;

/**
 * @Classname JavacvTranscodingUtils
 * @Description 使用 JavaCV (FFmpeg) 进行音频转码
 * @Version 1.0.0
 * @Date 2026/5/8
 * @Created by SQ
 */
@Slf4j
public class JavacvTranscodingUtils {

    /**
     * M4A 转 FLAC
     * @param source 源文件（M4A）
     * @param target 目标文件（FLAC）
     * @return 是否成功
     */
    public static boolean transcodingM4aToFlac(File source, File target) {
        return transcodeAudio(source, target, "flac", 0, 44100, 2);
    }

    /**
     * M4A 转 FLAC（自定义采样率）
     * @param source 源文件（M4A）
     * @param target 目标文件（FLAC）
     * @param sampleRate 采样率（如 44100, 48000, 96000）
     * @return 是否成功
     */
    public static boolean transcodingM4aToFlac(File source, File target, int sampleRate) {
        return transcodeAudio(source, target, "flac", 0, sampleRate, 2);
    }

    /**
     * M4A 转 MP3
     * @param source 源文件（M4A）
     * @param target 目标文件（MP3）
     * @param bitRate 比特率（如 128000, 192000, 320000）
     * @return 是否成功
     */
    public static boolean transcodingM4aToMp3(File source, File target, int bitRate) {
        return transcodeAudio(source, target, "mp3", bitRate, 44100, 2);
    }

    /**
     * 通用音频转码方法
     * @param source 源文件
     * @param target 目标文件
     * @param format 目标格式（flac, mp3, wav 等）
     * @param bitRate 比特率（0 表示无损，如 FLAC）
     * @param sampleRate 采样率
     * @param channels 声道数
     * @return 是否成功
     */
    public static boolean transcodeAudio(File source, File target, String format,
                                         int bitRate, int sampleRate, int channels) {
        log.info("========== JavaCV FFmpeg 转码 ==========");
        log.info("源文件: {}", source.getAbsolutePath());
        log.info("目标文件: {}", target.getAbsolutePath());
        log.info("格式: {}, 比特率: {}, 采样率: {}, 声道: {}", format, bitRate, sampleRate, channels);

        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;

        try {
            // 1. 创建抓取器（读取源文件）
            grabber = new FFmpegFrameGrabber(source);
            grabber.start();

            // 2. 创建录制器（写入目标文件）
            recorder = new FFmpegFrameRecorder(target, channels);
            recorder.setFormat(format);
            recorder.setSampleRate(sampleRate);
            recorder.setAudioChannels(channels);

            // 设置编码器
            if ("flac".equalsIgnoreCase(format)) {
                recorder.setAudioCodec(avcodec.AV_CODEC_ID_FLAC);
                recorder.setAudioBitrate(0); // FLAC 无损
            } else if ("mp3".equalsIgnoreCase(format)) {
                recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
                recorder.setAudioBitrate(bitRate);
            } else {
                log.error("不支持的格式: {}", format);
                return false;
            }

            recorder.start();

            // 3. 逐帧转码
            Frame frame;
            int frameCount = 0;
            while ((frame = grabber.grabSamples()) != null) {
                recorder.record(frame);
                frameCount++;

                if (frameCount % 1000 == 0) {
                    log.debug("已处理 {} 帧", frameCount);
                }
            }

            log.info("✓ 转码成功，共处理 {} 帧", frameCount);
            log.info("文件大小: {} bytes", target.length());
            log.info("========== 转码完成 ==========\n");

            return true;

        } catch (Exception e) {
            log.error("✗ 转码失败: {}", e.getMessage(), e);
            return false;
        } finally {
            // 4. 释放资源
            try {
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                }
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (Exception e) {
                log.error("释放资源失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        log.info("JavaCV FFmpeg 版本信息:");
        log.info("FFmpeg 版本: {}", org.bytedeco.ffmpeg.global.avutil.av_version_info());

        // 示例：M4A 转 FLAC
        File source = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_337502043.m4a");
        File target = new File("D:\\code\\simple_sq_musuc_plus\\tidal_track_javacv.flac");

        if (source.exists()) {
            boolean success = transcodingM4aToFlac(source, target);
            log.info("转码结果: {}", success ? "成功" : "失败");
        } else {
            log.error("源文件不存在: {}", source.getAbsolutePath());
        }
    }
}
