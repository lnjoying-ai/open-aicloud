package com.lnjoying.justice.iam.handler;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

import static com.lnjoying.justice.schema.common.ErrorCode.ILLEGAL_ARGUMENT;

/**
 * Replace the built-in IllegalArgumentExceptionToProducerResponseConverter
 *
 * @author merak
 **/

@Slf4j
public class IllegaArgumentExceptionToResponseConverter implements ExceptionToProducerResponseConverter<IllegalArgumentException>
{

    @Override
    public Class<IllegalArgumentException> getExceptionClass()
    {
        return IllegalArgumentException.class;
    }

    @Override
    public Response convert(SwaggerInvocation swaggerInvocation, IllegalArgumentException e)
    {
        String summary = ((Invocation) swaggerInvocation).getOperationMeta().getSwaggerOperation().getSummary();
        log.error(summary + " error: {}", e);
        ErrorData data = new ErrorData();
        data.setCode(ILLEGAL_ARGUMENT.getCode());
        data.setMessage(e.getMessage());
        InvocationException state = new InvocationException(WebSystemException.getHeadStatus(ILLEGAL_ARGUMENT), data);

        return Response.failResp(state);
    }

    @Override
    public int getOrder()
    {
        return -100;
    }
}
