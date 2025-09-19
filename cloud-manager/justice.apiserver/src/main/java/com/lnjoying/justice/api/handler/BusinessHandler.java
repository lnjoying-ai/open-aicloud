package com.lnjoying.justice.api.handler;

import com.lnjoying.justice.api.constant.RedisCache;
import com.lnjoying.justice.api.constant.TunnelOperName;
import com.lnjoying.justice.api.rpcserviceimpl.CombRpcService;
import com.lnjoying.justice.schema.common.ChannelState;
import com.lnjoying.justice.schema.constant.ConfigConstants;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.constant.WorkerType;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfStateInfos;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfStateInfos;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.GlobalInfo;
import com.micro.core.persistence.redis.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import com.lnjoying.justice.schema.msg.EeNetMessageApi.ee_net_message;
import com.lnjoying.justice.schema.msg.EeCtrlMessageDef;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class BusinessHandler extends SimpleChannelInboundHandler<ee_net_message>
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder().build();

    CombRpcService combRpcService;

    public BusinessHandler()
    {
        combRpcService = BeanUtils.getBean("combRpcService");
        LOGGER.info("business handler init");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ee_net_message netMessage) throws Exception
    {
        try
        {
            LOGGER.info("business recv: {}", netMessage.getHead().getMsgName());
            String msgName = netMessage.getHead().getMsgName();

            switch (msgName)
            {
                case MessageName.CLOUD_CTRL:
                    processCloudCtrlMessage(ctx, netMessage);
                    break;
                case MessageName.EHOST_OPERATOR:
                case MessageName.GHOST_OPERATOR:
                    deliverMessageToEcrm(ctx, netMessage);
                    break;
                case MessageName.INST_OPERATOR:
                    deliverMessageToCis(ctx, netMessage);
                    break;
                case MessageName.EDGE_STAT_OPERATOR:
                    deliverMessageToStat(ctx, netMessage);
                    break;
                case MessageName.STACK_OPERATOR:
                    deliverMessageToAos(ctx, netMessage);
                    break;
                case MessageName.IMAGE_OPERATOR:
                    deliverMessageToIms(ctx, netMessage);
                    break;
                case MessageName.GET_EDGE_REQ:
                    deliverMessageToEcrm(ctx, netMessage);
                    break;
                case MessageName.TUNNEL_OPERATOR:
                case MessageName.FWD_OPERATOR:
                    deliverMessageToClsvr(ctx, netMessage);
                    break;
                case MessageName.FILE_OPERATOR:
                    if (netMessage.getHead().getSessionId().startsWith(ConfigConstants.REQ_MSG_HEADER_NAME_CIS_PREFIX))
                    {
                        deliverMessageToCis(ctx, netMessage);
                    }
                    else if (netMessage.getHead().getSessionId().startsWith(ConfigConstants.REQ_MSG_HEADER_NAME_AOS_PREFIX))
                    {
                        deliverMessageToAos(ctx, netMessage);
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("process rpt error." + e);
        }
    }


    void deliverMessageToEcrm(ChannelHandlerContext ctx, ee_net_message net_message)
    {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(net_message.toByteArray());
        combRpcService.getEdgeResourceService().deliver(edgeMessage);
    }

    void deliverMessageToCis(ChannelHandlerContext ctx, ee_net_message net_message)
    {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(net_message.toByteArray());
        combRpcService.getContainerInstService().deliver(edgeMessage);
    }

    void deliverMessageToAos(ChannelHandlerContext ctx, ee_net_message net_message)
    {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(net_message.toByteArray());
        combRpcService.getAosService().deliver(edgeMessage);
    }

    void deliverMessageToStat(ChannelHandlerContext ctx, ee_net_message net_message)
    {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(net_message.toByteArray());
        combRpcService.getStatService().deliver(edgeMessage);
    }

    void deliverMessageToIms(ChannelHandlerContext ctx, ee_net_message netMessage) {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(netMessage.toByteArray());
        combRpcService.getImsService().deliver(edgeMessage);
    }

    void deliverMessageToCls(ChannelHandlerContext ctx, ee_net_message netMessage) {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(netMessage.toByteArray());
        combRpcService.getClusterManagerService().deliver(edgeMessage);
    }

    void deliverMessageToClsvr(ChannelHandlerContext ctx, ee_net_message netMessage) {
        WorkerMessage workerMessage = new WorkerMessage();
        workerMessage.setWorkerId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        workerMessage.setNetMessage(netMessage.toByteArray());

        try
        {
            String endpoint = netMessage.getHead().getSessionId().contains("_") ? netMessage.getHead().getSessionId().substring(0, netMessage.getHead().getSessionId().indexOf("_")) : null;
            if (StringUtils.isNotEmpty(endpoint))
            {
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.set("Content-Type", "application/json");
                HttpEntity<WorkerMessage> requestEntity = new HttpEntity<WorkerMessage>(workerMessage, requestHeaders);
                REST_TEMPLATE.postForObject("http://" + endpoint + "/ClusterServerServiceImpl/deliver", requestEntity, Integer.class);
            }
            else if (netMessage.getHead().getMsgName().equalsIgnoreCase(MessageName.TUNNEL_OPERATOR))
            {
                String tunnelId = null;
                switch (netMessage.getTunnelOperatorBody().getOperatorType())
                {
                    case TunnelOperName.CREATE_TUNNEL_RSP:
                        tunnelId = netMessage.getTunnelOperatorBody().getCreateTunnelRspBody().getTunnelId();
                        break;
                    case TunnelOperName.FWD_TUNNEL_DATA_RSP:
                        tunnelId = netMessage.getTunnelOperatorBody().getFwdTunnelDataRspBody().getTunnelId();
                        break;
                    case TunnelOperName.CLOSE_TUNNEL_RSP:
                        tunnelId = netMessage.getTunnelOperatorBody().getCloseTunnelRspBody().getTunnelId();
                        break;
                    default:
                        tunnelId = "";
                        break;
                }

                endpoint = tunnelId.contains("_") ? tunnelId.substring(0, tunnelId.indexOf("_")) : null;
                if (StringUtils.isNotEmpty(endpoint))
                {
                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.set("Content-Type", "application/json");
                    HttpEntity<WorkerMessage> requestEntity = new HttpEntity<WorkerMessage>(workerMessage, requestHeaders);
                    REST_TEMPLATE.postForObject("http://" + endpoint + "/ClusterServerServiceImpl/deliver", requestEntity, Integer.class);
                }
                else
                {
                    combRpcService.getClusterServerService().deliver(workerMessage);
                }
            }
            else
            {
                combRpcService.getClusterServerService().deliver(workerMessage);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("deliver message to servicegw error, {}", e.getMessage());
        }
    }

    void deliverMessageToCmp(ChannelHandlerContext ctx, ee_net_message netMessage) {
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode()));
        edgeMessage.setNetMessage(netMessage.toByteArray());
        combRpcService.getCloudManagerService().deliver(edgeMessage);
    }

    EdgeDevIfState setEdgeIfState(String gwNodeId, String nodeId, String regionId, int status)
    {
        EdgeDevIfState edgeDevIfState = new EdgeDevIfState();
        edgeDevIfState.setGwNodeId(gwNodeId);
        edgeDevIfState.setStatus(status);
        edgeDevIfState.setLastTime(System.currentTimeMillis());
        if (status == ChannelState.CONNECTED.getValue())
        {
            edgeDevIfState.setLoginTime(edgeDevIfState.getLastTime());
        }
        return edgeDevIfState;
    }

    void setEdgeIfState(String gwNodeId, String nodeId, String regionId, int status, EdgeDevIfState edgeDevIfState)
    {
        edgeDevIfState.setGwNodeId(gwNodeId);
        edgeDevIfState.setStatus(status);
        edgeDevIfState.setLastTime(System.currentTimeMillis());
        if (status == ChannelState.CONNECTED.getValue()) {
            edgeDevIfState.setLoginTime(edgeDevIfState.getLastTime());
        }
    }

    void processEdgeIfStateMsg(ChannelHandlerContext ctx, EeCtrlMessageDef.msg_sync_edge_if_state_req edgeIfState)
    {
        String gwNodeId = GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode());

        EdgeDevIfStateInfos edgeDevIfStateInfos = (EdgeDevIfStateInfos) RedisUtil.oget(RedisCache.EDGE_IF, edgeIfState.getDevIfState().getNodeId());
        if (edgeDevIfStateInfos == null)
        {
            edgeDevIfStateInfos = new EdgeDevIfStateInfos();
            edgeDevIfStateInfos.setNodeId(edgeIfState.getDevIfState().getNodeId());
            Map<String, EdgeDevIfState> edgeDevIfStateMap = new HashMap<>();
            edgeDevIfStateInfos.setEdgeDevIfStateMap(edgeDevIfStateMap);
        }

        if (! StringUtils.equals(edgeDevIfStateInfos.getRegionId(), edgeIfState.getDevIfState().getRegionId()))
        {
            edgeDevIfStateInfos.setRegionId(edgeIfState.getDevIfState().getRegionId());
        }

        EdgeDevIfState edgeDevIfState = edgeDevIfStateInfos.getEdgeDevIfStateMap().get(gwNodeId);

        if (edgeDevIfState == null)
        {
            edgeDevIfState = setEdgeIfState(gwNodeId, edgeIfState.getDevIfState().getNodeId(), edgeIfState.getDevIfState().getRegionId(), edgeIfState.getDevIfState().getIfStatus());
        }
        else
        {
            setEdgeIfState(gwNodeId, edgeIfState.getDevIfState().getNodeId(), edgeIfState.getDevIfState().getRegionId(), edgeIfState.getDevIfState().getIfStatus(), edgeDevIfState);
        }

        edgeDevIfStateInfos.getEdgeDevIfStateMap().put(gwNodeId, edgeDevIfState);

        LOGGER.info("new edge dev info: {}", edgeDevIfState);

        RedisUtil.oset(RedisCache.EDGE_IF, edgeDevIfStateInfos.getNodeId(), edgeDevIfStateInfos);
    }

    void processRegionIfState(ChannelHandlerContext ctx,  EeCtrlMessageDef.msg_sync_region_connected_dev_req regionConnectedDevMsg)
    {
        LOGGER.info("process region if state {}", regionConnectedDevMsg);
        String gwNodeId = GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode());
        for (EeCtrlMessageDef.region_connected_dev regionConnectedDev : regionConnectedDevMsg.getRegionConnectedDevsList())
        {
            String regionId = regionConnectedDev.getRegionId();
            for (EeCtrlMessageDef.site_connected_dev siteConnectedDev : regionConnectedDev.getSiteDevIfStatesList())
            {
                String siteId = siteConnectedDev.getSiteId();
//                RedisUtil.sadd(RedisCacheField.REGION + regionId, gwNodeId, siteId);
                for (EeCtrlMessageDef.edge_connected_dev edgeConnectedDev : siteConnectedDev.getEdgeConnectedDevsList())
                {
                    //site, gw and edge relation, use for edge state check.
                    if (!RedisUtil.sismember(RedisCache.SITE_GW_EDGE + regionId + ":" + siteId + ":", gwNodeId, edgeConnectedDev.getNodeId()))
                    {
                        RedisUtil.sadd(RedisCache.SITE_GW_EDGE + regionId + ":" + siteId + ":", gwNodeId, edgeConnectedDev.getNodeId());
                    }
                    //online edge set, use for scheduling
                    RedisUtil.sadd(RedisCache.ALL_ONLINE_EDGE, "", edgeConnectedDev.getNodeId());
                    RedisUtil.sadd(RedisCache.REGION_ONLINE_EDGE, regionId, edgeConnectedDev.getNodeId());
                    RedisUtil.sadd(RedisCache.SITE_ONLINE_EDGE, siteId, edgeConnectedDev.getNodeId());

                    EdgeDevIfStateInfos edgeDevIfStateInfos = (EdgeDevIfStateInfos) RedisUtil.oget(RedisCache.EDGE_IF, edgeConnectedDev.getNodeId());
                    if (edgeDevIfStateInfos == null)
                    {
                        edgeDevIfStateInfos = new EdgeDevIfStateInfos();
                        edgeDevIfStateInfos.setNodeId(edgeConnectedDev.getNodeId());
                        Map<String, EdgeDevIfState> edgeDevIfStateMap = new HashMap<>();
                        edgeDevIfStateInfos.setEdgeDevIfStateMap(edgeDevIfStateMap);
                    }


                    EdgeDevIfState edgeDevIfState = edgeDevIfStateInfos.getEdgeDevIfStateMap().get(gwNodeId);

                    if (edgeDevIfState == null)
                    {
                        edgeDevIfState = setEdgeIfState(gwNodeId, edgeConnectedDev.getNodeId(), regionId, OnlineStatus.ONLINE);
                    }
                    else
                    {
                        setEdgeIfState(gwNodeId, edgeConnectedDev.getNodeId(), regionId, OnlineStatus.ONLINE, edgeDevIfState);
                    }

                    if (! StringUtils.equals(edgeDevIfStateInfos.getRegionId(), regionId))
                    {
                        edgeDevIfStateInfos.setRegionId(regionId);
                    }

                    edgeDevIfStateInfos.getEdgeDevIfStateMap().put(gwNodeId, edgeDevIfState);

                    LOGGER.info("new edge dev info: {}", edgeDevIfState);

                    RedisUtil.oset(RedisCache.EDGE_IF, edgeDevIfStateInfos.getNodeId(), edgeDevIfStateInfos);
                }
            }
        }
    }

    void setEdgeWorkerIfState(String gwNodeId, String nodeId, String regionId, int status, EdgeWorkerIfState edgeWorkerIfState)
    {
        edgeWorkerIfState.setGwNodeId(gwNodeId);
        edgeWorkerIfState.setStatus(status);
        edgeWorkerIfState.setLastTime(System.currentTimeMillis());
        if (status == ChannelState.CONNECTED.getValue()) {
            edgeWorkerIfState.setLoginTime(edgeWorkerIfState.getLastTime());
        }
    }

    void processWorkerIfStateMsg(ChannelHandlerContext ctx, EeCtrlMessageDef.msg_sync_worker_if_state_req workerIfState)
    {
        String gwNodeId = GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode());

        String workerIfCacheKey = getWorkerIfCacheKey(workerIfState.getWorkerIfState().getWorkerType());

        EdgeWorkerIfStateInfos edgeWorkerIfStateInfos = (EdgeWorkerIfStateInfos) RedisUtil.oget(workerIfCacheKey, workerIfState.getWorkerIfState().getWorkerId());
        if (edgeWorkerIfStateInfos == null)
        {
            edgeWorkerIfStateInfos = new EdgeWorkerIfStateInfos();
            edgeWorkerIfStateInfos.setWorkerId(workerIfState.getWorkerIfState().getWorkerId());
            Map<String, EdgeWorkerIfState> edgeWorkerIfStateMap = new HashMap<>();
            edgeWorkerIfStateInfos.setEdgeWorkerIfStateMap(edgeWorkerIfStateMap);
        }

        if (! StringUtils.equals(edgeWorkerIfStateInfos.getRegionId(), workerIfState.getWorkerIfState().getRegionId()))
        {
            edgeWorkerIfStateInfos.setRegionId(workerIfState.getWorkerIfState().getRegionId());
        }

        EdgeWorkerIfState edgeWorkerIfState = edgeWorkerIfStateInfos.getEdgeWorkerIfStateMap().get(gwNodeId);

        if (edgeWorkerIfState == null)
        {
            edgeWorkerIfState = new EdgeWorkerIfState();
        }
        setEdgeWorkerIfState(gwNodeId, workerIfState.getWorkerIfState().getWorkerId(), workerIfState.getWorkerIfState().getRegionId(), workerIfState.getWorkerIfState().getIfStatus(), edgeWorkerIfState);

        edgeWorkerIfStateInfos.getEdgeWorkerIfStateMap().put(gwNodeId, edgeWorkerIfState);

        LOGGER.info("new edge worker info: {}", edgeWorkerIfState);

        RedisUtil.oset(workerIfCacheKey, edgeWorkerIfStateInfos.getWorkerId(), edgeWorkerIfStateInfos);
    }

    void processRegionWorkerIfState(ChannelHandlerContext ctx,  EeCtrlMessageDef.msg_sync_region_connected_worker_req regionConnectedWorkerMsg)
    {
        LOGGER.info("process region if state {}", regionConnectedWorkerMsg);
        String gwNodeId = GlobalInfo.channel2NodeIdMap.get(ctx.channel().hashCode());
        for (EeCtrlMessageDef.region_connected_worker regionConnectedWorker : regionConnectedWorkerMsg.getConnectedWorkersList())
        {
            String regionId = regionConnectedWorker.getRegionId();
            for (EeCtrlMessageDef.edge_connected_worker edgeConnectedWorker : regionConnectedWorker.getConnectWorkerIfList())
            {
                //site, gw and edge relation, use for edge state check.
                if (!RedisUtil.sismember(RedisCache.REGION_GW_WORKER + regionId + ":", gwNodeId, edgeConnectedWorker.getWorkerId()))
                {
                    RedisUtil.sadd(RedisCache.REGION_GW_WORKER + regionId + ":", gwNodeId, edgeConnectedWorker.getWorkerId());
                }

                String workerIfCacheKey = getWorkerIfCacheKey(edgeConnectedWorker.getWorkerType());

                EdgeWorkerIfStateInfos edgeWorkerIfStateInfos = (EdgeWorkerIfStateInfos) RedisUtil.oget(workerIfCacheKey, edgeConnectedWorker.getWorkerId());
                if (edgeWorkerIfStateInfos == null)
                {
                    edgeWorkerIfStateInfos = new EdgeWorkerIfStateInfos();
                    edgeWorkerIfStateInfos.setWorkerId(edgeConnectedWorker.getWorkerId());
                    Map<String, EdgeWorkerIfState> edgeWorkerIfStateMap = new HashMap<>();
                    edgeWorkerIfStateInfos.setEdgeWorkerIfStateMap(edgeWorkerIfStateMap);
                }


                EdgeWorkerIfState edgeWorkerIfState = edgeWorkerIfStateInfos.getEdgeWorkerIfStateMap().get(gwNodeId);

                if (edgeWorkerIfState == null)
                {
                    edgeWorkerIfState = new EdgeWorkerIfState();
                }
                setEdgeWorkerIfState(gwNodeId, edgeConnectedWorker.getWorkerId(), regionId, OnlineStatus.ONLINE, edgeWorkerIfState);

                if (! StringUtils.equals(edgeWorkerIfStateInfos.getRegionId(), regionId))
                {
                    edgeWorkerIfStateInfos.setRegionId(regionId);
                }

                edgeWorkerIfStateInfos.getEdgeWorkerIfStateMap().put(gwNodeId, edgeWorkerIfState);

                LOGGER.info("new edge worker info: {}", edgeWorkerIfState);

                RedisUtil.oset(workerIfCacheKey, edgeWorkerIfStateInfos.getWorkerId(), edgeWorkerIfStateInfos);
            }
        }
    }

    void processCloudCtrlMessage(ChannelHandlerContext ctx, ee_net_message net_message)
    {
        String ctrlType = net_message.getCloudCtrlBody().getCtrlType();
        switch (ctrlType)
        {
            case CtrlType.GET_GW_LIST_REQ:
                deliverMessageToEcrm(ctx, net_message);
                break;

            case CtrlType.SYNC_EDGE_IF_STATE_REQ:
                processEdgeIfStateMsg(ctx, net_message.getCloudCtrlBody().getSyncEdgeIfStateReqBody());
                deliverMessageToEcrm(ctx, net_message);
                break;

            case CtrlType.SYNC_REGION_CONNECTED_DEV_REQ:
                processRegionIfState(ctx, net_message.getCloudCtrlBody().getSyncRegionConnectedDevReqBody());
                break;

            case CtrlType.SYNC_WORKER_IF_STATE_REQ:
                processWorkerIfStateMsg(ctx, net_message.getCloudCtrlBody().getSyncWorkerIfStateReqBody());
                if (net_message.getCloudCtrlBody().getSyncWorkerIfStateReqBody().getWorkerIfState().getWorkerType().equals(WorkerType.CLOUD_AGENT))
                {
                    deliverMessageToCmp(ctx, net_message);
                }
                else if (net_message.getCloudCtrlBody().getSyncWorkerIfStateReqBody().getWorkerIfState().getWorkerType().equals(WorkerType.SERVICE_AGENT))
                {
                    deliverMessageToEcrm(ctx, net_message);
                }
                else
                {
                    deliverMessageToCls(ctx, net_message);
                }
                break;

            case CtrlType.SYNC_REGION_CONNECTED_WORKER_REQ:
                processRegionWorkerIfState(ctx, net_message.getCloudCtrlBody().getSyncRegionConnectedWorkerReqBody());
                break;
        }
    }

    private String getWorkerIfCacheKey(String workerType)
    {
        if (workerType.equals(WorkerType.CLOUD_AGENT))
        {
            return RedisCache.CMP_WORKER_IF;
        }
        else if (workerType.equals(WorkerType.SERVICE_AGENT))
        {
            return RedisCache.ECRM_WORKER_IF;
        }
        return RedisCache.CLS_WORKER_IF;
    }
}
