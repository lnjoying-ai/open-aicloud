package com.lnjoying.justice.aos.process.processor;

import com.lnjoying.justice.aos.common.AosMsgType;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.model.TblStackServiceInfo;
import com.lnjoying.justice.aos.facade.StackServiceFacade;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class StackMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackServiceFacade stackFacade;

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
                    case AosMsgType.SCHEDULE_STACK:
                        stackFacade.processScheduleStack((String) pack.getMessageObj());
                        break;
                    case AosMsgType.RE_SCHEDULE_STACK:
                        stackFacade.processReScheduleStack((String) pack.getMessageObj());
                        break;
                    case AosMsgType.SYNC_CFG:
                        stackFacade.processSyncCfg((String) pack.getMessageObj());
                        break;
                    case AosMsgType.ASSIGN_STACK:
                        stackFacade.processAssignStack((String) pack.getMessageObj());
                        break;
                    case AosMsgType.RESEND_STACK:
                        stackFacade.processResendStack((String) pack.getMessageObj());
                        break;
                    case AosMsgType.NO_RSP:
                        stackFacade.processNoRsp((String) pack.getMessageObj());
                        break;
                    case AosMsgType.START_STACK:
                        stackFacade.processStartStack((TblStackInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.STOP_STACK:
                        stackFacade.processStopStack((TblStackInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.RESTART_STACK:
                        stackFacade.processRestartStack((TblStackInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.DELETE_STACK:
                        stackFacade.processRemoveStack((TblStackInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.START_SERVICE:
                        stackFacade.processStartService((TblStackServiceInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.STOP_SERVICE:
                        stackFacade.processStopService((TblStackServiceInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.RESTART_SERVICE:
                        stackFacade.processRestartService((TblStackServiceInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.DELETE_SERVICE:
                        stackFacade.processRemoveService((TblStackServiceInfo) pack.getMessageObj());
                        break;
                    case AosMsgType.LOST_STATUS_GT_10M_LT_2H:
                        stackFacade.processLostStatusGT10MAndLT2H((String) pack.getMessageObj());
                        break;
                    case AosMsgType.LOST_STATUS_GT_2H:
                        stackFacade.processLostStatusGT2H((String) pack.getMessageObj());
                        break;
                    case AosMsgType.RESEND_LIFE_MNG:
                        Pair<String, String> enentPair = (Pair<String, String>) pack.getMessageObj();
                        stackFacade.actionStack(enentPair.getKey(), enentPair.getValue(), null);
                        break;
                    case AosMsgType.STACK_FAILOVER:
                        stackFacade.processStackFailover((Pair<String, String>) pack.getMessageObj());
                        break;
                    case AosMsgType.SCHEDULE_DAEMONSET_STACK:
                        stackFacade.processDaemonsetStack((String) pack.getMessageObj());
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
