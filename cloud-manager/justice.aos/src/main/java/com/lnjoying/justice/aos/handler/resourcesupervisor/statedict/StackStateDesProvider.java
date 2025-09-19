package com.lnjoying.justice.aos.handler.resourcesupervisor.statedict;

import com.lnjoying.justice.aos.common.StackState;
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
 * @date: 2023年11月29日 17:02
 */
public class StackStateDesProvider implements IBizModelStateDesProvider
{
    private StackStateDesProvider()
    {

    }

    public static final StackStateDesProvider INSTANCE = new StackStateDesProvider();

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
        statusValueDescriptionMap.put(StackState.UNKNOWN, newBizModelStateInfo(StackState.UNKNOWN, "unknown", "未知"));
        statusValueDescriptionMap.put(StackState.MAKED, newBizModelStateInfo(StackState.MAKED, "maked", "创建中"));
        statusValueDescriptionMap.put(StackState.SPAWNED, newBizModelStateInfo(StackState.SPAWNED, "spawned", "创建中"));
        statusValueDescriptionMap.put(StackState.WAIT_ASSIGN, newBizModelStateInfo(StackState.WAIT_ASSIGN, "wait assign", "创建中"));
        statusValueDescriptionMap.put(StackState.ASSIGNED, newBizModelStateInfo(StackState.ASSIGNED, "assigned", "创建中"));
        statusValueDescriptionMap.put(StackState.FWD, newBizModelStateInfo(StackState.FWD, "fwd", "创建中"));
        statusValueDescriptionMap.put(StackState.SYNC_CFG, newBizModelStateInfo(StackState.SYNC_CFG, "sync cfg", "创建中"));
        statusValueDescriptionMap.put(StackState.CFG_SYNCED, newBizModelStateInfo(StackState.CFG_SYNCED, "cfg synced", "创建中"));
        statusValueDescriptionMap.put(StackState.EDGE_UNREACHABLE, newBizModelStateInfo(StackState.EDGE_UNREACHABLE, "edge unreachable", "创建失败"));
        statusValueDescriptionMap.put(StackState.NO_MATCH_NODE, newBizModelStateInfo(StackState.NO_MATCH_NODE, "no match node", "创建失败"));
        statusValueDescriptionMap.put(StackState.SPAWN_SYSTEM_STOP, newBizModelStateInfo(StackState.SPAWN_SYSTEM_STOP, "spawn system stop", "容器退出"));
        statusValueDescriptionMap.put(StackState.SPAWN_USER_STOP_QUIT, newBizModelStateInfo(StackState.SPAWN_USER_STOP_QUIT, "spawn user stop quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.SPAWN_OVERDUE_QUIT, newBizModelStateInfo(StackState.SPAWN_OVERDUE_QUIT, "spawn overdue quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.FIN_SYSTEM_STOP, newBizModelStateInfo(StackState.FIN_SYSTEM_STOP, "fin system stop", "容器退出"));
        statusValueDescriptionMap.put(StackState.FIN_USER_STOP_QUIT, newBizModelStateInfo(StackState.FIN_USER_STOP_QUIT, "fin user stop quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.FIN_OVERDUE_QUIT, newBizModelStateInfo(StackState.FIN_OVERDUE_QUIT, "fin overdue quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.CLOUD_CRETAE_FAILED_PARAMS, newBizModelStateInfo(StackState.CLOUD_CRETAE_FAILED_PARAMS, "cloud create failed params", "创建失败"));
        statusValueDescriptionMap.put(StackState.CLOUD_CRETAE_FAILED_OVERDUE, newBizModelStateInfo(StackState.CLOUD_CRETAE_FAILED_OVERDUE, "cloud create failed overdue", "创建失败"));
        statusValueDescriptionMap.put(StackState.SPAWNED_CLOUD_REMOVE, newBizModelStateInfo(StackState.SPAWNED_CLOUD_REMOVE, "spawned cloud remove", "已删除"));
        statusValueDescriptionMap.put(StackState.FIN_CLOUD_REMOVE, newBizModelStateInfo(StackState.FIN_CLOUD_REMOVE, "fin cloud remove", "已删除"));
        statusValueDescriptionMap.put(StackState.CLOUD_SYSTEM_ABNORMAL, newBizModelStateInfo(StackState.CLOUD_SYSTEM_ABNORMAL, "cloud system abnormal", "系统异常"));
        statusValueDescriptionMap.put(StackState.QUEUEING, newBizModelStateInfo(StackState.QUEUEING, "queueing", "创建中"));
        statusValueDescriptionMap.put(StackState.IMAGE_DOWNLOADING, newBizModelStateInfo(StackState.IMAGE_DOWNLOADING, "image downloading", "创建中"));
        statusValueDescriptionMap.put(StackState.IMAGE_DOWNLOADED, newBizModelStateInfo(StackState.IMAGE_DOWNLOADED, "image downloaded", "创建中"));
        statusValueDescriptionMap.put(StackState.CREATED, newBizModelStateInfo(StackState.CREATED, "created", "创建成功"));
        statusValueDescriptionMap.put(StackState.STARTING, newBizModelStateInfo(StackState.STARTING, "starting", "运行中"));
        statusValueDescriptionMap.put(StackState.RUNNING, newBizModelStateInfo(StackState.RUNNING, "running", "运行中"));
        statusValueDescriptionMap.put(StackState.RESTARTING, newBizModelStateInfo(StackState.RESTARTING, "restarting", "运行中"));
        statusValueDescriptionMap.put(StackState.SUCCESS_QUIT, newBizModelStateInfo(StackState.SUCCESS_QUIT, "success quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.USER_STOP_QUIT, newBizModelStateInfo(StackState.USER_STOP_QUIT, "user stop quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.OVERDUE_QUIT, newBizModelStateInfo(StackState.OVERDUE_QUIT, "overdue quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.ABNORMAL_QUIT, newBizModelStateInfo(StackState.ABNORMAL_QUIT, "abnormal quit", "容器退出"));
        statusValueDescriptionMap.put(StackState.SYSTEM_STOP, newBizModelStateInfo(StackState.SYSTEM_STOP, "system stop", "系统异常"));
        statusValueDescriptionMap.put(StackState.HARDWARE_ERROR, newBizModelStateInfo(StackState.HARDWARE_ERROR, "hardware error", "创建失败"));
        statusValueDescriptionMap.put(StackState.NO_IMAGE, newBizModelStateInfo(StackState.NO_IMAGE, "no image", "创建失败"));
        statusValueDescriptionMap.put(StackState.IMAGE_DOWNLOAD_FAILED, newBizModelStateInfo(StackState.IMAGE_DOWNLOAD_FAILED, "image download failed", "创建失败"));
        statusValueDescriptionMap.put(StackState.CREATE_FAILED, newBizModelStateInfo(StackState.CREATE_FAILED, "create failed", "创建失败"));
        statusValueDescriptionMap.put(StackState.REMOVED, newBizModelStateInfo(StackState.REMOVED, "removed", "容器不存在"));
        statusValueDescriptionMap.put(StackState.OBJECT_NOT_EXIST, newBizModelStateInfo(StackState.OBJECT_NOT_EXIST, "object not exist", "容器不存在"));
        statusValueDescriptionMap.put(StackState.OBJECT_AUTO_REMOVE, newBizModelStateInfo(StackState.OBJECT_AUTO_REMOVE, "object auto remove", "容器不存在"));
        statusValueDescriptionMap.put(StackState.SYSTEM_ABNORMAL, newBizModelStateInfo(StackState.SYSTEM_ABNORMAL, "system abnormal", "系统异常"));

        return statusValueDescriptionMap;
    }


    @Override
    public Map<String, Map<Integer, BizModelStateInfo>> getStateDesDict()
    {
        return unmodifiableStateDesDict;
    }
}
