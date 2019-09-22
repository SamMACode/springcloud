FROM hub.c.163.com/library/java:alpine
ADD target/*.jar app.jar
EXPOSE 8762
ENTRYPOINT ["java", "-jar", "app.jar"]