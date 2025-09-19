package com.lnjoying.justice.servicegw.handler.serviceport;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.schema.entity.servicemanager.NodeInfo;
import com.lnjoying.justice.schema.entity.servicemanager.TargetPort;
import com.lnjoying.justice.schema.entity.servicemanager.TargetServiceInfo;
import com.lnjoying.justice.servicegw.config.ServerConfigBean;
import com.lnjoying.justice.servicegw.model.ClusterProxyMsg;
import com.lnjoying.justice.servicegw.model.ServicePort;
import com.lnjoying.justice.servicegw.service.ServicePortService;
import com.lnjoying.justice.servicegw.service.rpc.CombRpcService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import java.util.Date;
import java.util.List;

import static com.lnjoying.justice.schema.common.ErrorCode.RPC_AUTH_CLUSTER_FAILED;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ServicePortAuthHandler extends SimpleChannelInboundHandler<Object>
{
    private static Logger LOGGER = LogManager.getLogger();

    private ServicePortService servicePortService;

    private CombRpcService combRpcService;

    private boolean enalbeUpgrade;

    private ServicePort servicePort;

    ServerConfigBean serverConfigBean;

    private Integer position = 0;

    public ServicePortAuthHandler(CombRpcService combRpcService, ServicePortService servicePortService)
    {
        this.combRpcService = combRpcService;
        this.servicePortService = servicePortService;
        serverConfigBean = BeanUtils.getBean("serverConfigBean");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
    
    private static void notify100Continue(ChannelHandlerContext ctx)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }
    
    boolean refreshServicePort()
    {
        //to do: call auth rpc
        try
        {
            switch (servicePort.getTargetType())
            {
                case "k8s":
                {
                    CombRetPacket retPacket = combRpcService.getClusterManagerService().getClusterAgent(servicePort.getDeployment());
                    if (retPacket.getStatus() != ErrorCode.SUCCESS.getCode())
                    {
                        LOGGER.error("{} get cluster agent failed.", servicePort.getDeployment());
                        return false;
                    }

                    String agentId = (String) retPacket.getObj();
                    servicePort.getTargets().forEach(targetPort -> {
                        targetPort.setAgentId(agentId);
                    });
                    break;
                }
                case "cloud":
                {
                    CombRetPacket retPacket = combRpcService.getCmpManagerService().getCloudAgent(servicePort.getDeployment());

                    if (retPacket.getStatus() != ErrorCode.SUCCESS.getCode())
                    {
                        LOGGER.error("{} get cloud agent failed.", servicePort.getDeployment());
                        return false;
                    }

                    String agentId = (String) retPacket.getObj();
                    servicePort.getTargets().forEach(targetPort -> {
                        targetPort.setAgentId(agentId);
                    });
                    break;
                }
                case "other":
                {
                    for (TargetPort targetPort : servicePort.getTargets())
                    {
                        String agentId = servicePortService.getServiceAgent(targetPort.getSiteId());
                        targetPort.setAgentId(agentId);
                    }
                    break;
                }
                case "edge":
                {
                    NodeInfo nodeInfo = servicePortService.getNodeInfo(servicePort.getDeployment());
                    for (TargetPort targetPort : servicePort.getTargets())
                    {
                        targetPort.setRegionId(nodeInfo.getRegionId());
                        targetPort.setSiteId(nodeInfo.getSiteId());
                        targetPort.setNodeId(nodeInfo.getNodeId());
                        targetPort.setTargetIp(nodeInfo.getIp());
                        String agentId = servicePortService.getServiceAgent(targetPort.getSiteId());
                        targetPort.setAgentId(agentId);
                    }
                    break;
                }
                case "container":
                {
                    servicePort.getTargets().clear();
                    List<TargetServiceInfo> targetServiceInfos = servicePortService.getContainers(servicePort.getDeployment(), servicePort.getService());
                    targetServiceInfos.forEach(targetServiceInfo -> {
                        TargetPort targetPort = new TargetPort(targetServiceInfo.getRegionId(), targetServiceInfo.getSiteId(),
                                targetServiceInfo.getNodeId(), null ,null, servicePort.getTargetPort());
                        NodeInfo nodeInfo = servicePortService.getNodeInfo(targetPort.getNodeId());
                        targetPort.setRegionId(nodeInfo.getRegionId());
                        targetPort.setSiteId(nodeInfo.getSiteId());
                        targetPort.setNodeId(nodeInfo.getNodeId());
                        targetPort.setTargetIp(nodeInfo.getIp());
                        String agentId = servicePortService.getServiceAgent(targetPort.getSiteId());
                        targetPort.setAgentId(agentId);
                        servicePort.getTargets().add(targetPort);
                    });
                    break;
                }
                case "compose":
                {
                    servicePort.getTargets().clear();
                    List<TargetServiceInfo> targetServiceInfos = servicePortService.getStacks(servicePort.getDeployment(), servicePort.getService());
                    targetServiceInfos.forEach(targetServiceInfo -> {
                        TargetPort targetPort = new TargetPort(targetServiceInfo.getRegionId(), targetServiceInfo.getSiteId(),
                                targetServiceInfo.getNodeId(), null ,null, servicePort.getTargetPort());
                        NodeInfo nodeInfo = servicePortService.getNodeInfo(targetPort.getNodeId());
                        targetPort.setRegionId(nodeInfo.getRegionId());
                        targetPort.setSiteId(nodeInfo.getSiteId());
                        targetPort.setNodeId(nodeInfo.getNodeId());
                        targetPort.setTargetIp(nodeInfo.getIp());
                        String agentId = servicePortService.getServiceAgent(targetPort.getSiteId());
                        targetPort.setAgentId(agentId);
                        servicePort.getTargets().add(targetPort);
                    });
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(RPC_AUTH_CLUSTER_FAILED, ErrorLevel.ERROR);
        }
        servicePort.setTimestamp(new Date().getTime());
        return true;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
        LOGGER.info("channel {} recv service port req, recv http request: {}", ctx.channel().hashCode(),  msg);

        if (servicePort == null)
        {
            String[] temp = ctx.channel().localAddress().toString().split(":");
            servicePort = servicePortService.getServicePort(temp[0].replace("/", ""), Integer.valueOf(temp[1]));
            if (servicePort == null)
            {
                LOGGER.info("service port is null");
                return;
            }
        }
        
        if (msg instanceof HttpRequest)
        {
            HttpRequest req = (HttpRequest) msg;
            if (HttpUtil.is100ContinueExpected(req))
            {
                notify100Continue(ctx);
            }

            if (null == servicePort)
            {
                ctx.close();
                return;
            }

            if ((new Date().getTime() - servicePort.getTimestamp()) > serverConfigBean.getServicePortCacheTime())
            {
                LOGGER.info("refresh servicePort, servicePortId: {}, targetServiceId: {}", servicePort.getServicePortId(), servicePort.getTargetServiceId());
                if (! refreshServicePort())
                {
                    ctx.close();
                }
            }
            
            String connection = req.headers().get(HttpHeaderNames.CONNECTION);
            if (StringUtils.isNotBlank(connection))
            {
                if (connection.toUpperCase().contains("UPGRADE"))
                {
                    String dst = req.headers().get(HttpHeaderNames.UPGRADE);
                    dst = dst.toUpperCase();
                    if (! dst.isEmpty())
                    {
                        enalbeUpgrade = true;
                    }
                }
            }
        }
        else if (msg instanceof HttpContent)
        {
            LOGGER.debug("auth refcnt {}", ((HttpContent) msg).refCnt());
            
            if (msg instanceof LastHttpContent)
            {
                if (enalbeUpgrade)
                {
                    setAuthDecodePipeLine(ctx.pipeline());
                }
            }
        }
        else
        {
            if (servicePort != null && (new Date().getTime() - servicePort.getTimestamp()) > serverConfigBean.getServicePortCacheTime())
            {
                LOGGER.info("refresh servicePort, servicePortId: {}, targetServiceId: {}", servicePort.getServicePortId(), servicePort.getTargetServiceId());
                if (! refreshServicePort())
                {
                    ctx.close();
                }
            }
        }
        
        ClusterEntity clusterEntity = getClusterEntity();
        LOGGER.info("send to worker {}", clusterEntity.getWorkderId());
        ClusterProxyMsg proxyMsg = new ClusterProxyMsg();
        proxyMsg.setClusterEntity(clusterEntity);
        proxyMsg.setReqObj(msg);
        ctx.fireChannelRead(proxyMsg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        LOGGER.info("service port auth handler error, port: {}", servicePort.getListenPort());
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("service port handler inactive, port: {}", servicePort.getListenPort());
        ctx.fireChannelInactive();
    }
    
    void setAuthDecodePipeLine(ChannelPipeline pipeline)
    {
        pipeline.replace("httpDecoder", "byteDecoder", new ByteArrayDecoder());
    }

    private ClusterEntity getClusterEntity()
    {
        position = (position + 1) % servicePort.getTargets().size();
        TargetPort targetPort = servicePort.getTargets().get(position);

        ClusterEntity clusterEntity = new ClusterEntity();
        clusterEntity.setClusterId(servicePort.getTargetServiceId());
        clusterEntity.setWorkderId(targetPort.getAgentId());
        clusterEntity.setClusterIp(targetPort.getTargetIp());
        clusterEntity.setClusterPort(targetPort.getTargetPort());
        clusterEntity.setToken(null);
        clusterEntity.setClusterCaCrtPem(servicePort.getCert());
        clusterEntity.setUserCrtPem(null);
        clusterEntity.setUserKeyPem(null);
        clusterEntity.setClsvrUrl(null);
        clusterEntity.setTimestamp(0);
        clusterEntity.setVendor("serviceport");
        clusterEntity.setClusterType("serviceport");
        clusterEntity.setUrlPrefix(null);

        return clusterEntity;
    }
}
