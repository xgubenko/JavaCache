package com.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * This is an implementation of the Least Frequently Used cache
 *
 * {@link Cache}
 * @param <K> Key type
 * @param <V> Value type
 */
public class LFUCacheImpl<K, V> implements Cache<K, V> {

    private final HashMap<K, V> cache;
    private final HashMap<K, Integer> keyCallsCounter;
    private final HashMap<Integer, LinkedHashSet<K>> lists;
    private final int maxCacheSize;
    private int leastFrequentlyUsedCounter;

    /**
     * Public constructor for LFU cache
     * @param maxCacheSize determine max size of the cache
     */
    public LFUCacheImpl(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        cache = new HashMap<>(maxCacheSize);
        keyCallsCounter = new HashMap<>();
        lists = new HashMap<>();
        if (maxCacheSize > 0) {
            lists.put(1, new LinkedHashSet<>());
        }
    }

    @Override
    public V get(K key) {
        Objects.requireNonNull(key);

        int count = keyCallsCounter.get(key);
        keyCallsCounter.put(key, count + 1);
        lists.get(count).remove(key);

        if (count == leastFrequentlyUsedCounter && lists.get(count).size() == 0)
            leastFrequentlyUsedCounter++;
        if (!lists.containsKey(count + 1))
            lists.put(count + 1, new LinkedHashSet<>());
        lists.get(count + 1).add(key);
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
            cache.put(key, value);
            get(key);
            return;
        }
        if (cache.size() >= maxCacheSize) {
            K evict = lists.get(leastFrequentlyUsedCounter).iterator().next();
            lists.get(leastFrequentlyUsedCounter).remove(evict);
            cache.remove(evict);
            keyCallsCounter.remove(evict);
        }
        cache.put(key, value);
        keyCallsCounter.put(key, 1);
        leastFrequentlyUsedCounter = 1;

        lists.get(1).add(key);
    }

    @Override
    public boolean containsKey(K key) {
        return (cache.containsKey(key));
    }
}
