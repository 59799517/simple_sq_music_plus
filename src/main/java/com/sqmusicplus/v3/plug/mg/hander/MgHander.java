package com.sqmusicplus.v3.plug.mg.hander;

import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.mg.config.MgConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MgHander
 * @Description 咪咕处理器
 * @Version 1.0.0
 * @Date 2023/3/27 9:17
 * @Created by shang
 */

@Component("mgHander")
@Slf4j

public class MgHander extends SearchHanderAbstract {


    private static final long serialVersionUID = 1L;


    @Autowired
    private MgConfig mgConfig;


    @Override
    public <C> C getConfig() {
        return null;
    }

    @Override
    public String getPlugName() {
        return "mg";
    }

    @Override
    public PlugSearchResult querySongByName(SearchKeyData searchKeyData) {
        return null;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        return null;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        return null;
    }

    @Override
    public Music querySongById(String SongId) {
        return null;
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());

    }

    @Override
    public Artists queryArtistById(String artistId) {
        return null;
    }

    @Override
    public Album queryAlbumById(String albumId) {
        return null;
    }

    @Override
    public String queryLyric(String SongId) {
        return "";
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        return List.of();
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        return List.of();
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        return null;
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        return null;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        return List.of();
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        return List.of();
    }


}
