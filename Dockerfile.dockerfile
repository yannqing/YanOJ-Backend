# 使用 OpenJDK 17 slim 版本作为基础镜像
FROM openjdk:17-slim

# 设置维护者标签
LABEL maintainer="yannqing <yannqing.com>"
LABEL version="1.0"
LABEL description="YanOJ BackEnd"

# 设置工作目录
WORKDIR /yannqing/yanoj/yanoj-backend/java

# 创建一个挂载点
VOLUME /yannqing/yanoj/yanoj-backend/logs

# 复制应用程序
COPY ./target/yanoj-backend-0.0.1-SNAPSHOT.jar /tmp/app.jar

# 暴露端口
EXPOSE 8092

# 启动命令
CMD ["java", "-jar", "/tmp/app.jar", "--spring.profiles.active=dev"]
