package com.lnjoying.justice.scheduler.scheduler.clean;

import com.lnjoying.justice.scheduler.common.constant.RedisCache;
import com.lnjoying.justice.scheduler.domain.model.SchedulerBean;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheCleaner
{
    private static Logger LOGGER = LogManager.getLogger();

    public void doClean(SchedulerBean schedulerBean)
    {
        LOGGER.info("[step 4]clean cache.");

        RedisUtil.delete(RedisCache.SCHED_TEMP_REGION + schedulerBean.getReq().getWaitAssignId());
        RedisUtil.delete(RedisCache.SCHED_TEMP_SITE + schedulerBean.getReq().getWaitAssignId());
        RedisUtil.delete(RedisCache.SCHED_TEMP_EDGE + schedulerBean.getReq().getWaitAssignId());
        RedisUtil.delete(RedisCache.SCHED_TEMP + schedulerBean.getReq().getWaitAssignId());
    }
}
