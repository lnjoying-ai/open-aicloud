package com.lnjoying.justice.api.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.lnjoying.justice.api.config.sms.AlibabaConfig;
import com.lnjoying.justice.api.config.sms.CopoteConfig;
import com.lnjoying.justice.api.config.sms.ParamValueCalculation;
import com.lnjoying.justice.api.config.sms.UcpaasConfig;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Data
@NacosConfigurationProperties(dataId = "com.justice.api.config.notify", groupId = "api", prefix = "sms", type = ConfigType.YAML, autoRefreshed = true)
public class SmsConfig
{
    private static Logger LOGGER = LogManager.getLogger();

    public SmsConfig()
    {
        LOGGER.info("config sms");
    }

    private String provider;

    private UcpaasConfig ucpaas;
    private CopoteConfig copote;
    private AlibabaConfig alibaba;

    private Map<String, Map<String, String>> templateParamsReplacement;
    private Map<String, Map<String, ParamValueCalculation>> paramValueCalculation;
}
