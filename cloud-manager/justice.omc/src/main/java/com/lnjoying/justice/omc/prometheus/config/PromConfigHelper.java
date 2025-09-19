package com.lnjoying.justice.omc.prometheus.config;

import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import com.lnjoying.justice.omc.prometheus.client.PrometheusClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/1/10 16:56
 */

@Slf4j
@Component
public class PromConfigHelper
{
    private static String CONFIG_PATH = "/root/monitor/prometheus/prometheus.yml";

    @Autowired
    private  PrometheusClient prometheusClient;

    private static PromConfigHelper configHelper;
    @PostConstruct
    public void init() {
        configHelper = this;
        configHelper.prometheusClient = this.prometheusClient;
    }

    public static String modifyConfigFile(String filePath)
    {
        Map<?, ?> configMap = readConfigFile(filePath);
        return YamlParserUtil.dumpObject(configMap);
    }

    public static void modifyAndSaveConfigFile(String filePath)
    {
        String configFile = modifyConfigFile(filePath);
        if (StringUtils.isNotBlank(configFile))
        {
            createBackup();
            saveConfigFile(configFile);
        }
        else {
            log.error("modify config file error, config file is blank");
        }
    }

    public static void modifyRemoteReadAndSaveConfigFile(String filePath, RemoteReadConfig remoteReadConfig)
    {
        Map<String, Object> configMap  = modifyRemoteReadConfig(filePath, remoteReadConfig);
        String configFile = YamlParserUtil.dumpObject(configMap);
        if (StringUtils.isNotBlank(configFile))
        {
            createBackup();
            saveConfigFile(configFile);
        }
        else {
            log.error("modify config file error, config file is blank");
        }
    }

    public static void deleteRemoteReadAndSaveConfigFile(String filePath, RemoteReadConfig remoteReadConfig)
    {
        Map<String, Object> configMap  = deleteRemoteReadConfig(filePath, remoteReadConfig);
        String configFile = YamlParserUtil.dumpObject(configMap);
        if (StringUtils.isNotBlank(configFile))
        {
            createBackup();
            saveConfigFile(configFile);
        }
        else {
            log.error("modify config file error, config file is blank");
        }
    }

    public static Map<String, Object> modifyRemoteReadConfig(String filePath, RemoteReadConfig remoteReadConfig)
    {
        Map<String, Object> yamlMap = readConfigFile(filePath);
        if (CollectionUtils.isEmpty(yamlMap))
        {
            return null;
        }
        Collection<Map<String, Object>> remoteReadList =  (Collection<Map<String, Object>>)yamlMap.get("remote_read");
        if (!CollectionUtils.isEmpty(remoteReadList))
        {
            Map map = JacksonUtils.convertValueExcludeNull(remoteReadConfig, Map.class);
            remoteReadList.add(map);
        }
        else
        {
            LinkedList<Map> remoteReadConfigs = new LinkedList<>();
            Map map = JacksonUtils.convertValueExcludeNull(remoteReadConfig, Map.class);
            remoteReadConfigs.add(map);
            yamlMap.put("remote_read", remoteReadConfigs);
        }
        return (Map<String, Object>) yamlMap;
    }

    public static Map<String, Object> deleteRemoteReadConfig(String filePath, RemoteReadConfig remoteReadConfig)
    {
        Map<String, Object> yamlMap = readConfigFile(filePath);
        if (CollectionUtils.isEmpty(yamlMap))
        {
            return null;
        }
        Collection<Map<String, Object>> remoteReadList =  (Collection<Map<String, Object>>)yamlMap.get("remote_read");
        if (!CollectionUtils.isEmpty(remoteReadList))
        {
            remoteReadList.removeIf(map -> map.get("url").equals(remoteReadConfig.getUrl()));
        }

        return (Map<String, Object>) yamlMap;
    }

    public static Map<String, Object> readConfigFile(String filePath) {
        try {
            String path = StringUtils.isNotBlank(filePath) ? filePath : CONFIG_PATH;
            Map<String, Object> yamlMap = YamlParserUtil.readYamlConfig(path, Map.class);
            if (CollectionUtils.isEmpty(yamlMap))
            {
                return null;
            }
            return yamlMap;

        }
        catch (Exception e)
        {

            return null;
        }

    }

    private static void modifyConfig(Map<String, Object> configMap) {
        if (configMap.containsKey("global") && configMap.get("global") instanceof Map) {
            Map<String, Object> globalConfig = (Map<String, Object>) configMap.get("global");
            globalConfig.put("scrape_interval", "30s");
        }
    }


    public static void saveConfigFile(String filePath, Map<String, Object> configMap) {
        try {
            Path path = Paths.get(CONFIG_PATH);
            String yamlString = YamlParserUtil.dumpObject(configMap);
            Path parent = path.getParent();
            if (parent != null) {
                if (Files.notExists(parent)) {
                    Files.createDirectories(parent);
                }
            }
            Files.write(path, yamlString.getBytes());
            log.info("Configuration file updated successfully.");
        } catch (IOException e) {
           log.error("Failed to update configuration file.");
        }
    }

    public static void backUpAndSaveConfigFile(String content)
    {
        createBackup();
        saveConfigFile(content);
    }

    public static void saveConfigFile(String content) {
        try {
            Path path = Paths.get(CONFIG_PATH);
            Files.write(path, content.getBytes());
            log.info("Configuration file updated successfully.");
        } catch (IOException e) {
            log.error("Failed to update configuration file, error:{}", e);
        }
    }

    public static void createBackup() {
        try {
            Path sourcePath = Paths.get(CONFIG_PATH);
            if (Files.exists(sourcePath)) {
                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String timestamp = currentTime.format(formatter);

                String backupFileName = "prometheus_backup_" + timestamp + ".yml";
                Path backupPath = sourcePath.getParent().resolve(backupFileName);

                CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES };
                Files.copy(sourcePath, backupPath, options);

                log.info("Backup created successfully: " + backupPath);
            }
            else
            {
                log.info("Source file does not exist. Skipping backup creation.");
            }

        } catch (FileAlreadyExistsException e)
        {
            log.error("Backup already exists. Skipping backup creation.");
        } catch (IOException e)
        {
           log.error("Failed to create backup.");
        }
    }


}
