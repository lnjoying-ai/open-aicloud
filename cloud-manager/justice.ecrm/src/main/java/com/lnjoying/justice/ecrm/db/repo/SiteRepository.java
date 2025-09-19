package com.lnjoying.justice.ecrm.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.mapper.EcrmOperator;
import com.lnjoying.justice.ecrm.db.mapper.TblEdgeGwInfoMapper;
import com.lnjoying.justice.ecrm.db.mapper.TblRegionInfoMapper;
import com.lnjoying.justice.ecrm.db.mapper.TblSiteInfoMapper;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfo;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfoExample;
import com.lnjoying.justice.ecrm.domain.dto.model.Location;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionSiteInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.SiteInfo;
import com.lnjoying.justice.ecrm.domain.dto.response.QuerySiteRsp;
import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;


@Repository
@Transactional(rollbackFor = {Exception.class})
@Slf4j
public class SiteRepository
{
	@Autowired
	TblSiteInfoMapper tblSiteInfoMapper;

	@Autowired
	TblRegionInfoMapper tblRegionInfoMapper;

	@Autowired
	TblEdgeGwInfoMapper tblEdgeGwInfoMapper;

	@Autowired
	EcrmOperator ecrmOperator;

	@Autowired
	LabelProperty labelProperty;

	@Autowired
	CombRpcService combRpcService;

	@Autowired
	EdgeRepository edgeRepository;

	public int insertSite(TblSiteInfo tblSiteInfo)
	{
		return tblSiteInfoMapper.insert(tblSiteInfo);
	}

	public int updateSite(TblSiteInfo tblSiteInfo)
	{
		tblSiteInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
		return tblSiteInfoMapper.updateByPrimaryKeySelective(tblSiteInfo);
	}

	/**
	 * @Description:  Physically delete the site from the database, and logically delete it if it fails
	 * @Author: Perry
	 * @Date: 2022/1/12 8:17 下午
	 * @Param: tblSiteInfo
	 */
	public int deleteSite(TblSiteInfo tblSiteInfo)
	{
		return tblSiteInfoMapper.deleteByPrimaryKey(tblSiteInfo.getSiteId());
	}

	public TblSiteInfo getSite(String siteId)
	{
		return tblSiteInfoMapper.selectByPrimaryKey(siteId);
	}

	public QuerySiteRsp getPageableSites(String regionId, String siteId, String orchestration, String name, int pageNum, int pageSize)
	{

		String userId = null;
		String bpId = null;
		if (isBpAdmin())
		{
			bpId = getBpId();
		}
		else if (isBpUser())
		{
			userId = getUserId();
		}
		else if (isPersonal())
		{
			userId = getUserId();
		}

		checkRegionId(regionId);

		QuerySiteRsp querySiteRsp = new QuerySiteRsp();
		PageInfo<TblSiteInfo> pageInfo = doGetTblSiteInfoPageInfo(regionId, siteId, orchestration, name, pageNum, pageSize);
		List<SiteInfo> siteInfos = pageInfo.getList().stream().map(tblSiteInfo ->
		{
			TblRegionInfo tblRegionInfo = tblRegionInfoMapper.selectByPrimaryKey(tblSiteInfo.getRegionId());
			return tblSiteInfo.of(tblSiteInfo, tblRegionInfo);
		}).collect(Collectors.toList());

		for (SiteInfo siteInfo : siteInfos)
		{
			try
			{
				siteInfo.setCloud_num(combRpcService.getCloudService().getCloudNum(userId, bpId, siteInfo.getId()));
				siteInfo.setNode_num(edgeRepository.getEdgeNum(userId, bpId, siteInfo.getId()));
			}
			catch (Exception e)
			{
				siteInfo.setCloud_num(0);
				siteInfo.setNode_num(0);
				log.error("getCloudNum or getEdgeNum error", e);
			}
		}

		if (!CollectionUtils.isEmpty(siteInfos))
		{
			Map<RegionSiteInfo, List<SiteInfo>> regionSiteInfoMap = siteInfos.stream().collect(Collectors.groupingBy(siteInfo -> new RegionSiteInfo(siteInfo.getRegion_id(), siteInfo.getRegion_name())));
			List<RegionSiteInfo> regionSiteInfos = regionSiteInfoMap.entrySet().stream().map(regionSiteInfoListEntry ->
			{
				regionSiteInfoListEntry.getKey().setSites(regionSiteInfoListEntry.getValue());
				return regionSiteInfoListEntry.getKey();
			}).collect(Collectors.toList());
			querySiteRsp.setSites(regionSiteInfos);
			querySiteRsp.setTotalNum(pageInfo.getTotal());
			return querySiteRsp;
		}

		querySiteRsp.setSites(Collections.EMPTY_LIST);
		querySiteRsp.setTotalNum(0L);
		return querySiteRsp;
	}

	public List<RegionSiteInfo> getSites(String regionId, String orchestration, String name) throws WebSystemException
	{
		TblRegionInfo tblRegionInfoPoint = checkRegionId(regionId);

		List<TblSiteInfo> tblSiteInfoList = doGetTblSiteInfos(regionId, null, orchestration, name);
		List<RegionSiteInfo> regionSiteInfoList = new ArrayList<>();

		if (tblSiteInfoList == null)
		{
			return regionSiteInfoList;
		}

		Map<String, List<SiteInfo>> regionSiteMap = new HashMap<>();

		for (TblSiteInfo tblSiteInfo : tblSiteInfoList)
		{

			List<SiteInfo> siteInfoList = regionSiteMap.get(tblSiteInfo.getRegionId());
			if (siteInfoList == null)
			{
				siteInfoList = new ArrayList<>();
				regionSiteMap.put(tblSiteInfo.getRegionId(), siteInfoList);
			}

			TblRegionInfo tblRegionInfo = tblRegionInfoPoint;
			if (tblRegionInfo == null)
			{
				tblRegionInfo = tblRegionInfoMapper.selectByPrimaryKey(tblSiteInfo.getRegionId());
			}

			SiteInfo siteInfo = new SiteInfo();
			siteInfo.setRegion_name(tblRegionInfo.getRegionName());
			siteInfo.setInfo(tblSiteInfo);
			if (!org.apache.commons.lang3.StringUtils.isEmpty(tblSiteInfo.getLabels()))
			{
				Map<String, String> labels = JsonUtils.fromJson(tblSiteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
				if (!isAdmin()){
					if (checkLabels(labels))
					{
						continue;
					}
				}

				siteInfo.setLabels(labels);
			}
			if (!org.apache.commons.lang3.StringUtils.isEmpty(tblSiteInfo.getTaints()))
			{
				Map<String, String> taints = JsonUtils.fromJson(tblSiteInfo.getTaints(), new TypeToken<Map<String, String>>(){}.getType());
				siteInfo.setTaints(taints);
			}
			siteInfo.setLocation(JsonUtils.fromJson(tblSiteInfo.getSiteLocation(), Location.class));
			siteInfoList.add(siteInfo);
		}

		for (Map.Entry<String, List<SiteInfo>> entry : regionSiteMap.entrySet())
		{
			String mapKey = entry.getKey();
			List<SiteInfo> mapValue = entry.getValue();

			if (! mapValue.isEmpty())
			{
				String regionName = mapValue.get(0).getRegion_name();
				RegionSiteInfo regionSiteInfo = new RegionSiteInfo();
				regionSiteInfo.setRegion_id(mapKey);
				regionSiteInfo.setRegion_name(regionName);
				regionSiteInfo.setSites(mapValue);
				regionSiteInfoList.add(regionSiteInfo);
			}

		}

		return regionSiteInfoList;
	}

	public List<String> getSiteIds(List<String> adccodeList)
	{
		String adcodes = StringUtils.join( adccodeList,",");
		adcodes = "(" + adcodes + ")";

		return ecrmOperator.getSiteIdsByAdcodes(adcodes);
	}

	public List<TblSiteInfo> getSites(TblSiteInfoExample example)
	{
		return tblSiteInfoMapper.selectByExample(example);
	}

	public long countSiteByRegionId(String regionId)
	{
		TblSiteInfoExample example = new TblSiteInfoExample();
		TblSiteInfoExample.Criteria criteria = example.createCriteria();
		criteria.andRegionIdEqualTo(regionId);
		criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
		return tblSiteInfoMapper.countByExample(example);
	}

	public boolean checkUnique(String siteId)
	{
		TblSiteInfoExample example = new TblSiteInfoExample();
		TblSiteInfoExample.Criteria criteria = example.createCriteria();
		criteria.andSiteIdEqualTo(siteId);
		return tblSiteInfoMapper.countByExample(example) == 0;
	}

	private boolean checkLabels(Map<String, String> labels)
	{
		if (!CollectionUtils.isEmpty(labels))
		{
			String bpId = queryBpId();
			String userId = queryUserId();
			String siteBpId = labelProperty.getSiteBpId();
			String siteOwner = labelProperty.getSiteOwner();
			if (labels.containsKey(siteBpId))
			{
				if (!Objects.equals(labels.get(siteBpId), bpId))
				{
					if (labels.containsKey(siteOwner) && !Objects.equals(labels.get(siteOwner), userId))
					{
						return true;
					}
				}
			}
			else
			{
				if (labels.containsKey(siteOwner) && !Objects.equals(labels.get(siteOwner), userId))
				{
					return true;
				}
			}
		}
		return false;
	}

	private TblRegionInfo checkRegionId(String regionId)
	{
		TblRegionInfo tblRegionInfoPoint = null;
		if (! StringUtils.isEmpty(regionId))
		{
			tblRegionInfoPoint = tblRegionInfoMapper.selectByPrimaryKey(regionId);
			if (tblRegionInfoPoint == null)
			{
				throw new WebSystemException(ErrorCode.REGION_NOT_EXIST, ErrorLevel.INFO);
			}
		}
		return tblRegionInfoPoint;
	}

	private List<TblSiteInfo> doGetTblSiteInfos(String regionId, String siteId, String orchestration, String name)
	{
		TblSiteInfoExample example = new TblSiteInfoExample();
		TblSiteInfoExample.Criteria criteria = example.createCriteria();
		if (regionId != null && ! regionId.isEmpty())
		{
			criteria.andRegionIdEqualTo(regionId);
		}
		if (siteId != null && ! siteId.isEmpty())
		{
			criteria.andSiteIdEqualTo(siteId);
		}
		if (name != null && ! name.isEmpty())
		{
			criteria.andSiteNameLike("%" + name + "%");
		}
		criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
		if (StringUtils.isNotEmpty(orchestration))
		{
			criteria.andLabelIsEmptyOrEqualTo(labelProperty.getSiteOrchestration(), orchestration);
		}

		if (isBpAdmin())
		{
			criteria.andForBpAdmin(labelProperty.getSiteBpId(), getBpId(), labelProperty.getSiteOwner(), getUserId());
		}
		else if (isBpUser())
		{
			criteria.andForBpUser(labelProperty.getSiteBpId(), getBpId(), labelProperty.getSiteOwner(), getUserId());
		}
		else if (isPersonal())
		{
			criteria.andForPersonalUser(labelProperty.getSiteBpId(), labelProperty.getSiteOwner(), getUserId());
		}

		List<TblSiteInfo> tblSiteInfoList = tblSiteInfoMapper.selectByExample(example);
		return tblSiteInfoList;
	}

	private PageInfo<TblSiteInfo> doGetTblSiteInfoPageInfo(String regionId, String siteId, String orchestration, String name, int pageNum, int pageSize)
	{
		PageHelper.startPage(pageNum, pageSize);
		List<TblSiteInfo> tblSiteInfoList = doGetTblSiteInfos(regionId, siteId, orchestration, name);
		PageInfo<TblSiteInfo> pageInfo = new PageInfo<>(tblSiteInfoList);

		return pageInfo;
	}

	public long countByExample(TblSiteInfoExample example)
	{
		return tblSiteInfoMapper.countByExample(example);
	}
	
	public List<TblSiteInfo> getAllSite()
	{
		TblSiteInfoExample example = new TblSiteInfoExample();
		TblSiteInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
		return tblSiteInfoMapper.selectByExample(example);
	}
}
