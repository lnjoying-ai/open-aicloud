package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.schema.service.aos.AosService;
import com.lnjoying.justice.schema.service.aos.AosStackService;
import com.lnjoying.justice.schema.service.aos.AosTemplateService;
import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import com.lnjoying.justice.schema.service.cis.CCInstService;
import com.lnjoying.justice.schema.service.cis.ContainerInstService;
import com.lnjoying.justice.schema.service.cluster.ClusterManagerService;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.iam.CommonResourceFeederService;
import com.lnjoying.justice.schema.service.servicemanager.ServiceManagerService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 20:33
 */

@Component("combRpcService")
public class CombRpcService
{
    @RpcReference(microserviceName = "aos", schemaId = "aosTemplateService")
    private AosTemplateService aosTemplateService;

    @RpcReference(microserviceName = "iam", schemaId = "commonResourceFeederService")
    private CommonResourceFeederService commonResourceFeederService;

    @RpcReference(microserviceName = "iam", schemaId = "umsService")
    private UmsService umsService;

    @RpcReference(microserviceName = "aos", schemaId = "aosStackService")
    private AosStackService aosStackService;

    @RpcReference(microserviceName = "apiserver", schemaId = "tipMessage")
    TipMessageService tipMessageService;

    @RpcReference(microserviceName = "ecrm", schemaId = "regionResourceService")
    private RegionResourceService regionResourceService;

    @RpcReference(microserviceName = "service-manager", schemaId = "serviceManagerService")
    private ServiceManagerService serviceManagerService;

    @RpcReference(microserviceName = "cmp", schemaId = "cloudManagerService")
    private CloudManagerService cloudManagerService;

    @RpcReference(microserviceName = "cls", schemaId = "clusterManagerService")
    private ClusterManagerService clusterManagerService;

    @RpcReference(microserviceName = "cis", schemaId = "containerService")
    private ContainerInstService containerInstService;

    @RpcReference(microserviceName = "cis", schemaId = "ccService")
    private CCInstService ccInstService;

    @RpcReference(microserviceName = "apiserver", schemaId = "submit")
    private BusinessMsgSubmitService businessMsgSubmitService;

    public BusinessMsgSubmitService getBusinessMsgSubmitService() {
        return businessMsgSubmitService;
    }

    public RegionResourceService getRegionResourceService()
    {
        return regionResourceService;
    }

    public TipMessageService getTipMessageService() {return tipMessageService;}

    public CommonResourceFeederService getCommonResourceFeederService()
    {
        return this.commonResourceFeederService;
    }

    public AosTemplateService getAosTemplateService()
    {
        return aosTemplateService;
    }

    public void setAosTemplateService(AosTemplateService aosTemplateService)
    {
        this.aosTemplateService = aosTemplateService;
    }

    public UmsService getUmsService()
    {
        return umsService;
    }

    public AosStackService getAosStackService()
    {
        return aosStackService;
    }


    public ServiceManagerService getServiceManagerService()
    {
        return serviceManagerService;
    }


    public CloudManagerService getCloudManagerService()
    {
        return cloudManagerService;
    }


    public ClusterManagerService getClusterManagerService()
    {
        return clusterManagerService;
    }


    public ContainerInstService getContainerInstService()
    {
        return containerInstService;
    }


    public CCInstService getCcInstService()
    {
        return ccInstService;
    }


}
