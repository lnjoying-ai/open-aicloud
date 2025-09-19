package com.lnjoying.justice.cmp.controller.openstack.compute;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairsUniqueKey;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSKeypairCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSKeyPairWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSKeyPairsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSKeyPairSearchCritical;
import com.lnjoying.justice.cmp.handler.actiondescription.i18n.zh_cn.CmpOpenstackActionDescriptionTemplate;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.openstack.KeypairService;
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

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@RestSchema(schemaId = "oskeypair")
@RequestMapping("/cmp/v1/{vendor}/clouds/{cloud_id}/v2.1/os-keypairs")
@Api(value = "OpenStack Keypair Controller",tags = "OpenStack Keypair Controller")
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "os_keypair"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "os_keypairs"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "OpenStack SSH密钥对"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "TblCmpOsKeyPairs")})})
public class OSKeypairController extends RestWebController
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @Autowired
    private KeypairService keypairService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List Keypairs", response = Object.class)
    public ResponseEntity<OSKeyPairsRsp> getKeypairs(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                     @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                     @ApiParam(name = "limit") @RequestParam(required = false, value = "limit") Integer limit,
                                                     @ApiParam(name = "marker") @RequestParam(required = false, value = "marker") String marker,
                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.info("get keypairs. cloud:{}", cloudId);

            String filterUserId = operUserId;
            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                filterUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSKeyPairSearchCritical keyPairSearchCritical = new OSKeyPairSearchCritical(userId, limit, marker, null, null, null);

            OSKeyPairsRsp keyPairsRsp = keypairService.getKeypairs(cloudId, keyPairSearchCritical, filterUserId);

            return ResponseEntity.ok(keyPairsRsp);
        }
        catch (Exception e)
        {
            LOGGER.error("get keypairs failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Import (or create) Keypair", response = Object.class)
    @LNJoyAuditLog(value = "Import (or create) Keypair",
            resourceMapperId = CmpResources.OS_KEYPAIR,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.CREATE_KEY_PAIR,
            associatedResourceCnName = "备份",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.METHOD_RETURN_VALUE,
                    resourceIdParseSPEL = "#obj?.body.get('keypair')?.get('name')?.toString()", resourcePrimaryKeyClass = TblCmpOsKeyPairsUniqueKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairsUniqueKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> addKeypair(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                        @ApiParam(value = "keypair", required = true, name = "keypair") @RequestBody OSKeypairCreateReq request,
                                                        @RequestHeader(name = UserHeadInfo.BPID, required = false) String bpId,
                                                        @RequestHeader(name = UserHeadInfo.USERID, required = false) String userId)
    {
        try
        {
            LOGGER.info("add keypair, cloud:{}, request:{}", cloudId, request);

            if (! isAdmin() && ! cloudService.isOwner(cloudId, userId))
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return keypairService.addKeypair(cloudId, request, bpId, userId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add keypair failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @GetMapping(value = "/{keypair_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Show Keypair Details", response = Object.class)
    public ResponseEntity<OSKeyPairWithDetailsRsp> getKeypairDetails(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                                     @ApiParam(value = "keypair_name", required = true, name = "keypair_name") @PathVariable("keypair_name") String keypairName,
                                                                     @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                                     @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.info("get keypair info. cloud:{}, keypair_name:{}", cloudId, keypairName);

            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, true);
            }

            OSKeyPairWithDetailsRsp keyPairWithDetails = keypairService.getKeypairDetails(cloudId, keypairName, operUserId);

            return ResponseEntity.ok(keyPairWithDetails);
        }
        catch (Exception e)
        {
            LOGGER.error("get keypair failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }

    @DeleteMapping(value = "/{keypair_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete Keypair", response = Object.class)
    @LNJoyAuditLog(value = "Delete Keypair",
            resourceMapperId = CmpResources.OS_KEYPAIR,
            actionDescriptionTemplate = CmpOpenstackActionDescriptionTemplate.Descriptions.DELETE_KEY_PAIR,
            associatedResourceCnName = "备份",
            resourceIdExtractConfig = @ResourceIdExtractConfiguration(resourceIdLocation = ResourceIdLocationType.URL_PATH,
                    bindParameterName = "keypairName", resourcePrimaryKeyClass = TblCmpOsKeyPairsUniqueKey.class,
                    convertToResourcePrimaryKeyTypeSpEl = "new com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairsUniqueKey(#RESOURCE_POOL_ID,#RESOURCE_INSTANCE_ID)"))
    public ResponseEntity<Object> removeKeypair(@ApiParam(value = "", required = true, name = "cloud_id") @PathVariable(value = "cloud_id") String cloudId,
                                                @ApiParam(value = "keypair_name", required = true, name = "keypair_name") @PathVariable("keypair_name")String keypairName,
                                                @ApiParam(name = "user_id") @RequestParam(required = false, value = "user_id") String userId,
                                                @RequestHeader(name = UserHeadInfo.USERID, required = false) String operUserId)
    {
        try
        {
            LOGGER.info("remove keypair, cloud:{}, keypair_name:{}", cloudId, keypairName);

            if (isAdmin() || cloudService.isOwner(cloudId, operUserId))
            {
                operUserId = null;
            }
            else
            {
                cloudService.checkCloudStatus(cloudId, false);
            }

            return keypairService.removeKeypair(cloudId, keypairName, operUserId);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove keypair failed: {}", e.getMessage());
            e.printStackTrace();
            throw throwWebException(e);
        }
    }
}
