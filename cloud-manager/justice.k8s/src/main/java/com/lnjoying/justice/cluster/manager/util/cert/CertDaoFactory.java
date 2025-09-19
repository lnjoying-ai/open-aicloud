package com.lnjoying.justice.cluster.manager.util.cert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 7/25/22 1:04 PM
 */
@Service
public class CertDaoFactory
{
	@Autowired
	private X509CertECDao x509CertECDao;
	
	@Autowired
    private X509CertRSADao x509CertRSADao;
	
	public CertDao getCertDaoByKey(String key)
    {
        if (key.startsWith("-----BEGIN EC PRIVATE KEY-----"))
        {
            return x509CertECDao;
        }
    
        if (key.startsWith("-----BEGIN RSA PRIVATE KEY-----"))
        {
            return x509CertRSADao;
        }
        
        return x509CertECDao;
    }
    
    CertificateFactory CERTIFICATE_FACTORY = null;
    
    CertDaoFactory()
    {
        try
        {
            CERTIFICATE_FACTORY = CertificateFactory.getInstance("X.509");
        }
        catch (CertificateException e)
        {
            e.printStackTrace();
        }
    }
    
    
    public CertDao getCertDaoByCert(String certStr)
    {
        InputStream stream = new ByteArrayInputStream(certStr.getBytes(StandardCharsets.UTF_8));
        try
        {
            X509Certificate cert = (X509Certificate) CERTIFICATE_FACTORY.generateCertificate(stream);
            if (cert.getSigAlgName().contains("EC"))
            {
                return x509CertECDao;
            }
            else
            {
                return x509CertRSADao;
            }
        }
        catch (CertificateException e)
        {
            e.printStackTrace();
        }
        return x509CertECDao;
    }
    
}
