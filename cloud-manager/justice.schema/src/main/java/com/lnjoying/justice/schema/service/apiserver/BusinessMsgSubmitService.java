package com.lnjoying.justice.schema.service.apiserver;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.lnjoying.justice.schema.msg.WorkerMessage;
import io.swagger.annotations.ApiParam;

public interface  BusinessMsgSubmitService
{
    int submitMessage(@ApiParam(name = "edgeMessage")EdgeMessage edgeMessage);
    int submitWorkerMessage(@ApiParam(name = "workerMessage") WorkerMessage workerMessage);
}
