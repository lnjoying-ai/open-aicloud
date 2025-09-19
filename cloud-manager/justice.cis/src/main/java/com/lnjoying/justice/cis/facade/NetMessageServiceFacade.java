package com.lnjoying.justice.cis.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lnjoying.justice.cis.common.constant.CisRedisField;
import com.lnjoying.justice.cis.common.constant.InstAction;
import com.lnjoying.justice.cis.common.constant.OperatorType;
import com.lnjoying.justice.cis.controller.dto.request.LogContainerInstReq;
import com.lnjoying.justice.cis.service.CombRpcService;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.dev.Registry;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NetMessageServiceFacade
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CombRpcService combRpcService;

    public int submitMessage(String nodeId, EeNetMessageApi.ee_net_message ee_net_message)
    {
         
        EdgeMessage edgeMessage = new EdgeMessage();
        edgeMessage.setNodeId(nodeId);
        edgeMessage.setNetMessage(ee_net_message.toByteArray());
        return combRpcService.getBusinessMsgSubmitService().submitMessage(edgeMessage);
    }

    public EeCommonDef.msg_header.Builder makeReqMsgHeader(String name)
    {
        return EeCommonDef.msg_header.newBuilder().setMsgName(name)
                .setNonce(Utils.assignUUId())
                .setSessionId(Utils.assignUUId());
    }

    public EeCommonDef.msg_header.Builder makeReqMsgHeader(String name, String sessionId)
    {
        return EeCommonDef.msg_header.newBuilder().setMsgName(name)
                .setNonce(Utils.assignUUId())
                .setSessionId(sessionId);
    }

    public EeCommonDef.msg_header.Builder makeRspMsgHeader(String name, EeCommonDef.msg_header reqHeader)
    {
        return EeCommonDef.msg_header.newBuilder().setMsgName(name)
                .setNonce(Utils.assignUUId())
                .setSessionId(reqHeader.getSessionId());
    }
    
    public int createInst(Registry registry, String instParamsStr, String devNeedStr, boolean autoRun, int replica, String bpId, String userId, String instName, String instId, String nodeId, String extraParams)
    {
        try
        {
            //send create msg
            EeInstDef.msg_create_inst_req_body.Builder createInstReqBuilder = EeInstDef.msg_create_inst_req_body.newBuilder();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode instParams = mapper.readValue(instParamsStr, ObjectNode.class);

            //request ims for registry
            if (null != registry && CollectionUtils.hasContent(registry.getServer()))
            {
                if (CollectionUtils.hasContent(registry.getPassword()))
                {
                    EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
                    registryInfo.setServer(registry.getServer());
                    if (CollectionUtils.hasContent(registry.getUsername()))
                    {
                        registryInfo.setUsername(registry.getUsername());
                    }
                    registryInfo.setPassword(registry.getPassword());
                    createInstReqBuilder.setRegistry(registryInfo);
                }
                
                String imageName = instParams.get("Image").asText();
                if(!imageName.startsWith(registry.getServer()))
                {
                    instParams.put("Image", registry.getServer() + "/" + imageName);
                }
            }
            
            createInstReqBuilder.setInstParams(instParams.toString());
            createInstReqBuilder.setDevNeedInfo(devNeedStr);
            createInstReqBuilder.setAutoRun(autoRun);
            createInstReqBuilder.setReplicaNum(replica);
            if (StringUtils.isNotEmpty(bpId)) createInstReqBuilder.setBpId(bpId);
            if (StringUtils.isNotEmpty(userId)) createInstReqBuilder.setUserId(userId);
            
            createInstReqBuilder.setInstName(instName);
            createInstReqBuilder.setInstId(instId);
            
            if (CollectionUtils.hasContent(extraParams))
            {
                // set extra_params
                createInstReqBuilder.setExtraParams(extraParams);
            }
            
            EeCommonDef.msg_header.Builder reqHeader = makeReqMsgHeader(MessageName.INST_OPERATOR);
            EeInstDef.msg_inst_operator_body.Builder instOperator = EeInstDef.msg_inst_operator_body.newBuilder();
            instOperator.setOperatorType(OperatorType.CREATE_INST_REQ);
            instOperator.setInstType("docker");
            instOperator.setCreateInstReqBody(createInstReqBuilder);
            
            EeNetMessageApi.ee_net_message netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setInstOperatorBody(instOperator).build();
            submitMessage(nodeId, netMessgae);
        }
        catch (Exception e)
        {
            LOGGER.info("{} params error", instId);
            e.printStackTrace();
            return ErrorCode.SUBMIT_MSG_ERROR.getCode();
            //relesae resources(remove gpu bind and update edge monopoly status: remove)
        }

        return ErrorCode.SUCCESS.getCode();
    }
    
    public int logInstReq(String logId, LogContainerInstReq req, String nodeId, String refId)
    {
        //header
        EeCommonDef.msg_header.Builder header = makeReqMsgHeader(MessageName.INST_OPERATOR);
        header.setSessionId(logId);
        
        //get node id from db
//        TblContainerInstInfo tblContainerInstInfo = cisRepository.selectByPrimaryKey(req.getInstId());
        
        //log inst req body
        EeInstDef.msg_log_inst_req_body.Builder logInstReqBuilder = EeInstDef.msg_log_inst_req_body.newBuilder();
        logInstReqBuilder.setInstId(req.getInstId());
        logInstReqBuilder.setRefId(refId);
        logInstReqBuilder.setHeadOrTail(req.getHead_or_tail());
        logInstReqBuilder.setNumberOfLines(req.getLines());
        
        EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
        body.setLogInstReqBody(logInstReqBuilder).setOperatorType(OperatorType.LOG_INST_REQ).setInstType("docker");
        
        //net message
        EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();
        
        int ret = submitMessage(nodeId, netMessage);
        if (0 != ret)
        {
            LOGGER.error("log inst error: {} and close session: {}", ret, logId);
        }
        
        return ret;
    }
    
    public  int logFollowInstReq(String logId, LogContainerInstReq req, String nodeId, String refId)
    {
        //header
        EeCommonDef.msg_header.Builder header = makeReqMsgHeader(MessageName.INST_OPERATOR);
        
        //get node id from db

        //log inst req body
        EeInstDef.msg_log_follow_inst_req_body.Builder logFollowInstReqBuilder = EeInstDef.msg_log_follow_inst_req_body.newBuilder();
        logFollowInstReqBuilder.setInstId(req.getInstId());
        logFollowInstReqBuilder.setRefId(refId);
        logFollowInstReqBuilder.setLogId(logId);
        
        EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
        body.setLogFollowInstReqBody(logFollowInstReqBuilder).setOperatorType(OperatorType.LOG_FOLLOW_INST_REQ).setInstType("docker");
        
        //net message
        EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();
        
        int ret = submitMessage(nodeId, netMessage);
        if (0 != ret)
        {
            LOGGER.error("log follow inst error: {} and close session: {}", ret, logId);
        }
        
        return ret;
    }
    
    public int listInstReq(String instId, String nodeId)
    {
        //send create msg
        EeInstDef.msg_list_inst_req_body.Builder listInstReqBuilder = EeInstDef.msg_list_inst_req_body.newBuilder();
        listInstReqBuilder.addInstId(instId);
        
        EeCommonDef.msg_header.Builder reqHeader = makeReqMsgHeader(MessageName.INST_OPERATOR);
        EeInstDef.msg_inst_operator_body.Builder instOperator = EeInstDef.msg_inst_operator_body.newBuilder();
        instOperator.setOperatorType(OperatorType.LIST_INST_REQ);
        instOperator.setInstType("docker");
        instOperator.setListInstReqBody(listInstReqBuilder);
        
        EeNetMessageApi.ee_net_message netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setInstOperatorBody(instOperator).build();
        
        return submitMessage(nodeId, netMessgae);
    }
    
    public int lifeMngInstReq(String instId, String refId, String action, String nodeId)
    {
        //header
        EeCommonDef.msg_header.Builder header = makeReqMsgHeader(MessageName.INST_OPERATOR);
        
        //action inst req body
        EeInstDef.msg_life_mng_inst_req_body.Builder lifeMngInstReqBuilder = EeInstDef.msg_life_mng_inst_req_body.newBuilder();
        lifeMngInstReqBuilder.setAction(action);
        lifeMngInstReqBuilder.setInstId(instId);
        lifeMngInstReqBuilder.setRefId(refId);
        
        EeInstDef.msg_inst_operator_body.Builder body = EeInstDef.msg_inst_operator_body.newBuilder();
        body.setLifeMngInstReqBody(lifeMngInstReqBuilder).setOperatorType(OperatorType.LIFE_MNG_INST_REQ).setInstType("docker");
        
        //net message
        EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setInstOperatorBody(body).build();

        addLifeMngEvent(header.getSessionId(), action, instId, new Date().getTime());

        return submitMessage(nodeId, netMessage);
    }

    private void addLifeMngEvent(String sessionId, String action, String instId, long timeStamp)
    {
        InstAction instAction = InstAction.fromName(action);
        if (instAction == null)
        {
            return;
        }
        Pair<String, String> pair = new Pair<>(instId, action);
        switch (instAction)
        {
            case STOP:
            case START:
            case REMOVE:
            case RESTART:
                RedisUtil.set(CisRedisField.CIS_SPAWN_LIFE_EVENT + sessionId, JsonUtils.toJson(pair), 10*60);
                RedisUtil.zadd(CisRedisField.CIS_SPAWN_LIFE_EVENT_SET, sessionId, timeStamp + 60*1000);
                break;
        }
    }
}
