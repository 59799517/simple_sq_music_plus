# MusicBrainz 插件

## 简介

MusicBrainz 插件是一个基于 [MusicBrainz](https://musicbrainz.org/) 开放音乐数据库的搜索和查询插件。它使用 ISRC（国际标准录音代码）作为歌曲的唯一标识符，提供音乐元数据查询功能。

**注意：** 此插件仅提供音乐信息查询功能，不支持下载和歌词获取。

## 特性

- ✅ 通过歌曲名称搜索
- ✅ 通过 ISRC 查询歌曲详情
- ✅ 查询艺术家信息
- ✅ 查询专辑信息
- ❌ 不支持下载功能
- ❌ 不提供歌词

## 配置

在 `application-musicbrainz.yml` 中配置 MusicBrainz API：

```yaml
musicbrainz:
  id: musicbrainz-v1
  name: MusicBrainz
  islogin: false
  baseUrl: https://musicbrainz.org/ws/2
  searchUrl: ${musicbrainz.baseUrl}/recording?query=#{query}&fmt=json&offset=#{offset}&limit=#{limit}
  recordingUrl: ${musicbrainz.baseUrl}/recording/#{recordingId}?inc=artists+releases+isrcs&fmt=json
  artistUrl: ${musicbrainz.baseUrl}/artist/#{artistId}?fmt=json
  releaseUrl: ${musicbrainz.baseUrl}/release/#{releaseId}?inc=artists+recordings+media&fmt=json
  isrcSearchUrl: ${musicbrainz.baseUrl}/isrc/#{isrc}?inc=artists+releases&fmt=json
```

## 使用说明

### 1. 激活插件

在 `application.yml` 中添加 `musicbrainz` 到 active profiles：

```yaml
spring:
  profiles:
    active: kw,mg,qq,netease,kg,tidal,musicbrainz
```

### 2. 搜索歌曲

使用歌曲名称进行搜索：

```java
SearchKeyData searchKeyData = new SearchKeyData();
searchKeyData.setSearchkey("歌曲名称");
searchKeyData.setPageIndex(1);
searchKeyData.setPageSize(20);

PlugSearchResult<PlugSearchMusicResult> result = musicBrainzSearchHander.querySongByName(searchKeyData);
```

### 3. 通过 ISRC 查询歌曲

```java
// songId 实际上是 ISRC 代码
Music music = musicBrainzSearchHander.querySongById("USRC17607839");
```

### 4. 查询艺术家信息

```java
Artists artist = musicBrainzSearchHander.queryArtistById("artist-id");
```

### 5. 查询专辑信息

```java
Album album = musicBrainzSearchHander.queryAlbumById("release-id");
List<Music> tracks = album.getMusics();
```

## API 限制

MusicBrainz API 有使用限制：
- 每秒最多 1 个请求
- 需要设置合理的 User-Agent
- 建议实现请求频率控制

## 数据结构

### 歌曲 ID

MusicBrainz 插件使用 **ISRC**（国际标准录音代码）作为歌曲的唯一标识符。如果录音没有 ISRC，则使用 MusicBrainz 的 recording ID。

ISRC 格式示例：`USRC17607839`

### 搜索结果

搜索返回的结果包含：
- 歌曲名称
- 艺术家信息
- 专辑信息
- ISRC 代码
- 时长（毫秒）

## 注意事项

1. **不提供下载功能**：MusicBrainz 是元数据数据库，不提供音频文件下载
2. **不提供歌词**：MusicBrainz 不包含歌词信息
3. **ISRC 优先**：查询时优先使用 ISRC 作为歌曲标识
4. **API 限流**：请注意遵守 MusicBrainz 的 API 使用规范
5. **数据完整性**：某些录音可能缺少 ISRC 或部分元数据

## 开发者

@Created by SQ

## 参考资源

- [MusicBrainz 官方网站](https://musicbrainz.org/)
- [MusicBrainz API 文档](https://musicbrainz.org/doc/MusicBrainz_API)
- [ISRC 维基百科](https://en.wikipedia.org/wiki/International_Standard_Recording_Code)
