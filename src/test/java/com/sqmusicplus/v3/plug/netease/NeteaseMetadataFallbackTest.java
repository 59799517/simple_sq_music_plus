package com.sqmusicplus.v3.plug.netease;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.netease.entity.PlaylistTrackAllResult;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.plug.netease.hander.SQNeteaseCloudMusicInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NeteaseMetadataFallbackTest {

    @Test
    void getPlayListInfo_代理响应无歌单时使用官方回退() {
        TestNeteaseHander hander = new TestNeteaseHander();
        hander.neteaseCloudMusicInfo = new StubNeteaseCloudMusicInfo(
                JSONObject.parseObject("{\"code\":502,\"message\":\"proxy unavailable\"}"), null);
        hander.officialPlaylistResponse = JSONObject.parseObject("""
                {
                  "code": 200,
                  "playlist": {
                    "id": 18257070726,
                    "name": "test",
                    "trackCount": 1,
                    "trackIds": [{"id": 1890530891}]
                  }
                }
                """);

        PlaylistTrackAllResult result = hander.getPlayListInfo("18257070726");

        assertEquals("test", result.getPlaylist().getName());
        assertEquals(1890530891L, result.getPlaylist().getTrackIds().get(0).getId());
        assertEquals(1, hander.officialPlaylistCallCount);
    }

    @Test
    void getPlayListInfo_代理正常时不调用官方接口() {
        TestNeteaseHander hander = new TestNeteaseHander();
        hander.neteaseCloudMusicInfo = new StubNeteaseCloudMusicInfo(
                JSONObject.parseObject("{\"code\":200,\"playlist\":{\"name\":\"proxy playlist\"}}"), null);

        PlaylistTrackAllResult result = hander.getPlayListInfo("123");

        assertEquals("proxy playlist", result.getPlaylist().getName());
        assertEquals(0, hander.officialPlaylistCallCount);
    }

    @Test
    void getPlayListByIds_代理响应无歌曲时使用官方回退() {
        TestNeteaseHander hander = new TestNeteaseHander();
        hander.neteaseCloudMusicInfo = new StubNeteaseCloudMusicInfo(
                null, JSONObject.parseObject("{\"code\":502,\"songs\":[]}"));
        hander.officialSongResponse = JSONObject.parseObject("""
                {
                  "code": 200,
                  "songs": [{
                    "id": 1890530891,
                    "name": "如果可以",
                    "dt": 274599,
                    "ar": [{"id": 5379, "name": "韦礼安"}],
                    "al": {"id": 135391759, "name": "如果可以", "picUrl": "cover"},
                    "h": {"br": 320000}
                  }]
                }
                """);

        List<Music> musics = hander.getPlayListByIds(List.of(1890530891L));

        assertEquals(1, musics.size());
        assertEquals("1890530891", musics.get(0).getId());
        assertEquals("如果可以", musics.get(0).getMusicName());
        assertEquals(1, hander.officialSongCallCount);
    }

    @Test
    void getPlayListInfo_空ID直接拒绝() {
        TestNeteaseHander hander = new TestNeteaseHander();

        assertThrows(RuntimeException.class, () -> hander.getPlayListInfo(" "));
    }

    private static class StubNeteaseCloudMusicInfo extends SQNeteaseCloudMusicInfo {
        private final JSONObject playlistResponse;
        private final JSONObject songResponse;

        private StubNeteaseCloudMusicInfo(JSONObject playlistResponse, JSONObject songResponse) {
            this.playlistResponse = playlistResponse;
            this.songResponse = songResponse;
            init("https://proxy.invalid");
        }

        @Override
        public JSONObject playlistDetail(JSONObject parameter) {
            return playlistResponse;
        }

        @Override
        public JSONObject songDetail(JSONObject parameter) {
            return songResponse;
        }
    }

    private static class TestNeteaseHander extends NeteaseHander {
        private JSONObject officialPlaylistResponse;
        private JSONObject officialSongResponse;
        private int officialPlaylistCallCount;
        private int officialSongCallCount;

        @Override
        protected JSONObject fetchOfficialPlaylistDetail(String playlistId) {
            officialPlaylistCallCount++;
            return officialPlaylistResponse;
        }

        @Override
        protected JSONObject fetchOfficialSongDetail(List<Long> songIds) {
            officialSongCallCount++;
            return officialSongResponse;
        }
    }
}
