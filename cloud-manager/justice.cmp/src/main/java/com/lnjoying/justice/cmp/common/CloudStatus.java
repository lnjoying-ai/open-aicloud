package com.lnjoying.justice.cmp.common;

import com.lnjoying.justice.schema.entity.StatusCode;

import java.util.HashMap;
import java.util.Map;

public enum CloudStatus
{
    CREATED(0, "创建", "created"),
    IMPORTING(1, "导入中", "importing"),
    UPDATING(2, "升级中", "updating"),
    INTERNAL_TEST(3, "内测", "internal test"),

    PUBLIC_TEST(11, "公测", "public test"),
    ON_SHELF(12, "已上架", "on-shelf"),
    PRE_OFF_SHELF(13, "预下架", "pre off-shelf"),

    MAINTAINING(21, "维护中", "maintaining"),

    OFF_SHELF(31, "已下架", "off-shelf"),

    IMPORT_FILED(50, "导入失败", "import failed"),
    MAINTAIN_FILED(51, "维护失败", "maintain failed"),

    WAIT_REMOVE(81, "待删除", "wait remove"),

    REMOVING(90, "删除中", "removing"),
    REMOVE_FAILED(91, "删除失败", "remove failed"),
    REMOVED(99, "已删除", "removed");

    CloudStatus(int code, String cnDesc, String enDesc)
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
    
    public static CloudStatus fromCode(int code)
    {
        for (CloudStatus current : CloudStatus.values())
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
}
