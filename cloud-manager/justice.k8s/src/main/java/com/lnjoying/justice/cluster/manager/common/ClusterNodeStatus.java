package com.lnjoying.justice.cluster.manager.common;

import java.io.Serializable;

public enum ClusterNodeStatus implements Serializable
{
    BINDED(         0, "node binded to cluster"),
    PLANNING(       1, "building deploy cluster plan"),
    PLANNED(        2, "build cluster deploy plan success"),
    PLAN_FAILED(        3, "build cluster deploy plan failed"),
    DEPLOYING(      4, "DEPLOYING"),
    DEPLOYED(       5, "DEPLOYED"),
    DEPLOY_FAILED(  6, "Deploy failed"),
    ACTIVE(         7, "ACTIVE"),
    UPLANNING(      8, "building undeploy cluster plan"),
    UPLANNED(       9, "build cluster undeploy plan success"),
    UDEPLOYING(     10, "undeploying"),
    UDEPLOY_FAILED( 11, "undeploy failed"),
    RELEASED(       12, "RELEASED");
    
    ClusterNodeStatus(int code, String message)
    {
        this.setCode(code);
        this.setMessage(message);
    }
    
    public static ClusterNodeStatus fromCode(int code)
    {
        for (ClusterNodeStatus current : ClusterNodeStatus.values())
        {
            if (current.getCode() == code)
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid error code: " + code);
    }
    
    public void setCode(int code)
    {
        this.code = code;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public int getCode()
    {
        return code;
    }
    
    public String getMessage()
    {
        return message;
    }

    /** Numeric code */
    private int code;
    
    /** Error message */
    private String message;
}
