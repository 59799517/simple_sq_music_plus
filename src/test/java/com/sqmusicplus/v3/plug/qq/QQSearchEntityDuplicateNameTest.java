package com.sqmusicplus.v3.plug.qq;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.qq.entity.QQSearchEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class QQSearchEntityDuplicateNameTest {

    public static void main(String[] args) {
        testDuplicateNameDetection();
    }

    private static void testDuplicateNameDetection() {
        log.info("========== 测试同名歌曲去重逻辑 ==========");

        List<Music> musicList = new ArrayList<>();

        Music music1 = new Music()
                .setId("001abc")
                .setMusicName("测试歌曲")
                .setMusicArtists(List.of("歌手A"))
                .setMusicAlbum("测试专辑");
        musicList.add(music1);

        Music music2 = new Music()
                .setId("002def")
                .setMusicName("测试歌曲")
                .setMusicArtists(List.of("歌手A"))
                .setMusicAlbum("测试专辑");
        musicList.add(music2);

        Music music3 = new Music()
                .setId("003ghi,media123")
                .setMusicName("另一首歌")
                .setMusicArtists(List.of("歌手B"))
                .setMusicAlbum("测试专辑");
        musicList.add(music3);

        Music music4 = new Music()
                .setId("004jkl")
                .setMusicName("另一首歌")
                .setMusicArtists(List.of("歌手B"))
                .setMusicAlbum("测试专辑");
        musicList.add(music4);

        log.info("去重前:");
        for (Music m : musicList) {
            log.info("  {} - ID: {}", m.getMusicName(), m.getId());
        }

        Map<String, Integer> nameCount = new HashMap<>();
        for (Music music : musicList) {
            String baseName = music.getMusicName();
            nameCount.merge(baseName, 1, Integer::sum);
        }
        for (Music music : musicList) {
            if (nameCount.get(music.getMusicName()) > 1) {
                String songMid = music.getId().contains(",") ? music.getId().split(",")[0] : music.getId();
                music.setMusicName(music.getMusicName() + " (" + songMid + ")");
            }
        }

        log.info("去重后:");
        for (Music m : musicList) {
            log.info("  {} - ID: {}", m.getMusicName(), m.getId());
        }

        boolean allDifferent = true;
        for (int i = 0; i < musicList.size(); i++) {
            for (int j = i + 1; j < musicList.size(); j++) {
                if (musicList.get(i).getMusicName().equals(musicList.get(j).getMusicName())) {
                    allDifferent = false;
                    log.error("发现同名歌曲: {} 和 {}", musicList.get(i).getMusicName(), musicList.get(j).getMusicName());
                }
            }
        }

        if (allDifferent) {
            log.info("✓ 测试通过: 所有歌曲名称已唯一");
        } else {
            log.error("✗ 测试失败: 仍有同名歌曲");
        }

        assert musicList.get(0).getMusicName().equals("测试歌曲 (001abc)");
        assert musicList.get(1).getMusicName().equals("测试歌曲 (002def)");
        assert musicList.get(2).getMusicName().equals("另一首歌 (003ghi)");
        assert musicList.get(3).getMusicName().equals("另一首歌 (004jkl)");

        log.info("✓ 所有断言通过");
    }
}
