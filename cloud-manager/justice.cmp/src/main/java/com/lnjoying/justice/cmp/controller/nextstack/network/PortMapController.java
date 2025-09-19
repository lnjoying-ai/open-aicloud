package com.lnjoying.justice.cmp.controller.nextstack.network;

import com.alibaba.nacos.common.utils.StringUtils;
import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpEipMapKey;
import com.lnjoying.justice.cmp.db.model.search.EipPortMapSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.BaseRsp;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.network.NetworkService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "portmap")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/network/v1")
@Api(value = "PortMap Controller",tags = "PortMap Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
                properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_eip_map"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_eip_maps"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "Nat网关"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpPortMap")})})
public class PortMapController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "[0-9a-f]{32}";

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/portMaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get portMaps", response = Object.class, notes = DescriptionConfig.GET_PORT_MAPS)
    public Object getPortMaps(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                              @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                              @ApiParam(name = "name") @RequestParam(required = false) String name,
                              @ApiParam(name = "eip_id") @RequestParam(required = false) String eipId,
                              @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                              @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get portMaps list, cloud:{}, userId:{}, pageSize:{}, pageNum:{}", cloudId, userId, pageSize, pageNum);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = userId;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            EipPortMapSearchCritical pageSearchCritical = new EipPortMapSearchCritical();
            if (null != pageNum ) pageSearchCritical.setPageNum(pageNum);
            if (null != pageSize) pageSearchCritical.setPageSize(pageSize);
            if (StringUtils.isNotBlank(name)) pageSearchCritical.setName(name);
            if (StringUtils.isNotBlank(eipId)) pageSearchCritical.setEipId(eipId);

            return networkService.getEipPortMaps(cloudId, pageSearchCritical, filterUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/portMaps/{eipMapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get portMaps detail info", response = Object.class, notes = DescriptionConfig.GET_PORT_MAP)
    public Object getPortMap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                             @ApiParam(value = "eipMapId", required = true, name = "eipMapId") @PathVariable("eipMapId") @Pattern(regexp = REG_UUID) String eipMapId,
                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("get eipPortMap info, cloud:{}, eipMapId:{}", cloudId, eipMapId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return networkService.getEipPortMap(cloudId, eipMapId, operUserId);
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/portMaps", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create new portMaps", response = BaseRsp.class, notes = DescriptionConfig.POST_PORT_MAPS)
    @LNJoyAuditLog(value = "create new portMaps",
            resourceMapperId = CmpResources.NS_EIP_MAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_PORT_MAP,
            associatedResourceCnName = "NAT网关",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.REQUEST_BODY,
                    resourceIdParseSPEL = "#obj?.body?.get('natId')?.toString()", resourcePrimaryKeyClass = TblCmpEipMapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipMapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> postPortMaps(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                               @ApiParam(value = "eipPortMapInfo", required = true, name = "eipPortMapInfo") @RequestBody @Valid EipPortMapCreateReqVo eipPortMapInfo,
                                               @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                               @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create eipPortMap, cloud:{}, request:{}", cloudId, eipPortMapInfo);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.createEipPortMap(cloudId, eipPortMapInfo, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/nat-gateways/{eipMapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update nat-gateway")
    @LNJoyAuditLog(value = "update nat-gateway",
            resourceMapperId = CmpResources.NS_EIP_MAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_PORT_MAP_NAME,
            associatedResourceCnName = "NAT网关",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "mapId", resourcePrimaryKeyClass = TblCmpEipMapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipMapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updatePortMapName(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "eipMapId", required = true, name = "eipMapId") @PathVariable("eipMapId") String eipMapId,
                                                    @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid CommonReq request,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update portMap: eipMapId:{} name:{}", eipMapId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                networkService.checkPortMapUser(cloudId, eipMapId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update portMap name failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/nat-gateways/{eipMapId}/ports", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update nat-gateway", response = Object.class)
    @LNJoyAuditLog(value = "update nat-gateway",
            resourceMapperId = CmpResources.NS_EIP_MAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_PORT_MAP,
            associatedResourceCnName = "NAT网关",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "mapId", resourcePrimaryKeyClass = TblCmpEipMapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipMapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updatePortMap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "eipMapId", required = true, name = "eipMapId") @PathVariable("eipMapId") String eipMapId,
                                                @ApiParam(value = "EipPortMapUpdateReqVo", required = true, name = "EipPortMapUpdateReqVo") @RequestBody @Valid EipPortMapUpdateReqVo eipPortMapInfo,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("update portMap: eipMapId:{} eipPortMap:{}", eipMapId, eipPortMapInfo);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                networkService.checkPortMapUser(cloudId, eipMapId, userId);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update portMap failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/portMaps/{mapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "delete portMap", response = BaseRsp.class, notes = DescriptionConfig.DEL_PORT_MAP)
    @LNJoyAuditLog(value = "delete portMap",
            resourceMapperId = CmpResources.NS_EIP_MAP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_PORT_MAP,
            associatedResourceCnName = "NAT网关",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "mapId", resourcePrimaryKeyClass = TblCmpEipMapKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipMapKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> delPortMap(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "mapId", required = true, name = "mapId") @PathVariable("mapId") @Pattern(regexp = REG_UUID) String mapId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("remove eipPortMap, cloud:{}, mapId: {}", cloudId, mapId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.removeEipPortMap(cloudId, mapId, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }
}
