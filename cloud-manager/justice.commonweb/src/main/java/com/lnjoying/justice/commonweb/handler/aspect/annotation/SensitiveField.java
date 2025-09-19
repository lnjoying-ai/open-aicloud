package com.lnjoying.justice.commonweb.handler.aspect.annotation;

import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField
{
    SensitiveType value();
}
