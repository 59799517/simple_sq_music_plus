package com.sqmusicplus.v3.plug.netease;

import com.sqmusicplus.SimpleSqMusucPlusApplication;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.netease.entity.PlaylistTrackAllResult;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 网易云歌单增量同步测试
 * 测试歌单ID: 727095626
 */
@SpringBootTest(classes = {SimpleSqMusucPlusApplication.class}, properties = "spring.freemarker.enabled=false")
@Slf4j
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NeteasePlaylistIncrementalTest {

    @Autowired
    private NeteaseHander neteaseHander;

    /**
     * 测试获取歌单详情和trackIds
     */
    @Test
    public void testGetPlayListInfo() {
        String playlistId = "727095626";
        log.info("开始测试获取歌单详情, playlistId={}", playlistId);

        PlaylistTrackAllResult result = neteaseHander.getPlayListInfo(playlistId);

        log.info("歌单名称: {}", result.getPlaylist().getName());
        log.info("歌曲总数: {}", result.getPlaylist().getTrackCount());
        log.info("更新时间: {}", result.getPlaylist().getUpdateTime());
        log.info("trackUpdateTime: {}", result.getPlaylist().getTrackUpdateTime());

        if (result.getPlaylist().getTrackIds() != null) {
            log.info("trackIds数量: {}", result.getPlaylist().getTrackIds().size());
            // 打印前5个trackId
            result.getPlaylist().getTrackIds().stream()
                    .limit(5)
                    .forEach(id -> log.info("  trackId: {}, v: {}", id.getId(), id.getV()));
        }
    }

    /**
     * 测试获取trackIds列表（轻量级）
     */
    @Test
    public void testGetPlayListTrackIds() {
        String playlistId = "727095626";
        log.info("开始测试获取歌单trackIds, playlistId={}", playlistId);

        List<Long> trackIds = neteaseHander.getPlayListTrackIds(playlistId);

        log.info("trackIds总数: {}", trackIds.size());
        log.info("前10个ID: {}", trackIds.stream().limit(10).collect(Collectors.toList()));
    }

    /**
     * 测试根据ID列表获取歌曲详情
     */
    @Test
    public void testGetPlayListByIds() {
        String playlistId = "727095626";

        // 先获取所有trackIds
        List<Long> allTrackIds = neteaseHander.getPlayListTrackIds(playlistId);
        log.info("歌单总trackIds: {}", allTrackIds.size());

        // 测试获取前10首歌的详情
        List<Long> testIds = allTrackIds.stream().limit(10).collect(Collectors.toList());
        log.info("测试获取{}首歌的详情", testIds.size());

        ArrayList<Music> musics = neteaseHander.getPlayListByIds(testIds);

        log.info("获取到{}首歌详情", musics.size());
        musics.forEach(m -> log.info("  歌曲: {} - {} (ID: {})",
                m.getMusicArtists(), m.getMusicName(), m.getId()));
    }

    /**
     * 测试增量同步逻辑：模拟对比trackIds和已同步列表
     */
    @Test
    public void testIncrementalSyncLogic() {
        String playlistId = "727095626";

        // 1. 获取歌单所有trackIds
        List<Long> allTrackIds = neteaseHander.getPlayListTrackIds(playlistId);
        log.info("歌单总trackIds: {}", allTrackIds.size());

        // 2. 模拟已同步的歌曲ID（假设已同步前100首）
        Set<String> simulatedSyncedIds = new HashSet<>();
        for (int i = 0; i < 100 && i < allTrackIds.size(); i++) {
            simulatedSyncedIds.add(allTrackIds.get(i).toString());
        }
        log.info("模拟已同步歌曲数: {}", simulatedSyncedIds.size());

        // 3. 找出新增的歌曲ID
        List<Long> newSongIds = allTrackIds.stream()
                .filter(id -> !simulatedSyncedIds.contains(id.toString()))
                .collect(Collectors.toList());
        log.info("新增歌曲数: {}", newSongIds.size());

        // 4. 测试获取新增歌曲详情（取前5首）
        if (!newSongIds.isEmpty()) {
            List<Long> testNewIds = newSongIds.stream().limit(5).collect(Collectors.toList());
            ArrayList<Music> newMusics = neteaseHander.getPlayListByIds(testNewIds);
            log.info("获取到{}首新增歌曲详情", newMusics.size());
            newMusics.forEach(m -> log.info("  新增歌曲: {} - {} (ID: {})",
                    m.getMusicArtists(), m.getMusicName(), m.getId()));
        }
    }

    /**
     * 性能测试：对比全量拉取和增量拉取的请求数
     */
    @Test
    public void testPerformanceComparison() {
        String playlistId = "727095626";

        // 获取歌单详情
        PlaylistTrackAllResult result = neteaseHander.getPlayListInfo(playlistId);
        long trackCount = result.getPlaylist().getTrackCount();
        List<Long> allTrackIds = neteaseHander.getPlayListTrackIds(playlistId);

        log.info("=== 性能对比测试 ===");
        log.info("歌单歌曲总数: {}", trackCount);
        log.info("trackIds实际数量: {}", allTrackIds.size());

        // 模拟全量拉取的请求数
        int fullRequestCount = (int) Math.ceil((double) trackCount / 50);
        log.info("全量拉取需要请求数: {} (每次50首)", fullRequestCount);

        // 模拟增量拉取的请求数（假设只有10首新增）
        int incrementalRequestCount = (int) Math.ceil((double) 10 / 1000);
        log.info("增量拉取需要请求数: {} (每次1000首, 假设10首新增)", incrementalRequestCount + 1);

        log.info("请求减少比例: {}%", (1.0 - (double)(incrementalRequestCount + 1) / fullRequestCount) * 100);
    }
}
