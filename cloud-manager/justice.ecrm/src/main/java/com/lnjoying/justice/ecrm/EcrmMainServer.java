package com.lnjoying.justice.ecrm;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.lnjoying.justice.ecrm.common.GoogleCodeTool;
import com.lnjoying.justice.ecrm.config.SecurityModeConfig;
import com.lnjoying.justice.ecrm.process.service.DevIFStateCheckStrategy;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;
import static com.lnjoying.justice.commonweb.util.NacosConfigLoaderUtils.loadAndSyncNacosConfigToSpring;

public class EcrmMainServer
{
    private static Logger LOGGER = LogManager.getLogger();

    /**
     * Main.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException
    {
        LOGGER.info("start ecrm");
        SecurityModeConfig.loadSecurityModeConfig();
        GoogleCodeTool.generateSecretKey();
        loadAndSyncNacosConfigToSpring();
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
    }
}
