package com.lnjoying.justice.schema.constant;

/**
 * @Author: Regulus
 * @Date: 12/28/21 2:44 PM
 * @Description:
 */
public enum  UserKindEnum
{
    SYSTEM(      0, "SYSTEM"),
    ADMIN(       1, "ADMIN"),
    BP_ADMIN(    2, "BP_ADMIN"),
    PERSONAL(    3, "PERSONAL"),
    BP_SUB_USER( 4, "BP_SUB_USER");
    
    UserKindEnum(int code, String message)
    {
        this.setCode(code);
        this.setMessage(message);
    }
    
    public static UserKindEnum fromCode(int code)
    {
        for (UserKindEnum current : UserKindEnum.values())
        {
            if (current.getCode() == code)
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid error code: " + code);
    }

    public static UserKindEnum fromMessage(String message)
    {
        for (UserKindEnum current : UserKindEnum.values())
        {
            if (current.getMessage().equals(message))
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid error message: " + message);
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
