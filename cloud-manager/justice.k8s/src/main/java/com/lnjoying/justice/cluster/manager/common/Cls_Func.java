package com.lnjoying.justice.cluster.manager.common;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;

@FunctionalInterface
public interface Cls_Func<T1, T2>
{
    ErrorCode operator(T1 t1,T2 t2) throws WebSystemException;
}