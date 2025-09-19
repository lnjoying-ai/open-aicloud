package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSubnetCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSSubnetUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSubnetSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.SubnetService;
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
public class OSSubnetServiceImpl implements SubnetService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    public OSSubnetsWithDetailsRsp getSubnets(String cloudId, OSSubnetSearchCritical subnetSearchCritical, String userId) throws WebSystemException
    {
        OSSubnetsWithDetailsRsp getSubnetsRsp = new OSSubnetsWithDetailsRsp();
        List<TblCmpOsSubnets> subnets = osNeutronRepository.getSubnets(getTblCmpOsSubnetsExample(cloudId, subnetSearchCritical, userId));
        if (null == subnets)
        {
            return getSubnetsRsp;
        }

        List<OSSubnetInfo> subnetInfos = subnets.stream().map(tblCmpOsSubnet -> {
            OSSubnetInfo subnetInfo = new OSSubnetInfo();
            subnetInfo.setSubnetInfo(tblCmpOsSubnet);
            subnetInfo.setIpallocationpools(getTblCmpOsIpallocationpools(cloudId, subnetInfo.getId()));
            subnetInfo.setSubnetroutes(getTblCmpOsSubnetroutes(cloudId, subnetInfo.getId()));
            subnetInfo.setDnsnameservers(getTblCmpOsDnsnameservers(cloudId, subnetInfo.getId()));
            subnetInfo.setSubnetServiceTypes(getTblCmpOsSubnetServiceTypes(cloudId, subnetInfo.getId()));
            return subnetInfo;
        }).collect(Collectors.toList());

        subnetInfos.forEach(subnetInfo -> cloudService.syncSingleData(cloudId, subnetInfo.getId(), SyncMsg.OS_SUBNET));

        getSubnetsRsp.setSubnets(subnetInfos);
        return getSubnetsRsp;
    }

    public ResponseEntity<Object> addSubnet(String cloudId, OSSubnetCreateReq addSubnetReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            addSubnetReq.getSubnet().setProjectId(OSClientUtil.getOSProject(osClientV3));
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addSubnetReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSSubnetWithDetailsRsp osSubnetWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSSubnetWithDetailsRsp.class);

                if (osSubnetWithDetailsRsp == null || osSubnetWithDetailsRsp.getSubnet() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSubnetToDB(cloudId, osSubnetWithDetailsRsp.getSubnet(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add subnet failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSSubnetWithDetailsRsp getSubnetDetails(String cloudId, String subnetId, String userId) throws WebSystemException
    {
        TblCmpOsSubnets tblCmpOsSubnet = osNeutronRepository.getSubnetById(cloudId, subnetId);
        if (null == tblCmpOsSubnet)
        {
            throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpOsSubnet.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        OSSubnetWithDetailsRsp osSubnetWithDetailsRsp = new OSSubnetWithDetailsRsp();
        OSSubnetInfo subnetInfo = new OSSubnetInfo();
        subnetInfo.setSubnetInfo(tblCmpOsSubnet);
        subnetInfo.setIpallocationpools(getTblCmpOsIpallocationpools(cloudId, subnetInfo.getId()));
        subnetInfo.setSubnetroutes(getTblCmpOsSubnetroutes(cloudId, subnetInfo.getId()));
        subnetInfo.setDnsnameservers(getTblCmpOsDnsnameservers(cloudId, subnetInfo.getId()));
        subnetInfo.setSubnetServiceTypes(getTblCmpOsSubnetServiceTypes(cloudId, subnetInfo.getId()));
        osSubnetWithDetailsRsp.setSubnet(subnetInfo);

        cloudService.syncSingleData(cloudId, subnetId, SyncMsg.OS_SUBNET);

        return osSubnetWithDetailsRsp;
    }

    public ResponseEntity<Object> updateSubnet(String cloudId, String subnetId, OSSubnetUpdateReq subnetUpdate, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSubnets tblCmpOsSubnet = osNeutronRepository.getSubnetById(cloudId, subnetId);
            if (null == tblCmpOsSubnet)
            {
                throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSubnet.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(subnetUpdate);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSSubnetWithDetailsRsp osSubnetWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSSubnetWithDetailsRsp.class);

                if (osSubnetWithDetailsRsp == null || osSubnetWithDetailsRsp.getSubnet() == null)
                {
                    return response;
                }
                eskSyncDataService.updateSubnetToDB(cloudId, osSubnetWithDetailsRsp.getSubnet(), null, null);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update subnet failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeSubnet(String cloudId, String subnetId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsSubnets tblCmpOsSubnet = osNeutronRepository.getSubnetById(cloudId, subnetId);
            if (tblCmpOsSubnet == null)
            {
                LOGGER.error("get subnet null: subnet id {}", subnetId);
                throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsSubnet.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED)
            {
                tblCmpOsSubnet.setEeStatus(REMOVED);
                osNeutronRepository.updateSubnet(tblCmpOsSubnet);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove subnet failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private TblCmpOsSubnetsExample getTblCmpOsSubnetsExample(String cloudId, OSSubnetSearchCritical subnetSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsSubnetsExample example = new TblCmpOsSubnetsExample();
        TblCmpOsSubnetsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(subnetSearchCritical.getId())) criteria.andIdEqualTo(subnetSearchCritical.getId());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getTenantId()))
        {
            criteria.andProjectIdEqualTo(subnetSearchCritical.getTenantId());
        }
        else if (StringUtils.isNotEmpty(subnetSearchCritical.getProjectId()))
        {
            criteria.andProjectIdEqualTo(subnetSearchCritical.getProjectId());
        }
        if (StringUtils.isNotEmpty(subnetSearchCritical.getName())) criteria.andNameEqualTo(subnetSearchCritical.getName());
        if (subnetSearchCritical.getEnableDhcp() != null) criteria.andEnableDhcpEqualTo(BoolUtil.bool2Short(subnetSearchCritical.getEnableDhcp()));
        if (StringUtils.isNotEmpty(subnetSearchCritical.getNetworkId())) criteria.andNetworkIdEqualTo(subnetSearchCritical.getNetworkId());
        if (subnetSearchCritical.getIpVersion() != null) criteria.andIpVersionEqualTo(subnetSearchCritical.getIpVersion());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getGatewayIp())) criteria.andGatewayIpEqualTo(subnetSearchCritical.getGatewayIp());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getCidr())) criteria.andCidrEqualTo(subnetSearchCritical.getCidr());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getDescription())) criteria.andDescriptionEqualTo(subnetSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getIpv6AddressMode())) criteria.andIpv6AddressModeEqualTo(subnetSearchCritical.getIpv6AddressMode());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getIpv6RaMode())) criteria.andIpv6RaModeEqualTo(subnetSearchCritical.getIpv6RaMode());
        if (subnetSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(subnetSearchCritical.getRevisionNumber().longValue());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getSegmentId())) criteria.andSegmentIdEqualTo(subnetSearchCritical.getSegmentId());
        if (StringUtils.isNotEmpty(subnetSearchCritical.getSortKey()) && StringUtils.isNotEmpty(subnetSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", subnetSearchCritical.getSortKey(), subnetSearchCritical.getSortDir()));
        }
        else
        {
            example.setOrderByClause("created_at desc");
        }
        if (StringUtils.isNotEmpty(subnetSearchCritical.getSubnetpoolId())) criteria.andSubnetpoolIdEqualTo(subnetSearchCritical.getSubnetpoolId());

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    private List<TblCmpOsIpallocationpools> getTblCmpOsIpallocationpools(String cloudId, String subnetId) throws WebSystemException
    {
        TblCmpOsIpallocationpoolsExample example = new TblCmpOsIpallocationpoolsExample();
        TblCmpOsIpallocationpoolsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSubnetIdEqualTo(subnetId);

        List<TblCmpOsIpallocationpools> ipallocationpools = osNeutronRepository.getIpallocationpools(example);

        return ipallocationpools;
    }

    private List<TblCmpOsSubnetroutes> getTblCmpOsSubnetroutes(String cloudId, String subnetId) throws WebSystemException
    {
        TblCmpOsSubnetroutesExample example = new TblCmpOsSubnetroutesExample();
        TblCmpOsSubnetroutesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSubnetIdEqualTo(subnetId);

        List<TblCmpOsSubnetroutes> subnetroutes = osNeutronRepository.getSubnetroutes(example);

        return subnetroutes;
    }

    private List<TblCmpOsDnsnameservers> getTblCmpOsDnsnameservers(String cloudId, String subnetId) throws WebSystemException
    {
        TblCmpOsDnsnameserversExample example = new TblCmpOsDnsnameserversExample();
        TblCmpOsDnsnameserversExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSubnetIdEqualTo(subnetId);

        List<TblCmpOsDnsnameservers> subnetroutes = osNeutronRepository.getDnsnameservers(example);

        return subnetroutes;
    }

    private List<TblCmpOsSubnetServiceTypes> getTblCmpOsSubnetServiceTypes(String cloudId, String subnetId) throws WebSystemException
    {
        TblCmpOsSubnetServiceTypesExample example = new TblCmpOsSubnetServiceTypesExample();
        TblCmpOsSubnetServiceTypesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andSubnetIdEqualTo(subnetId);

        List<TblCmpOsSubnetServiceTypes> subnetServiceTypes = osNeutronRepository.getSubnetServiceTypes(example);

        return subnetServiceTypes;
    }

    public OSExtSubnetsWithDetailsRsp getSubnetsPage(String cloudId, OSSubnetSearchCritical subnetSearchCritical, String userId) throws WebSystemException
    {
        OSExtSubnetsWithDetailsRsp getSubnetsRsp = new OSExtSubnetsWithDetailsRsp();

        PageHelper.startPage(subnetSearchCritical.getPageNum(), subnetSearchCritical.getPageSize());
        List<TblCmpOsSubnets> subnets = osNeutronRepository.getSubnetsWithShared(getTblCmpOsSubnetsExample(cloudId, subnetSearchCritical, userId), cloudId, OSClientUtil.getOSProject(cloudId), userId);
        PageInfo<TblCmpOsSubnets> pageInfo = new PageInfo<>(subnets);


        if (null == subnets)
        {
            return getSubnetsRsp;
        }

        List<OSSubnetInfo> subnetInfos = subnets.stream().map(tblCmpOsSubnet -> {
            OSSubnetInfo subnetInfo = new OSSubnetInfo();
            subnetInfo.setSubnetInfo(tblCmpOsSubnet);
            subnetInfo.setIpallocationpools(getTblCmpOsIpallocationpools(cloudId, subnetInfo.getId()));
            subnetInfo.setSubnetroutes(getTblCmpOsSubnetroutes(cloudId, subnetInfo.getId()));
            subnetInfo.setDnsnameservers(getTblCmpOsDnsnameservers(cloudId, subnetInfo.getId()));
            subnetInfo.setSubnetServiceTypes(getTblCmpOsSubnetServiceTypes(cloudId, subnetInfo.getId()));
            return subnetInfo;
        }).collect(Collectors.toList());

        subnetInfos.forEach(subnetInfo -> cloudService.syncSingleData(cloudId, subnetInfo.getId(), SyncMsg.OS_SUBNET));

        getSubnetsRsp.setSubnets(subnetInfos);
        getSubnetsRsp.setTotalNum(pageInfo.getTotal());
        return getSubnetsRsp;
    }
}
