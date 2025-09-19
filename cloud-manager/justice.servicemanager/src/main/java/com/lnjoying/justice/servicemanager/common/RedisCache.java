package com.lnjoying.justice.servicemanager.common;

import com.lnjoying.justice.schema.common.RedisCacheField;

public interface RedisCache
{
    String SERVICEGW_MAP = RedisCacheField.SERVICEMANAGER + ":" + RedisCacheField.SERVICEGW;
}
