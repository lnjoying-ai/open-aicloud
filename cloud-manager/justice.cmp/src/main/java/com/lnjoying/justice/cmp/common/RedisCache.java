package com.lnjoying.justice.cmp.common;

import com.lnjoying.justice.schema.common.RedisCacheField;

public interface RedisCache
{
    String WORKER_IF = RedisCacheField.CMP + ":" + RedisCacheField.WORKER + ":" + RedisCacheField.IF + ":";

    String REGION_GW_WORKER = RedisCacheField.CMP + ":" + RedisCacheField.REGION + ":";

    String CLOUD_HEALTH_STATUS = RedisCacheField.CMP + ":" + RedisCacheField.CLOUD + ":" + RedisCacheField.HEALTH + ":";

    String CLOUD_HEALTH_IDS = RedisCacheField.CMP + ":" + RedisCacheField.CLOUD + ":" + RedisCacheField.HEALTH;

    String CLOUD_SYNC_IDS = RedisCacheField.CMP + ":" + RedisCacheField.CLOUD + ":" + RedisCacheField.SYNC;

    String CLOUD_STATUS = RedisCacheField.CMP + ":" + RedisCacheField.CLOUD + ":STATUS:";

    String CLOUD_USAGE = RedisCacheField.CMP + ":" + RedisCacheField.CLOUD + ":USAGE:";

    String USER_METRICS = RedisCacheField.CMP + ":USER:METRICS:";
}
