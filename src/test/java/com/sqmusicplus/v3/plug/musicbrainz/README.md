# MusicBrainz 插件测试说明

## 测试文件说明

### 1. MusicBrainzTest.java
**位置**: `src/test/java/com/sqmusicplus/v3/plug/musicbrainz/MusicBrainzTest.java`

这是完整的 Spring Boot 集成测试类，需要使用 Spring 容器运行。

**测试内容**:
- ✅ 通过歌曲名称搜索
- ✅ 通过 ISRC 查询歌曲详情
- ✅ 查询艺术家信息
- ✅ 查询专辑信息
- ✅ 获取专辑歌曲列表
- ✅ 验证下载功能不支持
- ✅ 验证歌词功能不支持
- ✅ 综合工作流程测试

**运行方式**:
```bash
# 使用 Maven 运行所有测试
mvn test -Dtest=MusicBrainzTest

# 运行单个测试方法
mvn test -Dtest=MusicBrainzTest#testSearchSongByName

# 使用 Gradle（如果使用）
gradle test --tests MusicBrainzTest
```

### 2. MusicBrainzApiTest.java
**位置**: `src/test/java/com/sqmusicplus/v3/plug/musicbrainz/MusicBrainzApiTest.java`

这是一个独立的 API 测试工具，可以直接运行 main 方法，不需要 Spring 容器。

**测试内容**:
- 直接调用 MusicBrainz API
- 搜索录音
- 通过 ISRC 查询
- 查询艺术家
- 查询专辑

**运行方式**:
```bash
# 方式1: 使用 Maven exec 插件
mvn exec:java -Dexec.mainClass="com.sqmusicplus.v3.plug.musicbrainz.MusicBrainzApiTest"

# 方式2: 在 IDE 中直接运行 main 方法
# 右键点击 MusicBrainzApiTest.java -> Run 'MusicBrainzApiTest.main()'

# 方式3: 编译后运行
mvn compile
java -cp target/classes:target/test-classes com.sqmusicplus.v3.plug.musicbrainz.MusicBrainzApiTest
```

## 前置要求

1. **网络连接**: 需要能够访问 `https://musicbrainz.org`
2. **依赖**: 确保项目已正确配置 MusicBrainz 插件
3. **配置文件**: `application-musicbrainz.yml` 已正确配置

## 测试示例数据

测试中使用了一些真实的 MusicBrainz ID：

### ISRC 示例
- `GBUM71029604` - Queen - Bohemian Rhapsody

### 艺术家 ID
- `0383dadf-2a4e-4d10-a46a-e9e041da8eb7` - Queen

### 专辑 ID
- `6defd963-fe91-4550-b18e-82c685603c2b` - A Night at the Opera

## 注意事项

### API 限流
MusicBrainz API 有严格的使用限制：
- **每秒最多 1 个请求**
- 需要在 User-Agent 中包含联系信息
- 建议添加延迟以避免被封禁

### 测试建议
1. **不要频繁运行测试**：避免触发 API 限流
2. **使用缓存**：如果需要多次测试，考虑缓存结果
3. **尊重 API 规范**：遵守 MusicBrainz 的使用条款

### 常见问题

**Q: 测试失败，提示连接超时？**
A: 检查网络连接，确保可以访问 musicbrainz.org

**Q: 返回结果为空？**
A: 可能是搜索关键词不准确，或者该 ISRC/ID 不存在

**Q: 被 API 限流了怎么办？**
A: 等待一段时间后再试，建议在代码中添加请求间隔

## 自定义测试

### 修改搜索关键词
在 `MusicBrainzTest.java` 中修改：
```java
searchKeyData.setSearchkey("你的搜索关键词");
```

### 使用不同的 ISRC
在测试方法中修改：
```java
String isrc = "你的ISRC代码";
```

### 添加新的测试用例
参考现有测试方法的结构，添加新的 `@Test` 方法。

## 输出示例

### 搜索结果示例
```
[1] 歌曲: Bohemian Rhapsody | 艺术家: Queen | 专辑: A Night at the Opera | ISRC: GBUM71029604
```

### 歌曲详情示例
```
歌曲名称: Bohemian Rhapsody
艺术家: Queen
专辑: A Night at the Opera
时长: 354000 ms
专辑ID: 6defd963-fe91-4550-b18e-82c685603c2b
```

## 开发者

@Created by SQ

## 相关文档

- [MusicBrainz 插件 README](../../main/java/com/sqmusicplus/v3/plug/musicbrainz/README.md)
- [MusicBrainz API 文档](https://musicbrainz.org/doc/MusicBrainz_API)
