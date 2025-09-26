#!/bin/bash

# 检查Docker容器权限和状态的脚本
# 该脚本会检查当前Docker环境中是否存在sqmusic_mysql、sqmusic_web、sqmusic_main容器
# 并获取它们的版本信息

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
        exit 1
    fi
    
    info "Docker 工具已安装"
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

# 检查Docker权限
check_docker_permissions() {
    info "检查Docker权限..."
    
    if docker info &>/dev/null; then
        success "当前用户具有Docker访问权限"
        return 0
    else
        error "当前用户没有Docker访问权限，请检查用户是否属于docker组或使用sudo运行"
        return 0  # 返回0确保脚本继续执行
    fi
}

# 主函数
main() {
    info "=========================================="
    info "开始检查Docker容器权限和状态"
    info "=========================================="
    
    # 检查必要工具
    check_tools
    
    # 检查Docker权限
    check_docker_permissions
    
    # 定义要检查的容器列表
    containers=("sqmusic_mysql" "sqmusic_web" "sqmusic_main")
    
    # 详细检查每个容器
    for container in "${containers[@]}"; do
        info "========== 检查容器 $container =========="
        
        # 检查容器是否存在
        exists=$(check_container_exists "$container")
        info "容器存在状态: $exists"
        
        # 无论容器是否存在都显示详细信息
        # 获取容器版本信息
        version=$(get_container_version "$container")
        info "容器镜像版本: $version"
        
        # 获取容器运行状态
        status=$(get_container_status "$container")
        info "容器运行状态: $status"
        
        # 获取容器创建时间
        created=$(get_container_created "$container")
        info "容器创建时间: $created"
        
        echo ""
    done
    
    info "=========================================="
    info "容器权限和状态检查完成"
    info "=========================================="
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo "选项:"
    echo "  -h, --help     显示帮助信息"
    echo ""
    echo "此脚本会检查以下Docker容器的状态和版本信息:"
    echo "  1. sqmusic_mysql"
    echo "  2. sqmusic_web"
    echo "  3. sqmusic_main"
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