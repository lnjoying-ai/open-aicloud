//package com.lnjoying.justice.commonweb.handler.aspect.annotation;
//
//import com.lnjoying.justice.commonweb.common.aspect.enums.EventLevel;
//import com.lnjoying.justice.schema.common.EventType;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target({ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface RaiseEventLog
//{
//    /************* 事件处理基础配置部分 *************/
//    EventLevel eventLevel() default EventLevel.INFO;
//    String okDescription() default "";
//    String errorDescription() default "";
//    Class<?> eventHandlerClass() default Object.class;
//
//    String eventHandler() default "";
//
//    EventType eventType() default EventType.DEFAULT;
//
//    String description();
//
//    boolean makeInParametersAsDescription() default false;
//
//    ResourceIdConfiguration resourceIdConfig() default @ResourceIdConfiguration();
//}