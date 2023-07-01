package ru.clevertec.user.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BaseIntegrationTest {

    public static final String POSTGRES_IMAGE = "postgres:15-alpine";
    protected static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>(POSTGRES_IMAGE);
        container.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }
}