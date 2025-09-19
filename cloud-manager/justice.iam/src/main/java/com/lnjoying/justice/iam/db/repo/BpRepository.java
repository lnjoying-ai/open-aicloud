package com.lnjoying.justice.iam.db.repo;

import com.lnjoying.justice.iam.db.mapper.TblBpInfoMapper;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblBpInfoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class BpRepository
{
    @Autowired
    TblBpInfoMapper tblBpInfoMapper;

    public int insertBp(TblBpInfo tblBpInfo)
    {
        return tblBpInfoMapper.insert(tblBpInfo);
    }

    public int deleteBp(String bpId)
    {
        return tblBpInfoMapper.deleteByPrimaryKey(bpId);
    }

    public int updateBp(TblBpInfo tblBpInfo)
    {
        return tblBpInfoMapper.updateByPrimaryKeySelective(tblBpInfo);
    }

    public TblBpInfo getBpInfo(String bpId)
    {
        return tblBpInfoMapper.selectByPrimaryKey(bpId);
    }


    public List<TblBpInfo> getBpsByExample(TblBpInfoExample example)
    {
        return tblBpInfoMapper.selectByExample(example);
    }

    public long countBpsByExample(TblBpInfoExample example)
    {
        return tblBpInfoMapper.countByExample(example);
    }

    public TblBpInfo selectByBpName(String bpName)
    {
        return tblBpInfoMapper.selectByBpName(bpName);
    }

    public List<TblBpInfo> selectAll()
    {
        return tblBpInfoMapper.selectAllByStatus(0);
    }

    public List<TblBpInfo> selectAllByCreateTime(Date createTime)
    {
        return tblBpInfoMapper.selectAllByCreateTime(createTime);
    }





}
