# 多阶段构建 - 第一阶段：使用 Maven 构建
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
MAINTAINER SQ

WORKDIR /build/

# 复制源代码和 pom.xml
COPY pom.xml /build/
COPY src /build/src/
COPY web /build/web/

# 构建项目（跳过测试）
RUN mvn clean package -DskipTests -B

# 第二阶段：使用 JRE 21 运行（支持多架构：amd64/arm64）
FROM eclipse-temurin:21-jre-alpine

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 JAR 文件
COPY --from=builder /build/target/simple_sq_music_plus.jar /app/app.jar

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
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]