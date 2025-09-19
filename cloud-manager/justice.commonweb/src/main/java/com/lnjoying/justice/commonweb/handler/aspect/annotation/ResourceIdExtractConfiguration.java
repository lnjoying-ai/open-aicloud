package com.lnjoying.justice.commonweb.handler.aspect.annotation;

import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月25日 20:25
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceIdExtractConfiguration
{
    /**
     * 资源项数据所在位置
     *
     * @return
     */
    ResourceIdLocationType resourceIdLocation() default ResourceIdLocationType.NONE;

    /**
     * 资源项数据绑定的方法参数名(resourceItemLocation设置为URL_PATH/QUERY_STRING时需要设置)
     *
     * @return
     */
    String bindParameterName() default  "";

    /**
     * 资源项数据的SpEl提取表达式(resourceItemLocation设置为REQUEST_BODY/METHOD_RETURN_VALUE时需要设置)
     *
     * @return
     */
    String resourceIdParseSPEL() default "";

    /**
     * 资源实体主键类型
     *
     * @return
     */
    Class<?> resourcePrimaryKeyClass() default String.class;
    String convertToResourcePrimaryKeyTypeSpEl() default "";
}
