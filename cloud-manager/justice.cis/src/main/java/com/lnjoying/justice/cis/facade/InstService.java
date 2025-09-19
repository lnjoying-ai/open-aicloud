package com.lnjoying.justice.cis.facade;

import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeInstDef;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 1/7/22 2:04 PM
 */
public interface InstService
{
    void updateInst4CreateEvent(EeInstDef.inst_status_desc statusInfo);
    void updateInstInfo(EeInstDef.inst_status_desc statusInfo);
    void dealInstRpt(EeInstDef.inst_status_desc statusInfo, EeCommonDef.msg_route route);
    void evacuateInst(String nodeId);
    void updateInstStatus(String instId, int status);
    
    void dealInstLogRsp(int errorCode, String logId, String logContent);
}
