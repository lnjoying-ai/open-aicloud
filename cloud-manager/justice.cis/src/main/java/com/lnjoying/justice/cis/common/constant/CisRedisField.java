package com.lnjoying.justice.cis.common.constant;

import com.lnjoying.justice.schema.common.RedisCacheField;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/7/22 11:30 AM
 */
public interface CisRedisField
{
    String CLS_LOG_CONTENT            = "cis_cls_log:";
    String CLS_LOG_ID_INSTID = "cis_cls_log_id:";

    //CIS:LIFE:EVENT                           cis life mng event set.
    String CIS_SPAWN_LIFE_EVENT_SET = RedisCacheField.CIS + ":" + RedisCacheField.LIFE + ":" + RedisCacheField.EVENT;
    //CIS:LIFE:EVENT:{session_id}              cis life mng event.
    String CIS_SPAWN_LIFE_EVENT = RedisCacheField.CIS + ":" + RedisCacheField.LIFE + ":" + RedisCacheField.EVENT + ":";

    //CIS:KEEPALIVE:SPEC                       keepalive spec id set
    String CIS_KEEPALIVE_SPECIDS = RedisCacheField.CIS + ":" + RedisCacheField.KEEPALIVE + ":" + RedisCacheField.SPEC;
    //CIS:KEEPALIVE:{spec_id}                  keepalive inst id set
    String CIS_KEEPALIVE_SPEC_INSTIDS = RedisCacheField.CIS + ":" + RedisCacheField.KEEPALIVE + ":";

}
