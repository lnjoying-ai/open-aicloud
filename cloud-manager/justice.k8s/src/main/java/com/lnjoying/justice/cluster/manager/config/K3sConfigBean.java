package com.lnjoying.justice.cluster.manager.config;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/12 16:13
 */

@Data
public class K3sConfigBean
{
    private Map<String,Map<String, Map<String,String>>> k3sServiceOptions;

    private Map<String,Map<String,String>>        k3sAddonTemplatesIndex;
    private Map<String,Map<String,String>>            k3sJkeSystemImages;

    private K3sBasicConfig k3sBasicConfig;

    @Data
    public static final class K3sBasicConfig
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
}
