package com.lnjoying.justice.api.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lnjoying.justice.api.constant.RedisCache;
import com.lnjoying.justice.api.rpcserviceimpl.CombRpcService;
import com.lnjoying.justice.api.service.NodeService;
import com.lnjoying.justice.api.utils.CipherUtil;
import com.lnjoying.justice.schema.common.ChannelState;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.CombRetPbPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.constant.WorkerType;
import com.lnjoying.justice.schema.entity.edgeif.EdgeGwIfState;
import com.lnjoying.justice.schema.entity.sys.NodeConfig;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.msg.EeNetMessageApi.ee_net_message;
import com.micro.core.common.GlobalInfo;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;

import java.net.InetSocketAddress;
import java.util.Map;

public class SessionManageHandler extends SimpleChannelInboundHandler<ee_net_message>
{
    private static Logger LOGGER = LogManager.getLogger();

    private ChannelState channnelState = ChannelState.DISCONNECTED;
    private String nodeId;

    CombRpcService combRpcService;
    NodeConfig nodeConfig = null;

    public SessionManageHandler()
    {
        combRpcService = BeanUtils.getBean("combRpcService");
        NodeService nodeService = BeanUtils.getBean("nodeService");
        nodeConfig = nodeService.getNodeConfig();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ee_net_message netMessage) throws Exception
    {
        LOGGER.info("recv from {} msg: {} channel state:{}", ctx.channel().remoteAddress(), netMessage.toString(), channnelState);

        if (nodeConfig == null)
        {
            LOGGER.error("node config is null");
            return;
        }

        if (channnelState == ChannelState.DISCONNECTED)
        {
            //如果channel是处于断连状态，要先登录，在登录之前本地必须要有设备的数据（设备数据可以是用户录入，或是设备自动注册）
            // 如果既不是注册消息，也不是登录消息，直接关闭
            if (! netMessage.getHead().getMsgName().equals(MessageName.GW_LOGIN_CLOUD_REQ)
             && ! netMessage.getHead().getMsgName().equals(MessageName.GW_REG_REQ)
             )
            {
                LOGGER.error("the peer should login or register.");
                ctx.close();
                return;
            }

            if (netMessage.getHead().getMsgName().equals(MessageName.GW_LOGIN_CLOUD_REQ))
            {
                processGwLogin(ctx, netMessage);
                return;
            }

            if (netMessage.getHead().getMsgName().equals(MessageName.GW_REG_REQ))
            {
                processGwReg(ctx, netMessage);
                return;
            }

            return;
        }

        if (netMessage.getHead().getMsgName().equals(MessageName.EDGE_REG_REQ))
        {
            processEdgeReg(ctx, netMessage);
            return;
        }

        if (netMessage.getHead().getMsgName().equals(MessageName.EDGE_LOGIN_GW_REQ))
        {
            processEdgeLogin(ctx, netMessage);
            return;
        }

        if (netMessage.getHead().getMsgName().equals(MessageName.WORKER_REG_REQ))
        {
            processWorkerReg(ctx, netMessage);
            return;
        }

        if (netMessage.getHead().getMsgName().equals(MessageName.WORKER_LOGIN_REQ))
        {
            processWorkerLogin(ctx, netMessage);
            return;
        }

        if (netMessage.getHead().getMsgName().equals(MessageName.HEART_BEAT_REQ))
        {
            processHeartBeat(ctx, netMessage);
            return;
        }

        ctx.fireChannelRead(netMessage);
    }

    void processGwLogin(ChannelHandlerContext ctx,ee_net_message netMessage)
    {
        if (! netMessage.getHead().getMsgName().equals(MessageName.GW_LOGIN_CLOUD_REQ))
        {
            ctx.close();
            return;
        }

        if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
        {
            LOGGER.info("sign error. ");
            ctx.close();
            return;
        }

        nodeId = netMessage.getGwLoginCloudReqBody().getNodeId();
        if (nodeId == null)
        {
            ctx.close();
            return;
        }

        try
        {
            GlobalInfo.channelMap.put(nodeId, ctx.channel());
            Integer channelCount = GlobalInfo.channelMap.getChannelCount(nodeId);
            LOGGER.debug("nodeId: {} have channel num: {}", nodeId, channelCount);

            if (channelCount > 5)
            {
                LOGGER.error("nodeId: {} have more than 5 channel {}", nodeId, channelCount);
                buildFailedLoginRsp(1, netMessage);
                GlobalInfo.channelMap.remove(nodeId, ctx.channel());
                ctx.close();
                return;
            }

            EdgeMessage edgeMessage = new EdgeMessage();
            edgeMessage.setNodeId(nodeId);
            byte[] msg = netMessage.toByteArray();

            edgeMessage.setNetMessage(msg);
            InetSocketAddress inetSocketAddress =  (InetSocketAddress)ctx.channel().remoteAddress();
            CombRetPbPacket retPacket = combRpcService.getEdgeResourceService().gwLogin(edgeMessage, inetSocketAddress.getAddress().getHostAddress());
            if (retPacket.getStatus() != ErrorCode.SUCCESS.getCode())
            {
                LOGGER.error("login failed. node id may be not exist. login node id= {}", nodeId);
                ee_net_message loginrsp = buildFailedLoginRsp(retPacket.getStatus(), netMessage);
                LOGGER.info("rsp to {} rsp: {}", ctx.channel().remoteAddress(), loginrsp);
                ctx.writeAndFlush(loginrsp);
                GlobalInfo.channelMap.remove(nodeId, ctx.channel());
                ctx.close();
                return;
            }

            EdgeGwIfState edgeGwIfState = new EdgeGwIfState();
            EeCommonDef.network_address network_address = netMessage.getGwLoginCloudReqBody().getAddrMe();
            edgeGwIfState.setAdvertise(network_address.getIp()+":"+network_address.getPort());
            edgeGwIfState.setNodeId(netMessage.getGwLoginCloudReqBody().getNodeId());
            edgeGwIfState.setStatus(ChannelState.CONNECTED.getValue());
            edgeGwIfState.setLoginTime(System.currentTimeMillis());
            edgeGwIfState.setLastTime(System.currentTimeMillis());
            RedisUtil.oset(RedisCache.GW_IF, edgeGwIfState.getNodeId(), edgeGwIfState);

            channnelState = ChannelState.CONNECTED;
            GlobalInfo.channel2NodeIdMap.put(ctx.channel().hashCode(), nodeId);
            LOGGER.info("cache id in {} node Id: {}", ctx.hashCode(), nodeId);
            ee_net_message loginrsp = buildSuccessLoginRsp(retPacket, netMessage);
            LOGGER.info("rsp to {} rsp: {}", ctx.channel().remoteAddress(), loginrsp);
            ctx.writeAndFlush(loginrsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            LOGGER.error("rpc call notify channel error" + e);
        }
        finally
        {
        }
    }

    ee_net_message buildFailedLoginRsp(Integer status, ee_net_message netMessage)
    {

        EeCommonDef.msg_header loginReqHead = netMessage.getHead();
        EeCommonDef.msg_header.Builder LogintHeadRsp = makeRspHeaderByReq(MessageName.GW_LOGIN_CLOUD_RSP, loginReqHead);

                EeCommonDef.msg_gw_login_cloud_rsp_body.Builder rspBody = EeCommonDef.msg_gw_login_cloud_rsp_body.newBuilder().setStatus(status);
        ee_net_message loginrsp = ee_net_message.newBuilder()
                .setHead(LogintHeadRsp).setGwLoginCloudRspBody(rspBody).build();
        LOGGER.info(loginrsp);
        return loginrsp;
    }

    EeCommonDef.msg_header.Builder makeRspHeaderByReq(String msgName, EeCommonDef.msg_header reqHeader)
    {
        String nonce = Utils.assignUUId();
        String signMsg = nonce + nodeConfig.getNode_id();
        Map<String,String> exten_info = CipherUtil.sign_exten(signMsg, nodeConfig.getNode_id());

        EeCommonDef.msg_header.Builder rspHead = EeCommonDef.msg_header.newBuilder().setMsgName(msgName)
                .setMagic(reqHeader.getMagic()).setNonce(nonce)
                .setSessionId(reqHeader.getSessionId()).putAllExtenInfo(exten_info);
        return rspHead;
    }

    ee_net_message buildSuccessLoginRsp(CombRetPbPacket retPacket, ee_net_message netMessage)
    {

        try
        {
            EeCommonDef.msg_gw_login_cloud_rsp_body gwLoginCloudRspBody = EeCommonDef.msg_gw_login_cloud_rsp_body.parseFrom(retPacket.getObj());
            EeCommonDef.msg_header loginReqHead = netMessage.getHead();
            gwLoginCloudRspBody = gwLoginCloudRspBody.toBuilder().setCoreVersion(nodeConfig.getCore_version())
                                                                .setProtocolVersion(nodeConfig.getProtocol_version())
                                                                .setNodeType(nodeConfig.getNode_type())
                                                                .setNodeName(nodeConfig.getNode_name()).build();

            EeCommonDef.msg_header.Builder LogintHeadRsp = makeRspHeaderByReq(MessageName.GW_LOGIN_CLOUD_RSP, loginReqHead);

            ee_net_message loginrsp = ee_net_message.newBuilder()
                    .setHead(LogintHeadRsp).setGwLoginCloudRspBody(gwLoginCloudRspBody).build();
            LOGGER.info(loginrsp);
            return loginrsp;

        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void processHeartBeat(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        Object object =RedisUtil.oget(RedisCache.GW_IF, nodeId);
        if (object != null)
        {
            EdgeGwIfState edgeGwIfState = (EdgeGwIfState) object;
            edgeGwIfState.setLastTime(System.currentTimeMillis());
            RedisUtil.oset(RedisCache.GW_IF, nodeId, edgeGwIfState);
        }

        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());
        if (! netMessage.getHead().getMsgName().equals(MessageName.HEART_BEAT_REQ))
        {
            ctx.close();
            return;
        }

        EeCommonDef.msg_header heartBeatHeadReq = netMessage.getHead();

        EeCommonDef.msg_header.Builder heartBeatHeadRsp = EeCommonDef.msg_header.newBuilder().setMsgName(MessageName.HEART_BEAT_RSP)
                                                .setMagic(heartBeatHeadReq.getMagic()).setNonce(Utils.assignUUId())
                                                .setSessionId(heartBeatHeadReq.getSessionId());

        ee_net_message heartBeatRsp  = EeNetMessageApi.ee_net_message.newBuilder().setHead(heartBeatHeadRsp).build();
        LOGGER.info("rsp to: {} rsp: {}", ctx.channel().remoteAddress(), heartBeatRsp);
        ctx.writeAndFlush(heartBeatRsp);
    }

    private void processGwReg(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());

        try
        {
            if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
            {
                LOGGER.info("sign error. ");
                ctx.close();
                return;
            }

            EdgeMessage edgeMessage = new EdgeMessage();
            edgeMessage.setNodeId(nodeId);
            byte[] msg = netMessage.toByteArray();

            edgeMessage.setNetMessage(msg);
            InetSocketAddress inetSocketAddress =  (InetSocketAddress)ctx.channel().remoteAddress();
            int ret = combRpcService.getEdgeResourceService().gwReg(edgeMessage, inetSocketAddress.getAddress().getHostAddress());

            EeCommonDef.msg_header regHeadReq = netMessage.getHead();
            EeCommonDef.msg_gw_reg_req_body regReqBody = netMessage.getGwRegReqBody();
            EeCommonDef.msg_header.Builder regHeadRsp = makeRspHeaderByReq(MessageName.GW_REG_RSP, regHeadReq);

            EeCommonDef.msg_gw_reg_rsp_body.Builder rspBody = EeCommonDef.msg_gw_reg_rsp_body.newBuilder()
                    .setStatus(ret).setNodeId(nodeConfig.getNode_id())
                    .setNodeName(nodeConfig.getNode_name()).setNodeType(nodeConfig.getNode_type())
                    .addAllRegionIds(regReqBody.getRegionIdsList());

            ee_net_message devRegRsp = ee_net_message.newBuilder()
                    .setHead(regHeadRsp).setGwRegRspBody(rspBody).build();

            LOGGER.info("rsp to: {} rsp: {}", ctx.channel().remoteAddress(), devRegRsp);
            ctx.writeAndFlush(devRegRsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }

    private void processEdgeReg(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());

        try
        {
            if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
            {
                LOGGER.info("sign error. ");
                ctx.close();
            }

            EdgeMessage edgeMessage = new EdgeMessage();
            edgeMessage.setNodeId(nodeId);
            byte[] msg = netMessage.toByteArray();

            edgeMessage.setNetMessage(msg);
            Pair<Integer,Integer> ret = combRpcService.getEdgeResourceService().edgeReg(edgeMessage);

            EeCommonDef.msg_header.Builder regHeadRsp = makeRspHeaderByReq(MessageName.EDGE_REG_RSP, netMessage.getHead());
            EeCommonDef.msg_route src_route = netMessage.getRoute();
            EeCommonDef.msg_route.Builder routeBuilder = EeCommonDef.msg_route.newBuilder();

            if (! StringUtils.isEmpty(src_route.getONodeId()) && ! StringUtils.isEmpty(src_route.getORegionId()))
            {
                routeBuilder.setDRegionId(src_route.getORegionId()).setDNodeId(src_route.getONodeId());
            }
            else
            {
                EeCommonDef.msg_edge_reg_req_body reqBody = netMessage.getEdgeRegReqBody();
                routeBuilder.setDNodeId(reqBody.getNodeId()).setDRegionId(reqBody.getRegionId());
            }

            routeBuilder.setONodeId(nodeConfig.getNode_id()).setORegionId("admin");


            EeCommonDef.msg_edge_reg_rsp_body.Builder rspBody = EeCommonDef.msg_edge_reg_rsp_body.newBuilder().setStatus(ret.getLeft()).setActiveStatus(ret.getRight());
            ee_net_message devRegRsp = ee_net_message.newBuilder()
                    .setHead(regHeadRsp).setEdgeRegRspBody(rspBody).setRoute(routeBuilder).build();

            LOGGER.debug("rsp to: {} rsp: {}", ctx.channel().remoteAddress(), devRegRsp);
            ctx.writeAndFlush(devRegRsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }

    private void processEdgeLogin(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());

        try
        {
            if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
            {
                LOGGER.info("sign error. ");
                ctx.close();
            }

            EdgeMessage edgeMessage = new EdgeMessage();
            edgeMessage.setNodeId(nodeId);
            byte[] msg = netMessage.toByteArray();

            edgeMessage.setNetMessage(msg);
            
            CombRetPacket ret = combRpcService.getEdgeResourceService().edgeRemoteLogin(edgeMessage);

            EeCommonDef.msg_header.Builder regHeadRsp = makeRspHeaderByReq(MessageName.EDGE_LOGIN_GW_RSP, netMessage.getHead());
            EeCommonDef.msg_route src_route = netMessage.getRoute();
            EeCommonDef.msg_route.Builder routeBuilder = EeCommonDef.msg_route.newBuilder();

            if (! StringUtils.isEmpty(src_route.getONodeId()) && ! StringUtils.isEmpty(src_route.getORegionId()))
            {
                routeBuilder.setDRegionId(src_route.getORegionId()).setDNodeId(src_route.getONodeId());
            }
            else
            {
                EeCommonDef.msg_edge_reg_req_body reqBody = netMessage.getEdgeRegReqBody();
                routeBuilder.setDNodeId(reqBody.getNodeId()).setDRegionId(reqBody.getRegionId());
            }

            routeBuilder.setONodeId(nodeConfig.getNode_id()).setORegionId("admin");
    
    
            EeCommonDef.msg_edge_login_gw_rsp_body.Builder rspBody = EeCommonDef.msg_edge_login_gw_rsp_body.newBuilder().setStatus(ret.getStatus());
    
            if (ret.getObj() != null)
            {
                
                rspBody.setExtenInfo((String) ret.getObj());
            }
            
            ee_net_message devRegRsp = ee_net_message.newBuilder()
                    .setHead(regHeadRsp).setEdgeLoginGwRspBody(rspBody).setRoute(routeBuilder).build();

            LOGGER.debug("rsp to: {} rsp: {}", ctx.channel().remoteAddress(), devRegRsp);
            ctx.writeAndFlush(devRegRsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }

    private void processWorkerReg(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());

        try
        {
            if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
            {
                LOGGER.info("sign error. ");
                ctx.close();
            }

            EdgeMessage edgeMessage = new EdgeMessage();
            edgeMessage.setNodeId(nodeId);
            byte[] msg = netMessage.toByteArray();

            edgeMessage.setNetMessage(msg);

            int ret;
            if (netMessage.getWorkerRegReqBody().getWorkerType().equals(WorkerType.CLOUD_AGENT))
            {
                ret = combRpcService.getCloudManagerService().workerReg(edgeMessage);
            }
            else if (netMessage.getWorkerRegReqBody().getWorkerType().equals(WorkerType.SERVICE_AGENT))
            {
                ret = combRpcService.getEdgeResourceService().workerReg(edgeMessage);
            }
            else
            {
                ret = combRpcService.getClusterManagerService().workerReg(edgeMessage);
            }

            EeCommonDef.msg_header.Builder regHeadRsp = makeRspHeaderByReq(MessageName.WORKER_REG_RSP, netMessage.getHead());
            EeCommonDef.msg_route src_route = netMessage.getRoute();
            EeCommonDef.msg_route.Builder routeBuilder = EeCommonDef.msg_route.newBuilder();

            routeBuilder.setDRegionId(src_route.getORegionId()).setDWorkerId(src_route.getOWorkerId());

            routeBuilder.setONodeId(nodeConfig.getNode_id()).setORegionId("admin");

            EeWorkerDef.msg_worker_reg_rsp_body.Builder rspBody = EeWorkerDef.msg_worker_reg_rsp_body.newBuilder().setErrorCode(ret);
            ee_net_message devRegRsp = ee_net_message.newBuilder()
                    .setHead(regHeadRsp).setWorkerRegRspBody(rspBody).setRoute(routeBuilder).build();

            LOGGER.debug("rsp to: {} rsp: {}", ctx.channel().remoteAddress(), devRegRsp);
            ctx.writeAndFlush(devRegRsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }

    private void processWorkerLogin(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());

        try
        {
            if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
            {
                LOGGER.info("sign error. ");
                ctx.close();
            }

            EdgeMessage edgeMessage = new EdgeMessage();
            edgeMessage.setNodeId(nodeId);
            byte[] msg = netMessage.toByteArray();

            edgeMessage.setNetMessage(msg);
            int ret;
            if (netMessage.getWorkerLoginReqBody().getWorkerType().equals(WorkerType.CLOUD_AGENT))
            {
                ret = combRpcService.getCloudManagerService().workerLogin(edgeMessage);
            }
            else if (netMessage.getWorkerLoginReqBody().getWorkerType().equals(WorkerType.SERVICE_AGENT))
            {
                ret = combRpcService.getEdgeResourceService().workerLogin(edgeMessage);
            }
            else
            {
                ret = combRpcService.getClusterManagerService().workerLogin(edgeMessage);
            }

            EeCommonDef.msg_header.Builder regHeadRsp = makeRspHeaderByReq(MessageName.WORKER_LOGIN_RSP, netMessage.getHead());
            EeCommonDef.msg_route src_route = netMessage.getRoute();
            EeCommonDef.msg_route.Builder routeBuilder = EeCommonDef.msg_route.newBuilder();

            routeBuilder.setDRegionId(src_route.getORegionId()).setDWorkerId(src_route.getOWorkerId());

            routeBuilder.setONodeId(nodeConfig.getNode_id()).setORegionId("admin");

            EeWorkerDef.msg_worker_login_rsp_body.Builder rspBody = EeWorkerDef.msg_worker_login_rsp_body.newBuilder().setErrorCode(ret);
            ee_net_message devRegRsp = ee_net_message.newBuilder()
                    .setHead(regHeadRsp).setWorkerLoginRspBody(rspBody).setRoute(routeBuilder).build();

            LOGGER.debug("rsp to: {} rsp: {}", ctx.channel().remoteAddress(), devRegRsp);
            ctx.writeAndFlush(devRegRsp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        LOGGER.info("channel inactive. nodeId= {}", nodeId);
        if (ctx.channel().isActive())
        {
            ctx.channel().close();
        }

        LOGGER.info("o o the channel have been disconnect. remote: {}", ctx.channel().remoteAddress());

        try
        {
            GlobalInfo.channelMap.remove(nodeId, ctx.channel());
            GlobalInfo.channel2NodeIdMap.remove(ctx.channel().hashCode());
            RedisUtil.odel(RedisCache.GW_IF, nodeId);


            if (nodeId == null)
            {
                return;
            }

            combRpcService.getEdgeResourceService().gwDisConnect(nodeId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("channel error" + e);
        }
        finally
        {
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        super.exceptionCaught(ctx, cause);
        if(ctx != null && ctx.channel() != null && ctx.channel().isActive())ctx.close();
    }
}
