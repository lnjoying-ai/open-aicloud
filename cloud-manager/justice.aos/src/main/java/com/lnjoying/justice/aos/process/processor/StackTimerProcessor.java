package com.lnjoying.justice.aos.process.processor;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.aos.common.AosMsgType;
import com.lnjoying.justice.aos.common.RedisCache;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.process.service.StackMsgProcessStrategy;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.EventType;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StackTimerProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackRepository stackRepo;

    @Autowired
    CombRpcService combRpcService;

    @Autowired
    StackMsgProcessStrategy stackMsgProcessStrategy;

    @Override
    public void start()
    {
        LOGGER.info("stack message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("stack message process stopped");
    }

    @Override
    public void run()
    {
        LOGGER.info("start StackTimerProcessor");
        loadSpawnedMsg();
        loadAssignedMsg();
        loadCfgSyncedMsg();
        loadNoRspMsg();
        loadFailMsg();
        loadSystemStopFailMsg();
        loadUserStopFailMsg();
        loadOverdueStopFailMsg();
        loadRemoveFailMsg();
        loadWaitAssignMsg();
        loadLostStatusGT10MMsg();
        loadLostStatusGT2HMsg();
        loadLifeMngEventMsg();
    }
    
    
    void loadSpawnedMsg()
    {
        try
        {
            List<String> stackIdList = stackRepo.getSpecIdlist(StackState.SPAWNED);
            if (stackIdList == null || stackIdList.isEmpty())
            {
                return;
            }

            for (String stackId : stackIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.SCHEDULE_STACK);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadSpawnedMsg error {}", e.getMessage());
        }
    }

    void loadAssignedMsg()
    {
        try
        {
            List<String> stackIdList = stackRepo.getStackIdlist(StackState.ASSIGNED);

            if (stackIdList == null || stackIdList.isEmpty())
            {
                return;
            }

            for (String stackId: stackIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.SYNC_CFG);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadAssignedMsg error {}", e.getMessage());
        }
    }

    void loadCfgSyncedMsg()
    {
        try
        {
            List<String> stackIdList = stackRepo.getStackIdlist(StackState.CFG_SYNCED);

            if (stackIdList == null || stackIdList.isEmpty())
            {
                return;
            }

            for (String stackId: stackIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.ASSIGN_STACK);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadCfgSyncedMsg error {}", e.getMessage());
        }
    }

    void loadNoRspMsg()
    {
        try
        {
            List<String> stackIds = stackRepo.getInstIdlistLtFailNum(StackState.FWD, "2 min", 50);

            if (stackIds == null || stackIds.isEmpty())
            {
                return;
            }

            for (String stackId: stackIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.RESEND_STACK);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadNoRspMsg error {}", e.getMessage());
        }
    }

    void loadFailMsg()
    {
        try
        {
            List<String> stackIds = stackRepo.getStackIdlistGteFailNum(StackState.FWD, "2 min", 50);

            if (stackIds == null || stackIds.isEmpty())
            {
                return;
            }

            for (String stackId: stackIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.NO_RSP);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadFailMsg error {}", e.getMessage());
        }
    }

    void loadSystemStopFailMsg()
    {
        try
        {
            List<String> stackIds = stackRepo.getStackIdlist(StackState.SPAWN_SYSTEM_STOP, "5 min");

            if (stackIds == null || stackIds.isEmpty())
            {
                return;
            }

            for (String stackId: stackIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.SPAWN_SYSTEM_STOP);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadSystemStopFailMsg error {}", e.getMessage());
        }
    }

    void loadUserStopFailMsg()
    {
        try
        {
            List<String> stackIds = stackRepo.getStackIdlist(StackState.SPAWN_USER_STOP_QUIT, "5 min");

            if (stackIds == null || stackIds.isEmpty())
            {
                return;
            }

            for (String stackId: stackIds)
            {
                TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.STOP_STACK);
                messagePack.setMessageObj(tblStackInfo);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadUserStopFailMsg error {}", e.getMessage());
        }
    }

    void loadOverdueStopFailMsg()
    {
        try
        {
            List<String> stackIds = stackRepo.getStackIdlist(StackState.SPAWN_OVERDUE_QUIT, "5 min");

            if (stackIds == null || stackIds.isEmpty())
            {
                return;
            }

            for (String stackId: stackIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.SPAWN_OVERDUE_QUIT);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadOverdueStopFailMsg error {}", e.getMessage());
        }
    }

    void loadRemoveFailMsg()
    {
        try
        {
            List<String> stackIds = stackRepo.getStackIdlist(StackState.SPAWNED_CLOUD_REMOVE, "5 min");

            if (stackIds == null || stackIds.isEmpty())
            {
                return;
            }

            for (String stackId: stackIds)
            {
                TblStackInfo tblStackInfo = stackRepo.getStack(stackId);
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.DELETE_STACK);
                messagePack.setMessageObj(tblStackInfo);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadRemoveFailMsg error {}", e.getMessage());
        }
    }

    void loadWaitAssignMsg()
    {
        try
        {
            List<String> stackIdList = stackRepo.getStackIdlist(StackState.WAIT_ASSIGN, "10 min");
            if (stackIdList == null || stackIdList.isEmpty())
            {
                return;
            }

            for (String stackId : stackIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.RE_SCHEDULE_STACK);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadWaitAssignMsg error {}", e.getMessage());
        }
    }

    private void loadLostStatusGT10MMsg()
    {
        try
        {
            List<String> stackIdList = stackRepo.getStackIdsLostStatusGT("10 min", EventType.DEFAULT.getCode());

            if (!CollectionUtils.isEmpty(stackIdList))
            {
                for (String stackId : stackIdList)
                {
                    MessagePack messagePack = new MessagePack();
                    messagePack.setMsgType(AosMsgType.LOST_STATUS_GT_10M_LT_2H);
                    messagePack.setMessageObj(stackId);
                    stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
                }
            }

            List<String> newStackIdList = stackRepo.getStackIdsLostStatusGTWithUpdateTime("10 min", EventType.LOST_STATUS_GT_10M_LT_2H.getCode());

            if (!CollectionUtils.isEmpty(newStackIdList))
            {
                for (String stackId : newStackIdList)
                {
                    MessagePack messagePack = new MessagePack();
                    messagePack.setMsgType(AosMsgType.LOST_STATUS_GT_10M_LT_2H);
                    messagePack.setMessageObj(stackId);
                    stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadLostStatusGT10MMsg error {}", e.getMessage());
        }
    }

    private void loadLostStatusGT2HMsg()
    {
        try
        {
            List<String> stackIdList = stackRepo.getStackIdsLostStatusGT("120 min", EventType.LOST_STATUS_GT_10M_LT_2H.getCode());

            if (stackIdList == null || stackIdList.isEmpty())
            {
                return;
            }

            for (String stackId : stackIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.LOST_STATUS_GT_2H);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadLostStatusGT2HMsg error {}", e.getMessage());
        }
    }

    void loadLifeMngEventMsg()
    {
        try
        {
            long time = new Date().getTime();
            List<String> sessionIds = RedisUtil.zrangebyscore(RedisCache.AOS_SPAWN_LIFE_EVENT_SET, 0, (double) time);
            if (CollectionUtils.isEmpty(sessionIds))
            {
                return;
            }
            for (String sessionId : sessionIds)
            {
                String pairJson = RedisUtil.get(RedisCache.AOS_SPAWN_LIFE_EVENT + sessionId);
                if (!StringUtils.isEmpty(pairJson))
                {
                    Pair<String, String> eventPair = JsonUtils.fromJson(pairJson, new TypeToken<Pair<String, String>>()
                    {
                    }.getType());
                    if (eventPair == null)
                    {
                        MessagePack messagePack = new MessagePack();
                        messagePack.setMsgType(AosMsgType.RESEND_LIFE_MNG);
                        messagePack.setMessageObj(eventPair);
                        stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOYMENT_MSG_PROCESSOR);
                    }
                }
                RedisUtil.delete(RedisCache.AOS_SPAWN_LIFE_EVENT + sessionId);
                RedisUtil.zrem(RedisCache.AOS_SPAWN_LIFE_EVENT_SET, sessionId);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadLifeMngEventMsg error {}", e.getMessage());
        }
    }
    
    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
