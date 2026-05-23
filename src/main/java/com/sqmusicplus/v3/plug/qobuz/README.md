# Qobuz 插件

这是一个使用第三方代理 API (api.zarz.moe) 开发的 Qobuz 音乐平台插件。

**注意：当前版本仅支持搜索和查询功能，不支持下载和 Artist 相关操作。**

## 目录结构

```
qobuz/
├── config/
│   └── QobuzConfig.java          # Qobuz 配置文件
├── entity/
│   ├── SearchTrackResult.java    # 搜索歌曲结果实体
│   ├── SearchArtistResult.java   # 搜索艺术家结果实体
│   ├── SearchAlbumResult.java    # 搜索专辑结果实体
│   ├── TrackInfoResult.java      # 歌曲详情结果实体
│   ├── AlbumInfoResult.java      # 专辑详情结果实体
│   ├── ArtistInfoResult.java     # 艺术家详情结果实体
│   ├── DownloadResult.java       # 下载链接结果实体
│   └── LyricResult.java          # 歌词结果实体
├── enums/
│   └── QobuzSearchType.java      # 搜索类型枚举
└── hander/
    └── QobuzSearchHander.java    # Qobuz 搜索处理器
```

## 功能特性

### ✅ 支持的功能
- 搜索歌曲
- 搜索专辑
- 获取歌曲详情
- 获取专辑详情（包含曲目列表）

### ❌ 不支持的功能
- Artist 搜索/详情（代理 API 未提供）
- 下载链接获取（代理 API 未提供）
- 歌词获取（代理 API 未提供）

## 配置说明

### 1. 配置文件

已在 `src/main/resources/` 目录下创建 [application-qobuz.yml](file:///D:/code/simple_sq_musuc_plus/src/main/resources/application-qobuz.yml) 配置文件。

### 2. 配置参数

```yaml
qobuz:
  id: qobuz-v1
  name: Qobuz
  islogin: false  # 不需要登录
  baseUrl: https://api.zarz.moe/v1/qbz
  appId: 798273057  # 您的 App ID
  searchTrackUrl: ${qobuz.baseUrl}/track/search?query=#{query}&limit=#{limit}&app_id=#{appId}
  searchAlbumUrl: ${qobuz.baseUrl}/album/search?query=#{query}&limit=#{limit}&app_id=#{appId}
  trackInfoUrl: ${qobuz.baseUrl}/track/get?track_id=#{trackId}&app_id=#{appId}
  albumInfoUrl: ${qobuz.baseUrl}/album/get?album_id=#{albumId}&app_id=#{appId}
```

### 3. 在主配置中引入

在 `application.yml` 中添加：

```yaml
spring:
  profiles:
    include:
      - qobuz
```

## 使用示例

### 搜索歌曲

```java
@Autowired
private QobuzSearchHander qobuzSearchHander;

SearchKeyData searchKeyData = new SearchKeyData();
searchKeyData.setSearchkey("周杰伦");
searchKeyData.setPageIndex(1);
searchKeyData.setPageSize(20);

PlugSearchResult<PlugSearchMusicResult> result = qobuzSearchHander.querySongByName(searchKeyData);
```

### 获取歌曲详情

```java
Music music = qobuzSearchHander.querySongById("track_id");
```

### 获取专辑详情

```java
Album album = qobuzSearchHander.queryAlbumById("album_id");
```

### 获取下载链接

```java
DownloadInfo downloadInfo = new DownloadInfo();
downloadInfo.setDownloadMusicId("track_id");
downloadInfo.setDownloadBrType("qobuz_flac_lossless");

DownloadUrlResult downloadUrl = qobuzSearchHander.getDownloadUrl(downloadInfo);
```

## 注意事项

1. **API 类型**：使用的是第三方代理 API (api.zarz.moe)，仅需 app_id，无需复杂认证
2. **功能限制**：
   - ❌ 不支持 Artist 搜索和详情获取
   - ❌ 不支持下载链接获取
   - ❌ 不支持歌词获取
3. **分页支持**：当前代理 API 可能不支持 offset 分页参数
4. **稳定性**：依赖第三方代理服务，稳定性由服务提供商保证
5. **音质信息**：根据歌曲的 Hi-Res 标志自动判断支持的音质等级

## 扩展建议

1. 如果需要下载功能，需要寻找其他提供下载接口的 API 服务
2. 如果需要 Artist 功能，可以集成 Qobuz 官方 API（需要 OAuth2 认证）
3. 集成第三方歌词服务（如 LRCLIB）
4. 添加播放列表支持

## 技术栈

- Spring Boot
- FastJSON2
- Hutool
- Lombok

## 作者

Lingma

## 日期

2026/5/7
