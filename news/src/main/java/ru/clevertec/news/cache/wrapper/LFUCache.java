package ru.clevertec.news.cache.wrapper;

import ru.clevertec.news.cache.provider.LFUCacheProvider;


public class LFUCache extends AbstractCacheWrapper {

    public LFUCache(int capacity, String name) {
        super(name, new LFUCacheProvider(capacity));
    }
}
