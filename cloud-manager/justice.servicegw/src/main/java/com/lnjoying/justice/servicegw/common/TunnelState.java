package com.lnjoying.justice.servicegw.common;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/26/22 12:42 PM
 */
public enum TunnelState
{
    CREATING(       0, "creating tunnel"),
    ACTIVE(         1, "tunnel active"),
    CLOSING(        2, "closing tunnel"),
    CLOSED(        3, "tunnel closed");
    
    TunnelState(int code, String message)
    {
        this.setCode(code);
        this.setMessage(message);
    }
    
    public static TunnelState fromCode(int code)
    {
        for (TunnelState current : TunnelState.values())
        {
            if (current.getCode() == code)
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid state code: " + code);
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
