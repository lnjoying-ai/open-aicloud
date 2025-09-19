package com.lnjoying.justice.api.rpcserviceimpl;


import com.lnjoying.justice.schema.service.aos.AosService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.clsvr.ClusterServerService;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
import com.lnjoying.justice.schema.service.ims.ImsService;
import com.lnjoying.justice.schema.service.stat.StatService;
import com.lnjoying.justice.schema.service.sys.SysService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcService
{
    @RpcReference(microserviceName = "ecrm", schemaId = "edgeResourceService")
    private EdgeResourceService edgeResourceService;

    public EdgeResourceService getEdgeResourceService()
    {
        return edgeResourceService;
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

    @RpcReference(microserviceName = "stat", schemaId = "statService")
    private StatService statService;
    public StatService getStatService()
    {
        return statService;
    }

    @RpcReference(microserviceName = "ims", schemaId = "imsService")
    private ImsService imsService;
    public ImsService getImsService() {
        return imsService;
    }

    @RpcReference(microserviceName = "cls", schemaId = "clusterManagerService")
    private ClusterManagerService clusterManagerService;
    public ClusterManagerService getClusterManagerService()
    {
        return clusterManagerService;
    }

    @RpcReference(microserviceName = "servicegw", schemaId = "clusterServer")
    private ClusterServerService clusterServerService;
    public ClusterServerService getClusterServerService()
    {
        return clusterServerService;
    }

    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cloudManagerService;
    public CloudManagerService getCloudManagerService()
    {
        return cloudManagerService;
    }

    @RpcReference(microserviceName = "sys", schemaId = "sysService")
    private SysService sysService;
    public SysService getSysService()
    {
        return sysService;
    }
}
