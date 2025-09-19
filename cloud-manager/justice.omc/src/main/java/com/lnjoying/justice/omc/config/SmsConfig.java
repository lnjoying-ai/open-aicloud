package com.lnjoying.justice.omc.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.lnjoying.justice.omc.service.notify.sender.sms.SmsSender;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;

@Data
@NacosConfigurationProperties(dataId = "com.justice.api.config.notify", groupId = "api", prefix = "sms",type = ConfigType.YAML, autoRefreshed = true)
public class SmsConfig
{
    private static Logger LOGGER = LogManager.getLogger();

    public SmsConfig()
    {
        LOGGER.info("config sms");
    }
    private String url;

    private String token;

    private String sid;

    private String appid;

    private String templateValideCode;
}
