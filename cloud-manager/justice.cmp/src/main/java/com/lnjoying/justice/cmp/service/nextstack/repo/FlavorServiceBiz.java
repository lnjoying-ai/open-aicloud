package com.lnjoying.justice.cmp.service.nextstack.repo;

import com.lnjoying.justice.cmp.common.VmInstanceStatus;
import com.lnjoying.justice.cmp.common.constant.FlavorType;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.FlavorRepository;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.FlavorCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.FlavorDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.FlavorsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.AvailableGPURsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.RpcGpuFlavorInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.FlavorSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.rpc.RpcVmServiceImpl;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Service
public class FlavorServiceBiz
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    @Autowired
    private FlavorRepository flavorRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private RpcVmServiceImpl rpcVmService;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    public ResponseEntity createFlavor(String cloudId, FlavorCreateReq addFlavorReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add flavor error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String flavorId = (String) resultMap.get("flavorId");
                    if (StringUtils.isEmpty(flavorId))
                    {
                        LOGGER.error("add flavor error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpFlavor tblCmpFlavor = new TblCmpFlavor();
                        tblCmpFlavor.setFlavorId(flavorId);
                        tblCmpFlavor.setCloudId(cloudId);
                        tblCmpFlavor.setEeBp(bpId);
                        tblCmpFlavor.setEeUser(userId);
                        tblCmpFlavor.setEeStatus(SYNCING);

                        tblCmpFlavor.setCpu(addFlavorReq.getCpu());
                        tblCmpFlavor.setMem(addFlavorReq.getMem());
                        tblCmpFlavor.setName(addFlavorReq.getName());
                        tblCmpFlavor.setType(addFlavorReq.getType());
                        tblCmpFlavor.setPhaseStatus(PhaseStatus.ADDED);
                        try
                        {
                            flavorRepository.insertFlavor(tblCmpFlavor);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpFlavor updateTblCmpFlavor = flavorRepository.getFlavorById(cloudId, flavorId);
                            updateTblCmpFlavor.setEeBp(bpId);
                            updateTblCmpFlavor.setEeUser(userId);
                            updateTblCmpFlavor.setEeStatus(SYNCING);
                            flavorRepository.updateFlavor(updateTblCmpFlavor);
                        }

                        cloudService.syncSingleData(cloudId, flavorId, SyncMsg.NS_FLAVOR);
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
            LOGGER.error("add flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public FlavorsRsp getFlavors(String cloudId, FlavorSearchCritical flavorSearchCritical) throws WebSystemException
    {
        TblCmpFlavorExample example = new TblCmpFlavorExample();
        TblCmpFlavorExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(flavorSearchCritical.getFlavorName()))
        {
            criteria.andNameLike("%" + flavorSearchCritical.getFlavorName() + "%");
        }
        if (!StringUtils.isBlank(flavorSearchCritical.getUserId()))
        {
            criteria.andEeUserEqualTo(flavorSearchCritical.getUserId());
        }
        if (null == flavorSearchCritical.getFlavorType() || FlavorType.VM_FLAVOR_TYPE == flavorSearchCritical.getFlavorType())
        {
            criteria.andTypeEqualTo(FlavorType.VM_FLAVOR_TYPE);
            if (null != flavorSearchCritical.getGpu() && flavorSearchCritical.getGpu())
            {
                criteria.andGpuCountEqualTo(0);
            }
            else if (null != flavorSearchCritical.getGpu() && !flavorSearchCritical.getGpu())
            {
                criteria.andGpuCountIsNullOrEqualTo(0);
            }
        }
        else if (FlavorType.BAREMETAL_FLAVOR_TYPE == flavorSearchCritical.getFlavorType())
        {
            criteria.andTypeEqualTo(FlavorType.BAREMETAL_FLAVOR_TYPE);
        }
        else if (FlavorType.ALL_FLAVOR_TYPE != flavorSearchCritical.getFlavorType() )
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
        }

        FlavorsRsp getFlavorsRsp = new FlavorsRsp();

        long totalNum = flavorRepository.countFlavorsByExample(example);

        getFlavorsRsp.setTotalNum(totalNum);
        if (totalNum < 1) {
            return getFlavorsRsp;
        }

        int begin = ((flavorSearchCritical.getPageNum() - 1) * flavorSearchCritical.getPageSize());
        example.setOrderByClause("create_time desc,flavor_id desc");

        example.setStartRow(begin);
        example.setPageSize(flavorSearchCritical.getPageSize());
        List<TblCmpFlavor> flavors = flavorRepository.getFlavors(example);
        if (null == flavors) {
            return getFlavorsRsp;
        }

        List<String> flavorIds = new ArrayList<>();

        List<FlavorDetailInfoRsp> flavorInfos = flavors.stream().map(tblRsFlavor -> {
            FlavorDetailInfoRsp flavorInfo = new FlavorDetailInfoRsp();
            flavorInfo.setFlavorDetailInfoRsp(tblRsFlavor);
            flavorInfo.setAvailable(checkFlavorAvailable(cloudId, tblRsFlavor));
            flavorIds.add(tblRsFlavor.getFlavorId());
            return flavorInfo;
        }).collect(Collectors.toList());

        getFlavorsRsp.setFlavors(flavorInfos);

        flavorInfos.forEach(flavor -> cloudService.syncSingleData(cloudId, flavor.getFlavorId(), SyncMsg.NS_FLAVOR));

        return getFlavorsRsp;
    }

    private boolean checkFlavorAvailable(String cloudId, TblCmpFlavor tblCmpFlavor)
    {
        if (null == tblCmpFlavor.getGpuCount() || 0 == tblCmpFlavor.getGpuCount())
        {
            TblCmpHypervisorNodeExample hypervisorNodeExample = new TblCmpHypervisorNodeExample();
            TblCmpHypervisorNodeExample.Criteria hypervisorNodeCriteria = hypervisorNodeExample.createCriteria();
            hypervisorNodeCriteria.andPhaseStatusEqualTo(VmInstanceStatus.HYPERVISOR_NODE_CREATED);
            hypervisorNodeCriteria.andCloudIdEqualTo(cloudId);
            hypervisorNodeCriteria.andEeStatusNotEqualTo(REMOVED);
            long count = vmComputeRepository.countHypervisorNodeByExample(hypervisorNodeExample);

            return 0 != count;
        }
        long count = vmComputeRepository.selectTotalAvailableGPURspByNameAndCount(cloudId, tblCmpFlavor.getGpuName(), tblCmpFlavor.getGpuCount());
        if (0 == count)
        {
            return false;
        }
        Integer ibCount = vmComputeRepository.getIbCount(tblCmpFlavor.getGpuName(), tblCmpFlavor.getGpuCount(), tblCmpFlavor.getNeedIb());
        List<AvailableGPURsp> availableGPUs = vmComputeRepository.selectAvailableGPURspByNameAndCount(cloudId, tblCmpFlavor.getGpuName(), tblCmpFlavor.getGpuCount(), 0, 10, ibCount);
        if (availableGPUs.isEmpty())
        {
            return false;
        }
        return true;
    }

    public FlavorDetailInfoRsp getFlavor(String cloudId, String flavorId) throws WebSystemException
    {
        TblCmpFlavor tblCmpFlavor = flavorRepository.getFlavorById(cloudId, flavorId);
        if (null == tblCmpFlavor || tblCmpFlavor.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.FLAVOR_NOT_EXIST, ErrorLevel.INFO);
        }
        FlavorDetailInfoRsp flavorDetailInfoRsp = new FlavorDetailInfoRsp();
        flavorDetailInfoRsp.setFlavorDetailInfoRsp(tblCmpFlavor);
        return flavorDetailInfoRsp;
    }

    public ResponseEntity removeFlavor(String cloudId, String flavorId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpFlavor tblCmpFlavor = flavorRepository.getFlavorById(cloudId, flavorId);

            if (tblCmpFlavor == null)
            {
                LOGGER.error("get flavor null: flavor id {}", flavorId);
                throw new WebSystemException(ErrorCode.FLAVOR_NOT_EXIST, ErrorLevel.INFO);
            }
            if (!isAdmin() && !cloudService.isOwner(cloudId, userId) && !tblCmpFlavor.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpFlavor.setEeStatus(REMOVED);
                tblCmpFlavor.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                flavorRepository.updateFlavor(tblCmpFlavor);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public List<RpcGpuFlavorInfo>  getAvailableGpu(String cloudId)
    {
        List<RpcGpuFlavorInfo> gpuFlavorInfos = rpcVmService.getGpuFlavorInfos(cloudId);
        if (null == gpuFlavorInfos || gpuFlavorInfos.size() < 1)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
        }
        Set<RpcGpuFlavorInfo> setWithoutDuplicates = new HashSet<>(gpuFlavorInfos);
        List<RpcGpuFlavorInfo> gpuInfos = setWithoutDuplicates.stream().map(gpuFlavorInfo -> {
            RpcGpuFlavorInfo gpuInfo = new RpcGpuFlavorInfo();
            gpuInfo.setGpuName(gpuFlavorInfo.getGpuName());
            gpuInfo.setGpuCount(gpuFlavorInfo.getGpuCount());
            return gpuInfo;
        }).collect(Collectors.toList());
        return gpuInfos;
    }
}
