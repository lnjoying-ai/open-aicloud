package com.lnjoying.justice.api.rpcserviceimpl;

import com.lnjoying.justice.api.constant.ApiMsgType;
import com.lnjoying.justice.api.process.service.TransferSubmitMessgeStrategy;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.msg.WorkerMessage;
import com.lnjoying.justice.schema.service.apiserver.BusinessMsgSubmitService;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

@RpcSchema(schemaId = "submit")
public class BusinessSubmitServiceImpl implements BusinessMsgSubmitService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private TransferSubmitMessgeStrategy transferSubmitMessgeStrategy;
    private BusinessSubmitServiceImpl()
    {
        LOGGER.info("init TaskSubmitServiceImpl");
    }

    @Override
    public int submitMessage(@ApiParam(name = "edgeMessage") EdgeMessage edgeMessage)
    {
        LOGGER.info("recv edge msg. send to worker {} ", edgeMessage.getNodeId());
        MessagePack pack = new MessagePack();
        pack.setMsgType(ApiMsgType.SUBMIT_MSG);
        pack.setMessageObj(edgeMessage);
        transferSubmitMessgeStrategy.sendMessage(pack);
        return 0;
    }
    
    @Override
    public int submitWorkerMessage(@ApiParam(name = "workerMessage")WorkerMessage workerMessage)
    {
        LOGGER.info("recv worker msg. send to worker {} ", workerMessage.getWorkerId());
        MessagePack pack = new MessagePack();
        pack.setMsgType(ApiMsgType.SUBMIT_WORKER_MSG);
        pack.setMessageObj(workerMessage);
        transferSubmitMessgeStrategy.sendMessage(pack);
        return 0;
    }
}
