/**
 * desc the node for k8s.
 */
package com.lnjoying.justice.cluster.manager.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.common.ClusterNodeStatus;
import com.lnjoying.justice.cluster.manager.common.K8sRole;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.*;

@EqualsAndHashCode(exclude = {"k3sContext", "tipMessage", "forceDelete", "clusterNodeInfo"})
@Data
public class ClusterNodePlanInfo implements Serializable
{
    private String                  clusterId;
    
    private  ClusterNodeStatus       nodeStatus;
    
    private TipMessage tipMessage;
    ClusterNodeInfo  clusterNodeInfo;
    
    List<ContainerPlan> containerPlanList;
    List<ContainerPlan> containerUPlanList;
    List<ContainerPlan> monitorPlanList;

    private boolean forceDelete = false;


    private K3sContext k3sContext = new K3sContext();
    
    public void addContainerPlan(ContainerPlan plan)
    {
        if (containerPlanList == null)
        {
            containerPlanList = new ArrayList<>();
        }
        containerPlanList.add(plan);
    }
    
    public void addContainerUPlan(ContainerPlan plan)
    {
        if (containerUPlanList == null)
        {
            containerUPlanList = new ArrayList<>();
        }
        containerUPlanList.add(plan);
    }

    public void addMonitorPlan(ContainerPlan plan)
    {
        if (monitorPlanList == null)
        {
            monitorPlanList = new ArrayList<>();
        }
        monitorPlanList.add(plan);
    }
    
    public ContainerPlan getContainerPlan()
    {
        if (CollectionUtils.isEmpty(containerPlanList))
        {
            return null;
        }
        return containerPlanList.get(0);
    }
    
    public ContainerPlan getContainerUPlan()
    {
        if (CollectionUtils.isEmpty(containerUPlanList))
        {
            return null;
        }
        return containerUPlanList.get(0);
    }
    
    public void popContainerPlan()
    {
        containerPlanList.remove(0);
    }
    public void popContainerUPlan()
    {
        containerUPlanList.remove(0);
    }
    
    
    public void setLabels()
    {
        clusterNodeInfo.setK8sLabels();
    }

    public static class K3sContext implements Serializable
    {

        private String k3sToken;

        private String k3sKubeConfigOutput;

        private String k3sUrl;

        private String criConfigFile;

        private String role;

        private String nodeName;

        private String withNodeId;

        private boolean disableAgent;

        /**
         * k3s init node
         */
        private boolean init = false;

        private String datastoreEndpoint;
        private String clusterCidr;
        private String serviceCidr;
        private String serviceNodePortRange;
        private String clusterDns;
        private String clusterDomain;

        public Optional<String> getK3sToken()
        {
            return Optional.ofNullable(k3sToken);
        }

        public void setK3sToken(String k3sToken)
        {
            this.k3sToken = k3sToken;
        }

        public Optional<String> getK3sKubeConfigOutput()
        {
            return Optional.ofNullable(k3sKubeConfigOutput);
        }

        public void setK3sKubeConfigOutput(String k3sKubeConfigOutput)
        {
            this.k3sKubeConfigOutput = k3sKubeConfigOutput;
        }

        public Optional<String> getK3sUrl()
        {
            return Optional.ofNullable(k3sUrl);
        }

        public void setK3sUrl(String k3sUrl)
        {
            this.k3sUrl = k3sUrl;
        }

        public Optional<String> getCriConfigFile()
        {
            return Optional.ofNullable(criConfigFile);
        }

        public void setCriConfigFile(String criConfigFile)
        {
            this.criConfigFile = criConfigFile;
        }

        public boolean isInit()
        {
            return init;
        }

        public void setInit(boolean init)
        {
            this.init = init;
        }

        public Optional<String> getRole()
        {
            return Optional.ofNullable(role);
        }

        public void setRole(String role)
        {
            this.role = role;
        }

        public Optional<String> getNodeName()
        {
            return Optional.ofNullable(nodeName);
        }

        public void setNodeName(String nodeName)
        {
            this.nodeName = nodeName;
        }

        public Optional<String> getWithNodeId()
        {
            return Optional.ofNullable(withNodeId);
        }

        public void setWithNodeId(String withNodeId)
        {
            this.withNodeId = withNodeId;
        }

        public boolean isDisableAgent()
        {
            return disableAgent;
        }

        public void setDisableAgent(boolean disableAgent)
        {
            this.disableAgent = disableAgent;
        }

        public Optional<String> getDatastoreEndpoint()
        {
            return Optional.ofNullable(datastoreEndpoint);
        }

        public void setDatastoreEndpoint(String datastoreEndpoint)
        {
            this.datastoreEndpoint = datastoreEndpoint;
        }

        public Optional<String> getClusterCidr()
        {
            return Optional.ofNullable(clusterCidr);
        }

        public void setClusterCidr(String clusterCidr)
        {
            this.clusterCidr = clusterCidr;
        }

        public Optional<String> getServiceCidr()
        {
            return Optional.ofNullable(serviceCidr);
        }

        public void setServiceCidr(String serviceCidr)
        {
            this.serviceCidr = serviceCidr;
        }

        public Optional<String> getServiceNodePortRange()
        {
            return Optional.ofNullable(serviceNodePortRange);
        }

        public void setServiceNodePortRange(String serviceNodePortRange)
        {
            this.serviceNodePortRange = serviceNodePortRange;
        }

        public Optional<String> getClusterDns()
        {
            return Optional.ofNullable(clusterDns);
        }

        public void setClusterDns(String clusterDns)
        {
            this.clusterDns = clusterDns;
        }

        public Optional<String> getClusterDomain()
        {
            return Optional.ofNullable(clusterDomain);
        }

        public void setClusterDomain(String clusterDomain)
        {
            this.clusterDomain = clusterDomain;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            K3sContext that = (K3sContext) o;
            return Objects.equals(nodeName, that.nodeName);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(nodeName);
        }
    }
}
