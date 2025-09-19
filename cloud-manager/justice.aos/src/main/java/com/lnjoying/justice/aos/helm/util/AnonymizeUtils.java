package com.lnjoying.justice.aos.helm.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * tool for anonymize
 *
 * @author merak
 **/

public class AnonymizeUtils
{
    private static final String MASK_SIGN = "******";

    private AnonymizeUtils()
    {
    }

    /**
     * anonymize password.
     * @param commands
     */
    public static void  anonymizePassword(List<String> commands)
    {
        if (!CollectionUtils.isEmpty(commands))
        {
            String passwordFlag = "--password";
            int passwordIndex = commands.indexOf(passwordFlag);
            if (passwordIndex != -1)
            {
                commands.set(passwordIndex + 1, "******");
            }
        }
    }



    /**
     * check if anonymized.
     *
     * @param strValue check str
     * @return if anonymized
     */
    public static boolean isAnonymized(String strValue)
    {
        if (StringUtils.isEmpty(strValue))
        {
            return false;
        }

        return strValue.indexOf(MASK_SIGN) >= 0;
    }
}
