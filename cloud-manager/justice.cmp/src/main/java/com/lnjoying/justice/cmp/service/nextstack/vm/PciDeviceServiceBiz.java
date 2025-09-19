package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.lnjoying.justice.cmp.common.RedisCache;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.VmInstanceStatus;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.PciDeviceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcFlavorInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.*;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.PciDeviceSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.rpc.RpcFlavorServiceImpl;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class PciDeviceServiceBiz
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private RpcFlavorServiceImpl rpcFlavorService;

    @Autowired
    private CloudService cloudService;

    public List<PciDeviceDetailInfo> getPciDevices(String cloudId, PciDeviceSearchCritical searchCritical, String nodeId)
    {
        TblCmpPciDeviceExample example = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andNodeIdEqualTo(nodeId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        if(!StringUtils.isBlank(searchCritical.getPciDeviceName()))
        {
            criteria.andNameLike("%" + searchCritical.getPciDeviceName() + "%");
        }
        if (0 == vmComputeRepository.countPCIDevices(example))
        {
            return new ArrayList<>();
        }

        int begin = ((searchCritical.getPageNum() - 1) * searchCritical.getPageSize());
        example.setOrderByClause("tbl_cmp_hypervisor_node.node_id desc, tbl_cmp_pci_device.create_time desc");
        example.setStartRow(begin);
        example.setPageSize(searchCritical.getPageSize());

        List<PciDeviceDetailInfo> deviceDetailInfos = vmComputeRepository.selectPciDevices(example);
        for (PciDeviceDetailInfo deviceDetailInfo : deviceDetailInfos)
        {
            if (VmInstanceStatus.DEVICE_DETACHED == deviceDetailInfo.getPhaseStatus())
            {
                deviceDetailInfo.setVmInstanceName(null);
                deviceDetailInfo.setVmInstanceId(null);
            }

            cloudService.syncSingleData(cloudId, deviceDetailInfo.getDeviceId(), SyncMsg.NS_PCI_DEVICE);
        }
        return deviceDetailInfos;
    }

    public PciDeviceDetailInfo getPciDeviceDetailInfo(String cloudId, String pciDeviceId)
    {
        TblCmpPciDevice pciDevice = vmComputeRepository.getPciDeviceById(cloudId, pciDeviceId);
        if (null == pciDevice || REMOVED == pciDevice.getPhaseStatus() || REMOVED == pciDevice.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.PCI_DEVICE_NOT_EXIST, ErrorLevel.INFO);
        }

        TblCmpPciDeviceExample example = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andDeviceIdEqualTo(pciDeviceId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        example.setOrderByClause("tbl_cmp_hypervisor_node.node_id desc, tbl_cmp_pci_device.create_time desc");
        example.setStartRow(0);
        example.setPageSize(1);

        List<PciDeviceDetailInfo> deviceDetailInfos = vmComputeRepository.selectPciDevices(example);

        return CollectionUtils.isEmpty(deviceDetailInfos) ? null : deviceDetailInfos.get(0);
    }

    public List<HypervisorNodeInfo> getAvailableNode(String cloudId, PageSearchCritical searchCritical, String vmId, String userId, boolean isGpu)
    {
        if (!StringUtils.isBlank(userId) && !StringUtils.isBlank(vmId))
        {
            vmInstanceOwnByUser(cloudId, vmId, userId);
        }
        if (!isGpu)
        {
            return getCustomAvailableNode(cloudId, searchCritical, vmId);
        }
        TblCmpPciDeviceExample pciDeviceExample = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria pciDeviceCriteria = pciDeviceExample.createCriteria();
        pciDeviceCriteria.andCloudIdEqualTo(cloudId);
        pciDeviceCriteria.andEeStatusNotEqualTo(REMOVED);
        pciDeviceCriteria.andVmInstanceIdIsNull();
        List<String> deviceIds = vmComputeRepository.getPciDevices(pciDeviceExample).stream().map(TblCmpPciDevice::getDeviceId).collect(Collectors.toList());

        if (0 == deviceIds.size())
        {
            return new ArrayList<>();
        }

        TblCmpHypervisorNodeExample hypervisorNodeExample = new TblCmpHypervisorNodeExample();
        TblCmpHypervisorNodeExample.Criteria hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
        hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
        hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
        hypervisorNodeCriteria.andPciDevicePhaseStatusEqualTo(VmInstanceStatus.DEVICE_DETACHED);
        hypervisorNodeCriteria.andPhaseStatusNotEqualTo(REMOVED);

        int begin = ((searchCritical.getPageNum() - 1) * searchCritical.getPageSize());
        hypervisorNodeExample.setOrderByClause("create_time desc,node_id desc");
        hypervisorNodeExample.setStartRow(begin);
        hypervisorNodeExample.setPageSize(searchCritical.getPageSize());

        List<HypervisorNodeInfo> hypervisorNodeInfos = vmComputeRepository.selectGpuNodeInfo(hypervisorNodeExample);

        if (hypervisorNodeInfos.isEmpty()) return  hypervisorNodeInfos;
        for (HypervisorNodeInfo nodeInfo: hypervisorNodeInfos)
        {
            HypervisorNodeAllocationInfo allocationInfo = resourceAllocation(cloudId, nodeInfo.getNodeId());
            if (null != allocationInfo)
            {
                if (null != allocationInfo.getCpuSum())
                {
                    nodeInfo.setCpuAllocation(allocationInfo.getCpuSum());
                }
                else
                {
                    nodeInfo.setCpuAllocation(0);
                }
                if (null != allocationInfo.getMemSum())
                {
                    nodeInfo.setMemAllocation(allocationInfo.getMemSum());
                }
                else
                {
                    nodeInfo.setMemAllocation(0);
                }
            }

            cloudService.syncSingleData(cloudId, nodeInfo.getNodeId(), SyncMsg.NS_HYPERVISOR_NODE);
        }
        return hypervisorNodeInfos;
    }

    public List<HypervisorNodeInfo> getCustomAvailableNode(String cloudId, PageSearchCritical searchCritical,  String userId)
    {
        TblCmpHypervisorNodeExample example = new TblCmpHypervisorNodeExample();
        TblCmpHypervisorNodeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);

        int begin = ((searchCritical.getPageNum() - 1) * searchCritical.getPageSize());
        example.setOrderByClause("tbl_cmp_hypervisor_node.create_time desc, tbl_cmp_hypervisor_node.node_id desc");
        example.setStartRow(begin);
        example.setPageSize(searchCritical.getPageSize());

        return vmComputeRepository.selectCustomNodeInfo(example);
    }

    public List<PciDeviceInfo> getAvailableDeviceInfos(String cloudId, PageSearchCritical searchCritical, @NotBlank String nodeId, String vmId, String userId)
    {
        if (!StringUtils.isBlank(vmId) && !StringUtils.isBlank(userId))
        {
            vmInstanceOwnByUser(cloudId, vmId, userId);
        }

        TblCmpPciDeviceExample pciDeviceExample = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria pciDeviceCriteria = pciDeviceExample.createCriteria();
        pciDeviceCriteria.andCloudIdEqualTo(cloudId);
        pciDeviceCriteria.andEeStatusNotEqualTo(REMOVED);
        pciDeviceCriteria.andVmInstanceIdIsNull();
        pciDeviceCriteria.andNodeIdEqualTo(nodeId);
        pciDeviceCriteria.andPhaseStatusEqualTo(VmInstanceStatus.DEVICE_DETACHED);
        long pciDeviceCount =vmComputeRepository.countPciDevicesByExample(pciDeviceExample);

        if (0 == pciDeviceCount)
        {
            return new ArrayList<>();
        }

        pciDeviceExample.setOrderByClause("tbl_cmp_pci_device.create_time desc, tbl_cmp_pci_device.device_id desc");
        if (searchCritical.getPageNum() != null && searchCritical.getPageSize() != null)
        {
            int begin = ((searchCritical.getPageNum() - 1) * searchCritical.getPageSize());
            pciDeviceExample.setStartRow(begin);
            pciDeviceExample.setPageSize(searchCritical.getPageSize());
        }

        List<TblCmpPciDevice> tblCmpPciDevices = vmComputeRepository.getPciDevices(pciDeviceExample);
        List<PciDeviceInfo> pciDeviceInfos = tblCmpPciDevices.stream().map(tblCmpPciDevice -> {
            PciDeviceInfo pciDeviceInfo = new PciDeviceInfo();
            pciDeviceInfo.setPciDevice(tblCmpPciDevice);
            return pciDeviceInfo;
        }).collect(Collectors.toList());

        pciDeviceInfos.forEach(pciDevice -> cloudService.syncSingleData(cloudId, pciDevice.getDeviceId(), SyncMsg.NS_PCI_DEVICE));
        return pciDeviceInfos;
    }

    private void vmInstanceOwnByUser(String cloudId, String vmId, String userId)
    {
        TblCmpVmInstance tblVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmId);
        if (null == tblVmInstance || REMOVED == tblVmInstance.getPhaseStatus() || REMOVED == tblVmInstance.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (!StringUtils.isBlank(userId) && !userId.equals(tblVmInstance.getEeUser()))
        {
            throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
        }
    }

    public HypervisorNodeAllocationInfo resourceAllocation(String cloudId, String hypervisorNodeId)
    {
        TblCmpHypervisorNodeExample example = new TblCmpHypervisorNodeExample();
        TblCmpHypervisorNodeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andNodeIdEqualTo(hypervisorNodeId);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andVmInstancePhaseStatusIsNullOrNotEqualTo(REMOVED);
        example.setOrderByClause("tbl_cmp_hypervisor_node.create_time desc, tbl_cmp_hypervisor_node.node_id desc");
        example.setStartRow(0);
        example.setPageSize(1);
        List<HypervisorNodeAllocationInfo> hypervisorNodeAllocationInfos = vmComputeRepository.selectNodeAllocationInfo(example);
        if (hypervisorNodeAllocationInfos.isEmpty())
        {
            return null;
        }

        hypervisorNodeAllocationInfos = hypervisorNodeAllocationInfos.stream().peek(nodeInfo -> {
            nodeInfo.setMemSum(nodeInfo.getMemSum()-nodeInfo.getMemRecycle());
            nodeInfo.setCpuSum(nodeInfo.getCpuSum()-nodeInfo.getCpuRecycle());
        }).collect(Collectors.toList());

        TblCmpPciDeviceExample tblCmpPciDeviceExample = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria tblCmpPciDeviceCriteria = tblCmpPciDeviceExample.createCriteria();
        tblCmpPciDeviceCriteria.andCloudIdEqualTo(cloudId);
        tblCmpPciDeviceCriteria.andNodeIdEqualTo(hypervisorNodeId);
        tblCmpPciDeviceCriteria.andPhaseStatusNotEqualTo(REMOVED);
        tblCmpPciDeviceCriteria.andEeStatusNotEqualTo(REMOVED);

        List<TblCmpPciDevice> pciDevices = vmComputeRepository.getPciDevices(tblCmpPciDeviceExample);

        hypervisorNodeAllocationInfos.get(0).setGpuTotal((long) pciDevices.size());
        hypervisorNodeAllocationInfos.get(0).setGpuName(CollectionUtils.isEmpty(pciDevices) ? null : pciDevices.get(0).getName());
        long availableGpuCount = pciDevices.stream().filter(pciDevice -> null == pciDevice.getVmInstanceId()).count();
        hypervisorNodeAllocationInfos.get(0).setGpuCount((int)availableGpuCount);

        return hypervisorNodeAllocationInfos.get(0);
    }

    public NodeAllocationInfosRsp getResourceAllocation(String cloudId, PageSearchCritical searchCritical, String flavorId)
    {
        RpcFlavorInfo flavorInfo = rpcFlavorService.getFlavorInfo(cloudId, flavorId);
        NodeAllocationInfosRsp nodeAllocationInfosRsp = new NodeAllocationInfosRsp();
        if (null == flavorInfo)
        {
            throw new WebSystemException(ErrorCode.FLAVOR_NOT_EXIST, ErrorLevel.INFO);
        }
        Map<String, AvailableGPURsp> gpuRspMap;

        long gpuTotal = vmComputeRepository.selectPciDeviceCount(cloudId);

        int begin = ((searchCritical.getPageNum() - 1) * searchCritical.getPageSize());

        if (null == flavorInfo.getGpuCount() || 0 == flavorInfo.getGpuCount())
        {
            TblCmpHypervisorNodeExample hypervisorNodeExample = new TblCmpHypervisorNodeExample();
            TblCmpHypervisorNodeExample.Criteria hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
            hypervisorNodeCriteria.andPhaseStatusEqualTo(VmInstanceStatus.HYPERVISOR_NODE_CREATED);
            hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
            hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
            long count = vmComputeRepository.countHypervisorNodeByExample(hypervisorNodeExample);
            nodeAllocationInfosRsp.setTotalNum(count);
            if (0 == count)
            {
                return nodeAllocationInfosRsp;
            }

            hypervisorNodeExample.clear();
            hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
            hypervisorNodeCriteria.andPhaseStatusEqualTo(VmInstanceStatus.HYPERVISOR_NODE_CREATED);
            hypervisorNodeCriteria.andVmInstancePhaseStatusNotEqualTo(REMOVED);
            hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
            hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);

            hypervisorNodeExample.setStartRow(begin);
            hypervisorNodeExample.setPageSize(searchCritical.getPageSize());

            List<HypervisorNodeAllocationInfo> nodeAllocationInfos = vmComputeRepository.selectNodeAllocationInfo(hypervisorNodeExample);
            List<HypervisorNodeAllocationInfo> filterNodeAllocationInfos = nodeAllocationInfos.stream().filter(nodeAllocationInfo ->
                    nodeAllocationInfo.getMemTotal()-nodeAllocationInfo.getMemSum() + nodeAllocationInfo.getMemRecycle() > flavorInfo.getMem()).collect(Collectors.toList());
            nodeAllocationInfosRsp.setNodeAllocationInfos(filterNodeAllocationInfos);
            if (filterNodeAllocationInfos.isEmpty())
            {
                nodeAllocationInfosRsp.setTotalNum(0);
                return nodeAllocationInfosRsp;
            }

            hypervisorNodeExample.clear();
            hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
            List<String> nodeIds = nodeAllocationInfosRsp.getNodeAllocationInfos().stream().map(HypervisorNodeAllocationInfo::getNodeId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(nodeIds)) hypervisorNodeCriteria.andNodeIdIn(nodeIds);
            hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
            hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
            hypervisorNodeCriteria.andPciDeviceCloudIdEqualTo(cloudId);
            hypervisorNodeCriteria.andPciDeviceEeStatusNotEqualTo(REMOVED);
            hypervisorNodeCriteria.andPciDeviceGroupCloudIdEqualTo(cloudId);
            hypervisorNodeCriteria.andPciDeviceGroupEeStatusNotEqualTo(REMOVED);

            List<AvailableGPURsp> availableGPURsps = vmComputeRepository.selectAvailableGPURsp(hypervisorNodeExample);
            if (availableGPURsps.isEmpty())
            {
                return nodeAllocationInfosRsp;
            }
            gpuRspMap = availableGPURsps.stream().collect(Collectors.toMap(AvailableGPURsp::getNodeId, Function.identity()));
            for (HypervisorNodeAllocationInfo allocationInfo: nodeAllocationInfosRsp.getNodeAllocationInfos())
            {
                allocationInfo.setMemSum(allocationInfo.getMemSum()-allocationInfo.getMemRecycle());
                allocationInfo.setCpuSum(allocationInfo.getCpuSum()-allocationInfo.getCpuRecycle());
                if (gpuRspMap.containsKey(allocationInfo.getNodeId()))
                {
                    allocationInfo.setGpuTotal(gpuTotal);
                    allocationInfo.setGpuCount(gpuRspMap.get(allocationInfo.getNodeId()).getGpuCount());
                    allocationInfo.setGpuName(gpuRspMap.get(allocationInfo.getNodeId()).getGpuName());
                }
                else
                {
                    allocationInfo.setGpuTotal(gpuTotal);
                    allocationInfo.setGpuCount(0);
                }
            }
            return nodeAllocationInfosRsp;
        }
        long count = vmComputeRepository.selectTotalAvailableGPURspByNameAndCount(cloudId, flavorInfo.getGpuName(), flavorInfo.getGpuCount());
        nodeAllocationInfosRsp.setTotalNum(count);
        if (0 == count)
        {
            return nodeAllocationInfosRsp;
        }
        int index = (searchCritical.getPageNum()-1) * searchCritical.getPageSize();
        Integer ibCount = vmComputeRepository.getIbCount(flavorInfo.getGpuName(), flavorInfo.getGpuCount(), flavorInfo.getNeedIb());
        List<AvailableGPURsp> availableGPUs = vmComputeRepository.selectAvailableGPURspByNameAndCount(cloudId, flavorInfo.getGpuName(), flavorInfo.getGpuCount(), index, searchCritical.getPageSize(), ibCount);
        if (availableGPUs.isEmpty())
        {
            return nodeAllocationInfosRsp;
        }
        gpuRspMap = new HashMap<>();
        for (AvailableGPURsp availableGPURsp: availableGPUs)
        {
            gpuRspMap.put(availableGPURsp.getNodeId(), availableGPURsp);
        }

        TblCmpHypervisorNodeExample hypervisorNodeExample = new TblCmpHypervisorNodeExample();
        TblCmpHypervisorNodeExample.Criteria hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
        hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
        hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
        hypervisorNodeCriteria.andPhaseStatusEqualTo(VmInstanceStatus.HYPERVISOR_NODE_CREATED);
        hypervisorNodeCriteria.andVmInstancePhaseStatusNotEqualTo(REMOVED);
        hypervisorNodeCriteria.andNodeIdIn(availableGPUs.stream().map(AvailableGPURsp::getNodeId).collect(Collectors.toList()));

        hypervisorNodeExample.setStartRow(begin);
        hypervisorNodeExample.setPageSize(searchCritical.getPageSize());

        nodeAllocationInfosRsp.setNodeAllocationInfos(vmComputeRepository.selectNodeAllocationInfo(hypervisorNodeExample));
        nodeAllocationInfosRsp.getNodeAllocationInfos().forEach(nodeAllocationInfo -> {
            nodeAllocationInfo.setMemSum(nodeAllocationInfo.getMemSum()-nodeAllocationInfo.getMemRecycle());
            nodeAllocationInfo.setCpuSum(nodeAllocationInfo.getCpuSum()-nodeAllocationInfo.getCpuRecycle());
            nodeAllocationInfo.setGpuTotal(gpuTotal);
            nodeAllocationInfo.setGpuName(gpuRspMap.get(nodeAllocationInfo.getNodeId()).getGpuName());
            nodeAllocationInfo.setGpuCount(gpuRspMap.get(nodeAllocationInfo.getNodeId()).getGpuCount());
        });
        return nodeAllocationInfosRsp;
    }

    public Double getCloudGPUUsage(String cloudId)
    {
        try
        {
            String usageStr = RedisUtil.get(RedisCache.CLOUD_USAGE + cloudId);

            Double usage = null;
            if (usageStr != null)
            {
                try
                {
                    usage = Double.parseDouble(usageStr);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    LOGGER.error("parse double error {}", e.getMessage());
                }
            }

            if (usage == null)
            {
                TblCmpPciDeviceExample example = new TblCmpPciDeviceExample();
                TblCmpPciDeviceExample.Criteria criteria = example.createCriteria();
                criteria.andCloudIdEqualTo(cloudId);
                criteria.andEeStatusNotEqualTo(REMOVED);
                criteria.andPhaseStatusNotEqualTo(REMOVED);

                long total = vmComputeRepository.countPCIDevices(example);

                if (total == 0)
                {
                    usage = 1d;
                }
                else
                {
                    criteria.andVmInstanceIdIsNotNull();
                    long used = vmComputeRepository.countPCIDevices(example);

                    usage = (double)used / total;
                }

                RedisUtil.set(RedisCache.CLOUD_USAGE + cloudId, usage.toString(), 5 * 60);
            }
            return usage;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
