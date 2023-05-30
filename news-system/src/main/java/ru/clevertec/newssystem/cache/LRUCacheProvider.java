package ru.clevertec.newssystem.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.clevertec.newssystem.cache.api.CacheProvider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


public class LRUCacheProvider implements CacheProvider {

    /**
     * Storage for cache.
     * LinkedHashMap implements access order
     * that's why is the best approach to use it in LRU cache
     */
    private final LinkedHashMap<Integer, Object> cache;
    private final int capacity;
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * Override LinkedHashMap.removeEldestEntry in way
     * that it will remove the least recently used object
     * when size becomes more than capacity
     *
     * @param capacity max size of cache
     * @throws IllegalArgumentException when passed capacity less than 1
     */
    public LRUCacheProvider(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be natural");
        }
        this.capacity = capacity;
        cache = new LinkedHashMap<>(LRUCacheProvider.this.capacity, LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRUCacheProvider.this.capacity;
            }
        };
    }

    /**
     * Get object from cache
     *
     * @param id of desired object
     * @return
     */
    @Override
    public Optional<?> get(Integer id) {
        return Optional.ofNullable(cache.get(id));
    }

    /**
     * Put object to cache if it doesn't contain such,
     * otherwise update it in cache.
     * If size == capacity, it removes the least frequently used object
     *
     * @param id     id of stored object
     * @param object object to store
     */
    @Override
    public void put(Integer id, Object object) {
        cache.put(id, object);
    }

    /**
     * Remove object from cache by id
     * Do nothing if there is no object with such id
     *
     * @param id id object that will be deleted
     */
    @Override
    public void delete(Integer id) {
        cache.remove(id);
    }
}
