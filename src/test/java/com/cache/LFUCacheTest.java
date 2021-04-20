package com.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LFUCacheTest {

    Cache<Integer, Integer> cache = new LFUCacheImpl<>(10);

    @Before
    public void setUp() {
        for (int i = 0; i < 10; i++) {
            cache.set(i, i);
        }
    }

    @Test
    public void cacheDeletesFirstInsertedValue() {
        Assert.assertFalse(cache.containsKey(10));
        Assert.assertTrue(cache.containsKey(0));
        cache.set(10, 10);
        Assert.assertFalse(cache.containsKey(0));
    }

    @Test
    public void cacheReplacesAllUnused() {
        for (int i = 10; i < 20; i++) {
            cache.set(i, i);
        }
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue(cache.containsKey(i + 10));
            Assert.assertFalse(cache.containsKey(i));
        }
    }

    @Test
    public void cacheDeletesUnused() {
        cache.get(0);
        cache.get(5);
        cache.get(9);

        for (int i = 10; i < 17; i++) {
            cache.set(i, i);
        }

        Assert.assertTrue(cache.containsKey(0));
        Assert.assertTrue(cache.containsKey(5));
        Assert.assertTrue(cache.containsKey(9));
        Assert.assertFalse(cache.containsKey(1));
        Assert.assertFalse(cache.containsKey(1));
        Assert.assertFalse(cache.containsKey(2));
        Assert.assertFalse(cache.containsKey(3));
        Assert.assertFalse(cache.containsKey(4));
        Assert.assertFalse(cache.containsKey(6));
        Assert.assertFalse(cache.containsKey(7));
        Assert.assertFalse(cache.containsKey(8));
    }

    @Test
    public void cacheNeverDeletesUsedWhenUnusedExist() {
        cache.get(0);
        cache.get(1);
        cache.get(2);

        for (int i = 10; i < 30; i++) {
            cache.set(i, i);
        }

        Assert.assertTrue(cache.containsKey(0));
        Assert.assertTrue(cache.containsKey(1));
        Assert.assertTrue(cache.containsKey(2));

        for (int i = 3; i < 23; i++) {
            Assert.assertFalse(cache.containsKey(i));
        }
    }
}
