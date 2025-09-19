package com.lnjoying.justice.servicemanager;

import com.lnjoying.justice.servicemanager.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;
import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;

public class ServiceManagerMainServer
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Main.
     */
    public static void main(String[] args)
    {
        LOGGER.info("start service manager");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
    }
}
