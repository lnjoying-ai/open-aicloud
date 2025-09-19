package com.lnjoying.justice.commonweb.service.rpcserviceimpl;

import com.lnjoying.justice.schema.service.omc.OmcService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月24日 19:25
 */
@Component("commonWebCombRpcService")
public class CommonWebCombRpcService
{
    @RpcReference(microserviceName = "omc", schemaId = "omcService")
    public OmcService omcService;
    public OmcService getOmcService()
    {
        return omcService;
    }

}
