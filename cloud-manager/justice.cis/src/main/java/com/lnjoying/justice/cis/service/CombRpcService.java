package com.lnjoying.justice.cis.service;

import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.scheduler.SchedulerService;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcService
{
    @RpcReference(microserviceName = "apiserver", schemaId = "submit")
    private BusinessMsgSubmitService businessMsgSubmitService;

    public BusinessMsgSubmitService getBusinessMsgSubmitService() {
        return businessMsgSubmitService;
    }

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;

    public UmsService getUmsService()
    {
        return umsService;
    }
    
    @RpcReference(microserviceName = "apiserver", schemaId = "submit")
    private BusinessMsgSubmitService submitService;
    
    @RpcReference(microserviceName = "ecss", schemaId = "schedulerService")
    private SchedulerService schedulerService;

    @RpcReference(microserviceName = "ecrm", schemaId = "regionResourceService")
    private RegionResourceService regionResourceService;

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;

    public SchedulerService getSchedulerService()
    {
        return schedulerService;
    }

    public RegionResourceService getRegionResourceService()
    {
        return regionResourceService;
    }

    public SysService getSysService()
    {
        return sysService;
    }

    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cloudManagerService;
    public CloudManagerService getCloudManagerService()
    {
        return cloudManagerService;
    }

    @RpcReference(microserviceName = "ecrm", schemaId = "edgeResourceService")
    private EdgeResourceService edgeResourceService;
    public EdgeResourceService getEdgeResourceService()
    {
        return edgeResourceService;
    }
}
