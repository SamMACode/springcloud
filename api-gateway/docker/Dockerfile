FROM openjdk:8u191-jre-alpine3.9
MAINTAINER Sam Ma <sam_newyork@163.com>
ENTRYPOINT ["/usr/bin/java", "-jar", "/app.jar"]
# 将构建镜像时pom.xml中<JAR_FILE>中传递的参数声明为变量JAR_FILE
ARG JAR_FILE
# 将传递的Jar包拷贝到/app.jar目录下,同时对外暴露8080的端口
ADD ${JAR_FILE} /app.jar
EXPOSE 8080