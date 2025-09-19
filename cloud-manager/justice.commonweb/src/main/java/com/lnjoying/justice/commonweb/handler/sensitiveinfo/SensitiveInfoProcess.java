package com.lnjoying.justice.commonweb.handler.sensitiveinfo;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;

public interface SensitiveInfoProcess
{
    SensitiveType type();
    String process(Object sensitiveValue);
}
