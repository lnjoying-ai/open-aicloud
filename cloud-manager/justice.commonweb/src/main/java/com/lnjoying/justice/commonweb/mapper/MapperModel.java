package com.lnjoying.justice.commonweb.mapper;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/6 17:18
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MapperModel
{
    /**
     * resource name
     * @return
     */
    String value() default "";

    /**
     * method for get resource info
     * @return
     */
    String method() default "selectByPrimaryKey";
}
