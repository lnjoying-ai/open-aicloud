package com.lnjoying.justice.aos.service;


import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.ims.ImsRegistryService;
import com.lnjoying.justice.schema.service.ims.ImsRepoService;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.lnjoying.justice.schema.service.scheduler.SchedulerService;
import com.lnjoying.justice.schema.service.servicemanager.ServiceManagerService;
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

    @RpcReference(microserviceName = "ecss", schemaId = "schedulerService")
    private SchedulerService schedulerService;

    public SchedulerService getSchedulerService()
    {
        return schedulerService;
    }

    @RpcReference(microserviceName = "cis", schemaId = "containerService")
    private ContainerInstService containerInstService;

    @RpcReference(microserviceName = "ecrm", schemaId = "regionResourceService")
    private RegionResourceService regionResourceService;

    public ContainerInstService getContainerInstService()
    {
        return containerInstService;
    }

    @RpcReference(microserviceName = "ims", schemaId = "imsRegistryService")
    private ImsRegistryService imsRegistryService;

    public ImsRegistryService getImsRegistryService()
    {
        return imsRegistryService;
    }

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;

    @RpcReference(microserviceName = "omc", schemaId = "omcService")
    public OmcService omcService;

    public UmsService getUmsService()
    {
        return umsService;
    }

    public RegionResourceService getRegionResourceService()
    {
        return regionResourceService;
    }

    @RpcReference(microserviceName = "cls", schemaId = "clusterManagerService")
    private ClusterManagerService clusterManagerService;
    public ClusterManagerService getClusterManagerService()
    {
        return clusterManagerService;
    }

    @RpcReference(microserviceName = "ims", schemaId = "ImsRepoService")
    private ImsRepoService ImsRepoService;

    public ImsRepoService getImsRepoService()
    {
        return ImsRepoService;
    }

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;

    public SysService getSysService()
    {
        return sysService;
    }
    
    @RpcReference(microserviceName = "service-manager", schemaId = "serviceManagerService")
    private ServiceManagerService serviceManagerService;
    public ServiceManagerService getServiceManagerService()
    {
        return serviceManagerService;
    }

    public OmcService getOmcService()
    {
        return omcService;
    }

}
