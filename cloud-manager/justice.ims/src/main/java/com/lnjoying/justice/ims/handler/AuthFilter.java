package com.lnjoying.justice.ims.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorData;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.RoleConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Utils;
import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.servicecomb.common.rest.codec.produce.ProduceProcessor;
import org.apache.servicecomb.common.rest.filter.HttpServerFilter;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.HttpServletRequestEx;
import org.apache.servicecomb.foundation.vertx.http.HttpServletResponseEx;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.exception.CommonExceptionData;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

import java.util.Objects;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.schema.common.ErrorCode.ILLEGAL_ARGUMENT;
import static com.lnjoying.justice.schema.common.ErrorCode.PARAMETER_IS_NOT_VALID_FOR_OPERATION;
import static com.lnjoying.justice.schema.constant.RoleConstants.*;

/**
 * Used for basic autiorities filtering
 *
 * @author merak
 **/

@Slf4j
public class AuthFilter implements HttpServerFilter
{
    private static ObjectMapper objectMapper;

    private static final String CONTENT_TYPE_JSON = "application/json";

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Override
    public int getOrder()
    {
        return 0;
    }

    @Override
    public Response afterReceiveRequest(Invocation invocation, HttpServletRequestEx requestEx)
    {
        String requestURI = requestEx.getRequestURI();

        String health_check_api = String.format("/%s/v1/health", invocation.getMicroserviceName());
        if (health_check_api.equals(requestURI))
        {
            return null;
        }

        if (StringUtils.isNotBlank(requestURI)
                && requestURI.length() >= 1
                && requestURI.substring(1).startsWith(invocation.getMicroserviceName()))
        {
            String userId = requestEx.getHeader(UserHeadInfo.USERID);
            String bpId = requestEx.getHeader(UserHeadInfo.BPID);
            String authorities = requestEx.getHeader(UserHeadInfo.AUTIORITIES);
            String kind = requestEx.getHeader(UserHeadInfo.USERKIND);

            boolean noAuthorities = !isSystemUser(kind);
            if (noAuthorities || StringUtils.isBlank(userId))
            {
                ErrorData errorData = new ErrorData();
                errorData.setCode(ErrorCode.User_Not_Grant.getCode());
                errorData.setMessage(ErrorCode.User_Not_Grant.getMessage());
                return Response.create(javax.ws.rs.core.Response.Status.UNAUTHORIZED, errorData);
            }

            // role of tenant_admin must have bpId
            if (isBpAdmin(kind) || isBpUser(kind))
            {
                if (StringUtils.isBlank(bpId))
                {
                    ErrorData errorData = new ErrorData();
                    errorData.setCode(ErrorCode.BP_ID_NOT_FOUND.getCode());
                    errorData.setMessage(ErrorCode.BP_ID_NOT_FOUND.getMessage());
                    return Response.create(javax.ws.rs.core.Response.Status.UNAUTHORIZED, errorData);
                }
            }

        }
        return null;
    }

    /**
     * Convert the content-type of ‘application/json’ from camel case to snake case
     *
     * @param invocation
     * @param responseEx
     */
    @Override
    public void beforeSendResponse(Invocation invocation, HttpServletResponseEx responseEx)
    {
        RpcSchema annotation = invocation.getInvocationRuntimeType().getAssociatedClass().getAnnotation(RpcSchema.class);
        // skip classes annotated by rpcschema
        if (Objects.isNull(annotation))
        {
            Response response = (Response) responseEx.getAttribute("servicecomb-invocation-hanlder-response");
            ProduceProcessor produceProcessor = (ProduceProcessor) responseEx.getAttribute("servicecomb-invocation-hanlder-processor");
            Object body = response.getResult();
            if (response.isSucceed())
            {
                String contentType = produceProcessor.getName();
                String contentTypeFromHeader = responseEx.getHeader("content-type");
                if (CONTENT_TYPE_JSON.equalsIgnoreCase(contentType)
                        && (Strings.isNotBlank(contentTypeFromHeader) && contentTypeFromHeader.contains(CONTENT_TYPE_JSON)))
                {
                    try
                    {
                        responseEx.setBodyBuffer(Buffer.buffer((objectMapper.writeValueAsString(body))));
                    }
                    catch (JsonProcessingException e)
                    {
                        log.error("encode json failed:{}", e);
                        String errorMsg = "{\"code\":9300,\"message\":\"Error parsing json\"}";
                        responseEx.setBodyBuffer(Buffer.buffer(errorMsg));
                    }
                }
            }

            if (response.isFailed())
            {
                if (body instanceof InvocationException)
                {
                    Object errorData = ((InvocationException) body).getErrorData();
                    if (errorData instanceof CommonExceptionData)
                    {
                        String message = ((CommonExceptionData) errorData).getMessage();
                        ErrorData error = new ErrorData();
                        try
                        {
                            if (StringUtils.isNotBlank(message) && message.contains(PARAMETER_IS_NOT_VALID_FOR_OPERATION.getMessage()))
                            {
                                error.setMessage(PARAMETER_IS_NOT_VALID_FOR_OPERATION.getMessage());
                                error.setCode(PARAMETER_IS_NOT_VALID_FOR_OPERATION.getCode());
                                responseEx.setBodyBuffer(Buffer.buffer((objectMapper.writeValueAsString(error))));
                            }
                        }
                        catch (JsonProcessingException e)
                        {
                            log.error("encode json failed:{}", e);
                            String errorMsg = "{\"code\":9300,\"message\":\"Error parsing json\"}";
                            responseEx.setBodyBuffer(Buffer.buffer(errorMsg));
                        }
                    }
                }
            }
        }
    }
}
