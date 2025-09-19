package com.lnjoying.justice.ecrm.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.mapper.EcrmOperator;
import com.lnjoying.justice.ecrm.db.mapper.TblRegionInfoMapper;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfoExample;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionNode;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class RegionRepository
{
	@Autowired
	TblRegionInfoMapper tblRegionInfoMapper;

	@Autowired
	EcrmOperator ecrmOperator;

	@Autowired
	LabelProperty labelProperty;

	public int insertRegion(TblRegionInfo tblRegionInfo)
	{
		return tblRegionInfoMapper.insert(tblRegionInfo);
	}

	public int updateRegion(TblRegionInfo tblRegionInfo)
	{
		tblRegionInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		return tblRegionInfoMapper.updateByPrimaryKeySelective(tblRegionInfo);
	}

	/**
	 * @Description:  Physically delete the region from the database, and logically delete it if it fails
	 * @Author: Perry
	 * @Date: 2022/1/12 8:32 下午
	 * @Param: tblRegionInfo
	 */
	public int deleteRegion(TblRegionInfo tblRegionInfo)
	{
		return tblRegionInfoMapper.deleteByPrimaryKey(tblRegionInfo.getRegionId());
	}

	public PageInfo<TblRegionInfo> getPageableRegions(String orchestration, String name, Integer pageNum, Integer pageSize)
	{
		PageHelper.startPage(pageNum, pageSize);
		List<TblRegionInfo> regions = getRegions(orchestration, name);
		PageInfo<TblRegionInfo> pageInfo = new PageInfo<>(regions);
		return pageInfo;
	}

	public List<TblRegionInfo> getRegions(String orchestration, String name)
	{
		TblRegionInfoExample example = new TblRegionInfoExample();
		TblRegionInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
		if (StringUtils.isNotEmpty(orchestration))
		{
			criteria.andLabelIsEmptyOrEqualTo(labelProperty.getRegionOrchestration(), orchestration);
		}
		if (StringUtils.isNotEmpty(name))
		{
			criteria.andRegionNameLike("%" + name + "%");
		}
		return tblRegionInfoMapper.selectByExample(example);
	}

	public TblRegionInfo getRegion(String regionId)
	{
		return tblRegionInfoMapper.selectByPrimaryKey(regionId);
	}

	public List<TblRegionInfo> getRegionsByName(String regionName) {
		TblRegionInfoExample example = new TblRegionInfoExample();
		TblRegionInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
		criteria.andRegionNameLike("%" + regionName + "%");
		return tblRegionInfoMapper.selectByExample(example);
	}

	public List<RegionNode>getAllNodes(String regionId) {
		return tblRegionInfoMapper.selectAllNodesByRegionId(regionId);
	}

	public Set<String> getAllRegionIds()
	{
		return tblRegionInfoMapper.selectDistinctRegionId();
	}

	public boolean checkUnique(String regionId)
	{
		TblRegionInfoExample example = new TblRegionInfoExample();
		TblRegionInfoExample.Criteria criteria = example.createCriteria();
		criteria.andRegionIdEqualTo(regionId);
		return tblRegionInfoMapper.countByExample(example) == 0;
	}

	public long countByExample(TblRegionInfoExample example)
	{
		return tblRegionInfoMapper.countByExample(example);
	}
}
