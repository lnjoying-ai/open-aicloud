package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import com.lnjoying.justice.schema.service.omc.OmcService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcSerice
{
    @RpcReference(microserviceName = "apiserver", schemaId = "tipMessage")
    TipMessageService tipMessageService;
    public TipMessageService getTipMessageService() {return tipMessageService;}

    @RpcReference(microserviceName = "omc", schemaId = "omcService")
    public OmcService omcService;
    public OmcService getOmcService()
    {
        return omcService;
    }

}
