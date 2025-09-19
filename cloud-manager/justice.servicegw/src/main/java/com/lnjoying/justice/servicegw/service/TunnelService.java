package com.lnjoying.justice.servicegw.service;

import com.google.protobuf.ByteString;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.cluster.ClusterEntity;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.servicegw.common.FwdOperName;
import com.lnjoying.justice.servicegw.common.TunnelOperName;
import com.lnjoying.justice.servicegw.common.TunnelState;
import com.lnjoying.justice.servicegw.model.DirectMsgEntity;
import com.lnjoying.justice.servicegw.model.TunnelEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 2/26/22 12:39 PM
 */
@Service
public class TunnelService
{
    private static Logger LOGGER = LogManager.getLogger();
    private Map<String, TunnelEntity> tunnelId2Entity = new ConcurrentHashMap<>();
    private Map<String, ClusterEntity> address2Entity = new ConcurrentHashMap<>();
    private Map<String, ClusterEntity> channel2Entity = new ConcurrentHashMap<>();
    private Map<String, DirectMsgEntity> direcSrcMap = new HashMap<>();
    
    final long MAX_TUNNEL_OVERTIME = 120000;
    @Autowired
    NetMessageServiceFacade netMessageServiceFacade;
    
    public ClusterEntity getClusterEntity(String address)
    {
        return address2Entity.get(address);
    }
   
    public  void setAddress2Entity(String address, ClusterEntity entity)
    {
        address2Entity.put(address, entity);
    }
    
    public  void delAddress2Entity(String address)
    {
        if (StringUtils.isNotEmpty(address))
        {
            address2Entity.remove(address);
        }
    }

    public ClusterEntity getChannelEntity(String id)
    {
        return channel2Entity.get(id);
    }

    public  void setChannel2Entity(String id, ClusterEntity entity)
    {
        channel2Entity.put(id, entity);
    }

    public  void delChannel2Entity(String id)
    {
        channel2Entity.remove(id);
    }
    
    public void setTunnelEntityMap(TunnelEntity tunnelEntity)
    {
        tunnelId2Entity.put(tunnelEntity.getTunnelId(), tunnelEntity);
    }
    
    public  TunnelEntity getTunnel(String tunnelId)
    {
        return tunnelId2Entity.get(tunnelId);
    }
    
    
    public  void onCreateTunnelReq(TunnelEntity tunnelEntity)
    {
        try
        {
            EeTunnelDef.msg_create_tunnel_req_body.Builder reqBuilder = EeTunnelDef.msg_create_tunnel_req_body.newBuilder();
            reqBuilder.setTunnelId(tunnelEntity.getTunnelId());
            LOGGER.info("create tunnel:{} for channel {}", tunnelEntity.getTunnelId(), tunnelEntity.getSvrChannelId());
            reqBuilder.setProto("tcp");
            reqBuilder.setAddress(String.format("%s:%d",tunnelEntity.getClusterEntity().getClusterIp(), tunnelEntity.getClusterEntity().getClusterPort()));
            EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.TUNNEL_OPERATOR);
            EeTunnelDef.msg_tunnel_operator_body.Builder bodyBuilder = netMessageServiceFacade.makeTunnelBody(TunnelOperName.CREATE_TUNNEL_REQ);
            bodyBuilder.setCreateTunnelReqBody(reqBuilder);
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setTunnelOperatorBody(bodyBuilder).build();
            netMessageServiceFacade.submitMessage(tunnelEntity.getClusterEntity().getWorkderId(), netMessage);
            
        }
        catch (Exception e)
        {
            LOGGER.error("create tunnel error, {}", e.getMessage());
            releaseTunnel(tunnelEntity);
        }
    }
    
    public  void onCreateTunnelRsp(EeTunnelDef.msg_create_tunnel_rsp_body rspBody)
    {
        LOGGER.info("recv create tunnel rsp for tunnel {}", rspBody.getTunnelId());
        TunnelEntity tunnelEntity = getTunnel(rspBody.getTunnelId());
        if (tunnelEntity == null)
        {
            LOGGER.error("tunnel {} entity is null.", rspBody.getTunnelId());
            return;
        }
        
        if (rspBody.getErrorCode() != ErrorCode.SUCCESS.getCode())
        {
            LOGGER.error("create tunnel error. for {}", rspBody.getTunnelId());
            releaseTunnel(tunnelEntity);
    
            return;
        }
        
        if (rspBody.getErrorCode() == 0)
        {
            if (tunnelEntity.getTunnelState().getCode() == TunnelState.CREATING.getCode())
            {
                LOGGER.info("tunnel {} is active. ready to fwd msg", tunnelEntity.getTunnelId());
                onFwdTunnelMessageReq(tunnelEntity);
                LOGGER.info("set {}. active", tunnelEntity.getTunnelId());
                tunnelEntity.setTunnelState(TunnelState.ACTIVE);
            }
            else
            {
                LOGGER.info("tunnel {} state is {}", tunnelEntity.getTunnelId(), tunnelEntity.getTunnelState());
            }
        }
        else
        {
            LOGGER.info("tunnel {} state is failed {}", tunnelEntity.getTunnelId(), rspBody.getErrorCode());
            releaseTunnel(tunnelEntity);
        }
        
    }
    
    public  void onCloseTunnelReq(TunnelEntity tunnelEntity)
    {
        try
        {
            if (tunnelEntity.getTunnelState() != TunnelState.ACTIVE && tunnelEntity.getTunnelState() != TunnelState.CREATING)
            {
                LOGGER.info("{} is not active or creating", tunnelEntity.getTunnelId());
                releaseTunnel(tunnelEntity);
                return;
            }
            
            tunnelEntity.setTunnelState(TunnelState.CLOSING);
            
            tunnelEntity.clearMsg();
            LOGGER.info("close tunnel {}",tunnelEntity.getTunnelId());
            EeTunnelDef.msg_close_tunnel_req_body.Builder reqBuilder = EeTunnelDef.msg_close_tunnel_req_body.newBuilder();
            reqBuilder.setTunnelId(tunnelEntity.getTunnelId());
            EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.TUNNEL_OPERATOR);
            EeTunnelDef.msg_tunnel_operator_body.Builder bodyBuilder = netMessageServiceFacade.makeTunnelBody(TunnelOperName.CLOSE_TUNNEL_REQ);
            bodyBuilder.setCloseTunnelReqBody(reqBuilder);
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setTunnelOperatorBody(bodyBuilder).build();
            netMessageServiceFacade.submitMessage(tunnelEntity.getClusterEntity().getWorkderId(), netMessage);
        }
        catch (Exception e)
        {
            LOGGER.error("close abnormal, {}", e.getMessage());
        }
    }
    
    public  void onCloseTunnelRsp(EeTunnelDef.msg_close_tunnel_rsp_body rspBody)
    {
        LOGGER.info("recv close tunnel rsp {}", rspBody.getTunnelId());
        TunnelEntity tunnelEntity = getTunnel(rspBody.getTunnelId());
        if (tunnelEntity == null)
        {
            LOGGER.error("tunnel {} entity is null.", rspBody.getTunnelId());
            return;
        }
        
        releaseTunnel(tunnelEntity);
    }
    
    public void onDirectFwdMessageReq(DirectMsgEntity msgEntity)
    {
        try
        {
            EeFwdDef.msg_fwd_data_req_body.Builder reqBuilder = EeFwdDef.msg_fwd_data_req_body.newBuilder();
            reqBuilder.setDataId(msgEntity.getDataId());
            reqBuilder.setAddress(msgEntity.getAddress());

            byte[] data = new byte[msgEntity.getByteBuf().readableBytes()];
            msgEntity.getByteBuf().readBytes(data);
            reqBuilder.setData(ByteString.copyFrom(data));
            reqBuilder.setProto("tcp");
    
            EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.FWD_OPERATOR);
            EeFwdDef.msg_fwd_operator_body.Builder bodyBuilder = netMessageServiceFacade.makeFwdBody(FwdOperName.FWD_DATA_REQ);
            bodyBuilder.setFwdDataReqBody(reqBuilder);
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setFwdOperatorBody(bodyBuilder).build();
            netMessageServiceFacade.submitMessage(msgEntity.getWorkerId(), netMessage);
            direcSrcMap.put(msgEntity.getDataId(), msgEntity);
        }
        catch (Exception e)
        {
            LOGGER.error("fwd tunnel msg error. {}", e.getMessage());
        }
        finally
        {
            ReferenceCountUtil.release(msgEntity.getByteBuf());
        }
    }
    
    public void onDirectFwdCloseMessageReq(String dataId, String workerId)
    {
        EeFwdDef.msg_fwd_data_req_body.Builder reqBuilder = EeFwdDef.msg_fwd_data_req_body.newBuilder();
        reqBuilder.setDataId(dataId);
        EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.FWD_OPERATOR);
        EeFwdDef.msg_fwd_operator_body.Builder bodyBuilder = netMessageServiceFacade.makeFwdBody(FwdOperName.FWD_CLOSE_REQ);
        bodyBuilder.setFwdDataReqBody(reqBuilder);
        EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setFwdOperatorBody(bodyBuilder).build();
        netMessageServiceFacade.submitMessage(workerId, netMessage);
        direcSrcMap.remove(dataId);
    }
    
    public void onDirectFwdMessageRsp(EeFwdDef.msg_fwd_data_rsp_body rspBody)
    {
        try
        {
            if (StringUtils.isNotEmpty(rspBody.getDataId()))
            {
                DirectMsgEntity msgEntity = direcSrcMap.get(rspBody.getDataId());
                if (msgEntity != null)
                {
                    ClusterEntity clusterEntity = getChannelEntity(msgEntity.getSrcCtx().channel().id().toString());
                    if (clusterEntity != null && clusterEntity.isNetworkTest())
                    {
                        if (rspBody.getErrorCode() == 0)
                        {
                            String messgae = "{\"message\":\"Network connect success\"}";
                            StringBuilder buf = new StringBuilder();
                            buf.append(HttpVersion.HTTP_1_1).append(' ').append(HttpResponseStatus.OK).append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE)
                                    .append("Content-type: ").append(MediaType.APPLICATION_JSON_VALUE).append("; charset=utf-8").append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE)
                                    .append("Content-Length: ").append(messgae.length()).append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE).append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE)
                                    .append(messgae);
                            msgEntity.getSrcCtx().writeAndFlush(buf.toString().getBytes());
                            msgEntity.getSrcCtx().channel().close();
                            return;
                        }
                        else
                        {
                            String messgae = "{\"message\":\"Network connect error\"}";
                            StringBuilder buf = new StringBuilder();
                            buf.append(HttpVersion.HTTP_1_1).append(' ').append(HttpResponseStatus.OK).append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE)
                                    .append("Content-type: ").append(MediaType.APPLICATION_JSON_VALUE).append("; charset=utf-8").append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE)
                                    .append("Content-Length: ").append(messgae.length()).append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE).append(StringUtil.CARRIAGE_RETURN).append(StringUtil.NEWLINE)
                                    .append(messgae);
                            msgEntity.getSrcCtx().writeAndFlush(buf.toString().getBytes());
                            msgEntity.getSrcCtx().channel().close();
                            return;
                        }
                    }

                    if (rspBody.getErrorCode() != 0)
                    {
                        ChannelHandlerContext ctx = msgEntity.getSrcCtx();
                        ctx.close();
                        direcSrcMap.remove(rspBody.getDataId());
                        return;
                    }
                    
                    msgEntity.getSrcCtx().writeAndFlush(rspBody.getData().toByteArray());
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("fwd data msg rsp error. {}", e.getMessage());
        }
    }
    
    public  void onFwdTunnelMessageReq(TunnelEntity tunnelEntity)
    {
        try
        {
            //take out msg from msgPool. assemble pb msg
            Object data = tunnelEntity.getMsg();
            while (data != null)
            {
                EeTunnelDef.msg_fwd_tunnel_data_req_body.Builder reqBuilder = EeTunnelDef.msg_fwd_tunnel_data_req_body.newBuilder();
                reqBuilder.setTunnelId(tunnelEntity.getTunnelId());
                reqBuilder.setData(ByteString.copyFrom((byte[]) data));
                EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.TUNNEL_OPERATOR);
                EeTunnelDef.msg_tunnel_operator_body.Builder bodyBuilder = netMessageServiceFacade.makeTunnelBody(TunnelOperName.FWD_TUNNEL_DATA_REQ);
                bodyBuilder.setFwdTunnelDataReqBody(reqBuilder);
                EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setTunnelOperatorBody(bodyBuilder).build();
                netMessageServiceFacade.submitMessage(tunnelEntity.getClusterEntity().getWorkderId(), netMessage);
                data = tunnelEntity.getMsg();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("fwd tunnel msg error. {}", e.getMessage());
        }
    }
    
    public  void onFwdTunnelMessageRsp(EeTunnelDef.msg_fwd_tunnel_data_rsp_body rspBody)
    {
        String tunnelId = rspBody.getTunnelId();
        TunnelEntity tunnelEntity = tunnelId2Entity.get(tunnelId);
        if (tunnelEntity == null)
        {
            LOGGER.error("no tunnelEntity for {} for fwd rsp data", tunnelId);
            return;
        }
        
        if (rspBody.getState() == 0)
        {
            LOGGER.info("tunnel have been quit, release tunnel {}", tunnelId);
            releaseTunnel(tunnelEntity);
            return;
        }
        
        if (rspBody.getData() == null)
        {
            return;
        }
        tunnelEntity.getAuthChannel().writeAndFlush(rspBody.getData().toByteArray());
    }
    
    public void checkTunnelState()
    {
        try
        {
            if (tunnelId2Entity.isEmpty())
            {
                return;
            }

            for (Map.Entry<String, TunnelEntity> entry : tunnelId2Entity.entrySet())
            {
                TunnelEntity entity = entry.getValue();
                if (entity.getTunnelState() == TunnelState.ACTIVE || entity.getTunnelState() == TunnelState.CLOSED)
                {
                    continue;
                }

                long current = System.currentTimeMillis();
                if (entity.getTunnelState() == TunnelState.CREATING)
                {
                    if (current - entity.getCreatTime() > MAX_TUNNEL_OVERTIME)
                    {
                        LOGGER.info("close channel for auth");
                        releaseTunnel(entity);
                    }
                    else
                    {
                    }
                }

                if (entity.getTunnelState() == TunnelState.CLOSING)
                {
                    if (current - entity.getCloseTime() > MAX_TUNNEL_OVERTIME)
                    {
                        releaseTunnel(entity);
                    }
                    else
                    {
                        onCloseTunnelReq(entity);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("checkTunnelState error {}", e.getMessage());
        }
    }
    
    private void releaseTunnel(TunnelEntity entity)
    {
        entity.clearMsg();
        entity.setTunnelState(TunnelState.CLOSED);
        tunnelId2Entity.remove(entity.getTunnelId());
        if (entity.getAuthChannel() != null)
        {
            entity.getAuthChannel().close();
        }
    
    }
}
