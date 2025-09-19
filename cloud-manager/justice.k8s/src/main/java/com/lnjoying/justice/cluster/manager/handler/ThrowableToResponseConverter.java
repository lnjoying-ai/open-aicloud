package com.lnjoying.justice.cluster.manager.handler;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.exception.ExceptionToProducerResponseConverter;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLException;
import java.util.Optional;

import static com.lnjoying.justice.schema.common.ErrorCode.*;


/**
 * handle Throwable and log
 *
 * @author merak
 **/

@Slf4j
public class ThrowableToResponseConverter implements
        ExceptionToProducerResponseConverter<Throwable>
{
    @Override
    public Class<Throwable> getExceptionClass()
    {
        return Throwable.class;
    }

    @Override
    public Response convert(SwaggerInvocation swaggerInvocation, Throwable e)
    {
        String summary = ((Invocation) swaggerInvocation).getOperationMeta().getSwaggerOperation().getSummary();
        log.error(summary + " error: {}", e);

        ErrorData data = new ErrorData();
        if (e instanceof SQLException || e instanceof DataAccessException)
        {
            if (e instanceof DuplicateKeyException)
            {
                data.setCode(DATA_DUP.getCode());
                data.setMessage(DATA_DUP.getMessage());
                InvocationException state = new InvocationException(WebSystemException.getHeadStatus(SQL_ERROR), data);
                return Response.failResp(state);
            }
            data.setCode(SQL_ERROR.getCode());
            data.setMessage(SQL_ERROR.getMessage());
            InvocationException state = new InvocationException(WebSystemException.getHeadStatus(SQL_ERROR), data);
            return Response.failResp(state);
        }

        if (e instanceof TaskRejectedException)
        {
            data.setCode(TASK_EXECUTE_EXCEPTION.getCode());
            data.setMessage(TASK_EXECUTE_EXCEPTION.getMessage());
            InvocationException state = new InvocationException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR, data);
            return Response.failResp(state);
        }

        if (e instanceof HttpClientErrorException)
        {
            data.setCode(REGISTRY_HTTP_REQUEST_EXCEPTION.getCode());
            data.setMessage(REGISTRY_HTTP_REQUEST_EXCEPTION.getMessage());
            InvocationException state = new InvocationException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR, data);
            return Response.failResp(state);
        }
        data.setCode(SystemError.getCode());
        data.setMessage(Optional.ofNullable(e.getCause()).isPresent() ? e.getCause().getMessage() : e.getMessage());
        InvocationException state = new InvocationException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR, data);
        return Response.failResp(state);
    }

    @Override
    public int getOrder()
    {
        return -50;
    }
}
