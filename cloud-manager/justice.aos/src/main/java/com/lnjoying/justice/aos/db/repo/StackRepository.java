package com.lnjoying.justice.aos.db.repo;

import com.lnjoying.justice.aos.common.SimpleStackStatusEnum;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.mapper.*;
import com.lnjoying.justice.aos.db.model.*;
import com.lnjoying.justice.aos.domain.model.StackDeployInfo;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.entity.scheduler.DstNode;
import com.micro.core.common.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("stackRepository")
@Transactional(rollbackFor = {Exception.class})
public class StackRepository
{
	@Autowired
	TblStackInfoMapper tblStackInfoMapper;

	@Autowired
	TblStackInstInfoMapper tblStackInstInfoMapper;

	@Autowired
	TblStackServiceInfoMapper tblStackServiceInfoMapper;

	@Autowired
	AosOperator aosOperator;

	@Autowired
	TblEdgeComputeInfoOperator tblEdgeComputeInfoOperator;

	@Autowired
	private TblStackDeployInfoMapper tblStackDeployInfoMapper;

	@Autowired
	private TblStackSpecInfoMapper tblStackSpecInfoMapper;

	@Autowired
	private TblCfgdataStackInfoMapper tblCfgdataStackInfoMapper;

	public int insertStack(TblStackInfo tblStackInfo)
	{
		return tblStackInfoMapper.insertSelective(tblStackInfo);
	}

	public int updateStack(TblStackInfo tblStackInfo)
	{
		return tblStackInfoMapper.updateByPrimaryKeySelective(tblStackInfo);
	}

	public int updateStackSelective(TblStackInfo tblStackInfo)
	{
		return tblStackInfoMapper.updateByPrimaryKeySelective(tblStackInfo);
	}

	public int updateStack(TblStackInfo tblStackInfo, TblStackInfoExample example)
	{
		return tblStackInfoMapper.updateByExampleSelective(tblStackInfo, example);
	}


	public int updateStackService(TblStackServiceInfo tblStackServiceInfo, TblStackServiceInfoExample example)
	{
		return tblStackServiceInfoMapper.updateByExampleSelective(tblStackServiceInfo, example);
	}

	public int deleteStack(String stackId)
	{
		return tblStackInfoMapper.deleteByPrimaryKey(stackId);
	}

	public TblStackInfo getStack(String stackId)
	{
		return tblStackInfoMapper.selectByPrimaryKey(stackId);
	}
	
	public List<String> getStack(String specId, String resLevel, String resId, List<Integer> status)
	{
		return aosOperator.getStackInfo(specId, status, resLevel, resId);
	}

	public List<TblStackInfo> getStack(TblStackInfoExample example)
	{
		return tblStackInfoMapper.selectByExample(example);
	}

	public int insertService(TblStackServiceInfo tblStackServiceInfo)
	{
		return tblStackServiceInfoMapper.insert(tblStackServiceInfo);
	}

	public int updateService(TblStackServiceInfo tblStackServiceInfo)
	{
		return tblStackServiceInfoMapper.updateByPrimaryKeySelective(tblStackServiceInfo);
	}

	public int deleteService(String serviceId)
	{
		return tblStackServiceInfoMapper.deleteByPrimaryKey(serviceId);
	}

	public int insertInst(TblStackInstInfo tblStackInstInfo)
	{
		return tblStackInstInfoMapper.insert(tblStackInstInfo);
	}

	public int updateInst(TblStackInstInfo tblStackInstInfo)
	{
		return tblStackInstInfoMapper.updateByPrimaryKeySelective(tblStackInstInfo);
	}

	public int updateInst(TblStackInstInfo tblStackInstInfo, TblStackInstInfoExample example)
	{
		return tblStackInstInfoMapper.updateByExampleSelective(tblStackInstInfo, example);
	}

	public int deleteInst(String instId)
	{
		return tblStackInstInfoMapper.deleteByPrimaryKey(instId);
	}
	
	public void deleteInstByServiceId(String serviceId)
	{
		TblStackInstInfoExample example = new TblStackInstInfoExample();
		TblStackInstInfoExample.Criteria criteria = example.createCriteria();
		criteria.andServiceIdEqualTo(serviceId);
		tblStackInstInfoMapper.deleteByExample(example);
	}

	public TblStackInstInfo getInst(String instId)
	{
		return tblStackInstInfoMapper.selectByPrimaryKey(instId);
	}


	public List<String> getServiceIds(String stackId)
	{
		return aosOperator.getServiceIdsByStackId(stackId);
	}

	public List<String> getInstIds(String serviceId)
	{
		return aosOperator.getInstIdByServiceId(serviceId);
	}

	public long countStack(TblStackInfoExample example)
	{
		return tblStackInfoMapper.countByExample(example);
	}

	public List<TblStackInfo> getStackList(TblStackInfoExample example)
	{
		return tblStackInfoMapper.selectByExample(example);
	}

	public List<TblStackInfo> selectAll(String specId,String name, String bpId, String userId,List<Integer> statusCollection,
								 String region, String site, String nodeId,
								int pageSize, int startRow, List<Integer> notInStatusCollection, String stackType)
	{
		return tblStackInfoMapper.selectAll(specId, name, bpId, userId, statusCollection, region, site, nodeId, pageSize, startRow, notInStatusCollection, stackType);
	}

	public int countAll(String name, String bpId, String userId,List<Integer> statusCollection,
								 String region, String site, String nodeId, List<Integer> notInStatusCollection,String stackType)
	{
		return tblStackInfoMapper.countAll(name, bpId, userId, statusCollection, region, site, nodeId, notInStatusCollection, stackType);
	}

	public TblStackServiceInfo getService(String serviceId)
	{
		return tblStackServiceInfoMapper.selectByPrimaryKey(serviceId);
	}

	public TblStackServiceInfo getService(String serviceName, String stackId)
	{
		TblStackServiceInfoExample example = new TblStackServiceInfoExample();
		TblStackServiceInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(serviceName);
		criteria.andStackIdEqualTo(stackId);
		List<TblStackServiceInfo> tblStackServiceInfoList = tblStackServiceInfoMapper.selectByExample(example);
		if (tblStackServiceInfoList == null || tblStackServiceInfoList.isEmpty())
		{
			return null;
		}
		return tblStackServiceInfoList.get(0);
	}

	public long countService(TblStackServiceInfoExample example)
	{
		return tblStackServiceInfoMapper.countByExample(example);
	}

	public int countAllService(String stackName, String serviceName, String userId,List<Integer> statusCollection,
						String region, String site, String nodeId, List<Integer> notInStatusCollection)
	{
		return tblStackServiceInfoMapper.countAllService(stackName, serviceName, userId, statusCollection, region, site, nodeId, notInStatusCollection);
	}

	public List<TblStackServiceInfo> getServiceList(TblStackServiceInfoExample example)
	{
		return tblStackServiceInfoMapper.selectByExample(example);
	}

	public List<TblStackServiceInfo> selectAllService(String stackName, String serviceName, String userId,List<Integer> statusCollection,
													  String region, String site, String nodeId, int pageSize, int startRow, List<Integer> notInStatusCollection)
	{
		return tblStackServiceInfoMapper.selectAllService(stackName, serviceName, userId, statusCollection, region, site, nodeId, pageSize, startRow, notInStatusCollection);
	}

	public List<TblStackInstInfo> getInstList(TblStackInstInfoExample example)
	{
		return tblStackInstInfoMapper.selectByExample(example);
	}

	public List<TblStackInfo> getStackListByStatus(Integer status)
	{
		TblStackInfoExample example = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		return tblStackInfoMapper.selectByExample(example);
	}

	public List<TblStackInfo> getStackListByStatus(Integer status, Long interval)
	{
		long current = System.currentTimeMillis();
		long lastTime = current - interval*1000;

		TblStackInfoExample example = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(status);
		criteria.andUpdateTimeLessThan(Utils.buildDate(lastTime));
		return tblStackInfoMapper.selectByExample(example);
	}

	public List<String> getStackIdlist(Integer status)
	{
		return aosOperator.getStackIdsByStatus(status);
	}

	public List<String> getInstIdlistLtFailNum(Integer status, String timeParams, Integer failNum)
	{
		return aosOperator.getStackIdsByStatusWithTimeAndLtFailNum(status, timeParams, failNum);
	}

	public List<String> getStackIdlistGteFailNum(Integer status, String timeParams, Integer failNum)
	{
		return aosOperator.getStackIdsByStatusWithTimeAndGteFailNum(status, timeParams, failNum);
	}

	public List<String> getStackIdlist(Integer status, String timeParams)
	{
		return aosOperator.getStackIdsByStatusWithTime(status, timeParams);
	}

	public List<String> getStackIdsLostStatusGTWithUpdateTime(String timeParams, Integer eventType)
	{
		return aosOperator.getStackIdsLostStatusGTWithUpdateTime(timeParams, eventType);
	}

	public List<String> getStackIdsLostStatusGT(String timeParams, Integer eventType)
	{
		return aosOperator.getStackIdsLostStatusGT(timeParams, eventType);
	}

	public List<TblStackServiceInfo> getServiceListByStackId(String stackId)
	{
		TblStackServiceInfoExample example = new TblStackServiceInfoExample();
		TblStackServiceInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStackIdEqualTo(stackId);
		criteria.andStatusNotEqualTo(StackState.REMOVED);
		return tblStackServiceInfoMapper.selectByExample(example);
	}

	public int updateRefIdAndStatusByNode(String nodeId, String refId, Integer status)
	{
		return tblEdgeComputeInfoOperator.updateGpuRefIdAndStatusByNode(nodeId, refId, status);
	}

	public int removeGpuBind(String refId, Integer status)
	{
		return tblEdgeComputeInfoOperator.removeGpuBind(refId, status);
	}

	public int updateEdgeComputeUser(String nodeId, String userId)
	{
		return tblEdgeComputeInfoOperator.updateEdgeComputeUser(nodeId, userId);
	}

    public int countAllDeployments(Integer status, List<Integer> statusExcludeCollection,
								   Collection<Integer> progressingStageCollection, Collection<Integer> availableStageCollection,
								   Collection<Integer> readyStageCollection, Collection<Integer> failedStageCollection,
								   String name, String regionId, String siteId, String nodeId, String bpId, String userId, String stackType)
    {
		return tblStackDeployInfoMapper.countAll(status, statusExcludeCollection,
				progressingStageCollection, availableStageCollection, readyStageCollection, failedStageCollection,
				name, regionId, siteId, nodeId, bpId, userId, stackType);
    }

	public List<StackDeployInfo> selectAllDeployments(Integer status, Collection<Integer> statusExcludeCollection,
													  Collection<Integer> progressingStageCollection, Collection<Integer> availableStageCollection,
													  Collection<Integer> readyStageCollection, Collection<Integer> failedStageCollection,
													  String name, String regionId, String siteId, String nodeId,  String bpId, String userId,
													  int startRow, int pageSize, String stackType)
	{
		return tblStackDeployInfoMapper.selectAll(status, statusExcludeCollection,
				progressingStageCollection, availableStageCollection, readyStageCollection, failedStageCollection,
				name, regionId, siteId, nodeId, bpId, userId, startRow, pageSize, stackType);
	}

	public StackDeployInfo selectDeploymentBySpecId(String specId, Collection<Integer> statusExcludeCollection,
													Collection<Integer> progressingStageCollection, Collection<Integer> availableStageCollection,
													Collection<Integer> readyStageCollection, Collection<Integer> failedStageCollection)
	{
		return tblStackDeployInfoMapper.selectBySpecId(specId, statusExcludeCollection, progressingStageCollection, availableStageCollection, readyStageCollection, failedStageCollection);
	}

	public int insertStackSpecInfo(TblStackSpecInfo tblStackSpecInfo)
	{
		return tblStackSpecInfoMapper.insertSelective(tblStackSpecInfo);
	}

	public TblStackSpecInfo selectStackSpecInfoByPrimaryKey(String specId)
	{
		return tblStackSpecInfoMapper.selectByPrimaryKey(specId);
	}

	public void deleteStackSpecInfoByPrimaryKey(String specId)
	{
		TblStackSpecInfo tblStackSpecInfo = new TblStackSpecInfo();
		tblStackSpecInfo.setSpecId(specId);
		tblStackSpecInfo.setStatus(RecordStatus.DELETED.value());
		tblStackSpecInfoMapper.updateByPrimaryKeySelective(tblStackSpecInfo);
	}
	public int updateSpec(TblStackSpecInfo tblStackSpecInfo)
	{
		return tblStackSpecInfoMapper.updateByPrimaryKeySelective(tblStackSpecInfo);
	}

	public void updateStacksStatus(List<String> stackIds, int status)
	{
		if (null == stackIds || stackIds.isEmpty())
		{
			return;
		}
		TblStackInfo tblStackInfo = new TblStackInfo();
		tblStackInfo.setStatus(status);
		tblStackInfo.setUpdateTime(new Date());
		TblStackInfoExample example = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStackIdIn(stackIds);
		updateStack(tblStackInfo, example);
	}

	public List<String> getSpecIdlist(Integer status)
	{
		return aosOperator.getSpecIdsByStatus(status);
	}
	
	public List<String> getDaemonset(Integer status)
	{
		return aosOperator.getDaemonsetSpecIdsByStatus(status);
	}

	public long countAlwaysOnlineStackSpec()
	{
		return tblStackSpecInfoMapper.countAlwaysOnline();
	}

	public List<TblStackSpecInfo> selectAlwaysOnlineStackSpec(int startRow, int pageSize)
	{
		return tblStackSpecInfoMapper.selectAlwaysOnline(startRow, pageSize);
	}

	public List<TblStackInfo> getCfgStacks(String userId, String dataId, String group, List<Integer> notInStatusCollection)
	{
		return tblCfgdataStackInfoMapper.selectCfgStacks(userId, dataId, group, notInStatusCollection);
	}

	public int insertCfgdataStackInfo(TblCfgdataStackInfo tblCfgdataStackInfo)
	{
		return tblCfgdataStackInfoMapper.insert(tblCfgdataStackInfo);
	}

	public TblCfgdataStackInfo selectCfgdataStackInfo(String cfgVolumeId)
	{
		return tblCfgdataStackInfoMapper.selectByPrimaryKey(cfgVolumeId);
	}

	public List<TblCfgdataStackInfo> selectCfgdataStackInfo(TblCfgdataStackInfoExample example)
	{
		return tblCfgdataStackInfoMapper.selectByExample(example);
	}

	public int updateCfgdataStackInfo(TblCfgdataStackInfo tblCfgdataStackInfo)
	{
		return tblCfgdataStackInfoMapper.updateByPrimaryKeySelective(tblCfgdataStackInfo);
	}

	public int deleteCfgdataStackInfo(String stackId)
	{
		TblCfgdataStackInfoExample example = new TblCfgdataStackInfoExample();
		TblCfgdataStackInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStackIdEqualTo(stackId);
		return tblCfgdataStackInfoMapper.deleteByExample(example);
	}
	
	public Set<String> getDesireDaemonsetSiteIdByspecId(TblStackSpecInfo tblStackSpecInfo)
	{
		if (! CollectionUtils.hasContent(tblStackSpecInfo.getTargetNodes()))
		{
			return null;
		}
		List<TargetNode> targetNodeList = JsonUtils.fromJson(tblStackSpecInfo.getTargetNodes(), new com.google.gson.reflect.TypeToken<List<TargetNode>>(){}.getType());
		
		Set<String> siteList = new HashSet<>();
		for (TargetNode targetNode : targetNodeList)
		{
			siteList.add(targetNode.getDstSiteId());
		}
		return siteList;
	}
	
	public Set<String> getDaemonsetSiteIdByspecId(String specId)
	{
		TblStackInfoExample stackInfoExample = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = stackInfoExample.createCriteria();
		List<Integer> fullStatus = SimpleStackStatusEnum.getFullStatus(SimpleStackStatusEnum.STACK_NOT_EXIST.getCode());
		fullStatus.add(StackState.SPAWNED_CLOUD_REMOVE);
		fullStatus.add( StackState.FIN_CLOUD_REMOVE);

		criteria.andStatusNotIn(fullStatus);
		criteria.andSpecIdEqualTo(specId);
		List<TblStackInfo> stacks = getStack(stackInfoExample);
		
		if (CollectionUtils.isEmpty(stacks))
		{
			return null;
		}
		Set<String> sites = new HashSet<>();
		for (TblStackInfo tblStackInfo : stacks)
		{
			if (! CollectionUtils.hasContent((String)tblStackInfo.getDstNode()))
			{
				continue;
			}
			
			DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);
			sites.add(dstNode.getDstSiteId());
		}
		
		return sites;
	}
	
	public List<String> getDaemonsetNodeIdByspecId(String specId, String siteId)
	{
		//String nodeLike = Utils.buildStr("%",siteId, "%");
		String nodeLike = Utils.buildStr(siteId);

		TblStackInfoExample stackInfoExample = new TblStackInfoExample();
		TblStackInfoExample.Criteria criteria = stackInfoExample.createCriteria();
		criteria.andDstNodeSiteLike(nodeLike);
		List<Integer> fullStatus = SimpleStackStatusEnum.getFullStatus(SimpleStackStatusEnum.STACK_NOT_EXIST.getCode());
		fullStatus.add(StackState.SPAWNED_CLOUD_REMOVE);
		fullStatus.add( StackState.FIN_CLOUD_REMOVE);

		criteria.andStatusNotIn(fullStatus);
		criteria.andSpecIdEqualTo(specId);
		List<TblStackInfo> stacks = getStack(stackInfoExample);
		if (CollectionUtils.isEmpty(stacks))
		{
			return null;
		}
		List<String> nodeList = new ArrayList<>();
		for (TblStackInfo tblStackInfo : stacks)
		{
			DstNode dstNode = JsonUtils.fromJson((String)tblStackInfo.getDstNode(), DstNode.class);
			if (dstNode == null)
			{
				continue;
			}
			nodeList.add(dstNode.getDstNodeId());
		}

		return nodeList;
	}
	
	public void deleteDaemonsetStackOnNode(List<TblStackInfo> stacks)
	{
		if (CollectionUtils.isEmpty(stacks))
		{
			return;
		}
		
		for (TblStackInfo tblStackInfo : stacks)
		{
			List<String> serviceIds = getServiceIds(tblStackInfo.getStackId());
			for (String serviceId : serviceIds)
			{
				//删除掉所有的容器实例
				deleteInstByServiceId(serviceId);
				//删除掉所有的应用服务
				deleteService(serviceId);
			}
			//删除所有的应用
			deleteStack(tblStackInfo.getStackId());
		}
	}
}
