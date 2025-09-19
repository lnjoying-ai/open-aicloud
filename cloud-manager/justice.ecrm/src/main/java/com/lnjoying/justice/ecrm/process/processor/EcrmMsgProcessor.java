package com.lnjoying.justice.ecrm.process.processor;

import com.lnjoying.justice.ecrm.common.constant.EcrmMsgType;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfo;
import com.lnjoying.justice.ecrm.domain.dto.request.ConfigEdgeReq;
import com.lnjoying.justice.ecrm.domain.dto.request.RegionInputReq;
import com.lnjoying.justice.ecrm.facade.ServiceAgentService;
import com.micro.core.common.Pair;
import com.lnjoying.justice.ecrm.facade.EdgeServiceFacade;
import com.lnjoying.justice.ecrm.facade.GwServiceFacade;
import com.lnjoying.justice.ecrm.facade.RegionServiceFacade;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Component
public class EcrmMsgProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    EdgeServiceFacade edgeServiceFacade;

    @Autowired
    GwServiceFacade gwServiceFacade;

    @Autowired
    RegionServiceFacade regionServiceFacade;

    @Autowired
    private ServiceAgentService serviceAgentService;

    @Override
    public void start()
    {

        LOGGER.info("ecrm message process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("ecrm message process stopped");
    }

    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                switch(pack.getMsgType())
                {
                    case EcrmMsgType.CONFIG_NODE:
                        edgeServiceFacade.processConfigEdge((Pair<String, ConfigEdgeReq>) pack.getMessageObj());
                        break;
                    case EcrmMsgType.ACTIVE_EDGE:
                        edgeServiceFacade.processActiveEdge((TblEdgeComputeInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.DEACTIVE_EDGE:
                        edgeServiceFacade.processDeactiveEdge((TblEdgeComputeInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.EVACUATE_EDGE:
                        edgeServiceFacade.processEvacuateEdge((TblEdgeComputeInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.REMOVE_EDGE:
                        edgeServiceFacade.processRemoveEdge((TblEdgeComputeInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.REBOOT_EDGE:
                        edgeServiceFacade.processRebootEdge((TblEdgeComputeInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.ACTIVE_GW:
                        gwServiceFacade.processActiveGw((TblEdgeGwInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.DEACTIVE_GW:
                        gwServiceFacade.processDeactiveGw((TblEdgeGwInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.REMOVE_GW:
                        gwServiceFacade.processRemoveGw((TblEdgeGwInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.REBOOT_GW:
                        gwServiceFacade.processRebootGw((TblEdgeGwInfo) pack.getMessageObj());
                        break;
                    case EcrmMsgType.SET_REGION:
                        regionServiceFacade.processRegionBindGw((RegionInputReq) pack.getMessageObj());
                        break;
                    case EcrmMsgType.RM_REGION:
                        regionServiceFacade.processRegionBindGw((List<TblRegionBindInfo>)pack.getMessageObj());
                        break;
					case EcrmMsgType.REG_GW:
						gwServiceFacade.processRegGw((EeCommonDef.msg_gw_reg_req_body)pack.getMessageObj());
						break;
                    case EcrmMsgType.LOGIN_GW:
                        gwServiceFacade.processLoginGw((EeCommonDef.msg_gw_login_cloud_req_body)pack.getMessageObj());
                        break;
                    case EcrmMsgType.GET_GW_LIST:
                        gwServiceFacade.processGetGwList((Pair) pack.getMessageObj());
                        break;
                    case EcrmMsgType.SYNC_EDGE_IF_STATE:
                         edgeServiceFacade.processEdgeIFState((Pair<String, EeNetMessageApi.ee_net_message>) pack.getMessageObj());
                         break;
                    case EcrmMsgType.GET_EDGE_REQ:
                        edgeServiceFacade.processGetEdgeReq((Pair<String, EeNetMessageApi.ee_net_message>) pack.getMessageObj());
                        break;
                    case EcrmMsgType.SYNC_WORKER_IF_STATE:
                        serviceAgentService.processWorkerIFState((Pair<String, EeNetMessageApi.ee_net_message>) pack.getMessageObj());
                        break;
                    case EcrmMsgType.CREATE_SERVICE_AGENT:
                        serviceAgentService.processCreateServiceAgent((String)pack.getMessageObj());
                        break;
                    default:
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("EcrmMsgProcessor.run error {}", e.getMessage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.error("EcrmMsgProcessor.run error {}", e.getMessage());
            }
        }

    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }




}
