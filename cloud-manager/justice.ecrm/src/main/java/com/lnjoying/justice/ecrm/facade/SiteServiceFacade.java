package com.lnjoying.justice.ecrm.facade;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.model.TblSiteInfo;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionSiteInfo;
import com.lnjoying.justice.ecrm.domain.dto.request.SiteInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QuerySiteRsp;
import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;


@Service
public class SiteServiceFacade
{
	@Autowired
	SiteRepository siteRepository;

	@Autowired
	RedisCacheFacade redisCacheFacade;

	@Autowired
	LabelProperty labelProperty;

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	EdgeRepository edgeRepository;

	@Autowired
    CombRpcService combRpcService;
	
	private String genSiteId()
	{
		String id = RandomName.getRandomNameRL();
		TblSiteInfo tblSiteInfo = siteRepository.getSite(id);
		if (tblSiteInfo != null)
		{
			return genSiteId();
		}
		else
		{
			return id;
		}
		
	}
	
	public void addSite(SiteInputReq siteInputReq) 
	{
		try
		{
			TblSiteInfo tblSiteInfo = new TblSiteInfo();
			if (StringUtils.isNotBlank(siteInputReq.getId()))
			{
				tblSiteInfo.setSiteId(siteInputReq.getId());
			}
			else
			{
				String siteId = genSiteId();
				tblSiteInfo.setSiteId(siteId);
				siteInputReq.setId(siteId);
			}
			tblSiteInfo.setSiteName(siteInputReq.getName());
			tblSiteInfo.setRegionId(siteInputReq.getRegion_id());
			tblSiteInfo.setStatus(ActiveStatus.ACITVE);
			tblSiteInfo.setSiteDesc(siteInputReq.getDescription());
			tblSiteInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
			tblSiteInfo.setUpdateTime(tblSiteInfo.getCreateTime());
			if (siteInputReq.getLocation() != null)
			{
				tblSiteInfo.setAdcode(siteInputReq.getLocation().getAdcode());
				tblSiteInfo.setSiteLocation(JsonUtils.toJson(siteInputReq.getLocation()));
			}
			if (siteInputReq.getLabels() == null)
			{
				siteInputReq.setLabels(new HashMap<>());
			}
			siteInputReq.getLabels().entrySet().removeIf(entry -> StringUtils.isEmpty(entry.getValue()));
			checkAndSetOwner(siteInputReq.getLabels(), tblSiteInfo);
			//check orchestration, inherit from region if non-existent
			checkOrchestration(siteInputReq.getLabels(), siteInputReq.getRegion_id());
			tblSiteInfo.setLabels(JsonUtils.toJson(siteInputReq.getLabels()));
			if (siteInputReq.getTaints() != null)
			{
				tblSiteInfo.setTaints(JsonUtils.toJson(siteInputReq.getTaints()));
			}
			siteRepository.insertSite(tblSiteInfo);

			//set label cache
			redisCacheFacade.setLabelCache(siteInputReq.getLabels(), RedisCache.LABEL_SITE_SET, RedisCache.LABEL_SITE, siteInputReq.getId());

			//set site cache
			RedisUtil.sadd(RedisCache.SCHED_ALL_SITE, "", siteInputReq.getId());
			RedisUtil.sadd(RedisCache.SCHED_REGION_SITE, siteInputReq.getRegion_id(), siteInputReq.getId());
		}
		catch (DuplicateKeyException e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.SITEID_OCCUPIED, ErrorLevel.INFO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void updateSite(String siteId, SiteInputReq siteInputReq)
	{
		try
		{
			TblSiteInfo tblSiteInfo = siteRepository.getSite(siteId);
			if (tblSiteInfo == null)
			{
				throw new WebSystemException(ErrorCode.SITE_NOT_EXIST, ErrorLevel.INFO);
			}

			if (tblSiteInfo.getStatus() == ActiveStatus.REMOVED)
			{
				throw new WebSystemException(ErrorCode.SITE_REMOVED, ErrorLevel.INFO);
			}

			Map<String, String> label = JsonUtils.fromJson(tblSiteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
			if (isBpAdmin())
			{
				if (!((label.containsKey(labelProperty.getSiteBpId()) && label.get(labelProperty.getSiteBpId()).equals(getBpId()))
						||(label.containsKey(labelProperty.getSiteOwner()) && label.get(labelProperty.getSiteOwner()).equals(getUserId()))))
				{
					throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
				}
			}
			else if (isBpUser() || isPersonal())
			{
				if (!(label.containsKey(labelProperty.getSiteOwner()) && label.get(labelProperty.getSiteOwner()).equals(getUserId())))
				{
					throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
				}
			}

			tblSiteInfo.setSiteName(siteInputReq.getName());
			tblSiteInfo.setRegionId(siteInputReq.getRegion_id());
			tblSiteInfo.setSiteDesc(siteInputReq.getDescription());

			if (siteInputReq.getLocation() != null)
			{
				tblSiteInfo.setAdcode(siteInputReq.getLocation().getAdcode());
				tblSiteInfo.setSiteLocation(JsonUtils.toJson(siteInputReq.getLocation()));
			}

			if (siteInputReq.getLabels() == null)
			{
				siteInputReq.setLabels(new HashMap<>());
			}
			siteInputReq.getLabels().entrySet().removeIf(entry -> StringUtils.isEmpty(entry.getValue()));
			checkAndSetOwner(siteInputReq.getLabels(), tblSiteInfo);
			//remove old label cache
			redisCacheFacade.deleteOldLabelCache(tblSiteInfo.getLabels(), RedisCache.LABEL_SITE_SET, RedisCache.LABEL_SITE, siteInputReq.getId());
			//check orchestration, inherit from region if non-existent
			checkOrchestration(siteInputReq.getLabels(), siteInputReq.getRegion_id());
			tblSiteInfo.setLabels(JsonUtils.toJson(siteInputReq.getLabels()));
			//set new label cache
			redisCacheFacade.setLabelCache(siteInputReq.getLabels(), RedisCache.LABEL_SITE_SET, RedisCache.LABEL_SITE, siteInputReq.getId());

			if (siteInputReq.getTaints() != null)
			{
				tblSiteInfo.setTaints(JsonUtils.toJson(siteInputReq.getTaints()));
			}
			siteRepository.updateSite(tblSiteInfo);

		}
		catch (WebSystemException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public QuerySiteRsp getPageableSites(String regionId, String siteId, String orchestration, String name, int pageNum, int pageSize)
	{
		return siteRepository.getPageableSites(regionId, siteId, orchestration, name, pageNum, pageSize);
	}

	public QuerySiteRsp getSites(String regionId, String orchestration, String name) {
		List<RegionSiteInfo> regionSiteInfoList = siteRepository.getSites(regionId, orchestration, name);
		QuerySiteRsp querySiteRsp = new QuerySiteRsp();
		querySiteRsp.setSites(regionSiteInfoList);
		return querySiteRsp;
	}

	public void deleteSite(String siteId)
	{
		try
		{
			//need to update relative dev
			TblSiteInfo tblSiteInfo = siteRepository.getSite(siteId);
			if (tblSiteInfo == null)
			{
				throw new WebSystemException(ErrorCode.SITE_NOT_EXIST, ErrorLevel.INFO);
			}

			Map<String, String> label = JsonUtils.fromJson(tblSiteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
			if (isBpAdmin())
			{
				if (!((label.containsKey(labelProperty.getSiteBpId()) && label.get(labelProperty.getSiteBpId()).equals(getBpId()))
						||(label.containsKey(labelProperty.getSiteOwner()) && label.get(labelProperty.getSiteOwner()).equals(getUserId()))))
				{
					throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
				}
			}
			else if (isBpUser() || isPersonal())
			{
				if (!(label.containsKey(labelProperty.getSiteOwner()) && label.get(labelProperty.getSiteOwner()).equals(getUserId())))
				{
					throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
				}
			}

			siteRepository.deleteSite(tblSiteInfo);

			//remove old label cache
			redisCacheFacade.deleteOldLabelCache(tblSiteInfo.getLabels(), RedisCache.LABEL_SITE_SET, RedisCache.LABEL_SITE, siteId);

			//remove region cache
			RedisUtil.srem(RedisCache.SCHED_ALL_SITE, "", siteId);
			RedisUtil.srem(RedisCache.SCHED_REGION_SITE, tblSiteInfo.getRegionId(), siteId);
			RedisUtil.delete(RedisCache.SITE_ONLINE_EDGE + siteId);
		}
		catch (DataIntegrityViolationException e)
		{
			throw new WebSystemException(ErrorCode.SITE_IN_USE, ErrorLevel.ERROR);
		}
		catch (WebSystemException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public List<String> getSiteIds(List<String> adccodeList)
	{
		return siteRepository.getSiteIds(adccodeList);
	}

	private void checkOrchestration(Map<String, String> labels, String regionId)
	{
		if (!labels.containsKey(labelProperty.getSiteOrchestration()))
		{
			TblRegionInfo tblRegionInfo = regionRepository.getRegion(regionId);
			if (tblRegionInfo != null)
			{
			    Map<String, String> regionLabels = JsonUtils.fromJson(tblRegionInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
				if (regionLabels.containsKey(labelProperty.getRegionOrchestration()))
				{
					labels.put(labelProperty.getSiteOrchestration(), regionLabels.get(labelProperty.getRegionOrchestration()));
				}
			}
		}
	}

	public boolean checkUnique(String siteId)
	{
		return siteRepository.checkUnique(siteId);
	}

	/**
	 * owner label is not set set to current user, set bp id default
	 * @param labels
	 */
	private void checkAndSetOwner(Map<String, String> labels, TblSiteInfo siteInfo)
	{
		if (isBpAdmin())
		{
			String bpId = getBpId();

			String labelBpId = labels.getOrDefault(labelProperty.getSiteBpId(), null);
			String labelUserId = labels.getOrDefault(labelProperty.getSiteOwner(), null);
			if (StringUtils.isEmpty(labelBpId))
			{
				labels.put(labelProperty.getSiteBpId(), bpId);
			}
			else if (!bpId.equals(labelBpId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
			if (StringUtils.isNotEmpty(labelUserId) && !combRpcService.getUmsService().isBpUser(bpId, labelUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}

		}
		else if (isBpUser())
		{
			String bpId = getBpId();
			String userId = getUserId();

			String labelBpId = labels.getOrDefault(labelProperty.getSiteBpId(), null);
			String labelUserId = labels.getOrDefault(labelProperty.getSiteOwner(), null);
			if (StringUtils.isEmpty(labelBpId))
			{
				labels.put(labelProperty.getSiteBpId(), bpId);
			}
			else if (!bpId.equals(labelBpId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
			if (StringUtils.isEmpty(labelUserId))
			{
				labels.put(labelProperty.getSiteOwner(), userId);
			}
			else if (!userId.equals(labelUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		else if (isPersonal())
		{
			String userId = getUserId();

			String labelBpId = labels.getOrDefault(labelProperty.getSiteBpId(), null);
			String labelUserId = labels.getOrDefault(labelProperty.getSiteOwner(), null);
			if (StringUtils.isNotEmpty(labelBpId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
			if (StringUtils.isEmpty(labelUserId))
			{
				labels.put(labelProperty.getSiteOwner(), userId);
			}
			else if (!userId.equals(labelUserId))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
	}

	public void checkDefaultSite()
	{
		try
		{
			TblSiteInfo tblSiteInfo = siteRepository.getSite("default");
			if (tblSiteInfo == null)
			{
				tblSiteInfo = new TblSiteInfo();
				tblSiteInfo.setSiteId("default");
				tblSiteInfo.setSiteName("默认");
				tblSiteInfo.setRegionId("default");
				tblSiteInfo.setStatus(ActiveStatus.ACITVE);
				tblSiteInfo.setCreateTime(Utils.buildDate(0L));
				tblSiteInfo.setUpdateTime(tblSiteInfo.getCreateTime());
				tblSiteInfo.setAdcode("");
				tblSiteInfo.setSiteLocation("{\"nation\":\"\",\"address\":\"\\u0026,,,\",\"adcode\":\"\",\"longitude\":0.0,\"latitude\":0.0,\"elevation\":0.0,\"detail_address\":\"\"}");
				tblSiteInfo.setLabels("{}");
				tblSiteInfo.setTaints("{}");
				siteRepository.insertSite(tblSiteInfo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
