package com.lnjoying.justice.aos.process.processor;

import com.lnjoying.justice.aos.common.AosMsgType;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.process.service.StackMsgProcessStrategy;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DamonsetStackTimerProcessor extends AbstractRunnableProcessor
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
        LOGGER.info("Damonset StackTimer Processor started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("Damonset StackTimer Processor process stopped");
    }

    @Override
    public void run()
    {
        LOGGER.info("start Damonset Stack Timer Processor");
        loadDaemonset();
    }
    
    void loadDaemonset()
    {
        try
        {
            List<String> stackIdList = stackRepo.getDaemonset(StackState.ASSIGNED);
            if (stackIdList == null || stackIdList.isEmpty())
            {
                return;
            }
        
            for (String stackId : stackIdList)
            {
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(AosMsgType.SCHEDULE_DAEMONSET_STACK);
                messagePack.setMessageObj(stackId);
                stackMsgProcessStrategy.sendMessage(messagePack, ProcessorName.DAEMONSET_MSG_PROCESSOR);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("load daemonset error {}", e.getMessage());
        }
    }
    
    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
