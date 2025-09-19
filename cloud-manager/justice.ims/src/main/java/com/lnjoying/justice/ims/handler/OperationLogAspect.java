package com.lnjoying.justice.ims.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lnjoying.justice.ims.db.model.TblOperationLog;
import com.lnjoying.justice.ims.facade.OperationLogFacade;
import com.lnjoying.justice.ims.util.RequestUtils;
import com.lnjoying.justice.ims.util.TimeUtils;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Utils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.apache.servicecomb.common.rest.RestConst.REST_REQUEST;

/**
 * Operation log interception
 *
 * @author merak
 **/

@Aspect
@Component
@Slf4j
public class OperationLogAspect
{
    private static final int MAX_RESPONSE = 100;

    private static final int MAX_REQUEST = 100;

    private static final ObjectMapper objectMapper;

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * start time
     */
    private long startTime;

    /**
     * end time
     */
    private long endTime;

    @Autowired
    private OperationLogFacade operationLogFacade;

    @Pointcut("execution(* *..controller..*.*(..))")
    //@Pointcut("execution(* com.lnjoying.justice.ims.controller.ImsRegistryController.*(..))")
    public void operationLogPointCut()
    {
    }

    @Before("operationLogPointCut()")
    public void doBeforeInServiceLayer(JoinPoint joinPoint)
    {
        if (log.isDebugEnabled())
        {
            log.debug("doBeforeInServiceLayer");
        }
        startTime = System.currentTimeMillis();
    }

    @After("operationLogPointCut()")
    public void doAfterInServiceLayer(JoinPoint joinPoint)
    {
        if (log.isDebugEnabled())
        {
            log.debug("doAfterInServiceLayer");
        }
    }

    @Around("operationLogPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable
    {
        // 获取request
        VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ContextUtils.getInvocationContext()
                .getLocalContext().get(REST_REQUEST);

        TblOperationLog operationLog = new TblOperationLog();
        operationLog.setId(Utils.assignUUId());
        // Obtain the operation name and response result from the swagger annotation
        Object result = pjp.proceed();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class))
        {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            operationLog.setDescription(log.value());
        }

        endTime = System.currentTimeMillis();
        long spendTime = endTime - startTime;
        if (log.isDebugEnabled())
        {
            log.debug("doAround>>>result={},耗时：{}", result, spendTime);
        }

        operationLog.setIp(RequestUtils.getIpAddr(request));
        operationLog.setMethod(request.getMethod());

        // Request parameter
        if (RequestMethod.GET.name().equalsIgnoreCase(request.getMethod()))
        {
            operationLog.setParameter(request.getQueryString());
        }
        else
        {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String strRequest = objectMapper.writeValueAsString(parameterMap);
            if (strRequest.length() > MAX_REQUEST)
            {
                operationLog.setParameter(strRequest.substring(0, MAX_REQUEST) + "...");
            }
            else
            {
                operationLog.setParameter(strRequest);
            }
        }

        // Request result
        String strResult = objectMapper.writeValueAsString(result);
        if (strResult.length() > MAX_RESPONSE)
        {
            operationLog.setResult(strResult.substring(0, MAX_RESPONSE) + "...");
        }
        else
        {
            operationLog.setResult(strResult);
        }
        operationLog.setSpendTime((int) spendTime);
        operationLog.setStartTime(TimeUtils.millsToLocalDateTime(startTime));

        operationLog.setUri(request.getRequestURI());
        operationLog.setUserAgent(request.getHeader("User-Agent"));

        Map<String, Object> swaggerArguments = ((Invocation) ContextUtils.getInvocationContext()).getSwaggerArguments();
        if (!CollectionUtils.isEmpty(swaggerArguments))
        {
            String userId = (String) swaggerArguments.get(UserHeadInfo.USERID);
            String userName = (String) swaggerArguments.get(UserHeadInfo.USERNAME);
            operationLog.setUserId(userId);
            operationLog.setUserName(userName);
        }
        operationLogFacade.insert(operationLog);
        return result;
    }
}
