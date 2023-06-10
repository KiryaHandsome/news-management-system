package ru.clevertec.news.cache.wrapper;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;
import ru.clevertec.news.cache.provider.api.CacheProvider;

import java.util.concurrent.Callable;

/**
 * Abstract base class for cache implementations that wrap a CacheProvider.
 * Provides common functionality and serves as a bridge between the Cache interface and the underlying CacheProvider.
 */
public abstract class AbstractCacheWrapper implements Cache {

    private final CacheProvider cacheProvider;
    private String name;

    /**
     * Constructs an AbstractCacheWrapper with the given name and CacheProvider.
     *
     * @param name          the name of the cache
     * @param cacheProvider the underlying CacheProvider to be wrapped
     */
    protected AbstractCacheWrapper(String name, CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
        this.name = name;
    }

    /**
     * Retrieves the name of the cache.
     *
     * @return the name of the cache
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the underlying CacheProvider.
     *
     * @return the underlying CacheProvider
     */
    @Override
    public Object getNativeCache() {
        return cacheProvider;
    }

    /**
     * Retrieves the value associated with the given key from the cache.
     *
     * @param key the key of the value to retrieve
     * @return a Cache.ValueWrapper containing the value, or null if not found
     */
    @Override
    public Cache.ValueWrapper get(Object key) {
        Object value = cacheProvider.get(key);
        return (value != null) ? new SimpleValueWrapper(value) : null;
    }

    /**
     * Retrieves the value associated with the given key from the cache,
     * casting it to the specified type.
     *
     * @param key  the key of the value to retrieve
     * @param type the expected type of the value
     * @return the value if found and of the specified type, or null otherwise
     */
    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = cacheProvider.get(key);
        return (value != null && type.isInstance(value)) ? type.cast(value) : null;
    }

    /**
     * Retrieves the value associated with the given key from the cache.
     * If the value is not found, it is loaded using the provided Callable
     * and stored in the cache.
     *
     * @param key         the key of the value to retrieve
     * @param valueLoader the Callable to load the value if not found
     * @return the value if found or loaded successfully, or null otherwise
     * @throws Cache.ValueRetrievalException if an exception occurs while loading the value
     */
    @Override
    public <T> T get(Object key, @Nullable Callable<T> valueLoader) {
        Object value = cacheProvider.get(key);
        if (value != null) {
            return (T) value;
        }
        if (valueLoader != null) {
            try {
                value = valueLoader.call();
                cacheProvider.put(key, value);
                return (T) value;
            } catch (Exception e) {
                throw new Cache.ValueRetrievalException(key, valueLoader, e);
            }
        }
        return null;
    }

    /**
     * Stores the given key-value pair in the cache.
     *
     * @param key   the key to store
     * @param value the value to store
     */
    @Override
    public void put(Object key, @Nullable Object value) {
        cacheProvider.put(key, value);
    }

    /**
     * Removes the value associated with the given key from the cache.
     *
     * @param key the key of the value to remove
     */
    @Override
    public void evict(Object key) {
        cacheProvider.delete(key);
    }

    /**
     * Clears the cache, removing all stored values.
     */
    @Override
    public void clear() {
        cacheProvider.clear();
    }
}
