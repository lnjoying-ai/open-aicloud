package com.lnjoying.justice.scheduler.scheduler.framework.noderesources;

import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.scheduler.common.constant.*;
import com.lnjoying.justice.scheduler.db.mapper.*;
import com.lnjoying.justice.scheduler.db.model.*;
import com.lnjoying.justice.scheduler.domain.model.*;
import com.lnjoying.justice.schema.common.ecrm.GpuState;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ResourcesUtil
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    TblEdgeComputeInfoMapper tblEdgeComputeInfoMapper;

    @Autowired
    TblEdgeComputeGpuInfoMapper tblEdgeComputeGpuInfoMapper;

    @Autowired
    ViewCompleteContainerInstInfoMapper viewCompleteContainerInstInfoMapper;

    @Autowired
    TblSchedEdgeMonopolyMapper tblSchedEdgeMonopolyMapper;

    @Autowired
    StackOperator stackOperator;

    private static final List<Integer> notOccupyResourcesStatusList = Lists.newArrayList(InstanceState.EDGE_UNREACHABLE,
            InstanceState.FIN_CLOUD_REMOVE, InstanceState.CREATE_FAILED, InstanceState.REMOVED, InstanceState.CLOUD_CRETAE_FAILED_PARAMS,
            InstanceState.CLOUD_CRETAE_FAILED_OVERDUE, InstanceState.HARDWARE_ERROR, InstanceState.NO_IMAGE, InstanceState.IMAGE_DOWNLOAD_FAILED,
            InstanceState.OBJECT_NOT_EXIST, InstanceState.OBJECT_AUTO_REMOVE);

    public NodeResourcesInfo getResources(String nodeId){
        TblEdgeComputeInfo tblEdgeComputeInfo = tblEdgeComputeInfoMapper.selectByPrimaryKey(nodeId);
        if (null == tblEdgeComputeInfo)
        {
            return null;
        }
        if (tblEdgeComputeInfo.getActiveStatus() != ActiveStatus.ACITVE)
        {
            RedisUtil.delete(RedisCache.SCHED_EDGE_RESOURCES + nodeId);
            LOGGER.info("node: {} active status is: {}", nodeId, tblEdgeComputeInfo.getActiveStatus());
            return null;
        }

        NodeResourcesInfo nodeRemainResources = new NodeResourcesInfo();
        String nodeRemainResourcesStr = RedisUtil.get(RedisCache.SCHED_EDGE_RESOURCES + nodeId);
        if (null != nodeRemainResourcesStr && !nodeRemainResourcesStr.isEmpty())
        {
            nodeRemainResources = JsonUtils.fromJson(nodeRemainResourcesStr, NodeResourcesInfo.class);
            LOGGER.info("node: {} cache remainResources: {}", nodeId, nodeRemainResourcesStr);
            return nodeRemainResources;
        }

        TblEdgeComputeGpuInfoExample example = new TblEdgeComputeGpuInfoExample();
        TblEdgeComputeGpuInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfoList = tblEdgeComputeGpuInfoMapper.selectByExample(example);

        CpuInfo cpuInfo = new CpuInfo();
        MemInfo memInfo = new MemInfo();
        DiskInfo diskInfo = new DiskInfo();
        List<GpuInfo> gpuInfos = new ArrayList<>();
        cpuInfo.setCpuNum(tblEdgeComputeInfo.getCpuLimit());
        cpuInfo.setCpuModel(tblEdgeComputeInfo.getCpuModel());
        memInfo.setMemLimit(tblEdgeComputeInfo.getMemLimit());
        diskInfo.setDiskType(tblEdgeComputeInfo.getDiskType());
        diskInfo.setDiskLimit(tblEdgeComputeInfo.getDiskLimit());
        for (TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo:tblEdgeComputeGpuInfoList)
        {
            GpuInfo gpuInfo = new GpuInfo();
            BeanUtils.copyProperties(tblEdgeComputeGpuInfo, gpuInfo);
            gpuInfos.add(gpuInfo);
        }
        nodeRemainResources.setCpu(cpuInfo);
        nodeRemainResources.setMem(memInfo);
        nodeRemainResources.setDisk(diskInfo);
        nodeRemainResources.setGpu(gpuInfos);
        nodeRemainResources.setInUse(false);
        nodeRemainResources.setMonopoly(false);

        ViewCompleteContainerInstInfoExample instExample = new ViewCompleteContainerInstInfoExample();
        ViewCompleteContainerInstInfoExample.Criteria instCriteria = instExample.createCriteria();
        instCriteria.andNodeIdEqualTo(nodeId);
        instCriteria.andStatusNotIn(notOccupyResourcesStatusList);
        instCriteria.andSpecIdIsNotNull();
        List<ViewCompleteContainerInstInfo> viewCompleteContainerInstInfos = viewCompleteContainerInstInfoMapper.selectByExample(instExample);
        if (viewCompleteContainerInstInfos.isEmpty())
        {
            nodeRemainResources.setBindInstNum(0);
        }
        else
        {
            for (ViewCompleteContainerInstInfo viewCompleteContainerInstInfo : viewCompleteContainerInstInfos)
            {
                nodeRemainResources.getCpu().setCpuNum(Math.max(nodeRemainResources.getCpu().getCpuNum() - viewCompleteContainerInstInfo.getCpuNum(), 0));
                nodeRemainResources.getMem().setMemLimit(Math.max(nodeRemainResources.getMem().getMemLimit() - viewCompleteContainerInstInfo.getMemLimit(), 0));
                nodeRemainResources.getDisk().setDiskLimit(Math.max(nodeRemainResources.getDisk().getDiskLimit() - viewCompleteContainerInstInfo.getDiskLimit(), 0));
            }
            nodeRemainResources.setInUse(true);
            nodeRemainResources.setBindInstNum(viewCompleteContainerInstInfos.size());
        }

        List<TblStackInfo> tblStackInfos = stackOperator.getStackByNodeId(nodeId, notOccupyResourcesStatusList);
        if (tblStackInfos.isEmpty())
        {
            nodeRemainResources.setBindStackNum(0);
        }
        else
        {
            for (TblStackInfo tblStackInfo : tblStackInfos)
            {
                DevNeedInfo devNeedInfo = JsonUtils.fromJson(tblStackInfo.getDevNeedInfo(), DevNeedInfo.class);
                nodeRemainResources.getCpu().setCpuNum(Math.max(nodeRemainResources.getCpu().getCpuNum() - devNeedInfo.getCpu().getCpuNum(), 0));
                nodeRemainResources.getMem().setMemLimit(Math.max(nodeRemainResources.getMem().getMemLimit() - devNeedInfo.getMem().getMemLimit(), 0));
                nodeRemainResources.getDisk().setDiskLimit(Math.max(nodeRemainResources.getDisk().getDiskLimit() - devNeedInfo.getDisk().getDiskLimit(), 0));
            }
            nodeRemainResources.setInUse(true);
            nodeRemainResources.setBindStackNum(tblStackInfos.size());
        }

        TblSchedEdgeMonopolyExample monopolyExample = new TblSchedEdgeMonopolyExample();
        TblSchedEdgeMonopolyExample.Criteria monopolyCriteria = monopolyExample.createCriteria();
        monopolyCriteria.andNodeIdEqualTo(nodeId);
        monopolyCriteria.andStatusNotEqualTo(EdgeMonopolyState.REMOVE);
        List<TblSchedEdgeMonopoly> tblSchedEdgeMonopolies = tblSchedEdgeMonopolyMapper.selectByExample(monopolyExample);
        if (null != tblSchedEdgeMonopolies && !tblSchedEdgeMonopolies.isEmpty())
        {
            nodeRemainResources.setMonopoly(true);
        }

        nodeRemainResources.setNodeId(tblEdgeComputeInfo.getNodeId());
        nodeRemainResources.setSiteId(tblEdgeComputeInfo.getSiteId());
        nodeRemainResources.setRegionId(tblEdgeComputeInfo.getRegionId());

        RedisUtil.set(RedisCache.SCHED_EDGE_RESOURCES + nodeId, JsonUtils.toJson(nodeRemainResources), RedisCache.NODE_REMAIN_RESOURCES_DURATION);
        LOGGER.info("node: {} remainResources: {}", nodeId, nodeRemainResources);
        return nodeRemainResources;
    }

    public boolean isResourcesSatisfied(NodeResourcesInfo nodeRemainingResources, DevNeedInfo requestResources){
        boolean isSatisfied = true;
        if (null == nodeRemainingResources || null == requestResources || null == requestResources.getCpu() ||
                null == requestResources.getCpu().getCpuNum() || requestResources.getCpu().getCpuNum() < 0 ||
                null == requestResources.getMem() || null == requestResources.getMem().getMemLimit() ||
                requestResources.getMem().getMemLimit() < 0 || null == requestResources.getDisk() ||
                null == requestResources.getDisk().getDiskLimit() || requestResources.getDisk().getDiskLimit() < 0)
        {
            return false;
        }
        if (null != requestResources.getGpu() && requestResources.getGpu().getGpuNum() > 0)
        {
            if (null != nodeRemainingResources.getGpu() && nodeRemainingResources.getGpu().size() > 0)
            {
                int gpuNeed = requestResources.getGpu().getGpuNum();
                for (GpuInfo gpuInfo:nodeRemainingResources.getGpu())
                {
                    if (gpuInfo.getStatus() != GpuState.FREE.code)
                    {
                        continue;
                    }
                    if (null != requestResources.getGpu().getGpuType() && !requestResources.getGpu().getGpuType().isEmpty() &&
                            !requestResources.getGpu().getGpuType().equals(gpuInfo.getGpuType()))
                    {
                        continue;
                    }
                    if (null != requestResources.getGpu().getGpuModel() && !requestResources.getGpu().getGpuModel().isEmpty() &&
                            !requestResources.getGpu().getGpuModel().equals(gpuInfo.getGpuModel()))
                    {
                        continue;
                    }
                    if (null != requestResources.getGpu().getDriverVersion() && !requestResources.getGpu().getDriverVersion().isEmpty() &&
                            !requestResources.getGpu().getDriverVersion().equals(gpuInfo.getDriverVersion()))
                    {
                        continue;
                    }
                    if (null != requestResources.getGpu().getCudaVersion() && !requestResources.getGpu().getCudaVersion().isEmpty() &&
                            !requestResources.getGpu().getCudaVersion().equals(gpuInfo.getCudaVersion()))
                    {
                        continue;
                    }
                    if (null != requestResources.getGpu().getCudnnVersion() && !requestResources.getGpu().getCudnnVersion().isEmpty() &&
                            !requestResources.getGpu().getCudnnVersion().equals(gpuInfo.getCudnnVersion()))
                    {
                        continue;
                    }
                    gpuNeed --;
                    if (gpuNeed == 0) break;
                }
                if (gpuNeed > 0) return false;
            }
            else
            {
                return false;
            }
        }
        if (nodeRemainingResources.getCpu().getCpuNum() < requestResources.getCpu().getCpuNum()) return false;
        if (nodeRemainingResources.getMem().getMemLimit() < requestResources.getMem().getMemLimit()) return false;
        if (nodeRemainingResources.getDisk().getDiskLimit() < requestResources.getDisk().getDiskLimit()) return false;

        return isSatisfied;
    }

    public boolean requireResouceMatchable(NodeResourcesInfo nodeRemainingResources, DevNeedInfo requestResources, String productType, String refId, boolean isMonopoly)
    {
        NodeResourcesInfo nodeResourcesInfo = new NodeResourcesInfo();
        BeanUtils.copyProperties(nodeRemainingResources, nodeResourcesInfo);

        List<String> bindGpus = new ArrayList<>();
        Integer refType = null;
        switch(productType)
        {
            case SchedulerMsgType.INST:
                refType = GpuState.CONTAINER_USE.code;
                break;
            case SchedulerMsgType.STACK:
                refType = GpuState.STACK_USE.code;
                break;
            case SchedulerMsgType.CLUSTER:
                refType = GpuState.CLUSTER_USE.code;
                break;
            default:
                return false;
        }

        if (null != requestResources.getGpu() && null != requestResources.getGpu().getGpuNum() && requestResources.getGpu().getGpuNum() > 0)
        {
            if (null != nodeRemainingResources.getGpu() && nodeRemainingResources.getGpu().size() > 0)
            {
                int gpuNeed = requestResources.getGpu().getGpuNum();

                for (GpuInfo gpuInfo:nodeRemainingResources.getGpu())
                {
                    if (gpuInfo.getStatus() != GpuState.FREE.code)
                    {
                        continue;
                    }

                    gpuNeed --;

                    bindGpus.add(gpuInfo.getGpuId());

                    if (gpuNeed == 0) break;
                }
                if (gpuNeed > 0)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        if (nodeRemainingResources.getCpu().getCpuNum() < requestResources.getCpu().getCpuNum())
        {
            return false;
        }
        if (nodeRemainingResources.getMem().getMemLimit() < requestResources.getMem().getMemLimit())
        {
            return false;
        }
        if (nodeRemainingResources.getDisk().getDiskLimit() < requestResources.getDisk().getDiskLimit())
        {
            return false;
        }

        nodeRemainingResources.getCpu().setCpuNum(nodeRemainingResources.getCpu().getCpuNum() - requestResources.getCpu().getCpuNum());
        nodeRemainingResources.getMem().setMemLimit(nodeRemainingResources.getMem().getMemLimit() - requestResources.getMem().getMemLimit());
        nodeRemainingResources.getDisk().setDiskLimit(nodeRemainingResources.getDisk().getDiskLimit() - requestResources.getDisk().getDiskLimit());

        nodeRemainingResources.setInUse(true);
        if (isMonopoly) nodeRemainingResources.setMonopoly(true);

        if (!bindGpus.isEmpty()) updateGpuStatusToBind(bindGpus, refId, refType);

        RedisUtil.set(RedisCache.SCHED_EDGE_RESOURCES + nodeRemainingResources.getNodeId(), JsonUtils.toJson(nodeRemainingResources), RedisCache.NODE_REMAIN_RESOURCES_DURATION);

        return true;
    }

    public void updateGpuStatusToBind(List<String> bindGpuInfos, String refId, Integer refType)
    {
        TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo = new TblEdgeComputeGpuInfo();
        tblEdgeComputeGpuInfo.setStatus(refType);
        tblEdgeComputeGpuInfo.setUpdateTime(new Date());
        tblEdgeComputeGpuInfo.setRefId(refId);

        TblEdgeComputeGpuInfoExample example = new TblEdgeComputeGpuInfoExample();
        TblEdgeComputeGpuInfoExample.Criteria criteria = example.createCriteria();
        criteria.andGpuIdIn(bindGpuInfos);

        tblEdgeComputeGpuInfoMapper.updateByExampleSelective(tblEdgeComputeGpuInfo, example);
    }

    public void updateGpuStatusToFree(String gpuId)
    {
        TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo = new TblEdgeComputeGpuInfo();
        tblEdgeComputeGpuInfo.setGpuId(gpuId);
        tblEdgeComputeGpuInfo.setStatus(GpuState.FREE.code);
        tblEdgeComputeGpuInfo.setRefId(null);
        tblEdgeComputeGpuInfo.setUpdateTime(new Date());
        tblEdgeComputeGpuInfoMapper.updateByPrimaryKey(tblEdgeComputeGpuInfo);
    }

    public void releaseResources(String nodeId, List<String> refIdList)
    {
        RedisUtil.delete(RedisCache.SCHED_EDGE_RESOURCES + nodeId);

        TblEdgeComputeGpuInfoExample example = new TblEdgeComputeGpuInfoExample();
        TblEdgeComputeGpuInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfoList = tblEdgeComputeGpuInfoMapper.selectByExample(example);

        if (null == tblEdgeComputeGpuInfoList || tblEdgeComputeGpuInfoList.isEmpty()) return;

        Set<String> refIdSet = new HashSet<>(refIdList);

        for (TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo:tblEdgeComputeGpuInfoList)
        {
            if (null != tblEdgeComputeGpuInfo.getRefId() && !tblEdgeComputeGpuInfo.getRefId().isEmpty() && refIdSet.contains(tblEdgeComputeGpuInfo.getRefId()))
            {
                updateGpuStatusToFree(tblEdgeComputeGpuInfo.getGpuId());
            }
        }
    }

    public void releaseResources(String nodeId, String refId)
    {
        RedisUtil.delete(RedisCache.SCHED_EDGE_RESOURCES + nodeId);

        TblEdgeComputeGpuInfoExample example = new TblEdgeComputeGpuInfoExample();
        TblEdgeComputeGpuInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        List<TblEdgeComputeGpuInfo> tblEdgeComputeGpuInfoList = tblEdgeComputeGpuInfoMapper.selectByExample(example);

        if (null == tblEdgeComputeGpuInfoList || tblEdgeComputeGpuInfoList.isEmpty()) return;

        for (TblEdgeComputeGpuInfo tblEdgeComputeGpuInfo:tblEdgeComputeGpuInfoList)
        {
            if (null != tblEdgeComputeGpuInfo.getRefId() && !tblEdgeComputeGpuInfo.getRefId().isEmpty() && refId.equals(tblEdgeComputeGpuInfo.getRefId()))
            {
                updateGpuStatusToFree(tblEdgeComputeGpuInfo.getGpuId());
                LOGGER.info("release gpu bind. node: {}, ref: {}, gpu: {}", nodeId, refId, tblEdgeComputeGpuInfo.getGpuId());
            }
        }
    }

    public boolean updateEdgeMonopolyStatusToRemove(String nodeId, String refId)
    {
        LOGGER.info("update edge: {}, ref: {}, status: remove", nodeId, refId);
        TblSchedEdgeMonopoly tblSchedEdgeMonopoly = new TblSchedEdgeMonopoly();
        tblSchedEdgeMonopoly.setNodeId(nodeId);
        tblSchedEdgeMonopoly.setRefId(refId);
        tblSchedEdgeMonopoly.setStatus(EdgeMonopolyState.REMOVE);
        tblSchedEdgeMonopoly.setUpdateTime(new Date());
        tblSchedEdgeMonopolyMapper.updateByPrimaryKeySelective(tblSchedEdgeMonopoly);
        return true;
    }

    public void releaseResourceWhileScheduleFail(List<BindRelation> bindRelations)
    {
        LOGGER.info("release resources due to scheduler fail. {}", bindRelations);

        for (BindRelation bindRelation:bindRelations)
        {
            releaseResources(bindRelation.getDstNodeId(), bindRelation.getWaitAssignId());
            updateEdgeMonopolyStatusToRemove(bindRelation.getDstNodeId(), bindRelation.getWaitAssignId());
        }
    }
}
