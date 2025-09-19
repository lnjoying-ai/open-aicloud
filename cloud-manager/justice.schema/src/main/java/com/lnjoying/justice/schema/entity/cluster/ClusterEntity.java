package com.lnjoying.justice.schema.entity.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/26/22 11:59 AM
 */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ClusterEntity implements Cloneable
//{
//    private String clusterId;
//    private String workderId;
//    private String clusterIp;
//    private int    clusterPort;
//    private String token;
//    private String clusterCaCrtPem;
//    private String userKeyPem;
//    private String userCrtPem;
//
//    public ClusterEntity clone() throws CloneNotSupportedException
//    {
//        ClusterEntity entity = (ClusterEntity) super.clone();
//        return entity;
//    }
//}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterEntity implements Cloneable
{
    private String clusterId;
    private String workderId;
    private String clusterIp;
    private int    clusterPort;
    private String token;
    private String clusterCaCrtPem;
    private String userKeyPem;
    private String userCrtPem;
    private String clsvrUrl;
    private long   timestamp;
    private String vendor;
    private String clusterType;
    private String mode;//direct、proxy

    private String urlPrefix;

    private boolean networkTest = false;

    String copyStr(String value)
    {
        if (null != value)
        {
            return new StringBuilder(value).toString();
        }
        
        return value;
    }
    
    
    public ClusterEntity clone() throws CloneNotSupportedException
    {
        super.clone();
        ClusterEntity entity = (ClusterEntity)super.clone();
        entity.clusterId = copyStr(clusterId);
        entity.workderId = copyStr(workderId);
        entity.clusterIp = copyStr(clusterIp);
        entity.token = copyStr(token);
        entity.clusterCaCrtPem = copyStr(clusterCaCrtPem);
        entity.userKeyPem = copyStr(userKeyPem);
        entity.userCrtPem = copyStr(userCrtPem);
        entity.urlPrefix = copyStr(urlPrefix);
        entity.mode = copyStr(mode);
        return entity;
        
    }
}
