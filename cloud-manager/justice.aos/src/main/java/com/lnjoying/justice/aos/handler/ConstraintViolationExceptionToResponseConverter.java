package com.lnjoying.justice.aos.handler;

import com.lnjoying.justice.schema.common.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.apache.tomcat.util.buf.StringUtils;

import javax.validation.ConstraintViolationException;

import static com.lnjoying.justice.schema.common.ErrorCode.PARAM_ERROR;


/**
 * BindExceptionHandler for param exception
 *
 * @author merak
 **/

@Slf4j
public class ConstraintViolationExceptionToResponseConverter implements
        ExceptionToProducerResponseConverter<ConstraintViolationException>
{
    @Override
    public Class<ConstraintViolationException> getExceptionClass()
    {
        return ConstraintViolationException.class;
    }

    @Override
    public Response convert(SwaggerInvocation swaggerInvocation, ConstraintViolationException e)
    {
        log.error(e.getMessage());
        ErrorData data = new ErrorData();

        data.setCode(PARAM_ERROR.getCode());

        String[] result = e.getConstraintViolations().stream().map(x -> "tip: " + x.getMessage() + ", invalidValue: " + x.getInvalidValue() + "; ")
                .toArray(String[]::new);
        String join = "param error [" + StringUtils.join(result) + "]";
        data.setMessage(join);
        InvocationException state = new InvocationException(javax.ws.rs.core.Response.Status.BAD_REQUEST, data);

        return Response.failResp(state);
    }

    @Override
    public int getOrder()
    {
        return -200;
    }
}
