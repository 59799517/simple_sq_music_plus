FROM maven:3.9.4-amazoncorretto-17 AS builder
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


# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
CMD ["sh", "-c", "java -jar app.jar"]