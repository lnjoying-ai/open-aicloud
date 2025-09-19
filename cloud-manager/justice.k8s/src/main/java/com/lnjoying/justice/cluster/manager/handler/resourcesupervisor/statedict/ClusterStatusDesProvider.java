package com.lnjoying.justice.cluster.manager.handler.resourcesupervisor.statedict;

import com.lnjoying.justice.cluster.manager.common.ClusterActiveStatus;
import com.lnjoying.justice.cluster.manager.common.ClusterStatus;
import com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider;

import static com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider.newBizModelStateInfo;

import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2024年01月22日 19:23
 */
public class ClusterStatusDesProvider implements IBizModelStateDesProvider
{
    private ClusterStatusDesProvider()
    {

    }

    public static final ClusterStatusDesProvider INSTANCE = new ClusterStatusDesProvider();

    public static final String STATUS_FIELD = "status";
    public static final String SERVICE_STATE_FIELD = "serviceState";
    private static final HashMap<String, Map<Integer, BizModelStateInfo>> STATE_DES_DICT = new HashMap<>();
    private static Map<String, Map<Integer, BizModelStateInfo>> unmodifiableStateDesDict;

    static
    {
        STATE_DES_DICT.put(STATUS_FIELD, Collections.unmodifiableMap(getStatusFieldDesDict()));
        STATE_DES_DICT.put(SERVICE_STATE_FIELD, Collections.unmodifiableMap(getServiceStateFieldDesDict()));

        //放最后一行
        unmodifiableStateDesDict = Collections.unmodifiableMap(STATE_DES_DICT);
    }

    private static HashMap<Integer, BizModelStateInfo> getStatusFieldDesDict()
    {
        HashMap<Integer, BizModelStateInfo> statusValueDescriptionMap = new HashMap<>();
        for (ClusterStatus status : ClusterStatus.values())
        {
            statusValueDescriptionMap.put(status.getCode(), newBizModelStateInfo(status.getCode(), status.getMessage().get("en"), status.getMessage().get("cn")));
        }
        return statusValueDescriptionMap;
    }

    private static HashMap<Integer, BizModelStateInfo> getServiceStateFieldDesDict()
    {
        HashMap<Integer, BizModelStateInfo> statusValueDescriptionMap = new HashMap<>();
        statusValueDescriptionMap.put(ClusterActiveStatus.ACTIVE, newBizModelStateInfo(ClusterActiveStatus.ACTIVE, "active", "可用"));
        statusValueDescriptionMap.put(ClusterActiveStatus.INACTIVE, newBizModelStateInfo(ClusterActiveStatus.INACTIVE, "inactive", "不可用"));
        statusValueDescriptionMap.put(ClusterActiveStatus.ABSENT, newBizModelStateInfo(ClusterActiveStatus.INACTIVE, "absent", "不存在"));
        return statusValueDescriptionMap;
    }

    @Override
    public Map<String, Map<Integer, BizModelStateInfo>> getStateDesDict()
    {
        return unmodifiableStateDesDict;
    }
}
