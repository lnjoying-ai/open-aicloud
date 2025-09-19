package com.lnjoying.justice.ecrm.service;


import com.lnjoying.justice.schema.service.aos.AosService;
import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import com.lnjoying.justice.schema.service.cis.CCInstService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
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

    @RpcReference(microserviceName = "cis", schemaId = "containerService")
    private ContainerInstService containerInstService;

    public ContainerInstService getContainerInstService()
    {
        return containerInstService;
    }

    @RpcReference(microserviceName = "aos", schemaId = "aosService")
    private AosService aosService;

    public AosService getAosService()
    {
        return aosService;
    }

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;


    public UmsService getUmsService()
    {
        return umsService;
    }

    @RpcReference(microserviceName = "cis", schemaId = "ccService")
    private CCInstService ccInstService;
    public CCInstService getInstService()
    {
        return ccInstService;
    }

    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cloudManagerService;
    public CloudManagerService getCloudService()
    {
        return cloudManagerService;
    }

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;
    public SysService getSysService()
    {
        return sysService;
    }

    @RpcReference(microserviceName = "ecss", schemaId = "schedulerService")
    private SchedulerService schedulerService;
    public SchedulerService getSchedulerService()
    {
        return schedulerService;
    }
}
