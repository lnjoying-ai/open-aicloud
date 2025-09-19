package com.lnjoying.justice.aos.common;


import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;

import java.util.*;

import static com.lnjoying.justice.aos.common.StackState.*;
import static com.lnjoying.justice.schema.common.ErrorCode.STACK_STATUS_PARAM_ERROR;

/**
 *
 * simple stack status enum Used to facilitate front-end display
 * simple status refers to the code in SimpleStackStatusEnum
 * full status refers to the const in StackState
 * @author merak
 **/

public enum SimpleStackStatusEnum
{
    /**
     * 创建中，在容器未运行之前的状态都为创建中
     */
    CREATING(1100, "creating", "创建中" ),

    /**
     * 创建成功
     */
    CREATED_SUCCEED(1101, "created succeed", "创建成功"),

    /**
     * 创建失败。过滤应用创建失败相关的状态
     */
    CREATED_FAILED(1102, "create failed", "创建失败"),

    /**
     * 运行中。正在运行的应用
     */
    RUNNING(1103, "running", "运行中"),

    /**
     * 应用退出。已经处于退出状态的应用和正在要停止的应用，用户停止的应用
     */
    STACK_QUIT(1104, "stack quit", "应用退出"),

    /**
     * 应用不存在。在边缘侧不存在的应用
     */
    STACK_NOT_EXIST(1105, "stack not exist", "应用不存在"),

    /**
     * 系统异常
     */
    SYSTEM_ABNORMAL(1110, "system abnormal", "系统异常");


    private final int code;

    private final String enName;

    private final String cnName;

    SimpleStackStatusEnum(int code, String enName, String cnName)
    {
        this.code = code;
        this.enName = enName;
        this.cnName = cnName;
    }

    public static Map<SimpleStackStatusEnum, List<Integer>> simpleMap = new HashMap<>();

    /**
     * key = full status --> value == simple status
     * full status in StackState
     * simple status in SimpleStackStatusEnum
     */
    public static Map<Integer, SimpleStackStatusEnum> fullMap = new HashMap<>();

    static{
        List<Integer> creatingList = Lists.newArrayList(MAKED, SPAWNED, WAIT_ASSIGN, ASSIGNED, FWD, QUEUEING, IMAGE_DOWNLOADED, IMAGE_DOWNLOADING, SYNC_CFG, CFG_SYNCED);
        simpleMap.put(CREATING, creatingList);

        List<Integer> createdSucceedList = Lists.newArrayList(CREATED);
        simpleMap.put(CREATED_SUCCEED, createdSucceedList);

        List<Integer> createdFailedList = Lists.newArrayList(EDGE_UNREACHABLE, NO_MATCH_NODE, CLOUD_CRETAE_FAILED_PARAMS, CLOUD_CRETAE_FAILED_OVERDUE,
                HARDWARE_ERROR, NO_IMAGE, IMAGE_DOWNLOAD_FAILED, CREATE_FAILED);
        simpleMap.put(CREATED_FAILED, createdFailedList);

        List<Integer> runningList = Lists.newArrayList(STARTING, StackState.RUNNING, RESTARTING);
        simpleMap.put(RUNNING, runningList);

        List<Integer> stackQuitList = Lists.newArrayList(SPAWN_SYSTEM_STOP, SPAWN_USER_STOP_QUIT, SPAWN_OVERDUE_QUIT, FIN_SYSTEM_STOP,
                FIN_USER_STOP_QUIT, FIN_OVERDUE_QUIT, SUCCESS_QUIT, USER_STOP_QUIT, OVERDUE_QUIT, ABNORMAL_QUIT);
        simpleMap.put(STACK_QUIT, stackQuitList);

        List<Integer> stackNotExist = Lists.newArrayList(REMOVED, OBJECT_NOT_EXIST, OBJECT_AUTO_REMOVE);
        simpleMap.put(STACK_NOT_EXIST, stackNotExist);

        List<Integer> systemAbnormalList = Lists.newArrayList(UNKNOWN, CLOUD_SYSTEM_ABNORMAL, SYSTEM_STOP, StackState.SYSTEM_ABNORMAL);
        simpleMap.put(SYSTEM_ABNORMAL, systemAbnormalList);

        simpleMap.entrySet().stream().forEach(entry -> {
            List<Integer> values = entry.getValue();
            values.stream().forEach(value -> {
                int key = entry.getKey().code;
                fullMap.put(value, fromValue(key));
            });
        });
    }

    /**
     * input simple status , output full status list
     * @param status
     * @return
     */
    public static List<Integer> getFullStatus(int status) throws WebSystemException
    {
        Optional<SimpleStackStatusEnum> stackStatus = Arrays.stream(SimpleStackStatusEnum.values()).filter(x -> x.getCode() == status)
                .findFirst();
        // orElseThrow(() -> new WebSystemException(STACK_STATUS_PARAM_ERROR, ErrorLevel.INFO));

        if (Objects.nonNull(stackStatus.get()))
        {
            return new ArrayList<>(simpleMap.get(stackStatus.get()));
        }

        throw  new WebSystemException(STACK_STATUS_PARAM_ERROR, ErrorLevel.INFO);
    }

    /**
     * input full status , output simple status
     * @param status
     * @return
     */
    public static SimpleStackStatusEnum getSimpleStatus(int status)
    {
        return fullMap.get(status);
    }

    public static SimpleStackStatusEnum fromValue(int code)
    {
        return Arrays.stream(SimpleStackStatusEnum.values()).filter(x -> x.getCode() == code).findFirst().get();
    }
    public int getCode()
    {
        return code;
    }

    public String getEnName()
    {
        return enName;
    }

    public String getCnName()
    {
        return cnName;
    }

}
