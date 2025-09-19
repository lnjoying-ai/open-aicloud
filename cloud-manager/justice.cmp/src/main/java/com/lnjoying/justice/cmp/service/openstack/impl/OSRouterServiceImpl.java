package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSExtRouterUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSRouterCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSRouterUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSRouterSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.RouterService;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSRouterServiceImpl implements RouterService
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

    public OSRoutersWithDetailsRsp getRouters(String cloudId, OSRouterSearchCritical routerSearchCritical, String userId) throws WebSystemException
    {
        OSRoutersWithDetailsRsp routersWithDetailsRsp = new OSRoutersWithDetailsRsp();
        List<TblCmpOsRouters> routers = osNeutronRepository.getRouters(getTblCmpOsRoutersExample(cloudId, routerSearchCritical, userId));
        if (null == routers) {
            return routersWithDetailsRsp;
        }

        List<OSRouterInfo> routerInfos = routers.stream().map(tblCmpOsRouters -> {
            OSRouterInfo routerInfo = new OSRouterInfo();
            routerInfo.setRouterInfo(tblCmpOsRouters);
            routerInfo.setFixedIp(getIpallocations(cloudId, tblCmpOsRouters.getId()));
            routerInfo.setRouterroute(getTblCmpOsRouterroutes(cloudId, tblCmpOsRouters.getId()));
            return routerInfo;
        }).collect(Collectors.toList());

        routerInfos.forEach(routerInfo -> cloudService.syncSingleData(cloudId, routerInfo.getId(), SyncMsg.OS_ROUTER));

        routersWithDetailsRsp.setRouters(routerInfos);
        return routersWithDetailsRsp;
    }

    public ResponseEntity<Object> addRouter(String cloudId, OSRouterCreateReq addRouterReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            addRouterReq.getRouter().setTenantId(OSClientUtil.getOSProject(osClientV3));
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(addRouterReq);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSRouterWithDetailsRsp osRouterWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSRouterWithDetailsRsp.class);
                if (osRouterWithDetailsRsp == null || osRouterWithDetailsRsp.getRouter() == null)
                {
                    return response;
                }
                eskSyncDataService.updateRouterToDB(cloudId, osRouterWithDetailsRsp.getRouter(), bpId, userId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add router failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public OSRouterWithDetailsRsp getRouterDetails(String cloudId, String routerId, String fields, String userId) throws WebSystemException
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
        OSRouterWithDetailsRsp osRouterWithDetailsRsp = new OSRouterWithDetailsRsp();
        OSRouterInfo routerInfo = new OSRouterInfo();
        routerInfo.setRouterInfo(tblCmpOsRouters);
        routerInfo.setFixedIp(getIpallocations(cloudId, tblCmpOsRouters.getId()));
        routerInfo.setRouterroute(getTblCmpOsRouterroutes(cloudId, tblCmpOsRouters.getId()));
        osRouterWithDetailsRsp.setRouter(routerInfo);

        cloudService.syncSingleData(cloudId, routerId, SyncMsg.OS_ROUTER);

        return osRouterWithDetailsRsp;
    }

    public ResponseEntity<Object> updateRouter(String cloudId, String routerId, OSRouterUpdateReq request, String bpId, String userId) throws WebSystemException
    {
        try
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

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud(request);
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSRouterWithDetailsRsp osRouterWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSRouterWithDetailsRsp.class);
                if (osRouterWithDetailsRsp == null || osRouterWithDetailsRsp.getRouter() == null)
                {
                    return response;
                }
                eskSyncDataService.updateRouterToDB(cloudId, osRouterWithDetailsRsp.getRouter(), null, null);

                cloudService.syncSingleData(cloudId, routerId, SyncMsg.OS_ROUTER);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update router failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity<Object> removeRouter(String cloudId, String routerId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpOsRouters tblCmpOsRouter = osNeutronRepository.getRouterById(cloudId, routerId);
            if (tblCmpOsRouter == null)
            {
                LOGGER.error("get router null: router id {}", routerId);
                throw new WebSystemException(ErrorCode.ROUTER_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpOsRouter.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response = osClientV3.sendHttpRequestToCloud();
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpOsRouter.setEeStatus(REMOVED);
                osNeutronRepository.updateRouter(tblCmpOsRouter);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove router failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

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

    public OSExtRoutersWithDetailsRsp getRoutersPage(String cloudId, OSRouterSearchCritical routerSearchCritical, String userId)
    {
        OSExtRoutersWithDetailsRsp routersWithDetailsRsp = new OSExtRoutersWithDetailsRsp();

        PageHelper.startPage(routerSearchCritical.getPageNum(), routerSearchCritical.getPageSize());
        List<TblCmpOsRouters> routers = osNeutronRepository.getRouters(getTblCmpOsRoutersExample(cloudId, routerSearchCritical, userId));
        PageInfo<TblCmpOsRouters> pageInfo = new PageInfo<>(routers);

        if (null == routers) {
            return routersWithDetailsRsp;
        }

        List<OSExtRouterInfo> routerInfos = routers.stream().map(tblCmpOsRouters -> {
            OSExtRouterInfo routerInfo = new OSExtRouterInfo();
            routerInfo.setRouterInfo(tblCmpOsRouters);
            routerInfo.setFixedIp(getIpallocations(cloudId, tblCmpOsRouters.getId()));
            routerInfo.setRouterroute(getTblCmpOsRouterroutes(cloudId, tblCmpOsRouters.getId()));
            if (routerInfo.getExternalGatewayInfo() != null && StringUtils.isNotEmpty(routerInfo.getExternalGatewayInfo().getNetworkId()))
            {
                routerInfo.setNetwork(getTblCmpOsNetworks(cloudId, routerInfo.getExternalGatewayInfo().getNetworkId()));
            }
            return routerInfo;
        }).collect(Collectors.toList());

        routerInfos.forEach(routerInfo -> cloudService.syncSingleData(cloudId, routerInfo.getId(), SyncMsg.OS_ROUTER));

        routersWithDetailsRsp.setRouters(routerInfos);
        routersWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return routersWithDetailsRsp;
    }

    private TblCmpOsNetworks getTblCmpOsNetworks(String cloudId, String networkId) throws WebSystemException
    {
        return osNeutronRepository.getNetworkById(cloudId, networkId);
    }

    public OSExtRouterWithDetailsRsp getExtRouterDetails(String cloudId, String routerId, String fields, String userId) throws WebSystemException
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
        OSExtRouterWithDetailsRsp osRouterWithDetailsRsp = new OSExtRouterWithDetailsRsp();
        OSExtRouterInfo routerInfo = new OSExtRouterInfo();
        routerInfo.setRouterInfo(tblCmpOsRouters);
        routerInfo.setFixedIp(getIpallocations(cloudId, tblCmpOsRouters.getId()));
        routerInfo.setRouterroute(getTblCmpOsRouterroutes(cloudId, tblCmpOsRouters.getId()));
        if (routerInfo.getExternalGatewayInfo() != null && StringUtils.isNotEmpty(routerInfo.getExternalGatewayInfo().getNetworkId()))
        {
            routerInfo.setNetwork(getTblCmpOsNetworks(cloudId, routerInfo.getExternalGatewayInfo().getNetworkId()));
        }
        osRouterWithDetailsRsp.setRouter(routerInfo);

        cloudService.syncSingleData(cloudId, routerId, SyncMsg.OS_ROUTER);

        return osRouterWithDetailsRsp;
    }

    public ResponseEntity<Object> updateExtRouter(String cloudId, String routerId, OSExtRouterUpdateReq request, String bpId, String userId) throws WebSystemException
    {
        try
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

            TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity response;
            if (request != null && request.getRouter() != null && request.getRouter().getCloseExternalGateway() != null && request.getRouter().getCloseExternalGateway())
            {
                String closeExternalGatewayBody = "{\"router\":{\"external_gateway_info\":null}}";
                response = osClientV3.sendHttpRequestToCloudWithoutExt(closeExternalGatewayBody);
            }
            else
            {
                response = osClientV3.sendHttpRequestToCloudWithoutExt(request);
            }
            if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                OSRouterWithDetailsRsp osRouterWithDetailsRsp = gson.fromJson(gson.toJson(response.getBody()), OSRouterWithDetailsRsp.class);
                if (osRouterWithDetailsRsp == null || osRouterWithDetailsRsp.getRouter() == null)
                {
                    return response;
                }
                eskSyncDataService.updateRouterToDB(cloudId, osRouterWithDetailsRsp.getRouter(), null, null);

                cloudService.syncSingleData(cloudId, routerId, SyncMsg.OS_ROUTER);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update router failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkRouterUser(String cloudId, String routerId, String userId)
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
    }
}
