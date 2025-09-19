package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.schema.service.cis.CCInstService;
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
    @RpcReference(microserviceName = "ecss", schemaId = "schedulerService")
    private SchedulerService schedulerService;
    public SchedulerService getSchedulerService()
    {
        return schedulerService;
    }
    
    @RpcReference(microserviceName = "cis", schemaId = "ccService")
    private CCInstService ccInstService;
    public CCInstService getInstService()
    {
        return ccInstService;
    }

    @RpcReference(microserviceName = "ecrm", schemaId = "edgeResourceService")
    private EdgeResourceService edgeResourceService;
    public EdgeResourceService getEdgeResourceService()
    {
        return edgeResourceService;
    }

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;

    public UmsService getUmsService()
    {
        return umsService;
    }

    @RpcReference(microserviceName = "ecrm", schemaId = "regionResourceService")
    private RegionResourceService regionResourceService;

    public RegionResourceService getRegionResourceService()
    {
        return regionResourceService;
    }

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;
    public SysService getSysService()
    {
        return sysService;
    }
}