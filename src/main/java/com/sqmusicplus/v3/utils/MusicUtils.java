package com.sqmusicplus.v3.utils;

import cn.hutool.core.img.ImgUtil;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;

import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;

import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.flac.FlacFileWriter;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.mp3.MP3FileWriter;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.OggFileWriter;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Classname MusicUtils
 * @Description 音乐工具类
 * @Version 1.0.0
 * @Date 2022/6/1 15:43
 * @Created by SQ
 */

@Slf4j
public class MusicUtils {


    public static final String KEY_ARTIST_DIR = "artistDir"; // 歌手目录
    public static final String KEY_ALBUM_DIR = "albumDir";   // 专辑目录
    public static final String KEY_SONG_DIR = "songDir";     // 歌曲所在目录 (完整目录)

    /**
     * 获取文件内容
     *
     * @param file
     * @return
     */
    public static MultimediaInfo getMediaFileInfo(File file) {
        MultimediaObject multimediaObject = new MultimediaObject(file);
        try {
            return multimediaObject.getInfo();
        } catch (EncoderException e) {
            log.error("获取文件信息失败：{}", e.getMessage());
            return null;
        }
    }

    public static  synchronized MultimediaInfo setMediaFileInfo(File file, String title, String albumName, String artists, String comment, String lyrics, File image, String mainArtist, String  albumYear) {
        try {
            if (StringUtils.isBlank(mainArtist)){
                mainArtist="群星";
            }
            AudioFile af =null;
            String s = FileTypeUtils.checkType(file);
            log.debug("文件类型检测结果: {}, 文件大小: {} bytes", s, file.length());
            if (s == null) {
                log.warn("无法检测文件类型，跳过标签写入: {}", file.getAbsolutePath());
                return null;
            }
            if (s.contains("flac")){
                FlacFileReader flacFileReader = new FlacFileReader();
                log.debug("开始读取FLAC文件: {}", file.getName());
                af = flacFileReader.read(file);
                log.debug("FLAC文件读取成功: {}", file.getName());
            }else if (s.contains("wma")||s.contains("wav")||s.contains("ape")){
                log.debug("不支持写入标签的格式: {}，跳过", s);
                return null;
            }else if (s.contains("ogg")){
                OggFileReader oggFileReader = new OggFileReader();
                log.debug("开始读取OGG文件: {}", file.getName());
                af = oggFileReader.read(file);
                log.debug("OGG文件读取成功: {}", file.getName());

            }else {
                MP3FileReader mp3FileReader = new MP3FileReader();
                log.debug("开始读取MP3文件: {}", file.getName());
                af = mp3FileReader.read(file);
                log.debug("MP3文件读取成功: {}", file.getName());
            }

//            AudioFile af = AudioFileIO.read(file);
            Tag tag = af.getTag();
//            if (tag instanceof ID3v1Tag) {
//                tag = new ID3v24Tag();
//            }
            if (image != null && image.exists()) {
                trySetArtwork(image, tag);
            }
            tag.setField(FieldKey.TITLE, title.trim());
            tag.setField(FieldKey.ALBUM, albumName.trim());
            tag.setField(FieldKey.ARTIST, artists.trim());
            tag.setField(FieldKey.COMMENT, comment.trim());
            tag.setField(FieldKey.ALBUM_ARTIST, mainArtist.trim());
            tag.setField(FieldKey.YEAR, albumYear);
            if (StringUtils.isNotEmpty(lyrics)) {
                try {
                    tag.setField(FieldKey.LYRICS, lyrics);
                } catch (KeyNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (FieldDataInvalidException e) {
                    throw new RuntimeException(e);
                }
            }

            af.setTag(tag);
//            af.commit();
//            AudioFileIO.write(af);


            if (s.contains("flac")){
                FlacFileWriter flacFileWriter = new FlacFileWriter();
                long beforeSize = file.length();
                log.debug("开始写入FLAC标签: {}, 文件大小: {} bytes", file.getName(), beforeSize);
                flacFileWriter.write(af);
                // JAudiotagger写入FLAC时会：创建.tmp→写入→删除原文件→重命名.tmp
                // 如果重命名失败，原文件会丢失，这里做验证
                if (!file.exists()) {
                    log.error("FLAC标签写入后文件丢失! 原始大小: {} bytes, 路径: {}", beforeSize, file.getAbsolutePath());
                    recoverFromTempFile(file);
                    throw new IOException("标签写入后文件丢失: " + file.getAbsolutePath());
                }
                long afterSize = file.length();
                log.debug("FLAC标签写入完成: {}, 写入后大小: {} bytes, 文件存在: {}", file.getName(), afterSize, file.exists());
            }else if (s.contains("ogg")){
                OggFileWriter oggFileWriter = new OggFileWriter();
                log.debug("开始写入OGG标签: {}", file.getName());
                oggFileWriter.write(af);
                if (!file.exists()) {
                    log.error("OGG标签写入后文件丢失! 路径: {}", file.getAbsolutePath());
                    throw new IOException("标签写入后文件丢失: " + file.getAbsolutePath());
                }
                log.debug("OGG标签写入完成: {}", file.getName());

            }else {
                MP3FileWriter mp3FileWriter = new MP3FileWriter();
                log.debug("开始写入MP3标签: {}", file.getName());
                mp3FileWriter.write(af);
                if (!file.exists()) {
                    log.error("MP3标签写入后文件丢失! 路径: {}", file.getAbsolutePath());
                    throw new IOException("标签写入后文件丢失: " + file.getAbsolutePath());
                }
                log.debug("MP3标签写入完成: {}", file.getName());
            }
            return null;
        } catch (CannotReadException e) {
            log.error("读取音频文件失败（文件可能已损坏或为空）: {} - {}", file.getName(), e.getMessage());
            throw new RuntimeException("读取音频文件失败: " + file.getAbsolutePath() + " - " + e.getMessage(), e);
        } catch (CannotWriteException e) {
            log.error("写入音频标签失败（JAudiotagger写入过程异常，可能导致文件丢失）: {} - {}", file.getName(), e.getMessage());
            recoverFromTempFile(file);
            throw new RuntimeException("写入音频标签失败: " + file.getAbsolutePath() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("设置音频标签失败: {} - {}", file.getName(), e.getMessage());
            throw new RuntimeException("设置音频标签失败: " + file.getAbsolutePath(), e);
        }
    }


    /**
     * 获取插件
     * @param plugName 插件名称
     * @return
     */
    public static SearchHanderAbstract getPlugHander(String plugName,List<SearchHanderAbstract> searchHanderAbstractList){
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(plugName)){
                return item;
            }
        }
        throw  new SQException("未知的搜索类型");
    }


    /**
     * 找到最大的bit
     * @param brTypes
     * @return
     */
    public static PlugBrType getMaxBr(List<PlugBrType> brTypes) {
        return brTypes.stream().max(Comparator.comparing(PlugBrType::getBit))
                .orElseThrow(() -> new SQException("brTypes 列表为空，无法获取最大比特率"));
    }


    // 变量分类配置
    private static final Set<String> ARTIST_VARS = Set.of("artists", "artist", "artistsId");
    private static final Set<String> ALBUM_VARS = Set.of("album", "albumId");
// ================== 公共入口方法 ==================

    /**
     * 解析路径模板
     *
     * @param template         主路径模板 (例如: "${artists}/${album}/${musicName}")
     * @param fallbackTemplate 备用模板 (主模板渲染失败时使用，可为 null)
     * @param pathTemplate     模板参数 Map (用于渲染)
     * @param separator        文件分隔符 (建议传入 File.separator)
     * @return 包含 artistDir, albumDir, songDir 的 HashMap，未找到则值为 null
     */
    // ================== 公共入口方法 ==================

    public static Map<String, String> parse(String template, String fallbackTemplate,
                                            Map<String, Object> pathTemplate, String separator) {

        Map<String, String> result = new HashMap<>();

        if (template == null || template.isEmpty()) return result;

        String sep = (separator == null) ? File.separator : separator;

        // 1. 归一化分隔符
        String activeTemplate = normalizeSeparator(template, sep);
        String safeFallback = (fallbackTemplate != null) ? normalizeSeparator(fallbackTemplate, sep) : null;

        // 2. 选择可用模板
        String finalTemplate = selectWorkingTemplate(activeTemplate, safeFallback, pathTemplate);
        if (finalTemplate == null) return result;

        // 3. 提取目录部分
        String dirTemplate = extractDirectoryPart(finalTemplate, sep);
        if (dirTemplate.isEmpty()) return result;

        // 4. 拆分路径段
        List<String> segments = splitAndClean(dirTemplate, sep);
        if (segments.isEmpty()) return result;

        // ================== 核心修复逻辑：范围截取 ==================

        // 5. 找到关键变量最后一次出现的索引
        int lastArtistIdx = -1;
        int lastAlbumIdx = -1;

        for (int i = 0; i < segments.size(); i++) {
            String seg = segments.get(i);
            if (containsAnyVariable(seg, ARTIST_VARS)) {
                lastArtistIdx = i; // 更新为当前索引，保证是最后一次出现
            }
            if (containsAnyVariable(seg, ALBUM_VARS)) {
                lastAlbumIdx = i; // 更新为当前索引
            }
        }

        // 6. 根据索引范围截取
        // 歌手目录：从 0 到 最后一个歌手变量 (包含)
        List<String> artistSegs = subListSafe(segments, 0, lastArtistIdx);

        // 专辑目录：从 0 到 最后一个专辑变量 (包含)
        List<String> albumSegs = subListSafe(segments, 0, lastAlbumIdx);

        // 7. 渲染
        result.put(KEY_ARTIST_DIR, renderSegments(artistSegs, pathTemplate, sep));
        result.put(KEY_ALBUM_DIR, renderSegments(albumSegs, pathTemplate, sep));
        result.put(KEY_SONG_DIR, renderSegments(segments, pathTemplate, sep));

        return result;
    }

    // ================== 私有辅助方法 ==================

    /**
     * 安全截取 List：如果 endIdx < 0，返回空 list
     */
    private static List<String> subListSafe(List<String> list, int start, int endIdx) {
        if (endIdx < 0) return Collections.emptyList();
        // subList 是 [start, end)，所以要 +1
        int end = Math.min(endIdx + 1, list.size());
        return new ArrayList<>(list.subList(start, end));
    }

    private static String normalizeSeparator(String path, String sep) {
        return path.replaceAll("[/\\\\]", Matcher.quoteReplacement(sep));
    }

    private static String selectWorkingTemplate(String main, String fallback, Map<String, Object> ctx) {
        if (tryRender(main, ctx)) return main;
        if (fallback != null && tryRender(fallback, ctx)) return fallback;
        return null;
    }

    private static boolean tryRender(String tpl, Map<String, Object> ctx) {
        try {
            SpelTemplateUtils.formatTemplateWithDollar(tpl, ctx);
            return true;
        } catch (Exception e) {
            log.debug("模板渲染失败: {} - {}", tpl, e.getMessage());
            return false;
        }
    }

    private static String extractDirectoryPart(String template, String sep) {
        int lastIdx = template.lastIndexOf(sep);
        return (lastIdx != -1) ? template.substring(0, lastIdx) : "";
    }

    // 缓存 Pattern 避免每次 splitAndClean 调用时重新编译
    private static final Map<String, Pattern> SPLIT_PATTERN_CACHE = new HashMap<>();

    private static List<String> splitAndClean(String dirTemplate, String sep) {
        String quoted = Pattern.quote(sep);
        Pattern pattern = SPLIT_PATTERN_CACHE.get(quoted);
        if (pattern == null) {
            pattern = Pattern.compile(quoted);
            SPLIT_PATTERN_CACHE.put(quoted, pattern);
        }
        return Arrays.stream(pattern.split(dirTemplate))
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
    }

    private static boolean containsAnyVariable(String segment, Set<String> keys) {
        for (String key : keys) {
            if (segment.contains("${" + key + "}")) return true;
        }
        return false;
    }

    private static String renderSegments(List<String> segments, Map<String, Object> ctx, String sep) {
        if (segments == null || segments.isEmpty()) return null;
        String tpl = String.join(sep, segments);
        try {
            String res = SpelTemplateUtils.formatTemplateWithDollar(tpl, ctx);
            return (res == null || res.trim().isEmpty()) ? null : res;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 尝试设置封面图片，优雅处理各种异常，不影响标签文字写入
     */
    private static void trySetArtwork(File image, Tag tag) {
        try {
            BufferedImage bufferedImage = ImageIOUtils.read(image);
            if (bufferedImage == null) {
                log.warn("图片不可读，尝试直接设置封面: {}", image.getAbsolutePath());
            } else {
                ImgUtil.write(bufferedImage, image);
            }
            Artwork artwork = ArtworkFactory.createArtworkFromFile(image);
            tag.setField(artwork);
        } catch (Exception e) {
            log.warn("设置封面图片失败，跳过封面（不影响标签文字）: {} - {}", image.getName(), e.getMessage());
        }
    }

    /**
     * JAudiotagger 写入失败时可能已删除原文件并在原地留下 .tmp 文件，尝试恢复
     * @return 是否成功恢复
     */
    private static boolean recoverFromTempFile(File file) {
        File tmpFile = new File(file.getAbsolutePath() + ".tmp");
        if (!file.exists() && tmpFile.exists()) {
            log.warn("检测到.tmp残留文件，尝试恢复: {} (大小: {} bytes)", tmpFile.getAbsolutePath(), tmpFile.length());
            if (tmpFile.renameTo(file)) {
                log.info("文件已从.tmp恢复: {}", file.getAbsolutePath());
                return true;
            }
        }
        return false;
    }
}


