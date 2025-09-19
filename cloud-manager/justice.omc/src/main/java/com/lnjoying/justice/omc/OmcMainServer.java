package com.lnjoying.justice.omc;

import com.lnjoying.justice.omc.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;
import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

/**
 * start omc
 *
 * @author merak
 **/

public class OmcMainServer
{
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args)
    {
        LOGGER.info("start omc");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
    }


}
