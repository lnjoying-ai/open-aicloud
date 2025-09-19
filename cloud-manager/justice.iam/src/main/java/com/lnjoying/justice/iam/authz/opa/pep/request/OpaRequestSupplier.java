package com.lnjoying.justice.iam.authz.opa.pep.request;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 9:54
 */

@FunctionalInterface
public interface OpaRequestSupplier<T>
{
    T get(HttpServletRequest request);
}
