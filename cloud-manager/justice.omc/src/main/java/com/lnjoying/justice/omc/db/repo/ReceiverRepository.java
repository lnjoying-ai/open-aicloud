package com.lnjoying.justice.omc.db.repo;

import com.lnjoying.justice.omc.db.mapper.TblOmcReceiverMapper;
import com.lnjoying.justice.omc.db.model.TblOmcReceiver;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 10:48
 */

@Repository
public class ReceiverRepository
{

    @Autowired
    private TblOmcReceiverMapper tblOmcReceiverMapper;


    public int insertReceiverSelective(TblOmcReceiver receiver)
    {
        return tblOmcReceiverMapper.insertSelective(receiver);
    }

    public int updateReceiverByPrimaryKeySelective(TblOmcReceiver receiver)
    {
        return tblOmcReceiverMapper.updateByPrimaryKeySelective(receiver);
    }

    public List<TblOmcReceiver> selectAllReceivers(String queryBpId, String queryUserId, String name)
    {
        return tblOmcReceiverMapper.selectAll(queryBpId, queryUserId, name);
    }

    public int deleteReceiverByPrimaryKey(String id)
    {
        return tblOmcReceiverMapper.deleteByPrimaryKey(id);
    }

    public TblOmcReceiver selectReceiverByPrimaryKey(String receiverId)
    {
        return tblOmcReceiverMapper.selectByPrimaryKey(receiverId);
    }
}
