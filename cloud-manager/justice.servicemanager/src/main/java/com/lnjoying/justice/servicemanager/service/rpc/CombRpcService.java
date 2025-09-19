package com.lnjoying.justice.servicemanager.service.rpc;

import com.lnjoying.justice.schema.service.aos.AosService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.lnjoying.justice.schema.service.servicemanager.ServicePortCallback;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcService
{
    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;
    public SysService getSysService()
    {
        return sysService;
    }

    @RpcReference(microserviceName = "omc", schemaId = "omcService")
    private OmcService omcService;
    public OmcService getOmcService()
    {
        return omcService;
    }

    @RpcReference(microserviceName = "ecrm", schemaId = "regionResourceService")
    private RegionResourceService regionResourceService;
    public RegionResourceService getRegionResourceService()
    {
        return regionResourceService;
    }

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;
    public UmsService getUmsService()
    {
        return umsService;
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

    @RpcReference(microserviceName = "cls", schemaId = "clusterManagerService")
    private ClusterManagerService clusterManagerService;
    public ClusterManagerService getClusterManagerService()
    {
        return clusterManagerService;
    }

    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cloudManagerService;
    public CloudManagerService getCloudManagerService()
    {
        return cloudManagerService;
    }

    @RpcReference(microserviceName = "aos", schemaId = "aosStackService")
    private ServicePortCallback aosStackService;
    public ServicePortCallback getAosStackService()
    {
        return aosStackService;
    }

    public ServicePortCallback getServicePortCallback(String microServiceName)
    {
        if (microServiceName.equals("aos"))
        {
            return getAosStackService();
        }

        return null;
    }
}