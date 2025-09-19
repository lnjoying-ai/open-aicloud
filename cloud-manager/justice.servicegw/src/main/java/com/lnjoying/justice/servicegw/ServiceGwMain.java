package com.lnjoying.justice.servicegw;

import com.lnjoying.justice.servicegw.config.SecurityModeConfig;
import com.lnjoying.justice.servicegw.service.AuthProxyService;
import com.lnjoying.justice.servicegw.service.ServicePortService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;
public class ServiceGwMain
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Main.
     */
    public static void main(String[] args)
    {
        LOGGER.info("start cluster server");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init();
        ServicePortService servicePortService = BeanUtils.getBean("servicePortService");
        servicePortService.registerServiceGateway();
        AuthProxyService authProxy = BeanUtils.getBean("authProxyService");
        authProxy.start();
    }
}
