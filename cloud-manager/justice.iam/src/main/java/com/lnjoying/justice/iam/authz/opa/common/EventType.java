package com.lnjoying.justice.iam.authz.opa.common;

import java.lang.annotation.*;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType
{
    String value() default DATA_TYPE;
}
