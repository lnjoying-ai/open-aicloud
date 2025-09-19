package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSecurityGroupRuleCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSecurityGroupRuleSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SecurityGroupRuleService;
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

@Service
public class OSSecurityGroupRuleServiceImpl implements SecurityGroupRuleService
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

    public OSSecurityGroupRulesWithDetailsRsp getSecurityGroupRules(String cloudId, OSSecurityGroupRuleSearchCritical securityGroupRuleSearchCritical, String userId) throws WebSystemException
    {
        OSSecurityGroupRulesWithDetailsRsp securityGroupRulesWithDetailsRsp = new OSSecurityGroupRulesWithDetailsRsp();
        List<TblCmpOsSecuritygrouprules> securitygrouprules = osNeutronRepository.getSecuritygrouprules(getTblCmpOsSecurityGroupRulesExample(cloudId, securityGroupRuleSearchCritical, userId));
        if (null == securitygrouprules) {
            return securityGroupRulesWithDetailsRsp;
        }

        List<OSSecurityGroupRuleInfo> securityGroupRuleInfos = securitygrouprules.stream().map(tblCmpOsSecuritygrouprule -> {
            OSSecurityGroupRuleInfo securityGroupRuleInfo = new OSSecurityGroupRuleInfo();
            securityGroupRuleInfo.setSecurityGroupRuleInfo(tblCmpOsSecuritygrouprule);
            return securityGroupRuleInfo;
        }).collect(Collectors.toList());

        securityGroupRuleInfos.forEach(securityGroupRuleInfo -> cloudService.syncSingleData(cloudId, securityGroupRuleInfo.getId(), SyncMsg.OS_SECURITY_GROUP_RULE));

        securityGroupRulesWithDetailsRsp.setSecurityGroupRules(securityGroupRuleInfos);
        return securityGroupRulesWithDetailsRsp;
    }

    public ResponseEntity<Object> addSecurityGroupRule(String cloudId, OSSecurityGroupRuleCreateReq securityGroupRuleCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(securityGroupRuleCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSSecurityGroupRuleWithDetailsRsp securityGroupRuleWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSSecurityGroupRuleWithDetailsRsp.class);
                if (securityGroupRuleWithDetailsRsp == null || securityGroupRuleWithDetailsRsp.getSecurityGroupRule() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSecurityGroupRuleToDB(cloudId, securityGroupRuleWithDetailsRsp.getSecurityGroupRule(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group rule failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSSecurityGroupRuleWithDetailsRsp getSecurityGroupRuleDetails(String cloudId, String securityGroupRuleId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprule = osNeutronRepository.getSecuritygroupruleById(cloudId, securityGroupRuleId);
        if (null == tblCmpOsSecuritygrouprule)
        {
            throw new WebSystemException(ErrorCode.SECURITY_GROUP_RULE_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsSecuritygrouprule.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSSecurityGroupRuleWithDetailsRsp osSecurityGroupRuleWithDetailsRsp = new OSSecurityGroupRuleWithDetailsRsp();
        OSSecurityGroupRuleInfo securityGroupRuleInfo = new OSSecurityGroupRuleInfo();
        securityGroupRuleInfo.setSecurityGroupRuleInfo(tblCmpOsSecuritygrouprule);
        osSecurityGroupRuleWithDetailsRsp.setSecurityGroupRule(securityGroupRuleInfo);

        cloudService.syncSingleData(cloudId, securityGroupRuleId, SyncMsg.OS_SECURITY_GROUP_RULE);

        return osSecurityGroupRuleWithDetailsRsp;
    }

    public ResponseEntity<Object> removeSecurityGroupRule(String cloudId, String securityGroupRuleId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprule = osNeutronRepository.getSecuritygroupruleById(cloudId, securityGroupRuleId);
            if (tblCmpOsSecuritygrouprule == null)
            {
                LOGGER.error("get security group rule null: network id {}", securityGroupRuleId);
                throw new WebSystemException(ErrorCode.SECURITY_GROUP_RULE_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSecuritygrouprule.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsSecuritygrouprule.setEeStatus(REMOVED);
                osNeutronRepository.updateSecuritygrouprule(tblCmpOsSecuritygrouprule);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group rule failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsSecuritygrouprulesExample getTblCmpOsSecurityGroupRulesExample(String cloudId, OSSecurityGroupRuleSearchCritical securityGroupRuleSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsSecuritygrouprulesExample example = new TblCmpOsSecuritygrouprulesExample();
        TblCmpOsSecuritygrouprulesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getRemoteGroupId())) criteria.andRemoteGroupIdEqualTo(securityGroupRuleSearchCritical.getRemoteGroupId());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getDirection())) criteria.andDirectionEqualTo(securityGroupRuleSearchCritical.getDirection());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getProtocol())) criteria.andProtocolEqualTo(securityGroupRuleSearchCritical.getProtocol());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getEthertype())) criteria.andEthertypeEqualTo(securityGroupRuleSearchCritical.getEthertype());
        if (securityGroupRuleSearchCritical.getPortRangeMax() != null) criteria.andPortRangeMaxEqualTo(securityGroupRuleSearchCritical.getPortRangeMax());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getSecurityGroupId())) criteria.andSecurityGroupIdEqualTo(securityGroupRuleSearchCritical.getSecurityGroupId());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(securityGroupRuleSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(securityGroupRuleSearchCritical.getProjectId());
        }
        if (securityGroupRuleSearchCritical.getPortRangeMin() != null) criteria.andPortRangeMinEqualTo(securityGroupRuleSearchCritical.getPortRangeMin());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getRemoteIpPrefix())) criteria.andRemoteIpPrefixEqualTo(securityGroupRuleSearchCritical.getRemoteIpPrefix());
        if (securityGroupRuleSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(securityGroupRuleSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getId())) criteria.andIdEqualTo(securityGroupRuleSearchCritical.getId());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getDescription())) criteria.andDescriptionEqualTo(securityGroupRuleSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getSortKey()) && StringUtils.isNotEmpty(securityGroupRuleSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", securityGroupRuleSearchCritical.getSortKey(), securityGroupRuleSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public OSExtSecurityGroupRulesWithDetailsRsp getSecurityGroupRulesPage(String cloudId, OSSecurityGroupRuleSearchCritical securityGroupRuleSearchCritical, String userId) throws WebSystemException
    {
        OSExtSecurityGroupRulesWithDetailsRsp securityGroupRulesWithDetailsRsp = new OSExtSecurityGroupRulesWithDetailsRsp();

        PageHelper.startPage(securityGroupRuleSearchCritical.getPageNum(), securityGroupRuleSearchCritical.getPageSize());
        List<TblCmpOsSecuritygrouprules> securitygrouprules = osNeutronRepository.getSecuritygrouprules(getTblCmpOsSecurityGroupRulesExample(cloudId, securityGroupRuleSearchCritical, userId));
        PageInfo<TblCmpOsSecuritygrouprules> pageInfo = new PageInfo<>(securitygrouprules);

        if (null == securitygrouprules) {
            return securityGroupRulesWithDetailsRsp;
        }

        List<OSSecurityGroupRuleInfo> securityGroupRuleInfos = securitygrouprules.stream().map(tblCmpOsSecuritygrouprule -> {
            OSSecurityGroupRuleInfo securityGroupRuleInfo = new OSSecurityGroupRuleInfo();
            securityGroupRuleInfo.setSecurityGroupRuleInfo(tblCmpOsSecuritygrouprule);
            return securityGroupRuleInfo;
        }).collect(Collectors.toList());

        securityGroupRuleInfos.forEach(securityGroupRuleInfo -> cloudService.syncSingleData(cloudId, securityGroupRuleInfo.getId(), SyncMsg.OS_SECURITY_GROUP_RULE));

        securityGroupRulesWithDetailsRsp.setSecurityGroupRules(securityGroupRuleInfos);
        securityGroupRulesWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return securityGroupRulesWithDetailsRsp;
    }
}
