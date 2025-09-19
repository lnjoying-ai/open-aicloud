package com.lnjoying.justice.cis.process.processor;

import com.lnjoying.justice.cis.common.constant.CisMsgType;
import com.lnjoying.justice.cis.common.constant.CisRedisField;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.db.model.TblContainerSpecInfo;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.process.service.SpecMsgProcessStrategy;
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
    CisRepository cisRepository;

    @Autowired
    SpecMsgProcessStrategy specMsgProcessStrategy;

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
            List<String> specIds = RedisUtil.zrangebyscore(CisRedisField.CIS_KEEPALIVE_SPECIDS, 0, (double) time);
            if (CollectionUtils.isEmpty(specIds))
            {
                return;
            }
            for (String specId : specIds)
            {
                TblContainerSpecInfo tblContainerSpecInfo = cisRepository.getSpec(specId);
                if (tblContainerSpecInfo == null || tblContainerSpecInfo.getStatus() != InstanceState.ASSIGNED.getCode())
                {
                    RedisUtil.zrem(CisRedisField.CIS_KEEPALIVE_SPECIDS, specId);
                    continue;
                }

                FailoverPolicy failoverPolicy = null;
                try
                {
                    failoverPolicy = JsonUtils.fromJson(tblContainerSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
                } catch (Exception e)
                {
                    continue;
                }

                if (!failoverPolicy.getNeedFailover())
                {
                    RedisUtil.zrem(CisRedisField.CIS_KEEPALIVE_SPECIDS, specId);
                    continue;
                }

                List<String> instIds = RedisUtil.zrangebyscore(CisRedisField.CIS_KEEPALIVE_SPEC_INSTIDS + specId, 0, (double) (time - failoverPolicy.getDelays()));

                if (!CollectionUtils.isEmpty(instIds))
                {
                    for (String instId : instIds)
                    {
                        createNewInst(instId, failoverPolicy.getFailoverRange());
                        RedisUtil.zrem(CisRedisField.CIS_KEEPALIVE_SPEC_INSTIDS + specId, instId);
                    }
                }
                RedisUtil.zincrby(CisRedisField.CIS_KEEPALIVE_SPECIDS, (double) interval, specId);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("checkInstAlive error {}", e.getMessage());
        }
    }

    private void createNewInst(String instId, String failoverRange)
    {
        Pair<String, String> failoverPair = new Pair<>(instId, failoverRange);
        MessagePack messagePack = new MessagePack();
        messagePack.setMsgType(CisMsgType.INST_FAILOVER);
        messagePack.setMessageObj(failoverPair);
        specMsgProcessStrategy.sendMessage(messagePack);
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
