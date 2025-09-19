package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.cluster.manager.common.ClusterNodeStatus;
import com.lnjoying.justice.cluster.manager.common.InstanceState;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.K8sStatusCode;
import com.lnjoying.justice.cluster.manager.domain.model.TipLevel;
import com.lnjoying.justice.cluster.manager.domain.model.TipMessage;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.docker.DockerInfo;
import com.micro.core.common.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Regulus
 * @Date: 12/14/21 10:27 AM
 * @Description:
 */
@Data
@Slf4j
public class ClusterNodeInfoDto
{
    @JsonProperty("node_id")
    private String nodeId;
    
    @JsonProperty("node_name")
    private String nodeName;
    
    @JsonProperty("internal_address")
    private String internalAddress;
    
    @JsonProperty("external_address")
    private  String externalAddress;
    
    @JsonProperty("region_id")
    private String regionId;
    
    @JsonProperty("site_id")
    private String siteId;
    
    @JsonProperty("cluster_roles")
    private String clusterRoles;
    
    @JsonProperty("status")
    private K8sStatusCode status;
    
    @JsonProperty("tip_message")
    private TipMessage tipMessage;
    
    @JsonProperty("docker_version")
    private String dockerVersion;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("site_name")
    private String siteName;

    @JsonProperty("runtime_version")
    private String runtimeVersion;

    @JsonProperty("core_services")
    private List<CoreService> coreServices;

    public static ClusterNodeInfoDto of(CombRpcService combRpcService, TblClusterNodeInfo tblClusterNodeInfo)
    {
        ClusterNodeInfoDto clusterNodeInfo = new ClusterNodeInfoDto();
        BeanUtils.copyProperties(tblClusterNodeInfo, clusterNodeInfo);
        setSiteAndRegionInfo(combRpcService, clusterNodeInfo);
        DockerInfo dockerInfo = JsonUtils.fromJson(tblClusterNodeInfo.getDockerInfo(), DockerInfo.class);
        if (dockerInfo != null)
        {
            clusterNodeInfo.setDockerVersion(dockerInfo.getServerVersion());
        }
        K8sStatusCode statusCode = K8sStatusCode.builder().code(tblClusterNodeInfo.getStatus()).build();
        clusterNodeInfo.setStatus(statusCode);
        Map<String,String> codedesc = new HashMap<>();
        ClusterNodeStatus clusterNodeStatus = ClusterNodeStatus.fromCode(tblClusterNodeInfo.getStatus());
        codedesc.put("en", clusterNodeStatus.getMessage());
        statusCode.setDesc(codedesc);
        
        TipMessage tipMessage = new TipMessage();
        
        switch (clusterNodeStatus)
        {
            case PLAN_FAILED:
                tipMessage.setLevel(TipLevel.ERROR);
                tipMessage.setMessage("build plan failed");
                break;
            case DEPLOY_FAILED:
                tipMessage.setLevel(TipLevel.ERROR);
                List<ContainerPlan> failedPlanList = JsonUtils.fromJson(tblClusterNodeInfo.getDeployPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
                if (CollectionUtils.isEmpty(failedPlanList))
                {
                    tipMessage.setMessage("deploy failed");
                }
                else
                {
                    ContainerPlan plan = failedPlanList.get(0);
                    String msg = Utils.buildStr("deploy ", plan.getContainerName(), " filed, action: ", plan.getCurAction().getMessage());
                    tipMessage.setMessage(msg);
                }
                break;
            case DEPLOYING:
                List<ContainerPlan> planList = JsonUtils.fromJson(tblClusterNodeInfo.getDeployPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
                if (! CollectionUtils.isEmpty(planList))
                {
                    ContainerPlan plan = planList.get(0);
                    String msg = Utils.buildStr("deploying ", plan.getContainerName(), " action: ", plan.getCurAction().getMessage());
                    tipMessage.setMessage(msg);
                }
                break;
            case UDEPLOYING:
                List<ContainerPlan> uplanList = JsonUtils.fromJson(tblClusterNodeInfo.getUndeployPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
                if (! CollectionUtils.isEmpty(uplanList))
                {
                    ContainerPlan uplan = uplanList.get(0);
                    String msg = Utils.buildStr("undeploying ", uplan.getContainerName(), " action: ", uplan.getCurAction().getMessage());
                    tipMessage.setMessage(msg);
                }
                break;
            default:
                tipMessage.setMessage(clusterNodeStatus.getMessage());
        }

        clusterNodeInfo.setCoreServices(getCoreServices(combRpcService, tblClusterNodeInfo));
        clusterNodeInfo.setTipMessage(tipMessage);
        return clusterNodeInfo;
    }

    private static void setSiteAndRegionInfo(CombRpcService combRpcService, ClusterNodeInfoDto clusterNodeInfoDto)
    {
        String siteId = clusterNodeInfoDto.getSiteId();
        CompletableFuture<String> siteCompletableFuture = CompletableFuture.supplyAsync(() ->
        {
            if (StringUtils.isNotBlank(siteId))
            {
                String siteName = combRpcService.getRegionResourceService().getSiteNameById(siteId);
                return siteName;
            }
            return "";
        });

        String regionId = clusterNodeInfoDto.getRegionId();
        CompletableFuture<String> regionCompletableFuture = CompletableFuture.supplyAsync(() ->
        {
            if (StringUtils.isNotBlank(regionId))
            {
                String regionName = combRpcService.getRegionResourceService().getRegionNameById(regionId);
                return regionName;
            }
            return "";
        });

        CompletableFuture.allOf(siteCompletableFuture, regionCompletableFuture);
        try
        {
            clusterNodeInfoDto.setRegionName(regionCompletableFuture.get());
            clusterNodeInfoDto.setSiteName(siteCompletableFuture.get());
        }
        catch (Exception e)
        {
           log.error("rpc getRegionResource error:{}", e);
        }
    }

    private static List<CoreService> getCoreServices(CombRpcService combRpcService, TblClusterNodeInfo tblClusterNodeInfo)
    {
        List<CoreService> coreServiceList = null;

        if (tblClusterNodeInfo == null ||
                (tblClusterNodeInfo.getStatus()!=ClusterNodeStatus.ACTIVE.getCode() && tblClusterNodeInfo.getStatus()!=ClusterNodeStatus.DEPLOYED.getCode()))
        {
            return coreServiceList;
        }
        List<ContainerPlan> monitorPlans = JsonUtils.fromJson(tblClusterNodeInfo.getMonitorPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
        coreServiceList = new ArrayList<>();

        for (ContainerPlan plan : monitorPlans)
        {
            coreServiceList.add(new CoreService(plan.getContainerName().split("_")[0],
                    Objects.requireNonNull(InstanceState.fromCode(combRpcService.getInstService().getContainerStatus(tblClusterNodeInfo.getNodeId(), plan.getContainerName()).getStatus())).toStatusCode()));
        }

        return coreServiceList;
    }
}
