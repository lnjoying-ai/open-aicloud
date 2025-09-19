package com.lnjoying.justice.cluster.manager.util;

import com.lnjoying.justice.cluster.manager.common.KeyCertName;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Regulus
 * @Date: 11/17/21 4:16 PM
 * @Description:
 */
public class EnvUtil
{
    private static String K8sBaseDir              = "/etc/kubernetes/";
    private static String K3sBaseDir              = "/var/lib/rancher/k3s/";
    private  static String CertPathPrefix          = K8sBaseDir + "ssl/";
    private  static String K3sCertPathPrefix          = K3sBaseDir + "server/tls/";
    
    public static String getEnvFromName(String name )
    {
        return StringUtils.upperCase(name).replace("-","_");
    }
    
    public static String getKeyEnvFromEnv(String env)
    {
        return String.format("%s_KEY", env);
    }
    
    public static String getCertPath(String name)
    {
        return Utils.buildStr(CertPathPrefix, name, ".pem");
    }
    
    public static String getKeyPath(String name)
    {
        return Utils.buildStr(CertPathPrefix, name, "-key.pem");
    }

    public static String getK3sCertPath(String name)
    {
        return Utils.buildStr(K3sCertPathPrefix, name, ".crt");
    }

    public static String getK3sKeyPath(String name)
    {
        return Utils.buildStr(K3sCertPathPrefix, name, ".key");
    }
    
    /**
     * get default common name
     * @param name
     * @return
     */
    public static String getDefaultCN(String name)
    {
        return String.format("system:%s", name);
    }
    
    public static String getConfigPath(String name)
    {
        return Utils.buildStr(CertPathPrefix, "kubecfg-", name, ".yaml");
    }
    
    public static String getConfigEnvFromEnv(String env)
    {
        return String.format("KUBECFG_%s", env);
    }
    
    public static String getLocalKubeConfig(String name)
    {
        return Utils.buildStr(CertPathPrefix, KeyCertName.KubeAdminConfigPrefix, name);
    }
    
    public static String getCertNameForHost(ClusterNodeInfo host, String prefix)
    {
        String newAddress = null;
        if (! StringUtils.isEmpty(host.getInternalAddress()))
        {
            newAddress = host.getInternalAddress().replace(".", "-");
    
        }
        else
        {
            newAddress = host.getExternalAddress().replace(".", "-");
        }
        
        return prefix + "-" + newAddress.toLowerCase();
    }
}
