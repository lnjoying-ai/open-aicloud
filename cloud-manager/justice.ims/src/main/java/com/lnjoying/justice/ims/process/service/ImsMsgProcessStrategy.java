package com.lnjoying.justice.ims.process.service;

import com.lnjoying.justice.ims.process.processor.ImsMsgProcessor;
import com.micro.core.process.service.ProcessStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * ims msg process strategy
 *
 * @author merak
 **/

@Service("ImsMsgProcessStrategy")
@Slf4j
public class ImsMsgProcessStrategy extends ProcessStrategy
{
    @Autowired
    private ImsMsgProcessor imsMsgProcessor;

    public ImsMsgProcessStrategy()
    {
        super("ims message service", 1, 10000);
        log.info("init ImsMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> imsMsgProcessor, 1);
    }

}
