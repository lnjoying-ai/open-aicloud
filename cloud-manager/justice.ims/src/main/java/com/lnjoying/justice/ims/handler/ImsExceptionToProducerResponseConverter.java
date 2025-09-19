package com.lnjoying.justice.ims.handler;

import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorData;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

/**
 * ImsWebSystemException converter
 *
 * @author merak
 **/

public class ImsExceptionToProducerResponseConverter implements
        ExceptionToProducerResponseConverter<ImsWebSystemException>
{
    @Override
    public Class<ImsWebSystemException> getExceptionClass()
    {
        return ImsWebSystemException.class;
    }

    @Override
    public int getOrder()
    {
        return 99;
    }

    @Override
    public Response convert(SwaggerInvocation swaggerInvocation, ImsWebSystemException e)
    {
        ErrorData data = new ErrorData();

        data.setCode(e.getCode().getCode());

        data.setMessage(e.getMessage());

        InvocationException state = new InvocationException(ImsWebSystemException.getHeadStatus(e.getCode()), data);

        return Response.failResp(state);
    }
}