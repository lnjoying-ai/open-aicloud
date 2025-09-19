package com.lnjoying.justice.cmp.service.easystack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.ESNeutronRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.easystack.neutron.ESFirewallUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.*;
import com.lnjoying.justice.cmp.entity.search.easystack.ESFirewallSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.FirewallService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
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
public class ESFirewallServiceImpl implements FirewallService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private ESNeutronRepository esNeutronRepository;

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public ESFirewallsRsp getFirewalls(String cloudId, ESFirewallSearchCritical firewallSearchCritical, String userId) throws WebSystemException
    {
        ESFirewallsRsp firewallsRsp = new ESFirewallsRsp();
        List<TblCmpEsFirewalls> firewalls = esNeutronRepository.getFirewalls(getTblCmpEsFirewallsExample(cloudId, firewallSearchCritical, userId));
        if (null == firewalls) {
            return firewallsRsp;
        }

        List<ESFirewallInfo> firewallInfos = firewalls.stream().map(tblCmpEsFirewall -> {
            ESFirewallInfo firewallInfo = new ESFirewallInfo();
            firewallInfo.setFirewallInfo(tblCmpEsFirewall);
            firewallInfo.setRouterIds(getTblCmpEsFirewallBindings(cloudId, firewallInfo.getId()));
            return firewallInfo;
        }).collect(Collectors.toList());

        firewallInfos.forEach(backupInfo -> cloudService.syncSingleData(cloudId, backupInfo.getId(), SyncMsg.ES_FIREWALL));

        firewallsRsp.setFirewalls(firewallInfos);
        return firewallsRsp;
    }

    public ResponseEntity<Object> addFirewall(String cloudId, ESFirewallCreateReq firewallCreateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(firewallCreateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                ESFirewallRsp firewallRsp = gson.fromJson(gson.toJson(response.getBody()), ESFirewallRsp.class);
                if (firewallRsp == null || firewallRsp.getFirewall() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFirewallToDB(cloudId, firewallRsp.getFirewall(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add firewall failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ESFirewallRsp getFirewall(String cloudId, String firewallId, String userId) throws WebSystemException
    {
        TblCmpEsFirewalls tblCmpEsFirewall = esNeutronRepository.getFirewallById(cloudId, firewallId);
        if (null == tblCmpEsFirewall)
        {
            throw new WebSystemException(ErrorCode.FIREWALL_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpEsFirewall.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        ESFirewallRsp firewallRsp = new ESFirewallRsp();
        ESFirewallInfo firewallInfo = new ESFirewallInfo();
        firewallInfo.setFirewallInfo(tblCmpEsFirewall);
        firewallRsp.setFirewall(firewallInfo);

        cloudService.syncSingleData(cloudId, firewallId, SyncMsg.ES_FIREWALL);

        return firewallRsp;
    }

    public ResponseEntity<Object> updateFirewall(String cloudId, String firewallId, ESFirewallUpdateReq request, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewalls tblCmpEsFirewall = esNeutronRepository.getFirewallById(cloudId, firewallId);
            if (null == tblCmpEsFirewall)
            {
                throw new WebSystemException(ErrorCode.FIREWALL_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewall.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject(request);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                ESFirewallRsp firewallRsp = gson.fromJson(gson.toJson(response.getBody()), ESFirewallRsp.class);
                if (firewallRsp == null || firewallRsp.getFirewall() == null)
                {
                    return response;
                }
                eskSyncDataService.updateFirewallToDB(cloudId, firewallRsp.getFirewall(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update firewall failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeFirewall(String cloudId, String firewallId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEsFirewalls tblCmpEsFirewall = esNeutronRepository.getFirewallById(cloudId, firewallId);
            if (tblCmpEsFirewall == null)
            {
                LOGGER.error("get firewall null: firewall id {}", firewallId);
                throw new WebSystemException(ErrorCode.FIREWALL_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEsFirewall.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloudWithProject();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpEsFirewall.setEeStatus(REMOVED);
                esNeutronRepository.updateFirewall(tblCmpEsFirewall);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove firewall failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpEsFirewallsExample getTblCmpEsFirewallsExample(String cloudId, ESFirewallSearchCritical firewallSearchCritical, String userId) throws WebSystemException
    {
        TblCmpEsFirewallsExample example = new TblCmpEsFirewallsExample();
        TblCmpEsFirewallsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("id desc");

        if (StringUtils.isNotEmpty(firewallSearchCritical.getId())) criteria.andIdLike("%" + firewallSearchCritical.getId() + "%");
        if (StringUtils.isNotEmpty(firewallSearchCritical.getTenantId())) criteria.andTenantIdEqualTo(firewallSearchCritical.getTenantId());
        if (StringUtils.isNotEmpty(firewallSearchCritical.getProjectId())) criteria.andProjectIdEqualTo(firewallSearchCritical.getProjectId());
        if (StringUtils.isNotEmpty(firewallSearchCritical.getName())) criteria.andNameLike("%" + firewallSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(firewallSearchCritical.getDescription())) criteria.andDescriptionLike("%" + firewallSearchCritical.getDescription() + "%");
        if (StringUtils.isNotEmpty(firewallSearchCritical.getFirewallPolicyId())) criteria.andFirewallPolicyIdEqualTo(firewallSearchCritical.getFirewallPolicyId());
        if (StringUtils.isNotEmpty(firewallSearchCritical.getStatus())) criteria.andStatusEqualTo(firewallSearchCritical.getStatus());

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public List<TblCmpEsFirewallBindings> getTblCmpEsFirewallBindings(String cloudId, String firewallId)
    {
        TblCmpEsFirewallBindingsExample example = new TblCmpEsFirewallBindingsExample();
        TblCmpEsFirewallBindingsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andFirewallIdEqualTo(firewallId);
        List<TblCmpEsFirewallBindings> firewallBindings = esNeutronRepository.getFirewallBindings(example);
        return firewallBindings;
    }

    public ESExtFirewallsRsp getFirewallsPage(String cloudId, ESFirewallSearchCritical firewallSearchCritical, String userId)
    {
        ESExtFirewallsRsp firewallsRsp = new ESExtFirewallsRsp();

        PageHelper.startPage(firewallSearchCritical.getPageNum(), firewallSearchCritical.getPageSize());
        List<TblCmpEsFirewalls> firewalls = esNeutronRepository.getFirewalls(getTblCmpEsFirewallsExample(cloudId, firewallSearchCritical, userId));
        PageInfo<TblCmpEsFirewalls> pageInfo = new PageInfo<>(firewalls);

        if (null == firewalls) {
            return firewallsRsp;
        }

        List<ESExtFirewallInfo> firewallInfos = firewalls.stream().map(tblCmpEsFirewall -> {
            ESExtFirewallInfo firewallInfo = new ESExtFirewallInfo();
            firewallInfo.setFirewallInfo(tblCmpEsFirewall);
            firewallInfo.setRouterIds(getTblCmpEsFirewallBindings(cloudId, firewallInfo.getId()));
            firewallInfo.setFirewallPolicy(getTblCmpEsFirewallPolicy(cloudId, firewallInfo.getFirewallPolicyId()));
            if (!CollectionUtils.isEmpty(firewallInfo.getRouterIds())) firewallInfo.setRouter(getTblCmpOsRouters(cloudId, firewallInfo.getRouterIds()));
            return firewallInfo;
        }).collect(Collectors.toList());

        firewallInfos.forEach(firewallInfo -> cloudService.syncSingleData(cloudId, firewallInfo.getId(), SyncMsg.ES_FIREWALL));

        firewallsRsp.setFirewalls(firewallInfos);
        firewallsRsp.setTotalNum(pageInfo.getTotal());
        return firewallsRsp;
    }

    public TblCmpEsFirewallPolicies getTblCmpEsFirewallPolicy(String cloudId, String firewallPolicyId)
    {
        return esNeutronRepository.getFirewallPolicyById(cloudId, firewallPolicyId);
    }

    public List<TblCmpOsRouters> getTblCmpOsRouters(String cloudId, List<String> routerIds)
    {
        TblCmpOsRoutersExample example = new TblCmpOsRoutersExample();
        TblCmpOsRoutersExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andIdIn(routerIds);
        return osNeutronRepository.getRouters(example);
    }
}
