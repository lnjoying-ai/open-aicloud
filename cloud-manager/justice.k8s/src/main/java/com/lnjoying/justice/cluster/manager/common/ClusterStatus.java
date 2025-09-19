package com.lnjoying.justice.cluster.manager.common;

import java.util.HashMap;
import java.util.Map;

public enum  ClusterStatus
{
    CREATED(               0, "实例已创建", "Created"),
    SPAWN(                 1, "发起调度", "Spawn"),
    ASSIGNING(             2, "分配节点中", "Assigning"),
    ASSIGNED(              3, "已分配节点", "Assigned"),
    PLANNING(              4, "构建任务中", "building deploy plan"),
    PLANNED(               5, "构建任务完成", "build deploy plan success"),
    PLAN_FAILED(           6, "构建任务失败", "build deploy plan failed"),
    DEPLOYING(             7, "部署中", "deploying"),
    DEPLOYED(              8, "部署完成", "deployed"),
    DEPLOY_PARTIAL_SUCCESS(9, "部分部署成功", "deploy partial success"),
    IMPORTED(             10, "导入完成", "imported"),
    DEPLOY_FAILED(        11, "部署失败", "deploy failed"),
    ASSIGNED_FAILED(      12, "分配节点失败", "Assigned failed"),
    BUILD_FAILED(         13, "构建失败", "build failed"),
    
    REMOVING(             90, "删除中", "Removing"),
    REMOVE_FAILED(        91, "删除失败", "Remove failed"),
    REMOVED(              99, "已删除", "Removed");
    
    ClusterStatus(int code, String cnDesc, String enDesc)
    {
        this.setCode(code);
        this.message = new HashMap<String, String>()
        {
            {
                put("cn", cnDesc);
                put("en", enDesc);
            }
        };
    }
    
    public static ClusterStatus fromCode(int code)
    {
        for (ClusterStatus current : ClusterStatus.values())
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
    
    public int getCode()
    {
        return code;
    }
    
    public Map<String, String> getMessage()
    {
        return message;
    }

    /** Numeric code */
    private int code;
    
    /** Error message */
    private Map<String, String> message;
}
