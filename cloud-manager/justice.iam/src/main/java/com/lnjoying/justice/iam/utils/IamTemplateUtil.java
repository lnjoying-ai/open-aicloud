package com.lnjoying.justice.iam.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lnjoying.justice.schema.common.ErrorCode.TEMPLATE_FORMAT_FAILED;

/**
 * @Description: learn from ClsTemplateUtil
 * @Author: Merak
 * @Date: 2022/1/13 17:09
 */
@Slf4j
@Component
public class IamTemplateUtil
{
    @Qualifier("freeMarkerConfig")
    @Autowired
    private Configuration freemarkerConfig;

    private static IamTemplateUtil aosTemplateUtil;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        aosTemplateUtil = this;
        aosTemplateUtil.freemarkerConfig = this.freemarkerConfig;
    }

    public static String format(String templateName, String templateContent, Map<String, String> param)
    {
        Map<String, Object> preParams = preprocessingParams(param);
        MultiTemplateLoader templateLoader = (MultiTemplateLoader) aosTemplateUtil.freemarkerConfig.getTemplateLoader();
        int templateLoaderCount = templateLoader.getTemplateLoaderCount();
        StringTemplateLoader stringTemplateLoader = null;
        int i = 0;
        while (i < templateLoaderCount)
        {
            TemplateLoader loader = templateLoader.getTemplateLoader(i);
            if (loader instanceof StringTemplateLoader)
            {
                stringTemplateLoader = (StringTemplateLoader)loader;
                break;
            }
            i++;
        }

        if (Objects.isNull(stringTemplateLoader))
        {
            stringTemplateLoader = new StringTemplateLoader();
        }
        stringTemplateLoader.putTemplate(templateName, templateContent);
        try
        {
            Template tpl = aosTemplateUtil.freemarkerConfig.getTemplate(templateName, "utf-8");
            Writer out = new StringWriter();
            tpl.process(preParams, out);
            // not used anymore so removed
            stringTemplateLoader.removeTemplate(templateName);
            return out.toString();
        }
        catch (IOException | TemplateException e)
        {
           log.error("template process failed:{}", e);
           throw new WebSystemException(TEMPLATE_FORMAT_FAILED, ErrorLevel.ERROR);
        }
    }

    private static Map<String, Object> preprocessingParams(Map<String, String> params)
    {
        Map<String, Object> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(params))
        {
            params.entrySet().stream().forEach(param -> {
                String value = param.getValue();
                Object tmp = null;
                try {
                    JsonNode jsonNode = objectMapper.readTree(value);
                    if (jsonNode.isObject())
                    {
                        tmp = objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>(){});
                    }
                    else if (jsonNode.isArray())
                    {
                        tmp = objectMapper.convertValue(jsonNode, new TypeReference<List<Object>>(){});
                    }
                    else if (jsonNode.isValueNode())
                    {
                        tmp = jsonNode.asText();
                    }
                }
                catch (Exception e)
                {
                    tmp = value;
                }
                result.put(param.getKey(), tmp);
            });
        }

        return result;
    }

}
