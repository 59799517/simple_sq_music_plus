# 使用 Node.js 镜像进行构建
FROM node:16 AS builder

# 设置工作目录
WORKDIR /app

# 复制前端源码
COPY . /app

# 设置 npm 镜像源为淘宝镜像，提高下载速度（可选）
# RUN npm config set registry https://registry.npmmirror.com

# 列出文件结构以供调试
RUN ls -la

# 检查是否存在 package.json
RUN if [ ! -f package.json ]; then \
      echo "ERROR: package.json not found!" && \
      exit 1; \
    fi

# 安装依赖,添加错误处理
RUN echo "Installing dependencies..." && \
    npm install || \
    (echo "npm install failed, trying with --verbose flag" && \
     npm install --verbose)

# 清理 node_modules 缓存并重新安装（解决 PWA 插件问题）
RUN rm -rf node_modules/.vite && \
    npm ci --ignore-scripts || \
    npm install

# 构建前端项目
RUN npm run build

# 使用 Nginx 作为生产环境服务器
FROM nginx:alpine

# 复制构建结果到 Nginx 服务器
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制 Nginx 配置文件
COPY nginx.conf /etc/nginx/nginx.conf

# 暴露端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]

