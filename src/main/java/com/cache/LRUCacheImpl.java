package com.cache;

import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is an implementation of the Least Recently Used cache
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class LRUCacheImpl<K, V> implements Cache<K, V> {

    private final Map<K, V> cache;
    private final AbstractQueue<K> queue;
    private final int maxCacheSize;

    /**
     * Public constructor for LRU cache
     *
     * @param maxCacheSize  determine max size of the cache
     */
    public LRUCacheImpl(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        cache = new HashMap<>(maxCacheSize);
        queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public V get(K key) {
        Objects.requireNonNull(key);

        queue.remove(key);
        queue.add(key);
        return cache.get(key);
    }

    @Override
    public void set(K key, V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        if (maxCacheSize <= 0) {
            return;
        }

        if (cache.containsKey(key)) {
            queue.remove(key);
        }

        if (queue.size() >= maxCacheSize) {
            K lruKey = queue.poll();
            if (lruKey != null) {
                cache.remove(lruKey);
            }
        }

        queue.add(key);
        cache.put(key, value);
    }

    @Override
    public boolean containsKey(K key) {
        return (cache.containsKey(key));
    }
}
