package com.lnjoying.justice.cmp.common.os;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum OSInstanceStatus
{
    ACTIVE(0, "运行中", "active"),
    BUILD(1, "创建中", "build"),
    REBUILD(3, "重建", "rebuild"),
    SUSPENDED(4, "挂起", "suspended"),
    PAUSED(5, "已暂停", "paused"),
    RESIZE(6, "修改配置", "resize"),
    VERIFY_RESIZE(7, "验证配置", "verify_resize"),
    REVERT_RESIZE(8, "恢复配置", "revert_resize"),
    PASSWORD(9, "密码", "password"),
    REBOOT(10, "重启", "reboot"),
    HARD_REBOOT(11, "硬重启", "hard_reboot"),
    DELETED(12, "已删除", "deleted"),
    UNKNOWN(13, "未知", "unknown"),
    ERROR(14, "错误", "error"),
    STOPPED(15, "已关闭", "stopped"),
    SHUTOFF(16, "关闭", "shutoff"),
    MIGRATING(17, "迁移", "migrating"),
    SHELVED(18, "已归档", "shelved"),
    SHELVED_OFFLOADED(19, "已归档", "shelved_offloaded"),
    UNRECOGNIZED(20, "未识别", "unrecognized"),
    RESIZED(21, "修改配置", "resized"),
    SOFT_DELETE(22, "待删除", "soft-delete");

    OSInstanceStatus(int code, String cnDesc, String enDesc)
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

    public static OSInstanceStatus fromCode(int code)
    {
        for (OSInstanceStatus current : OSInstanceStatus.values())
        {
            if (current.getCode() == code)
            {
                return current;
            }
        }
        return UNRECOGNIZED;
    }

    public static OSInstanceStatus fromEn(String enDesc)
    {
        for (OSInstanceStatus current : OSInstanceStatus.values())
        {
            if (current.getDesc().get("en").equalsIgnoreCase(enDesc))
            {
                return current;
            }
        }
        return UNRECOGNIZED;
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
}
