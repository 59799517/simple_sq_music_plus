#!/bin/bash

# 检查并更新 simple_sq_music_plus 和 simple_sq_music_plus_web 的 Docker 镜像
# 该脚本会检查 GitHub 上的最新 release，并拉取对应的 Docker 镜像
# 如果发现更新，则停止并删除旧容器，然后启动新容器

set -e

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
    
    if ! command -v curl &> /dev/null; then
        error "curl 未安装，请先安装 curl"
        exit 1
    fi
    
    if ! command -v docker &> /dev/null; then
        error "docker 未安装，请先安装 docker"
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        error "jq 未安装，请先安装 jq"
        exit 1
    fi
    
    success "所有必要工具已安装"
}

# 检查 GitHub 仓库的最新 release
check_github_release() {
    local repo_owner=$1
    local repo_name=$2
    
    info "检查仓库 $repo_owner/$repo_name 的最新 release..."
    
    local api_url="https://api.github.com/repos/$repo_owner/$repo_name/releases/latest"
    local response
    response=$(curl -sL "$api_url")
    
    # 检查 API 调用是否成功
    if echo "$response" | jq -e .tag_name &>/dev/null; then
        local tag_name
        tag_name=$(echo "$response" | jq -r '.tag_name')
        echo "$tag_name"
    else
        error "无法获取仓库 $repo_owner/$repo_name 的 release 信息"
        echo ""
    fi
}

# 停止并删除容器
stop_and_remove_container() {
    local container_name=$1
    
    info "检查容器 $container_name 是否正在运行..."
    
    if docker ps -a --format "{{.Names}}" | grep -q "^${container_name}$"; then
        if docker ps --format "{{.Names}}" | grep -q "^${container_name}$"; then
            info "停止容器 $container_name..."
            if docker stop "$container_name"; then
                success "成功停止容器 $container_name"
            else
                error "停止容器 $container_name 失败"
            fi
        else
            info "容器 $container_name 已停止"
        fi
        
        info "删除容器 $container_name..."
        if docker rm "$container_name"; then
            success "成功删除容器 $container_name"
        else
            error "删除容器 $container_name 失败"
        fi
    else
        info "容器 $container_name 不存在"
    fi
}

# 拉取主应用 Docker 镜像
pull_main_app() {
    local tag=$1
    local image_name="ghcr.io/59799517/simple_sq_music_plus:$tag"
    
    info "拉取主应用 Docker 镜像: $image_name"
    
    if docker pull "$image_name"; then
        success "成功拉取主应用镜像 $image_name"
        echo "$image_name"
    else
        error "拉取主应用镜像失败"
        return 1
    fi
}

# 拉取 Web 应用 Docker 镜像
pull_web_app() {
    local tag=$1
    local image_name="ghcr.io/59799517/simple_sq_music_plus_web:$tag"
    
    info "拉取 Web 应用 Docker 镜像: $image_name"
    
    if docker pull "$image_name"; then
        success "成功拉取 Web 应用镜像 $image_name"
        echo "$image_name"
    else
        error "拉取 Web 应用镜像失败"
        return 1
    fi
}

# 启动主应用容器
start_main_container() {
    local image_name=$1
    local container_name="sqmusic_main"
    
    info "启动主应用容器 $container_name..."
    
    # 停止并删除旧容器
    stop_and_remove_container "$container_name"
    
    # 启动新容器
    if docker run -d \
        --name "$container_name" \
        --network sq-app-network \
        -e DB_IP=mysql \
        -e DB_PORT=3306 \
        -e DB_NAME=sqmusicv3 \
        -e DB_USERNAME=root \
        -e DB_PASSWORD=sqmusicv3password \
        -v "$(pwd)/music:/music" \
        -p 8099:8099 \
        "$image_name"; then
        success "成功启动主应用容器 $container_name"
    else
        error "启动主应用容器 $container_name 失败"
        return 1
    fi
}

# 启动 Web 应用容器
start_web_container() {
    local image_name=$1
    local container_name="sqmusic_web"
    
    info "启动 Web 应用容器 $container_name..."
    
    # 停止并删除旧容器
    stop_and_remove_container "$container_name"
    
    # 启动新容器
    if docker run -d \
        --name "$container_name" \
        --network sq-app-network \
        -p 8096:80 \
        "$image_name"; then
        success "成功启动 Web 应用容器 $container_name"
    else
        error "启动 Web 应用容器 $container_name 失败"
        return 1
    fi
}

# 主函数
main() {
    info "=========================================="
    info "开始检查 GitHub 仓库更新并拉取 Docker 镜像"
    info "=========================================="
    
    # 检查必要工具
    check_tools
    
    # 检查 simple_sq_music_plus 仓库
    local main_repo_owner="59799517"
    local main_repo_name="simple_sq_music_plus"
    
    info "========== 检查主应用 =========="
    local main_latest_tag
    main_latest_tag=$(check_github_release "$main_repo_owner" "$main_repo_name")
    
    if [ -n "$main_latest_tag" ]; then
        info "仓库 $main_repo_owner/$main_repo_name 最新版本: $main_latest_tag"
        
        # 拉取主应用镜像
        local main_image
        main_image=$(pull_main_app "$main_latest_tag")
        
        if [ -n "$main_image" ]; then
            # 启动主应用容器
            start_main_container "$main_image"
        fi
    else
        error "无法检查主应用仓库的最新版本"
    fi
    
    # 检查 simple_sq_music_plus_web 仓库
    local web_repo_owner="59799517"
    local web_repo_name="simple_sq_music_plus_web"
    
    info "========== 检查 Web 应用 =========="
    local web_latest_tag
    web_latest_tag=$(check_github_release "$web_repo_owner" "$web_repo_name")
    
    if [ -n "$web_latest_tag" ]; then
        info "仓库 $web_repo_owner/$web_repo_name 最新版本: $web_latest_tag"
        
        # 拉取 Web 应用镜像
        local web_image
        web_image=$(pull_web_app "$web_latest_tag")
        
        if [ -n "$web_image" ]; then
            # 启动 Web 应用容器
            start_web_container "$web_image"
        fi
    else
        warn "无法检查 Web 应用仓库的最新版本，可能仓库名称不正确"
        warn "请确认仓库地址: https://github.com/$web_repo_owner/$web_repo_name"
    fi
    
    info "=========================================="
    info "Docker 镜像拉取和容器启动完成"
    info "=========================================="
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "此脚本会检查以下 GitHub 仓库的最新 release，并拉取对应的 Docker 镜像:"
    echo "  1. https://github.com/59799517/simple_sq_music_plus"
    echo "  2. https://github.com/59799517/simple_sq_music_plus_web (如果存在)"
    echo ""
    echo "Docker 镜像来自 GitHub Packages:"
    echo "  主应用: ghcr.io/59799517/simple_sq_music_plus:{版本号}"
    echo "  Web应用: ghcr.io/59799517/simple_sq_music_plus_web:{版本号}"
    echo ""
    echo "如果发现新版本，脚本会:"
    echo "  1. 停止并删除正在运行的旧容器"
    echo "  2. 拉取最新的 Docker 镜像"
    echo "  3. 启动新的容器"
    echo ""
    echo "选项:"
    echo "  -h, --help     显示帮助信息"
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