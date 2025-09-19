package com.lnjoying.justice.commonweb.validation.date;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月16日 13:30
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDate {
    String message() default "Invalid date format, required yyyy-MM-dd HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default "yyyy-MM-dd HH:mm:ss";
}