package com.lnjoying.justice.commonweb.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/13 19:13
 */

public final class CommonWebScanPath
{

    public static final String DEFAULT_MAPPER_PATH = "com.lnjoying.justice.commonweb.mapper";

    public static final String DEFAULT_SERVICE_PATH = "com.lnjoying.justice.commonweb.service";

    public static final String[] DEFAULT_BEAN_SCAN = new String[] {DEFAULT_MAPPER_PATH
            , DEFAULT_SERVICE_PATH};

    public static final String DEFAULT_COMMON_WEB_BEAN_RESOURCE = "classpath*:META-INF/common-web/*.bean.xml";


    public static final String[] DEFAULT_BEAN_SCAN_PATH = new String[] {DEFAULT_COMMON_WEB_BEAN_RESOURCE};
}
