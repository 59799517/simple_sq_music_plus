#!/bin/bash

# 综合检查脚本：检查Docker网络和容器状态

# 数据库配置
DB_IP="mysql"
DB_PORT="3306"
DB_NAME="sqmusicv3"
DB_USERNAME="root"
DB_PASSWORD="sqmusicv3password"

# 音乐目录配置
MUSIC_DIR_HOST="$(pwd)/../music"
MUSIC_DIR_CONTAINER="/music"

# 容器名称配置
CONTAINER_MYSQL="sqmusic_mysql"
CONTAINER_WEB="sqmusic_web"
CONTAINER_MAIN="sqmusic_main"

# 定义全局网络名称
NETWORK_NAME="simple_sq_music_plus_sq-app-network"

# 不使用set -e，避免命令失败时脚本提前退出
# set -e

# 定义颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log() {
    echo -e "$(date +'%Y-%m-%d %H:%M:%S') - $1"
}

info() {
    log "${GREEN}[INFO]${NC} $1"
}

warn() {
    log "${YELLOW}[WARN]${NC} $1"
}

error() {
    log "${RED}[ERROR]${NC} $1"
}

success() {
    log "${BLUE}[SUCCESS]${NC} $1"
}

# 检查必要工具
check_tools() {
    info "检查必要工具..."
    
    if ! command -v docker &> /dev/null; then
        error "docker 未安装，请先安装 docker"
        return 1
    fi
    
    # 检查 jq 是否安装
    if ! command -v jq &> /dev/null; then
        error "jq 未安装，请先安装 jq"
        return 1
    fi
    
    # 检查 curl 是否安装
    if ! command -v curl &> /dev/null; then
        error "curl 未安装，请先安装 curl"
        return 1
    fi
    
    info "Docker 工具已安装"
    return 0
}

# 检查Docker权限
check_docker_permissions() {
    info "检查Docker权限..."
    
    if docker info &>/dev/null; then
        success "当前用户具有Docker访问权限"
        return 0
    else
        error "当前用户没有Docker访问权限，请检查用户是否属于docker组或使用sudo运行"
        return 1
    fi
}

# 检查网络是否存在
check_network_exists() {
    local network_name=$1
    
    info "检查Docker网络 $network_name 是否存在..."
    
    if docker network ls --format "{{.Name}}" 2>/dev/null | grep -q "^${network_name}$"; then
        success "Docker网络 $network_name 存在"
        return 0
    else
        error "Docker网络 $network_name 不存在"
        return 1
    fi
}

# 检查容器是否存在
check_container_exists() {
    local container_name=$1
    
    # 使用docker命令检查容器是否存在，即使命令失败也要返回结果
    if docker ps -a --format "{{.Names}}" 2>/dev/null | grep -q "^${container_name}$"; then
        echo "存在"
        return 0
    else
        echo "不存在"
        return 1
    fi
}

# 获取容器镜像版本
get_container_version() {
    local container_name=$1
    local image_info
    
    # 检查容器是否存在
    if docker ps -a --format "{{.Names}}" 2>/dev/null | grep -q "^${container_name}$"; then
        # 获取镜像信息，即使失败也要返回结果
        image_info=$(docker inspect --format='{{.Config.Image}}' "$container_name" 2>/dev/null) || {
            echo "未知"
            return 0  # 返回0确保脚本继续执行
        }
        echo "$image_info"
        return 0
    else
        echo "容器不存在"
        return 0  # 返回0确保脚本继续执行
    fi
}

# 获取容器运行状态
get_container_status() {
    local container_name=$1
    local status
    
    # 检查容器是否存在
    if docker ps -a --format "{{.Names}}" 2>/dev/null | grep -q "^${container_name}$"; then
        # 获取状态信息，即使失败也要返回结果
        status=$(docker inspect --format='{{.State.Status}}' "$container_name" 2>/dev/null) || {
            echo "未知"
            return 0  # 返回0确保脚本继续执行
        }
        echo "$status"
        return 0
    else
        echo "容器不存在"
        return 0  # 返回0确保脚本继续执行
    fi
}

# 获取容器创建时间
get_container_created() {
    local container_name=$1
    local created
    
    # 检查容器是否存在
    if docker ps -a --format "{{.Names}}" 2>/dev/null | grep -q "^${container_name}$"; then
        # 获取创建时间，即使失败也要返回结果
        created=$(docker inspect --format='{{.Created}}' "$container_name" 2>/dev/null) || {
            echo "未知"
            return 0  # 返回0确保脚本继续执行
        }
        echo "$created"
        return 0
    else
        echo "容器不存在"
        return 0  # 返回0确保脚本继续执行
    fi
}

# 获取GitHub仓库最新版本
get_latest_version() {
    local repo_url=$1
    local repo_name=$2
    
    # 构建API URL
    local api_url="https://api.github.com/repos/$repo_url/releases/latest"
    
    # 发送HTTP GET请求并解析JSON响应
    local response
    response=$(curl -sL "$api_url" 2>/dev/null) || {
        echo "未知"
        return 0
    }
    
    # 检查响应是否有效
    if echo "$response" | jq -e .tag_name &>/dev/null; then
        # 获取版本号
        local tag_name
        tag_name=$(echo "$response" | jq -r '.tag_name')
        echo "$tag_name"
    else
        echo "未知"
    fi
    
    return 0
}

# 获取Web版本号（基于主应用版本号）
get_web_version_by_main_version() {
    local main_version=$1
    
    # 从GitHub下载对应版本的application.yml文件并提取webversion
    local app_yml_url="https://gh.xmly.dev/https://raw.githubusercontent.com/59799517/simple_sq_musuc_plus/${main_version}/src/main/resources/application.yml"
    local response
    response=$(curl -sL "$app_yml_url" 2>/dev/null) || {
        echo "未知"
        return 0
    }
    
    # 从application.yml中提取webversion
    local web_version
    web_version=$(echo "$response" | grep "webversion:" | cut -d ' ' -f 2)
    
    if [ -n "$web_version" ]; then
        echo "$web_version"
    else
        echo "未知"
    fi
    
    return 0
}

# 检查容器状态
check_container_status() {
    local container_name=$1
    
    info "========== 检查容器 $container_name =========="
    
    # 检查容器是否存在
    exists=$(check_container_exists "$container_name")
    info "容器存在状态: $exists"
    
    # 无论容器是否存在都显示详细信息
    # 获取容器版本信息
    version=$(get_container_version "$container_name")
    info "容器镜像版本: $version"
    
    # 获取容器运行状态
    status=$(get_container_status "$container_name")
    info "容器运行状态: $status"
    
    # 获取容器创建时间
    created=$(get_container_created "$container_name")
    info "容器创建时间: $created"
    
    echo ""
}

# 更新容器到最新版本
update_container() {
    local container_name=$1
    local latest_version=$2
    
    info "开始更新 $container_name 到版本 $latest_version"
    
    if [ "$container_name" = "sqmusic_main" ]; then
        # 拉取最新的 sqmusic_main 镜像
        info "正在拉取镜像: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v$latest_version"
        if docker pull "registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v$latest_version"; then
            success "成功拉取镜像: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v$latest_version"
            
            # 停止并删除旧容器
            info "正在停止容器: $container_name"
            docker stop "$container_name" 2>/dev/null || warn "容器 $container_name 未运行或不存在"
            
            info "正在删除容器: $container_name"
            docker rm "$container_name" 2>/dev/null || warn "容器 $container_name 不存在"
            
            # 启动新容器
            info "正在启动新容器: $container_name"
            local run_cmd="docker run -d \
                --name $container_name \
                --network $NETWORK_NAME \
                -e DB_IP=$DB_IP \
                -e DB_PORT=$DB_PORT \
                -e DB_NAME=$DB_NAME \
                -e DB_USERNAME=$DB_USERNAME \
                -e DB_PASSWORD=$DB_PASSWORD \
                -v $MUSIC_DIR_HOST:$MUSIC_DIR_CONTAINER \
                registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v$latest_version"
            
            info "执行命令: $run_cmd"
            
            if eval "$run_cmd"; then
                success "成功启动新容器: $container_name"
            else
                error "启动新容器失败: $container_name"
            fi
        else
            error "拉取镜像失败: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus:v$latest_version"
        fi
    elif [ "$container_name" = "sqmusic_web" ]; then
        # 拉取最新的 sqmusic_web 镜像
        info "正在拉取镜像: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:$latest_version"
        if docker pull "registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:$latest_version"; then
            success "成功拉取镜像: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:$latest_version"
            
            # 停止并删除旧容器
            info "正在停止容器: $container_name"
            docker stop "$container_name" 2>/dev/null || warn "容器 $container_name 未运行或不存在"
            
            info "正在删除容器: $container_name"
            docker rm "$container_name" 2>/dev/null || warn "容器 $container_name 不存在"
            
            # 启动新容器
            info "正在启动新容器: $container_name"
            local run_cmd="docker run -d \
                --name $container_name \
                --network $NETWORK_NAME \
                -p 8096:80 \
                registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:$latest_version"
            
            info "执行命令: $run_cmd"
            
            if eval "$run_cmd"; then
                success "成功启动新容器: $container_name"
            else
                error "启动新容器失败: $container_name"
            fi
        else
            error "拉取镜像失败: registry.cn-hangzhou.aliyuncs.com/sqdockler/simple_sq_music_plus_web:$latest_version"
        fi
    fi
}

# 检查应用容器的最新版本
check_app_versions() {
    info "========== 检查应用容器最新版本 =========="
    
    # 检查 sqmusic_main 最新版本
    local main_latest_version
    main_latest_version=$(get_latest_version "59799517/simple_sq_musuc_plus" "sqmusic_main")
    info "sqmusic_main 最新版本: $main_latest_version"
    
    # 检查 sqmusic_web 最新版本（基于 sqmusic_main 版本）
    local web_latest_version
    web_latest_version=$(get_web_version_by_main_version "$main_latest_version")
    info "sqmusic_web 最新版本: $web_latest_version"
    
    # 获取当前容器版本
    local main_current_version
    main_current_version=$(get_container_version "sqmusic_main")
    info "sqmusic_main 当前版本: $main_current_version"
    
    local web_current_version
    web_current_version=$(get_container_version "sqmusic_web")
    info "sqmusic_web 当前版本: $web_current_version"
    
    # 比较版本（去除可能的前缀v）
    local main_latest_clean
    local main_current_clean
    local web_latest_clean
    local web_current_clean
    
    # 去除最新版本中的v前缀
    main_latest_clean=$(echo "$main_latest_version" | sed 's/^[vV]//')
    web_latest_clean=$(echo "$web_latest_version" | sed 's/^[vV]//')
    
    # 从当前版本中提取版本号部分（去除镜像名和可能的v前缀）
    main_current_clean=$(echo "$main_current_version" | sed -E 's/.*:([vV]?[0-9].*)/\1/' | sed 's/^[vV]//')
    web_current_clean=$(echo "$web_current_version" | sed -E 's/.*:([vV]?[0-9].*)/\1/' | sed 's/^[vV]//')
    
    # 调试信息（可选）
    # info "调试: main_latest_clean=$main_latest_clean, main_current_clean=$main_current_clean"
    # info "调试: web_latest_clean=$web_latest_clean, web_current_clean=$web_current_clean"
    
    # 检查 sqmusic_main 是否需要更新
    if [ "$main_latest_clean" = "$main_current_clean" ]; then
        success "sqmusic_main 当前已是最新版本"
    else
        warn "sqmusic_main 有新版本可用: $main_latest_version"
        update_container "sqmusic_main" "$main_latest_version"
    fi
    
    # 检查 sqmusic_web 是否需要更新
    if [ "$web_latest_clean" = "$web_current_clean" ]; then
        success "sqmusic_web 当前已是最新版本"
    else
        warn "sqmusic_web 有新版本可用: $web_latest_version"
        update_container "sqmusic_web" "$web_latest_version"
    fi
    
    echo ""
}

# 主函数
main() {
    # 注意：这里不再定义local network_name，而是使用全局变量
    
    info "=========================================="
    info "开始综合检查Docker网络和容器状态"
    info "=========================================="
    
    # 检查必要工具
    check_tools || {
        error "必要工具检查失败"
        exit 1
    }
    
    # 检查Docker权限
    check_docker_permissions || {
        error "Docker权限检查失败"
        exit 1
    }
    
    # 检查网络是否存在
    if ! check_network_exists "$NETWORK_NAME"; then
        error "请先运行 docker-compose up 命令创建所需的网络和容器"
        exit 1
    fi
    
    # 定义要检查的容器列表
    containers=("$CONTAINER_MYSQL" "$CONTAINER_WEB" "$CONTAINER_MAIN")
    
    # 记录不存在的容器数量
    local not_exist_count=0
    
    # 详细检查每个容器
    for container in "${containers[@]}"; do
        # 检查容器是否存在
        exists=$(check_container_exists "$container")
        if [ "$exists" == "不存在" ]; then
            not_exist_count=$((not_exist_count + 1))
        fi
    done
    
       # 如果任意一个容器不存在，提示运行docker-compose
    if [ $not_exist_count -gt 0 ]; then
        error "检测到有容器不存在，请先运行 docker-compose up 命令创建容器"
        exit 1
    fi
    
    # 检查各个容器的详细状态
    for container in "${containers[@]}"; do
        check_container_status "$container"
    done
    
    # 检查应用容器的最新版本
    check_app_versions
    
    info "=========================================="
    info "Docker网络和容器状态检查完成"
    info "=========================================="
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo "选项:"
    echo "  -h, --help     显示帮助信息"
    echo ""
    echo "此脚本会按顺序执行以下检查:"
    echo "  1. 检查Docker网络 $NETWORK_NAME 是否存在"
    echo "  2. 如果网络不存在，提示运行 docker-compose 并退出"
    echo "  3. 如果网络存在，检查容器状态"
    echo "  4. 如果所有容器都不存在，提示运行 docker-compose 并退出"
    echo "  5. 否则显示所有容器的详细状态信息"
    echo ""
}

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            error "未知选项 $1"
            show_help
            exit 1
            ;;
    esac
done

# 运行主函数
main "$@"