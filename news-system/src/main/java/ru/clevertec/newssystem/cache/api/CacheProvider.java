package ru.clevertec.newssystem.cache.api;

import java.util.Optional;

/**
 * Interface for cache operations as get, put and delete
 */
public interface CacheProvider<K> {

    /**
     * Method serves for getting entity from cache
     *
     * @param key key of desired object
     * @return object from cache if it exists, null otherwise
     */
    Object get(K key);

    /**
     * Method serves for saving object in cache or updating it if already presented
     *
     * @param key   object key
     * @param value value to save
     */
    void put(K key, Object value);

    /**
     * Method serves for deleting object from cache
     * or do nothing it if already presented
     *
     * @param key object id
     */
    void delete(K key);
}