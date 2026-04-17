# 多阶段构建 - 第一阶段：前端构建（平台无关）
FROM node:20-alpine AS frontend-builder
WORKDIR /frontend
COPY web/package*.json ./
RUN npm install
COPY web/ ./
RUN npm run build

# 第二阶段：使用 Maven 构建 Java 项目
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
MAINTAINER SQ

WORKDIR /build/

# 复制 pom.xml 并下载依赖（利用 Docker 缓存）
COPY pom.xml /build/
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src /build/src/

# 复制前端构建产物
COPY --from=frontend-builder /frontend/dist /build/web/dist

# 构建 Java 项目（跳过测试，使用 docker-build profile）
RUN mvn clean package -DskipTests -Pdocker-build -B

# 第二阶段：提取分层 JAR
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /extractor
COPY --from=builder /build/target/simple_sq_music_plus.jar app.jar
# 使用 Spring Boot 的分层工具提取 JAR
RUN java -Djarmode=layertools -jar app.jar extract --destination /extractor/layers

# 第三阶段：运行环境
FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

# 按层次复制文件（优化 Docker 缓存）
# dependencies 层（很少变化）
COPY --from=extractor /extractor/layers/dependencies/ ./
# spring-boot-loader 层（很少变化）
COPY --from=extractor /extractor/layers/spring-boot-loader/ ./
# application 层（经常变化）
COPY --from=extractor /extractor/layers/application/ ./

# 显示架构信息（便于调试）
RUN echo "Running on architecture: $(uname -m)" && \
    echo "Java version:" && java -version

# 设置 JVM 参数优化
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]