package com.lnjoying.justice.cmp.common;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum InstanceState
{
    UNKNOWN                      ("UNKNOWN",                     -100, ""         ,""),
    MAKED                        ("MAKED",                       0,    "创建中"    ,"creating"),
    SPAWNED                      ("SPAWNED",                     1,    "创建中"    ,"creating"),
    WAIT_ASSIGN                  ("WAIT_ASSIGN",                 2,    "创建中"    ,"creating"),
    ASSIGNED                     ("ASSIGNED",                    3,    "创建中"    ,"creating"),
    FWD                          ("FWD",                         4,    "创建中"    ,"creating"),

    EDGE_UNREACHABLE             ("EDGE_UNREACHABLE",            50,   "创建失败"   ,"create failed"),
    NO_MATCH_NODE                ("NO_MATCH_NODE",               51,   "创建失败"   ,"create failed"),

    SPAWN_SYSTEM_STOP            ("SPAWN_SYSTEM_STOP",           60,   "容器退出"   ,"inst quit"),
    SPAWN_USER_STOP_QUIT         ("SPAWN_USER_STOP_QUIT",        61,   "容器退出"   ,"inst quit"),
    SPAWN_OVERDUE_QUIT           ("SPAWN_OVERDUE_QUIT",          62,   "容器退出"   ,"inst quit"),
    FIN_SYSTEM_STOP              ("FIN_SYSTEM_STOP",             63,   "容器退出"   ,"inst quit"),
    FIN_USER_STOP_QUIT           ("FIN_USER_STOP_QUIT",          64,   "容器退出"   ,"inst quit"),
    FIN_OVERDUE_QUIT             ("FIN_OVERDUE_QUIT",            65,   "容器退出"   ,"inst quit"),

    CLOUD_CRETAE_FAILED_PARAMS   ("CLOUD_CRETAE_FAILED_PARAMS",  81,   "创建失败"   ,"create failed"),
    CLOUD_CRETAE_FAILED_OVERDUE  ("CLOUD_CRETAE_FAILED_OVERDUE", 82,   "创建失败"   ,"create failed"),
    SPAWNED_CLOUD_REMOVE         ("SPAWNED_CLOUD_REMOVE",        83,   ""          ,""),
    FIN_CLOUD_REMOVE             ("FIN_CLOUD_REMOVE",            84,   ""          ,""),

    CLOUD_SYSTEM_ABNORMAL        ("CLOUD_SYSTEM_ABNORMAL",       99,   "系统异常"   ,"system abnormal"),

    QUEUEING                     ("QUEUEING",                    100,  "创建中"    ,"creating"),
    IMAGE_DOWNLOADING            ("IMAGE_DOWNLOADING",           101,  "创建中"    ,"creating"),
    IMAGE_DOWNLOADED             ("IMAGE_DOWNLOADED",            102,  "创建中"    ,"creating"),
    CREATED                      ("CREATED",                     103,  "创建成功"  ,"created succeed"),
    STARTING                     ("STARTING",                    104,  "运行中"    ,"running"),
    RUNNING                      ("RUNNING",                     105,  "运行中"    ,"running"),
    RESTARTING                   ("RESTARTING",                  108,  "运行中"    ,"running"),

    SUCCESS_QUIT                 ("SUCCESS_QUIT",                110,  "容器退出"   ,"inst quit"),
    USER_STOP_QUIT               ("USER_STOP_QUIT",              111,  "容器退出"   ,"inst quit"),
    ABNORMAL_QUIT                ("ABNORMAL_QUIT",               113,  "容器退出"   ,"inst quit"),
    SYSTEM_STOP                  ("SYSTEM_STOP",                 120,  "系统异常"   ,"system abnormal"),

    HARDWARE_ERROR               ("HARDWARE_ERROR",              122,  "创建失败"   ,"create failed"),
    NO_IMAGE                     ("NO_IMAGE",                    123,  "创建失败"   ,"create failed"),
    IMAGE_DOWNLOAD_FAILED        ("IMAGE_DOWNLOAD_FAILED",       124,  "创建失败"   ,"create failed"),
    CREATE_FAILED                ("CREATE_FAILED",               125,  "创建失败"   ,"create failed"),

    REMOVED                      ("REMOVED",                     130,  "容器不存在"  ,"inst not exist"),
    OBJECT_NOT_EXIST             ("OBJECT_NOT_EXIST",            131,  "容器不存在"  ,"inst not exist"),
    OBJECT_AUTO_REMOVE           ("OBJECT_AUTO_REMOVE",          132,  "容器不存在"  ,"inst not exist"),

    EDGE_SYSTEM_ABNORMAL         ("EDGE_SYSTEM_ABNORMAL",        190,  "系统异常"    ,"system abnormal"),

    KEEP_ON                      ("KEEP_ON",                     200,  ""           ,"");

    private String name;
    private int code;
    private Map<String, String> desc;

    public static List<Integer> removeStatusList = Lists.newArrayList(REMOVED.code, OBJECT_NOT_EXIST.code, OBJECT_AUTO_REMOVE.code, SPAWNED_CLOUD_REMOVE.code, FIN_CLOUD_REMOVE.code);

    public static Map<Integer, List<Integer>> simpleMap = new HashMap<Integer, List<Integer>>(){
        {
            put(1100, Lists.newArrayList(MAKED.code, SPAWNED.code, WAIT_ASSIGN.code, ASSIGNED.code, FWD.code, QUEUEING.code,
                    IMAGE_DOWNLOADING.code, IMAGE_DOWNLOADED.code));
            put(1101, Lists.newArrayList(CREATED.code));
            put(1102, Lists.newArrayList(EDGE_UNREACHABLE.code, NO_MATCH_NODE.code, HARDWARE_ERROR.code, NO_IMAGE.code,
                    IMAGE_DOWNLOAD_FAILED.code, CREATE_FAILED.code));
            put(1103, Lists.newArrayList(STARTING.code, RUNNING.code, RESTARTING.code));
            put(1104, Lists.newArrayList(SPAWN_SYSTEM_STOP.code, SPAWN_USER_STOP_QUIT.code, SPAWN_OVERDUE_QUIT.code,
                    FIN_SYSTEM_STOP.code, FIN_USER_STOP_QUIT.code, FIN_OVERDUE_QUIT.code, SUCCESS_QUIT.code,
                    USER_STOP_QUIT.code, ABNORMAL_QUIT.code));
            put(1105, Lists.newArrayList(REMOVED.code, OBJECT_NOT_EXIST.code, OBJECT_AUTO_REMOVE.code));
            put(1110, Lists.newArrayList(CLOUD_SYSTEM_ABNORMAL.code, SYSTEM_STOP.code, EDGE_SYSTEM_ABNORMAL.code));
        }
    };

    InstanceState(String name, int code, String cnDesc, String enDesc)
    {
        this.name = name;
        this.code = code;
        this.desc = new HashMap<String, String>()
        {
            {
                put("cn", cnDesc);
                put("en", enDesc);
            }
        };
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public Map<String, String> getDesc() {
        return desc;
    }

    public static InstanceState fromName(String name)
    {
        for (InstanceState s : InstanceState.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public static InstanceState fromCode(int code)
    {
        for (InstanceState value : InstanceState.values())
        {
            if(value.getCode() == code)
            {
                return value;
            }
        }
        return null;
    }

    public StatusCode toStatusCode()
    {
        return new StatusCode(this.code, this.desc);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class StatusCode{
        private int code;
        private Map<String, String> desc;
    }
}
