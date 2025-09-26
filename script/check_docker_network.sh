#!/bin/bash

# 检查Docker网络sq-app-network是否存在

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
    
    if ! command -v docker &> /dev/null; then
        error "docker 未安装，请先安装 docker"
        exit 1
    fi
    
    info "Docker 工具已安装"
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
    
    if docker network ls --format "{{.Name}}" | grep -q "^${network_name}$"; then
        success "Docker网络 $network_name 存在"
        return 0
    else
        error "Docker网络 $network_name 不存在"
        return 1
    fi
}

# 获取网络详细信息
get_network_details() {
    local network_name=$1
    
    info "获取网络 $network_name 的详细信息..."
    
    if docker network inspect "$network_name" &>/dev/null; then
        # 获取网络驱动
        local driver
        driver=$(docker network inspect "$network_name" --format='{{.Driver}}' 2>/dev/null)
        info "网络驱动: $driver"
        
        # 获取网络ID
        local id
        id=$(docker network inspect "$network_name" --format='{{.Id}}' 2>/dev/null)
        info "网络ID: ${id:0:12}"
        
        # 获取网络创建时间
        local created
        created=$(docker network inspect "$network_name" --format='{{.Created}}' 2>/dev/null)
        info "创建时间: $created"
        
        # 获取网络范围
        local scope
        scope=$(docker network inspect "$network_name" --format='{{.Scope}}' 2>/dev/null)
        info "网络范围: $scope"
        
        # 获取连接到网络的容器
        info "连接到网络的容器:"
        docker network inspect "$network_name" --format='{{range .Containers}}{{.Name}} ({{.IPv4Address}}){{println}}{{end}}' 2>/dev/null || echo "  无"
        
        return 0
    else
        error "无法获取网络 $network_name 的详细信息"
        return 1
    fi
}

# 列出所有自定义网络
list_custom_networks() {
    info "列出所有自定义网络:"
    
    # 获取所有自定义网络（排除默认的bridge、host和none网络）
    local custom_networks
    custom_networks=$(docker network ls --format "table {{.Name}}\t{{.Driver}}\t{{.Scope}}" | grep -v -E "^(bridge|host|none)")
    
    if [ -n "$custom_networks" ]; then
        echo "$custom_networks"
    else
        echo "  没有找到自定义网络"
    fi
}

# 主函数
main() {
    local network_name="simple_sq_music_plus_sq-app-network"
    
    info "=========================================="
    info "开始检查Docker网络 $network_name"
    info "=========================================="
    
    # 检查必要工具
    check_tools
    
    # 检查Docker权限
    check_docker_permissions
    
    # 检查网络是否存在
    if check_network_exists "$network_name"; then
        # 获取网络详细信息
        get_network_details "$network_name"
    fi
    
    echo ""
    
    # 列出所有自定义网络
    list_custom_networks
    
    info "=========================================="
    info "Docker网络检查完成"
    info "=========================================="
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo "选项:"
    echo "  -h, --help     显示帮助信息"
    echo ""
    echo "此脚本会检查Docker网络 sq-app-network 是否存在，并显示其详细信息"
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