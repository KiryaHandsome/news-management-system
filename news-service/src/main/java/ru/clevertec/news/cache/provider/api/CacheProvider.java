package ru.clevertec.news.cache.provider.api;

/**
 * Interface for cache operations as get, put and delete
 */
public interface CacheProvider {

    /**
     * Method serves for getting entity from cache
     *
     * @param key key of desired object
     * @return object from cache if it exists, null otherwise
     */
    Object get(Object key);

    /**
     * Method serves for saving object in cache
     * or updating it if already presented
     *
     * @param key   object key
     * @param value value to save
     */
    void put(Object key, Object value);

    /**
     * Method serves for deleting object from cache
     * or do nothing it if already present
     *
     * @param key object id
     */
    void delete(Object key);

    /**
     * Method serves for clearing all elements in cache
     */
    void clear();
}