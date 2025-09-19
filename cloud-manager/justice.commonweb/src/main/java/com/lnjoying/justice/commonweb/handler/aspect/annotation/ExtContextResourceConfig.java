package com.lnjoying.justice.commonweb.handler.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月25日 11:26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtContextResourceConfig
{
    String resourceMapperId() default "";
    String resourceIdParseSpEl() default "";
    String variableName() default "";
    String variableValueParseSpEl() default "";
}
