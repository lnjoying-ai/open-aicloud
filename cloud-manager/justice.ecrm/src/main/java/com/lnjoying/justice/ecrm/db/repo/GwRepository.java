package com.lnjoying.justice.ecrm.db.repo;

import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.db.mapper.*;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.domain.dto.model.*;
import com.lnjoying.justice.ecrm.domain.dto.response.QueryGwRsp;
import com.lnjoying.justice.ecrm.domain.model.search.GwSearchCritical;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class GwRepository
{
	@Autowired
	TblEdgeGwInfoMapper tblEdgeGwInfoMapper;

	@Autowired
	TblRegionInfoMapper tblRegionInfoMapper;

	@Autowired
	TblSiteInfoMapper tblSiteInfoMapper;

	@Autowired
	TblRegionBindInfoMapper tblRegionBindInfoMapper;

	@Autowired
	EcrmOperator ecrmOperator;

	public int insertGw(TblEdgeGwInfo tblEdgeGwInfo)
	{
		return tblEdgeGwInfoMapper.insert(tblEdgeGwInfo);
	}

	public int updateGw(TblEdgeGwInfo tblEdgeGwInfo)
	{
		return tblEdgeGwInfoMapper.updateByPrimaryKey(tblEdgeGwInfo);
	}

	public int deleteGw(String nodeId)
	{
		return tblEdgeGwInfoMapper.deleteByPrimaryKey(nodeId);
	}

	public TblEdgeGwInfo getGwByNodeId(String nodeId)
	{
		return tblEdgeGwInfoMapper.selectByPrimaryKey(nodeId);
	}

	public QueryGwRsp getGws(GwSearchCritical critical)
	{
		QueryGwRsp queryGwRsp = new QueryGwRsp();

		Long count = ecrmOperator.countEdgeGws(critical);

		if (count == null || count == 0)
		{
			return queryGwRsp;
		}

		queryGwRsp.setTotal_num(count);
        List<TblEdgeGwInfo> tblEdgeGwInfoList = ecrmOperator.getEdgeGws(critical);
        if (tblEdgeGwInfoList == null)
		{
			return queryGwRsp;
		}

        List<GwNodeInfo> gwNodeInfoList = new ArrayList<>();
        queryGwRsp.setNodes(gwNodeInfoList);
        for (TblEdgeGwInfo tblEdgeGwInfo : tblEdgeGwInfoList)
		{
			GwNodeInfo gwNodeInfo = new GwNodeInfo();
			gwNodeInfo.setInfo(tblEdgeGwInfo);

			DiskInfo diskInfo = new DiskInfo();
			if (tblEdgeGwInfo.getDiskTotal() != null) diskInfo.setDiskTotal(tblEdgeGwInfo.getDiskTotal());
			if (tblEdgeGwInfo.getDiskType() != null) diskInfo.setDiskTypes(tblEdgeGwInfo.getDiskType());
			if (tblEdgeGwInfo.getDiskDetail() != null)
			{
				List<DiskPartInfo> diskPartInfos = JsonUtils.fromJson(tblEdgeGwInfo.getDiskDetail(), new com.google.gson.reflect.TypeToken<List<DiskPartInfo>>(){}.getType());
				diskInfo.setDisks(diskPartInfos);
			}
			gwNodeInfo.getDev_info().setDiskInfo(diskInfo);
			if (tblEdgeGwInfo.getNetwork() != null)
			{
				List<NetworkInfo> networkInfos = JsonUtils.fromJson(tblEdgeGwInfo.getNetwork(),new com.google.gson.reflect.TypeToken<List<NetworkInfo>>(){}.getType());
			    gwNodeInfo.getDev_info().setNetworkInfo(networkInfos);
			}

			if (tblEdgeGwInfo.getSoftwareVersion() != null)
			{
				List<SoftwareVersion> softwareVersionList = JsonUtils.fromJson(tblEdgeGwInfo.getSoftwareVersion(), new com.google.gson.reflect.TypeToken<List<SoftwareVersion>>(){}.getType());
				gwNodeInfo.getDev_info().setSoftwareInfo(softwareVersionList);
			}

			List<NameIDInfo> regionIds = ecrmOperator.getRegionInfosByGwId(tblEdgeGwInfo.getNodeId());
			if (regionIds != null)
			{
				gwNodeInfo.setRegions(regionIds);
			}

			gwNodeInfoList.add(gwNodeInfo);
		}

        return queryGwRsp;
	}

	public List<TblEdgeGwInfo> getGWsByRegionId(String regionId)
	{
		return ecrmOperator.getGwsByRegion(regionId);
	}

	public List<TblEdgeGwInfo> getBrotherGws(String excludeNodeId)
	{
		TblEdgeGwInfoExample example = new TblEdgeGwInfoExample();
		TblEdgeGwInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdNotEqualTo(excludeNodeId);
		criteria.andActiveStatusEqualTo(ActiveStatus.ACITVE);
		return tblEdgeGwInfoMapper.selectByExample(example);
	}

	public List<TblEdgeGwInfo> getAllActiveGws()
	{
		TblEdgeGwInfoExample example = new TblEdgeGwInfoExample();
		TblEdgeGwInfoExample.Criteria criteria = example.createCriteria();
		criteria.andActiveStatusEqualTo(ActiveStatus.ACITVE);
		return tblEdgeGwInfoMapper.selectByExample(example);
	}
	
	public List<TblEdgeGwInfo> getAllGws()
	{
		TblEdgeGwInfoExample example = new TblEdgeGwInfoExample();
		TblEdgeGwInfoExample.Criteria criteria = example.createCriteria();
		criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);
		return tblEdgeGwInfoMapper.selectByExample(example);
	}

	public int deleteGW(String nodeId)
	{
		TblRegionBindInfoExample example = new TblRegionBindInfoExample();
		TblRegionBindInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		return tblRegionBindInfoMapper.deleteByExample(example);
	}
}
