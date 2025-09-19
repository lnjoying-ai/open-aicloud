package com.lnjoying.justice.omc.common.cache;



import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/24 11:27
 */

public class CaffeineCacheServiceImpl<K, V> implements CommonCacheService<K, V>
{

    private  final Cache<K, V> cache;

    public CaffeineCacheServiceImpl(final int initialCapacity, final long maxSize, final Duration expireAfterWrite, final boolean useWeakKey)
    {
        if (useWeakKey)
        {
            this.cache = Caffeine.newBuilder().weakKeys().initialCapacity(initialCapacity).maximumSize(maxSize).expireAfterWrite(expireAfterWrite).build();
        }
        else
        {
            this.cache = Caffeine.newBuilder().initialCapacity(initialCapacity).maximumSize(maxSize).expireAfterWrite(expireAfterWrite).build();
        }
    }

    public CaffeineCacheServiceImpl(Cache<K, V> cache)
    {
        this.cache = cache;
    }
    @Override
    public V get(K key)
    {
        return cache.getIfPresent(key);
    }

    @Override
    public V put(K key, V value)
    {
        V oldValue = cache.getIfPresent(key);
        cache.put(key, value);

        return oldValue;
    }

    @Override
    public void putAll(Map<K, V> map)
    {
        cache.putAll(map);
    }

    @Override
    public boolean containsKey(K key)
    {
        return cache.asMap().containsKey(key);
    }

    @Override
    public V remove(K key)
    {
        V value = cache.getIfPresent(key);
        this.cache.invalidate(key);
        this.cache.cleanUp();
        return value;
    }

    @Override
    public boolean clear()
    {
        this.cache.invalidateAll();
        this.cache.cleanUp();
        return true;
    }
}
