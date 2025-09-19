package com.lnjoying.justice.api.service;

import com.lnjoying.justice.api.rpcserviceimpl.CombRpcService;
import com.lnjoying.justice.api.utils.CipherUtil;
import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/8/23 11:03 AM
 */
@Service("nodeService")
public class NodeService
{
    @Autowired
    private CombRpcService combRpcService;
    
    private static Logger LOGGER = LogManager.getLogger();
    
    NodeConfig nodeConfig = null;
    
    public NodeConfig getNodeConfig()
    {
        if (nodeConfig != null)
        {
            return nodeConfig;
        }
        
        LOGGER.info("load node config from sys");
    
        nodeConfig = combRpcService.getSysService().getNodeConfig();
    
        CipherUtil.import_private_key(nodeConfig.getPrivate_key());
        LOGGER.info("node config is {}", nodeConfig);
        
        return nodeConfig;
    }
}
