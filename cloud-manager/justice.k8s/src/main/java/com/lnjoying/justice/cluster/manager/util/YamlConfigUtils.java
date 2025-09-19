package com.lnjoying.justice.cluster.manager.util;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.lnjoying.justice.schema.common.ErrorCode.DUMP_YAML_FILE_FAILED;
import static com.lnjoying.justice.schema.common.ErrorCode.LOAD_YAML_FILE_FAILED;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/18 11:22
 */

public class YamlConfigUtils
{
    private static Logger LOGGER = LogManager.getLogger();

    private static  String baseConfigPath;

    static {
        baseConfigPath = System.getProperty("lj_config");
        if (!baseConfigPath.endsWith("/"))
        {
            baseConfigPath = baseConfigPath + "/";
        }
    }

    /**
     * Data is written to file in yaml format
     * @param filePath relative path
     * @param data
     */
    public static void writeYamlConfig(String filePath, Object data)
    {
        String configPath = Utils.buildStr(baseConfigPath, filePath);
        Yaml yaml = new Yaml();
        try (FileWriter fileWriter = new FileWriter(configPath))
        {
            yaml.dump(data, fileWriter);
        }
        catch (IOException e)
        {
            LOGGER.error("dump yaml file failed:{}", e);
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
        String configPath = Utils.buildStr(baseConfigPath, filePath);
        File svrConfigFile = new File(configPath);
        if (! svrConfigFile.exists())
        {
            return null;
        }
        Yaml yaml = new Yaml();
        try (FileReader fileReader = new FileReader(configPath))
        {
           return  yaml.loadAs(fileReader, clazz);
        }
        catch (IOException e)
        {
            LOGGER.error("load yaml file failed:{}", e);
            throw new WebSystemException(LOAD_YAML_FILE_FAILED, ErrorLevel.ERROR);
        }
    }
}
