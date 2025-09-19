package com.lnjoying.justice.ecrm.common.constant;

public enum PlanAction
{
    INIT(         0, "init"),
    
    //before new container create, need to drop old container
    REMOVE_BEFORE(1,"remove old container before create"),
    CREATE(       2, "create container"),
    LIST(         3, "list container"),
    LOG(          4, "log container"),
    LOG_CHECK(    5, "log check"),
    
    //container has been executed, need to dropped
    REMOVE_AFTER( 6,"remove after container used"),
    
    NEW(          7, "new next container"),
    ABNORMAL_QUIT(8,"abnormal quit");
    
    
    PlanAction(int code, String message)
    {
        this.setCode(code);
        this.setMessage(message);
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
