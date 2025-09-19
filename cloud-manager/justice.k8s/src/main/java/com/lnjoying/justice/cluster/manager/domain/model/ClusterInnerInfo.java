/**
 * cluster inner info
 */
package com.lnjoying.justice.cluster.manager.domain.model;

import com.lnjoying.justice.cluster.manager.common.ClusterActiveStatus;
import com.lnjoying.justice.cluster.manager.common.ClusterStatus;
import com.lnjoying.justice.cluster.manager.common.K8sRole;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.ClusterBaseInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.*;

@Data
public class ClusterInnerInfo extends ClusterBaseInfo implements Serializable
{
    public ClusterInnerInfo()
    {
//        clusterNodePlans = new ArrayList<>();
        certMap = new HashMap<>();
        clusterNodePlanMap = new HashMap<>();
        networkOption = new NetworkOption();
        authnStrategies = new HashMap<>();
    }
    
    private String                                        clusterName;
    private  String                                          clusterId;
    private Map<String,X509CertificateInfo>                   certMap;
    private Map<String, List<ClusterNodePlanInfo>> clusterNodePlanMap;
    private ClusterStatus                                      status;
    private  NetworkOption                               networkOption;
    private Map<String, Boolean>                      authnStrategies;
    private int                                       serviceStatus = ClusterActiveStatus.INACTIVE;
    public void addNodePlan(ClusterNodePlanInfo clusterNodePlanInfo, String roles)
    {
        List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterNodePlanMap.get(roles);
        if (clusterNodePlanInfos == null)
        {
            clusterNodePlanInfos = new ArrayList<>();
            clusterNodePlanMap.put(roles, clusterNodePlanInfos);
        }
        clusterNodePlanInfos.add(clusterNodePlanInfo);
    }
    
    public int nodePlanSize()
    {
        return clusterNodePlanMap.size();
    }
    
    public List<ClusterNodePlanInfo> getClusterNodePlanInfos(String role)
    {
        List<ClusterNodePlanInfo> clusterNodePlanInfoList = new ArrayList<>();
        for (Map.Entry<String, List<ClusterNodePlanInfo>> entry : clusterNodePlanMap.entrySet())
        {
            if (entry.getKey().contains(role))
            {
                clusterNodePlanInfoList.addAll(entry.getValue());
            }
        }
        return clusterNodePlanInfoList;
    }
    
    public ClusterNodePlanInfo getClusterNodePlanInfo(String role)
    {
        for (Map.Entry<String, List<ClusterNodePlanInfo>> entry : clusterNodePlanMap.entrySet())
        {
            if (entry.getKey().contains(role))
            {
                return entry.getValue().get(0);
            }
        }
        return null;
    }
    
    public void addCert(String certName, X509CertificateInfo certificateInfo)
    {
        if (certMap == null)
        {
            certMap = new HashMap<>();
        }
        certMap.put(certName, certificateInfo);
    }
    
    public X509CertificateInfo getCertInfo(String certName)
    {
        return certMap.get(certName);
    }
    
    public X509Certificate getX509Certificate(String certName)
    {
        X509CertificateInfo certInfo = certMap.get(certName);
        if (certInfo == null)
        {
            return null;
        }
        return certInfo.getCertificate();
    }
    
    public KeyPair getKeyPair(String certName)
    {
        X509CertificateInfo certInfo = certMap.get(certName);
        if (certInfo == null)
        {
            return null;
        }
        
        KeyPair keyPair = new KeyPair(certInfo.getPublicKey(), certInfo.getPrivateKey());
        
        return keyPair;
    }
    
    public void putAuthnStrategies(String key, boolean value)
    {
        authnStrategies.put(key, value);
    }

    public String getEndpointNodeIp()
    {
        String endpointNodeIp = "";
        // todo multiple masters
        ClusterNodePlanInfo clusterNodePlanInfo = this.getClusterNodePlanInfo(K8sRole.CONTROLLER);
        if (Objects.nonNull(clusterNodePlanInfo))
        {
            endpointNodeIp = getEndpointNodeIp(clusterNodePlanInfo.getClusterNodeInfo());
        }
        return endpointNodeIp;
    }

    private String getEndpointNodeIp(ClusterNodeInfo clusterNodeInfo){
        String endpointNodeIp = "127.0.0.1";
        String externalAddress = clusterNodeInfo.getExternalAddress();
        if (StringUtils.isNotBlank(externalAddress))
        {
            return externalAddress;
        }

        String internalAddress = clusterNodeInfo.getInternalAddress();
        if (StringUtils.isNotBlank(internalAddress))
        {
            return internalAddress;
        }

        return endpointNodeIp;
    }
}
