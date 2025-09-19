package com.lnjoying.justice.omc.prometheus.util;

import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import com.lnjoying.justice.omc.prometheus.model.PrometheusAlertingRule;
import com.lnjoying.justice.omc.prometheus.model.PrometheusRuleGroup;
import com.lnjoying.justice.omc.prometheus.model.PrometheusRuleGroups;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ReflectionUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.MethodProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/26 14:30
 */

public class PrometheusUtils
{
    public static void writeAlertRuleToYAML(PrometheusRuleGroups alertRule, String filePath)
    {
        //  dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        CustomRepresenter customRepresenter = new CustomRepresenter();
        customRepresenter.addClassTag(PrometheusRuleGroups.class, Tag.MAP);
        YamlParserUtil.writeYamlConfig(filePath, alertRule, new Constructor() , customRepresenter);
    }

    public static boolean deleteAlertRule(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
        {
            if (file.delete())
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return true;
    }

    static class CustomRepresenter extends Representer
    {

        @Override
        protected Node representScalar(Tag tag, String value)
        {
            if (value.contains(" ")) {
                return representScalar(tag, value, DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            } else {

                return super.representScalar(tag, value, null);
            }
        }

        @Override
        protected Node representScalar(Tag tag, String value, DumperOptions.ScalarStyle style)
        {
            if (needsDoubleQuotesForValue(value)) {
                return super.representScalar(tag, value, DumperOptions.ScalarStyle.SINGLE_QUOTED);
            }
            else {
                return super.representScalar(tag, value, null);
            }
        }

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag)
        {
            if (property.getName().equalsIgnoreCase("forValue"))
            {
                try
                {
                    Field nameField = ReflectionUtils.findField(property.getClass(), "name");
                    nameField.setAccessible(true);
                    ReflectionUtils.setField(nameField, property,"for");
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }



            }

            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
    }

    public static String removeEscapeCharacters(String input) {
        // 替换字符串中的转义字符
        return input.replaceAll("\\\\", "");
    }

    private static boolean needsDoubleQuotesForValue(String value) {
        return value.contains(" ") || value.contains("@") || value.contains(":") || value.contains("!")
                || value.contains("#") || value.contains("&") || value.contains("*");
    }

    private static boolean needsDoubleQuotesForName(String name) {
        return name.equals("summary") || name.contains("description") ;
    }
}
