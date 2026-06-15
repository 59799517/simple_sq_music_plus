package com.sqmusicplus.v3.plug.musicbrainz;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.plug.musicbrainz.entity.MusicBrainzIsrcResult;
import com.sqmusicplus.v3.plug.musicbrainz.entity.MusicBrainzSearchResult;
import com.sqmusicplus.v3.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * MusicBrainz API 快速测试工具
 * 可以直接运行 main 方法进行测试
 * 
 * @author SQ
 * @version 1.0.0
 * @date 2026/5/23
 */
@Slf4j
public class MusicBrainzApiTest {

    private static final String BASE_URL = "https://musicbrainz.org/ws/2";

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("MusicBrainz API 测试工具");
        System.out.println("========================================\n");

        // 测试1: 搜索歌曲
        testSearchRecording();

        // 测试2: 通过 ISRC 查询
        testQueryByIsrc();

        // 测试3: 查询艺术家
        testQueryArtist();

        // 测试4: 查询专辑
        testQueryRelease();

        System.out.println("\n========================================");
        System.out.println("所有测试完成！");
        System.out.println("========================================");
    }

    /**
     * 测试搜索录音
     */
    public static void testSearchRecording() {
        System.out.println("\n【测试1】搜索录音 - 'Bohemian Rhapsody'");
        System.out.println("----------------------------------------");

        try {
            String url = BASE_URL + "/recording?query=Bohemian+Rhapsody&fmt=json&offset=0&limit=5";
            
            // 添加 User-Agent 头（MusicBrainz API 要求）
            System.setProperty("http.agent", "SimpleSqMusicPlus/1.0 ( test@example.com )");
            
            MusicBrainzSearchResult result = DownloadUtils.get(url, MusicBrainzSearchResult.class);

            if (result != null && result.getRecordings() != null) {
                System.out.println("✓ 搜索成功！");
                System.out.println("找到 " + result.getCount() + " 个结果\n");

                for (int i = 0; i < Math.min(5, result.getRecordings().size()); i++) {
                    MusicBrainzSearchResult.Recording recording = result.getRecordings().get(i);
                    
                    System.out.println((i + 1) + ". " + recording.getTitle());
                    
                    // 艺术家信息
                    if (recording.getArtistCredit() != null && !recording.getArtistCredit().isEmpty()) {
                        StringBuilder artists = new StringBuilder();
                        for (MusicBrainzSearchResult.Recording.ArtistCredit credit : recording.getArtistCredit()) {
                            if (credit.getArtist() != null) {
                                if (artists.length() > 0) artists.append(", ");
                                artists.append(credit.getArtist().getName());
                            }
                        }
                        System.out.println("   艺术家: " + artists);
                    }

                    // 时长
                    if (recording.getLength() != null) {
                        int seconds = recording.getLength() / 1000;
                        System.out.println("   时长: " + seconds + " 秒 (" + recording.getLength() + " ms)");
                    }

                    // ISRC
                    if (recording.getIsrcs() != null && !recording.getIsrcs().isEmpty()) {
                        System.out.println("   ISRC: " + String.join(", ", recording.getIsrcs()));
                    }

                    // 专辑信息
                    if (recording.getReleases() != null && !recording.getReleases().isEmpty()) {
                        MusicBrainzSearchResult.Recording.Release release = recording.getReleases().get(0);
                        System.out.println("   专辑: " + release.getTitle());
                    }

                    System.out.println();
                }
            } else {
                System.out.println("✗ 未找到结果或请求失败");
            }
        } catch (Exception e) {
            System.err.println("✗ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试通过 ISRC 查询
     */
    public static void testQueryByIsrc() {
        System.out.println("\n【测试2】通过 ISRC 查询 - GBUM71029604");
        System.out.println("----------------------------------------");

        try {
            String isrc = "GBUM71029604"; // Queen - Bohemian Rhapsody
            String url = BASE_URL + "/isrc/" + isrc + "?inc=artists+releases&fmt=json";
            
            MusicBrainzIsrcResult result = DownloadUtils.get(url, MusicBrainzIsrcResult.class);

            if (result != null && result.getRecordings() != null && !result.getRecordings().isEmpty()) {
                System.out.println("✓ 查询成功！");
                System.out.println("ISRC: " + result.getIsrc());
                System.out.println("找到 " + result.getRecordings().size() + " 个录音\n");

                for (int i = 0; i < result.getRecordings().size(); i++) {
                    MusicBrainzIsrcResult.Recording recording = result.getRecordings().get(i);
                    
                    System.out.println((i + 1) + ". " + recording.getTitle());
                    
                    // 艺术家
                    if (recording.getArtistCredit() != null && !recording.getArtistCredit().isEmpty()) {
                        StringBuilder artists = new StringBuilder();
                        for (MusicBrainzIsrcResult.Recording.ArtistCredit credit : recording.getArtistCredit()) {
                            if (credit.getArtist() != null) {
                                if (artists.length() > 0) artists.append(", ");
                                artists.append(credit.getArtist().getName());
                            }
                        }
                        System.out.println("   艺术家: " + artists);
                    }

                    // 时长
                    if (recording.getLength() != null) {
                        int seconds = recording.getLength() / 1000;
                        System.out.println("   时长: " + seconds + " 秒");
                    }

                    // 专辑
                    if (recording.getReleases() != null && !recording.getReleases().isEmpty()) {
                        MusicBrainzIsrcResult.Recording.Release release = recording.getReleases().get(0);
                        System.out.println("   专辑: " + release.getTitle());
                        if (release.getDate() != null) {
                            System.out.println("   发行日期: " + release.getDate());
                        }
                    }

                    System.out.println();
                }
            } else {
                System.out.println("✗ 未找到 ISRC 对应的录音");
            }
        } catch (Exception e) {
            System.err.println("✗ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试查询艺术家
     */
    public static void testQueryArtist() {
        System.out.println("\n【测试3】查询艺术家 - Queen");
        System.out.println("----------------------------------------");

        try {
            String artistId = "0383dadf-2a4e-4d10-a46a-e9e041da8eb7"; // Queen
            String url = BASE_URL + "/artist/" + artistId + "?fmt=json";
            
            String response = DownloadUtils.getBodyStr(url);
            JSONObject json = JSONObject.parseObject(response);

            if (json != null) {
                System.out.println("✓ 查询成功！");
                System.out.println("ID: " + json.getString("id"));
                System.out.println("名称: " + json.getString("name"));
                System.out.println("排序名称: " + json.getString("sort-name"));
                
                if (json.containsKey("country")) {
                    System.out.println("国家: " + json.getString("country"));
                }
                
                if (json.containsKey("type")) {
                    System.out.println("类型: " + json.getString("type"));
                }
                
                if (json.containsKey("disambiguation")) {
                    System.out.println("区分说明: " + json.getString("disambiguation"));
                }
                
                // 生命周期
                if (json.containsKey("life-span")) {
                    JSONObject lifeSpan = json.getJSONObject("life-span");
                    if (lifeSpan.containsKey("begin")) {
                        System.out.println("开始时间: " + lifeSpan.getString("begin"));
                    }
                }
            } else {
                System.out.println("✗ 查询失败");
            }
        } catch (Exception e) {
            System.err.println("✗ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试查询专辑
     */
    public static void testQueryRelease() {
        System.out.println("\n【测试4】查询专辑 - A Night at the Opera");
        System.out.println("----------------------------------------");

        try {
            String releaseId = "6defd963-fe91-4550-b18e-82c685603c2b"; // A Night at the Opera
            String url = BASE_URL + "/release/" + releaseId + "?inc=artists+recordings+media&fmt=json";
            
            String response = DownloadUtils.getBodyStr(url);
            JSONObject json = JSONObject.parseObject(response);

            if (json != null) {
                System.out.println("✓ 查询成功！");
                System.out.println("ID: " + json.getString("id"));
                System.out.println("标题: " + json.getString("title"));
                
                if (json.containsKey("status")) {
                    System.out.println("状态: " + json.getString("status"));
                }
                
                if (json.containsKey("date")) {
                    System.out.println("发行日期: " + json.getString("date"));
                }
                
                if (json.containsKey("country")) {
                    System.out.println("国家: " + json.getString("country"));
                }

                // 艺术家
                if (json.containsKey("artist-credit") && json.getJSONArray("artist-credit").size() > 0) {
                    JSONObject artistCredit = json.getJSONArray("artist-credit").getJSONObject(0);
                    if (artistCredit.containsKey("artist")) {
                        System.out.println("艺术家: " + artistCredit.getJSONObject("artist").getString("name"));
                    }
                }

                // 媒体和曲目
                if (json.containsKey("media")) {
                    var mediaArray = json.getJSONArray("media");
                    System.out.println("\n媒体数量: " + mediaArray.size());
                    
                    for (int i = 0; i < Math.min(1, mediaArray.size()); i++) {
                        JSONObject media = mediaArray.getJSONObject(i);
                        System.out.println("\n媒体 " + (i + 1) + ":");
                        System.out.println("  格式: " + media.getString("format"));
                        System.out.println("  曲目数: " + media.getInteger("track-count"));
                        
                        if (media.containsKey("tracks")) {
                            var tracks = media.getJSONArray("tracks");
                            System.out.println("  前5首曲目:");
                            
                            for (int j = 0; j < Math.min(5, tracks.size()); j++) {
                                JSONObject track = tracks.getJSONObject(j);
                                System.out.println("    " + (j + 1) + ". " + track.getString("title"));
                                
                                if (track.containsKey("length")) {
                                    int seconds = track.getInteger("length") / 1000;
                                    System.out.println("       时长: " + seconds + " 秒");
                                }
                                
                                // 录音信息
                                if (track.containsKey("recording")) {
                                    JSONObject recording = track.getJSONObject("recording");
                                    if (recording.containsKey("isrcs") && recording.getJSONArray("isrcs").size() > 0) {
                                        System.out.println("       ISRC: " + recording.getJSONArray("isrcs").getString(0));
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("✗ 查询失败");
            }
        } catch (Exception e) {
            System.err.println("✗ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
