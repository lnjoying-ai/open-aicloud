package com.lnjoying.justice.cmp.service.easystack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.ESNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallPolicyCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallPolicyUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESPolicyAddRuleReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESPolicyRemoveRuleReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.*;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallPoliciesSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallPolicyService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
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
public class ESFirewallPolicyServiceImpl implements FirewallPolicyService
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

    public ESFirewallPoliciesRsp getFirewallPolicies(String cloudId, ESFirewallPoliciesSearchCritical firewallPoliciesSearchCritical, String userId) throws WebSystemException
    {
        ESFirewallPoliciesRsp firewallPoliciesRsp = new ESFirewallPoliciesRsp();
        List<TblCmpEsFirewallPolicies> firewallPolicies = esNeutronRepository.getFirewallPolicies(getTblCmpEsFirewallPoliciesExample(cloudId, firewallPoliciesSearchCritical, userId));
        if (null == firewallPolicies) {
            return firewallPoliciesRsp;
        }

        List<ESFirewallPolicyInfo> firewallPolicyInfos = firewallPolicies.stream().map(tblCmpEsFirewallPolicy -> {
            ESFirewallPolicyInfo firewallPolicyInfo = new ESFirewallPolicyInfo();
            firewallPolicyInfo.setFirewallPolicyInfo(tblCmpEsFirewallPolicy);
            firewallPolicyInfo.setRules(getTblCmpEsFirewallRules(cloudId, tblCmpEsFirewallPolicy.getId()));
            firewallPolicyInfo.setFirewalls(getTblCmpEsFirewalls(cloudId, tblCmpEsFirewallPolicy.getId()));
            return firewallPolicyInfo;
        }).collect(Collectors.toList());

        firewallPolicyInfos.forEach(backupInfo -> cloudService.syncSingleData(cloudId, backupInfo.getId(), SyncMsg.ES_FIREWALL_POLICY));

        firewallPoliciesRsp.setFirewallPolicies(firewallPolicyInfos);
        return firewallPoliciesRsp;
    }

    public ResponseEntity<Object> addFirewallPolicy(String cloudId, ESFirewallPolicyCreateReq firewallPolicyCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(firewallPolicyCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                ESFirewallPolicyRsp firewallPolicyRsp = gson.fromJson(gson.toJson(response.getBody()), ESFirewallPolicyRsp.class);
                if (firewallPolicyRsp == null || firewallPolicyRsp.getFirewallPolicy() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFirewallPolicyToDB(cloudId, firewallPolicyRsp.getFirewallPolicy(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall policy failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ESFirewallPolicyRsp getFirewallPolicy(String cloudId, String firewallPolicyId, String userId) throws WebSystemException
    {
        TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
        if (null == tblCmpEsFirewallPolicy)
        {
            throw new WebSystemException(ErrorCode.FIREWALL_POLICY_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpEsFirewallPolicy.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        ESFirewallPolicyRsp firewallPolicyRsp = new ESFirewallPolicyRsp();
        ESFirewallPolicyInfo firewallPolicyInfo = new ESFirewallPolicyInfo();
        firewallPolicyInfo.setFirewallPolicyInfo(tblCmpEsFirewallPolicy);
        firewallPolicyInfo.setRules(getTblCmpEsFirewallRules(cloudId, firewallPolicyId));
        firewallPolicyInfo.setFirewalls(getTblCmpEsFirewalls(cloudId, firewallPolicyId));
        firewallPolicyRsp.setFirewallPolicy(firewallPolicyInfo);

        cloudService.syncSingleData(cloudId, firewallPolicyId, SyncMsg.ES_FIREWALL_POLICY);

        return firewallPolicyRsp;
    }

    public ResponseEntity<Object> updateFirewallPolicy(String cloudId, String firewallPolicyId, ESFirewallPolicyUpdateReq request, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
            if (null == tblCmpEsFirewallPolicy)
            {
                throw new WebSystemException(ErrorCode.FIREWALL_POLICY_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewallPolicy.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(request);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                ESFirewallPolicyRsp firewallPolicyRsp = gson.fromJson(gson.toJson(response.getBody()), ESFirewallPolicyRsp.class);
                if (firewallPolicyRsp == null || firewallPolicyRsp.getFirewallPolicy() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFirewallPolicyToDB(cloudId, firewallPolicyRsp.getFirewallPolicy(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall policy failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeFirewallPolicy(String cloudId, String firewallPolicyId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
            if (tblCmpEsFirewallPolicy == null)
            {
                LOGGER.error("get firewall policy null: backup id {}", firewallPolicyId);
                throw new WebSystemException(ErrorCode.FIREWALL_POLICY_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewallPolicy.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpEsFirewallPolicy.setEeStatus(REMOVED);
                esNeutronRepository.updateFirewallPolicy(tblCmpEsFirewallPolicy);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall policy failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> addFirewallRuleToPolicy(String cloudId, String firewallPolicyId, ESPolicyAddRuleReq request, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
            if (tblCmpEsFirewallPolicy == null)
            {
                LOGGER.error("get firewall policy null: backup id {}", firewallPolicyId);
                throw new WebSystemException(ErrorCode.FIREWALL_POLICY_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewallPolicy.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(request);

            if (request != null && request.getFirewallRuleId() != null)
            {
                cloudService.syncSingleData(cloudId, request.getFirewallRuleId(), SyncMsg.ES_FIREWALL_RULE);
            }

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall rule to policy failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeFirewallRuleFromPolicy(String cloudId, String firewallPolicyId, ESPolicyRemoveRuleReq request, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
            if (tblCmpEsFirewallPolicy == null)
            {
                LOGGER.error("get firewall policy null: backup id {}", firewallPolicyId);
                throw new WebSystemException(ErrorCode.FIREWALL_POLICY_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewallPolicy.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(request);

            if (request != null && request.getFirewallRuleId() != null)
            {
                cloudService.syncSingleData(cloudId, request.getFirewallRuleId(), SyncMsg.ES_FIREWALL_RULE);
            }

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall rule from policy failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpEsFirewallPoliciesExample getTblCmpEsFirewallPoliciesExample(String cloudId, ESFirewallPoliciesSearchCritical firewallPoliciesSearchCritical, String userId) throws WebSystemException
    {
        TblCmpEsFirewallPoliciesExample example = new TblCmpEsFirewallPoliciesExample();
        TblCmpEsFirewallPoliciesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("id desc");

        if (StringUtils.isNotEmpty(firewallPoliciesSearchCritical.getId())) criteria.andIdLike("%" + firewallPoliciesSearchCritical.getId() + "%");
        if (StringUtils.isNotEmpty(firewallPoliciesSearchCritical.getTenantId())) criteria.andTenantIdEqualTo(firewallPoliciesSearchCritical.getTenantId());
        if (StringUtils.isNotEmpty(firewallPoliciesSearchCritical.getProjectId())) criteria.andProjectIdEqualTo(firewallPoliciesSearchCritical.getProjectId());
        if (StringUtils.isNotEmpty(firewallPoliciesSearchCritical.getName())) criteria.andNameLike("%" + firewallPoliciesSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(firewallPoliciesSearchCritical.getDescription())) criteria.andDescriptionLike("%" + firewallPoliciesSearchCritical.getDescription() + "%");
        if (firewallPoliciesSearchCritical.getShared() != null) criteria.andSharedEqualTo(BoolUtil.bool2Short(firewallPoliciesSearchCritical.getShared()));

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public List<TblCmpEsFirewalls> getTblCmpEsFirewalls(String cloudId, String firewallPolicyId)
    {
        TblCmpEsFirewallsExample example = new TblCmpEsFirewallsExample();
        TblCmpEsFirewallsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andFirewallPolicyIdEqualTo(firewallPolicyId);
        List<TblCmpEsFirewalls> firewalls = esNeutronRepository.getFirewalls(example);
        return firewalls;
    }

    public List<TblCmpEsFirewallRules> getTblCmpEsFirewallRules(String cloudId, String firewallPolicyId)
    {
        TblCmpEsFirewallRulesExample example = new TblCmpEsFirewallRulesExample();
        TblCmpEsFirewallRulesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andFirewallPolicyIdEqualTo(firewallPolicyId);
        List<TblCmpEsFirewallRules> securitygrouprules = esNeutronRepository.getFirewallRules(example);

        if (!CollectionUtils.isEmpty(securitygrouprules))
        {
            securitygrouprules.forEach(tblCmpEsFirewallRule -> cloudService.syncSingleData(cloudId, tblCmpEsFirewallRule.getFirewallPolicyId(), SyncMsg.ES_FIREWALL_RULE));
        }

        return securitygrouprules;
    }

    public ESExtFirewallPoliciesRsp getFirewallPoliciesPage(String cloudId, ESFirewallPoliciesSearchCritical firewallPoliciesSearchCritical, String userId)
    {
        ESExtFirewallPoliciesRsp firewallPoliciesRsp = new ESExtFirewallPoliciesRsp();

        PageHelper.startPage(firewallPoliciesSearchCritical.getPageNum(), firewallPoliciesSearchCritical.getPageSize());
        List<TblCmpEsFirewallPolicies> firewallPolicies = esNeutronRepository.getFirewallPolicies(getTblCmpEsFirewallPoliciesExample(cloudId, firewallPoliciesSearchCritical, userId));
        PageInfo<TblCmpEsFirewallPolicies> pageInfo = new PageInfo<>(firewallPolicies);

        if (null == firewallPolicies) {
            return firewallPoliciesRsp;
        }

        List<ESFirewallPolicyInfo> firewallPolicyInfos = firewallPolicies.stream().map(tblCmpEsFirewallPolicy -> {
            ESFirewallPolicyInfo firewallPolicyInfo = new ESFirewallPolicyInfo();
            firewallPolicyInfo.setFirewallPolicyInfo(tblCmpEsFirewallPolicy);
            firewallPolicyInfo.setRules(getTblCmpEsFirewallRules(cloudId, tblCmpEsFirewallPolicy.getId()));
            firewallPolicyInfo.setFirewalls(getTblCmpEsFirewalls(cloudId, tblCmpEsFirewallPolicy.getId()));
            return firewallPolicyInfo;
        }).collect(Collectors.toList());

        firewallPolicyInfos.forEach(firewallPolicyInfo -> cloudService.syncSingleData(cloudId, firewallPolicyInfo.getId(), SyncMsg.ES_FIREWALL_POLICY));

        firewallPoliciesRsp.setFirewallPolicies(firewallPolicyInfos);
        firewallPoliciesRsp.setTotalNum(pageInfo.getTotal());
        return firewallPoliciesRsp;
    }
}
