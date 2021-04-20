package com.cache;

import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LRUCacheImpl<K, V> implements Cache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final AbstractQueue<K> queue = new ConcurrentLinkedQueue<>();
    private final int cacheSize;

    public LRUCacheImpl(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Override
    public boolean containsKey(K key) {
        return (cache.containsKey(key));
    }

    @Override
    public V get(K key) {
        queue.remove(key);
        queue.add(key);
        return cache.get(key);
    }

    @Override
    public void set(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        if (cacheSize <= 0) {
            return;
        }

        if (cache.containsKey(key)) {
            queue.remove(key);
        }

        if (queue.size() >= cacheSize) {
            K lruKey = queue.poll();
            if (lruKey != null) {
                cache.remove(lruKey);
            }
        }

        queue.add(key);
        cache.put(key, value);
    }
}
