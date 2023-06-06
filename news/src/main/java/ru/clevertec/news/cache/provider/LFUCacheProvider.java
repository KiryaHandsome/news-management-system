package ru.clevertec.news.cache.provider;

import ru.clevertec.news.cache.provider.api.CacheProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class LFUCacheProvider<K> implements CacheProvider<K> {

    private Map<K, Object> values = new HashMap<>();

    /**
     * Map that contains key of object
     * and count of usages as value
     */
    private Map<K, Integer> countMap = new HashMap<>();

    /**
     * Sorted map that contains frequency of using as key
     * and list of objects keys as value
     */
    private TreeMap<Integer, List<K>> frequencyMap = new TreeMap<>();
    private final int capacity;

    /**
     * @param capacity max size of cache
     * @throws IllegalArgumentException if passed capacity less than 1
     */
    public LFUCacheProvider(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be natural");
        }
        this.capacity = capacity;
    }

    /**
     * Get object from cache
     *
     * @param key of desired object
     * @return instance if object in cache, null otherwise
     */
    @Override
    public Object get(K key) {
        if (!values.containsKey(key)) {
            return null;
        }
        updateStorages(key);
        return values.get(key);
    }

    /**
     * Update count of usages in {@link LFUCacheProvider#countMap}
     * and add key to {@link LFUCacheProvider#frequencyMap} with updated frequency
     *
     * @param key key of upgradeable object
     */
    private void updateStorages(K key) {
        int frequency = countMap.get(key);
        countMap.put(key, frequency + 1);
        frequencyMap.get(frequency).remove(key);
        if (frequencyMap.get(frequency).size() == 0) {
            frequencyMap.remove(frequency);
        }
        frequencyMap.computeIfAbsent(frequency + 1, v -> new LinkedList<>()).add(key);
    }

    /**
     * Put object to cache if it doesn't contain such,
     * otherwise update it in cache.
     * If {@link LFUCacheProvider#values}.size() == {@link LFUCacheProvider#capacity}, it removes the least frequently used object
     *
     * @param key    key of stored object
     * @param object object to store
     */
    @Override
    public void put(K key, Object object) {
        if (!values.containsKey(key)) {
            if (values.size() == capacity) {
                int leastFrequency = frequencyMap.firstKey();
                K keyToRemove = frequencyMap.get(leastFrequency).remove(0);

                if (frequencyMap.get(leastFrequency).size() == 0) {
                    frequencyMap.remove(leastFrequency);
                }

                countMap.remove(keyToRemove);
                values.remove(keyToRemove);
            }
            values.put(key, object);
            countMap.put(key, 1);
            frequencyMap.computeIfAbsent(1, v -> new LinkedList<>()).add(key);
        } else {
            values.put(key, object);
            updateStorages(key);
        }
    }

    /**
     * Remove object from cache by key
     *
     * @param key key object that will be deleted
     *            Do nothing if there is no object with such key
     */
    @Override
    public void delete(K key) {
        if (values.containsKey(key)) {
            int frequency = countMap.get(key);
            countMap.remove(key);
            values.remove(key);
            frequencyMap.get(frequency).remove(key);
        }
    }

    @Override
    public void clear() {
        values = new HashMap<>();
        countMap = new HashMap<>();
        frequencyMap = new TreeMap<>();
    }
}
