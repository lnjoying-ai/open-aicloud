package com.lnjoying.justice.cis.process.service;

import com.lnjoying.justice.cis.process.processor.KeepAliveProcessor;
import com.lnjoying.justice.cis.process.processor.ScanKeepAliveProcessor;
import com.lnjoying.justice.cis.process.processor.SpecTimerProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("keepAliveStrategy")
public class KeepAliveStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    KeepAliveProcessor keepAliveProcessor;

    @Autowired
    ScanKeepAliveProcessor scanKeepAliveProcessor;

    //60s
    Integer cycle = 10000;

    public KeepAliveStrategy()
    {
        super("keep alive timer service",2);
        LOGGER.info("init KeepAliveStrategy");
    }

    @PostConstruct
    public void start()
    {
        LOGGER.info("start processor");

        {
            super.start(() -> {return keepAliveProcessor;}, 10000, cycle, null);
            super.start(() -> {return scanKeepAliveProcessor;}, 10000, cycle, null);
        }
    }
}
