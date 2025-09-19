package com.lnjoying.justice.cluster.manager.config;

import com.micro.core.common.Utils;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class YamlReader
{
    void loadTemplateIfo()
    {
        Yaml yaml = new Yaml();
        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader("/Users/regulus/source/lnjoying_src_git/edge_cloud_src/edgecloud-manager/config/etc/k8s/k8s_addon_templates.yaml");
            
            Map<String,String> template_info = yaml.load(fileReader);
            for (Map.Entry<String, String> entry : template_info.entrySet())
            {
                String filePath = "/Users/regulus/source/lnjoying_src_git/edge_cloud_src/edgecloud-manager/config/etc/k8s/templates/" + entry.getKey() + ".yaml.ftl";
                FileWriter writer = new FileWriter(filePath);
                writer.write(entry.getValue());
                writer.close();
//                System.out.println(entry.getValue());
            }
            fileReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    void loadK8sVersionInfo()
    {
        Yaml yaml = new Yaml();
        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader("/Users/regulus/source/lnjoying_src_git/edge_cloud_src/edgecloud-manager/config/etc/k8s/k8s_version_components_options.yaml");
        
            Map<String,Map<String,Map<String,String>>> template_info = yaml.load(fileReader);
            
            fileReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    void loadK8sBasic()
    {
        Yaml yaml = new Yaml();
        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader("/Users/regulus/source/lnjoying_src_git/edge_cloud_src/edgecloud-manager/config/etc/k8s/k8s_version_basic_config.yaml");
            K8sBasicConfig k8sBasicConfig = yaml.loadAs(fileReader, K8sBasicConfig.class);
            fileReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
