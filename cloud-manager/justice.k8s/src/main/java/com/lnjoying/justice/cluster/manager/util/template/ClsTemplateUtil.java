package com.lnjoying.justice.cluster.manager.util.template;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;
/**
 * @description util for format freemaker template
 * @author Regulus
 * @date 11/26/21 9:02 PM
 */
@Service("clsTemplateUtil")
public class ClsTemplateUtil
{
    @Autowired
    private freemarker.template.Configuration freemarkerConfig;
    
    public String format(String templateName, Map<String, Object> param) throws IOException, TemplateException
    {
        Template tpl = freemarkerConfig.getTemplate(templateName);
        Writer out = new StringWriter();
        tpl.process(param, out);
        return out.toString();
    }
}