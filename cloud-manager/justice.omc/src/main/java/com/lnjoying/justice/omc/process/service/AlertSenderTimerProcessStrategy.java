package com.lnjoying.justice.omc.process.service;

import com.lnjoying.justice.omc.process.processor.AlertSenderTimerProcessor;
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

@Service("AlertSenderTimerProcessStrategy")
@Slf4j
public class AlertSenderTimerProcessStrategy extends ScheduleProcessStrategy
{
    @Autowired
    private AlertSenderTimerProcessor alertSenderTimerProcessor;

    private Integer cycle = 10 * 1000;

    private Integer delay = 60 * 1000;

    public AlertSenderTimerProcessStrategy()
    {
        super("alert sender ",1);
        log.info("init alert sender strategy");
    }

    public AlertSenderTimerProcessStrategy(String desc, int threadNum)
    {
        super(desc, threadNum);
    }

    @PostConstruct
    public void start()
    {
        log.info("start alert sender timer processor");

        super.start(() -> alertSenderTimerProcessor, delay, cycle, null);
    }
}
