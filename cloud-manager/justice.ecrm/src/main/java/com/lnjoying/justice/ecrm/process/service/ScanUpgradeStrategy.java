package com.lnjoying.justice.ecrm.process.service;

import com.lnjoying.justice.ecrm.process.processor.ScanUpgradeProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("scanUpgradeStrategy")
public class ScanUpgradeStrategy extends ScheduleProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ScanUpgradeProcessor scanUpgradeProcessor;

    private  Integer cycle = 5000;
    public ScanUpgradeStrategy()
    {
        super("sacn upgrade strategy", 1);
        LOGGER.info("init sacn upgrade strategy");
    }
    
    @PostConstruct
    public void start()
    {
        LOGGER.info("start sacn upgrade service");
        super.start(() -> scanUpgradeProcessor, 5000, cycle, null);
    }
}
