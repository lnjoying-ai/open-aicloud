package com.lnjoying.justice.sys.service;


import com.lnjoying.justice.schema.service.ums.UmsService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcService
{

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;


    public UmsService getUmsService()
    {
        return umsService;
    }

}
