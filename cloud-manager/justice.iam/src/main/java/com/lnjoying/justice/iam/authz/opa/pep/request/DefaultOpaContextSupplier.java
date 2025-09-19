package com.lnjoying.justice.iam.authz.opa.pep.request;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 9:56
 */

public class DefaultOpaContextSupplier implements OpaRequestSupplier<Context>
{
    @Override
    public Context get(HttpServletRequest request)
    {
       // todo Extract the information in the request header and convert it to context information
        return null;
    }
}
