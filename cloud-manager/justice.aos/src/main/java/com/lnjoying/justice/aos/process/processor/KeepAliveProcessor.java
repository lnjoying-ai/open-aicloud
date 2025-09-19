package com.lnjoying.justice.aos.process.processor;

import com.lnjoying.justice.aos.common.AosMsgType;
import com.lnjoying.justice.aos.common.RedisCache;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.process.service.StackMsgProcessStrategy;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.dev.FailoverPolicy;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class KeepAliveProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackRepository stackRepository;

    @Autowired
    StackMsgProcessStrategy stackMsgProcessStrategy;

    @Override
    public void start()
    {
        LOGGER.info("keep alive process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("keep alive process stopped");
    }

    @Override
    public void run()
    {
        LOGGER.info("start KeepAliveProcessor");
        checkInstAlive();
    }

    long interval = 1000;


    void checkInstAlive()
    {
        try
        {
            long time = new Date().getTime();
            List<String> specIds = RedisUtil.zrangebyscore(RedisCache.AOS_KEEPALIVE_SPECIDS, 0, (double) time);
            if (CollectionUtils.isEmpty(specIds))
            {
                return;
            }
            for (String specId : specIds)
            {
                TblStackSpecInfo tblStackSpecInfo = stackRepository.selectStackSpecInfoByPrimaryKey(specId);
                if (tblStackSpecInfo == null || tblStackSpecInfo.getStatus() != StackState.ASSIGNED)
                {
                    RedisUtil.zrem(RedisCache.AOS_KEEPALIVE_SPECIDS, specId);
                    continue;
                }

                FailoverPolicy failoverPolicy = null;
                try
                {
                    failoverPolicy = JsonUtils.fromJson(tblStackSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
                } catch (Exception e)
                {
                    continue;
                }

                if (!failoverPolicy.getNeedFailover())
                {
                    RedisUtil.zrem(RedisCache.AOS_KEEPALIVE_SPECIDS, specId);
                    continue;
                }

                List<String> stackIds = RedisUtil.zrangebyscore(RedisCache.AOS_KEEPALIVE_SPEC_STACKIDS + specId, 0, (double) (time - failoverPolicy.getDelays()));

                if (!CollectionUtils.isEmpty(stackIds))
                {
                    for (String stackId : stackIds)
                    {
                        createNewStack(stackId, failoverPolicy.getFailoverRange());
                        RedisUtil.zrem(RedisCache.AOS_KEEPALIVE_SPEC_STACKIDS + specId, stackId);
                    }
                }
                RedisUtil.zincrby(RedisCache.AOS_KEEPALIVE_SPECIDS, (double) interval, specId);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("checkInstAlive error {}", e.getMessage());
        }
    }

    private void createNewStack(String stackId, String failoverRange)
    {
        Pair<String, String> failoverPair = new Pair<>(stackId, failoverRange);
        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType(AosMsgType.STACK_FAILOVER);
        messagePack.setMessageObj(failoverPair);
        stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
