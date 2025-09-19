package com.lnjoying.justice.servicegw.secret;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/15/22 6:09 PM
 */
@Data
public class SslContextFactory
{
    private static Logger LOGGER = LogManager.getLogger();
    static CertDaoFactory certDaoFactory=new CertDaoFactory();

    public static SSLEngine svrEngine(ByteBufAllocator allocator, String clsCrtPem, String clsKeyPem, String rootCAPem)
    {
        try
        {
            CertDao certDao = null;
            if (StringUtils.isNotBlank(clsKeyPem))
            {
                certDao = certDaoFactory.getCertDaoByKey(clsKeyPem);
            }
            else if(StringUtils.isNotBlank(clsCrtPem))
            {
                certDao = certDaoFactory.getCertDaoByCert(clsCrtPem);
                
            }
            else if (StringUtils.isNotBlank(rootCAPem))
            {
                certDao = certDaoFactory.getCertDaoByCert(rootCAPem);
            }
            else
            {
                return null;
            }
            
            PrivateKey clsKey = certDao.getPrivatekey(clsKeyPem);
            X509Certificate clsCrt = certDao.getX509Cert(clsCrtPem);
            X509Certificate rootCrt = certDao.getX509Cert(rootCAPem);
            SslContext sslContext = SslContextBuilder.forServer(clsKey, clsCrt).trustManager(rootCrt).build();
            SSLEngine engine = sslContext.newEngine(allocator);
            engine.setUseClientMode(false);
            engine.setNeedClientAuth(false);
            return engine;
        }
        catch (CertificateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static SSLEngine cliEngine(ByteBufAllocator allocator, String clsCrtPem, String clsKeyPem, String usrCrtPem, String usrKeyPem)
    {
        try
        {
            CertDao certDao = null;
            if (StringUtils.isNotBlank(clsKeyPem))
            {
                certDao = certDaoFactory.getCertDaoByKey(clsKeyPem);
            }
            else if(StringUtils.isNotBlank(clsCrtPem))
            {
                certDao = certDaoFactory.getCertDaoByCert(clsCrtPem);
        
            }
            else if (StringUtils.isNotBlank(usrCrtPem))
            {
                certDao = certDaoFactory.getCertDaoByCert(usrCrtPem);
            }
            else if (StringUtils.isNotBlank(usrKeyPem))
            {
                certDao = certDaoFactory.getCertDaoByKey(usrKeyPem);
            }
            else
            {
                return null;
            }

            X509Certificate clsCrt = certDao.getX509Cert(clsCrtPem);
    
            PrivateKey usrKey = certDao.getPrivatekey(usrKeyPem);
            X509Certificate usrCrt = certDao.getX509Cert(usrCrtPem);
            
            SslContext sslContext = SslContextBuilder.forClient().trustManager(clsCrt, usrCrt).keyManager(usrKey, usrCrt).build();
    
            SSLEngine engine = sslContext.newEngine(allocator);
            engine.setUseClientMode(true);
            engine.setNeedClientAuth(true);
            return engine;
        }
        catch (CertificateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static SSLEngine cliEngine(ByteBufAllocator allocator, ClusterEntity clusterEntity)
    {
        if (! CollectionUtils.hasContent(clusterEntity.getUserCrtPem()) || ! CollectionUtils.hasContent(clusterEntity.getUserKeyPem()))
        {
            return cliEngine(allocator, clusterEntity.getClusterCaCrtPem(), "");
        }
        
        return cliEngine(allocator, clusterEntity.getClusterCaCrtPem(), "", clusterEntity.getUserCrtPem(), clusterEntity.getUserKeyPem());
    }
    
    
    public static SSLEngine cliEngine(ByteBufAllocator allocator, String clsCrtPem, String clsKeyPem)
    {
        try
        {
            CertDao certDao = null;
            if (StringUtils.isNotBlank(clsKeyPem))
            {
                certDao = certDaoFactory.getCertDaoByKey(clsKeyPem);
            }
            else if(StringUtils.isNotBlank(clsCrtPem))
            {
                certDao = certDaoFactory.getCertDaoByCert(clsCrtPem);
        
            }
            else
            {
                return null;
            }
            
            X509Certificate clsCrt = certDao.getX509Cert(clsCrtPem);
            
            SslContext sslContext = SslContextBuilder.forClient().trustManager(clsCrt).build();
            
            SSLEngine engine = sslContext.newEngine(allocator);
            engine.setUseClientMode(true);
            engine.setNeedClientAuth(false);
            return engine;
        }
        catch (CertificateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
}
