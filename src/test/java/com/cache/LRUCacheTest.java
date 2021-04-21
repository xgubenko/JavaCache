package com.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LRUCacheTest {

    private final int ZERO_VALUE = 0;
    private final int MAX_CACHE_SIZE = 10;

    private final Cache<Integer, Integer> cache = new LRUCacheImpl<>(MAX_CACHE_SIZE);

    @Before
    public void setUp() {
        for (int i = ZERO_VALUE; i < MAX_CACHE_SIZE; i++) {
            cache.set(i, i);
        }
    }

    @Test(expected = NullPointerException.class)
    public void addNullKeyWithException() {
        cache.set(null, MAX_CACHE_SIZE);
    }

    @Test(expected = NullPointerException.class)
    public void addNullValueWithException() {
        cache.set(MAX_CACHE_SIZE, null);
    }

    @Test
    public void cacheDeletesFirstInsertedValue() {
        Assert.assertFalse(cache.containsKey(MAX_CACHE_SIZE));
        Assert.assertTrue(cache.containsKey(ZERO_VALUE));
        cache.set(MAX_CACHE_SIZE, MAX_CACHE_SIZE);
        Assert.assertFalse(cache.containsKey(ZERO_VALUE));
    }

    @Test
    public void cacheReplacesAllUnused() {
        int DOUBLE_CACHE_SIZE = 20;
        for (int i = MAX_CACHE_SIZE; i < DOUBLE_CACHE_SIZE; i++) {
            cache.set(i, i);
        }
        for (int i = ZERO_VALUE; i < MAX_CACHE_SIZE; i++) {
            Assert.assertTrue(cache.containsKey(i + MAX_CACHE_SIZE));
            Assert.assertFalse(cache.containsKey(i));
        }
    }

    @Test
    public void cacheDeletesUnusedRandom() {
        cache.get(0);
        cache.get(5);
        cache.get(9);

        // Add 7 elements to replace unused. 3 used values should stay
        for (int i = MAX_CACHE_SIZE; i < 17; i++) {
            cache.set(i, i);
        }

        Assert.assertTrue(cache.containsKey(0));
        Assert.assertTrue(cache.containsKey(5));
        Assert.assertTrue(cache.containsKey(9));
        Assert.assertFalse(cache.containsKey(1));
        Assert.assertFalse(cache.containsKey(2));
        Assert.assertFalse(cache.containsKey(3));
        Assert.assertFalse(cache.containsKey(4));
        Assert.assertFalse(cache.containsKey(6));
        Assert.assertFalse(cache.containsKey(7));
        Assert.assertFalse(cache.containsKey(8));
    }
}
