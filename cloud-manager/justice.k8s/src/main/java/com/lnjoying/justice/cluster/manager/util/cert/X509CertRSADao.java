package com.lnjoying.justice.cluster.manager.util.cert;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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
    
    X509Certificate buildCert(CertConfig certConfig,X509v3CertificateBuilder builder, PrivateKey privateKey) throws IOException, OperatorCreationException, CertificateException
    {
        if (certConfig.getKeyUsage() != null)
        {
            builder.addExtension(Extension.keyUsage, true, certConfig.getKeyUsage());
        }
        
        if (certConfig.getExtendedKeyUsage() != null)
        {
            builder.addExtension(Extension.extendedKeyUsage, false, certConfig.getExtendedKeyUsage());
        }
        
        if (certConfig.getIsCa())
        {
            builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        }
        
        List<ASN1Encodable> encodeAbleList = null;
        if (! CollectionUtils.isEmpty(certConfig.getDnsNames()))
        {
            if (encodeAbleList == null)
            {
                encodeAbleList = new ArrayList<>();
            }
            for (String dns : certConfig.getDnsNames())
            {
                encodeAbleList.add(new GeneralName(GeneralName.dNSName, dns));
            }
        }
        
        if (! CollectionUtils.isEmpty(certConfig.getIpAddresses()))
        {
            if (encodeAbleList == null)
            {
                encodeAbleList = new ArrayList<>();
            }
            for (String ip : certConfig.getIpAddresses())
            {
                encodeAbleList.add(new GeneralName(GeneralName.iPAddress, ip));
            }
        }
        
        if (! CollectionUtils.isEmpty(encodeAbleList))
        {
            builder.addExtension(Extension.subjectAlternativeName, false, new DERSequence(encodeAbleList.toArray(new ASN1Encodable[0])));
        }
        
        //证书的签名数据
        ContentSigner sigGen = new JcaContentSignerBuilder(Default_Signature).build(privateKey);
        X509CertificateHolder holder = builder.build(sigGen);
        
        byte[] certBuf = holder.getEncoded();
        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance(cert_type).generateCertificate(new ByteArrayInputStream(certBuf));
        return certificate;
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
//            pemKeyConverter.setProvider("RSA");
            KeyPair kp = pemKeyConverter.getKeyPair(pemKeyPair);
            return kp.getPrivate();
        }
        finally
        {
            pemParser.close();
        }
    }
}
