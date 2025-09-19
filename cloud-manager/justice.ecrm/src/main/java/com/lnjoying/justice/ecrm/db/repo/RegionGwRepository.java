package com.lnjoying.justice.ecrm.db.repo;

import com.lnjoying.justice.ecrm.db.mapper.EcrmOperator;
import com.lnjoying.justice.ecrm.db.mapper.TblRegionBindInfoMapper;
import com.lnjoying.justice.ecrm.db.mapper.TblRegionInfoMapper;
import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfoExample;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfoExample;
import com.lnjoying.justice.ecrm.domain.dto.model.NameIDInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class RegionGwRepository
{
	@Autowired
	TblRegionBindInfoMapper tblRegionBindInfoMapper;

	@Autowired
	EcrmOperator ecrmOperator;

	public int insertRegionBind(TblRegionBindInfo tblRegionBindInfo)
	{
		return tblRegionBindInfoMapper.insert(tblRegionBindInfo);
	}


	public int deleteRegion(String regionId, String nodeId)
	{
		TblRegionBindInfoExample example =  new TblRegionBindInfoExample();
		TblRegionBindInfoExample.Criteria criteria = example.createCriteria();
		criteria.andRegionIdEqualTo(regionId);
		criteria.andNodeIdEqualTo(nodeId);
		return tblRegionBindInfoMapper.deleteByExample(example);
	}

	public int deleteRegion(String regionId)
	{
		TblRegionBindInfoExample example =  new TblRegionBindInfoExample();
		TblRegionBindInfoExample.Criteria criteria = example.createCriteria();
		criteria.andRegionIdEqualTo(regionId);
		return tblRegionBindInfoMapper.deleteByExample(example);
	}

	public List<NameIDInfo> getRegionBindGwInfo(String regionId)
	{
		return ecrmOperator.getRegionBinds(regionId);
	}

	public List<TblRegionBindInfo> getRegionBinds(String regionId)
	{
		TblRegionBindInfoExample example = new TblRegionBindInfoExample();
		TblRegionBindInfoExample.Criteria criteria = example.createCriteria();
		criteria.andRegionIdEqualTo(regionId);
		return tblRegionBindInfoMapper.selectByExample(example);
	}

	public List<String> getRegionIdsByGwNodeId(String nodeId)
	{
		return ecrmOperator.getRegionIdsByGwNodeId(nodeId);
	}
}
