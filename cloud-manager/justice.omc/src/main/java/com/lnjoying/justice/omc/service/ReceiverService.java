package com.lnjoying.justice.omc.service;

import com.lnjoying.justice.omc.domain.dto.req.AddReceiverReq;
import com.lnjoying.justice.omc.domain.dto.req.UpdateReceiverReq;
import com.lnjoying.justice.omc.domain.dto.rsp.ReceiversRsp;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 9:58
 */

public interface ReceiverService
{
    Object addReceiver(AddReceiverReq addReceiverReq);

    void updateReceiver(String receiverId, UpdateReceiverReq req);

    ReceiversRsp getReceiverList(String queryBpId, String queryUserId, String name, Integer pageNum, Integer pageSize);

    void deleteReceiver(String receiverId);
}
