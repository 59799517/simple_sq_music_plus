
最新NAS坏了 等修好了再更新版本


### simple_sq_music_plus
是下载音乐工具，可以当普通的音乐下载工具使用，支持，flac，ape，mp3等格式的下载（根据码率不同）， 下载的歌曲目录结构支持emby 与 subsonic（后续开放） 类的服务，下载文件支持文件标签识别，歌词下载。
```js
\Music下载根路径
       \歌手名称
               \专辑名称
                       1- 歌曲1.flac
                       2- 歌曲2.flac

```
默认支持群辉等第三方音乐服务标识：
emby,jellyfin识别请参考如下配置 https://support.emby.media/support/solutions/articles/44001159113-music-naming

- 默认账号：admin
- 默认密码：admin  （登录后设置自行修改）




效果截图
![wechat_2025-09-28_160749_034.png](img/wechat_2025-09-28_160749_034.png)
![wechat_2025-09-28_160832_175.png](img/wechat_2025-09-28_160832_175.png)
![wechat_2025-09-28_160855_350.png](img/wechat_2025-09-28_160855_350.png)
![wechat_2025-09-28_160921_626.png](img/wechat_2025-09-28_160921_626.png)
![wechat_2025-09-28_160933_756.png](img/wechat_2025-09-28_160933_756.png)
![wechat_2025-09-28_161030_965.png](img/wechat_2025-09-28_161030_965.png)
![wechat_2025-09-28_161133_048.png](img/wechat_2025-09-28_161133_048.png)
![wechat_2025-09-28_161212_579.png](img/wechat_2025-09-28_161212_579.png)
![wechat_2025-09-28_161306_157.png](img/wechat_2025-09-28_161306_157.png)
![wechat_2025-09-28_161505_837.png](img/wechat_2025-09-28_161505_837.png)
![wechat_2025-09-28_161552_590.png](img/wechat_2025-09-28_161552_590.png)



### 2.x迁移3.x版本
1. 导出已经同步过的歌单、专辑、歌手信息（文件是.json）
![img.png](img/img.png)
2. 3.0版本导入已经同步信息(时间较长耐心等待)
![wechat_2025-09-28_163226_700.png](img/wechat_2025-09-28_163226_700.png)

### 运行项目
#### 1. docker-compose（推荐--mysql启动慢导致报错可以多运行几次）
    运行docker-compose文件即可（本地编译使用docker-compose-local）
#### 2.docker启动请参考docker-compose配置手动启动
1. 启动mysql
```dockerfile
# 拉取 MySQL 5.7 镜像
docker pull mysql:5.7

# 创建自定义网络
docker network create sq-app-network

# 运行 MySQL 容器
docker run -d \
  --name sqmusic_mysql \
  --restart=always \
  -e MYSQL_ROOT_PASSWORD=sqmusicv3password \
  -e MYSQL_DATABASE=sqmusicv3 \
  -v ./mysql_data:/var/lib/mysql \
  -p 3306:3306 \
  --network simple_sq_music_plus_sq-app-network \
  mysql:5.8
```
2. 启动后端服务
```dockerfile
# 拉取后端服务镜像（使用最新版本号）
docker pull registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v3.0.8

# 运行后端容器
docker run -d \
  --name sqmusic_main \
  --restart=always \
  -e DB_IP=mysql \
  -e DB_PORT=3306 \
  -e DB_NAME=sqmusicv3 \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=sqmusicv3password \
  -v ./music:/music \
  --network simple_sq_music_plus_sq-app-network \
  registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v3.0.8
```
3. 启动前段服务
```dockerfile
# 拉取前端服务镜像（使用最新版本号）
docker pull registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:v3.0.5

# 运行前端容器
docker run -d \
  --name sqmusic_web \
  --restart=always \
  -p 8996:80 \
  --network simple_sq_music_plus_sq-app-network \
  registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:v3.0.5

```
#### 3.0后续升级脚本可以使用scrpit下的 check_update.sh脚本



