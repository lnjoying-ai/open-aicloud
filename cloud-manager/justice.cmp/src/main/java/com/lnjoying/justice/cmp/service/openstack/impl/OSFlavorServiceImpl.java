package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNovaRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSFlavorCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSFlavorUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.FlavorService;
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
public class OSFlavorServiceImpl implements FlavorService
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

    public OSFlavorsRsp getFlavors(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId) throws WebSystemException
    {
        OSFlavorsRsp getFlavorsRsp = new OSFlavorsRsp();
        List<TblCmpOsFlavors> flavors = osNovaRepository.getFlavors(getTblCmpOsFlavorsExample(cloudId, flavorSearchCritical, userId));
        if (null == flavors) {
            return getFlavorsRsp;
        }

        List<OSFlavorBasicInfo> flavorBasicInfos = flavors.stream().map(tblCmpOsFlavors -> {
            OSFlavorBasicInfo flavorBasicInfo = new OSFlavorBasicInfo();
            flavorBasicInfo.setFlavorBasicInfo(tblCmpOsFlavors);
            return flavorBasicInfo;
        }).collect(Collectors.toList());

        getFlavorsRsp.setFlavors(flavorBasicInfos);
        return getFlavorsRsp;
    }

    public ResponseEntity<Object> addFlavor(String cloudId, OSFlavorCreateReq addFlavorReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addFlavorReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSFlavorWithDetailsRsp flavorWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSFlavorWithDetailsRsp.class);

                if (flavorWithDetailsRsp == null || flavorWithDetailsRsp.getFlavor() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFlavorToDB(cloudId, flavorWithDetailsRsp.getFlavor(), bpId, userId);
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

    public OSFlavorsWithDetailsRsp getFlavorsWithDetails(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId) throws WebSystemException
    {
        OSFlavorsWithDetailsRsp getFlavorsWithDetailRsp = new OSFlavorsWithDetailsRsp();
        List<TblCmpOsFlavors> flavors = osNovaRepository.getFlavors(getTblCmpOsFlavorsExample(cloudId, flavorSearchCritical, userId));
        if (null == flavors) {
            return getFlavorsWithDetailRsp;
        }

        List<OSFlavorInfo> flavorInfos = flavors.stream().map(tblCmpOsFlavors -> {
            OSFlavorInfo flavorInfo = new OSFlavorInfo();
            flavorInfo.setFlavorInfo(tblCmpOsFlavors);
            flavorInfo.setFlavorInfoExtraSpecs(getTblCmpOsFlavorExtraSpecs(cloudId, flavorInfo.getId()));
            return flavorInfo;
        }).collect(Collectors.toList());

        getFlavorsWithDetailRsp.setFlavors(flavorInfos);
        return getFlavorsWithDetailRsp;
    }

    public OSFlavorWithDetailsRsp getFlavor(String cloudId, String flavorId) throws WebSystemException
    {
        TblCmpOsFlavors tblCmpOsFlavors = osNovaRepository.getFlavorById(cloudId, flavorId);
        if (null == tblCmpOsFlavors)
        {
            throw new WebSystemException(ErrorCode.FLAVOR_NOT_EXIST, ErrorLevel.INFO);
        }
        OSFlavorWithDetailsRsp osFlavorWithDetailsRsp = new OSFlavorWithDetailsRsp();
        OSFlavorInfo flavorInfo = new OSFlavorInfo();
        flavorInfo.setFlavorInfo(tblCmpOsFlavors);
        flavorInfo.setFlavorInfoExtraSpecs(getTblCmpOsFlavorExtraSpecs(cloudId, flavorId));
        osFlavorWithDetailsRsp.setFlavor(flavorInfo);

        cloudService.syncSingleData(cloudId, flavorId, SyncMsg.OS_FLAVOR);

        return osFlavorWithDetailsRsp;
    }

    public ResponseEntity<Object> updateFlavor(String cloudId, String flavorId, OSFlavorUpdateReq osFlavorUpdateReq) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(osFlavorUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSFlavorWithDetailsRsp flavorWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSFlavorWithDetailsRsp.class);

                if (flavorWithDetailsRsp == null || flavorWithDetailsRsp.getFlavor() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFlavorToDB(cloudId, flavorWithDetailsRsp.getFlavor(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeFlavor(String cloudId, String flavorId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsFlavors tblCmpFlavor = osNovaRepository.getFlavorById(cloudId, flavorId);
            if (tblCmpFlavor == null)
            {
                LOGGER.error("get flavor null: flavor id {}", flavorId);
                throw new WebSystemException(ErrorCode.FLAVOR_NOT_EXIST, ErrorLevel.INFO);
            }
            if (!isAdmin() && !cloudService.isOwner(cloudId, userId) && !tblCmpFlavor.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpFlavor.setEeStatus(REMOVED);
                osNovaRepository.updateFlavor(tblCmpFlavor);
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

    private TblCmpOsFlavorsExample getTblCmpOsFlavorsExample(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsFlavorsExample example = new TblCmpOsFlavorsExample();
        TblCmpOsFlavorsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(flavorSearchCritical.getSortKey()) && StringUtils.isNotEmpty(flavorSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", flavorSearchCritical.getSortKey(), flavorSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }

        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andEeUserEqualTo(userId);
            criteria.andIsPublicEqualTo((short) 1);
            criteria.andDisabledEqualTo((short) 0);
        }
        else
        {
            if (StringUtils.isNumeric(flavorSearchCritical.getIsPublic()))
            {
                criteria.andIsPublicEqualTo(Short.parseShort(flavorSearchCritical.getIsPublic()));
            }
        }

        if (flavorSearchCritical.getMinRam() != null) criteria.andMemoryMbGreaterThanOrEqualTo(flavorSearchCritical.getMinRam());
        if (flavorSearchCritical.getMinDisk() != null) criteria.andRootGbGreaterThanOrEqualTo(flavorSearchCritical.getMinDisk());

        return example;
    }

    private List<TblCmpOsFlavorExtraSpecs> getTblCmpOsFlavorExtraSpecs(String cloudId, String flavorId) throws WebSystemException
    {
        TblCmpOsFlavorExtraSpecsExample example = new TblCmpOsFlavorExtraSpecsExample();
        TblCmpOsFlavorExtraSpecsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andFlavorIdEqualTo(flavorId);
        List<TblCmpOsFlavorExtraSpecs> flavorExtraSpecs = osNovaRepository.getFlavorExtraSpecs(example);
        return flavorExtraSpecs;
    }

    public OSExtFlavorsWithDetailsRsp getFlavorsWithDetailsPage(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId) throws WebSystemException
    {
        OSExtFlavorsWithDetailsRsp getFlavorsWithDetailRsp = new OSExtFlavorsWithDetailsRsp();

        PageHelper.startPage(flavorSearchCritical.getPageNum(), flavorSearchCritical.getPageSize());
        List<TblCmpOsFlavors> flavors = osNovaRepository.getFlavors(getTblCmpOsFlavorsExample(cloudId, flavorSearchCritical, userId));
        PageInfo<TblCmpOsFlavors> pageInfo = new PageInfo<>(flavors);

        if (null == flavors) {
            return getFlavorsWithDetailRsp;
        }

        List<OSFlavorInfo> flavorInfos = flavors.stream().map(tblCmpOsFlavors -> {
            OSFlavorInfo flavorInfo = new OSFlavorInfo();
            flavorInfo.setFlavorInfo(tblCmpOsFlavors);
            flavorInfo.setFlavorInfoExtraSpecs(getTblCmpOsFlavorExtraSpecs(cloudId, flavorInfo.getId()));
            return flavorInfo;
        }).collect(Collectors.toList());

        getFlavorsWithDetailRsp.setFlavors(flavorInfos);
        getFlavorsWithDetailRsp.setTotalNum(pageInfo.getTotal());
        return getFlavorsWithDetailRsp;
    }
}
