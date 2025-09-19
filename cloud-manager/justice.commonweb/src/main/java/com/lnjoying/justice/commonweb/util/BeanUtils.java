package com.lnjoying.justice.commonweb.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

/**
 * 对象属性值拷贝工具
 *
 * @author: Robin
 * @date: 2024年01月22日 09:51
 */
public class BeanUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    private BeanUtils()
    {

    }

    /**
     * 非null值的属性值拷贝
     * @param src
     * @param target
     * @param <T>
     */
    public static <T> void copyNonNullProperties(T src, T target)
    {
        try
        {
            final BeanWrapper srcWrapper = new BeanWrapperImpl(src);
            final BeanWrapper trgWrapper = new BeanWrapperImpl(target);

            for (final PropertyDescriptor propertyDescriptor : srcWrapper.getPropertyDescriptors())
            {
                String propertyName = propertyDescriptor.getName();
                if ("class".equals(propertyName))
                {
                    continue;
                }
                Object srcPropertyValue = srcWrapper.getPropertyValue(propertyName);
                if (srcPropertyValue != null && propertyDescriptor.getWriteMethod() != null)
                {
                    trgWrapper.setPropertyValue(propertyName, srcPropertyValue);
                }
            }
        } catch (Exception e)
        {
            LOGGER.error("拷贝对象非空属性时报错! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }
}
