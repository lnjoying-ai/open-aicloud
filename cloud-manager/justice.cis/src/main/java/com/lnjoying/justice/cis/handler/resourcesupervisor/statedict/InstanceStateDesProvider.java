package com.lnjoying.justice.cis.handler.resourcesupervisor.statedict;

import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;

import static com.lnjoying.justice.commonweb.handler.operationevent.IBizModelStateDesProvider.newBizModelStateInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月30日 11:04
 */
public class InstanceStateDesProvider implements IBizModelStateDesProvider
{
    private InstanceStateDesProvider()
    {

    }

    public static final InstanceStateDesProvider INSTANCE = new InstanceStateDesProvider();

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
        statusValueDescriptionMap.put(InstanceState.UNKNOWN.getCode(), newBizModelStateInfo(InstanceState.UNKNOWN.getCode(), "unknown", "未知"));
        statusValueDescriptionMap.put(InstanceState.MAKED.getCode(), newBizModelStateInfo(InstanceState.MAKED.getCode(), "maked", "创建中"));
        statusValueDescriptionMap.put(InstanceState.SPAWNED.getCode(), newBizModelStateInfo(InstanceState.SPAWNED.getCode(), "spawned", "创建中"));
        statusValueDescriptionMap.put(InstanceState.WAIT_ASSIGN.getCode(), newBizModelStateInfo(InstanceState.WAIT_ASSIGN.getCode(), "wait assign", "创建中"));
        statusValueDescriptionMap.put(InstanceState.ASSIGNED.getCode(), newBizModelStateInfo(InstanceState.ASSIGNED.getCode(), "assigned", "创建中"));
        statusValueDescriptionMap.put(InstanceState.FWD.getCode(), newBizModelStateInfo(InstanceState.FWD.getCode(), "fwd", "创建中"));
        statusValueDescriptionMap.put(InstanceState.EDGE_UNREACHABLE.getCode(), newBizModelStateInfo(InstanceState.EDGE_UNREACHABLE.getCode(), "edge unreachable", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.NO_MATCH_NODE.getCode(), newBizModelStateInfo(InstanceState.NO_MATCH_NODE.getCode(), "no match node", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.SPAWN_SYSTEM_STOP.getCode(), newBizModelStateInfo(InstanceState.SPAWN_SYSTEM_STOP.getCode(), "spawn system stop", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.SPAWN_USER_STOP_QUIT.getCode(), newBizModelStateInfo(InstanceState.SPAWN_USER_STOP_QUIT.getCode(), "spawn user stop quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.SPAWN_OVERDUE_QUIT.getCode(), newBizModelStateInfo(InstanceState.SPAWN_OVERDUE_QUIT.getCode(), "spawn overdue quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.FIN_SYSTEM_STOP.getCode(), newBizModelStateInfo(InstanceState.FIN_SYSTEM_STOP.getCode(), "fin system stop", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.FIN_USER_STOP_QUIT.getCode(), newBizModelStateInfo(InstanceState.FIN_USER_STOP_QUIT.getCode(), "fin user stop quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.FIN_OVERDUE_QUIT.getCode(), newBizModelStateInfo(InstanceState.FIN_OVERDUE_QUIT.getCode(), "fin overdue quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode(), newBizModelStateInfo(InstanceState.CLOUD_CRETAE_FAILED_PARAMS.getCode(), "cloud create failed params", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.CLOUD_CRETAE_FAILED_OVERDUE.getCode(), newBizModelStateInfo(InstanceState.CLOUD_CRETAE_FAILED_OVERDUE.getCode(), "cloud create failed overdue", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), newBizModelStateInfo(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), "spawned cloud remove", "已删除"));
        statusValueDescriptionMap.put(InstanceState.FIN_CLOUD_REMOVE.getCode(), newBizModelStateInfo(InstanceState.FIN_CLOUD_REMOVE.getCode(), "fin cloud remove", "已删除"));
        statusValueDescriptionMap.put(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode(), newBizModelStateInfo(InstanceState.CLOUD_SYSTEM_ABNORMAL.getCode(), "cloud system abnormal", "系统异常"));
        statusValueDescriptionMap.put(InstanceState.QUEUEING.getCode(), newBizModelStateInfo(InstanceState.QUEUEING.getCode(), "queueing", "创建中"));
        statusValueDescriptionMap.put(InstanceState.IMAGE_DOWNLOADING.getCode(), newBizModelStateInfo(InstanceState.IMAGE_DOWNLOADING.getCode(), "image downloading", "创建中"));
        statusValueDescriptionMap.put(InstanceState.IMAGE_DOWNLOADED.getCode(), newBizModelStateInfo(InstanceState.IMAGE_DOWNLOADED.getCode(), "image downloaded", "创建中"));
        statusValueDescriptionMap.put(InstanceState.CREATED.getCode(), newBizModelStateInfo(InstanceState.CREATED.getCode(), "created", "创建成功"));
        statusValueDescriptionMap.put(InstanceState.STARTING.getCode(), newBizModelStateInfo(InstanceState.STARTING.getCode(), "starting", "运行中"));
        statusValueDescriptionMap.put(InstanceState.RUNNING.getCode(), newBizModelStateInfo(InstanceState.RUNNING.getCode(), "running", "运行中"));
        statusValueDescriptionMap.put(InstanceState.RESTARTING.getCode(), newBizModelStateInfo(InstanceState.RESTARTING.getCode(), "restarting", "运行中"));
        statusValueDescriptionMap.put(InstanceState.SUCCESS_QUIT.getCode(), newBizModelStateInfo(InstanceState.SUCCESS_QUIT.getCode(), "success quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.USER_STOP_QUIT.getCode(), newBizModelStateInfo(InstanceState.USER_STOP_QUIT.getCode(), "user stop quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.ABNORMAL_QUIT.getCode(), newBizModelStateInfo(InstanceState.ABNORMAL_QUIT.getCode(), "abnormal quit", "容器退出"));
        statusValueDescriptionMap.put(InstanceState.SYSTEM_STOP.getCode(), newBizModelStateInfo(InstanceState.SYSTEM_STOP.getCode(), "system stop", "系统异常"));
        statusValueDescriptionMap.put(InstanceState.HARDWARE_ERROR.getCode(), newBizModelStateInfo(InstanceState.HARDWARE_ERROR.getCode(), "hardware error", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.NO_IMAGE.getCode(), newBizModelStateInfo(InstanceState.NO_IMAGE.getCode(), "no image", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.IMAGE_DOWNLOAD_FAILED.getCode(), newBizModelStateInfo(InstanceState.IMAGE_DOWNLOAD_FAILED.getCode(), "image download failed", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.CREATE_FAILED.getCode(), newBizModelStateInfo(InstanceState.CREATE_FAILED.getCode(), "create failed", "创建失败"));
        statusValueDescriptionMap.put(InstanceState.REMOVED.getCode(), newBizModelStateInfo(InstanceState.REMOVED.getCode(), "removed", "容器不存在"));
        statusValueDescriptionMap.put(InstanceState.OBJECT_NOT_EXIST.getCode(), newBizModelStateInfo(InstanceState.OBJECT_NOT_EXIST.getCode(), "object not exist", "容器不存在"));
        statusValueDescriptionMap.put(InstanceState.OBJECT_AUTO_REMOVE.getCode(), newBizModelStateInfo(InstanceState.OBJECT_AUTO_REMOVE.getCode(), "object auto remove", "容器不存在"));
        statusValueDescriptionMap.put(InstanceState.EDGE_SYSTEM_ABNORMAL.getCode(), newBizModelStateInfo(InstanceState.EDGE_SYSTEM_ABNORMAL.getCode(), "edge system abnormal", "系统异常"));
        statusValueDescriptionMap.put(InstanceState.KEEP_ON.getCode(), newBizModelStateInfo(InstanceState.KEEP_ON.getCode(), "keep on", "未知"));
        return statusValueDescriptionMap;
    }


    @Override
    public Map<String, Map<Integer, BizModelStateInfo>> getStateDesDict()
    {
        return unmodifiableStateDesDict;
    }
}
