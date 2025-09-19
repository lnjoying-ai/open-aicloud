package com.lnjoying.justice.cmp.service.easystack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFloatingIPSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.easystack.ESFloatingIPService;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class ESFloatingIpServiceImpl implements ESFloatingIPService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private CloudService cloudService;

    private TblCmpOsFloatingipsExample getTblCmpOsFloatingipsExample(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsFloatingipsExample example = new TblCmpOsFloatingipsExample();
        TblCmpOsFloatingipsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getId())) criteria.andIdEqualTo(floatingIPSearchCritical.getId());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getRouterId())) criteria.andRouterIdEqualTo(floatingIPSearchCritical.getRouterId());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getStatus())) criteria.andStatusEqualTo(floatingIPSearchCritical.getStatus());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(floatingIPSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(floatingIPSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(floatingIPSearchCritical.getProjectId());
        }
        if (floatingIPSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(floatingIPSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getDescription())) criteria.andDescriptionEqualTo(floatingIPSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getFloatingNetworkId())) criteria.andFloatingNetworkIdEqualTo(floatingIPSearchCritical.getFloatingNetworkId());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getFixedIpAddress()))
        {
            if (floatingIPSearchCritical.getFixedIpAddress().equalsIgnoreCase("empty"))
            {
                criteria.andFixedIpAddressIsNull();
            }
            else
            {
                criteria.andFixedIpAddressEqualTo(floatingIPSearchCritical.getFixedIpAddress());
            }
        }
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getFloatingIpAddress())) criteria.andFloatingIpAddressEqualTo(floatingIPSearchCritical.getFloatingIpAddress());
        if (StringUtils.isNotEmpty(floatingIPSearchCritical.getSortKey()) && StringUtils.isNotEmpty(floatingIPSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", floatingIPSearchCritical.getSortKey(), floatingIPSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc, id desc");
        }

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private TblCmpOsNetworks getTblCmpOsNetwork(String cloudId, String networkId) throws WebSystemException
    {
        return osNeutronRepository.getNetworkById(cloudId, networkId);
    }

    public OSExtFloatingIpsWithDetailsRsp getFloatingIPsPage(String cloudId, OSFloatingIPSearchCritical floatingIPSearchCritical, String userId)
    {
        OSExtFloatingIpsWithDetailsRsp floatingIpsWithDetails = new OSExtFloatingIpsWithDetailsRsp();

        PageHelper.startPage(floatingIPSearchCritical.getPageNum(), floatingIPSearchCritical.getPageSize());
        List<TblCmpOsFloatingips> floatingips = osNeutronRepository.getFloatingips(getTblCmpOsFloatingipsExample(cloudId, floatingIPSearchCritical, userId));
        PageInfo<TblCmpOsFloatingips> pageInfo = new PageInfo<>(floatingips);

        if (null == floatingips) {
            return floatingIpsWithDetails;
        }

        List<OSExtFloatingIpInfo> floatingIpInfos = floatingips.stream().map(tblCmpOsFloatingip -> {
            OSExtFloatingIpInfo floatingIpInfo = new OSExtFloatingIpInfo();
            floatingIpInfo.setFloatingIpInfo(tblCmpOsFloatingip);
            floatingIpInfo.setNetwork(getTblCmpOsNetwork(cloudId, tblCmpOsFloatingip.getFloatingNetworkId()));
            floatingIpInfo.setRouter(getTblCmpOsRouter(cloudId, tblCmpOsFloatingip.getRouterId()));
            return floatingIpInfo;
        }).collect(Collectors.toList());

        floatingIpInfos.forEach(floatingIpInfo -> cloudService.syncSingleData(cloudId, floatingIpInfo.getId(), SyncMsg.OS_FLOATING_IP));

        floatingIpsWithDetails.setFloatingips(floatingIpInfos);
        floatingIpsWithDetails.setTotalNum(pageInfo.getTotal());
        return floatingIpsWithDetails;
    }

    private TblCmpOsRouters getTblCmpOsRouter(String cloudId, String routerId) throws WebSystemException
    {
        return osNeutronRepository.getRouterById(cloudId, routerId);
    }
}
