package com.lnjoying.justice.commonweb.handler.sensitiveinfo.impl;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.SensitiveInfoProcess;

/**
 * 邮箱脱敏处理
 *
 * @author: Robin
 * @date: 2024年01月30日 19:20
 */
public class EmailSensitiveInfoProcessImpl implements SensitiveInfoProcess
{
    @Override
    public SensitiveType type()
    {
        return SensitiveType.EMAIL;
    }

    @Override
    public String process(Object obj)
    {
        if (obj == null)
        {
            return "";
        }

        String email = obj.toString();
        if (!email.contains("@") || !email.contains("."))
        {
            return email;
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        int maskLength = (int) Math.ceil(localPart.length() * 2 / 3.0);
        String maskedLocalPart = localPart.substring(0, localPart.length() - maskLength)
                + localPart.substring(localPart.length() - maskLength)
                .replaceAll(".", "*");

        int dotIndex = domainPart.indexOf('.');
        String maskedDomainPart = domainPart.substring(0, dotIndex).replaceAll(".", "*")
                + domainPart.substring(dotIndex);

        return maskedLocalPart + "@" + maskedDomainPart;
    }
}
