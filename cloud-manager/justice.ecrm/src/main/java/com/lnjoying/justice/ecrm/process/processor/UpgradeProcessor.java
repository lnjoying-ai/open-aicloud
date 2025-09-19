package com.lnjoying.justice.ecrm.process.processor;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfo;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.service.EdgeUpgradeService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class UpgradeProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private EdgeUpgradeService edgeUpgradeService;
    
    @Autowired
    private EdgeRepository edgeRepository;
    
    @Override
    public void start()
    {
        LOGGER.info("upgrade processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("upgrade processor stopped");
    }
    
    @Override
    public void run()
    {
        LOGGER.info("process upgrade");
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                process(pack);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("UpgradeProcessor.run error {}", e.getMessage());
            }
        }
    }
    
    void process(MessagePack pack)
    {
        String nodeId = (String) pack.getMessageObj();
        TblEdgeUpgradeInfo tblEdgeUpgradeInfo = edgeRepository.getEdgeUpgradeInfo(nodeId);
        if (tblEdgeUpgradeInfo == null)
        {
            return;
        }
        try
        {
            LOGGER.info("process upgrade plan: {}", tblEdgeUpgradeInfo);
            edgeUpgradeService.upgrade(tblEdgeUpgradeInfo);
        }
        catch (WebSystemException e)
        {
            e.printStackTrace();
            LOGGER.error("UpgradeProcessor.process error {}", e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("UpgradeProcessor.process error {}", e.getMessage());
        }
        finally
        {
            edgeRepository.updateEdgeUpgradeInfo(tblEdgeUpgradeInfo);
        }
    }
}
