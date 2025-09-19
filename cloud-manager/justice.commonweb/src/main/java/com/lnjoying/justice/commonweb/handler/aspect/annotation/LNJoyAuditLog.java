package com.lnjoying.justice.commonweb.handler.aspect.annotation;

import com.lnjoying.justice.commonweb.handler.template.actiondescription.ActionDescriptionTemplate;
import com.lnjoying.justice.commonweb.handler.template.actiondescription.i18n.zh_cn.SimpleActionDescriptionTemplate;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.ResponseHeader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年10月25日 20:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LNJoyAuditLog
{
    String value();

    String notes() default "";

    String[] tags() default {""};

    Class<?> response() default Void.class;

    String responseContainer() default "";

    String responseReference() default "";

    String httpMethod() default "";

    /**
     * @deprecated
     */
    @Deprecated
    int position() default 0;

    String nickname() default "";

    String produces() default "";

    String consumes() default "";

    String protocols() default "";

    Authorization[] authorizations() default {@Authorization("")};

    boolean hidden() default false;

    ResponseHeader[] responseHeaders() default {@ResponseHeader(
            name = "",
            response = Void.class
    )};

    int code() default 200;

    Extension[] extensions() default {@Extension(
            properties = {@ExtensionProperty(
                    name = "",
                    value = ""
            )}
    )};

    boolean ignoreJsonView() default false;

    ResourceIdExtractConfiguration resourceIdExtractConfig() default @ResourceIdExtractConfiguration();

    String actionDescriptionField() default "";

    String actionDescriptionValueSpEl() default "";

    String actionDescriptionFormatString() default "";

    String resourceMapperId() default "";

    /**
     * 关联的资源英文名
     *
     * @return
     */
    String associatedResourceEnName() default "";

    /**
     * 关联的资源中文名
     *
     * @return
     */
    String associatedResourceCnName() default "";


    /**
     * 是否有明确的资源实例名
     *
     * @return
     */
    boolean hasResourceInstanceName() default true;

    /**
     * 资源实例名是否从入参中读取
     *
     * @return
     */
    boolean isResourceInstanceNameFromArgs() default false;

    String resourceInstanceNameFromArgsParseSpEl() default "";

    /**
     * 资源实例名是否从远程rpc获取
     *
     * @return
     */
    boolean isResourceInstanceNameFromRpcService() default false;

    ResourceInstanceNameFromRpcServiceConfig resourceInstanceNameFromRpcServiceConfig() default @ResourceInstanceNameFromRpcServiceConfig();

    String resourceInstanceNameFromRpcServiceParseSpEl() default "";

    boolean isDynamicOperationType() default false;

    String dynamicOperationTypeSpEl() default "";

    Class<? extends ActionDescriptionTemplate> actionDescriptionTemplateClass() default SimpleActionDescriptionTemplate.class;

    String actionDescriptionTemplate() default "";

    FunctionRegisterConfiguration[] functionRegisterConfig() default @FunctionRegisterConfiguration();

//    ExtContextResourceConfig[] extContextResourceConfig() default @ExtContextResourceConfig();

    boolean logOff() default false;
}
