package com.lnjoying.justice.aos;

import com.lnjoying.justice.aos.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;
import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

public class AosMainServer
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Main.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException
    {
        LOGGER.info("start aos");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
    }
}
