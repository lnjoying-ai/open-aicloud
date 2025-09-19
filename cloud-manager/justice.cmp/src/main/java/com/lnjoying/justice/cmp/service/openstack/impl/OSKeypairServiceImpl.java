package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNovaRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSKeypairCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSKeyPairSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.KeypairService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Service
public class OSKeypairServiceImpl implements KeypairService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public OSKeyPairsRsp getKeypairs(String cloudId, OSKeyPairSearchCritical keyPairSearchCritical, String userId) throws WebSystemException
    {
        OSKeyPairsRsp keyPairsRsp = new OSKeyPairsRsp();
        List<TblCmpOsKeyPairs> keyPairs = osNovaRepository.getKeyPairs(getTblCmpOsKeyPairsExample(cloudId, keyPairSearchCritical, userId));
        if (null == keyPairs) {
            return keyPairsRsp;
        }

        List<OSKeyPairBasicInfo> keyPairsBasicInfos = keyPairs.stream().map(tblCmpOsKeyPairs -> {
            OSKeyPairBasicInfo keyPairBasicInfo = new OSKeyPairBasicInfo();
            keyPairBasicInfo.setKeyPairBasicInfo(tblCmpOsKeyPairs);
            return keyPairBasicInfo;
        }).collect(Collectors.toList());

        keyPairsRsp.setKeyPairBasicInfo(keyPairsBasicInfos);
        return keyPairsRsp;
    }

    public ResponseEntity<Object> addKeypair(String cloudId, OSKeypairCreateReq addFlavorReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            String eeName = addFlavorReq.getKeypair().getName();
            addFlavorReq.getKeypair().setName(eeName + "_" + userId);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addFlavorReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSKeyPairWithDetailsRsp keyPairWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSKeyPairWithDetailsRsp.class);
                if (keyPairWithDetailsRsp == null || keyPairWithDetailsRsp.getKeypair() == null)
                {
                    return response;
                }
                eskSyncDataService.syncKeypair(tblCloudInfo, addFlavorReq.getKeypair().getName(), bpId, userId, eeName);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add keypair failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSKeyPairWithDetailsRsp getKeypairDetails(String cloudId, String keypairName, String userId) throws WebSystemException
    {
        TblCmpOsKeyPairs tblCmpOsKeyPair = osNovaRepository.getKeyPairByName(cloudId, keypairName);
        if (null == tblCmpOsKeyPair)
        {
            throw new WebSystemException(ErrorCode.KEYPAIR_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsKeyPair.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSKeyPairWithDetailsRsp keyPairWithDetailsRsp = new OSKeyPairWithDetailsRsp();
        OSKeyPairInfo keyPairInfo = new OSKeyPairInfo();
        keyPairInfo.setKeyPairInfo(tblCmpOsKeyPair);
        keyPairWithDetailsRsp.setKeypair(keyPairInfo);

        cloudService.syncSingleData(cloudId, keypairName, SyncMsg.OS_KEYPAIR);

        return keyPairWithDetailsRsp;
    }

    public ResponseEntity<Object> removeKeypair(String cloudId, String keypairName, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsKeyPairs tblCmpOsKeyPair = osNovaRepository.getKeyPairByName(cloudId, keypairName);
            if (null == tblCmpOsKeyPair)
            {
                LOGGER.error("get keypair null: keypair name {}", keypairName);
                throw new WebSystemException(ErrorCode.KEYPAIR_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsKeyPair.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsKeyPair.setEeStatus(REMOVED);
                osNovaRepository.updateKeyPair(tblCmpOsKeyPair);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove keypair failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsKeyPairsExample getTblCmpOsKeyPairsExample(String cloudId, OSKeyPairSearchCritical keyPairSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsKeyPairsExample example = new TblCmpOsKeyPairsExample();
        TblCmpOsKeyPairsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("created_at desc");

        if (StringUtils.isNotEmpty(keyPairSearchCritical.getUserId())) criteria.andUserIdEqualTo(keyPairSearchCritical.getUserId());

        if (StringUtils.isNotEmpty(keyPairSearchCritical.getName())) criteria.andEeNameLike("%" + keyPairSearchCritical.getName() + "%");

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public OSExtKeyPairsRsp getKeypairsPage(String cloudId, OSKeyPairSearchCritical keyPairSearchCritical, String userId) throws WebSystemException
    {
        OSExtKeyPairsRsp keyPairsRsp = new OSExtKeyPairsRsp();

        PageHelper.startPage(keyPairSearchCritical.getPageNum(), keyPairSearchCritical.getPageSize());
        List<TblCmpOsKeyPairs> keyPairs = osNovaRepository.getKeyPairs(getTblCmpOsKeyPairsExample(cloudId, keyPairSearchCritical, userId));
        PageInfo<TblCmpOsKeyPairs> pageInfo = new PageInfo<>(keyPairs);

        if (null == keyPairs) {
            return keyPairsRsp;
        }

        List<OSKeyPairBasicInfo> keyPairsBasicInfos = keyPairs.stream().map(tblCmpOsKeyPairs -> {
            OSKeyPairBasicInfo keyPairBasicInfo = new OSKeyPairBasicInfo();
            keyPairBasicInfo.setKeyPairBasicInfo(tblCmpOsKeyPairs);
            return keyPairBasicInfo;
        }).collect(Collectors.toList());

        keyPairsRsp.setKeyPairBasicInfo(keyPairsBasicInfos);
        keyPairsRsp.setTotalNum(pageInfo.getTotal());
        return keyPairsRsp;
    }
}
