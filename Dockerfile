FROM maven:3.9.15-amazoncorretto-21 AS builder
MAINTAINER SQ

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
RUN java -Djarmode=layertools -jar app.jar extract --destination /app/layers

# 按顺序复制各层（利用Docker缓存）
WORKDIR /app/layers
COPY --from=builder /app/layers/dependencies/ ./
COPY --from=builder /app/layers/spring-boot-loader/ ./
COPY --from=builder /app/layers/snapshot-dependencies/ ./
COPY --from=builder /app/layers/application/ ./

# 显示架构信息（便于调试）
RUN echo "Running on architecture: $(uname -m)"

# 暴露端口
EXPOSE 8099

# 挂载音乐目录
VOLUME ["/music"]

# 启动应用
CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]
