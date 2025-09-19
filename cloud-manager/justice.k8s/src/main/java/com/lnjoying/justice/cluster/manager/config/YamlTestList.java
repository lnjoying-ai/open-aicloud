package com.lnjoying.justice.cluster.manager.config;

import java.io.*;
import java.util.*;

import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.RollingUpdateDaemonSet;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.Toleration;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.yaml.snakeyaml.Yaml;

import static freemarker.template.Configuration.VERSION_2_3_31;

public class YamlTestList {
    //模板配置对象
    private Configuration cfg;
    //Yaml目录
    private static String yamlPath = "/Users/regulus/source/lnjoying_src_git/edge_cloud_src/edgecloud-manager/config/etc/k8s/new_templates";
    
    /**
     * 初始化配置
     */
    public void init() {
        cfg = new Configuration(VERSION_2_3_31);
        File yamlFile = null;
        try {
            yamlFile = new File(yamlPath);
            cfg.setDirectoryForTemplateLoading(yamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void process(Map<String, Object> map){
        try {
            Template template = cfg.getTemplate("calico-v3.16.5.yaml.ftl");
            String filePath = yamlPath + "/calico-v3.16.5.yaml";
            Writer writer = new FileWriter(filePath);
            template.process(map, writer);
            writer.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
