package com.cache;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * This is an interface for cache implementations
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface Cache<K, V> {

    /**
     * Get a value from the cache
     *
     * @param key The key to get
     * @return The value of the looked up key (or null if it does not exist)
     */

    @Nullable V get(@NotNull K key);

    /**
     * Save key and value
     *
     * @param key Key to use
     * @param value Value to use
     */
    void set(@NotNull K key, @NotNull V value);

    /**
     * Determine whether the cache contains a particular key
     *
     * @param key Key to check for existence
     * @return Whether the key isx within the cache
     */
    boolean containsKey(@NotNull K key);
}