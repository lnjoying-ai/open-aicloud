package com.lnjoying.justice.commonweb.handler.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lnjoying.justice.commonweb.handler.aspect.model.SensitiveFieldDefinition;
import com.lnjoying.justice.commonweb.handler.aspect.rpcproxy.RpcServiceProxy;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.FunctionRegisterConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceNameFromRpcServiceConfig;
import com.lnjoying.justice.commonweb.handler.aspect.aspectbase.AnnotationAspectBase;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.commonweb.handler.aspect.util.SensitiveFieldScanner;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.SensitiveInfoProcess;
import com.lnjoying.justice.commonweb.handler.sensitiveinfo.SensitiveInfoProcessFactory;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.ActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.i18n.zh_cn.SimpleActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.mapper.CommonResourceCache;
import com.lnjoying.justice.commonweb.process.service.OperationLogOrEventSendProcessStrategy;
import com.lnjoying.justice.commonweb.swagger.ScbSchemaUtils;
import com.lnjoying.justice.commonweb.util.*;
import com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.service.omc.OmcService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.servicecomb.foundation.vertx.http.BodyBufferSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import static com.lnjoying.justice.schema.constant.ResourceOperationTypeConstants.*;

/**
 * 接口日志切面类
 *
 * @author: Robin
 * @date: 2023年10月13日 10:58
 */
@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class LNJoyAuditLogAspect extends AnnotationAspectBase
{
    public LNJoyAuditLogAspect()
    {
        super.setAnnotationName("LNJoyAuditLog");
    }

    @Autowired
    private OperationLogOrEventSendProcessStrategy logOrEventSendProcessStrategy;

    @Autowired
    private CommonResourceCache generalResourceSearcher;

    @Autowired
    private RpcServiceProxy rpcServiceProxy;

    private static final int OUT_PARAM_MAX_LEN = 500;

    //    @Pointcut("@annotation(com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog) || @annotation(io.swagger.annotations.ApiOperation)")
    @Pointcut("@annotation(com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog)")
    public void pc()
    {

    }

    @Around("pc()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LNJoyAuditLog lnJoyAuditLog = null;
        if (methodSignature.getMethod().isAnnotationPresent(LNJoyAuditLog.class))
        {
            lnJoyAuditLog = methodSignature.getMethod().getAnnotation(LNJoyAuditLog.class);
            if (lnJoyAuditLog.logOff())
            {
                //不记录日志, 直接返回
                return joinPoint.proceed();
            }
        }

        // 暂时停止对所有GET请求的日志记录(根据2024.01.10会议讨论)
        if (isHttpGetRequest())
        {
            //不记录日志, 直接返回
            return joinPoint.proceed();
        }

        OmcService.OperationLog.OperationLogBuilder logBuilder = OmcService.OperationLog.builder()
                .id(Utils.assignUUId())
                .triggerTime(new Date());

        //构建上下文数据(1.queryString数据,2.路径变量数据,3.请求体数据 → 方法参数列表数据)
        Map<String, Object> contextData = new HashMap<>();
        //从请求报文中提取日志信息
        extractLogDataFromRequest(joinPoint, contextData, logBuilder);
        String operationType = null;
        if (lnJoyAuditLog != null)
        {
            //识别操作行为
            operationType = getOperationType(lnJoyAuditLog, contextData);
            contextData.put(ActionDescriptionContextFields.OPERATION_TYPE, operationType);
            //提取资源id
            String resourceId = findResourceId(joinPoint, lnJoyAuditLog.resourceIdExtractConfig());
            if (StringUtils.hasText(resourceId))
            {
                contextData.putIfAbsent(ActionDescriptionContextFields.RESOURCE_INSTANCE_ID, resourceId);
            }
            //提取资源池id
            String resourcePoolId = findResourcePoolId(joinPoint, lnJoyAuditLog.resourceIdExtractConfig());
            if (StringUtils.hasText(resourcePoolId))
            {
                contextData.putIfAbsent(ActionDescriptionContextFields.RESOURCE_POOL_ID, resourcePoolId);
            }
            if (!RESOURCE_CREATE.equals(operationType))
            {
                //更新 或 删除行为
                Optional<String> actionDescription = getFriendlyActionDescription(joinPoint, lnJoyAuditLog, contextData,
                        StringUtils.hasText(lnJoyAuditLog.associatedResourceCnName())
                                ? lnJoyAuditLog.associatedResourceCnName()
                                : getResourceCnName(joinPoint),
                        resourceId);
                if (actionDescription.isPresent())
                {
                    logBuilder.description(actionDescription.get());
                } else
                {
                    //没有合适的描述模板，直接返回
                    return joinPoint.proceed();
                }
            }
            logBuilder.resourceId(resourceId);
            //提取操作类别
            logBuilder.operationType(operationType);
        }

        Object targetMethodResult = null;
        Throwable targetMethodException = null;
        StopWatch stopWatch = new StopWatch();
        try
        {
            //调用目标方法
            stopWatch.start();
            try
            {
                targetMethodResult = joinPoint.proceed();
            } catch (Throwable e)
            {
                targetMethodException = e;
                logBuilder.level(LogLevel.ERROR.name())
                        .result(Optional.ofNullable(e.getMessage())
                                .map(x -> x.length() > 64
                                        ? x.substring(0, 64)
                                        : x
                                ).orElse(""));
            }
            if (stopWatch.isRunning())
            {
                stopWatch.stop();
            }
            if (targetMethodResult != null)
            {
                String outParamsText = null;
                if (targetMethodResult instanceof ResponseEntity)
                {
                    Object responseBody = ((ResponseEntity) targetMethodResult).getBody();
                    if (responseBody != null)
                    {
                        outParamsText = serializeOutputParameterAsJson(joinPoint, responseBody);
                    }
                    String statusCode = String.valueOf(((ResponseEntity) targetMethodResult).getStatusCodeValue());
                    contextData.put(ActionDescriptionContextFields.HTTP_STATUS_CODE, statusCode);
                } else
                {
                    outParamsText = serializeOutputParameterAsJson(joinPoint, targetMethodResult);
                }

                if (outParamsText != null)
                {
                    if (outParamsText.length() > OUT_PARAM_MAX_LEN)
                    {
                        logBuilder.outParams(outParamsText.substring(0, OUT_PARAM_MAX_LEN) + "...");
                    } else
                    {
                        logBuilder.outParams(outParamsText);
                    }
                }
            }

            //对从方法出参中提取资源id提供支持(创建行为)
            if (lnJoyAuditLog != null)
            {
                String resourceId = Optional.ofNullable(contextData.get(ActionDescriptionContextFields.RESOURCE_INSTANCE_ID))
                        .map(x -> x.toString()).orElse(null);
                boolean createResourceFlag1Configured = ResourceIdLocationType.METHOD_RETURN_VALUE.equals(lnJoyAuditLog.resourceIdExtractConfig().resourceIdLocation());
                boolean createResourceFlag2Configured = RESOURCE_CREATE.equals(operationType);
                boolean isCreateAction = createResourceFlag1Configured || createResourceFlag2Configured;
                if (isCreateAction)
                {
                    if (createResourceFlag1Configured)
                    {
                        log.debug("Extract resource id from the return value of method...");
                        if (resourceId == null)
                        {
                            resourceId = getResourceIdFromMethodReturnValue(joinPoint, lnJoyAuditLog.resourceIdExtractConfig(), targetMethodResult);
                        }
                        logBuilder.resourceId(resourceId);
                    }

                    //创建行为
                    Optional<String> actionDescription = getFriendlyActionDescription(joinPoint, lnJoyAuditLog, contextData,
                            StringUtils.hasText(lnJoyAuditLog.associatedResourceCnName())
                                    ? lnJoyAuditLog.associatedResourceCnName()
                                    : getResourceCnName(joinPoint),
                            resourceId);
                    if (actionDescription.isPresent())
                    {
                        logBuilder.description(actionDescription.get());
                    } else
                    {
                        //没有合适的描述模板，直接返回
                        return targetMethodResult;
                    }
                }
            }
        } catch (Throwable e)
        {
            targetMethodException = e;
            logBuilder.level(LogLevel.ERROR.name())
                    .result(Optional.ofNullable(e.getMessage())
                            .map(x -> x.length() > 64
                                    ? x.substring(0, 64)
                                    : x
                            ).orElse(""));
            log.error("记录接口日志时出错, stackTrace: {}, errorMessage: {}", ExceptionUtils.getStackTrace(e), e.getMessage());
        } finally
        {
            writeLogToOMC(logBuilder, stopWatch);
        }

        if (targetMethodException != null)
        {
            throw targetMethodException;
        }
        return targetMethodResult;
    }

    private Optional<String> getFriendlyActionDescription(ProceedingJoinPoint joinPoint, LNJoyAuditLog lnJoyAuditLog, Map<String, Object> contextData, String resourceCnName, String resourceId)
    {
        //模板数据
        contextData.put(ActionDescriptionContextFields.RESOURCE_NAME, resourceCnName);
        if (StringUtils.hasText(resourceId))
        {
            contextData.putIfAbsent(ActionDescriptionContextFields.RESOURCE_INSTANCE_ID, resourceId);
        }

        if (lnJoyAuditLog.isResourceInstanceNameFromArgs()
                && StringUtils.hasText(lnJoyAuditLog.resourceInstanceNameFromArgsParseSpEl()))
        {
            //从入参中读取资源实例名称
            extractResourceInstanceNameFromArgs(lnJoyAuditLog, contextData);
        } else if (lnJoyAuditLog.isResourceInstanceNameFromRpcService())
        {
            extractResourceInstanceNameFromRpcService(lnJoyAuditLog, resourceId, contextData);
        } else
        {
            if (StringUtils.hasText(resourceId))
            {
                //根据资源id从db中获取实例名称
                String resourceProviderId = getResourceMapperId(joinPoint, lnJoyAuditLog);
                if (StringUtils.hasText(resourceProviderId))
                {
                    Object primaryKeyId = null;
                    if (lnJoyAuditLog.resourceIdExtractConfig().resourcePrimaryKeyClass() != String.class)
                    {
                        if (!StringUtils.hasText(lnJoyAuditLog.resourceIdExtractConfig().convertToResourcePrimaryKeyTypeSpEl()))
                        {
                            log.debug("convertToResourcePrimaryKeyTypeSpEl is not set");
                            return Optional.empty();
                        }

                        if (contextData.containsKey(ActionDescriptionContextFields.RESOURCE_POOL_ID))
                        {
                            String resourcePoolId = Optional.ofNullable(contextData.get(ActionDescriptionContextFields.RESOURCE_POOL_ID))
                                    .map(x -> x.toString()).orElse(null);
                            primaryKeyId = convertToPrimaryKeyTypeValue(lnJoyAuditLog, resourcePoolId, resourceId);
                        }
                        else
                        {
                            primaryKeyId = convertToPrimaryKeyTypeValue(lnJoyAuditLog, null, resourceId);
                        }
                        if (primaryKeyId == null)
                        {
                            log.debug("convertToPrimaryKeyTypeValue failed");
                            return Optional.empty();
                        }
                    } else
                    {
                        primaryKeyId = resourceId;
                    }

                    Optional<Object> resourceInstance = generalResourceSearcher.getResourceInstance(resourceProviderId, "selectByPrimaryKey", primaryKeyId);
                    if (!resourceInstance.isPresent())
                    {
                        log.error("fetch resource instance failed! resourceEnName:{}, resourceId:{}", resourceCnName, resourceId);
                        return Optional.empty();
                    }
                    //装载资源实例
                    contextData.put(ActionDescriptionContextFields.RESOURCE_INSTANCE, resourceInstance.get());

                    if (lnJoyAuditLog.hasResourceInstanceName())
                    {
                        //获取资源实例名称
                        Optional<Object> resourceInstanceNameOptional = ReflectionUtil.getFieldValueByAnnotation(resourceInstance.get(), ResourceInstanceName.class);
                        if (!resourceInstanceNameOptional.isPresent())
                        {
                            log.warn("resource instance name cannot be found! resourceClass:{}", resourceInstance.get().getClass().getName());
                            //return Optional.empty();
                        }
                        else
                        {
                            contextData.put(ActionDescriptionContextFields.RESOURCE_INSTANCE_NAME, resourceInstanceNameOptional.get().toString());
                        }

                    }
                }
            }
        }

        ActionDescriptionTemplate actionDescriptionTemplate;
        String templateContent = null;
        boolean useDefaultTemplateClass = false;
        if (StringUtils.hasText(lnJoyAuditLog.actionDescriptionTemplate()))
        {
            //使用自定义的模板字符串
            templateContent = lnJoyAuditLog.actionDescriptionTemplate();
        } else if (!Objects.equals(lnJoyAuditLog.actionDescriptionTemplateClass(), SimpleActionDescriptionTemplate.class))
        {
            try
            {
                //使用自定义的模板类
                actionDescriptionTemplate = lnJoyAuditLog.actionDescriptionTemplateClass().newInstance();
                actionDescriptionTemplate.setContextData(contextData);
                Optional<String> descriptionTemplate = actionDescriptionTemplate.getDescriptionTemplate();
                if (descriptionTemplate.isPresent())
                {
                    templateContent = descriptionTemplate.get();
                }
            } catch (Exception e)
            {
                log.error("actionDescriptionTemplateClass config error, stackTrace:{}, errorMessage:{}",
                        ExceptionUtils.getStackTrace(e), e.getMessage());
                return Optional.empty();
            }
        } else
        {
            //使用默认模板
            actionDescriptionTemplate = new SimpleActionDescriptionTemplate();
            actionDescriptionTemplate.setContextData(contextData);
            Optional<String> descriptionTemplate = actionDescriptionTemplate.getDescriptionTemplate();
            if (descriptionTemplate.isPresent())
            {
                templateContent = descriptionTemplate.get();
            }
            useDefaultTemplateClass = true;
        }

        try
        {

            if (useDefaultTemplateClass && StringUtils.isEmpty(contextData.get(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME)))
            {
                //如使用默认模板，但没提供资源实例名称，说明没配置好模板需要的所有必要参数，为避免日志内容可读性太差，关闭该日志记录
                return Optional.empty();
            }

            if (!StringUtils.hasText(templateContent))
            {
                log.debug("no suitable template content, stop logging.");
                return Optional.empty();
            }

            //渲染模板
            return Optional.of(TemplateEngineUtils.render0(templateContent, contextData));
        } catch (Exception e)
        {
            log.error("api log description render failed, stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }

        return Optional.empty();
    }

    private static Object convertToPrimaryKeyTypeValue(LNJoyAuditLog lnJoyAuditLog, String resourcePoolId, String resourceId)
    {
        Object primaryKeyId = null;
        try
        {
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_ID, resourceId);
            context.setVariable(ActionDescriptionTemplateFields.RESOURCE_POOL_ID, resourcePoolId);
            ExpressionParser parser = new SpelExpressionParser();
            primaryKeyId = parser.parseExpression(lnJoyAuditLog.resourceIdExtractConfig().convertToResourcePrimaryKeyTypeSpEl()).getValue(context, lnJoyAuditLog.resourceIdExtractConfig().resourcePrimaryKeyClass());
        } catch (Throwable e)
        {
            log.error("convert resourceId to its primary key type failed! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }

        return primaryKeyId;
    }

    private void extractResourceInstanceNameFromRpcService(LNJoyAuditLog lnJoyAuditLog, String resourceId, Map<String, Object> contextData)
    {
        try
        {
            ResourceInstanceNameFromRpcServiceConfig config = lnJoyAuditLog.resourceInstanceNameFromRpcServiceConfig();
            Optional<Object> rpcResult = rpcServiceProxy.invoke(config, new Object[]{resourceId});
            if (rpcResult.isPresent())
            {
                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setVariable("obj", rpcResult.get());
                String resourceInstanceName = new SpelExpressionParser().parseExpression(lnJoyAuditLog.resourceInstanceNameFromRpcServiceParseSpEl())
                        .getValue(context, String.class);
                contextData.put(ActionDescriptionContextFields.RESOURCE_INSTANCE_NAME, resourceInstanceName);
            }
        } catch (Exception e)
        {
            log.error("从rpc提取资源实例名称失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private static void extractResourceInstanceNameFromArgs(LNJoyAuditLog lnJoyAuditLog, Map<String, Object> contextData)
    {
        try
        {
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable("obj", contextData);
            String resourceInstanceName = new SpelExpressionParser().parseExpression(lnJoyAuditLog.resourceInstanceNameFromArgsParseSpEl())
                    .getValue(context, String.class);
            contextData.put(ActionDescriptionContextFields.RESOURCE_INSTANCE_NAME, resourceInstanceName);
        } catch (Exception e)
        {
            log.error("使用自定义表达式提取资源实例名称失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private String getResourceMapperId(ProceedingJoinPoint joinPoint, LNJoyAuditLog lnJoyAuditLog)
    {
        String resourceMapperId;
        if (StringUtils.hasText(lnJoyAuditLog.resourceMapperId()))
        {
            //优先使用注解指定的
            resourceMapperId = lnJoyAuditLog.resourceMapperId();
        } else
        {
            //注解上没有指定则使用Controller上的资源名称
            resourceMapperId = getResourceEnName(joinPoint);
        }
        return resourceMapperId;
    }

    private String serializeOutputParameterAsJson(ProceedingJoinPoint joinPoint, Object obj)
    {
        String methodFullName = String.format("%s.%s", joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName());
        try
        {
            try
            {
                return JsonUtils.toJson(obj);
            } catch (Exception e)
            {
                //fallback to JacksonUtils
                log.warn("使用JsonUtils序列化 {} 出错, 已自动降级为JacksonUtils序列化! errorMessage: {}", methodFullName, e.getMessage());
                return JacksonUtils.objToStr(obj);
            }
        } catch (Exception e)
        {
            log.error("序列化 {} 出参时出错! stackTrace: {} errorMessage: {}", methodFullName,
                    ExceptionUtils.getStackTrace(e), e.getMessage());
            return e.getMessage();
        }
    }

    private void writeLogToOMC(OmcService.OperationLog.OperationLogBuilder logBuilder, StopWatch stopWatch)
    {
        try
        {
            OmcService.OperationLog operationLog = logBuilder.build();
            if (!StringUtils.hasText(operationLog.getDescription()))
            {
                log.debug("description is empty, discard it");
                return;
            }
            logBuilder.executionTime((int) stopWatch.getTotalTimeMillis());
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType("operationLog");
            messagePack.setMessageObj(operationLog);
            logOrEventSendProcessStrategy.sendMessage(messagePack);
        } catch (Throwable e)
        {
            log.error("api日志切面类保存日志失败: {}", e.getMessage());
        }
    }

    private void extractLogDataFromRequest(ProceedingJoinPoint joinPoint, Map<String, Object> contextData, OmcService.OperationLog.OperationLogBuilder logBuilder)
    {
        try
        {
            contextData.put(ActionDescriptionContextFields.USER_ID, getUserId().orElse(""));
            contextData.put(ActionDescriptionContextFields.USER_NAME, getUserName().orElse(""));
            // 获取request
            HttpServletRequest request = getHttpServletRequest();

            contextData.put(ActionDescriptionContextFields.HTTP_METHOD, request.getMethod());
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            //获取资源名称
            String resourceName = getResourceEnName(joinPoint);
            logBuilder.level(LogLevel.INFO.name())
                    .bpId(request.getHeader(UserHeadInfo.BPID))
                    .service(ScbSchemaUtils.getMicrosoftServiceName())//服务名称
                    .resource(resourceName)
                    .action(signature.getName())
                    .accessIp(RequestUtils.getIpAddr(request))
                    .userAgent(request.getHeader(UserHeadInfo.USER_AGENT))
                    .httpMethod(request.getMethod())
                    .requestPath(StringUtils.isEmpty(request.getQueryString()) ? request.getRequestURI()
                            : request.getRequestURI() + "?" + request.getQueryString())
                    .userId(getUserId().orElse(""))
                    .operator(getUserName().orElse(""));
            if (RequestMethod.GET.name().equalsIgnoreCase(request.getMethod()))
            {
                logBuilder.inParams(request.getQueryString());
            } else
            {
                String requestBody = null;
                //表单形式提交的参数提取
                if (request.getParameterMap() != null && request.getParameterMap().size() > 0)
                {
                    Map<String, Object> inParamMap = flattenRequestParameterMap(request.getParameterMap());
                    //找到对应的模型类
                    Object requestBodyObject = getRequestBodyObject(joinPoint);
                    Map<String, SensitiveFieldDefinition> sensitiveFields = SensitiveFieldScanner.findSensitiveFields(requestBodyObject);
                    if (CollectionUtils.isEmpty(sensitiveFields))
                    {
                        //无敏感信息
                        contextData.putAll(inParamMap);
                        requestBody = JsonUtils.toJsonWithSnakeCase(inParamMap);
                    } else
                    {
                        //有敏感信息(进行数据脱敏)
                        JsonElement jsonElement = dataMasking(JsonUtils.toJsonWithSnakeCase(inParamMap), requestBodyObject, sensitiveFields);
                        requestBody = JsonUtils.toJsonWithSnakeCase(jsonElement);
                        Map maskedRequestBodyMap = JsonUtils.fromJsonWithSnakeCase(requestBody, Map.class);
                        contextData.putAll(maskedRequestBodyMap);
                    }
                }

                if (request instanceof BodyBufferSupport)
                {
                    BodyBufferSupport bps = (BodyBufferSupport) request;
                    if (bps.getBodyBytesLength() > 0)
                    {
                        String contentType = request.getContentType();
                        if (!StringUtils.isEmpty(contentType) &&
                                contentType.contains(MediaType.APPLICATION_JSON_VALUE))
                        {
                            //json格式提交的参数提取
                            byte[] requestBodyBytes = Arrays.copyOfRange(bps.getBodyBytes(), 0, bps.getBodyBytesLength());
                            requestBody = new String(requestBodyBytes);

                            //找到对应的模型类
                            Object requestBodyObject = getRequestBodyObject(joinPoint);
                            Map<String, SensitiveFieldDefinition> sensitiveFields = Collections.emptyMap();
                            if (requestBodyObject instanceof List)
                            {
                                List requestBodyObjList = (List) requestBodyObject;
                                if (!CollectionUtils.isEmpty(requestBodyObjList))
                                {
                                    Object requestBodyObjectListItem = requestBodyObjList.get(0);
                                    sensitiveFields = SensitiveFieldScanner.findSensitiveFields(requestBodyObjectListItem);
                                }
                            } else
                            {
                                sensitiveFields = SensitiveFieldScanner.findSensitiveFields(requestBodyObject);
                            }

                            if (CollectionUtils.isEmpty(sensitiveFields))
                            {
                                //无敏感信息
                                loadRequestBodyDataIntoContextMap(requestBodyObject, contextData, requestBody);
                            } else
                            {
                                //有敏感信息(进行数据脱敏)
                                JsonElement jsonElement = dataMasking(requestBody, requestBodyObject, sensitiveFields);
                                requestBody = JsonUtils.toJsonWithSnakeCase(jsonElement);
                                Object maskedRequestBodyObject = JsonUtils.fromJsonWithSnakeCase(requestBody, requestBodyObject.getClass());
                                loadRequestBodyDataIntoContextMap(maskedRequestBodyObject, contextData, requestBody);
                            }
                        }
                    }
                }

                logBuilder.inParams(requestBody);
            }
            logBuilder.result("ok");
        } catch (Throwable e)
        {
            log.error("提取api请求数据时出错! error: {}", e.getMessage());
        }
    }

    private static JsonElement dataMasking(String requestBody, Object requestBodyObject, Map<String, SensitiveFieldDefinition> sensitiveFields)
    {
        JsonElement jsonElement = JsonUtils.fromJsonWithSnakeCase(requestBody, JsonElement.class);
        if (jsonElement instanceof JsonArray)
        {
            JsonArray jsonEntryArr = (JsonArray) jsonElement;
            int requestBodyObjectIndex = 1;
            if (CollectionUtils.isEmpty(sensitiveFields))
            {
                requestBodyObjectIndex = 0;
            }
            List requestBodyObjectList = (List) requestBodyObject;
            dataMaskingImpl(requestBodyObjectList.get(0), sensitiveFields, jsonEntryArr.get(0));
            int requestBodyObjectCount = requestBodyObjectList.size();
            for (int i = requestBodyObjectIndex; i < requestBodyObjectCount; i++)
            {
                sensitiveFields = SensitiveFieldScanner.findSensitiveFields(requestBodyObjectList.get(i));
                dataMaskingImpl(requestBodyObjectList.get(i), sensitiveFields, jsonEntryArr.get(i));
            }
        } else
        {
            dataMaskingImpl(requestBodyObject, sensitiveFields, jsonElement);
        }

        return jsonElement;
    }

    private static void dataMaskingImpl(Object requestBodyObject, Map<String, SensitiveFieldDefinition> sensitiveFields, JsonElement jsonElement)
    {
        for (Map.Entry<String, SensitiveFieldDefinition> sensitiveEntry : sensitiveFields.entrySet())
        {
            List<String> fieldPathList = FieldPathResolverUtil.getFieldPath(requestBodyObject.getClass(), sensitiveEntry.getKey());
            SensitiveFieldDefinition sensitiveEntryDefinition = sensitiveEntry.getValue();
            Optional<SensitiveInfoProcess> processor = SensitiveInfoProcessFactory.getProcessor(sensitiveEntryDefinition.getSensitiveType());
            if (!processor.isPresent())
            {
                //未定义敏感信息处理类
                log.error("there is no sensitive info processor defined for '{}' type", sensitiveEntryDefinition.getSensitiveType().name());
                //替换为*!
                JsonElement newValue = JsonUtils.toJsonTreeWithSnakeCase("*n*p*!");
                JsonUtils.replaceValue(jsonElement, fieldPathList.toArray(new String[fieldPathList.size()]), newValue);
            } else
            {
                String maskedValue = processor.get().process(sensitiveEntryDefinition.getFieldValue());
                JsonElement newValue = JsonUtils.toJsonTreeWithSnakeCase(maskedValue);
                JsonUtils.replaceValue(jsonElement, fieldPathList.toArray(new String[fieldPathList.size()]), newValue);
            }
        }
    }

    private void loadRequestBodyDataIntoContextMap(Object requestBodyObj, Map<String, Object> contextData, String requestBody) throws IOException
    {
        if (StringUtils.isEmpty(requestBody))
        {
            return;
        }

        try
        {
            if (requestBodyObj != null && !(requestBodyObj instanceof List))
            {
                Map<String, Object> requestBodyMap = BeanMapTool.beanToMap(requestBodyObj);
                contextData.putAll(requestBodyMap);
            }
        } catch (Exception e)
        {

        }

        //记录原始入参信息
        Object originalInputParameter;
        if (requestBody.startsWith("["))
        {
            originalInputParameter = JacksonUtils.strToObjType(requestBody, new TypeReference<List<Object>>()
            {
            });
        } else
        {
            Map<String, Object> originalRequestBodyMap = JacksonUtils.strToObjType(requestBody, new TypeReference<Map<String, Object>>()
            {
            });
            HashMap<String, Object> originalInputParameterMap = new HashMap<>();
            originalInputParameterMap.putAll(originalRequestBodyMap);
            originalInputParameter = originalInputParameterMap;
        }
        contextData.put(ActionDescriptionContextFields.RAW_INPUT_PARAMETER_MAP, originalInputParameter);
    }

    private static Map<String, Object> flattenRequestParameterMap(Map<String, String[]> parameterMap)
    {
        Map<String, Object> inParamMap = new HashMap<>();
        if (CollectionUtils.isEmpty(parameterMap))
        {
            return inParamMap;
        }
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet())
        {
            if (entry.getValue() != null)
            {
                if (entry.getValue().length == 1)
                {
                    inParamMap.put(entry.getKey(), entry.getValue()[0]);
                } else
                {
                    inParamMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return inParamMap;
    }

    private String getMethodDescription(ProceedingJoinPoint joinPoint, LNJoyAuditLog lnJoyAuditLog)
    {
        try
        {
            //未定义action描述规则
            if (StringUtils.isEmpty(lnJoyAuditLog.actionDescriptionField()))
            {
                //返回默认描述
                return lnJoyAuditLog.value();
            }

            return getDescriptionByRule(joinPoint, lnJoyAuditLog);
        } catch (Throwable e)
        {
            log.error("提取api日志描述时出错, error: {}", e.getMessage());
            return lnJoyAuditLog.value();
        }
    }

    private String getDescriptionByRule(ProceedingJoinPoint joinPoint, LNJoyAuditLog lnJoyAuditLog)
    {
        //定义了action描述规则
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames == null)
        {
            //描述字段有误，返回默认描述
            return lnJoyAuditLog.value();
        }
        Object descriptionFieldObj = null;
        for (int i = 0; i < parameterNames.length; i++)
        {
            if (lnJoyAuditLog.actionDescriptionField().equals(parameterNames[i]))
            {
                descriptionFieldObj = joinPoint.getArgs()[i];
                break;
            }
        }
        if (descriptionFieldObj == null)
        {
            //描述字段的值为空，返回默认描述
            return lnJoyAuditLog.value();
        }
        boolean isSpELValid = !StringUtils.isEmpty(lnJoyAuditLog.actionDescriptionValueSpEl());
        boolean isFormatStringValid = !StringUtils.isEmpty(lnJoyAuditLog.actionDescriptionFormatString());
        if (!isSpELValid)
        {
            //未填写字段值提取表达式，尝试直接返回已找到的描述字段的值
            if (descriptionFieldObj instanceof String && !StringUtils.isEmpty(descriptionFieldObj))
            {
                if (isFormatStringValid)
                {
                    return String.format(lnJoyAuditLog.actionDescriptionFormatString(), descriptionFieldObj);
                }
            }

            return lnJoyAuditLog.value();
        }

        //有描述字段、描述值提取表达式
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (lnJoyAuditLog.functionRegisterConfig() != null)
        {
            for (FunctionRegisterConfiguration functionRegisterConfiguration : lnJoyAuditLog.functionRegisterConfig())
            {
                try
                {
                    if (StringUtils.isEmpty(functionRegisterConfiguration.functionId()))
                    {
                        log.debug("functionId is not assigned, skipped.");
                        continue;
                    }
                    if (functionRegisterConfiguration.functionClass() == null)
                    {
                        log.debug("functionClass is not assigned, skipped");
                        continue;
                    }
                    if (StringUtils.isEmpty(functionRegisterConfiguration.functionMethodName()))
                    {
                        log.debug("functionMethodName is not assigned, skipped.");
                        continue;
                    }
                    if (functionRegisterConfiguration.parameterTypes() == null || functionRegisterConfiguration.parameterTypes().length == 0)
                    {
                        log.debug("parameterTypes is not assigned, skipped");
                        continue;
                    }
                    Method targetMethod = functionRegisterConfiguration.functionClass().getDeclaredMethod(functionRegisterConfiguration.functionMethodName(),
                            functionRegisterConfiguration.parameterTypes());
                    targetMethod.setAccessible(true);
                    context.registerFunction(functionRegisterConfiguration.functionId(), targetMethod);
                } catch (Throwable e)
                {
                    log.warn("functionRegisterConfiguration error! error: {}", e.getMessage());
                }
            }
        }

        context.setVariable("obj", descriptionFieldObj);
        String descriptionInField = new SpelExpressionParser().parseExpression(lnJoyAuditLog.actionDescriptionValueSpEl())
                .getValue(context, String.class);
        if (StringUtils.isEmpty(descriptionInField))
        {
            return lnJoyAuditLog.value();
        }

        if (isFormatStringValid)
        {
            return String.format(lnJoyAuditLog.actionDescriptionFormatString(), descriptionInField);
        }

        return lnJoyAuditLog.value();
    }

    @PostConstruct
    public void init()
    {
        System.out.println("LNJoyAuditLogAspect is initialized");
    }

    private boolean isHttpGetRequest()
    {
        try
        {
            // 获取request
            HttpServletRequest request = getHttpServletRequest();
            return "GET".equals(request.getMethod());
        } catch (Exception e)
        {

        }

        return false;
    }

    private String getOperationType(LNJoyAuditLog lnJoyAuditLog, Map<String, Object> contextData)
    {
        try
        {
            if (lnJoyAuditLog.isDynamicOperationType() &&
                    StringUtils.hasText(lnJoyAuditLog.dynamicOperationTypeSpEl()))
            {
                //动态operationType
                try
                {
                    StandardEvaluationContext context = new StandardEvaluationContext();
                    context.setVariable("obj", contextData);
                    String customOperationType = new SpelExpressionParser().parseExpression(lnJoyAuditLog.dynamicOperationTypeSpEl())
                            .getValue(context, String.class);
                    if (StringUtils.hasText(customOperationType))
                    {
                        return customOperationType;
                    }
                } catch (Exception e)
                {
                    log.error("提取自定义的operationType失败! stackTrace:{}, errorMessage:{}",
                            ExceptionUtils.getStackTrace(e), e.getMessage());
                }
            }

            String[] targetMethodTags = lnJoyAuditLog.tags();
            HttpServletRequest request = getHttpServletRequest();
            String httpMethod = request.getMethod();
            //如果是POST请求，且方法上有RESOURCE_CREATION标签
            if (targetMethodTags != null &&
                    RequestMethod.POST.name().equals(httpMethod) &&
                    ArrayUtils.contains(targetMethodTags,
                            RESOURCE_CREATE))
            {
                //标记操作类型为资源创建
                return RESOURCE_CREATE;
            }

            switch (RequestMethod.valueOf(httpMethod))
            {
                case POST:
                case PUT:
                case PATCH:
                    //标记操作类型为资源更新
                    return RESOURCE_UPDATE;
                case DELETE:
                    //标记操作类型为资源删除
                    return RESOURCE_DELETE;
            }
        } catch (Exception e)
        {
            log.error("获取操作资源操作类别时出错！stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }

        //其它操作类型
        return ResourceOperationTypeConstants.RESOURCE_OTHER_OPERATION;
    }

    private static HttpServletRequest getHttpServletRequest()
    {
        // 获取request
        return ServiceCombRequestUtils.getHttpServletRequest();
    }

    protected Optional<String> getUserId()
    {
        String userId = null;
        try
        {
            userId = ServiceCombRequestUtils.getUserId();
        } catch (Exception e)
        {
            try
            {
                userId = (String)ServiceCombRequestUtils.getHttpServletRequest().getAttribute(UserHeadInfo.USERID);
            } catch (Exception ex)
            {

            }
        }

        return Optional.ofNullable(userId);
    }

    protected Optional<String> getUserName()
    {
        String userName = null;
        try
        {
            userName = ServiceCombRequestUtils.getUserName();
        } catch (Exception e)
        {
            try
            {
                userName = (String)ServiceCombRequestUtils.getHttpServletRequest().getAttribute(UserHeadInfo.USERNAME);
            } catch (Exception ex)
            {

            }
        }

        return Optional.ofNullable(userName);
    }
}

