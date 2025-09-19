package com.lnjoying.justice.omc.handler.cron;

import cn.hutool.core.lang.UUID;
import com.lnjoying.justice.omc.facade.LogServiceFacade;
import com.lnjoying.justice.omc.util.DateUtils;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月09日 11:05
 */
@Component
@Lazy(false)
public class CleanupScheduler
{
    private static Logger LOGGER = LogManager.getLogger(CleanupScheduler.class);

    @Autowired
    private LogServiceFacade logServiceFacade;

    /**
     * 每日凌晨3点运行一次（物理删除日期久远的api日志）
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Async
    public void deleteApiLogsOlderThanSixMonths()
    {
        LOGGER.info("api日志定时清理任务开始执行!");

        final String LOCK_KEY = RedisCacheField.OMC + ":" + RedisCacheField.API_LOG_EXPIRE_REMOVE_LOCK;
        final String LOCK_ID = UUID.randomUUID().toString();
        final long EXPIRE_SECONDS = 60;
        final String todayDate = DateUtils.formatLocalDateTime(LocalDateTime.now());
        boolean lockSuccess = false;
        try
        {
            final String LAST_CLEAN_DATE_KEY = RedisCacheField.OMC + ":" + RedisCacheField.API_LOG_LAST_CLEAN_DATE;
            String lastCleanDate = RedisUtil.get(LAST_CLEAN_DATE_KEY);
            if (todayDate.equals(lastCleanDate))
            {
                LOGGER.info("今日已执行过api日志的清理任务，将忽略此次执行!");
                return;
            }

            if (RedisUtil.tryLock(LOCK_KEY, LOCK_ID, EXPIRE_SECONDS))
            {
                lockSuccess = true;
            }

            if (!lockSuccess)
            {
                LOGGER.warn("api日志定时清理任务执行失败(可能的原因:Redis连不上或清理任务正在进行中)!");
                return;
            }
            
            long affectedCount = logServiceFacade.deleteSixMonthAgoApiLog();
            RedisUtil.set(LAST_CLEAN_DATE_KEY, todayDate);
            LOGGER.info("api日志定时清理任务执行成功, 清理记录数:{}", affectedCount);
        } catch (Exception e)
        {
            LOGGER.error("执行api日志定时清理任务失败, stackTrace: {}, errorMessage: {}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    /**
     * 每日凌晨3点运行一次（物理删除日期久远的资源事件日志）
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Async
    public void deleteEventLogOlderThanSixMonths()
    {
        LOGGER.info("资源事件日志定时清理任务开始执行!");

        final String LOCK_KEY = RedisCacheField.OMC + ":" + RedisCacheField.EVENT_LOG_EXPIRE_REMOVE_LOCK;
        final String LOCK_ID = UUID.randomUUID().toString();
        final long EXPIRE_SECONDS = 60;
        final String todayDate = DateUtils.formatLocalDateTime(LocalDateTime.now());
        boolean lockSuccess = false;
        try
        {
            final String LAST_CLEAN_DATE_KEY = RedisCacheField.OMC + ":" + RedisCacheField.EVENT_LOG_LAST_CLEAN_DATE;
            String lastCleanDate = RedisUtil.get(LAST_CLEAN_DATE_KEY);
            if (todayDate.equals(lastCleanDate))
            {
                LOGGER.info("今日已执行过资源事件日志的清理任务，将忽略此次执行!");
                return;
            }
            if (RedisUtil.tryLock(LOCK_KEY, LOCK_ID, EXPIRE_SECONDS))
            {
                lockSuccess = true;
            }

            if (!lockSuccess)
            {
                LOGGER.warn("资源事件日志定时清理任务执行失败(可能的原因:Redis连不上或清理任务正在进行中)!");
                return;
            }
            
            long affectedCount = logServiceFacade.deleteSixMonthAgoEventLog();
            RedisUtil.set(LAST_CLEAN_DATE_KEY, todayDate);
            LOGGER.info("资源事件日志定时清理任务执行成功, 清理记录数:{}", affectedCount);
        } catch (Exception e)
        {
            LOGGER.error("执行资源事件日志定时清理任务失败, stackTrace: {}, errorMessage: {}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }
}
