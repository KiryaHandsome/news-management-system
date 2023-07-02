# News management system

This project implements a microservice-based News Management System.
It consists of two primary microservices:

### News Service
Handles CRUD operations for news articles, including pagination,
full-text search, and comment retrieval.

### User Service 
Provides user registration and authentication functionality, 
with different roles for admin, journalist, and subscriber.

## Features

* Logging (custom Spring Boot Starter)
* Caching (overridden `Spring Boot Cache` in `dev` profile and `Redis` in `prod` profile)
* Docker 
* Spring Cloud Config Server (all property files moved to config server)
* Spring Security (user-service generates `JWT` for client, and client uses it for authorization in news-service)
* Exception handling moved to Spring Boot Starter
* Swagger

## System diagram
![Diagram](https://gist.githubusercontent.com/KiryaHandsome/6edb35a4bf2f3c714f3ec393bf1e443c/raw/6573b224802be00be7af343a29c070e544f74e5c/diagram.png)

## Getting started

1. You need to publish starters to local maven repository

   * ```./gradlew :logging-starter:publishMavenPublicationToMavenLocal```
   * ```./gradlew :exception-handling-starter:publishMavenPublicationToMavenLocal```

2. Build all services ```./gradlew build```
3. Up environment(2 databases, Redis cache, Config Server, User and News services in Docker) 
   ```docker-compose up```

4. Use API. news-service starts on `localhost:8080` and user-service on `localhost:8081`

## API description
You can see all endpoints of service on URI of service ```/swagger-ui/index.html```

