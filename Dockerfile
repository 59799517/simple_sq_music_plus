FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
MAINTAINER SQ

WORKDIR /build/

COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package

# 使用支持多架构的OpenJDK镜像
FROM amazoncorretto:17-alpine3.23-full

# 设置工作目录
WORKDIR /app

# 从构建阶段复制JAR文件
COPY --from=builder /build/target/*.jar /app/app.jar

# 显示架构信息（便于调试）
RUN echo "Running on architecture: $(uname -m)"

# 根据架构设置不同的内存参数
RUN ARCH=$(uname -m) && \
    if [ "$ARCH" = "aarch64" ]; then \
        echo "Setting ARM64 memory parameters" && \
        export JAVA_OPTS="-Xms64m -Xmx256m"; \
    elif [ "$ARCH" = "armv7l" ]; then \
        echo "Setting ARM32 memory parameters" && \
        export JAVA_OPTS="-Xms32m -Xmx128m"; \
    else \
        echo "Setting default memory parameters" && \
        export JAVA_OPTS="-Xms128m -Xmx512m"; \
    fi

# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]