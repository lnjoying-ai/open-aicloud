package com.lnjoying.justice.cis.common.constant;

import com.lnjoying.justice.schema.entity.OperType;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: define action for inst life mng
 * @Author: Regulus
 * @Date: 1/7/22 11:43 AM
 */
public enum InstAction
{
    CREATE("create", "创建容器", "create instance"),
    REMOVE("remove", "删除容器", "remove instance"),
    START("start", "启动容器", "start instance"),
    STOP("stop", "停止容器", "stop instance"),
    EXECUTE("execute", "远程命令行执行", "remote execute command"),
    LOGS("logs", "查看容器日志", "log inst"),
    RESTART("restart", "重启容器", "restart instance");

    private String name;
    private Map<String, String> desc;

    InstAction(String name, String cnDesc, String enDesc)
    {
        this.name = name;
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

    public Map<String, String> getDesc() {
        return desc;
    }

    public static InstAction fromName(String name)
    {
        for (InstAction s : InstAction.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }

    public OperType toOperType()
    {
        return new OperType(this.name, this.desc);
    }
}
