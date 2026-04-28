FROM maven:3.9.15-amazoncorretto-21 AS builder
LABEL maintainer="SQ"

WORKDIR /build/

COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package -DskipTests

# 使用支持多架构的OpenJDK镜像
FROM amazoncorretto:21-alpine3.23-full

# 设置工作目录
WORKDIR /app

# 从构建阶段复制JAR文件并解压分层
COPY --from=builder /build/target/*.jar /app/app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# 按顺序复制各层（利用Docker缓存）
# Spring Boot 默认分层：dependencies, spring-boot-loader, snapshot-dependencies, application
# 如果某个层不存在（如没有SNAPSHOT依赖），COPY会失败，所以使用条件复制
RUN if [ -d /app/dependencies ]; then cp -r /app/dependencies/* /app/; fi && \
    if [ -d /app/spring-boot-loader ]; then cp -r /app/spring-boot-loader/* /app/; fi && \
    if [ -d /app/snapshot-dependencies ]; then cp -r /app/snapshot-dependencies/* /app/; fi && \
    if [ -d /app/application ]; then cp -r /app/application/* /app/; fi

# 显示架构信息（便于调试）
RUN echo "Running on architecture: $(uname -m)"

# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]
