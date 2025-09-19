package com.lnjoying.justice.cluster.agent.process;

import com.lnjoying.justice.cluster.agent.common.AgentGlobalInfo;
import com.lnjoying.justice.cluster.agent.config.AgentConfigYaml;
import com.lnjoying.justice.cluster.agent.model.NodeConfig;
import com.lnjoying.justice.cluster.agent.net.AgentSvrConnector;
import com.lnjoying.justice.cluster.agent.netutil.NetMsgBuild;
import com.lnjoying.justice.schema.common.ChannelState;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.micro.core.nework.entity.NetEntity;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description
 * @author Regulus
 * @date 11/29/21 4:49 PM
 */
@Component
public class HeartBeatProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    AgentSvrConnector agentSvrConnector;
    
    @Autowired
    AgentConfigYaml agentConfigYaml;
    
    
    public HeartBeatProcessor()
    {
        LOGGER.info("init heart beat check processor");
    }
    
    @Override
    public void run()
    {
        connectsvr();
        sendHeartBeat();
    }
    
    
    void connectsvr()
    {
        try
        {
            NodeConfig nodeConfig = agentConfigYaml.getNodeConfig();
            List<NetEntity> gwCandidates = agentConfigYaml.getGwCandidates();
            if (AgentGlobalInfo.gConnectChanel == null || ! AgentGlobalInfo.gConnectChanel.isActive())
            {
                LOGGER.info("send connect msg");
                agentSvrConnector.connect(nodeConfig, gwCandidates);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("connectsvr error {}", e.getMessage());
        }
    }
  
    void sendHeartBeat()
    {
        try
        {
            if (AgentGlobalInfo.channnelState != ChannelState.CONNECTED)
            {
                return;
            }

            EeNetMessageApi.ee_net_message heartBeatReq =  NetMsgBuild.buildHeartBeatReq();
            LOGGER.info("send heart beat, ", heartBeatReq.getHead().getMsgName());
            if (AgentGlobalInfo.gConnectChanel != null && AgentGlobalInfo.gConnectChanel.isActive())
            {
                AgentGlobalInfo.gConnectChanel.writeAndFlush(heartBeatReq);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("sendHeartBeat error {}", e.getMessage());
        }
    }
}
