package com.lnjoying.justice.cmp.service.process.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.service.process.CloudProcessStrategy;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @description scan cloud data in redis & db to processor
 */
@Component
public class ScanProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").create();

    public ScanProcessor()
    {
        LOGGER.info("init scan cloud processor");
    }

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudProcessStrategy cloudProcessStrategy;

    @Override
    public void start()
    {
        LOGGER.info("start scan cloud job");
    }

    @Override
    public void stop()
    {
        LOGGER.info("stop can cloud check");
    }

    @Override
    public void run()
    {
        loadCloudHealthCheck();
        loadCloudRemoving();
        loadHotResources();
    }

    private void loadCloudHealthCheck()
    {
        if (cloudProcessStrategy.getTaskQueueLength(ProcessorName.HEALTH_CHECK) > 0)
        {
            LOGGER.debug("msg queue is not empty for processor plan");
            return;
        }

        long time = new Date().getTime();
        LOGGER.debug("load cloud health check");
        try
        {
            Long count = RedisUtil.zcard(RedisCache.CLOUD_HEALTH_IDS);
            if (count == null || count == 0)
            {
                loadHealthCheckFromDB();
                return;
            }

            List<String> cloudIds = RedisUtil.zrangebyscore(RedisCache.CLOUD_HEALTH_IDS, 0, (double)(time));

            for (String cloudId : cloudIds)
            {
                sendMessageToHealthCheckProcessor(cloudId, null);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadClusterToBuildPlan error:{}", e.getMessage());
        }

    }

    private void sendMessageToHealthCheckProcessor(Object o, String msgType)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(o);
        messagePack.setMsgType(msgType);
        cloudProcessStrategy.sendMessage(messagePack, ProcessorName.HEALTH_CHECK);
    }

    private  void loadHealthCheckFromDB()
    {
        TblCloudInfoExample example = new TblCloudInfoExample();
        TblCloudInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusBetween(CloudStatus.INTERNAL_TEST.getCode(), CloudStatus.OFF_SHELF.getCode());
        List<TblCloudInfo> tblCloudInfos = cloudRepository.getCloudsByExample(example);

        for (TblCloudInfo tblCloudInfo : tblCloudInfos)
        {
            RedisUtil.zadd(RedisCache.CLOUD_HEALTH_IDS, tblCloudInfo.getCloudId(), new Date().getTime());
        }
    }

    private void loadCloudRemoving()
    {
        try
        {
            TblCloudInfoExample example = new TblCloudInfoExample();
            TblCloudInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo(CloudStatus.REMOVING.getCode());
            List<TblCloudInfo> tblCloudInfos = cloudRepository.getCloudsByExample(example);

            if (CollectionUtils.isEmpty(tblCloudInfos))
            {
                return;
            }

            for (TblCloudInfo tblCloudInfo : tblCloudInfos)
            {
                TblCloudAgentInfoExample agentInfoExample = new TblCloudAgentInfoExample();
                TblCloudAgentInfoExample.Criteria agentInfoExampleCriteria = agentInfoExample.createCriteria();
                agentInfoExampleCriteria.andStatusNotIn(InstanceState.removeStatusList);
                agentInfoExampleCriteria.andCloudIdEqualTo(tblCloudInfo.getCloudId());
                long agentNum = cloudRepository.countAgentsByExample(agentInfoExample);
                if (agentNum == 0)
                {
                    tblCloudInfo.setStatus(CloudStatus.REMOVED.getCode());
                    cloudRepository.updateCloud(tblCloudInfo);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadCloudRemoving error:{}", e.getMessage());
        }
    }

    private void loadHotResources()
    {
        long time = new Date().getTime();
        LOGGER.debug("load hot resources");
        cloudProcessStrategy.syncHotResource(time);
    }
}
