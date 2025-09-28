## 3.0版本

[图片]


### 2.x迁移3.x版本
1. 导出已经同步过的歌单、专辑、歌手信息（文件是.json）
![img.png](img/img.png)
2. 3.0版本导入已经同步信息



### 运行项目

#### 1. docker-compose（推荐--mysql启动慢导致报错可以多运行几次）
    运行docker-compose文件即可
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
  -e MYSQL_ROOT_PASSWORD=sqmusicv3password \
  -e MYSQL_DATABASE=sqmusicv3 \
  -v ./mysql_data:/var/lib/mysql \
  -p 3306:3306 \
  --network simple_sq_music_plus_sq-app-network \
  mysql:5.7
```
2. 启动后端服务
```dockerfile
# 拉取后端服务镜像（使用最新版本号）
docker pull registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v3.0.5

# 运行后端容器
docker run -d \
  --name sqmusic_main \
  -e DB_IP=mysql \
  -e DB_PORT=3306 \
  -e DB_NAME=sqmusicv3 \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=sqmusicv3password \
  -v ./music:/music \
  --network simple_sq_music_plus_sq-app-network \
  registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v3.0.5
```
3. 启动前段服务
```dockerfile
# 拉取前端服务镜像（使用最新版本号）
docker pull registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:v3.0.5

# 运行前端容器
docker run -d \
  --name sqmusic_web \
  -p 8096:80 \
  --network simple_sq_music_plus_sq-app-network \
  registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:v3.0.5

```
#### 3.0后续升级脚本可以使用scrpit下的 check_update.sh脚本



