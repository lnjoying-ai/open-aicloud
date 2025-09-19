package com.lnjoying.justice.servicegw.service.rpc;

import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.servicegw.common.FwdOperName;
import com.lnjoying.justice.servicegw.common.TunnelOperName;
import com.lnjoying.justice.servicegw.service.TunnelService;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.service.clsvr.ClusterServerService;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/1/22 5:08 PM
 */
@RpcSchema(schemaId = "clusterServer")
public class ClusterServerServiceImpl implements ClusterServerService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    TunnelService tunnelService;
    
    @Override
    public int deliver(@ApiParam(name = "message")WorkerMessage workerMessage)
    {
        CombRetPacket ret = new CombRetPacket();
    
        try
        {
            EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.parseFrom(workerMessage.getNetMessage());
            switch (netMessage.getHead().getMsgName())
            {
                case MessageName.TUNNEL_OPERATOR:
                    onTunnelOperatorMessage(netMessage);
                    break;
                case MessageName.FWD_OPERATOR:
                    onFwdOperatorMessage(netMessage);
                    break;
    
                default:
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("handler tunnel msg error. msg:{}",e.getMessage());
            ret.setStatus(ErrorCode.SystemError.getCode());
            return ErrorCode.DEV_ABNORMAL.getCode();
        }
    
        return ErrorCode.SUCCESS.getCode();
    }
    
    private void onTunnelOperatorMessage(EeNetMessageApi.ee_net_message netMessage)
    {
        EeTunnelDef.msg_tunnel_operator_body tunnelOperatorBody = netMessage.getTunnelOperatorBody();
        switch (tunnelOperatorBody.getOperatorType())
        {
            case TunnelOperName.CREATE_TUNNEL_RSP:
                tunnelService.onCreateTunnelRsp(tunnelOperatorBody.getCreateTunnelRspBody());
                break;
            case TunnelOperName.FWD_TUNNEL_DATA_RSP:
                tunnelService.onFwdTunnelMessageRsp(tunnelOperatorBody.getFwdTunnelDataRspBody());
                break;
            case TunnelOperName.CLOSE_TUNNEL_RSP:
                tunnelService.onCloseTunnelRsp(tunnelOperatorBody.getCloseTunnelRspBody());
                break;
            default:
                break;
        }
    }
    
    private void onFwdOperatorMessage(EeNetMessageApi.ee_net_message netMessage)
    {
        EeFwdDef.msg_fwd_operator_body tunnelOperatorBody = netMessage.getFwdOperatorBody();
        switch (tunnelOperatorBody.getOperatorType())
        {
            case FwdOperName.FWD_DATA_RSP:
                tunnelService.onDirectFwdMessageRsp(tunnelOperatorBody.getFwdDataRspBody());
            default:
                break;
        }
    }
}
