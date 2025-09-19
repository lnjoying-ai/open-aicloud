package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.lnjoying.justice.cmp.common.CommonPhaseStatus;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.VmInstanceStatus;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNode;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeExample;
import com.lnjoying.justice.cmp.db.model.TblCmpPciDeviceGroupExample;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.HypervisorNodeAddReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.*;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.HypervisorNodeSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Service
public class HypervisorNodeServiceBiz
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    public ResponseEntity addHypervisorNode(String cloudId, HypervisorNodeAddReq addHypervisorNodeReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add hypervisor node error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String nodeId = (String) resultMap.get("nodeId");
                    if (StringUtils.isEmpty(nodeId))
                    {
                        LOGGER.error("add hypervisor node error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpHypervisorNode tblCmpHypervisorNode = new TblCmpHypervisorNode();
                        tblCmpHypervisorNode.setNodeId(nodeId);
                        tblCmpHypervisorNode.setCloudId(cloudId);
                        tblCmpHypervisorNode.setEeBp(bpId);
                        tblCmpHypervisorNode.setEeUser(userId);
                        tblCmpHypervisorNode.setEeStatus(SYNCING);

                        tblCmpHypervisorNode.setName(addHypervisorNodeReq.getName());
                        tblCmpHypervisorNode.setManageIp(addHypervisorNodeReq.getManageIp());
                        tblCmpHypervisorNode.setHostName(addHypervisorNodeReq.getHostname());
                        tblCmpHypervisorNode.setDescription(addHypervisorNodeReq.getDescription());
                        try
                        {
                            vmComputeRepository.insertHypervisorNode(tblCmpHypervisorNode);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpHypervisorNode updateTblCmpHypervisorNode = vmComputeRepository.getHypervisorNodeById(cloudId, nodeId);
                            updateTblCmpHypervisorNode.setEeBp(bpId);
                            updateTblCmpHypervisorNode.setEeUser(userId);
                            updateTblCmpHypervisorNode.setEeStatus(SYNCING);
                            vmComputeRepository.updateHypervisorNode(updateTblCmpHypervisorNode);
                        }

                        cloudService.syncSingleData(cloudId, nodeId, SyncMsg.NS_HYPERVISOR_NODE);
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add hypervisor node failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public NodeAllocationInfosRsp getHypervisorNodes(String cloudId, HypervisorNodeSearchCritical critical) throws WebSystemException
    {
        NodeAllocationInfosRsp nodeAllocationInfosRsp = new NodeAllocationInfosRsp();

        TblCmpHypervisorNodeExample example = new TblCmpHypervisorNodeExample();
        TblCmpHypervisorNodeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (null != critical.getNodeId() && !critical.getNodeId().isEmpty())
        {
            criteria.andNodeIdEqualTo(critical.getNodeId());
        }
        else if (null != critical.getName() && !critical.getName().isEmpty())
        {
            criteria.andNameLike("%" + critical.getName() + "%");
        }
        if (null != critical.getIsHealthy() && critical.getIsHealthy())
        {
            criteria.andPhaseStatusEqualTo(VmInstanceStatus.HYPERVISOR_NODE_CREATED);
        }

        int begin = ((critical.getPageNum() - 1) * critical.getPageSize());
        example.setOrderByClause("create_time desc,node_id desc");
        example.setStartRow(begin);
        example.setPageSize(critical.getPageSize());

        long totalNum = vmComputeRepository.countHypervisorNodeByExample(example);
        nodeAllocationInfosRsp.setTotalNum(totalNum);
        if (totalNum < 1)
        {
            return nodeAllocationInfosRsp;
        }

        List<HypervisorNodeAllocationInfo> nodeAllocationInfos = vmComputeRepository.selectNodeAllocationInfo(example);

        nodeAllocationInfos = nodeAllocationInfos.stream().peek(nodeInfo ->
        {
            nodeInfo.setMemSum(nodeInfo.getMemSum() - nodeInfo.getMemRecycle());
            nodeInfo.setCpuSum(nodeInfo.getCpuSum() - nodeInfo.getCpuRecycle());
        }).collect(Collectors.toList());

        List<String> nodeIds = nodeAllocationInfos.stream().map(HypervisorNodeAllocationInfo::getNodeId).collect(Collectors.toList());

        TblCmpHypervisorNodeExample hypervisorNodeExample = new TblCmpHypervisorNodeExample();
        TblCmpHypervisorNodeExample.Criteria hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
        hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
        hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
        hypervisorNodeCriteria.andNodeIdIn(nodeIds);

        List<AvailableGPURsp> availableGPURsps = vmComputeRepository.selectAvailableGPURsp(hypervisorNodeExample);
        Map<String,AvailableGPURsp> gpuRspMap = availableGPURsps.stream().collect(Collectors.toMap(AvailableGPURsp::getNodeId, Function.identity()));

        long gpuCount = vmComputeRepository.selectPciDeviceCount(cloudId);
        for (HypervisorNodeAllocationInfo allocationInfo : nodeAllocationInfos)
        {
            if (gpuRspMap.containsKey(allocationInfo.getNodeId()))
            {
                allocationInfo.setGpuTotal(gpuCount);
                allocationInfo.setGpuCount(gpuRspMap.get(allocationInfo.getNodeId()).getGpuCount());
                allocationInfo.setGpuName(gpuRspMap.get(allocationInfo.getNodeId()).getGpuName());
            }
        }

        nodeAllocationInfosRsp.setNodeAllocationInfos(nodeAllocationInfos);

        nodeAllocationInfos.forEach(hypervisorNode -> cloudService.syncSingleData(cloudId, hypervisorNode.getNodeId(), SyncMsg.NS_HYPERVISOR_NODE));

        return nodeAllocationInfosRsp;
    }

    public HypervisorNodeDetailInfoRsp getHypervisorNode(String cloudId, String nodeId) throws WebSystemException
    {
        TblCmpHypervisorNode tblCmpHypervisorNode = vmComputeRepository.getHypervisorNodeById(cloudId, nodeId);
        if (null == tblCmpHypervisorNode || tblCmpHypervisorNode.getEeStatus() == REMOVED)
        {
            LOGGER.info("get hypervisor node error: not exists, hypervisorNodeId: {}", nodeId);
            throw new WebSystemException(ErrorCode.HYPERVISOR_NODE_NOT_EXIST,ErrorLevel.INFO);
        }
        HypervisorNodeDetailInfoRsp getHypervisorNodeDetailInfoRsp = new HypervisorNodeDetailInfoRsp();
        getHypervisorNodeDetailInfoRsp.setHasGpu(false);
        TblCmpPciDeviceGroupExample example = new TblCmpPciDeviceGroupExample();
        TblCmpPciDeviceGroupExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andNodeIdEqualTo(nodeId);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        if (vmComputeRepository.countPciDeviceGroupsByExample(example) > 0)
        {
            getHypervisorNodeDetailInfoRsp.setHasGpu(true);
        }
        getHypervisorNodeDetailInfoRsp.setHypervisorNodeDetailInfo(tblCmpHypervisorNode);

        cloudService.syncSingleData(cloudId, nodeId, SyncMsg.NS_HYPERVISOR_NODE);

        return getHypervisorNodeDetailInfoRsp;
    }

    public ResponseEntity removeHypervisorNode(String cloudId, String nodeId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpHypervisorNode tblCmpHypervisorNode = vmComputeRepository.getHypervisorNodeById(cloudId, nodeId);

            if (tblCmpHypervisorNode == null)
            {
                LOGGER.error("get hypervisor node null: device id {}", nodeId);
                throw new WebSystemException(ErrorCode.HYPERVISOR_NODE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (!isAdmin() && !cloudService.isOwner(cloudId, userId) && !tblCmpHypervisorNode.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpHypervisorNode.setEeStatus(CommonPhaseStatus.REMOVED);
                tblCmpHypervisorNode.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove hypervisor node failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }
}
