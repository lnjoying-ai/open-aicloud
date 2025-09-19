package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSNetworkCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSNetworkUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtNetworksWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSNetworkInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSNetworkWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSNetworksWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSNetworkSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.NetworkService;
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
public class OSNetworkServiceImpl implements NetworkService
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

    public OSNetworkWithDetailsRsp getNetworkDetails(String cloudId, String networkId, String fields, String userId) throws WebSystemException
    {
        TblCmpOsNetworks tblCmpOsNetworks = osNeutronRepository.getNetworkById(cloudId, networkId);
        if (null == tblCmpOsNetworks)
        {
            throw new WebSystemException(ErrorCode.NETWORK_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsNetworks.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSNetworkWithDetailsRsp osNetworkWithDetailsRsp = new OSNetworkWithDetailsRsp();
        OSNetworkInfo networkInfo = new OSNetworkInfo();
        networkInfo.setNetworkInfo(tblCmpOsNetworks);
        networkInfo.setNetworkSegment(getTblCmpOsNetworksegments(cloudId, networkInfo.getId()));
        networkInfo.setSubnet(getTblCmpOsSubnets(cloudId, networkInfo.getId()));
        osNetworkWithDetailsRsp.setNetwork(networkInfo);

        cloudService.syncSingleData(cloudId, networkId, SyncMsg.OS_NETWORK);

        return osNetworkWithDetailsRsp;
    }

    public ResponseEntity<Object> updateNetwork(String cloudId, String networkId, OSNetworkUpdateReq osServerUpdateReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsNetworks tblCmpOsNetworks = osNeutronRepository.getNetworkById(cloudId, networkId);
            if (null == tblCmpOsNetworks)
            {
                throw new WebSystemException(ErrorCode.NETWORK_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsNetworks.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(osServerUpdateReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSNetworkWithDetailsRsp networkWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSNetworkWithDetailsRsp.class);
                if (networkWithDetailsRsp == null || networkWithDetailsRsp.getNetwork() == null)
                {
                    return response;
                }
                eskSyncDataService.updateNetworkToDB(cloudId, networkWithDetailsRsp.getNetwork(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update network failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeNetwork(String cloudId, String networkId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsNetworks tblCmpOsNetworks = osNeutronRepository.getNetworkById(cloudId, networkId);
            if (tblCmpOsNetworks == null)
            {
                LOGGER.error("get network null: network id {}", networkId);
                throw new WebSystemException(ErrorCode.NETWORK_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsNetworks.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsNetworks.setEeStatus(REMOVED);
                osNeutronRepository.updateNetwork(tblCmpOsNetworks);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove network failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSNetworksWithDetailsRsp getNetworks(String cloudId, OSNetworkSearchCritical networkSearchCritical, String userId) throws WebSystemException
    {
        OSNetworksWithDetailsRsp networksWithDetails = new OSNetworksWithDetailsRsp();
        List<TblCmpOsNetworks> networks = osNeutronRepository.getNetworks(getTblCmpOsNetworksExample(cloudId, networkSearchCritical, userId)) ;
        if (null == networks) {
            return networksWithDetails;
        }

        List<OSNetworkInfo> networkInfos = networks.stream().map(tblCmpOsFlavors -> {
            OSNetworkInfo networkInfo = new OSNetworkInfo();
            networkInfo.setNetworkInfo(tblCmpOsFlavors);
            networkInfo.setNetworkSegment(getTblCmpOsNetworksegments(cloudId, networkInfo.getId()));
            networkInfo.setSubnet(getTblCmpOsSubnets(cloudId, networkInfo.getId()));
            return networkInfo;
        }).collect(Collectors.toList());

        networkInfos.forEach(networkInfo -> cloudService.syncSingleData(cloudId, networkInfo.getId(), SyncMsg.OS_NETWORK));

        networksWithDetails.setNetworks(networkInfos);
        return networksWithDetails;
    }

    public ResponseEntity<Object> addNetwork(String cloudId, OSNetworkCreateReq addNetworkReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            addNetworkReq.getNetwork().setTenantId(OSClientUtil.getOSProject(osClientV3));
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addNetworkReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSNetworkWithDetailsRsp networkWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSNetworkWithDetailsRsp.class);
                if (networkWithDetailsRsp == null || networkWithDetailsRsp.getNetwork() == null)
                {
                    return response;
                }
                eskSyncDataService.updateNetworkToDB(cloudId, networkWithDetailsRsp.getNetwork(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add network failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsNetworksExample getTblCmpOsNetworksExample(String cloudId, OSNetworkSearchCritical networkSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsNetworksExample example = new TblCmpOsNetworksExample();
        TblCmpOsNetworksExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (networkSearchCritical.getAdminStateUp() != null) criteria.andAdminStateUpEqualTo(BoolUtil.bool2Short(networkSearchCritical.getAdminStateUp()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getId())) criteria.andIdEqualTo(networkSearchCritical.getId());
        if (networkSearchCritical.getMtu() != null) criteria.andMtuEqualTo(networkSearchCritical.getMtu());
        if (StringUtils.isNotEmpty(networkSearchCritical.getName())) criteria.andNameLike("%" + networkSearchCritical.getName() + "%");
        if (StringUtils.isNotEmpty(networkSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(networkSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(networkSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(networkSearchCritical.getProjectId());
        }
        if (networkSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(networkSearchCritical.getRevisionNumber().longValue());
        if (networkSearchCritical.getExternal() != null)
        {
            if (networkSearchCritical.getExternal())
            {
                criteria.andIsDefaultIsNotNull();
            }
            else
            {
                criteria.andIsDefaultIsNull();
            }
        }
        if (networkSearchCritical.getShared() != null) criteria.andSharedEqualTo(BoolUtil.bool2Short(networkSearchCritical.getShared()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getStatus())) criteria.andStatusEqualTo(networkSearchCritical.getStatus());
        if (networkSearchCritical.getVlanTransparent() != null) criteria.andVlanTransparentEqualTo(BoolUtil.bool2Short(networkSearchCritical.getVlanTransparent()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getDescription())) criteria.andDescriptionEqualTo(networkSearchCritical.getDescription());
        if (networkSearchCritical.getIsDefault() != null) criteria.andIsDefaultEqualTo(BoolUtil.bool2Short(networkSearchCritical.getIsDefault()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getSortKey()) && StringUtils.isNotEmpty(networkSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", networkSearchCritical.getSortKey(), networkSearchCritical.getSortDir()));
        }
        if (StringUtils.isNotEmpty(networkSearchCritical.getFields()));

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsNetworksegments> getTblCmpOsNetworksegments(String cloudId, String networkId) throws WebSystemException
    {
        TblCmpOsNetworksegmentsExample example = new TblCmpOsNetworksegmentsExample();
        TblCmpOsNetworksegmentsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andNetworkIdEqualTo(networkId);

        List<TblCmpOsNetworksegments> networksegments = osNeutronRepository.getNetworksegments(example);

        return networksegments;
    }

    private List<TblCmpOsSubnets> getTblCmpOsSubnets(String cloudId, String networkId) throws WebSystemException
    {
        TblCmpOsSubnetsExample example = new TblCmpOsSubnetsExample();
        TblCmpOsSubnetsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andNetworkIdEqualTo(networkId);

        List<TblCmpOsSubnets> subnets = osNeutronRepository.getSubnets(example);

        return subnets;
    }

    public OSExtNetworksWithDetailsRsp getNetworksPage(String cloudId, OSNetworkSearchCritical networkSearchCritical, String userId) throws WebSystemException
    {
        OSExtNetworksWithDetailsRsp networksWithDetails = new OSExtNetworksWithDetailsRsp();

        PageHelper.startPage(networkSearchCritical.getPageNum(), networkSearchCritical.getPageSize());
        List<TblCmpOsNetworks> networks = osNeutronRepository.getNetworksWithShared(getPageTblCmpOsNetworksExample(cloudId, networkSearchCritical, userId), networkSearchCritical.getProjectId(), userId);
        PageInfo<TblCmpOsNetworks> pageInfo = new PageInfo<>(networks);

        if (null == networks) {
            return networksWithDetails;
        }

        List<OSNetworkInfo> networkInfos = networks.stream().map(tblCmpOsFlavors -> {
            OSNetworkInfo networkInfo = new OSNetworkInfo();
            networkInfo.setNetworkInfo(tblCmpOsFlavors);
            networkInfo.setNetworkSegment(getTblCmpOsNetworksegments(cloudId, networkInfo.getId()));
            networkInfo.setSubnet(getTblCmpOsSubnets(cloudId, networkInfo.getId()));
            return networkInfo;
        }).collect(Collectors.toList());

        networkInfos.forEach(networkInfo -> cloudService.syncSingleData(cloudId, networkInfo.getId(), SyncMsg.OS_NETWORK));

        networksWithDetails.setNetworks(networkInfos);
        networksWithDetails.setTotalNum(pageInfo.getTotal());
        return networksWithDetails;
    }

    private TblCmpOsNetworksExample getPageTblCmpOsNetworksExample(String cloudId, OSNetworkSearchCritical networkSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsNetworksExample example = new TblCmpOsNetworksExample();
        TblCmpOsNetworksExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (networkSearchCritical.getAdminStateUp() != null) criteria.andAdminStateUpEqualTo(BoolUtil.bool2Short(networkSearchCritical.getAdminStateUp()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getId())) criteria.andIdEqualTo(networkSearchCritical.getId());
        if (networkSearchCritical.getMtu() != null) criteria.andMtuEqualTo(networkSearchCritical.getMtu());
        if (StringUtils.isNotEmpty(networkSearchCritical.getName())) criteria.andNameLike("%" + networkSearchCritical.getName() + "%");
        if (networkSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(networkSearchCritical.getRevisionNumber().longValue());
        if (networkSearchCritical.getExternal() != null)
        {
            if (networkSearchCritical.getExternal())
            {
                criteria.andIsDefaultIsNotNull();
            }
            else
            {
                criteria.andIsDefaultIsNull();
            }
        }
        if (networkSearchCritical.getShared() != null) criteria.andSharedEqualTo(BoolUtil.bool2Short(networkSearchCritical.getShared()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getStatus())) criteria.andStatusEqualTo(networkSearchCritical.getStatus());
        if (networkSearchCritical.getVlanTransparent() != null) criteria.andVlanTransparentEqualTo(BoolUtil.bool2Short(networkSearchCritical.getVlanTransparent()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getDescription())) criteria.andDescriptionEqualTo(networkSearchCritical.getDescription());
        if (networkSearchCritical.getIsDefault() != null) criteria.andIsDefaultEqualTo(BoolUtil.bool2Short(networkSearchCritical.getIsDefault()));
        if (StringUtils.isNotEmpty(networkSearchCritical.getSortKey()) && StringUtils.isNotEmpty(networkSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", networkSearchCritical.getSortKey(), networkSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }
        if (StringUtils.isNotEmpty(networkSearchCritical.getFields()));

        return example;
    }
}
