FROM openjdk:17-jdk
ADD build/libs/user-service-1.0.jar user-service.jar
ENTRYPOINT ["java", "-jar", "user-service.jar"]
EXPOSE 8081