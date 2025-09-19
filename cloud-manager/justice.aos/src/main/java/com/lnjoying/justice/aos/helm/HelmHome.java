package com.lnjoying.justice.aos.helm;


import com.lnjoying.justice.aos.helm.util.EnvUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 19:14
 */

final class HelmHome
{
    private static final String HELM_HOME = "HELM_HOME";

    private final Path path;

    HelmHome()
    {
       this(null);
    }

    HelmHome(final String helmPath)
    {
        super();
        if (StringUtils.isBlank(helmPath))
        {
            String config = EnvUtils.getConfig(HELM_HOME);
            // todo 根据os决定默认目录
            this.path = Paths.get(config);
        }
        else
        {
            this.path = Paths.get(helmPath);
        }
    }
}
