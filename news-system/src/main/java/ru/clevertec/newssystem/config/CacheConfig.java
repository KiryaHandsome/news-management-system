package ru.clevertec.newssystem.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.newssystem.cache.LFUCacheProvider;
import ru.clevertec.newssystem.cache.LRUCacheProvider;
import ru.clevertec.newssystem.cache.api.CacheProvider;

@Configuration
public class CacheConfig {

    @Value("${cache.capacity:20}")
    private String capacityProperty;
    private Integer capacity;

    @PostConstruct
    public void init() {
        capacity = Integer.parseInt(capacityProperty);
    }

    @Bean
    @ConditionalOnProperty(
            name = "cache.impl",
            havingValue = "LFU"
    )
    public CacheProvider<String> LFUcacheProvider() {
        return new LFUCacheProvider<>(capacity);
    }

    @Bean
    @ConditionalOnProperty(
            name = "cache.impl",
            havingValue = "LRU"
    )
    public CacheProvider<String> LRUcacheProvider() {
        return new LRUCacheProvider<>(capacity);
    }
}
