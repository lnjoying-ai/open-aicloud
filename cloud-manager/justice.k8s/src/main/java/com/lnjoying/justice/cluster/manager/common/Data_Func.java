package com.lnjoying.justice.cluster.manager.common;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;

@FunctionalInterface
public interface Data_Func<T1, T2>
{
    T2 operator(T1 t1);
}