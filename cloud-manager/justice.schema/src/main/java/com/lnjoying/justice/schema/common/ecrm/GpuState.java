package com.lnjoying.justice.schema.common.ecrm;

public enum GpuState
{
    REMOVE("Remove",-1),
    FREE("Free",0),
    CONTAINER_USE("ContainerUse",1),
    STACK_USE("StackUse",2),
    CLUSTER_USE("ClusterUse",3);

    public String name;
    public int code;

    GpuState(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static GpuState fromName(String name)
    {
        for (GpuState s : GpuState.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static GpuState fromCode(int code)
    {
        for (GpuState s : GpuState.values())
        {
            if (s.getCode()==code)
            {
                return s;
            }
        }
        return null;
    }
}
