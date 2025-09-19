package com.lnjoying.justice.cmp.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.domain.dto.response.CloudBasicInfoListRsp;
import com.lnjoying.justice.cmp.domain.dto.response.CloudInfoListRsp;
import com.lnjoying.justice.cmp.domain.model.*;
import com.lnjoying.justice.cmp.service.nextstack.vm.PciDeviceServiceBiz;
import com.lnjoying.justice.cmp.service.nextstack.vm.VmInstanceServiceBiz;
import com.lnjoying.justice.cmp.service.openstack.ServerService;
import com.lnjoying.justice.cmp.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.swagger.ScbSchemaUtils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import com.lnjoying.justice.schema.service.cmp.CloudManagerService;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Repository("cloudRepository")
@Transactional(rollbackFor = {Exception.class})
public class CloudRepository
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	private TblCloudInfoMapper tblCloudInfoMapper;

	@Autowired
	private TblCloudAgentInfoMapper tblCloudAgentInfoMapper;

	@Autowired
	private ManualMapper manualMapper;

	@Autowired
	private TblCmpHypervisorNodeMapper tblHypervisorNodeMapper;

	@Autowired
	private TblCmpBaremetalDeviceMapper tblCmpBaremetalDeviceMapper;

	@Autowired
	private TblCmpBaremetalInstanceMapper tblCmpBaremetalInstanceMapper;

	@Autowired
	private TblCmpVmInstanceMapper tblVmInstanceMapper;

	@Autowired
	private TblCmpOsInstancesMapper tblCmpOsInstancesMapper;

	@Autowired
	private CombRpcService combRpcService;

	@Autowired
	private PciDeviceServiceBiz pciDeviceService;

	@Autowired
	private VmInstanceServiceBiz vmComputeService;

	@Autowired
	private ServerService serverService;

	public int insertCloud(TblCloudInfo tblCloudInfo)
	{
		return tblCloudInfoMapper.insert(tblCloudInfo);
	}

	public int updateCloud(TblCloudInfo tblCloudInfo)
	{
		return tblCloudInfoMapper.updateByPrimaryKey(tblCloudInfo);
	}

	public int updateCloudSelective(TblCloudInfo tblCloudInfo)
	{
		return tblCloudInfoMapper.updateByPrimaryKeySelective(tblCloudInfo);
	}

	public TblCloudInfo getCloud(String cloudId)
	{
		return tblCloudInfoMapper.selectByPrimaryKey(cloudId);
	}

	public List<TblCloudInfo> getCloudsByExample(TblCloudInfoExample example)
	{
		return tblCloudInfoMapper.selectByExample(example);
	}

	public CloudInfoListRsp getClouds(CloudSearchCritical critical)
	{
		PageHelper.startPage(critical.getPageNum(), critical.getPageSize());
		try
		{
			TblCloudInfoExample example = new TblCloudInfoExample();
			example.setOrderByClause("create_time desc");
			TblCloudInfoExample.Criteria criteria = example.createCriteria();

			if (CollectionUtils.hasContent(critical.getName()))
			{
				criteria.andNameLike("%" + critical.getName() + "%");
			}

			if (critical.getStatus() != null)
			{
				criteria.andStatusNotEqualTo(critical.getStatus());
			}
			else
			{
				criteria.andStatusNotEqualTo(CloudStatus.REMOVED.getCode());
			}

			if (CollectionUtils.hasContent(critical.getRegion()))
			{
				criteria.andRegionIdEqualTo(critical.getRegion());
			}

			if (CollectionUtils.hasContent(critical.getSite()))
			{
				criteria.andSiteIdEqualTo(critical.getSite());
			}

			if (CollectionUtils.hasContent(critical.getProduct()))
			{
				criteria.andProductEqualTo(critical.getProduct());
			}

			if (CollectionUtils.hasContent(critical.getOwner()))
			{
				criteria.andOwnerEqualTo(critical.getOwner());
			}

			if (CollectionUtils.hasContent(critical.getBp()))
			{
				criteria.andBpEqualTo(critical.getBp());
			}

			PageHelper.startPage(critical.getPageNum(), critical.getPageSize());
			List<TblCloudInfo> tblCloudInfoList = tblCloudInfoMapper.selectByExample(example);
			PageInfo<TblCloudInfo> pageInfo = new PageInfo<>(tblCloudInfoList);

			List<CloudInfo> clouds = tblCloudInfoList.stream().map(CloudInfo::of).collect(Collectors.toList());

			for (CloudInfo cloudInfo : clouds)
			{
				assembleNameInfo(cloudInfo);

				String status = RedisUtil.get(RedisCache.CLOUD_HEALTH_STATUS + cloudInfo.getCloudId());
				if (!StringUtils.isNumeric(status))
				{
					cloudInfo.setHealthStatus(HealthStatus.UNKNOWN.toStatusCode());
				}
				else
				{
					cloudInfo.setHealthStatus(HealthStatus.fromCode(Integer.parseInt(status)).toStatusCode());
				}
				cloudInfo.setMetrics(getCloudSystemMetrics(cloudInfo.getCloudId(), cloudInfo.getProduct()));
				if (cloudInfo.getAuthorization().getAccessKey() != null)
				{
					cloudInfo.getAuthorization().getAccessKey().setSecret(null);
				}
				else if (cloudInfo.getAuthorization().getPassword() != null)
				{
					cloudInfo.getAuthorization().getPassword().setPassword(null);
				}
				cloudInfo.setUsage(pciDeviceService.getCloudGPUUsage(cloudInfo.getCloudId()));
			}
			return CloudInfoListRsp.builder().totalNum(pageInfo.getTotal()).clouds(clouds).build();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public CloudBasicInfoListRsp getCloudsBasicInfo(String userId)
	{
		try
		{
			TblCloudInfoExample example = new TblCloudInfoExample();
			example.setOrderByClause("create_time desc");
			TblCloudInfoExample.Criteria criteria = example.createCriteria();

			if (isAdmin())
			{
				criteria.andStatusNotEqualTo(CloudStatus.REMOVED.getCode());
			}
			else
			{
				List<String> roles = combRpcService.getUmsService().getRolesByUserIdAndServiceCode(userId, ScbSchemaUtils.getMicrosoftServiceName());
				List<Integer> availableStatus = new ArrayList<>();
				availableStatus.add(CloudStatus.ON_SHELF.getCode());
				availableStatus.add(CloudStatus.MAINTAINING.getCode());
				availableStatus.add(CloudStatus.MAINTAIN_FILED.getCode());
				availableStatus.add(CloudStatus.PRE_OFF_SHELF.getCode());
				if (roles.contains(CmpRole.INTERNAL_TEST))
				{
					availableStatus.add(CloudStatus.INTERNAL_TEST.getCode());
				}
				if (roles.contains(CmpRole.PUBLIC_TEST))
				{
					availableStatus.add(CloudStatus.PUBLIC_TEST.getCode());
				}
				criteria.andStatusIn(availableStatus);
			}

			List<TblCloudInfo> tblCloudInfoList = tblCloudInfoMapper.selectByExample(example);


			List<CloudBasicInfo> clouds = tblCloudInfoList.stream().map(CloudBasicInfo::of).collect(Collectors.toList());

			if (CollectionUtils.isEmpty(clouds))
			{
				return CloudBasicInfoListRsp.builder().totalNum(0).clouds(new ArrayList<>()).build();
			}

			Map<String, UserResourceMetrics> userMetrics;
			boolean updateCache = false;
			String userMetricsStr = RedisUtil.get(RedisCache.USER_METRICS + userId);
			if (userMetricsStr != null)
			{
				userMetrics = JsonUtils.fromJson(userMetricsStr, new TypeToken<Map<String, UserResourceMetrics>>(){}.getType());
			}
			else
			{
				userMetrics = new HashMap<>();
			}

			for (CloudBasicInfo cloudBasicInfo : clouds)
			{
				cloudBasicInfo.setUsage(pciDeviceService.getCloudGPUUsage(cloudBasicInfo.getCloudId()));
				UserResourceMetrics userResourceMetrics = userMetrics.getOrDefault(cloudBasicInfo.getCloudId(), null);
				if (userResourceMetrics == null)
				{
					userResourceMetrics = getUserResourceMetrics(userMetrics, cloudBasicInfo.getCloudId(), cloudBasicInfo.getProduct(), userId);
					updateCache = true;
				}
				cloudBasicInfo.setUserResourceMetrics(userResourceMetrics);
			}

			clouds.removeIf(cloudBasicInfo -> (! isAdmin() && cloudBasicInfo.getStatus().getCode() == CloudStatus.PRE_OFF_SHELF.getCode() &&
					(cloudBasicInfo.getUserResourceMetrics() == null || cloudBasicInfo.getUserResourceMetrics().getVmNum() == null ||
							cloudBasicInfo.getUserResourceMetrics().getVmNum() == 0)));

			if (updateCache)
			{
				RedisUtil.set(RedisCache.USER_METRICS + userId, JsonUtils.toJson(userMetrics), 5 * 60);
			}

			return CloudBasicInfoListRsp.builder().totalNum(clouds.size()).clouds(clouds).build();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void assembleNameInfo(CloudInfo cloudInfo)
	{
		String bpId = cloudInfo.getBpId();
		String bpName = "";
		if (StringUtils.isNotBlank(bpId))
		{
			try
			{
				bpName = combRpcService.getUmsService().getBpNameByBpId(bpId);
			}
			catch (Exception e)
			{
				LOGGER.error("get bp name failed");
			}
		}
		cloudInfo.setBpName(bpName);

		String userId = cloudInfo.getUserId();
		String userName = "";
		if (StringUtils.isNotBlank(userId))
		{
			try
			{
				userName = combRpcService.getUmsService().getUserNameByUserId(userId);
			}
			catch (Exception e)
			{
				LOGGER.error("get user name failed");
			}
		}
		cloudInfo.setUserName(userName);

		String regionId = cloudInfo.getRegionId();
		String regionName = "";
		if (StringUtils.isNotBlank(regionId))
		{
			try
			{
				regionName = combRpcService.getRegionResourceService().getRegionNameById(regionId);
			}
			catch (Exception e)
			{
				LOGGER.error("get region name failed");
			}
		}
		cloudInfo.setRegionName(regionName);

		String siteId = cloudInfo.getSiteId();
		String siteName = "";
		if (StringUtils.isNotBlank(siteId))
		{
			try
			{
				siteName = combRpcService.getRegionResourceService().getSiteNameById(siteId);
			}
			catch (Exception e)
			{
				LOGGER.error("get site name failed");
			}
		}
		cloudInfo.setSiteName(siteName);

		String nodeId = cloudInfo.getNodeId();
		String nodeName = "";
		if (StringUtils.isNotBlank(nodeId))
		{
			try
			{
				nodeName = combRpcService.getRegionResourceService().getNodeNameById(nodeId);
			}
			catch (Exception e)
			{
				LOGGER.error("get node name failed");
			}
		}
		cloudInfo.setNodeName(nodeName);
	}

	public long countByExample(TblCloudInfoExample example)
	{
		return tblCloudInfoMapper.countByExample(example);
	}

	public int insertCloudAgent(TblCloudAgentInfo tblCloudAgentInfo)
	{
		return tblCloudAgentInfoMapper.insert(tblCloudAgentInfo);
	}

	public long countAgentsByExample(TblCloudAgentInfoExample example)
	{
		return tblCloudAgentInfoMapper.countByExample(example);
	}

	public List<TblCloudAgentInfo> getCloudAgents(TblCloudAgentInfoExample example)
	{
		List<TblCloudAgentInfo> tblCloudAgentInfos = tblCloudAgentInfoMapper.selectByExample(example);
		return tblCloudAgentInfos;
	}

	public TblCloudAgentInfo getCloudAgentByPrimaryKey(String agentId)
	{
		return tblCloudAgentInfoMapper.selectByPrimaryKey(agentId);
	}

	public int updateCloudAgentInfo(TblCloudAgentInfo tblCloudAgentInfo)
	{
		return tblCloudAgentInfoMapper.updateByPrimaryKeySelective(tblCloudAgentInfo);
	}

	public CloudSystemMetrics getCloudSystemMetrics(String cloudId, String product)
	{
		CloudSystemMetrics cloudSystemMetrics = new CloudSystemMetrics();

		if (product.equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
		{
			TblCmpBaremetalDeviceExample tblCmpBaremetalDeviceExample = new TblCmpBaremetalDeviceExample();
			TblCmpBaremetalDeviceExample.Criteria baremetalDeviceCriteria = tblCmpBaremetalDeviceExample.createCriteria();
			baremetalDeviceCriteria.andCloudIdEqualTo(cloudId);
			baremetalDeviceCriteria.andEeStatusNotEqualTo(REMOVED);
			cloudSystemMetrics.setBaremetalDeviceNum((int)tblCmpBaremetalDeviceMapper.countByExample(tblCmpBaremetalDeviceExample));

			TblCmpBaremetalInstanceExample tblCmpBaremetalInstanceExample = new TblCmpBaremetalInstanceExample();
			TblCmpBaremetalInstanceExample.Criteria baremetalInstanceCriteria = tblCmpBaremetalInstanceExample.createCriteria();
			baremetalInstanceCriteria.andCloudIdEqualTo(cloudId);
			baremetalInstanceCriteria.andEeStatusNotEqualTo(REMOVED);
			baremetalInstanceCriteria.andPhaseStatusNotEqualTo(REMOVED);
			cloudSystemMetrics.setBaremetalInstanceNum((int)tblCmpBaremetalInstanceMapper.countByExample(tblCmpBaremetalInstanceExample));

			TblCmpHypervisorNodeExample tblCmpHypervisorNodeExample = new TblCmpHypervisorNodeExample();
			TblCmpHypervisorNodeExample.Criteria hypervisorNodeCriteria = tblCmpHypervisorNodeExample.createCriteria();
			hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
			hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
			cloudSystemMetrics.setHypervisorNodeNum((int)tblHypervisorNodeMapper.countByExample(tblCmpHypervisorNodeExample));

			TblCmpVmInstanceExample tblCmpVmInstanceExample = new TblCmpVmInstanceExample();
			TblCmpVmInstanceExample.Criteria vmInstanceCriteria = tblCmpVmInstanceExample.createCriteria();
			vmInstanceCriteria.andCloudIdEqualTo(cloudId);
			vmInstanceCriteria.andEeStatusNotEqualTo(REMOVED);
			vmInstanceCriteria.andPhaseStatusNotEqualTo(REMOVED);
			cloudSystemMetrics.setVmInstanceNum((int)tblVmInstanceMapper.countByExample(tblCmpVmInstanceExample));
		}
		else if (product.equalsIgnoreCase(CloudProduct.OPENSTACK.getName()) || product.equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
		{
			TblCmpOsInstancesExample tblCmpOsInstancesExample = new TblCmpOsInstancesExample();
			TblCmpOsInstancesExample.Criteria tblCmpOsInstancesCriteria = tblCmpOsInstancesExample.createCriteria();
			tblCmpOsInstancesCriteria.andCloudIdEqualTo(cloudId);
			tblCmpOsInstancesCriteria.andEeStatusNotEqualTo(REMOVED);
			cloudSystemMetrics.setVmInstanceNum((int)tblCmpOsInstancesMapper.countByExample(tblCmpOsInstancesExample));
		}

		return cloudSystemMetrics;
	}

	public GetStatusMetricsRsp getCloudStatusMetrics(String userId, String bpId)
	{
		GetStatusMetricsRsp getStatusMetricsRsp = new GetStatusMetricsRsp();
		TblCloudInfoExample example = new TblCloudInfoExample();
		TblCloudInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(CloudStatus.REMOVED.getCode());
		if (StringUtils.isNotEmpty(userId))
		{
			criteria.andOwnerEqualTo(userId);
		}
		if (StringUtils.isNotEmpty(bpId))
		{
			criteria.andBpEqualTo(bpId);
		}
		getStatusMetricsRsp.setTotalNum((int)countByExample(example));
		getStatusMetricsRsp.setStatusMetrics(new ArrayList<>());
		List<StatusMetrics> statusMetricsList = tblCloudInfoMapper.getCloudStatusMetrics(userId, bpId);

		for (CloudStatus current : CloudStatus.values())
		{
			if (current.equals(CloudStatus.REMOVED)) continue;
			GetStatusMetricsRsp.StatusMetrics metrics =  new GetStatusMetricsRsp.StatusMetrics();
			metrics.setCount(0);
			metrics.setStatus(current.toStatusCode());
			getStatusMetricsRsp.getStatusMetrics().add(metrics);
		}

		for (StatusMetrics statusMetrics : statusMetricsList)
		{
			GetStatusMetricsRsp.StatusMetrics metrics =  new GetStatusMetricsRsp.StatusMetrics();
			metrics.setCount(statusMetrics.getCount());
			metrics.setStatus(CloudStatus.fromCode(statusMetrics.getStatus()).toStatusCode());
			getStatusMetricsRsp.getStatusMetrics().add(metrics);
		}
		return getStatusMetricsRsp;
	}

	public int getVmNum(String filter, String userId, String bpId)
	{
		int status = 0;
		switch (filter.toLowerCase(Locale.ROOT))
		{
			case "running":
				status = VmInstanceStatus.INSTANCE_RUNNING;
		}

		return manualMapper.getVmNum(status, userId, bpId);
	}

	public int getBaremetalNum(String filter, String userId, String bpId)
	{
		int status = 0;
		switch (filter.toLowerCase(Locale.ROOT))
		{
			case "running":
				status = BaremetalInstanceStatus.INSTANCE_RUNNING;
		}

		return manualMapper.getBaremetalNum(status, userId, bpId);
	}

	public String selectWorkerIdByCloudId(String cloudId)
	{
		Assert.hasText(cloudId, "The given cloud id must not be null!");
		return tblCloudAgentInfoMapper.selectWorkerIdByCloudId(cloudId);
	}

	public UserResourceMetrics getUserResourceMetrics(Map<String, UserResourceMetrics> userMetrics, String cloudId, String product, String userId)
	{
		UserResourceMetrics userResourceMetrics = null;
		if (userMetrics == null)
		{
			userMetrics = new HashMap<>();
		}
		else
		{
			userResourceMetrics = userMetrics.getOrDefault(cloudId, null);
			if (userResourceMetrics != null)
			{
				return userResourceMetrics;
			}
		}

		if (product.equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
		{
			userResourceMetrics = vmComputeService.getUserResourceMetrics(cloudId, userId);
		}
		else if (product.equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || product.equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
		{
			userResourceMetrics = serverService.getUserResourceMetrics(cloudId, userId);
		}
		if (userResourceMetrics != null)
		{
			userMetrics.put(cloudId, userResourceMetrics);
		}

		return userResourceMetrics;
	}

	public CloudManagerService.RpcCloudInfos getClouds(Integer pageNum, Integer pageSize)
	{
		TblCloudInfoExample example = new TblCloudInfoExample();
		TblCloudInfoExample.Criteria criteria = example.createCriteria();
		example.setOrderByClause("create_time desc");
		criteria.andStatusNotEqualTo(CloudStatus.REMOVED.getCode());

		PageHelper.startPage(pageNum, pageSize);
		List<TblCloudInfo> tblCloudInfoList = tblCloudInfoMapper.selectByExample(example);
		PageInfo<TblCloudInfo> pageInfo = new PageInfo<>(tblCloudInfoList);

		List<CloudManagerService.RpcCloudInfo> clouds = tblCloudInfoList.stream().map(tblCloudInfo -> new CloudManagerService.RpcCloudInfo(tblCloudInfo.getCloudId(), tblCloudInfo.getName())).collect(Collectors.toList());

		return CloudManagerService.RpcCloudInfos.builder().totalNum(pageInfo.getTotal()).clouds(clouds).build();
	}

	public CloudBasicInfoListRsp getMarketCloudsBasicInfo()
	{
		try
		{
			TblCloudInfoExample example = new TblCloudInfoExample();
			example.setOrderByClause("create_time desc");
			TblCloudInfoExample.Criteria criteria = example.createCriteria();

			List<Integer> availableStatus = new ArrayList<>();
			availableStatus.add(CloudStatus.ON_SHELF.getCode());
			availableStatus.add(CloudStatus.MAINTAINING.getCode());
			availableStatus.add(CloudStatus.MAINTAIN_FILED.getCode());
			criteria.andStatusIn(availableStatus);

			List<TblCloudInfo> tblCloudInfoList = tblCloudInfoMapper.selectByExample(example);

			List<CloudBasicInfo> clouds = tblCloudInfoList.stream().map(CloudBasicInfo::of).collect(Collectors.toList());

			if (CollectionUtils.isEmpty(clouds))
			{
				return CloudBasicInfoListRsp.builder().totalNum(0).clouds(new ArrayList<>()).build();
			}

			return CloudBasicInfoListRsp.builder().totalNum(clouds.size()).clouds(clouds).build();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getMarketCloudsIds()
	{
		try
		{
			TblCloudInfoExample example = new TblCloudInfoExample();

			TblCloudInfoExample.Criteria criteria = example.createCriteria();

			List<Integer> availableStatus = new ArrayList<>();
			availableStatus.add(CloudStatus.ON_SHELF.getCode());
			availableStatus.add(CloudStatus.MAINTAINING.getCode());
			availableStatus.add(CloudStatus.MAINTAIN_FILED.getCode());
			criteria.andStatusIn(availableStatus);

			List<TblCloudInfo> tblCloudInfoList = tblCloudInfoMapper.selectByExample(example);

			List<String> cloudIds = tblCloudInfoList.stream().map(TblCloudInfo::getCloudId).collect(Collectors.toList());

			return cloudIds;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
}
