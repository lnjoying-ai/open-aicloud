package com.lnjoying.justice.commonweb.util;

import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.ReflectionUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

import static com.lnjoying.justice.schema.common.ErrorCode.DUMP_YAML_FILE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorCode.LOAD_YAML_FILE_FAILED;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/5/28 17:05
 */

@Slf4j
public class YamlParserUtil
{
    public static String dumpObject(Object object) {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        //options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        //NullIgnoringRepresenter
        return new Yaml(new SafeConstructor(),  new NullIgnoringRepresenter(), options).dumpAsMap(object);
    }

    public static String dumpNacosObject(Object object) {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        return new Yaml(new SafeConstructor(), new CustomRepresenter(), options).dumpAsMap(object);
    }


    public static <T> T loadObject(String content, Class<T> type) {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        return new Yaml(options).loadAs(content, type);
    }


    /**
     * Data is written to file in yaml format
     * @param filePath relative path
     * @param data
     */
    public static void writeYamlConfig(String filePath, Object data)
    {
        Yaml yaml = new Yaml();
        try (FileWriter fileWriter = new FileWriter(filePath))
        {
            yaml.dump(data, fileWriter);
        }
        catch (IOException e)
        {
            log.error("dump yaml file failed:{}", e);
            throw new WebSystemException(DUMP_YAML_FILE_FAILED, ErrorLevel.ERROR);
        }
    }

    public static void writeYamlConfig(String filePath, Object data, Constructor constructor, Representer representer)
    {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(false);
        //options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        Yaml yaml = new Yaml(constructor, representer, options);
        try
        {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null)
            {
                parentDir.mkdirs();
            }
            try (FileWriter fileWriter = new FileWriter(filePath))
            {

                yaml.dump(data, fileWriter);
            }
            catch (IOException e)
            {
                log.error("dump yaml file failed:{}", e);
                throw new WebSystemException(DUMP_YAML_FILE_FAILED, ErrorLevel.ERROR);
            }
        }
        catch (Exception e)
        {
            log.error("create file failed:{}", e);
            throw new WebSystemException(DUMP_YAML_FILE_FAILED, ErrorLevel.ERROR);
        }

    }

    /**
     * read data from yaml file
     * @param filePath file relative path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T readYamlConfig(String filePath, Class<T> clazz)
    {
        File svrConfigFile = new File(filePath);
        if (! svrConfigFile.exists())
        {
            return null;
        }
        Yaml yaml = new Yaml();
        try (FileReader fileReader = new FileReader(filePath))
        {
            return  yaml.loadAs(fileReader, clazz);
        }
        catch (IOException e)
        {
            log.error("load yaml file failed:{}", e);
            throw new WebSystemException(LOAD_YAML_FILE_FAILED, ErrorLevel.ERROR);
        }
    }

    public static class CustomRepresenter extends Representer
    {

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue,
                                                      Tag customTag) {
            if (propertyValue instanceof DefaultListableBeanFactory) {
                return null;
            } else {
                Field field = ReflectionUtils.findField(javaBean.getClass(), property.getName());
                NacosProperty nacosProperty = field.getAnnotation(NacosProperty.class);
                String propertyName = property.getName();
                if (Objects.isNull(nacosProperty))
                {
                    Value value = field.getAnnotation(Value.class);
                    if (Objects.nonNull(value))
                    {
                        propertyName = value.value();
                    }
                }
               else
                {
                    propertyName = nacosProperty.value();
                }

                Field nameField = ReflectionUtils.findField(property.getClass(), "name");
                nameField.setAccessible(true);
                ReflectionUtils.setField(nameField, property, removePlaceholder(propertyName));

                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }

        private String removePlaceholder(String value)
        {
            PropertyPlaceholderHelper placeholderHelper
                    = new PropertyPlaceholderHelper("${", "}", ":", false);
            return placeholderHelper.replacePlaceholders(
                    value
                    ,  name -> {
                        String tmp = "";
                        int index = name.indexOf(':');
                        if (index != -1)
                        {
                            tmp = name.substring(0, index);
                        }
                        else
                        {
                            tmp = name;
                        }
                        return tmp ;

                    }
            );
        }
    }

    static class NullIgnoringRepresenter extends Representer {
        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
            if (propertyValue == null)
            {
                return null; // Skip null properties
            }
            return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
        }
    }

    static class NoTypeTagRepresenter extends Representer {
        @Override
        protected Tag getTag(Class<?> clazz, Tag defaultTag)
        {
            return null;
        }
    }
}
