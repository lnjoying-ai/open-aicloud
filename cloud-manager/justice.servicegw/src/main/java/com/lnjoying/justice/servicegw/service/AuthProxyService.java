package com.lnjoying.justice.servicegw.service;

import com.lnjoying.justice.servicegw.common.TimeConst;
import com.lnjoying.justice.servicegw.config.ServerConfigBean;
import com.lnjoying.justice.servicegw.handler.authproxy.AuthHandler;
import com.lnjoying.justice.servicegw.handler.authproxy.ConvertHandler;
import com.lnjoying.justice.servicegw.handler.authproxy.TransHandler;
import com.lnjoying.justice.servicegw.secret.SslContextFactory;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.cluster.ClusterSecretInfo;
import com.micro.core.common.Utils;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.TcpAcceptor;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import javax.net.ssl.SSLEngine;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.lnjoying.justice.schema.common.ErrorCode.CLUSTER_SERVER_MISSING_CERTIFICATE;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/15/22 5:44 PM
 */
@Service
public class AuthProxyService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    ServerConfigBean serverConfigBean;
    
    @Autowired
    CombRpcService combRpcService;
    
    TcpAcceptor acceptor;
    
    public void start()
    {
        startSvr();
    }
    
//    @PostConstruct
    public void startSvr()
    {
        LOGGER.info("start auth proxy server");
        String enableSsl = System.getenv("ENABLE_SSL");
        
        int serverPort = serverConfigBean.getSvrPort();
        if (StringUtils.isNotBlank(enableSsl) && enableSsl.equals("true"))
        {
            if (! CollectionUtils.hasContent(serverConfigBean.getSvrCrt()) || ! CollectionUtils.hasContent(serverConfigBean.getSvrKey()) ||
                    ! CollectionUtils.hasContent(serverConfigBean.getSvrRootCrt()))
            {
                //to call req cert rpc and save cert
                ClusterSecretInfo clusterSecretInfo = combRpcService.getClusterManagerService().getClusterServerCert();
                if (Objects.isNull(clusterSecretInfo))
                {
                    throw new WebSystemException(CLUSTER_SERVER_MISSING_CERTIFICATE, ErrorLevel.ERROR);
                }
        
                serverConfigBean.setSvrCrt(clusterSecretInfo.getCrtPem());
                serverConfigBean.setSvrKey(clusterSecretInfo.getKeyPem());
                serverConfigBean.setSvrRootCrt(clusterSecretInfo.getRootPem());
                saveServerConfig();
            }
        }

        if (acceptor == null)
        {
            acceptor = new TcpAcceptor(socketChannel ->
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                
                if (StringUtils.isNotBlank(enableSsl) && enableSsl.equals("true"))
                {
                    SSLEngine engine = SslContextFactory.svrEngine(socketChannel.alloc(), serverConfigBean.getSvrCrt(), serverConfigBean.getSvrKey(), serverConfigBean.getSvrRootCrt());  //3
                    //////////////////////////////
                    LOGGER.info("recv conn from "+socketChannel.remoteAddress().toString());
                    pipeline.addLast(new SslHandler(engine, true));
                }
                
                pipeline.addLast(new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT,TimeUnit.SECONDS));
                pipeline.addLast(new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
                pipeline.addLast("httpDecoder", new HttpRequestDecoder());
                pipeline.addLast("httpEncoder", new HttpResponseEncoder());
                pipeline.addLast("authHandler", new AuthHandler(combRpcService));
                pipeline.addLast("convertHandler", new ConvertHandler());
                pipeline.addLast("transHandler", new TransHandler());
            });
        }
        
        try
        {
            acceptor.bind(new NetEntity("0.0.0.0", serverPort, NetEntity.ChannelType.SERVER));
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("bind auth proxy server error {}", e.getMessage());
            return;
        }
        LOGGER.info("start auth proxy success.");
        acceptor.loop();
    }
    
    void saveServerConfig()
    {
        String baseConfigPath = System.getProperty("lj_config");
        String svrConfigPath = Utils.buildStr(baseConfigPath, "/cluster_server.yaml");
        Yaml yaml = new Yaml();
        FileWriter fileWriter;
        try
        {
            fileWriter = new FileWriter(svrConfigPath);
        
            yaml.dump(serverConfigBean, fileWriter);
    
            fileWriter.close();
            String sslcertpem = Utils.buildStr(baseConfigPath, "/ssl", "/ssl.pem");
    
            fileWriter = new FileWriter(sslcertpem);
            fileWriter.write(serverConfigBean.getSvrCrt());
            fileWriter.flush();
            fileWriter.close();
    
            String sslkey = Utils.buildStr(baseConfigPath, "/ssl", "/ssl.key");
            fileWriter = new FileWriter(sslkey);
            fileWriter.write(serverConfigBean.getSvrKey());
            fileWriter.flush();
            fileWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
