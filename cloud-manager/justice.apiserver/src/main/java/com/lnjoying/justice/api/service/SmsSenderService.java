package com.lnjoying.justice.api.service;

import com.lnjoying.justice.api.config.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SmsSenderService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CopoteSmsSenderImpl copoteSmsSender;

    @Autowired
    private UcPaasSmsSenderImpl ucPaasSmsSender;

    @Autowired
    private AlibabaSmsSenderImpl alibabaSmsSender;

    @Autowired
    SmsConfig smsConfig;

    public SmsSender getSmsSender()
    {
        if (StringUtils.isEmpty(smsConfig.getProvider()) || smsConfig.getProvider().equalsIgnoreCase("ucpaas"))
        {
            return ucPaasSmsSender;
        }
        else if (smsConfig.getProvider().equalsIgnoreCase("copote"))
        {
            return copoteSmsSender;
        }
        else if (smsConfig.getProvider().equalsIgnoreCase("alibaba"))
        {
            return alibabaSmsSender;
        }
        else
        {
            return ucPaasSmsSender;
        }
    }
}
