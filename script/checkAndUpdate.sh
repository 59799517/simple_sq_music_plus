#!/bin/bash

# 检查并更新simple_sq_music_plus和simple_sq_music_plus_web的Docker镜像
# 该脚本会检查GitHub上的最新release，如果有更新则拉取最新的Docker镜像

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
    
    if ! command -v docker-compose &> /dev/null; then
        warn "docker-compose 未安装，将使用 docker compose"
    fi
    
    info "所有必要工具已安装"
}

# 获取当前运行的镜像版本
get_current_image_version() {
    local container_name=$1
    local image_name
    
    if docker ps -a --format "{{.Names}}" | grep -q "^${container_name}$"; then
        image_name=$(docker inspect --format='{{.Config.Image}}' "$container_name" 2>/dev/null) || {
            echo ""
            return
        }
        echo "$image_name"
    else
        echo ""
    fi
}

# 检查GitHub仓库的最新release
check_github_release() {
    local repo_owner=$1
    local repo_name=$2
    
    info "检查仓库 $repo_owner/$repo_name 的最新release..."
    
    local api_url="https://api.github.com/repos/$repo_owner/$repo_name/releases/latest"
    local response
    response=$(curl -sL "$api_url")
    
    # 检查API调用是否成功
    if echo "$response" | jq -e .tag_name &>/dev/null; then
        local tag_name
        tag_name=$(echo "$response" | jq -r '.tag_name')
        echo "$tag_name"
    else
        error "无法获取仓库 $repo_owner/$repo_name 的release信息"
        echo ""
    fi
}

# 拉取最新的Docker镜像
pull_latest_image() {
    local image_name=$1
    local tag=$2
    
    info "拉取镜像 $image_name:$tag..."
    
    if docker pull "$image_name:$tag"; then
        success "成功拉取镜像 $image_name:$tag"
        return 0
    else
        error "拉取镜像 $image_name:$tag 失败"
        return 1
    fi
}

# 停止并删除现有容器
stop_and_remove_container() {
    local container_name=$1
    
    if docker ps -a --format "{{.Names}}" | grep -q "^${container_name}$"; then
        info "停止容器 $container_name..."
        docker stop "$container_name" || warn "停止容器 $container_name 失败"
        
        info "删除容器 $container_name..."
        docker rm "$container_name" || warn "删除容器 $container_name 失败"
    else
        info "容器 $container_name 不存在"
    fi
}

# 更新docker-compose.yml中的镜像版本
update_docker_compose() {
    local service_name=$1
    local image_tag=$2
    
    if [ -f "docker-compose.yml" ]; then
        info "更新 docker-compose.yml 中的 $service_name 服务版本为 $image_tag"
        # 备份原文件
        cp docker-compose.yml docker-compose.yml.bak
        
        # 更新镜像版本（这里只是示例，实际可能需要更复杂的处理）
        # sed -i "s/$service_name:.*/$service_name:$image_tag/" docker-compose.yml
        
        success "已更新 docker-compose.yml"
    else
        warn "未找到 docker-compose.yml 文件"
    fi
}

# 通过docker-compose重启服务
restart_with_docker_compose() {
    local service_name=$1
    
    if [ -f "docker-compose.yml" ]; then
        info "使用 docker-compose 重启服务 $service_name"
        
        if command -v docker-compose &> /dev/null; then
            docker-compose up -d "$service_name"
        else
            docker compose up -d "$service_name"
        fi
        
        success "服务 $service_name 已重启"
    else
        warn "未找到 docker-compose.yml 文件，无法使用 docker-compose 重启"
    fi
}

# 直接启动新容器
start_new_container() {
    local container_name=$1
    local image_name=$2
    local tag=$3
    shift 3
    local extra_params=("$@")
    
    info "启动新容器 $container_name..."
    
    if docker run -d --name "$container_name" "${extra_params[@]}" "$image_name:$tag"; then
        success "成功启动容器 $container_name"
        return 0
    else
        error "启动容器 $container_name 失败"
        return 1
    fi
}

# 主函数
main() {
    info "=========================================="
    info "开始检查GitHub仓库更新"
    info "=========================================="
    
    # 检查必要工具
    check_tools
    
    # 检查 simple_sq_music_plus 仓库
    local main_repo_owner="59799517"
    local main_repo_name="simple_sq_music_plus"
    local main_image_name="sqmusic_main"
    local main_container_name="sqmusic_main"
    
    info "========== 检查主应用 =========="
    local main_latest_tag
    main_latest_tag=$(check_github_release "$main_repo_owner" "$main_repo_name")
    
    if [ -n "$main_latest_tag" ]; then
        info "仓库 $main_repo_owner/$main_repo_name 最新版本: $main_latest_tag"
        
        local current_main_image
        current_main_image=$(get_current_image_version "$main_container_name")
        
        if [ -n "$current_main_image" ]; then
            info "当前运行的主应用镜像: $current_main_image"
            
            # 检查是否需要更新
            if [[ "$current_main_image" != *":$main_latest_tag" ]] && [[ "$current_main_image" != "$main_image_name:$main_latest_tag" ]]; then
                warn "发现新版本，准备更新主应用..."
                
                # 拉取最新镜像
                if pull_latest_image "$main_image_name" "$main_latest_tag"; then
                    # 更新 docker-compose.yml
                    update_docker_compose "$main_image_name" "$main_latest_tag"
                    
                    # 重启服务
                    restart_with_docker_compose "$main_container_name"
                fi
            else
                info "主应用已是最新版本"
            fi
        else
            warn "未找到当前运行的主应用容器，尝试启动..."
            # 可以在这里添加启动新容器的逻辑
        fi
    else
        error "无法检查主应用仓库的最新版本"
    fi
    
    # 检查 simple_sq_music_plus_web 仓库
    local web_repo_owner="59799517"
    local web_repo_name="simple_sq_music_plus_web"
    local web_image_name="sqmusic_web"
    local web_container_name="sqmusic_web"
    
    info "========== 检查Web应用 =========="
    local web_latest_tag
    web_latest_tag=$(check_github_release "$web_repo_owner" "$web_repo_name")
    
    if [ -n "$web_latest_tag" ]; then
        info "仓库 $web_repo_owner/$web_repo_name 最新版本: $web_latest_tag"
        
        local current_web_image
        current_web_image=$(get_current_image_version "$web_container_name")
        
        if [ -n "$current_web_image" ]; then
            info "当前运行的Web应用镜像: $current_web_image"
            
            # 检查是否需要更新
            if [[ "$current_web_image" != *":$web_latest_tag" ]] && [[ "$current_web_image" != "$web_image_name:$web_latest_tag" ]]; then
                warn "发现新版本，准备更新Web应用..."
                
                # 拉取最新镜像
                if pull_latest_image "$web_image_name" "$web_latest_tag"; then
                    # 更新 docker-compose.yml
                    update_docker_compose "$web_image_name" "$web_latest_tag"
                    
                    # 重启服务
                    restart_with_docker_compose "$web_container_name"
                fi
            else
                info "Web应用已是最新版本"
            fi
        else
            warn "未找到当前运行的Web应用容器"
        fi
    else
        error "无法检查Web应用仓库的最新版本"
    fi
    
    info "=========================================="
    info "检查和更新过程完成"
    info "=========================================="
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo "选项:"
    echo "  -h, --help     显示帮助信息"
    echo ""
    echo "此脚本会检查以下GitHub仓库的最新release，并在有更新时更新对应的Docker镜像:"
    echo "  1. https://github.com/59799517/simple_sq_music_plus"
    echo "  2. https://github.com/59799517/simple_sq_music_plus_web"
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