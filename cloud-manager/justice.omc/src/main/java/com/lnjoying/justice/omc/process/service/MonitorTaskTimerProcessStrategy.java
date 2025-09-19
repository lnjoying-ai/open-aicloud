package com.lnjoying.justice.omc.process.service;

import com.lnjoying.justice.omc.process.processor.MonitorTaskTimerProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/28 15:46
 */

@Service("MonitorTaskTimerProcessStrategy")
@Slf4j
public class MonitorTaskTimerProcessStrategy extends ScheduleProcessStrategy
{
    @Autowired
    private MonitorTaskTimerProcessor monitorTaskTimerProcessor;

    private Integer cycle = 10 * 1000;

    private Integer delay = 60 * 1000;

    public MonitorTaskTimerProcessStrategy()
    {
        super("omc monitor stack",1);
        log.info("init omc monitor stack strategy");
    }

    public MonitorTaskTimerProcessStrategy(String desc, int threadNum)
    {
        super(desc, threadNum);
    }

    @PostConstruct
    public void start()
    {
        log.info("start monitor task timer processor");

        super.start(() -> monitorTaskTimerProcessor, delay, cycle, null);
    }
}
