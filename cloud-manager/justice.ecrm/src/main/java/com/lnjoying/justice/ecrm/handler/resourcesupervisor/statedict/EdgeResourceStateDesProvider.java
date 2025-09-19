package com.lnjoying.justice.ecrm.handler.resourcesupervisor.statedict;

import com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider;
import static com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider.newBizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月30日 11:18
 */
public class EdgeResourceStateDesProvider implements IBizModelStateDesProvider
{
    private EdgeResourceStateDesProvider()
    {

    }

    public static final EdgeResourceStateDesProvider INSTANCE = new EdgeResourceStateDesProvider();

    public static final String ACTIVE_STATUS_FIELD = "activeStatus";
    public static final String ONLINE_STATUS_FIELD = "onlineStatus";
    private static final HashMap<String, Map<Integer, BizModelStateInfo>> STATE_DES_DICT = new HashMap<>();
    private static Map<String, Map<Integer, BizModelStateInfo>> unmodifiableStateDesDict;

    static
    {
        STATE_DES_DICT.put(ACTIVE_STATUS_FIELD, Collections.unmodifiableMap(getActiveStatusFieldDesDict()));
        STATE_DES_DICT.put(ONLINE_STATUS_FIELD, Collections.unmodifiableMap(getOnlineStatusFieldDesDict()));

        //放最最后一行
        unmodifiableStateDesDict = Collections.unmodifiableMap(STATE_DES_DICT);
    }

    private static HashMap<Integer, BizModelStateInfo> getActiveStatusFieldDesDict()
    {
        HashMap<Integer, BizModelStateInfo> statusValueDescriptionMap = new HashMap<>();
        statusValueDescriptionMap.put(ActiveStatus.UPGRADE, newBizModelStateInfo(ActiveStatus.UPGRADE, "upgrade", "升级"));
        statusValueDescriptionMap.put(ActiveStatus.ACITVE, newBizModelStateInfo(ActiveStatus.ACITVE, "active", "激活"));
        statusValueDescriptionMap.put(ActiveStatus.INACTIVE, newBizModelStateInfo(ActiveStatus.INACTIVE, "inactive", "未激活"));
        statusValueDescriptionMap.put(ActiveStatus.REMOVED, newBizModelStateInfo(ActiveStatus.REMOVED, "removed", "已删除"));

        return statusValueDescriptionMap;
    }

    private static HashMap<Integer, BizModelStateInfo> getOnlineStatusFieldDesDict()
    {
        HashMap<Integer, BizModelStateInfo> statusValueDescriptionMap = new HashMap<>();
        statusValueDescriptionMap.put(OnlineStatus.ONLINE, newBizModelStateInfo(OnlineStatus.ONLINE, "online", "在线"));
        statusValueDescriptionMap.put(OnlineStatus.OFFLINE, newBizModelStateInfo(OnlineStatus.OFFLINE, "offline", "离线"));
        return statusValueDescriptionMap;
    }


    @Override
    public Map<String, Map<Integer, BizModelStateInfo>> getStateDesDict()
    {
        return unmodifiableStateDesDict;
    }
}
