package com.lnjoying.justice.schema.service.aos;

import com.lnjoying.justice.schema.entity.aos.AddStackReq;
import com.lnjoying.justice.schema.entity.aos.StackDeploySimpleInfo;
import com.lnjoying.justice.schema.entity.omc.MonitorEndpointInfo;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/4 16:31
 */

public interface AosStackService
{
    boolean isStackRunningStatus(@ApiParam(name = "stackId")String stackId);

    boolean deleteStack(@ApiParam(name = "stackId")String stackId);
    
    String createStackSpec(@ApiParam(name = "addStackReq")AddStackReq addStackReq);
    int   removeStackSpec(@ApiParam(name = "specId")String specId);

    StackDeploySimpleInfo getStackDeployInfo(@ApiParam(name = "specId") String specId);

    List<MonitorEndpointInfo> getMonitorEndpointList(@ApiParam(name = "specId") String specId);

}
