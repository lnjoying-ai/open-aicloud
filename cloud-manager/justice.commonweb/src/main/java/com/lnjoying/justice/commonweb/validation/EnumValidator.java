package com.lnjoying.justice.commonweb.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * Enumeration verification tool
 *
 * @author merak
 **/

@Slf4j
public class EnumValidator implements ConstraintValidator<Enum, Object>
{
    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation)
    {
        this.annotation = constraintAnnotation;
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context)
    {
        if (value == null)
        {
            return false;
        }

        Object[] objects = annotation.clazz().getEnumConstants();
        try
        {
            Method method = annotation.clazz().getMethod(annotation.method());
            for (Object o : objects)
            {
                if (value.equals(method.invoke(o)))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            log.error("enum validator error: {}", e);
            throw new RuntimeException(e);
        }
        return false;
    }
}
