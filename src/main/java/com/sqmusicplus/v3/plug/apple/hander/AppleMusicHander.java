//package com.sqmusicplus.v3.plug.apple.hander;
//
//import cn.hutool.core.collection.ListUtil;
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.sqmusicplus.v3.base.entity.DownloadInfo;
//import com.sqmusicplus.v3.base.enums.PlugBrType;
//import com.sqmusicplus.v3.base.enums.SetConfigEnum;
//import com.sqmusicplus.v3.config.SqConfigCache;
//import com.sqmusicplus.v3.config.exception.SQException;
//import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
//import com.sqmusicplus.v3.plug.apple.config.AppleConfig;
//import com.sqmusicplus.v3.plug.apple.entity.TipResult;
//import com.sqmusicplus.v3.plug.apple.entity.searchsongresult.Results;
//import com.sqmusicplus.v3.plug.apple.entity.searchsongresult.SearchSongResult;
//import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
//import com.sqmusicplus.v3.plug.entity.*;
//import com.sqmusicplus.v3.utils.LrcUtils;
//import com.sqmusicplus.v3.utils.OkHttpUtils;
//import com.sqmusicplus.v3.utils.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//
///**
// * @Classname AppleMusicHander
// * @Description TODO
// * @Version 1.0.0
// * @Date 2025/10/11 11:16
// * @Created by SQ
// */
//@Component("appleMusicHander")
//@Slf4j
//public class AppleMusicHander extends SearchHanderAbstract {
//
//    @Autowired
//    private AppleConfig appleConfig;
//    @Override
//    public AppleConfig getConfig() {
//        return appleConfig;
//    }
//
//    @Override
//    public String getPlugName() {
//        return "apple";
//    }
//
//    /**
//     * 构建请求
//     * @param url
//     * @param token
//     * @param mediausertoken
//     * @param origin
//     * @return
//     */
//    public  Request buildRequest(String url, String token, String mediausertoken, String origin, HashMap<String, String> params) {
//        StringBuilder urlBuilder = new StringBuilder(url);
//        if (params != null) {
//            urlBuilder.append("?");
//            try {
//                for (Map.Entry<String, String> entry : params.entrySet()) {
//                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
//                            append("=").
//                            append(URLEncoder.encode(entry.getValue(), "utf-8")).
//                            append("&");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
//        }
//        Request request = new Request.Builder()
//                .url(urlBuilder.toString())
//                .addHeader("authorization", "Bearer " + token)
//                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0")
//                .addHeader("Accept", "application/json")
//                .addHeader("Accept-Language", "en-US,en;q=0.5")
//                // 移除Accept-Encoding，让OkHttp自动处理解压
//                .addHeader("content-type", "application/json")
//                .addHeader("Media-User-Token", mediausertoken)
//                .addHeader("x-apple-renewal", "true")
//                .addHeader("DNT", "1")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Sec-Fetch-Dest", "empty")
//                .addHeader("Sec-Fetch-Mode", "cors")
//                .addHeader("Sec-Fetch-Site", "same-site")
//                .addHeader("origin", origin)
//                .build();
//
//        return request;
//    }
//    Request buildRequest(String url, String token, String mediausertoken, String origin){
//        return buildRequest(url, token, mediausertoken, origin, null);
//    }
//    /**
//     * 结果处理
//     * @param response
//     * @return
//     */
//    public String handleResult(Response response) {
//
//        //如果是 401 错误，重新获取token
//        if (response.code() == 401) {
//            throw new SQException("token失效请使用cookie重新刷新token");
//            //重新获取token 如果获取失败则返回错误信息
//
//        }
//        //如果是200
//        if (response.code() == 200) {
//            try {
//                return response.body().string();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        //否则返回错误信息
//        return null;
//    }
//
//
//    @Override
//    public List<String> searchTip(String searchKey) {
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/songs/905206660",
//                token,
//                mediausertoken,
//                origin);
//        try (Response response = OkHttpUtils.newCall(request).execute()) {
//
//            // 获取响应内容
//            String responseBody = handleResult(response);
//            // 尝试解析为JSON
//            try {
//                TipResult tipResult = JSONObject.parseObject(responseBody, TipResult.class);
//                Boolean success = tipResult.getSuccess();
//                if (success) {
//                    TipResult.HintsDTO.ResultsDTO results = tipResult.getHints().getResults();
//                    return results.getTerms();
//                }
//            } catch (Exception e) {
//                // 如果不是有效的JSON，直接打印内容
//                log.error("API 响应前500字符: " + responseBody.substring(0, Math.min(500, responseBody.length())));
//                log.error("完整API响应: " + responseBody);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return new ArrayList<>();
//
//    }
//
//    @Override
//    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
//        int total=999999;
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//
//        // 添加limit和offset参数，从searchKeyData中获取pageIndex和pageSize
//        // limit默认值为10，最大值为25
//        int limit = searchKeyData.getPageSize() != null ? Math.min(searchKeyData.getPageSize(), 25) : 10;
//        // offset计算方式：(pageIndex - 1) * pageSize
//        int offset = searchKeyData.getPageIndex() != null ? (searchKeyData.getPageIndex() - 1) * limit : 0;
//        //空格变为+
//        String searchkey = searchKeyData.getSearchkey();
//        searchkey = searchkey.replaceAll(" ", "+");
//
//        params.put("limit", String.valueOf(limit));
//        params.put("offset", String.valueOf(offset));
//        params.put("types", "songs");
//        params.put("term", searchkey);
//        params.put("include", "songs,artists,albums");
//
//        List<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
//
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/search",
//                token,
//                mediausertoken,
//                origin,
//                params);
//        try (Response response = OkHttpUtils.newCall(request).execute()) {
//
//            String responseBody = handleResult(response);
//            JSONObject jsonObject = JSONObject.parseObject(responseBody);
//                ArrayList<String> songids = new ArrayList<>();
//                JSONObject results = jsonObject.getJSONObject("results");
//                String next = results.getJSONObject("songs").getString("next");
//                if (StringUtils.isBlank(next)) {
//                     total = searchKeyData.getPageSize()* searchKeyData.getPageIndex();
//                }
//                JSONArray songs = results.getJSONObject("songs").getJSONArray("data");
//                for (int i = 0; i < songs.size(); i++) {
//                    JSONObject song = songs.getJSONObject(i);
//                    songids.add(song.getString("id"));
//                }
//                ArrayList<Music> musics = querySongByIds(songids);
//                for (Music music : musics) {
//                    PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult();
//                    plugSearchMusicResult.setId(music.getId());
//                    plugSearchMusicResult.setName(music.getMusicName());
//                    plugSearchMusicResult.setArtistName(music.getMusicArtists());
//                    plugSearchMusicResult.setArtistids(music.getArtistsIds());
//                    plugSearchMusicResult.setPic(music.getMusicImage());
//                    plugSearchMusicResult.setAlbumName(music.getMusicAlbum());
//                    plugSearchMusicResult.setAlbumid(music.getAlbumId());
//                    plugSearchMusicResult.setLyric(music.getMusicLyric());
//                    plugSearchMusicResult.setLyricId(music.getMusicLyricTrans());
//                    plugSearchMusicResult.setPlugName(getPlugName());
//                    plugSearchMusicResult.setDuration(music.getMusicDuration().toString());
//                    plugSearchMusicResult.setBrTypes(music.getBits());
//                    plugSearchMusicResults.add(plugSearchMusicResult);
//                }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
//        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
//                .setSearchSize(searchKeyData.getPageSize())
//                .setPlugName(getPlugName())
//                .setSearchTotal(total)
//                .setSearchKeyWork(searchKeyData.getSearchkey())
//                .setRecords(plugSearchMusicResults);
//        plugSearchResult.setPlugName(getPlugName());
//        return plugSearchResult;
//
//    }
//
//    @Override
//    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
//        int total = 999999;
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//
//        // 添加limit和offset参数，从searchKeyData中获取pageIndex和pageSize
//        // limit默认值为10，最大值为25
//        int limit = searchKeyData.getPageSize() != null ? Math.min(searchKeyData.getPageSize(), 25) : 10;
//        // offset计算方式：(pageIndex - 1) * pageSize
//        int offset = searchKeyData.getPageIndex() != null ? (searchKeyData.getPageIndex() - 1) * limit : 0;
//        //空格变为+
//        String searchkey = searchKeyData.getSearchkey();
//        searchkey = searchkey.replaceAll(" ", "+");
//
//        params.put("limit", String.valueOf(limit));
//        params.put("offset", String.valueOf(offset));
//        params.put("types", "artists");
//        params.put("term", searchkey);
//        params.put("include", "songs,artists,albums");
//
//        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/search",
//                token,
//                mediausertoken,
//                origin,
//                params);
//                try (Response response = OkHttpUtils.newCall(request).execute()) {
//                    String responseBody = handleResult(response);
//                    JSONObject jsonObject = JSONObject.parseObject(responseBody);
//
//                    JSONObject artists = jsonObject.getJSONObject("results").getJSONObject("artists");
//                    String next = artists.getString("next");
//                    if (StringUtils.isBlank(next)) {
//                        total = searchKeyData.getPageSize()* searchKeyData.getPageIndex();
//                    }
//                    JSONArray artistsData = artists.getJSONArray("data");
//                    for (int i = 0; i < artistsData.size(); i++) {
//                        JSONObject artist = artistsData.getJSONObject(i);
//                        PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult();
//                        plugSearchArtistResult.setArtistid(artist.getString("id"));
//                        plugSearchArtistResult.setArtistName(artist.getString("name"));
//                        plugSearchArtistResult.setPic(artist.getJSONObject("attributes").getJSONObject("artwork").getString("url").replace("{w}", "800").replace("{h}", "800"));
//                        plugSearchArtistResult.setPlugName(getPlugName());
//                        plugSearchArtistResult.setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artist)));
//                        plugSearchArtistResults.add(plugSearchArtistResult);
//                    }
//                }catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
//        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
//                .setSearchSize(searchKeyData.getPageSize())
//                .setPlugName(getPlugName())
//                .setSearchTotal(total)
//                .setSearchKeyWork(searchKeyData.getSearchkey())
//                .setRecords(plugSearchArtistResults);
//        plugSearchResult.setPlugName(getPlugName());
//        return plugSearchResult;
//
//    }
//
//    @Override
//    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
//        int total = 999999;
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//
//        // 添加limit和offset参数，从searchKeyData中获取pageIndex和pageSize
//        // limit默认值为10，最大值为25
//        int limit = searchKeyData.getPageSize() != null ? Math.min(searchKeyData.getPageSize(), 25) : 10;
//        // offset计算方式：(pageIndex - 1) * pageSize
//        int offset = searchKeyData.getPageIndex() != null ? (searchKeyData.getPageIndex() - 1) * limit : 0;
//        //空格变为+
//        String searchkey = searchKeyData.getSearchkey();
//        searchkey = searchkey.replaceAll(" ", "+");
//
//        params.put("limit", String.valueOf(limit));
//        params.put("offset", String.valueOf(offset));
//        params.put("types", "albums");
//        params.put("term", searchkey);
//        params.put("include", "songs,artists,albums");
//
//
//        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/search",
//                token,
//                mediausertoken,
//                origin,
//                params);
//        try (Response response = OkHttpUtils.newCall(request).execute()) {
//            String responseBody = handleResult(response);
//            JSONObject jsonObject = JSONObject.parseObject(responseBody);
//
//            JSONObject albums = jsonObject.getJSONObject("results").getJSONObject("albums");
//            String next = albums.getString("next");
//            if (StringUtils.isBlank(next)) {
//                total = searchKeyData.getPageSize()* searchKeyData.getPageIndex();
//            }
//            JSONArray artistsData = albums.getJSONArray("data");
//            for (int i = 0; i < artistsData.size(); i++) {
//                JSONObject artist = artistsData.getJSONObject(i);
//                PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult();
//                plugSearchAlbumResult.setArtistid(artist.getString("id"));
//                plugSearchAlbumResult.setArtistName(artist.getString("name"));
//                plugSearchAlbumResult.setPic(artist.getJSONObject("attributes").getJSONObject("artwork").getString("url").replace("{w}", "800").replace("{h}", "800"));
//                plugSearchAlbumResult.setPlugName(getPlugName());
//                plugSearchAlbumResult.setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artist)));
//                plugSearchAlbumResults.add(plugSearchAlbumResult);
//            }
//        }catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
//        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
//                .setSearchSize(searchKeyData.getPageSize())
//                .setPlugName(getPlugName())
//                .setSearchTotal(total)
//                .setSearchKeyWork(searchKeyData.getSearchkey())
//                .setRecords(plugSearchAlbumResults);
//        plugSearchResult.setPlugName(getPlugName());
//        return plugSearchResult;
//
//    }
//
//
//    public ArrayList<Music> querySongByIds(List<String> SongIds) {
//        if (SongIds== null|| SongIds.isEmpty()){
//            return new ArrayList<>();
//        }
//        ArrayList<Music> musics = new ArrayList<>();
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("ids",String.join(",", SongIds));
//        params.put("include","lyrics,albums,artists");
//        params.put("extend", "extendedAssetUrls");
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/songs",
//                token,
//                mediausertoken,
//                origin,
//                params);
//        try (Response response = OkHttpUtils.newCall(request).execute()) {
//            if (response.isSuccessful()){
//                String responseBody = handleResult(response);
//                JSONObject jsonObject = JSONObject.parseObject(responseBody);
//                JSONArray data = jsonObject.getJSONArray("data");
//                for (int i = 0; i < data.size(); i++) {
//                    JSONObject song = data.getJSONObject(i);
//                    String id = song.getString("id");
//                    String songName = song.getJSONObject("attributes").getString("name");
//                    String duration = song.getJSONObject("attributes").getString("durationInMillis");
//                    JSONObject album = song.getJSONObject("relationships").getJSONObject("albums").getJSONArray("data").getJSONObject(0);
//                    String albumId = album.getString("id");
//                    String albumName = album.getJSONObject("attributes").getString("name");
//                    JSONArray artists = song.getJSONObject("relationships").getJSONObject("artists").getJSONArray("data");
//                    ArrayList<String> artistIds = new ArrayList<>();
//                    ArrayList<String> artistNames = new ArrayList<>();
//                    String imamge = song.getJSONObject("relationships").getJSONObject("artwork").getString("url");
//                    imamge = imamge.replaceAll("\\{w\\}", "800").replaceAll("\\{h\\}", "800");
//                    Boolean aBoolean = song.getJSONObject("attributes").getBoolean("hasLyrics");
//                    String lrc = "";
//                    for (int i1 = 0; i1 < artists.size(); i1++) {
//                        JSONObject artist = artists.getJSONObject(i1);
//                        artistIds.add(artist.getString("id"));
//                        artistNames.add(artist.getJSONObject("attributes").getString("name"));
//                    }
//
//                    if (aBoolean){
//                        JSONObject lyrics = song.getJSONObject("relationships").getJSONObject("lyrics");
//                        String string = lyrics.getJSONArray("data").getJSONObject(0).getString("ttml");
//                         lrc = LrcUtils.convertTtmlToLrc(string,albumName,String.join("&",artistNames),songName);
//                    }
//
//
//                    Music music = new Music()
//                            .setId(id)
//                            .setMusicImage(imamge)
//                            .setMusicLyric(lrc)
//                            .setMusicAlbum(albumName)
//                            .setMusicArtists(artistIds)
//                            .setMusicName(songName)
//                            .setMusicDuration(Long.parseLong(duration))
//                            .setAlbumId(albumId)
//                            .setDataInfo(JSON.parseObject(JSONObject.toJSONString(song)))
//                            .setBits(PlugBrType.getAppleAllType())
//                            .setArtistsIds(artistNames);
//                    musics.add( music);
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return musics;
//    }
//
//    @Override
//    public Music querySongById(String SongId) {
//        if (SongId== null|| SongId.isEmpty()){
//            return null;
//        }
//        return  querySongByIds(List.of(SongId)).get(0);
//    }
//
//    @Override
//    public Music querySongById(DownloadInfo downloadInfo) {
//        return querySongById(downloadInfo.getDownloadMusicId());
//
//    }
//
//    @Override
//    public Artists queryArtistById(String artistId) {
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("include","lyrics,albums,artists,flavor");
//        params.put("extend", "extendedAssetUrls");
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/artists/" + artistId,
//                token,
//                mediausertoken,
//                origin,
//                params);
//        try (Response response = OkHttpUtils.newCall(request).execute()){
//            String responseBody = handleResult(response);
//            JSONObject jsonObject = JSONObject.parseObject(responseBody);
//            Artists artists = new Artists();
//            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
//            JSONObject attributes = data.getJSONObject("attributes");
//            JSONObject editorialNotes = attributes.getJSONObject("editorialNotes");
//            JSONObject artwork = attributes.getJSONObject("artwork");
//            String imamge = artwork.getString("url");
//            imamge = imamge.replaceAll("\\{w\\}", "800").replaceAll("\\{h\\}", "800");
//            artists.setMusicArtistsName(attributes.getString("name"))
//                    .setMusicArtistsAlias(editorialNotes.getString("name"))
//                    .setMusicArtistsPhoto(imamge)
//                    .setMusicArtistsDescribe(editorialNotes.getString("standard"))
//                    .setId(artistId)
//                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(data)));
//            return artists;
//        }catch (IOException e){
//            e.printStackTrace();
////            throw new RuntimeException(e);
//        }
//        return null;
//    }
//
//    @Override
//    public Album queryAlbumById(String albumId) {
//
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("include","lyrics,albums,artists,flavor,songs");
//        params.put("extend", "extendedAssetUrls");
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/albums/" + albumId,
//                token,
//                mediausertoken,
//                origin,
//                params);
//        try (Response response = OkHttpUtils.newCall(request).execute()){
//            String responseBody = handleResult(response);
//            JSONObject jsonObject = JSONObject.parseObject(responseBody);
//
//            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
//            JSONObject attributes = data.getJSONObject("attributes");
//            JSONObject editorialNotes = attributes.getJSONObject("editorialNotes");
//            JSONObject artwork = attributes.getJSONObject("artwork");
//            String imamge = artwork.getString("url");
//            imamge = imamge.replaceAll("\\{w\\}", "800").replaceAll("\\{h\\}", "800");
//            String alubNme = attributes.getString("name");
//            String describe = editorialNotes.getString("standard");
//            String artistName = data.getJSONObject("relationships").getJSONObject("artists").getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("name");
//            String artistId = data.getJSONObject("relationships").getJSONObject("artists").getJSONArray("data").getJSONObject(0).getString("id");
//            JSONArray tracks = data.getJSONObject("relationships").getJSONObject("tracks").getJSONArray("data");
//
//            String next = data.getJSONObject("relationships").getJSONObject("tracks").getString("next");
//            boolean hasNext = StringUtils.isNotBlank(next);
//            ArrayList<Music> musics = tracksToMusic(tracks);
//            while (hasNext){
//                Request request1 = buildRequest(next,
//                        token,
//                        mediausertoken,
//                        origin);
//                        try (Response response1 = OkHttpUtils.newCall(request1).execute()){
//                            String responseBody1 = handleResult(response1);
//                            JSONObject jsonObject1 = JSONObject.parseObject(responseBody1);
//                            JSONArray nexttracks = jsonObject1.getJSONArray("data");
//                            next = jsonObject1.getString("next");
//                            hasNext = StringUtils.isNotBlank(jsonObject1.getString("next"));
//                            ArrayList<Music> music1 = tracksToMusic(nexttracks);
//                            musics.addAll(music1);
//                        }catch ( IOException e){
//                            hasNext=false;
//                            e.printStackTrace();
//                        }
//            }
//
//            Album album = new Album()
//                    .setAlbumId(albumId)
//                    .setAlbumName(alubNme)
//                    .setAlbumTime(attributes.getString("releaseDate"))
//                    .setAlbumDescribe(describe)
//                    .setAlbumArtist(artistName)
//                    .setAlbumArtistId(artistId)
//                    .setAlbumImg(imamge)
//                    .setMusics(musics)
//                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(data)));
//                    return album;
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private  ArrayList<Music> tracksToMusic(JSONArray tracks) {
//        ArrayList<Music> musics = new ArrayList<>();
//        for (int i = 0; i < tracks.size(); i++) {
//            JSONObject song = tracks.getJSONObject(i);
//            String songid = song.getString("id");
//            String songName = song.getJSONObject("attributes").getString("name");
//            String songduration = song.getJSONObject("attributes").getString("durationInMillis");
//            JSONObject album = song.getJSONObject("relationships").getJSONObject("albums").getJSONArray("data").getJSONObject(0);
//            String songalbumId = album.getString("id");
//            String songalbumName = album.getJSONObject("attributes").getString("name");
//            JSONArray artists = song.getJSONObject("relationships").getJSONObject("artists").getJSONArray("data");
//            ArrayList<String> songartistIds = new ArrayList<>();
//            ArrayList<String> songartistNames = new ArrayList<>();
//            String songimamge = song.getJSONObject("relationships").getJSONObject("artwork").getString("url");
//            songimamge = songimamge.replaceAll("\\{w\\}", "800").replaceAll("\\{h\\}", "800");
//            for (int i1 = 0; i1 < artists.size(); i1++) {
//                JSONObject artist = artists.getJSONObject(i1);
//                songartistIds.add(artist.getString("id"));
//                songartistNames.add(artist.getJSONObject("attributes").getString("name"));
//            }
//            Music music = new Music()
//                    .setId(songid)
//                    .setMusicImage(songimamge)
//                    .setMusicLyric("")
//                    .setMusicAlbum(songalbumName)
//                    .setMusicArtists(songartistIds)
//                    .setMusicName(songName)
//                    .setMusicDuration(Long.parseLong(songduration))
//                    .setAlbumId(songalbumId)
//                    .setDataInfo(JSON.parseObject(JSONObject.toJSONString(song)))
//                    .setBits(PlugBrType.getAppleAllType())
//                    .setArtistsIds(songartistNames);
//            musics.add( music);
//        }
//        return musics;
//    }
//
//    @Override
//    public String queryLyric(String SongId) {
//        Music music = querySongById(SongId);
//        if (music!=null){
//            if (StringUtils.isNotBlank(music.getMusicLyric())){
//                return music.getMusicLyric();
//            }else{
//                String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//                String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//                String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//                String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//                HashMap<String, String> params = new HashMap<>();
//                params.put("extend","ttmlLocalizations");
//                Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/songs/" + SongId+"/syllable-lyrics",
//                        token,
//                        mediausertoken,
//                        origin,
//                        params);
//                try (Response response = OkHttpUtils.newCall(request).execute()){
//                    String responseBody = handleResult(response);
//                    JSONObject jsonObject = JSONObject.parseObject(responseBody);
//                    JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
//                    JSONObject attributes = data.getJSONObject("attributes");
//                    String ttmlLocalizations = attributes.getString("ttmlLocalizations");
//                    String s = LrcUtils.convertTtmlToLrc(ttmlLocalizations, music.getMusicAlbum(), String.join("&", music.getMusicArtists()), music.getMusicName());
//                    return s;
//                }catch (IOException e){
//                    e.printStackTrace();
//
//                }
//            }
//        }
//        return "";
//
//    }
//
//    @Override
//    public List<Album> getAlbumsByArtist(String artistId) {
//        boolean isnext = false;
//        String next = "";
//
//        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_STOREFRONT);
//        String token = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_TOKEN);
//        String mediausertoken = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_MEDIAUSERTOKEN);
//        String origin = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_APPLE_ORIGIN);
//        HashMap<String, String> params = new HashMap<>();
//        params.put("include","lyrics,albums,artists,flavor");
//        params.put("extend", "extendedAssetUrls");
//        Request request = buildRequest(getConfig().getBaseUrl() + "/v1/catalog/" + sqConfigValue + "/artists/" + artistId,
//                token,
//                mediausertoken,
//                origin,
//                params);
//        try (Response response = OkHttpUtils.newCall(request).execute()){
//            String responseBody = handleResult(response);
//            JSONObject jsonObject = JSONObject.parseObject(responseBody);
//            JSONObject data = jsonObject.getJSONArray("data").getJSONObject(0);
//
//            JSONObject relationships = data.getJSONObject("relationships");
//            JSONObject albumsData = relationships.getJSONObject("albums");
//            next = albumsData.getString("next");
//            if (StringUtils.isNotBlank(next)){
//                isnext = true;
//            }
//            JSONArray albumsList =albumsData.getJSONArray("data");
//            String artistName = data.getJSONObject("attributes").getString("name");
//
//            ArrayList<Album> albumList = toAlbumList(artistId, albumsList, artistName);
//            while (isnext){
//                Request nextRequest = buildRequest(getConfig().getBaseUrl() + next,
//                        token,
//                        mediausertoken,
//                        origin,
//                        params);
//                try (Response nextresponse = OkHttpUtils.newCall(nextRequest).execute()){
//                    String nextresponseBody = handleResult(nextresponse);
//                    JSONObject nextjsonObject = JSONObject.parseObject(nextresponseBody);
//                    next = nextjsonObject.getString("next");
//                    if (StringUtils.isNotBlank(next)){
//                        isnext = true;
//                    }else{
//                        isnext = false;
//                    }
//                    JSONArray nextdata = nextjsonObject.getJSONArray("data");
//                    ArrayList<Album> albumList1 = toAlbumList(artistId, nextdata, artistName);
//                    albumList.addAll(albumList1);
//                }
//            }
//            return albumList;
//        }catch (IOException e){
//            e.printStackTrace();
////            throw new RuntimeException(e);
//        }
//        return null;
//
//    }
//
//    private  ArrayList<Album> toAlbumList(String artistId, JSONArray nextdata, String artistName) {
//        ArrayList<Album> albums = new ArrayList<>();
//        for (int i = 0; i < nextdata.size(); i++) {
//
//            JSONObject albumData = nextdata.getJSONObject(i);
//            String albumId = albumData.getString("id");
//            String alubNme = albumData.getJSONObject("attributes").getString("name");
//            JSONObject attributes = albumData.getJSONObject("attributes");
//            String describe = attributes.getJSONObject("editorialNotes").getString("standard");
//            String imamge = albumData.getJSONObject("attributes").getJSONArray("artwork").getJSONObject(0).getString("url");
//            imamge = imamge.replaceAll("\\{w\\}", "800").replaceAll("\\{h\\}", "800");
//            Album album = new Album()
//                    .setAlbumId(albumId)
//                    .setAlbumName(alubNme)
//                    .setAlbumTime(attributes.getString("releaseDate"))
//                    .setAlbumDescribe(describe)
//                    .setAlbumArtist(artistName)
//                    .setAlbumArtistId(artistId)
//                    .setAlbumImg(imamge)
//                    .setDataInfo(albumData);
//            albums.add(album);
//        }
//        return albums;
//    }
//
//    @Override
//    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
//        Album album = queryAlbumById(albumsId);
//        return album.getMusics();
//    }
//
//    @Override
//    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
//        return null;
//    }
//
//    @Override
//    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
//        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
//        ArrayList<DownloadInfo> downloadEntities = new ArrayList<>();
//        musiclist.forEach(md -> {
//            if (isAudioBook) {
//                md.setMusicAlbum(albumName).setMusicArtists(artists);
//            }
//            DownloadInfo downloadInfo = super.musicToDownloadInfo(md, brType, isAudioBook);
//            downloadEntities.add(downloadInfo);
//        });
//        return downloadEntities;
//    }
//
//    @Override
//    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
//        return downloadArtistAllAlbum(artistId, brType);
//    }
//
//    @Override
//    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
//        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
//        List<Album> albumsByArtist = getAlbumsByArtist(artistId);
//        List<String> collect = albumsByArtist.stream().map(e -> e.getAlbumId()).collect(Collectors.toList());
//        collect.forEach(e->downloadInfos.addAll(downloadAlbum(e,brType,null,false,null)));
//        return downloadInfos;
//    }
//
//
//}
