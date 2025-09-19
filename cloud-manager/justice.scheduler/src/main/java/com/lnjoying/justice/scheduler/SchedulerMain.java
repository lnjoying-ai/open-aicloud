package com.lnjoying.justice.scheduler;

import com.lnjoying.justice.scheduler.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

public class SchedulerMain
{
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception
    {
        LOGGER.info("start scheduler");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init();
    }
}
