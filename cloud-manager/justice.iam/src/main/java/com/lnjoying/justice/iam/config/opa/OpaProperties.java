package com.lnjoying.justice.iam.config.opa;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.config.annotation.NacosIgnore;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:57
 */

@Data
@NacosConfigurationProperties(dataId = "com.justice.iam.opa.config", groupId = "iam", prefix = OpaProperties.PREFIX, autoRefreshed = true,  type = ConfigType.YAML)
public class OpaProperties
{

    public static final String PREFIX = "opa.authz";

    private String url = "http://localhost:8181/";

    private String queryPath = "lnjoying/root";

    private String adHocQueryPath = "data.lnjoying.abac.merge_rules[_].actions=actions";

    @NacosIgnore
    private ResourceOptions resource;

    public OpaProperties(){}

    public OpaProperties(ResourceOptions resourceOptions)
    {
        this.resource = resourceOptions;
    }

    @Data
    @NacosConfigurationProperties(dataId = "com.justice.iam.opa.config", groupId = "iam", autoRefreshed = true, prefix = OpaProperties.PREFIX + ".resource", type = ConfigType.YAML)
    public static final class ResourceOptions
    {
        /**
         * Whether to update basic resources, such as resource action , resource str
         */
        @NacosProperty(value = "update")
        private boolean update = true;
    }
}
