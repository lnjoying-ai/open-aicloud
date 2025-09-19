package com.lnjoying.justice.cmp.service.easystack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.ESNeutronRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtRouterInfo;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtRouterWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.ESExtRoutersWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.easystack.ESRouterSearchCritical;
import com.lnjoying.justice.cmp.entity.search.openstack.OSRouterSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.ESRouterService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class ESRouterServiceImpl implements ESRouterService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private ESNeutronRepository esNeutronRepository;

    @Autowired
    private CloudService cloudService;

    private TblCmpOsRoutersExample getTblCmpOsRoutersExample(String cloudId, OSRouterSearchCritical routerSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsRoutersExample example = new TblCmpOsRoutersExample();
        TblCmpOsRoutersExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(routerSearchCritical.getId())) criteria.andIdEqualTo(routerSearchCritical.getId());
        if (StringUtils.isNotEmpty(routerSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(routerSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(routerSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(routerSearchCritical.getProjectId());
        }
        if (StringUtils.isNotEmpty(routerSearchCritical.getName())) criteria.andNameLike("%" + routerSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(routerSearchCritical.getDescription())) criteria.andDescriptionEqualTo(routerSearchCritical.getDescription());
        if (routerSearchCritical.getAdminStateUp() != null) criteria.andAdminStateUpEqualTo(BoolUtil.bool2Short(routerSearchCritical.getAdminStateUp()));
        if (routerSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(routerSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(routerSearchCritical.getSortKey()) && StringUtils.isNotEmpty(routerSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", routerSearchCritical.getSortKey(), routerSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }

        if (StringUtils.isNotEmpty(routerSearchCritical.getStatus())) criteria.andStatusEqualTo(routerSearchCritical.getStatus());

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsRouterports> getTblCmpOsRouterports(String cloudId, String routerId, String portType) throws WebSystemException
    {
        TblCmpOsRouterportsExample example = new TblCmpOsRouterportsExample();
        TblCmpOsRouterportsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andRouterIdEqualTo(routerId);
        criteria.andPortTypeEqualTo(portType);

        List<TblCmpOsRouterports> routerports = osNeutronRepository.getRouterports(example);
        return routerports;
    }

    private List<TblCmpOsIpallocations> getTblCmpOsIpallocatios(String cloudId, String portId) throws WebSystemException
    {
        TblCmpOsIpallocationsExample example = new TblCmpOsIpallocationsExample();
        TblCmpOsIpallocationsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);

        List<TblCmpOsIpallocations> ipallocations = osNeutronRepository.getIpallocations(example);

        return ipallocations;
    }

    private List<TblCmpOsIpallocations> getIpallocations(String cloudId, String routerId) throws WebSystemException
    {
        List<TblCmpOsRouterports> routerports = getTblCmpOsRouterports(cloudId, routerId, "network:router_gateway");
        List<TblCmpOsIpallocations> ipallocations = new ArrayList<>();
        if (! CollectionUtils.isEmpty(routerports))
        {
            for (TblCmpOsRouterports routerport : routerports)
            {
                List<TblCmpOsIpallocations> tempipallocations = getTblCmpOsIpallocatios(cloudId, routerport.getPortId());
                if (! CollectionUtils.isEmpty(tempipallocations))
                {
                    ipallocations.addAll(tempipallocations);
                }
            }
        }
        return ipallocations;
    }

    private List<TblCmpOsRouterroutes> getTblCmpOsRouterroutes(String cloudId, String routerId) throws WebSystemException
    {
        TblCmpOsRouterroutesExample example = new TblCmpOsRouterroutesExample();
        TblCmpOsRouterroutesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andRouterIdEqualTo(routerId);

        List<TblCmpOsRouterroutes> routerroutes = osNeutronRepository.getRouterroutes(example);

        return routerroutes;
    }

    private TblCmpOsNetworks getTblCmpOsNetworks(String cloudId, String networkId) throws WebSystemException
    {
        return osNeutronRepository.getNetworkById(cloudId, networkId);
    }

    public ESExtRoutersWithDetailsRsp getEsRoutersPage(String cloudId, ESRouterSearchCritical routerSearchCritical, String userId)
    {
        ESExtRoutersWithDetailsRsp routersWithDetailsRsp = new ESExtRoutersWithDetailsRsp();

        PageHelper.startPage(routerSearchCritical.getPageNum(), routerSearchCritical.getPageSize());
        List<TblCmpEsRouters> routers = osNeutronRepository.getRoutersWithFirewall(getTblCmpOsRoutersExample(cloudId, routerSearchCritical, userId), cloudId, routerSearchCritical.getFirewallId());
        PageInfo<TblCmpEsRouters> pageInfo = new PageInfo<>(routers);

        if (null == routers) {
            return routersWithDetailsRsp;
        }

        List<ESExtRouterInfo> routerInfos = routers.stream().map(tblCmpOsRouters -> {
            ESExtRouterInfo routerInfo = new ESExtRouterInfo();
            routerInfo.setRouterInfo(tblCmpOsRouters);
            routerInfo.setFixedIp(getIpallocations(cloudId, tblCmpOsRouters.getId()));
            routerInfo.setRouterroute(getTblCmpOsRouterroutes(cloudId, tblCmpOsRouters.getId()));
            if (routerInfo.getExternalGatewayInfo() != null && StringUtils.isNotEmpty(routerInfo.getExternalGatewayInfo().getNetworkId()))
            {
                routerInfo.setNetwork(getTblCmpOsNetworks(cloudId, routerInfo.getExternalGatewayInfo().getNetworkId()));
            }
            if (routerInfo.getFirewallId() != null) routerInfo.setFirewall(getTblCmpEsFirewalls(cloudId, routerInfo.getFirewallId()));
            return routerInfo;
        }).collect(Collectors.toList());

        routerInfos.forEach(routerInfo -> cloudService.syncSingleData(cloudId, routerInfo.getId(), SyncMsg.OS_ROUTER));

        routersWithDetailsRsp.setRouters(routerInfos);
        routersWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return routersWithDetailsRsp;
    }

    private TblCmpEsFirewalls getTblCmpEsFirewalls(String cloudId, String firewallId) throws WebSystemException
    {
        return esNeutronRepository.getFirewallById(cloudId, firewallId);
    }

    private List<TblCmpEsFirewallBindings> getTblCmpEsFirewallBindings(String cloudId, String routerId) throws WebSystemException
    {
        TblCmpEsFirewallBindingsExample example = new TblCmpEsFirewallBindingsExample();
        TblCmpEsFirewallBindingsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andRouterIdEqualTo(routerId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        return esNeutronRepository.getFirewallBindings(example);
    }

    public ESExtRouterWithDetailsRsp getEsExtRouterDetails(String cloudId, String routerId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsRouters tblCmpOsRouters = osNeutronRepository.getRouterById(cloudId, routerId);
        if (null == tblCmpOsRouters)
        {
            throw new WebSystemException(ErrorCode.ROUTER_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsRouters.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        ESExtRouterWithDetailsRsp osRouterWithDetailsRsp = new ESExtRouterWithDetailsRsp();
        ESExtRouterInfo routerInfo = new ESExtRouterInfo();
        routerInfo.setRouterInfo(tblCmpOsRouters);
        routerInfo.setFixedIp(getIpallocations(cloudId, tblCmpOsRouters.getId()));
        routerInfo.setRouterroute(getTblCmpOsRouterroutes(cloudId, tblCmpOsRouters.getId()));
        if (routerInfo.getExternalGatewayInfo() != null && StringUtils.isNotEmpty(routerInfo.getExternalGatewayInfo().getNetworkId()))
        {
            routerInfo.setNetwork(getTblCmpOsNetworks(cloudId, routerInfo.getExternalGatewayInfo().getNetworkId()));
        }

        List<TblCmpEsFirewallBindings> firewallBindings = getTblCmpEsFirewallBindings(cloudId, routerId);
        if (!CollectionUtils.isEmpty(firewallBindings))
        {
            routerInfo.setFirewall(getTblCmpEsFirewalls(cloudId, firewallBindings.get(0).getFirewallId()));
        }

        osRouterWithDetailsRsp.setRouter(routerInfo);

        cloudService.syncSingleData(cloudId, routerId, SyncMsg.OS_ROUTER);

        return osRouterWithDetailsRsp;
    }
}
