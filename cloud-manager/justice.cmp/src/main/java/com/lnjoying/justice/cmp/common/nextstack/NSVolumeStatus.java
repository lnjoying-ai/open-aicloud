package com.lnjoying.justice.cmp.common.nextstack;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum NSVolumeStatus
{
    ADDING(0, "创建中", "ADDING"),
    ADDED(1, "已创建", "ADDED"),
    ADD_FAILED(2, "创建失败", "ADD_FAILED"),
    DELETING(3, "删除中", "DELETING"),
    DELETED(4, "已删除", "DELETED"),
    DELETE_FAILED(5, "删除失败", "DELETE_FAILED"),
    AGENT_ADDING(6, "创建中", "AGENT_ADDING"),
    AGENT_ADDING_ERR(7, "创建失败", "AGENT_ADDING_ERR"),
    AGENT_DELETING(8, "删除中", "AGENT_DELETING"),
    AGENT_DELETING_ERR(9, "删除失败", "AGENT_DELETING_ERR"),
    UPDATING(41, "更新中", "UPDATING"),
    UPDATED(42, "已更新", "UPDATED"),
    UPDATE_FAILED(45, "更新失败", "UPDATE_FAILED"),
    ATTACHING(20, "挂载中", "ATTACHING"),
    AGENT_ATTACHING(21, "挂载中", "AGENT_ATTACHING"),
    DETACHING(22, "卸载中", "DETACHING"),
    AGENT_DETACHING(23, "卸载中", "AGENT_DETACHING"),
    ATTACH_FAILED(24, "挂载失败", "ATTACH_FAILED"),
    DETACH_FAILED(25, "卸载失败", "DETACH_FAILED"),
    ATTACHED(26, "已挂载", "ATTACHED"),
    DETACHED(27, "未挂载", "DETACHED"),
    PRE_DEST_RESUMING(28, "恢复中", "PRE_DEST_RESUMING"),
    RESUMING(29, "恢复中", "RESUMING"),
    AGENT_RESUMING(30, "恢复中", "AGENT_RESUMING"),
    RESUMED(31, "运行中", "RESUMED"),
    SUSPEND(32, "迁移中", "SUSPEND"),
    EXPORTING(33, "导出中", "EXPORTING"),
    AGENT_EXPORTING(34, "导出中", "AGENT_EXPORTING"),
    SUSPENDING(35, "迁移中", "SUSPENDING"),
    AGENT_SUSPENDING(36, "迁移中", "AGENT_SUSPENDING"),
    SUSPEND_FAILED(37, "迁移失败", "SUSPEND_FAILED"),
    RESUME_FAILED(38, "恢复失败", "RESUME_FAILED"),
    IMPORTING(39, "导入中", "IMPORTING"),
    SNAP_SWITCHING(50, "切换中", "SNAP_SWITCHING"),
    AGENT_SWITCHING(51, "切换中", "AGENT_SWITCHING"),
    SNAP_SWITCH_FAILED(52, "切换失败", "SNAP_SWITCH_FAILED"),
    EXPORT_FAILED(53, "导出失败", "EXPORT_FAILED");

    NSVolumeStatus(int code, String cnDesc, String enDesc)
    {
        this.code = code;
        this.desc = new HashMap<String, String>()
        {
            {
                put("cn", cnDesc);
                put("en", enDesc);
            }
        };
    }

    public static NSVolumeStatus fromCode(int code)
    {
        for (NSVolumeStatus current : NSVolumeStatus.values())
        {
            if (current.getCode() == code)
            {
                return current;
            }
        }
        throw new RuntimeException("Invalid error code: " + code);
    }

    private int code;

    private Map<String, String> desc;

    public int getCode() {
        return code;
    }

    public Map<String, String> getDesc()
    {
        return desc;
    }

    public StatusCode toStatusCode()
    {
        return new StatusCode(this.code, this.desc);
    }

    public static Set<Integer> CREATING_STATUS = new HashSet<Integer>(){
        {
            add(ADDING.getCode());
            add(AGENT_ADDING.getCode());
        }
    };
}
