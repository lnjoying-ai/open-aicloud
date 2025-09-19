package com.lnjoying.justice.ecrm.db.repo;

import com.lnjoying.justice.ecrm.db.mapper.TblServiceAgentInfoMapper;
import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfo;
import com.lnjoying.justice.ecrm.db.model.TblServiceAgentInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ServiceAgentRepository
{
    @Autowired
    private TblServiceAgentInfoMapper tblServiceAgentInfoMapper;

    public int insertServiceAgent(TblServiceAgentInfo tblServiceAgentInfo)
    {
        return tblServiceAgentInfoMapper.insert(tblServiceAgentInfo);
    }

    public int updateServiceAgent(TblServiceAgentInfo tblServiceAgentInfo)
    {
        return tblServiceAgentInfoMapper.updateByPrimaryKey(tblServiceAgentInfo);
    }

    public int updateServiceAgentSelective(TblServiceAgentInfo tblServiceAgentInfo)
    {
        return tblServiceAgentInfoMapper.updateByPrimaryKeySelective(tblServiceAgentInfo);
    }

    public TblServiceAgentInfo getServiceAgent(String agentId)
    {
        return tblServiceAgentInfoMapper.selectByPrimaryKey(agentId);
    }

    public List<TblServiceAgentInfo> getServiceAgentsByExample(TblServiceAgentInfoExample example)
    {
        return tblServiceAgentInfoMapper.selectByExample(example);
    }

    public String selectAgentIdBySiteId(String siteId)
    {
        Assert.hasText(siteId, "The given site id must not be null!");
        return tblServiceAgentInfoMapper.selectAgentIdBySiteId(siteId);
    }
}
