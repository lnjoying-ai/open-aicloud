package com.lnjoying.justice.cis;

import com.lnjoying.justice.cis.config.SecurityModeConfig;
import com.lnjoying.justice.cis.config.WSConfig;
import com.lnjoying.justice.cis.webserver.WsServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;
import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

public class CisApplication
{
    private static Logger LOGGER = LogManager.getLogger();
    /**
     * Main.
     */
    public static void main(String[] args)
    {
        LOGGER.info("start cis");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);

        WSConfig wsConfig = BeanUtils.getBean("wsConfig");
        WsServer wsServer = new WsServer(wsConfig.getPort());
        wsServer.start();
    }
}
