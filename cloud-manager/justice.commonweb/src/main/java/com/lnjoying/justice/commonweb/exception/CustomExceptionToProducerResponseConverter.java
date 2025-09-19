package com.lnjoying.justice.commonweb.exception;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorData;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

public class CustomExceptionToProducerResponseConverter implements
        ExceptionToProducerResponseConverter<WebSystemException> {
    @Override
    public Class<WebSystemException> getExceptionClass() {
        return WebSystemException.class;
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public Response convert(SwaggerInvocation swaggerInvocation, WebSystemException e)
    {
        ErrorData data = new ErrorData();

        data.setCode(e.getCode().getCode());

        data.setMessage(e.getMessage());

        InvocationException state = new InvocationException(e.getHeadStatus(e.getCode()), data);

        return Response.failResp(state);
    }
}
