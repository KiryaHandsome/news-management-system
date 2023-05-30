package ru.clevertec.newssystem.cache.api;

import java.util.Optional;

/**
 * Interface for cache operations as get, put and delete
 */
public interface CacheProvider {

    /**
     * Method serves for getting entity from cache
     *
     * @param id id of desired object
     * @return object from cache if it exists, null otherwise
     */
    Optional<?> get(Integer id);

    /**
     * Method serves for saving object in cache or updating it if already presented
     *
     * @param id     object id
     * @param object value to save
     */
    void put(Integer id, Object object);

    /**
     * Method serves for deleting object from cache
     * or do nothing it if already presented
     *
     * @param id object id
     */
    void delete(Integer id);
}