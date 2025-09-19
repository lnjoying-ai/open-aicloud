package com.lnjoying.justice.ecrm.db.repo;

import com.google.gson.Gson;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.mapper.*;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.domain.EdgeStatusCounts;
import com.lnjoying.justice.ecrm.domain.dto.model.*;
import com.lnjoying.justice.ecrm.domain.dto.request.EdgeInputReq;
import com.lnjoying.justice.ecrm.domain.dto.response.QueryEdgeRsp;
import com.lnjoying.justice.ecrm.domain.model.search.EdgeSearchCritical;
import com.lnjoying.justice.ecrm.facade.DivisionServiceFacade;
import com.lnjoying.justice.ecrm.facade.SiteServiceFacade;
import com.lnjoying.justice.schema.common.ecrm.GpuState;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.schema.entity.stat.*;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;


@Repository("edgeRepository")
@Transactional(rollbackFor = {Exception.class})
public class EdgeRepository
{
	@Autowired
	TblEdgeComputeInfoMapper tblEdgeComputeInfoMapper;

	@Autowired
	TblEdgeComputeLabelInfoMapper tblEdgeComputeLabelInfoMapper;

	@Autowired
	TblEdgeComputeTaintInfoMapper tblEdgeComputeTaintInfoMapper;

	@Autowired
	TblSiteInfoMapper tblSiteInfoMapper;

	@Autowired
	TblRegionInfoMapper tblRegionInfoMapper;

	@Autowired
	TblEdgeImageInfoMapper tblEdgeImageInfoMapper;

	@Autowired
	TblEdgePortInfoMapper tblEdgePortInfoMapper;

	@Autowired
	TblEdgeComputeGpuInfoMapper tblEdgeComputeGpuInfoMapper;

	@Autowired
	EcrmOperator ecrmOperator;

	Gson gson =  new Gson();

//	@Autowired
//	TblLabelInfoMapper tblLabelInfoMapper;

	@Autowired
	DivisionServiceFacade divisionServiceFacade;

	@Autowired
	SiteServiceFacade siteServiceFacade;

	@Autowired
	LabelProperty labelProperty;

	@Autowired
	TblEdgeUpgradeInfoMapper tblEdgeUpgradeInfoMapper;

	@Autowired
	StatMapper statMapper;

	public Map<String, String> addEdge(EdgeInputReq edgeInputReq)
	{
		return null;
	}

	public int insertEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		return tblEdgeComputeInfoMapper.insert(tblEdgeComputeInfo);
	}

	public int updateEdge(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		return tblEdgeComputeInfoMapper.updateByPrimaryKey(tblEdgeComputeInfo);
	}

	public int updateEdgeSelective(TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		return tblEdgeComputeInfoMapper.updateByPrimaryKeySelective(tblEdgeComputeInfo);
	}

	public TblEdgeComputeInfoExample buildEdgeOnlineStatusWithUnchangedSinceExample(long timeInterval)
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();
		criteria.andOnlineStatusEqualTo(OnlineStatus.ONLINE);
		long cur = System.currentTimeMillis();
		criteria.andUpdateTimeLessThan(new Date(cur - timeInterval));
		return example;
	}

	public int deleteEdge(String nodeId)
	{
		return tblEdgeComputeInfoMapper.deleteByPrimaryKey(nodeId);
	}

	public int insertLabel(TblEdgeComputeLabelInfo tblLabelInfo)
	{
		return tblEdgeComputeLabelInfoMapper.insert(tblLabelInfo);
	}

	public int deleteEdgeLabel(String nodeId, String labelKey)
	{
		TblEdgeComputeLabelInfoExample example = new TblEdgeComputeLabelInfoExample();
		TblEdgeComputeLabelInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		criteria.andLabelKeyEqualTo(labelKey);
		return tblEdgeComputeLabelInfoMapper.deleteByExample(example);
	}

	public QueryEdgeRsp getEdges(EdgeSearchCritical critical)
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();
		if (critical.getSite() != null && ! critical.getSite().isEmpty())
		{
			criteria.andSiteIdEqualTo(critical.getSite());
		}
		else
		{
			if (critical.getAdcode() != null || critical.getNation() != null)
			{
				List<String> adcodes = new ArrayList<>();
				divisionServiceFacade.getChildAdcodes(critical.getNation(), critical.getAdcode(), adcodes);
				if (! adcodes.isEmpty())
				{
					List<String> siteIds = siteServiceFacade.getSiteIds(adcodes);
					if (siteIds != null)
					{
						criteria.andSiteIdIn(siteIds);
					}
				}
			}
		}

		if (StringUtils.isNotEmpty(critical.getRegion()))
		{
			criteria.andRegionIdEqualTo(critical.getRegion());
		}

		if (critical.getActiveStatus() != null)
		{
			criteria.andActiveStatusEqualTo(critical.getActiveStatus());
		}

		criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);

		if (critical.getOnlineStatus() != null)
		{
			criteria.andOnlineStatusEqualTo(critical.getOnlineStatus());
		}

		if (StringUtils.isNotEmpty(critical.getOrchestration()))
		{
			criteria.andLabelIsEmptyOrEqualTo(labelProperty.getNodeOrchestration(), critical.getOrchestration());
		}

		if (StringUtils.isNotEmpty(critical.getGpu()))
		{
			criteria.andLabelEqualTo(labelProperty.getNodeGpu(), critical.getGpu());
		}
		else if (critical.getGpu() != null && critical.getGpu().isEmpty())
		{
			criteria.andLabelContainLabelKey(labelProperty.getNodeGpu());
		}

		if (StringUtils.isNotEmpty(critical.getVendor()))
		{
			criteria.andVenderLike("%" + critical.getVendor()+ "%");
		}

		if (StringUtils.isNotEmpty(critical.getUuid()))
		{
			criteria.andUuidLike("%" + critical.getUuid()+ "%");
		}

		if (StringUtils.isNotEmpty(critical.getName()))
		{
			criteria.andNodeNameLike("%" + critical.getName()+ "%");
		}
		checkUserRelatedLabels(criteria);

		long count = tblEdgeComputeInfoMapper.countByExample(example);
		QueryEdgeRsp queryEdgeRsp = new QueryEdgeRsp();
		if (count == 0)
		{
			return queryEdgeRsp;
		}

		queryEdgeRsp.setTotal_num(count);

		int begin = ((critical.getPageNum()-1) * critical.getPageSize());
		example.setOrderByClause("create_time desc");
		example.setStartRow(begin);
		example.setPageSize(critical.getPageSize());

		List<TblEdgeComputeInfo> tblEdgeComputeInfoList = tblEdgeComputeInfoMapper.selectByExample(example);
		if (tblEdgeComputeInfoList == null)
		{
			return null;
		}

		List<EdgeNodeInfo> edgeNodeInfoList = new ArrayList<>();
		queryEdgeRsp.setNodes(edgeNodeInfoList);
		for (TblEdgeComputeInfo tblEdgeComputeInfo : tblEdgeComputeInfoList)
		{
			EdgeNodeInfo edgeNodeInfo = new EdgeNodeInfo();
			setEdgeNodeInfo(edgeNodeInfo, tblEdgeComputeInfo);

			setRegionNameAndSiteName(tblEdgeComputeInfo, edgeNodeInfo);

			edgeNodeInfoList.add(edgeNodeInfo);
		}
		return queryEdgeRsp;
	}

	public void setEdgeNodeInfo(EdgeNodeInfo edgeNodeInfo, TblEdgeComputeInfo tblEdgeComputeInfo)
	{
		edgeNodeInfo.setInfo(tblEdgeComputeInfo);
		edgeNodeInfo.getDev_info().setNetworkInfo(gson.fromJson(tblEdgeComputeInfo.getNetwork(), new com.google.gson.reflect.TypeToken<List<NetworkInfo>>(){}.getType()));
		DiskInfo diskInfo = new DiskInfo();
		if (tblEdgeComputeInfo.getDiskTotal() != null) diskInfo.setDiskTotal(tblEdgeComputeInfo.getDiskTotal());
		if (tblEdgeComputeInfo.getDiskType() != null) diskInfo.setDiskTypes(tblEdgeComputeInfo.getDiskType());
		if (tblEdgeComputeInfo.getDiskDetail() != null)
		{
			List<DiskPartInfo> diskPartInfos = gson.fromJson(tblEdgeComputeInfo.getDiskDetail(),new com.google.gson.reflect.TypeToken<List<DiskPartInfo>>(){}.getType());
			diskInfo.setDisks(diskPartInfos);
		}
		edgeNodeInfo.getDev_info().setDiskInfo(diskInfo);

		if (tblEdgeComputeInfo.getSoftwareVersion() != null)
		{
			List<SoftwareVersion> softwareVersionList = gson.fromJson(tblEdgeComputeInfo.getSoftwareVersion(), new com.google.gson.reflect.TypeToken<List<SoftwareVersion>>(){}.getType());
			edgeNodeInfo.getDev_info().setSoftwareInfo(softwareVersionList);
		}

		if (tblEdgeComputeInfo.getSoftwareMap() != null)
		{
			Map<String, String> softwareMap = gson.fromJson(tblEdgeComputeInfo.getSoftwareMap(), new com.google.gson.reflect.TypeToken<Map<String, String>>(){}.getType());
			edgeNodeInfo.getDev_info().setSoftwareMap(softwareMap);
		}

		if (tblEdgeComputeInfo.getNetwork() != null)
		{
			List<NetworkInfo> networkInfos = gson.fromJson(tblEdgeComputeInfo.getNetwork(),new com.google.gson.reflect.TypeToken<List<NetworkInfo>>(){}.getType());
			edgeNodeInfo.getDev_info().setNetworkInfo(networkInfos);

			if (!CollectionUtils.isEmpty(networkInfos))
			{
				for (NetworkInfo networkInfo : networkInfos)
				{
					if (StringUtils.isNotEmpty(networkInfo.getIpv4()))
					{
						edgeNodeInfo.setHost_ip(networkInfo.getIpv4());
						break;
					}
					if (StringUtils.isNotEmpty(networkInfo.getIpv6()))
					{
						edgeNodeInfo.setHost_ip(networkInfo.getIpv6());
						break;
					}
				}
			}

		}

		ResourceLimit resourceLimit = new ResourceLimit(tblEdgeComputeInfo.getMemLimit(), tblEdgeComputeInfo.getCpuLimit(), tblEdgeComputeInfo.getDiskLimit());
		edgeNodeInfo.setResource_limit(resourceLimit);

		List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfos = selectGpuByNodeId(edgeNodeInfo.getNode_id());
		List<GpuInfo> gpuInfos = new ArrayList<>();
		if (null != tblEdgeComputeGpuInfos && tblEdgeComputeGpuInfos.size()>0)
		{
			for (TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo:tblEdgeComputeGpuInfos)
			{
				GpuInfo gpuInfo = new GpuInfo();
				gpuInfo.setGpuId(tblEdgeComputeGpuInfo.getGpuId());
				gpuInfo.setGpuType(tblEdgeComputeGpuInfo.getGpuType());
				gpuInfo.setGpuModel(tblEdgeComputeGpuInfo.getGpuModel());
				gpuInfo.setDriverVersion(tblEdgeComputeGpuInfo.getDriverVersion());
				gpuInfo.setCudaVersion(tblEdgeComputeGpuInfo.getCudaVersion());
				gpuInfo.setCudnnVersion(tblEdgeComputeGpuInfo.getCudnnVersion());
				gpuInfo.setIndex(tblEdgeComputeGpuInfo.getIndex());
				gpuInfos.add(gpuInfo);
			}
		}
		edgeNodeInfo.getDev_info().setGpuInfo(gpuInfos);

		List<TblEdgeComputeLabelInfo> tblEdgeComputeLabelInfos = selectLabelByNodeId(edgeNodeInfo.getNode_id());
		Map<String, String> labelMap = new HashMap<>();
		if (null != tblEdgeComputeLabelInfos && tblEdgeComputeLabelInfos.size()>0)
		{
			for (TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo:tblEdgeComputeLabelInfos)
			{
				labelMap.put(tblEdgeComputeLabelInfo.getLabelKey(), tblEdgeComputeLabelInfo.getLabelValue());
			}
		}
		edgeNodeInfo.setLabels(labelMap);

		List<TblEdgeComputeTaintInfo> tblEdgeComputeTaintInfos = selectTaintByNodeId(edgeNodeInfo.getNode_id());
		Map<String, String> taintMap = new HashMap<>();
		if (null != tblEdgeComputeTaintInfos && tblEdgeComputeTaintInfos.size()>0)
		{
			for (TblEdgeComputeTaintInfo tblEdgeComputeTaintInfo:tblEdgeComputeTaintInfos)
			{
				taintMap.put(tblEdgeComputeTaintInfo.getTaintKey(), tblEdgeComputeTaintInfo.getTaintValue());
			}
		}
		edgeNodeInfo.setTaints(taintMap);

	}

	public TblEdgeComputeInfo getEdgeNode(String nodeId)
	{
		return tblEdgeComputeInfoMapper.selectByPrimaryKey(nodeId);
	}

	public int deleteLabels(String nodeId)
	{
		TblEdgeComputeLabelInfoExample example = new TblEdgeComputeLabelInfoExample();
		TblEdgeComputeLabelInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		return tblEdgeComputeLabelInfoMapper.deleteByExample(example);
	}

	public List<TblEdgeComputeLabelInfo> selectLabelByNodeId(String nodeId)
	{
		TblEdgeComputeLabelInfoExample example = new TblEdgeComputeLabelInfoExample();
		TblEdgeComputeLabelInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		return tblEdgeComputeLabelInfoMapper.selectByExample(example);
	}

	public long  countAcitveEdge()
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria= example.createCriteria();
		criteria.andActiveStatusEqualTo(1);
		Long edgeSum = countEdge(example);
		return edgeSum;
	}

	public long countAcitveEdgeCores()
	{
		return ecrmOperator.countEdgeActiveCores();
	}
	public long countEdge(TblEdgeComputeInfoExample example)
	{
		return tblEdgeComputeInfoMapper.countByExample(example);
	}

	public long countEdge(String bpId, String userId)
	{
		return tblEdgeComputeInfoMapper.countByBpIdAndUserId(bpId, userId);
	}

	public List<TblEdgeComputeInfo> getEdges(TblEdgeComputeInfoExample example)
	{
		return tblEdgeComputeInfoMapper.selectByExample(example);
	}

	public long insertHostImage(TblEdgeImageInfo tblEdgeImageInfo)
	{
		return tblEdgeImageInfoMapper.insert(tblEdgeImageInfo);
	}

	public long insertHostPort(TblEdgePortInfo tblEdgePortInfo)
	{
		return tblEdgePortInfoMapper.insert(tblEdgePortInfo);
	}

	public TblEdgeImageInfo getHostImage(String nodeId)
	{
		return tblEdgeImageInfoMapper.selectByPrimaryKey(nodeId);
	}

	public TblEdgePortInfo getHostPortInfo(String nodeId)
	{
		return tblEdgePortInfoMapper.selectByPrimaryKey(nodeId);
	}

	public long updateHostImage(TblEdgeImageInfo tblEdgeImageInfo)
	{
		return tblEdgeImageInfoMapper.updateByPrimaryKeySelective(tblEdgeImageInfo);
	}

	public long updateHostPort(TblEdgePortInfo tblEdgePortInfo)
	{
		return tblEdgePortInfoMapper.updateByPrimaryKeySelective(tblEdgePortInfo);
	}

	public int insertTaint(TblEdgeComputeTaintInfo tblTaintInfo)
	{
		return tblEdgeComputeTaintInfoMapper.insert(tblTaintInfo);
	}

	public int deleteTaints(String nodeId)
	{
		TblEdgeComputeTaintInfoExample example = new TblEdgeComputeTaintInfoExample();
		TblEdgeComputeTaintInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		return tblEdgeComputeTaintInfoMapper.deleteByExample(example);
	}

	public List<TblEdgeComputeTaintInfo> selectTaintByNodeId(String nodeId)
	{
		TblEdgeComputeTaintInfoExample example = new TblEdgeComputeTaintInfoExample();
		TblEdgeComputeTaintInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		return tblEdgeComputeTaintInfoMapper.selectByExample(example);
	}

	public int insertEdgeGpu(TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo)
	{
		return tblEdgeComputeGpuInfoMapper.insert(tblEdgeComputeGpuInfo);
	}

	public TblEdgeComputeGpuInfo selectEdgeGpuByPrimaryKey(String gpuId)
	{
		return tblEdgeComputeGpuInfoMapper.selectByPrimaryKey(gpuId);
	}

	public List<TblEdgeComputeGpuInfo> selectGpuByNodeId(String nodeId)
	{
		TblEdgeComputeGpuInfoExample example = new TblEdgeComputeGpuInfoExample();
		TblEdgeComputeGpuInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		criteria.andStatusNotEqualTo(GpuState.REMOVE.code);
		return tblEdgeComputeGpuInfoMapper.selectByExample(example);
	}

	public int updateEdgeGpu(TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo)
	{
		return tblEdgeComputeGpuInfoMapper.updateByPrimaryKey(tblEdgeComputeGpuInfo);
	}

	public List<TblEdgeComputeInfo> getEdgesByExample(TblEdgeComputeInfoExample example)
	{
		return tblEdgeComputeInfoMapper.selectByExample(example);
	}

	public List<TblEdgeComputeLabelInfo> getEdgeLabels(TblEdgeComputeLabelInfoExample example)
	{
		return tblEdgeComputeLabelInfoMapper.selectByExample(example);
	}

	public String getLabelValueByNodeIdAndLabelKey(String nodeId, String labelKey)
	{
		String labelValue = null;
		TblEdgeComputeLabelInfoExample edgeLabelExample = new TblEdgeComputeLabelInfoExample();
		TblEdgeComputeLabelInfoExample.Criteria edgeLabelCriteria = edgeLabelExample.createCriteria();
		edgeLabelCriteria.andNodeIdEqualTo(nodeId);
		edgeLabelCriteria.andLabelKeyEqualTo(labelKey);
		List<TblEdgeComputeLabelInfo> edgeLabels = getEdgeLabels(edgeLabelExample);
		if (! CollectionUtils.isEmpty(edgeLabels))
		{
			TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo = edgeLabels.get(0);
			labelValue = tblEdgeComputeLabelInfo.getLabelValue();
		}

		return labelValue;
	}

	public long countEdgeBySiteId(String siteId)
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();
		criteria.andSiteIdEqualTo(siteId);
		criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);
		return tblEdgeComputeInfoMapper.countByExample(example);
	}

	public RegionResourceService.NodeInfo selectByNodeId(String nodeId)
	{
		return tblEdgeComputeInfoMapper.selectByNodeId(nodeId);
	}

	private void checkUserRelatedLabels(TblEdgeComputeInfoExample.Criteria criteria)
	{
		if (isBpAdmin())
		{
			criteria.andLabelEqualTo(labelProperty.getNodeBpId(), queryBpId());
			//criteria.andLabelIsEmptyOrEqualTo(labelProperty.getNodeBpId(), queryBpId());
		}
		else if (isBpUser())
		{
			criteria.andLabelJoin(labelProperty.getNodeBpId(), queryBpId(), labelProperty.getNodeOwner(), queryUserId());
		}
	}

	public TblEdgeUpgradeInfo getEdgeUpgradeInfo(String nodeId)
	{
		return tblEdgeUpgradeInfoMapper.selectByPrimaryKey(nodeId);
	}

	public List<TblEdgeUpgradeInfo> getEdgeUpgradeInfos(TblEdgeUpgradeInfoExample example)
	{
		return tblEdgeUpgradeInfoMapper.selectByExample(example);
	}

	public int insertEdgeUpgradeInfo(TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
	{
		return tblEdgeUpgradeInfoMapper.insert(tblEdgeUpgradeInfo);
	}

	public int updateEdgeUpgradeInfo(TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
	{
		return tblEdgeUpgradeInfoMapper.updateByPrimaryKey(tblEdgeUpgradeInfo);
	}

	public NodeStatusMetrics getAllNodeStatusMetrics(List<String> categories, String bpId, String userId)
	{
		return statMapper.getAllNodeStatusMetrics(categories, labelProperty.getNodeBpId(), bpId, labelProperty.getNodeOwner(), userId);
	}

	public List<SiteNodeMetrics> getSiteStatusMetrics(List<String> siteIds, List<String> categories, String bpId, String userId)
	{
		return statMapper.getSiteStatusMetrics(siteIds, categories, labelProperty.getNodeBpId(), bpId, labelProperty.getNodeOwner(), userId);
	}

	public List<RegionNodeMetrics> getRegionStatusMetrics(List<String> regionIds, List<String> categories, String bpId, String userId)
	{
		return statMapper.getRegionStatusMetrics(regionIds, categories, labelProperty.getNodeBpId(), bpId, labelProperty.getNodeOwner(), userId);
	}

	public void setRegionNameAndSiteName(TblEdgeComputeInfo tblEdgeComputeInfo, EdgeNodeInfo edgeNodeInfo)
	{
		if (! StringUtils.isEmpty(tblEdgeComputeInfo.getRegionId()))
		{
			TblRegionInfo tblRegionInfo = tblRegionInfoMapper.selectByPrimaryKey(tblEdgeComputeInfo.getRegionId());
			if (tblRegionInfo != null)
			{
				edgeNodeInfo.setRegion_name(tblRegionInfo.getRegionName());
			}
		}

		if (! StringUtils.isEmpty(tblEdgeComputeInfo.getSiteId()))
		{
			TblSiteInfo tblSiteInfo = tblSiteInfoMapper.selectByPrimaryKey(tblEdgeComputeInfo.getSiteId());
			if (tblSiteInfo != null)
			{
				edgeNodeInfo.setSite_name(tblSiteInfo.getSiteName());
			}
		}
	}

	public String getHostIpv4(String nodeId)
	{
		String hostIpv4 = "";
		TblEdgeComputeInfo tblEdgeComputeInfo = getEdgeNode(nodeId);
		if (tblEdgeComputeInfo == null)
		{
			return null;
		}

		if (tblEdgeComputeInfo.getNetwork() != null)
		{
			List<NetworkInfo> networkInfos = gson.fromJson(tblEdgeComputeInfo.getNetwork(),new com.google.gson.reflect.TypeToken<List<NetworkInfo>>(){}.getType());

			if (!CollectionUtils.isEmpty(networkInfos))
			{
				for (NetworkInfo networkInfo : networkInfos)
				{
					if (StringUtils.isNotEmpty(networkInfo.getIpv4()))
					{
						hostIpv4 = networkInfo.getIpv4();
						if (StringUtils.isNotBlank(hostIpv4))
						{
							// 192.168.1.224/24
							String[] split = StringUtils.split(hostIpv4, "/");
							if (split.length >= 1)
							{
								return split[0];
							}
						}
					}
				}
			}

		}

		return hostIpv4;
	}

	public int getEdgeNum(String userId, String bpId, String siteId)
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();

		if (CollectionUtils.hasContent(bpId))
		{
			criteria.andBpIdEqualTo(bpId);
		}

		if (CollectionUtils.hasContent(userId))
		{
			criteria.andUserIdEqualTo(userId);
		}

		if (CollectionUtils.hasContent(siteId))
		{
			criteria.andSiteIdEqualTo(siteId);
		}
		criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);

		return (int)countEdge(example);
	}

	public GetStatusMetricsRsp getEdgeStatusMetrics(String userId, String bpId)
	{
		GetStatusMetricsRsp getStatusMetricsRsp = new GetStatusMetricsRsp();
		getStatusMetricsRsp.setTotalNum(0);
		getStatusMetricsRsp.setStatusMetrics(new ArrayList<>());
		List<StatusMetrics> statusMetricsList = tblEdgeComputeInfoMapper.getEdgeStatusMetrics(userId, bpId);

		Set<GetStatusMetricsRsp.StatusMetrics> statusMetricsSet = new HashSet<>();
		for (StatusMetrics statusMetrics : statusMetricsList)
		{
			GetStatusMetricsRsp.StatusMetrics metrics = new GetStatusMetricsRsp.StatusMetrics();
			metrics.setCount(statusMetrics.getCount());
			String statusEn = statusMetrics.getStatus() == 0 ? "offline" : "online";
			String statusCn = statusMetrics.getStatus() == 0 ? "离线" : "在线";
			Map<String, String> desc = new HashMap<>();
			desc.put("en", statusEn);
			desc.put("cn", statusCn);
			metrics.setStatus(new StatusCode(statusMetrics.getStatus(), desc));
			statusMetricsSet.add(metrics);
			getStatusMetricsRsp.setTotalNum(getStatusMetricsRsp.getTotalNum() + statusMetrics.getCount());
		}
		setDefaultStatusMetrics(statusMetricsSet, "online", "在线", 1);
		setDefaultStatusMetrics(statusMetricsSet, "offline", "离线", 0);
		getStatusMetricsRsp.getStatusMetrics().addAll(statusMetricsSet);
		return getStatusMetricsRsp;
	}

	private static void setDefaultStatusMetrics(Set<GetStatusMetricsRsp.StatusMetrics> statusMetricsSet, String statusEn, String statusCn, int statusCode)
	{
		GetStatusMetricsRsp.StatusMetrics metrics = new GetStatusMetricsRsp.StatusMetrics();
		metrics.setCount(0);
		Map<String, String> desc = new HashMap<>();
		desc.put("en", statusEn);
		desc.put("cn", statusCn);
		metrics.setStatus(new StatusCode(statusCode, desc));
		statusMetricsSet.add(metrics);
	}

	public List<TblEdgeComputeInfo> getNodesBySite(String siteId)
	{
		TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
		TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();
		criteria.andSiteIdEqualTo(siteId);
		return tblEdgeComputeInfoMapper.selectByExample(example);
	}
	
	public Map<String, String> getNodeLabels(String nodeId)
	{
		TblEdgeComputeLabelInfoExample example = new TblEdgeComputeLabelInfoExample();
		TblEdgeComputeLabelInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		List<TblEdgeComputeLabelInfo> computeLabelInfos = tblEdgeComputeLabelInfoMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(computeLabelInfos))
		{
			return null;
		}
		
		Map<String ,String> labels = new HashMap<>();
		for (TblEdgeComputeLabelInfo computeLabelInfo : computeLabelInfos)
		{
			labels.put(computeLabelInfo.getLabelKey(), computeLabelInfo.getLabelValue());
		}
		return labels;
	}
	
	public Map<String, String> getNodeTaints(String nodeId)
	{
		TblEdgeComputeTaintInfoExample example = new TblEdgeComputeTaintInfoExample();
		TblEdgeComputeTaintInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		List<TblEdgeComputeTaintInfo> computeLabelInfos = tblEdgeComputeTaintInfoMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(computeLabelInfos))
		{
			return null;
		}
		
		Map<String ,String> taints = new HashMap<>();
		for (TblEdgeComputeTaintInfo computeLabelInfo : computeLabelInfos)
		{
			taints.put(computeLabelInfo.getTaintKey(), computeLabelInfo.getTaintValue());
		}
		return taints;
	}

	public List<EdgeStatusCounts> countByOnlineStatus()
	{
		List<EdgeStatusCounts> edgeStatusCounts = tblEdgeComputeInfoMapper.countByOnlineStatus();
		if (!CollectionUtils.isEmpty(edgeStatusCounts))
		{
			edgeStatusCounts.stream().map(edgeStatusCounts1 -> {
				edgeStatusCounts1.setOnlineStatus(Integer.valueOf(edgeStatusCounts1.getOnlineStatusCode()) == 0 ? "offline" : "online");
				return edgeStatusCounts1;
			});
		}

		return edgeStatusCounts;
	}

	public int updateEdgeComputeLabelByPrimaryKeySelective(TblEdgeComputeLabelInfo record)
	{
		return tblEdgeComputeLabelInfoMapper.updateByPrimaryKeySelective(record);
	}

	public int insertEdgeComputeLabelSelective(TblEdgeComputeLabelInfo record)
	{
		return tblEdgeComputeLabelInfoMapper.insertSelective(record);
	}
}
