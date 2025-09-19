package com.lnjoying.justice.cluster.manager.service.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.PlanAction;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.RegistryInfo;
import com.lnjoying.justice.cluster.manager.domain.model.TipMessage;
import com.lnjoying.justice.cluster.manager.util.ClusterConfigUtils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 11/15/21 11:34 AM
 * @Description:
 */
@Data
public class ContainerPlan implements Serializable
{
    private DockerInstParams     dockerInstParams;
    private RegistryInfo         registryInfo;
    private String               containerName;
    
    private String orchType = "k8s";
    private boolean coreContainer = false;
    
    Boolean autoRun      = true;
    
    @JsonProperty("cur_action")
    private PlanAction           preAction;

    //inited/creating/loging/checking
    @SerializedName("cur_action")
    @JsonProperty("cur_action")
    private PlanAction           curAction = PlanAction.INIT;
    
    private String logSucess = "";
    private String logFailed = "";
    private TipMessage tipMessage;

    //inited/creating/loging/checking
    @SerializedName("next_action")
    @JsonProperty("next_action")
    private PlanAction           nextAction = PlanAction.CREATE;
//    private PlanAction           nextAction = PlanAction.REMOVE_BEFORE;
    
    
    public ContainerPlan(String containerName)
    {
        this.containerName = containerName;
        dockerInstParams = new DockerInstParams();
    }

    public ContainerPlan(String containerName, List<RegistryInfo> registryInfos)
    {
        this(containerName);

        String systemRegistryUrl = ClusterConfigUtils.getSystemRegistryUrl();
        if (StringUtils.isNotBlank(systemRegistryUrl))
        {
            this.registryInfo = new RegistryInfo();
            registryInfo.setServer(systemRegistryUrl);
            registryInfo.setType("public");
            return;
        }

        if (CollectionUtils.isEmpty(registryInfos))
        {
            return;
        }
        
        if (registryInfos.size() == 1)
        {
            this.registryInfo =  registryInfos.get(0);
            return;
        }
        
        for (RegistryInfo r : registryInfos)
        {
            if (r.getServer().startsWith("docker.io"))
            {
                continue;
            }
            
            this.registryInfo = r;
            
            if (r.getType().equals("public"))
            {
                return;
            }
        }
    }
    
    private long actionTime = 0;
    private int  errorTime  = 0;
}
