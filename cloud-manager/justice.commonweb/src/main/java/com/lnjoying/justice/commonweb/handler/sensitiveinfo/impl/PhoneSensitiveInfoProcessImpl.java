package com.lnjoying.justice.commonweb.handler.sensitiveinfo.impl;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.SensitiveInfoProcess;

/**
 * 手机号脱敏处理
 *
 * @author: Robin
 * @date: 2024年01月30日 19:18
 */
public class PhoneSensitiveInfoProcessImpl implements SensitiveInfoProcess
{
    @Override
    public SensitiveType type()
    {
        return SensitiveType.PHONE;
    }

    @Override
    public String process(Object obj)
    {
        if (obj == null)
        {
            return "";
        }

        String phoneNumber = obj.toString();
        if (phoneNumber.length() < 7)
        {
            return phoneNumber;
        }

        if (phoneNumber.length() - 8 < 0)
        {
            return phoneNumber;
        }

        String front = phoneNumber.substring(0, phoneNumber.length() - 8);
        String end = phoneNumber.substring(phoneNumber.length() - 4);
        String stars = "****";
        return front + stars + end;
    }
}
