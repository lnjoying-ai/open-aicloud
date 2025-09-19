package com.lnjoying.justice.omc.process.service;

import com.lnjoying.justice.omc.process.processor.OmcMsgProcessor;
import com.micro.core.process.service.ProcessStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 19:13
 */

@Slf4j
@Service("omcMsgProcessStrategy")
public class OmcMsgProcessStrategy extends ProcessStrategy
{
    @Autowired
    private OmcMsgProcessor omcMsgProcessor;

    public OmcMsgProcessStrategy()
    {
        super("omc message service",2, 10000);
        log.info("init StackMsgProcessStrategy");
    }


    @PostConstruct
    public void start()
    {
        super.start(() -> omcMsgProcessor,1);
    }
}
