package com.lnjoying.justice.cluster.manager.common;

/**
 * @Author: Regulus
 * @Date: 12/21/21 3:15 PM
 * @Description:
 */
public enum SchedulerState
{
    WAITING_SCHEDULING("WaitScheduling",0),
    SCHEDULING("Scheduling",1),
    SUCCESS("success",2),
    FAIL("fail",3),
    NO_MATCHED_NODE("NoMatchedNode",4);
    
    String name;
    int code;
    
    SchedulerState(String name, int code)
    {
        this.name = name;
        this.code = code;
    }
    
    public int getCode() {return code;}
    public String getName() {return name;}
}
