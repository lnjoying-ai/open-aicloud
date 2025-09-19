package com.lnjoying.justice.commonweb.util;

import com.lnjoying.justice.schema.constant.UserKindEnum;

/**
 * @Description: util for user
 * @Author: Regulus
 * @Date: 1/6/22 9:07 PM
 */
public class UserUtil
{
    public static boolean isAdmin(String userKind)
    {
        if (UserKindEnum.valueOf(userKind).equals(UserKindEnum.ADMIN)
                || UserKindEnum.valueOf(userKind).equals(UserKindEnum.SYSTEM))
        {
            return true;
        }
        return false;
    }
    
    public static boolean isBpAdmin(String userKind)
    {
        if (UserKindEnum.valueOf(userKind).equals(UserKindEnum.BP_ADMIN))
        {
            return true;
        }
        return false;
    }
    
    public static boolean isBpSubUser(String userKind)
    {
        if (UserKindEnum.valueOf(userKind).equals(UserKindEnum.BP_SUB_USER))
        {
            return true;
        }
        return false;
    }
    
    public static boolean isPersonal(String userKind)
    {
        if (UserKindEnum.valueOf(userKind).equals(UserKindEnum.PERSONAL))
        {
            return true;
        }
        return false;
    }
}
