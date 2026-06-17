package com.sqmusicplus.v3.plug.qq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateNameTest {

    static class SimpleMusic {
        String id;
        String musicName;

        SimpleMusic(String id, String musicName) {
            this.id = id;
            this.musicName = musicName;
        }

        void setMusicName(String name) { this.musicName = name; }
        String getMusicName() { return musicName; }
        String getId() { return id; }
    }

    public static void main(String[] args) {
        System.out.println("========== 测试1: 专辑内存在同名歌曲 ==========");
        testAlbumWithDuplicateNames();

        System.out.println();
        System.out.println("========== 测试2: 专辑内歌曲名各不相同 ==========");
        testAlbumWithUniqueNames();

        System.out.println();
        System.out.println("========== 测试3: 带逗号的ID（含media_mid） ==========");
        testWithMediaMidId();

        System.out.println();
        System.out.println("========== 测试4: 三首同名歌曲 ==========");
        testThreeDuplicateNames();

        System.out.println();
        System.out.println("所有测试通过!");
    }

    private static void testAlbumWithDuplicateNames() {
        List<SimpleMusic> musicList = new ArrayList<>();
        musicList.add(new SimpleMusic("001abc", "歌曲A"));
        musicList.add(new SimpleMusic("002def", "歌曲A"));
        musicList.add(new SimpleMusic("003ghi", "歌曲B"));

        System.out.println("去重前:");
        for (SimpleMusic m : musicList) {
            System.out.println("  名称: " + m.getMusicName() + "  ID: " + m.getId());
        }

        applyDuplicateNameFix(musicList);

        System.out.println("去重后:");
        for (SimpleMusic m : musicList) {
            System.out.println("  名称: " + m.getMusicName() + "  ID: " + m.getId());
        }

        assert musicList.get(0).getMusicName().equals("歌曲A (001abc)") : "歌曲A第一个应为 '歌曲A (001abc)'，实际: " + musicList.get(0).getMusicName();
        assert musicList.get(1).getMusicName().equals("歌曲A (002def)") : "歌曲A第二个应为 '歌曲A (002def)'，实际: " + musicList.get(1).getMusicName();
        assert musicList.get(2).getMusicName().equals("歌曲B") : "歌曲B不应被修改，实际: " + musicList.get(2).getMusicName();
        System.out.println("✓ 测试1通过");
    }

    private static void testAlbumWithUniqueNames() {
        List<SimpleMusic> musicList = new ArrayList<>();
        musicList.add(new SimpleMusic("001abc", "歌曲A"));
        musicList.add(new SimpleMusic("002def", "歌曲B"));
        musicList.add(new SimpleMusic("003ghi", "歌曲C"));

        applyDuplicateNameFix(musicList);

        assert musicList.get(0).getMusicName().equals("歌曲A") : "歌曲A不应被修改";
        assert musicList.get(1).getMusicName().equals("歌曲B") : "歌曲B不应被修改";
        assert musicList.get(2).getMusicName().equals("歌曲C") : "歌曲C不应被修改";
        System.out.println("✓ 测试2通过");
    }

    private static void testWithMediaMidId() {
        List<SimpleMusic> musicList = new ArrayList<>();
        musicList.add(new SimpleMusic("001abc,media123", "歌曲A"));
        musicList.add(new SimpleMusic("002def,media456", "歌曲A"));

        applyDuplicateNameFix(musicList);

        assert musicList.get(0).getMusicName().equals("歌曲A (001abc)") : "应使用逗号前的MID，实际: " + musicList.get(0).getMusicName();
        assert musicList.get(1).getMusicName().equals("歌曲A (002def)") : "应使用逗号前的MID，实际: " + musicList.get(1).getMusicName();
        System.out.println("✓ 测试3通过");
    }

    private static void testThreeDuplicateNames() {
        List<SimpleMusic> musicList = new ArrayList<>();
        musicList.add(new SimpleMusic("aaa", "歌曲X"));
        musicList.add(new SimpleMusic("bbb", "歌曲X"));
        musicList.add(new SimpleMusic("ccc", "歌曲X"));

        applyDuplicateNameFix(musicList);

        assert musicList.get(0).getMusicName().equals("歌曲X (aaa)") : "实际: " + musicList.get(0).getMusicName();
        assert musicList.get(1).getMusicName().equals("歌曲X (bbb)") : "实际: " + musicList.get(1).getMusicName();
        assert musicList.get(2).getMusicName().equals("歌曲X (ccc)") : "实际: " + musicList.get(2).getMusicName();
        System.out.println("✓ 测试4通过");
    }

    private static void applyDuplicateNameFix(List<SimpleMusic> musicList) {
        Map<String, Integer> nameCount = new HashMap<>();
        for (SimpleMusic music : musicList) {
            String baseName = music.getMusicName();
            nameCount.merge(baseName, 1, Integer::sum);
        }
        for (SimpleMusic music : musicList) {
            if (nameCount.get(music.getMusicName()) > 1) {
                String songMid = music.getId().contains(",") ? music.getId().split(",")[0] : music.getId();
                music.setMusicName(music.getMusicName() + " (" + songMid + ")");
            }
        }
    }
}
