package com.lnjoying.justice.cluster.manager.util.cert;

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
import java.security.spec.ECGenParameterSpec;

@Component
public class X509CertECDao extends X509CertBaseDao
{
    public static final String Default_Signature="SHA256withECDSA";
    public static final String ecCurveName = "prime256v1";
    
    public X509CertECDao()
    {
        setSignature(Default_Signature);
    }
    
    static
    {
        // 系统添加BC加密算法 以后系统中调用的算法都是BC的算法
        Security.addProvider(new BouncyCastleProvider());
    }

    public KeyPair generateKeyPair() throws Exception
    {
        //gen key pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(ecCurveName);
        kpg.initialize(ecGenParameterSpec);
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
            pemKeyConverter.setProvider("BC");
            KeyPair kp = pemKeyConverter.getKeyPair(pemKeyPair);
            return kp.getPrivate();
        }
        finally
        {
            pemParser.close();
        }
    }
}
