package com.lnjoying.justice.cluster.manager.common;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;

public interface LogCheck
{
    ErrorCode check(String log) throws WebSystemException;
}