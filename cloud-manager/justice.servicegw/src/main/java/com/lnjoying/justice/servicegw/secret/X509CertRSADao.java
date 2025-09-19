package com.lnjoying.justice.servicegw.secret;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Security;

@Component
public class X509CertRSADao extends X509CertBaseDao
{
    public static final String Default_KeyPairGenerator="RSA";
    public static final String Default_Signature="SHA256withRSA";
    
    
    static
    {
        // 系统添加BC加密算法 以后系统中调用的算法都是BC的算法
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public X509CertRSADao()
    {
        setSignature(Default_Signature);
    }

    public KeyPair generateKeyPair() throws Exception
    {
        //gen key pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(Default_KeyPairGenerator);
        kpg.initialize(Default_KeySize);
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }
    
    public  PrivateKey getPrivatekey(String privatKeyStr) throws Exception
    {
        StringReader reader = new StringReader(privatKeyStr);
        PEMParser pemParser = new PEMParser(reader);
        
        try
        {
            Object object = pemParser.readObject();
            
            if (object == null || !(object instanceof PEMKeyPair))
            {
                throw new IOException("Not an OpenSSL key");
            }
            
            PEMKeyPair pemKeyPair = (PEMKeyPair) object;
            JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();
            pemKeyConverter.setProvider("RSA");
            KeyPair kp = pemKeyConverter.getKeyPair(pemKeyPair);
            return kp.getPrivate();
        }
        finally
        {
            pemParser.close();
        }
    }
}
