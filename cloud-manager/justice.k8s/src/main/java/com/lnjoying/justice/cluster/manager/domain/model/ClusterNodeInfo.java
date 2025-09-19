package com.lnjoying.justice.cluster.manager.domain.model;

import com.lnjoying.justice.cluster.manager.common.K8sRole;
import com.lnjoying.justice.schema.entity.docker.DockerInfo;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.schema.entity.scheduler.ClusterBindNode;
import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ClusterNodeInfo extends ClusterBindNode implements Serializable
{
    private Boolean    existingEtcdCluster = false;
    
    public void setK8sLabels()
    {
        if (getLabels() == null)
        {
            setLabels(new HashMap<>());
        }
    
        getLabels().put("kubernetes.io/hostname", getNodeName());
    
        if (getClusterRoles().contains(K8sRole.ETCD))
        {
            getLabels().put("node-role.kubernetes.io/etcd", "true");
        }
    
        if (getClusterRoles().contains(K8sRole.CONTROLLER))
        {
            getLabels().put("node-role.kubernetes.io/controller", "true");
        }
    
        if (getClusterRoles().contains(K8sRole.WORKER))
        {
            getLabels().put("node-role.kubernetes.io/worker", "true");
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
