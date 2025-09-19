package com.lnjoying.justice.commonweb.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import freemarker.template.Template;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

import static com.lnjoying.justice.schema.common.ErrorCode.TEMPLATE_FORMAT_FAILED;

/**
 * 模板引擎工具类(动态字符串插值替换)
 *
 * @author: Robin
 * @date: 2024年01月11日 11:01
 */
public class TemplateEngineUtils
{
    private TemplateEngineUtils()
    {

    }

    private static Logger LOGGER = LogManager.getLogger();
    private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("\\$\\{([a-zA-Z_][a-zA-Z0-9_]*(?:\\.[a-zA-Z0-9_]+)*)\\}");
    private static freemarker.template.Configuration freeMarkerConfig;

    static
    {
        try
        {
            FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
            freeMarkerConfigurer.setDefaultEncoding("UTF-8");
            freeMarkerConfig = freeMarkerConfigurer.createConfiguration();
            freeMarkerConfig.setLocale(Locale.ROOT);
        } catch (Exception e)
        {
            LOGGER.error("freeMarker配置初始化失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public static Map.Entry<String, Object> newEntry(String key, Object value)
    {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static String render0(String templateContent, boolean throwExceptionWhenError, Map.Entry<String, Object>... templateDataEntries)
    {
        try
        {
            Map<String, Object> templateDataMap = new HashMap<>();
            if (templateDataEntries != null)
            {
                for (Map.Entry<String, Object> templateDataEntry : templateDataEntries)
                {
                    templateDataMap.put(templateDataEntry.getKey(), templateDataEntry.getValue());
                }
            }

            return render0(templateContent, templateDataMap);
        } catch (Exception e)
        {
            if (throwExceptionWhenError)
            {
                throw e;
            }
        }

        return "";
    }

    public static String render(String templateContent, Map<String, String> templateData)
    {
        Map<String, Object> templateDataMap = new HashMap<>();
        templateDataMap.putAll(templateData);
        return render0(templateContent, templateDataMap);
    }

    public static String render0(String templateContent, Map<String, Object> templateData)
    {
        Map<String, Object> parsedParameterMap = parseTemplateData(templateData);
        try (StringWriter out = new StringWriter())
        {
            Template template = new Template("", convertToOptionalValueTemplate(templateContent), freeMarkerConfig);
            template.process(parsedParameterMap, out);
            return out.getBuffer().toString();
        } catch (Exception e)
        {
            LOGGER.error("template render failed! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
            throw new WebSystemException(TEMPLATE_FORMAT_FAILED, ErrorLevel.ERROR);
        }
    }

    public static String convertToOptionalValueTemplate(String templateContent)
    {
        if (StringUtils.isEmpty(templateContent))
        {
            return "";
        }

        return INTERPOLATION_PATTERN.matcher(templateContent).replaceAll("\\${($1)!\"\"}");
    }

    private static Map<String, Object> parseTemplateData(Map<String, Object> templateData)
    {
        Map<String, Object> parameterMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(templateData))
        {
            templateData.entrySet().stream().forEach(param ->
            {
                Object rawValue = param.getValue();
                Object parsedValue = null;
                try
                {
                    if (rawValue != null && rawValue instanceof String)
                    {
                        JsonNode jsonNode = JacksonUtils.readTree(rawValue.toString());
                        if (jsonNode.isObject())
                        {
                            parsedValue = JacksonUtils.convertValue(jsonNode, new TypeReference<Map<String, Object>>()
                            {
                            });
                        } else if (jsonNode.isArray())
                        {
                            parsedValue = JacksonUtils.convertValue(jsonNode, new TypeReference<List<Object>>()
                            {
                            });
                        } else if (jsonNode.isValueNode())
                        {
                            parsedValue = jsonNode.asText();
                        }
                    }
                    else
                    {
                        parsedValue = rawValue;
                    }
                } catch (Exception e)
                {
                    parsedValue = rawValue;
                }
                parameterMap.put(param.getKey(), parsedValue);
            });
        }

        return parameterMap;
    }
}
