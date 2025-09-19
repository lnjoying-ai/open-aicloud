package com.lnjoying.justice.commonweb.handler.template.actiondescription;

import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionContextFields;
import com.lnjoying.justice.commonweb.util.ApplicationUtils;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月15日 17:04
 */
public abstract class AbstractActionDescriptionTemplate implements ActionDescriptionTemplate
{
    private String language;
    private Map<String, Object> contextData;

    public AbstractActionDescriptionTemplate()
    {

    }

    public AbstractActionDescriptionTemplate(String language)
    {
        this.language = language;
    }

    @Override
    public Optional<String> getLanguage()
    {
        return Optional.of(language);
    }

    @Override
    public void setContextData(Map<String, Object> contextData)
    {
        this.contextData = contextData;
    }

    @Override
    public Map<String, Object> getContextData()
    {
        return this.contextData;
    }

    public <T> T getResourceInstance()
    {
        return Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RESOURCE_INSTANCE))
                .map(x -> ((T) x))
                .orElse(null);
    }

    public String getDataFromContext(String key)
    {
        return Optional.ofNullable(getContextData().get(key)).map(x -> x.toString())
                .orElse("");
    }

    public String getInputParameter(String key, String fallbackKey)
    {
        String value = Optional.ofNullable(getContextData().get(key)).map(x -> x.toString()).orElse("");
        if (!StringUtils.hasText(value) && StringUtils.hasText(fallbackKey))
        {
            value = Optional.ofNullable(getContextData().get(ActionDescriptionContextFields.RAW_INPUT_PARAMETER_MAP))
                    .map(x -> Optional.ofNullable(((Map<String, Object>) x).get(fallbackKey))
                            .map(x2 -> x2.toString()).orElse(""))
                    .orElse("");
        }

        return Optional.ofNullable(value).orElse("");
    }

    protected  <T> Optional<T> getBean(Class<T> clazz)
    {
        T instance = null;
        try
        {
            instance = ApplicationUtils.getBean(clazz);
        } catch (BeansException e)
        {

        }
        return Optional.ofNullable(instance);
    }
}
