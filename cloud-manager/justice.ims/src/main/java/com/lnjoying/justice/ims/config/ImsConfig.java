package com.lnjoying.justice.ims.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/27 10:43
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.ims.config", groupId = "ims", autoRefreshed = true)
public class ImsConfig
{

    @NacosProperty(value = "harbor.registry.install.path")
    private String harborInstallPath;
}

