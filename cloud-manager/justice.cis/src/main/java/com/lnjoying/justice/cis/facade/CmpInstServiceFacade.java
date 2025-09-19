package com.lnjoying.justice.cis.facade;

import com.lnjoying.justice.cis.service.CombRpcService;
import com.lnjoying.justice.cis.service.rpcserviceimpl.ContainerServiceImpl;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeInstDef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 */
@Service
public class CmpInstServiceFacade extends ClsInstServiceFacade
{
    private static Logger LOGGER = LogManager.getLogger(ContainerServiceImpl.class);

    @Autowired
    CombRpcService combRpcService;
    
    @Override
    public void updateInst4CreateEvent(EeInstDef.inst_status_desc statusInfo)
    {
        super.updateInst4CreateEvent(statusInfo);

        try
        {
            combRpcService.getCloudManagerService().updateCloudAgentStatus(statusInfo.getInstId(), statusInfo.getInstStatus());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create container instance rsp exception: {}", e.getMessage());
        }
    }
    
    @Override
    public void updateInstInfo(EeInstDef.inst_status_desc statusInfo)
    {
        super.updateInstInfo(statusInfo);
        try
        {
            combRpcService.getCloudManagerService().updateCloudAgentStatus(statusInfo.getInstId(), statusInfo.getInstStatus());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create container instance rsp exception: {}", e.getMessage());
        }
    }
    
    @Override
    public void dealInstRpt(EeInstDef.inst_status_desc statusInfo, EeCommonDef.msg_route route)
    {
        //update db
        updateInstInfo(statusInfo);
    }
    
    @Override
    public void updateInstStatus(String instId, int status)
    {
        super.updateInstStatus(instId, status);
        try
        {
            combRpcService.getCloudManagerService().updateCloudAgentStatus(instId, status);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            LOGGER.error("create container instance rsp exception: {}", e.getMessage());
        }
    }
}
