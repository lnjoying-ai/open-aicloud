package com.lnjoying.justice.cluster.manager.service.agent;

import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.EeWorkerDef;
import com.micro.core.common.Pair;

public interface ClusterAgentService
{
    int addWorker(EeWorkerDef.msg_worker_reg_req_body workerRegReqBody);

    int workerLogin(EeWorkerDef.msg_worker_login_req_body workerLoginReqBody);

    void processWorkerIFState(Pair<String, EeNetMessageApi.ee_net_message> ifState);
}
