package ru.clevertec.news.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import ru.clevertec.news.cache.wrapper.LFUCache;
import ru.clevertec.news.cache.wrapper.LRUCache;

@Profile("!prod")
@Configuration
public class CustomCacheConfig {

    @Value("${cache.impl}")
    private String cacheType;

    private static final String LRU_CACHE = "LRU";
    private static final String LFU_CACHE = "LFU";

    /**
     * Bean that implements factory for cache.
     *
     * @param capacity cache capacity
     * @param name cache name
     * @return cache instance
     */
    @Bean
    @Scope("prototype")
    public Cache cache(int capacity, String name) {
        if (LRU_CACHE.equalsIgnoreCase(cacheType)) {
            return new LRUCache(capacity, name);
        }
        if (LFU_CACHE.equalsIgnoreCase(cacheType)) {
            return new LFUCache(capacity, name);
        }
        return new ConcurrentMapCache("default");
    }
}
