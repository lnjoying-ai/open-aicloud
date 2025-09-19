package com.lnjoying.justice.cis.db.repo;

import com.lnjoying.justice.cis.db.mapper.*;
import com.lnjoying.justice.cis.db.model.*;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @Description: operator for k8s inst
 * @Author: Regulus
 * @Date: 1/7/22 10:46 AM
 */
@Repository("k8sInstRepository")
@Transactional(rollbackFor = {Exception.class})
public class ClsInstRepository
{
    @Autowired
    TblContainerClsinstInfoMapper tblClsInstInfoMapper;

    public int insertInst(TblContainerClsinstInfo instInfo)
    {
        return tblClsInstInfoMapper.insert(instInfo);
    }
    
    TblContainerClsinstInfoExample buildExample(String name, String nodeId)
    {
        TblContainerClsinstInfoExample example = new TblContainerClsinstInfoExample();
        TblContainerClsinstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andContainerNameEqualTo(name);
        criteria.andNodeIdEqualTo(nodeId);
        return example;
    }
    
    public TblContainerClsinstInfo getInst(String name, String nodeId)
    {
        TblContainerClsinstInfoExample example = buildExample(name, nodeId);
        
        List<TblContainerClsinstInfo> tblContainerClsinstInfoList = tblClsInstInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tblContainerClsinstInfoList))
        {
            return null;
        }
        
        return tblContainerClsinstInfoList.get(0);
    }
    
    public List<TblContainerClsinstInfo> getInstByNodeIdAndType(String nodeId, String type)
    {
        TblContainerClsinstInfoExample example = new TblContainerClsinstInfoExample();
        TblContainerClsinstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        criteria.andOrchTypeEqualTo(type);
        return tblClsInstInfoMapper.selectByExample(example);
    }
    public TblContainerClsinstInfo getInst(String instId)
    {
        return tblClsInstInfoMapper.selectByPrimaryKey(instId);
    }
    
    public int removeInst(String name, String nodeId)
    {
        TblContainerClsinstInfoExample example = buildExample(name, nodeId);
        return tblClsInstInfoMapper.deleteByExample(example);
    }
    
    public int removeInst(String instId)
    {
        return tblClsInstInfoMapper.deleteByPrimaryKey(instId);
    }
    
    public int updateInst(TblContainerClsinstInfo tblContainerClsinstInfo)
    {
        return tblClsInstInfoMapper.updateByPrimaryKeySelective(tblContainerClsinstInfo);
    }
    
    public int removeInstOnNode(String nodeId)
    {
        TblContainerClsinstInfoExample example = new TblContainerClsinstInfoExample();
        TblContainerClsinstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        return tblClsInstInfoMapper.deleteByExample(example);
    }
}
