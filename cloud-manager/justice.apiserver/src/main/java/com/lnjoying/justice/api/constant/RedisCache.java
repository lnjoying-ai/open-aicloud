package com.lnjoying.justice.api.constant;

import com.lnjoying.justice.schema.common.RedisCacheField;

public interface RedisCache
{
    //ECRM:EDGE:IF:{node_id}                    edge if.
    String EDGE_IF = RedisCacheField.ECRM + ":" + RedisCacheField.EDGE + ":" + RedisCacheField.IF + ":";
    //ECRM:GW:IF:{gw_id}                        gw if.
    String GW_IF = RedisCacheField.ECRM + ":" + RedisCacheField.GW + ":" + RedisCacheField.IF + ":";
    //CLS:WORKER:IF:{worker_id}                 worker if.
    String CLS_WORKER_IF = RedisCacheField.CLS + ":" + RedisCacheField.WORKER + ":" + RedisCacheField.IF + ":";
    //CMP:WORKER:IF:{worker_id}                 worker if.
    String CMP_WORKER_IF = RedisCacheField.CMP + ":" + RedisCacheField.WORKER + ":" + RedisCacheField.IF + ":";
    //ECRM:WORKER:IF:{worker_id}                 worker if.
    String ECRM_WORKER_IF = RedisCacheField.ECRM + ":" + RedisCacheField.WORKER + ":" + RedisCacheField.IF + ":";

    //EDGE:ONLINE                              all online edge set.
    String ALL_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE;
    //EDGE:ONLINE:REGION:{region_id}           region's online edge set.
    String REGION_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE + ":" + RedisCacheField.REGION + ":";
    //EDGE:ONLINE:SITE:{site_id}               site's online edge set.
    String SITE_ONLINE_EDGE = RedisCacheField.EDGE + ":" + RedisCacheField.ONLINE + ":" + RedisCacheField.SITE + ":";

    //ECRM:SITE:{region_id}:{site_id}:{gw_id}
    String SITE_GW_EDGE = RedisCacheField.ECRM + ":" + RedisCacheField.SITE + ":";

    //CLS:REGION:{region_id}:{gw_id}
    String REGION_GW_WORKER = RedisCacheField.CLS + ":" + RedisCacheField.REGION + ":";
}
