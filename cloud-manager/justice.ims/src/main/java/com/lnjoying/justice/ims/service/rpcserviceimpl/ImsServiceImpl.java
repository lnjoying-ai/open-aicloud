package com.lnjoying.justice.ims.service.rpcserviceimpl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lnjoying.justice.ims.common.ImsOperatorType;
import com.lnjoying.justice.ims.facade.ImsRegistryFacade;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeImageDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.service.ims.ImsService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Rpc implementation class
 *
 * @author merak
 **/

@RpcSchema(schemaId = "imsService")
@Slf4j
public class ImsServiceImpl implements ImsService
{
    @Autowired
    private ImsRegistryFacade imsRegistryFacade;

    @Override
    public int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage)
    {

        try
        {
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(edgeMessage.getNetMessage());
            log.info("deliver msg: {}", netMessage);

            EeCommonDef.msg_header head = netMessage.getHead();
            EeImageDef.msg_image_operator_body imageOperatorBody = netMessage.getImageOperatorBody();

            if (imageOperatorBody == null || head == null)
            {
                return ErrorCode.PARAM_ERROR.getCode();
            }

            String msgName = imageOperatorBody.getOperatorType();
            switch (msgName)
            {
                case ImsOperatorType.IMAGE_PULL_RSP:
                    EeImageDef.msg_image_pull_rsp_body imagePullRspBody = imageOperatorBody.getImagePullRspBody();
                    imsRegistryFacade.processImagePullRsp(imagePullRspBody, head);
                    break;
                case ImsOperatorType.IMAGE_OPER_RPT_REQ:
                    EeImageDef.msg_image_oper_rpt_req_body imageOperRptReqBody = imageOperatorBody.getImageOperRptReqBody();
                    EeCommonDef.msg_route route = netMessage.getRoute();
                    imsRegistryFacade.processImageOperRptReq(imageOperRptReqBody, route, head);
                    break;
                default:
            }
        }
        catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
        }

        return ErrorCode.SUCCESS.getCode();
    }
}
