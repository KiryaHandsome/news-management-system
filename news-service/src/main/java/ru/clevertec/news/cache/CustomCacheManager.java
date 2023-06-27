package ru.clevertec.news.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Profile("!prod")
@Component
@RequiredArgsConstructor
public class CustomCacheManager extends AbstractCacheManager {

    @Value("${cache.capacity}")
    private String cacheCapacity;
    private final Map<String, Cache> caches = new HashMap<>();
    private final ApplicationContext applicationContext;

    @Override
    public Cache getCache(String name) {
        Cache cache = caches.get(name);
        if (cache != null) {
            log.info("Get existing cache with name '{}'", name);
            return cache;
        }
        int capacity = Integer.parseInt(cacheCapacity);
        Cache newCache = applicationContext.getBean(Cache.class, capacity, name);
        caches.put(name, newCache);
        log.info("Create new cache with name '{}'", name);
        return newCache;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return new ArrayList<>();
    }
}
