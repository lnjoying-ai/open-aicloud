package com.lnjoying.justice.sys;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import static com.lnjoying.justice.commonweb.common.CommonWebScanPath.DEFAULT_BEAN_SCAN_PATH;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/23 14:40
 */
public class SysMainServer
{
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args)
    {
        LOGGER.info("start sys");
        BeanUtils.init(DEFAULT_BEAN_SCAN_PATH);
    }
}
