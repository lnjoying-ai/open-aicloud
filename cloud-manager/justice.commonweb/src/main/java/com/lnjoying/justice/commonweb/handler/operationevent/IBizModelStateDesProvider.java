package com.lnjoying.justice.commonweb.handler.operationevent;

import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;

import java.util.Map;

public interface IBizModelStateDesProvider
{
    Map<String, Map<Integer, BizModelStateInfo>> getStateDesDict();

    static BizModelStateInfo newBizModelStateInfo(Integer stateValue, String enDescription, String cnDescription)
    {
        return BizModelStateInfo.builder()
                .code(stateValue)
                .enDescription(enDescription)
                .cnDescription(cnDescription)
                .build();
    }
}
