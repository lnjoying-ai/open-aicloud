package com.lnjoying.justice.aos.handler.resourcesupervisor.statedict;

import com.lnjoying.justice.aos.db.model.RecordStatus;
import com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider;
import static com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider.newBizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月30日 10:49
 */
@Component
public class RecordStatusDesProvider implements IBizModelStateDesProvider
{
    private RecordStatusDesProvider()
    {

    }

    public static final RecordStatusDesProvider INSTANCE = new RecordStatusDesProvider();

    public static final String STATUS_FIELD = "status";
    private static final HashMap<String, Map<Integer, BizModelStateInfo>> STATE_DES_DICT = new HashMap<>();
    private static Map<String, Map<Integer, BizModelStateInfo>> unmodifiableStateDesDict;

    static
    {
        STATE_DES_DICT.put(STATUS_FIELD, Collections.unmodifiableMap(getStatusFieldDesDict()));

        //放最后一行
        unmodifiableStateDesDict = Collections.unmodifiableMap(STATE_DES_DICT);
    }

    private static HashMap<Integer, BizModelStateInfo> getStatusFieldDesDict()
    {
        HashMap<Integer, BizModelStateInfo> statusValueDescriptionMap = new HashMap<>();
        statusValueDescriptionMap.put(RecordStatus.NORMAL.value(), newBizModelStateInfo(RecordStatus.NORMAL.value(), "normal", "正常"));
        statusValueDescriptionMap.put(RecordStatus.DELETED.value(), newBizModelStateInfo(RecordStatus.DELETED.value(), "deleted", "已删除"));

        return statusValueDescriptionMap;
    }


    @Override
    public Map<String, Map<Integer, BizModelStateInfo>> getStateDesDict()
    {
        return unmodifiableStateDesDict;
    }
}