package com.lnjoying.justice.commonweb.handler.sensitiveinfo.impl;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.SensitiveInfoProcess;

/**
 * 密码脱敏处理
 *
 * @author: Robin
 * @date: 2024年01月30日 19:22
 */
public class PasswordSensitiveInfoProcessImpl implements SensitiveInfoProcess
{
    @Override
    public SensitiveType type()
    {
        return SensitiveType.PASSWORD;
    }

    @Override
    public String process(Object obj)
    {
        return "********";
    }
}
