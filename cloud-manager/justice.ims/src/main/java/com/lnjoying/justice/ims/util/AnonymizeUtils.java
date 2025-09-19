package com.lnjoying.justice.ims.util;

import org.apache.commons.lang3.StringUtils;

/**
 * tool for anonymize
 *
 * @author merak
 **/

public class AnonymizeUtils
{
    private static final int MOBILE_PHONE_LEN = 11;

    private static final String MASK_SIGN = "****";

    private AnonymizeUtils()
    {
    }

    public static String anonymizePhoneNum(String phoneNum)
    {
        if (StringUtils.isEmpty(phoneNum) || phoneNum.length() != MOBILE_PHONE_LEN)
        {
            return phoneNum;
        }
        return phoneNum.substring(0, 3).concat(MASK_SIGN).concat(phoneNum.substring(7));
    }

    /**
     * anonymize password.
     *
     * @param password password
     * @return anonymized password
     */
    public static String anonymizePassword(String password)
    {
        if (StringUtils.isEmpty(password))
        {
            return password;
        }
        return password.substring(0, 3).concat(MASK_SIGN).concat(password.substring(7));
    }

    /**
     * anonymize mail address.
     *
     * @param mailAddress email address
     * @return anonymized email address
     */
    public static String anonymizeMail(String mailAddress)
    {
        if (StringUtils.isEmpty(mailAddress))
        {
            return mailAddress;
        }

        int pos = mailAddress.indexOf("@");
        if (pos <= 1)
        {
            return mailAddress;
        }

        return mailAddress.substring(0, 1).concat(MASK_SIGN).concat(mailAddress.substring(pos));
    }

    /**
     * check if anonymized.
     *
     * @param strValue check str
     * @return if anonymized
     */
    public static boolean isanonymized(String strValue)
    {
        if (StringUtils.isEmpty(strValue))
        {
            return false;
        }

        return strValue.indexOf(MASK_SIGN) >= 0;
    }
}
