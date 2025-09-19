package com.lnjoying.justice.omc.service;

import cn.hutool.crypto.SecureUtil;
import com.lnjoying.justice.schema.constant.ConfigConstants;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeFileDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.MessageName;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/12/27 15:06
 */

@Slf4j
@Service
public class PrometheusRuleService
{

    @Autowired
    NetMessageServiceFacade netMessageServiceFacade;

    public boolean sendFileCreateReq(String type, String bpId, String userId, String uniqueKey, String content, String nodeId)
    {
        try
        {

            EeFileDef.msg_file_create_req_body.Builder fileCreateBody = EeFileDef.msg_file_create_req_body.newBuilder();
            fileCreateBody.setContent(content);
            fileCreateBody.setFilePath(String.format("/var/lnjoying/volume/monitor/target_files/%s/%s-%s-%s.json", type, bpId, userId,uniqueKey));
            fileCreateBody.setHash(SecureUtil.md5(content));

            EeFileDef.msg_file_operator_body.Builder fileOperatorBody = EeFileDef.msg_file_operator_body.newBuilder();
            fileOperatorBody.setAlgorithm(ConfigConstants.FILE_CREATE_ALGORITHM);
            fileOperatorBody.setOperatorType(MessageName.FILE_CREATE_REQ);
            fileOperatorBody.setFileCreateReqBody(fileCreateBody);

            EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.FILE_OPERATOR, ConfigConstants.REQ_MSG_HEADER_NAME_OMC_PREFIX + uniqueKey + "." + Utils.getRandomStr(10));

            EeNetMessageApi.ee_net_message.Builder netMessgae = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setFileOperatorBody(fileOperatorBody);

            byte [] message = netMessgae.build().toByteArray();
            netMessageServiceFacade.submitMessage(nodeId, message);
            return true;
        }
        catch (Exception e)
        {
            log.error("sendFileCreateReq error", e);
        }
        return false;
    }

}
