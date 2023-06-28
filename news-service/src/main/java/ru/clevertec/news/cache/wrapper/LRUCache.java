package ru.clevertec.news.cache.wrapper;

import ru.clevertec.news.cache.provider.LRUCacheProvider;

public class LRUCache extends AbstractCacheWrapper {

    public LRUCache(int capacity, String name) {
        super(name, new LRUCacheProvider(capacity));
    }
}
