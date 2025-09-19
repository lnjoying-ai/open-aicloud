package com.lnjoying.justice.cluster.agent.handler;

import com.lnjoying.justice.cluster.agent.common.AgentGlobalInfo;
import com.lnjoying.justice.cluster.agent.common.SpringUtil;
import com.lnjoying.justice.cluster.agent.config.AgentConfigYaml;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.netutil.NetMsgBuild;
import com.lnjoying.justice.cluster.agent.util.CipherUtil;
import com.lnjoying.justice.schema.common.ChannelState;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi.ee_net_message;
import com.lnjoying.justice.schema.msg.EeWorkerDef;
import com.lnjoying.justice.schema.msg.MessageName;
import com.micro.core.nework.entity.NetEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SessionManageHandler extends SimpleChannelInboundHandler<ee_net_message>
{
    private static Logger LOGGER = LogManager.getLogger();

    AgentConfigYaml agentConfigYaml;

    public SessionManageHandler()
    {
        agentConfigYaml = SpringUtil.getBean("agentConfigYaml");
        
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ee_net_message netMessage) throws Exception
    {
        if (netMessage.getHead().getMsgName().equals(MessageName.EXCHANGE_GWS_REQ))
        {
            processExchangeGW(ctx, netMessage);
            return;
        }
        
        if (AgentGlobalInfo.channnelState == ChannelState.DISCONNECTED)
        {
            LOGGER.info("recv msg name {}", netMessage.getHead().getMsgName());
            LOGGER.info("recv from {} msg: {} ", ctx.channel().remoteAddress(), netMessage.toString());
            //如果channel是处于断连状态，要先登录，在登录之前本地必须要有设备的数据（设备数据可以是用户录入，或是设备自动注册）
            // 如果既不是注册消息，也不是登录消息，直接关闭
            if (! netMessage.getHead().getMsgName().equals(MessageName.WORKER_REG_RSP)
             && ! netMessage.getHead().getMsgName().equals(MessageName.WORKER_LOGIN_RSP)
             )
            {
                LOGGER.error("the peer should login or register.");
                ctx.close();
                return;
            }

            if (netMessage.getHead().getMsgName().equals(MessageName.WORKER_REG_RSP))
            {
                processWorkerReg(ctx, netMessage);
                return;
            }

            if (netMessage.getHead().getMsgName().equals(MessageName.WORKER_LOGIN_RSP))
            {
                processWorkerLogin(ctx, netMessage);
                return;
            }

            return;
        }
    
        

        ctx.fireChannelRead(netMessage);
    }
    

    private void processWorkerReg(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.debug("recv: {}", netMessage.getHead().getMsgName());

        NodeConfig nodeConfig = agentConfigYaml.getNodeConfig();
        try
        {
            if (!CipherUtil.checkSign(netMessage.getHead().getExtenInfoMap(), netMessage.getHead().getNonce()))
            {
                LOGGER.info("sign error. ");
                ctx.close();
            }
            
            EeWorkerDef.msg_worker_reg_rsp_body regRspBody = netMessage.getWorkerRegRspBody();
            if (regRspBody.getErrorCode() == 0)
            {
                nodeConfig.setRegFlag(1);
                ee_net_message loginReq = NetMsgBuild.buildWorkerLoginMessage(nodeConfig);
                ctx.writeAndFlush(loginReq);
                agentConfigYaml.dumpNodeConfig(nodeConfig);
            }
            else
            {
                nodeConfig.setRegFlag(0);
                ctx.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("hello.");
        }
    }
    
    private void processExchangeGW(ChannelHandlerContext ctx, ee_net_message netMessage)
    {
        LOGGER.info("process exchange gw");
        List<EeCommonDef.gw_node_info> gws = netMessage.getExchangeGwsReqBody().getGwNodesList();
        if (gws == null || gws.isEmpty())
        {
            return;
        }
    
        List<NetEntity> candidateList = new ArrayList<>();
        String regionId = agentConfigYaml.getNodeConfig().getRegion_id();
        LOGGER.info("process exchange gw, local region id:{}", regionId);
    
        gws.forEach(gw -> {
            List<String> regions = gw.getRegionIdsList();
            for (String region : regions)
            {
                if (region.equals(regionId))
                {
                    LOGGER.info("find addr {} for {}", gw.getAddr(), regionId);
                    NetEntity entity = new NetEntity();
                    entity.setChannelType(NetEntity.ChannelType.CLIENT);
                    entity.setHost(gw.getAddr().getIp());
                    entity.setPort(gw.getAddr().getPort());
                    candidateList.add(entity);
                }
            }
        });
        
        if (! candidateList.isEmpty())
        {
            LOGGER.info("save candidate addr");
            agentConfigYaml.saveGwCandidate(candidateList);
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
    
            EeWorkerDef.msg_worker_login_rsp_body loginRspBody = netMessage.getWorkerLoginRspBody();
            if (loginRspBody.getErrorCode() != 0)
            {
                ctx.close();
            }
            AgentGlobalInfo.channnelState = ChannelState.CONNECTED;
            LOGGER.info("login success");
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
        AgentGlobalInfo.channnelState = ChannelState.DISCONNECTED;
        if (ctx.channel().isActive())
        {
            ctx.channel().close();
        }

        LOGGER.info("o o the channel have been disconnect. remote: {}", ctx.channel().remoteAddress());

        try
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("channel error {}", e.getMessage());
        }
        finally
        {
        }
        ctx.fireChannelInactive();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        AgentGlobalInfo.channnelState = ChannelState.DISCONNECTED;
        super.exceptionCaught(ctx, cause);
        if(ctx != null && ctx.channel() != null && ctx.channel().isActive())
        {
            ctx.close();
        }
        
        if (AgentGlobalInfo.gConnectChanel != null && AgentGlobalInfo.gConnectChanel.isActive())
        {
            AgentGlobalInfo.gConnectChanel.close();
        }
        ctx.fireExceptionCaught(cause);
    }
}
