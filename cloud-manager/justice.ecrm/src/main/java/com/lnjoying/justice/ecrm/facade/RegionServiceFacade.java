package com.lnjoying.justice.ecrm.facade;

import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.constant.CloudCtrlType;
import com.lnjoying.justice.ecrm.common.constant.EcrmMsgType;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.model.TblRegionBindInfo;
import com.lnjoying.justice.ecrm.db.model.TblRegionInfo;
import com.lnjoying.justice.ecrm.db.repo.RegionGwRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.NameIDInfo;
import com.lnjoying.justice.ecrm.domain.dto.request.RegionInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QueryRegionRsp;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionInfo;
import com.lnjoying.justice.ecrm.process.service.EcrmMsgProcessStrategy;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.msg.*;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;


@Service
public class RegionServiceFacade
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	RegionRepository regionRepository;

	@Autowired
	RegionGwRepository regionGwRepository;

	@Autowired
	SiteRepository siteRepository;

	@Autowired
	EcrmMsgProcessStrategy ecrmMsgProcessStrategy;

	@Autowired
	NetMessageServiceFacade netMessageServiceFacade;

	@Autowired
	RedisCacheFacade redisCacheFacade;

	@Autowired
	LabelProperty labelProperty;

	int maxretry = 67*150;
	
	private String genRegionId()
	{
		String id = RandomName.getRandomNameLR();
		TblRegionInfo tblRegionInfo = regionRepository.getRegion(id);
		if (tblRegionInfo != null)
		{
			return genRegionId();
		}
		else
		{
			return id;
		}
		
	}

	@Transactional(rollbackFor = {Exception.class})
	public void addRegion(RegionInputReq regionInputReq)
	{
		try
		{
			TblRegionInfo tblRegionInfo = new TblRegionInfo();
			if (StringUtils.isNotBlank(regionInputReq.getId()))
			{
				tblRegionInfo.setRegionId(regionInputReq.getId());
			}
			else
			{
				String regionId = genRegionId();
				tblRegionInfo.setRegionId(regionId);
				regionInputReq.setId(regionId);
			}
			
			tblRegionInfo.setRegionName(regionInputReq.getName());
			tblRegionInfo.setRegionDesc(regionInputReq.getDescription());
			tblRegionInfo.setStatus(ActiveStatus.ACITVE);
			tblRegionInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
			tblRegionInfo.setUpdateTime(tblRegionInfo.getCreateTime());
			if (regionInputReq.getLabels() != null)
			{
				tblRegionInfo.setLabels(JsonUtils.toJson(regionInputReq.getLabels()));
			}
			if (regionInputReq.getTaints() != null)
			{
				tblRegionInfo.setTaints(JsonUtils.toJson(regionInputReq.getTaints()));
			}
			regionRepository.insertRegion(tblRegionInfo);

			if (regionInputReq.getGw_node_ids() != null)
			{
				setRegionInfo(regionInputReq);
			}

			//set label cache
			redisCacheFacade.setLabelCache(regionInputReq.getLabels(), RedisCache.LABEL_REGION_SET, RedisCache.LABEL_REGION, regionInputReq.getId());

			//set all region cache
			RedisUtil.sadd(RedisCache.SCHED_ALL_REGION, "", regionInputReq.getId());
		}
		catch (DuplicateKeyException e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.REGIONID_OCCUPIED, ErrorLevel.INFO);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	void setRegionInfo(RegionInputReq regionInputReq)
	{
		for (String gwId : regionInputReq.getGw_node_ids())
		{
			TblRegionBindInfo tblRegionBindInfo = new TblRegionBindInfo();
			tblRegionBindInfo.setRegionId(regionInputReq.getId());
			tblRegionBindInfo.setNodeId(gwId);
			tblRegionBindInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
			tblRegionBindInfo.setUpdateTime(tblRegionBindInfo.getCreateTime());
			regionGwRepository.insertRegionBind(tblRegionBindInfo);
		}

		MessagePack msgPack = new MessagePack();
		msgPack.setMsgType(EcrmMsgType.SET_REGION);
		msgPack.setMessageObj(regionInputReq);
		ecrmMsgProcessStrategy.sendMessage(msgPack);
	}

	public void updateRegion(String regionId, RegionInputReq regionInputReq)
	{
		try
		{
			regionInputReq.setId(regionId);
			TblRegionInfo tblRegionInfo = regionRepository.getRegion(regionId);
			if (tblRegionInfo == null)
			{
				throw new WebSystemException(ErrorCode.REGION_EMPTY, ErrorLevel.INFO);
			}

			if (tblRegionInfo.getStatus() == ActiveStatus.REMOVED)
			{
				throw new WebSystemException(ErrorCode.REGION_REMOVED, ErrorLevel.INFO);
			}
			tblRegionInfo.setRegionName(regionInputReq.getName());
			tblRegionInfo.setRegionDesc(regionInputReq.getDescription());
			if (regionInputReq.getLabels() != null)
			{
				//remove old label cache
				redisCacheFacade.deleteOldLabelCache(tblRegionInfo.getLabels(), RedisCache.LABEL_REGION_SET, RedisCache.LABEL_REGION, regionInputReq.getId());

				tblRegionInfo.setLabels(JsonUtils.toJson(regionInputReq.getLabels()));

				//set new label cache
				redisCacheFacade.setLabelCache(regionInputReq.getLabels(), RedisCache.LABEL_REGION_SET, RedisCache.LABEL_REGION, regionInputReq.getId());
			}
			if (regionInputReq.getTaints() != null)
			{
				tblRegionInfo.setTaints(JsonUtils.toJson(regionInputReq.getTaints()));
			}
			regionRepository.updateRegion(tblRegionInfo);

			if (regionInputReq.getGw_node_ids() != null)
			{
				regionGwRepository.deleteRegion(regionId);
				setRegionInfo(regionInputReq);
			}

		}
		catch (WebSystemException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public QueryRegionRsp getPageableRegions(String orchestration, String name, int pageNum, int pageSize)
	{
		PageInfo<TblRegionInfo> regions = regionRepository.getPageableRegions(orchestration, name, pageNum, pageSize);
		List<RegionInfo> regionInfos = regions.getList().stream().map(tblRegionInfo -> tblRegionInfo.of(tblRegionInfo, regionGwRepository)).collect(Collectors.toList());
		QueryRegionRsp queryRegionRsp = new QueryRegionRsp();
		queryRegionRsp.setTotalNum(regions.getTotal());
		queryRegionRsp.setRegions(regionInfos);
		return queryRegionRsp;
	}

	public QueryRegionRsp getRegions(String orchestration, String name)
	{
		List<TblRegionInfo> tblRegionInfoList = regionRepository.getRegions(orchestration, name);
		if (tblRegionInfoList == null)
		{
			return null;
		}

		QueryRegionRsp queryRegionRsp = new QueryRegionRsp();
		List<RegionInfo> regionInfos = new ArrayList<>();
		queryRegionRsp.setRegions(regionInfos);
		for (TblRegionInfo tblRegionInfo : tblRegionInfoList)
		{
			RegionInfo regionInfo = new RegionInfo();
			regionInfo.setInfo(tblRegionInfo);
			if (!StringUtils.isEmpty(tblRegionInfo.getLabels()))
			{
				Map<String, String> labels = JsonUtils.fromJson(tblRegionInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
				regionInfo.setLabels(labels);
				if (!isAdmin())
				{
					if (checkLabels(labels))
					{
						continue;
					}
				}
			}
			if (!StringUtils.isEmpty(tblRegionInfo.getTaints()))
			{
				Map<String, String> taints = JsonUtils.fromJson(tblRegionInfo.getTaints(), new TypeToken<Map<String, String>>(){}.getType());
				regionInfo.setTaints(taints);
			}
			List<NameIDInfo> gwBinds = regionGwRepository.getRegionBindGwInfo(tblRegionInfo.getRegionId());
			if (gwBinds != null)
			{
				regionInfo.setGw_node_ids(gwBinds);
			}

			regionInfos.add(regionInfo);
		}
		return queryRegionRsp;
	}


	public void deleteRegion(String regionId)
	{
		try
		{
			TblRegionInfo tblRegionInfo = regionRepository.getRegion(regionId);
			if (tblRegionInfo == null)
			{
				throw new WebSystemException(ErrorCode.REGION_EMPTY, ErrorLevel.INFO);
			}

			if (tblRegionInfo.getStatus() == ActiveStatus.REMOVED)
			{
				throw new WebSystemException(ErrorCode.REGION_REMOVED, ErrorLevel.INFO);
			}

			long regionSiteNum = siteRepository.countSiteByRegionId(regionId);
			if (regionSiteNum > 0)
			{
				throw new WebSystemException(ErrorCode.REGION_IN_USE, ErrorLevel.ERROR);
			}

			List<TblRegionBindInfo> tblRegionBindInfoList = regionGwRepository.getRegionBinds(regionId);

			regionGwRepository.deleteRegion(regionId);
			regionRepository.deleteRegion(tblRegionInfo);

			if (tblRegionBindInfoList != null && ! tblRegionBindInfoList.isEmpty())
			{
				MessagePack msgPack = new MessagePack();
				msgPack.setMsgType(EcrmMsgType.RM_REGION);
				msgPack.setMessageObj(regionId);
				ecrmMsgProcessStrategy.sendMessage(msgPack);
			}

			//remove old label cache
			redisCacheFacade.deleteOldLabelCache(tblRegionInfo.getLabels(), RedisCache.LABEL_REGION_SET, RedisCache.LABEL_REGION, regionId);

			//remove region cache
			RedisUtil.srem(RedisCache.SCHED_ALL_REGION, "", regionId);
			RedisUtil.delete(RedisCache.SCHED_REGION_SITE + regionId);
			RedisUtil.delete(RedisCache.REGION_ONLINE_EDGE + regionId);
		}
		catch (DataIntegrityViolationException e)
		{
			throw new WebSystemException(ErrorCode.REGION_IN_USE, ErrorLevel.INFO);
		}
		catch (WebSystemException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public List<String> getRegionIdsByGwId(String nodeId)
	{
		return regionGwRepository.getRegionIdsByGwNodeId(nodeId);
	}

	public void processRegionBindGw(RegionInputReq regionInputReq)
	{
		LOGGER.info("process region bind. {}", regionInputReq);
		for (String nodeId : regionInputReq.getGw_node_ids())
		{
			sendRegionBindMessage(nodeId);
		}
	}

	public void processRegionBindGw(List<TblRegionBindInfo> tblRegionBindInfoList)
	{
		for (TblRegionBindInfo tblRegionBindInfo : tblRegionBindInfoList)
		{
			sendRegionBindMessage(tblRegionBindInfo.getNodeId());
		}
	}

	void sendRegionBindMessage(String nodeId)
	{
		List<String> regionIds = regionGwRepository.getRegionIdsByGwNodeId(nodeId);
		if (regionIds == null || regionIds.isEmpty())
		{
			return;
		}

		EeCtrlMessageDef.msg_set_region_info_req.Builder regionBuilder = EeCtrlMessageDef.msg_set_region_info_req.newBuilder().setBindNodeId(nodeId).addAllRegionIds(regionIds);
		EeCtrlMessageDef.msg_cloud_ctrl_body.Builder ctrlBody =  EeCtrlMessageDef.msg_cloud_ctrl_body.newBuilder().setCtrlType(CloudCtrlType.SET_REGION_INFO_REQ).setSetRegionInfoReqBody(regionBuilder);

		EeCommonDef.msg_header.Builder header = netMessageServiceFacade.makeReqMsgHeader(MessageName.CLOUD_CTRL);
		EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(header).setCloudCtrlBody(ctrlBody).build();
		netMessageServiceFacade.submitMessage(nodeId, netMessage);
	}

	public boolean checkUnique(String regionId)
	{
		return regionRepository.checkUnique(regionId);
	}


	private boolean checkLabels(Map<String, String> labels)
	{
		if (!CollectionUtils.isEmpty(labels))
		{
			String bpId = queryBpId();
			String userId = queryUserId();
			String regionBpId = labelProperty.getRegionBpId();
			String regionOwner = labelProperty.getRegionOwner();
			if (labels.containsKey(regionBpId))
			{
				if (!Objects.equals(labels.get(regionBpId), bpId))
				{
					if (labels.containsKey(regionOwner) && !Objects.equals(labels.get(regionOwner), userId))
					{
						return true;
					}
				}
			}
			else
			{
				if (labels.containsKey(regionOwner) && !Objects.equals(labels.get(regionOwner), userId))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void checkDefaultRegion()
	{
		try
		{
			TblRegionInfo tblRegionInfo = regionRepository.getRegion("default");
			if (tblRegionInfo == null)
			{
				tblRegionInfo = new TblRegionInfo();
				tblRegionInfo.setRegionId("default");
				tblRegionInfo.setRegionName("默认");
				tblRegionInfo.setStatus(ActiveStatus.ACITVE);
				tblRegionInfo.setCreateTime(Utils.buildDate(0L));
				tblRegionInfo.setUpdateTime(tblRegionInfo.getCreateTime());
				tblRegionInfo.setLabels("{}");
				tblRegionInfo.setTaints("{}");
				regionRepository.insertRegion(tblRegionInfo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
