FROM gradle AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:17
RUN mkdir /news
COPY --from=build /home/gradle/src/build/libs/*.jar /news/news-service-app.jar
ENTRYPOINT ["java", "-jar","/news/news-service-app.jar"]