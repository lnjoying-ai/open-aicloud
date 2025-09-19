package com.lnjoying.justice.cluster.agent.netutil;

import com.google.protobuf.ByteString;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.util.CipherUtil;
import com.lnjoying.justice.schema.msg.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.lnjoying.justice.cluster.agent.common.TunnelOperName.*;
import static com.lnjoying.justice.schema.msg.MessageName.HEART_BEAT_REQ;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 5/19/22 8:48 PM
 */

public class NetMsgBuild
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public static EeNetMessageApi.ee_net_message buildWorkerRegMessage(NodeConfig nodeConfig)
    {
        EeCommonDef.msg_header.Builder header = NetMessageUtil.makeReqMsgHeader("worker_reg_req");
        EeCommonDef.msg_route.Builder route = EeCommonDef.msg_route.newBuilder();
        String workerId = nodeConfig.getNode_id();
        String regionId = nodeConfig.getRegion_id();
        route.setOWorkerId(workerId);
        route.setORegionId(regionId);
        EeWorkerDef.msg_worker_reg_req_body.Builder reqBody = EeWorkerDef.msg_worker_reg_req_body.newBuilder();
        reqBody.setWorkerId(workerId);
        reqBody.setRegionId(regionId);
        reqBody.setWorkerType(nodeConfig.getWorkerType());
        reqBody.setToken(nodeConfig.getToken());
        if (!  StringUtils.isEmpty(nodeConfig.getSaToken()))
        {
            reqBody.setSaToken(nodeConfig.getSaToken());
        }
        
        if (! StringUtils.isEmpty(nodeConfig.getKubeCa()))
        {
            reqBody.setKubeCa(nodeConfig.getKubeCa());
        }
        
        String sign_msg = header.getNonce() + workerId;
        Map<String, String> exten = CipherUtil.sign_exten(sign_msg, workerId);
        header.putAllExtenInfo(exten);
        
        return EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setRoute(route).setWorkerRegReqBody(reqBody).build();
    }
    
    public static EeNetMessageApi.ee_net_message buildWorkerLoginMessage(NodeConfig nodeConfig)
    {
        EeCommonDef.msg_header.Builder header = NetMessageUtil.makeReqMsgHeader("worker_login_req");
        EeWorkerDef.msg_worker_login_req_body.Builder reqBody = EeWorkerDef.msg_worker_login_req_body.newBuilder();
        String workerId = nodeConfig.getNode_id();
        String regionId = nodeConfig.getRegion_id();
        reqBody.setWorkerId(workerId);
        reqBody.setRegionId(regionId);
        reqBody.setCoreVersion(nodeConfig.getCore_version());
        reqBody.setWorkerType(nodeConfig.getWorkerType());
        String sign_msg = header.getNonce() + workerId;
        Map<String, String> exten = CipherUtil.sign_exten(sign_msg, workerId);
        header.putAllExtenInfo(exten);
        EeCommonDef.msg_route.Builder route = EeCommonDef.msg_route.newBuilder();
        route.setOWorkerId(workerId);
        route.setORegionId(regionId);
        return EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setRoute(route).setWorkerLoginReqBody(reqBody).build();
    }
    
    public static EeNetMessageApi.ee_net_message buildTunnelRspMessage(EeTunnelDef.msg_tunnel_operator_body reqBody, EeCommonDef.msg_header reqHeader, EeCommonDef.msg_route msgRoute)
    {
        EeCommonDef.msg_header.Builder header = NetMessageUtil.makeRspMsgHeader("tunnel_operator", reqHeader);
    
        EeCommonDef.msg_route.Builder route = EeCommonDef.msg_route.newBuilder();
        route.setOWorkerId(msgRoute.getDWorkerId());
        route.setORegionId(msgRoute.getDRegionId());
        route.setDRegionId(msgRoute.getORegionId());
        route.setDNodeId(msgRoute.getONodeId());
        
        return EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setRoute(route).setTunnelOperatorBody(reqBody).build();
    }
    
    public static EeNetMessageApi.ee_net_message buildFwdRspMessage(EeFwdDef.msg_fwd_operator_body reqBody, EeCommonDef.msg_header reqHeader, EeCommonDef.msg_route msgRoute)
    {
        EeCommonDef.msg_header.Builder header = NetMessageUtil.makeRspMsgHeader("fwd_operator", reqHeader);
        
        EeCommonDef.msg_route.Builder route = EeCommonDef.msg_route.newBuilder();
        route.setOWorkerId(msgRoute.getDWorkerId());
        route.setORegionId(msgRoute.getDRegionId());
        route.setDRegionId(msgRoute.getORegionId());
        route.setDNodeId(msgRoute.getONodeId());
        
        return EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setRoute(route).setFwdOperatorBody(reqBody).build();
    }
    
    public static EeNetMessageApi.ee_net_message buildTunnelMessage(EeTunnelDef.msg_tunnel_operator_body reqBody, EeCommonDef.msg_route msgRoute)
    {
        EeCommonDef.msg_header.Builder header = NetMessageUtil.makeReqMsgHeader("tunnel_operator");
        EeCommonDef.msg_route.Builder route = EeCommonDef.msg_route.newBuilder();
        route.setOWorkerId(msgRoute.getDWorkerId());
        route.setORegionId(msgRoute.getDRegionId());
        route.setDRegionId(msgRoute.getORegionId());
        route.setDNodeId(msgRoute.getONodeId());
        
        return EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setRoute(route).setTunnelOperatorBody(reqBody).build();
    }
    
    public static EeTunnelDef.msg_tunnel_operator_body.Builder buildTunnelBody()
    {
        return EeTunnelDef.msg_tunnel_operator_body.newBuilder();
    }
    
    public static EeFwdDef.msg_fwd_operator_body.Builder buildFwdBody()
    {
        return EeFwdDef.msg_fwd_operator_body.newBuilder();
    }
    
    public static EeNetMessageApi.ee_net_message   buildTunnelFwdRsp(int errorCode, byte [] data, String tunnelId, int seq, int state, EeCommonDef.msg_route route)
    {
        EeTunnelDef.msg_fwd_tunnel_data_rsp_body.Builder rspBody = EeTunnelDef.msg_fwd_tunnel_data_rsp_body.newBuilder();
        rspBody.setSeq(seq);
        rspBody.setErrorCode(errorCode);
        if (data != null)
        {
            rspBody.setData(ByteString.copyFrom(data));
        }
        
        rspBody.setTunnelId(tunnelId);
        rspBody.setState(state);
    
        EeTunnelDef.msg_tunnel_operator_body.Builder operBody = NetMsgBuild.buildTunnelBody();
        operBody.setOperatorType(FWD_TUNNEL_DATA_RSP);
        operBody.setFwdTunnelDataRspBody(rspBody);
        EeNetMessageApi.ee_net_message rspMsg = NetMsgBuild.buildTunnelMessage(operBody.build(), route);
        return rspMsg;
        
    }
    
    public static EeNetMessageApi.ee_net_message buildDirectFwdRsp(int errorCode, byte [] data, String dataId, EeCommonDef.msg_route route, EeCommonDef.msg_header reqHeader)
    {
        EeFwdDef.msg_fwd_data_rsp_body.Builder rspBody = EeFwdDef.msg_fwd_data_rsp_body.newBuilder();
        rspBody.setErrorCode(errorCode);
        if (data != null)
        {
            rspBody.setData(ByteString.copyFrom(data));
        }
        
        rspBody.setDataId(dataId);
        
        EeFwdDef.msg_fwd_operator_body.Builder operBody = NetMsgBuild.buildFwdBody();
        operBody.setOperatorType(FWD_DATA_RSP);
        operBody.setFwdDataRspBody(rspBody);
        EeNetMessageApi.ee_net_message rspMsg = NetMsgBuild.buildFwdRspMessage(operBody.build(), reqHeader, route);
        return rspMsg;
    }
    
    public static EeNetMessageApi.ee_net_message   buildHeartBeatReq()
    {
        EeCommonDef.msg_header.Builder header = NetMessageUtil.makeReqMsgHeader(HEART_BEAT_REQ);
        
        return EeNetMessageApi.ee_net_message.newBuilder().setHead(header).build();
        
    }
    
    public static EeNetMessageApi.ee_net_message  buildCreateTunnelRsp(int errorCode,  String tunnelId, EeCommonDef.msg_header reqHeader, EeCommonDef.msg_route route)
    {
        EeTunnelDef.msg_create_tunnel_rsp_body.Builder rspBody = EeTunnelDef.msg_create_tunnel_rsp_body.newBuilder();
        rspBody.setErrorCode(errorCode);
        rspBody.setTunnelId(tunnelId);
    
        EeTunnelDef.msg_tunnel_operator_body.Builder operBody = NetMsgBuild.buildTunnelBody();
        operBody.setCreateTunnelRspBody(rspBody);
        operBody.setOperatorType(CREATE_TUNNEL_RSP);
    
        EeNetMessageApi.ee_net_message rspMsg = NetMsgBuild.buildTunnelRspMessage(operBody.build(),reqHeader, route);
        
        return rspMsg;
    }
    
    public static EeNetMessageApi.ee_net_message buildCloseTunnelRsp(int errorCode,  String tunnelId, int state, EeCommonDef.msg_header reqHeader, EeCommonDef.msg_route route)
    {
        EeTunnelDef.msg_close_tunnel_rsp_body.Builder rspBody = EeTunnelDef.msg_close_tunnel_rsp_body.newBuilder();
        rspBody.setErrorCode(errorCode);
        rspBody.setTunnelId(tunnelId);
    
        EeTunnelDef.msg_tunnel_operator_body.Builder operBody = NetMsgBuild.buildTunnelBody();
        operBody.setCloseTunnelRspBody(rspBody);
        operBody.setOperatorType(CLOSE_TUNNEL_RSP);
        EeNetMessageApi.ee_net_message rspMsg = NetMsgBuild.buildTunnelRspMessage(operBody.build(),reqHeader, route);
        return rspMsg;
    }
}
