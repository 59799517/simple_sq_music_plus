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
# COPY 指令在源路径不存在时会报错，所以只复制确定存在的层
COPY dependencies/ /
COPY spring-boot-loader/ /
COPY application/ /

# 显示架构信息（便于调试）
RUN echo "Running on architecture: $(uname -m)"

# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]
