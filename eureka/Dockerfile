# 从网易云镜像中心先获取下载到jdk镜像.
FROM hub.c.163.com/library/java:alpine

# 将eureka服务打包后的jar文件添加到docker中,并对其进行重命名为app.jar.
ADD target/*.jar app.jar

# 对外声明端口为8762,外部可以通过该端口进行访问eureka服务.
EXPOSE 8762

# 在ENTRYPOINT中指明运行容器中的jar文件需要制定的命令.
ENTRYPOINT ["java", "-jar", "app.jar"]