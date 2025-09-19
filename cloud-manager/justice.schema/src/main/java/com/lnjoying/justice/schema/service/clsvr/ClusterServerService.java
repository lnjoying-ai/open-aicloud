package com.lnjoying.justice.schema.service.clsvr;

import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.lnjoying.justice.schema.msg.WorkerMessage;
import io.swagger.annotations.ApiParam;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/1/22 3:05 PM
 */
public interface ClusterServerService
{
    int deliver(@ApiParam(name = "message") WorkerMessage workerMessage);
}
