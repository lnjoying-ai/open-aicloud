package com.lnjoying.justice.cluster.manager.config;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @Author: Regulus
 * @Date: 12/9/21 8:25 PM
 * @Description:
 */
@Data
public class K8sBasicConfig
{
    private String default_k8s_version;
    private Map<String, Set<String>> docker_versions;
    
    public String getDefaultK8sVersion()
    {
        return default_k8s_version;
    }
    
    public Map<String, Set<String>> getDockerVersions()
    {
        return docker_versions;
    }
}
