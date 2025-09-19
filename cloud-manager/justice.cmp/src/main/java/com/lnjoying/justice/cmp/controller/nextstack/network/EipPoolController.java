package com.lnjoying.justice.cmp.controller.nextstack.network;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpEipPoolKey;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.EipPoolCreateReqVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.*;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;

@RestSchema(schemaId = "eippool")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/api/network/v1")
@Api(value = "EipPool Controller",tags = "EipPool Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
                properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "ns_eip_pool"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "ns_eip_pools"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "EIP Pool"),
                        @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpEipPool")})})
public class EipPoolController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NetworkService networkService;

    @Autowired
    private CloudService cloudService;

    @PostMapping(value = "/eip_pools", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create eip pool", response = Object.class)
    @LNJoyAuditLog(value = "create eip pool",
            resourceMapperId = CmpResources.NS_EIP_POOL,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.ADD_EIP_POOL,
            associatedResourceCnName = "EIP Pool",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body?.get('eipPoolId')?.toString()", resourcePrimaryKeyClass = TblCmpEipPoolKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipPoolKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addEipPool(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                             @ApiParam(value = "CreateEipPoolReq", required = true, name = "CreateEipPoolReq") @RequestBody @Valid EipPoolCreateReqVo request,
                                             @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("create eip pool, cloud:{}, request:{}", cloudId, request);
            return networkService.createEipPool(cloudId, request, bpId, userId);
        }
        catch (Exception e)
        {
            LOGGER.error("create eip pool failed: {}", e.getMessage());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PutMapping(value = "/eip_pools/{eipPoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update eip pool", response = Object.class)
    @LNJoyAuditLog(value = "update eip pool",
            resourceMapperId = CmpResources.NS_EIP_POOL,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.UPDATE_EIP_POOL,
            associatedResourceCnName = "EIP Pool",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "eipPoolId", resourcePrimaryKeyClass = TblCmpEipPoolKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipPoolKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> updateEipPool(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "eipPoolId", required = true, name = "eipPoolId") @PathVariable("eipPoolId") String eipPoolId,
                                                @ApiParam(value = "CommonReq", required = true, name = "CommonReq") @RequestBody @Valid EipPoolCreateReqVo request)
    {
        try
        {
            LOGGER.info("update eip pool: eipPoolId:{} name:{}", eipPoolId, request);

            return cloudService.sendHttpRequestToCloud(cloudId);
        }
        catch (Exception e)
        {
            LOGGER.error("update  eip pool failed: {}", e.getMessage());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/eip_pools/{eipPoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "remove eip pool", response = Object.class)
    @LNJoyAuditLog(value = "remove eip pool",
            resourceMapperId = CmpResources.NS_EIP_POOL,
            actionDescriptionTemplate = CmpNextStackActionDescriptionTemplate.Descriptions.DELETE_EIP_POOL,
            associatedResourceCnName = "EIP Pool",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "eipPoolId", resourcePrimaryKeyClass = TblCmpEipPoolKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpEipPoolKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeEipPool(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "eipPoolId", required = true, name = "eipPoolId") @PathVariable("eipPoolId")String eipPoolId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("remove eip pool, cloud:{}, eipPoolId:{}", cloudId, eipPoolId);

            return networkService.removeEipPool(cloudId, eipPoolId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove  eip pool failed: {}", e.getMessage());

            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/eip_pools/{eipPoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get eip pool info", response = Object.class)
    public ResponseEntity<EipPoolDetailInfoRspVo> getEipPool(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                             @ApiParam(value = "eipPoolId", required = true, name = "eipPoolId") @PathVariable("eipPoolId") String eipPoolId,
                                                             @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get eip pool info, cloud:{}, eipPoolId:{}", cloudId, eipPoolId);

            EipPoolDetailInfoRspVo getEipPoolDetailInfoRsp = networkService.getEipPool(cloudId, eipPoolId);

            return ResponseEntity.ok(getEipPoolDetailInfoRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get eip pool failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/eip_pools", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "get eip pools", response = Object.class)
    public ResponseEntity<EipPoolsRspVo> getEipPools(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @ApiParam(name = "name") @RequestParam(required = false) String name,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("get eip pools, cloud:{}", cloudId);

            EipPoolsRspVo getEipPoolsRsp = networkService.getEipPools(cloudId, name, null);
            return ResponseEntity.ok(getEipPoolsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get eip pool failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
