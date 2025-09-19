package com.lnjoying.justice.cis.db.repo;

import com.google.common.collect.Lists;
import com.lnjoying.justice.cis.common.constant.InstAction;
import com.lnjoying.justice.cis.controller.dto.response.ContainerOperatorLog;
import com.lnjoying.justice.cis.controller.dto.response.ContainerRunLog;
import com.lnjoying.justice.cis.controller.dto.response.DockerContainerDeployInfo;
import com.lnjoying.justice.schema.common.ecrm.GpuState;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.db.mapper.*;
import com.lnjoying.justice.cis.db.model.*;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("cisRepository")
@Transactional(rollbackFor = {Exception.class})
public class CisRepository
{
    @Autowired
    TblContainerInstInfoMapper tblContainerInstInfoMapper;

    @Autowired
    TblContainerSpecInfoMapper tblContainerSpecInfoMapper;

    @Autowired
    TblContainerInstInfoOperator tblContainerInstInfoOperator;

    @Autowired
    TblContainerSpecInfoOperator tblContainerSpecInfoOperator;

    @Autowired
    TblEdgeComputeGpuInfoOperator tblEdgeComputeGpuInfoOperator;

    @Autowired
    TblContainerRunInfoOperator tblContainerRunInfoOperator;

    @Autowired
    TblContainerDeployInfoMapper tblContainerDeployInfoMapper;

    @Autowired
    TblContainerRunInfoMapper tblContainerRunInfoMapper;

    @Autowired
    TblContainerOperatorInfoMapper tblContainerOperatorInfoMapper;

    private static final List<Integer> cloudRemoveStatusList = Lists.newArrayList(InstanceState.SPAWNED_CLOUD_REMOVE.getCode(), InstanceState.FIN_CLOUD_REMOVE.getCode());

    public int insertSpec(TblContainerSpecInfo record)
    {
        return tblContainerSpecInfoMapper.insert(record);
    }

    public int updateSpec(TblContainerSpecInfo tblContainerSpecInfo){
        return tblContainerSpecInfoMapper.updateByPrimaryKeySelective(tblContainerSpecInfo);
    }

    public List<String> getSpecIdlist(Integer status)
    {
        return tblContainerSpecInfoOperator.getSpecIdsByStatus(status);
    }

    public List<String> getInstIdlist(Integer status)
    {
        return tblContainerInstInfoOperator.getInstIdsByStatus(status);
    }

    public List<String> getInstIdlistLtFailNum(Integer status, String timeParams, Integer failNum)
    {
        return tblContainerInstInfoOperator.getInstIdsByStatusWithTimeAndLtFailNum(status, timeParams, failNum);
    }

    public List<String> getInstIdlistGteFailNum(Integer status, String timeParams, Integer failNum)
    {
        return tblContainerInstInfoOperator.getInstIdsByStatusWithTimeAndGteFailNum(status, timeParams, failNum);
    }

    public List<String> getInstIdlistByStatusWithTime(Integer status, String timeParams)
    {
        return tblContainerInstInfoOperator.getInstIdsByStatusWithTime(status, timeParams);
    }

    public List<String> getInstIdsLostStatusGTWithUpdateTime(String timeParams, Integer eventType)
    {
        return tblContainerInstInfoOperator.getInstIdsLostStatusGTWithUpdateTime(timeParams, eventType);
    }

    public List<String> getInstIdsLostStatusGT(String timeParams, Integer eventType)
    {
        return tblContainerInstInfoOperator.getInstIdsLostStatusGT(timeParams, eventType);
    }

    public TblContainerSpecInfo getSpec(String specId)
    {
        return tblContainerSpecInfoMapper.selectByPrimaryKey(specId);
    }

    public long countSpecByExample(TblContainerSpecInfoExample example)
    {
        return tblContainerSpecInfoMapper.countByExample(example);
    }

    public List<TblContainerSpecInfo> selectSpecByExample(TblContainerSpecInfoExample example)
    {
        return tblContainerSpecInfoMapper.selectByExample(example);
    }

    public List<TblContainerInstInfo> getContainerInstInfosByExample(TblContainerInstInfoExample example)
    {
        return tblContainerInstInfoMapper.selectByExample(example);
    }

    public long countByExample(TblContainerInstInfoExample example)
    {
        return tblContainerInstInfoMapper.countByExample(example);
    }

    public int deleteByExample(TblContainerInstInfoExample example)
    {
        return tblContainerInstInfoMapper.deleteByExample(example);
    }

    public int deleteByPrimaryKey(String instId)
    {
        return tblContainerInstInfoMapper.deleteByPrimaryKey(instId);
    }

    public int insertInst(TblContainerInstInfo record)
    {
        return tblContainerInstInfoMapper.insert(record);
    }

    public int insertSelective(TblContainerInstInfo record)
    {
        return tblContainerInstInfoMapper.insertSelective(record);
    }

    public List<TblContainerInstInfo> selectByExample(TblContainerInstInfoExample example)
    {
        return tblContainerInstInfoMapper.selectByExample(example);
    }

    public TblContainerInstInfo selectByPrimaryKey(String instId)
    {
        return tblContainerInstInfoMapper.selectByPrimaryKey(instId);
    }

    public TblContainerInstInfo selectByInstIdOrRefId(String uniqueId)
    {
        return tblContainerInstInfoMapper.selectByInstIdOrRefId(uniqueId);
    }

    public int updateByExampleSelective(TblContainerInstInfo record, TblContainerInstInfoExample example)
    {
        return tblContainerInstInfoMapper.updateByExampleSelective(record, example);
    }

    public int updateByExample(TblContainerInstInfo record, TblContainerInstInfoExample example)
    {
        return tblContainerInstInfoMapper.updateByExample(record, example);
    }

    public int updateByPrimaryKeySelective(TblContainerInstInfo record)
    {
        return tblContainerInstInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblContainerInstInfo record)
    {
        return tblContainerInstInfoMapper.updateByPrimaryKey(record);
    }

    public List<TblContainerInstInfo> selectByStatus(Integer status)
    {
        return tblContainerInstInfoOperator.getInstByStatus(status);
    }

    public TblContainerInstInfoExample buildEvacuateInstByNodeIdExample(String nodeId)
    {
        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        criteria.andStatusNotIn(cloudRemoveStatusList);
        return example;
    }

    public List<TblEdgeComputeGpuInfo> selectBindGpuByInstId(String instId)
    {
        return tblEdgeComputeGpuInfoOperator.selectBindGpuByRefId(instId, GpuState.CONTAINER_USE.code);
    }

    public List<TblEdgeComputeGpuInfo> selectFreeGpuByNodeId(String nodeId)
    {
        return tblEdgeComputeGpuInfoOperator.selectGpuByNodeIdAndStatus(nodeId, GpuState.FREE.code);
    }

    public List<TblEdgeComputeGpuInfo> selectGpuByNodeId(String nodeId)
    {
        return tblEdgeComputeGpuInfoOperator.selectGpuByNodeId(nodeId);
    }

    public int updateRefIdAndStatus(String gpuId, String refId, Integer status)
    {
        return tblEdgeComputeGpuInfoOperator.updateRefIdAndStatus(gpuId, refId, status);
    }

    public int removeGpuBind(String refId, Integer status)
    {
        return tblEdgeComputeGpuInfoOperator.removeGpuBind(refId, status);
    }

    public int insertRunInfo(TblContainerRunInfo record)
    {
        return tblContainerRunInfoMapper.insert(record);
    }

    public TblContainerRunInfo getInstLatestRunInfo(String instId)
    {
        return tblContainerRunInfoOperator.getInstLatestRunInfo(instId);
    }

    public int updateRunInfoByPrimaryKey(TblContainerRunInfo record)
    {
        return tblContainerRunInfoMapper.updateByPrimaryKey(record);
    }

    public void updateInstsStatus(List<String> instIds, int status)
    {
        if (null == instIds || instIds.isEmpty())
        {
            return;
        }
        TblContainerInstInfo tblContainerInstInfo = new TblContainerInstInfo();
        tblContainerInstInfo.setStatus(status);
        tblContainerInstInfo.setUpdateTime(new Date());
        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andInstIdIn(instIds);
        updateByExampleSelective(tblContainerInstInfo, example);
    }

    public TblContainerInstInfoExample buildUpdateContainerInstInfoByInstIdListExample(List<String> instIds)
    {
        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        criteria.andInstIdIn(instIds);
        return example;
    }

    public List<DockerContainerDeployInfo> selectAllDeployments(Integer status, Collection<Integer> statusExcludeCollection,
                                                                Collection<Integer> progressingStageCollection, Collection<Integer> availableStageCollection,
                                                                Collection<Integer> readyStageCollection, Collection<Integer> failedStageCollection,
                                                                String name, String regionId, String siteId, String nodeId, String userId, String bpId,
                                    int startRow, int pageSize)
    {
        return tblContainerDeployInfoMapper.selectAll(status, statusExcludeCollection,
                progressingStageCollection, availableStageCollection, readyStageCollection, failedStageCollection,
                name, regionId, siteId, nodeId, userId, bpId, startRow, pageSize);
    }

    public int countAllDeployments(Integer status, Collection<Integer> statusExcludeCollection,
                                   Collection<Integer> progressingStageCollection, Collection<Integer> availableStageCollection,
                                   Collection<Integer> readyStageCollection, Collection<Integer> failedStageCollection,
                                   String name, String regionId, String siteId, String nodeId, String userId, String bpId)
    {
        return tblContainerDeployInfoMapper.countAll(status, statusExcludeCollection,
                progressingStageCollection, availableStageCollection, readyStageCollection, failedStageCollection,
                name, regionId, siteId, nodeId, userId, bpId);
    }

    public DockerContainerDeployInfo selectDeploymentBySpecId(String specId, Collection<Integer> statusExcludeCollection,
                                                              Collection<Integer> progressingStageCollection, Collection<Integer> availableStageCollection,
                                                              Collection<Integer> readyStageCollection, Collection<Integer> failedStageCollection)
    {
        return tblContainerDeployInfoMapper.selectBySpecId(specId, statusExcludeCollection, progressingStageCollection, availableStageCollection, readyStageCollection, failedStageCollection);
    }

    public void deleteContainerSpecInfoByPrimaryKey(String specId)
    {
        TblContainerSpecInfo tblContainerSpecInfo = new TblContainerSpecInfo();
        tblContainerSpecInfo.setSpecId(specId);
        tblContainerSpecInfo.setStatus(RecordStatus.DELETED.value());
        tblContainerSpecInfoMapper.updateByPrimaryKeySelective(tblContainerSpecInfo);
    }

    public List<ContainerOperatorLog> getContainerInstOperLogs(String instanceId)
    {
        List<ContainerOperatorLog> containerOperatorLogs = new ArrayList<>();
        TblContainerOperatorInfoExample example = new TblContainerOperatorInfoExample();
        TblContainerOperatorInfoExample.Criteria criteria = example.createCriteria();
        criteria.andInstIdEqualTo(instanceId);
        example.setOrderByClause("oper_time desc");
        List<TblContainerOperatorInfo> tblContainerOperatorInfos = tblContainerOperatorInfoMapper.selectByExample(example);
        for (TblContainerOperatorInfo tblContainerOperatorInfo : tblContainerOperatorInfos)
        {
            ContainerOperatorLog containerOperatorLog = new ContainerOperatorLog();
            containerOperatorLog.setOperId(tblContainerOperatorInfo.getOperId());
            containerOperatorLog.setOperType(InstAction.fromName(tblContainerOperatorInfo.getOperType()).toOperType());
            containerOperatorLog.setOperTime(Utils.formatDate(tblContainerOperatorInfo.getOperTime()));
            containerOperatorLog.setOperatorId(tblContainerOperatorInfo.getOperatorId());
            containerOperatorLog.setOperatorName(tblContainerOperatorInfo.getOperatorName());
            containerOperatorLog.setOperResult(tblContainerOperatorInfo.getOperResult());
            containerOperatorLogs.add(containerOperatorLog);
        }
        return containerOperatorLogs;
    }

    public List<ContainerRunLog> getContainerInstRunLogs(String instanceId)
    {
        List<ContainerRunLog> containerRunLogs = new ArrayList<>();
        TblContainerRunInfoExample example = new TblContainerRunInfoExample();
        TblContainerRunInfoExample.Criteria criteria = example.createCriteria();
        criteria.andInstIdEqualTo(instanceId);
        example.setOrderByClause("start_time desc");
        List<TblContainerRunInfo> tblContainerRunInfos = tblContainerRunInfoMapper.selectByExample(example);
        for (TblContainerRunInfo tblContainerRunInfo : tblContainerRunInfos)
        {
            ContainerRunLog containerRunLog = new ContainerRunLog();
            containerRunLog.setStartTime(Utils.formatDate(tblContainerRunInfo.getStartTime()));
            containerRunLog.setStopTime(Utils.formatDate(tblContainerRunInfo.getStopTime()));
            containerRunLog.setState(InstanceState.fromCode(tblContainerRunInfo.getState()).toStatusCode());
            containerRunLogs.add(containerRunLog);
        }
        return containerRunLogs;
    }

    public int insertOperatorInfo(TblContainerOperatorInfo record)
    {
        return tblContainerOperatorInfoMapper.insert(record);
    }


    public GetStatusMetricsRsp getContainerInstanceStatusMetrics(String bpId, String userId)
    {
        GetStatusMetricsRsp getStatusMetricsRsp = new GetStatusMetricsRsp();
        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andUserIdEqualTo(userId);
        }
        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpIdEqualTo(bpId);
        }

        criteria.andStatusNotIn(cloudRemoveStatusList);
        int totalNum = (int)tblContainerInstInfoMapper.countByExample(example);
        getStatusMetricsRsp.setTotalNum(totalNum);
        getStatusMetricsRsp.setStatusMetrics(new ArrayList<>());
        List<StatusMetrics> containerInstanceStatusMetrics = tblContainerInstInfoMapper.getContainerInstanceStatusMetrics(cloudRemoveStatusList, bpId, userId);

        Set<GetStatusMetricsRsp.StatusMetrics> statusMetricsSet = new HashSet<>();
        for (StatusMetrics statusMetrics : containerInstanceStatusMetrics)
        {
            InstanceState instanceState = InstanceState.fromCode(Integer.valueOf(statusMetrics.getStatusStr()));
            GetStatusMetricsRsp.StatusMetrics metrics =  new GetStatusMetricsRsp.StatusMetrics();
            metrics.setCount(statusMetrics.getCount());
            Map<String, String> desc = new HashMap<>();
            desc.put("cn", instanceState.getDesc().get("cn"));
            desc.put("en", instanceState.getDesc().get("en"));
            StatusCode statusCode = new StatusCode(instanceState.getCode(), desc);
            metrics.setStatus(statusCode);
            statusMetricsSet.add(metrics);

        }

        for (InstanceState current : InstanceState.values())
        {
            GetStatusMetricsRsp.StatusMetrics metrics =  new GetStatusMetricsRsp.StatusMetrics();
            metrics.setCount(0);
            Map<String, String> desc = new HashMap<>();
            desc.put("cn", current.getDesc().get("cn"));
            desc.put("en", current.getDesc().get("en"));
            StatusCode statusCode = new StatusCode(current.getCode(), desc);
            metrics.setStatus(statusCode);
            statusMetricsSet.add(metrics);
        }

        getStatusMetricsRsp.getStatusMetrics().addAll(Lists.newArrayList(statusMetricsSet));
        return getStatusMetricsRsp;
    }
}
