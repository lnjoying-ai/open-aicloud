package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SecurityGroupService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
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

@Service
public class OSSecurityGroupServiceImpl implements SecurityGroupService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public OSSecurityGroupsWithDetailsRsp getSecurityGroups(String cloudId, OSSecurityGroupSearchCritical securityGroupSearchCritical, String userId) throws WebSystemException
    {
        OSSecurityGroupsWithDetailsRsp securityGroupsWithDetailsRsp = new OSSecurityGroupsWithDetailsRsp();
        List<TblCmpOsSecuritygroups> securitygroups = osNeutronRepository.getSecuritygroups(getTblCmpOsSecuritygroupsExample(cloudId, securityGroupSearchCritical, userId));
        if (null == securitygroups) {
            return securityGroupsWithDetailsRsp;
        }

        List<OSSecurityGroupInfo> securityGroupInfos = securitygroups.stream().map(tblCmpOsSecuritygroup -> {
            OSSecurityGroupInfo securityGroupInfo = new OSSecurityGroupInfo();
            securityGroupInfo.setSecurityGroupInfo(tblCmpOsSecuritygroup);
            securityGroupInfo.setSecurityGroupRule(getTblCmpOsSecuritygrouprules(cloudId, tblCmpOsSecuritygroup.getId()));
            return securityGroupInfo;
        }).collect(Collectors.toList());

        securityGroupInfos.forEach(securityGroupInfo -> cloudService.syncSingleData(cloudId, securityGroupInfo.getId(), SyncMsg.OS_SECURITY_GROUP));

        securityGroupsWithDetailsRsp.setSecurityGroups(securityGroupInfos);
        return securityGroupsWithDetailsRsp;
    }

    public ResponseEntity<Object> addSecurityGroup(String cloudId, OSSecurityGroupCreateReq securityGroupCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            String eeName = securityGroupCreateReq.getSecurityGroup().getName();
            securityGroupCreateReq.getSecurityGroup().setName(eeName + "_" + userId);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(securityGroupCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSSecurityGroupWithDetailsRsp securityGroupWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSSecurityGroupWithDetailsRsp.class);
                if (securityGroupWithDetailsRsp == null || securityGroupWithDetailsRsp.getSecurityGroup() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSecurityGroupToDB(cloudId, securityGroupWithDetailsRsp.getSecurityGroup(), bpId, userId, eeName);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSSecurityGroupWithDetailsRsp getSecurityGroupDetails(String cloudId, String securityGroupId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsSecuritygroups tblCmpOsSecuritygroup = osNeutronRepository.getSecuritygroupById(cloudId, securityGroupId);
        if (null == tblCmpOsSecuritygroup)
        {
            throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsSecuritygroup.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSSecurityGroupWithDetailsRsp osSecurityGroupWithDetailsRsp = new OSSecurityGroupWithDetailsRsp();
        OSSecurityGroupInfo securityGroupInfo = new OSSecurityGroupInfo();
        securityGroupInfo.setSecurityGroupInfo(tblCmpOsSecuritygroup);
        securityGroupInfo.setSecurityGroupRule(getTblCmpOsSecuritygrouprules(cloudId, securityGroupId));
        osSecurityGroupWithDetailsRsp.setSecurityGroup(securityGroupInfo);

        cloudService.syncSingleData(cloudId, securityGroupId, SyncMsg.OS_SECURITY_GROUP);

        return osSecurityGroupWithDetailsRsp;
    }

    public ResponseEntity<Object> updateSecurityGroup(String cloudId, String securityGroupId, OSSecurityGroupUpdateReq securityGroupUpdateReq, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSecuritygroups tblCmpOsSecuritygroup = osNeutronRepository.getSecuritygroupById(cloudId, securityGroupId);
            if (null == tblCmpOsSecuritygroup)
            {
                throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSecuritygroup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            String eeName = securityGroupUpdateReq.getSecurityGroup().getName();
            securityGroupUpdateReq.getSecurityGroup().setName(eeName + "_" + userId);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(securityGroupUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSSecurityGroupWithDetailsRsp securityGroupWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSSecurityGroupWithDetailsRsp.class);
                if (securityGroupWithDetailsRsp == null || securityGroupWithDetailsRsp.getSecurityGroup() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSecurityGroupToDB(cloudId, securityGroupWithDetailsRsp.getSecurityGroup(), null, null, eeName);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update security group failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeSecurityGroup(String cloudId, String securityGroupId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSecuritygroups tblCmpOsSecuritygroup = osNeutronRepository.getSecuritygroupById(cloudId, securityGroupId);
            if (tblCmpOsSecuritygroup == null)
            {
                LOGGER.error("get security group null: security group id {}", securityGroupId);
                throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSecuritygroup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsSecuritygroup.setEeStatus(REMOVED);
                osNeutronRepository.updateSecuritygroup(tblCmpOsSecuritygroup);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsSecuritygroupsExample getTblCmpOsSecuritygroupsExample(String cloudId, OSSecurityGroupSearchCritical securityGroupSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsSecuritygroupsExample example = new TblCmpOsSecuritygroupsExample();
        TblCmpOsSecuritygroupsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(securityGroupSearchCritical.getId())) criteria.andIdEqualTo(securityGroupSearchCritical.getId());
        if (StringUtils.isNotEmpty(securityGroupSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(securityGroupSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(securityGroupSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(securityGroupSearchCritical.getProjectId());
        }
        if (securityGroupSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(securityGroupSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(securityGroupSearchCritical.getName())) criteria.andNameLike("%" + securityGroupSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(securityGroupSearchCritical.getDescription())) criteria.andDescriptionEqualTo(securityGroupSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(securityGroupSearchCritical.getSortKey()) && StringUtils.isNotEmpty(securityGroupSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", securityGroupSearchCritical.getSortKey(), securityGroupSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }
        if (securityGroupSearchCritical.getShared() != null) criteria.andSharedEqualTo(BoolUtil.bool2Short(securityGroupSearchCritical.getShared()));

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public List<TblCmpOsSecuritygrouprules> getTblCmpOsSecuritygrouprules(String cloudId, String securityGroupId)
    {
        TblCmpOsSecuritygrouprulesExample example = new TblCmpOsSecuritygrouprulesExample();
        TblCmpOsSecuritygrouprulesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSecurityGroupIdEqualTo(securityGroupId);
        List<TblCmpOsSecuritygrouprules> securitygrouprules = osNeutronRepository.getSecuritygrouprules(example);
        return securitygrouprules;
    }

    public OSExtSecurityGroupsWithDetailsRsp getSecurityGroupsPage(String cloudId, OSSecurityGroupSearchCritical securityGroupSearchCritical, String userId) throws WebSystemException
    {
        OSExtSecurityGroupsWithDetailsRsp securityGroupsWithDetailsRsp = new OSExtSecurityGroupsWithDetailsRsp();

        PageHelper.startPage(securityGroupSearchCritical.getPageNum(), securityGroupSearchCritical.getPageSize());
        List<TblCmpOsSecuritygroups> securitygroups = osNeutronRepository.getSecuritygroups(getTblCmpOsSecuritygroupsExample(cloudId, securityGroupSearchCritical, userId));
        PageInfo<TblCmpOsSecuritygroups> pageInfo = new PageInfo<>(securitygroups);

        if (null == securitygroups) {
            return securityGroupsWithDetailsRsp;
        }

        List<OSSecurityGroupInfo> securityGroupInfos = securitygroups.stream().map(tblCmpOsSecuritygroup -> {
            OSSecurityGroupInfo securityGroupInfo = new OSSecurityGroupInfo();
            securityGroupInfo.setSecurityGroupInfo(tblCmpOsSecuritygroup);
            securityGroupInfo.setSecurityGroupRule(getTblCmpOsSecuritygrouprules(cloudId, tblCmpOsSecuritygroup.getId()));
            return securityGroupInfo;
        }).collect(Collectors.toList());

        securityGroupInfos.forEach(securityGroupInfo -> cloudService.syncSingleData(cloudId, securityGroupInfo.getId(), SyncMsg.OS_SECURITY_GROUP));

        securityGroupsWithDetailsRsp.setSecurityGroups(securityGroupInfos);
        securityGroupsWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return securityGroupsWithDetailsRsp;
    }
}
