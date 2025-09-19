package com.lnjoying.justice.cis.facade;

import com.lnjoying.justice.cis.common.constant.CisRedisField;
import com.lnjoying.justice.cis.common.constant.CisTimeConst;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.db.model.TblContainerClsinstInfo;
import com.lnjoying.justice.cis.db.repo.ClsInstRepository;
import com.lnjoying.justice.cis.service.rpcserviceimpl.ContainerServiceImpl;
import com.lnjoying.justice.cis.util.CisUtils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeInstDef;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/7/22 11:52 AM
 */
@Service
public class ClsInstServiceFacade  implements InstService
{
    @Autowired
    private ClsInstRepository clsInstRepository;
    
    private static Logger LOGGER = LogManager.getLogger(ContainerServiceImpl.class);
    
    @Override
    public void updateInst4CreateEvent(EeInstDef.inst_status_desc statusInfo)
    {
        try
        {
            //update db
            TblContainerClsinstInfo record = new TblContainerClsinstInfo();
            record.setInstId(statusInfo.getInstId());
            record.setStatus(statusInfo.getInstStatus());
            String refId = statusInfo.getRefId();
            Date date = new Date();
            record.setUpdateTime(date);
            if (null != refId)
            {
                record.setRefId(refId);
            }
        
            clsInstRepository.updateInst(record);
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
        //update db
        TblContainerClsinstInfo record = clsInstRepository.getInst(statusInfo.getInstId());
        if (record == null)
        {
            LOGGER.error("inst {} maybe removed", statusInfo.getInstId());
            return;
        }
    
        if (statusInfo.getInstStatus() == InstanceState.REMOVED.getCode()
                || statusInfo.getInstStatus() == InstanceState.OBJECT_NOT_EXIST.getCode()
                || statusInfo.getInstStatus() == InstanceState.OBJECT_AUTO_REMOVE.getCode())
        {
            clsInstRepository.removeInst(statusInfo.getInstId());
            return;
        }
        
        Date date = new Date();
        record.setUpdateTime(date);
    
        if (StringUtils.isNotBlank(statusInfo.getExternInfo()))
        {
            InstanceState state = CisUtils.getDockerInstState(statusInfo.getExternInfo(), statusInfo.getInstStatus());
            record.setStatus(state.getCode());
        }
        else
        {
            record.setStatus(statusInfo.getInstStatus());
        }
        //update inst state according to rsp status. insert or update running info when needed.
        
        record.setInspetParams(statusInfo.getExternInfo());
        record.setRefId(statusInfo.getRefId());
        clsInstRepository.updateInst(record);
    }
    
    @Override
    public void dealInstRpt(EeInstDef.inst_status_desc statusInfo, EeCommonDef.msg_route route)
    {
        //update db
        updateInstInfo(statusInfo);
    }
    
    /**
     * @Description:
     * @Author: Regulus
     * @Date: 1/7/22 7:45 PM
     * @Param: nodeId
     */
    @Override
    public void evacuateInst(String nodeId)
    {
        clsInstRepository.removeInstOnNode(nodeId);
    }
    
    @Override
    public void updateInstStatus(String instId, int status)
    {
        TblContainerClsinstInfo tblContainerClsinstInfo = new TblContainerClsinstInfo();
        tblContainerClsinstInfo.setInstId(instId);
        tblContainerClsinstInfo.setStatus(status);
        tblContainerClsinstInfo.setUpdateTime(new Date());
        clsInstRepository.updateInst(tblContainerClsinstInfo);
    }
    
    @Override
    public void dealInstLogRsp(int errorCode, String logId, String logContent)
    {
        String instId = RedisUtil.get(CisRedisField.CLS_LOG_ID_INSTID, logId);
        if (! CollectionUtils.hasContent(instId))
        {
            return;
        }
        
        String key = Utils.buildStr(CisRedisField.CLS_LOG_CONTENT, instId);
        RedisUtil.set(key, logContent, CisTimeConst.CacheLogTime);
    }
}
