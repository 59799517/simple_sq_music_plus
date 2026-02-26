package com.sqmusicplus.v3.utils;

import cn.hutool.core.img.ImgUtil;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.config.exception.SQException;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.flac.FlacFileWriter;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.mp3.MP3FileWriter;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.OggFileWriter;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.datatype.Artwork;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @Classname MusicUtils
 * @Description 音乐工具类
 * @Version 1.0.0
 * @Date 2022/6/1 15:43
 * @Created by SQ
 */

@Slf4j
public class MusicUtils {

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

    public static  synchronized MultimediaInfo setMediaFileInfo(File file, String title, String album, String artists, String comment, String lyrics, File image,String mainArtist) throws TagException, CannotReadException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
        try {
            if (StringUtils.isBlank(mainArtist)){
                mainArtist="群星";
            }
            AudioFile af =null;
            String s = FileTypeUtils.checkType(file);
            if (s.contains("flac")){
                FlacFileReader flacFileReader = new FlacFileReader();
                af = flacFileReader.read(file);
            }else if (s.contains("wma")||s.contains("wav")||s.contains("ape")){
                return null;
            }else if (s.contains("ogg")){
                OggFileReader oggFileReader = new OggFileReader();
                af = oggFileReader.read(file);

            }else {
                MP3FileReader mp3FileReader = new MP3FileReader();
                af = mp3FileReader.read(file);
            }

//            AudioFile af = AudioFileIO.read(file);
            Tag tag = af.getTag();
//            if (tag instanceof ID3v1Tag) {
//                tag = new ID3v24Tag();
//            }
            if (image != null && image.exists()) {
                try {
                    BufferedImage bufferedImage = ImageIOUtils.read(image);
                    if (bufferedImage == null){
                        return null;
                    }
                    ImgUtil.write(bufferedImage, image);
                    Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                    tag.setField(firstArtwork);
                } catch (Exception e) {
                    try {
                        Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                        tag.setField(firstArtwork);
                    }  catch (Exception fex) {
                        BufferedImage bufferedImage = ImageIOUtils.read(image);
                        if (bufferedImage == null){
                            return null;
                        }
                        ImgUtil.write(bufferedImage, image);
                        Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                        tag.setField(firstArtwork);
                    }catch (Error exc) {
                        System.out.println("Caught an error: " + e.getMessage());
                    }
                }
            }
            tag.setField(FieldKey.TITLE, title.trim());
            tag.setField(FieldKey.ALBUM, album.trim());
            tag.setField(FieldKey.ARTIST, artists.trim());
            tag.setField(FieldKey.COMMENT, comment.trim());
            tag.setField(FieldKey.ALBUM_ARTIST, mainArtist.trim());
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
                flacFileWriter.write(af);
            }else if (s.contains("ogg")){
                OggFileWriter oggFileWriter = new OggFileWriter();
                oggFileWriter.write(af);

            }else {
                MP3FileWriter mp3FileWriter = new MP3FileWriter();
                mp3FileWriter.write(af);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取插件
     * @param plugName 插件名称
     * @return
     */
    public static SearchHanderAbstract getPlugHander(String plugName,List<SearchHanderAbstract> searchHanderAbstractList){
        SearchHanderAbstract searchHanderAbstract = null;
        for (SearchHanderAbstract item : searchHanderAbstractList) {
            if(item.getPlugName().equals(plugName)){
                searchHanderAbstract = item;
            }
        }
        if (searchHanderAbstract==null){
            throw  new SQException("未知的搜索类型");
        }
        return searchHanderAbstract;
    }


    /**
     * 找到最大的bit
     * @param brTypes
     * @return
     */
    public static PlugBrType getMaxBr(List<PlugBrType> brTypes) {
        if (brTypes == null || brTypes.isEmpty()) {
            return null;
        }
        return brTypes.stream().max(Comparator.comparing(PlugBrType::getBit)).get();
    }

}
