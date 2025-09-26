
#!/bin/bash

# 定义GitHub仓库的信息
repo_owner="59799517"
repo_name="simple_sq_musuc_plus"
docker_name="sqmusic" #容器名称
docker_port="${1:-8002}" #容器端口
docker_v_music="${2:-/volume1/music/}" #挂载的music路径地址
docker_host_name="${3:-nasdocker}" #容器所属的host 如果不知道就天默认的
auto_start=${4:-true}  # 默认为 true 表示自动启动容器

db_ip=${5:-"mysql"}  # 数据库ip
db_port=${6:-"3306"}  # 数据库端口号
db_name=${7:-"sqmusic"}  # 数据库名称
db_username=${8:-"root"}  # 数据库账号
db_password=${9:-"11111"}  # 数据库密码


# 构建API URL
api_url="https://api.github.com/repos/$repo_owner/$repo_name/releases/latest"

# 发送HTTP GET请求并解析JSON响应
response=$(curl -sL "$api_url")

# 获取版本号
tag_name=$(echo "$response" | jq -r '.tag_name')

# 提取第一个 asset 的 name 和 browser_download_url
asset_name=$(echo "$response" | jq -r '.assets[0].name')
asset_url=$(echo "$response" | jq -r '.assets[0].browser_download_url')

# 判断 asset 名称是否以 MusicServer 开头且以 .jar 结尾
if [[ $asset_name == MusicServer* ]] && [[ $asset_name == *.jar ]]; then
    echo "Valid asset found（找到文件信息）:"
    echo "Name（文件名称）: $asset_name"
	  echo "version（版本号）:$tag_name"
	  echo "Download URL（下载url）: $asset_url"

# 下载文件
    echo "Downloading file（开始下载文件）..."
    curl -L -# -w "\nDownload speed: %k bytes/s\n" --retry 3 -O "https://ghfast.top/$asset_url"
# 创建 Dockerfile
echo "Creating Dockerfile(创建Dockerfile文件)..."
cat > Dockerfile <<EOF
FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
MAINTAINER SQ
WORKDIR /app
COPY ./*.jar /app/app.jar
EXPOSE 8099
VOLUME ["/music"]
CMD ["java", "-jar", "app.jar"]
EOF
# 构建 Docker 镜像
    echo "Building Docker image（构建docker镜像）..."
    docker build -t $docker_name:v$tag_name .
 # 获取正在运行的容器ID
    container_id=$(docker ps -a --format "{{.Names}}" | grep $docker_name)

    if [ -n "$container_id" ]; then
        echo "Stopping existing container: $container_id"
        # 停止容器
        docker stop "$container_id"
		  # 删除容器
        docker rm "$container_id"
    fi

# 根据 auto_restart 参数决定容器的重启策略
    restart_policy="--restart=always"
    if [ "$auto_restart" != "true" ]; then
        restart_policy=""
    fi

    # 启动新容器
    echo "Starting new container(启动新的容器)..."
    docker run -d --name $docker_name --net=$docker_host_name $restart_policy --link mysql -p $docker_port:8099 -v $docker_v_music:/music -e DB_IP=${db_ip} -e DB_PORT=${db_port} -e DB_NAME=${db_name} -e DB_USERNAME=$db_username -e DB_PASSWORD=$db_password  $docker_name:v$tag_name
 # 删除下载的文件
    echo "Deleting downloaded file(删除下载文件)..."
    rm "$asset_name"
	  rm Dockerfile
else
    echo "The first asset does not match the required pattern.（未找到对应文件请手动下载打包）"
fi





