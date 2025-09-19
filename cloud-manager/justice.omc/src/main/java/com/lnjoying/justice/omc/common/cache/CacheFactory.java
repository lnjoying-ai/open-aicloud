package com.lnjoying.justice.omc.common.cache;

import java.time.Duration;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/24 11:19
 */

public final class CacheFactory
{
    private CacheFactory() {}

    public static final String CACHE_KEY_ALERT_INHIBITION_RULE = "alert_inhibition_rule";

    public static final String CACHE_KEY_ALERT_SILENCE_RULE = "alert_silence_rule";


    private static final CommonCacheService<String, Object> ALERT_INHIBITION_RULE_CACHE = new CaffeineCacheServiceImpl<>(10, 1000L, Duration.ofDays(1), false);

    private static final CommonCacheService<String, Object> ALERT_SILENCE_RULE_CACHE = new CaffeineCacheServiceImpl<>(10, 1000L, Duration.ofDays(1), false);

    public static CommonCacheService<String, Object> getAlertInhibitionRuleCache()
    {
        return ALERT_INHIBITION_RULE_CACHE;
    }

    public static CommonCacheService<String, Object> getAlertSilenceRuleCache()
    {
        return ALERT_SILENCE_RULE_CACHE;
    }

}
