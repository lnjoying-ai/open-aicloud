package com.lnjoying.justice.commonweb.handler.sensitiveinfo;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.impl.EmailSensitiveInfoProcessImpl;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.impl.PasswordSensitiveInfoProcessImpl;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.impl.PhoneSensitiveInfoProcessImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 敏感信息脱敏处理器工厂，根据敏感类别提供敏感信息处理器
 *
 * @author: Robin
 * @date: 2024年01月30日 19:25
 */
public class SensitiveInfoProcessFactory
{
    private static Map<SensitiveType, SensitiveInfoProcess> processMap = new HashMap<>();

    static
    {
        processMap.put(SensitiveType.EMAIL, new EmailSensitiveInfoProcessImpl());
        processMap.put(SensitiveType.PHONE, new PhoneSensitiveInfoProcessImpl());
        processMap.put(SensitiveType.PASSWORD, new PasswordSensitiveInfoProcessImpl());
    }

    public static Optional<SensitiveInfoProcess> getProcessor(SensitiveType type)
    {
        return Optional.ofNullable(processMap.get(type));
    }
}
