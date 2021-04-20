package com.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class LFUCacheImpl<K, V> implements Cache<K, V> {

    private final HashMap<K, V> cache = new HashMap<>();
    private final HashMap<K, Integer> keyCallsCounter = new HashMap<>();
    private final HashMap<Integer, LinkedHashSet<K>> lists = new HashMap<>();//Counter and item list
    private final int cacheSize;
    private int leastFrequentlyUsedValue;

    public LFUCacheImpl(int cacheSize) {
        this.cacheSize = cacheSize;

        if (cacheSize > 0) {
            lists.put(1, new LinkedHashSet<>());
        }
    }

    @Override
    public V get(K key) {
        int count = keyCallsCounter.get(key);
        keyCallsCounter.put(key, count + 1);
        lists.get(count).remove(key);

        if (count == leastFrequentlyUsedValue && lists.get(count).size() == 0)
            leastFrequentlyUsedValue++;
        if (!lists.containsKey(count + 1))
            lists.put(count + 1, new LinkedHashSet<>());
        lists.get(count + 1).add(key);
        return cache.get(key);
    }

    @Override
    public void set(K key, V value) {
        if (key == null || value == null) throw new NullPointerException();
        if (cacheSize <= 0) {
            return;
        }

        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key);
            return;
        }
        if (cache.size() >= cacheSize) {
            K evit = lists.get(leastFrequentlyUsedValue).iterator().next();
            lists.get(leastFrequentlyUsedValue).remove(evit);
            cache.remove(evit);
            keyCallsCounter.remove(evit);
        }
        cache.put(key, value);
        keyCallsCounter.put(key, 1);
        leastFrequentlyUsedValue = 1;

        lists.get(1).add(key);
    }

    @Override
    public boolean containsKey(K key) {
        return (cache.containsKey(key));
    }
}
