package com.lnjoying.justice.cluster.manager;

import com.lnjoying.justice.cluster.manager.config.ClusterServerRootCAConfig;
import com.lnjoying.justice.cluster.manager.config.SecurityModeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;
import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

public class ClusterMainServer
{
    private static Logger LOGGER = LogManager.getLogger();
    
    /**
     * Main.
     */
    public static void main(String[] args)
    {
        LOGGER.info("start cluster manager server");
        SecurityModeConfig.loadSecurityModeConfig();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
        
        ClusterServerRootCAConfig clusterServerRootCAConfig  = BeanUtils.getBean("clusterServerRootCAConfig");
        clusterServerRootCAConfig.genServerCert();
    }
}
