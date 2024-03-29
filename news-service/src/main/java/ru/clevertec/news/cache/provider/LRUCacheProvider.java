package ru.clevertec.news.cache.provider;

import ru.clevertec.news.cache.provider.api.CacheProvider;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCacheProvider implements CacheProvider {

    /**
     * Storage for cache.
     * LinkedHashMap implements access order
     * that's why is the best approach to use it in LRU cache
     */
    private LinkedHashMap<Object, Object> cache;
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
     * @param key of desired object
     * @return optional of object
     */
    @Override
    public Object get(Object key) {
        return cache.get(key);
    }

    /**
     * Put value to cache if it doesn't contain such,
     * otherwise update it in cache.
     * If size == capacity, it removes the least frequently used value
     *
     * @param key   key of stored value
     * @param value value to store
     */
    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    /**
     * Remove object from cache by key
     * Do nothing if there is no object with such key
     *
     * @param key key object that will be deleted
     */
    @Override
    public void delete(Object key) {
        cache.remove(key);
    }

    /**
     * Removes all entries from cache
     */
    @Override
    public void clear() {
        cache = new LinkedHashMap<>();
    }
}
