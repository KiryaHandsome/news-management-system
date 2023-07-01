FROM openjdk:17-jdk
ADD build/libs/cloud-config-server-1.0.jar cloud-config-server.jar
ENTRYPOINT ["java", "-jar", "cloud-config-server.jar"]
EXPOSE 8888