package com.lnjoying.justice.cmp.service.easystack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.ESNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallRuleCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallRuleUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.*;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallRuleSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallRuleService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
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
public class ESFirewallRuleServiceImpl implements FirewallRuleService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private ESNeutronRepository esNeutronRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public ESFirewallRulesRsp getFirewallRules(String cloudId, ESFirewallRuleSearchCritical firewallRuleSearchCritical, String userId) throws WebSystemException
    {
        ESFirewallRulesRsp esFirewallRulesRsp = new ESFirewallRulesRsp();
        List<TblCmpEsFirewallRules> firewallRules = esNeutronRepository.getFirewallRules(getTblCmpEsFirewallRulesExample(cloudId, firewallRuleSearchCritical, userId));
        if (null == firewallRules) {
            return esFirewallRulesRsp;
        }

        List<ESFirewallRuleInfo> firewallRuleInfos = firewallRules.stream().map(tblCmpEsFirewallRule -> {
            ESFirewallRuleInfo firewallRuleInfo = new ESFirewallRuleInfo();
            firewallRuleInfo.setFirewallRuleInfo(tblCmpEsFirewallRule);
            return firewallRuleInfo;
        }).collect(Collectors.toList());

        firewallRuleInfos.forEach(firewallRuleInfo -> cloudService.syncSingleData(cloudId, firewallRuleInfo.getId(), SyncMsg.ES_FIREWALL_RULE));

        esFirewallRulesRsp.setFirewallRules(firewallRuleInfos);
        return esFirewallRulesRsp;
    }

    public ResponseEntity<Object> addFirewallRule(String cloudId, ESFirewallRuleCreateReq firewallRuleCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(firewallRuleCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                ESFirewallRuleRsp firewallRuleRsp = gson.fromJson(gson.toJson(response.getBody()), ESFirewallRuleRsp.class);
                if (firewallRuleRsp == null || firewallRuleRsp.getFirewallRule() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFirewallRuleToDB(cloudId, firewallRuleRsp.getFirewallRule(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall rule failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ESFirewallRuleRsp getFirewallRule(String cloudId, String firewallRuleId, String userId) throws WebSystemException
    {
        TblCmpEsFirewallRules tblCmpEsFirewallRule = esNeutronRepository.getFirewallRuleById(cloudId, firewallRuleId);
        if (null == tblCmpEsFirewallRule)
        {
            throw new WebSystemException(ErrorCode.FIREWALL_RULE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpEsFirewallRule.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        ESFirewallRuleRsp firewallRuleRsp = new ESFirewallRuleRsp();
        ESFirewallRuleInfo firewallRuleInfo = new ESFirewallRuleInfo();
        firewallRuleInfo.setFirewallRuleInfo(tblCmpEsFirewallRule);
        firewallRuleRsp.setFirewallRule(firewallRuleInfo);

        cloudService.syncSingleData(cloudId, firewallRuleId, SyncMsg.ES_FIREWALL_RULE);

        return firewallRuleRsp;
    }

    public ResponseEntity<Object> updateFirewallRule(String cloudId, String firewallRuleId, ESFirewallRuleUpdateReq request, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewallRules tblCmpEsFirewallRule = esNeutronRepository.getFirewallRuleById(cloudId, firewallRuleId);
            if (null == tblCmpEsFirewallRule)
            {
                throw new WebSystemException(ErrorCode.FIREWALL_RULE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewallRule.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(request);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                ESFirewallRuleRsp firewallRuleRsp = gson.fromJson(gson.toJson(response.getBody()), ESFirewallRuleRsp.class);
                if (firewallRuleRsp == null || firewallRuleRsp.getFirewallRule() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFirewallRuleToDB(cloudId, firewallRuleRsp.getFirewallRule(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall rule failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeFirewallRule(String cloudId, String firewallRuleId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewallRules tblCmpEsFirewallRule = esNeutronRepository.getFirewallRuleById(cloudId, firewallRuleId);
            if (tblCmpEsFirewallRule == null)
            {
                LOGGER.error("get firewall rule null: firewall rule id {}", firewallRuleId);
                throw new WebSystemException(ErrorCode.FIREWALL_RULE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewallRule.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpEsFirewallRule.setEeStatus(REMOVED);
                esNeutronRepository.updateFirewallRuleSelective(tblCmpEsFirewallRule);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall rule failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpEsFirewallRulesExample getTblCmpEsFirewallRulesExample(String cloudId, ESFirewallRuleSearchCritical firewallRuleSearchCritical, String userId) throws WebSystemException
    {
        TblCmpEsFirewallRulesExample example = new TblCmpEsFirewallRulesExample();
        TblCmpEsFirewallRulesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("id desc");

        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getId())) criteria.andIdLike("%" + firewallRuleSearchCritical.getId() + "%");
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getTenantId())) criteria.andTenantIdEqualTo(firewallRuleSearchCritical.getTenantId());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getProjectId())) criteria.andProjectIdEqualTo(firewallRuleSearchCritical.getProjectId());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getName())) criteria.andNameLike("%" + firewallRuleSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getDescription())) criteria.andDescriptionLike("%" + firewallRuleSearchCritical.getDescription() + "%");
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getFirewallPolicyId()))
        {
            if (firewallRuleSearchCritical.getFirewallPolicyId().equalsIgnoreCase("empty"))
            {
                criteria.andFirewallPolicyIdIsNull();
            }
            else
            {
                criteria.andFirewallPolicyIdEqualTo(firewallRuleSearchCritical.getFirewallPolicyId());
            }
        }
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getAction())) criteria.andActionEqualTo(firewallRuleSearchCritical.getAction());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getProtocol())) criteria.andProtocolEqualTo(firewallRuleSearchCritical.getProtocol());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getSourceIpAddress())) criteria.andSourceIpAddressEqualTo(firewallRuleSearchCritical.getSourceIpAddress());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getSourcePort())) criteria.andSourcePortEqualTo(firewallRuleSearchCritical.getSourcePort());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getDestinationIpAddress())) criteria.andDestinationIpAddressEqualTo(firewallRuleSearchCritical.getDestinationIpAddress());
        if (StringUtils.isNotEmpty(firewallRuleSearchCritical.getDestinationPort())) criteria.andDestinationPortEqualTo(firewallRuleSearchCritical.getDestinationPort());
        if (firewallRuleSearchCritical.getShared() != null) criteria.andSharedEqualTo(BoolUtil.bool2Short(firewallRuleSearchCritical.getShared()));
        if (firewallRuleSearchCritical.getIpVersion() != null) criteria.andIpVersionEqualTo(firewallRuleSearchCritical.getIpVersion());

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public ESExtFirewallRulesRsp getFirewallRulesPage(String cloudId, ESFirewallRuleSearchCritical firewallRuleSearchCritical, String userId)
    {
        ESExtFirewallRulesRsp firewallRulesRsp = new ESExtFirewallRulesRsp();

        PageHelper.startPage(firewallRuleSearchCritical.getPageNum(), firewallRuleSearchCritical.getPageSize());
        List<TblCmpEsFirewallRules> firewallRules = esNeutronRepository.getFirewallRules(getTblCmpEsFirewallRulesExample(cloudId, firewallRuleSearchCritical, userId));
        PageInfo<TblCmpEsFirewallRules> pageInfo = new PageInfo<>(firewallRules);

        if (null == firewallRules) {
            return firewallRulesRsp;
        }

        List<ESExtFirewallRuleInfo> firewallRuleInfos = firewallRules.stream().map(tblCmpEsFirewallRule -> {
            ESExtFirewallRuleInfo firewallRuleInfo = new ESExtFirewallRuleInfo();
            firewallRuleInfo.setFirewallRuleInfo(tblCmpEsFirewallRule);
            if (firewallRuleInfo.getFirewallPolicyId() != null) firewallRuleInfo.setFirewallPolicy(getTblCmpEsFirewallPolicy(cloudId, firewallRuleInfo.getFirewallPolicyId()));
            return firewallRuleInfo;
        }).collect(Collectors.toList());

        firewallRuleInfos.forEach(backupInfo -> cloudService.syncSingleData(cloudId, backupInfo.getId(), SyncMsg.ES_FIREWALL_RULE));

        firewallRulesRsp.setFirewallRules(firewallRuleInfos);
        firewallRulesRsp.setTotalNum(pageInfo.getTotal());
        return firewallRulesRsp;
    }

    public TblCmpEsFirewallPolicies getTblCmpEsFirewallPolicy(String cloudId, String firewallPolicyId)
    {
        return esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
    }
}
