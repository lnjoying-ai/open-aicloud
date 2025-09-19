package com.lnjoying.justice.iam.handler;

import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.definition.OperationMeta;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.swagger.invocation.Response;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/30 10:16
 */

public class CacheRequestFilter implements HttpServerFilter
{

    @Override
    public int getOrder()
    {
        return 0;
    }

    @Override
    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx)
    {
        return null;
    }

    @Override
    public boolean needCacheRequest(OperationMeta operationMeta)
    {
        return true;
    }
}

