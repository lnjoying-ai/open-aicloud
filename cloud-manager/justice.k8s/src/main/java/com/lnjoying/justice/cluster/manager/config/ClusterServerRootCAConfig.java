package com.lnjoying.justice.cluster.manager.config;

import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.service.cert.CertService;
import com.lnjoying.justice.cluster.manager.service.cert.X509CertServiceImpl;
import com.lnjoying.justice.cluster.manager.util.YamlConfigUtils;
import com.micro.core.common.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import static com.lnjoying.justice.cluster.manager.common.KeyCertName.ClusterServerCACertName;
import static com.lnjoying.justice.cluster.manager.config.ClusterServerRootCA.FILE_PATH;
import static com.lnjoying.justice.cluster.manager.util.ClsUtils.needGenCert;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/3/18 14:40
 */
@Configuration
public class ClusterServerRootCAConfig
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    CertService certService;
    
    @Bean
    public CertService certService()
    {
        return new X509CertServiceImpl();
    }
    
    @Bean(name = "clusterServerRootCA")
    public ClusterServerRootCA loadClusterServerRootCAConfig(CertService certService)
    {
        ClusterServerRootCA clusterServerRootCA = YamlConfigUtils.readYamlConfig(FILE_PATH, ClusterServerRootCA.class);
        if (Objects.isNull(clusterServerRootCA))
        {
            clusterServerRootCA = certService.genClusterServerCACerts();
        }
        
        return clusterServerRootCA;
    }
    
    
    void closeWriter(FileWriter fileWriter)
    {
        try
        {
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("close writer error. {}", e);
        }
    }
    
    void saveServerConfig(String svrCrt, String svrKey)
    {
        String baseConfigPath = System.getProperty("lj_config");
        FileWriter fileWriter = null;
        try
        {
            String filePath = Utils.buildStr(baseConfigPath, "/ssl");
            File file = new File(filePath);
            if (file.exists() && file.isFile())
            {
                LOGGER.info("file path {} is not a directory, delete it", filePath);
                file.delete();
            }
            
            if (! file.exists())
            {
                LOGGER.info("file path {} is not exist, make it", filePath);
                file.mkdir();
            }
            
            String sslcertpem = Utils.buildStr(filePath, "/ssl.pem");
            
            fileWriter = new FileWriter(sslcertpem);
            fileWriter.write(svrCrt);
            fileWriter.flush();
            fileWriter.close();
            
            String sslkey = Utils.buildStr(filePath, "/ssl.key");
            fileWriter = new FileWriter(sslkey);
            fileWriter.write(svrKey);
            fileWriter.flush();
            closeWriter(fileWriter);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("save cert error. {}", e);
            if (fileWriter != null)
            {
                closeWriter(fileWriter);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("save cert error. {}", e);
            if (fileWriter != null)
            {
                closeWriter(fileWriter);
            }
        }
    }
    
    
    public  void genServerCert()
    {
        if (! needGenCert())
        {
            return;
        }
        
        X509CertificateInfo x509CertificateInfo = certService.genClusterServerCerts(ClusterServerCACertName);
        saveServerConfig(x509CertificateInfo.getCertificatePem(), x509CertificateInfo.getKeyPem());
    }
}
