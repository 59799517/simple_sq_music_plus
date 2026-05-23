package com.sqmusicplus;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.utils.AliyunDriveUtils;
import com.sqmusicplus.v3.utils.MusicUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TestPositionAndDistance {


    public static void main(String[] args) {
        // 1. 准备参数
        Map<String, Object> params = new HashMap<>();
        params.put("musicName", "双截棍");
        params.put("artists", "周杰伦");
        params.put("artist", "周杰伦");
        params.put("album", "范特西");
        params.put("albumId", "12345");

        // 2. 定义模板 (测试开头带斜杠的情况)
        String template = "/自定义文件1/${artists}/自定义文件夹2/${album}/${musicName}/自定义文件3/${musicName} - ${artists}";
        String fallback = "/${artists}/${album}/${musicName}";

        // 3. 调用工具类
        Map<String, String> result = MusicUtils.parse(
                template,
                fallback,
                params,
                File.separator
        );




        // 4. 输出结果
        System.out.println("模板："+template);
        System.out.println("歌手目录: " + result.get(MusicUtils.KEY_ARTIST_DIR)); // null (因为模板里没歌手变量)
        System.out.println("专辑目录: " + result.get(MusicUtils.KEY_ALBUM_DIR));   // 范特西
        System.out.println("歌曲目录: " + result.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");     // 范特西

        String template1 = "/${artists}/${album}/${musicName} - ${artists}";
        Map<String, String> result1 = MusicUtils.parse(
                template1,
                fallback,
                params,
                File.separator
        );
        System.out.println("-----------------------------");
        System.out.println("模板："+template1);
        System.out.println("歌手目录: " + result1.get(MusicUtils.KEY_ARTIST_DIR));
        System.out.println("专辑目录: " + result1.get(MusicUtils.KEY_ALBUM_DIR));
        System.out.println("歌曲目录: " + result1.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");

        String template2 = "/${album}/${musicName} - ${artists}";
        Map<String, String> result2 = MusicUtils.parse(
                template2,
                fallback,
                params,
                File.separator
        );
        System.out.println("-----------------------------");
        System.out.println("模板："+template2);
        System.out.println("歌手目录: " + result2.get(MusicUtils.KEY_ARTIST_DIR));
        System.out.println("专辑目录: " + result2.get(MusicUtils.KEY_ALBUM_DIR));
        System.out.println("歌曲目录: " + result2.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");
        String template3 = "/${musicName} - ${artists}";
        Map<String, String> result3 = MusicUtils.parse(
                template3,
                fallback,
                params,
                File.separator
        );
        System.out.println("-----------------------------");
        System.out.println("模板："+template3);
        System.out.println("歌手目录: " + result3.get(MusicUtils.KEY_ARTIST_DIR));
        System.out.println("专辑目录: " + result3.get(MusicUtils.KEY_ALBUM_DIR));
        System.out.println("歌曲目录: " + result3.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");
        String template4 = "${album}/${musicName} - ${artists}";
        Map<String, String> result4 = MusicUtils.parse(
                template4,
                fallback,
                params,
                File.separator
        );
        System.out.println("-----------------------------");
        System.out.println("模板："+template4);
        System.out.println("歌手目录: " + result4.get(MusicUtils.KEY_ARTIST_DIR));
        System.out.println("专辑目录: " + result4.get(MusicUtils.KEY_ALBUM_DIR));
        System.out.println("歌曲目录: " + result4.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");
        String template5 = "${artists}/${album}/${musicName} - ${artists}";
        Map<String, String> result5 = MusicUtils.parse(
                template5,
                fallback,
                params,
                File.separator
        );
        System.out.println("-----------------------------");
        System.out.println("模板："+template5);
        System.out.println("歌手目录: " + result5.get(MusicUtils.KEY_ARTIST_DIR));
        System.out.println("专辑目录: " + result5.get(MusicUtils.KEY_ALBUM_DIR));
        System.out.println("歌曲目录: " + result5.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");
        String template6 = "${artists}/${musicName} - ${artists}";
        Map<String, String> result6 = MusicUtils.parse(
                template6,
                fallback,
                params,
                File.separator
        );
        System.out.println("-----------------------------");
        System.out.println("模板："+template6);
        System.out.println("歌手目录: " + result6.get(MusicUtils.KEY_ARTIST_DIR));
        System.out.println("专辑目录: " + result6.get(MusicUtils.KEY_ALBUM_DIR));
        System.out.println("歌曲目录: " + result6.get(MusicUtils.KEY_SONG_DIR)+"\\双截棍 - 周杰伦.flac");




    }
}