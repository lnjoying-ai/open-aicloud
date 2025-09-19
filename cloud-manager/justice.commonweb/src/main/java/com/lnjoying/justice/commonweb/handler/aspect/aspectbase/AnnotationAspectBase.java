package com.lnjoying.justice.commonweb.handler.aspect.aspectbase;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.util.Optional;


/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月25日 20:10
 */
@Data
@Slf4j
public class AnnotationAspectBase
{
    private String annotationName;

    protected String getResourceEnName(ProceedingJoinPoint joinPoint)
    {
        Class methodOwnerClass = joinPoint.getTarget().getClass();
        return ServiceCombRequestUtils.getResourceEnName(methodOwnerClass);
    }

    protected String getResourceCnName(ProceedingJoinPoint joinPoint)
    {
        Class methodOwnerClass = joinPoint.getTarget().getClass();
        return ServiceCombRequestUtils.getResourceCnName(methodOwnerClass);
    }

    protected String findResourceId(ProceedingJoinPoint joinPoint, ResourceIdExtractConfiguration extractConfiguration)
    {
        try
        {
            if (extractConfiguration.resourceIdLocation() == ResourceIdLocationType.NONE)
            {
                return "";
            }

            switch (extractConfiguration.resourceIdLocation())
            {
                case URL_PATH:
                    //从路径变量中获取
                    return getResourceIdFromAnnotatedParameter(joinPoint, extractConfiguration, PathVariable.class);
                case REQUEST_BODY:
                    return getResourceIdFromAnnotatedParameter(joinPoint, extractConfiguration, RequestBody.class);
                case QUERY_STRING:
                    return getResourceIdFromAnnotatedParameter(joinPoint, extractConfiguration, RequestParam.class);
            }
        } catch (Exception e)
        {
            log.error("提取resourceId时出错! error: {}", e.getMessage());
        }

        return "";
    }

    protected String findResourcePoolId(ProceedingJoinPoint joinPoint, ResourceIdExtractConfiguration extractConfiguration)
    {
        try
        {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
            if (parameterAnnotations == null)
            {
                return "";
            }

            int targetIndex = -1;
            for (int i = 0; i < parameterAnnotations.length; i++)
            {
                Annotation[] parameterAnnotationArr = parameterAnnotations[i];
                if (parameterAnnotationArr == null)
                {
                    continue;
                }
                boolean flag = false;
                for (int j = 0; j < parameterAnnotationArr.length; j++)
                {
                    if (methodSignature.getParameterNames()[i].equals("cloudId"))
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex == -1)
            {
                log.error("提取池id失败");
                return "";
            }

            return Optional.ofNullable(joinPoint.getArgs()[targetIndex]).map(x -> x.toString())
                    .orElse("");
        }
        catch (Exception e)
        {
            log.error("提取resourcePoolId时出错! error: {}", e.getMessage());
        }

        return "";
    }

    protected <T> String getResourceIdFromAnnotatedParameter(ProceedingJoinPoint joinPoint,
                                                             ResourceIdExtractConfiguration extractConfiguration,
                                                             Class<T> annotationClass)
    {
        if (annotationClass == null)
        {
            return "";
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        if (parameterAnnotations == null)
        {
            return "";
        }

        int targetIndex = -1;
        for (int i = 0; i < parameterAnnotations.length; i++)
        {
            Annotation[] parameterAnnotationArr = parameterAnnotations[i];
            if (parameterAnnotationArr == null)
            {
                continue;
            }
            boolean flag = false;
            for (int j = 0; j < parameterAnnotationArr.length; j++)
            {
                if (annotationClass.equals(parameterAnnotationArr[j].annotationType()))
                {
                    if (extractConfiguration.resourceIdLocation() == ResourceIdLocationType.REQUEST_BODY)
                    {
                        flag = true;
                        break;
                    } else
                    {
                        if (StringUtils.isEmpty(extractConfiguration.bindParameterName()))
                        {
                            return "";
                        }
                        if (methodSignature.getParameterNames()[i].equals(extractConfiguration.bindParameterName()))
                        {
                            flag = true;
                            break;
                        }
                    }
                }
            }
            if (flag)
            {
                targetIndex = i;
                break;
            }
        }

        String methodFullName = String.format("%s.%s", joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName());
        if (targetIndex == -1)
        {
            log.error("未在{}中找到带有@{}注解的参数,提取资源失败", methodFullName, annotationClass.getSimpleName());
            return "";
        }

        switch (extractConfiguration.resourceIdLocation())
        {
            case QUERY_STRING:
            case URL_PATH:
                return Optional.ofNullable(joinPoint.getArgs()[targetIndex]).map(x -> x.toString())
                        .orElse("");
            case REQUEST_BODY:
            {
                if (StringUtils.isEmpty(extractConfiguration.resourceIdParseSPEL()))
                {
                    log.error("在{}中, @{}中配置的resourceIdParseSPEL:{}有误!", methodFullName,
                            getAnnotationName(),
                            extractConfiguration.resourceIdParseSPEL());
                    return "";
                }
                Object resourceObject = joinPoint.getArgs()[targetIndex];
                if (resourceObject == null)
                {
                    String parameterName = methodSignature.getParameterNames()[targetIndex];
                    log.error("{}.{} 参数值为空,提取资源失败!", methodFullName, parameterName);
                    return "";
                }

                return getResourceIdFromResourceObject(joinPoint, extractConfiguration, resourceObject);
            }
        }

        return "";
    }

    protected <T> Object getRequestBodyObject(ProceedingJoinPoint joinPoint)
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
        if (parameterAnnotations == null)
        {
            return null;
        }

        int targetIndex = -1;
        for (int i = 0; i < parameterAnnotations.length; i++)
        {
            Annotation[] parameterAnnotationArr = parameterAnnotations[i];
            if (parameterAnnotationArr == null)
            {
                continue;
            }
            boolean flag = false;
            for (int j = 0; j < parameterAnnotationArr.length; j++)
            {
                if (RequestBody.class.equals(parameterAnnotationArr[j].annotationType()))
                {
                    flag = true;
                    break;
                }
            }
            if (flag)
            {
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1)
        {
            return null;
        }

        return joinPoint.getArgs()[targetIndex];
    }

    protected <T> String getResourceIdFromMethodReturnValue(ProceedingJoinPoint joinPoint,
                                                            ResourceIdExtractConfiguration extractConfiguration,
                                                            Object methodReturnResult)
    {
        if (!ResourceIdLocationType.METHOD_RETURN_VALUE.equals(extractConfiguration.resourceIdLocation()))
        {
            return "";
        }

        String methodFullName = String.format("%s.%s", joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName());
        if (StringUtils.isEmpty(extractConfiguration.resourceIdParseSPEL()))
        {
            log.error("在{}中, @{}中配置的resourceIdParseSPEL:{}有误!", methodFullName,
                    getAnnotationName(),
                    extractConfiguration.resourceIdParseSPEL());
            return "";
        }
        Object resourceObject = methodReturnResult;
        if (resourceObject == null)
        {
            log.error("{} 返回值为空,提取资源失败!", methodFullName);
            return "";
        }

        return getResourceIdFromResourceObject(joinPoint, extractConfiguration, resourceObject);
    }

    private String getResourceIdFromResourceObject(ProceedingJoinPoint joinPoint, ResourceIdExtractConfiguration extractConfiguration, Object resourceObject)
    {
        String methodFullName = String.format("%s.%s", joinPoint.getTarget().getClass().getName(),
                joinPoint.getSignature().getName());

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("obj", resourceObject);
        try
        {
            String resourceId = new SpelExpressionParser().parseExpression(extractConfiguration.resourceIdParseSPEL())
                    .getValue(context, String.class);
            return Optional.ofNullable(resourceId).orElse("");
        } catch (Exception e)
        {
            log.error("在{}中,通过SpEL '{}' 获取resourceId报错! error:{}", methodFullName, extractConfiguration.resourceIdParseSPEL(),
                    e.getMessage());
            return "";
        }
    }
}
