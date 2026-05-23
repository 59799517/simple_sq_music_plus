package com.sqmusicplus;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.tidal.hander.TidalSearchHander;
import com.sqmusicplus.v3.plug.tidal.utils.TidalTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Classname TidalTest
 * @Description Tidal插件完整功能测试
 * @Date 2026/4/29 16:22
 * @Created by SQ
 */
@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "spring.freemarker.enabled=false",
        "spring.profiles.active=kw,mg,qq,netease,kg,tidal"
    }
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TidalTest {

    @Autowired
    private TidalSearchHander tidalSearchHander;

    // 测试用的歌曲ID（更换为数据更完整的歌曲）
    private static final String TEST_TRACK_ID = "337502043";
    // 测试歌词的歌曲ID
    private static final String TEST_LYRICS_TRACK_ID = "212494705";
    // 测试用的专辑ID（更换为 116125894）
    private static final String TEST_ALBUM_ID = "116125894";
    // 测试用的艺术家ID（更换为 3557299）
    private static final String TEST_ARTIST_ID = "3557299";
    // 测试搜索关键词
    private static final String TEST_SEARCH_KEYWORD = "星晴";

    /**
     * 测试1: Token获取和刷新
     */
    @Test
    @Order(1)
    @DisplayName("测试1: Token获取和刷新")
    public void test1_GetAndRefreshToken() {
        log.info("\n========== 开始测试1: Token获取和刷新 ==========");
        
        // 1.1 获取Token
        log.info("步骤1.1: 获取Access Token...");
        String token = TidalTokenUtils.getAccessToken();
        Assertions.assertNotNull(token, "Token不应为null");
        Assertions.assertFalse(token.isEmpty(), "Token不应为空");
        log.info("✓ Token获取成功: {}...", token.substring(0, Math.min(20, token.length())));
        
        // 1.2 检查Token有效性
        log.info("步骤1.2: 检查Token有效性...");
        boolean isValid = TidalTokenUtils.isTokenValid();
        Assertions.assertTrue(isValid, "Token应该有效");
        log.info("✓ Token状态: 有效");
        
        // 1.3 获取剩余时间
        log.info("步骤1.3: 获取Token剩余时间...");
        long remainingSeconds = TidalTokenUtils.getTokenRemainingSeconds();
        log.info("✓ Token剩余时间: {} 秒 ({} 分钟)", remainingSeconds, remainingSeconds / 60);
        Assertions.assertTrue(remainingSeconds > 0, "Token剩余时间应大于0");
        
        // 1.4 强制刷新Token
        log.info("步骤1.4: 强制刷新Token...");
        String newToken = TidalTokenUtils.forceRefreshToken();
        Assertions.assertNotNull(newToken, "新Token不应为null");
        log.info("✓ Token刷新成功: {}...", newToken.substring(0, Math.min(20, newToken.length())));
        log.info("当前token是：{}",newToken);
        log.info("========== 测试1完成: Token获取和刷新 ==========\n");
    }

    /**
     * 测试2: 搜索歌曲
     */
    @Test
    @Order(2)
    @DisplayName("测试2: 搜索歌曲")
    public void test2_SearchTracks() {
        log.info("\n========== 开始测试2: 搜索歌曲 ==========");
        
        SearchKeyData searchKeyData = new SearchKeyData();
        searchKeyData.setSearchkey(TEST_SEARCH_KEYWORD);
        searchKeyData.setPageIndex(1);
        searchKeyData.setPageSize(10);
        
        log.info("搜索关键词: {}", TEST_SEARCH_KEYWORD);
        log.info("页码: {}, 每页数量: {}", searchKeyData.getPageIndex(), searchKeyData.getPageSize());
        
        // 检查配置是否加载
        log.info("检查TidalConfig配置...");
        try {
            java.lang.reflect.Field searchUrlField = tidalSearchHander.getClass().getDeclaredField("config");
            searchUrlField.setAccessible(true);
            Object config = searchUrlField.get(tidalSearchHander);
            if (config != null) {
                log.info("✓ TidalConfig已加载");
                java.lang.reflect.Field urlField = config.getClass().getDeclaredField("searchUrl");
                urlField.setAccessible(true);
                String searchUrl = (String) urlField.get(config);
                log.info("  searchUrl: {}", searchUrl != null ? searchUrl : "❌ NULL");
            } else {
                log.error("❌ TidalConfig为null");
            }
        } catch (Exception e) {
            log.error("检查配置失败", e);
        }
        
        log.info("\n开始调用搜索方法...");
        PlugSearchResult<PlugSearchMusicResult> result = tidalSearchHander.querySongByName(searchKeyData);
        
        log.info("\n搜索结果分析:");
        log.info("  结果对象: {}", result != null ? "✓ 非null" : "❌ null");
        
        if (result != null) {
            log.info("  总记录数: {}", result.getSearchTotal());
            log.info("  记录列表: {}", result.getRecords() != null ? result.getRecords().size() + " 条" : "❌ null");
            
            Assertions.assertNotNull(result, "搜索结果不应为null");
            Assertions.assertNotNull(result.getRecords(), "结果记录不应为null");
            log.info("✓ 搜索成功，共找到 {} 首歌曲", result.getSearchTotal());
            log.info("✓ 当前页返回 {} 首歌曲", result.getRecords().size());
            
            // 打印前3首歌曲信息
            if (!result.getRecords().isEmpty()) {
                log.info("\n前3首歌曲详情:");
                int count = Math.min(3, result.getRecords().size());
                for (int i = 0; i < count; i++) {
                    PlugSearchMusicResult song = result.getRecords().get(i);
                    log.info("  [{}] {}", i + 1, song.getName());
                    log.info("      艺术家: {}", String.join(", ", song.getArtistName()));
                    log.info("      专辑: {}", song.getAlbumName());
                    log.info("      时长: {} ms", song.getDuration());
                    log.info("      ID: {}", song.getId());
                    if (song.getBrTypes() != null && !song.getBrTypes().isEmpty()) {
                        log.info("      可用音质: {}", song.getBrTypes().size() + "种");
                    }
                }
            }
        } else {
            log.error("❌ 搜索结果为null，请检查上面的日志");
            Assertions.fail("搜索结果不应为null");
        }
        
        log.info("========== 测试2完成: 搜索歌曲 ==========\n");
    }

    /**
     * 测试3: 搜索艺术家
     */
    @Test
    @Order(3)
    @DisplayName("测试3: 搜索艺术家")
    public void test3_SearchArtists() {
        log.info("\n========== 开始测试3: 搜索艺术家 ==========");
        
        SearchKeyData searchKeyData = new SearchKeyData();
        searchKeyData.setSearchkey(TEST_SEARCH_KEYWORD);
        searchKeyData.setPageIndex(1);
        searchKeyData.setPageSize(5);
        
        log.info("搜索关键词: {}", TEST_SEARCH_KEYWORD);
        
        var result = tidalSearchHander.queryArtistByName(searchKeyData);
        
        Assertions.assertNotNull(result, "搜索结果不应为null");
        log.info("✓ 搜索成功，共找到 {} 位艺术家", result.getSearchTotal());
        log.info("✓ 当前页返回 {} 位艺术家", result.getRecords().size());
        
        if (!result.getRecords().isEmpty()) {
            log.info("\n艺术家详情:");
            result.getRecords().forEach(artist -> {
                log.info("  - {}", artist.getArtistName());
                log.info("    ID: {}", artist.getArtistid());
            });
        }
        
        log.info("========== 测试3完成: 搜索艺术家 ==========\n");
    }

    /**
     * 测试4: 搜索专辑
     */
    @Test
    @Order(4)
    @DisplayName("测试4: 搜索专辑")
    public void test4_SearchAlbums() {
        log.info("\n========== 开始测试4: 搜索专辑 ==========");
        
        SearchKeyData searchKeyData = new SearchKeyData();
        searchKeyData.setSearchkey(TEST_SEARCH_KEYWORD);
        searchKeyData.setPageIndex(1);
        searchKeyData.setPageSize(5);
        
        log.info("搜索关键词: {}", TEST_SEARCH_KEYWORD);
        
        var result = tidalSearchHander.queryAlbumByName(searchKeyData);
        
        Assertions.assertNotNull(result, "搜索结果不应为null");
        log.info("✓ 搜索成功，共找到 {} 张专辑", result.getSearchTotal());
        log.info("✓ 当前页返回 {} 张专辑", result.getRecords().size());
        
        if (!result.getRecords().isEmpty()) {
            log.info("\n专辑详情:");
            result.getRecords().forEach(album -> {
                log.info("  - {}", album.getAlbumName());
                log.info("    艺术家: {}", album.getArtistName());
                log.info("    ID: {}", album.getAlbumid());
            });
        }
        
        log.info("========== 测试4完成: 搜索专辑 ==========\n");
    }

    /**
     * 测试5: 获取歌曲详情
     */
    @Test
    @Order(5)
    @DisplayName("测试5: 获取歌曲详情")
    public void test5_GetTrackById() {
        log.info("\n========== 开始测试5: 获取歌曲详情 ==========");
        log.info("测试歌曲ID: {}", TEST_TRACK_ID);
        
        Music music = tidalSearchHander.querySongById(TEST_TRACK_ID);
        
        Assertions.assertNotNull(music, "歌曲信息不应为null");
        Assertions.assertEquals(TEST_TRACK_ID, music.getId(), "歌曲ID应匹配");
        
        log.info("✓ 歌曲详情获取成功:");
        log.info("  歌曲名称: {}", music.getMusicName());
        log.info("  艺术家: {}", String.join(", ", music.getMusicArtists()));
        log.info("  专辑: {}", music.getMusicAlbum());
        log.info("  时长: {} ms ({} 秒)", music.getMusicDuration(), music.getMusicDuration() / 1000);
        log.info("  专辑ID: {}", music.getAlbumId());
        if (music.getArtistsIds() != null && !music.getArtistsIds().isEmpty()) {
            log.info("  艺术家IDs: {}", String.join(", ", music.getArtistsIds()));
        }
        if (music.getMusicImage() != null && !music.getMusicImage().isEmpty()) {
            log.info("  封面图片: {}", music.getMusicImage());
        }
        if (music.getMusicLyric() != null && !music.getMusicLyric().isEmpty()) {
            log.info("  歌词长度: {} 字符", music.getMusicLyric().length());
        } else {
            log.info("  歌词: 无");
        }
        if (music.getDataInfo() != null) {
            log.info("  原始数据: {}", music.getDataInfo().toJSONString().substring(0, Math.min(100, music.getDataInfo().toJSONString().length())) + "...");
        }
        
        log.info("========== 测试5完成: 获取歌曲详情 ==========\n");
    }

    /**
     * 测试5.5: 获取歌词（专门测试）
     */
    @Test
    @Order(6)
    @DisplayName("测试5.5: 获取歌词（专门测试）")
    public void test5_5_GetLyrics() {
        log.info("\n========== 开始测试5.5: 获取歌词 ==========");
        log.info("测试歌曲ID: {}", TEST_LYRICS_TRACK_ID);
        
        // 先获取歌曲详情，这会触发歌词获取
        log.info("步骤1: 调用 querySongById() 获取歌曲详情（包含歌词）...");
        Music music = tidalSearchHander.(TEST_LYRICS_TRACK_ID);
        
        Assertions.assertNotNull(music, "歌曲信息不应为null");
        log.info("✓ 歌曲详情获取成功:");
        log.info("  歌曲名称: {}", music.getMusicName());
        log.info("  艺术家: {}", String.join(", ", music.getMusicArtists()));
        
        // 检查歌词
        log.info("\n步骤2: 检查歌词...");
        String lyrics = music.getMusicLyric();
        
        if (lyrics != null && !lyrics.isEmpty()) {
            log.info("✓ 歌词获取成功！");
            log.info("  歌词长度: {} 字符", lyrics.length());
            log.info("  歌词预览（前200字符）: {}", 
                lyrics.length() > 200 ? lyrics.substring(0, 200) + "..." : lyrics);
            
            // 验证歌词格式
            if (lyrics.contains("[") && lyrics.contains("]")) {
                log.info("  ✓ 歌词包含时间戳标签（LRC格式）");
            } else {
                log.info("  ⚠ 歌词不包含时间戳标签（纯文本格式）");
            }
            
            // 统计行数
            long lineCount = lyrics.lines().count();
            log.info("  歌词行数: {}", lineCount);
            
        } else {
            log.warn("❌ 歌词为空！");
            log.warn("  可能原因:");
            log.warn("    1. 该歌曲没有歌词");
            log.warn("    2. 歌词 API 返回空");
            log.warn("    3. 网络连接问题");
            log.warn("    4. Token 权限不足");
        }
        
        log.info("========== 测试5.5完成: 获取歌词 ==========\n");
    }

    /**
     * 测试6: 获取艺术家详情
     */
    @Test
    @Order(7)
    @DisplayName("测试6: 获取艺术家详情")
    public void test6_GetArtistById() {
        log.info("\n========== 开始测试6: 获取艺术家详情 ==========");
        log.info("测试艺术家ID: {}", TEST_ARTIST_ID);
        
        log.info("步骤1: 调用 queryArtistById() 方法...");
        Artists artist = tidalSearchHander.queryArtistById(TEST_ARTIST_ID);
        
        log.info("\n步骤2: 验证返回结果...");
        log.info("  artist 对象: {}", artist != null ? "✓ 非null" : "❌ null");
        
        if (artist != null) {
            log.info("  artist.getId(): '{}' (类型: {})", artist.getId(), artist.getId() != null ? artist.getId().getClass().getSimpleName() : "null");
            log.info("  artist.getMusicArtistsName(): '{}'", artist.getMusicArtistsName());
            log.info("  artist.getMusicArtistsPhoto(): '{}'", artist.getMusicArtistsPhoto() != null && !artist.getMusicArtistsPhoto().isEmpty() ? "有值" : "空");
            log.info("  artist.getMusicArtistsDescribe(): '{}'", artist.getMusicArtistsDescribe() != null && !artist.getMusicArtistsDescribe().isEmpty() ? "有值 (" + artist.getMusicArtistsDescribe().length() + "字符)" : "空");
            
            if (artist.getDataInfo() != null) {
                String dataJson = artist.getDataInfo().toJSONString();
                log.info("  artist.getDataInfo() JSON: {}", dataJson.length() > 300 ? dataJson.substring(0, 300) + "..." : dataJson);
            } else {
                log.warn("  artist.getDataInfo(): ❌ null");
            }
        } else {
            log.error("❌ artist 对象为 null，无法继续验证");
        }
        
        log.info("\n步骤3: 执行断言检查...");
        Assertions.assertNotNull(artist, "艺术家信息不应为null");
        
        log.info("  预期 ID: '{}'", TEST_ARTIST_ID);
        log.info("  实际 ID: '{}'", artist.getId());
        log.info("  ID 是否匹配: {}", TEST_ARTIST_ID.equals(artist.getId()) ? "✓ 是" : "❌ 否");
        
        Assertions.assertEquals(TEST_ARTIST_ID, artist.getId(), "艺术家ID应匹配");
        
        log.info("\n✓ 艺术家详情获取成功:");
        log.info("  艺术家名称: {}", artist.getMusicArtistsName());
        if (artist.getMusicArtistsPhoto() != null && !artist.getMusicArtistsPhoto().isEmpty()) {
            log.info("  照片URL: {}", artist.getMusicArtistsPhoto());
        }
        if (artist.getMusicArtistsDescribe() != null && !artist.getMusicArtistsDescribe().isEmpty()) {
            log.info("  简介长度: {} 字符", artist.getMusicArtistsDescribe().length());
        }
        
        log.info("========== 测试6完成: 获取艺术家详情 ==========\n");
    }

    /**
     * 测试7: 获取专辑详情及所有曲目
     */
    @Test
    @Order(8)
    @DisplayName("测试7: 获取专辑详情及所有曲目")
    public void test7_GetAlbumById() {
        log.info("\n========== 开始测试7: 获取专辑详情及所有曲目 ==========");
        log.info("测试专辑ID: {}", TEST_ALBUM_ID);
        
        log.info("步骤1: 调用 queryAlbumById() 方法...");
        Album album = tidalSearchHander.queryAlbumById(TEST_ALBUM_ID);
        
        log.info("\n步骤2: 验证返回结果...");
        log.info("  album 对象: {}", album != null ? "✓ 非null" : "❌ null");
        
        if (album != null) {
            log.info("  album.getAlbumId(): '{}' (类型: {})", 
                album.getAlbumId(), 
                album.getAlbumId() != null ? album.getAlbumId().getClass().getSimpleName() : "null");
            log.info("  album.getAlbumName(): '{}'", album.getAlbumName());
            log.info("  album.getAlbumArtist(): '{}'", album.getAlbumArtist());
            log.info("  album.getAlbumArtistId(): '{}'", album.getAlbumArtistId());
            log.info("  album.getAlbumImg(): '{}'", 
                album.getAlbumImg() != null && !album.getAlbumImg().isEmpty() ? "有值" : "空");
            log.info("  album.getAlbumTime(): '{}'", album.getAlbumTime());
            log.info("  album.getMusics(): {}", 
                album.getMusics() != null ? album.getMusics().size() + " 首曲目" : "❌ null");
            
            if (album.getDataInfo() != null) {
                String dataJson = album.getDataInfo().toJSONString();
                log.info("  album.getDataInfo() JSON: {}", 
                    dataJson.length() > 400 ? dataJson.substring(0, 400) + "..." : dataJson);
            } else {
                log.warn("  album.getDataInfo(): ❌ null");
            }
            
            // 打印前3首曲目详情
            if (album.getMusics() != null && !album.getMusics().isEmpty()) {
                log.info("\n  前3首曲目详情:");
                int count = Math.min(3, album.getMusics().size());
                for (int i = 0; i < count; i++) {
                    Music track = album.getMusics().get(i);
                    log.info("    [{}] ID: {}, 名称: {}, 时长: {} 秒", 
                        i + 1, track.getId(), track.getMusicName(), track.getMusicDuration() / 1000);
                }
            }
        } else {
            log.error("❌ album 对象为 null，无法继续验证");
        }
        
        log.info("\n步骤3: 执行断言检查...");
        Assertions.assertNotNull(album, "专辑信息不应为null");
        
        log.info("  预期 ID: '{}'", TEST_ALBUM_ID);
        log.info("  实际 ID: '{}'", album.getAlbumId());
        log.info("  ID 是否匹配: {}", TEST_ALBUM_ID.equals(album.getAlbumId()) ? "✓ 是" : "❌ 否");
        
        Assertions.assertEquals(TEST_ALBUM_ID, album.getAlbumId(), "专辑ID应匹配");
        
        log.info("\n✓ 专辑详情获取成功:");
        log.info("  专辑名称: {}", album.getAlbumName());
        log.info("  艺术家: {}", album.getAlbumArtist());
        if (album.getAlbumImg() != null && !album.getAlbumImg().isEmpty()) {
            log.info("  封面图片: {}", album.getAlbumImg());
        }
        if (album.getAlbumTime() != null) {
            log.info("  发行时间: {}", album.getAlbumTime());
        }
        log.info("  曲目数量: {}", album.getMusics() != null ? album.getMusics().size() : 0);
        
        // 打印前5首曲目
        if (album.getMusics() != null && !album.getMusics().isEmpty()) {
            log.info("\n  前5首曲目:");
            int count = Math.min(5, album.getMusics().size());
            for (int i = 0; i < count; i++) {
                Music track = album.getMusics().get(i);
                log.info("    [{}] {} ({} 秒)", i + 1, track.getMusicName(), track.getMusicDuration() / 1000);
            }
        }
        
        log.info("========== 测试7完成: 获取专辑详情及所有曲目 ==========\n");
    }

    /**
     * 测试8: 获取艺术家专辑列表
     */
    @Test
    @Order(9)
    @DisplayName("测试8: 获取艺术家专辑列表")
    public void test8_GetArtistAlbums() {
        log.info("\n========== 开始测试8: 获取艺术家专辑列表 ==========");
        log.info("测试艺术家ID: {}", TEST_ARTIST_ID);
        
        log.info("步骤1: 调用 getAlbumsByArtist() 方法...");
        List<Album> albums = tidalSearchHander.getAlbumsByArtist(TEST_ARTIST_ID);
        
        log.info("\n步骤2: 验证返回结果...");
        log.info("  albums 对象: {}", albums != null ? "✓ 非null" : "❌ null");
        
        if (albums != null) {
            log.info("  专辑数量: {}", albums.size());
            
            if (albums.isEmpty()) {
                log.warn("⚠ 专辑列表为空！可能原因：");
                log.warn("  1. 艺术家ID错误或不存咋");
                log.warn("  2. API返回了空数组");
                log.warn("  3. 网络连接问题");
                log.warn("  4. Token权限不足");
            } else {
                log.info("\n  所有专辑详情:");
                for (int i = 0; i < albums.size(); i++) {
                    Album album = albums.get(i);
                    log.info("    [{}] 专辑ID: '{}'", i + 1, album.getAlbumId());
                    log.info("        专辑名称: '{}'", album.getAlbumName());
                    log.info("        艺术家: '{}'", album.getAlbumArtist());
                    log.info("        艺术家ID: '{}'", album.getAlbumArtistId());
                    log.info("        发行时间: '{}'", album.getAlbumTime());
                    log.info("        封面图片: '{}'", 
                        album.getAlbumImg() != null && !album.getAlbumImg().isEmpty() ? "有值" : "空");
                    log.info("        曲目数量: {}", 
                        album.getMusics() != null ? album.getMusics().size() : "null");
                    
                    if (album.getDataInfo() != null) {
                        String dataJson = album.getDataInfo().toJSONString();
                        log.info("        原始数据: {}", 
                            dataJson.length() > 300 ? dataJson.substring(0, 300) + "..." : dataJson);
                    } else {
                        log.warn("        原始数据: ❌ null");
                    }
                    log.info(""); // 空行分隔
                }
                
                log.info("\n  前5张专辑（简化）:");
                int count = Math.min(5, albums.size());
                for (int i = 0; i < count; i++) {
                    Album album = albums.get(i);
                    log.info("    [{}] {} ({})", i + 1, album.getAlbumName(), album.getAlbumTime());
                }
            }
        } else {
            log.error("❌ albums 对象为 null，无法继续验证");
        }
        
        log.info("\n步骤3: 执行断言检查...");
        Assertions.assertNotNull(albums, "专辑列表不应为null");
        
        log.info("  预期: 专辑列表不为 null");
        log.info("  实际: {}", albums != null ? "✓ 非null" : "❌ null");
        log.info("  专辑数量: {}", albums.size());
        
        log.info("\n✓ 获取成功，共 {} 张专辑", albums.size());
        
        log.info("========== 测试8完成: 获取艺术家专辑列表 ==========\n");
    }

    /**
     * 测试9: 获取下载链接
     */
    @Test
    @Order(10)
    @DisplayName("测试9: 获取下载链接")
    public void test9_GetDownloadUrl() {
        log.info("\n========== 开始测试9: 获取下载链接 ==========");
        log.info("测试歌曲ID: {}", TEST_TRACK_ID);
        
        // 9.1 先获取歌曲详情
        log.info("步骤9.1: 获取歌曲详情...");
        Music music = tidalSearchHander.querySongById(TEST_TRACK_ID);
        Assertions.assertNotNull(music, "歌曲信息不应为null");
        log.info("✓ 歌曲: {}", music.getMusicName());
        
        // 9.2 创建下载信息
        log.info("步骤9.2: 创建下载信息...");
        PlugBrType brType = PlugBrType.TIDAL_FLAC_LOSSLESS; // 使用MP3 320kbps
        DownloadInfo downloadInfo = tidalSearchHander.musicToDownloadInfo(music, brType, false);
        Assertions.assertNotNull(downloadInfo, "下载信息不应为null");
        log.info("✓ 下载信息创建成功");
        log.info("  音质: {}", brType.getId());
        log.info("  格式: {}", brType.getType());
        log.info("  码率: {} kbps", brType.getBit());
        
        // 9.3 获取下载链接
        log.info("步骤9.3: 获取下载链接...");
        DownloadUrlResult downloadUrlResult = tidalSearchHander.getDownloadUrl(downloadInfo);
        
        if (downloadUrlResult != null) {
            log.info("✓ 下载链接获取成功:");
            log.info("  URL: {}", downloadUrlResult.getUrl() != null ? downloadUrlResult.getUrl().substring(0, Math.min(100, downloadUrlResult.getUrl().length())) + "..." : "null");
            log.info("  音质: {}", downloadUrlResult.getPlugBrTypeId());
            log.info("  码率: {}", downloadUrlResult.getBit());
            if (downloadUrlResult.getErrorMsg() != null) {
                log.info("  错误信息: {}", downloadUrlResult.getErrorMsg());
            }
        } else {
            log.warn("⚠ 下载链接获取失败（可能原因：需要付费订阅或地区限制）");
        }
        
        log.info("========== 测试9完成: 获取下载链接 ==========\n");
    }

    /**
     * 测试10: 完整流程测试（从搜索到下载链接）
     */
    @Test
    @Order(11)
    @DisplayName("测试10: 完整流程测试")
    public void test10_FullWorkflow() {
        log.info("\n========== 开始测试10: 完整流程测试 ==========");
        
        // 10.1 搜索歌曲
        log.info("步骤10.1: 搜索歌曲 '{}'", TEST_SEARCH_KEYWORD);
        SearchKeyData searchKeyData = new SearchKeyData();
        searchKeyData.setSearchkey(TEST_SEARCH_KEYWORD);
        searchKeyData.setPageIndex(1);
        searchKeyData.setPageSize(5);
        
        PlugSearchResult<PlugSearchMusicResult> searchResult = tidalSearchHander.querySongByName(searchKeyData);
        Assertions.assertNotNull(searchResult, "搜索结果不应为null");
        Assertions.assertFalse(searchResult.getRecords().isEmpty(), "搜索结果不应为空");
        log.info("✓ 搜索成功，找到 {} 首歌曲", searchResult.getSearchTotal());
        
        // 10.2 选择第一首歌曲
        PlugSearchMusicResult firstSong = searchResult.getRecords().get(0);
        log.info("步骤10.2: 选择第一首歌曲 '{}'", firstSong.getName());
        log.info("  艺术家: {}", String.join(", ", firstSong.getArtistName()));
        
        // 10.3 获取歌曲详情
        log.info("步骤10.3: 获取歌曲详情...");
        Music music = tidalSearchHander.querySongById(firstSong.getId());
        Assertions.assertNotNull(music, "歌曲详情不应为null");
        log.info("✓ 歌曲详情获取成功");
        
        // 10.4 创建下载信息
        log.info("步骤10.4: 创建下载信息（音质: FLAC无损）...");
        PlugBrType brType = PlugBrType.TIDAL_FLAC_LOSSLESS;
        DownloadInfo downloadInfo = tidalSearchHander.musicToDownloadInfo(music, brType, false);
        log.info("✓ 下载信息创建成功");
        
        // 10.5 获取下载链接
        log.info("步骤10.5: 获取下载链接...");
        DownloadUrlResult downloadUrlResult = tidalSearchHander.getDownloadUrl(downloadInfo);
        
        if (downloadUrlResult != null && downloadUrlResult.getUrl() != null) {
            log.info("✓ 完整流程成功！");
            log.info("  下载链接: {}...", downloadUrlResult.getUrl().substring(0, Math.min(80, downloadUrlResult.getUrl().length())));
        } else {
            log.warn("⚠ 下载链接获取失败（可能需要HiFi订阅）");
            log.info("但整体流程已验证通过");
        }
        
        log.info("========== 测试10完成: 完整流程测试 ==========\n");
    }

    /**
     * 测试11: Token工具类其他方法
     */
    @Test
    @Order(12)
    @DisplayName("测试11: Token工具类其他方法")
    public void test11_TokenUtilsOtherMethods() {
        log.info("\n========== 开始测试11: Token工具类其他方法 ==========");
        
        // 11.1 清除Token缓存
        log.info("步骤11.1: 清除Token缓存...");
        TidalTokenUtils.clearToken();
        log.info("✓ Token缓存已清除");
        
        // 11.2 重新获取（会从服务器重新请求）
        log.info("步骤11.2: 重新获取Token...");
        String newToken = TidalTokenUtils.getAccessToken();
        Assertions.assertNotNull(newToken, "新Token不应为null");
        log.info("✓ 新Token获取成功");
        
        // 11.3 检查有效性
        log.info("步骤11.3: 检查Token有效性...");
        boolean isValid = TidalTokenUtils.isTokenValid();
        Assertions.assertTrue(isValid, "Token应该有效");
        log.info("✓ Token有效");
        
        // 11.4 获取剩余时间
        log.info("步骤11.4: 获取Token剩余时间...");
        long remainingSeconds = TidalTokenUtils.getTokenRemainingSeconds();
        log.info("✓ 剩余时间: {} 秒 ({} 分钟)", remainingSeconds, remainingSeconds / 60);
        Assertions.assertTrue(remainingSeconds > 3000, "Token剩余时间应大于50分钟");
        
        log.info("========== 测试11完成: Token工具类其他方法 ==========\n");
    }

    /**
     * 清理工作（可选）
     */
    @AfterAll
    public static void cleanup() {
        log.info("\n========== 所有测试完成 ==========");
        log.info("Token状态: {}", TidalTokenUtils.isTokenValid() ? "有效" : "无效");
        log.info("剩余时间: {} 秒", TidalTokenUtils.getTokenRemainingSeconds());
        log.info("====================================\n");
    }
}
