package com.lnjoying.justice.cluster.manager.config;

import lombok.Data;

import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 12/24/21 3:28 PM
 * @Description:
 */
@Data
public class K8sConfigBean
{
    private Map<String,Map<String, Map<String,String>>> k8sServiceOptions;
    
    private Map<String,Map<String,String>>        k8sAddonTemplatesIndex;
    private Map<String,Map<String,String>>            k8sJkeSystemImages;
    
    private K8sBasicConfig k8sBasicConfig;
}
