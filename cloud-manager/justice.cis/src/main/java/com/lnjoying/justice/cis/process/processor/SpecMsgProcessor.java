package com.lnjoying.justice.cis.process.processor;

import com.lnjoying.justice.cis.common.constant.CisMsgType;
import com.lnjoying.justice.cis.common.constant.InstAction;
import com.lnjoying.justice.cis.service.CisManagerService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SpecMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    CisManagerService cisManagerService;

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
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                switch(pack.getMsgType())
                {
                    case CisMsgType.SCHEDULE_INST:
                        cisManagerService.processScheduleInst((String) pack.getMessageObj());
                        break;
                    case CisMsgType.RE_SCHEDULE_INST:
                        cisManagerService.processReScheduleInst((String) pack.getMessageObj());
                        break;
                    case CisMsgType.ASSIGN_INST:
                        cisManagerService.processAssignInst((String) pack.getMessageObj());
                        break;
                    case CisMsgType.RESEND_INST:
                        cisManagerService.processResendInst((String) pack.getMessageObj());
                        break;
                    case CisMsgType.CREATE_NO_RSP:
                        cisManagerService.processCreateNoRsp((String) pack.getMessageObj());
                        break;
                    case CisMsgType.SPAWN_USER_STOP_QUIT:
                        cisManagerService.containerLifeMng((String) pack.getMessageObj(), InstAction.STOP.getName());
                        break;
                    case CisMsgType.SPAWNED_CLOUD_REMOVE:
                        cisManagerService.containerLifeMng((String) pack.getMessageObj(), InstAction.REMOVE.getName());
                        break;
                    case CisMsgType.LOST_STATUS_GT_10M_LT_2H:
                        cisManagerService.processLostStatusGT10MAndLT2H((String) pack.getMessageObj());
                        break;
                    case CisMsgType.LOST_STATUS_GT_2H:
                        cisManagerService.processLostStatusGT2H((String) pack.getMessageObj());
                        break;
                    case CisMsgType.RESEND_LIFE_MNG:
                        Pair<String, String> enentPair = (Pair<String, String>) pack.getMessageObj();
                        cisManagerService.containerLifeMng(enentPair.getKey(), enentPair.getValue());
                        break;
                    case CisMsgType.INST_FAILOVER:
                        cisManagerService.processInstFailover((Pair<String, String>)pack.getMessageObj());
                        break;
                    case CisMsgType.CONFIG_FILE_CREATE:
                        cisManagerService.processConfigFileCreateReq(pack.getMessageObj());
                        break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
