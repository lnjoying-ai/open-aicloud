package com.lnjoying.justice.servicegw.service.processor;

import com.lnjoying.justice.servicegw.service.TunnelService;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/26/22 1:32 PM
 */
@Component
public class TunnelProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    TunnelService tunnelService;
    
    @Override
    public void start()
    {
        LOGGER.info("tunnel processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("tunnel processor stopped");
    }
    
    @Override
    public void run()
    {
        LOGGER.debug("check tunnel state");
        tunnelService.checkTunnelState();
    }
}
