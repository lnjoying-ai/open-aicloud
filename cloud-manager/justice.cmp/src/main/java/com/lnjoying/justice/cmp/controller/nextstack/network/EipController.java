package com.lnjoying.justice.cmp.controller.nextstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.common.DataSyncLevel;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.config.DescriptionConfig;
import com.lnjoying.justice.cmp.db.model.TblCmpEipKey;
import com.lnjoying.justice.cmp.db.model.TblCmpPortKey;
import com.lnjoying.justice.cmp.db.model.TblCmpVpcKey;
import com.lnjoying.justice.cmp.db.model.search.EipSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.BaseRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.EipInfoVo;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpNextStackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.network.NetworkService;
import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.LNJoyAuditLog;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceIdExtractConfiguration;
import com.lnjoying.justice.commonweb.handler.aspect.enums.ResourceIdLocationType;
import com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "eip")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/network/v1")
@Api(value = "Eip Controller",tags = "Eip Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
                properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_eip"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_eips"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "EIP"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEip")})})
public class EipController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    private static final String REG_UUID = "[0-9a-f]{32}";

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CloudService cloudService;

    @GetMapping(value = "/eips", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get eips", response = Object.class, notes = DescriptionConfig.GET_EIPS)
    public ResponseEntity<Object> getEips(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                          @ApiParam(name = "oneToOne") @RequestParam(required = false) Boolean oneTOne,
                                          @ApiParam(name = "page_size") @RequestParam(required = false, value = "page_size") Integer pageSize,
                                          @ApiParam(name = "page_num") @RequestParam(required = false, value = "page_num") Integer pageNum,
                                          @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                          @ApiParam(name = "eip_pool_id") @RequestParam(required = false, value = "eip_pool_id") String eipPoolId,
                                          @ApiParam(name = "vpc_id") @RequestParam(required = false, value = "vpc_id") String vpcId,
                                          @ApiParam(name = "bound_type") @RequestParam(required = false, value = "bound_type") String boundType,
                                          @ApiParam(name = "name") @RequestParam(required = false, value = "name") String ipAddress,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get eip list, cloud:{}, pageSize:{}, pageNum:{}", cloudId, pageSize, pageNum);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = userId;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            EipSearchCritical pageSearchCritical = new EipSearchCritical();

            if (ipAddress != null) pageSearchCritical.setIpAddress(ipAddress.trim());
            if (pageNum != null) pageSearchCritical.setPageNum(pageNum);
            if (pageSize != null) pageSearchCritical.setPageSize(pageSize);
            if (eipPoolId != null) pageSearchCritical.setEipPoolId(eipPoolId.trim());
            if (vpcId != null) pageSearchCritical.setVpcId(vpcId);
            if (boundType != null) pageSearchCritical.setBoundType(boundType);

            return ResponseEntity.ok().body(networkService.getEips(cloudId, pageSearchCritical, filterUserId, oneTOne));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/eips/{eipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get eip detail info", response = EipInfoVo.class, notes = DescriptionConfig.GET_EIP)
    public ResponseEntity<Object> getEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                         @ApiParam(value = "eipId", required = true, name = "eipId") @PathVariable("eipId") @Pattern(regexp = REG_UUID) String eipId,
                                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("get eip detail info, cloud:{}, eipId:{}", cloudId, eipId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok().body(networkService.getEip(cloudId, eipId, operUserId));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/eips", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create eip", response = BaseRsp.class, notes = DescriptionConfig.POST_EIP)
    @LNJoyAuditLog(value = "create eip",
            resourceMapperId = CmpResources.CLOUD,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "cloudId"))
    public ResponseEntity<Object> postEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                          @ApiParam(value = "CreatEipReq", required = true, name = "CreatEipReq") @RequestBody @Valid EipCreateReqVo req,
                                          @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                          @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("create eip, cloud:{}, request{}", cloudId, req);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.createEip(cloudId, req, bpId, userId);
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

    @DeleteMapping(value = "/eips/{eipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove eip", response = BaseRsp.class, notes = DescriptionConfig.DEL_EIP)
    @LNJoyAuditLog(value = "remove eip",
            resourceMapperId = CmpResources.NS_EIP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "eipId", resourcePrimaryKeyClass = TblCmpEipKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> delEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                         @ApiParam(value = "eipId", required = true, name = "eipId") @PathVariable("eipId") @Pattern(regexp = REG_UUID) String eipId,
                         @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("remove eip, cloud:{}, eipId: {}", cloudId, eipId);

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return networkService.removeEip(cloudId, eipId, operUserId);
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

    @PostMapping(value = "/eips/allocate/{vpcId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "allocate eip to user", response = EipInfoVo.class)
    @LNJoyAuditLog(value = "allocate eip to user",
            resourceMapperId = CmpResources.NS_VPC,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ALLOCATE_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "vpcId", resourcePrimaryKeyClass = TblCmpVpcKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpVpcKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> allocateEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                              @ApiParam(value = "vpcId", required = true, name = "vpcId") @PathVariable("vpcId") @Pattern(regexp = REG_UUID) String vpcId,
//                                              @ApiParam(value = "EipApplyReq", required = true, name = "EipApplyReq") @RequestBody @Valid EipApplyReq req,
                                              @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                              @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("allocate eip to user: {}", userId);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            EipInfoVo eip = networkService.applyEip(cloudId, vpcId, null, bpId, userId);
            return ResponseEntity.ok(eip);
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

    @GetMapping(value = "/eips/{eipId}/ports",produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get tcp or udp ports", response = BaseRsp.class, notes = DescriptionConfig.DEL_EIP)
    public ResponseEntity<Object> getProtocolPorts(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                   @ApiParam(value = "eipId", required = true, name = "eipId") @PathVariable("eipId") @Pattern(regexp = REG_UUID) String eipId,
                                                   @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                   @ApiParam(name = "protocol") @RequestParam(required = true) short protocol,
                                                   @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("get ports, cloud:{}, eipId:{}", cloudId, eipId);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = userId;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            return ResponseEntity.ok().body(networkService.getEipProtocolPorts(cloudId, filterUserId, eipId, protocol));
        }
        catch (Exception e)
        {
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/eips/{eipId}/attach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "attach eip", response = BaseRsp.class, notes = DescriptionConfig.ATTACH_EIP)
    @LNJoyAuditLog(value = "attach eip",
            resourceMapperId = CmpResources.NS_EIP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ATTACH_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "eipId", resourcePrimaryKeyClass = TblCmpEipKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> attachEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "eipId", required = true, name = "eipId") @PathVariable("eipId") String eipId,
                                            @ApiParam(value = "EipAttachReq", required = true, name = "EipAttachReq") @RequestBody @Valid EipAttachReq req,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                networkService.checkEipUser(cloudId, eipId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                cloudService.syncSingleData(cloudId, eipId, SyncMsg.NS_EIP, DataSyncLevel.LEVEL_6);
            }
            return response;
        }
        catch (Exception e)
        {
            LOGGER.error("attach eip failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/eips/{eipId}/detach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach eip", response = BaseRsp.class, notes = DescriptionConfig.DETACH_EIP)
    @LNJoyAuditLog(value = "detach eip",
            resourceMapperId = CmpResources.NS_EIP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DETACH_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "eipId", resourcePrimaryKeyClass = TblCmpEipKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> detachEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                            @ApiParam(value = "eipId", required = true, name = "eipId") @PathVariable("eipId") String eipId,
                                            @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
                networkService.checkEipUser(cloudId, eipId, userId);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.OK)
            {
                cloudService.syncSingleData(cloudId, eipId, SyncMsg.NS_EIP, DataSyncLevel.LEVEL_6);
            }
            return response;
        }
        catch (Exception e)
        {
            LOGGER.error("attach eip failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/ports/{portId}/detach", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "detach eip", response = BaseRsp.class, notes = DescriptionConfig.DETACH_EIP)
    @LNJoyAuditLog(value = "detach eip",
            resourceMapperId = CmpResources.NS_PORT,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DETACH_EIP_PORT,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "portId", resourcePrimaryKeyClass = TblCmpPortKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpPortKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> detachEipFromPort(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                    @ApiParam(value = "portId", required = true, name = "portId") @PathVariable("portId") String portId,
                                                    @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("attach eip failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "/eips/apply", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "apply eip", response = EipInfoVo.class)
    @LNJoyAuditLog(value = "apply eip to user",
            resourceMapperId = CmpResources.NS_EIP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.APPLY_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.getEipId()", resourcePrimaryKeyClass = TblCmpEipKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> applyEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                           @ApiParam(value = "EipApplyReq", required = true, name = "EipApplyReq") @RequestBody @Valid EipApplyReq req,
                                           @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                           @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId) throws WebSystemException
    {
        try
        {
            LOGGER.debug("allocate eip to user: {}", ServiceCombRequestUtils.getUserId());

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            EipInfoVo eip = networkService.applyEip(cloudId, null, req, bpId, userId);
            return ResponseEntity.ok(eip);
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

    @PostMapping(value = "/eips/{eipId}/release", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "release eip", response = BaseRsp.class, notes = DescriptionConfig.ATTACH_EIP)
    @LNJoyAuditLog(value = "release eip",
            resourceMapperId = CmpResources.NS_EIP,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.RELEASE_EIP,
            associatedResourceCnName = "EIP",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "eipId", resourcePrimaryKeyClass = TblCmpEipKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> releaseEip(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "eipId", required = true, name = "eipId") @PathVariable("eipId") String eipId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {

            String operUserId = userId;
            if (isAdmin() || cloudService.isOwner(cloudId, userId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            networkService.releaseEip(cloudId, eipId, operUserId);
            return ResponseEntity.ok(eipId);
        }
        catch (Exception e)
        {
            LOGGER.error("attach eip failed: {}", e.getMessage());
            throw throwWebException(e);
        }
    }
}
