package com.lnjoying.justice.cis.process.processor;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cis.common.constant.CisMsgType;
import com.lnjoying.justice.cis.common.constant.CisRedisField;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerSpecInfo;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.process.service.SpecMsgProcessStrategy;
import com.lnjoying.justice.cis.service.CisManagerService;
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
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class SpecTimerProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    CisRepository cisRepository;

    @Autowired
    SpecMsgProcessStrategy specMsgProcessStrategy;

    @Autowired
    private CisManagerService cisManagerService;

    @Override
    public void start()
    {
        LOGGER.info("spec message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("spec message process stopped");
    }

    @Override
    public void run()
    {
        LOGGER.info("start SpecTimerProcessor");
        try
        {
            loadSpawnedMsg();
            loadAssignedMsg();
            loadCreateNoRspMsg();
            loadCreateFailMsg();
            loadSystemStopFailMsg();
            loadUserStopFailMsg();
            loadOverdueStopFailMsg();
            loadRemoveFailMsg();
            loadWaitAssignMsg();
            loadLostStatusGT10MMsg();
            loadLostStatusGT2HMsg();
            loadLifeMngEventMsg();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    void loadSpawnedMsg()
    {
        try
        {
            List<String> specIdList = cisRepository.getSpecIdlist(InstanceState.SPAWNED.getCode());
            if (specIdList == null || specIdList.isEmpty())
            {
                return;
            }

            for (String specId : specIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.SCHEDULE_INST);
                messagePack.setMessageObj(specId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdlist(InstanceState.ASSIGNED.getCode());

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            // remove the instId that did not send the config to the edge node
            cisManagerService.checkConfig(instIds);

            for (String instId : instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.ASSIGN_INST);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadAssignedMsg error {}", e.getMessage());
        }
    }


    void loadCreateNoRspMsg()
    {
        try
        {
            List<String> instIds = cisRepository.getInstIdlistLtFailNum(InstanceState.FWD.getCode(), "2 min", 50);

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.RESEND_INST);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadCreateNoRspMsg error {}", e.getMessage());
        }
    }

    void loadCreateFailMsg()
    {
        try
        {
            List<String> instIds = cisRepository.getInstIdlistGteFailNum(InstanceState.FWD.getCode(), "2 min", 50);

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.CREATE_NO_RSP);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadCreateFailMsg error {}", e.getMessage());
        }
    }

    void loadSystemStopFailMsg()
    {
        try
        {
            List<String> instIds = cisRepository.getInstIdlistByStatusWithTime(InstanceState.SPAWN_SYSTEM_STOP.getCode(), "5 min");

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.SPAWN_SYSTEM_STOP);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdlistByStatusWithTime(InstanceState.SPAWN_USER_STOP_QUIT.getCode(), "5 min");

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.SPAWN_USER_STOP_QUIT);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdlistByStatusWithTime(InstanceState.SPAWN_OVERDUE_QUIT.getCode(), "5 min");

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.SPAWN_OVERDUE_QUIT);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdlistByStatusWithTime(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), "5 min");

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.SPAWNED_CLOUD_REMOVE);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdlistByStatusWithTime(InstanceState.WAIT_ASSIGN.getCode(), "10 min");

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.RE_SCHEDULE_INST);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdsLostStatusGT("10 min", EventType.DEFAULT.getCode());

            if (instIds != null && !instIds.isEmpty())
            {
                for (String instId: instIds)
                {
                    MessagePack messagePack = new MessagePack();
                    messagePack.setMsgType(CisMsgType.LOST_STATUS_GT_10M_LT_2H);
                    messagePack.setMessageObj(instId);
                    specMsgProcessStrategy.sendMessage(messagePack);
                }
            }

            List<String> newInstIds = cisRepository.getInstIdsLostStatusGTWithUpdateTime("10 min", EventType.LOST_STATUS_GT_10M_LT_2H.getCode());

            if (newInstIds != null && !newInstIds.isEmpty())
            {
                for (String instId: newInstIds)
                {
                    MessagePack messagePack = new MessagePack();
                    messagePack.setMsgType(CisMsgType.LOST_STATUS_GT_10M_LT_2H);
                    messagePack.setMessageObj(instId);
                    specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> instIds = cisRepository.getInstIdsLostStatusGT("120 min", EventType.LOST_STATUS_GT_10M_LT_2H.getCode());

            if (instIds == null || instIds.isEmpty())
            {
                return;
            }

            for (String instId: instIds)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(CisMsgType.LOST_STATUS_GT_2H);
                messagePack.setMessageObj(instId);
                specMsgProcessStrategy.sendMessage(messagePack);
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
            List<String> sessionIds = RedisUtil.zrangebyscore(CisRedisField.CIS_SPAWN_LIFE_EVENT_SET, 0, (double)time);
            if (CollectionUtils.isEmpty(sessionIds))
            {
                return;
            }
            for(String sessionId : sessionIds)
            {
                String pairJson = RedisUtil.get(CisRedisField.CIS_SPAWN_LIFE_EVENT + sessionId);
                if (!StringUtils.isEmpty(pairJson))
                {
                    Pair<String,String> eventPair = JsonUtils.fromJson(pairJson, new TypeToken<Pair<String,String>>(){}.getType());
                    if (eventPair != null)
                    {
                        MessagePack messagePack = new MessagePack();
                        messagePack.setMsgType(CisMsgType.RESEND_LIFE_MNG);
                        messagePack.setMessageObj(eventPair);
                        specMsgProcessStrategy.sendMessage(messagePack);
                    }
                }
                RedisUtil.delete(CisRedisField.CIS_SPAWN_LIFE_EVENT + sessionId);
                RedisUtil.zrem(CisRedisField.CIS_SPAWN_LIFE_EVENT_SET, sessionId);
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
