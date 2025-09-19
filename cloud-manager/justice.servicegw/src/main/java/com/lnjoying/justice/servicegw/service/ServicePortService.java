package com.lnjoying.justice.servicegw.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.cluster.ClusterSecretInfo;
import com.lnjoying.justice.schema.entity.servicemanager.*;
import com.lnjoying.justice.servicegw.common.TimeConst;
import com.lnjoying.justice.servicegw.config.ServerConfigBean;
import com.lnjoying.justice.servicegw.handler.authproxy.ConvertHandler;
import com.lnjoying.justice.servicegw.handler.authproxy.TransHandler;
import com.lnjoying.justice.servicegw.handler.serviceport.ServicePortAuthHandler;
import com.lnjoying.justice.servicegw.handler.serviceport.TcpTransHandler;
import com.lnjoying.justice.servicegw.model.ServicePort;
import com.lnjoying.justice.servicegw.secret.SslContextFactory;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.nework.tcp.MultiPortTcpAcceptor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.SPIServiceUtils;
import org.apache.servicecomb.registry.api.Registration;
import org.apache.servicecomb.registry.api.registry.MicroserviceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLEngine;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lnjoying.justice.schema.common.ErrorCode.CLUSTER_SERVER_MISSING_CERTIFICATE;

@Service
public class ServicePortService
{
    private static Logger LOGGER = LogManager.getLogger();

    public static Map<String, NodeInfo> nodeInfoMap = new HashMap<>();

    public static Map<String, List<TargetServiceInfo>> targetServiceMap = new HashMap<>();

    public static Map<String, ServiceAgent> serviceAgentMap = new HashMap<>();

    @Autowired
    private ServerConfigBean serverConfigBean;

    @Autowired
    private CombRpcService combRpcService;

    private MicroserviceInstance microserviceInstance;

    MultiPortTcpAcceptor tcpAcceptor;

    MultiPortTcpAcceptor httpAcceptor;

    MultiPortTcpAcceptor httpsAcceptor;

    private Map<Integer, Map<String, Channel>> channelMap = new HashMap<>();

    private Map<Integer, Map<String, ServicePort>> servicePortMap = new HashMap<>();

    public MicroserviceInstance getMicroserviceInstance()
    {
        if (microserviceInstance != null)
        {
            return microserviceInstance;
        }

        List<Registration> registrationList = (List) SPIServiceUtils.getOrLoadSortedService(Registration.class).stream().filter((registration) -> {
            return registration.enabled();
        }).collect(Collectors.toList());

        if (registrationList == null || registrationList.size() < 2)
        {
            return null;
        }

        microserviceInstance = registrationList.get(1).getMicroservice().getInstance();

        return microserviceInstance;
    }

    public void registerServiceGateway()
    {
        try
        {
            String endpoint = getMicroserviceInstance().getEndpoints().get(0);
            String instanceId = getMicroserviceInstance().getInstanceId();
            RegisterServiceGatewayRsp registerServiceGatewayRsp = combRpcService.getServiceManagerService().registerServiceGateway(endpoint, instanceId);
            if (registerServiceGatewayRsp != null && !CollectionUtils.isEmpty(registerServiceGatewayRsp.getPorts()))
            {
                registerServiceGatewayRsp.getPorts().forEach(rpcCreatePortReq -> {
                    createPort(rpcCreatePortReq);
                });
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("register servicegateway to servicemanager error, {}", e.getMessage());
        }
    }

    public void initTcpAcceptor()
    {
        LOGGER.info("init service port tcp acceptor");
        String enableSsl = System.getenv("ENABLE_SSL");
        tcpAcceptor = new MultiPortTcpAcceptor(socketChannel ->
        {
            ChannelPipeline pipeline = socketChannel.pipeline();
            if (StringUtils.isNotBlank(enableSsl) && enableSsl.equals("true"))
            {
                SSLEngine engine = SslContextFactory.svrEngine(socketChannel.alloc(), serverConfigBean.getSvrCrt(), serverConfigBean.getSvrKey(), serverConfigBean.getSvrRootCrt());
                LOGGER.info("recv conn from "+socketChannel.remoteAddress().toString());
                pipeline.addLast(new SslHandler(engine, true));
            }
            pipeline.addLast(new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
            pipeline.addLast(new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
            pipeline.addLast("bytedecoder", new ByteArrayDecoder());
            pipeline.addLast("byteEncoder", new ByteArrayEncoder());
            pipeline.addLast("servicePortAuthHandler", new ServicePortAuthHandler(combRpcService, this));
            pipeline.addLast("convertHandler", new ConvertHandler());
            pipeline.addLast("tcpTransHandler", new TcpTransHandler());
        });
        LOGGER.info("start tcp acceptor success.");
    }

    public void initHttpAcceptor()
    {
        LOGGER.info("init service port http acceptor");
        String enableSsl = System.getenv("ENABLE_SSL");
        httpAcceptor = new MultiPortTcpAcceptor(socketChannel ->
        {
            ChannelPipeline pipeline = socketChannel.pipeline();
            if (StringUtils.isNotBlank(enableSsl) && enableSsl.equals("true"))
            {
                SSLEngine engine = SslContextFactory.svrEngine(socketChannel.alloc(), serverConfigBean.getSvrCrt(), serverConfigBean.getSvrKey(), serverConfigBean.getSvrRootCrt());
                LOGGER.info("recv conn from "+socketChannel.remoteAddress().toString());
                pipeline.addLast(new SslHandler(engine, true));
            }
            pipeline.addLast(new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
            pipeline.addLast(new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
            pipeline.addLast("httpDecoder", new HttpRequestDecoder());
            pipeline.addLast("httpEncoder", new HttpResponseEncoder());
            pipeline.addLast("servicePortAuthHandler", new ServicePortAuthHandler(combRpcService, this));
            pipeline.addLast("convertHandler", new ConvertHandler());
            pipeline.addLast("transHandler", new TransHandler());
        });
        LOGGER.info("start http acceptor success.");
    }

    public void initHttpsAcceptor()
    {
        LOGGER.info("init service port https acceptor");
        httpsAcceptor = new MultiPortTcpAcceptor(socketChannel ->
        {
            ChannelPipeline pipeline = socketChannel.pipeline();
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
            }
            SSLEngine engine = SslContextFactory.svrEngine(socketChannel.alloc(), serverConfigBean.getSvrCrt(), serverConfigBean.getSvrKey(), serverConfigBean.getSvrRootCrt());
            LOGGER.info("recv conn from "+socketChannel.remoteAddress().toString());
            pipeline.addLast(new SslHandler(engine, true));
            pipeline.addLast(new ReadTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
            pipeline.addLast(new WriteTimeoutHandler(TimeConst.TCP_TIMEOUT, TimeUnit.SECONDS));
            pipeline.addLast("httpDecoder", new HttpRequestDecoder());
            pipeline.addLast("httpEncoder", new HttpResponseEncoder());
            pipeline.addLast("servicePortAuthHandler", new ServicePortAuthHandler(combRpcService, this));
            pipeline.addLast("convertHandler", new ConvertHandler());
            pipeline.addLast("transHandler", new TransHandler());
        });
        LOGGER.info("start https acceptor success.");
    }

    private MultiPortTcpAcceptor getAcceptor(String protocol)
    {
        //http
        if (StringUtils.isNotEmpty(protocol) && protocol.equalsIgnoreCase("http"))
        {
            if (httpAcceptor == null)
            {
                initHttpAcceptor();
            }
            return httpAcceptor;
        }
        else if (StringUtils.isNotEmpty(protocol) && protocol.equalsIgnoreCase("https"))
        {
            if (httpsAcceptor == null)
            {
                initHttpsAcceptor();
            }
            return httpsAcceptor;
        }
        else
        {
            if (tcpAcceptor == null)
            {
                initTcpAcceptor();
            }
            return tcpAcceptor;
        }
    }
    
    public int createPort(RpcCreatePortReq rpcCreatePortReq)
    {
        MultiPortTcpAcceptor acceptor = getAcceptor(rpcCreatePortReq.getProtocol());

        Map<String, ServicePort> portMap = servicePortMap.get(rpcCreatePortReq.getListenPort());

        if (portMap == null)
        {
            servicePortMap.put(rpcCreatePortReq.getListenPort(), new HashMap<>());
            if (rpcCreatePortReq.getInternalIp() == null)
            {
                rpcCreatePortReq.setInternalIp("0.0.0.0");
            }
        }
        else
        {
            for (String internalIp : portMap.keySet())
            {
                ServicePort tmpServicePort = portMap.get(internalIp);
                try
                {
                    RpcCreatePortReq oldRpcCreatePortReq = combRpcService.getServiceManagerService().getRpcCreatePortReq(tmpServicePort.getTargetServiceId());
                    if (oldRpcCreatePortReq == null || ! oldRpcCreatePortReq.getListenPort().equals(rpcCreatePortReq.getListenPort()) ||
                            ! internalIp.equals(oldRpcCreatePortReq.getInternalIp()))
                    {
                        releasePort(tmpServicePort.getTargetServiceId(), internalIp, rpcCreatePortReq.getListenPort());
                    }
                    else
                    {
                        tmpServicePort = ServicePort.of(oldRpcCreatePortReq);
                        portMap.put(internalIp, tmpServicePort);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    LOGGER.error("get old RpcCreatePortReq error. {}", e.getMessage());
                    return ErrorCode.SERVICE_GATEWAY_PORT_CONFLICT.getCode();
                }
            }

            if (rpcCreatePortReq.getInternalIp() == null)
            {
                if (portMap.isEmpty())
                {
                    rpcCreatePortReq.setInternalIp("0.0.0.0");
                }
                else
                {
                    return ErrorCode.SERVICE_GATEWAY_PORT_CONFLICT.getCode();
                }
            }
            else
            {
                if (portMap.containsKey("0.0.0.0"))
                {
                    return ErrorCode.SERVICE_GATEWAY_PORT_CONFLICT.getCode();
                }
            }

            ServicePort servicePort = portMap.get(rpcCreatePortReq.getInternalIp());

            if (servicePort != null)
            {
                if (servicePort.getTargetServiceId().equals(rpcCreatePortReq.getTargetServiceId()))
                {
                    return ErrorCode.SUCCESS.getCode();
                }
                else
                {
                    return ErrorCode.SERVICE_GATEWAY_PORT_CONFLICT.getCode();
                }
            }
        }

        ServicePort servicePort = ServicePort.of(rpcCreatePortReq);
        servicePortMap.get(servicePort.getListenPort()).put(rpcCreatePortReq.getInternalIp(), servicePort);

        try
        {
            ChannelFuture channelFuture = acceptor.bind(new NetEntity(rpcCreatePortReq.getInternalIp(), servicePort.getListenPort(), NetEntity.ChannelType.SERVER));

            if (!channelMap.containsKey(servicePort.getListenPort()))
            {
                channelMap.put(servicePort.getListenPort(), new HashMap<>());
            }
            channelMap.get(servicePort.getListenPort()).put(rpcCreatePortReq.getInternalIp(), channelFuture.channel());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("bind port error {}", e.getMessage());
        }

        return ErrorCode.SUCCESS.getCode();
    }

    public Integer releasePort(String targetPortId, String internalIp, Integer listenPort)
    {
        Map<String, ServicePort> portMap = servicePortMap.get(listenPort);
        if (portMap == null)
        {
            return ErrorCode.SUCCESS.getCode();
        }
        ServicePort servicePort = portMap.get(internalIp);
        if (servicePort == null)
        {
            return ErrorCode.SUCCESS.getCode();
        }

        if (! servicePort.getTargetServiceId().equals(targetPortId))
        {
            return ErrorCode.SERVICE_GATEWAY_PORT_CONFLICT.getCode();
        }

        try
        {
            servicePort.getTargets().clear();
            MultiPortTcpAcceptor acceptor = getAcceptor(servicePort.getProtocol());
            Map<String, Channel> ipChannelMap = channelMap.get(listenPort);
            if (ipChannelMap != null)
            {
                Channel channel = ipChannelMap.get(internalIp);
                if (acceptor != null && channel != null)
                {
                    acceptor.removeChannel(channel.hashCode());
                }
                ipChannelMap.remove(internalIp);
            }
            if (ipChannelMap != null && ipChannelMap.isEmpty())
            {
                channelMap.remove(listenPort);
            }
            portMap.remove(internalIp);
            if (portMap.isEmpty())
            {
                servicePortMap.remove(listenPort);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("release port error {}", e.getMessage());
        }
        return ErrorCode.SUCCESS.getCode();
    }

    public ServicePort getServicePort(String internalIp, Integer listenPort)
    {
        Map<String, ServicePort> portMap = servicePortMap.get(listenPort);
        if (portMap == null)
        {
            return null;
        }
        ServicePort servicePort = portMap.get(internalIp);
        if (servicePort == null)
        {
            servicePort = portMap.get("0.0.0.0");
        }
        return servicePort;
    }

    public NodeInfo getNodeInfo(String nodeId)
    {
        Long timestamp = new Date().getTime();
        NodeInfo nodeInfo = nodeInfoMap.get(nodeId);
        if (nodeInfo == null || (timestamp - nodeInfo.getTimestamp()) > serverConfigBean.getNodeInfoCacheTime())
        {
            nodeInfo = combRpcService.getEdgeResourceService().getNode(nodeId);
            nodeInfo.setTimestamp(new Date().getTime());
            nodeInfoMap.put(nodeId, nodeInfo);
        }
        return nodeInfo;
    }

    public List<TargetServiceInfo> getContainers(String specId, String instId)
    {
        Long timestamp = new Date().getTime();
        List<TargetServiceInfo> targetServiceInfos;
        if (instId != null)
        {
            targetServiceInfos = targetServiceMap.get(instId);
        }
        else
        {
            targetServiceInfos = targetServiceMap.get(specId);
        }

        if (targetServiceInfos == null || (timestamp - targetServiceInfos.get(0).getTimestamp()) > 600000)
        {
            targetServiceInfos = combRpcService.getContainerInstService().getContainer(specId, instId);

            targetServiceInfos.forEach(targetServiceInfo -> {
                targetServiceInfo.setTimestamp(timestamp);
            });

            if (instId != null)
            {
                targetServiceMap.put(instId, targetServiceInfos);
            }
            else
            {
                targetServiceMap.put(specId, targetServiceInfos);
            }
        }
        return targetServiceInfos;
    }

    public List<TargetServiceInfo> getStacks(String specId, String stackId)
    {
        Long timestamp = new Date().getTime();
        List<TargetServiceInfo> targetServiceInfos;
        if (stackId != null)
        {
            targetServiceInfos = targetServiceMap.get(stackId);
        }
        else
        {
            targetServiceInfos = targetServiceMap.get(specId);
        }

        if (targetServiceInfos == null || (timestamp - targetServiceInfos.get(0).getTimestamp()) > 600000)
        {
            targetServiceInfos = combRpcService.getAosService().getStack(specId, stackId);

            targetServiceInfos.forEach(targetServiceInfo -> {
                targetServiceInfo.setTimestamp(timestamp);
            });

            if (stackId != null)
            {
                targetServiceMap.put(stackId, targetServiceInfos);
            }
            else
            {
                targetServiceMap.put(specId, targetServiceInfos);
            }
        }
        return targetServiceInfos;
    }

    public String getServiceAgent(String siteId)
    {
        String agentId = null;
        Long timestamp = new Date().getTime();
        ServiceAgent serviceAgent = serviceAgentMap.get(siteId);
        if (serviceAgent == null || (timestamp - serviceAgent.getTimestamp()) > 600000)
        {
            CombRetPacket retPacket = combRpcService.getEdgeResourceService().getServiceAgent(siteId);
            if (retPacket.getStatus() != ErrorCode.SUCCESS.getCode())
            {
                LOGGER.error("{} get service agent failed.", siteId);
                return agentId;
            }
            else if (retPacket.getObj() == null)
            {
                LOGGER.error("{} get service agent creating.", siteId);
                return agentId;
            }
            agentId = (String) retPacket.getObj();
            serviceAgentMap.put(siteId, new ServiceAgent(agentId, timestamp));
        }
        else
        {
            agentId = serviceAgent.getAgentId();
        }
        return agentId;
    }

    public List<PortStatus> getPortStatus()
    {
        List<PortStatus> portStatuses = new ArrayList<>();
        servicePortMap.forEach((port, portMap) -> {
            portMap.forEach((ip, servicePort) ->{
                PortStatus portStatus = new PortStatus();
                portStatus.setServicePortId(servicePort.getServicePortId());
                portStatus.setTargetType(servicePort.getTargetType());
                portStatus.setDeployment(servicePort.getDeployment());
                portStatus.setTargetServiceId(servicePort.getTargetServiceId());
                portStatus.setService(servicePort.getService());
                portStatus.setTargetPort(servicePort.getTargetPort());
                portStatus.setInternalIp(servicePort.getInternalIp());
                portStatus.setExternalIp(servicePort.getExternalIp());
                portStatus.setPort(servicePort.getPort());
                portStatus.setListenPort(servicePort.getListenPort());
                Channel channel = channelMap.containsKey(port) ? channelMap.get(port).get(ip) : null;
                portStatus.setStatus(channel == null ? null : String.valueOf(channel.isOpen()));
                portStatuses.add(portStatus);
            });
        });
        return portStatuses;
    }
}
