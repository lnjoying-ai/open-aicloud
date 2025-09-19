package com.lnjoying.justice.scheduler.common.constant;

import com.lnjoying.justice.schema.common.RedisCacheField;

public interface RedisCache
{
    Integer NODE_REMAIN_RESOURCES_DURATION = 24*60*60;
    String SCHED_TEMP_REGION = RedisCacheField.SCHED + ":" + RedisCacheField.TEMP + ":" + RedisCacheField.REGION + ":";
    String SCHED_TEMP_SITE = RedisCacheField.SCHED + ":" + RedisCacheField.TEMP + ":" + RedisCacheField.SITE + ":";
    String SCHED_TEMP_EDGE = RedisCacheField.SCHED + ":" + RedisCacheField.TEMP + ":" + RedisCacheField.EDGE + ":";
    String SCHED_TEMP = RedisCacheField.SCHED + ":" + RedisCacheField.TEMP + ":";
    String SCHED_ALL_REGION = RedisCacheField.SCHED + ":" + RedisCacheField.REGION;
    String SCHED_ALL_SITE = RedisCacheField.SCHED + ":" + RedisCacheField.SITE;
    String SCHED_REGION_SITE = RedisCacheField.SCHED + ":" + RedisCacheField.SITE + ":" + RedisCacheField.REGION + ":";
    String SCHED_EDGE_RESOURCES = RedisCacheField.SCHED + ":" + RedisCacheField.EDGE + ":" + RedisCacheField.RESOURCES + ":";


    String ALL_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE;
    String REGION_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE + ":" + RedisCacheField.REGION + ":";
    String SITE_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE + ":" + RedisCacheField.SITE + ":";

    String LABEL_EDGE = RedisCacheField.LABEL + ":" + RedisCacheField.EDGE + ":";
    String LABEL_SITE = RedisCacheField.LABEL + ":" + RedisCacheField.SITE + ":";
    String LABEL_REGION = RedisCacheField.LABEL + ":" + RedisCacheField.REGION + ":";
}
