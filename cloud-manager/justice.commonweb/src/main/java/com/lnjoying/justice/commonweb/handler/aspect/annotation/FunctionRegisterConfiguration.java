package com.lnjoying.justice.commonweb.handler.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionRegisterConfiguration {
    /**
     * 给函数取个唯一的名称(必填)
     *
     * @return
     */
    String functionId() default "";

    /**
     * 函数类(必填)
     * @return
     */
    Class<?> functionClass() default Object.class;

    /**
     * 函数方法名(必填)
     * @return
     */
    String functionMethodName() default "";

    /**
     * 函数方法参数类型列表(方法签名)(必填)
     * @return
     */
    Class<?>[] parameterTypes() default Object.class;
}
