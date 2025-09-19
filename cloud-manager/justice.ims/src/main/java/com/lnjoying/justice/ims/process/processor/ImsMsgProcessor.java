package com.lnjoying.justice.ims.process.processor;

import com.lnjoying.justice.ims.common.ImsMsgType;
import com.lnjoying.justice.ims.domain.model.ImsPreDownload;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistryFacade;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

import static com.lnjoying.justice.schema.common.ErrorCode.MSG_PROCESSING_ERROR;

/**
 * ims msg processor
 *
 * @author merak
 **/

@Component
@Slf4j
public class ImsMsgProcessor extends AbstractRunnableProcessor
{
    @Autowired
    @Lazy
    private ImsRegistryFacade imsRegistryFacade;

    @Override
    public void start()
    {
        super.start();
        log.info("ims message process started");
    }

    @Override
    public void stop()
    {
        super.stop();
        log.info("ims message process stopped");
    }

    @Override
    public void run()
    {
        super.run();

        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                switch (pack.getMsgType())
                {
                    case ImsMsgType.PRE_DOWNLOAD:
                        imsRegistryFacade.processPreDownload((ImsPreDownload) pack.getMessageObj());
                        break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                log.error("ims msg processing failed:{}", e);
            }
            catch (Exception e)
            {
                log.error("ims msg processing failed:{}", e);
            }
        }
    }
}
