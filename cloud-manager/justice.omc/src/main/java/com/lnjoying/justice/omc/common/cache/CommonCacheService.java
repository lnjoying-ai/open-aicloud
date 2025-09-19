package com.lnjoying.justice.omc.common.cache;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/24 11:25
 */

public interface CommonCacheService<K, V>
{
    V get(K key);

    V put(K key, V value);

    void putAll(Map<K, V> map);

    boolean containsKey(K key);

    V remove(K key);

    boolean clear();
}
