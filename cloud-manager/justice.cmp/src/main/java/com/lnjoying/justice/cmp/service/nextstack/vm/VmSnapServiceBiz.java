package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.RepoRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.*;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.VmSnapSearchCritical;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.SnapCreateReq;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.utils.DateUtils;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
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

@Service
public class VmSnapServiceBiz
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VmSnapServiceBiz.class);

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepoRepository repoRepository;

    public ResponseEntity addVmSnap(String cloudId, SnapCreateReq addSnapReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            checkVmAndVolumeChargeType(cloudId, addSnapReq.getVmInstanceId());

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add vm snap error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String snapId = (String) resultMap.get("snapId");
                    if (StringUtils.isEmpty(snapId))
                    {
                        LOGGER.error("add vm snap error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVmSnap tblCmpVmSnap = new TblCmpVmSnap();
                        tblCmpVmSnap.setSnapId(snapId);
                        tblCmpVmSnap.setCloudId(cloudId);
                        tblCmpVmSnap.setEeBp(bpId);
                        tblCmpVmSnap.setEeUser(userId);
                        tblCmpVmSnap.setEeStatus(SYNCING);
                        try
                        {
                            vmComputeRepository.insertVmSnap(tblCmpVmSnap);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVmSnap updateTblCmpVmSnap = vmComputeRepository.getVmSnapById(cloudId, snapId);
                            updateTblCmpVmSnap.setEeBp(bpId);
                            updateTblCmpVmSnap.setEeUser(userId);
                            updateTblCmpVmSnap.setEeStatus(SYNCING);
                            vmComputeRepository.updateVmSnap(updateTblCmpVmSnap);
                        }

                        cloudService.syncSingleData(cloudId, snapId, SyncMsg.NS_SNAP);
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
            LOGGER.error("add vm snap failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkVmAndVolumeChargeType(String cloudId, String instanceId)
    {
        TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
        if (tblCmpVmInstance == null || tblCmpVmInstance.getEeStatus() == REMOVED)
        {
            LOGGER.error("add vm snap error");
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        TblCmpVolumeExample example = new TblCmpVolumeExample();
        TblCmpVolumeExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andVmIdEqualTo(instanceId);
        criteria.andImageIdIsNull();
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        List<TblCmpVolume> volumes = repoRepository.getVolumes(example);
        if (tblCmpVmInstance.getChargeType() == null || CollectionUtils.isEmpty(volumes))
        {
            return;
        }
        else if (tblCmpVmInstance.getChargeType() == 1)
        {
            if (tblCmpVmInstance.getExpireTime() == null)
            {
                tblCmpVmInstance.setExpireTime(DateUtils.getExpireData(tblCmpVmInstance.getCreateTime(), tblCmpVmInstance.getChargeType(), tblCmpVmInstance.getPriceUnit(), tblCmpVmInstance.getPeriod()));
            }
            for (TblCmpVolume tblCmpVolume : volumes)
            {
                if (tblCmpVolume.getChargeType() != null && tblCmpVolume.getChargeType() == 1)
                {
                    if (tblCmpVolume.getExpireTime() == null)
                    {
                        tblCmpVolume.setExpireTime(DateUtils.getExpireData(tblCmpVolume.getCreateTime(), tblCmpVolume.getChargeType(), tblCmpVolume.getPriceUnit(), tblCmpVolume.getPeriod()));
                    }

                    if (DateUtils.diff(tblCmpVmInstance.getExpireTime(), tblCmpVolume.getExpireTime()) > 5 * 60 * 1000)
                    {
                        LOGGER.error("add vm snap error");
                        throw new WebSystemException(ErrorCode.EXPIRE_TIME_CONFLICT, ErrorLevel.INFO);
                    }
                }
            }
        }
        else if (tblCmpVmInstance.getChargeType() == 2)
        {
            for (TblCmpVolume tblCmpVolume : volumes)
            {
                if (tblCmpVolume.getChargeType() != null && tblCmpVolume.getChargeType() == 1)
                {
                    LOGGER.error("add vm snap error");
                    throw new WebSystemException(ErrorCode.EXPIRE_TIME_CONFLICT, ErrorLevel.INFO);
                }
            }
        }
    }

    public VmSnapsRsp getSnaps(String cloudId, VmSnapSearchCritical vmSnapSearchCritical, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVmSnapExample example = new TblCmpVmSnapExample();
            TblCmpVmSnapExample.Criteria criteria= example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (!StringUtils.isBlank(vmSnapSearchCritical.getVmSnapId()))
            {
                criteria.andSnapIdEqualTo(vmSnapSearchCritical.getVmSnapId());
            }
            else if (!StringUtils.isBlank(vmSnapSearchCritical.getName()))
            {
                criteria.andNameLike("%" + vmSnapSearchCritical.getName() + "%");
            }
            else if (!StringUtils.isBlank(vmSnapSearchCritical.getVmInstanceId()))
            {
                criteria.andVmInstanceIdEqualTo(vmSnapSearchCritical.getVmInstanceId());
            }
            if (userId != null)
            {
                criteria.andEeUserEqualTo(userId);
            }
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            long totalNum = vmComputeRepository.countVmSnapsByExample(example);
            VmSnapsRsp getSnapsRsp = new VmSnapsRsp();
            getSnapsRsp.setTotalNum(totalNum);
            if(totalNum < 1)
            {
                return getSnapsRsp;
            }

            int begin = ((vmSnapSearchCritical.getPageNum() - 1) * vmSnapSearchCritical.getPageSize());
            example.setOrderByClause("create_time desc, snap_id desc");

            example.setStartRow(begin);
            example.setPageSize(vmSnapSearchCritical.getPageSize());
            List<TblCmpVmSnap> tblCmpVmSnaps = vmComputeRepository.getVmSnaps(example);

            List<VmSnapInfo> getSnapInfos = tblCmpVmSnaps.stream().map(
                    tblVmSnap -> {
                        VmSnapInfo getSnapInfo = new VmSnapInfo();
                        String vmName = vmComputeRepository.getVmInstanceById(cloudId, tblVmSnap.getVmInstanceId()).getName();
                        getSnapInfo.setVmInstanceName(vmName);
                        getSnapInfo.setSnapInfo(tblVmSnap);
                        getSnapInfo.setEeBpName(userService.getBpName(tblVmSnap.getEeBp()));
                        getSnapInfo.setEeUserName(userService.getUserName(tblVmSnap.getEeUser()));
                        return getSnapInfo;
                    }
            ).collect(Collectors.toList());
            getSnapsRsp.setSnaps(getSnapInfos);

            getSnapInfos.forEach(vmSnapInfo -> cloudService.syncSingleData(cloudId, vmSnapInfo.getSnapId(), SyncMsg.NS_SNAP));

            return getSnapsRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get vm snaps failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public VmSnapDetailInfoRsp getSnap(String cloudId, String snapId, String userId) throws WebSystemException
    {
        TblCmpVmSnap tblCmpVmSnap = vmComputeRepository.getVmSnapById(cloudId, snapId);
        if (null == tblCmpVmSnap || REMOVED == tblCmpVmSnap.getPhaseStatus() || REMOVED == tblCmpVmSnap.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VM_SNAP_NOT_EXIST,ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpVmSnap.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, tblCmpVmSnap.getVmInstanceId());
        if (null == tblCmpVmInstance || REMOVED == tblCmpVmInstance.getPhaseStatus())
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST,ErrorLevel.INFO);
        }
        VmSnapDetailInfoRsp getSnapDetailInfoRsp = new VmSnapDetailInfoRsp();
        getSnapDetailInfoRsp.setDescription(tblCmpVmSnap.getDescription());
        getSnapDetailInfoRsp.setSnapInfo(tblCmpVmSnap);
        getSnapDetailInfoRsp.setUserId(tblCmpVmSnap.getUserId());
        getSnapDetailInfoRsp.setVmInstanceName(tblCmpVmInstance.getName());
        getSnapDetailInfoRsp.setEeBpName(userService.getBpName(tblCmpVmSnap.getEeBp()));
        getSnapDetailInfoRsp.setEeUserName(userService.getUserName(tblCmpVmSnap.getEeUser()));

        cloudService.syncSingleData(cloudId, snapId, SyncMsg.NS_SNAP);
        return getSnapDetailInfoRsp;
    }

    public ResponseEntity removeSnap(String cloudId, String snapId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVmSnap tblCmpVmSnap = vmComputeRepository.getVmSnapById(cloudId, snapId);

            if (tblCmpVmSnap == null)
            {
                LOGGER.error("get vm snap null: device id {}", snapId);
                throw new WebSystemException(ErrorCode.VM_SNAP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpVmSnap.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpVmSnap.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                vmComputeRepository.updateVmSnap(tblCmpVmSnap);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove vm snap failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public List<SnapsTreeRsp> getSnapsTree(String cloudId, String vmInstanceId, String userId)
    {
        TblCmpVmInstance tblVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
        if (null == tblVmInstance || REMOVED == tblVmInstance.getPhaseStatus())
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (StringUtils.isNotBlank(userId) && !tblVmInstance.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        TblCmpVmSnapExample example = new TblCmpVmSnapExample();
        TblCmpVmSnapExample.Criteria criteria= example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andVmInstanceIdEqualTo(vmInstanceId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);

        List<TblCmpVmSnap> tblVmSnaps = vmComputeRepository.getVmSnaps(example);
        if (null == tblVmSnaps || tblVmSnaps.isEmpty())
        {
            return new ArrayList<>();
        }
        List<SnapsTreeRsp> snapsTreeRspList = new ArrayList<>();
        for (TblCmpVmSnap tblVmSnap : tblVmSnaps)
        {
            SnapsTreeRsp snapsTreeRsp = new SnapsTreeRsp();
            snapsTreeRsp.setSnapId(tblVmSnap.getSnapId());
            snapsTreeRsp.setParentId(tblVmSnap.getParentId());
            if (snapsTreeRsp.getParentId() == null)
            {
                snapsTreeRsp.setParentId("");
            }
            snapsTreeRsp.setSnapInfo(tblVmSnap);
            snapsTreeRspList.add(snapsTreeRsp);
        }
        Map<String, List<SnapsTreeRsp>> parentMap = snapsTreeRspList.stream().collect(Collectors.groupingBy(SnapsTreeRsp::getParentId));
        for(SnapsTreeRsp snapsTreeRsp : snapsTreeRspList)
        {
            if (parentMap.containsKey(snapsTreeRsp.getSnapId()))
            {
                snapsTreeRsp.setChildren(parentMap.get(snapsTreeRsp.getSnapId()));
            }
        }
        return snapsTreeRspList.stream().filter(item -> "".equals(item.getParentId())).collect(Collectors.toList());
    }

    public void checkVmSnapUser(String cloudId, String snapId, String userId)
    {
        TblCmpVmSnap tblCmpVmSnap = vmComputeRepository.getVmSnapById(cloudId, snapId);

        if (tblCmpVmSnap == null)
        {
            LOGGER.error("get vm snap null: device id {}", snapId);
            throw new WebSystemException(ErrorCode.VM_SNAP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpVmSnap.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
