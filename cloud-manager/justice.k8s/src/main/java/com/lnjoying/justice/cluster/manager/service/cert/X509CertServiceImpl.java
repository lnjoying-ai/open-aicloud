package com.lnjoying.justice.cluster.manager.service.cert;

import com.lnjoying.justice.cluster.manager.common.K8sRole;
import com.lnjoying.justice.cluster.manager.common.KeyCertName;
import com.lnjoying.justice.cluster.manager.config.ClusterManagerConfig;
import com.lnjoying.justice.cluster.manager.config.ClusterServerRootCA;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.EtcdServiceInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.KubeApiServiceInfo;
import com.lnjoying.justice.cluster.manager.domain.model.*;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.EnvUtil;
import com.lnjoying.justice.cluster.manager.util.YamlConfigUtils;
import com.lnjoying.justice.cluster.manager.util.cert.CertConfig;
import com.lnjoying.justice.cluster.manager.util.cert.CertDao;
import com.lnjoying.justice.cluster.manager.util.cert.CertDaoFactory;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Pair;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.jruby.ext.socket.SubnetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.lnjoying.justice.cluster.manager.common.KeyCertName.ClusterServerCACertName;
import static com.lnjoying.justice.cluster.manager.common.KeyCertName.ClusterServerCertName;
import static com.lnjoying.justice.cluster.manager.util.ClsUtils.needGenCert;

/**
 * @description supply x509cert service for k8s
 * @author Regulus
 * @date 11/29/21 5:14 PM
 */
@Service("x509CertService")
public class X509CertServiceImpl implements CertService
{
    
    @Autowired
    private CertDaoFactory certDaoFactory;

    @Autowired
    private ClusterManagerConfig clusterManagerConfig;
    
    private final int CertOverTime = 10;
    
    private String kubernetesURL = "https://127.0.0.1:6443";
    
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClusterDataService clusterDataService;

    @Autowired
    @Lazy
    private ClusterServerRootCA clusterServerRootCA;

    /**
     * @description generate jke certificates
     * @author Regulus
     * @date 11/18/21 7:52 PM
     * @param clusterInfo
     * @param rotate 
     * @return int
     */
    @Override
    public int genClusterCerts(ClusterInnerInfo clusterInfo, boolean rotate)
    {
        try
        {
            CertDao certDao = certDaoFactory.getCertDaoByKey("");
            genCaCerts(clusterInfo, rotate, certDao);
            genJKEComponentsCerts(clusterInfo, rotate, certDao);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return 0;
    }

    @Override
    public int genK3sClusterCerts(ClusterInnerInfo clusterInfo, boolean rotate)
    {
        try
        {
            CertDao certDao = certDaoFactory.getCertDaoByKey("");
            genSelfSignedCerts(clusterInfo, KeyCertName.K3S_SERVER_CA, certDao);
        }
        catch (Exception e)
        {
            LOGGER.info("gen k3s cluster cert error: {}", e);
        }

        return 0;
    }

    @Override
    public X509CertificateInfo genClusterServerCerts(String certName)
    {
        return genClusterServerCerts(certName, clusterServerRootCA);
    }
    
    public ClusterServerRootCA useUSerSsl()
    {
        if (needGenCert())
        {
            return null;
        }
    
        String key  = ClsUtils.readKey();
        String cert = ClsUtils.readCert();
        if (! CollectionUtils.hasContent(key) || ! CollectionUtils.hasContent(cert))
        {
            return null;
        }
    
        LOGGER.info("use user config ssl");
        ClusterServerRootCA caData = new ClusterServerRootCA();
        caData.setSvrRootCrt(cert);
        caData.setSvrRootKey(key);
        return caData;
    }
    
    @Override
    public ClusterServerRootCA genClusterServerCACerts()
    {
        try
        {
            ClusterServerRootCA caData = useUSerSsl();
            if (caData != null)
            {
                return caData;
            }
    
            LOGGER.info("generate root key and cert");
            CertDao certDao = certDaoFactory.getCertDaoByKey("");
            KeyPair keyPair = certDao.generateKeyPair();
            CertConfig certConfig = buildCertConfig(ClusterServerCACertName, "lnjoying", certDao);
            KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.keyCertSign);
            certConfig.setKeyUsage(keyUsage);
            certConfig.setIsCa(true);
            X509Certificate cert = certDao.createCert(keyPair, certConfig);
            X509CertificateInfo caCertInfo = buildCertInfo(ClusterServerCACertName, "", "", cert, keyPair, certDao);
            LOGGER.info("cluster server ca info:{}", caCertInfo.getCertificatePem());
            caData = new ClusterServerRootCA();
            caData.setSvrRootKey(caCertInfo.getKeyPem());
            caData.setSvrRootCrt(caCertInfo.getCertificatePem());
            YamlConfigUtils.writeYamlConfig("cluster_server_root_ca.yaml", caData);
            return caData;
        }
        catch (Exception e)
        {
            LOGGER.error("gen cluster server ca certs error:{}", e);
            throw new WebSystemException(ErrorCode.GEN_CLUSTER_SERVER_CA_CERTS_ERROR, ErrorLevel.ERROR);
        }
    }
    
    public X509CertificateInfo genClusterServerCerts(String certName, ClusterServerRootCA rootCA)
    {
        try
        {
            String svrRootCrt = rootCA.getSvrRootCrt();
            String svrRootKey = rootCA.getSvrRootKey();
            X509CertificateInfo caCertInfo = new X509CertificateInfo();
            caCertInfo.setCertificatePem(svrRootCrt);
            
            CertDao  svrCrtDao = null;
            if (StringUtils.isNotBlank(svrRootKey))
            {
                svrCrtDao = certDaoFactory.getCertDaoByKey(svrRootKey);
            }
            else
            {
                svrCrtDao = certDaoFactory.getCertDaoByCert(svrRootCrt);
            }
            
            caCertInfo.setCertificate(svrCrtDao.getX509Cert(svrRootCrt));
            caCertInfo.setKeyPem(svrRootKey);
            
            // server
            List<String> ips = new ArrayList<>();
            ips.add("127.0.0.1");
            //String clusterServerHost = RedisUtil.get(K8S_CLUSTER_SERVER_URL);
            String clusterServerHost = clusterManagerConfig.getClusterServerUrl();
            CertAltNames certAltNames = new CertAltNames();
            certAltNames.setIps(ips);
            certAltNames.setDnsNames(new ArrayList<>(Arrays.asList("localhost", "cluster-server.lnjoying.io")));
            
            if (StringUtils.isNotBlank(clusterServerHost))
            {
                String [] addressArray = StringUtils.split(clusterServerHost, ":");
                setAltNames(certAltNames, addressArray[0]);
            }
            
            Pair<X509Certificate, KeyPair> pair = genSignedCerts(true, null, caCertInfo, certAltNames, ClusterServerCertName, "", svrCrtDao);
            X509CertificateInfo clusterServerCertInfo = buildCertInfo(ClusterServerCertName, ClusterServerCertName, "", pair.getLeft(), pair.getRight(), svrCrtDao);
            LOGGER.info("cluster server  info:{}", clusterServerCertInfo.getCertificatePem());
            return clusterServerCertInfo;
        }
        catch (Exception e)
        {
            LOGGER.error("gen cluster server certs error:{}", e);
        }
        
        return null;
    }
    
    /**
     * @description generate root certificate
     * @author Regulus
     * @date 11/18/21 7:52 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genCaCerts(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        genMasterCerts(clusterInfo, rotate, certDao);
        genRequestHeaderCerts(clusterInfo, certDao);
        return 0;
    }
    
    /**
     * @description get self signed certs
     * @author Regulus
     * @date 11/18/21 7:53 PM
     * @param clusterInfo
     * @param certName 
     * @return int
     */
    private int genSelfSignedCerts(ClusterInnerInfo clusterInfo, String certName, CertDao certDao) throws Exception
    {
        KeyPair keyPair = certDao.generateKeyPair();
        CertConfig certConfig = buildCertConfig(certName, "", certDao);
        KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.keyCertSign);
        certConfig.setKeyUsage(keyUsage);
        certConfig.setIsCa(true);
        X509Certificate cert = certDao.createCert(keyPair, certConfig);
        
        X509CertificateInfo x509CertificateInfo = buildCertInfo(certName, "", "", cert, keyPair, certDao);
        clusterInfo.addCert(certName, x509CertificateInfo);
        return 0;
    }
    
    /**
     * @description genertate master cert
     * @author Regulus
     * @date 11/18/21 7:53 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genMasterCerts(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        genSelfSignedCerts(clusterInfo, KeyCertName.CACertName, certDao);
        return 0;
    }
    
    /**
     * @description build cert config
     * @author Regulus
     * @date 11/18/21 7:54 PM
     * @param commonName
     * @param organization
     * @return com.lnjoying.justice.cluster.manager.util.cert.CertConfig
     */
    CertConfig buildCertConfig(String commonName, String organization, CertDao certDao)
    {
        CertConfig certConfig = new CertConfig();
        X500Name subjectDn = certDao.buildName(commonName, organization);
        X500Name issuerDn = certDao.buildName(commonName, organization);
        
        certConfig.setSubjectDn(subjectDn);
        certConfig.setIssuer(issuerDn);
        certConfig.setSerial(BigInteger.valueOf(0));
        Date curDate = Date.from(LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC));
        certConfig.setNotBefore(curDate);
        certConfig.setNotAfter(ClsUtils.rollYear(curDate, CertOverTime));
        return certConfig;
    }
    
    /**
     * @description get kube cert config for service
     * @author Regulus
     * @date 11/18/21 7:54 PM
     * @param kubernetesURL
     * @param clusterName
     * @param componentName
     * @param caPath
     * @param crtPath
     * @param keyPath 
     * @return java.lang.String
     */
    String getKubeConfigX509(String kubernetesURL , String clusterName, String  componentName, String caPath, String crtPath, String keyPath) throws IOException, TemplateException
    {
        Map<String,Object> configMap = new HashMap<>();
        configMap.put("kubernetesURL", kubernetesURL);
        configMap.put("clusterName", clusterName);
        configMap.put("componentName", componentName);
        configMap.put("caPath", caPath);
        configMap.put("crtPath", crtPath);
        configMap.put("keyPath", keyPath);
        return clusterDataService.getTemplate("service-config-file", configMap);
    }
    
    /**
     * @description build cert obj info
     * @author Regulus
     * @date 11/18/21 7:55 PM
     * @param componentName
     * @param commonName
     * @param ouName
     * @param cert
     * @param keyPair
     * @return com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo
     */
    X509CertificateInfo buildCertInfo(String componentName, String commonName, String ouName, X509Certificate cert, KeyPair keyPair, CertDao certDao) throws Exception
    {
        X509CertificateInfo certInfo = new X509CertificateInfo();
        String envName = EnvUtil.getEnvFromName(componentName);
        
        certInfo.setCertEnvName(envName);
        certInfo.setCertificate(cert);
        certInfo.setKeyEnvName(EnvUtil.getKeyEnvFromEnv(envName));
        
        if (cert != null)
        {
            certInfo.setCertificatePem(certDao.convertX509ToPemStr(cert));
        }
        
        if (keyPair != null)
        {
            certInfo.setKeyPem(certDao.convertPrivateKey2Str(keyPair.getPrivate()));
            certInfo.setPublicKey(keyPair.getPublic());
            certInfo.setPrivateKey(keyPair.getPrivate());
        }
        
        certInfo.setCertPath(EnvUtil.getCertPath(componentName));
        certInfo.setKeyPath(EnvUtil.getKeyPath(componentName));
        
        if (! componentName.equals(KeyCertName.CACertName)
                && ! componentName.equals(KeyCertName.KubeAPICertName)
                && ! componentName.contains(KeyCertName.EtcdCertName)
                && ! componentName.contains(KeyCertName.KubeletCertName)
                && ! componentName.contains(KeyCertName.ServiceAccountTokenKeyName))
        {
            String caCertPath = EnvUtil.getCertPath(KeyCertName.CACertName);
            String config = getKubeConfigX509(kubernetesURL, "local", componentName, caCertPath, certInfo.getCertPath(), certInfo.getKeyPath());
            if (! CollectionUtils.hasContent(config))
            {
                LOGGER.info("{} config is empty", componentName);
            }
            certInfo.setConfigEnvName(EnvUtil.getConfigEnvFromEnv(envName));
            certInfo.setConfigPath(EnvUtil.getConfigPath(componentName));
            certInfo.setConfig(config);
        }
        
        certInfo.setName(componentName);
        certInfo.setOuName(ouName);
        certInfo.setCommonName(commonName);
        
        return certInfo;
    }

    /**
     * @description generate request header certs
     * @author Regulus
     * @date 11/18/21 7:55 PM
     * @param clusterInfo
     * @return int
     */
    private int genRequestHeaderCerts(ClusterInnerInfo clusterInfo, CertDao certDao) throws Exception
    {
        genSelfSignedCerts(clusterInfo, KeyCertName.RequestHeaderCACertName, certDao);
        return 0;
    }

    /**
     * @description generate k8s service certs
     * @author Regulus
     * @date 11/18/21 7:56 PM
     * @param clusterInfo
     * @param rotate 
     * @return int
     */
    private int genJKEComponentsCerts(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        genKubeApiCertificate(clusterInfo, rotate, certDao);
        genServiceTokenKey(clusterInfo, rotate, certDao);
        genKubeControllerCertificate(clusterInfo, rotate, certDao);
        genKubeSchedulerCertificate(clusterInfo, rotate, certDao);
        genKubeProxyCertificate(clusterInfo, rotate, certDao);
        genKubeNodeCertificate(clusterInfo, rotate, certDao);
        genKubeAdminCertificate(clusterInfo, rotate, certDao);
        genAPIProxyClientCertificate(clusterInfo, rotate, certDao);
        genEtcdCertificates(clusterInfo, rotate, certDao);
        
        EtcdServiceInfo etcdServiceInfo = clusterInfo.getJkeConfig().getServices().getEtcdServiceInfo();
        if (etcdServiceInfo != null && etcdServiceInfo.getExternalConfig() != null
                && ! CollectionUtils.isEmpty(etcdServiceInfo.getExternalConfig().getUrls()))
        {
            genExternalEtcdCertificates(clusterInfo, rotate, certDao);
        }
        
        return 0;
    }
    
    /**
     * @description get ip list from cidr list. return the netmask list
     * @author Regulus
     * @date 11/18/21 8:10 PM
     * @param ipCidrs 
     * @return java.util.List<java.lang.String>
     */
    private static List<String> getIpList(String ipCidrs)
    {
        String [] cidrArray = ipCidrs.split(",");
        List<String> maskList = new ArrayList<>();
        for (String ipCidr : cidrArray)
        {
            SubnetUtils utils = new SubnetUtils(ipCidr);
            SubnetUtils.SubnetInfo subnetInfo = utils.getInfo();
            String address = subnetInfo.getAddress();
            String [] ipArray = address.split("\\.");
            for (int i = ipArray.length - 1; i>=0; i--)
            {
                String p = ipArray[i];
                Integer ip = Integer.parseInt(p) + 1;
                if (ip > 0)
                {
                    ipArray[i] = ip.toString();
                    break;
                }
            }
            address = StringUtils.join(ipArray, ".");
            maskList.add(address);
        }
        
        return maskList;
    }
    
    /**
     * @description get node list by cluster role
     * @author Regulus
     * @date 11/18/21 8:11 PM
     * @param clusterNodePlans
     * @param role
     * @return java.util.List<com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo>
     */
    private List<ClusterNodeInfo> getNodeList(List<ClusterNodePlanInfo>  clusterNodePlans, String role)
    {
        List<ClusterNodeInfo> clusterNodeInfos = new ArrayList<>();
        for (ClusterNodePlanInfo nodePlanInfo : clusterNodePlans)
        {
            ClusterNodeInfo clusterNodeInfo = nodePlanInfo.getClusterNodeInfo();
            if (clusterNodeInfo.getClusterRoles().contains(role))
            {
                clusterNodeInfos.add(clusterNodeInfo);
            }
        }
        return clusterNodeInfos;
    }
    
    /**
     * @description init dns
     * @author Regulus
     * @date 11/18/21 8:11 PM
     * @param clusterDomain
     * @return java.util.List<java.lang.String>
     */
    List<String> initDnsNames(String clusterDomain)
    {
        List<String> dnsNames = new ArrayList<>();
        dnsNames.add("localhost");
        dnsNames.add("kubernetes");
        dnsNames.add("kubernetes.default");
        dnsNames.add("kubernetes.default.svc");
        dnsNames.add("kubernetes.default.svc."+clusterDomain);
        return dnsNames;
    }
    
    

    /**
     * @description if address is a ip, add it to ip list else add it to dns list
     * @author Regulus
     * @date 11/25/21 3:04 PM
     * @param certAltNames
     * @param clusterNodePlans 
     * @return void
     */
    void getAltNames(CertAltNames certAltNames, List<ClusterNodePlanInfo> clusterNodePlans, List<String> sans)
    {
        for (ClusterNodePlanInfo clusterNode : clusterNodePlans)
        {
            ClusterNodeInfo host = clusterNode.getClusterNodeInfo();
            
            if (StringUtils.isNotBlank(host.getExternalAddress()))
            {
                setAltNames(certAltNames, host.getExternalAddress());
            }
            
            if (StringUtils.isBlank(host.getExternalAddress()) || ! host.getInternalAddress().equals(host.getExternalAddress()))
            {
                setAltNames(certAltNames, host.getInternalAddress());
            }
        }
        
        if (! CollectionUtils.isEmpty(sans))
        {
            sans.forEach(host -> setAltNames(certAltNames, host));
        }
        
        certAltNames.getIps().add("127.0.0.1");
    }
    
    void setAltNames(CertAltNames certAltNames, String host)
    {
        if (ClsUtils.isIpAdress(host))
        {
            certAltNames.getIps().add(host);
        }
        else
        {
            certAltNames.getDnsNames().add(host);
        }
    }
    
    /**
     * @Description:
     * @Author: Regulus
     * @Date: 1/15/22 9:16 AM
     * @Param: clusterInfo
     * @Param: isServerCrt
     * @Param: keyPair
     * @Param: caCertInfo
     * @Param: certAltNames
     * @Param: commonName
     * @Param: orgName 
     * @Return: com.micro.core.common.Pair<java.security.cert.X509Certificate,java.security.KeyPair>
     */
    private Pair<X509Certificate, KeyPair> genSignedCerts(Boolean isServerCrt, KeyPair keyPair, X509CertificateInfo caCertInfo, CertAltNames certAltNames, String commonName, String orgName, CertDao certDao) throws Exception
    {
        if (keyPair == null)
        {
            keyPair = certDao.generateKeyPair();
        }
        
        CertConfig certConfig = buildCertConfig(commonName, orgName, certDao);
        certConfig.setSerial(BigInteger.valueOf(System.currentTimeMillis()));
        if (certAltNames != null)
        {
            if (!CollectionUtils.isEmpty(certAltNames.getIps()))
            {
                certConfig.setIpAddresses(certAltNames.getIps());
            }
            
            if (! CollectionUtils.isEmpty(certAltNames.getDnsNames()))
            {
                certConfig.setDnsNames(certAltNames.getDnsNames());
            }
        }
        
        ExtendedKeyUsage extendedKeyUsage = null;
        KeyUsage keyUsage = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);
        certConfig.setKeyUsage(keyUsage);
        if (isServerCrt)
        {
            KeyPurposeId [] purposeIds  = new KeyPurposeId[]{KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth};
            extendedKeyUsage = new ExtendedKeyUsage(purposeIds);
        }
        else
        {
            extendedKeyUsage =  new ExtendedKeyUsage(KeyPurposeId.id_kp_clientAuth);
        }
        certConfig.setExtendedKeyUsage(extendedKeyUsage);
        
        PrivateKey privateKey = certDao.getPrivatekey(caCertInfo.getKeyPem());
        X509Certificate cert = certDao.createCert(caCertInfo.getCertificate(), keyPair, privateKey, certConfig);
        return new Pair<>(cert, keyPair);
    }
    
    /**
     * @description gen kube-apiserver certs
     * @author Regulus
     * @date 11/18/21 8:37 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genKubeApiCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        //if not refresh cert, return.
        X509CertificateInfo x509CertificateInfo = clusterInfo.getCertInfo(KeyCertName.KubeAPICertName);
        if (x509CertificateInfo != null && x509CertificateInfo.getCertificate() != null && ! rotate)
        {
            return 0;
        }
        
        
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.CACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getKeyPem() == null)
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        List<ClusterNodePlanInfo> clusterNodePlans = clusterInfo.getClusterNodePlanInfos(K8sRole.CONTROLLER);;
        
        CertAltNames certAltNames = getCertAltNames(clusterInfo, clusterNodePlans);
        
        X509CertificateInfo kubeApiCertificateInfo = clusterInfo.getCertInfo(KeyCertName.KubeAPICertName);
        if (kubeApiCertificateInfo != null)
        {
            if (certAltNames.getDnsNames().equals(kubeApiCertificateInfo.getDnsNames())
                    && certAltNames.getIps().equals(kubeApiCertificateInfo.getIPAddresses()))
            {
                return 0;
            }
        }
        
        KeyPair keyPair = getKeyPair(x509CertificateInfo);

        String commonName = KeyCertName.KubeAPICertName;
        Pair<X509Certificate, KeyPair> pair = genSignedCerts(true, keyPair, caCertInfo, certAltNames, KeyCertName.KubeAPICertName, "", certDao);
        X509CertificateInfo dstCert = buildCertInfo(KeyCertName.KubeAPICertName, commonName, "", pair.getLeft(), pair.getRight(), certDao);
        clusterInfo.addCert(KeyCertName.KubeAPICertName, dstCert);
        return 0;
    }
    
    /**
     * @description gen service token key
     * @author Regulus
     * @date 11/18/21 8:37 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genServiceTokenKey(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        if (clusterInfo.getCertInfo(KeyCertName.ServiceAccountTokenKeyName) != null && ! rotate)
        {
            return 0;
        }
        
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.CACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getKeyPem() == null)
        {
            LOGGER.info("gen service token key error. ca mast cert is null");
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        X509CertificateInfo apiCertInfo = clusterInfo.getCertInfo(KeyCertName.KubeAPICertName);
        if (apiCertInfo == null)
        {
            LOGGER.info("gen service token key error. api cert is null");
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        PrivateKey privateKey = apiCertInfo.getPrivateKey();
        if (privateKey == null)
        {
            privateKey = certDao.getPrivatekey(apiCertInfo.getKeyPem());
        }
        PublicKey publicKey = apiCertInfo.getPublicKey();
        if (publicKey == null)
        {
            publicKey = apiCertInfo.getCertificate().getPublicKey();
        }
        KeyPair apiKeyPair = new KeyPair(publicKey, privateKey);
        Pair<X509Certificate, KeyPair> pair = genSignedCerts(false, apiKeyPair, caCertInfo, null, KeyCertName.ServiceAccountTokenKeyName, "", certDao);
        X509CertificateInfo dstCert = buildCertInfo(KeyCertName.ServiceAccountTokenKeyName,  KeyCertName.ServiceAccountTokenKeyName, "", pair.getLeft(), pair.getRight(), certDao);
        clusterInfo.addCert(KeyCertName.ServiceAccountTokenKeyName, dstCert);
        
        return 0;
    }
    
    /**
     * @description gen kube controller cert
     * @author Regulus
     * @date 11/29/21 5:16 PM
     * @param clusterInfo
     * @param rotate 
     * @return int
     */
    private int genKubeControllerCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        genSignCertificate(clusterInfo, KeyCertName.KubeControllerCertName, rotate, certDao);
        return 0;
    }
    
    /**
     * @description a common func for generating sign cert
     * @author Regulus
     * @date 11/29/21 5:16 PM
     * @param clusterInfo
     * @param certName
     * @param rotate
     * @return int
     */
    private int genSignCertificate(ClusterInnerInfo clusterInfo, String certName, boolean rotate, CertDao certDao) throws Exception
    {
        X509CertificateInfo x509CertificateInfo = clusterInfo.getCertInfo(certName);
        if (x509CertificateInfo != null && x509CertificateInfo.getCertificate() != null && ! rotate)
        {
            return 0;
        }
        
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.CACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getKeyPem() == null)
        {
            LOGGER.info("gen service token key error. ca mast cert is null");
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        KeyPair keyPair = getKeyPair(x509CertificateInfo);
        
        String commonName = EnvUtil.getDefaultCN(certName);
        Pair<X509Certificate, KeyPair> pair = genSignedCerts(false, keyPair, caCertInfo, null,
                commonName, "", certDao);
        
        X509CertificateInfo dstCert = buildCertInfo(certName,  commonName, "", pair.getLeft(), pair.getRight(), certDao);
        clusterInfo.addCert(certName, dstCert);
        return 0;
    }
    
    /**
     * @description gen kube scheduler cert
     * @author Regulus
     * @date 11/29/21 5:17 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genKubeSchedulerCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        genSignCertificate(clusterInfo, KeyCertName.KubeSchedulerCertName, rotate, certDao);
        
        return 0;
    }
    
    /**
     * @description gen kube proxy cert
     * @author Regulus
     * @date 11/29/21 5:17 PM
     * @param clusterInfo
     * @param rotate 
     * @return int
     */
    private int genKubeProxyCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        genSignCertificate(clusterInfo, KeyCertName.KubeProxyCertName, rotate, certDao);
        return 0;
    }
    
    /**
     * @description gen kube node cert
     * @author Regulus
     * @date 11/29/21 5:17 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genKubeNodeCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        X509CertificateInfo x509CertificateInfo = clusterInfo.getCertInfo(KeyCertName.KubeNodeCertName);
        if (x509CertificateInfo != null && x509CertificateInfo.getCertificate() != null && ! rotate)
        {
            return 0;
        }
        
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.CACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getKeyPem() == null)
        {
            LOGGER.info("gen service token key error. ca mast cert is null");
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }

        X509CertificateInfo proxyCert = clusterInfo.getCertInfo(KeyCertName.KubeProxyCertName);
        KeyPair keyPair = getKeyPair(proxyCert);
        Pair<X509Certificate, KeyPair> pair = genSignedCerts(false, keyPair, caCertInfo, null,
                KeyCertName.KubeNodeCommonName, KeyCertName.KubeNodeOrganizationName, certDao);
        
        X509CertificateInfo dstCert = buildCertInfo(KeyCertName.KubeNodeCertName, KeyCertName.KubeNodeCommonName, KeyCertName.KubeNodeOrganizationName, pair.getLeft(), pair.getRight(), certDao);
        clusterInfo.addCert(KeyCertName.KubeNodeCertName, dstCert);
        
        return 0;
    }
    
    /**
     * @description use template to gen kube config for x509 with data
     * @author Regulus
     * @date 11/29/21 5:18 PM
     * @param kubernetesURL
     * @param clusterName
     * @param componentName
     * @param cacrt
     * @param crt
     * @param key
     * @return java.lang.String
     */
    String getKubeConfigX509WithData(String kubernetesURL, String clusterName , String componentName, String cacrt, String crt, String key) throws IOException, TemplateException
    {
//        kubernetesURL string, clusterName string, componentName string, cacrt string, crt string, key strin
        Map<String,Object> configMap = new HashMap<>();
        configMap.put("kubernetesURL", kubernetesURL);
        configMap.put("clusterName", clusterName);
        configMap.put("componentName", componentName);
        configMap.put("cacrt", ClsUtils.getBase64(cacrt));
        configMap.put("crt", ClsUtils.getBase64(crt));
        configMap.put("key", ClsUtils.getBase64(key));
        return clusterDataService.getTemplate("local-config-file", configMap);
    }
    
    /**
     * @description gen kube admin cert
     * @author Regulus
     * @date 11/29/21 5:19 PM
     * @param clusterInfo
     * @param rotate 
     * @return int
     */
    private int genKubeAdminCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        X509CertificateInfo x509CertificateInfo = clusterInfo.getCertInfo(KeyCertName.KubeAdminCertName);
        if (x509CertificateInfo != null && x509CertificateInfo.getCertificate() != null && ! rotate)
        {
            return 0;
        }
        
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.CACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getKeyPem() == null)
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterInfo.getClusterNodePlanInfos(K8sRole.CONTROLLER);
        KeyPair keyPair = getKeyPair(x509CertificateInfo);
        
        Pair<X509Certificate, KeyPair> pair = genSignedCerts(false, keyPair, caCertInfo, null, KeyCertName.KubeAdminCertName, KeyCertName.KubeAdminOrganizationName, certDao);
        X509CertificateInfo dstCert = buildCertInfo(KeyCertName.KubeAdminCertName, KeyCertName.KubeAdminCertName, KeyCertName.KubeNodeOrganizationName, pair.getLeft(), pair.getRight(), certDao);
        clusterInfo.addCert(KeyCertName.KubeAdminCertName, dstCert);
        
        if (! CollectionUtils.isEmpty(clusterNodePlanInfos))
        {
            String config = getKubeConfigX509WithData(kubernetesURL, clusterInfo.getClusterName(), KeyCertName.KubeAdminCertName,
                    certDao.convertX509ToPemStr(caCertInfo.getCertificate()),
                    certDao.convertX509ToPemStr(dstCert.getCertificate()),
                    certDao.convertPrivateKey2Str(dstCert.getPrivateKey()));
            dstCert.setConfig(config);
            dstCert.setConfigPath(EnvUtil.getLocalKubeConfig(KeyCertName.ClusterConfig));
        }
        return 0;
    }
    
    KeyPair getKeyPair(X509CertificateInfo x509CertificateInfo)
    {
        if (x509CertificateInfo != null)
        {
            return new KeyPair(x509CertificateInfo.getPublicKey(), x509CertificateInfo.getPrivateKey());
        }
        return null;
    }
    
    /**
     * @description gen kube api proxy client cert
     * @author Regulus
     * @date 11/29/21 5:19 PM
     * @param clusterInfo
     * @param rotate 
     * @return int
     */
    private int genAPIProxyClientCertificate(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        X509CertificateInfo x509CertificateInfo = clusterInfo.getCertInfo(KeyCertName.APIProxyClientCertName);
        if (x509CertificateInfo != null && x509CertificateInfo.getCertificate() != null && ! rotate)
        {
            return 0;
        }
        
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.RequestHeaderCACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getPrivateKey() == null)
        {
            LOGGER.info("gen service token key error. ca mast cert is null");
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        KeyPair keyPair = getKeyPair(x509CertificateInfo);
        
        Pair<X509Certificate, KeyPair> pair = genSignedCerts(true, keyPair, caCertInfo, null,
                KeyCertName.APIProxyClientCertName, "", certDao);
        X509CertificateInfo dstCert = buildCertInfo(KeyCertName.APIProxyClientCertName, KeyCertName.APIProxyClientCertName, "", pair.getLeft(), pair.getRight(), certDao);
        clusterInfo.addCert(KeyCertName.APIProxyClientCertName, dstCert);
        
        return 0;
    }
    
    /**
     * @description get cert alt names
     * @author Regulus
     * @date 11/29/21 5:20 PM
     * @param clusterInfo
     * @param clusterNodePlans 
     * @return com.lnjoying.justice.cluster.manager.domain.model.CertAltNames
     */
    public CertAltNames getCertAltNames(ClusterInnerInfo clusterInfo, List<ClusterNodePlanInfo> clusterNodePlans)
    {
        CertAltNames certAltNames = new CertAltNames();
        KubeApiServiceInfo kubeApiServiceInfo = clusterInfo.getJkeConfig().getServices().getKubeApiService();
        certAltNames.setIps(getIpList(kubeApiServiceInfo.getServiceClusterIpRange()));
        String clusterDomain = clusterInfo.getJkeConfig().getServices().getKubeletServiceInfo().getClusterDomain();
        certAltNames.setDnsNames(initDnsNames(clusterDomain));
        List<String> sans = clusterInfo.getJkeConfig().getAuthentication().getSans();
        getAltNames(certAltNames, clusterNodePlans, sans);
        return certAltNames;
    }


    /**
     * @description delete unused cert
     * @author Regulus
     * @date 11/29/21 5:21 PM
     * @param clusterInfo
     * @param clusterNodePlanInfos
     * @param certNamePrefix
     * @return void
     */
    void deleteUnusedCert(ClusterInnerInfo clusterInfo, List<ClusterNodePlanInfo> clusterNodePlanInfos, String certNamePrefix)
    {
        Map<String, Boolean> unUsedCertMap = new HashMap<>();
        
        for (Map.Entry<String,X509CertificateInfo> entry : clusterInfo.getCertMap().entrySet())
        {
            if (entry.getKey().startsWith(certNamePrefix))
            {
                unUsedCertMap.put(entry.getKey(), true);
            }
        }
        
        for (ClusterNodePlanInfo nodePlan : clusterNodePlanInfos)
        {
            ClusterNodeInfo host = nodePlan.getClusterNodeInfo();
            String certName = EnvUtil.getCertNameForHost(host, certNamePrefix);
            unUsedCertMap.remove(certName);
        }
        
        for (Map.Entry<String,Boolean> entry : unUsedCertMap.entrySet())
        {
            clusterInfo.getCertMap().remove(entry.getKey());
        }
    }
    
    /**
     * @description gen etcd cert
     * @author Regulus
     * @date 11/29/21 5:22 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genEtcdCertificates(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        X509CertificateInfo caCertInfo = clusterInfo.getCertInfo(KeyCertName.CACertName);
        if  (caCertInfo == null || caCertInfo.getCertificate() == null || caCertInfo.getPrivateKey() == null)
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_CERT_ERROR, ErrorLevel.INFO);
        }
        
        List<ClusterNodePlanInfo> clusterNodePlanInfos = clusterInfo.getClusterNodePlanInfos(K8sRole.ETCD);
        CertAltNames certAltNames = getCertAltNames(clusterInfo, clusterNodePlanInfos);
        
        for (ClusterNodePlanInfo nodePlanInfo : clusterNodePlanInfos)
        {
            ClusterNodeInfo clusterNodeInfo = nodePlanInfo.getClusterNodeInfo();
            String etcdCertName = EnvUtil.getCertNameForHost(clusterNodeInfo, KeyCertName.EtcdCertName);
            X509CertificateInfo certInfo = clusterInfo.getCertInfo(etcdCertName);
            if (certInfo != null && ! StringUtils.isEmpty(certInfo.getCertificatePem()) && ! StringUtils.isEmpty(certInfo.getKeyPem()) &&  !rotate)
            {
                if (certAltNames.getIps().equals(certInfo.getIPAddresses()) && certAltNames.getDnsNames().equals(certInfo.getDnsNames()))
                {
                    continue;
                }
            }
            
            KeyPair keyPair = null;
            if (! rotate)
            {
                keyPair = getKeyPair(certInfo);
            }
            
            Pair<X509Certificate, KeyPair> pair = genSignedCerts(true, keyPair, caCertInfo, certAltNames, KeyCertName.EtcdCertName, "", certDao);
            X509CertificateInfo dstCert = buildCertInfo(etcdCertName,  etcdCertName, "", pair.getLeft(), pair.getRight(), certDao);
            clusterInfo.addCert(etcdCertName, dstCert);
            
        }
        
        deleteUnusedCert(clusterInfo, clusterNodePlanInfos, KeyCertName.EtcdCertName);
        return 0;
    }
    
    /**
     * @description gen external etcd
     * @author Regulus
     * @date 11/29/21 5:22 PM
     * @param clusterInfo
     * @param rotate
     * @return int
     */
    private int genExternalEtcdCertificates(ClusterInnerInfo clusterInfo, boolean rotate, CertDao certDao) throws Exception
    {
        String clientCrtStr = clusterInfo.getJkeConfig().getServices().getEtcdServiceInfo().getExternalConfig().getClientCert();
        String clientKey    = clusterInfo.getJkeConfig().getServices().getEtcdServiceInfo().getExternalConfig().getKey();
        X509Certificate x509Certificate = certDao.getX509Cert(clientCrtStr);
        PrivateKey privateKey = certDao.getPrivatekey(clientKey);
        
        KeyPair keyPair = new KeyPair(x509Certificate.getPublicKey(),privateKey);
        
        X509CertificateInfo certificateInfo = buildCertInfo(KeyCertName.EtcdClientCertName,EnvUtil.getDefaultCN(KeyCertName.EtcdClientCertName),"",x509Certificate, keyPair, certDao);
        clusterInfo.addCert(KeyCertName.EtcdClientCertName, certificateInfo);
        
        String cacert = clusterInfo.getJkeConfig().getServices().getEtcdServiceInfo().getExternalConfig().getCaCert();
        X509Certificate caCertificate = certDao.getX509Cert(cacert);
        X509CertificateInfo caCertInfo = buildCertInfo(KeyCertName.EtcdClientCACertName,EnvUtil.getDefaultCN(KeyCertName.EtcdClientCACertName),"",caCertificate, null, certDao);
        
        clusterInfo.addCert(KeyCertName.EtcdClientCACertName, caCertInfo);
        return 0;
    }
}
