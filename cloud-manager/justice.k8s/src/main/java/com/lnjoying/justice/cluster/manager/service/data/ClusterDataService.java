package com.lnjoying.justice.cluster.manager.service.data;

import com.lnjoying.justice.cluster.manager.common.ClusterType;
import com.lnjoying.justice.cluster.manager.config.K3sConfigBean;
import com.lnjoying.justice.cluster.manager.config.K8sBasicConfig;
import com.lnjoying.justice.cluster.manager.config.K8sConfigBean;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.cluster.manager.util.template.RedisTemplateStorage;
import com.lnjoying.justice.cluster.manager.util.template.ClsTemplateUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import freemarker.template.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @description  k8s image & service option & template service
 * @author Regulus
 * @date 11/29/21 4:33 PM
 */
@Service("clusterDataService")
public class ClusterDataService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClsTemplateUtil templateUtil;
    
    @Autowired
    private RedisTemplateStorage redisTemplateStorage;
    
    @Autowired
    private K8sConfigBean k8sConfigBean;

    @Autowired
    private K3sConfigBean k3sConfigBean;
    
    private Set<String> k8sVersions = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return -ClsUtils.versionCompare(o1, o2);
        }
    });

    private Set<String> k3sVersions = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return -ClsUtils.versionCompare(o1, o2);
        }
    });

    @PostConstruct
    void setK8sVersions()
    {
        for (Map.Entry<String, Map<String,String>> entry : k8sConfigBean.getK8sJkeSystemImages().entrySet())
        {
            k8sVersions.add(entry.getKey());
        }
    }

    @PostConstruct
    void setK3sVersions()
    {
        for (Map.Entry<String, Map<String,String>> entry : k3sConfigBean.getK3sJkeSystemImages().entrySet())
        {
            k3sVersions.add(entry.getKey());
        }
    }
    /**
     * @description get service option by k8s version and service module name
     * @author Regulus
     * @date 11/29/21 4:35 PM
     * @param version
     * @param service
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String,String> getK8sServiceOption(String version, String service) throws WebSystemException
    {
        String versionClone = version;
        String [] versionArray = versionClone.split("\\.");
        if (versionArray.length > 2)
        {
            versionClone = Utils.buildStr(versionArray[0],".", versionArray[1]);
        }
        
        String key = Utils.buildStr(K8sRedisField.K8S_SERVICE_VERSION, version,":");
        Object serviceObj = RedisUtil.oget(key, service);
        Map<String,String> serviceMap = null;
        
        if (serviceObj == null)
        {
            Map<String,Map<String,String>> versionMap = k8sConfigBean.getK8sServiceOptions().get(versionClone);
            if (versionMap == null || versionMap.isEmpty())
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_VERSION_EMPTY, ErrorLevel.INFO);
            }
            
            serviceMap = versionMap.get(service);
            RedisUtil.oset(key, service, serviceMap);
            
        }
        else
        {
            serviceMap = (Map<String,String>) serviceObj;
        }
        
        return serviceMap;
    }
    
    /**
     * @description get container service image name by k8s version and container service name
     * @author Regulus
     * @date 11/29/21 4:36 PM
     * @param version
     * @param serviceName 
     * @return java.lang.String
     */
    public String getImage(String version,String serviceName) throws WebSystemException
    {
        String key = Utils.buildStr(K8sRedisField.K8S_IMAGE_VERSION, version);
        String imageName = RedisUtil.get(key, serviceName);
        if (StringUtils.isEmpty(imageName))
        {
            Map<String,String>versionMap = k8sConfigBean.getK8sJkeSystemImages().get(version);
            if (versionMap == null || versionMap.isEmpty())
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_VERSION_EMPTY, ErrorLevel.INFO);
            }
            
            imageName = versionMap.get(serviceName);
            if (StringUtils.isEmpty(imageName))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_IMAGE_NOT_CFG, ErrorLevel.INFO);
            }
            RedisUtil.set(key, imageName);
        }
        return imageName;
    }
    
    /**
     * @description get addon template index by k8s version and the system addon name
     * @author Regulus
     * @date 11/29/21 4:36 PM
     * @param version
     * @param addonName 
     * @return java.lang.String
     */
    public String getTemplateIndex(String version,String addonName) throws WebSystemException
    {
        String key = Utils.buildStr(K8sRedisField.K8S_ADDON_VERSION, version, ":",addonName);
    
        String index = RedisUtil.get(key);
        if (StringUtils.isEmpty(index))
        {
            Map<String,String>versionMap = k8sConfigBean.getK8sAddonTemplatesIndex().get(version);
            if (versionMap == null || versionMap.isEmpty())
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_VERSION_EMPTY, ErrorLevel.INFO);
            }
    
            index =  versionMap.get(addonName);
            if (StringUtils.isEmpty(index))
            {
                throw new WebSystemException(ErrorCode.CLUSTER_K8S_ADDON_TEMPLATE_INDEX_EMPTY, ErrorLevel.INFO);
            }
            RedisUtil.set(key, index);
        }
        return index;
    }
    
    /**
     * @description generate addon config yaml info by addon index and template params
     * @author Regulus
     * @date 11/29/21 4:37 PM
     * @param index
     * @param params
     * @return java.lang.String
     */
    public String getTemplate(String index, Map<String, Object> params) throws IOException, TemplateException
    {
        String template;
        String makerKey = index.replace(".","-");
        try
        {
            try
            {
                template = templateUtil.format(makerKey, params);
            }
            catch (TemplateNotFoundException e)
            {
                LOGGER.info("{} template is empty, load template from local", index);
                String ftlDir =  Utils.buildStr(System.getProperty("lj_config"),"/k8s/templates");
                String fileName = Utils.buildStr(index,".yaml.ftl");
                String filePath = Utils.buildStr(ftlDir,"/",fileName);
                FileInputStream fs = new FileInputStream(new File(filePath));
                String ftl = IOUtils.toString(fs, StandardCharsets.UTF_8);
                template = readTmplFromLocal(ftlDir, fileName, params);
                if (CollectionUtils.hasContent(template))
                {
                    redisTemplateStorage.saveTemplate(makerKey, ftl);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        
        return template;
    }
    
    String readTmplFromLocal(String ftlDir, String fileName, Map<String, Object> params)
    {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("UTF-8");
        try
        {
            configuration.setDirectoryForTemplateLoading(new File(ftlDir));
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template template = configuration.getTemplate(fileName);
            Writer writer = new StringWriter();
            template.process(params, writer);
            writer.flush();
            writer.close();
            String result = writer.toString();
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (TemplateException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getDefaultK8sVersion()
    {
        return k8sConfigBean.getK8sBasicConfig().getDefaultK8sVersion();
    }
    
    public boolean checkDockerVersion(String k8sVersion, String dockerVersion)
    {
        if (CollectionUtils.isEmpty(k8sConfigBean.getK8sBasicConfig().getDockerVersions()))
        {
            return false;
        }
        
        Set<String> dockers = k8sConfigBean.getK8sBasicConfig().getDockerVersions().get(k8sVersion);
        return dockers.contains(dockerVersion);
    }
    
    public Map<String, Set<String>> getAllClusterVersions()
    {
        Map<String, Set<String>> versionMap = new HashMap<>();
        versionMap.put(ClusterType.K8S, k8sVersions);
    
        return versionMap;
    }
    
    
    public Map<String, List<String>> getClusterVersions(String type)
    {
        Map<String, List<String>> versionMap = new HashMap<>();
        if (! CollectionUtils.hasContent(type))
        {
            getAllClusterVersions().forEach((key, value) -> {versionMap.put(key, new ArrayList<>(value));});
        }else if (type.equals(ClusterType.K8S))
        {
            versionMap.put(ClusterType.K8S, new ArrayList<>(k8sVersions));
        }
        else if (ClusterType.K3S.equals(type))
        {
            versionMap.put(ClusterType.K3S, new ArrayList<>(k3sVersions));
        }
        return versionMap;
    }
}
