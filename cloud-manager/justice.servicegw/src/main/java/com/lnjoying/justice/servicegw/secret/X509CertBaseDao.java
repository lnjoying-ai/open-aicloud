package com.lnjoying.justice.servicegw.secret;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class X509CertBaseDao implements  CertDao
{
    public static final String Default_keyType="PKCS12";
    
    public static final String cert_type="X509";
    public static final Integer Default_KeySize=2048;

    static
    {
        // 系统添加BC加密算法 以后系统中调用的算法都是BC的算法
        Security.addProvider(new BouncyCastleProvider());
    }
    
    private String signature="SHA256withRSA";
    
    public void setSignature(String sig)
    {
        signature = sig;
    }
    
    public KeyPair generateKeyPair() throws Exception
    {
        return null;
    }
    
    private CertificateFactory CERTIFICATE_FACTORY = null;
    
    X509CertBaseDao()
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
    
    public X509Certificate getX509Cert(String certStr) throws CertificateException
    {
        InputStream stream = new ByteArrayInputStream(certStr.getBytes(StandardCharsets.UTF_8));
        X509Certificate cert = (X509Certificate) CERTIFICATE_FACTORY.generateCertificate(stream);
        return cert;
    }
    
    public  PrivateKey getPrivatekey(String privatKeyStr) throws Exception
    {
        return null;
    }
}
