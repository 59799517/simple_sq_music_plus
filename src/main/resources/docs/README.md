# Simple SQ Music Plus

## 项目简介

Simple SQ Music Plus 是一个功能强大的音乐下载和管理工具，支持多个音乐平台的资源获取和管理。

### 主要特性

- 🎵 **多平台支持**：支持 QQ音乐、酷狗、网易云等多个音乐平台
- 📥 **高速下载**：高效的音乐下载引擎
- 🗂️ **智能管理**：自动整理和分类音乐文件
- 🔧 **易于部署**：支持 Docker 部署，开箱即用
- 🌐 **Web 界面**：提供友好的 Web 管理界面

### 快速开始

#### 环境要求

- Docker
- Docker Compose

#### Docker Compose 部署

##### x86_64 / AMD64 架构（大多数 PC 和服务器）

创建 `docker-compose.yml` 文件：

```yaml
version: '3.8'

services:
  sqmusic_main:
    image: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:latest
    container_name: sqmusic_main
    environment:
      - DB_IP=mysql
      - DB_PORT=3306
      - DB_NAME=sqmusicv3
      - DB_USERNAME=root
      - DB_PASSWORD=sqmusicv3password
    volumes:
      - ./music:/music
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - sq-app-network
    expose:
      - "8099"
    restart: always

  sqmusic_web:
    image: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:latest
    container_name: sqmusic_web
    ports:
      - "8096:80"
    networks:
      - sq-app-network
    depends_on:
      - sqmusic_main
    restart: always

  mysql:
    image: mysql:5.7
    container_name: sqmusic_mysql
    environment:
      MYSQL_ROOT_PASSWORD: sqmusicv3password
      MYSQL_DATABASE: sqmusicv3
    volumes:
      - ./mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"
    networks:
      - sq-app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: always

networks:
  sq-app-network:
    driver: bridge
```

启动服务：

```bash
docker-compose up -d
```

##### ARM 架构（树莓派、Apple Silicon Mac、ARM 服务器等）

创建 `docker-compose-arm.yml` 文件：

```yaml
version: '3.8'

services:
  sqmusic_main:
    image: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:latest-arm64
    container_name: sqmusic_main
    environment:
      - DB_IP=mysql
      - DB_PORT=3306
      - DB_NAME=sqmusicv3
      - DB_USERNAME=root
      - DB_PASSWORD=sqmusicv3password
    volumes:
      - ./music:/music
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - sq-app-network
    expose:
      - "8099"
    restart: always

  sqmusic_web:
    image: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:latest-arm64
    container_name: sqmusic_web
    ports:
      - "8096:80"
    networks:
      - sq-app-network
    depends_on:
      - sqmusic_main
    restart: always

  mysql:
    image: pistar/mysql-arm:hf-5.7.25
    container_name: sqmusic_mysql
    environment:
      MYSQL_ROOT_PASSWORD: sqmusicv3password
      MYSQL_DATABASE: sqmusicv3
    volumes:
      - ./mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"
    networks:
      - sq-app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5
    restart: always

networks:
  sq-app-network:
    driver: bridge
```

启动服务：

```bash
docker-compose -f docker-compose-arm.yml up -d
```

##### 验证部署

```bash
# x86 架构查看容器状态
docker-compose ps

# ARM 架构查看容器状态
docker-compose -f docker-compose-arm.yml ps
```

##### 常用管理命令

**x86 架构：**

```bash
# 停止服务
docker-compose down

# 查看日志
docker-compose logs -f

# 重启服务
docker-compose restart
```

**ARM 架构：**

```bash
# 停止服务
docker-compose -f docker-compose-arm.yml down

# 查看日志
docker-compose -f docker-compose-arm.yml logs -f

# 重启服务
docker-compose -f docker-compose-arm.yml restart
```

### 技术栈

- **后端框架**：Spring Boot
- **数据库**：MySQL 
- **构建工具**：Maven
- **容器化**：Docker

### 接口文档模块

系统提供丰富的 RESTful API 接口,主要包含以下模块:

---

#### 1. 系统设置模块 (ConfigController)

##### 1.1 用户登录
**接口地址**: `/api/config/login`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 否  
**接口描述**: 用户登录获取Token

**请求参数**:
```json
{
    "username": "admin",
    "password": "admin",
    "device": "web"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| device | string | 是 | 登录设备类型(如:web、app) |

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "tokenName": "sqmusic",
        "tokenValue": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjk1MjcsImRldmljZSI6IndlYiIsImVmZiI6LTEsInJuU3RyIjoidFVLVlFrTEQwaVMxdEFCR3dvYXo2bFZ6cXJCSFJSOUIifQ.XKlsqcwjp1U-E4zzqVviWGBLo4gDcbbLzPg2eWx8Nlo",
        "isLogin": true,
        "loginId": "9527",
        "loginType": "login",
        "tokenTimeout": -1,
        "sessionTimeout": -2,
        "tokenSessionTimeout": -2,
        "tokenActivityTimeout": -2,
        "loginDevice": "web",
        "tag": null
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| tokenName | string | Token名称(如:sqmusic) |
| tokenValue | string | Token值(JWT格式,用于后续请求认证) |
| isLogin | boolean | 是否已登录 |
| loginId | string | 登录ID(字符串类型) |
| loginType | string | 登录类型(如:login) |
| tokenTimeout | integer | Token过期时间(秒,-1表示永不过期) |
| sessionTimeout | integer | Session过期时间(秒,-2表示未设置) |
| tokenSessionTimeout | integer | Token-Session过期时间(秒,-2表示未设置) |
| tokenActivityTimeout | integer | Token活跃超时时间(秒,-2表示未设置) |
| loginDevice | string | 登录设备类型 |
| tag | object | 标签信息(可为null) |

---

##### 1.2 用户登出
**接口地址**: `/api/config/logout`  
**请求方式**: POST  
**是否需要登录**: 是  
**接口描述**: 用户登出(JWT模式前端清除token即可)

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 1.3 检查登录状态
**接口地址**: `/api/config/isLogin`  
**请求方式**: GET  
**是否需要登录**: 否  
**接口描述**: 检查当前用户是否已登录

**响应参数**:
```json
{
    "code": 200,
    "msg": "登录有效",
    "data": true
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| data | boolean | true=已登录, false=未登录或过期 |

---

##### 1.4 获取全部配置
**接口地址**: `/api/config/getConfigList`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 获取系统所有配置项列表

**请求参数**: 无

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": [
        {
            "configId": 18,
            "configName": "QQ音乐cookie",
            "configValue": "",
            "configKey": "plug.qqvip.cookie",
            "configType": "input",
            "configOptions": null,
            "configShow": 0,
            "configRemark": "看看就行尽量别改",
            "configNullCheck": 1,
            "configDisabled": 1
        },
        {
            "configId": 26,
            "configName": "开启酷狗插件",
            "configValue": "false",
            "configKey": "plug.kg.open",
            "configType": "boolean",
            "configOptions": null,
            "configShow": 1,
            "configRemark": "开启酷狗插件",
            "configNullCheck": 1,
            "configDisabled": 0
        },
        {
            "configId": 4,
            "configName": "同时下载的数量",
            "configValue": "9",
            "configKey": "system.download.num",
            "configType": "number",
            "configOptions": null,
            "configShow": 1,
            "configRemark": "允许同时下载歌的数量（最小是1）越大消耗的资源越多也有可能碰到封IP",
            "configNullCheck": 1,
            "configDisabled": 0
        }
    ]
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| configId | integer | 配置ID |
| configName | string | 配置名称 |
| configValue | string | 配置值 |
| configKey | string | 配置键(唯一标识) |
| configType | string | 配置类型(input/password/number/boolean/select/path) |
| configOptions | string/null | 配置选项(select类型时使用,JSON数组字符串,其他类型为null) |
| configShow | integer | 是否显示(1=显示, 0=隐藏) |
| configRemark | string | 配置备注说明 |
| configNullCheck | integer | 是否允许为空(1=不允许, 0=允许) |
| configDisabled | integer | 是否禁用(1=禁用, 0=启用) |

**配置类型说明**:
- **input**: 普通文本输入框
- **password**: 密码输入框(前端会隐藏显示)
- **number**: 数字输入框
- **boolean**: 布尔值开关(true/false)
- **select**: 下拉选择框(需要从configOptions解析选项)
- **path**: 路径选择框

**注意事项**:
- configOptions 字段仅在 configType 为 "select" 时有值,为 JSON 数组字符串格式
- configDisabled 为 1 的配置项不允许修改
- configShow 为 0 的配置项在前端界面中隐藏,但仍可通过API访问

---

##### 1.5 修改配置
**接口地址**: `/api/config/updateConfig`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 修改系统配置项

**请求参数**:
```json
{
    "configKey": "system.download.num",
    "configValue": 9
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| configKey | string | 是 | 配置键(如:system.download.num) |
| configValue | any | 是 | 新的配置值(可以是字符串、数字、布尔值等) |

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200
}
```

**注意事项**:
- 禁用的配置项不允许修改
- 不允许为空的配置项必须提供非空值
- 不同配置类型有不同的校验规则:
  - number: 必须为数字
  - boolean: 必须为true或false
  - select: 必须在选项范围内
  - input/password/path: 字符串类型
- configValue 的类型应与配置项定义的类型一致

---

##### 1.6 获取当前网络使用情况
**接口地址**: `/api/config/getCurrentNetwork`  
**请求方式**: GET  
**是否需要登录**: 否  
**接口描述**: 获取当前系统的网络使用情况(上传/下载速度等)

**请求参数**: 无

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "uploadSpeed": 0.0,
        "downloadSpeed": 9.738001932701496E-46,
        "totalSpeed": 9.738001932701496E-46,
        "uploadSpeedFormatted": "0.00 B/s",
        "downloadSpeedFormatted": "0.00 B/s",
        "totalSpeedFormatted": "0.00 B/s",
        "timestamp": 1775810503123
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| uploadSpeed | number | 当前上传速度(bytes/s) |
| downloadSpeed | number | 当前下载速度(bytes/s) |
| totalSpeed | number | 总速度(bytes/s) |
| uploadSpeedFormatted | string | 格式化后的上传速度(如:"0.00 B/s") |
| downloadSpeedFormatted | string | 格式化后的下载速度(如:"0.00 B/s") |
| totalSpeedFormatted | string | 格式化后的总速度(如:"0.00 B/s") |
| timestamp | long | 时间戳(毫秒) |

---

##### 1.7 获取插件选项
**接口地址**: `/api/config/getOption`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 获取已启用的音乐平台插件选项列表

**请求参数**: 无

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": [
        {
            "label": "某我",
            "value": "kw"
        },
        {
            "label": "猪厂",
            "value": "netease"
        },
        {
            "label": "鹅厂(不要太过频繁否则无法下载)",
            "value": "qq"
        }
    ]
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| label | string | 插件显示名称(可能包含提示信息) |
| value | string | 插件标识符(kw/netease/qq/kg/mg/apple等) |

**注意事项**:
- 只返回已启用的插件
- label 字段可能包含使用提示或警告信息
- value 字段用于其他接口的 plugName 参数

---

##### 1.8 获取版本信息
**接口地址**: `/api/config/version`  
**请求方式**: GET  
**是否需要登录**: 否  
**接口描述**: 获取当前系统版本号

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": "3.1.0"
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| data | string | 系统版本号 |

---

##### 1.9 导入V2.x版本歌单配置
**接口地址**: `/api/config/importSongList`  
**请求方式**: POST  
**Content-Type**: multipart/form-data  
**是否需要登录**: 否  
**接口描述**: 从V2.x版本导入歌单配置(JSON文件)

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| file | file | 是 | JSON格式的配置文件 |

**文件格式要求**:
- 文件后缀必须为.json
- 文件内容应为JSON数组格式
- 支持三种导入类型:
  1. playList: 歌单导入
  2. likeAlubids: 喜欢的专辑导入
  3. likeArtistids: 喜欢的歌手导入

**响应参数**:
```json
{
    "code": 200,
    "msg": "导入成功"
}
```

**注意事项**:
- 系统会自动去重,已存在的歌曲/专辑/歌手不会重复导入
- 批量插入时采用每300条分割的方式提高性能
- 导入失败时会逐条尝试,忽略错误继续处理

---

#### 2. 音乐搜索模块 (MusicController)

##### 2.1 搜索提示词
**接口地址**: `/api/music/searchTips`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据关键字获取搜索建议/提示词

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| keyword | string | 是 | 搜索关键字 |
| _t | long | 否 | 时间戳(用于防止缓存) |

**请求示例**: `/api/music/searchTips?plugName=kw&keyword=jay&_t=1775810618952`

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": [
        "Jay",
        "Jay Ludovico Einaudi",
        "jay chou",
        "jay of love",
        "JAYZ",
        "Jay Sean",
        "JAYWALK",
        "Jay伴同行26年",
        "Jay Park",
        "Jaymes Young"
    ]
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| data | array | 搜索建议列表(string数组) |

---

##### 2.2 搜索单曲
**接口地址**: `/api/music/searchSong`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据关键字搜索歌曲

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| keyword | string | 是 | 搜索关键字 |
| pageSize | integer | 否 | 每页数量(默认10) |
| pageIndex | integer | 否 | 页码(默认1) |

**请求示例**: `/api/music/searchSong?plugName=kw&keyword=jay&pageSize=10&pageIndex=1`

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "searchKeyWork": "jay",
        "searchIndex": 1,
        "searchSize": 10,
        "searchTotal": 3600,
        "records": [
            {
                "id": "550531860",
                "name": "那天下雨了",
                "artistName": ["周杰伦"],
                "artistids": ["336"],
                "pic": "https://img3.kuwo.cn/star/albumcover/500/s4s86/95/3059703046.jpg",
                "albumName": "太阳之子",
                "albumid": "87758985",
                "lyric": null,
                "lyricId": null,
                "plugName": "kw",
                "duration": "223000",
                "brTypes": ["KW_FLAC_2000", "KW_MP3_320", "KW_MP3_128"],
                "dataInfo": {
                    "ARTIST": "周杰伦",
                    "ALBUM": "太阳之子",
                    "DURATION": "223"
                }
            }
        ]
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| searchKeyWork | string | 搜索关键字 |
| searchIndex | integer | 当前页码 |
| searchSize | integer | 每页数量 |
| searchTotal | integer | 总记录数 |
| records | array | 歌曲列表 |
| records[].id | string | 歌曲ID |
| records[].name | string | 歌曲名称 |
| records[].artistName | array | 歌手名称列表 |
| records[].artistids | array | 歌手ID列表 |
| records[].pic | string | 封面图片URL |
| records[].albumName | string | 专辑名称 |
| records[].albumid | string | 专辑ID |
| records[].lyric | string | 歌词内容(可能为null) |
| records[].lyricId | string | 歌词ID(可能为null) |
| records[].plugName | string | 插件名称 |
| records[].duration | string | 歌曲时长(毫秒,字符串类型) |
| records[].brTypes | array | 可用音质列表(如:KW_FLAC_2000) |
| records[].dataInfo | object | 扩展信息对象(包含详细元数据) |

---

##### 2.3 获取单曲信息(根据ID)
**接口地址**: `/api/music/SongInfoById`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据歌曲ID获取详细信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称 |
| id | string | 是 | 歌曲ID |

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": {
        "id": "001234567890",
        "name": "歌曲名称",
        "artistName": ["歌手名"],
        "artistId": ["artist123"],
        "albumName": "专辑名称",
        "albumId": "album456",
        "duration": 240000,
        "pic": "https://...",
        "lyric": "歌词内容",
        "brTypes": ["MP3_128", "FLAC_2000"]
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | string | 歌曲ID |
| name | string | 歌曲名称 |
| artistName | array | 歌手名称列表 |
| artistId | array | 歌手ID列表 |
| albumName | string | 专辑名称 |
| albumId | string | 专辑ID |
| duration | integer | 歌曲时长(毫秒) |
| pic | string | 封面图片URL |
| lyric | string | 歌词内容 |
| brTypes | array | 可用音质列表 |

---

##### 2.4 搜索歌手
**接口地址**: `/api/music/searchArtist`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据关键字搜索歌手

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| keyword | string | 是 | 搜索关键字 |
| pageSize | integer | 否 | 每页数量(默认10) |
| pageIndex | integer | 否 | 页码(默认1) |

**请求示例**: `/api/music/searchArtist?plugName=kw&keyword=jay&pageSize=10&pageIndex=1`

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "searchKeyWork": "jay",
        "searchIndex": 1,
        "searchSize": 10,
        "searchTotal": 45,
        "records": [
            {
                "artistName": "周杰伦",
                "artistid": "336",
                "plugName": "kw",
                "pic": "https://img1.kuwo.cn/star/starheads/240/s4s56/58/291211030.jpg",
                "total": "45",
                "dataInfo": {
                    "ARTIST": "周杰伦",
                    "ALBUMNUM": "45",
                    "SONGNUM": "2115"
                }
            }
        ]
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| searchKeyWork | string | 搜索关键字 |
| searchIndex | integer | 当前页码 |
| searchSize | integer | 每页数量 |
| searchTotal | integer | 总记录数 |
| records | array | 歌手列表 |
| records[].artistName | string | 歌手名称 |
| records[].artistid | string | 歌手ID |
| records[].plugName | string | 插件名称 |
| records[].pic | string | 头像URL |
| records[].total | string | 作品总数(字符串类型) |
| records[].dataInfo | object | 扩展信息对象(包含详细元数据) |
| records[].dataInfo.ARTIST | string | 歌手名称 |
| records[].dataInfo.ALBUMNUM | string | 专辑数量 |
| records[].dataInfo.SONGNUM | string | 歌曲数量 |

---

##### 2.5 根据歌手ID查询歌手全部专辑
**接口地址**: `/api/music/artistAlbumById`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据歌手ID获取歌手信息及全部专辑

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称(kw/qq/kg/netease/mg等) |
| id | string | 是 | 歌手ID |

**请求示例**: `/api/music/artistAlbumById?id=336&plugName=kw`

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "id": "336",
        "musicArtistsName": "周杰伦",
        "musicArtistsSex": null,
        "musicArtistsPhoto": "https://star.kuwo.cn/star/starheads/500/s4s56/58/291211030.jpg",
        "musicArtistsDescribe": "周杰伦(Jay Chou),1979年1月18日出生于台湾省新北市..."
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | string | 歌手ID |
| musicArtistsName | string | 歌手名称 |
| musicArtistsSex | string/null | 歌手性别(可能为null) |
| musicArtistsPhoto | string | 歌手头像URL |
| musicArtistsDescribe | string | 歌手详细描述(包含基本资料、从艺历程、荣誉记录等,HTML格式) |

**注意事项**:
- musicArtistsDescribe 字段包含完整的歌手介绍,内容非常详细,包含HTML标签
- 描述内容包括:基本资料、从艺历程、荣誉记录等完整信息
- musicArtistsSex 字段可能为 null

---

##### 2.6 搜索专辑
**接口地址**: `/api/music/searchAlbum`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据关键字搜索专辑

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| keyword | string | 是 | 搜索关键字 |
| pageSize | integer | 否 | 每页数量(默认10) |
| pageIndex | integer | 否 | 页码(默认1) |

**请求示例**: `/api/music/searchAlbum?plugName=kw&keyword=jay&pageSize=10&pageIndex=1`

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "searchKeyWork": "jay",
        "searchIndex": 1,
        "searchSize": 10,
        "searchTotal": 7910,
        "records": [
            {
                "artistName": "周杰伦",
                "artistid": "336",
                "pic": "https://img3.kuwo.cn/star/albumcover/120/s4s32/71/2716993423.jpg",
                "total": null,
                "albumName": "Jay",
                "albumid": "1286",
                "plugName": "kw",
                "dataInfo": {
                    "artist": "周杰伦",
                    "pub": "2000-11-07",
                    "musiccnt": "10",
                    "company": "杰威尔音乐有限公司"
                }
            }
        ]
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| searchKeyWork | string | 搜索关键字 |
| searchIndex | integer | 当前页码 |
| searchSize | integer | 每页数量 |
| searchTotal | integer | 总记录数 |
| records | array | 专辑列表 |
| records[].artistName | string | 歌手名称 |
| records[].artistid | string | 歌手ID |
| records[].pic | string | 封面图片URL |
| records[].total | integer | 歌曲总数(可能为null) |
| records[].albumName | string | 专辑名称 |
| records[].albumid | string | 专辑ID |
| records[].plugName | string | 插件名称 |
| records[].dataInfo | object | 扩展信息对象(包含详细元数据) |
| records[].dataInfo.artist | string | 歌手名称 |
| records[].dataInfo.pub | string | 发行日期 |
| records[].dataInfo.musiccnt | string | 歌曲数量 |
| records[].dataInfo.company | string | 唱片公司 |

---

##### 2.7 获取专辑信息(根据ID)
**接口地址**: `/api/music/albumInfoById`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 根据专辑ID获取专辑详细信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称(kw/qq/kg/netease/mg等) |
| id | string | 是 | 专辑ID |

**请求示例**: `/api/music/albumInfoById?id=87758985&plugName=kw`

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "albumId": "87758985",
        "albumName": "太阳之子",
        "albumTime": "2026-03-25",
        "albumDescribe": "万众期盼！雨过天晴\n太阳之子以音乐能量的光芒照耀全球！...",
        "albumArtist": "周杰伦",
        "albumArtistId": "336",
        "albumImg": "http://img2.sycdn.kuwo.cn/star/albumcover/500/s4s86/95/3059703046.jpg",
        "dataInfo": {
            "aartist": "Jay Chou",
            "artist": "周杰伦",
            "artistid": "336",
            "company": "杰威尔音乐有限公司",
            "id": "87758985"
        }
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| albumId | string | 专辑ID |
| albumName | string | 专辑名称 |
| albumTime | string | 专辑发行时间 |
| albumDescribe | string | 专辑详细描述(包含专辑介绍、曲目介绍等,内容非常详细) |
| albumArtist | string | 歌手名称 |
| albumArtistId | string | 歌手ID |
| albumImg | string | 专辑封面图片URL |
| dataInfo | object | 扩展信息对象(包含详细的元数据) |
| dataInfo.aartist | string | 歌手英文名 |
| dataInfo.artist | string | 歌手名称 |
| dataInfo.artistid | string | 歌手ID |
| dataInfo.company | string | 唱片公司 |
| dataInfo.id | string | 专辑ID |
| dataInfo.img | string | 专辑封面URL |
| dataInfo.hts_img | string | 高清封面URL |
| dataInfo.artistpic | string | 歌手头像URL |

**注意事项**:
- `albumDescribe` 字段包含完整的专辑介绍和所有曲目的详细介绍,内容非常长
- 描述内容包括:专辑概念、创作背景、实体专辑装帧说明、每首歌曲的详细介绍(词曲编制作人、歌词片段、创作理念等)
- `dataInfo` 对象包含丰富的元数据,具体字段因平台而异

---

##### 2.8 获取歌词
**接口地址**: `/api/music/getLyric`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 根据歌曲ID获取歌词

**请求参数**:
```json
{
    "plugName": "qq",
    "id": "001234567890"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称 |
| id | string | 是 | 歌曲ID |

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": "[00:00.00]歌词第一行\n[00:05.00]歌词第二行"
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| data | string | 歌词内容(带时间戳的LRC格式) |

---

##### 2.9 获取下载链接(播放链接)
**接口地址**: `/api/music/getDownloadUrl`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 获取歌曲的下载/播放链接

**请求参数**:
```json
{
    "plugName": "qq",
    "id": "001234567890",
    "brType": "QQ_Flac_2000",
    "brTypes": ["MP3_128", "FLAC_2000"]
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称 |
| id | string | 是 | 歌曲ID |
| brType | string | 是 | 音质类型 |
| brTypes | array | 否 | 可选音质列表 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": {
        "url": "https://dl.stream.qq.com/...",
        "brType": "QQ_Flac_2000",
        "size": 25600000,
        "duration": 240000,
        "format": "flac"
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| url | string | 下载/播放链接 |
| brType | string | 音质类型 |
| size | integer | 文件大小(bytes) |
| duration | integer | 时长(毫秒) |
| format | string | 音频格式(mp3/flac/ape/m4a等) |

**注意事项**:
- 需要选择正确的音质类型,不同平台支持的音质不同
- 链接可能有有效期限制,请及时使用
- 部分高音质可能需要VIP权限
- 具体音质类型请参考“音质类型模块”

---

#### 3. 下载模块 (DownloadServiceController)

##### 3.1 下载单曲
**接口地址**: `/api/download/downloadSong`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 将单曲添加到下载队列

**请求参数**:
```json
{
    "id": "550531860",
    "name": "那天下雨了",
    "artistName": ["周杰伦"],
    "artistids": ["336"],
    "pic": "https://img3.kuwo.cn/star/albumcover/500/s4s86/95/3059703046.jpg",
    "albumName": "太阳之子",
    "albumid": "87758985",
    "lyric": null,
    "lyricId": null,
    "plugName": "kw",
    "duration": "223000",
    "brTypes": ["KW_FLAC_2000", "KW_MP3_320", "KW_MP3_128"],
    "brType": "KW_FLAC_2000",
    "dataInfo": {
        "ARTIST": "周杰伦",
        "ALBUM": "太阳之子",
        "DURATION": "223"
    }
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | string | 是 | 歌曲ID |
| name | string | 否 | 歌曲名称 |
| artistName | array | 否 | 歌手名称列表 |
| artistids | array | 否 | 歌手ID列表 |
| pic | string | 否 | 封面图片URL |
| albumName | string | 否 | 专辑名称 |
| albumid | string | 否 | 专辑ID |
| lyric | string | 否 | 歌词内容(可能为null) |
| lyricId | string | 否 | 歌词ID(可能为null) |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| duration | string | 否 | 歌曲时长(毫秒,字符串类型) |
| brTypes | array | 否 | 可用音质列表 |
| brType | string | 否 | 指定音质(不填则自动选择最高音质) |
| dataInfo | object | 否 | 扩展信息对象 |

**响应参数**:
```json
{
    "msg": "下载成功",
    "code": 200,
    "data": {
        "id": 797,
        "downloadGid": "550531860",
        "downloadTime": "2026-04-10 17:01:30",
        "downloadFile": "那天下雨了 - 周杰伦",
        "downloadMusicId": "550531860",
        "downloadPlugName": "kw",
        "downloadBrType": "kw_flac_2000",
        "downloadMusicname": "那天下雨了",
        "downloadArtistname": "周杰伦",
        "downloadAlbumname": "太阳之子",
        "downloadMsg": null,
        "downloadStatus": "waiting",
        "springName": "nKwSearchHander",
        "audioBook": 0,
        "downloadUpdateTime": "2026-04-10 17:01:30",
        "rewriteMp3tag": 1,
        "downloadBits": "2000,320,128",
        "downloadBrTypes": "kw_flac_2000,kw_mp3_320,kw_mp3_128"
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | integer | 下载任务ID |
| downloadGid | string | 下载全局ID |
| downloadTime | string | 下载创建时间 |
| downloadFile | string | 下载文件名(格式:歌曲名 - 歌手名) |
| downloadMusicId | string | 歌曲ID |
| downloadPlugName | string | 插件名称 |
| downloadBrType | string | 音质类型ID(如:kw_flac_2000) |
| downloadMusicname | string | 歌曲名称 |
| downloadArtistname | string | 歌手名称 |
| downloadAlbumname | string | 专辑名称 |
| downloadMsg | string | 下载消息(可能为null) |
| downloadStatus | string | 下载状态(waiting/downloading/success/error) |
| springName | string | Spring Bean名称 |
| audioBook | integer | 是否为有声书(0=否, 1=是) |
| downloadUpdateTime | string | 最后更新时间 |
| rewriteMp3tag | integer | 是否重写MP3标签(1=是, 0=否) |
| downloadBits | string | 音质比特率列表(逗号分隔,如:"2000,320,128") |
| downloadBrTypes | string | 音质类型ID列表(逗号分隔,如:"kw_flac_2000,kw_mp3_320,kw_mp3_128") |

---

##### 3.2 下载歌手的全部专辑
**接口地址**: `/api/download/downloadArtistAlbum`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 批量下载指定歌手的所有专辑

**请求参数**:
```json
{
    "artistName": "潘儿",
    "artistid": "90082",
    "plugName": "kw",
    "pic": "https://img4.kuwo.cn/star/starheads/240/s4s54/14/4175099840.jpg",
    "total": "2",
    "dataInfo": {
        "ARTIST": "潘儿",
        "ALBUMNUM": "2",
        "SONGNUM": "17"
    }
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| artistName | string | 否 | 歌手名称 |
| artistid | string | 是 | 歌手ID |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| pic | string | 否 | 歌手头像URL |
| total | string | 否 | 作品总数 |
| bit | integer | 否 | 音质比特率(如:2000代表FLAC无损,不填则使用默认音质) |
| dataInfo | object | 否 | 扩展信息对象 |

**响应参数**:
```json
{
    "msg": "下载成功",
    "code": 200,
    "data": [
        {
            "id": 786,
            "downloadGid": "2328807",
            "downloadTime": "2026-04-10 16:58:17",
            "downloadFile": "香烟 女孩 夏天 - 潘儿",
            "downloadMusicId": "2328807",
            "downloadPlugName": "kw",
            "downloadBrType": "kw_mp3_128",
            "downloadMusicname": "香烟 女孩 夏天",
            "downloadArtistname": "潘儿",
            "downloadAlbumname": "香烟 女孩 夏天",
            "downloadMsg": null,
            "downloadStatus": "waiting",
            "springName": "nKwSearchHander",
            "audioBook": 0,
            "downloadUpdateTime": "2026-04-10 16:58:17",
            "rewriteMp3tag": 1,
            "downloadBits": "128",
            "downloadBrTypes": "kw_mp3_128"
        }
    ]
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | integer | 下载任务ID |
| downloadGid | string | 下载全局ID |
| downloadTime | string | 下载创建时间 |
| downloadFile | string | 下载文件名(格式:歌曲名 - 歌手名) |
| downloadMusicId | string | 歌曲ID |
| downloadPlugName | string | 插件名称 |
| downloadBrType | string | 音质类型ID(如:kw_mp3_128) |
| downloadMusicname | string | 歌曲名称 |
| downloadArtistname | string | 歌手名称 |
| downloadAlbumname | string | 专辑名称 |
| downloadMsg | string | 下载消息(可能为null) |
| downloadStatus | string | 下载状态(waiting/downloading/success/error) |
| springName | string | Spring Bean名称 |
| audioBook | integer | 是否为有声书(0=否, 1=是) |
| downloadUpdateTime | string | 最后更新时间 |
| rewriteMp3tag | integer | 是否重写MP3标签(1=是, 0=否) |
| downloadBits | string | 音质比特率(字符串类型) |
| downloadBrTypes | string | 音质类型ID |

**注意事项**:
- 此操作会批量下载指定歌手的所有专辑中的歌曲
- 返回的是下载任务列表,每个任务对应一首歌曲
- 建议在后台执行,避免阻塞
- 如果未指定bit参数,系统会使用默认音质

---

##### 3.3 下载专辑
**接口地址**: `/api/download/downloadAlbum`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 批量下载指定专辑的所有歌曲

**请求参数**:
```json
{
    "artistName": "Jay",
    "artistid": "11007730",
    "pic": "https://img3.kuwo.cn/star/albumcover/120/23/78/3227440250.jpg",
    "total": null,
    "albumName": "Разве это не любовь",
    "albumid": "8871872",
    "plugName": "kw",
    "dataInfo": {
        "artist": "Jay",
        "pub": "2018-09-02",
        "musiccnt": "1",
        "company": "Sferoom Free"
    }
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| artistName | string | 否 | 歌手名称(多个歌手用&分隔) |
| artistid | string | 否 | 歌手ID |
| pic | string | 否 | 专辑封面URL |
| total | integer | 否 | 歌曲总数(可能为null) |
| albumName | string | 否 | 专辑名称 |
| albumid | string | 是 | 专辑ID |
| plugName | string | 是 | 插件名称(qq/kg/netease/kw等) |
| bit | integer | 否 | 音质比特率(如:2000代表FLAC无损,不填则使用默认音质) |
| dataInfo | object | 否 | 扩展信息对象 |

**响应参数**:
```json
{
    "msg": "下载成功",
    "code": 200,
    "data": [
        {
            "id": 796,
            "downloadGid": "54221778",
            "downloadTime": "2026-04-10 16:59:42",
            "downloadFile": "Разве это не любовь (Explicit) - Jay",
            "downloadMusicId": "54221778",
            "downloadPlugName": "kw",
            "downloadBrType": "kw_flac_2000",
            "downloadMusicname": "Разве это не любовь (Explicit)",
            "downloadArtistname": "Jay",
            "downloadAlbumname": "Разве это не любовь",
            "downloadMsg": null,
            "downloadStatus": "waiting",
            "springName": "nKwSearchHander",
            "audioBook": 0,
            "downloadUpdateTime": "2026-04-10 16:59:42",
            "rewriteMp3tag": 1,
            "downloadBits": "2000,320,128",
            "downloadBrTypes": "kw_flac_2000,kw_mp3_320,kw_mp3_128"
        }
    ]
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| id | integer | 下载任务ID |
| downloadGid | string | 下载全局ID |
| downloadTime | string | 下载创建时间 |
| downloadFile | string | 下载文件名(格式:歌曲名 - 歌手名) |
| downloadMusicId | string | 歌曲ID |
| downloadPlugName | string | 插件名称 |
| downloadBrType | string | 音质类型ID(如:kw_flac_2000) |
| downloadMusicname | string | 歌曲名称 |
| downloadArtistname | string | 歌手名称 |
| downloadAlbumname | string | 专辑名称 |
| downloadMsg | string | 下载消息(可能为null) |
| downloadStatus | string | 下载状态(waiting/downloading/success/error) |
| springName | string | Spring Bean名称 |
| audioBook | integer | 是否为有声书(0=否, 1=是) |
| downloadUpdateTime | string | 最后更新时间 |
| rewriteMp3tag | integer | 是否重写MP3标签(1=是, 0=否) |
| downloadBits | string | 音质比特率列表(逗号分隔,如:"2000,320,128") |
| downloadBrTypes | string | 音质类型ID列表(逗号分隔,如:"kw_flac_2000,kw_mp3_320,kw_mp3_128") |

**注意事项**:
- 多个歌手名称使用 & 符号分隔,如: "周杰伦&方文山"
- 系统会自动解析并创建专辑中每首歌曲的下载任务
- 返回的是下载任务列表,每个任务对应一首歌曲
- 如果未指定bit参数,系统会使用默认音质
- downloadBits 和 downloadBrTypes 可能包含多个值,用逗号分隔

---

##### 3.4 下载解析的URL歌曲
**接口地址**: `/api/download/downloadParserUrl`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 解析音乐平台URL并添加到下载队列

**请求参数**:
```json
{
    "url": "http://xxxx/xxx/xxx/xxx/xx/",
    "isAudioBook": false,
    "bookName": "",
    "artist": ""
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| url | string | 是 | 音乐平台分享链接或音频文件URL |
| isAudioBook | boolean | 否 | 是否为有声书(默认false) |
| bookName | string | 否 | 有声书名称(仅当isAudioBook=true时有效) |
| artist | string | 否 | 歌手/作者名称 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "下载成功",
    "data": [
        {
            "id": 1,
            "musicName": "歌曲名称",
            "downloadStatus": "waiting"
        }
    ]
}
```

**支持的URL类型**:
- QQ音乐分享链接
- 酷我音乐分享链接
- 酷狗概念版分享链接
- 网易云音乐分享链接
- 直接音频文件URL

**注意事项**:
- 支持音乐平台分享链接和直接音频URL
- isAudioBook 用于标识是否为有声书,默认为false
- 如果是有声书,需要填写 bookName 字段
- artist 字段可以填写歌手或作者名称
- 解析失败会返回错误提示

---

##### 3.5 下载解析的URL歌曲(替代解析方法)
**接口地址**: `/api/download/downloadParserUrlResult`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 使用已解析的音乐列表直接创建下载任务

**请求参数**:
```json
[
    {
        "id": "001234567890",
        "name": "歌曲名称",
        "artistName": ["歌手名"],
        "albumName": "专辑名称",
        "plugName": "qq",
        "duration": 240000,
        "pic": "https://..."
    }
]
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| [].id | string | 是 | 歌曲ID |
| [].name | string | 是 | 歌曲名称 |
| [].artistName | array | 否 | 歌手名称列表 |
| [].albumName | string | 否 | 专辑名称 |
| [].plugName | string | 是 | 插件名称 |
| [].duration | integer | 否 | 时长(毫秒) |
| [].pic | string | 否 | 封面URL |

**响应参数**:
```json
{
    "code": 200,
    "msg": "下载成功",
    "data": [
        {
            "id": 1,
            "musicId": "001234567890",
            "downloadStatus": "waiting"
        }
    ]
}
```

**使用场景**:
- 当前端已经自行解析URL获取到音乐信息后,直接调用此接口创建下载任务
- 避免后端重复解析,提高效率

---

##### 3.6 下载解析的文本歌曲
**接口地址**: `/api/download/downloadParserText`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 解析文本中的歌曲信息并异步添加到下载队列

**请求参数**:
```json
{
    "text": "晴天 jay"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| text | string | 是 | 包含歌曲信息的文本(每行一首歌,格式:歌曲名 歌手名) |

**响应参数**:
```json
{
    "msg": "开始解析并下载，稍后在下载中查看！（每首识别大致需要500毫秒耐心等待）",
    "code": 200
}
```

**文本格式示例**:
```
周杰伦 青花瓷
林俊杰 江南
陈奕迅 十年
```

**注意事项**:
- 此接口为异步处理,立即返回成功消息
- 实际解析和下载在后台线程中进行
- 每首歌曲识别约需500毫秒
- 解析结果可在下载任务列表中查看
- 识别失败的歌曲会被跳过

---

##### 3.7 批量下载解析的文本歌曲(替代解析方法)
**接口地址**: `/api/download/downloadParserTextResult`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 使用已解析的文本结果直接创建下载任务

**请求参数**:
```json
[
    {
        "plugSearchMusicResult": {
            "id": "001234567890",
            "name": "歌曲名称",
            "artistName": ["歌手名"],
            "albumName": "专辑名称",
            "plugName": "qq",
            "duration": 240000
        }
    }
]
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| [].plugSearchMusicResult | object | 是 | 解析后的音乐信息对象 |
| [].plugSearchMusicResult.id | string | 是 | 歌曲ID |
| [].plugSearchMusicResult.name | string | 是 | 歌曲名称 |
| [].plugSearchMusicResult.artistName | array | 否 | 歌手名称列表 |
| [].plugSearchMusicResult.albumName | string | 否 | 专辑名称 |
| [].plugSearchMusicResult.plugName | string | 是 | 插件名称 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "下载成功",
    "data": [
        {
            "id": 1,
            "musicId": "001234567890",
            "downloadStatus": "waiting"
        }
    ]
}
```

**使用场景**:
- 当前端已经自行解析文本获取到音乐信息后,直接调用此接口
- 适合需要自定义解析逻辑的场景

---

#### 4. 下载任务模块 (TaskController)

##### 4.1 获取任务列表
**接口地址**: `/api/task/list`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 否  
**接口描述**: 分页查询下载任务列表,支持多种筛选条件

**请求参数**:
```json
{
    "downloadMusicname": "那天下雨了",
    "downloadArtistname": "周杰伦",
    "downloadAlbumname": "太阳之子",
    "downloadPlugName": "kw",
    "downloadStatus": "success",
    "downloadTimeStart": "2026-04-10 00:00:00",
    "downloadTimeEnd": "2026-04-11 00:00:00",
    "pageSize": 10,
    "pageIndex": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| downloadMusicname | string | 否 | 歌曲名称(模糊搜索) |
| downloadArtistname | string | 否 | 歌手名称(模糊搜索) |
| downloadAlbumname | string | 否 | 专辑名称(模糊搜索) |
| downloadPlugName | string | 否 | 插件名称 |
| downloadStatus | string | 否 | 下载状态(success/error/waiting/loading) |
| downloadTimeStart | string | 否 | 下载开始时间(格式:yyyy-MM-dd HH:mm:ss) |
| downloadTimeEnd | string | 否 | 下载结束时间(格式:yyyy-MM-dd HH:mm:ss) |
| pageSize | integer | 是 | 每页数量 |
| pageIndex | integer | 是 | 页码 |

**响应参数**:
```json
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "records": [
            {
                "id": 797,
                "downloadGid": "550531860",
                "downloadTime": "2026-04-10 17:01:30",
                "downloadFile": "那天下雨了 - 周杰伦",
                "downloadMusicId": "550531860",
                "downloadPlugName": "kw",
                "downloadBrType": "kw_flac_2000",
                "downloadMusicname": "那天下雨了",
                "downloadArtistname": "周杰伦",
                "downloadAlbumname": "太阳之子",
                "downloadMsg": null,
                "downloadStatus": "success",
                "springName": "nKwSearchHander",
                "audioBook": 0,
                "downloadUpdateTime": "2026-04-10 17:01:32",
                "rewriteMp3tag": 1,
                "downloadBits": "2000,320,128",
                "downloadBrTypes": "kw_flac_2000,kw_mp3_320,kw_mp3_128"
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "pages": 1
    }
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| records | array | 任务列表 |
| records[].id | integer | 下载任务ID |
| records[].downloadGid | string | 下载全局ID |
| records[].downloadTime | string | 下载创建时间 |
| records[].downloadFile | string | 下载文件名(格式:歌曲名 - 歌手名) |
| records[].downloadMusicId | string | 歌曲ID |
| records[].downloadPlugName | string | 插件名称 |
| records[].downloadBrType | string | 音质类型ID |
| records[].downloadMusicname | string | 歌曲名称 |
| records[].downloadArtistname | string | 歌手名称 |
| records[].downloadAlbumname | string | 专辑名称 |
| records[].downloadMsg | string | 下载消息(可能为null) |
| records[].downloadStatus | string | 下载状态(success/error/waiting/loading) |
| records[].springName | string | Spring Bean名称 |
| records[].audioBook | integer | 是否为有声书(0=否, 1=是) |
| records[].downloadUpdateTime | string | 最后更新时间 |
| records[].rewriteMp3tag | integer | 是否重写MP3标签(1=是, 0=否) |
| records[].downloadBits | string | 音质比特率列表(逗号分隔) |
| records[].downloadBrTypes | string | 音质类型ID列表(逗号分隔) |
| total | integer | 总记录数 |
| size | integer | 每页数量 |
| current | integer | 当前页码 |
| pages | integer | 总页数 |

---

##### 4.2 删除任务
**接口地址**: `/api/task/del`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 否  
**接口描述**: 根据ID删除指定的下载任务

**请求参数**:
```json
{
    "id": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | integer | 是 | 任务ID |

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 4.3 重新下载任务
**接口地址**: `/api/task/refreshTask`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 否  
**接口描述**: 将任务状态重置为等待,重新加入下载队列

**请求参数**:
```json
{
    "id": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | integer | 是 | 任务ID |

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 4.4 重新下载错误任务
**接口地址**: `/api/task/againTask`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 批量重新下载所有失败的任务

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 4.5 刷新正在下载的任务
**接口地址**: `/api/task/refreshDownloading`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 重新下载所有正在下载中的任务

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 4.6 删除所有错误任务
**接口地址**: `/api/task/delErrorTask`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 批量删除所有失败的下载任务

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 4.7 删除成功任务
**接口地址**: `/api/task/delSuccessTask`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 批量删除所有成功的下载任务

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 4.8 删除正在等待任务
**接口地址**: `/api/task/delWaitingTask`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 批量删除所有等待中的下载任务

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

#### 5. 插件功能模块 (PlugController)

##### 5.1 获取酷狗登录二维码
**接口地址**: `/api/plug/kg/getQrImage`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 获取酷狗音乐APP扫码登录的二维码图片

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": {
        "qrCodeUrl": "https://login.kugou.com/...",
        "qrCodeKey": "xxx-xxx-xxx"
    }
}
```

---

##### 5.2 获取酷狗扫码信息
**接口地址**: `/api/plug/kg/checkQrCodeStatus`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 检查酷狗扫码登录的状态

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": {
        "status": "scanned",
        "message": "已扫码,请在手机端确认"
    }
}
```

---

##### 5.3 微信扫酷狗登录二维码生成
**接口地址**: `/api/plug/kg/getWxQrImage`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 获取微信扫码登录酷狗的二维码

---

##### 5.4 微信扫酷狗登录二维码检测
**接口地址**: `/api/plug/kg/checkWxQrCodeStatus`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 检查微信扫码登录酷狗的状态

---

##### 5.5 刷新酷狗token
**接口地址**: `/api/plug/kg/refreshToken`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 手动刷新酷狗音乐的访问令牌

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功"
}
```

---

##### 5.6 酷狗签到
**接口地址**: `/api/plug/kg/signIn`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 执行酷狗音乐每日签到任务

---

##### 5.7 QQVIP登录相关(QQ二维码)
**接口地址**: `/api/plug/qqvip/getQrImage`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 获取QQ音乐扫码登录的二维码图片(base64格式)

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": "data:image/png;base64,iVBORw0KGgoAAAANS..."
}
```

---

##### 5.8 微信登录二维码
**接口地址**: `/api/plug/qqvip/getWechatQrImage`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 获取微信扫码登录QQ音乐的二维码图片(base64格式)

---

##### 5.9 QQ二维码检测
**接口地址**: `/api/plug/qqvip/checkQrCodeStatus`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 检查QQ扫码登录的状态并刷新cookie

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": "扫码成功"
}
```

---

##### 5.10 手动刷新QQ登录cookie
**接口地址**: `/api/plug/qqvip/refreshQQvipCookie`  
**请求方式**: GET  
**是否需要登录**: 是  
**接口描述**: 手动刷新QQ音乐的登录cookie信息

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": "刷新成功"
}
```

---

#### 6. 解析模块 (ParserController)

> 注:以下接口已标记为@Deprecated(弃用),建议使用下载模块中的替代接口

##### 6.1 解析URL歌曲(弃用)
**接口地址**: `/api/parser/parserUrl`  
**请求方式**: POST  
**是否需要登录**: 是  
**接口描述**: 解析音乐平台URL获取歌曲信息

---

##### 6.2 下载解析好的歌曲信息(弃用)
**接口地址**: `/api/parser/download/parserUrl`  
**请求方式**: POST  
**是否需要登录**: 是  

---

##### 6.3 解析文本歌曲(弃用)
**接口地址**: `/api/parser/parserText`  
**请求方式**: POST  
**是否需要登录**: 是  

---

##### 6.4 解析URL歌曲信息(弃用)
**接口地址**: `/api/parser/parserUrlInfo`  
**请求方式**: POST  
**是否需要登录**: 是  

---

#### 7. 监听歌单模块 (MonitorController)

##### 7.1 获取监控列表
**接口地址**: `/api/monitor/list`  
**请求方式**: GET  
**是否需要登录**: 否  
**接口描述**: 获取所有歌单监控任务列表

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": [
        {
            "id": 1,
            "plugName": "qq",
            "targetId": "playlist123",
            "targetName": "歌单名称",
            "createTime": "2026-04-10 15:30:00",
            "updateTime": "2026-04-10 15:30:00"
        }
    ]
}
```

---

##### 7.2 添加监控任务
**接口地址**: `/api/monitor/add`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 否  
**接口描述**: 添加新的歌单监控任务

**请求参数**:
```json
{
    "plugName": "qq",
    "targetId": "playlist123",
    "targetName": "歌单名称"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| plugName | string | 是 | 插件名称 |
| targetId | string | 是 | 监控目标ID(歌单/专辑/歌手) |
| targetName | string | 否 | 监控目标名称 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": true
}
```

**注意事项**:
- 系统会自动检测重复,同一plugName和targetId不能重复添加

---

##### 7.3 删除监控任务
**接口地址**: `/api/monitor/delete`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 否  
**接口描述**: 删除指定的歌单监控任务

**请求参数**:
```json
{
    "id": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | integer | 是 | 监控任务ID |

---

#### 8. 阿里云盘扩展模块 (ExpandController)

##### 8.1 获取阿里云盘授权码URL
**接口地址**: `/api/expand/ali/getAuthorizationCode`  
**请求方式**: POST  
**是否需要登录**: 是  
**接口描述**: 获取阿里云盘OAuth2.0授权链接

**响应参数**:
```json
{
    "code": 200,
    "msg": "成功",
    "data": {
        "url": "https://open.aliyundrive.com/oauth/authorize?..."
    }
}
```

**注意事项**:
- 需要先配置EXPAND_ALIYUN_APPID

---

##### 8.2 获取确认授权码
**接口地址**: `/api/expand/ali/getConfirmCode`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 使用授权码换取access_token

**请求参数**:
```json
{
    "code": "auth_code_xxx",
    "code_verifier": "verifier_xxx"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| code | string | 是 | 授权码 |
| code_verifier | string | 是 | 验证码 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "授权成功"
}
```

---

##### 8.3 校验access_token是否有效
**接口地址**: `/api/expand/ali/checkAccessToken`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 检查当前access_token是否有效

**响应参数**:
```json
{
    "code": 200,
    "msg": "授权成功"
}
```

---

##### 8.4 获取并设置用户信息
**接口地址**: `/api/expand/ali/getAndSetUserInfo`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 获取阿里云盘用户信息并保存到配置

**响应参数**:
```json
{
    "code": 200,
    "msg": "获取阿里云盘用户信息成功"
}
```

---

##### 8.5 校验文件夹是否存在
**接口地址**: `/api/expand/ali/checkFolder`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 检查指定路径的文件夹是否存在

**请求参数**:
```json
{
    "path": "/music/backup"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| path | string | 是 | 文件夹路径 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "检测通过",
    "data": true
}
```

---

##### 8.6 获取默认保存位置
**接口地址**: `/api/expand/ali/getDefaultSavePath`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 获取阿里云盘默认的文件保存路径

**响应参数**:
```json
{
    "code": 200,
    "msg": "获取默认保存位置成功",
    "data": "/music/backup"
}
```

---

##### 8.7 自动创建文件夹
**接口地址**: `/api/expand/ali/autoCreateFolder`  
**请求方式**: POST  
**Content-Type**: application/json  
**是否需要登录**: 是  
**接口描述**: 根据路径自动创建阿里云盘文件夹

**请求参数**:
```json
{
    "path": "/music/backup/2026"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| path | string | 是 | 要创建的文件夹路径 |

**响应参数**:
```json
{
    "code": 200,
    "msg": "创建成功"
}
```

---

##### 8.8 手动同步一次(全量)
**接口地址**: `/api/expand/ali/syncOnce`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 执行全量同步,清空历史记录后重新上传所有文件

**响应参数**:
```json
{
    "code": 200,
    "msg": "正在后台同步！"
}
```

**注意事项**:
- 此操作在后台线程执行,立即返回
- 会先清空同步记录,然后重新上传所有文件
- 适合首次同步或需要完全重新同步的场景

---

##### 8.9 增量同步
**接口地址**: `/api/expand/ali/incrementalSync`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 只上传新增或修改的文件

**响应参数**:
```json
{
    "code": 200,
    "msg": "正在后台增量同步！"
}
```

**注意事项**:
- 此操作在后台线程执行
- 仅上传新增或修改的文件,提高效率
- 适合日常定期同步

---

##### 8.10 查询所有已上传的文件列表
**接口地址**: `/api/expand/ali/queryAllUploadFile`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 查询所有已经上传到阿里云盘的文件记录

**响应参数**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "data": [
        {
            "id": 1,
            "path": "/music/周杰伦/青花瓷.flac",
            "fileId": "file_xxx",
            "uploadTime": "2026-04-10 15:30:00"
        }
    ]
}
```

---

##### 8.11 查询所有已上传的文件列表(树状展示)
**接口地址**: `/api/expand/ali/queryAllUploadFileTree`  
**请求方式**: GET/POST  
**是否需要登录**: 是  
**接口描述**: 以树状结构展示已上传的文件

**响应参数**:
```json
{
    "code": 200,
    "msg": "查询成功",
    "data": [
        {
            "name": "music",
            "type": "folder",
            "children": [
                {
                    "name": "周杰伦",
                    "type": "folder",
                    "children": [
                        {
                            "name": "青花瓷.flac",
                            "type": "file",
                            "fileInfo": {...}
                        }
                    ]
                }
            ]
        }
    ]
}
```

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| name | string | 文件/文件夹名称 |
| type | string | 类型(folder/file) |
| children | array | 子节点列表(仅文件夹有) |
| fileInfo | object | 文件详细信息(仅文件有) |

---

#### 9. 音质类型模块 (PlugBrType)

> 音质类型枚举,用于指定下载音乐的音质和格式

**平台说明**:
- **KW**: 酷我音乐
- **MG**: 咪咕音乐
- **QQ**: QQ音乐
- **NETEASE**: 网易云音乐
- **QQVIP**: QQ音乐VIP
- **KG**: 酷狗音乐
- **APPLE**: Apple Music
- **Free**: FreeMP3(已关闭移除)

---

##### 9.1 酷我音乐 (KW)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| KW_MP3_128 | 128kmp3 | mp3 | 128 | kw | kw_mp3_128 |
| KW_MP3_192 | 192kmp3 | mp3 | 192 | kw | kw_mp3_192 |
| KW_MP3_320 | 320kmp3 | mp3 | 320 | kw | kw_mp3_320 |
| KW_APE_1000 | 1000kape | ape | 1000 | kw | kw_ape_1000 |
| KW_FLAC_2000 | 2000kflac | flac | 2000 | kw | kw_flac_2000 |

---

##### 9.2 咪咕音乐 (MG)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| MG_MP3_64 | LQ | mp3 | 64 | mg | mg_mp3_64 |
| MG_MP3_128 | PQ | mp3 | 128 | mg | mg_mp3_128 |
| MG_MP3_320 | HQ | mp3 | 320 | mg | mg_mp3_320 |
| MG_M4A_1000 | SQ | m4a | 1000 | mg | mg_m4a_1000 |
| MG_FLAC_2000 | ZQ | flac | 2000 | mg | mg_flac_2000 |

---

##### 9.3 QQ音乐 (QQ)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| QQ_MP3_128 | HQ_M500 | mp3 | 128 | qq | qq_mp3_128 |
| QQ_MP3_320 | HQ_M800 | mp3 | 320 | qq | qq_mp3_320 |
| QQ_Flac_2000 | SQ_F000 | flac | 2000 | qq | qq_flac_2000 |
| QQ_Flac_3000 | HR_RS01 | falc | 3000 | qq | qq_flac_3000 |
| QQ_Flac_4000 | HR_Q000 | falc | 3000 | qq | qq_flac_4000 |
| QQ_Flac_5000 | HR_AI00 | falc | 3000 | qq | qq_flac_5000 |

---

##### 9.4 网易云音乐 (NETEASE)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| NETEASE_MP3_128 | standard | mp3 | 128 | netease | netease_mp3_128 |
| NETEASE_MP3_192 | higher | mp3 | 192 | netease | netease_mp3_192 |
| NETEASE_MP3_320 | exhigh | mp3 | 320 | netease | netease_mp3_320 |
| NETEASE_FLAC_2000 | lossless | flac | 2000 | netease | netease_flac_2000 |
| NETEASE_FLAC_3000 | hires | flac | 3000 | netease | netease_flac_3000 |

---

##### 9.5 QQ音乐VIP (QQVIP)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| QQVIP_MP3_128 | 128 | mp3 | 128 | qqvip | qqvip_mp3_128 |
| QQVIP_MP3_320 | 320 | mp3 | 320 | qqvip | qqvip_mp3_320 |
| QQVIP_Flac_2000 | flac | flac | 2000 | qqvip | qqvip_flac_2000 |
| QQVIP_Flac_3000 | flac | falc | 3000 | qqvip | qqvip_flac_3000 |
| QQVIP_Flac_4000 | flac | falc | 4000 | qqvip | qqvip_flac_4000 |
| QQVIP_Flac_5000 | flac | falc | 5000 | qqvip | qqvip_flac_5000 |
| QQVIP_Ape_2000 | ape | ape | 2000 | qqvip | qqvip_ape_2000 |
| QQVIP_M4A_2000 | m4a | m4a | 3000 | qqvip | qqvip_m4a_2000 |

---

##### 9.6 酷狗音乐 (KG)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| KG_MP3_128 | 128 | mp3 | 128 | kg | kg_mp3_128 |
| KG_MP3_320 | 320 | mp3 | 320 | kg | kg_mp3_320 |
| KG_Flac_890 | flac | flac | 890 | kg | kg_flac_890 |
| KG_Flac_2000 | flac | flac | 2000 | kg | kg_flac_2000 |
| KG_Flac_3000 | high | flac | 3000 | kg | kg_flac_3000 |
| KG_Flac_4000 | viper_atmos | flac | 4000 | kg | kg_flac_4000 |
| KG_Flac_5000 | viper_tape | flac | 5000 | kg | kg_flac_5000 |

---

##### 9.7 Apple Music (APPLE)

| 枚举值 | value | type | bit | plugName | id |
| --- | --- | --- | --- | --- | --- |
| APPLE_MP3_320 | 320 | mp3 | 320 | apple | apple_mp3_320 |
| APPLE_AAC_256 | 3000 | aac | 256 | apple | apple_aac_256 |
| APPLE_OGG_257 | 256 | ogg | 257 | apple | apple_ogg_257 |
| APPLE_M4A_258 | 257 | m4a | 258 | apple | apple_m4a_258 |
| APPLE_WAV_1500 | 1500 | wav | 1500 | apple | apple_wav_1500 |
| APPLE_FLAC_2000 | 2000 | flac | 2000 | apple | apple_flac_2000 |
| APPLE_SOURCE_9999 | 2000 | m4a | 9999 | apple | apple_source_9999 |

---

##### 9.8 字段说明

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| value | string | 音质值(用于API请求) |
| type | string | 音频格式(mp3/flac/ape/m4a/aac/ogg/wav) |
| bit | integer | 比特率(kbps) |
| plugName | string | 插件名称标识 |
| id | string | 唯一标识ID(用于前端选择) |

### 贡献指南

欢迎提交 Issue 和 Pull Request！

### 许可证

本项目采用 MIT 许可证

---

**开始使用 Simple SQ Music Plus，享受便捷的音乐管理体验！** 🎶
