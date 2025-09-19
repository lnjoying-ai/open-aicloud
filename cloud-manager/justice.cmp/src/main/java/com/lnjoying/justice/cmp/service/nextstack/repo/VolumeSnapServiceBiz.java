package com.lnjoying.justice.cmp.service.nextstack.repo;

import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.db.model.TblCmpVolume;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnap;
import com.lnjoying.justice.cmp.db.model.TblCmpVolumeSnapExample;
import com.lnjoying.justice.cmp.db.repo.RepoRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo.VolumeSnapCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.SnapsTreeRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeSnapDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.VolumeSnapsRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.repo.VolumeSnapSearchCritical;
import com.lnjoying.justice.cmp.service.UserService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;

@Service("volumeSnapServiceBiz")
public class VolumeSnapServiceBiz
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private UserService userService;

    public Object getVolumeSnaps(String cloudId, VolumeSnapSearchCritical volumeSnapSearchCritical, String userId) throws WebSystemException
    {
        TblCmpVolumeSnapExample example = new TblCmpVolumeSnapExample();
        TblCmpVolumeSnapExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(volumeSnapSearchCritical.getVolumeSnapName()))
        {
            criteria.andNameLike("%" + volumeSnapSearchCritical.getVolumeSnapName() + "%");
        }
        if (null != volumeSnapSearchCritical.getVolumeId())
        {
            criteria.andVolumeIdEqualTo(volumeSnapSearchCritical.getVolumeId());
        }
        if (!StringUtils.isBlank(userId))
        {
            criteria.andEeUserEqualTo(userId);
        }

        VolumeSnapsRsp getVolumeSnapsRsp = new VolumeSnapsRsp();

        long totalNum = repoRepository.countVolumeSnaps(example);

        getVolumeSnapsRsp.setTotalNum(totalNum);
        if (totalNum < 1) {
            return getVolumeSnapsRsp;
        }

        int begin = ((volumeSnapSearchCritical.getPageNum() - 1) * volumeSnapSearchCritical.getPageSize());
        example.setOrderByClause("create_time desc, volume_snap_id desc");

        example.setStartRow(begin);
        example.setPageSize(volumeSnapSearchCritical.getPageSize());

        List<VolumeSnapDetailInfoRsp> volumeSnapDetailInfos = repoRepository.selectVolumeSnaps(example);



        getVolumeSnapsRsp.setVolumeSnaps(volumeSnapDetailInfos);

        volumeSnapDetailInfos.forEach(volumeSnap -> {
            volumeSnap.setEeBpName(userService.getBpName(volumeSnap.getEeBp()));
            volumeSnap.setEeUserName(userService.getUserName(volumeSnap.getEeUser()));
        });

        volumeSnapDetailInfos.forEach(volumeSnap -> cloudService.syncSingleData(cloudId, volumeSnap.getVolumeSnapId(), SyncMsg.NS_VOLUME_SNAP));

        return getVolumeSnapsRsp;
    }

    public Object getVolumeSnap(String cloudId, String volumeSnapId, String userId) throws WebSystemException
    {
        TblCmpVolumeSnap volumeSnap = repoRepository.getVolumeSnapById(cloudId, volumeSnapId);
        if (null == volumeSnap || REMOVED == volumeSnap.getPhaseStatus() || REMOVED == volumeSnap.getEeStatus()) {
            throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (!StringUtils.isBlank(userId) && !userId.equals(volumeSnap.getEeUser()))
        {
            throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
        }

        TblCmpVolume tblVolume = repoRepository.getVolumeById(cloudId, volumeSnap.getVolumeId());

        VolumeSnapDetailInfoRsp volumeSnapDetailInfoRsp = new VolumeSnapDetailInfoRsp();
        volumeSnapDetailInfoRsp.setVolumeSnapDetailInfoRsp(volumeSnap);
        volumeSnapDetailInfoRsp.setVolumeName(tblVolume.getName());
        volumeSnapDetailInfoRsp.setStoragePoolId(tblVolume.getStoragePoolId());
        volumeSnapDetailInfoRsp.setEeBpName(userService.getBpName(tblVolume.getEeBp()));
        volumeSnapDetailInfoRsp.setEeUserName(userService.getUserName(tblVolume.getEeBp()));

        cloudService.syncSingleData(cloudId, volumeSnapId, SyncMsg.NS_VOLUME_SNAP);

        return volumeSnapDetailInfoRsp;
    }

    public ResponseEntity removeVolumeSnap(String cloudId, String volumeSnapId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVolumeSnap tblCmpVolumeSnap = repoRepository.getVolumeSnapById(cloudId, volumeSnapId);

            if (tblCmpVolumeSnap == null)
            {
                LOGGER.error("get volume snap null: volume snap id {}", volumeSnapId);
                throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpVolumeSnap.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpVolumeSnap.setEeStatus(REMOVED);
                tblCmpVolumeSnap.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                repoRepository.updateVolumeSnap(tblCmpVolumeSnap);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove volume snap failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity createVolumeSnap(String cloudId, VolumeSnapCreateReq volumeSnapInfo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add volume snap error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String volumeSnapId = (String) resultMap.get("volumeSnapId");
                    if (StringUtils.isEmpty(volumeSnapId))
                    {
                        LOGGER.error("add volume snap error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVolumeSnap tblCmpVolumeSnap = new TblCmpVolumeSnap();
                        tblCmpVolumeSnap.setVolumeSnapId(volumeSnapId);
                        tblCmpVolumeSnap.setCloudId(cloudId);
                        tblCmpVolumeSnap.setEeBp(bpId);
                        tblCmpVolumeSnap.setEeUser(userId);
                        tblCmpVolumeSnap.setEeStatus(SYNCING);

                        tblCmpVolumeSnap.setName(volumeSnapInfo.getName());
                        tblCmpVolumeSnap.setDescription(volumeSnapInfo.getDescription());
                        tblCmpVolumeSnap.setPhaseStatus(PhaseStatus.ADDING);
                        try
                        {
                            repoRepository.insertVolumeSnap(tblCmpVolumeSnap);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVolumeSnap updateTblCmpVolumeSnap = repoRepository.getVolumeSnapById(cloudId, volumeSnapId);
                            updateTblCmpVolumeSnap.setEeBp(bpId);
                            updateTblCmpVolumeSnap.setEeUser(userId);
                            updateTblCmpVolumeSnap.setEeStatus(SYNCING);
                            repoRepository.updateVolumeSnap(updateTblCmpVolumeSnap);
                        }

                        cloudService.syncSingleData(cloudId, volumeSnapId, SyncMsg.NS_VOLUME_SNAP);
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
            LOGGER.error("add volume snap failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public List<SnapsTreeRsp> getSnapsTree(String cloudId, String volumeId, String userId)
    {
        TblCmpVolume tblVolume = repoRepository.getVolumeById(cloudId, volumeId);
        if (null == tblVolume || REMOVED == tblVolume.getPhaseStatus())
        {
            throw new WebSystemException(ErrorCode.VOLUME_NOT_EXIST, ErrorLevel.INFO);
        }
        if (StringUtils.isNotBlank(userId) && !tblVolume.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        TblCmpVolumeSnapExample example = new TblCmpVolumeSnapExample();
        TblCmpVolumeSnapExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andVolumeIdEqualTo(volumeId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);

        List<TblCmpVolumeSnap> tblVolumeSnaps = repoRepository.getVolumeSnaps(example);
        if (null == tblVolumeSnaps || tblVolumeSnaps.isEmpty())
        {
            return new ArrayList<>();
        }
        List<SnapsTreeRsp> snapsTreeRspList = new ArrayList<>();
        for (TblCmpVolumeSnap tblVolumeSnap : tblVolumeSnaps)
        {
            cloudService.syncSingleData(cloudId, tblVolumeSnap.getVolumeSnapId(), SyncMsg.NS_VOLUME_SNAP);
            if (tblVolumeSnap.getParentId() == null) continue;
            SnapsTreeRsp snapsTreeRsp = new SnapsTreeRsp();
            snapsTreeRsp.setVolumeSnapDetailInfoRsp(tblVolumeSnap);
            snapsTreeRspList.add(snapsTreeRsp);
        }
        Map<String, List<SnapsTreeRsp>> parentMap = snapsTreeRspList.stream().collect(Collectors.groupingBy(SnapsTreeRsp::getParentId));
        for(SnapsTreeRsp snapsTreeRsp : snapsTreeRspList)
        {
            if (parentMap.containsKey(snapsTreeRsp.getVolumeSnapId()))
            {
                snapsTreeRsp.setChildren(parentMap.get(snapsTreeRsp.getVolumeSnapId()));
            }
        }
        return snapsTreeRspList.stream().filter(item -> "".equals(item.getParentId())).collect(Collectors.toList());
    }

    public void checkVolumeSnapUser(String cloudId, String volumeSnapId, String userId) throws WebSystemException
    {
        TblCmpVolumeSnap volumeSnap = repoRepository.getVolumeSnapById(cloudId, volumeSnapId);
        if (null == volumeSnap || REMOVED == volumeSnap.getPhaseStatus() || REMOVED == volumeSnap.getEeStatus()) {
            throw new WebSystemException(ErrorCode.VOLUME_SNAP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !userId.equals(volumeSnap.getEeUser()))
        {
            throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.INFO);
        }
    }
}
