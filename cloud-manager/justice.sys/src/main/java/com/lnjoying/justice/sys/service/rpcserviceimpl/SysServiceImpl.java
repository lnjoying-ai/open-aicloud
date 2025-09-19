package com.lnjoying.justice.sys.service.rpcserviceimpl;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import com.lnjoying.justice.sys.facade.NacosService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Properties;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/29 23:35
 */

@RpcSchema(schemaId = "sysService")
@Slf4j
public class SysServiceImpl implements SysService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    @Qualifier("globalNacosProperties$config")
    private Properties configGlobalNacosPropertiesBeanName;

    @Autowired
    private NacosService nacosService;

    @Autowired
    private NodeConfig nodeConfig;
    
    @Override
    public NacosConfig getNacos()
    {
        String serverAddr = (String) configGlobalNacosPropertiesBeanName.get(LOWER_HYPHEN.to(LOWER_CAMEL, NacosProperties.SERVER_ADDR));
        String userName = (String) configGlobalNacosPropertiesBeanName.get(NacosProperties.USERNAME);
        String password = (String) configGlobalNacosPropertiesBeanName.get(NacosProperties.PASSWORD);
        String timeoutStr = (String) configGlobalNacosPropertiesBeanName.get(NacosProperties.CONFIG_LONG_POLL_TIMEOUT);
        long timeout = 30000L;
        if (StringUtils.isNotBlank(timeoutStr))
        {
            timeout = Long.parseLong(timeoutStr);
        }
        NacosConfig nacosConfig = NacosConfig.builder().serverAddr(serverAddr).userName(userName).password(password).timeout(timeout).build();
        return nacosConfig;
    }

    @Override
    public Boolean exists(@ApiParam(name = "namespace") String namespace, @ApiParam(name = "group") String group, @ApiParam(name = "dataId") String dataId)
    {
       return nacosService.exists(namespace, group, dataId);
    }

    @Override
    public ConfigInfoBase getConfig(@ApiParam(name = "namespace") String namespace, @ApiParam(name = "group") String group, @ApiParam(name = "dataId") String dataId)
    {
        return nacosService.getConfigInfoBase(namespace, group, dataId);
    }
    
    @Override
    public NodeConfig getNodeConfig()
    {
        LOGGER.info("get node config info {}", nodeConfig);
        return nodeConfig;
    }
}
