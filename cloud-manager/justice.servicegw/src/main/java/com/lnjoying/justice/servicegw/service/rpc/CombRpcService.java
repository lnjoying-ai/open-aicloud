package com.lnjoying.justice.servicegw.service.rpc;

import com.lnjoying.justice.schema.service.aos.AosService;
import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.lnjoying.justice.schema.service.ecrm.EdgeResourceService;
import com.lnjoying.justice.schema.service.iam.AuthzService;
import com.lnjoying.justice.schema.service.servicemanager.ServiceManagerService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

@Component("combRpcService")
public class CombRpcService
{
    @RpcReference(microserviceName = "cls", schemaId = "clusterManagerService")
    private ClusterManagerService clusterManagerService;
    public ClusterManagerService getClusterManagerService()
    {
        return clusterManagerService;
    }
    
    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cmpManagerService;
    public CloudManagerService getCmpManagerService()
    {
        return cmpManagerService;
    }
    
    @RpcReference(microserviceName = "apiserver", schemaId = "submit")
    private BusinessMsgSubmitService businessMsgSubmitService;
    public BusinessMsgSubmitService getBusinessMsgSubmitService() {
        return businessMsgSubmitService;
    }


    @RpcReference(microserviceName = "iam", schemaId = "authzService")
    private AuthzService authzService;


    public AuthzService getAuthzService()
    {
        return authzService;
    }

    @RpcReference(microserviceName = "service-manager", schemaId = "serviceManagerService")
    private ServiceManagerService serviceManagerService;
    public ServiceManagerService getServiceManagerService()
    {
        return serviceManagerService;
    }

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
}