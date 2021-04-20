package com.cache;

public interface Cache<K, V> {

    V get(K key);

    void set(K key, V value);

    boolean containsKey(K key);
}