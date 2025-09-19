package com.lnjoying.justice.cis.service;

import com.lnjoying.justice.cis.processor.WebSocketTimerProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("container instance timer service")
public class CisTimerService extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger(CisTimerService.class);

    @Autowired
    private WebSocketTimerProcessor webSocketProcessor;

    //60s
    Integer cycle = 60000;

    public CisTimerService()
    {
        super("container instance timer service", 1);
    }

    @PostConstruct
    public void start()
    {
        LOGGER.info("start processor");
        super.start(() -> {return webSocketProcessor;}, 100000, cycle, null);
    }
}
