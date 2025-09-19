package com.lnjoying.justice.ims.handler;

import com.lnjoying.justice.schema.common.ErrorData;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.lnjoying.justice.schema.common.ErrorCode.SystemError;


/**
 * handle invocation and log
 *
 * @author merak
 **/

@Slf4j
public class InvocationExceptionToResponseConverter implements
        ExceptionToProducerResponseConverter<InvocationException>
{
    @Override
    public Class<InvocationException> getExceptionClass()
    {
        return InvocationException.class;
    }

    @Override
    public Response convert(SwaggerInvocation swaggerInvocation, InvocationException e)
    {
        String summary = ((Invocation) swaggerInvocation).getOperationMeta().getSwaggerOperation().getSummary();
        log.error(summary + " error: {}", e);

        ErrorData data = new ErrorData();
        data.setCode(SystemError.getCode());
        data.setMessage(Optional.ofNullable(e.getMessage()).isPresent() ? e.getMessage() : "");
        InvocationException state = new InvocationException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR, data);
        return Response.failResp(state);
    }

    @Override
    public int getOrder()
    {
        return -50;
    }
}
