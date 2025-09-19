package com.lnjoying.justice.iam.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.lnjoying.justice.iam.config.opa.OpaProperties;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/6/9 11:17
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.iam.common.config", groupId = "iam", autoRefreshed = true,  type = ConfigType.YAML)
public class CommonProperties
{
    @NacosProperty(value = "cors-allow-origins")
    private String corsAllowOrigins;

    @NacosProperty(value = "register-mode")
    private String registerMode;

    @NacosProperty(value = "invitation-url")
    private String invitationUrl;

    @NacosProperty(value = "invitation-url-effective-sec")
    private Integer invitationUrlEffectiveSec;

    /**
     * Multiple roles separated by commas, role name is ROLE_{MODULE}_{ ROLENAME}
     */
    @NacosProperty(value = "bp-admin-default-roles")
    private String bpAdminDefaultRoles;

    /**
     * Multiple roles separated by commas, role name is ROLE_{MODULE}_{ ROLENAME}
     */
    @NacosProperty(value = "bp-user-default-roles")
    private String bpUserDefaultRoles;

    @NacosProperty(value = "default-roles-for-all")
    private String defaultRolesForAll;

    @NacosProperty(value = "validate-code-effective-sec")
    private Integer validateCodeEffectiveSec;
}
