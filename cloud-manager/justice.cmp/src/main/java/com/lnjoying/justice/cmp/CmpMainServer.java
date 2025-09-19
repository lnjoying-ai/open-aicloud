package com.lnjoying.justice.cmp;

import com.lnjoying.justice.cmp.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;
import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;

public class CmpMainServer
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Main.
     */
    public static void main(String[] args)
    {
        LOGGER.info("start cmp");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
    }
}
