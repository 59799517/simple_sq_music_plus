
[check_update.sh](check_update.sh)脚本使用说明


如果用[docker-compose.yml](../docker-compose.yml)执行的则啥都不用改直接运行即可


如果改了配置文件则需要修改docker-compose.yml文件

### 数据库配置
- DB_IP="mysql" 数据库地址可以写服务名称
- DB_PORT="3306" 数据库端口（内部端口）
- DB_NAME="sqmusicv3" 数据库名称
- DB_USERNAME="root" 数据库用户名
- DB_PASSWORD="sqmusicv3password" 数据库密码

### 音乐目录配置
- MUSIC_DIR_HOST="$(pwd)/../music"  映射本地的路径
- MUSIC_DIR_CONTAINER="/music" 容器内部路径

###  容器名称配置
CONTAINER_MYSQL="sqmusic_mysql"  
CONTAINER_WEB="sqmusic_web"
CONTAINER_MAIN="sqmusic_main"

###  全局的网关名称 
NETWORK_NAME="simple_sq_music_plus_sq-app-network"