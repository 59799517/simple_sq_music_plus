package com.sqmusicplus.v3.plug.musicbrainz;

import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchResult;
import com.sqmusicplus.v3.plug.entity.SearchKeyData;
import com.sqmusicplus.v3.plug.musicbrainz.hander.MusicBrainzSearchHander;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

/**
 * MusicBrainz 插件测试类
 * 
 * @author SQ
 * @version 1.0.0
 * @date 2026/5/23
 */
@Slf4j
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.freemarker.enabled=false",
                "spring.profiles.active=kw,mg,qq,netease,kg,tidal,musicbrainz" 
        }
)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MusicBrainzTest {

    @Autowired
    private MusicBrainzSearchHander musicBrainzSearchHander;

    /**
     * 测试通过歌曲名称搜索
     */
    @Test
    @Order(1)
    public void testSearchSongByName() {
        log.info("========== 开始测试：通过歌曲名称搜索 ==========");
        
        SearchKeyData searchKeyData = new SearchKeyData();
        searchKeyData.setSearchkey("爱我还是他");
        searchKeyData.setPageIndex(1);
        searchKeyData.setPageSize(10);
        
        try {
            PlugSearchResult<PlugSearchMusicResult> result = musicBrainzSearchHander.querySongByName(searchKeyData);
            
            log.info("搜索成功！");
            log.info("搜索结果总数: {}", result.getSearchTotal());
            log.info("当前页码: {}", result.getSearchIndex());
            log.info("每页数量: {}", result.getSearchSize());
            
            if (result.getRecords() != null && !result.getRecords().isEmpty()) {
                log.info("找到 {} 首歌曲:", result.getRecords().size());
                for (int i = 0; i < result.getRecords().size(); i++) {
                    PlugSearchMusicResult music = result.getRecords().get(i);
                    log.info("[{}] id={} | name={} | artistName={} | artistids={} | albumName={} | albumid={} | pic={} | duration={}ms | lyric={} | lyricId={} | plugName={} | brTypes={} | dataInfo={}", 
                            i + 1,
                            music.getId(),
                            music.getName(),
                            music.getArtistName(),
                            music.getArtistids(),
                            music.getAlbumName(),
                            music.getAlbumid(),
                            music.getPic(),
                            music.getDuration(),
                            music.getLyric(),
                            music.getLyricId(),
                            music.getPlugName(),
                            music.getBrTypes(),
                            music.getDataInfo());
                }
            } else {
                log.warn("未找到任何歌曲");
            }
        } catch (Exception e) {
            log.error("搜索失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 测试通过 ISRC 查询歌曲详情
     */
    @Test
    @Order(2)
    public void testQuerySongByIsrc() {
        log.info("========== 开始测试：通过 ISRC 查询歌曲详情 ==========");
        
        // 使用一个真实的 ISRC 示例（Queen - Bohemian Rhapsody）
        String isrc = "7f85b708-8392-4d9f-8566-0597b0e3e542";
        
        try {
            log.info("查询 ISRC: {}", isrc);
            Music music = musicBrainzSearchHander.querySongById(isrc);
            
            if (music != null) {
                log.info("查询成功！");
                log.info("歌曲信息: {}", JSONObject.toJSONString(music));
            } else {
                log.warn("未找到 ISRC 对应的歌曲: {}", isrc);
            }
        } catch (Exception e) {
            log.error("查询失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 测试查询艺术家信息
     */
    @Test
    @Order(3)
    public void testQueryArtistById() {
        log.info("========== 开始测试：查询艺术家信息 ==========");
        
        // Queen 乐队 MusicBrainz ID (从搜索 API 验证)
        String artistId = "7f85b708-8392-4d9f-8566-0597b0e3e542";
        
        try {
            log.info("查询艺术家 ID: {}", artistId);
            Artists artist = musicBrainzSearchHander.queryArtistById(artistId);
            
            if (artist != null) {
                log.info("查询成功！");
                log.info("艺术家信息: {}", JSONObject.toJSONString(artist));
            } else {
                log.warn("未找到艺术家: {}", artistId);
            }
        } catch (Exception e) {
            log.error("查询失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 测试查询专辑信息
     */
    @Test
    @Order(4)
    public void testQueryAlbumById() {
        log.info("========== 开始测试：查询专辑信息 ==========");
        
        // A Night at the Opera (Queen) 从搜索 API 验证的 ID
        String albumId = "09a586bd-6f45-4f8c-9923-2d20333a13cf";
        
        try {
            log.info("查询专辑 ID: {}", albumId);
            Album album = musicBrainzSearchHander.queryAlbumById(albumId);
            
            if (album != null) {
                log.info("查询成功！");
                log.info("专辑信息: {}", JSONObject.toJSONString(album));
                if (album.getMusics() != null) {
                    log.info("歌曲数量: {}", album.getMusics().size());
                    log.info("歌曲列表:");
                    for (int i = 0; i < album.getMusics().size(); i++) {
                        Music track = album.getMusics().get(i);
                        log.info("歌曲 信息{}", JSONObject.toJSONString(track));
                    }
                }
            } else {
                log.warn("未找到专辑: {}", albumId);
            }
        } catch (Exception e) {
            log.error("查询失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 测试获取专辑中的歌曲列表
     */
    @Test
    @Order(5)
    public void testGetAlbumSongs() {
        log.info("========== 开始测试：获取专辑歌曲列表 ==========");
        
        String albumId = "09a586bd-6f45-4f8c-9923-2d20333a13cf";
        
        try {
            List<Music> songs = musicBrainzSearchHander.getAlbumSongByAlbumsId(albumId);
            
            if (songs != null && !songs.isEmpty()) {
                log.info("获取成功！共 {} 首歌曲", songs.size());
                for (int i = 0; i < songs.size(); i++) {
                    Music song = songs.get(i);
                    log.info("歌曲 信息{}", JSONObject.toJSONString(song));
                }
            } else {
                log.warn("专辑中没有歌曲或专辑不存在");
            }
        } catch (Exception e) {
            log.error("获取失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    @Test
    @Order(6)
    public void testGetAlbumsByArtist() {
        log.info("========== 开始测试：获取艺术家所有专辑 ==========");
        
        String artistId = "38fe7fb3-2bde-4672-8016-2ba6f7d1808f";
        
        try {
            log.info("查询艺术家 {} 的所有专辑", artistId);
            List<Album> albums = musicBrainzSearchHander.getAlbumsByArtist(artistId);
            
            if (albums != null && !albums.isEmpty()) {
                log.info("获取成功！共 {} 张专辑", albums.size());
                for (int i = 0; i < albums.size(); i++) {
                    Album album = albums.get(i);
                    log.info("专辑 信息{}", JSONObject.toJSONString(album));
                }
            } else {
                log.warn("未找到艺术家的专辑");
            }
        } catch (Exception e) {
            log.error("获取失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 测试下载功能（应该返回空，因为 MusicBrainz 不提供下载）
     */
    @Test
    @Order(7)
    public void testDownloadNotSupported() {
        log.info("========== 开始测试：验证下载功能不支持 ==========");
        
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadMusicId("test-id");
        downloadInfo.setDownloadBrType("FLAC");
        
        try {
            var downloadUrl = musicBrainzSearchHander.getDownloadUrl(downloadInfo);
            
            if (downloadUrl == null) {
                log.info("✓ 符合预期：MusicBrainz 不提供下载功能");
            } else {
                log.warn("⚠ 警告：不应该返回下载链接");
            }
        } catch (Exception e) {
            log.error("测试异常", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 测试歌词功能（应该返回空字符串，因为 MusicBrainz 不提供歌词）
     */
    @Test
    @Order(8)
    public void testLyricNotSupported() {
        log.info("========== 开始测试：验证歌词功能不支持 ==========");
        
        try {
            String lyric = musicBrainzSearchHander.queryLyric("test-isrc");
            
            if (lyric == null || lyric.isEmpty()) {
                log.info("✓ 符合预期：MusicBrainz 不提供歌词功能");
            } else {
                log.warn("⚠ 警告：不应该返回歌词");
            }
        } catch (Exception e) {
            log.error("测试异常", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }

    /**
     * 综合测试：搜索并获取详情
     */
    @Test
    @Order(9)
    public void testComprehensiveWorkflow() {
        log.info("========== 开始综合测试：完整工作流程 ==========");
        
        try {
            // 1. 搜索歌曲
            log.info("步骤 1: 搜索歌曲 'Imagine' by John Lennon");
            SearchKeyData searchKeyData = new SearchKeyData();
            searchKeyData.setSearchkey("Imagine John Lennon");
            searchKeyData.setPageIndex(1);
            searchKeyData.setPageSize(5);
            
            PlugSearchResult<PlugSearchMusicResult> searchResult = 
                    musicBrainzSearchHander.querySongByName(searchKeyData);
            
            if (searchResult.getRecords() != null && !searchResult.getRecords().isEmpty()) {
                PlugSearchMusicResult firstResult = searchResult.getRecords().get(0);
                log.info("找到歌曲: {} - {}", firstResult.getName(), 
                        String.join(", ", firstResult.getArtistName()));
                
                // 2. 通过 ISRC 获取详细信息
                String isrc = firstResult.getId();
                log.info("步骤 2: 通过 ISRC 获取详细信息: {}", isrc);
                
                Music music = musicBrainzSearchHander.querySongById(isrc);
                if (music != null) {
                    log.info("歌曲详情:");
                    log.info("歌曲 信息{}", JSONObject.toJSONString(music));


                    // 3. 获取专辑信息
                    if (music.getAlbumId() != null && !music.getAlbumId().isEmpty()) {
                        log.info("步骤 3: 获取专辑信息: {}", music.getAlbumId());
                        Album album = musicBrainzSearchHander.queryAlbumById(music.getAlbumId());
                        
                        if (album != null) {
                            log.info("专辑: {}", album.getAlbumName());
                            log.info("艺术家: {}", album.getAlbumArtist());
                            log.info("歌曲数量: {}", 
                                    album.getMusics() != null ? album.getMusics().size() : 0);
                        }
                    }
                }
                
                log.info("\n✓ 综合测试完成！");
            } else {
                log.warn("未找到搜索结果");
            }
        } catch (Exception e) {
            log.error("综合测试失败", e);
        }
        
        log.info("========== 测试结束 ==========\n");
    }
}
