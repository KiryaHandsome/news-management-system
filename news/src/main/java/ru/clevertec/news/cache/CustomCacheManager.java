package ru.clevertec.news.cache;

import lombok.RequiredArgsConstructor;
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


@Profile("dev")
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
            return cache;
        }
        int capacity = Integer.parseInt(cacheCapacity);
        Cache newCache = applicationContext.getBean(Cache.class, capacity, name);
        caches.put(name, newCache);
        return newCache;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return new ArrayList<>();
    }
}
