package com.lnjoying.justice.servicegw.secret;

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
public class CertDaoFactory
{
	private X509CertECDao x509CertECDao = new X509CertECDao();
	
    private X509CertRSADao x509CertRSADao = new X509CertRSADao();
	
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
