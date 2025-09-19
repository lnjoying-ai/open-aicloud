package com.lnjoying.justice.cluster.manager.common;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;

import java.util.Arrays;

import static com.lnjoying.justice.schema.common.ErrorCode.CLUSTER_K8S_UNSUPPORTED_ACTION;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/30 16:30
 */

public enum CLusterAction
{
    EXPORT_TEMPLATE(0, "export_template");
    
    private int code;

    private String name;

    CLusterAction(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public int getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }
    
    public static CLusterAction fromAction(String name)
    {
        return Arrays.stream(CLusterAction.values()).filter(action -> action.getName().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new WebSystemException(CLUSTER_K8S_UNSUPPORTED_ACTION, ErrorLevel.ERROR));
    }
}
