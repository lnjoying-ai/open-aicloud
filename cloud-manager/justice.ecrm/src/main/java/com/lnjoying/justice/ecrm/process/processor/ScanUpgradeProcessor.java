package com.lnjoying.justice.ecrm.process.processor;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.constant.EcrmMsgType;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.common.constant.UpgradeStatus;
import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfoExample;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.domain.dto.request.ConfigEdgeReq;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import com.lnjoying.justice.ecrm.process.service.UpgradeProcessStrategy;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ScanUpgradeProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    public ScanUpgradeProcessor()
    {
        LOGGER.info("init edge upgrade scan processor");
    }

    @Autowired
    private EdgeRepository edgeRepository;
    
    @Autowired
    private UpgradeProcessStrategy upgradeProcessStrategy;

    @Autowired
    EcrmMsgProcessStrategy ecrmMsgProcessStrategy;
    
    @Override
    public void start()
    {
        LOGGER.info("start scan upgrade job");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("stop scan upgrade process");
    }
    
    @Override
    public void run()
    {
        loadUpgradeInfo();
        loadTimeOutUpgradeInfo();
        loadHostConfigMsg();
    }

    void loadUpgradeInfo()
    {
        LOGGER.info("load upgrade plan");
        try
        {
            if (upgradeProcessStrategy.getTaskQueueLength("upgrade") > 0)
            {
                LOGGER.info("msg queue is not empty for processor upgrade");
                return;
            }
            
            Set<String> upgradeNodeIds = RedisUtil.smembers(RedisCache.UPGRADE_NODEID_SET, "");
            if (CollectionUtils.isEmpty(upgradeNodeIds))
            {
                loadUpgradeInfoFromDB();
                return;
            }
    
            for (String nodeId : upgradeNodeIds)
            {
                TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(nodeId);
                if (tblEdgeUpgradeInfo == null)
                {
                    continue;
                }
                LOGGER.info("upgrade plan: {}", tblEdgeUpgradeInfo);
                if (tblEdgeUpgradeInfo.getUpgradeStatus().equals(UpgradeStatus.PLANED))
                {
                    tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADING);
                    tblEdgeUpgradeInfo.setUpdateTime(new Date());
                    edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
                }
                else if (tblEdgeUpgradeInfo.getUpgradeStatus() >= UpgradeStatus.UPGRADE_FAILED)
                {
                    RedisUtil.srem(RedisCache.UPGRADE_NODEID_SET, "", tblEdgeUpgradeInfo.getNodeId());
                    continue;
                }

                sendMessageToDeployProcessor(nodeId, "upgrade");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadUpgradeInfo error {}", e.getMessage());
        }
    }

    void sendMessageToDeployProcessor(String nodeId, String type)
    {
        LOGGER.info("send node: {} to process", nodeId);
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(nodeId);
        messagePack.setMsgType(type);
        upgradeProcessStrategy.sendMessage(messagePack, "upgrade");
    }
    
    void loadUpgradeInfoFromDB()
    {
        LOGGER.info("load upgrade plan from db");
        TblEdgeUpgradeInfoExample example = new TblEdgeUpgradeInfoExample();
        TblEdgeUpgradeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUpgradeStatusEqualTo(UpgradeStatus.PLANED);
        List<TblEdgeUpgradeInfo> tblEdgeUpgradeInfos = edgeRepository.getEdgeUpgradeInfos(example);

        for (TblEdgeUpgradeInfo tblEdgeUpgradeInfo : tblEdgeUpgradeInfos)
        {
            LOGGER.info("upgrade plan: {}", tblEdgeUpgradeInfo);
            if (tblEdgeUpgradeInfo.getUpgradeStatus().equals(UpgradeStatus.PLANED))
            {
                tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADING);
                tblEdgeUpgradeInfo.setUpdateTime(new Date());
                edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
                RedisUtil.sadd(RedisCache.UPGRADE_NODEID_SET, "", tblEdgeUpgradeInfo.getNodeId());
            }
            else if (tblEdgeUpgradeInfo.getUpgradeStatus() >= UpgradeStatus.UPGRADE_FAILED)
            {
                continue;
            }

            sendMessageToDeployProcessor(tblEdgeUpgradeInfo.getNodeId(), "upgrade");
        }
    }

    void loadTimeOutUpgradeInfo()
    {
        try
        {
            LOGGER.info("load failed upgrade plan from db");
            TblEdgeUpgradeInfoExample example = new TblEdgeUpgradeInfoExample();
            TblEdgeUpgradeInfoExample.Criteria criteria = example.createCriteria();
            criteria.andUpgradeStatusIn(Arrays.asList(UpgradeStatus.PLANED, UpgradeStatus.UPGRADING));

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, -5);

            criteria.andCreateTimeLessThan(calendar.getTime());
            List<TblEdgeUpgradeInfo> tblEdgeUpgradeInfos = edgeRepository.getEdgeUpgradeInfos(example);

            for (TblEdgeUpgradeInfo tblEdgeUpgradeInfo : tblEdgeUpgradeInfos)
            {
                LOGGER.info("failed upgrade plan: {}", tblEdgeUpgradeInfo);
                if (tblEdgeUpgradeInfo.getUpgradeStatus().equals(UpgradeStatus.PLANED) ||
                        tblEdgeUpgradeInfo.getUpgradeStatus().equals(UpgradeStatus.UPGRADING))
                {
                    tblEdgeUpgradeInfo.setUpgradeStatus(UpgradeStatus.UPGRADE_FAILED);
                    tblEdgeUpgradeInfo.setUpdateTime(new Date());
                    edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
                    RedisUtil.srem(RedisCache.UPGRADE_NODEID_SET, "", tblEdgeUpgradeInfo.getNodeId());
                } else
                {
                    continue;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadTimeOutUpgradeInfo error {}", e.getMessage());
        }
    }

    void loadHostConfigMsg()
    {
        try
        {
            long time = new Date().getTime();
            List<String> sessionIds = RedisUtil.zrangebyscore(RedisCache.HOST_CONFIG_SET, 0, (double) time);
            if (CollectionUtils.isEmpty(sessionIds))
            {
                return;
            }
            for (String sessionId : sessionIds)
            {
                String pairJson = RedisUtil.get(RedisCache.HOST_CONFIG_SESSION + sessionId);
                if (!StringUtils.isEmpty(pairJson))
                {
                    Pair<String, ConfigEdgeReq> configEdgeReqPair = JsonUtils.fromJson(pairJson, new com.google.gson.reflect.TypeToken<Pair<String, ConfigEdgeReq>>()
                    {
                    }.getType());
                    if (configEdgeReqPair != null)
                    {
                        MessagePack messagePack = new MessagePack();
                        messagePack.setMsgType(EcrmMsgType.CONFIG_NODE);
                        messagePack.setMessageObj(configEdgeReqPair);
                        ecrmMsgProcessStrategy.sendMessage(messagePack);
                    }
                }
                RedisUtil.delete(RedisCache.HOST_CONFIG_SESSION + sessionId);
                RedisUtil.zrem(RedisCache.HOST_CONFIG_SET, sessionId);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadHostConfigMsg error {}", e.getMessage());
        }
    }
}
