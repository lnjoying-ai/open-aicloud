package com.lnjoying.justice.ims.service;


import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcService
{
    @RpcReference(microserviceName = "apiserver", schemaId = "submit")
    private BusinessMsgSubmitService businessMsgSubmitService;

    @RpcReference(microserviceName = "ecrm", schemaId = "regionResourceService")
    private RegionResourceService regionResourceService;

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;

    public BusinessMsgSubmitService getBusinessMsgSubmitService()
    {
        return businessMsgSubmitService;
    }

    public RegionResourceService getRegionResourceService()
    {
        return regionResourceService;
    }

    public UmsService getUmsService()
    {
        return umsService;
    }

    public SysService getSysService()
    {
        return sysService;
    }
}
