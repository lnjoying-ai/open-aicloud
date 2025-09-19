package com.lnjoying.justice.omc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.omc.db.model.TblOmcReceiver;
import com.lnjoying.justice.omc.db.repo.ReceiverRepository;
import com.lnjoying.justice.omc.domain.dto.req.AddReceiverReq;
import com.lnjoying.justice.omc.domain.dto.req.UpdateReceiverReq;
import com.lnjoying.justice.omc.domain.dto.rsp.ReceiversRsp;
import com.lnjoying.justice.omc.domain.model.Receiver;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.omc.service.ReceiverService;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.service.ums.UmsService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserAttributes;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.OMC_RECEIVER_NOT_EXIST;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 10:22
 */

@Service
@Slf4j
public class ReceiverServiceImpl implements ReceiverService
{

    @Autowired
    private ReceiverRepository receiverRepository;

    @Autowired
    private CombRpcService combRpcService;

    @Override
    public Object addReceiver(AddReceiverReq addReceiverReq)
    {
        checkParams(addReceiverReq);

        TblOmcReceiver tblOmcReceiver = new TblOmcReceiver();

        tblOmcReceiver.setId(Utils.assignUUId());
        BeanUtils.copyProperties(addReceiverReq, tblOmcReceiver);
        receiverRepository.insertReceiverSelective(tblOmcReceiver);
        return tblOmcReceiver.getId();
    }

    @Override
    public void updateReceiver(String receiverId, UpdateReceiverReq req)
    {
        if (!checkReceiverExists(receiverId))
        {
            throw new WebSystemException(OMC_RECEIVER_NOT_EXIST, ErrorLevel.ERROR);
        }

        TblOmcReceiver tblOmcReceiver = new TblOmcReceiver();
        tblOmcReceiver.setId(receiverId);
        BeanUtils.copyProperties(req, tblOmcReceiver);
        tblOmcReceiver.setUpdateTime(new Date());
        receiverRepository.updateReceiverByPrimaryKeySelective(tblOmcReceiver);
    }

    @Override
    public ReceiversRsp getReceiverList(String queryBpId, String queryUserId, String name, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblOmcReceiver> tblOmcReceivers = receiverRepository.selectAllReceivers(queryBpId, queryUserId, name);
        PageInfo<TblOmcReceiver> pageInfo = new PageInfo<>(tblOmcReceivers);
        return ReceiversRsp.builder()
                .totalNum(pageInfo.getTotal())
                .receivers(tblOmcReceivers.stream().map(Receiver::of)
                        .map(receiver -> assembleUserInfo(receiver, combRpcService.getUmsService() ))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void deleteReceiver(String receiverId)
    {
        if (!checkReceiverExists(receiverId))
        {
           log.error("receiver:{} not exist when delete", receiverId);
        }
        else
        {
            receiverRepository.deleteReceiverByPrimaryKey(receiverId);
        }

    }

    private boolean checkReceiverExists(String receiverId)
    {
        String queryBpId = "";
        String queryUserId = "";
        if (!isAdmin())
        {
            queryBpId = getUserAttributes().getLeft();
            queryUserId = getUserAttributes().getRight();
        }
        // todo
        return true;
    }

    private void checkParams(AddReceiverReq addReceiverReq)
    {
        // todo
    }


    public static Receiver assembleUserInfo(Receiver receiver , UmsService umsService)
    {

        String bpName = "";
        if (StringUtils.isNotBlank(receiver.getBpId()))
        {

            {
                try
                {
                    bpName = umsService.getBpNameByBpId(receiver.getBpId());
                }
                catch (Exception e)
                {
                    log.error("get bp name failed");
                }
            }
        }
        receiver.setBpName(bpName);

        String userName = "";
        if (StringUtils.isNotBlank(receiver.getUserId()))
        {
            try
            {
                userName = umsService.getUserNameByUserId(receiver.getUserId());
            }
            catch (Exception e)
            {
                log.error("get user name failed");
            }
        }
        receiver.setUserName(userName);

        String iamUserName = "";
        if (StringUtils.isNotBlank(receiver.getIamUserId()))
        {
            try
            {
                iamUserName = umsService.getUserNameByUserId(receiver.getIamUserId());
            }
            catch (Exception e)
            {
                log.error("get iam user name failed");
            }
        }
        receiver.setIamUserName(iamUserName);

        return receiver;
    }
}
