package com.lnjoying.justice.cluster.manager.util.cert;

import com.lnjoying.justice.commonweb.util.CollectionUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class X509CertBaseDao implements  CertDao
{
    public static final String Default_keyType="PKCS12";
//    public static final String Default_Signature="SHA256WithRSAEncryption";
    
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
    
    public X500Name buildName(String commonName, String organization)
    {
        X500NameBuilder nameBuilder = new X500NameBuilder();
        
        if (!commonName.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.CN, commonName);
        }
        
        if (!organization.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.O, organization);
        }
        
        return nameBuilder.build();
    }
    
    public X500Name buildName(String commonName, String organization, String organizationUnit, String locality,
                              String state, String country)
    {
        X500NameBuilder nameBuilder = new X500NameBuilder();
        
        if (!commonName.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.CN, commonName);
        }
        
        if (!organizationUnit.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.OU, organizationUnit);
        }
        if (!organization.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.O, organization);
        }
        
        if (!locality.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.L, locality);
        }
        
        if (!state.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.ST, state);
        }
        
        if (!country.isEmpty())
        {
            nameBuilder.addRDN(BCStyle.C, country);
        }
        
        return nameBuilder.build();
    }
    
    @Override
    public X509Certificate  createCert(KeyPair keyPair, CertConfig certConfig) throws Exception
    {
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        //组装公钥信息
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(certConfig.getIssuer(), certConfig.getSerial(),
                certConfig.notBefore, certConfig.notAfter, certConfig.getSubjectDn(), publicKey);
        return buildCert(certConfig, builder, privateKey);
    }
    
    @Override
    public X509Certificate createCert(X509Certificate parentCert, KeyPair keyPair, PrivateKey privateKey, CertConfig certConfig) throws Exception
    {
        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
        
        //组装公钥信息
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(parentCert, certConfig.getSerial(),
                certConfig.notBefore, certConfig.notAfter, certConfig.getSubjectDn(), publicKey);
        return buildCert(certConfig, builder, privateKey);
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
        ContentSigner sigGen = new JcaContentSignerBuilder(signature).build(privateKey);
        X509CertificateHolder holder = builder.build(sigGen);
        
        byte[] certBuf = holder.getEncoded();
        X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance(cert_type).generateCertificate(new ByteArrayInputStream(certBuf));
        return certificate;
    }
    
    @Override
    public void printCert(String certPath, String keyPassword) throws Exception
    {
        char[] charArray = keyPassword.toCharArray();
        KeyStore ks = KeyStore.getInstance(Default_keyType);
        FileInputStream fis = new FileInputStream(certPath);
        ks.load(fis, charArray);
        fis.close();
        System.out.println("keystore type=" + ks.getType());
        Enumeration enumas = ks.aliases();
        String keyAlias = null;
        if (enumas.hasMoreElements())
        {
            keyAlias = (String)enumas.nextElement();
            System.out.println("alias=[" + keyAlias + "]");
        }
        System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
        PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias,charArray );
        Certificate cert = ks.getCertificate(keyAlias);
        PublicKey pubkey = cert.getPublicKey();
        System.out.println("cert class = " + cert.getClass().getName());
        System.out.println("cert = " + cert);
        System.out.println("public key = " + pubkey);
        System.out.println("private key = " + prikey);
    }


    @Override
    public  PublicKey getPublicKey(String certPath, String keyPassword) throws Exception
    {
        char[] charArray = keyPassword.toCharArray();
        KeyStore ks = KeyStore.getInstance(Default_keyType);
        FileInputStream fis = new FileInputStream(certPath);
        ks.load(fis, charArray);
        fis.close();
        Enumeration enumas = ks.aliases();
        String keyAlias = null;
        if (enumas.hasMoreElements())
        {
            keyAlias = (String)enumas.nextElement();
//            Certificate certificate = ks.getCertificate(keyAlias);
            return ks.getCertificate(keyAlias).getPublicKey();
        }
        return null;
    }

    @Override
    public  PrivateKey getPrivateKey(String certPath, String keyPassword) throws Exception
    {
        char[] charArray = keyPassword.toCharArray();
        KeyStore ks = KeyStore.getInstance(Default_keyType);
        FileInputStream fis = new FileInputStream(certPath);
        ks.load(fis, charArray);
        fis.close();
        Enumeration enumas = ks.aliases();
        String keyAlias = null;
        if (enumas.hasMoreElements())
        {
            keyAlias = (String)enumas.nextElement();
//            Certificate certificate = ks.getCertificate(keyAlias);
            return (PrivateKey) ks.getKey(keyAlias, charArray);
        }
        return null;
    }


    @Override
    public void certDelayTo(Date endTime,String certPath,String password) throws Exception
    {

    }

    @Override
    public void changePassword(String certPath,String oldPwd,String newPwd) throws Exception
    {
        KeyStore ks = KeyStore.getInstance(Default_keyType);
        FileInputStream fis = new FileInputStream(certPath);
        ks.load(fis, oldPwd.toCharArray());
        fis.close();
        FileOutputStream output = new  FileOutputStream(certPath);
        ks.store(output,newPwd.toCharArray());
        output.close();
    }

    @Override
    public void deleteAlias(String certPath,String password,String alias,String entry) throws Exception
    {
        char[] charArray = password.toCharArray();
        KeyStore ks = KeyStore.getInstance(Default_keyType);
        FileInputStream fis = new FileInputStream(certPath);
        ks.load(fis, charArray);
        fis.close();
        if (ks.containsAlias(alias))
        {
            ks.deleteEntry(entry);
            FileOutputStream output = new  FileOutputStream(certPath );
            ks.store(output,password.toCharArray());
            output.close();
        }
        else
        {
            throw new Exception("cert do not contain alias--->"+alias);
        }
    }

    public  String convertX509ToPemStr(X509Certificate x509Cert) throws Exception
    {
        StringWriter str = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(str);
        pemWriter.writeObject(x509Cert);
        pemWriter.close();
        str.close();
        return str.toString();
    }

    public  String convertPrivateKey2Str(PrivateKey priv) throws Exception
    {
        ByteArrayOutputStream  bos = new ByteArrayOutputStream();
        JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
        pemWriter.writeObject(priv);
        
        pemWriter.close();
        return new String(bos.toByteArray());
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
    
    public static String read(String fillePath)
    {
        String str="";
        
        File file=new File(fillePath);
        
        try {
            
            FileInputStream in=new FileInputStream(file);
            int size=in.available();
            
            byte[] buffer=new byte[size];
            
            in.read(buffer);
            
            in.close();
            
            str=new String(buffer,"UTF-8");
            
        } catch (IOException e)
        {
            
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
}
