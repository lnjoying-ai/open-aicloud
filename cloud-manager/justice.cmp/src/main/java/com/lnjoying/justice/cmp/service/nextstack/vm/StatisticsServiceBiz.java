package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.VmInstanceStatus;
import com.lnjoying.justice.cmp.db.model.TblCmpResourceStats;
import com.lnjoying.justice.cmp.db.model.TblCmpResourceStatsExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceExample;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.InstanceStatsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.ResourceStats;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Service
public class StatisticsServiceBiz
{
    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private CloudService cloudService;

    public List<ResourceStats> getResourceStats(String cloudId, String userId, String name, int days)
            throws WebSystemException
    {
        name = name.trim();
        TblCmpResourceStatsExample example = new TblCmpResourceStatsExample();
        TblCmpResourceStatsExample.Criteria criteria = example.createCriteria();
        if( !"cpu".equals(name) && !"mem".equals(name) && !"vm".equals(name)
                && !"nat".equals(name) && !"storage".equals(name))
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }

        criteria.andUserIdEqualTo(userId);
        criteria.andNameEqualTo(name);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        example.setOrderByClause("create_time desc, stats_id desc");
        example.setStartRow(0);
        example.setPageSize(days);
        List<TblCmpResourceStats> tblCmpResourceStats = vmComputeRepository.getResourceStats(example);

        if (tblCmpResourceStats == null)
        {
            return null;
        }

        List<ResourceStats> resourceStats = new ArrayList<>();

        tblCmpResourceStats.forEach(tblCmpResourceStat -> {
            ResourceStats resourceStat = new ResourceStats();
            resourceStat.setStatsId(tblCmpResourceStat.getStatsId());
            resourceStat.setName(tblCmpResourceStat.getName());
            resourceStat.setTotal(tblCmpResourceStat.getTotal());
            resourceStat.setUsed(tblCmpResourceStat.getUsed());
            resourceStat.setRunning(tblCmpResourceStat.getRunning());
            resourceStat.setUnit(tblCmpResourceStat.getUnit());
            resourceStat.setPhaseStatus(tblCmpResourceStat.getPhaseStatus());
            resourceStat.setCreateTime(tblCmpResourceStat.getCreateTime());
            resourceStat.setUpdateTime(tblCmpResourceStat.getUpdateTime());
            resourceStats.add(resourceStat);
        });

        cloudService.syncSingleData(cloudId, null, SyncMsg.NS_RESOURCE_STATS);

        return resourceStats;
    }

    public long getUserStorageSize(String cloudId, String userId)
    {
        if (isAdmin() || cloudService.isOwner(cloudId, userId))
        {
            userId = null;
        }
        long rootDiskSize = vmComputeRepository.sumRootDiskSizeByUserId(cloudId, userId);
        long dataDiskSize = vmComputeRepository.sumDataDiskSizeByUserId(cloudId, userId);

        return rootDiskSize+dataDiskSize;
    }

    public long getVmCount(String cloudId, String userId)
    {
        TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
        TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if ((!isAdmin()) && (!cloudService.isOwner(cloudId, userId)))
        {
            criteria.andUserIdEqualTo(userId);
        }
        return vmComputeRepository.countVmInstancesByExample(example);
    }

    public InstanceStatsRsp getInstanceStats(String cloudId, String userId)
    {
        if (isAdmin() || cloudService.isOwner(cloudId, userId))
        {
            userId = null;
        }
        int vmTotal = countByVmPhase(cloudId, userId, null);
        int vmRunning = countByVmPhase(cloudId, userId, VmInstanceStatus.INSTANCE_RUNNING) + countByVmPhase(cloudId, userId, VmInstanceStatus.INSTANCE_MONITOR_TAG_DONE);
        int vmCreateFailed = countByVmPhase(cloudId, userId, VmInstanceStatus.INSTANCE_CREATE_FAILED);
        int vmPowerOff = countByVmPhase(cloudId, userId, VmInstanceStatus.INSTANCE_POWEROFF);
        int vmCreating = vmTotal-vmRunning-vmPowerOff-vmCreateFailed;
        InstanceStatsRsp getVmStatsRsp = new InstanceStatsRsp();
        getVmStatsRsp.setInstancePowerOff(vmPowerOff);
        getVmStatsRsp.setInstanceRunning(vmRunning);
        getVmStatsRsp.setInstanceTotal(vmTotal);
        getVmStatsRsp.setInstanceCreateFailed(vmCreateFailed);
        getVmStatsRsp.setInstanceCreating(vmCreating);
        return getVmStatsRsp;
    }

    public int countByVmPhase(String cloudId, String userId, Integer phase)
    {
        TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
        TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (null != userId)
        {
            criteria.andEeUserEqualTo(userId);
        }
        if (null != phase)
        {
            criteria.andPhaseStatusEqualTo(phase);
        }
        else
        {
            criteria.andPhaseStatusNotEqualTo(REMOVED);
        }
        return (int) vmComputeRepository.countVmInstancesByExample(example);
    }
}
