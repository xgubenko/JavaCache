package com.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LFUCacheTest {

    private final int ZERO_VALUE = 0;
    private final int MAX_CACHE_SIZE = 10;
    private final int DOUBLE_CACHE_SIZE = 20;

    private final Cache<Integer, Integer> cache = new LFUCacheImpl<>(MAX_CACHE_SIZE);


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

        for (int i = MAX_CACHE_SIZE; i < DOUBLE_CACHE_SIZE; i++) {
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

    @Test
    public void cacheDeletesUnusedBeginning() {
        cache.get(0);
        cache.get(1);
        cache.get(2);

        for (int i = MAX_CACHE_SIZE; i < DOUBLE_CACHE_SIZE; i++) {
            cache.set(i, i);
        }

        Assert.assertTrue(cache.containsKey(0));
        Assert.assertTrue(cache.containsKey(1));
        Assert.assertTrue(cache.containsKey(2));

        // Assert oldest unused values don't exist
        for (int i = 3; i < 13; i++) {
            Assert.assertFalse(cache.containsKey(i));
        }
    }
}
