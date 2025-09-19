package com.lnjoying.justice.commonweb.handler.aspect.functionalinterface;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月25日 16:30
 */
public interface ThreeArgumentActionConsumer<T1, T2, T3>
{
    void callback(T1 t1, T2 t2, T3 t3);
}
